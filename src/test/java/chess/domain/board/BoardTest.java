package chess.domain.board;

import chess.domain.board.dto.BoardOutput;
import chess.domain.board.dto.GameResult;
import chess.domain.board.state.GameOverState;
import chess.domain.piece.CampType;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceType;
import chess.domain.square.File;
import chess.domain.square.Rank;
import chess.domain.square.Square;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("체스판")
public class BoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board(new BoardFactory().create());
    }

    @DisplayName("체스판은 목적지에 같은 색의 말이 있으면 예외를 반환한다.")
    @Test
    void checkPieceSameColor() {
        // given
        Square source = Square.of(File.A, Rank.EIGHT);
        Square destination = Square.of(File.A, Rank.SEVEN);

        // when & then
        assertThatThrownBy(() -> board.move(source, destination))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("체스판은 출발지에서 목적지로 말을 옮긴다.")
    @Test
    void move() {
        // given
        Square source = Square.of(File.A, Rank.TWO);
        Square destination = Square.of(File.A, Rank.THREE);

        // when & then
        assertThatCode(() -> board.move(source, destination)).doesNotThrowAnyException();
    }

    @DisplayName("체스판은 목적지로 가는 경로에 다른 말이 있으면 예외가 발생한다.")
    @Test
    void checkPathBlocked() {
        // given
        Square source = Square.of(File.A, Rank.EIGHT);
        Square destination = Square.of(File.A, Rank.FIVE);

        // when & then
        assertThatThrownBy(() -> board.move(source, destination))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("체스판은 목적지로 가는 경로에 다른 말이 없으면 예외가 발생하지 않는다.")
    @Test
    void checkPathNotBlocked() {
        // given
        Square source = Square.of(File.A, Rank.TWO);
        Square destination = Square.of(File.A, Rank.FOUR);

        // when & then
        assertThatCode(() -> board.move(source, destination)).doesNotThrowAnyException();
    }

    @DisplayName("체스판은 말의 규칙에 따라 갈 수 없는 도착지가 들어오면 예외가 발생한다.")
    @Test
    void checkCannotMove() {
        // given
        Square source = Square.of(File.B, Rank.EIGHT);
        Square destination = Square.of(File.C, Rank.THREE);

        // when & then
        assertThatThrownBy(() -> board.move(source, destination))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("체스판은 말의 규칙에 따라 갈 수 있는 도착지가 들어오면 예외가 발생하지 않는다.")
    @Test
    void checkCanMove() {
        // given
        Square source = Square.of(File.B, Rank.ONE);
        Square destination = Square.of(File.C, Rank.THREE);

        // when & then
        assertThatCode(() -> board.move(source, destination)).doesNotThrowAnyException();
    }

    @DisplayName("체스판은 턴에 해당하지 않는 말을 옮기려고 할 때 예외가 발생한다.")
    @Test
    void checkTurn() {
        // given
        Square source = Square.of(File.B, Rank.SEVEN);
        Square destination = Square.of(File.C, Rank.SIX);

        // when & then
        assertThatThrownBy(() -> board.move(source, destination))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("체스판은 말을 이동할 때 도착지에 다른 색의 말이 있으면 말을 잡는다.")
    @Test
    void catchOpponent() {
        // given
        board.move(Square.of(File.G, Rank.TWO), Square.of(File.G, Rank.FOUR));
        board.move(Square.of(File.H, Rank.SEVEN), Square.of(File.H, Rank.FIVE));

        // when
        board.move(Square.of(File.G, Rank.FOUR), Square.of(File.H, Rank.FIVE));

        BoardOutput boardOutput = board.toBoardOutput();
        Piece actual = boardOutput.board().get(Square.of(File.H, Rank.FIVE));

        // then
        assertThat(actual.getPieceType().equals(PieceType.PAWN)
                && actual.getCampType().equals(CampType.WHITE))
                .isTrue();
    }

    @DisplayName("폰이 직선이동으로 말을 잡으려고 하면 예외가 발생한다.")
    @Test
    void pawnStraightCatch() {
        // given
        board.move(Square.of(File.G, Rank.TWO), Square.of(File.G, Rank.FOUR));
        board.move(Square.of(File.G, Rank.SEVEN), Square.of(File.G, Rank.FIVE));

        // when & then
        assertThatThrownBy(() -> board.move(Square.of(File.G, Rank.FOUR), Square.of(File.G, Rank.FIVE)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("체스판은 한 진영의 킹이 잡히면 게임 종료 상태로 변경된다.")
    @Test
    void makeGameOverTurn() {
        // given
        board.move(Square.of(File.F, Rank.TWO), Square.of(File.F, Rank.THREE));
        board.move(Square.of(File.E, Rank.SEVEN), Square.of(File.E, Rank.FIVE));
        board.move(Square.of(File.G, Rank.TWO), Square.of(File.G, Rank.FOUR));
        board.move(Square.of(File.D, Rank.EIGHT), Square.of(File.H, Rank.FOUR));
        board.move(Square.of(File.H, Rank.TWO), Square.of(File.H, Rank.THREE));

        // when
        board.move(Square.of(File.H, Rank.FOUR), Square.of(File.E, Rank.ONE));

        // then
        assertThat(board).extracting("boardState").isInstanceOf(GameOverState.class);
    }

    @DisplayName("체스판은 게임이 끝난 후 남아있는 말들의 점수를 계산한다.")
    @Test
    void calculateScore() {
        // given
        board.move(Square.of(File.F, Rank.TWO), Square.of(File.F, Rank.THREE));
        board.move(Square.of(File.E, Rank.SEVEN), Square.of(File.E, Rank.FIVE));
        board.move(Square.of(File.G, Rank.TWO), Square.of(File.G, Rank.FOUR));
        board.move(Square.of(File.D, Rank.EIGHT), Square.of(File.H, Rank.FOUR));
        board.move(Square.of(File.H, Rank.TWO), Square.of(File.H, Rank.THREE));
        board.move(Square.of(File.H, Rank.FOUR), Square.of(File.E, Rank.ONE));

        GameResult expected = new GameResult(CampType.BLACK, 38, 38);

        // when
        GameResult actual = board.createGameResult();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("보드는 게임이 끝난 후 남아있는 말들의 점수(특수한 폰 점수 포함)를 계산한다.")
    @Test
    void calculateScorePawnScoreCut() {
        // given
        board.move(Square.of(File.B, Rank.TWO), Square.of(File.B, Rank.FOUR));
        board.move(Square.of(File.A, Rank.SEVEN), Square.of(File.A, Rank.FIVE));
        board.move(Square.of(File.B, Rank.FOUR), Square.of(File.A, Rank.FIVE));
        board.move(Square.of(File.C, Rank.SEVEN), Square.of(File.C, Rank.FIVE));
        board.move(Square.of(File.F, Rank.TWO), Square.of(File.F, Rank.THREE));
        board.move(Square.of(File.E, Rank.SEVEN), Square.of(File.E, Rank.FIVE));
        board.move(Square.of(File.G, Rank.TWO), Square.of(File.G, Rank.FOUR));
        board.move(Square.of(File.D, Rank.EIGHT), Square.of(File.H, Rank.FOUR));
        board.move(Square.of(File.H, Rank.TWO), Square.of(File.H, Rank.THREE));
        board.move(Square.of(File.H, Rank.FOUR), Square.of(File.E, Rank.ONE));

        GameResult expected = new GameResult(CampType.BLACK, 37, 37);

        // when
        GameResult actual = board.createGameResult();

        // then
        System.out.println(board);
        assertThat(actual).isEqualTo(expected);
    }
}
