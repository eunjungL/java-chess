package chess.domain.board.dao;

import chess.domain.board.Board;
import chess.domain.piece.CampType;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceType;
import chess.domain.square.File;
import chess.domain.square.Rank;
import chess.domain.square.Square;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("보드 DAO")
class BoardDaoTest {

    private BoardDao boardDao;
    private Board board;

    @BeforeEach
    void setUp() {
        boardDao = new BoardDao();
        board = new Board();
    }

    @DisplayName("보드 DAO는 위치에 맞게 체스말을 저장한다.")
    @Test
    void savePieceBySquare() {
        // given
        Square square = Square.of(File.A, Rank.SEVEN);
        Piece piece = new Piece(PieceType.PAWN, CampType.BLACK);

        // when & then
        assertThatCode(() -> boardDao.savePieceBySquare(1, square, piece)).doesNotThrowAnyException();
    }

    @DisplayName("보드 DAO는 id에 맞는 체스판을 제공한다.")
    @Test
    void findBoardById() {
        // given
        int id = 1;

        // when
        Optional<Map<Square, Piece>> board = boardDao.findBoardById(id);

        // then
        assertThat(board.get().size()).isEqualTo(64);
    }

    @DisplayName("보드 DAO는 주어진 정보에 따라 테이블을 업데이트한다.")
    @Test
    void updateBoardBySquare() {
        // given
        int boardId = 1;

        Square source = Square.of(File.A, Rank.SEVEN);
        Square destination = Square.of(File.A, Rank.FIVE);

        Piece sourcePiece = board.findPieceBySquare(source);
        Piece destinationPiece = board.findPieceBySquare(destination);

        // when & then
        assertAll(
                () -> boardDao.updateBoardBySquare(boardId, source, destinationPiece),
                () -> boardDao.updateBoardBySquare(boardId, destination, sourcePiece)
        );
    }
}
