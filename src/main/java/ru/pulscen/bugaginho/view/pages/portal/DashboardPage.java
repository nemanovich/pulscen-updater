package ru.pulscen.bugaginho.view.pages.portal;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfAllElements;

public class DashboardPage {

    private WebDriver driver;

	@FindBy(className = "cii-title")
	public List<WebElement> companyTitles;

	public DashboardPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);

	}

    public List<String> getSiteUrls() {
        return new WebDriverWait(driver, 30).until(visibilityOfAllElements(companyTitles)).stream()
                .map(e -> e.getAttribute("href"))
                .collect(toList());
    }

}
