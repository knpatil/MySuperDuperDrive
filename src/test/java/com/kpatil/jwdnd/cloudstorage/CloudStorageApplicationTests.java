package com.kpatil.jwdnd.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

    private static final String SERVER = "http://localhost:";
    private static final String USERNAME = "test11";
    private static final String PASSWORD = "test1234";
    private static final String FIRST_NAME = "First Name";
    private static final String LAST_NAME = "Last Name";
    private static final String LOGIN_URL = "/login";
    private static final String SIGN_UP_URL = "/signup";

    @LocalServerPort
    private int port;

    private static WebDriver driver;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @BeforeEach
    public void beforeEach() {
    }

    @AfterEach
    public void afterEach() {
    }

    @AfterAll
    static void afterAll() {
        if (driver != null) {
            driver.quit();
        }
        driver = null;
    }

    @Test
    public void getLoginPage() {
        driver.get(getLoginUrl());
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    public void getSignUpPage() {
        driver.get(getSignUpUrl());
        Assertions.assertEquals("Sign Up", driver.getTitle());
    }

    @Test
    public void testUnauthorizedHomePageAccess() {
        driver.get(getHomePageUrl());
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    public void testUserSignUpLoginAndLogout() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 30);

        // sign up user
        driver.get(getSignUpUrl());
        SignUpPage singUpPage = new SignUpPage(driver);
        singUpPage.signUp(FIRST_NAME, LAST_NAME, USERNAME, PASSWORD);

        // login user
        driver.get(getLoginUrl());
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(USERNAME, PASSWORD);

        // assertions
        Assertions.assertEquals("Home", driver.getTitle());
        WebElement welcomeTextField = driver.findElement(By.id("welcomeText"));
        assertThat(welcomeTextField.getText()).isEqualTo("Welcome " + FIRST_NAME);

        // logout
        loginPage.logout();
        assertThat(driver.getTitle()).isEqualTo("Login");

        // try accessing home page after logout
        driver.get(getHomePageUrl());
        assertThat(driver.getTitle()).isEqualTo("Login"); // redirect to login page
    }

    @Test
    public void testNoteCreateEditDelete() throws InterruptedException {
        // sign up user
        driver.get(getSignUpUrl());
        SignUpPage singUpPage = new SignUpPage(driver);
        singUpPage.signUp(FIRST_NAME, LAST_NAME, USERNAME, PASSWORD);

        // login user
        driver.get(getLoginUrl());
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(USERNAME, PASSWORD);

        // go to notes tab
        NotePage notePage = new NotePage(driver);
        notePage.goToNotesTab();

        // create 3 notes
        for (int id = 1; id <= 3; id++) {
            // create a new note
            notePage.addNewNote("Test Note " + id, "Test Description for note " + id);
            // assert note creation
            WebElement noteTitle = driver.findElement(By.id("note" + id));
            assertThat(noteTitle.getText()).isEqualTo("Test Note " + id);
        }

        // edit second note
        WebElement noteToEdit = driver.findElement(By.id("editNote2"));
        notePage.editNote(noteToEdit, "Edited Test Note 2", "Edited Test Note Description 2");
        // assert note edit
        WebElement noteTitle = driver.findElement(By.id("note2"));
        assertThat(noteTitle.getText()).isEqualTo("Edited Test Note 2");

        // delete third note
        WebElement noteToDelete = driver.findElement(By.id("deleteNote3"));
        notePage.deleteNote(noteToDelete);
        // assert note deletion
        assertThatThrownBy(() -> driver.findElement(By.id("note3")))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Unable to locate element:");

        // assert that remaining 2 notes are still there
        for (int id = 1; id <= 2; id++) {
            WebElement title = driver.findElement(By.id("note" + id));
            assertThat(title.getText()).contains("Test Note " + id);
        }

        // logout
        notePage.logout();
    }

    @Test
    public void testCredentialCreateEditDelete() throws InterruptedException {
        // sign up user
        driver.get(getSignUpUrl());
        SignUpPage singUpPage = new SignUpPage(driver);
        singUpPage.signUp(FIRST_NAME, LAST_NAME, USERNAME, PASSWORD);

        // login user
        driver.get(getLoginUrl());
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(USERNAME, PASSWORD);

        // go to notes tab
        CredentialPage credentialPage = new CredentialPage(driver);
        credentialPage.goToCredentialsTab();

        // create 3 credentials
        for (int id = 1; id <= 3; id++) {
            // create a new note
            credentialPage.addNewCredential("URL" + id, "Username" + id, "Password" + id);
            // assert note creation
            WebElement credentialUrl = driver.findElement(By.id("credentialUrl" + id));
            assertThat(credentialUrl.getText()).isEqualTo("URL" + id);
        }

        // edit second note
        WebElement credentialToEdit = driver.findElement(By.id("editCredential2"));
        credentialPage.editCredential(credentialToEdit, "Edited URL2", "EditedUsername2", "EditedPassword2");
        // assert note edit
        WebElement url = driver.findElement(By.id("credentialUrl2"));
        assertThat(url.getText()).isEqualTo("Edited URL2");

        // delete third note
        WebElement credentialToDelete = driver.findElement(By.id("deleteCredential3"));
        credentialPage.deleteCredential(credentialToDelete);
        // assert note deletion
        assertThatThrownBy(() -> driver.findElement(By.id("credentialUrl3")))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Unable to locate element:");

        // assert that remaining 2 notes are still there
        for (int id = 1; id <= 2; id++) {
            url = driver.findElement(By.id("credentialUrl" + id));
            assertThat(url.getText()).contains("URL" + id);
        }
        
        // logout
        credentialPage.logout();
    }

    private String getHomePageUrl() {
        return SERVER + this.port + "/home";
    }

    private String getLoginUrl() {
        return SERVER + this.port + LOGIN_URL;
    }

    private String getSignUpUrl() {
        return SERVER + this.port + SIGN_UP_URL;
    }
}
