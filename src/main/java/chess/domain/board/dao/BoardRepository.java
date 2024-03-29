package chess.domain.board.dao;

import chess.domain.piece.Piece;
import chess.domain.square.Square;

import java.util.Map;
import java.util.Optional;

public interface BoardRepository {

    void save(int gameId, Square square, Piece piece);

    void saveAll(int gameId, Map<Square, Piece> board);

    Optional<Map<Square, Piece>> findByGameId(int gameId);

    void update(int gameId, Square square, Piece piece);
}
