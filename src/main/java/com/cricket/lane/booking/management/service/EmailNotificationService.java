package com.cricket.lane.booking.management.service;

import com.cloudinary.utils.StringUtils;
import com.cricket.lane.booking.management.api.dto.EmailDataDto;
import com.cricket.lane.booking.management.exception.EmailProcessingException;
import com.cricket.lane.booking.management.queue.Queue;
import com.cricket.lane.booking.management.queue.QueueMessage;
import com.cricket.lane.booking.management.service.factory.ContextFactory;
import com.cricket.lane.booking.management.service.factory.QueueMessageFactory;
import com.cricket.lane.booking.management.service.support.TemplateEngineProcessor;
import com.cricket.lane.booking.management.service.support.TemplateResolver;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationService {

    @Value("${notification.mail.from}")
    private String from;

    private final Queue mailQueue;
    private final TemplateResolver templateResolver;
    private final ContextFactory contextFactory;
    private final QueueMessageFactory queueMessageFactory;
    private final TemplateEngineProcessor templateEngineProcessor;
    private final JavaMailSender javaMailSender;

    public void send(EmailDataDto dataDto) {
        if (dataDto == null || CollectionUtils.isEmpty(dataDto.getRecipients())) {
            log.error("Invalid email data: recipients are empty");
            return;
        }

        try {
            log.debug("Preparing email with subject: {}, CC: {}, BCC: {}",
                    dataDto.getSubject(),
                    dataDto.getCcList(),
                    dataDto.getBccList());

            Context context = contextFactory.create(dataDto);
            String template = templateResolver.resolve(dataDto.getServiceProvider(), dataDto.getMailTemplateName());
            String html = templateEngineProcessor.process(template, context);

            // Validate the constructed email data
            if (StringUtils.isBlank(html)) {
                throw new IllegalArgumentException("Generated email content is empty");
            }

            QueueMessage<EmailData> message = queueMessageFactory.constructQueueMessage(dataDto, html);

            if (message == null || message.getData() == null) {
                throw new IllegalArgumentException("Failed to construct queue message");
            }

            mailQueue.submit(message);
            log.info("Email queued successfully to: {}", dataDto.getRecipients());
        } catch (Exception e) {
            log.error("Failed to process email with subject: {}", dataDto.getSubject(), e);
            throw new EmailProcessingException("Failed to process email", e);
        }
    }
    public String sendEmailWithAttachment(@RequestBody EmailDataDto emailData) throws MessagingException {
        log.info("EmailNotificationService.sendEmailWithAttachment() invoked.");
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // Set recipients, ccList, and bccList
        helper.setTo(emailData.getRecipients().toArray(new String[0]));
        if (emailData.getCcList() != null && !emailData.getCcList().isEmpty()) {
            helper.setCc(emailData.getCcList().toArray(new String[0]));
        }
        if (emailData.getBccList() != null && !emailData.getBccList().isEmpty()) {
            helper.setBcc(emailData.getBccList().toArray(new String[0]));
        }
        Context context = contextFactory.create(emailData);
        String template = templateResolver.resolve(emailData.getServiceProvider(), emailData.getMailTemplateName());
        String html = templateEngineProcessor.process(template, context);

        // Set subject and email text
        helper.setSubject(emailData.getSubject());
        helper.setText("Transaction Report", html);
        helper.setFrom(from);

        // Add the attachment if provided
        if (emailData.getAttachment() != null) {
            helper.addAttachment(emailData.getAttachmentName(), new ByteArrayResource(emailData.getAttachment()), emailData.getContentType());
        }

        javaMailSender.send(message);

        return "Email sent with attachment successfully!";
    }

}
