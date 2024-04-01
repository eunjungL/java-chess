package chess.domain.game.dao;

import chess.domain.board.state.GameProgressState;
import chess.domain.board.state.StateName;
import chess.domain.piece.CampType;

import java.util.List;

public interface GameRepository {

    int save();

    GameProgressState findStateById(int gameId);

    List<Integer> findAllId();

    void update(int gameId, StateName stateName);

    void update(int gameId, CampType campType);

    boolean existsById(String gameId);
}
