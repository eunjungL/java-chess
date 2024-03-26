package chess.domain.piece;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DisplayName("체스말")
public class PieceTest {

    @DisplayName("체스말을 생성한다.")
    @Test
    void createPiece() {
        // when & then
        assertThatCode(() -> new Piece(PieceType.KING, CampType.WHITE)).doesNotThrowAnyException();
    }

    @DisplayName("체스말은 같은 색인지 여부를 반환한다.")
    @Test
    void checkSameColor() {
        // given
        Piece blackPiece = new Piece(PieceType.PAWN, CampType.BLACK);
        Piece whitePiece = new Piece(PieceType.ROOK, CampType.WHITE);

        // when
        boolean actual = blackPiece.isSameColor(whitePiece);

        // then
        assertThat(actual).isFalse();
    }

    @DisplayName("체스말은 타입에 따른 점수를 반환한다.")
    @Test
    void calculateScore() {
        // given
        Piece queen = new Piece(PieceType.QUEEN, CampType.WHITE);

        // when
        double actual = queen.calculateScore();

        // then
        assertThat(actual).isEqualTo(9);
    }
}
