package com.example.rps.dto;

public record GameResponse(
        boolean valid,
        String message
) {
}
