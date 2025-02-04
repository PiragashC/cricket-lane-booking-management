package com.cricket.lane.booking.management.enums;

import com.cricket.lane.booking.management.exception.ServiceException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum BookingStatus {
    SUCCESS("Success"),
    PENDING("Pending"),
    FAILURE("Failure");

    private final String mappedValue;

    BookingStatus(String mappedValue) {
        this.mappedValue = mappedValue;
    }
    public static BookingStatus fromMappedValue(String mappedValue){
        if (mappedValue == null || mappedValue.isBlank()) {
            return null;
        }
        for (BookingStatus bookingStatus : BookingStatus.values()) {
            if (bookingStatus.mappedValue.equalsIgnoreCase(mappedValue)) {
                return bookingStatus;
            }
        }
        throw new ServiceException("Unsupported type: " + mappedValue, "Bad request", HttpStatus.BAD_REQUEST);
    }

    public static List<String> getAll() {
        List<String> list = new ArrayList<>();
        for (BookingStatus bookingStatus : BookingStatus.values()) {
            list.add(bookingStatus.mappedValue);
        }
        return list;
    }

}
