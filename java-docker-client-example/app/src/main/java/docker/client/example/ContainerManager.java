package docker.client.example;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.DockerClientBuilder;

import java.io.FileInputStream;
import java.io.IOException;

public class ContainerManager {

    private String containerId;
    private String dockerConnectionString;

    private String dockerImageId;

    public ContainerManager(String dockerConnectionString, String dockerImagePath) throws IOException {

        this.dockerConnectionString = dockerConnectionString;

        DockerClient dockerClient = DockerClientBuilder.getInstance(dockerConnectionString).build();

        this.dockerImageId = dockerClient.buildImageCmd()
                .withTarInputStream(new FileInputStream(dockerImagePath))
                .withNoCache(true)
                .exec(new BuildImageResultCallback()).awaitImageId();

        dockerClient.close();
    }

    public boolean createContainer( int port, int exposedPort, String desiredContainerName, String envString) {

        if (null == this.dockerImageId) {
            return false;
        }

        if (envString.isEmpty()) {
            return false;
        }

        DockerClient dockerClient = DockerClientBuilder.getInstance(dockerConnectionString).build();

        Ports portBindings = new Ports();
        portBindings.bind(ExposedPort.tcp(exposedPort), Ports.Binding.bindPort(port));

        CreateContainerResponse createContainerResponse = dockerClient.createContainerCmd(this.dockerImageId)
                .withName(desiredContainerName)
                .withEnv(envString)
                .withExposedPorts(ExposedPort.tcp(80))
                .withHostConfig(HostConfig.newHostConfig().withPortBindings(portBindings))
                .exec();
        this.containerId = createContainerResponse.getId();

        try {
            dockerClient.close();
        } catch (IOException ioException) {
            return false;
        }
        return true;
    }
    public boolean createContainer(int port, int exposedPort, String desiredContainerName) {

        if (null == this.dockerImageId) {
            return false;
        }

        DockerClient dockerClient = DockerClientBuilder.getInstance(dockerConnectionString).build();

        Ports portBindings = new Ports();
        portBindings.bind(ExposedPort.tcp(exposedPort), Ports.Binding.bindPort(port));

        CreateContainerResponse createContainerResponse = dockerClient.createContainerCmd(this.dockerImageId)
                .withName(desiredContainerName)
                .withExposedPorts(ExposedPort.tcp(80))
                .withHostConfig(HostConfig.newHostConfig().withPortBindings(portBindings))
                .exec();
        this.containerId = createContainerResponse.getId();

        try {
            dockerClient.close();
        } catch (IOException ioException) {
            return false;
        }
        return true;
    }

    public boolean startContainer() {

        DockerClient dockerClient = DockerClientBuilder.getInstance(dockerConnectionString).build();

        // Start the container
        if (null == this.containerId) {
            return false;
        }

        dockerClient.startContainerCmd(this.containerId).exec();

        try {
            dockerClient.close();
        } catch (IOException ioException) {
            return false;
        }

        return true;
    }

    public boolean stopContainer() {
        DockerClient dockerClient = DockerClientBuilder.getInstance(dockerConnectionString).build();

        // Stop the container
        if (null == this.containerId) {
            return false;
        }

        dockerClient.stopContainerCmd(this.containerId).exec();

        try {
            dockerClient.close();
            return true;
        } catch (IOException ioException) {
            return false;
        }
    }

    public boolean removeContainer() {
        DockerClient dockerClient = DockerClientBuilder.getInstance(dockerConnectionString).build();

        // Delete the container
        if (null == this.containerId) {
            return false;
        }

        dockerClient.removeContainerCmd(this.containerId).exec();

        try {
            dockerClient.close();
            this.containerId = null;
            return true;
        } catch (IOException ioException) {
            return false;
        }
    }
}
