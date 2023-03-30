package chessgame.domain.piece;

import chessgame.domain.Team;
import chessgame.domain.point.Point;

public class King implements Piece {

    private final PieceType pieceType;

    private static final int DISTANCE = 1;

    private final Team team;

    private King(Team team) {
        this.pieceType = PieceType.KING;
        this.team = team;
    }

    public static King from(Team team) {
        return new King(team);
    }

    private boolean isKingMove(Point source, Point target) {
        if (source.isHorizontal(target) && Math.abs(source.fileDistance(target)) == DISTANCE) {
            return true;
        }
        if (source.isVertical(target) && Math.abs(source.rankDistance(target)) == DISTANCE) {
            return true;
        }
        return source.isDiagonal(target) && Math.abs(source.fileDistance(target)) == DISTANCE
                && Math.abs(source.rankDistance(target)) == DISTANCE;
    }

    @Override
    public boolean isMovable(Point source, Point target, boolean hasTarget) {
        return isKingMove(source, target);
    }

    @Override
    public Team team() {
        return team;
    }

    @Override
    public boolean isPiece(PieceType piece) {
        return pieceType.equals(piece);
    }

    @Override
    public double getScore() {
        return pieceType.getScore();
    }

    @Override
    public String toString() {
        return pieceType.getName();
    }
}
