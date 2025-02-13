package com.cricket.lane.booking.management.service;


import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Builder
@Data
@ToString
public class EmailData {
    private String body;
    private String from;
    private String subject;
    private List<String> recipients;
    private List<String> ccList;
    private List<String> bccList;
    private byte[] attachment;
    private String contentType;
    private String attachmentName;
}
