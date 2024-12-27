package com.example.rps.service;

import com.example.rps.exception.GameNotFoundException;
import com.example.rps.model.GameResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class StatisticsService {

    private static final Logger log = LoggerFactory.getLogger(StatisticsService.class);

    private static final String WINS = "wins";
    private static final String LOSSES = "losses";
    private static final String DRAWS = "draws";

    private final Map<String, Map<String, Integer>> gameStatistics = new HashMap<>();

    public void initializeStatistics(String gameId) {
        Map<String, Integer> stats = new HashMap<>();
        stats.put(WINS, 0);
        stats.put(LOSSES, 0);
        stats.put(DRAWS, 0);
        gameStatistics.put(gameId, stats);
    }

    public Map<String, Integer> getStatisticsByGameId(String gameId) {
        return Optional.ofNullable(gameStatistics.get(gameId))
                .orElseThrow(() -> new GameNotFoundException("Game statistics not found for ID: " + gameId));
    }

    public void updateStatistics(String gameId, GameResult result) {
        Map<String, Integer> stats = gameStatistics.get(gameId);
        switch (result) {
            case DRAW:
                stats.put(DRAWS, stats.get(DRAWS) + 1);
                break;
            case WIN:
                stats.put(WINS, stats.get(WINS) + 1);
                break;
            case LOSE:
                stats.put(LOSSES, stats.get(LOSSES) + 1);
                break;
            default:
                throw new IllegalArgumentException("Unexpected result: " + result);
        }
        log.info("Updated statistics for game ID: {}. Current stats: {}", gameId, stats);
    }
}
