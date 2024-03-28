package chess.domain.piece.dao;

import chess.domain.piece.CampType;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DisplayName("체스말 DAO")
class PieceDaoTest {

    private PieceDao pieceDao;

    @BeforeEach
    void setUp() {
        pieceDao = new PieceDao();
    }

    @DisplayName("체스말 DAO는 새로운 체스말을 DB에 저장한다.")
    @Test
    void createPiece() {
        // given
        Piece piece = new Piece(PieceType.PAWN, CampType.WHITE);

        assertThatCode(() -> pieceDao.save(piece)).doesNotThrowAnyException();
    }

    @DisplayName("체스말 DAO는 타입과 진영을 통해 체스말의 id를 가져온다.")
    @Test
    void findPieceIdByTypeAndCamp() {
        // given
        Piece piece = new Piece(PieceType.ROOK, CampType.BLACK);

        // when
        Optional<Integer> pieceId = pieceDao.findIdByPiece(piece);

        // then
        assertThat(pieceId).isEqualTo(Optional.of(1));
    }
}
