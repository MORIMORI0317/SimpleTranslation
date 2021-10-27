package net.morimori0317.simpletranslation;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = SimpleTranslation.MODID)
public class STConfig implements ConfigData {
    public String targetLang = "";

    public String sourceLang = "";

    public boolean enableToggleMode = false;
}
