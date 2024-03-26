package chess.domain.board.state;

import chess.domain.piece.CampType;
import chess.domain.piece.Piece;

public class WhiteTurnState implements BoardState {

    @Override
    public BoardState nextTurnState() {
        return new BlackTurnState();
    }

    @Override
    public boolean checkMovable(Piece source, Piece destination) {
        return source.isWhite() && (destination.isBlack() || !destination.isNotEmpty());
    }

    @Override
    public BoardState makeGameOver() {
        return new GameOverState(CampType.WHITE);
    }
}
