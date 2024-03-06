package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController
@PreAuthorize("isAuthenticated()")
public class TenmoController {
    private AccountDao accountDao;
    private UserDao userDao;

    public TenmoController(AccountDao accountDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @RequestMapping(path="/account/balance", method= RequestMethod.GET)
    public double getAccountBalanceByUserId(Principal principal) {
        User currentUser = userDao.getUserByUsername(principal.getName());
        double balance = accountDao.getAccountBalanceByUserId(currentUser.getId());
        return balance;
    }
}
