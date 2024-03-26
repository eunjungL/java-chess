package chess.domain.piece;

import chess.domain.board.Board;
import chess.domain.piece.strategy.*;
import chess.domain.square.Square;

public enum PieceType {

    KING(new KingLegalMoveCheckStrategy(), 0),
    QUEEN(new QueenLegalMoveCheckStrategy(), 9),
    ROOK(new RookLegalMoveCheckStrategy(), 5),
    BISHOP(new BishopLegalMoveCheckStrategy(), 3),
    KNIGHT(new KnightLegalMoveCheckStrategy(), 2.5),
    PAWN(new PawnLegalMoveCheckStrategy(), 1),
    EMPTY(new EmptyLegalMoveCheckStrategy(), 0),
    ;

    private static final double PAWN_SCORE_CUNT = 0.5;

    private final LegalMoveCheckStrategy legalMoveCheckStrategy;
    private final double score;

    PieceType(LegalMoveCheckStrategy legalMoveCheckStrategy, double score) {
        this.legalMoveCheckStrategy = legalMoveCheckStrategy;
        this.score = score;
    }

    public boolean canMove(Square source, Square destination, Board board) {
        return legalMoveCheckStrategy.check(source, destination, board);
    }

    public double getScore(){
        return score;
    }
}
