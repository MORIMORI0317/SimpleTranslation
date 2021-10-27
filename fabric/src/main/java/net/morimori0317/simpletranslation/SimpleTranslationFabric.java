package net.morimori0317.simpletranslation;

import net.fabricmc.api.ClientModInitializer;

public class SimpleTranslationFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SimpleTranslation.clientInit();
    }
}
