package kadasterfuseki.filter.user;

import java.util.Hashtable;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.jena.fuseki.Fuseki;
import org.apache.jena.query.Query;

import kadasterfuseki.filter.user.db.UserDB;
import kadasterfuseki.logging.SparqlLogging;

public class UserFactory {

	public UserFactory() {
		// TODO Auto-generated constructor stub
	}

	
	private static Hashtable<String, UserDB> endpoint_userdb = new Hashtable<String, UserDB>();
	
 	
	
	public static User getUser(HttpServletRequest request)
	{
		
		String servletPath=request.getServletPath();
		String repo  =servletPath.split("/")[1];
		
		
		String personaV2=(String) request.getParameter("persona");
		if (personaV2!=null)
		{
			if (personaV2.equalsIgnoreCase(UserDB.SystemPersona))
			{
				//System.out.println("found system persona");
				return createAll();
			}
			if (personaV2.equalsIgnoreCase(SparqlLogging.logPersona))
			{
				User u=createAll();
				u.label=SparqlLogging.logPersona;
				return u;
			}
			// new
			 return getPersona2FromDB(request,personaV2);
		}
		else
		{
		//	System.out.println("found anonymous user");
			 return getPersona2FromDB(request,"anonymous");
		}
				
		
	
	}
	
	public static User getUser_old(HttpServletRequest request,String query)
	{
		
		String servletPath=request.getServletPath();
		String repo  =servletPath.split("/")[1];
		
		
		if (false)
		/// ah well..
		{
			if  ( (!repo.equalsIgnoreCase("unlocked")) && (!repo.equalsIgnoreCase("unlockedInMem")) )
			{
				return null;
			}
		}
		String personaV2=(String) request.getParameter("persona");
		if (personaV2!=null)
		{
			if (personaV2.equalsIgnoreCase(UserDB.SystemPersona))
			{
				System.out.println("found system persona");
				return createAll();
			}
			// new
			 return getPersona2FromDB(request,personaV2);
		}
		
		if (false)
		{
			// old
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
					//  return createUser(usertype);
				  }
				
				  
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		return createAnonymous();
	}
	
	private User old2(Query query) {
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
	
	public static String getRepo(HttpServletRequest request)
	
	{
		
		String localHost = request.getLocalAddr();
        int localPort = request.getLocalPort();
        String path = request.getRequestURI();
        String endpoint = localHost+":"+localPort+path;
	      String repo = path.toLowerCase().replace("/sparql", "").replace("/query", "").replace("/","");
	      return repo;
	}
	
	public static User getPersona2FromDB(HttpServletRequest request,String type)
	{
		
		if (type.equalsIgnoreCase(UserDB.SystemPersona))
		{
		//	System.out.println("found system persona");
			return createAll();
		}
		
        String repo = getRepo(request);
        UserDB userDB=endpoint_userdb.get(repo);
        if (userDB==null)
        {
        	userDB=new UserDB(request);
        	endpoint_userdb.put(repo, userDB);
        }
    	User user =userDB.getUser(type);
		if (user!=null) return user;
		System.err.println("unknown user "+type);
		return createAnonymousFromDB(request);
	}
	
	
	private static User createAnonymousFromDB(HttpServletRequest request)
	{
     return getPersona2FromDB(request, "anonymous");
	}
	
	
	
	
	
	
	private static User createGemeente2(String gemeente)
	{
		System.out.println("gemeente user "+gemeente);
		
		User u = new User("gemeente_"+gemeente);
		u.performGraphRestrictions=false;
		u.performPredicateRestrictions=false;
		u.performHorizontalFilters=true;
		int gcode=0;
		if (gemeente.equalsIgnoreCase("zeewolde")) gcode=1156;
		if (gemeente.equalsIgnoreCase("almere")) gcode=25;
		
	//	u.horizontalFilters.add(new HorizontalFilter("https://data.federatief.datastelsel.nl/lock-unlock/brp/def/NatuurlijkPersoon", "https://data.federatief.datastelsel.nl/lock-unlock/brp/def/heeftVerblijfsplaats","https://brk.basisregistraties.overheid.nl/brk2/id/kadastraleGemeente/"+gcode));
	//	u.horizontalFilters.add(new HorizontalFilter("http://modellen.geostandaarden.nl/def/imx-geo#Perceel", "https://brk.basisregistraties.overheid.nl/brk2/def/kadastraleGemeente","https://brk.basisregistraties.overheid.nl/brk2/id/kadastraleGemeente/"+gcode));
		
		return u;
	}
	
		
	private static User createAnonymous()
	{
		User u = new User("anon");
		u.addAllowedGraph("https://data.labs.kadaster.nl/Unlock/unlock/rkkgpercelen");
		u.addAllowedGraph("https://data.federatief.datastelsel.nl/lock-unlock/anbi");
		u.performGraphRestrictions=true;
		u.performPredicateRestrictions=false;
		return u;
		
		
	}
	private static User createKoopsomUser()
	{
		User u = new User("koopsom");
		u.performGraphRestrictions=false;
		u.performPredicateRestrictions=true;
		PredicateFilter pf = new PredicateFilter("http://www.w3.org/1999/02/22-rdf-syntax-ns#value");
		u.predicateFilters.add(pf);
		return u;
	}
	
	private static User createTest()
	{
		User u = new User("test");
		u.performGraphRestrictions=false;
		u.performPredicateRestrictions=true;
		PredicateFilter pf = new PredicateFilter("http://www.w3.org/1999/02/22-rdf-syntax-ns#value");
		
		
		// pf.conditions.add("?subject <http://www.w3.org/1999/02/22-rdf-syntax-ns#value> ?object");

		 
		u.predicateFilters.add(pf);
		return u;
		
		
	}
	
	private static User createAdmin()
	{
		User u = new User("admin");
		u.addAllowedGraph("https://data.labs.kadaster.nl/lock-unlock/authentication-ontology/graphs/users");
		u.addAllowedGraph("https://data.labs.kadaster.nl/lock-unlock/authorisation-ontology/graphs/rules");
		u.performPredicateRestrictions=false;
		u.performGraphRestrictions=true;
		return u;
		
		
	}
	private static User createLogUser()
	{
		User u = new User("logUser");
		u.performGraphRestrictions=true;
		u.performPredicateRestrictions=false;
		u.addAllowedGraph("http://labs.kadaster.nl/logging");
		return u;
		
	}
	
	private static User createAll()
	{
		User u = new User("all");
		u.performGraphRestrictions=false;
		u.performPredicateRestrictions=false;
		u.performHorizontalFilters=false;
		//u.allowedGraphs.add("https://data.labs.kadaster.nl/lock-unlock/authentication-ontology/graphs/users");
		//u.allowedGraphs.add("https://data.labs.kadaster.nl/lock-unlock/authorisation-ontology/graphs/rules");
		
		return u;
		
		
	}

}
