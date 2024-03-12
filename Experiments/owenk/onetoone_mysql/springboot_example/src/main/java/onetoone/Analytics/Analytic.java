package onetoone.Analytics;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import com.fasterxml.jackson.annotation.JsonIgnore;

import onetoone.Users.User;

@Entity
public class Analytic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String date ;
    private int birdsClassifiedOnDate;
    private int totalBirdsClassified;


    @OneToOne
    @JsonIgnore
    private User user;

    public Analytic( String date, int birdsClassifiedOnDate, int totalBirdsClassified ) {
        this.date = date;
        this.birdsClassifiedOnDate = birdsClassifiedOnDate;
        this.totalBirdsClassified = totalBirdsClassified;
    }

    public Analytic() {

    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getBirdsClassifiedOnDate() {
        return birdsClassifiedOnDate;
    }

    public void setBirdsClassifiedOnDate(int birdsClassifiedOnDate) {
        this.birdsClassifiedOnDate = birdsClassifiedOnDate;
    }

    public int getTotalBirdsClassified() {
        return totalBirdsClassified;
    }

    public void setTotalBirdsClassified(int totalBirdsClassified) {
        this.totalBirdsClassified = totalBirdsClassified;
    }
}
