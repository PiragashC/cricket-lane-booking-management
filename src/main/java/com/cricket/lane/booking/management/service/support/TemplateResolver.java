package com.cricket.lane.booking.management.service.support;

import org.springframework.stereotype.Component;

@Component
public class TemplateResolver {
    private final String templateLocationFormat = "%s_%s";

    public String resolve(String serviceProvider, String mainTemplateName) {
        return String.format(templateLocationFormat, serviceProvider, mainTemplateName);
    }
}