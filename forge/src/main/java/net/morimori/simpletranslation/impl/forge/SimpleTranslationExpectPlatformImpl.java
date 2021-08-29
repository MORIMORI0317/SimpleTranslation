package net.morimori.simpletranslation.impl.forge;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.locale.Language;

public class SimpleTranslationExpectPlatformImpl {
    public static boolean isKeyPress(KeyMapping key) {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), key.getKey().getValue());
    }

    public static Language getLanguage() {
        return I18n.language;
    }
}
