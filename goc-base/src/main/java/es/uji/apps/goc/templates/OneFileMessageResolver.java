package es.uji.apps.goc.templates;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.Arguments;
import org.thymeleaf.messageresolver.AbstractMessageResolver;
import org.thymeleaf.messageresolver.MessageResolution;

import java.io.FileReader;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public class OneFileMessageResolver extends AbstractMessageResolver
{
    private static Logger log = LoggerFactory.getLogger(OneFileMessageResolver.class);

    private Map<String, Properties> configurationRegistry;
   
    private String application;
    
    public OneFileMessageResolver(String application)
    {
        super();

        configurationRegistry = new HashMap<>();
        this.application = application;
    }

    @Override
    public MessageResolution resolveMessage(Arguments arguments, String key,
                                            Object[] messageParameters)
    {
        Locale locale = arguments.getContext().getLocale();
        String language = locale.getLanguage();

        if (!configurationRegistry.containsKey(language))
        {
            initPropertiesFileForLanguage(language);
        }

        Properties configuration = configurationRegistry.get(locale.getLanguage());

        if (messageParameters != null && messageParameters.length > 0)
        {
            return new MessageResolution(MessageFormat.format(configuration.getProperty(key),
                    messageParameters));
        }
        else
        {
            return new MessageResolution(configuration.getProperty(key));
        }
    }

    private void initPropertiesFileForLanguage(String language)
    {
        try
        {
            Properties diskConfiguration = new Properties();
            diskConfiguration.load(new FileReader(MessageFormat.format(
                    "/etc/uji/{0}/i18n/i18n_{1}.properties", this.application, language)));
            configurationRegistry.put(language, diskConfiguration);
        }
        catch (Exception e)
        {
            log.error("Error loading i18n properties", e);
            throw new RuntimeException(e);
        }
    }

}