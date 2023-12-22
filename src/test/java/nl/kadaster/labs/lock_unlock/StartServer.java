package nl.kadaster.labs.lock_unlock;

import org.apache.jena.fuseki.Fuseki;
import org.apache.jena.fuseki.cmd.FusekiCmd;
import org.apache.jena.fuseki.webapp.FusekiEnv;
import org.apache.jena.sparql.engine.QueryEngineRegistry;

import kadasterfuseki.filter.SparqlFilter;



public class StartServer 
{
    public static void main(String[] _args)
    {
        //String[] args = { "--mem", "/ds" };
    	
    	//System.out.println("hallo");
    	if (true)
    	{
    	Fuseki.init(); // so that the new queryengine filter sticks.
    	 QueryEngineRegistry.addFactory(new SparqlFilter());
    	}
    	
    	
        String[] args = {};
        FusekiCmd.main(args);
       // System.out.println("hallo2");
    }
}
