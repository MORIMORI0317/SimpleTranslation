package net.morimori0317.simpletranslation.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.LanguageInfo;

public class LangUtils {
    private static final Minecraft mc = Minecraft.getInstance();
    private static final String[] googleLangs = {"am", "ar", "eu", "bn", "en-GB", "pt-BR", "bg", "ca", "chr", "hr", "cs", "da", "nl", "en", "et", "fil", "fi", "fr", "de", "el", "gu", "iw", "hi", "hu", "is", "id", "it", "ja", "kn", "ko", "lv", "lt", "ms", "ml", "mr", "no", "pl", "pt-PT", "ro", "ru", "sr", "zh-CN", "sk", "sl", "es", "sw", "sv", "ta", "te", "th", "zh-TW", "tr", "ur", "uk", "vi", "cy"};

    public static String getLangByGoogleCodeName(String googleId) {
        LanguageInfo inf = getLangByGoogleCode(googleId);
        if (inf != null)
            return inf.getName();
        return googleId;
    }

    private static LanguageInfo getLangByGoogleCode(String id) {
        for (LanguageInfo language : mc.getLanguageManager().getLanguages()) {
            if (language.getCode().split("_")[0].equals(id))
                return language;
        }
        return null;
    }

    public static String getGoogleCodeByLang(LanguageInfo language) {
        String name = language.getCode().split("_")[0];
        for (String lang : googleLangs) {
            if (lang.equals(name))
                return lang;
        }
        return null;
    }
}
