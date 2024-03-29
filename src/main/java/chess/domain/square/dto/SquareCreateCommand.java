package chess.domain.square.dto;

public record SquareCreateCommand(String fileCommand, String rankValue) {

    public SquareCreateCommand(String command) {
        this(String.valueOf(command.charAt(0)), String.valueOf(command.charAt(1)));
    }
}
