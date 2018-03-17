package ru.pulscen.bugaginho.view.pages.portal;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.pulscen.bugaginho.User;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

public class LoginPage {

    private WebDriver driver;

	@FindBy(className = "js-input-phone-auth")
	public WebElement loginInput;

	@FindBy(className = "js-input-password-auth")
	public WebElement passwordInput;

    @FindBy(className = "apress-button")
    public WebElement submitButton;

	public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);

	}

	public void login(User user) {
		new WebDriverWait(driver, 30).until(visibilityOf(loginInput)).clear();
        loginInput.sendKeys(user.getLogin());
        passwordInput.clear();
        passwordInput.sendKeys(user.getPassword());
        submitButton.click();
	}
}
