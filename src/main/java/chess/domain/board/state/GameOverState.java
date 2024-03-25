package chess.domain.board.state;

import chess.domain.piece.Piece;

public class GameOverState implements BoardState {

    private static final String UNSUPPORTED_EXCEPTION = "이미 종료된 게임입니다.";

    @Override
    public BoardState nextState() {
        throw new UnsupportedOperationException(UNSUPPORTED_EXCEPTION);
    }

    @Override
    public boolean checkMovable(Piece source, Piece destination) {
        throw new UnsupportedOperationException(UNSUPPORTED_EXCEPTION);
    }
}
