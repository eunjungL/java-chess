package chess.domain.board;

import chess.domain.board.state.BlackTurnState;
import chess.domain.board.state.BoardState;
import chess.domain.board.state.WhiteTurnState;
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
        BoardState actual = boardState.nextState();

        // then
        assertThat(actual).isInstanceOf(WhiteTurnState.class);
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
