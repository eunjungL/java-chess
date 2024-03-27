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

    public Map<Square, Piece> create(int boardId) {
        Optional<Map<Square, Piece>> board = new BoardDao().findBoardById(boardId);

        return board.orElseGet(() -> makeNewBoard(boardId));
    }

    private Map<Square, Piece> makeNewBoard(int boardId) {
        Map<Square, Piece> board = new HashMap<>();

        makeBlackPiece(boardId, board);
        makeWhitePiece(boardId, board);
        makeEmptyPiece(boardId, board);

        return board;
    }

    private void makeEmptyPiece(int boardId, Map<Square, Piece> expected) {
        for (Rank rank : Arrays.copyOfRange(Rank.values(), EMPTY_PIECE_FROM, EMPTY_PIECE_TO)) {
            for (File file : File.values()) {
                Square square = Square.of(file, rank);
                Piece piece = new Piece(PieceType.EMPTY, CampType.EMPTY);
                expected.put(square, piece);
                boardDao.savePieceBySquare(boardId, square, piece);
            }
        }
    }

    private void makeBlackPiece(int boardId, Map<Square, Piece> expected) {
        Iterator<PieceType> pieceTypeIterator = PIECE_TYPE_ORDER.iterator();
        Iterator<File> fileIterator = Arrays.stream(File.values()).iterator();

        while (fileIterator.hasNext() && pieceTypeIterator.hasNext()) {
            File file = fileIterator.next();

            Square square = Square.of(file, Rank.EIGHT);
            Piece piece = new Piece(pieceTypeIterator.next(), CampType.BLACK);
            expected.put(square, piece);
            boardDao.savePieceBySquare(boardId, square, piece);

            square = Square.of(file, Rank.SEVEN);
            piece = new Piece(PieceType.PAWN, CampType.BLACK);
            expected.put(square, piece);
            boardDao.savePieceBySquare(boardId, square, piece);
        }
    }

    private void makeWhitePiece(int boardId, Map<Square, Piece> expected) {
        Iterator<File> fileIterator = Arrays.stream(File.values()).iterator();
        Iterator<PieceType> pieceTypeIterator = PIECE_TYPE_ORDER.iterator();

        while (fileIterator.hasNext() && pieceTypeIterator.hasNext()) {
            File file = fileIterator.next();

            Square square = Square.of(file, Rank.ONE);
            Piece piece = new Piece(pieceTypeIterator.next(), CampType.WHITE);
            expected.put(square, piece);
            boardDao.savePieceBySquare(boardId, square, piece);

            square = Square.of(file, Rank.TWO);
            piece = new Piece(PieceType.PAWN, CampType.WHITE);
            expected.put(square, piece);
            boardDao.savePieceBySquare(boardId, square, piece);
        }
    }
}
