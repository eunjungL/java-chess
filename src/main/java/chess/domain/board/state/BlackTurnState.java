package chess.domain.board.state;

import chess.domain.piece.CampType;
import chess.domain.piece.Piece;

public class BlackTurnState implements BoardState {

    @Override
    public BoardState nextTurnState() {
        return new WhiteTurnState();
    }

    @Override
    public boolean checkMovable(Piece source, Piece destination) {
        return source.isBlack() && (destination.isWhite() || !destination.isNotEmpty());
    }

    @Override
    public BoardState makeGameOver() {
        return new GameOverState(CampType.BLACK);
    }
}
