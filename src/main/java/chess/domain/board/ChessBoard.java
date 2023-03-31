package chess.domain.board;

import chess.domain.game.ChessGame;
import chess.domain.piece.Piece;
import chess.domain.piece.TeamColor;
import chess.domain.position.Position;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ChessBoard {
    public static final int INITIAL_KING_COUNT = 2;
    private static final Map<ChessGame, ChessBoard> CACHE = new ConcurrentHashMap<>();

    private final Map<Position, Piece> board;

    public ChessBoard(final Map<Position, Piece> board) {
        this.board = board;
    }

    public static ChessBoard getInstance(final ChessGame chessGame) {
        if (CACHE.containsKey(chessGame)) {
            return CACHE.get(chessGame);
        }
        final ChessBoard chessBoard = new ChessBoard(new ChessBoardFactory().createBoard());
        CACHE.put(chessGame, chessBoard);
        return chessBoard;
    }

    public boolean contains(final Position position) {
        return board.containsKey(position);
    }

    public Piece getPiece(final Position source) {
        return board.get(source);
    }

    public void removePiece(final Position position) {
        board.remove(position);
    }

    public void putPiece(final Position position, final Piece piece) {
        board.put(position, piece);
    }

    public boolean isPossibleRoute(final Position source, final Position target, final TeamColor teamColor) {
        final Position unitPosition = source.computeUnitPosition(target);
        Position currentPosition = Position.copy(source);

        if (isObstructed(target, unitPosition, currentPosition)) {
            return false;
        }

        final Piece targetPiece = board.get(target);
        return targetPiece == null || !targetPiece.isSameTeam(teamColor);
    }

    private boolean isObstructed(final Position target, final Position unitPosition, Position currentPosition) {
        currentPosition = currentPosition.calculate(unitPosition.getRank(), unitPosition.getFile());
        if (currentPosition.equals(target)) {
            return false;
        }
        if (board.containsKey(currentPosition)) {
            return true;
        }
        return isObstructed(target, unitPosition, currentPosition);
    }

    public Map<Position, Piece> getBoard() {
        return Map.copyOf(board);
    }

    public boolean checkKingDie() {
        final double kingCount = board.values().stream()
                .filter(Piece::isKing)
                .count();
        return kingCount != INITIAL_KING_COUNT;
    }
}
