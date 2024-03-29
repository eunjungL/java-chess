package chess.view.dto;

public record Command(String command, String source, String destination) {

    private static final String END_COMMAND = "end";
    private static final String STATUS_COMMAND = "status";

    public Command(String command) {
        this(command, "", "");
    }

    public Command(String command, String source) {
        this(command, source, "");
    }

    public boolean isEndCommand() {
        return command.equals(END_COMMAND);
    }

    public boolean isStatusCommand() {
        return command.equals(STATUS_COMMAND);
    }

    public boolean isCreateCommand() {
        return command.equals("create");
    }
}
