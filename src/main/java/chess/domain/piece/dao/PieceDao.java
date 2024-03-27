package chess.domain.piece.dao;

import chess.DBConnection;
import chess.domain.piece.CampType;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class PieceDao {

    public void createPiece(Piece piece) {
        Connection connection = DBConnection.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO piece (type, camp) VALUES (?, ?)");
            statement.setString(1, piece.getPieceType().name());
            statement.setString(2, piece.getCampType().name());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("DB에 Piece를 저장하는 중 오류가 발생했습니다.");
        }
    }

    public Optional<Integer> findPieceIdByPiece(Piece piece) {
        Connection connection = DBConnection.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM piece WHERE type = ? AND camp =?");
            statement.setString(1, piece.getPieceType().name());
            statement.setString(2, piece.getCampType().name());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(Integer.valueOf(resultSet.getString("id")));
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Piece findPieceById(String id) {
        Connection connection = DBConnection.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM piece WHERE id = ?");
            statement.setString(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                throw new IllegalArgumentException("존재하지 않는 piece ID입니다.");
            }

            PieceType pieceType = PieceType.findPieceTypeByName(resultSet.getString("type"));
            CampType campType = CampType.findCampTypeByName(resultSet.getString("camp"));

            return new Piece(pieceType, campType);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
