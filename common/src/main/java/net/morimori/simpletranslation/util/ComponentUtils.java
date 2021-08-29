package net.morimori.simpletranslation.util;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.morimori.simpletranslation.SimpleTranslation;
import net.morimori.simpletranslation.impl.SimpleTranslationExpectPlatform;
import net.morimori.simpletranslation.language.ILangCheckClientLanguage;

import java.util.ArrayList;
import java.util.List;

public class ComponentUtils {
    private static final Minecraft mc = Minecraft.getInstance();

    public static String getLang(Component text) {

        if (!SimpleTranslation.CONFIG.sourceLang.isEmpty())
            return SimpleTranslation.CONFIG.sourceLang;

        if (text instanceof TranslatableComponent) {
            List<TranslatableComponent> list = getTranslatableComponents((TranslatableComponent) text);
            ILangCheckClientLanguage language = (ILangCheckClientLanguage) SimpleTranslationExpectPlatform.getLanguage();
            if (list.stream().noneMatch(n -> language.getNoLocalizedLang().contains(n.getKey()))) {
                return LangUtils.getGoogleCodeByLang(mc.getLanguageManager().getSelected());
            }
            return "en";
        }
        return "";
    }

    private static List<TranslatableComponent> getTranslatableComponents(TranslatableComponent component) {
        List<TranslatableComponent> list = new ArrayList<>();
        list.add(component);
        for (Component sibling : component.getSiblings()) {
            if (sibling instanceof TranslatableComponent)
                list.addAll(getTranslatableComponents((TranslatableComponent) sibling));
        }
        for (Object arg : component.getArgs()) {
            if (arg instanceof TranslatableComponent)
                list.addAll(getTranslatableComponents((TranslatableComponent) arg));
        }
        return list;
    }
}