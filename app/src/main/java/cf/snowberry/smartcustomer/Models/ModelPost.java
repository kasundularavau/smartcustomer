package cf.snowberry.smartcustomer.Models;

public class ModelPost {

    String pId, uid, uEmail, pTitle, pDescription, pImage, pLikes, pComments, pTime, uName, uDp;

    public ModelPost() {
    }

    public ModelPost(String pId, String uid, String uEmail, String pTitle, String pDescription, String pImage, String pLikes, String pCommentCount, String pTime, String uName, String uDp) {
        this.pId = pId;
        this.uid = uid;
        this.uEmail = uEmail;
        this.pTitle = pTitle;
        this.pDescription = pDescription;
        this.pImage = pImage;
        this.pLikes = pLikes;
        this.pComments = pCommentCount;
        this.pTime = pTime;
        this.uName = uName;
        this.uDp = uDp;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getpTitle() {
        return pTitle;
    }

    public void setpTitle(String pTitle) {
        this.pTitle = pTitle;
    }

    public String getpDescription() {
        return pDescription;
    }

    public void setpDescription(String pDescription) {
        this.pDescription = pDescription;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }

    public String getpLikes() {
        return pLikes;
    }

    public void setpLikes(String pLikes) {
        this.pLikes = pLikes;
    }

    public String getpComments() {
        return pComments;
    }

    public void setpComments(String pCommentCount) {
        this.pComments = pCommentCount;
    }

    public String getpTime() {
        return pTime;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuDp() {
        return uDp;
    }

    public void setuDp(String uDp) {
        this.uDp = uDp;
    }
}
