package chess.domain.piece;

import chess.domain.board.Board;
import chess.domain.piece.strategy.*;
import chess.domain.square.Square;

import java.util.Arrays;

public enum PieceType {

    KING(new KingLegalMoveCheckStrategy(), 0),
    QUEEN(new QueenLegalMoveCheckStrategy(), 9),
    ROOK(new RookLegalMoveCheckStrategy(), 5),
    BISHOP(new BishopLegalMoveCheckStrategy(), 3),
    KNIGHT(new KnightLegalMoveCheckStrategy(), 2.5),
    PAWN(new PawnLegalMoveCheckStrategy(), 1),
    EMPTY(new EmptyLegalMoveCheckStrategy(), 0),
    ;

    private static final double PAWN_PENALTY_SCORE = 0.5;

    private final LegalMoveCheckStrategy legalMoveCheckStrategy;
    private final double score;

    PieceType(LegalMoveCheckStrategy legalMoveCheckStrategy, double score) {
        this.legalMoveCheckStrategy = legalMoveCheckStrategy;
        this.score = score;
    }

    public boolean canMove(Square source, Square destination, Board board) {
        return legalMoveCheckStrategy.check(source, destination, board);
    }

    public double getScore() {
        return score;
    }

    public double getScore(double duplicatedPawnCount) {
        if (this.equals(PieceType.PAWN) && duplicatedPawnCount >= 2) {
            return PAWN_PENALTY_SCORE;
        }

        return score;
    }

    public static PieceType findByName(String name) {
        return Arrays.stream(PieceType.values())
                .filter(pieceType -> pieceType.name().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 체스말 종류입니다."));
    }
}
