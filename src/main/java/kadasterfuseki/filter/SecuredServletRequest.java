package kadasterfuseki.filter;

import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpServletRequestWrapper;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QueryParseException;

import kadasterfuseki.filter.user.User;
import kadasterfuseki.filter.user.UserFactory;
import kadasterfuseki.filter.user.db.TAccess;
import kadasterfuseki.filter.user.db.UserDB;
import kadasterfuseki.logging.SparqlLogging;

public class SecuredServletRequest extends HttpServletRequestWrapper {

	@Override
	public String getQueryString() {
		// TODO Auto-generated method stub
		
		String s =super.getQueryString();
		System.out.println(s);
		return super.getQueryString();
	}

	String squery=null;
	
	public SecuredServletRequest(HttpServletRequest request,String query)
	{
		super(request);
		try
		{
			String endpoint =request.getRequestURL().toString();
				Query q  = QueryFactory.create(query);
				User user =UserFactory.getUser(request);
				
				if (user!=null)
				{
				   if (user.isLogUser()) {this.squery=query; return;}
					RewriteSparqlEngine esq = new RewriteSparqlEngine(request,q, user,endpoint);
					this.squery=esq.query.toString();
					if ((esq.rewritten) && (esq.user.needsLoging()) )
					{
							try
							{
								endpoint=endpoint.split("\\?")[0];
								
								
										TAccess ta =new TAccess(endpoint+"?persona="+SparqlLogging.logPersona);
										if (SparqlLogging.shouldLog(ta,true))
										{
										 SparqlLogging log=new SparqlLogging(request.getRequestURL().toString());
										 log.addQueries(query, esq.query.toString(), esq.user);
										}
								
								
								//System.out.println("\n\n rewrite:\n#Persona_All\n"+this.squery+"\n\n");
							}
							catch(Exception e)
							{
								System.err.println("logging error");
								e.printStackTrace();
							}
					}
			 }
			else
			{
				this.squery=query;
			}
		}
		catch(QueryParseException e)
		{
			this.squery=query; //very dangereous
		}
		catch(Exception e)
		{
			e.printStackTrace();
		
		}
	}

	@Override
	public String getParameter(String name) {
		// TODO Auto-generated method stub
		if (name.equalsIgnoreCase("query"))
		{
			return squery;
		}
		return super.getParameter(name);
	}

	@Override
	public String[] getParameterValues(String name) {
		// TODO Auto-generated method stub
		//System.out.println("get aprameter values "+name);
		if (name.equalsIgnoreCase("query"))
		{
			String[] s=super.getParameterValues(name);
		//	System.out.println(s);
			
		}
		return super.getParameterValues(name);
	}
	

}
