package es.uji.apps.goc.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LanguageConfig
{
    @Value("${goc.mainLanguage}")
    public String mainLanguage;

    @Value("${goc.mainLanguageDescription}")
    public String mainLanguageDescription;

    @Value("${goc.alternativeLanguage}")
    public String alternativeLanguage;

    @Value("${goc.alternativeLanguageDescription}")
    public String alternativeLanguageDescription;

    public boolean isMainLangauge(String lang)
    {
        return (lang == null || lang.isEmpty() || lang.equals(mainLanguage));
    }

    public String getLangCode(String lang)
    {
        if (lang == null || lang.isEmpty() || !(lang.toLowerCase()
                .equals(mainLanguage) || lang.toLowerCase().equals(alternativeLanguage)))
        {
            return mainLanguage;
        }

        return lang;
    }
}
