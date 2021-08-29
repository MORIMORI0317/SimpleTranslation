package net.morimori.simpletranslation;

import me.shedaniel.architectury.registry.KeyBindings;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.minecraft.client.KeyMapping;
import net.morimori.simpletranslation.translation.TranslationManager;
import org.lwjgl.glfw.GLFW;

public class SimpleTranslation {
    public static final String MODID = "simpletranslation";
    public static final KeyMapping TRANSLATE_KEY = new KeyMapping("key.simpletranslation.translate", GLFW.GLFW_KEY_G, "key.categories.simpletranslation");
    public static final STConfig CONFIG = AutoConfig.register(STConfig.class, Toml4jConfigSerializer::new).getConfig();

    public static void clientInit() {
        KeyBindings.registerKeyBinding(TRANSLATE_KEY);
        TranslationManager.getInstance().init();
        ClientHandler.init();

    }
}
