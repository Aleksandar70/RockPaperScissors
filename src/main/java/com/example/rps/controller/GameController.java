package com.example.rps.controller;

import com.example.rps.dto.GameResponse;
import com.example.rps.dto.MoveRequest;
import com.example.rps.exception.GameNotFoundException;
import com.example.rps.model.Game;
import com.example.rps.model.GameResult;
import com.example.rps.model.Move;
import com.example.rps.service.GameService;
import com.example.rps.service.MoveService;
import com.example.rps.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;
    private final MoveService moveService;
    private final StatisticsService statisticsService;

    @Autowired
    public GameController(GameService gameService,
                          MoveService moveService,
                          StatisticsService statisticsService) {
        this.gameService = gameService;
        this.moveService = moveService;
        this.statisticsService = statisticsService;
    }

    /**
     * Starts a new Rock-Paper-Scissors game and returns the game ID.
     *
     * @return A ResponseEntity with a message containing the new game ID.
     */
    @PostMapping("/start")
    public ResponseEntity<GameResponse> startGame() {
        String gameId = gameService.startNewGame();
        statisticsService.initializeStatistics(gameId);
        String message = String.format("Game started! Your game ID is: %s", gameId);

        return ResponseEntity.ok(new GameResponse(true, message));
    }

    /**
     * Handles a move made by the user in the Rock-Paper-Scissors game.
     *
     * @param request The request body containing the game ID and the user's move.
     * @return A ResponseEntity containing the result of the move, including the computer's move and current game statistics.
     * Returns a 404 status if the game is not found or an invalid move is provided.
     */
    @PostMapping("/move")
    public ResponseEntity<GameResponse> makeMove(@RequestBody MoveRequest request) {
        try {
            Game game = gameService.getGameOrThrow(request.gameId());
            Move userMove = moveService.convertToEnum(request.move(), Move.class);

            GameResult result = gameService.playRound(request.gameId(), userMove);
            Move computerMove = game.getComputerMoves().get(game.getComputerMoves().size() - 1);
            Map<String, Integer> stats = statisticsService.getStatisticsByGameId(request.gameId());

            String message = String.format("You played %s. Computer played %s. Result: %s. Current stats: %s",
                    request.move(), computerMove, result, stats);

            return ResponseEntity.ok(new GameResponse(true, message));
        } catch (GameNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GameResponse(false, e.getMessage()));
        }
    }

    /**
     * Retrieves overall game statistics for a specific game.
     *
     * @param gameId The ID of the game to retrieve statistics for.
     * @return A ResponseEntity containing the game statistics or an error message if the game is not found.
     */
    @GetMapping("/stats/{gameId}")
    public ResponseEntity<GameResponse> getGameStatistics(@PathVariable String gameId) {
        try {
            Map<String, Integer> stats = statisticsService.getStatisticsByGameId(gameId);
            return ResponseEntity.ok(new GameResponse(true, stats.toString()));
        } catch (GameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GameResponse(false, e.getMessage()));
        }
    }

    /**
     * Retrieves the game by ID.
     *
     * @param gameId The ID of the game to retrieve.
     * @return A ResponseEntity containing the game details or an error message if the game is not found.
     */
    @GetMapping("/{gameId}")
    public ResponseEntity<GameResponse> getGameDetails(@PathVariable String gameId) {
        try {
            Game game = gameService.getGameOrThrow(gameId);
            return ResponseEntity.ok(new GameResponse(true, game.toString()));
        } catch (GameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GameResponse(false, e.getMessage()));
        }
    }

    /**
     * Terminates the game with the specified ID.
     *
     * @param gameId The ID of the game to terminate.
     * @return A ResponseEntity indicating success or an error message if the game is not found.
     */
    @DeleteMapping("/terminate/{gameId}")
    public ResponseEntity<GameResponse> terminateGame(@PathVariable String gameId) {
        try {
            gameService.terminateGame(gameId);
            return ResponseEntity.ok(new GameResponse(true, "Game terminated successfully."));
        } catch (GameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GameResponse(false, e.getMessage()));
        }
    }
}
