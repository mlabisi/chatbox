package edu.cpp.cs.networks.attm;

public enum ClientStatus {
    NOT_REGISTERED("Please enter your desired username"),
    LOGGED_IN(" Connected."),
    LOGGING_OUT(" Disconnected.");

    private String message;

    ClientStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
