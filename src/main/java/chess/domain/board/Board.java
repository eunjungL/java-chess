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
    private static final double DUPLICATED_PAWN_PENALTY = 0.5;

    private final Map<Square, Piece> board;
    private BoardState boardState;

    public Board() {
        this.board = new BoardFactory().create();
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

    private double calculateScoreByCamp(Predicate<Piece> filterByColor) {
        double totalScore = board.values().stream()
                .filter(filterByColor)
                .mapToDouble(Piece::calculateScore)
                .sum();

        return totalScore - countDuplicatedPawn(filterByColor) * DUPLICATED_PAWN_PENALTY;
    }

    private long countDuplicatedPawn(Predicate<Piece> filterByColor) {
        return Arrays.stream(File.values())
                .mapToLong(file -> countDuplicatedPawnInFile(filterByColor, file))
                .sum();
    }

    private long countDuplicatedPawnInFile(Predicate<Piece> filterByColor, File file) {
        long cutPAwnCount = Arrays.stream(Rank.values())
                .map(rank -> board.get(Square.of(file, rank)))
                .filter(filterByColor)
                .filter(Piece::isPawn)
                .count();

        if (cutPAwnCount < 2) {
            return 0;
        }

        return cutPAwnCount;
    }
}
