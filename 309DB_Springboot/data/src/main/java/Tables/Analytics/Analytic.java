package Tables.Analytics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import Tables.Users.User;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Entity
public class Analytic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ApiModelProperty(notes = "Date of the Analytic",name="date",required=true,value="10/31/2023")
    private String date ;
    @ApiModelProperty(notes="Number of Birds Classified on Date",name="birdsClassifiedOnDate",required = true,value="30")
    private int birdsClassifiedOnDate;
    @ApiModelProperty(notes="Total Number of Birds Classified Ever",name = "totalBirdsClassified",required = true,value = "480")
    private int totalBirdsClassified;


//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "user_id")
//    private User user;

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
