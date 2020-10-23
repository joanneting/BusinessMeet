package tw.com.businessmeet.bean;

import java.util.Date;

public class TimelinePropertiesBean {
    private static String[] column = new String[]{"timeline_properties_no","name","create_date","modify_date"};
    private Integer timelinePropertiesNo;
    private String name;
    private String createDate;
    private String modifyDate;
    private Integer statusCode;

    public static String[] getColumn() {
        return column;
    }

    public Integer getTimelinePropertiesNo() {
        return timelinePropertiesNo;
    }

    public void setTimelinePropertiesNo(Integer timelinePropertiesNo) {
        this.timelinePropertiesNo = timelinePropertiesNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
