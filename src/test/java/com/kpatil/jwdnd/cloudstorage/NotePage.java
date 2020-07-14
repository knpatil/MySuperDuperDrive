package com.kpatil.jwdnd.cloudstorage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class NotePage {

    @FindBy(id = "nav-notes-tab")
    private WebElement noteTab;

    @FindBy(id = "addNewNote")
    private WebElement addNewNote;

    @FindBy(id = "note-title")
    private WebElement noteTitle;

    @FindBy(id = "note-description")
    private WebElement noteDescription;

    @FindBy(id = "saveChanges")
    private WebElement saveChangesButton;

    @FindBy(id = "logout")
    private WebElement logoutButton;

    public NotePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void goToNotesTab() throws InterruptedException {
        Thread.sleep(1000);
        this.noteTab.click();
    }

    public void addNewNote(String testNote, String testDescription) throws InterruptedException {
        Thread.sleep(1000);
        // WebElement addNewNote = driver.findElement(By.id("addNewNote"));
        addNewNote.click();
        Thread.sleep(1000);
        noteTitle.sendKeys(testNote);
        noteDescription.sendKeys(testDescription);
        saveChangesButton.click();
    }

    public void logout() throws InterruptedException {
        Thread.sleep(1000);
        logoutButton.click();
    }
}
