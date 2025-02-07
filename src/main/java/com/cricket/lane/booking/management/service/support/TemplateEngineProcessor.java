package com.cricket.lane.booking.management.service.support;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@RequiredArgsConstructor
public class TemplateEngineProcessor {
    private final TemplateEngine templateEngine;

    public String process(String template, Context context) {
        return templateEngine.process(template, context);
    }

}
