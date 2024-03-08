package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TenmoService {
    private final String BASE_API_URL = "http://localhost:8080";
    private AuthenticatedUser currentUser;
    private RestTemplate restTemplate = new RestTemplate();

    public void setCurrentUser(AuthenticatedUser currentUser) {
        this.currentUser = currentUser;
    }

    public double getAccountBalanceByUserId(int userId) {
        String url = BASE_API_URL + "/account/balance";
        ResponseEntity<Double> response = restTemplate.exchange(url, HttpMethod.GET, makeAuthEntity(currentUser), Double.class);
        return response.getBody();
    }
    public Account getAccountByUserId(int userId) {
        String url = BASE_API_URL + "/accounts/my_account";
        ResponseEntity<Account> response = restTemplate.exchange(url, HttpMethod.GET,
                makeAuthEntity(currentUser), Account.class);
        return response.getBody();
    }
    public List<User> getAllUsersExceptMyself() {
        List<User> users = new ArrayList<>();
        String url = BASE_API_URL + "/users";
        ResponseEntity<User[]> response = restTemplate.exchange(url, HttpMethod.GET,
                makeAuthEntity(currentUser), User[].class);
        users = Arrays.asList(response.getBody());
        return users;
    }

    public User getUserById(int userId) {
        User user = null;
        String url = BASE_API_URL + "/user/" + userId;
        ResponseEntity<User> response = restTemplate.exchange(url,HttpMethod.GET,
                makeAuthEntity(currentUser), User.class);
        return response.getBody();
    }

    public void sendBucks(Transfer transfer) {
        String url = BASE_API_URL + "/send_bucks";
            HttpEntity<Transfer> h = makeAuthEntity(currentUser, transfer);
            ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.PUT,
                    h, Void.class);
    }




    private HttpEntity<Void> makeAuthEntity(AuthenticatedUser user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(headers);
    }
    private HttpEntity<Transfer> makeAuthEntity(AuthenticatedUser user, Transfer Body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<Transfer>(Body, headers);
    }
}
