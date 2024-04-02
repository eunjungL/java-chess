package chess.domain.piece;

import chess.domain.board.Board;
import chess.domain.piece.strategy.legalmove.*;
import chess.domain.piece.strategy.score.CalculateNotPawnScoreStrategy;
import chess.domain.piece.strategy.score.CalculatePawnScoreStrategy;
import chess.domain.piece.strategy.score.CalculateScoreStrategy;
import chess.domain.square.Square;

import java.util.Arrays;

public enum PieceType {

    KING(new KingLegalMoveCheckStrategy(), new CalculateNotPawnScoreStrategy(0), true),
    QUEEN(new QueenLegalMoveCheckStrategy(), new CalculateNotPawnScoreStrategy(9), false),
    ROOK(new RookLegalMoveCheckStrategy(), new CalculateNotPawnScoreStrategy(5), false),
    BISHOP(new BishopLegalMoveCheckStrategy(), new CalculateNotPawnScoreStrategy(3), false),
    KNIGHT(new KnightLegalMoveCheckStrategy(), new CalculateNotPawnScoreStrategy(2.5), false),
    PAWN(new PawnLegalMoveCheckStrategy(), new CalculatePawnScoreStrategy(1), false),
    EMPTY(new EmptyLegalMoveCheckStrategy(), new CalculateNotPawnScoreStrategy(0), false),
    ;

    public static final String PIECE_TYPE_NOT_FOUND = "존재하지 않는 체스말 종류입니다.";

    private final LegalMoveCheckStrategy legalMoveCheckStrategy;
    private final CalculateScoreStrategy scoreStrategy;
    private final boolean isGameOver;

    PieceType(LegalMoveCheckStrategy legalMoveCheckStrategy, CalculateScoreStrategy scoreStrategy, boolean isGameOver) {
        this.legalMoveCheckStrategy = legalMoveCheckStrategy;
        this.scoreStrategy = scoreStrategy;
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

    public double calculateScore(long duplicatedPawnCount) {
        return scoreStrategy.calculate(duplicatedPawnCount);
    }
}
