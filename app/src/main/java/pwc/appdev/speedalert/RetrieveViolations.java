package pwc.appdev.speedalert;

public class RetrieveViolations {

    public String id, address, datetime, violation;

    public RetrieveViolations() {

    }

    public RetrieveViolations(String id, String address, String datetime, String violation) {
        this.id = id;
        this.address = address;
        this.datetime = datetime;
        this.violation = violation;
    }

    public String getViolationid() {
        return id;
    }

    public void setViolationid(String violationid) {
        this.id = violationid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getViolation() {
        return violation;
    }

    public void setViolation(String violation) {
        this.violation = violation;
    }
}
