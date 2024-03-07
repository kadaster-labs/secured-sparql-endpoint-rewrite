package kadasterfuseki.filter.user.db;

import org.apache.jena.query.QueryExecution;

import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFuseki;
import org.apache.jena.rdfconnection.SparqlQueryConnection;
import org.apache.jena.sparql.graph.GraphSPARQLService;

public class TAccess {

	String endpoint=null;
	public TAccess(String endpoint)
	{
		this.endpoint=endpoint;
		// TODO Auto-generated constructor stub
	}
	public  ResultSet select(String s)
	{
		try (RDFConnection  conn =RDFConnectionFuseki.connect(this.endpoint)) {
		QueryExecution qExec = conn.query(s) ;
		ResultSet rs = qExec.execSelect() ;
		return rs;
		
		}
	 	
	}

}
