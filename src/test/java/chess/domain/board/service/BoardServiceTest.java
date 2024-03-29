package chess.domain.board.service;

import chess.dao.FakeBoardDao;
import chess.dao.FakeGameDao;
import chess.domain.board.Board;
import chess.domain.board.BoardFactory;
import chess.domain.board.dao.BoardDao;
import chess.domain.board.dto.MoveCommand;
import chess.domain.game.dao.GameDao;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("체스판 서비스")
class BoardServiceTest {

    private BoardService boardService;
    private BoardDao fakeBoardDao;
    private GameDao fakeGameDao;

    @BeforeEach
    void setUp() {
        fakeBoardDao = new FakeBoardDao();
        fakeGameDao = new FakeGameDao();
        boardService = new BoardService(fakeBoardDao, fakeGameDao);
    }

    @DisplayName("체스판 서비스는 체스판을 생성한다.")
    @Test
    void createBoard() {
        // given
        int gameId = fakeGameDao.save();

        // when
        Board actual = boardService.createBoard(gameId);
        Map<Square, Piece> expected = new BoardFactory().create();

        // then
        assertThat(actual.toBoardOutput().board()).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("체스판 서비스는 체스판의 체스말을 움직인다.")
    @Test
    void move() {
        // given
        int gameId = fakeGameDao.save();
        Board board = boardService.createBoard(gameId);
        MoveCommand moveCommand = new MoveCommand(Square.of(File.A, Rank.TWO), Square.of(File.A, Rank.FOUR));

        // when
        boardService.move(gameId, board, moveCommand);
        Piece sourceActual = board.findPieceBySquare(Square.of(File.A, Rank.FOUR));
        Piece destinationActual = board.findPieceBySquare(Square.of(File.A, Rank.TWO));

        // then
        assertAll(
                () -> assertThat(sourceActual.getPieceType()).isEqualTo(PieceType.PAWN),
                () -> assertThat(sourceActual.getCampType()).isEqualTo(CampType.WHITE),
                () -> assertThat(destinationActual.getPieceType()).isEqualTo(PieceType.EMPTY),
                () -> assertThat(destinationActual.getCampType()).isEqualTo(CampType.EMPTY)
        );
    }
}
