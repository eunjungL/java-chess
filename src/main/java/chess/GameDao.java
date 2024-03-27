package chess;

import chess.domain.board.state.*;
import chess.domain.piece.CampType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameDao {

    public int createGame() {
        Connection connection = DBConnection.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO game (winner_camp) VALUES (?)",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, null);

            statement.executeUpdate();
            ResultSet resultSet =  statement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

            throw new SQLException();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateStateById(int gameId, StateName stateName) {
        Connection connection = DBConnection.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE game SET state = ? WHERE id = ?");
            statement.setString(1, stateName.name());
            statement.setString(2, String.valueOf(gameId));

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void updateWinnerCampById(int gameId, CampType campType) {
        Connection connection = DBConnection.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE game SET winner_camp = ? WHERE id = ?");
            statement.setString(1, campType.name());
            statement.setString(2, String.valueOf(gameId));

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public BoardState findStateById(int gameId) {
        Connection connection = DBConnection.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM game WHERE id = ?");
            statement.setString(1, String.valueOf(gameId));

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String stateName = resultSet.getString("state");
                return makeBoardState(stateName, resultSet);
            }

            throw new IllegalArgumentException("존재하지 않는 게임입니다.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private BoardState makeBoardState(String stateName, ResultSet resultSet) throws SQLException {
        if (stateName.equals(StateName.BLACK_TURN.name())) {
            return new BlackTurnState();
        }

        if (stateName.equals(StateName.WHITE_TURN.name())) {
            return new WhiteTurnState();
        }

        if (stateName.equals(StateName.GAME_OVER.name())) {
            String winnerCampName = resultSet.getString("winner_camp");
            return new GameOverState(CampType.findCampTypeByName(winnerCampName));
        }

        throw new IllegalArgumentException("존재하지 않는 상태입니다.");
    }

    public List<String> findAllIds() {
        Connection connection = DBConnection.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT id FROM game");
            ResultSet resultSet = statement.executeQuery();

            List<String> gameIds = new ArrayList<>();
            while (resultSet.next()) {
                gameIds.add(resultSet.getString("id"));
            }

            return gameIds;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
