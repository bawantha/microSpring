package com.bawantha.microSpring.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

@Controller
public class HomeController {

    @GetMapping("/")
    public Mono<String> home(Model model, @AuthenticationPrincipal OidcUser principal) {
        if (principal != null) {
            model.addAttribute("name", principal.getFullName());
            model.addAttribute("email", principal.getEmail());
        }
        return Mono.just("index");
    }

    @GetMapping("/profile")
    public Mono<String> profile(Model model, @AuthenticationPrincipal OidcUser principal) {
        model.addAttribute("name", principal.getFullName());
        model.addAttribute("email", principal.getEmail());
        model.addAttribute("profile", principal.getClaims());
        return Mono.just("profile");
    }

}

