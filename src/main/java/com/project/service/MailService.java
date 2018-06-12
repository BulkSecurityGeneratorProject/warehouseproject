package com.project.service;

import com.lowagie.text.DocumentException;
import com.project.domain.Shipment;
import com.project.domain.User;

import com.project.service.util.MailServiceTest;
import io.github.jhipster.config.JHipsterProperties;

import org.apache.commons.lang3.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.activation.DataSource;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.util.Locale;

/**
 * Service for sending emails.
 * <p>
 * We use the @Async annotation to send emails asynchronously.
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private static final String USER = "user";

    private static final String BASE_URL = "baseUrl";

    private final JHipsterProperties jHipsterProperties;

    private final JavaMailSender javaMailSender;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;

    public MailService(JHipsterProperties jHipsterProperties, JavaMailSender javaMailSender,
            MessageSource messageSource, SpringTemplateEngine templateEngine) {

        this.jHipsterProperties = jHipsterProperties;
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to);
            message.setFrom(jHipsterProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.warn("Email could not be sent to user '{}'", to, e);
            } else {
                log.warn("Email could not be sent to user '{}': {}", to, e.getMessage());
            }
        }
    }
    @Async
    public void sendMailWithAttachments(String to, boolean isMultiPart,boolean isHtml, byte[] attachment) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new
                MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom(jHipsterProperties.getMail().getFrom());



              mimeMessageHelper.setText("This email is coming from warehouse application",isHtml);

          //  final InputStreamSource attachmentSource = new ByteArrayResource(attachment);
            DataSource dataSource = new ByteArrayDataSource(attachment, "application/pdf");
            mimeMessageHelper.addAttachment("pdf", dataSource);


            javaMailSender.send(mimeMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Async
    public void sendEmailFromTemplate(User user, String templateName, String titleKey) {
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);

    }

//    @Async
//    public void sendEmailFromTemplateAttachment(User user) throws IOException, DocumentException {
//
//        MailServiceTest mailServiceTest = new MailServiceTest(templateEngine,jHipsterProperties);
//
//        byte[] array=  mailServiceTest.se(user);
//        sendMailWithAttachments(user.getEmail(),true,false,array);
//    }

    @Async
    public void sendEmailShipped(Shipment shipment) throws IOException, DocumentException {

        MailServiceTest mailServiceTest = new MailServiceTest(templateEngine,jHipsterProperties);

        byte[] array=  mailServiceTest.sendShippedPdf(shipment);
        sendMailWithAttachments(shipment.getSenderP().getEmail(),true,false,array);
    }
    @Async
    public void sendEmailDeliveredS(Shipment shipment) throws IOException, DocumentException {

        MailServiceTest mailServiceTest = new MailServiceTest(templateEngine,jHipsterProperties);

        byte[] array=  mailServiceTest.sendDeliveredSPDFTemplate(shipment);
        sendMailWithAttachments(shipment.getSenderP().getEmail(),true,false,array);
    }
    @Async
    public void sendEmailDeliveredR(Shipment shipment) throws IOException, DocumentException {

        MailServiceTest mailServiceTest = new MailServiceTest(templateEngine,jHipsterProperties);

        byte[] array=  mailServiceTest.sendDeliveredRPDFTemplate(shipment);
        sendMailWithAttachments(shipment.getReceiver().getEmail(),true,false,array);
    }
//    @Async
//    public void sendEmailFromAttachment(Shipment shipment) throws IOException, DocumentException {
//
//        MailServiceTest mailServiceTest = new MailServiceTest(templateEngine,jHipsterProperties);
//
//        byte[] array=  mailServiceTest.sendPdfTemplets(shipment);
//        sendMailWithAttachments(shipment.getSenderP().getEmail(),true,false,array);
//    }



    @Async
    public void sendActivationEmail(User user) {
        log.debug("Sending activation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "activationEmail", "email.activation.title");
    }

    @Async
    public void sendCreationEmail(User user) {
        log.debug("Sending creation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "creationEmail", "email.activation.title");
    }

    @Async
    public void sendPasswordResetMail(User user) {
        log.debug("Sending password reset email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "passwordResetEmail", "email.reset.title");
    }

    public void sendShippedStatusEmail(Shipment shipment) throws IOException, DocumentException {
        log.debug("Sending status changed email to '{} '",shipment.getSenderP().getEmail());
        sendEmailShipped(shipment);
    }
    public void sendDeliveredSEmail(Shipment shipment) throws IOException, DocumentException {
        log.debug("Sending status changed email to '{} '",shipment.getSenderP().getEmail());
        sendEmailDeliveredS(shipment);
    }
    public void sendDeliveredREmail(Shipment shipment) throws IOException, DocumentException {
        log.debug("Sending status changed email to '{} '",shipment.getReceiver().getEmail());
        sendEmailDeliveredR(shipment);
    }
}


