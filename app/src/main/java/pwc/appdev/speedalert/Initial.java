package pwc.appdev.speedalert;

public class Initial {

    private String speed, time, distance, average, location;

    public Initial(){

        this.speed = " ";
        this.time = " ";
        this.distance = " ";
        this.average = " ";
        this.location = " ";

    }

    public void setSpeed(String fspeed){
        this.speed = fspeed;
    }
    public String getSpeed(){
        return speed;
    }

    public void setTime(String ftime){
        this.time = ftime;
    }
    public String getTime(){
        return time;
    }

    public void setDistance(String fdistance){
        this.distance = fdistance;
    }
    public String getDistance(){
        return distance;
    }

    public void setAverage(String faverage){
        this.average = faverage;
    }
    public String getAverage(){
        return average;
    }

    public void setLocation(String flocation){
        this.location = flocation;
    }
    public String getLocation(){
        return location;
    }

}
