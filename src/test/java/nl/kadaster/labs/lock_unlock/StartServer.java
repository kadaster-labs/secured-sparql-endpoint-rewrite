package nl.kadaster.labs.lock_unlock;

import org.apache.jena.fuseki.cmd.FusekiCmd;
import org.apache.jena.fuseki.main.FusekiServer;
import org.apache.jena.fuseki.main.FusekiServer.Builder;
import org.apache.jena.fuseki.main.cmds.FusekiMain;

import kadasterfuseki.filter.LockedServletFilter;

public class StartServer {

	public static void withGui() {
		String[] args = {
				"--conf=src/test/resources/config.ttl",
		};
		FusekiCmd.main(args);
	}

	public static void main(String[] _args) {
		withGui();
	}

	public static void test() {
		// String[] args = { "--mem", "/ds" };

		// System.out.println("hallo");
		if (false) {
			// old
			// Fuseki.init(); // so that the new queryengine filter sticks.
			// QueryEngineRegistry.addFactory(new LockedQueryEngineFactory());
		}

		// ServletContextHandler context = new
		// ServletContextHandler(ServletContextHandler.SESSIONS);

		// context.addFilter( LockedServletFilter.class, "/*",
		// EnumSet.of(DispatcherType.REQUEST));

		String[] args = { "--conf=C:/DSchijf/dbdata/fusekidb/config.ttl", "--ping" };

		try {

			Builder builder = FusekiMain.builder(args);
			builder.port(3030).verbose(true).addFilter("/*", new LockedServletFilter()).enableCors(true);
			builder.parseConfigFile("C:\\DSchijf\\dbdata\\fusekidb\\configuration/unlocked.ttl");

			// builder.parseConfigFile("C:\\DSchijf\\dbdata\\fusekidb\\configuration/unlocked.ttl");
			FusekiServer fs = builder.build();
			fs.start();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// FusekiCmd.main(args);

		// System.out.println("hallo2");
	}
}
