package cs601.webmail.services;

import org.eclipse.jetty.server.*;
import org.eclipse.jetty.util.ssl.SslContextFactory;

/**
 * Created by shreyarajani on 5/4/15.
 */

/**
 * Reference: http://java.dzone.com/articles/adding-ssl-support-embedded and
 * https://www.sslshopper.com/article-how-to-create-a-self-signed-certificate-using-java-keytool.html
 */

public class SSLService {
    public static void configureSLL(Server server) {

        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8080);

        HttpConfiguration https = new HttpConfiguration();
        https.addCustomizer(new SecureRequestCustomizer());

        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStorePath("ssl/keystore.jks");
        sslContextFactory.setKeyStorePassword("password");
        sslContextFactory.setKeyManagerPassword("password");

        ServerConnector sslConnector = new ServerConnector(server,
                new SslConnectionFactory(sslContextFactory, "http/1.1"),
                new HttpConnectionFactory(https));
        sslConnector.setPort(8081);

        server.setConnectors(new Connector[]{connector, sslConnector});
    }
}
