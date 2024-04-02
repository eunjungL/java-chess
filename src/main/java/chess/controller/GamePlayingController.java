package chess.controller;

import chess.domain.board.Board;
import chess.domain.board.dto.MoveCommand;
import chess.domain.board.service.BoardService;
import chess.domain.square.Square;
import chess.domain.square.dto.SquareCreateCommand;
import chess.util.RetryUtil;
import chess.view.InputView;
import chess.view.OutputView;
import chess.view.dto.Command;

import java.util.List;

public class GamePlayingController {

    private final InputView inputView;
    private final OutputView outputView;
    private final BoardService boardService;

    public GamePlayingController(InputView inputView, OutputView outputView, BoardService boardService) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.boardService = boardService;
    }

    public void play(int gameId) {
        Board board = boardService.createBoard(gameId);
        printBoardOutput(board);

        boolean run = true;
        while (run) {
            run = RetryUtil.retryUntilNoException(() -> loopWhileEnd(gameId, board));
        }
    }

    private boolean loopWhileEnd(int gameId, Board board) {
        Command command = inputView.readCommand();

        if (command.isEndCommand()) {
            return false;
        }

        if (command.isStatusCommand()) {
            outputView.writeGameResult(board.createGameResult());
            return true;
        }

        MoveCommand moveCommand = createMoveCommand(command);
        boardService.move(gameId, board, moveCommand);

        printBoardOutput(board);
        return true;
    }

    private MoveCommand createMoveCommand(Command command) {
        Square source = Square.from(new SquareCreateCommand(command.source()));
        Square destination = Square.from(new SquareCreateCommand(command.destination()));

        return new MoveCommand(source, destination);
    }

    private void printBoardOutput(Board board) {
        List<String> output = boardService.makeBoardOutput(board);
        outputView.writeBoard(output);
    }
}
