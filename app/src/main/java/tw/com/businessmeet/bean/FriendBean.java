package tw.com.businessmeet.bean;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;

public class FriendBean {


    private static String[] column = new String[]{"friend_no","matchmaker_id","friend_id","remark","status","create_date","modify_date"};
    private Integer friendNo;
    private String matchmakerId;
    private String friendId;
    private String friendName;
    private String friendAvatar;
    private String friendProfession;
    private String remark;
    private Integer status;
    private String createDate;
    private String modifyDate;
    private Integer statusCode;

    public static String[] getColumn() {
        return column;
    }


    public Integer getFriendNo() {
        return friendNo;
    }

    public void setFriendNo(Integer friendNo) {
        this.friendNo = friendNo;
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

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendAvatar() {
        return friendAvatar;
    }

    public void setFriendAvatar(String friendAvatar) {
        this.friendAvatar = friendAvatar;
    }

    public String getFriendProfession() {
        return friendProfession;
    }

    public void setFriendProfession(String friendProfession) {
        this.friendProfession = friendProfession;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        FriendBean that = (FriendBean) o;

        return new EqualsBuilder()
                .append(friendNo, that.friendNo)
                .append(matchmakerId, that.matchmakerId)
                .append(friendId, that.friendId)
                .append(friendName, that.friendName)
                .append(friendAvatar, that.friendAvatar)
                .append(remark, that.remark)
                .append(status, that.status)
                .append(createDate, that.createDate)
                .append(modifyDate, that.modifyDate)
                .append(statusCode, that.statusCode)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(friendNo)
                .append(matchmakerId)
                .append(friendId)
                .append(friendName)
                .append(friendAvatar)
                .append(remark)
                .append(status)
                .append(createDate)
                .append(modifyDate)
                .append(statusCode)
                .toHashCode();
    }
}
