package Tables.BirdTrackingInfo;

import javax.persistence.*;

import Tables.BirdInfo.BirdInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;

import Tables.BirdTrackingInfo.BirdTrackingInfo;
import Tables.Users.User;
import io.swagger.annotations.ApiModelProperty;

@Entity
public class BirdTrackingInfo{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ApiModelProperty(notes = "Latitude Location of The Bird Scan",name="latitude",required=true)
    private double latitude;
    @ApiModelProperty(notes = "Longitude Location of the Bird Scan",name="longitude",required=true)
    private double longitude;
    @ApiModelProperty(notes = "Date of the Bird Scan",name="date",required=true)
    private String date;
    @ApiModelProperty(notes = "Time of the Bird Scan",name="time",required=true)
    private String time;
    @ApiModelProperty(notes = "Name of the Bird from Scan",name="birdName",required=true)
    private String birdName;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    public BirdTrackingInfo( double latitude, double longitude, String date, String time, String birdName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.time = time;
        this.birdName = birdName;
    }

    public BirdTrackingInfo() {

    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBirdName() {
        return birdName;
    }
    public void setBirdName(String birdName) {
        this.birdName = birdName;
    }

}
