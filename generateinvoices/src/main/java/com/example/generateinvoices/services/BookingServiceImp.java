package com.example.generateinvoices.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.generateinvoices.exceptions.CustomerSessionNotFoundException;
import com.example.generateinvoices.models.Booking;
import com.example.generateinvoices.models.CustomerSession;
import com.example.generateinvoices.models.CustomerSessionStatus;
import com.example.generateinvoices.models.Invoice;
import com.example.generateinvoices.models.Room;
import com.example.generateinvoices.repositories.BookingRepository;
import com.example.generateinvoices.repositories.CustomerSessionRepository;
import com.example.generateinvoices.repositories.imp.BookingRepositoryImp;
import com.example.generateinvoices.repositories.imp.CustomerSessionRepositoryImp;

public class BookingServiceImp implements BookingService {
    private static CustomerSessionRepository customerSessionRepository;
    private static BookingRepository bookingRepository;
    private static double GST_IN_PERCENTAGE = 18;
    private static double SERVICE_CHARGE_IN_PERCENTAGE = 10;

    public BookingServiceImp() {
        customerSessionRepository = new CustomerSessionRepositoryImp();
        bookingRepository = new BookingRepositoryImp();
    }

    @Override
    public Invoice generateInvoice(long userId) throws CustomerSessionNotFoundException {
        Invoice invoice = new Invoice();
        CustomerSession customerSession = customerSessionRepository
                                        .findActiveCustomerSessionByUserId(userId)
                                        .orElseThrow(() -> new CustomerSessionNotFoundException("Customer session doesn't exist"));
        // Customer session ends
        customerSession.setCustomerSessionStatus(CustomerSessionStatus.ENDED);
        customerSessionRepository.delete(userId);

        // get the rooms booked 
        long customerSessionId = customerSession.getId();
        List<Booking> bookings = bookingRepository.findBookingByCustomerSession(customerSessionId);
        double totalCost = 0;
        Map<Room, Integer> bookedRooms = new HashMap<>();

        for(Booking booking: bookings) {
            bookedRooms.putAll(booking.getBookedRooms());
            for(Integer cost: booking.getBookedRooms().values()) 
                totalCost += cost;
        }

        bookingRepository.delete(customerSessionId);
        
        invoice.setBookedRooms(bookedRooms);
        invoice.setTotalAmount(totalCost);
        invoice.setGst(calcGst(totalCost));
        invoice.setServiceCharge(calcServiceCharge(totalCost));

        return invoice;
    }

    private double calcGst(double cost) {
        return cost * GST_IN_PERCENTAGE / 100;
    }

    private double calcServiceCharge(double cost) {
        return cost * SERVICE_CHARGE_IN_PERCENTAGE / 100;
    }
}
