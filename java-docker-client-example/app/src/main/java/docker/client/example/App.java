package docker.client.example;

import java.io.IOException;

public class App {

    public static void main(String[] args) {
        
		TestProperties testProperties = new TestProperties("application.properties");
    	
    	System.out.println("Docker Engine API Java Example");
    	System.out.println("Interact with remote docker ");

		ContainerManager containerManager = null;
		 try {
			 containerManager = new ContainerManager(testProperties.dockerConnectionString, testProperties.dockerContainerImagePath);
		 } catch (IOException ioException) {
			 System.out.println("Error creating image");
			 System.out.println(ioException.getMessage());
		 }

	 	boolean createResult = containerManager.createContainer(8081, 80, "testing123", "INFO=" + "esp32-001");
		boolean startResult = containerManager.startContainer();

		System.out.println(createResult);
		System.out.println(startResult);

		containerManager.stopContainer();
		containerManager.removeContainer();

    }
}
