package chess.domain.board.dao;

import chess.DBConnection;
import chess.domain.piece.Piece;
import chess.domain.piece.dao.PieceDao;
import chess.domain.square.Square;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BoardDao {

    private final Connection connection;
    private final PieceDao pieceDao;

    public BoardDao() {
        this.pieceDao = new PieceDao();
        this.connection = DBConnection.getConnection();
    }

    public void save(int gameId, Square square, Piece piece) {
        try {
            int pieceId = pieceDao.findIdByPiece(piece)
                    .orElseGet(() -> pieceDao.save(piece));

            PreparedStatement statement = connection
                    .prepareStatement("INSERT INTO board (game_id, square, piece_id) VALUES (?, ?, ?)");
            statement.setInt(1, gameId);
            statement.setString(2, square.getKey());
            statement.setInt(3, pieceId);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Board 테이블에 정보를 저장하던 중 오류가 발생했습니다.");
        }
    }

    public Optional<Map<Square, Piece>> findByGameId(int gameId) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM board WHERE game_id = ?");
            statement.setInt(1, gameId);

            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                return Optional.empty();
            }

            Map<Square, Piece> board = new HashMap<>();
            while (resultSet.next()) {
                Square square = Square.from(resultSet.getString("square"));
                Piece piece = pieceDao.findById(resultSet.getInt("piece_id"));

                board.put(square, piece);
            }

            return Optional.of(board);
        } catch (SQLException e) {
            throw new RuntimeException("Board 테이블의 정보를 불러오던 중 오류가 발생했습니다.");
        }
    }

    public void update(int gameId, Square square, Piece piece) {
        int pieceId = pieceDao.findIdByPiece(piece)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 체스말입니다."));

        try {
            PreparedStatement statement = connection
                    .prepareStatement("UPDATE board SET piece_id = ? WHERE game_id = ? AND square = ?");
            statement.setInt(1, pieceId);
            statement.setInt(2, gameId);
            statement.setString(3, square.getKey());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Board 테이블을 업데이트하던 중 오류가 발생했습니다.");
        }
    }
}
