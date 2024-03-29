package chess.game.service;

import chess.game.dao.FakeGameDao;
import chess.domain.game.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("게임 서비스")
class GameServiceTest {

    private GameService gameService;

    @BeforeEach
    void setUp() {
        gameService = new GameService(new FakeGameDao());
    }

    @DisplayName("게임 서비스는 게임을 생성한다.")
    @Test
    void createGame() {
        // when
        int actual = gameService.createGame();;

        // then
        assertThat(actual).isEqualTo(1);
    }

    @DisplayName("게임 서비스는 존재하는 게임의 아이디를 찾는다.")
    @Test
    void findAllId() {
        // given
        gameService.createGame();
        gameService.createGame();

        // when
        List<String> actual = gameService.findAllId();

        // then
        assertThat(actual).isEqualTo(List.of("1", "2"));
    }

    @DisplayName("게임 서비스는 게임의 존재 여부를 확인한다.")
    @Test
    void existById() {
        // given
        int gameId = gameService.createGame();

        // when
        boolean actual = gameService.existById(String.valueOf(gameId));

        // then
        assertThat(actual).isTrue();
    }
}
