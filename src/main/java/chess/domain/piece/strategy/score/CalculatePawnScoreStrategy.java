package chess.domain.piece.strategy.score;

public class CalculatePawnScoreStrategy implements CalculateScoreStrategy {

    private static final double PAWN_PENALTY_SCORE = 0.5;
    private static final int DUPLICATED_BOUND = 1;

    private final double score;

    public CalculatePawnScoreStrategy(double score) {
        this.score = score;
    }

    @Override
    public double calculate(long duplicatedPawnCount) {
        if (duplicatedPawnCount > DUPLICATED_BOUND) {
            return PAWN_PENALTY_SCORE;
        }

        return score;
    }
}
