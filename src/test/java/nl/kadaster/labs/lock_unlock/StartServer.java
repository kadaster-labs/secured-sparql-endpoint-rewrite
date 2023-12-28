package nl.kadaster.labs.lock_unlock;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.apache.jena.fuseki.Fuseki;
import org.apache.jena.fuseki.cmd.FusekiArgs;
import org.apache.jena.fuseki.cmd.FusekiCmd;
import org.apache.jena.fuseki.cmd.JettyServerConfig;
import org.apache.jena.fuseki.main.FusekiServer.Builder;
import org.apache.jena.fuseki.main.JettyServer;
import org.apache.jena.fuseki.main.cmds.FusekiMain;
import org.apache.jena.fuseki.main.cmds.FusekiMainCmd;
import org.apache.jena.fuseki.main.FusekiServer;
import org.apache.jena.sparql.engine.QueryEngineRegistry;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.apache.jena.fuseki.servlets.FusekiFilter;
import org.apache.jena.fuseki.system.FusekiLogging;

import kadasterfuseki.filter.LockedQueryEngineFactory;
import kadasterfuseki.filter.LockedServletFilter;


public class StartServer {
	
public static void serverWithoutGui()
{
	//FUSEKI_HOME=C:\DSchijf\dbdata\fusekidb
	//[2023-12-28 13:22:45] Config     INFO  FUSEKI_BASE=C:\DSchijf\dbdata\fusekidb
	System.out.println("start server");
	 FusekiLogging.setLogging();
	Builder builder= FusekiServer.create();
	builder.port(3030).verbose(true).addFilter("/*",new LockedServletFilter()).enableCors(true);
	//builder.parseConfigFile("C:\\DSchijf\\dbdata\\fusekidb/config.ttl");
	builder.parseConfigFile("C:\\DSchijf\\dbdata\\fusekidb\\configuration/unlocked.ttl");

	FusekiServer server  = builder.build();

	
	
	server.start();
	server.join();
	System.out.println("end server");
	
}
public static void withGui()
{
	 String[] args = {};
	  FusekiCmd.main(args);
}


    public static void main(String[] _args)
    {
    	withGui();
    }
    public static void test()
    {
        //String[] args = { "--mem", "/ds" };
    	
    	//System.out.println("hallo");
    	if (false)
    	{
    		//old
    		Fuseki.init(); // so that the new queryengine filter sticks.
    		QueryEngineRegistry.addFactory(new LockedQueryEngineFactory());
    	}

    	
    	
    //	  ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    	  
    	//  context.addFilter( LockedServletFilter.class, "/*",  EnumSet.of(DispatcherType.REQUEST));
    	  
    
    	  
    	
    	
        String[] args = {"--conf=C:/DSchijf/dbdata/fusekidb/config.ttl", "--ping"};
   
        try {
        	
        	
        	
			Builder builder=FusekiMain.builder(args);
			builder.port(3030).verbose(true).addFilter("/*",new LockedServletFilter()).enableCors(true);
			builder.parseConfigFile("C:\\DSchijf\\dbdata\\fusekidb\\configuration/unlocked.ttl");
			
			//builder.parseConfigFile("C:\\DSchijf\\dbdata\\fusekidb\\configuration/unlocked.ttl");
			FusekiServer fs=builder.build();
			fs.start();
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //FusekiCmd.main(args);
        
        
      
      
       // System.out.println("hallo2");
    }
}
