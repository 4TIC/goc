package es.uji.apps.goc.templates;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import java.util.HashMap;
import java.util.Map;

public class TemplateEngineFactory
{
    private static Map<String, TemplateEngine> templateEngines = new HashMap<>();

    private static Long TIME_TO_LIVE = 3600000L;

    @SuppressWarnings("unused")
    private String application;

    private static TemplateEngine initializeTemplateEngine(String templateMode, String prefix,
                                                           String sufix, boolean cacheable, Long timeToLive, String application)
    {
        TemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setTemplateMode(templateMode);
        templateResolver.setPrefix(prefix);
        templateResolver.setSuffix(sufix);
        templateResolver.setCacheable(cacheable);
        templateResolver.setCacheTTLMs(timeToLive);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        templateEngine.addDialect(new LayoutDialect());

        if (application != null)
        {
            templateEngine.setMessageResolver(new OneFileMessageResolver(application));
        }

        return templateEngine;
    }

    public static TemplateEngine getTemplateEngine(String templateMode, String prefix,
                                                   String sufix, String application)
    {
        return getTemplateEngine(templateMode, prefix, sufix, false, TIME_TO_LIVE, application);
    }

    public static TemplateEngine getTemplateEngine(String templateMode, String prefix,
                                                   String sufix, boolean cacheable, Long timeToLive, String application)
    {
        if (templateEngines == null || !templateEngines.containsKey(templateMode))
        {
            TemplateEngine templateEngine = initializeTemplateEngine(templateMode, prefix, sufix,
                    cacheable, timeToLive, application);
            templateEngines.put(templateMode, templateEngine);
        }

        return templateEngines.get(templateMode);
    }
}