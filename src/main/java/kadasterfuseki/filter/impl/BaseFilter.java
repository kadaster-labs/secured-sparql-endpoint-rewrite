package kadasterfuseki.filter.impl;

import java.net.URL;

import java.util.Vector;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;

import kadasterfuseki.filter.impl.service.SecuredElementVisitorForSecuredServices;
import kadasterfuseki.filter.user.ServiceFilter;
import kadasterfuseki.filter.user.User;
import kadasterfuseki.filter.user.UserFactory;
import kadasterfuseki.filter.user.db.UserDB;

public class BaseFilter {

	public Query query=null;
	public User user=null;
	String endpoint = null;
	HttpServletRequest request =null;
	
	public BaseFilter(HttpServletRequest request,Query query,User user,String endpoint)  {
		
		//remove self service
		this.user=user;
		this.query=query;
		this.endpoint = endpoint;
		this.request=request;
		checkSelf();
	}
	
	
	public void checkSelf()
	{
		try
		{
		Vector<String> sfv =new Vector<String>();
		
	  
	     String url=request.getRequestURL().toString();
	     URL url2  = new URL(url);
	     String x  = url2.getProtocol()+"://"+url2.getHost();
		
		sfv.add(x);
		
		String type =null;
		
	
		ServiceFilter sf= new ServiceFilter(null,sfv);
		SecuredElementVisitorForSecuredServices f= new SecuredElementVisitorForSecuredServices(sf);
		query.getQueryPattern().visit(f);
		if (f.runThisQuery==false)
		{
			String queryS = query.toString();
			for (String service:f.foundNotAllowedServicesStartingWith)
			{
				queryS=queryS.replace("SERVICE <"+service+">", "");
				try{type =	service.split("persona=")[1];} catch(Exception e) {}
			}
			query  = QueryFactory.create(queryS);
			this.query=query;
			if (type!=null) 
				{this.user= UserFactory.getPersona2FromDB(request, type);}
		}
		}
		catch(Exception e) {}
	}
	

}
