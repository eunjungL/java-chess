package chess.domain.board.state;

import chess.domain.piece.CampType;
import chess.domain.piece.Piece;

public interface GameProgressState {

    GameProgressState nextTurnState();

    boolean checkMovable(Piece source, Piece destination);

    GameProgressState makeGameOver();

    CampType findWinner();

    StateName getSateName();
}
