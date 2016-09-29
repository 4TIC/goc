package es.uji.apps.goc.templates;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;

import java.util.Locale;

public class HTMLTemplate extends GenericTemplate implements Template
{
    private final Locale locale;
    private String application;
    
    public HTMLTemplate(String name)
    {
        this(name, new Locale("ca"));
    }

    public HTMLTemplate(String name, Locale locale)
    {
        super(name);

        this.locale = locale;
    }

    public HTMLTemplate(String name, Locale locale, String application)
    {
        super(name);

        this.locale = locale;
        this.application = application;
    }

    @Override
    public byte[] process()
    {
        TemplateEngine templateEngine = TemplateEngineFactory.getTemplateEngine("HTML5",
                "templates/", ".html", this.application);

        IContext context = new Context(locale);
        context.getVariables().putAll(properties);

        return templateEngine.process(name, context).getBytes();
    }
}