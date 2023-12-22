package kadasterfuseki.filter.user;

import java.util.Vector;

public class PredicateFilter {

	public String predicate=null;
	public Vector<String> conditions=new Vector<String>(); 
	
	public static String notexistingPred="http://protected";
	
	public PredicateFilter(String predicate) {
		this.predicate=predicate;
	}
	public boolean hasConditions()
	{
		if (conditions.size()==0 ) return false;
		return true;
	}

}
