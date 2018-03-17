package ru.pulscen.bugaginho.view.pages.site;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.apache.commons.lang3.StringUtils.appendIfMissing;
import static org.apache.commons.lang3.StringUtils.removeEnd;
import static org.openqa.selenium.support.ui.ExpectedConditions.alertIsPresent;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOf;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.not;
import static org.openqa.selenium.support.ui.ExpectedConditions.stalenessOf;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfAllElements;

public class GoodsEditorPage {

    private WebDriver driver;

	@FindBy(css = ".js-actualize-button[type='button']")
	public WebElement confirmRelevance;

	public GoodsEditorPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
	}

    public void openOnSite(String shopUrl) {
        this.driver.get(appendIfMissing(shopUrl, "/") + "predl/binds");
    }

    public void refreshDates() {
        new WebDriverWait(driver, 30).until(elementToBeClickable(confirmRelevance)).click();
        new WebDriverWait(driver, 30).until(alertIsPresent());
        driver.switchTo().alert().accept();
        driver.switchTo().defaultContent();
        new WebDriverWait(driver, 30).until(invisibilityOfElementLocated(By.id("progressbar")));
    }


}
