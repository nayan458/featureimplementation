package com.example.generateinvoices.controllers;

import com.example.generateinvoices.dtos.GenerateInvoiceRequestDto;
import com.example.generateinvoices.dtos.GenerateInvoiceResponseDto;
import com.example.generateinvoices.dtos.ResponseStatus;
import com.example.generateinvoices.models.Invoice;
import com.example.generateinvoices.services.BookingService;

public class BookingController {
    private BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public GenerateInvoiceResponseDto generateInvoice(GenerateInvoiceRequestDto requestDto) {
        GenerateInvoiceResponseDto response = new GenerateInvoiceResponseDto();
        try {
            Invoice invoice = bookingService.generateInvoice(requestDto.getUserId());
            response.setInvoice(invoice);
            response.setResponseStatus(ResponseStatus.SUCCESS);
        } catch (Exception e) {
            response.setInvoice(null);
            response.setResponseStatus(ResponseStatus.FAILURE);
        }
        return response;
    }
}

