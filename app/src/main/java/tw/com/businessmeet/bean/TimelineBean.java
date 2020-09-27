package tw.com.businessmeet.bean;

import java.util.Date;
import java.util.List;

public class TimelineBean {
    private static String[] column = new String[]{"timeline_no","matchmaker_id","friend_id",
            "place","title","remark","timeline_properties_no","color","create_date","modify_date"};
    private Integer timelineNo;
    private String matchmakerId;
    private String friendId;
    private String place;
    private String title;
    private String remark;
    private Integer timelinePropertiesNo;
    private String color;
    private String activityDate;
    private String createDateStr;
    private String modifyDateStr;
    private String startDate;
    private String endDate;
    private List<ActivityLabelBean> activityLabelBeanList;
    private List<ActivityInviteBean> activityInviteBeanList;
    private Integer statusCode;

    public static String[] getColumn() {
        return column;
    }

    public Integer getTimelineNo() {
        return timelineNo;
    }

    public void setTimelineNo(Integer timelineNo) {
        this.timelineNo = timelineNo;
    }

    public String getMatchmakerId() {
        return matchmakerId;
    }

    public void setMatchmakerId(String matchmakerId) {
        this.matchmakerId = matchmakerId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getTimelinePropertiesNo() {
        return timelinePropertiesNo;
    }

    public void setTimelinePropertiesNo(Integer timelinePropertiesNo) {
        this.timelinePropertiesNo = timelinePropertiesNo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(String activityDate) {
        this.activityDate = activityDate;
    }

    public String getCreateDateStr() {
        return createDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    public String getModifyDateStr() {
        return modifyDateStr;
    }

    public void setModifyDateStr(String modifyDateStr) {
        this.modifyDateStr = modifyDateStr;
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

    public List<ActivityLabelBean> getActivityLabelBeanList() {
        return activityLabelBeanList;
    }

    public void setActivityLabelBeanList(List<ActivityLabelBean> activityLabelBeanList) {
        this.activityLabelBeanList = activityLabelBeanList;
    }

    public List<ActivityInviteBean> getActivityInviteBeanList() {
        return activityInviteBeanList;
    }

    public void setActivityInviteBeanList(List<ActivityInviteBean> activityInviteBeanList) {
        this.activityInviteBeanList = activityInviteBeanList;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
}
