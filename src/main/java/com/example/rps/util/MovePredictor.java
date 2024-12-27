package com.example.rps.util;

import com.example.rps.model.Move;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class MovePredictor {

    private final Map<Move, Integer> moveHistory;

    public MovePredictor() {
        this.moveHistory = Arrays.stream(Move.values())
                .collect(Collectors.toMap(
                        Function.identity(),
                        move -> 0,
                        (a, b) -> a,
                        () -> new EnumMap<>(Move.class)
                ));
    }

    public void recordMove(Move move) {
        moveHistory.put(move, moveHistory.get(move) + 1);
    }

    public Move predict() {
        return moveHistory.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseGet(() -> Move.values()[(int) (Math.random() * Move.values().length)]);
    }
}
