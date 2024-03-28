package chess;

import chess.domain.board.state.*;
import chess.domain.piece.CampType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameDao {

    private final Connection connection;

    public GameDao() {
        this.connection = DBConnection.getConnection();
    }

    public int save() {
        try {
            PreparedStatement statement = connection
                    .prepareStatement("INSERT INTO game (winner_camp) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            statement.setNull(1, Types.VARCHAR);

            statement.executeUpdate();

            ResultSet resultSet =  statement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

            throw new RuntimeException();
        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException("Game 테이블에 정보를 저장하던 중 오류가 발생했습니다.");
        }
    }

    public BoardState findStateById(int gameId) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM game WHERE id = ?");
            statement.setInt(1, gameId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String stateName = resultSet.getString("state");
                return makeBoardState(stateName, resultSet);
            }

            throw new IllegalArgumentException("존재하지 않는 게임입니다.");
        } catch (SQLException e) {
            throw new RuntimeException("Game 테이블을 업데이트하던 중 오류가 발생했습니다.");
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
            return new GameOverState(CampType.findByName(winnerCampName));
        }

        throw new IllegalArgumentException("존재하지 않는 상태입니다.");
    }

    public List<String> findAllId() {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT id FROM game");

            ResultSet resultSet = statement.executeQuery();

            List<String> gameIds = new ArrayList<>();
            while (resultSet.next()) {
                gameIds.add(resultSet.getString("id"));
            }

            return gameIds;
        } catch (SQLException e) {
            throw new RuntimeException("Game 테이블에서 정보를 읽어오던 중 발생했습니다.");
        }
    }

    public void update(int gameId, StateName stateName) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE game SET state = ? WHERE id = ?");
            statement.setString(1, stateName.name());
            statement.setInt(2, gameId);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Game 테이블을 업데이트하던 중 오류가 발생했습니다.");
        }

    }

    public void update(int gameId, CampType campType) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE game SET winner_camp = ? WHERE id = ?");
            statement.setString(1, campType.name());
            statement.setInt(2, gameId);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Game 테이블을 업데이트하던 중 오류가 발생했습니다.");
        }
    }
}
