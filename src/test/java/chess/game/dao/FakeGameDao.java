package chess.game.dao;

import chess.domain.board.state.*;
import chess.domain.game.dao.GameRepository;
import chess.domain.piece.CampType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeGameDao implements GameRepository {

    private record GameState(GameProgressState gameProgressState, CampType winnerCamp) {
    }

    private final Map<Integer, GameState> game;

    public FakeGameDao() {
        this.game = new HashMap<>();
    }

    @Override
    public int save() {
        int gameId = game.size() + 1;
        GameState gameState = new GameState(new WhiteTurnState(), null);

        game.put(gameId, gameState);
        return gameId;
    }

    @Override
    public GameProgressState findStateById(int gameId) {
        GameState gameState = game.get(gameId);
        return gameState.gameProgressState;
    }

    @Override
    public List<Integer> findAllId() {
        return game.keySet().stream()
                .toList();
    }

    @Override
    public void update(int gameId, StateName stateName) {
        GameState gameState = game.get(gameId);
        game.replace(gameId, new GameState(findBoardStateByName(stateName, gameState.winnerCamp()), gameState.winnerCamp()));
    }

    private GameProgressState findBoardStateByName(StateName stateName, CampType winnerCamp) {
        List<GameProgressState> gameProgressStates = List.of(new WhiteTurnState(), new BlackTurnState(), new GameOverState(winnerCamp));

        return gameProgressStates.stream()
                .filter(boardState -> boardState.getSateName().equals(stateName))
                .findAny()
                .orElseThrow();
    }

    @Override
    public void update(int gameId, CampType campType) {
        GameState gameState = game.get(gameId);
        game.replace(gameId, new GameState(gameState.gameProgressState, campType));
    }

    @Override
    public boolean existsById(String gameId) {
        return game.containsKey(Integer.parseInt(gameId));
    }
}
