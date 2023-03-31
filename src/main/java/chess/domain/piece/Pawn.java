package chess.domain.piece;

import chess.domain.position.Position;

public class Pawn extends Piece {
    protected static final int PAWN_MAX_MOVE_COUNT = 1;
    protected static final int PAWN_FIRST_MAX_MOVE_COUNT = 2;
    private static final Double SCORE_VALUE = 1.0;
    private static final Double SCORE_VALUE_SAME_FILE = 0.5;
    private static final int PAWN_COUNT_CRITERION_FOR_SCORING = 1;

    Pawn(final PieceType pieceType, final TeamColor teamColor) {
        super(pieceType, teamColor);
    }

    public static Double calculateScore(int count) {
        if (count == PAWN_COUNT_CRITERION_FOR_SCORING) {
            return SCORE_VALUE;
        }
        return SCORE_VALUE_SAME_FILE * count;
    }

    boolean canAttack(Position source, Position target, Piece piece) {
        return false;
    }

    @Override
    public boolean isPawn() {
        return true;
    }

    @Override
    public Double getScoreValue() {
        return null;
    }

    @Override
    public boolean canMove(Position source, Position target, Piece targetPiece) {
        return false;
    }
}
