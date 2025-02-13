package com.cricket.lane.booking.management.api.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class EmailDataDto {
    private List<String> recipients;
    private List<String> ccList;
    private List<String> bccList;
    private String mailTemplateName;
    private String serviceProvider;
    private Map<String, Object> data;
    private String subject;
    private byte[] attachment;
    private String attachmentName;
    private String contentType;
    private List<String> attachments;
}
