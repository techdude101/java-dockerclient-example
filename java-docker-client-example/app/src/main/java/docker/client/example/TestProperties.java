package docker.client.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class TestProperties {

    private Properties properties = new Properties();
    public String dockerConnectionString;
    public String dockerContainerImagePath;

    public String dockerHost;
    public TestProperties(String filename) {
        String appConfigPath = new File(filename).getAbsolutePath();

        try {
            properties.load(new FileInputStream(appConfigPath));

            this.dockerConnectionString = properties.getProperty("dockerConnectionString");
            this.dockerContainerImagePath = properties.getProperty("dockerContainerImagePath");
            this.dockerHost = properties.getProperty("dockerHost");
        } catch (IOException ioException) {
            System.out.println(ioException);
        }
    }






}
