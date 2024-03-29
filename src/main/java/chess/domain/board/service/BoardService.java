package chess.domain.board.service;

import chess.domain.board.Board;
import chess.domain.board.BoardFactory;
import chess.domain.board.dao.BoardDao;
import chess.domain.board.dto.MoveCommand;
import chess.domain.board.state.BoardState;
import chess.domain.board.state.StateName;
import chess.domain.game.dao.GameDao;
import chess.domain.piece.Piece;
import chess.domain.square.Square;

import java.util.Map;

public class BoardService {

    private final BoardDao boardDao;
    private final GameDao gameDao;

    public BoardService(BoardDao boardDao, GameDao gameDao) {
        this.boardDao = boardDao;
        this.gameDao = gameDao;
    }

    public Board createBoard(int gameId) {
        Map<Square, Piece> board = boardDao.findByGameId(gameId)
                .orElseGet(() -> createNewBoard(gameId));
        BoardState boardState = gameDao.findStateById(gameId);

        return new Board(board, boardState);
    }

    private Map<Square, Piece> createNewBoard(int gameId) {
        Map<Square, Piece> newBoard = new BoardFactory().create();
        boardDao.saveAll(gameId, newBoard);

        return newBoard;
    }

    public void move(int gameId, Board board, MoveCommand moveCommand) {
        Square source = moveCommand.source();
        Square destination = moveCommand.destination();

        Piece destinationPiece = board.movePiece(source, destination);
        boardDao.update(gameId, source, destinationPiece);
        boardDao.update(gameId, destination, board.findPieceBySquare(destination));

        BoardState boardState = board.getBoardState();
        gameDao.update(gameId, boardState.getSateName());
        if (boardState.getSateName().equals(StateName.GAME_OVER)) {
            gameDao.update(gameId, boardState.findWinner());
        }
    }
}
