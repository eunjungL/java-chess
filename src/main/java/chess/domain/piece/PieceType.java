package chess.domain.piece;

import chess.domain.board.Board;
import chess.domain.piece.strategy.*;
import chess.domain.square.Square;

import java.util.Arrays;

public enum PieceType {

    KING(new KingLegalMoveCheckStrategy(), 0, true),
    QUEEN(new QueenLegalMoveCheckStrategy(), 9, false),
    ROOK(new RookLegalMoveCheckStrategy(), 5, false),
    BISHOP(new BishopLegalMoveCheckStrategy(), 3, false),
    KNIGHT(new KnightLegalMoveCheckStrategy(), 2.5, false),
    PAWN(new PawnLegalMoveCheckStrategy(), 1, false),
    EMPTY(new EmptyLegalMoveCheckStrategy(), 0, false),
    ;

    private static final double PAWN_PENALTY_SCORE = 0.5;
    public static final String PIECE_TYPE_NOT_FOUND = "존재하지 않는 체스말 종류입니다.";

    private final LegalMoveCheckStrategy legalMoveCheckStrategy;
    private final double score;
    private final boolean isGameOver;

    PieceType(LegalMoveCheckStrategy legalMoveCheckStrategy, double score, boolean isGameOver) {
        this.legalMoveCheckStrategy = legalMoveCheckStrategy;
        this.score = score;
        this.isGameOver = isGameOver;
    }

    public boolean canMove(Square source, Square destination, Board board) {
        return legalMoveCheckStrategy.check(source, destination, board);
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public static PieceType findByName(String name) {
        return Arrays.stream(PieceType.values())
                .filter(pieceType -> pieceType.name().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(PIECE_TYPE_NOT_FOUND));
    }

    public double getScore() {
        return score;
    }

    public double getScore(double duplicatedPawnCount) {
        if (this.equals(PieceType.PAWN) && duplicatedPawnCount > 1) {
            return PAWN_PENALTY_SCORE;
        }

        return score;
    }
}
