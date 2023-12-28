package kadasterfuseki.filter.user;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.jena.query.Query;

public class UserFactory {

	public UserFactory() {
		// TODO Auto-generated constructor stub
	}
	public static User getUser(String query)
	{
		try
		{ 
			
			  String regexPattern ="#persona_.*[\\s;]";
			  Pattern pattern = Pattern.compile(regexPattern);
			  Matcher matcher = pattern.matcher(query.toLowerCase());
			  if (matcher.find())
			  {
				  String usertype =matcher.group();
				  usertype=usertype.replace("#persona_","").trim();
				  usertype=usertype.replace(";","");
				  return createUser(usertype);
			  }
	  
			
		
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return createAnonymous();
	}
	public static User getUser_old(Query query)
	{
		try
		{
			List<String> vars = query.getResultVars();
			for (String v:vars)
			{
				if   (v.toLowerCase().startsWith("persona_")) 
				{
					v = v.toLowerCase().replace("persona_","");
					return createUser(v);
				}
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return createAnonymous();
	}
	private User old(Query query) {
		try
		{
			
			String queryS=query.toString();
			String patternString = "#persona:[^;\\n]+";
		    Pattern pattern = Pattern.compile(patternString);
	        // Create a matcher with the input string
		    Matcher matcher = pattern.matcher(queryS);
		    // Find and print matches
		    while (matcher.find()) 
		    {
		            String match = matcher.group();
		            System.out.println("Found User : " + match);
		    }
		   
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return createAnonymous();
	}
	
	private static User createUser(String type)
	{
		
		if (type==null)
		{
			return createAnonymous();
		}
		if (type.equalsIgnoreCase("admin"))
		{
			return createAdmin();
		}
		if (type.equalsIgnoreCase("test"))
		{
			return createTest();
		}
		
		if (type.equalsIgnoreCase("all"))
		{
			return createAll();
		}
		
		
		
		return createAnonymous();
	}
	private static User createAnonymous()
	{
		User u = new User("anon");
		u.allowedGraphs.add("https://data.labs.kadaster.nl/Unlock/unlock/rkkgpercelen");
		u.allowedGraphs.add("https://data.federatief.datastelsel.nl/lock-unlock/anbi");
		u.performGraphRestrictions=true;
		u.performPredicateRestrictions=false;
		return u;
		
		
	}
	
	private static User createTest()
	{
		User u = new User("test");
		u.performGraphRestrictions=true;
		u.performPredicateRestrictions=false;
		PredicateFilter pf = new PredicateFilter("http://www.w3.org/1999/02/22-rdf-syntax-ns#value");
		
		
		 pf.conditions.add("?subject <http://www.w3.org/1999/02/22-rdf-syntax-ns#value> ?object");

		 
		u.predicateFilters.add(pf);
		return u;
		
		
	}
	
	private static User createAdmin()
	{
		User u = new User("admin");
		u.allowedGraphs.add("https://data.labs.kadaster.nl/lock-unlock/authentication-ontology/graphs/users");
		u.allowedGraphs.add("https://data.labs.kadaster.nl/lock-unlock/authorisation-ontology/graphs/rules");
		u.performPredicateRestrictions=false;
		u.performGraphRestrictions=true;
		return u;
		
		
	}
	private static User createAll()
	{
		User u = new User("all");
		u.performGraphRestrictions=false;
		u.performPredicateRestrictions=false;
		//u.allowedGraphs.add("https://data.labs.kadaster.nl/lock-unlock/authentication-ontology/graphs/users");
		//u.allowedGraphs.add("https://data.labs.kadaster.nl/lock-unlock/authorisation-ontology/graphs/rules");
		
		return u;
		
		
	}

}
