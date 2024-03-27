package chess;

import chess.domain.board.Board;
import chess.domain.board.dto.GameResult;
import chess.domain.piece.Piece;
import chess.domain.square.File;
import chess.domain.square.Rank;
import chess.domain.square.Square;
import chess.domain.board.dto.MoveCommand;
import chess.domain.square.dto.SquareCreateCommand;
import chess.util.RetryUtil;
import chess.view.InputView;
import chess.view.OutputView;
import chess.view.PieceName;
import chess.view.dto.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChessGame {

    private final InputView inputView;
    private final OutputView outputView;

    public ChessGame() {
        this.inputView = new InputView();
        this.outputView = new OutputView();
    }

    public void play() {
        Command progressCommand = RetryUtil.retryUntilNoException(inputView::readProgressCommand);

        if (progressCommand.isEndCommand()) {
            return;
        }

        int gameId = 1; // TODO: gameId 1로 하드 코딩 된 부분 처리
        Board board = new Board(gameId);
        printBoardOutput(board);

        playUntilEnd(board);
    }

    private void playUntilEnd(Board board) {
        boolean run = true;

        while (run) {
            run = RetryUtil.retryUntilNoException(() -> loopWhileEnd(board));
        }
    }

    private boolean loopWhileEnd(Board board) {
        Command command = inputView.readCommand();

        if (command.isEndCommand()) {
            return false;
        }

        if (command.isStatusCommand()) {
            printGameResult(board);
            return true;
        }

        movePiece(board, createMoveCommand(command));
        return true;
    }

    private void printGameResult(Board board) {
        GameResult gameResult = board.createGameResult();
        outputView.writeGameResult(gameResult);
    }

    private MoveCommand createMoveCommand(Command command) {
        Square source = Square.from(new SquareCreateCommand(command.source()));
        Square destination = Square.from(new SquareCreateCommand(command.destination()));

        return new MoveCommand(source, destination);
    }

    private void movePiece(Board board, MoveCommand moveCommand) {
        board.move(moveCommand.source(), moveCommand.destination());
        printBoardOutput(board);
    }

    private void printBoardOutput(Board board) {
        Map<Square, Piece> boardOutput = board.toBoardOutput().board();
        List<String> output = new ArrayList<>();

        for (Rank rank : Rank.reverse()) {
            output.add(makeRankOutput(rank, boardOutput));
        }

        outputView.writeBoard(output);
    }

    private String makeRankOutput(Rank rank, Map<Square, Piece> boardOutput) {
        StringBuilder stringBuilder = new StringBuilder();

        for (File file : File.values()) {
            Square square = Square.of(file, rank);
            Piece piece = boardOutput.get(square);

            String pieceName = PieceName.findName(piece);
            stringBuilder.append(pieceName);
        }

        return stringBuilder.toString();
    }
}
