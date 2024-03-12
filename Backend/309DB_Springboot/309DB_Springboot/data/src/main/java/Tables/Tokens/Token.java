package Tables.Tokens;

import Tables.Users.User;
import lombok.Builder;

import javax.persistence.*;
import java.util.Date;

@Entity
@Builder
public class Token {

    @Id
    private String token;
    private Date generatedTime;
    private Date expirationTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;

    public Token(String token, Date generatedTime, Date expirationTime, User user) {
        this.token = token;
        this.generatedTime = generatedTime;
        this.expirationTime = expirationTime;
        this.user = user;

    }
    public Token(){

    }


}
