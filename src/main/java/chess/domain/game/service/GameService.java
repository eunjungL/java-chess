package chess.domain.game.service;

import chess.domain.game.dao.GameRepository;

import java.util.List;

public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public int createGame() {
        return gameRepository.save();
    }

    public List<String> findAllId() {
        return gameRepository.findAllId().stream()
                .map(String::valueOf)
                .toList();
    }

    public boolean existById(String gameId) {
        return gameRepository.existsById(gameId);
    }
}
