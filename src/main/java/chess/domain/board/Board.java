package chess.domain.board;

import chess.domain.board.dto.BoardOutput;
import chess.domain.board.dto.GameResult;
import chess.domain.board.state.BoardState;
import chess.domain.board.state.WhiteTurnState;
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

    private final Map<Square, Piece> board;
    private BoardState boardState;

    public Board() {
        // TODO: boardId 1 하드 코딩 된 부분 처리
        this.board = new BoardFactory().create(1);
        this.boardState = new WhiteTurnState();
    }

    public BoardOutput toBoardOutput() {
        return new BoardOutput(board);
    }

    public Piece findPieceBySquare(Square square) {
        return board.get(square);
    }

    public void move(Square source, Square destination) {
        checkMovable(source, destination);
        moveOrCatch(source, destination);
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

    private void moveOrCatch(Square source, Square destination) {
        Piece sourcePiece = board.get(source);
        Piece destinationPiece = board.get(destination);

        if (destinationPiece.isNotEmpty()) {
            board.replace(source, new Piece(PieceType.EMPTY, CampType.EMPTY));
            board.replace(destination, sourcePiece);
            boardState = checkGameOver(destinationPiece);
            return;
        }

        board.replace(source, destinationPiece);
        board.replace(destination, sourcePiece);
        boardState = boardState.nextTurnState();
    }

    private BoardState checkGameOver(Piece destinationPiece) {
        if (destinationPiece.isKing()) {
            return boardState.makeGameOver();
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
