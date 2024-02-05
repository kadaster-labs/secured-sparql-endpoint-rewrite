package kadasterfuseki.filter.impl.graphs;

import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.jena.fuseki.Fuseki;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.sparql.core.DatasetGraph;
import org.apache.jena.sparql.syntax.ElementSubQuery;

import kadasterfuseki.filter.impl.BaseFilter;
import kadasterfuseki.filter.impl.service.SecuredElementVisitorForSecuredServices;
import kadasterfuseki.filter.user.ServiceFilter;
import kadasterfuseki.filter.user.User;



public class SecureGraphs {
	

	public Query query=null;
	User user=null;
	boolean debug=true;
	
	
	public SecureGraphs(HttpServletRequest request,Query query,User user,String endpoint) 
	{
	
		this.query=query;
		this.user=user;
		
		
		// Fuseki.serverLog.info("Executing locked/unlocked Graph filter");
		
		
		 if (usesFromGraph())
		 {
			if (debug) System.out.println("check 'from' graph query");
			 checkGraphs(query.getGraphURIs());
		 }
		 else
		 {
			 if (debug)  System.out.println("add 'from' clause");
			 addFromGraphs();
		 }
		 
		 if (usesFromNamedGraph())
		 {
			 if (debug) System.out.println("check 'from named' graph query");
			 checkGraphs(query.getNamedGraphURIs());
		 }
		 else
		 {
			 if (debug)  System.out.println("add 'from named' clause");
			 addFromNamedGraphs();
		 }
		 
				 
		 SecureGraphsVisitor f=new SecureGraphsVisitor();
		 query.getQueryPattern().visit(f);
		 if (checkGraphs(f.namedGraphs))
			 {
			 this.query.setLimit(0);
			 };
		 
		 
		 for (Query q:f.qs)
		 {
			 if (checkGraphs(q.getNamedGraphURIs()))
			 {
				 //niet uitvoeren
				 this.query.setLimit(0);
			 }
		 }
		 
		
		 
		 // make sure it does not contain

	}
	
	
	public boolean checkGraphs(List<String> graphs)
	{
		Vector<String> remove = new Vector<String>();
		
	
		for (String graph:graphs)
		{
			if (!user.allowedGraphs.contains(graph))
			{
				remove.add(graph);
				//datasets.removeGraph(NodeFactory.createURI(graph));
			}
		}
		for (String rg:remove)
		{
			graphs.remove(rg);
		}
		if (remove.size()>0) return true;
		
		return false;
			
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
