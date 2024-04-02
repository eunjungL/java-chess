package chess.domain.piece.strategy.legalmove;

import chess.domain.board.Board;
import chess.domain.square.Square;

public class EmptyLegalMoveCheckStrategy implements LegalMoveCheckStrategy {

    @Override
    public boolean check(Square source, Square destination, Board board) {
        return false;
    }
}
