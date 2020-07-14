package com.kpatil.jwdnd.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CredentialPage {

    @FindBy(id = "nav-credentials-tab")
    private WebElement credentialsTab;

    @FindBy(id = "addNewCredential")
    private WebElement addNewCredential;

    @FindBy(id = "credential-url")
    private WebElement urlField;

    @FindBy(id = "credential-username")
    private WebElement usernameField;

    @FindBy(id = "credential-password")
    private WebElement passwordField;

    @FindBy(id = "save-credentials")
    private WebElement saveCredentialsButton;

    @FindBy(id = "logout")
    private WebElement logoutButton;

    public CredentialPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void goToCredentialsTab() throws InterruptedException {
        Thread.sleep(500);
        credentialsTab.click();
        Thread.sleep(500);
    }

    public void addNewCredential(String url, String username, String password) throws InterruptedException {
        Thread.sleep(500);
        addNewCredential.click();
        Thread.sleep(500);

        urlField.sendKeys(url);
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);

        saveCredentialsButton.click();
        Thread.sleep(500);
    }

    public void editCredential(WebElement credentialToEdit, String url, String username, String password) throws InterruptedException {
        Thread.sleep(500);
        credentialToEdit.click();
        Thread.sleep(500);

        urlField.clear();
        urlField.sendKeys(url);
        usernameField.clear();
        usernameField.sendKeys(username);
        passwordField.clear();
        passwordField.sendKeys(password);

        saveCredentialsButton.click();
        Thread.sleep(500);
    }

    public void logout() throws InterruptedException {
        Thread.sleep(500);
        logoutButton.click();
        Thread.sleep(500);
    }

    public void deleteCredential(WebElement credentialToDelete) throws InterruptedException {
        Thread.sleep(500);
        credentialToDelete.click();
        Thread.sleep(500);
    }
}
