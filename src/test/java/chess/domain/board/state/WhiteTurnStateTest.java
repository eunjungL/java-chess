package chess.domain.board.state;

import chess.domain.piece.CampType;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("흰색 말 차례")
public class WhiteTurnStateTest {

    private GameProgressState gameProgressState;

    @BeforeEach
    void setUp() {
        gameProgressState = new WhiteTurnState();
    }

    @DisplayName("흰색 말 차례에서 다음 차례로 넘어가면 검은 말 차례를 반환한다.")
    @Test
    void nextState() {
        // when
        GameProgressState actual = gameProgressState.nextTurnState();

        // then
        assertThat(actual).isInstanceOf(BlackTurnState.class);
    }

    @DisplayName("흰색 말 차례에서 게임이 종료되면 게임 종료 상태를 반환한다.")
    @Test
    void gameOverState() {
        // when
        GameProgressState actual = gameProgressState.makeGameOver();

        // then
        assertThat(actual).isInstanceOf(GameOverState.class);
    }

    @DisplayName("출발지의 말이 본인의 진영이고 도착지의 말이 본인의 진영이 아닌지 확인한다.")
    @Test
    void checkMovable() {
        // given
        Piece source = new Piece(PieceType.PAWN, CampType.WHITE);
        Piece destination = new Piece(PieceType.PAWN, CampType.BLACK);

        // when
        boolean actual = gameProgressState.checkMovable(source, destination);

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("흰색 말 차례에서 우승자를 찾으려고하면 예외가 발생한다.")
    @Test
    void findWinnerException() {
        // when & then
        assertThatThrownBy(() -> gameProgressState.findWinner())
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
