package com.example.addroomshotelmanagement.controllers;

import com.example.addroomshotelmanagement.dtos.AddRoomRequestDto;
import com.example.addroomshotelmanagement.dtos.AddRoomResponseDto;
import com.example.addroomshotelmanagement.dtos.ResponseStatus;
import com.example.addroomshotelmanagement.models.Room;
import com.example.addroomshotelmanagement.services.RoomService;

public class RoomController {
    private RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    public AddRoomResponseDto addRoom(AddRoomRequestDto requestDto) {
        AddRoomResponseDto response = new AddRoomResponseDto();

        try {
            long userId = requestDto.getUserId();
            String roomName = requestDto.getName();
            double price = requestDto.getPrice();
            String roomType = requestDto.getRoomType();
            String description = requestDto.getDescription();

            Room room = roomService.addRoom(userId, roomName, price, roomType, description);
            
            response.setRoom(room);
            response.setResponseStatus(ResponseStatus.SUCCESS);
        } catch (Exception e) {
            response.setResponseStatus(ResponseStatus.FAILURE);
        }
        return response;
    }
}
