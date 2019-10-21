package ru.pulscen.bugaginho;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.pulscen.bugaginho.api.PulscenApi;
import ru.pulscen.bugaginho.config.JAXBUserParser;
import ru.pulscen.bugaginho.view.pages.portal.LoginPage;
import ru.pulscen.bugaginho.view.pages.site.GoodsEditorPage;

import java.io.IOException;
import java.util.List;

public class UpdatePulscen {

    public static Logger log = LoggerFactory.getLogger("main");

    public static String AUTH_URL = "http://www.pulscen.ru/users/session/request?return_to=http://www.pulscen.ru";

    /**
     * @param args
     * @throws Exception
     * @throws IOException
     */
    public static void main(String[] args) {
        WebDriverManager.firefoxdriver().setup();

        Users users = JAXBUserParser.readUsers("Users.xml");

        for (User user : users.getUsers()) {
            FirefoxDriver driver = new FirefoxDriver();
            try {
                driver.get(AUTH_URL);
                new LoginPage(driver).login(user);
                long userId = (long) driver.executeScript("return app.config.currentUser.id");
                String userCredentialsCookie = driver.manage().getCookieNamed("user_credentials").getValue();
                List<String> companiesURL = new PulscenApi(userId, userCredentialsCookie).getCompaniesURL();

                for (String url : companiesURL) {
                    try {
                        GoodsEditorPage editor = new GoodsEditorPage(driver);
                        editor.openOnSite(url);
                        if (!editor.isSiteEditClosed()) {
                            editor.refreshDates();
                        }
                    } catch (Exception e) {
                        log.error("Cannot update company " + url, e);
                    }
                }
            } catch (Exception e) {
                log.error("Unexpected error by user " + user.getLogin(), e);
                throw e;
            } finally {
                driver.quit();
            }
        }
    }

}
