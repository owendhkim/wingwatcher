package Tables.Users;

import Tables.Analytics.Analytic;
import Tables.BirdTrackingInfo.BirdTrackingInfo;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
public class User {

    /*
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ApiModelProperty(notes = "Username of the User",name="username",required=true)
    private String username;
    @ApiModelProperty(notes = "Email of the User",name="email",required=true)
    private String email;
    @ApiModelProperty(notes = "Password of the User (Hashed)",name="password",required=true)
    private String password;
    //(0 is viewer, 1 is researcher, 2 is administrator)
    @ApiModelProperty(notes = "Privilege of the user(viewer=0,researcher=1,admin=2)",name="privilege",required=true)
    private int privilege;

    /*
     * @OneToOne creates a relation between the current entity/table(Laptop) with the entity/table defined below it(User)
     * cascade is responsible propagating all changes, even to children of the class Eg: changes made to laptop within a user object will be reflected
     * in the database (more info : https://www.baeldung.com/jpa-cascade-types)
     * @JoinColumn defines the ownership of the foreign key i.e. the user table will have a field called laptop_id
     */
    @OneToMany(mappedBy="user")
    private List<BirdTrackingInfo> birdTrackingInfo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "analytic_id")
    private Analytic analytic;

    public User(String username, String email, String password, int privilege) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.privilege = privilege;
    }

    public User() {
    }

    // =============================== Getters and Setters for each field ================================== //

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public int getPrivilege(){
        return privilege;
    }

    public void setPrivilege(int privilege){
        this.privilege = privilege;
    }

    public List<BirdTrackingInfo> getBirdTrackingInfo() {
        return birdTrackingInfo;
    }

    public void setBirdTrackingInfo(List<BirdTrackingInfo> birdTrackingInfo) {
        this.birdTrackingInfo = birdTrackingInfo;
    }

    public void addBirdTrackingInfo(BirdTrackingInfo birdTrackingInfo){
        if (this.birdTrackingInfo == null){
            this.birdTrackingInfo = new ArrayList<>();
        }
        this.birdTrackingInfo.add(birdTrackingInfo);
    }

    public void addAnalytic(Analytic analytic){
        if(this.analytic == null){
            this.analytic = new Analytic();
        }
        this.analytic = analytic;
    }

    public Analytic getAnalytics() {
        return analytic;
    }

}
