package edu.cpp.cs.attm;

public enum Status {
    NOT_REGISTERED("Please enter your desired username"),
    LOGGED_IN(" Connected."),
    LOGGING_OUT(" Disconnected.");

    private String message;

    Status(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
