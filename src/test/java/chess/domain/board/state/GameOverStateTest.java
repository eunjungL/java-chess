package chess.domain.board.state;

import chess.domain.piece.CampType;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("게임 종료 상태")
class GameOverStateTest {

    private GameProgressState gameProgressState;

    @BeforeEach
    void setUp() {
        gameProgressState = new GameOverState(CampType.WHITE);
    }

    @DisplayName("게임 종료 상태에서 상태 변환을 시도하면 예외가 발생한다.")
    @Test
    void nextStateException() {
        // when & then
        assertThatThrownBy(() -> gameProgressState.nextTurnState())
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @DisplayName("게임 종료 상태에서 움직임 가능 여부를 확인하면 예외가 발생한다.")
    @Test
    void checkMovableException() {
        // given
        Piece source = new Piece(PieceType.PAWN, CampType.WHITE);
        Piece destination = new Piece(PieceType.EMPTY, CampType.EMPTY);

        // when & then
        assertThatThrownBy(() -> gameProgressState.checkMovable(source, destination))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @DisplayName("게임 종료 상태에서 우승자를 반환한다. ")
    @Test
    void findWinner() {
        // when
        CampType actual = gameProgressState.findWinner();

        // then
        assertThat(actual).isEqualTo(CampType.WHITE);
    }
}
