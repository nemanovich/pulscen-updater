package ru.pulscen.bugaginho;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.pulscen.bugaginho.config.JAXBUserParser;
import ru.pulscen.bugaginho.view.pages.portal.DashboardPage;
import ru.pulscen.bugaginho.view.pages.portal.LoginPage;
import ru.pulscen.bugaginho.view.pages.site.GoodsEditorPage;

import java.io.IOException;
import java.util.stream.Collectors;

public class UpdatePulscen {

    public static Logger log = LoggerFactory.getLogger("main");

    public static String AUTH_URL = "http://www.pulscen.ru/users/session/request?return_to=http://www.pulscen.ru/dashboard";

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

                for (String url : new DashboardPage(driver).getSiteUrls()) {
                    try {
                        GoodsEditorPage editor = new GoodsEditorPage(driver);
                        editor.openOnSite(url);
                        //обход бага кросcдоменной авторизации - с первого раза не авторизует на другом домене
                        if (!url.contains("pulscen")) {
                            Thread.sleep(1_000);
                            driver.navigate().refresh();
                        }
                        editor.refreshDates();
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
