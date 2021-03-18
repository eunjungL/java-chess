package chess.domain.piece;

import chess.domain.position.Position;
import chess.domain.position.Vertical;

import java.util.Arrays;
import java.util.List;

public class Pawn extends Piece {
    private static final List<Direction> POSSIBLE_DIRECTIONS = Arrays.asList(Direction.NORTH, Direction.NORTHEAST, Direction.NORTHWEST,
            Direction.INITIAL_PAWN_NORTH);
    public static final List<Integer> INITIAL_VERTICALS = Arrays.asList(2,7);
    private static final String INITIAL_NAME = "P";

    public Pawn(final boolean isBlack) {
        super(isBlack, INITIAL_NAME);
    }

    @Override
    public boolean canMove(final Position source, final Position target, final Piece piece) {
        final Direction direction = POSSIBLE_DIRECTIONS.stream()
                .filter(possibleDirection -> possibleDirection.isSameDirection(subtractByTeam(source, target)))
                .findAny()
                .orElse(Direction.NOTHING);

        return direction != Direction.NOTHING && checkPossible(direction, piece, source.getVertical());
    }

    private List<Integer> subtractByTeam(Position source, Position target) {
        if (isBlack) {
            return source.subtract(target);
        }
        return target.subtract(source);
    }

    private boolean checkPossible(final Direction direction, final Piece piece, final Vertical vertical) {
        if (direction == Direction.NORTH) {
            return piece.equals(new Blank());
        }
        if (direction == Direction.NORTHEAST || direction == Direction.NORTHWEST) {
            return piece.isOpponent(this);
        }
        if (direction == Direction.INITIAL_PAWN_NORTH) {
            return INITIAL_VERTICALS.contains(vertical.getValue()) && piece.equals(new Blank());
        }
        return false;
    }
}
