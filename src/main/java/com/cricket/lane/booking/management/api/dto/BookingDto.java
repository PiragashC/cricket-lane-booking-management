package com.cricket.lane.booking.management.api.dto;

import com.cricket.lane.booking.management.enums.BookingStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class BookingDto {

    private static final AtomicInteger counter = new AtomicInteger(1);

    private String id;
    private String customerName;
    private LocalDate date;
    private LocalTime fromTime;
    private LocalTime toTime;
    private String laneName;
    private String status;
    private String bookingNumber;
    private String email;
    private String telephoneNumber;

    public BookingDto(String id, String customerName, LocalDate bookingDate, LocalTime fromTime,LocalTime toTime, String laneName, BookingStatus bookingStatus,String email, String telephoneNumber) {
        this.id = id;
        this.customerName = customerName;
        this.date = bookingDate;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.laneName = laneName;
        this.status = bookingStatus.getMappedValue();
        this.bookingNumber = generateBookingNumber();
        this.email = email;
        this.telephoneNumber = telephoneNumber;
    }

    private String generateBookingNumber() {
        return String.format("B%06d", counter.getAndIncrement());
    }

    public static void resetCounter() {
        counter.set(1);
    }
}
