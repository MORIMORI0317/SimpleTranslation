package net.morimori0317.simpletranslation.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.LanguageInfo;
import net.minecraft.client.resources.language.LanguageManager;

import java.util.Optional;

public class LangUtils {
    private static final Minecraft mc = Minecraft.getInstance();
    public static final String[] googleLangCodes = {"af", "sq", "am", "ar", "hy", "az", "eu", "be", "bn", "bs", "bg", "ca", "ceb", "zh-CN", "zh", "zh-TW", "co", "hr", "cs", "da", "nl", "en", "eo", "et", "fi", "fr", "fy", "gl", "ka", "de", "el", "gu", "ht", "ha", "haw", "he", "iw", "hi", "hmn", "hu", "is", "ig", "id", "ga", "it", "ja", "jv", "kn", "kk", "km", "rw", "ko", "ku", "ky", "lo", "lv", "lt", "lb", "mk", "mg", "ms", "ml", "mt", "mi", "mr", "mn", "my", "ne", "no", "ny", "or", "ps", "fa", "pl", "pt", "pa", "ro", "ru", "sm", "gd", "sr", "st", "sn", "sd", "si", "sk", "sl", "so", "es", "su", "sw", "sv", "tl", "tg", "ta", "tt", "te", "th", "tr", "tk", "uk", "ur", "ug", "uz", "vi", "cy", "xh", "yi", "yo", "zu"};

    public static String getLangByGoogleCodeName(String googleId) {
        LanguageInfo inf = getLangByGoogleCode(googleId);
        if (inf != null)
            return inf.getName();
        return googleId;
    }

    public static LanguageInfo getLanguageByGoogleCode(String googleCode) {
        var str = getLangIdByGoogleCode(googleCode);
        if (str.isPresent())
            return mc.getLanguageManager().getLanguage(str.get());

        for (LanguageInfo language : mc.getLanguageManager().getLanguages()) {
            if (language.getCode().split("_")[0].equals(googleCode))
                return language;
        }

        return mc.getLanguageManager().getLanguage(LanguageManager.DEFAULT_LANGUAGE_CODE);
    }

    public static String getGoogleCodeByLanguage(LanguageInfo language) {
        var lng = getGoogleCodeByLangId(language.getCode());
        if (lng.isPresent())
            return lng.get();

        String name = language.getCode().split("_")[0];
        for (String lang : googleLangCodes) {
            if (lang.equals(name))
                return lang;
        }

        return "en";
    }

    private static LanguageInfo getLangByGoogleCode(String id) {
        return getLanguageByGoogleCode(id);
    }

    public static String getGoogleCodeByLang(LanguageInfo language) {
        return getGoogleCodeByLanguage(language);
    }


    public static Optional<String> getLangIdByGoogleCode(String googleCode) {
        return switch (googleCode) {
            case "af" -> Optional.of("af_za");
            case "sq" -> Optional.of("sq_al");
            case "ar" -> Optional.of("ar_sa");
            case "hy" -> Optional.of("hy_am");
            case "az" -> Optional.of("az_az");
            case "eu" -> Optional.of("eu_es");
            case "be" -> Optional.of("be_by");
            case "bs" -> Optional.of("bs_ba");
            case "bg" -> Optional.of("bg_bg");
            case "ca" -> Optional.of("ca_es");
            case "zh-CN" -> Optional.of("zh_cn");
            case "zh" -> Optional.of("zh_cn");
            case "zh-TW" -> Optional.of("zh_tw");
            case "hr" -> Optional.of("hr_hr");
            case "cs" -> Optional.of("cs_cz");
            case "da" -> Optional.of("da_dk");
            case "nl" -> Optional.of("nl_nl");
            case "en" -> Optional.of("en_us");
            case "eo" -> Optional.of("eo_uy");
            case "et" -> Optional.of("et_ee");
            case "fi" -> Optional.of("fi_fi");
            case "fr" -> Optional.of("fr_fr");
            case "fy" -> Optional.of("fy_nl");
            case "gl" -> Optional.of("gl_es");
            case "ka" -> Optional.of("ka_ge");
            case "de" -> Optional.of("de_de");
            case "el" -> Optional.of("el_gr");
            case "haw" -> Optional.of("haw");
            case "he" -> Optional.of("he_il");
            case "iw" -> Optional.of("he_il");
            case "hi" -> Optional.of("hi_in");
            case "hu" -> Optional.of("hu_hu");
            case "is" -> Optional.of("is_is");
            case "ig" -> Optional.of("ig_ng");
            case "id" -> Optional.of("id_id");
            case "ga" -> Optional.of("ga_ie");
            case "it" -> Optional.of("it_it");
            case "ja" -> Optional.of("ja_jp");
            case "kn" -> Optional.of("kn_in");
            case "ko" -> Optional.of("ko_kr");
            case "lv" -> Optional.of("lv_lv");
            case "lt" -> Optional.of("lt_lt");
            case "lb" -> Optional.of("lb_lu");
            case "mk" -> Optional.of("mk_mk");
            case "ms" -> Optional.of("ms_my");
            case "mt" -> Optional.of("mt_mt");
            case "mi" -> Optional.of("mi_nz");
            case "mn" -> Optional.of("mn_mn");
            case "no" -> Optional.of("no_no");
            case "fa" -> Optional.of("fa_ir");
            case "pl" -> Optional.of("pl_pl");
            case "pt" -> Optional.of("pt_pt");
            case "ro" -> Optional.of("ro_ro");
            case "ru" -> Optional.of("ru_ru");
            case "gd" -> Optional.of("gd_gb");
            case "sr" -> Optional.of("sr_sp");
            case "sk" -> Optional.of("sk_sk");
            case "sl" -> Optional.of("sl_si");
            case "so" -> Optional.of("so_so");
            case "es" -> Optional.of("es_es");
            case "sv" -> Optional.of("sv_se");
            case "tl" -> Optional.of("tl_ph");
            case "ta" -> Optional.of("ta_IN");
            case "tt" -> Optional.of("tt_ru");
            case "th" -> Optional.of("th_th");
            case "tr" -> Optional.of("tr_tr");
            case "uk" -> Optional.of("uk_ua");
            case "vi" -> Optional.of("vi_vn");
            case "cy" -> Optional.of("cy_gb");
            case "yo" -> Optional.of("yo_ng");
            case "kk" -> Optional.of("kk_kz");
            case "yi" -> Optional.of("yi_de");
            default -> Optional.empty();
        };
    }

    public static Optional<String> getGoogleCodeByLangId(String langId) {
        return switch (langId) {
            case "ro_ro" -> Optional.of("ro");
            case "af_za" -> Optional.of("af");
            case "mt_mt" -> Optional.of("mt");
            case "tl_ph" -> Optional.of("tl");
            case "th_th" -> Optional.of("th");
            case "ig_ng" -> Optional.of("ig");
            case "cs_cz" -> Optional.of("cs");
            case "ca_es" -> Optional.of("ca");
            case "hu_hu" -> Optional.of("hu");
            case "ar_sa" -> Optional.of("ar");
            case "gl_es" -> Optional.of("gl");
            case "kn_in" -> Optional.of("kn");
            case "so_so" -> Optional.of("so");
            case "sk_sk" -> Optional.of("sk");
            case "eo_uy" -> Optional.of("eo");
            case "es_es" -> Optional.of("es");
            case "nl_nl" -> Optional.of("nl");
            case "haw" -> Optional.of("haw");
            case "is_is" -> Optional.of("is");
            case "sq_al" -> Optional.of("sq");
            case "sv_se" -> Optional.of("sv");
            case "da_dk" -> Optional.of("da");
            case "gd_gb" -> Optional.of("gd");
            case "ta_IN" -> Optional.of("ta");
            case "hr_hr" -> Optional.of("hr");
            case "sr_sp" -> Optional.of("sr");
            case "ko_kr" -> Optional.of("ko");
            case "en_us" -> Optional.of("en");
            case "lt_lt" -> Optional.of("lt");
            case "kk_kz" -> Optional.of("kk");
            case "no_no" -> Optional.of("no");
            case "el_gr" -> Optional.of("el");
            case "it_it" -> Optional.of("it");
            case "ru_ru" -> Optional.of("ru");
            case "pl_pl" -> Optional.of("pl");
            case "be_by" -> Optional.of("be");
            case "tr_tr" -> Optional.of("tr");
            case "hi_in" -> Optional.of("hi");
            case "id_id" -> Optional.of("id");
            case "yi_de" -> Optional.of("yi");
            case "mn_mn" -> Optional.of("mn");
            case "fr_fr" -> Optional.of("fr");
            case "he_il" -> Optional.of("he");
            case "ja_jp" -> Optional.of("ja");
            case "de_de" -> Optional.of("de");
            case "ms_my" -> Optional.of("ms");
            case "zh_tw" -> Optional.of("zh-TW");
            case "eu_es" -> Optional.of("eu");
            case "yo_ng" -> Optional.of("yo");
            case "fa_ir" -> Optional.of("fa");
            case "bg_bg" -> Optional.of("bg");
            case "hy_am" -> Optional.of("hy");
            case "lb_lu" -> Optional.of("lb");
            case "mk_mk" -> Optional.of("mk");
            case "cy_gb" -> Optional.of("cy");
            case "az_az" -> Optional.of("az");
            case "fi_fi" -> Optional.of("fi");
            case "vi_vn" -> Optional.of("vi");
            case "et_ee" -> Optional.of("et");
            case "ka_ge" -> Optional.of("ka");
            case "lv_lv" -> Optional.of("lv");
            case "bs_ba" -> Optional.of("bs");
            case "uk_ua" -> Optional.of("uk");
            case "ga_ie" -> Optional.of("ga");
            case "tt_ru" -> Optional.of("tt");
            case "zh_cn" -> Optional.of("zh-CN");
            case "sl_si" -> Optional.of("sl");
            case "mi_nz" -> Optional.of("mi");
            case "fy_nl" -> Optional.of("fy");
            case "pt_pt" -> Optional.of("pt");
            default -> Optional.empty();
        };
    }
}
