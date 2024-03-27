package chess;

import chess.domain.board.state.StateName;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GameDao {

    public void createGame() {
        Connection connection = DBConnection.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO game (winner_camp) VALUES (?)");
            statement.setString(1, null);

            statement.executeUpdate();
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
}
