package net.morimori0317.simpletranslation.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.morimori0317.simpletranslation.SimpleTranslation;

public class SimpleTranslationFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SimpleTranslation.clientInit();
    }
}
