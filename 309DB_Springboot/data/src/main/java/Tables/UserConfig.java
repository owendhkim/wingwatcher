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

        UserDetails details = null;

        try {
            Tables.Users.User user = userRepository.findByUsername(username);

            details =
                    new org.springframework.security.core.userdetails.User(username,
                            user.getPassword(),
                            AuthorityUtils.createAuthorityList());
        } catch (UsernameNotFoundException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new UsernameNotFoundException(username);
        }

        return details;
    }

}