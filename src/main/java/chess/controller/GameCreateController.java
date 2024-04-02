package chess.controller;

import chess.domain.game.service.GameService;
import chess.view.InputView;
import chess.view.dto.Command;

import java.util.List;

public class GameCreateController {

    private static final String GAME_NOT_FOUND = "존재하지 않는 게임입니다.";

    private final GameService gameService;
    private final InputView inputView;

    public GameCreateController(GameService gameService, InputView inputView) {
        this.gameService = gameService;
        this.inputView = inputView;
    }

    public int createGame() {
        Command gameCommand = readGameCommand();
        return makeGame(gameCommand);
    }

    private Command readGameCommand() {
        List<String> gameIds = gameService.findAllId();
        return inputView.readGameCommand(gameIds);
    }

    private int makeGame(Command gameCommand) {
        if (gameCommand.isCreateCommand()) {
            return gameService.createGame();
        }

        validateExistGame(gameCommand);
        return Integer.parseInt(gameCommand.source());
    }

    private void validateExistGame(Command gameCommand) {
        if (!gameService.existById(gameCommand.source())) {
            throw new IllegalArgumentException(GAME_NOT_FOUND);
        }
    }
}
