package chess.domain.piece.dao;

import chess.domain.piece.Piece;

import java.util.Optional;

public interface PieceDao {

    int save(Piece piece);

    Optional<Integer> findIdByPiece(Piece piece);

    Piece findById(int id);
}
