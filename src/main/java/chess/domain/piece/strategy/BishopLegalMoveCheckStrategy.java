package chess.domain.piece.strategy;

import chess.domain.board.Board;
import chess.domain.square.Square;
import chess.domain.square.dto.SquareDifferent;

public class BishopLegalMoveCheckStrategy implements LegalMoveCheckStrategy {

    private final BlockedPathCheckStrategy blockedPathCheckStrategy;

    public BishopLegalMoveCheckStrategy() {
        this.blockedPathCheckStrategy = new BlockedPathCheckStrategy();
    }

    @Override
    public boolean check(Square source, Square destination, Board board) {
        SquareDifferent diff = source.calculateDiff(destination);

        if (!blockedPathCheckStrategy.check(source, destination, board)) {
            return false;
        }

        return Math.abs(diff.fileDiff()) - Math.abs(diff.rankDiff()) == 0;
    }
}
