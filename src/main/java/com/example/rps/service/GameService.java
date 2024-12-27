package com.example.rps.service;

import com.example.rps.exception.GameNotFoundException;
import com.example.rps.model.Game;
import com.example.rps.model.GameResult;
import com.example.rps.model.Move;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class GameService {

    private static final Logger log = LoggerFactory.getLogger(GameService.class);

    private final MoveService moveService;
    private final StatisticsService statisticsService;
    private final Map<String, Game> games = new HashMap<>();

    public GameService(MoveService moveService, StatisticsService statisticsService) {
        this.moveService = moveService;
        this.statisticsService = statisticsService;
    }

    public String startNewGame() {
        String gameId = UUID.randomUUID().toString();
        games.put(gameId, new Game());
        log.info("New game started with ID: {}", gameId);

        return gameId;
    }

    public GameResult playRound(String gameId, Move userMove) {
        Game game = getGameOrThrow(gameId);
        Move computerMove = moveService.generateComputerMove();
        GameResult result = moveService.determineResult(userMove, computerMove);
        log.info("Computer move: {}. Result of the round: {}", computerMove, result);

        statisticsService.updateStatistics(gameId, result);
        game.addMove(userMove, computerMove, result);
        moveService.recordMove(userMove);

        return result;
    }

    public Game getGameOrThrow(String gameId) {
        return Optional.ofNullable(games.get(gameId))
                .orElseThrow(() -> new GameNotFoundException("Game not found with ID: " + gameId));
    }

    public void terminateGame(String gameId) {
        if (games.containsKey(gameId)) {
            games.remove(gameId);
            statisticsService.initializeStatistics(gameId);
            log.info("Game with ID: {} terminated successfully.", gameId);
        } else {
            log.warn("Attempted to terminate a non-existent game with ID: {}", gameId);
            throw new GameNotFoundException("Game not found with ID: " + gameId);
        }
    }
}
