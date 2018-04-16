package tech.spiro.addrparser.crawler;

/**
 * Get region exception by crawler.
 * @author Spiro Huang
 * @since 1.0
 */
public class GetRegionException extends Exception {
    public GetRegionException() {
        super();
    }

    public GetRegionException(String message) {
        super(message);
    }

    public GetRegionException(String message, Throwable cause) {
        super(message, cause);
    }

    public GetRegionException(Throwable cause) {
        super(cause);
    }

    protected GetRegionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
