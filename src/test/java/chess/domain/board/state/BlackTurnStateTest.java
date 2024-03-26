package chess.domain.board.state;

import chess.domain.piece.CampType;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("검은색 말 차례")
public class BlackTurnStateTest {

    private BoardState boardState;

    @BeforeEach
    void setUp() {
        boardState = new BlackTurnState();
    }

    @DisplayName("검은색 말 차례에서 다음 차례로 넘어가면 하얀말 차례를 반환한다.")
    @Test
    void nextState() {
        // when
        BoardState actual = boardState.nextTurnState();

        // then
        assertThat(actual).isInstanceOf(WhiteTurnState.class);
    }

    @DisplayName("검은색 말 차례에서 게임이 종료되면 게임 종료 상태를 반환한다.")
    @Test
    void gameOverState() {
        // when
        BoardState actual = boardState.makeGameOver();

        // then
        assertThat(actual).isInstanceOf(GameOverState.class);
    }

    @DisplayName("출발지의 말이 본인의 진영이고 도착지의 말이 본인의 진영이 아닌지 확인한다.")
    @Test
    void checkMovable() {
        // given
        Piece source = new Piece(PieceType.PAWN, CampType.BLACK);
        Piece destination = new Piece(PieceType.PAWN, CampType.WHITE);

        // when
        boolean actual = boardState.checkMovable(source, destination);

        // then
        assertThat(actual).isTrue();
    }
}
