package tw.com.businessmeet.bean;

import java.util.Date;

public class UserCustomizationBean {
    private static String[] column = new String[]{ "user_customization","user_id","column_name", "content","create_date","modify_date"};

    private Integer userCustomizationNo;
    private String userId;
    private String columnName;
    private String content;
    private String createDate;
    private String modifyDate;
    private Integer statusCode;

    public static String[] getColumn() {
        return column;
    }

    public Integer getUserCustomizationNo() {
        return userCustomizationNo;
    }

    public void setUserCustomizationNo(Integer userCustomizationNo) {
        this.userCustomizationNo = userCustomizationNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
}
