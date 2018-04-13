package tech.spiro.addrparser.crawler;

/**
 * @Author: Shaoping Huang
 * @Description:
 * @Date: 7/31/2017
 */
public class GetDistrictsException extends Exception {
    public GetDistrictsException() {
        super();
    }

    public GetDistrictsException(String message) {
        super(message);
    }

    public GetDistrictsException(String message, Throwable cause) {
        super(message, cause);
    }

    public GetDistrictsException(Throwable cause) {
        super(cause);
    }

    protected GetDistrictsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
