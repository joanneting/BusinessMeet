package tw.com.businessmeet.exception;

public class BluetoothServerStartException extends RuntimeException {
    public BluetoothServerStartException() {
        super();
    }

    public BluetoothServerStartException(String message) {
        super(message);
    }

    public BluetoothServerStartException(String message, Throwable cause) {
        super(message, cause);
    }

    public BluetoothServerStartException(Throwable cause) {
        super(cause);
    }

    protected BluetoothServerStartException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
