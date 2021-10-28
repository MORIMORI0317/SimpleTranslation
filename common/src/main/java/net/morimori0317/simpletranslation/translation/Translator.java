package net.morimori0317.simpletranslation.translation;

import com.google.gson.JsonObject;
import net.morimori0317.simpletranslation.exception.TranslationException;
import net.morimori0317.simpletranslation.util.URLUtils;

import java.net.URL;

public class Translator {
    private static final String TRANS_URL_V1 = "https://script.google.com/macros/s/AKfycbyw2976cR_fYKtFV9FYM-IxzmhqdLr9pOI8WX7F8KgoKGsDfQjL8r5Z_balq8m1EDIr/exec?text=%s&source=%s&target=%s";

    public String translate(String text, String sourceLanguage, String targetLanguage) throws Exception {
        JsonObject jo = URLUtils.getJsonResponse(new URL(String.format(TRANS_URL_V1, convertToUnicode(text), sourceLanguage, targetLanguage)));

        if (jo.get("code").getAsInt() != 200)
            throw new TranslationException(jo.get("code").getAsInt());

        return jo.get("text").getAsString();
    }

    private static String convertToUnicode(String original) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < original.length(); i++) {
            sb.append(String.format("\\" + "\\u%04X", Character.codePointAt(original, i)));
        }
        return sb.toString();
    }
}