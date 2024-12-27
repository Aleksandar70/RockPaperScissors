package com.example.rps.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Game {

    private final List<Move> userMoves;
    private final List<Move> computerMoves;
    private final List<GameResult> results;

    private final Map<Move, Integer> userMoveCounts;
    private final Map<Move, Integer> computerMoveCounts;
    private final Map<GameResult, Integer> resultCounts;

    public Game() {
        this.userMoves = new ArrayList<>();
        this.computerMoves = new ArrayList<>();
        this.results = new ArrayList<>();

        this.userMoveCounts = initializeMoveCounts();
        this.computerMoveCounts = initializeMoveCounts();
        this.resultCounts = initializeResultCounts();
    }

    public void addMove(Move userMove, Move computerMove, GameResult result) {
        userMoves.add(userMove);
        computerMoves.add(computerMove);
        results.add(result);

        updateMoveCounts(userMove, computerMove);
        updateResultCounts(result);
    }

    public List<Move> getComputerMoves() {
        return Collections.unmodifiableList(computerMoves);
    }

    private void updateMoveCounts(Move userMove, Move computerMove) {
        userMoveCounts.put(userMove, userMoveCounts.get(userMove) + 1);
        computerMoveCounts.put(computerMove, computerMoveCounts.get(computerMove) + 1);
    }

    private void updateResultCounts(GameResult result) {
        resultCounts.put(result, resultCounts.get(result) + 1);
    }

    private Map<Move, Integer> initializeMoveCounts() {
        return Stream.of(Move.values())
                .collect(Collectors.toMap(
                        Function.identity(),
                        move -> 0,
                        (existing, replacement) -> existing,
                        () -> new EnumMap<>(Move.class)
                ));
    }

    private Map<GameResult, Integer> initializeResultCounts() {
        return Stream.of(GameResult.values())
                .collect(Collectors.toMap(
                        Function.identity(),
                        result -> 0,
                        (existing, replacement) -> existing,
                        () -> new EnumMap<>(GameResult.class)
                ));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(userMoves, game.userMoves)
                && Objects.equals(computerMoves, game.computerMoves)
                && Objects.equals(results, game.results)
                && Objects.equals(userMoveCounts, game.userMoveCounts)
                && Objects.equals(computerMoveCounts, game.computerMoveCounts)
                && Objects.equals(resultCounts, game.resultCounts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userMoves, computerMoves, results, userMoveCounts, computerMoveCounts, resultCounts);
    }

    @Override
    public String toString() {
        return "userMoves=" + userMoves +
                ", computerMoves=" + computerMoves +
                ", results=" + results;
    }
}
