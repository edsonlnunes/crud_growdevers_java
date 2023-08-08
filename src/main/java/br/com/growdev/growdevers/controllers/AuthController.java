package br.com.growdev.growdevers.controllers;

import br.com.growdev.growdevers.dtos.LoginAuth;
import br.com.growdev.growdevers.models.Growdever;
import br.com.growdev.growdevers.services.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthController {
    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity doLogin(@RequestBody @Valid LoginAuth data) {
        var token = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var authentication = manager.authenticate(token);

        var jwt = tokenService.getToken((Growdever) authentication.getPrincipal());

        return ResponseEntity.ok().body(jwt);

    }
}
