package tw.com.businessmeet.bean;

public class ActivityRemindBean {
    private static String[] column = new String[]{"activity_remind_no", "time", "activity_no", "create_date", "modify_date"};
    private Integer activityRemindNo;
    private String time;
    private Integer activityNo;
    private String createDate;
    private String modifyDate;
    private Integer statusCode;

    public static String[] getColumn() {
        return column;
    }

    public Integer getActivityRemindNo() {
        return activityRemindNo;
    }

    public void setActivityRemindNo(Integer activityRemindNo) {
        this.activityRemindNo = activityRemindNo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getActivityNo() {
        return activityNo;
    }

    public void setActivityNo(Integer activityNo) {
        this.activityNo = activityNo;
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
