package kadasterfuseki.filter.impl.service;

import org.apache.jena.query.Query;
import org.apache.jena.sparql.syntax.Element;

import kadasterfuseki.filter.user.ServiceFilter;
import kadasterfuseki.filter.user.User;

public class SecureService {

	public SecureService(Query query,User user)
	{
		
		for (ServiceFilter sf : user.serviceFilters)
		{
		//	sevfs.visit(query.getQueryPattern());
			
			SecuredElementVisitorForSecuredServices sev = new SecuredElementVisitorForSecuredServices(sf);
			Element el=query.getQueryPattern();
			el.visit(sev);
			if (sev.runThisQuery==false)
			{
				System.out.println("should not run this query because of faulty service clause");
				query.setLimit(0);
			}
			
			
		}
		
	}

}
