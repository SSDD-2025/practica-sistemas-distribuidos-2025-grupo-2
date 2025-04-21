package codehub.grupo2;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Dimension;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddUserLogInTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        // Configurar FirefoxDriver
        // System.setProperty("webdriver.gecko.driver", "/path/to/geckodriver");
        driver = new FirefoxDriver();
        driver.manage().window().setSize(new Dimension(1936, 1048));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void addUserLogIn() {
        // Navegar a la página principal
        driver.get("https://localhost:8443/");

        // Ir al formulario de registro
        wait.until(ExpectedConditions.elementToBeClickable(By.id("register-btn"))).click();

        // Completar formulario de registro
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email"))).sendKeys("User@gmail.com");
        driver.findElement(By.id("username")).sendKeys("User");
        driver.findElement(By.id("password")).sendKeys("123451234512345");

        // Obtener token CSRF
        String csrfToken = driver.findElement(By.name("_csrf")).getAttribute("value");

        // Enviar formulario de registro
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("form#register-form button[type='submit']")));
        driver.findElement(By.name("_csrf")).sendKeys(csrfToken);
        submitButton.click();

        // Login con el usuario recién creado
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username"))).sendKeys("User");
        driver.findElement(By.id("password")).sendKeys("123451234512345");
        driver.findElement(By.id("password")).sendKeys(Keys.ENTER);

        // Verificar que el login fue exitoso (comprobar título o elemento visible)
        WebElement titleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".title")));
        assertTrue(titleElement.isDisplayed(), "El título debería estar visible tras el login");

        // Limpieza: Eliminar el usuario creado (si tu UI lo permite)
        // Nota: Ajusta según tu interfaz. Por ejemplo, navegar a la página de perfil y eliminar.
        /*
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Profile"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("delete-user-btn"))).click();
        csrfToken = driver.findElement(By.name("_csrf")).getAttribute("value");
        driver.findElement(By.name("_csrf")).sendKeys(csrfToken);
        driver.findElement(By.cssSelector("form.delete-user button[type='submit']")).click();
        */
    }
}