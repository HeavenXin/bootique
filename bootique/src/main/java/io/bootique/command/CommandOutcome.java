package io.bootique.command;

import io.bootique.cli.Cli;

public class CommandOutcome {

    // UNIX success exit code
    private static final int SUCCESS_EXIT_CODE = 0;

    private static final CommandOutcome SUCCESS = new CommandOutcome(SUCCESS_EXIT_CODE, false, null, null);

    private final String message;
    private final int exitCode;
    private final Throwable exception;
    private final boolean forkedToBackground;

    private CommandOutcome(int exitCode, boolean forkedToBackground, String message, Throwable exception) {
        this.forkedToBackground = forkedToBackground;
        this.message = message;
        this.exitCode = exitCode;
        this.exception = exception;
    }

    /**
     * Returns a successful outcome with an indicator that that a process was left running on the background.
     *
     * @return a successful {@link CommandOutcome}.
     * @since 0.25
     */
    public static CommandOutcome succeededAndForkedToBackground() {
        return new CommandOutcome(SUCCESS_EXIT_CODE, true, null, null);
    }

    public static CommandOutcome succeeded() {
        return SUCCESS;
    }

    public static CommandOutcome failed(int exitCode, Throwable cause) {
        return failed(exitCode, null, cause);
    }

    public static CommandOutcome failed(int exitCode, String message) {
        return failed(exitCode, message, null);
    }

    public static CommandOutcome failed(int exitCode, String message, Throwable th) {
        if (exitCode == SUCCESS_EXIT_CODE) {
            throw new IllegalArgumentException("Success code '0' used for failure outcome.");
        }

        return new CommandOutcome(exitCode, false, message, th);
    }

    public String getMessage() {
        return message;
    }

    public int getExitCode() {
        return exitCode;
    }

    public Throwable getException() {
        return exception;
    }

    public boolean isSuccess() {
        return exitCode == SUCCESS_EXIT_CODE;
    }

    /**
     * Returns whether one or more tasks started by this command were still executing on threads other than the
     * command run thread as of {@link Command#run(Cli)} completion.
     *
     * @return whether one or more tasks started by this command were still executing on threads other than the
     * command run thread as of {@link Command#run(Cli)} completion.
     * @since 0.25
     */
    public boolean forkedToBackground() {
        return forkedToBackground;
    }

    /**
     * Exits the current OS process with the outcome exit code, unless {@link #forkedToBackground} is true.
     */
    public void exit() {

        // don't force exit if there are remaining tasks...
        // TODO: a new name for the 'exit' method to reflect this behavior
        if (!forkedToBackground) {
            System.exit(exitCode);
        }
    }

    @Override
    public String toString() {

        String message = this.message;

        if (message == null && exception != null) {
            message = exception.getMessage();
        }

        StringBuilder buffer = new StringBuilder().append("[").append(exitCode);
        if (message != null) {
            buffer.append(": ").append(message);
        }

        return buffer.append("]").toString();
    }

}
