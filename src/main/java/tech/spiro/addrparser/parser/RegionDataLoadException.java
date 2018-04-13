package tech.spiro.addrparser.parser;

public class RegionDataLoadException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 6162368331216310167L;

    public RegionDataLoadException() {
        super();
    }

    public RegionDataLoadException(String message, Throwable cause,
                                   boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public RegionDataLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public RegionDataLoadException(String message) {
        super(message);
    }

    public RegionDataLoadException(Throwable cause) {
        super(cause);
    }
    
}
