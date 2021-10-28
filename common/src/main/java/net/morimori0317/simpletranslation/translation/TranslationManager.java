package net.morimori0317.simpletranslation.translation;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.morimori0317.simpletranslation.SimpleTranslation;
import net.morimori0317.simpletranslation.util.ComponentUtils;
import net.morimori0317.simpletranslation.util.LangUtils;

import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class TranslationManager {
    private static final Minecraft mc = Minecraft.getInstance();
    private static final Gson GSON = new Gson();
    private static final Component PROGRESS_TEXT = new TranslatableComponent("tooltip.simpletranslation.progress").withStyle(ChatFormatting.GRAY);
    private static final Component REJECTION_TEXT = new TranslatableComponent("tooltip.simpletranslation.rejection").withStyle(ChatFormatting.GRAY);
    private static final Component NOREQUIRED_TEXT = new TranslatableComponent("tooltip.simpletranslation.norequired").withStyle(ChatFormatting.GRAY);
    private static final Component AUTODETECTION_TEXT = new TranslatableComponent("tooltip.simpletranslation.autodetection").withStyle(ChatFormatting.GRAY);
    private static final TranslationManager INSTANCE = new TranslationManager();
    private final Map<SourceLangText, TranslationData> CASH = new HashMap<>();
    private final List<String> PROGRESS = new ArrayList<>();
    public final Translator translator = new Translator();

    public static TranslationManager getInstance() {
        return INSTANCE;
    }

    public void init() {
        readCash();
    }

    public String getChatTranslation(Component text, Consumer<String> strlsiner) {
        return toTranslation(text.getString(),"", strlsiner);
    }

    public void readCash() {
        try {
            if (Paths.get("simpletranslation.json").toFile().exists()) {
                JsonObject jo = GSON.fromJson(new FileReader("simpletranslation.json"), JsonObject.class);
                for (Map.Entry<String, JsonElement> lang : jo.entrySet()) {
                    JsonObject je = lang.getValue().getAsJsonObject();
                    for (Map.Entry<String, JsonElement> entry : je.entrySet()) {
                        JsonObject jk = entry.getValue().getAsJsonObject();
                        TranslationData data = new TranslationData();
                        for (Map.Entry<String, JsonElement> langs : jk.entrySet()) {
                            data.addTranslateInfo(langs.getKey(), new TranslationData.TranslationInfo(langs.getValue().getAsString(), null, System.currentTimeMillis()));
                        }
                        CASH.put(new SourceLangText(lang.getKey(), entry.getKey()), data);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeCash() {
        try {
            JsonObject jo = new JsonObject();
            mc.submit(() -> {
                for (String lang : CASH.keySet().stream().map(n -> n.langCode).collect(Collectors.toSet())) {
                    jo.add(lang, new JsonObject());
                }
                CASH.forEach((n, m) -> {
                    JsonObject ja = jo.get(n.langCode).getAsJsonObject();
                    JsonObject jk = new JsonObject();
                    m.getAllTranslateText().forEach((l, k) -> {
                        if (k.getError() == null && k.getText() != null)
                            jk.addProperty(l, k.getText());
                    });
                    ja.add(n.text, jk);
                });
            }).get();
            Files.writeString(Paths.get("simpletranslation.json"), jo.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Component> createToolTip(Component text) {
        String str = text.getString();
        String lang = ComponentUtils.getLang(text);

        if (currentLang().equals(lang))
            return Collections.singletonList(NOREQUIRED_TEXT);

        SourceLangText langText = new SourceLangText(lang, str);

        if (CASH.containsKey(langText) && CASH.get(langText).isTranslated(currentLang())) {
            TranslationData.TranslationInfo info = CASH.get(langText).getTranslateInfo(currentLang());
            if (info.getError() != null)
                return Collections.singletonList(new TranslatableComponent("tooltip.simpletranslation.error", info.getError().getClass().getName()));

            if (str.equals(info.getText()))
                return Collections.singletonList(NOREQUIRED_TEXT);

            if (info.getText() != null) {
                Object targetLang = lang == null || lang.isEmpty() ? AUTODETECTION_TEXT : LangUtils.getLangByGoogleCodeName(lang);

                List<Component> ary = new ArrayList<>();
                ary.add(new TranslatableComponent("tooltip.simpletranslation.result", new TextComponent(info.getText()).setStyle(text.getStyle())).withStyle(ChatFormatting.GRAY));
                ary.add(new TranslatableComponent("tooltip.simpletranslation.lang", targetLang, LangUtils.getLangByGoogleCodeName(currentLang())).withStyle(ChatFormatting.GRAY));

                return ary;
            }

            return Collections.singletonList(REJECTION_TEXT);
        }

        if (PROGRESS.contains(str)) {
            MutableComponent c = PROGRESS_TEXT.copy();
            for (int i = 0; i < (System.currentTimeMillis() % 400) / 100; i++) {
                c.append(".");
            }
            return Collections.singletonList(c);
        }
        PROGRESS.add(str);
        TranslateThread tt = new TranslateThread(str, lang != null ? lang : "", currentLang());
        tt.start();

        return Collections.singletonList(REJECTION_TEXT);
    }

    public String toTranslation(String text, String srcLang, Consumer<String> strlisner) {
        SourceLangText txt = new SourceLangText(srcLang, text);
        if (CASH.containsKey(txt)) {
            if (CASH.get(txt).getTranslateInfo(srcLang).getError() != null)
                return CASH.get(txt).getTranslateInfo(srcLang).getText();
            else
                return null;
        }
        if (PROGRESS.contains(text))
            return null;
        PROGRESS.add(text);
        TranslateThread tt = new TranslateThread(text, srcLang != null ? srcLang : "", currentLang());
        tt.start();
        return null;
    }

    public String currentLang() {
        if (!SimpleTranslation.CONFIG.targetLang.isEmpty())
            return SimpleTranslation.CONFIG.targetLang;

        String lang = LangUtils.getGoogleCodeByLang(mc.getLanguageManager().getSelected());
        return lang != null ? lang : "en";
    }


    private class TranslateThread extends Thread {
        private final String text;
        private final String srcLang;
        private final String langCode;
        private final Consumer<String> strLisner;

        public TranslateThread(String text, String srcLang, String langCode) {
            this(text, srcLang, langCode, null);
        }

        public TranslateThread(String text, String srcLang, String langCode, Consumer<String> strLisner) {
            this.text = text;
            this.srcLang = srcLang;
            this.langCode = langCode;
            this.strLisner = strLisner;
            this.setName("Translate Thread");
        }

        @Override
        public void run() {
            TranslationData data = CASH.get(new SourceLangText(srcLang, text));
            if (data == null)
                data = new TranslationData();

            TranslationData.TranslationInfo info;

            try {
                String str = translator.translate(text, srcLang, langCode);
                info = new TranslationData.TranslationInfo(str, null, System.currentTimeMillis());
            } catch (Exception ex) {
                ex.printStackTrace();
                info = new TranslationData.TranslationInfo(null, ex, System.currentTimeMillis());
            }
            data.addTranslateInfo(langCode, info);

            TranslationData finalData = data;
            try {
                mc.submit(() -> {
                    CASH.put(new SourceLangText(srcLang, text), finalData);
                    if (strLisner != null)
                        strLisner.accept(text);
                }).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            PROGRESS.remove(text);
        }
    }

    public static class SourceLangText {
        private final String langCode;
        private final String text;

        public SourceLangText(String langCode, String text) {
            this.langCode = langCode;
            this.text = text;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SourceLangText that = (SourceLangText) o;
            return Objects.equals(langCode, that.langCode) && Objects.equals(text, that.text);
        }

        @Override
        public int hashCode() {
            return Objects.hash(langCode, text);
        }
    }
}
