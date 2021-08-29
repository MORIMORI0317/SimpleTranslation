package net.morimori.simpletranslation.forge.mixin;

import com.google.gson.JsonElement;
import net.minecraft.locale.Language;
import net.minecraft.server.packs.resources.Resource;
import net.morimori.simpletranslation.language.UntranslatedLangChecker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@Mixin(Language.class)
public class LanguageMixin {
    @ModifyVariable(method = "loadFromJson", ordinal = 0, at = @At("STORE"))
    private static Map.Entry<String, JsonElement> loadFromJson(Map.Entry<String, JsonElement> entry) {
        if (UntranslatedLangChecker.loadLang) {
            List<Resource> loadList = UntranslatedLangChecker.loadList;
            String loadName = loadList != null ? loadList.get(UntranslatedLangChecker.loadCont).getLocation().getPath() : null;
            if (loadName != null) {
                if (!UntranslatedLangChecker.loadLangList.containsKey(loadName))
                    UntranslatedLangChecker.loadLangList.put(loadName, new ArrayList<>());

                UntranslatedLangChecker.loadLangList.get(loadName).add(entry.getKey());
            }
        }
        return entry;
    }

    @Inject(method = "loadFromJson", at = @At("TAIL"))
    private static void loadFromJson(InputStream inputStream, BiConsumer<String, String> biConsumer, CallbackInfo ci) {
        if (UntranslatedLangChecker.loadLang)
            UntranslatedLangChecker.loadCont++;
    }
}
