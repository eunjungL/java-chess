package chess;

import chess.controller.GameCreateController;
import chess.controller.GamePlayingController;
import chess.domain.board.dao.BoardDao;
import chess.domain.board.dao.BoardRepository;
import chess.domain.board.service.BoardService;
import chess.domain.game.dao.GameDao;
import chess.domain.game.dao.GameRepository;
import chess.domain.game.service.GameService;
import chess.util.RetryUtil;
import chess.view.InputView;
import chess.view.OutputView;
import chess.view.dto.Command;

public class Application {

    public static void main(String[] args) {
        GameRepository gameRepository = new GameDao();
        BoardRepository boardRepository = new BoardDao();

        InputView inputView = new InputView();
        OutputView outputView = new OutputView();

        GameService gameService = new GameService(gameRepository);
        BoardService boardService = new BoardService(boardRepository, gameRepository);

        GameCreateController gameCreateController = new GameCreateController(gameService, inputView);
        GamePlayingController gamePlayingController = new GamePlayingController(inputView, outputView, boardService);

        Command progressCommand = RetryUtil.retryUntilNoException(inputView::readProgressCommand);
        if (progressCommand.isEndCommand()) {
            return;
        }

        int gameId = RetryUtil.retryUntilNoException(gameCreateController::createGame);
        gamePlayingController.play(gameId);
    }
}
