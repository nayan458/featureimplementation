package com.example.getrooms.controllers;

import com.example.getrooms.dtos.GetRoomsRequestDto;
import com.example.getrooms.dtos.GetRoomsResponseDto;
import com.example.getrooms.services.RoomService;

public class RoomController {
    private RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    public GetRoomsResponseDto getRooms(GetRoomsRequestDto requestDto) {
        //ToDo
        return null;
    }
}

