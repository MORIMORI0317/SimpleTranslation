package net.morimori0317.simpletranslation;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SimpleTranslation.MODID)
public class SimpleTranslationForge {
    public SimpleTranslationForge() {
        EventBuses.registerModEventBus(SimpleTranslation.MODID, FMLJavaModLoadingContext.get().getModEventBus());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        SimpleTranslation.clientInit();
    }
}