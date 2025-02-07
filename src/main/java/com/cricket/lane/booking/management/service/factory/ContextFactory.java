package com.cricket.lane.booking.management.service.factory;

import com.cricket.lane.booking.management.api.dto.EmailDataDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

import java.util.Map;
@Component
public class ContextFactory {
    @Value("images.base.url")
    private String imageBaseUrl;

    public Context create(EmailDataDto dataDto) {
        Context context = new Context();
        context.setVariable("message", "Thanks for signing up");
        context.setVariable("imageBaseUrl", imageBaseUrl);
        if (dataDto.getData() != null && !dataDto.getData().isEmpty()) {
            for (Map.Entry<String, Object> entry : dataDto.getData().entrySet()) {
                context.setVariable(entry.getKey(), entry.getValue());
            }
        }
        return context;
    }
}
