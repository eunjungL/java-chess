package chess.domain.board.state;

import chess.domain.piece.CampType;
import chess.domain.piece.Piece;

public class BlackTurnState implements GameProgressState {

    private static final String GAME_NOT_OVER_EXCEPTION = "게임이 아직 종료되지 않았습니다.";

    @Override
    public GameProgressState nextTurnState() {
        return new WhiteTurnState();
    }

    @Override
    public boolean checkMovable(Piece source, Piece destination) {
        return source.isBlack() && (destination.isWhite() || !destination.isNotEmpty());
    }

    @Override
    public GameProgressState makeGameOver() {
        return new GameOverState(CampType.BLACK);
    }

    @Override
    public CampType findWinner() {
        throw new UnsupportedOperationException(GAME_NOT_OVER_EXCEPTION);
    }

    @Override
    public StateName getSateName() {
        return StateName.BLACK_TURN;
    }
}
