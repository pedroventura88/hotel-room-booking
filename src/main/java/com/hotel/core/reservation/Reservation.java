package com.hotel.core.reservation;

import com.hotel.core.customer.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Reservation {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String reservedBy;

    @Column(nullable = false)
    private LocalDate entranceDate;

    @Column(nullable = false)
    private LocalDate checkOut;

    @Column(nullable = false)
    private LocalDateTime createTime;

    @Column(nullable = false)
    private LocalDateTime updateTime;

    @Column(nullable = false)
    private Boolean isCanceled;

}
