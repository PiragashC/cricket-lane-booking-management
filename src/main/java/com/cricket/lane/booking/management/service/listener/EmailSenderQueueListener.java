package com.cricket.lane.booking.management.service.listener;

import com.cricket.lane.booking.management.queue.QueueListener;
import com.cricket.lane.booking.management.queue.QueueMessage;
import com.cricket.lane.booking.management.service.EmailData;
import com.cricket.lane.booking.management.service.support.EmailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailSenderQueueListener implements QueueListener {
    private final EmailSender emailSender;

    @Override
    public void execute(QueueMessage queueMessage) {
        if (queueMessage != null) {
            try {
                log.info("Start sending mail to : " + ((EmailData) queueMessage.getData()).getRecipients());
                EmailData data = (EmailData) queueMessage.getData();
                emailSender.sendHtmlMessage(data);
                log.info("Mail sent to : " + data.getRecipients());
            } catch (Exception e) {
                log.error("Error occurred while sending mail: " + queueMessage.getData(), e);
            }
        }
    }
}
