package chess.domain.board.service;

import chess.GameDao;
import chess.domain.board.Board;
import chess.domain.board.BoardFactory;
import chess.domain.board.dao.BoardDao;
import chess.domain.board.dto.MoveCommand;
import chess.domain.board.state.BoardState;
import chess.domain.piece.Piece;
import chess.domain.square.Square;

import java.util.Map;

public class BoardService {

    private final BoardDao boardDao;
    private final GameDao gameDao;

    public BoardService() {
        this.boardDao = new BoardDao();
        this.gameDao = new GameDao();
    }

    public Board createBoard(int gameId) {
        Map<Square, Piece> board = boardDao.findByGameId(gameId)
                .orElseGet(() -> {
                    Map<Square, Piece> newBoard = new BoardFactory().create();
                    boardDao.saveAll(gameId, newBoard);
                    return newBoard;
                });
        BoardState boardState = gameDao.findStateById(gameId);

        return new Board(board, boardState);
    }

    public void move(int gameId, Board board, MoveCommand moveCommand) {
        Square source = moveCommand.source();
        Square destination = moveCommand.destination();

        Piece destinationPiece = board.movePiece(source, destination);
        boardDao.update(gameId, source, destinationPiece);
        boardDao.update(gameId, destination, board.findPieceBySquare(destination));

        gameDao.update(gameId, board.getBoardState().getSateName());
    }
}
