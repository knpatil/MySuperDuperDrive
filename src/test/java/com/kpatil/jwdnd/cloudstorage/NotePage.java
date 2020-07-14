package com.kpatil.jwdnd.cloudstorage;

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
        addNewNote.click();
        Thread.sleep(1000);
        noteTitle.sendKeys(testNote);
        noteDescription.sendKeys(testDescription);
        saveChangesButton.click();
        Thread.sleep(500);
    }

    public void logout() throws InterruptedException {
        Thread.sleep(1000);
        logoutButton.click();
        Thread.sleep(500);
    }

    public void editNote(WebElement noteToEdit, String testNote, String testDescription) throws InterruptedException {
        Thread.sleep(500);
        noteToEdit.click();
        Thread.sleep(500);

        noteTitle.clear();
        noteTitle.sendKeys(testNote);

        noteDescription.clear();
        noteDescription.sendKeys(testDescription);

        saveChangesButton.click();
        Thread.sleep(500);
    }

    public void deleteNote(WebElement noteToDelete) throws InterruptedException {
        Thread.sleep(500);
        noteToDelete.click();
        Thread.sleep(500);
    }
}
