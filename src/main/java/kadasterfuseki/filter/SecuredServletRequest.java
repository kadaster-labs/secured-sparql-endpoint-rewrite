package kadasterfuseki.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QueryParseException;

import kadasterfuseki.filter.user.User;
import kadasterfuseki.filter.user.UserFactory;
import kadasterfuseki.logging.SparqlLogging;

public class SecuredServletRequest extends HttpServletRequestWrapper {

	String squery=null;
	
	public SecuredServletRequest(HttpServletRequest request,String query)
	{
		super(request);
		try
		{
				Query q  = QueryFactory.create(query);
				User user =UserFactory.getUser(request);
				if (user!=null)
				{
				
					RewriteSparqlEngine esq = new RewriteSparqlEngine(q, user);
					this.squery=esq.query.toString();
			
				//.getServletPath()
				
					try
					{
						SparqlLogging log=new SparqlLogging(request.getRequestURL().toString());
						log.addQueries(query, esq.query.toString(), user);
						//System.out.println("\n\n rewrite:\n#Persona_All\n"+this.squery+"\n\n");
					}
					catch(Exception e)
					{
						System.err.println("logging error");
						e.printStackTrace();
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
		return super.getParameterValues(name);
	}
	

}
