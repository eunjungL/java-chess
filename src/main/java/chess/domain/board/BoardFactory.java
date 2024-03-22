package chess.domain.board;

import chess.domain.position.File;
import chess.domain.position.Square;
import chess.domain.position.Rank;
import chess.domain.piece.ColorType;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceType;

import java.util.HashMap;
import java.util.Map;

public class BoardFactory {

    public BoardFactory() {
    }

    public Map<Square, Piece> create() {
        Map<Square, Piece> board = new HashMap<>();

        for (Rank rank : Rank.values()) {
            createByRank(rank, board);
        }

        return board;
    }

    private void createByRank(Rank rank, Map<Square, Piece> board) {
        for (File file : File.values()) {
            Square square = Square.of(file, rank);
            Piece piece = new Piece(PieceType.EMPTY, ColorType.EMPTY);

            piece = makePiece(rank, file, piece);

            board.put(square, piece);
        }
    }

    private Piece makePiece(Rank rank, File file, Piece piece) {
        if (rank.equals(Rank.ONE) || rank.equals(Rank.EIGHT)) {
            piece = decideType(file, piece, rank);
        }

        if (rank.equals(Rank.TWO) || rank.equals(Rank.SEVEN)) {
            piece = decideColorType(rank, PieceType.PAWN);
        }

        return piece;
    }

    private Piece decideType(File file, Piece piece, Rank rank) {
        if (file.equals(File.A) || file.equals(File.H)) {
            return decideColorType(rank, PieceType.ROOK);
        }

        if (file.equals(File.B) || file.equals(File.G)) {
            return decideColorType(rank, PieceType.KNIGHT);
        }

        if (file.equals(File.C) || file.equals(File.F)) {
            return decideColorType(rank, PieceType.BISHOP);
        }

        if (file.equals(File.D)) {
            return decideColorType(rank, PieceType.QUEEN);
        }

        if (file.equals(File.E)) {
            return decideColorType(rank, PieceType.KING);
        }

        return piece;
    }

    private Piece decideColorType(Rank rank, PieceType pieceType) {
        if (rank == Rank.ONE || rank == Rank.TWO) {
            return new Piece(pieceType, ColorType.WHITE);
        }

        return new Piece(pieceType, ColorType.BLACK);
    }
}
