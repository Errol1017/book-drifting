package project.resource.properties;

/**
 * Created by Errol on 17/5/27.
 */
public class ServerProperties {

    private String fileBasePath;
    private String host;
    private String redirect;
    private String remote;

    private static ServerProperties instance = new ServerProperties();

    private ServerProperties() {}

    public static ServerProperties getInstance() {
        return instance;
    }

    public void init(String fileBasePath, String host, String redirect, String remote) {
        if (this.fileBasePath == null) {
            this.fileBasePath = fileBasePath;
            this.host = host;
            this.redirect = redirect;
            this.remote = remote;
        }
    }

    public String getFileBasePath() {
        return fileBasePath;
    }

    public String getHost() {
        return host;
    }

    public String getRedirect() {
        return redirect;
    }

    public String getRemote() {
        return remote;
    }

}
