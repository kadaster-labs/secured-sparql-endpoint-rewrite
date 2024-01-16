package kadasterfuseki.filter;

import java.util.List;

import org.apache.jena.query.Query;
import org.apache.jena.sparql.core.DatasetGraph;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.syntax.Element;

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
 User user=null;

 
	public RewriteSparqlEngine(Query query,User user) 
	{
	   this.query=query;
	   this.user=user;
	 
	   if (user.performHorizontalFilters)
	   {
		   processHorizontalFilters();
	   }
	   if (user.performPredicateRestrictions)
	   {
		     processPredicates();
		     
	   }
	   if (user.performGraphRestrictions)
	   {
		   processGraphs();
	   }
	   if (user.serviceFilters.size()>0)
	   {
		   processServiceFilters();
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
		new SecureGraphs(query,user);
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
