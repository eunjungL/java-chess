package chess.domain.board.state;

import chess.domain.piece.Piece;

public interface BoardState {

    BoardState nextState();
    boolean checkMovable(Piece source, Piece destination);
}
