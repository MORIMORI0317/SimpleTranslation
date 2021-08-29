package net.morimori.simpletranslation.language;

import java.util.List;

public interface ILangCheckClientLanguage {
    List<String> getNoLocalizedLang();

    void setNoLocalizedLang(List<String> langs);
}
