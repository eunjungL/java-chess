package chess.domain.piece.strategy;

import chess.domain.board.Board;
import chess.domain.board.BoardFactory;
import chess.domain.piece.strategy.legalmove.LegalMoveCheckStrategy;
import chess.domain.piece.strategy.legalmove.QueenLegalMoveCheckStrategy;
import chess.domain.square.File;
import chess.domain.square.Rank;
import chess.domain.square.Square;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("퀸 움직임 전략")
class QueenLegalMoveCheckStrategyTest {

    private LegalMoveCheckStrategy legalMoveCheckStrategy;
    private Board board;

    @BeforeEach
    void setUp() {
        legalMoveCheckStrategy = new QueenLegalMoveCheckStrategy();
        board = new Board(new BoardFactory().create());
    }

    @DisplayName("퀸은 수평 이동이 들어오면 이동 가능을 반환한다.")
    @Test
    void horizontalMove() {
        // given
        Square source = Square.of(File.D, Rank.SIX);
        Square destination = Square.of(File.H, Rank.SIX);

        // when
        boolean actual = legalMoveCheckStrategy.check(source, destination, board);

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("퀸은 수직 이동이 들어오면 이동 가능을 반환한다.")
    @Test
    void verticalMove() {
        // given
        Square source = Square.of(File.D, Rank.SIX);
        Square destination = Square.of(File.D, Rank.THREE);

        // when
        boolean actual = legalMoveCheckStrategy.check(source, destination, board);

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("퀸은 대각 이동이 들어오면 이동 가능을 반환한다.")
    @Test
    void diagonalMove() {
        // given
        Square source = Square.of(File.D, Rank.SIX);
        Square destination = Square.of(File.G, Rank.THREE);

        // when
        boolean actual = legalMoveCheckStrategy.check(source, destination, board);

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("퀸은 잘못된 이동이 들어오면 이동 불가능을 반환한다.")
    @Test
    void cannotMove() {
        // given
        Square source = Square.of(File.D, Rank.EIGHT);
        Square destination = Square.of(File.C, Rank.SIX);

        // when
        boolean actual = legalMoveCheckStrategy.check(source, destination, board);

        // then
        assertThat(actual).isFalse();
    }
}
