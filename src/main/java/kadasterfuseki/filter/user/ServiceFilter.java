package kadasterfuseki.filter.user;

import java.util.Vector;

public class ServiceFilter {

	public Vector<String> notAllowedServices = null;
	public Vector<String> notAllowedStartingWith = null; 
	
	public ServiceFilter(Vector<String> notAllowedServices,Vector<String> notAllowedStartingWith) {
		this.notAllowedServices = notAllowedServices;
		this.notAllowedStartingWith = notAllowedStartingWith;
		
	}

}
