package tw.com.businessmeet.exception;

public class BluetoothServerCloseException extends RuntimeException {
    public BluetoothServerCloseException() {
    }

    public BluetoothServerCloseException(String message) {
        super(message);
    }

    public BluetoothServerCloseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BluetoothServerCloseException(Throwable cause) {
        super(cause);
    }

    public BluetoothServerCloseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
