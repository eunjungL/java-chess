package chess.domain.piece;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

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

    @DisplayName("체스말은 타입에 따른 점수를 반환한다.")
    @Test
    void calculateScore() {
        // given
        Piece queen = new Piece(PieceType.QUEEN, CampType.WHITE);
        List<Piece> piecesByFile = List.of(
                new Piece(PieceType.QUEEN, CampType.WHITE),
                new Piece(PieceType.PAWN, CampType.WHITE),
                new Piece(PieceType.PAWN, CampType.BLACK),
                new Piece(PieceType.QUEEN, CampType.BLACK)
        );

        // when
        double actual = queen.calculateScore(piecesByFile);

        // then
        assertThat(actual).isEqualTo(9);
    }
}
