package docker.client.example;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class SmokeTest {

    private final ThreadLocal<ContainerManager> containerManagerThreadLocal = new ThreadLocal<>();
    TestProperties testProperties;

    @BeforeMethod
    public void setup() {

        this.testProperties = new TestProperties("src/test/resources/application.properties");

        try {
            this.containerManagerThreadLocal.set(new ContainerManager(testProperties.dockerConnectionString, testProperties.dockerContainerImagePath));
        } catch (IOException ioException) {
            System.out.println("Error creating image");
            System.out.println(ioException.getMessage());
        }
    }

    @Test(dataProvider = "smokeTestPositive", dataProviderClass = SmokeTestDataProvider.class)
    public void assertRootEndpoint(String name, String info, int port) {

        boolean createResult = this.containerManagerThreadLocal.get().createContainer(port, 80, name, "INFO=" + info);

        Assert.assertTrue(createResult, "Error creating container");
        if (createResult) {
            boolean startResult = this.containerManagerThreadLocal.get().startContainer();
            Assert.assertTrue(startResult, "Error starting container");
        }

        given().when().get("http://" + this.testProperties.dockerHost + ":" + port)
                .then()
                .statusCode(200)
                .body("message", equalTo(info));
    }

    @Test(dataProvider = "smokeTestNegative", dataProviderClass = SmokeTestDataProvider.class)
    public void assertRootEndpointMessageFail(String name, String info, int port) {

        boolean createResult = this.containerManagerThreadLocal.get().createContainer(port, 80, name, "INFO=" + info);

        Assert.assertTrue(createResult, "Error creating container");

        if (createResult) {
            boolean startResult = this.containerManagerThreadLocal.get().startContainer();
            Assert.assertTrue(startResult, "Error starting container");
        }

        given().when().get("http://" + this.testProperties.dockerHost + ":" + port)
                .then()
                .statusCode(200)
                .body("message", equalTo(info + " this test should fail"));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {

        this.containerManagerThreadLocal.get().stopContainer();
        this.containerManagerThreadLocal.get().removeContainer();
    }
}
