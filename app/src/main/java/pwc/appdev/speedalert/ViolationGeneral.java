package pwc.appdev.speedalert;

class ViolationGeneral {

    private String driver, plate, date, time, address, geolocation, violation;

    public ViolationGeneral(){

        this.driver = " ";
        this.plate = " ";
        this.address = " ";
        this.geolocation = " ";
        this.time = " ";
        this.violation = " ";
        this.date = " ";

    }

    public void setDriver(String fdriver){ this.driver = fdriver; }
    public String getDriver(){
        return driver;
    }

    public void setPlate(String fplate){ this.plate = fplate; }
    public String getPlate(){
        return plate;
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

    public void setGeoLocation(String flocation){ this.geolocation = flocation; }
    public String getGeoLocation(){
        return geolocation;
    }

    public void setViolation(String fviolation){ this.violation = fviolation; }
    public String getViolation(){
        return violation;
    }

}
