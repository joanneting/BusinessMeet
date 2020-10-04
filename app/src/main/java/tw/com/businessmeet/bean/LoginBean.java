package tw.com.businessmeet.bean;

public class LoginBean {

    private String identity;
    private UserInformationBean userInformationBean;


    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public UserInformationBean getUserInformationBean() {
        return userInformationBean;
    }

    public void setUserInformationBean(UserInformationBean userInformationBean) {
        this.userInformationBean = userInformationBean;
    }
}
