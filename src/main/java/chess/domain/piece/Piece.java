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

    public boolean isSameColor(Piece whitePiece) {
        return campType.equals(whitePiece.campType);
    }

    public boolean isNotEmpty() {
        return !pieceType.equals(PieceType.EMPTY);
    }

    public boolean canMove(Square source, Square destination, Board board) {
        return pieceType.canMove(source, destination, board);
    }

    public boolean isKing() {
        return pieceType.equals(PieceType.KING);
    }

    public boolean isPawn() {
        return pieceType.equals(PieceType.PAWN);
    }

    public double calculateScore() {
        return pieceType.getScore();
    }

    public PieceType getPieceType() {
        return pieceType;
    }
}
