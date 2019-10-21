package ru.pulscen.bugaginho.view.pages.site;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.apache.commons.lang3.StringUtils.appendIfMissing;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.textToBePresentInElement;

public class GoodsEditorPage {

    private WebDriver driver;

    @FindBy(xpath = ".//*[contains(text(), 'Редактирование некоторых элементов этой компании закрыто')]")
    public WebElement closeEditMessage;

    @FindBy(className = "error-block")
    public WebElement errorBlock;

    @FindBy(css = ".js-actualize-button")
    public WebElement confirmRelevance;

    @FindBy(css = ".js-actualize-flash")
    public WebElement confirmPopup;

    public GoodsEditorPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isSiteEditClosed() {
        try {
            return closeEditMessage.isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }
    public void openOnSite(String shopUrl) {
        this.driver.get(appendIfMissing(shopUrl, "/") + "predl/binds");

        //обход бага кросcдоменной авторизации - с первого раза не авторизует на другом домене
        new WebDriverWait(driver, 90)
                .until((driver) -> {
                    try {
                        if (errorBlock.getText().contains("Ошибка 403")) {
                            driver.navigate().refresh();
                        }
                        return !errorBlock.getText().contains("Ошибка 403");
                    } catch (NoSuchElementException e) {
                        return true;
                    }
                });
    }

    public void refreshDates() {
        new WebDriverWait(driver, 30).until(elementToBeClickable(confirmRelevance));
        try {
            Thread.sleep(3_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        confirmRelevance.click();
        new WebDriverWait(driver, 60)
                .until(textToBePresentInElement(confirmPopup, "Актуализация"));
    }


}
