package com.example.rps.service;

import com.example.rps.model.GameResult;
import com.example.rps.model.Move;
import com.example.rps.util.MovePredictor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class MoveService {

    private static final Logger log = LoggerFactory.getLogger(MoveService.class);

    private final MovePredictor movePredictor;
    private final Random random = new Random();

    public MoveService(MovePredictor movePredictor) {
        this.movePredictor = movePredictor;
    }

    public Move generateComputerMove() {
        Move predictedMove = movePredictor.predict();
        if (predictedMove == null || random.nextDouble() < 0.2) {
            return Move.values()[random.nextInt(Move.values().length)];
        }
        log.info("Move chosen based on prediction: {}", predictedMove);
        return predictedMove;
    }

    public GameResult determineResult(Move userMove, Move computerMove) {
        if (userMove == computerMove) {
            return GameResult.DRAW;
        }
        return userMove.beats(computerMove) ? GameResult.WIN : GameResult.LOSE;
    }

    public void recordMove(Move move) {
        movePredictor.recordMove(move);
    }

    public <E extends Enum<E>> E convertToEnum(String value, Class<E> enumType) {
        try {
            return Enum.valueOf(enumType, value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid enum value: " + value);
        }
    }
}
