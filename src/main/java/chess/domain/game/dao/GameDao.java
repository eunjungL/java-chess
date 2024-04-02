package chess.domain.game.dao;

import chess.domain.board.state.*;
import chess.domain.piece.CampType;
import chess.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameDao implements GameRepository {

    private static final String SAVE_EXCEPTION = "Game 테이블에 정보를 저장하던 중 오류가 발생했습니다.";
    private static final String GAME_NOT_FOUND = "존재하지 않는 게임입니다.";
    private static final String UPDATE_EXCEPTION = "Game 테이블을 업데이트하던 중 오류가 발생했습니다.";
    private static final String READ_EXCEPTION = "Game 테이블에서 정보를 읽어오던 중 발생했습니다.";

    private final Connection connection;

    public GameDao() {
        this.connection = DBConnection.getConnection();
    }

    @Override
    public int save() {
        try {
            PreparedStatement statement = connection
                    .prepareStatement("INSERT INTO game (winner_camp) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            statement.setNull(1, Types.VARCHAR);

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

    @Override
    public GameProgressState findStateById(int gameId) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM game WHERE id = ?");
            statement.setInt(1, gameId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String stateName = resultSet.getString("state");
                return findGameProgressState(resultSet, stateName);
            }

            throw new IllegalArgumentException(GAME_NOT_FOUND);
        } catch (SQLException e) {
            throw new RuntimeException(READ_EXCEPTION);
        }
    }

    private GameProgressState findGameProgressState(ResultSet resultSet, String stateName) throws SQLException {
        if (resultSet.getString("winner_camp") == null) {
            return StateName.findByStateName(stateName);
        }

        return StateName.findByStateName(stateName, resultSet.getString("winner_camp"));
    }

    @Override
    public List<Integer> findAllId() {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT id FROM game");

            ResultSet resultSet = statement.executeQuery();

            List<Integer> gameIds = new ArrayList<>();
            while (resultSet.next()) {
                gameIds.add(resultSet.getInt("id"));
            }

            return gameIds;
        } catch (SQLException e) {
            throw new RuntimeException(READ_EXCEPTION);
        }
    }

    @Override
    public void update(int gameId, StateName stateName) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE game SET state = ? WHERE id = ?");
            statement.setString(1, stateName.name());
            statement.setInt(2, gameId);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(UPDATE_EXCEPTION);
        }

    }

    @Override
    public void update(int gameId, CampType campType) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE game SET winner_camp = ? WHERE id = ?");
            statement.setString(1, campType.name());
            statement.setInt(2, gameId);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(UPDATE_EXCEPTION);
        }
    }

    @Override
    public boolean existsById(String gameId) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM game WHERE id = ?");
            statement.setString(1, gameId);

            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(GAME_NOT_FOUND);
        }
    }
}
