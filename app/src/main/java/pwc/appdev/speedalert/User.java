package pwc.appdev.speedalert;

public class User {

    private String fullname, dateofbirth, contactnumber, emailaddress, platenumber, username;

    public User(){

        this.fullname = "";
        this.dateofbirth = "";
        this.contactnumber = "";
        this.emailaddress = "";
        this.platenumber = "";
        this.username = " ";

    }

    public void setFullname(String fname){
        this.fullname = fname;
    }
    public String getFullname(){
        return fullname;
    }

    public void setdob(String fdob){
        this.dateofbirth = fdob;
    }
    public String getdob(){
        return dateofbirth;
    }

    public void setContactnumber(String fnum){
        this.contactnumber = fnum;
    }
    public String getContactnumber(){
        return contactnumber;
    }

    public void setEmailaddress(String femail){
        this.emailaddress = femail;
    }
    public String getEmailaddress(){
        return emailaddress;
    }

    public void setPlatenumber(String fplate){
        this.platenumber = fplate;
    }
    public String getPlatenumber(){
        return platenumber;
    }

    public void setUsername(String fuser){
        this.username = fuser;
    }
    public String getUsername(){
        return username;
    }


}
