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

    private final PieceDao pieceDao;

    public BoardDao() {
        this.pieceDao = new PieceDao();
    }

    public void savePieceBySquare(int gameId, Square square, Piece piece) {
        Connection connection = DBConnection.getConnection();

        try {
            int pieceId = pieceDao.findPieceIdByPiece(piece)
                    .orElseGet(() -> createNewPiece(piece));

            PreparedStatement statement = connection.prepareStatement("INSERT INTO board (game_id, square, piece_id) VALUES (?, ?, ?)");
            statement.setString(1, String.valueOf(gameId));
            statement.setString(2, square.getKey());
            statement.setString(3, String.valueOf(pieceId));

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Board 테이블에 정보를 저장하던 중 오류가 발생했습니다.");
        }
    }

    private int createNewPiece(Piece piece) {
        pieceDao.createPiece(piece);

        return pieceDao.findPieceIdByPiece(piece)
                .orElseThrow(() -> new RuntimeException("piece를 새로 생성하고 가져오는 과정에서 오류가 발생했습니다."));
    }

    public Optional<Map<Square, Piece>> findBoardByGameId(int gameId) {
        Connection connection = DBConnection.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM board WHERE game_id = ?");
            statement.setString(1, String.valueOf(gameId));

            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                return Optional.empty();
            }

            Map<Square, Piece> board = new HashMap<>();
            while (resultSet.next()) {
                Square square = Square.from(resultSet.getString("square"));
                Piece piece = pieceDao.findPieceById(resultSet.getString("piece_id"));

                board.put(square, piece);
            }

            return Optional.of(board);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateBoardBySquare(int gameId, Square square, Piece piece) {
        Connection connection = DBConnection.getConnection();

        int pieceId = pieceDao.findPieceIdByPiece(piece)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 체스말입니다."));

        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE board SET piece_id = ? WHERE game_id = ? AND square = ?");
            statement.setString(1, String.valueOf(pieceId));
            statement.setString(2, String.valueOf(gameId));
            statement.setString(3, square.getKey());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
