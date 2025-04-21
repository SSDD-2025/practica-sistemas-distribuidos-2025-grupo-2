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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddTopicTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        // Configurar FirefoxDriver (asegúrate de que geckodriver esté en el PATH o configúralo manualmente)
        // System.setProperty("webdriver.gecko.driver", "/path/to/geckodriver");
        driver = new FirefoxDriver();
        driver.manage().window().setSize(new Dimension(550, 683));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void addTopic() {
        // Navegar a la página principal
        driver.get("https://localhost:8443/");

        // Login
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username"))).sendKeys("User");
        driver.findElement(By.id("password")).sendKeys("123451234512345");
        driver.findElement(By.id("password")).sendKeys(Keys.ENTER);

        // Navegar a la página de temas
        wait.until(ExpectedConditions.elementToBeClickable(By.id("toggle-menu"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Topics"))).click();

        // Crear un nuevo tema
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".add-topic-btn"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("topicName"))).sendKeys("Python");

        // Obtener token CSRF

        // Enviar formulario de tema
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("form button[type='submit']")));
        submitButton.click();

        // Navegar al tema creado
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".Python"))).click();

        // Crear un nuevo post
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("ADD POST"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("title"))).sendKeys("NEW POST");
        driver.findElement(By.id("content")).sendKeys("kdfjsiogjsdiofgjosdfg");

        // Seleccionar tema del dropdown
        WebElement dropdown = driver.findElement(By.id("topic"));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//option[. = 'Python']"))).click();


        // Enviar formulario de post
        submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("form button[type='submit']")));
        submitButton.click();

        // Crear un comentario
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("ADD COMMENT"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("content"))).sendKeys("asdfasdfasdfasd");


        // Enviar formulario de comentario
        submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("form button[type='submit']")));
        submitButton.click();

        // Verificaciones
        List<WebElement> commentElements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".comment > p")));
        assertTrue(commentElements.size() > 0, "Debería haber al menos un comentario presente");
        List<WebElement> headerElements = driver.findElements(By.cssSelector("h1"));
        assertTrue(headerElements.size() > 0, "Debería haber al menos un encabezado presente");

        // Limpieza: Eliminar el post creado (si tu UI tiene un botón de eliminar)
        // Nota: Esto depende de tu interfaz. Ajusta según corresponda.
        /*
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".delete-post-btn"))).click();
        csrfToken = driver.findElement(By.name("_csrf")).getAttribute("value");
        driver.findElement(By.name("_csrf")).sendKeys(csrfToken);
        driver.findElement(By.cssSelector("form.delete-post button[type='submit']")).click();
        */
    }
}