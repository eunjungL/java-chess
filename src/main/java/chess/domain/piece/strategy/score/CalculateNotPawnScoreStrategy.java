package chess.domain.piece.strategy.score;

public class CalculateNotPawnScoreStrategy implements CalculateScoreStrategy {

    private final double score;

    public CalculateNotPawnScoreStrategy(double score) {
        this.score = score;
    }

    @Override
    public double calculate(long duplicatedPawnCount) {
        return score;
    }
}
