package pwc.appdev.speedalert;

class ViolationUser {

    private String date, time, address, geolocation, violation;

    public ViolationUser(){

        this.address = " ";
        this.geolocation = " ";
        this.time = " ";
        this.violation = " ";
        this.date = " ";

    }

    public void setDate(String fdate){ this.date = fdate; }
    public String getDate(){
        return date;
    }

    public void setTime(String ftime){ this.time = ftime; }
    public String getTime(){
        return time;
    }

    public void setAddress(String faddress){ this.address = faddress; }
    public String getAddress(){
        return address;
    }

    public void setGeoLocation(String fgeolocation){ this.geolocation = fgeolocation; }
    public String getGeoLocation(){
        return geolocation;
    }

    public void setViolation(String fviolation){ this.violation = fviolation; }
    public String getViolation(){
        return violation;
    }

}
