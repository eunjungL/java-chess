package chess.domain.board.state;

import chess.domain.piece.Piece;

public class WhiteTurnState implements BoardState {

    @Override
    public BoardState nextState() {
        return new BlackTurnState();
    }

    @Override
    public boolean checkMovable(Piece source, Piece destination) {
        return source.isWhite() && (destination.isBlack() || !destination.isNotEmpty());
    }
}
