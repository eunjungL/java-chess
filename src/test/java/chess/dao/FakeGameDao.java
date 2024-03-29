package chess.dao;

import chess.domain.board.state.*;
import chess.domain.game.dao.GameDao;
import chess.domain.piece.CampType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeGameDao implements GameDao {

    private record GameState(BoardState boardState, CampType winnerCamp) {
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
    public BoardState findStateById(int gameId) {
        GameState gameState = game.get(gameId);
        return gameState.boardState;
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

    private BoardState findBoardStateByName(StateName stateName, CampType winnerCamp) {
        List<BoardState> boardStates = List.of(new WhiteTurnState(), new BlackTurnState(), new GameOverState(winnerCamp));

        return boardStates.stream()
                .filter(boardState -> boardState.getSateName().equals(stateName))
                .findAny()
                .orElseThrow();
    }

    @Override
    public void update(int gameId, CampType campType) {
        GameState gameState = game.get(gameId);
        game.replace(gameId, new GameState(gameState.boardState, campType));
    }

    @Override
    public boolean existsById(String gameId) {
        return game.containsKey(Integer.parseInt(gameId));
    }
}
