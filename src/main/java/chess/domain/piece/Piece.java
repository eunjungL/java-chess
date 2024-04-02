package chess.domain.piece;

import chess.domain.board.Board;
import chess.domain.square.Square;

public class Piece {

    private final PieceType pieceType;
    private final CampType campType;

    public Piece(PieceType pieceType, CampType campType) {
        this.pieceType = pieceType;
        this.campType = campType;
    }

    public boolean isBlack() {
        return campType.equals(CampType.BLACK);
    }

    public boolean isWhite() {
        return campType.equals(CampType.WHITE);
    }

    public boolean isNotEmpty() {
        return !pieceType.equals(PieceType.EMPTY);
    }

    public boolean isGameOver() {
        return pieceType.isGameOver();
    }

    public boolean isPawn() {
        return pieceType.equals(PieceType.PAWN);
    }

    public boolean canMove(Square source, Square destination, Board board) {
        return pieceType.canMove(source, destination, board);
    }

    public double calculateScore() {
        return pieceType.getScore();
    }

    public double calculateScore(long duplicatedPawnCount) {
        return pieceType.getScore(duplicatedPawnCount);
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public CampType getCampType() {
        return campType;
    }
}
