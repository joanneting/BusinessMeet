package tw.com.businessmeet.bean;

import java.util.Date;

public class ProblemReportBean {

    private static String[] column = new String[]{"problem_report_no","content","user_id","status","start_date","end_date","create_date","modify_date"};
    private Integer problemReportNo;
    private String content;
    private String userId;
    private String status;
    private String startDate;
    private String endDate;
    private String createDate;
    private String modifyDate;
    private Integer statusCode;

    public static String[] getColumn() {
        return column;
    }

    public Integer getProblemReportNo() {
        return problemReportNo;
    }

    public void setProblemReportNo(Integer problemReportNo) {
        this.problemReportNo = problemReportNo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
}
