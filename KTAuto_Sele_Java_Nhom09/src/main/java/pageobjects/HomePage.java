package pageobjects;

import common.Constant;
import common.Utilities;

public class HomePage extends GeneralPage {
    public HomePage open() {
        Constant.WEBDRIVER.navigate().to(Constant.RAILWAY_URL);
        Utilities.sleep(2000);
        return this;
    }
}
