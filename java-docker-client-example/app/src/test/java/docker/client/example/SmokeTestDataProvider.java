package docker.client.example;

import org.testng.annotations.DataProvider;

public class SmokeTestDataProvider {
    @DataProvider(name = "smokeTestPositive", parallel = true)
    public Object[][] smokeTestPositive() {
        return new Object[][] {
                {"test-01", "hello world from test-01", 8081 },
                {"test-02", "hello world from test-02", 9082 },
        };
    }

    @DataProvider(name = "smokeTestNegative", parallel = true)
    public Object[][] smokeTestNegative() {
        return new Object[][] {
                {"test-01", "hello world from test-01", 8089 },
                {"test-02", "hello world from test-02", 9089 },
        };
    }
}
