package chess.domain.piece.strategy.score;

public interface CalculateScoreStrategy {

    double calculate(long duplicatedPawnCount);
}
