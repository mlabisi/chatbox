package edu.cpp.cs.networks.attm;

public enum ClientStatus {
    NOT_REGISTERED("Please enter your desired username: "),
    LOGGED_IN(" Connected."),
    LOGGING_OUT(" Disconnected.");

    private final String MESSAGE;

    ClientStatus(String message) {
        this.MESSAGE = message;
    }

    public String toString() {
        return MESSAGE;
    }
}
