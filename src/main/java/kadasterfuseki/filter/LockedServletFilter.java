package kadasterfuseki.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.jena.fuseki.Fuseki;
import org.apache.jena.fuseki.servlets.FusekiFilter;

public class LockedServletFilter extends FusekiFilter {

	public LockedServletFilter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(FilterConfig filterConfig) {
		// TODO Auto-generated method stub
		super.init(filterConfig);
		 Fuseki.serverLog.info("Add Kadaster Lock-Unlock Authorization filter version 0.2");
		//System.out.println("init Kadaster locked-unlocked filter version 0.2");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		if (request instanceof HttpServletRequest )
		{
			HttpServletRequest r2 = (HttpServletRequest) request;
			
			
			String url=r2.getRequestURI();
			
			if (url.endsWith("ping")) return;
			//if (true) {super.doFilter(request, response, chain); System.out.println("skipping");return;}
			
			
				String query =r2.getParameter("query");
				if (query!=null)
				{
					System.out.println("\ndo filter");
					SecuredServletRequest ssr=new SecuredServletRequest(r2,query);
					chain.doFilter(ssr, response);
					return;
				}
				
				//r2.setAttribute("query", );
				
				
			
		}
		super.doFilter(request, response, chain);
		
		
		
	}

}
