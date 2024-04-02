package chess.domain.piece;

import chess.domain.board.Board;
import chess.domain.square.Square;

import java.util.List;

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

    public boolean canMove(Square source, Square destination, Board board) {
        return pieceType.canMove(source, destination, board);
    }

    public double calculateScore(List<Piece> piecesByFile) {
        if (!pieceType.equals(PieceType.PAWN)) {
            return pieceType.calculateScore(0);
        }

        long duplicatedPawnCount = piecesByFile.stream()
                .filter(piece -> piece.pieceType.equals(PieceType.PAWN))
                .count();

        return pieceType.calculateScore(duplicatedPawnCount);
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public CampType getCampType() {
        return campType;
    }
}
