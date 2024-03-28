package chess;

import chess.domain.board.state.BlackTurnStateTest;
import chess.domain.board.state.BoardState;
import chess.domain.board.state.StateName;
import chess.domain.piece.CampType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DisplayName("게임 DAO")
class GameDaoTest {

    private GameDao gameDao;

    @BeforeEach
    void setUp() {
        gameDao = new GameDao();
    }

    @DisplayName("게임 DAO는 새로운 게임을 생성한다.")
    @Test
    void createGame() {
        // when & then
        assertThatCode(() -> gameDao.save()).doesNotThrowAnyException();
    }

    @DisplayName("게임 DAO는 게임의 상태를 변경한다.")
    @Test
    void updateStateById() {
        // given
        int gameId = 1;
        StateName stateName = StateName.BLACK_TURN;

        // when & then
        assertThatCode(() -> gameDao.update(gameId, stateName)).doesNotThrowAnyException();
    }

    @DisplayName("게임 DAO는 우승자를 기록한다.")
    @Test
    void updateWinnerCampById() {
        // given
        int gameId = 1;
        CampType winner = CampType.WHITE;

        // when & then
        assertThatCode(() -> gameDao.update(gameId, winner)).doesNotThrowAnyException();
    }

    @DisplayName("게임 DAO는 게임의 상태를 반환한다.")
    @Test
    void findStateById() {
        // given
        int gameId = 1;
        gameDao.update(gameId, StateName.BLACK_TURN);

        // when
        BoardState actual = gameDao.findStateById(gameId);

        // then
        assertThat(actual).isInstanceOf(BlackTurnStateTest.class);
    }
}
