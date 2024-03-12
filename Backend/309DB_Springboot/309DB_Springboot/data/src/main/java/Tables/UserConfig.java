package Tables;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import Tables.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@ComponentScan
public class UserConfig implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//        UserDetails details = null;
//
//        try {
//            Tables.Users.User user = userRepository.findByUsername(username);
//
//            details =
//                    new org.springframework.security.core.userdetails.User(username,
//                            user.getPassword(),
//                            AuthorityUtils.createAuthorityList());
//        } catch (UsernameNotFoundException exception) {
//            throw exception;
//        } catch (Exception exception) {
//            throw new UsernameNotFoundException(username);
//        }
//
//        return details;

        List<SimpleGrantedAuthority> roles=null;
        if(username.equals("admin"))
        {
            roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
            return new User("admin", "$2a$10$flB7dGnQ3Blp3pK2U5i5vurs.aDlIpnyXATpj59O.Q3CiJgPUpXZm",
                    roles);
        }
        else if(username.equals("user"))
        {
            roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
            return new User("user", "$2a$10$x5apmQbgsHZwA.e1YRCnWOh4JdxhpSaxgOUFg..zb0QX4mwegT696",
                    roles);
        }
        throw new UsernameNotFoundException("User not found with username: " + username);
    }

}