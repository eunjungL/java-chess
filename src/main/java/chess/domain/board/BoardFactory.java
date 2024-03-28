package chess.domain.board;

import chess.domain.board.dao.BoardDao;
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

    private final BoardDao boardDao;

    public BoardFactory() {
        boardDao = new BoardDao();
    }

    public Map<Square, Piece> create(int gameId) {
        Optional<Map<Square, Piece>> board = boardDao.findByGameId(gameId);

        return board.orElseGet(() -> makeNewBoard(gameId));
    }

    private Map<Square, Piece> makeNewBoard(int gameId) {
        Map<Square, Piece> board = new HashMap<>();

        makeBlackPiece(gameId, board);
        makeWhitePiece(gameId, board);
        makeEmptyPiece(gameId, board);

        return board;
    }

    private void makeEmptyPiece(int gameId, Map<Square, Piece> expected) {
        for (Rank rank : Arrays.copyOfRange(Rank.values(), EMPTY_PIECE_FROM, EMPTY_PIECE_TO)) {
            makeEmptyPieceByFile(gameId, expected, rank);
        }
    }

    private void makeEmptyPieceByFile(int gameId, Map<Square, Piece> expected, Rank rank) {
        for (File file : File.values()) {
            Piece piece = new Piece(PieceType.EMPTY, CampType.EMPTY);
            savePiece(gameId, expected, Square.of(file, rank), piece);
        }
    }

    private void makeBlackPiece(int gameId, Map<Square, Piece> expected) {
        Iterator<File> fileIterator = Arrays.stream(File.values()).iterator();
        Iterator<PieceType> pieceTypeIterator = PIECE_TYPE_ORDER.iterator();

        while (fileIterator.hasNext() && pieceTypeIterator.hasNext()) {
            File file = fileIterator.next();

            Piece normalPiece = new Piece(pieceTypeIterator.next(), CampType.BLACK);
            savePiece(gameId, expected, Square.of(file, Rank.EIGHT), normalPiece);

            Piece pawnPiece = new Piece(PieceType.PAWN, CampType.BLACK);
            savePiece(gameId, expected, Square.of(file, Rank.SEVEN), pawnPiece);
        }
    }

    private void makeWhitePiece(int gameId, Map<Square, Piece> expected) {
        Iterator<File> fileIterator = Arrays.stream(File.values()).iterator();
        Iterator<PieceType> pieceTypeIterator = PIECE_TYPE_ORDER.iterator();

        while (fileIterator.hasNext() && pieceTypeIterator.hasNext()) {
            File file = fileIterator.next();

            Piece normalPiece = new Piece(pieceTypeIterator.next(), CampType.WHITE);
            savePiece(gameId, expected, Square.of(file, Rank.ONE), normalPiece);

            Piece pawnPiece = new Piece(PieceType.PAWN, CampType.WHITE);
            savePiece(gameId, expected, Square.of(file, Rank.TWO), pawnPiece);
        }
    }

    private void savePiece(int gameId, Map<Square, Piece> expected, Square square, Piece piece) {
        expected.put(square, piece);
        boardDao.save(gameId, square, piece);
    }
}
