package tech.spiro.addrparser.parser;

public class ParserEngineException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 6162368331216310167L;

    public ParserEngineException() {
        super();
    }

    public ParserEngineException(String message, Throwable cause,
                                 boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ParserEngineException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParserEngineException(String message) {
        super(message);
    }

    public ParserEngineException(Throwable cause) {
        super(cause);
    }
    
}
