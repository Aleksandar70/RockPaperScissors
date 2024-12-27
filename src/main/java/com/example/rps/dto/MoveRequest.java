package com.example.rps.dto;

public record MoveRequest(
        String gameId,
        String move
) {
}
