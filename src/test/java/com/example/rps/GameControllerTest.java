package com.example.rps;

import com.example.rps.controller.GameController;
import com.example.rps.model.Game;
import com.example.rps.model.GameResult;
import com.example.rps.model.Move;
import com.example.rps.service.GameService;
import com.example.rps.service.MoveService;
import com.example.rps.service.StatisticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameController.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private MoveService moveService;

    @MockBean
    private StatisticsService statisticsService;

    private String gameId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        gameId = UUID.randomUUID().toString();
    }

    @Test
    void testStartGame() throws Exception {
        when(gameService.startNewGame()).thenReturn(gameId);

        mockMvc.perform(MockMvcRequestBuilders.post("/game/start")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.valid").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Game started! Your game ID is: " + gameId));
    }

    @Test
    void testMakeMove() throws Exception {
        Move userMove = Move.ROCK;
        Move computerMove = Move.PAPER;
        GameResult result = GameResult.LOSE;

        Map<String, Integer> stats = new HashMap<>();
        stats.put("wins", 1);
        stats.put("draws", 0);
        stats.put("losses", 1);

        Game game = new Game();
        game.addMove(userMove, computerMove, result);

        when(gameService.getGameOrThrow(gameId)).thenReturn(game);
        when(moveService.convertToEnum("ROCK", Move.class)).thenReturn(userMove);
        when(gameService.playRound(anyString(), any(Move.class))).thenReturn(result);
        when(statisticsService.getStatisticsByGameId(gameId)).thenReturn(stats);

        mockMvc.perform(MockMvcRequestBuilders.post("/game/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"gameId\":\"" + gameId + "\",\"move\":\"ROCK\"}"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.valid").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("You played ROCK. Computer played PAPER. Result: LOSE. Current stats: {wins=1, draws=0, losses=1}"));
    }

    @Test
    void testGetGameStatistics() throws Exception {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("wins", 1);
        stats.put("losses", 1);
        stats.put("draws", 0);

        when(statisticsService.getStatisticsByGameId(gameId)).thenReturn(stats);

        mockMvc.perform(MockMvcRequestBuilders.get("/game/stats/" + gameId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.valid").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(stats.toString()));
    }

    @Test
    void testGetGameDetails() throws Exception {
        Game game = new Game();
        when(gameService.getGameOrThrow(gameId)).thenReturn(game);

        mockMvc.perform(MockMvcRequestBuilders.get("/game/" + gameId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.valid").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(game.toString()));
    }

    @Test
    void testTerminateGame() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/game/terminate/" + gameId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.valid").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Game terminated successfully."));
    }
}
