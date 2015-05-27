package cs601.webmail;

import cs601.webmail.managers.DBConnectionManager;
import cs601.webmail.misc.STListener;
import cs601.webmail.services.DispatchServlet;
import cs601.webmail.services.SSLService;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.io.File;

public class WebmailServer {
    public static final String RESOURCES_CS601_WEBMAIL_TEMPLATES = "resources/cs601/webmail/templates";
    public static final STListener stListener = new STListener();
    static final Logger logger = Logger.getLogger(WebmailServer.class);

    public static void main(String[] args) throws Exception {
        PropertyConfigurator.configure("log4j.properties");

        DBConnectionManager db = new DBConnectionManager();
        db.init();
        String staticFilesDir = "static";
        Server server = new Server(8080);

        SSLService.configureSLL(server);

        ServletContextHandler context = new
                ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        // add a simple Servlet at "/dynamic/*"
        ServletHolder holderDynamic = new ServletHolder("dynamic", DispatchServlet.class);
        context.addServlet(holderDynamic, "/*");

        // add special pathspec of "/home/" content mapped to the homePath
        ServletHolder holderHome = new ServletHolder("static-home", DefaultServlet.class);
        holderHome.setInitParameter("resourceBase", staticFilesDir);
        holderHome.setInitParameter("dirAllowed", "true");
        holderHome.setInitParameter("pathInfoOnly", "true");
        context.addServlet(holderHome, "/files/*");

        ServletHolder bootstrapcss = new ServletHolder("bootstrap-css", DefaultServlet.class);
        bootstrapcss.setInitParameter("resourceBase", "static/bootstrap/css");
        bootstrapcss.setInitParameter("dirAllowed", "true");
        bootstrapcss.setInitParameter("pathInfoOnly", "true");
        context.addServlet(bootstrapcss, "/boot/css/*");

        ServletHolder bootstrapjs = new ServletHolder("bootstrap-js", DefaultServlet.class);
        bootstrapjs.setInitParameter("resourceBase", "static/bootstrap/js");
        bootstrapjs.setInitParameter("dirAllowed", "true");
        bootstrapjs.setInitParameter("pathInfoOnly", "true");
        context.addServlet(bootstrapjs, "/boot/js/*");

        ServletHolder bootstrapimage = new ServletHolder("static-image", DefaultServlet.class);
        bootstrapimage.setInitParameter("resourceBase", "static/image");
        bootstrapimage.setInitParameter("dirAllowed", "true");
        bootstrapimage.setInitParameter("pathInfoOnly", "true");
        context.addServlet(bootstrapimage, "/image/*");

        ServletHolder bootStrapFonts = new ServletHolder("bootstrap-fonts", DefaultServlet.class);
        bootStrapFonts.setInitParameter("resourceBase", "static/bootstrap/fonts");
        bootStrapFonts.setInitParameter("dirAllowed", "true");
        bootStrapFonts.setInitParameter("pathInfoOnly", "true");
        context.addServlet(bootStrapFonts, "/boot/fonts/*");

        // Lastly, the default servlet for root content (always needed, to satisfy servlet spec)
        // It is important that this is last.
        ServletHolder holderPwd = new ServletHolder("default", DefaultServlet.class);
        holderPwd.setInitParameter("resourceBase", "/tmp/foo");
        holderPwd.setInitParameter("dirAllowed", "true");
        context.addServlet(holderPwd, "/");

        // log using NCSA (common log format)
        // http://en.wikipedia.org/wiki/Common_Log_Format
        //
//        File requestLogFile = new File("/Users/shreyarajani/MSCS/MS-Sem4/CS-601/Projects/shreyarajani-webmail/request.log");
        File requestLogFile = new File("/var/log/webmail/request.log");
        requestLogFile.setExecutable(true, false);
        requestLogFile.setReadable(true, false);
        requestLogFile.setWritable(true, false);

        HandlerCollection handlers = new HandlerCollection();
        RequestLogHandler requestLogHandler = new RequestLogHandler();
        handlers.setHandlers(new Handler[]{context, new DefaultHandler(), requestLogHandler});

        NCSARequestLog requestLog = new NCSARequestLog(requestLogFile.getPath());
        requestLog.setRetainDays(90);
        requestLog.setAppend(true);
        requestLog.setExtended(false);
        requestLog.setLogTimeZone("GMT");
        requestLogHandler.setRequestLog(requestLog);

        server.setHandler(handlers);

        server.start();
        server.join();
    }
}