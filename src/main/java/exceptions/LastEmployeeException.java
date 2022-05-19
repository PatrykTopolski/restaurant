package exceptions;

public class LastEmployeeException extends RuntimeException{
    public LastEmployeeException() {
        super("deleted last employee of that type!!");
    }
}
