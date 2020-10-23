package tw.com.businessmeet.bean;

import com.google.gson.annotations.SerializedName;

public class ResponseBody<D> {
    @SerializedName("result")
    boolean success;
    D data;
    String message;

    Integer status;
    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
