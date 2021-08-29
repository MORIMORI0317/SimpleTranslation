package net.morimori.simpletranslation;

import me.shedaniel.clothconfig2.ClothConfigInitializer;
import net.fabricmc.api.ClientModInitializer;

public class SimpleTranslationFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SimpleTranslation.clientInit();
    }
}
