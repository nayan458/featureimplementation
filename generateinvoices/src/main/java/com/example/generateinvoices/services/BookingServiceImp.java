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

public class BookingServiceImp implements BookingService {
    private CustomerSessionRepository customerSessionRepository;
    private BookingRepository bookingRepository;
    private static double GST_IN_PERCENTAGE = 18;
    private static double SERVICE_CHARGE_IN_PERCENTAGE = 10;

    public BookingServiceImp(CustomerSessionRepository customerSessionRepository, BookingRepository bookingRepository) {
        this.customerSessionRepository = customerSessionRepository;
        this.bookingRepository = bookingRepository;
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
            for(Map.Entry<Room, Integer> entry: booking.getBookedRooms().entrySet()){
                Room room = entry.getKey();
                int quantity = entry.getValue();
                double cost = room.getPrice();

                bookedRooms.put(room, bookedRooms.getOrDefault(room, 0) + quantity);
                totalCost += ((double)quantity * cost);
            }
        }

        bookingRepository.delete(customerSessionId);
        
        double gst = calcGst(totalCost);
        double serviceCharge = calcServiceCharge(totalCost);
        double totalAmount = totalCost + gst + serviceCharge;

        invoice.setBookedRooms(bookedRooms);
        invoice.setTotalAmount(totalAmount);
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
