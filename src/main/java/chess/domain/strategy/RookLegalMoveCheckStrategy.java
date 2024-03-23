package chess.domain.strategy;

import chess.domain.board.Board;
import chess.domain.square.Square;
import chess.domain.square.dto.SquareDifferent;

public class RookLegalMoveCheckStrategy implements LegalMoveCheckStrategy {

    private final BlockedPathCheckStrategy pathFindStrategy;

    public RookLegalMoveCheckStrategy() {
        this.pathFindStrategy = new BlockedPathCheckStrategy();
    }

    @Override
    public boolean check(Square source, Square destination, Board board) {
        SquareDifferent diff = source.calculateDiff(destination);

        if (!pathFindStrategy.check(source, destination, board)) {
            return false;
        }

        return Math.abs(diff.rankDiff()) == 0 || Math.abs(diff.fileDiff()) == 0;
    }
}
