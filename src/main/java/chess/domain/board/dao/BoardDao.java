package chess.domain.board.dao;

import chess.util.DBConnection;
import chess.domain.piece.Piece;
import chess.domain.piece.dao.PieceDao;
import chess.domain.square.Square;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BoardDao implements BoardRepository {

    private static final String SAVE_EXCEPTION = "Board 테이블에 정보를 저장하던 중 오류가 발생했습니다.";
    private static final String READ_EXCEPTION = "Board 테이블의 정보를 불러오던 중 오류가 발생했습니다.";
    private static final String UPDATE_EXCEPTION = "Board 테이블을 업데이트하던 중 오류가 발생했습니다.";
    private static final String PIECE_NOT_FOUND = "존재하지 않는 체스말입니다.";
    private static final String GAME_NOT_FOUND = "존재하지 않는 게임입니다.";

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
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new IllegalArgumentException(GAME_NOT_FOUND);
        } catch (SQLException e) {
            throw new RuntimeException(SAVE_EXCEPTION);
        }
    }

    public void saveAll(int gameId, Map<Square, Piece> board) {
        try {
            PreparedStatement statement = connection
                    .prepareStatement("INSERT INTO board (game_id, square, piece_id) VALUES (?, ?, ?)");

            for (Map.Entry<Square, Piece> b : board.entrySet()) {
                String squareKey = b.getKey().getKey();
                int pieceId = pieceDao.findIdByPiece(b.getValue())
                        .orElseGet(() -> pieceDao.save(b.getValue()));

                statement.setInt(1, gameId);
                statement.setString(2, squareKey);
                statement.setInt(3, pieceId);

                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(SAVE_EXCEPTION);
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

            return Optional.of(makeResultBoard(resultSet));
        } catch (SQLException e) {
            throw new RuntimeException(READ_EXCEPTION);
        }
    }

    private Map<Square, Piece> makeResultBoard(ResultSet resultSet) throws SQLException {
        Map<Square, Piece> board = new HashMap<>();

        while (resultSet.next()) {
            Square square = Square.from(resultSet.getString("square"));
            Piece piece = pieceDao.findById(resultSet.getInt("piece_id"));

            board.put(square, piece);
        }

        return board;
    }

    public void update(int gameId, Square square, Piece piece) {
        int pieceId = pieceDao.findIdByPiece(piece)
                .orElseThrow(() -> new IllegalArgumentException(PIECE_NOT_FOUND));

        try {
            PreparedStatement statement = connection
                    .prepareStatement("UPDATE board SET piece_id = ? WHERE game_id = ? AND square = ?");
            statement.setInt(1, pieceId);
            statement.setInt(2, gameId);
            statement.setString(3, square.getKey());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(UPDATE_EXCEPTION);
        }
    }
}
