package com.news.aggregator.controller;

import org.springframework.security.authentication.jaas.memory.InMemoryConfiguration;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
    @GetMapping("/admin")
    public String getAdminPage() {
        return "Administrator page";
    }
}
