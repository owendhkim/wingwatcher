package onetoone.Users;

import onetoone.BirdInfo.BirdInfo;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;


/**
 * @author Brian Xicon
 */ 

@Entity
public class User {

     /* 
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String email;
    private String password;
    //(0 is viewer, 1 is researcher, 2 is administrator)
    private int privilege;

    /*
     * @OneToOne creates a relation between the current entity/table(Laptop) with the entity/table defined below it(User)
     * cascade is responsible propagating all changes, even to children of the class Eg: changes made to laptop within a user object will be reflected
     * in the database (more info : https://www.baeldung.com/jpa-cascade-types)
     * @JoinColumn defines the ownership of the foreign key i.e. the user table will have a field called laptop_id
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "birdInfo_id")
    private BirdInfo birdInfo;

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

    public BirdInfo getBirdInfo(){
        return birdInfo;
    }

    public void setBirdInfo(BirdInfo birdInfo){
        this.birdInfo = birdInfo;
    }
    
}
