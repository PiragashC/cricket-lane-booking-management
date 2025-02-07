package com.cricket.lane.booking.management.service;

import com.cricket.lane.booking.management.api.dto.EmailDataDto;
import com.cricket.lane.booking.management.queue.Queue;
import com.cricket.lane.booking.management.queue.QueueMessage;
import com.cricket.lane.booking.management.service.factory.ContextFactory;
import com.cricket.lane.booking.management.service.factory.QueueMessageFactory;
import com.cricket.lane.booking.management.service.support.TemplateEngineProcessor;
import com.cricket.lane.booking.management.service.support.TemplateResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
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
        try {
            Context context = contextFactory.create(dataDto);
            String template = templateResolver.resolve(dataDto.getServiceProvider(), dataDto.getMailTemplateName());
            String html = templateEngineProcessor.process(template, context);
            QueueMessage<EmailData> message = queueMessageFactory.constructQueueMessage(dataDto, html);
            mailQueue.submit(message);
        } catch (Exception e) {
            log.error("Error", e);
        }
    }
}
