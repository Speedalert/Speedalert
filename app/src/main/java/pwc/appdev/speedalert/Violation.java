package pwc.appdev.speedalert;

class Violation {

    private String id, driver, plate, datetime, zone, address, geolocation, violation;

    public Violation(){

        this.id = "  ";
        this.driver = " ";
        this.plate = " ";
        this.address = " ";
        this.zone = " ";
        this.geolocation = " ";
        this.violation = " ";
        this.datetime = " ";

    }

    public void setViolationID(String fid){ this.id = fid; }
    public String getViolationID(){
        return id;
    }

    public void setDriver(String fdriver){ this.driver = fdriver; }
    public String getDriver(){
        return driver;
    }

    public void setPlate(String fplate){ this.plate = fplate; }
    public String getPlate(){
        return plate;
    }

    public void setDateTime(String fdatetime){ this.datetime = fdatetime; }
    public String getDateTime(){
        return datetime;
    }

    public void setAddress(String faddress){ this.address = faddress; }
    public String getAddress(){
        return address;
    }

    public void setZone(String fzone){ this.zone = fzone; }
    public String getZone(){
        return zone;
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
