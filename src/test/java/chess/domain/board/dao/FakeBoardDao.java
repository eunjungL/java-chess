package chess.domain.board.dao;

import chess.domain.piece.Piece;
import chess.domain.square.Square;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FakeBoardDao implements BoardRepository {

    private final Map<Integer, Map<Square, Piece>> board;

    public FakeBoardDao() {
        this.board = new HashMap<>();
    }

    @Override
    public void save(int gameId, Square square, Piece piece) {
        Map<Square, Piece> board = this.board.get(gameId);
        board.put(square, piece);
    }

    @Override
    public void saveAll(int gameId, Map<Square, Piece> board) {
        this.board.put(gameId, board);
    }

    @Override
    public Optional<Map<Square, Piece>> findByGameId(int gameId) {
        if (board.containsKey(gameId)) {
            return Optional.of(board.get(gameId));
        }

        return Optional.empty();
    }

    @Override
    public void update(int gameId, Square square, Piece piece) {
        Map<Square, Piece> board = this.board.get(gameId);
        board.replace(square, piece);
    }
}
