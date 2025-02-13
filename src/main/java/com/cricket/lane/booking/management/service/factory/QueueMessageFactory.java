package com.cricket.lane.booking.management.service.factory;

import com.cricket.lane.booking.management.api.dto.EmailDataDto;
import com.cricket.lane.booking.management.queue.QueueMessage;
import com.cricket.lane.booking.management.service.EmailData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class QueueMessageFactory {

    @Value("${notification.mail.from}")
    private String from;

    public QueueMessage<EmailData> constructQueueMessage(EmailDataDto dataDto, String html) {
        EmailData emailData = EmailData.builder().
                body(html)
                .from(from)
                .recipients(dataDto.getRecipients())
                .ccList(dataDto.getCcList())
                .bccList(dataDto.getBccList())
                .subject(dataDto.getSubject())
                .attachment(dataDto.getAttachment() != null ? dataDto.getAttachment() : null)
                .attachmentName(dataDto.getAttachmentName() != null ? dataDto.getAttachmentName() : null)
                .contentType(dataDto.getContentType() != null ? dataDto.getContentType() : null)
                .build();
        QueueMessage<EmailData> message = new QueueMessage<>();
        message.setData(emailData);
        return message;
    }
}
