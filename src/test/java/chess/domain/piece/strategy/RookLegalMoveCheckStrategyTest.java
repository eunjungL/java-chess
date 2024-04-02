package chess.domain.piece.strategy;

import chess.domain.board.Board;
import chess.domain.board.BoardFactory;
import chess.domain.piece.strategy.legalmove.LegalMoveCheckStrategy;
import chess.domain.piece.strategy.legalmove.RookLegalMoveCheckStrategy;
import chess.domain.square.File;
import chess.domain.square.Rank;
import chess.domain.square.Square;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("룩 움직임 전략")
public class RookLegalMoveCheckStrategyTest {

    private LegalMoveCheckStrategy legalMoveCheckStrategy;
    private Board board;

    @BeforeEach
    void setUp() {
        legalMoveCheckStrategy = new RookLegalMoveCheckStrategy();
        board = new Board(new BoardFactory().create());
    }

    @DisplayName("룩은 수평 이동 위치가 들어오면 이동 가능을 반환한다.")
    @Test
    void rookHorizontalDestination() {
        // given
        Square source = Square.of(File.A, Rank.SIX);
        Square destination = Square.of(File.H, Rank.SIX);

        // when
        boolean actual = legalMoveCheckStrategy.check(source, destination, board);

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("룩은 수직 이동 위치가 들어오면 이동 가능을 반환한다.")
    @Test
    void rookVerticalDestination() {
        // given
        Square source = Square.of(File.A, Rank.FIVE);
        Square destination = Square.of(File.A, Rank.TWO);

        // when
        boolean actual = legalMoveCheckStrategy.check(source, destination, board);

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("룩은 잘못된 위치가 들어오면 이동 불가능을 반환한다.")
    @Test
    void rookInCorrectDestination() {
        // given
        Square source = Square.of(File.A, Rank.FIVE);
        Square destination = Square.of(File.B, Rank.TWO);

        // when
        boolean actual = legalMoveCheckStrategy.check(source, destination, board);

        // then
        assertThat(actual).isFalse();
    }
}
