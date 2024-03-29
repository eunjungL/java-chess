package chess.domain.game.service;

import chess.domain.game.dao.GameDao;

import java.util.List;

public class GameService {

    private final GameDao gameDao;

    public GameService(GameDao gameDao) {
        this.gameDao = gameDao;
    }

    public int createGame() {
        return gameDao.save();
    }

    public List<String> findAllId() {
        return gameDao.findAllId().stream()
                .map(String::valueOf)
                .toList();
    }
}
