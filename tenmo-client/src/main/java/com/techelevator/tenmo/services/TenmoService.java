package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class TenmoService {
    private String baseApiUrl;
    private AuthenticatedUser currentUser;
    private RestTemplate restTemplate = new RestTemplate();

    public TenmoService(String baseApiUrl) {
        this.baseApiUrl = baseApiUrl;
    }

    public Account getAccountByUserId(int userId) {
        String url = baseApiUrl + "" + userId;
        return restTemplate.getForObject(url, Account.class);
    }

    public Double getAccountBalanceByUserId(AuthenticatedUser user) {
        ResponseEntity<Double> response = restTemplate.exchange(baseApiUrl + "/id",
                HttpMethod.GET, makeAuthEntity(user), double.class);
        return response.getBody();
    }

    public Product (String sku) {
        // Make the URL
        String url = baseApiUrl + "products/" + sku;
        // Call the API
        return restTemplate.getForObject(url, Product.class);
    }

    public double getAccountBalanceByUserId(Account account) {
        double userId = account.getUserId();
        return userId;
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(headers);
    }

}
