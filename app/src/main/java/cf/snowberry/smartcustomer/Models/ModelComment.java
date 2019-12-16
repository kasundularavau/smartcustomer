package cf.snowberry.smartcustomer.Models;

import java.util.HashMap;

public class ModelComment {

    String cId, comment, timeStamp, uId, uProPic, uName;

    public ModelComment() {

    }

    public ModelComment(String cId, String comment, String timeStamp, String uId, String uProPic, String uName) {
        this.cId = cId;
        this.comment = comment;
        this.timeStamp = timeStamp;
        this.uId = uId;
        this.uProPic = uProPic;
        this.uName = uName;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getuProPic() {
        return uProPic;
    }

    public void setuProPic(String uProPic) {
        this.uProPic = uProPic;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }
}
