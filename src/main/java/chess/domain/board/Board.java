package chess.domain.board;

import chess.GameDao;
import chess.domain.board.dao.BoardDao;
import chess.domain.board.dto.BoardOutput;
import chess.domain.board.dto.GameResult;
import chess.domain.board.state.BoardState;
import chess.domain.piece.CampType;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceType;
import chess.domain.square.File;
import chess.domain.square.Rank;
import chess.domain.square.Square;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Predicate;

public class Board {

    private static final String NOT_YOUR_TURN_ERROR = "움직이려고 하는 말이 본인 진영의 말이 아닙니다.";
    private static final String CANNOT_MOVE_ERROR = "해당 경로로는 말을 이동할 수 없습니다.";

    private final int gameId;
    private final BoardDao boardDao;
    private final GameDao gameDao;
    private final Map<Square, Piece> board;
    private BoardState boardState;

    public Board() {
        this.boardDao = new BoardDao();
        this.gameDao = new GameDao();

        this.gameId = gameDao.save();
        this.board = makeBoard(gameId);
    }

    public Board(int gameId) {
        this.boardDao = new BoardDao();
        this.gameDao = new GameDao();

        this.gameId = gameId;
        board = makeBoard(gameId);
    }

    private Map<Square, Piece> makeBoard(int gameId) {
        final Map<Square, Piece> board = new BoardFactory().create(gameId);
        this.boardState = gameDao.findStateById(gameId);

        return board;
    }

    public BoardOutput toBoardOutput() {
        return new BoardOutput(board);
    }

    public Piece findPieceBySquare(Square square) {
        return board.get(square);
    }

    public void movePiece(Square source, Square destination) {
        checkMovable(source, destination);
        move(source, destination);
    }

    private void checkMovable(Square source, Square destination) {
        Piece sourcePiece = board.get(source);
        Piece destinationPiece = board.get(destination);

        checkTurn(sourcePiece, destinationPiece);
        checkCanMove(source, destination, sourcePiece);
    }

    private void checkTurn(Piece sourcePiece, Piece destinationPiece) {
        if (!boardState.checkMovable(sourcePiece, destinationPiece)) {
            throw new IllegalArgumentException(NOT_YOUR_TURN_ERROR);
        }
    }

    private void checkCanMove(Square source, Square destination, Piece sourcePiece) {
        if (!sourcePiece.canMove(source, destination, this)) {
            throw new IllegalArgumentException(CANNOT_MOVE_ERROR);
        }
    }

    private void move(Square source, Square destination) {
        Piece sourcePiece = board.get(source);
        Piece destinationPiece = board.get(destination);

        if (destinationPiece.isNotEmpty()) {
            updateBoard(source, destination, sourcePiece, new Piece(PieceType.EMPTY, CampType.EMPTY));
            boardState = checkGameOver(destinationPiece);
            gameDao.update(gameId, boardState.getSateName());
            return;
        }

        updateBoard(source, destination, sourcePiece, destinationPiece);
        boardState = boardState.nextTurnState();
        gameDao.update(gameId, boardState.getSateName());
    }

    private void updateBoard(Square source, Square destination, Piece sourcePiece, Piece destinationPiece) {
        board.replace(source, destinationPiece);
        board.replace(destination, sourcePiece);
        boardDao.update(gameId, source, destinationPiece);
        boardDao.update(gameId, destination, sourcePiece);
    }

    private BoardState checkGameOver(Piece destinationPiece) {
        if (destinationPiece.isKing()) {
            BoardState gameOverState = boardState.makeGameOver();
            gameDao.update(gameId, gameOverState.findWinner());

            return gameOverState;
        }

        return boardState.nextTurnState();
    }

    public GameResult createGameResult() {
        double whiteScore = calculateScoreByCamp(Piece::isWhite);
        double blackScore = calculateScoreByCamp(Piece::isBlack);

        return new GameResult(boardState.findWinner(), whiteScore, blackScore);
    }

    private double calculateScoreByCamp(Predicate<Piece> filterByCamp) {
        double totalScore = 0;

        for (File file : File.values()) {
            double pawnScore = calculatePawnScore(filterByCamp, file);
            double notPawnScore = calculateNotPawnScore(filterByCamp, file);

            totalScore += (pawnScore + notPawnScore);
        }

        return totalScore;
    }

    private double calculatePawnScore(Predicate<Piece> filterByCamp, File file) {
        long countDuplicatedPawn = countDuplicatedPawnInFile(filterByCamp, file);

        return Arrays.stream(Rank.values())
                .map(rank -> board.get(Square.of(file, rank)))
                .filter(filterByCamp)
                .filter(Piece::isPawn)
                .mapToDouble(piece -> piece.calculateScore(countDuplicatedPawn))
                .sum();
    }

    private double calculateNotPawnScore(Predicate<Piece> filterByCamp, File file) {
        return Arrays.stream(Rank.values())
                .map(rank -> board.get(Square.of(file, rank)))
                .filter(filterByCamp)
                .filter(piece -> !piece.isPawn())
                .mapToDouble(Piece::calculateScore)
                .sum();
    }

    private long countDuplicatedPawnInFile(Predicate<Piece> filterByColor, File file) {
        return Arrays.stream(Rank.values())
                .map(rank -> board.get(Square.of(file, rank)))
                .filter(filterByColor)
                .filter(Piece::isPawn)
                .count();
    }
}
