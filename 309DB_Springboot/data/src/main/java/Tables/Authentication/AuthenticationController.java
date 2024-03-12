package Tables.Authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/viewerRegister")
    public ResponseEntity<AuthenticationResponse> viewerRegister (@RequestBody RegisterRequest request)
    {
        return ResponseEntity.ok(service.viewerRegister(request));
    }
    @PostMapping("/adminRegister")
    public ResponseEntity<AuthenticationResponse> adminRegister (@RequestBody RegisterRequest request)
    {
        return ResponseEntity.ok(service.adminRegister(request));
    }

    @PostMapping("/researcherRegister")
    public ResponseEntity<AuthenticationResponse> researcherRegister (@RequestBody RegisterRequest request)
    {
        return ResponseEntity.ok(service.researcherRegister(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> register (@RequestBody AuthenticationRequest request)
    {
        return ResponseEntity.ok(service.authenticate(request));

    }

}
