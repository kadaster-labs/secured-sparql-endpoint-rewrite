package kadasterfuseki.filter.user.db;

import java.util.Hashtable;
import java.util.UUID;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.jena.fuseki.Fuseki;
import org.apache.jena.fuseki.webapp.FusekiEnv;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.update.UpdateProcessor;

import kadasterfuseki.filter.user.HorizontalFilter;
import kadasterfuseki.filter.user.PredicateFilter;
import kadasterfuseki.filter.user.User;
import kadasterfuseki.logging.SparqlLogging;

public class UserDB {

	 Hashtable<String, User> userdb = new Hashtable<String, User>();
	 
	public UserDB(HttpServletRequest request) 
	{
		try
		{
			System.out.println("setting up userdb");
			String localHost = request.getLocalAddr();
	        int localPort = request.getLocalPort();
	        String path = request.getRequestURI();
	        String met =request.getMethod();
	        String endpoint =met+ localHost+":"+localPort+"/"+path;
	        String repo = path.toLowerCase().replace("/sparql", "").replace("/query", "").replace("/","");
	        endpoint=request.getRequestURL().toString()+"?persona="+UserDB.SystemPersona;
	        create(endpoint);
	        System.out.println("user db ready");
	        SparqlLogging.addLog(request, "loading users (v0.5)");
	        
	        
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	TAccess ta=null;
	Vector<LDResource> graphs = null;
	public void create(String endpoint)
	{
		this.ta=new TAccess(endpoint);
		Vector<String>  uris = LDResource.getAuthUrisOfCls(ta, LDResource.baseAuth+"Graph");
		graphs =new Vector<LDResource>();
		for (String uri:uris)
		{
			LDResource r=new LDResource(ta,uri);
			graphs.add(r);
		}
		createUsers();
		
	}
	private void createUsers()
	{
		Vector<String>  uris = LDResource.getAuthUrisOfCls(ta, LDResource.baseUSER+"User");
		for (String uri:uris)
		{
			LDResource r=new LDResource(ta,uri);
			User user =new User(r.getFirstLabel());
			
			userdb.put(r.getFirstLabel(), user);
			userdb.put(r.getFirstLabel().toLowerCase(), user);
			
             System.out.println("creating user "+r.getFirstLabel());			
		
			Vector<LDResource> roles=r.getRelations(LDResource.baseAuth+"has_role");
			for (LDResource rol:roles)
			{
				Vector<LDResource> groups= rol.getRelations(LDResource.baseAuth+"belongs_to_group");
				for (LDResource group:groups)
				{
					Vector<LDResource> rules = group.getRelations(LDResource.baseAuth+"has_rule");
					for (LDResource rule:rules)
					{
					
						createRule(user,rule);
					}
				}
			}
		}
	}
	
	public void createRule(User user, LDResource rule)
	{
		
		if (rule.isOfType(LDResource.baseAuth+"SimpleHorizontalSubsetUsingClassAndObject")) {createHorizontalRule(user,rule);return;}
		if (rule.isOfType(LDResource.baseAuth+"InAccessiblePredicateValue")) {createInAccessiblePredicateValue(user,rule);return;}
		if (rule.isOfType(LDResource.baseAuth+"AccessibleDataset")) {createAccessibleDataset(user,rule);return;}
		
		
		System.err.println("found UNIMPLEMENTED rule "+rule.getFirstLabel()+" for "+user.label);
		
	}
	
	public void createAccessibleDataset(User user, LDResource rule)
	{
		// should also check graph... now now
		user.performGraphRestrictions=true;
		Vector<LDResource> datasets = rule.getRelations(LDResource.baseAuth+"dataset");
		for (LDResource dataset:datasets)
		{
		    for (LDResource graph:graphs)
		    {
		            if (graph.contains(LDResource.baseAuth+"part_of_dataset",dataset.uri))
		            		{
		            				 
		            	        user.allowedGraphs.add(graph.getFirstAsString(LDResource.baseAuth+"graph"));
		            	        
		            		}
		    }
			
		}
	}
	
	public void createHorizontalRule(User user, LDResource rule)
	{
		user.performHorizontalFilters=true;
		
		String ofClass=rule.getFirstAsString(LDResource.baseAuth+"ofClass");
		String hpred =rule.getFirstAsString(LDResource.baseAuth+"predicate");
		String objectValue=rule.getFirstAsString(LDResource.baseAuth+"objectValueShouldBe");
		
		HorizontalFilter hf = new HorizontalFilter(ofClass, hpred, objectValue);
		user.horizontalFilters.add(hf);
	}
	public void createInAccessiblePredicateValue(User user, LDResource rule)
	{
		// should also check graph... now now
		user.performPredicateRestrictions=true;
		String hpred =rule.getFirstAsString(LDResource.baseAuth+"predicate");
		PredicateFilter pf =new PredicateFilter(hpred);
		user.predicateFilters.add(pf);
	}
	
	
	
	
	public User getUser(String type)
	{
		User u=userdb.get(type);
		if (u!=null) return u;
		
		try {
			 u=userdb.get(type.toLowerCase());
			return u;
		}
		catch(Exception e) {}
		return null;
		 
	}

	public static String SystemPersona="System";
	static {
		if (false)
		{
			SystemPersona=UUID.randomUUID().toString();
		}
	}
	
	public static void main(String[] args) 
	{
		  UserDB udb= new UserDB(null);
		  udb.create("http://localhost:3030/unlocked?persona="+SystemPersona);
		  
	}

}
