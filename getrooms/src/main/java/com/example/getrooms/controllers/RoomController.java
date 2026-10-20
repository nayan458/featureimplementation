package com.example.getrooms.controllers;

import java.util.List;

import com.example.getrooms.dtos.GetRoomsRequestDto;
import com.example.getrooms.dtos.GetRoomsResponseDto;
import com.example.getrooms.dtos.ResponseStatus;
import com.example.getrooms.models.Room;
import com.example.getrooms.services.RoomService;

public class RoomController {
    private RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    public GetRoomsResponseDto getRooms(GetRoomsRequestDto requestDto) {
        GetRoomsResponseDto response = new GetRoomsResponseDto();

        try {
            List<Room> rooms = roomService.getRooms(requestDto.getRoomType());
            response.setRooms(rooms);
            response.setResponseStatus(ResponseStatus.SUCCESS);
        } catch (Exception e) {
            response.setResponseStatus(ResponseStatus.FAILURE);
        }
        
        return response;
    }
}

