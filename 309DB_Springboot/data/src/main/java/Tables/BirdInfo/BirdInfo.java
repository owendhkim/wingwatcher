package Tables.BirdInfo;

import javax.persistence.*;
import Tables.BirdTrackingInfo.BirdTrackingInfo;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;

@Entity
public class BirdInfo {

    /*
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ApiModelProperty(notes = "Scientific Name of Bird",name="scientificName",required=true)
    private String scientificName;
    @ApiModelProperty(notes = "Common Name of Bird",name="name",required=true)
    private String name;
    @ApiModelProperty(notes = "Short Description of Bird",name="shortDesc",required=true)
    @Column(length = 9999)
    private String shortDesc;
    @ApiModelProperty(notes = "Image URL of Bird",name="image",required=true)
    @Column(length = 9999)
    private String image;
    @ApiModelProperty(notes = "Range Map URL of Bird",name="rangeMap",required=true)
    private String rangeMap;
    @ApiModelProperty(notes = "Call Sound URL of Bird",name="callSound",required=true)
    private String callSound;

    /*
     * @OneToOne creates a relation between the current entity/table(Laptop) with the entity/table defined below it(User)
     * @JsonIgnore is to assure that there is no infinite loop while returning either user/laptop objects (laptop->user->laptop->...)
     */
    @ManyToMany
    @JoinTable(
            name = "bird_info_tracking_info",
            joinColumns = @JoinColumn(name = "bird_info_id"),
            inverseJoinColumns = @JoinColumn(name = "tracking_info_id")
    )
    private List<BirdTrackingInfo> birdTrackingInfo;

    public BirdInfo( String scientificName, String name, String shortDesc, String image, String rangeMap, String callSound) {
        this.scientificName = scientificName;
        this.name = name;
        this.shortDesc = shortDesc;
        this.image = image;
        this.rangeMap = rangeMap;
        this.callSound = callSound;
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

    public String getCallSound(){
        return callSound;
    }

    public void setCallSound(String callSound){
        this.callSound = callSound;
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
//        birdTrackingInfo.setBirdInfo(this);
    }
}
