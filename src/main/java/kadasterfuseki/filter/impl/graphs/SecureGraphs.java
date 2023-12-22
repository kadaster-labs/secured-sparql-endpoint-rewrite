package kadasterfuseki.filter.impl.graphs;

import java.util.List;
import java.util.Vector;

import org.apache.jena.fuseki.Fuseki;
import org.apache.jena.query.Query;

import kadasterfuseki.filter.user.User;



public class SecureGraphs {
	

	Query query=null;
	User user=null;
	boolean debug=false;
	
	public SecureGraphs(Query query,User user) 
	{
		this.query=query;
		
		this.user=user;
		 Fuseki.serverLog.info("Executing locked/unlocked Graph filter");
		 if (usesFromGraph())
		 {
			if (debug) System.out.println("check from graph query");
			 checkGraphs(query.getGraphURIs());
		 }
		 else
		 {
			 if (debug)  System.out.println("add from clause");
			 addFromGraphs();
		 }
		 
		 if (usesFromNamedGraph())
		 {
			 if (debug) System.out.println("check from named graph query");
			 checkGraphs(query.getNamedGraphURIs());
		 }
		 else
		 {
			 if (debug)  System.out.println("add from named clause");
			 addFromNamedGraphs();
		 }
		 
		 if (debug) 
		 {
			 System.out.println(" resulting query "+query.toString());
		 }

	}
	
	
	public void checkGraphs(List<String> graphs)
	{
		Vector<String> remove = new Vector<String>();
		
	
		for (String graph:graphs)
		{
			if (!user.allowedGraphs.contains(graph))
			{
				remove.add(graph);
			}
		}
		for (String rg:remove)
		{
			graphs.remove(rg);
		}
			
	}
	
	public void addFromGraphs()
	{
		for (String graph:user.allowedGraphs)
		{
			query.addGraphURI(graph);
			query.addNamedGraphURI(graph);
		}
	}
	public void addFromNamedGraphs()
	{
		for (String graph:user.allowedGraphs)
		{
			query.addNamedGraphURI(graph);
		}
	}
	public boolean usesFromGraph()
	{
		if (query.getGraphURIs().size()>0) return true;
		return false;
	}
	public boolean usesFromNamedGraph()
	{
		if (query.getNamedGraphURIs().size()>0) return true;
		return false;
	}
	
	

}
