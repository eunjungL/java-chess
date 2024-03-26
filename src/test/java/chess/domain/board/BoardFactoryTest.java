package chess.domain.board;

import chess.domain.square.File;
import chess.domain.square.Rank;
import chess.domain.square.Square;
import chess.domain.piece.CampType;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("체스판 생성")
public class BoardFactoryTest {

    @DisplayName("체스판을 생성한다.")
    @Test
    void createBoard() {
        // given
        Map<Square, Piece> expected = createExpectedBoard();
        BoardFactory boardFactory = new BoardFactory();

        // when
        Map<Square, Piece> actual = boardFactory.create();

        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    private Map<Square, Piece> createExpectedBoard() {
        Map<Square, Piece> expected = new HashMap<>();

        expected.put(Square.of(File.A, Rank.ONE), new Piece(PieceType.ROOK, CampType.WHITE));
        expected.put(Square.of(File.B, Rank.ONE), new Piece(PieceType.KNIGHT, CampType.WHITE));
        expected.put(Square.of(File.C, Rank.ONE), new Piece(PieceType.BISHOP, CampType.WHITE));
        expected.put(Square.of(File.D, Rank.ONE), new Piece(PieceType.QUEEN, CampType.WHITE));
        expected.put(Square.of(File.E, Rank.ONE), new Piece(PieceType.KING, CampType.WHITE));
        expected.put(Square.of(File.F, Rank.ONE), new Piece(PieceType.BISHOP, CampType.WHITE));
        expected.put(Square.of(File.G, Rank.ONE), new Piece(PieceType.KNIGHT, CampType.WHITE));
        expected.put(Square.of(File.H, Rank.ONE), new Piece(PieceType.ROOK, CampType.WHITE));

        for (File file : File.values()) {
            expected.put(Square.of(file, Rank.TWO), new Piece(PieceType.PAWN, CampType.WHITE));
        }

        expected.put(Square.of(File.A, Rank.EIGHT), new Piece(PieceType.ROOK, CampType.BLACK));
        expected.put(Square.of(File.B, Rank.EIGHT), new Piece(PieceType.KNIGHT, CampType.BLACK));
        expected.put(Square.of(File.C, Rank.EIGHT), new Piece(PieceType.BISHOP, CampType.BLACK));
        expected.put(Square.of(File.D, Rank.EIGHT), new Piece(PieceType.QUEEN, CampType.BLACK));
        expected.put(Square.of(File.E, Rank.EIGHT), new Piece(PieceType.KING, CampType.BLACK));
        expected.put(Square.of(File.F, Rank.EIGHT), new Piece(PieceType.BISHOP, CampType.BLACK));
        expected.put(Square.of(File.G, Rank.EIGHT), new Piece(PieceType.KNIGHT, CampType.BLACK));
        expected.put(Square.of(File.H, Rank.EIGHT), new Piece(PieceType.ROOK, CampType.BLACK));

        for (File file : File.values()) {
            expected.put(Square.of(file, Rank.SEVEN), new Piece(PieceType.PAWN, CampType.BLACK));
        }

        for (Rank rank : Arrays.copyOfRange(Rank.values(), 2, 6)) {
            for (File file : File.values()) {
                expected.put(Square.of(file, rank), new Piece(PieceType.EMPTY, CampType.EMPTY));
            }
        }

        return expected;
    }
}
