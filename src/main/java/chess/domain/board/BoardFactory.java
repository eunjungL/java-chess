package chess.domain.board;

import chess.domain.piece.CampType;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceType;
import chess.domain.square.File;
import chess.domain.square.Rank;
import chess.domain.square.Square;

import java.util.*;

public class BoardFactory {

    private static final List<PieceType> PIECE_TYPE_ORDER = List.of(PieceType.ROOK, PieceType.KNIGHT, PieceType.BISHOP,
            PieceType.QUEEN, PieceType.KING, PieceType.BISHOP, PieceType.KNIGHT, PieceType.ROOK);
    public static final int EMPTY_PIECE_FROM = 2;
    public static final int EMPTY_PIECE_TO = 6;

    public BoardFactory() {
    }

    public Map<Square, Piece> create() {
        Map<Square, Piece> board = new HashMap<>();

        makeBlackPiece(board);
        makeWhitePiece(board);
        makeEmptyPiece(board);

        return board;
    }

    private void makeEmptyPiece(Map<Square, Piece> expected) {
        for (Rank rank : Arrays.copyOfRange(Rank.values(), EMPTY_PIECE_FROM, EMPTY_PIECE_TO)) {
            makeEmptyPieceByFile(expected, rank);
        }
    }

    private void makeEmptyPieceByFile(Map<Square, Piece> expected, Rank rank) {
        for (File file : File.values()) {
            Piece piece = new Piece(PieceType.EMPTY, CampType.EMPTY);
            savePiece(expected, Square.of(file, rank), piece);
        }
    }

    private void makeBlackPiece(Map<Square, Piece> expected) {
        Iterator<File> fileIterator = Arrays.stream(File.values()).iterator();
        Iterator<PieceType> pieceTypeIterator = PIECE_TYPE_ORDER.iterator();

        while (fileIterator.hasNext() && pieceTypeIterator.hasNext()) {
            File file = fileIterator.next();

            Piece normalPiece = new Piece(pieceTypeIterator.next(), CampType.BLACK);
            savePiece(expected, Square.of(file, Rank.EIGHT), normalPiece);

            Piece pawnPiece = new Piece(PieceType.PAWN, CampType.BLACK);
            savePiece(expected, Square.of(file, Rank.SEVEN), pawnPiece);
        }
    }

    private void makeWhitePiece(Map<Square, Piece> expected) {
        Iterator<File> fileIterator = Arrays.stream(File.values()).iterator();
        Iterator<PieceType> pieceTypeIterator = PIECE_TYPE_ORDER.iterator();

        while (fileIterator.hasNext() && pieceTypeIterator.hasNext()) {
            File file = fileIterator.next();

            Piece normalPiece = new Piece(pieceTypeIterator.next(), CampType.WHITE);
            savePiece(expected, Square.of(file, Rank.ONE), normalPiece);

            Piece pawnPiece = new Piece(PieceType.PAWN, CampType.WHITE);
            savePiece(expected, Square.of(file, Rank.TWO), pawnPiece);
        }
    }

    private void savePiece(Map<Square, Piece> expected, Square square, Piece piece) {
        expected.put(square, piece);
    }
}
