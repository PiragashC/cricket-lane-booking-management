package com.cricket.lane.booking.management.enums;

import com.cricket.lane.booking.management.exception.ServiceException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;


public enum BookingType {
    ALL("All"),
    ONLINE("Online"),
    OFFLINE("Offline");

    private final String mappedValue;

    BookingType(String mappedValue) {
        this.mappedValue = mappedValue;
    }
    public static BookingType fromMappedValue(String mappedValue){
        if (mappedValue == null || mappedValue.isBlank()) {
            return null;
        }
        for (BookingType bookingType : BookingType.values()) {
            if (bookingType.mappedValue.equalsIgnoreCase(mappedValue)) {
                return bookingType;
            }
        }
        throw new ServiceException("Unsupported type: " + mappedValue, "Bad request", HttpStatus.BAD_REQUEST);
    }

    public static List<String> getAll() {
        List<String> list = new ArrayList<>();
        for (BookingType bookingType : BookingType.values()) {
            list.add(bookingType.mappedValue);
        }
        return list;
    }
    public String getMappedValue() {
        return mappedValue;
    }


}
