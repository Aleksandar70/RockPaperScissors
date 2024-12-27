package com.example.rps.model;

public enum Move {
    ROCK, PAPER, SCISSORS;

    public boolean beats(Move other) {
        return (this == ROCK && other == SCISSORS) ||
               (this == PAPER && other == ROCK) ||
               (this == SCISSORS && other == PAPER);
    }
}