package chess.domain.piece.strategy;

import chess.domain.board.Board;
import chess.domain.square.Square;

public interface LegalMoveCheckStrategy {

    boolean check(Square source, Square destination, Board board);
}
