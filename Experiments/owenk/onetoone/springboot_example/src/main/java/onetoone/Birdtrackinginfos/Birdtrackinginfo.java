package onetoone.Birdtrackinginfos;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import com.fasterxml.jackson.annotation.JsonIgnore;

import onetoone.Birdtrackinginfos.Birdtrackinginfo;
import onetoone.Users.User;

@Entity
public class Birdtrackinginfo {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private double latitude;
    private double longitude;
    private String date;

    private String time;
    private String imageLink;

//    @OneToOne
//    @JsonIgnore
//    private  ;

    public Birdtrackinginfo( double latitude, double longitude, String date, String time, String imageLink) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.time = time;
        this.imageLink = imageLink;
    }

    public Birdtrackinginfo() {

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

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}
