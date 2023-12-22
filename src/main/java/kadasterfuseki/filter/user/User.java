package kadasterfuseki.filter.user;

import java.util.Vector;

public class User {

	public Vector<String> allowedGraphs = new Vector<String>();
	public Vector<PredicateFilter> predicateFilters = new Vector<PredicateFilter>();
	
	public String label=null;
	public boolean performGraphRestrictions=true;
	public boolean performPredicateRestrictions=true;
	
	public User(String label)
	{
	  this.label=label;
	  System.out.println("creating user "+label);
	}

}
