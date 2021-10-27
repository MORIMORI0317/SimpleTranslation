package net.morimori0317.simpletranslation.impl;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.KeyMapping;
import net.minecraft.locale.Language;

public class SimpleTranslationExpectPlatform {
    @ExpectPlatform
    public static boolean isKeyPress(KeyMapping key) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static Language getLanguage(){
        throw new AssertionError();
    }
}
