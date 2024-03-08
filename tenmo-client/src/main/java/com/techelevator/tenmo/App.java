package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TenmoService;

import java.util.List;

public class App {

    public static final int MENU_EXIT = 0;
    public static final int LOGIN_MENU_REGISTER = 1;
    public static final int LOGIN_MENU_LOGIN = 2;
    public static final int MAIN_MENU_VIEW_BALANCE = 1;
    public static final int MAIN_MENU_VIEW_TRANSFER_HISTORY = 2;
    public static final int MAIN_MENU_VIEW_PENDING_REQUESTS = 3;
    public static final int MAIN_MENU_SEND_TE_BUCKS = 4;
    public static final int MAIN_MENU_REQUEST_TE_BUCKS = 5;

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;
    private TenmoService tenmoService = new TenmoService();

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            tenmoService.setCurrentUser(currentUser);
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != MENU_EXIT && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == LOGIN_MENU_REGISTER) {
                handleRegister();
            } else if (menuSelection == LOGIN_MENU_LOGIN) {
                handleLogin();
            } else if (menuSelection != MENU_EXIT) {
                consoleService.printMessage("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        consoleService.printMessage("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            consoleService.printMessage("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != MENU_EXIT) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == MAIN_MENU_VIEW_BALANCE) {
                viewCurrentBalance();
            } else if (menuSelection == MAIN_MENU_VIEW_TRANSFER_HISTORY) {
                viewTransferHistory();
            } else if (menuSelection == MAIN_MENU_VIEW_PENDING_REQUESTS) {
                viewPendingRequests();
            } else if (menuSelection == MAIN_MENU_SEND_TE_BUCKS) {
                sendBucks();
            } else if (menuSelection == MAIN_MENU_REQUEST_TE_BUCKS) {
                requestBucks();
            } else if (menuSelection == MENU_EXIT) {
                continue;
            } else {
                consoleService.printMessage("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
		// TODO Auto-generated method stub
        System.out.println("Your current account balance is: $" + tenmoService.getAccountBalanceByUserId(currentUser.getUser().getId()));

	}

	private void viewTransferHistory() {
		// TODO Auto-generated method stub
		
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}

	private void sendBucks() {
		// TODO Auto-generated method stub
        System.out.println("-------------------------------------------");
        System.out.println("Users");
        System.out.println("ID          Name                  ");
        System.out.println("-------------------------------------------");
        List<User> availableUsers =  tenmoService.getAllUsersExceptMyself();

        for (User user : availableUsers) {
            System.out.println(user.getId() + "      " + user.getUsername());
        }

        int selectedUserId = consoleService.promptForInt("Enter ID of user you are sending to (0 to cancel):");
        if (selectedUserId == 0) {
            return;
        }
        if (selectedUserId == currentUser.getUser().getId()) {
            System.out.println("Please select a valid userId");
            return;
        }

        if ( !(availableUsers.contains(tenmoService.getUserById(selectedUserId))) ) {
            System.out.println("Please select a valid userId");
            return;
        }

        double selectedAmount = consoleService.promptForBigDecimal("Enter the amount to transfer:").doubleValue();


        if (selectedAmount <= 0 || selectedAmount >=
                tenmoService.getAccountBalanceByUserId(currentUser.getUser().getId())) {
            System.out.println("Please enter a valid amount");
            return;
        }

        Transfer transfer = new Transfer(currentUser.getUser().getId(), selectedUserId, selectedAmount);
        tenmoService.sendBucks(transfer);
	}

	private void requestBucks() {

	}

}
