package tw.com.businessmeet.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class ActivityLabelBean implements Parcelable {
    private static String[] column = new String[]{"activityLabel_no","activity_no","content","create_date","modify_date"};
    private Integer activityLabelNo;
    private Integer activityNo;
    private String content;
    private String createDate;
    private String modifyDate;
    private Integer statusCode;

    public ActivityLabelBean() {
    }

    protected ActivityLabelBean(Parcel in) {
        if (in.readByte() == 0) {
            activityLabelNo = null;
        } else {
            activityLabelNo = in.readInt();
        }
        if (in.readByte() == 0) {
            activityNo = null;
        } else {
            activityNo = in.readInt();
        }
        content = in.readString();
        createDate = in.readString();
        modifyDate = in.readString();
        if (in.readByte() == 0) {
            statusCode = null;
        } else {
            statusCode = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (activityLabelNo == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(activityLabelNo);
        }
        if (activityNo == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(activityNo);
        }
        dest.writeString(content);
        dest.writeString(createDate);
        dest.writeString(modifyDate);
        if (statusCode == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(statusCode);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ActivityLabelBean> CREATOR = new Creator<ActivityLabelBean>() {
        @Override
        public ActivityLabelBean createFromParcel(Parcel in) {
            return new ActivityLabelBean(in);
        }

        @Override
        public ActivityLabelBean[] newArray(int size) {
            return new ActivityLabelBean[size];
        }
    };

    public static String[] getColumn() {
        return column;
    }

    public Integer getActivityLabelNo() {
        return activityLabelNo;
    }

    public void setActivityLabelNo(Integer activityLabelNo) {
        this.activityLabelNo = activityLabelNo;
    }

    public Integer getActivityNo() {
        return activityNo;
    }

    public void setActivityNo(Integer activityNo) {
        this.activityNo = activityNo;
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
