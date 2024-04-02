package chess.domain.board;

import chess.domain.board.dto.BoardOutput;
import chess.domain.board.dto.GameResult;
import chess.domain.board.state.GameProgressState;
import chess.domain.board.state.WhiteTurnState;
import chess.domain.piece.CampType;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceType;
import chess.domain.square.File;
import chess.domain.square.Rank;
import chess.domain.square.Square;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class Board {

    private static final String NOT_YOUR_TURN_EXCEPTION = "움직이려고 하는 말이 본인 진영의 말이 아닙니다.";
    private static final String CANNOT_MOVE_EXCEPTION = "해당 경로로는 말을 이동할 수 없습니다.";

    private final Map<Square, Piece> board;
    private GameProgressState gameProgressState;

    public Board(Map<Square, Piece> board) {
        this.board = board;
        this.gameProgressState = new WhiteTurnState();
    }

    public Board(Map<Square, Piece> board, GameProgressState gameProgressState) {
        this.board = board;
        this.gameProgressState = gameProgressState;
    }

    public Piece move(Square source, Square destination) {
        checkMovable(source, destination);

        Piece sourcePiece = board.get(source);
        Piece destinationPiece = board.get(destination);

        if (destinationPiece.isNotEmpty()) {
            updateBoard(source, destination, sourcePiece, new Piece(PieceType.EMPTY, CampType.EMPTY));
            gameProgressState = checkGameOver(destinationPiece);
            return new Piece(PieceType.EMPTY, CampType.EMPTY);
        }

        updateBoard(source, destination, sourcePiece, destinationPiece);
        gameProgressState = gameProgressState.nextTurnState();
        return destinationPiece;
    }

    private void checkMovable(Square source, Square destination) {
        Piece sourcePiece = board.get(source);
        Piece destinationPiece = board.get(destination);

        checkTurn(sourcePiece, destinationPiece);
        checkCanMove(source, destination, sourcePiece);
    }

    private void checkTurn(Piece sourcePiece, Piece destinationPiece) {
        if (!gameProgressState.checkMovable(sourcePiece, destinationPiece)) {
            throw new IllegalArgumentException(NOT_YOUR_TURN_EXCEPTION);
        }
    }

    private void checkCanMove(Square source, Square destination, Piece sourcePiece) {
        if (!sourcePiece.canMove(source, destination, this)) {
            throw new IllegalArgumentException(CANNOT_MOVE_EXCEPTION);
        }
    }

    private GameProgressState checkGameOver(Piece destinationPiece) {
        if (destinationPiece.isGameOver()) {
            return gameProgressState.makeGameOver();
        }

        return gameProgressState.nextTurnState();
    }

    private void updateBoard(Square source, Square destination, Piece sourcePiece, Piece destinationPiece) {
        board.replace(source, destinationPiece);
        board.replace(destination, sourcePiece);
    }

    public GameResult createGameResult() {
        double whiteScore = calculateScoreByCamp(Piece::isWhite);
        double blackScore = calculateScoreByCamp(Piece::isBlack);

        return new GameResult(gameProgressState.findWinner(), whiteScore, blackScore);
    }

    private double calculateScoreByCamp(Predicate<Piece> filterByCamp) {
        double score = 0;

        for (File file : File.values()) {
            score += calculateScoreByFile(filterByCamp, file, score);
        }

        return score;
    }

    private double calculateScoreByFile(Predicate<Piece> filterByCamp, File file, double score) {
        List<Piece> piecesByFile = Arrays.stream(Rank.values())
                .map(rank -> board.get(Square.of(file, rank)))
                .filter(filterByCamp)
                .toList();

        return piecesByFile.stream()
                .mapToDouble(piece -> piece.calculateScore(piecesByFile))
                .reduce(0, Double::sum);
    }

    public BoardOutput toBoardOutput() {
        return new BoardOutput(board);
    }

    public Piece findPieceBySquare(Square square) {
        return board.get(square);
    }
    
    public GameProgressState getBoardState() {
        return gameProgressState;
    }
}
