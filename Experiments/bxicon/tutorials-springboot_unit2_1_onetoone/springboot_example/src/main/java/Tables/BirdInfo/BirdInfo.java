package Tables.BirdInfo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import Tables.Users.User;

/**
 * @author Brian Xicon
 */

@Entity
public class BirdInfo {

    /*
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String scientificName;
    private String name;
    private String shortDesc;
    private String image;
    private String rangeMap;
    private String call;

    /*
     * @OneToOne creates a relation between the current entity/table(Laptop) with the entity/table defined below it(User)
     * @JsonIgnore is to assure that there is no infinite loop while returning either user/laptop objects (laptop->user->laptop->...)
     */
    @OneToOne
    @JsonIgnore
    private User user;

    public BirdInfo( String scientificName, String name, String shortDesc, String image, String rangeMap, String call) {
        this.scientificName = scientificName;
        this.name = name;
        this.shortDesc = shortDesc;
        this.image = image;
        this.rangeMap = rangeMap;
        this.call = call;
    }

    public BirdInfo() {
    }

    // =============================== Getters and Setters for each field ================================== //
    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }
    public String getScientificName(){
        return scientificName;
    }

    public void setScientificName(String scientificName){
        this.scientificName = scientificName;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getShortDesc(){
        return shortDesc;
    }

    public void setShortDesc(String shortDesc){
        this.shortDesc = shortDesc;
    }

    public String getImage(){
        return image;
    }

    public void setImage(String image){
        this.image = image;
    }

    public String getRangeMap(){
        return rangeMap;
    }

    public void setRangeMap(String rangeMap){
        this.rangeMap = rangeMap;
    }

    public String getCall(){
        return call;
    }

    public void setCall(String call){
        this.call = call;
    }

    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }

}
