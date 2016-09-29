package es.uji.apps.goc.templates;

import org.apache.cocoon.optional.pipeline.components.sax.fop.FopSerializer;
import org.apache.cocoon.pipeline.NonCachingPipeline;
import org.apache.cocoon.pipeline.Pipeline;
import org.apache.cocoon.sax.SAXPipelineComponent;
import org.apache.cocoon.sax.component.XMLGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

public class PDFTemplate extends GenericTemplate implements Template
{
    private static Logger log = LoggerFactory.getLogger(PDFTemplate.class);
    
    private Locale locale;
    private String application;
    
    public PDFTemplate(String name)
    {
        this(name, new Locale("ca"));
    }

    public PDFTemplate(String name, Locale locale)
    {
        super(name);
        
        this.locale = locale;
    }

    public PDFTemplate(String name, Locale locale, String application)
    {
        super(name);
        
        this.locale = locale;
        this.application = application;
    }

    public byte[] pdf(String data) throws Exception
    {
        Pipeline<SAXPipelineComponent> pipeline = new NonCachingPipeline<SAXPipelineComponent>();

        pipeline.addComponent(new XMLGenerator(data.getBytes()));
        pipeline.addComponent(new FopSerializer());

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        pipeline.setup(bos);
        pipeline.execute();

        return bos.toByteArray();
    }

    @Override
    public byte[] process()
    {
        TemplateEngine templateEngine = TemplateEngineFactory.getTemplateEngine("XML",
                "templates/", ".xml", application);

        IContext context = new Context(locale);
        context.getVariables().putAll(properties);

        String foSource = templateEngine.process(name, context);
        
        try
        {
            return pdf(foSource);
        }
        catch (Exception e)
        {
            log.error("Error processing template", e);
        }
        
        return null;
    }
}