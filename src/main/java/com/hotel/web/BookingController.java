package com.hotel.web;

import com.hotel.core.reservation.BookingRequest;
import com.hotel.core.reservation.Booking;
import com.hotel.core.reservation.BookingService;
import com.hotel.web.dto.BookingDto;
import com.hotel.web.dto.BookingMapper;
import com.hotel.web.exceptions.BookingNotFoundException;
import com.hotel.web.exceptions.BookingRequestAttributesException;
import com.hotel.web.exceptions.BookingUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/bookings")
public class BookingController {
    private static final Logger LOG = LoggerFactory.getLogger(BookingController.class);


    private BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    private BookingMapper mapper = new BookingMapper();

    @PostMapping
    @Transactional
    public ResponseEntity<BookingDto> bookRoom(@RequestBody BookingRequest request) throws Exception {
        try {
            Booking booking = service.bookRoom(request);
            return ResponseEntity.ok(mapper.toDto(booking));
        } catch (BookingUnavailableException e) {
            throw new BookingUnavailableException(e.getErrors());
        } catch (BookingRequestAttributesException e) {
            throw new BookingRequestAttributesException(e.getErrors());
        } catch (Exception e) {
            LOG.error("Some unexpected exception occurs", e);
            throw new Exception();
        }
    }

    @GetMapping("/check")
    public ResponseEntity<String> checkAvailability(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate date) throws Exception {
        try {
            String message = service.checkRoomAvailability(date);
            return ResponseEntity.ok(message);
        } catch (BookingUnavailableException e) {
            throw new BookingUnavailableException(e.getErrors());
        } catch (Exception e) {
            LOG.error("Some unexpected exception occurs", e);
            throw new Exception();
        }
    }


    @GetMapping
    public ResponseEntity<List<Booking>> getBookings() throws Exception {
        try {
            return ResponseEntity.ok(service.getAllBookings());
        } catch (Exception e) {
            LOG.error("Some unexpected exception occurs", e);
            throw new Exception();
        }
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<BookingDto> updateBooking(@PathVariable("id") Long id, @RequestBody BookingRequest bookingRequest) throws Exception {
        try {
            Booking booking = service.updateBooking(id, bookingRequest);
            return ResponseEntity.ok(mapper.toDto(booking));
        } catch (BookingNotFoundException e) {
            throw new BookingNotFoundException(e.getErrors());
        } catch (BookingUnavailableException e) {
            throw new BookingUnavailableException(e.getErrors());
        } catch (BookingRequestAttributesException e) {
            throw new BookingRequestAttributesException(e.getErrors());
        } catch (Exception e) {
            LOG.error("Some unexpected exception occurs", e);
            throw new Exception();
        }
    }

    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> cancelBooking(@PathVariable("id") Long id) throws Exception {
        try {
            service.cancelBooking(id);
            return ResponseEntity.noContent().build();
        } catch (BookingNotFoundException e) {
            throw new BookingNotFoundException(e.getErrors());
        } catch (Exception e) {
            LOG.error("Some unexpected exception occurs", e);
            throw new Exception();
        }
    }


}
