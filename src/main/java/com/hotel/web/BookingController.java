package com.hotel.web;

import com.hotel.core.BookingRequest;
import com.hotel.core.reservation.Reservation;
import com.hotel.core.reservation.ReservationService;
import com.hotel.web.exceptions.BookingUnavailableException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;

@RestController
@RequestMapping("/v1/reservation")
public class BookingController {

    private ReservationService service;

    public BookingController(ReservationService service) {
        this.service = service;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Reservation> makeReservation(@RequestBody BookingRequest request) {
        try {
            Reservation reservation = service.makeReservation(request);
            return ResponseEntity.ok(reservation);
        } catch (BookingUnavailableException e) {
            throw new BookingUnavailableException(e.getErrors());
        } catch (Exception e) {
            throw new RuntimeException("some error goes here (makeReservation)");
        }
    }

    @GetMapping
    public ResponseEntity<String> checkAvailability(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            String message = service.checkRoomAvailability(date);
            return ResponseEntity.ok(message);
        } catch (BookingUnavailableException e) {
            throw new BookingUnavailableException(e.getErrors());
        } catch (Exception e) {
            throw new RuntimeException("some error goes here (checkAvailability)");
        }
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Reservation> updateReservation(@PathVariable("id") Long id, @RequestBody BookingRequest bookingRequest) {
        try {
            Reservation reservation = service.updateReservation(id, bookingRequest);
            return ResponseEntity.ok(reservation);
        } catch (Exception e) {
            throw new RuntimeException("some error goes here (updateReservation)");
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable("id") Long id) {
        try {
            service.cancelReservation(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new RuntimeException("some error goes here (updateReservation)");
        }
    }

}
