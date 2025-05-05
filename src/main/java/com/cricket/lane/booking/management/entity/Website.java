package com.cricket.lane.booking.management.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Website {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    private String id;
    private String contentOne;
    private String contentTwo;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "websiteId")
    private Set<LaneCard> contentThree;
    private String contentFour;
    private String contentFive;
    private String contentSix;
    private String contentSeven;
    private String contentEight;
    private String contentNine;
    private String contentTen;
    private String contentEleven;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "websiteId")
    private Set<FeatureCard> contentTwelve;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "websiteId")
    private Set<Gallery> contentThirteen;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "websiteId")
    private Set<Event> contentFourteen;
}
