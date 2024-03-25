package chess.domain.board.state;

import chess.domain.piece.Piece;

public class BlackTurnState implements BoardState {

    @Override
    public BoardState nextState() {
        return new WhiteTurnState();
    }

    @Override
    public boolean checkMovable(Piece source, Piece destination) {
        return source.isBlack() && (destination.isWhite() || !destination.isNotEmpty());
    }
}
