package tw.com.businessmeet.bean;

import java.util.Date;

public class FriendCustomizationBean {

    private static String[] column = new String[]{"friend_customization_no","name","friend_no","create_date","modify_date"};
    private Integer friendCustomizationNo;
    private String name;
    private Integer friendNo;
    private String createDate;
    private String modifyDate;
    private Integer statusCode;
    private String content;

    public static String[] getColumn() {
        return column;
    }

    public Integer getFriendCustomizationNo() {
        return friendCustomizationNo;
    }

    public void setFriendCustomizationNo(Integer friendCustomizationNo) {
        this.friendCustomizationNo = friendCustomizationNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFriendNo() {
        return friendNo;
    }

    public void setFriendNo(Integer friendNo) {
        this.friendNo = friendNo;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
