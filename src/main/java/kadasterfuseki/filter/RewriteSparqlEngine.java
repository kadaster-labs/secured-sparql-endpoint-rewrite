package kadasterfuseki.filter;

import java.util.List;


import jakarta.servlet.http.HttpServletRequest;

import org.apache.jena.query.Query;
import org.apache.jena.sparql.core.DatasetGraph;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.syntax.Element;

import kadasterfuseki.filter.impl.BaseFilter;
import kadasterfuseki.filter.impl.graphs.SecureGraphs;
import kadasterfuseki.filter.impl.horizontal.AddendumHorizontal;
import kadasterfuseki.filter.impl.predicates.SecuredElementVisitor;
import kadasterfuseki.filter.impl.service.SecureService;
import kadasterfuseki.filter.impl.shared.ParameterExtractieVisitor;
import kadasterfuseki.filter.user.HorizontalFilter;
import kadasterfuseki.filter.user.PredicateFilter;
import kadasterfuseki.filter.user.User;

public class RewriteSparqlEngine 
{
 public Query query = null;
 public User user=null;
 String endpoint = null;
 HttpServletRequest request=null;

 public boolean rewritten=false;
 
	public RewriteSparqlEngine(HttpServletRequest request,Query query,User user, String endpoint) 
	{
	   this.query=query;
	   String orgQuery=this.query.toString();
	   this.user=user;
	   this.endpoint=endpoint;
	   this.request=request;
	   
	   
		BaseFilter bf = new BaseFilter(request,query, user, endpoint);
		this.user=bf.user;
		this.query=bf.query;
		query=bf.query;
		user=this.user;
		if (user.isSystem()) return;
		
	
		 if (user.performGraphRestrictions)
		   {
			   processGraphs();
		   }
		 
	   if (user.performHorizontalFilters)
	   {
		   processHorizontalFilters();
	   }
	   if (user.performPredicateRestrictions)
	   {
		     processPredicates();
		     
	   }
	  
	   
	   String nQuery=this.query.toString();
	   if (nQuery.equalsIgnoreCase(orgQuery)) 
	   {
		   System.out.println("\n\nquery not rewritten\n\n");
	   }
	   else
	   {
		   rewritten=true;
		   System.out.println("\n\nrewritten query:\n\n "+nQuery+"\n\n");
	   }
		   
	   
	   
	   if (false)
	   {
		   if (user.serviceFilters.size()>0)
		   {
			   processServiceFilters();
		   }
	   }
	  
	}
	
	private void processHorizontalFilters()
	{
		List<Var> projectVars = null;
		
		ParameterExtractieVisitor pev=null;
		
		  for (HorizontalFilter pf:user.horizontalFilters)
		   {
			  AddendumHorizontal ah= new AddendumHorizontal(pf);
			  ah.pev=pev;
			  ah.processQuery(query);
			  
			  if (pev==null) pev=ah.pev;
			  if (projectVars==null) projectVars=ah.projectVars;
		   }
	}
	
	private void processServiceFilters()
	{
		new SecureService(query,user);
	}
	
	private void processGraphs()
	{
		this.query= new SecureGraphs(this.request  ,query,user,endpoint).query;
	}
	
	private void processPredicates()
	{
		   for (PredicateFilter pf:user.predicateFilters)
		   {
			   processPredicateFilter(pf);
		   }
			 
		
	}
	
	private void processPredicateFilter(PredicateFilter pf)
	{
		Element el=query.getQueryPattern();
		SecuredElementVisitor sev = new SecuredElementVisitor(null,el,pf);
		el.visit(sev);
		sev.changeElement();
		if (sev.doNotRunNoImplementation)
		{
			System.out.println("NOT RUNNING query");
			query.setLimit(0);
		}
		
	
		
	}
	
	

}
