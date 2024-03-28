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

    public Board createBoard() {
        int gameId = gameDao.save();
        Map<Square, Piece> board = new BoardFactory().create();

        boardDao.saveAll(gameId, board);
        return new Board(gameId, board);
    }

    public Board createBoard(int gameId) {
        BoardState boardState = gameDao.findStateById(gameId);
        Map<Square, Piece> board = new BoardFactory().create(gameId);

        return new Board(gameId, board, boardState);
    }

    public void move(Board board, MoveCommand moveCommand) {
        Square source = moveCommand.source();
        Square destination = moveCommand.destination();

        Piece destinationPiece = board.movePiece(source, destination);
        boardDao.update(board.getGameId(), source, destinationPiece);
        boardDao.update(board.getGameId(), destination, board.findPieceBySquare(destination));

        gameDao.update(board.getGameId(), board.getBoardState().getSateName());
    }
}
