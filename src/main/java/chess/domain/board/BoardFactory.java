package chess.domain.board;

import chess.domain.square.File;
import chess.domain.square.Square;
import chess.domain.square.Rank;
import chess.domain.piece.CampType;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceType;

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
            for (File file : File.values()) {
                expected.put(Square.of(file, rank), new Piece(PieceType.EMPTY, CampType.EMPTY));
            }
        }
    }

    private void makeBlackPiece(Map<Square, Piece> expected) {
        Iterator<PieceType> pieceTypeIterator = PIECE_TYPE_ORDER.iterator();
        Iterator<File> fileIterator = Arrays.stream(File.values()).iterator();

        while (fileIterator.hasNext() && pieceTypeIterator.hasNext()) {
            File file = fileIterator.next();
            expected.put(Square.of(file, Rank.EIGHT), new Piece(pieceTypeIterator.next(), CampType.BLACK));
            expected.put(Square.of(file, Rank.SEVEN), new Piece(PieceType.PAWN, CampType.BLACK));
        }
    }

    private void makeWhitePiece(Map<Square, Piece> expected) {
        Iterator<File> fileIterator = Arrays.stream(File.values()).iterator();
        Iterator<PieceType> pieceTypeIterator = PIECE_TYPE_ORDER.iterator();

        while (fileIterator.hasNext() && pieceTypeIterator.hasNext()) {
            File file = fileIterator.next();
            expected.put(Square.of(file, Rank.ONE), new Piece(pieceTypeIterator.next(), CampType.WHITE));
            expected.put(Square.of(file, Rank.TWO), new Piece(PieceType.PAWN, CampType.WHITE));
        }
    }
}
