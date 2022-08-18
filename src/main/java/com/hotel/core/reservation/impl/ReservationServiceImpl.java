package com.hotel.core.reservation.impl;

import com.hotel.core.BookingRequest;
import com.hotel.core.customer.CustomerRepository;
import com.hotel.core.reservation.Reservation;
import com.hotel.core.reservation.ReservationRepository;
import com.hotel.core.reservation.ReservationService;
import com.hotel.web.exceptions.BookingUnavailableException;
import com.hotel.web.exceptions.ExceptionFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;

//Regras:
//        - estadias n podem ser maiores do que 3 dias
//        - so podem ser reservadas no maximo ate 30 dias de antecedencia
//        - toda reserva começa no proximo dia de reserva (n se pode reservar para o dia corrente)


@Service
public class ReservationServiceImpl implements ReservationService {
    private static final Logger LOG = LoggerFactory.getLogger(ReservationServiceImpl.class);

    private ReservationRepository reservationRepository;
    private CustomerRepository customerRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository, CustomerRepository customerRepository) {
        this.reservationRepository = reservationRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public String checkRoomAvailability(LocalDate date) throws Exception {
        if (date.isEqual(LocalDate.now())) {
            LOG.warn("Its not allowed to book at the current day");
            ExceptionFormatter ex = new ExceptionFormatter("failed on checkAvailability", "Its not allowed to book at the current day");
            throw new BookingUnavailableException(ex.buildErrors());
        }
        if (reservationRepository.existsReservationByEntranceDate(date)) {
            LOG.warn("The day is already booked");
            ExceptionFormatter ex = new ExceptionFormatter("failed on checkAvailability", "The day " + date + " is already booked");
            throw new BookingUnavailableException(ex.buildErrors());
        }
        return "The day " + date + " is available for booking";
    }

    @Override
    public Reservation makeReservation(BookingRequest bookingRequest) throws Exception {
        checkRoomAvailability(bookingRequest.getCheckkIn());
        checkDaysValidation(bookingRequest.getCheckkIn(), bookingRequest.getCheckOut());

        Reservation entity = Reservation.builder()
                .reservedBy(bookingRequest.getCustomerName())
                .entranceDate(bookingRequest.getCheckkIn())
                .checkOut(bookingRequest.getCheckOut())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .isCanceled(false)
                .build();

        LOG.info(entity.toString());
        Reservation reservation = reservationRepository.save(entity);
        return reservation;
    }

    private void checkDaysValidation(LocalDate checkIn, LocalDate checkOut) {
        ExceptionFormatter ex;
        long period30DaysMax = ChronoUnit.DAYS.between(LocalDate.now(), checkIn);
        if (period30DaysMax > 30) {
            ex = new ExceptionFormatter("failed on 30 days max period", "The checkIn date " + checkIn + " have more then 30 days of difference");
            throw new BookingUnavailableException(ex.buildErrors());
        }
        long period3Days = ChronoUnit.DAYS.between(checkIn, checkOut);
        if (period3Days > 3) {
            ex = new ExceptionFormatter("failed on check 3 days difference", "The period between checkIn and checkOut is higher than 3 days");
            throw new BookingUnavailableException(ex.buildErrors());
        }
    }


    @Override
    public void cancelReservation(Long reservationId) {

    }

    @Override
    public Reservation updateReservation(Long reservationId, BookingRequest bookingRequest) {
        return null;
    }
}
