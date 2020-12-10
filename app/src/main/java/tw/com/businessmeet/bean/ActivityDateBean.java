package tw.com.businessmeet.bean;

public class ActivityDateBean {
    private static String[] column = new String[]{"activity_date_no", "activity_no", "start_date", "end_date", "create_date", "modify_date"};
    private Integer activityDateNo;
    private Integer activityNo;
    private String startDate;
    private String endDate;
    private String createDate;
    private String modifyDate;

    public static String[] getColumn() {
        return column;
    }

    public Integer getActivityDateNo() {
        return activityDateNo;
    }

    public void setActivityDateNo(Integer activityDateNo) {
        this.activityDateNo = activityDateNo;
    }

    public Integer getActivityNo() {
        return activityNo;
    }

    public void setActivityNo(Integer activityNo) {
        this.activityNo = activityNo;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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
}
