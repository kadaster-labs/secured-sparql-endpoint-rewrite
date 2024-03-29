package kadasterfuseki.filter.user;

import java.util.Vector;

import kadasterfuseki.filter.user.db.UserDB;
import kadasterfuseki.logging.SparqlLogging;



public class User {

	public Vector<String> allowedGraphs = new Vector<String>();
	public Vector<PredicateFilter> predicateFilters = new Vector<PredicateFilter>();
	public Vector<ServiceFilter> serviceFilters = new Vector<ServiceFilter>();
	public Vector<HorizontalFilter> horizontalFilters = new Vector<HorizontalFilter>();
	
	
	public String label=null;
	public boolean performGraphRestrictions=true;
	public boolean performPredicateRestrictions=true;
	public boolean performHorizontalFilters=false;
	
	String uri="http://labs.kadaster.nl/user#";
	
	public User(String label)
	{
	  this.label=label;
	  this.uri+=label;
	 // System.out.println("creating user "+label);
	 // addTestServiceFilters();
	  
	}
	
	public void addAllowedGraph(String graph)
	{
		graph=graph.replaceAll("\"", "");
		allowedGraphs.add(graph);
	}
	
	
	public boolean needsLoging()
	{
		if (this.label==UserDB.SystemPersona) return false;
		if (this.label.equalsIgnoreCase("all")) return false;
		if (this.label.equalsIgnoreCase("auditor")) return false;
		if (this.label==SparqlLogging.logPersona) return false;
		return true;
	}
	public boolean isSystem()
	{
		if (this.label==UserDB.SystemPersona) return true;
		if (this.label.equalsIgnoreCase("all")) return true;
	//	if (this.label.equalsIgnoreCase("auditor")) return true;
		if (this.label==SparqlLogging.logPersona) return true;
		return false;
	}
	public boolean isLogUser()
	{
		if (this.label==SparqlLogging.logPersona) return true;
		//if (this.label.equalsIgnoreCase("auditor")) return true;
		return false;
	}
	private void addTestServiceFilters()
	{
	  Vector<String> sfilter=new Vector<String>();
	  sfilter.add("http://localhost:3030/unlocked");
	  this.serviceFilters.add(new ServiceFilter(sfilter,sfilter));
	  
	  
	}
	
	public String getUri()
	{
		return this.uri;
	}

}
