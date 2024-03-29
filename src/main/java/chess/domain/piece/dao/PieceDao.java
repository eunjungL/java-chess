package chess.domain.piece.dao;

import chess.util.DBConnection;
import chess.domain.piece.CampType;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceType;

import java.sql.*;
import java.util.Optional;

public class PieceDao implements PieceRepository {

    private static final String SAVE_EXCEPTION = "Piece 테이블에 정보를 저장하던 중 오류가 발생했습니다.";
    private static final String READ_EXCEPTION = "Piece 테이블에서 정보를 가져오던 중 오류가 발생했습니다.";
    private static final String PIECE_NOT_FOUND = "존재하지 않는 piece ID입니다.";

    private final Connection connection;

    public PieceDao() {
        this.connection = DBConnection.getConnection();
    }

    public int save(Piece piece) {
        try {
            PreparedStatement statement = connection
                    .prepareStatement("INSERT INTO piece (type, camp) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, piece.getPieceType().name());
            statement.setString(2, piece.getCampType().name());

            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

            throw new RuntimeException();
        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException(SAVE_EXCEPTION);
        }
    }

    public Optional<Integer> findIdByPiece(Piece piece) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM piece WHERE type = ? AND camp = ?");
            statement.setString(1, piece.getPieceType().name());
            statement.setString(2, piece.getCampType().name());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(resultSet.getInt("id"));
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(READ_EXCEPTION);
        }
    }

    public Piece findById(int id) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM piece WHERE id = ?");
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                PieceType pieceType = PieceType.findByName(resultSet.getString("type"));
                CampType campType = CampType.findByName(resultSet.getString("camp"));

                return new Piece(pieceType, campType);
            }

            throw new IllegalArgumentException(PIECE_NOT_FOUND);
        } catch (SQLException e) {
            throw new RuntimeException(READ_EXCEPTION);
        }
    }
}
