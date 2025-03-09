package com.cricket.lane.booking.management.entity;

import com.cricket.lane.booking.management.enums.BookingStatus;
import com.cricket.lane.booking.management.enums.BookingType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class CricketLaneBooking {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    private String id;
    private String email;
    private LocalTime fromTime;
    private LocalTime toTime;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "cricketLaneBookingId")
    private Set<SelectedLanes> selectedLanes;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "cricketLaneBookingId")
    private Set<BookingDates> bookingDates;
    private String bookingTitle;
    private String bookingDetails;
    private String firstName;
    private String lastName;
    private String telephoneNumber;
    private String organization;
    private BigDecimal bookingPrice;
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;
    private LocalDate createdDate;
    @Enumerated(EnumType.STRING)
    private BookingType bookingType;
}
