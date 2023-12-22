package nl.kadaster.labs.lock_unlock.fuseki;

import java.util.Set;

import org.apache.jena.fuseki.Fuseki;
import org.apache.jena.fuseki.main.FusekiServer.Builder;
import org.apache.jena.fuseki.main.sys.FusekiAutoModule;
import org.apache.jena.fuseki.server.Operation;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.sparql.engine.QueryEngineRegistry;

import kadasterfuseki.filter.SparqlFilter;

/**
 * Skeleton copied from:
 * 
 * -
 * https://github.com/apache/jena/blob/main/jena-fuseki2/jena-fuseki-main/src/test/java/org/apache/jena/fuseki/main/sys/ModuleByServiceLoader.java
 * -
 * https://github.com/apache/jena/blob/main/jena-fuseki2/jena-fuseki-main/src/test/java/org/apache/jena/fuseki/main/examples/ExFuseki_04_CustomOperation_Module.java
 */
public class ModuleByServiceLoader implements FusekiAutoModule {

    private Operation myOperation = null;

    public ModuleByServiceLoader() {
        this.myOperation = Operation.alloc("http://example/extra-service", "extra-service", "Test");
    }

    @Override
    public String name() {
        return "LockUnlockSecureFilter";
    }

    @Override
    public void prepare(Builder serverBuilder, Set<String> datasetNames, Model configModel) {
        Fuseki.serverLog.info("Add authorization filter");
        // Register only for the server being built.
   	   QueryEngineRegistry.addFactory(new SparqlFilter());
   	   
        if (false)
        {
        	serverBuilder.registerOperation(myOperation, new AuthorizationFilter());
        	datasetNames.forEach(name -> serverBuilder.addEndpoint(name, "extra", myOperation));
        }
    }

}