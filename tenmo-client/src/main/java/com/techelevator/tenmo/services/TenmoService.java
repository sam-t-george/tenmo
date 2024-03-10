package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

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
        String url = BASE_API_URL + "/user/" + userId;
        ResponseEntity<User> response = restTemplate.exchange(url,HttpMethod.GET,
                makeAuthEntity(currentUser), User.class);
        return response.getBody();
    }

    public Transfer sendBucks(Transfer requestTransfer) {
        Transfer responseTransfer = null;
        String url = BASE_API_URL + "/send_bucks";
        ResponseEntity<Transfer> response = restTemplate.exchange(url, HttpMethod.POST,
                makeTransferAuthEntity(currentUser, requestTransfer), Transfer.class);
        responseTransfer = response.getBody();
        return response.getBody();
    }

    public List<Transfer> getMyTransfers(int currentUserId) {
        List<Transfer> transfers = new ArrayList<>();
        String url = BASE_API_URL + "/my_transfers";
        ResponseEntity<Transfer[]> response = restTemplate.exchange(url, HttpMethod.GET,
                makeAuthEntity(currentUser), Transfer[].class);
        transfers = Arrays.asList(response.getBody());
        return transfers;
    }

    public Transfer getTransferById(int transferId) {
        Transfer transfer = null;
        try {
            transfer = restTemplate.getForObject(BASE_API_URL + "reservations/" + transferId, Transfer.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfer;
    }




    private HttpEntity<Void> makeAuthEntity(AuthenticatedUser user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(headers);
    }
    private HttpEntity<Transfer> makeTransferAuthEntity(AuthenticatedUser user, Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(transfer, headers);
    }
    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(headers);
    }
}
