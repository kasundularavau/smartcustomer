package cf.snowberry.smartcustomer.Models;

public class ModelCompanies {

    String companyEmail, companyName, companyDescription, companyPhone, companyLogo, companyCoverPhoto, companyID;

    public ModelCompanies() {
    }


    public ModelCompanies(String companyEmail, String companyName, String companyDescription, String companyPhone, String companyLogo, String companyCoverPhoto, String companyID) {
        this.companyEmail = companyEmail;
        this.companyName = companyName;
        this.companyDescription = companyDescription;
        this.companyPhone = companyPhone;
        this.companyLogo = companyLogo;
        this.companyCoverPhoto = companyCoverPhoto;
        this.companyID = companyID;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    public String getCompanyCoverPhoto() {
        return companyCoverPhoto;
    }

    public void setCompanyCoverPhoto(String companyCoverPhoto) {
        this.companyCoverPhoto = companyCoverPhoto;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }
}
