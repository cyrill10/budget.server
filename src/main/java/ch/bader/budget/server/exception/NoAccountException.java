package ch.bader.budget.server.exception;

import lombok.Getter;

@Getter
public class NoAccountException extends Exception {

    private final int id;

    public NoAccountException(int id) {
        super();
        this.id = id;
    }

}
