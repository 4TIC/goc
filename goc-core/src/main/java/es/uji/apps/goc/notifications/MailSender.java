package es.uji.apps.goc.notifications;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MailSender
{
    private static Logger log = Logger.getLogger(MailSender.class);
    private String smtpHost;
    private Boolean smtpStartTLS;
    private Boolean smtpAuthRequired;
    private Integer smtpPort;
    private String smtpUsername;
    private String smtpPassword;
    private String defaultSender;

    @Autowired
    public MailSender(
            @Value("${uji.smtp.host}") String smtpHost,
            @Value("${uji.smtp.starttls.enable}") Boolean smtpStartTLS,
            @Value("${uji.smtp.auth}") Boolean smtpAuthRequired,
            @Value("${uji.smtp.port}") Integer smtpPort,
            @Value("${uji.smtp.username}") String smtpUsername,
            @Value("${uji.smtp.password}") String smtpPassword,
            @Value("${uji.smtp.defaultSender}") String defaultSender)
    {
        this.smtpHost = smtpHost;
        this.smtpStartTLS = smtpStartTLS;
        this.smtpAuthRequired = smtpAuthRequired;
        this.smtpPort = smtpPort;
        this.smtpUsername = smtpUsername;
        this.smtpPassword = smtpPassword;
        this.defaultSender = defaultSender;
    }

    public void send(Mensaje message) throws CanNotSendException
    {
        if (message.getDestinos().size() == 0) {
            return;
        }

        try
        {
            Session session = getMailSession();

            Message mimeMessage = new MimeMessage(session);
            defineMessageHeaders(mimeMessage);
            setSubject(message, mimeMessage);
            setRecipients(message, mimeMessage);
            setMessageContent(message, mimeMessage);

            Transport.send(mimeMessage);
        }
        catch (Exception e)
        {
            throw new CanNotSendException(e);
        }
    }

    private Authenticator getAuthenticator()
    {
        if (smtpUsername == null || smtpPassword == null)
        {
            return null;
        }

        return new Authenticator()
        {
            @Override
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(smtpUsername, smtpPassword);
            }
        };
    }

    private Properties getEmailProperties()
    {
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.starttls.enable", smtpStartTLS);
        props.put("mail.smtp.auth", smtpAuthRequired);
        props.put("mail.smtp.port", smtpPort);

        return props;
    }

    private Session getMailSession()
    {
        return Session.getInstance(getEmailProperties(), getAuthenticator());
    }

    private void defineMessageHeaders(Message message) throws MessagingException
    {
        message.addHeader("Auto-Submitted", "auto-generated");
    }

    private void setSubject(Mensaje mensaje, Message message) throws MessagingException
    {
        message.setSubject(mensaje.getAsunto());
    }

    private void setRecipients(Mensaje mensaje, Message message) throws MessagingException
    {
        message.setFrom(new InternetAddress(defaultSender));

        if (mensaje.getReplyTo() != null && !mensaje.getReplyTo().isEmpty())
        {
            message.setReplyTo(new Address[]{getAddress(mensaje.getReplyTo())});
        }

        for (String destino : mensaje.getDestinos())
        {
            Message.RecipientType recipientType = Message.RecipientType.TO;
            message.addRecipient(recipientType, getAddress(destino));
        }
    }

    private Address getAddress(String address) throws AddressException
    {
        if (address == null || address.equals(""))
        {
            return new InternetAddress();
        }

        return new InternetAddress(address.trim());
    }

    private void setMessageContent(Mensaje mensaje, Message message) throws MessagingException
    {
        MimeMultipart multipart = new MimeMultipart();

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(mensaje.getCuerpo(), mensaje.getContentType());
        multipart.addBodyPart(messageBodyPart);

        message.setContent(multipart);
    }
}