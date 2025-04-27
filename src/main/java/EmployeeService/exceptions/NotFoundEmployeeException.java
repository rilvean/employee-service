package EmployeeService.exceptions;

public class NotFoundEmployeeException extends Exception {
    public NotFoundEmployeeException() {
        super();
    }

    public NotFoundEmployeeException(String message) {
        super(message);
    }
}