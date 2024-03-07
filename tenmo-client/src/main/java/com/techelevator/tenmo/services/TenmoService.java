package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TenmoService {
    private final String BASE_API_URL = "http://localhost:8080/";
    private AuthenticatedUser currentUser;
    private RestTemplate restTemplate = new RestTemplate();

    public double getAccountBalanceByUserId() {
        String url = BASE_API_URL + "account/balance";
        ResponseEntity<Double> response = restTemplate.exchange(url, HttpMethod.GET, makeAuthEntity(currentUser), Double.class);
        return response.getBody();
    }

    public List<User> getAllUsersExceptMyself() {
        List<User> users = new ArrayList<>();
        String url = BASE_API_URL + "users";
        ResponseEntity<User[]> response = restTemplate.exchange(url, HttpMethod.GET,
                makeAuthEntity(currentUser), User[].class);
        users = Arrays.asList(response.getBody());
        return users;
    }


    public void setCurrentUser(AuthenticatedUser currentUser) {
        this.currentUser = currentUser;
    }

    private HttpEntity<Void> makeAuthEntity(AuthenticatedUser user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(headers);
    }
}
