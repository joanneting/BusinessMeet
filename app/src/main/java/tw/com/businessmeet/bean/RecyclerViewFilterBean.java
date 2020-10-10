package tw.com.businessmeet.bean;

public class RecyclerViewFilterBean<T> {
    T data;
    int position;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
