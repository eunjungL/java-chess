package chess.domain.board.service;

import chess.domain.board.dao.FakeBoardDao;
import chess.game.dao.FakeGameDao;
import chess.domain.board.Board;
import chess.domain.board.BoardFactory;
import chess.domain.board.dao.BoardRepository;
import chess.domain.board.dto.MoveCommand;
import chess.domain.board.state.GameProgressState;
import chess.domain.board.state.GameOverState;
import chess.domain.game.dao.GameRepository;
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
    private BoardRepository fakeBoardRepository;
    private GameRepository fakeGameRepository;
    private int gameId;

    @BeforeEach
    void setUp() {
        fakeBoardRepository = new FakeBoardDao();
        fakeGameRepository = new FakeGameDao();
        boardService = new BoardService(fakeBoardRepository, fakeGameRepository);

        gameId = fakeGameRepository.save();
    }

    @DisplayName("체스판 서비스는 체스판을 생성한다.")
    @Test
    void createBoard() {
        // when
        Board actual = boardService.createBoard(gameId);
        Map<Square, Piece> expected = new BoardFactory().create();

        // then
        assertThat(actual.toBoardOutput().board())
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @DisplayName("체스판 서비스는 체스판의 체스말을 움직인다.")
    @Test
    void move() {
        // given
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

    @DisplayName("체스판 서비스는 게임이 종료되면 정보를 업데이트한다.")
    @Test
    void updateGameWhenGameOver() {
        // given
        Board board = boardService.createBoard(gameId);

        boardService.move(gameId, board, new MoveCommand(Square.of(File.F, Rank.TWO), Square.of(File.F, Rank.THREE)));
        boardService.move(gameId, board, new MoveCommand(Square.of(File.E, Rank.SEVEN), Square.of(File.E, Rank.FIVE)));
        boardService.move(gameId, board, new MoveCommand(Square.of(File.G, Rank.TWO), Square.of(File.G, Rank.FOUR)));
        boardService.move(gameId, board, new MoveCommand(Square.of(File.D, Rank.EIGHT), Square.of(File.H, Rank.FOUR)));
        boardService.move(gameId, board, new MoveCommand(Square.of(File.H, Rank.TWO), Square.of(File.H, Rank.THREE)));
        boardService.move(gameId, board, new MoveCommand(Square.of(File.H, Rank.FOUR), Square.of(File.E, Rank.ONE)));

        // when
        GameProgressState actual = fakeGameRepository.findStateById(gameId);

        // then
        assertThat(actual).isInstanceOf(GameOverState.class);
    }
}
