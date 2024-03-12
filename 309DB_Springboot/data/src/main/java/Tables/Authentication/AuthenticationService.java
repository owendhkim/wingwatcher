package Tables.Authentication;

import Tables.JwtService;
import Tables.Tokens.TokenRepository;
import Tables.Users.User;
import Tables.Users.Role;
import Tables.Users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import Tables.Tokens.Token;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Service
@RequiredArgsConstructor
public class   AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Autowired
    TokenRepository tokenRepository;
    public AuthenticationResponse viewerRegister(RegisterRequest request)
    {
        var user = User.builder()
                .role(Role.VIEWER)
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse adminRegister(RegisterRequest request)
    {
        var user = User.builder()
                .role(Role.ADMIN)
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse researcherRegister(RegisterRequest request)
    {
        var user = User.builder()
                .role(Role.RESEARCHER)
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request)
    {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail());
        var jwtToken = jwtService.generateToken(user);
        Token t = new Token(jwtToken, jwtService.extractGeneratedTime(jwtToken), jwtService.extractExpiration(jwtToken), user);
        tokenRepository.save(t);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
