package tw.com.businessmeet.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ActivityInviteBean implements Parcelable {
    public static List<ActivityInviteBean> inviteBean;
    private static String[] column = new String[]{"activity_invite_no", "user_Id", "activity_no", "create_date", "modify_date"};
    private Integer activityInviteNo;
    private String userId;
    private Integer activityNo;
    @SerializedName("createDateStr")
    private String createDate;
    @SerializedName("modifyDateStr")
    private String modifyDate;
    private Integer statusCode;
    private String userName;
    private String avatar;
    private Integer status;
    private transient Boolean isInvite;//json不序列化
    private String title;
    private String place;
    private String activityDate;

    public ActivityInviteBean() {
    }

    protected ActivityInviteBean(Parcel in) {
        if (in.readByte() == 0) {
            activityInviteNo = null;
        } else {
            activityInviteNo = in.readInt();
        }
        userId = in.readString();
        if (in.readByte() == 0) {
            activityNo = null;
        } else {
            activityNo = in.readInt();
        }
        createDate = in.readString();
        modifyDate = in.readString();
        if (in.readByte() == 0) {
            statusCode = null;
        } else {
            statusCode = in.readInt();
        }
        userName = in.readString();
        avatar = in.readString();
        byte tmpIsInvite = in.readByte();
        isInvite = tmpIsInvite == 0 ? null : tmpIsInvite == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (activityInviteNo == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(activityInviteNo);
        }
        dest.writeString(userId);
        if (activityNo == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(activityNo);
        }
        dest.writeString(createDate);
        dest.writeString(modifyDate);
        if (statusCode == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(statusCode);
        }
        dest.writeString(userName);
        dest.writeString(avatar);
        dest.writeByte((byte) (isInvite == null ? 0 : isInvite ? 1 : 2));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ActivityInviteBean> CREATOR = new Creator<ActivityInviteBean>() {
        @Override
        public ActivityInviteBean createFromParcel(Parcel in) {
            return new ActivityInviteBean(in);
        }

        @Override
        public ActivityInviteBean[] newArray(int size) {
            return new ActivityInviteBean[size];
        }
    };

    public static String[] getColumn() {
        return column;
    }

    public Integer getActivityInviteNo() {
        return activityInviteNo;
    }

    public void setActivityInviteNo(Integer activityInviteNo) {
        this.activityInviteNo = activityInviteNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean isInvite() {
        return isInvite;
    }

    public void setInvite(Boolean invite) {
        isInvite = invite;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(String activityDate) {
        this.activityDate = activityDate;
    }
}
