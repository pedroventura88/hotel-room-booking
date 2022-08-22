package com.hotel.core.reservation.impl;

import com.hotel.core.reservation.BookingRequest;
import com.hotel.core.reservation.*;
import com.hotel.web.exceptions.BookingNotFoundException;
import com.hotel.web.exceptions.BookingRequestAttributesException;
import com.hotel.web.exceptions.BookingUnavailableException;
import com.hotel.web.exceptions.ExceptionFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Strings.isNullOrEmpty;

@Service
public class BookingServiceImpl implements BookingService {
    private static final Logger LOG = LoggerFactory.getLogger(BookingServiceImpl.class);

    private BookingRepository bookingRepository;

    public BookingServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    /**If the room is available, will return an String message saying 'The day XXXX-XX-XX is available for booking' */
    public String checkRoomAvailability(LocalDate date) throws Exception {
        if (Objects.isNull(date)) {
            LOG.warn("The date passed is null, and its not allowed");
            ExceptionFormatter ex = new ExceptionFormatter("failed on checkAvailability", "The date passed as parameter is null");
            throw new BookingUnavailableException(ex.buildErrors());
        }

        if (date.isEqual(LocalDate.now())) {
            LOG.warn("Its not allowed to book at the current day");
            ExceptionFormatter ex = new ExceptionFormatter("failed on checkAvailability", "Its not allowed to book at the current day");
            throw new BookingUnavailableException(ex.buildErrors());
        }

        if (bookingRepository.existsBookingByCheckInDate(date)) {
            LOG.warn("The day is already booked");
            ExceptionFormatter ex = new ExceptionFormatter("failed on checkAvailability", "The day " + date + " is already booked");
            throw new BookingUnavailableException(ex.buildErrors());
        }

        checkDaysBetweenCheckInAndCheckout(date);
        return "The day " + date + " is available for booking";
    }

    @Override
    /**If successfully created, will return the object created with the description and Id*/
    public Booking bookRoom(BookingRequest bookingRequest) throws Exception {
        checkRequestAttributes(bookingRequest);
        checkRoomAvailability(bookingRequest.getCheckkIn());
        checkDaysValidation(bookingRequest.getCheckkIn(), bookingRequest.getCheckOut());

        Booking entity = Booking.builder()
                .reservedBy(bookingRequest.getCustomerName())
                .checkInDate(bookingRequest.getCheckkIn())
                .checkOutDate(bookingRequest.getCheckOut())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .isCanceled(false)
                .build();

        LOG.info(entity.toString());
        Booking booking = bookingRepository.save(entity);
        LOG.info("BookingId {} created", entity.getId());
        return booking;
    }

    private void checkRequestAttributes(BookingRequest bookingRequest) {
        ExceptionFormatter ex;
        if (Objects.isNull(bookingRequest.getCheckkIn()) || Objects.isNull(bookingRequest.getCheckOut()) || isNullOrEmpty(bookingRequest.getCustomerName())) {
            LOG.warn("BookingRequest attributes cannot be null or empty");
            ex = new ExceptionFormatter("failed on attributes validation", "The Request attributes cannot be null or empty");
            throw new BookingRequestAttributesException(ex.buildErrors());
        }

        if (bookingRequest.getCheckkIn().isAfter(bookingRequest.getCheckOut())) {
            LOG.warn("checkIn date is after checkOut date");
            ex = new ExceptionFormatter("failed on checkRequestAttributes", "CheckIn date cannot be after checkOut date");
            throw new BookingRequestAttributesException(ex.buildErrors());
        }
    }

    @Override
    /**If successfully canceled, will save the object with flag isCanceled = true */
    public void cancelBooking(Long bookingId) {
        Optional<Booking> booking = bookingExists(bookingId);
        booking.get().setIsCanceled(true);
        booking.get().setUpdateTime(LocalDateTime.now());
        bookingRepository.save(booking.get());
        LOG.info("ReservationId {} canceled", bookingId);
    }


    @Override
    /**If successfully updated, will return the Updated Booking by Id, with the new description */
    public Booking updateBooking(Long bookingId, BookingRequest bookingRequest) throws Exception {
        checkRequestAttributes(bookingRequest);
        Optional<Booking> booking = bookingExists(bookingId);
        checkRoomAvailability(bookingRequest.getCheckkIn());
        checkDaysValidation(bookingRequest.getCheckkIn(), bookingRequest.getCheckOut());

        booking.get().setCheckInDate(bookingRequest.getCheckkIn());
        booking.get().setCheckOutDate(bookingRequest.getCheckOut());
        booking.get().setReservedBy(bookingRequest.getCustomerName());
        booking.get().setUpdateTime(LocalDateTime.now());
        bookingRepository.save(booking.get());
        LOG.info("BookingId {} updated", bookingId);
        return booking.get();
    }

    @Override
    /**not part of the challenge. Simple Get endpoint without pagination, for listing the bookings that exists on database,
     * including canceled bookings. This functionality was developed just to help the navigation on the API */
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    private Optional<Booking> bookingExists(Long bookingId) {
        Optional<Booking> reservation = bookingRepository.findByIdAndIsCanceledFalse(bookingId);
        if (Objects.isNull(reservation) || reservation.isEmpty()) {
            LOG.warn("Reservation {} not found on database", bookingId);
            ExceptionFormatter ex = new ExceptionFormatter("failed on bookingExists", "Booking " + bookingId + " Not Found on database");
            throw new BookingNotFoundException(ex.buildErrors());
        }
        return reservation;
    }

    private void checkDaysValidation(LocalDate checkIn, LocalDate checkOut) {
        ExceptionFormatter ex;

        if (checkIn.isBefore(LocalDate.now()) || checkOut.isBefore(LocalDate.now())) {
            LOG.warn("Its not possible to book at the past");
            ex = new ExceptionFormatter("failed on checkAvailability", "Its not possible to book at the past");
            throw new BookingUnavailableException(ex.buildErrors());
        }

        long period30DaysMax = ChronoUnit.DAYS.between(LocalDate.now(), checkIn);
        if (period30DaysMax > 30) {
            LOG.warn("The checkIn date {} have more then 30 days in advance", checkIn);
            ex = new ExceptionFormatter("failed on 30 days max period", "The checkIn date " + checkIn + " have more then 30 days in advance");
            throw new BookingUnavailableException(ex.buildErrors());
        }

        long period3Days = ChronoUnit.DAYS.between(checkIn, checkOut);
        if (period3Days > 3) {
            ex = new ExceptionFormatter("failed on check 3 days difference", "The period between checkIn and checkOut is higher than 3 days");
            throw new BookingUnavailableException(ex.buildErrors());
        }
    }

    private void checkDaysBetweenCheckInAndCheckout(LocalDate date) {
        for (Booking booking : getAllBookings()) {
            DateRangeValidator rangeValidator = new DateRangeValidator(booking.getCheckInDate(), booking.getCheckOutDate());
            if (rangeValidator.isWithinRange(date)) {
                LOG.warn("The day " + date + " is already booked");
                ExceptionFormatter ex = new ExceptionFormatter("failed on checkRangeDates", "The day " + date + " is already booked");
                throw new BookingUnavailableException(ex.buildErrors());
            }
        }
    }
}
