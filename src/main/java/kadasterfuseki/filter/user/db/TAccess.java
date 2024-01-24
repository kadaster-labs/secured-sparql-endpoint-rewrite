package kadasterfuseki.filter.user.db;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;

public class TAccess {

	String endpoint=null;
	public TAccess(String endpoint)
	{
		this.endpoint=endpoint;
		// TODO Auto-generated constructor stub
	}
	public  ResultSet select(String s)
	{
		  QueryExecution queryExecution = QueryExecutionFactory.sparqlService(endpoint, s);
		  return queryExecution.execSelect();
	}

}
