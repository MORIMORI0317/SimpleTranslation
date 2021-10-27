package net.morimori0317.simpletranslation.fabric.mixin;


import net.minecraft.client.resources.language.ClientLanguage;
import net.minecraft.client.resources.language.LanguageInfo;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.morimori0317.simpletranslation.language.ILangCheckClientLanguage;
import net.morimori0317.simpletranslation.language.UntranslatedLangChecker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(ClientLanguage.class)
public class ClientLanguageMixin implements ILangCheckClientLanguage {

    private List<String> noLocalizedLang = new ArrayList<>();

    @Override
    public List<String> getNoLocalizedLang() {
        return noLocalizedLang;
    }

    @Override
    public void setNoLocalizedLang(List<String> langs) {
        noLocalizedLang = langs;
    }

    @Inject(method = "loadFrom", at = @At("HEAD"))
    private static void loadFrom(ResourceManager resourceManager, List<LanguageInfo> list, CallbackInfoReturnable<ClientLanguage> cir) {
        UntranslatedLangChecker.loadLang = true;
    }

    @Inject(method = "loadFrom", at = @At("RETURN"))
    private static void loadFrom2(ResourceManager resourceManager, List<LanguageInfo> list, CallbackInfoReturnable<ClientLanguage> cir) {

        List<String> noLocalizedKey = new ArrayList<>();

        List<String> en_us = UntranslatedLangChecker.loadLangList.get("lang/en_us.json");

        if (en_us != null) {
            for (Map.Entry<String, List<String>> entry : UntranslatedLangChecker.loadLangList.entrySet()) {
                if (entry.getKey().equals("lang/en_us.json"))
                    continue;
                for (String str : en_us) {
                    if (!entry.getValue().contains(str))
                        noLocalizedKey.add(str);
                }
            }
        }

        UntranslatedLangChecker.loadList.clear();
        UntranslatedLangChecker.loadList = null;
        UntranslatedLangChecker.loadLangList.clear();
        UntranslatedLangChecker.loadLangList = null;


        UntranslatedLangChecker.loadLang = false;
        ILangCheckClientLanguage language = (ILangCheckClientLanguage) cir.getReturnValue();
        language.setNoLocalizedLang(noLocalizedKey);
    }

    @Inject(method = "appendFrom", at = @At("HEAD"))
    private static void appendFrom(List<Resource> list, Map<String, String> map, CallbackInfo ci) {
        UntranslatedLangChecker.loadList = list;
        UntranslatedLangChecker.loadCont = 0;
        if (UntranslatedLangChecker.loadLangList == null)
            UntranslatedLangChecker.loadLangList = new HashMap<>();
    }
}
