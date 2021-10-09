package validate;

public abstract class ProgramValidationException extends RuntimeException {

    public ProgramValidationException(String message) {
        super(message);
    }
}
