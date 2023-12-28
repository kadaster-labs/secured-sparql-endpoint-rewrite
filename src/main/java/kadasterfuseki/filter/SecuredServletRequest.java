package kadasterfuseki.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;

import kadasterfuseki.filter.user.User;
import kadasterfuseki.filter.user.UserFactory;

public class SecuredServletRequest extends HttpServletRequestWrapper {

	String squery=null;
	public SecuredServletRequest(HttpServletRequest request,String query)
	{
		super(request);
		try
		{
			Query q  = QueryFactory.create(query);
		
			User user =UserFactory.getUser(query);
			EnrichSparqlQuery esq = new EnrichSparqlQuery(q, user);
			
			this.squery=esq.query.toString();
		//	System.out.println(this.squery);
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
		System.out.println("get aprameter values "+name);
		return super.getParameterValues(name);
	}
	

}
