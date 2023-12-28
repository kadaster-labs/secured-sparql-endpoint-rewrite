package nl.kadaster.labs.lock_unlock.fuseki;

import org.apache.jena.fuseki.servlets.ActionService;
import org.apache.jena.fuseki.servlets.HttpAction;

public class AuthorizationFilter extends ActionService {

    @Override
    public void validate(HttpAction arg0) {
        // TODO Auto-generated method stub
    	System.out.println("authorization filter "+arg0);
        throw new UnsupportedOperationException("Unimplemented method 'validate'");
    }

    @Override
    public void execute(HttpAction arg0) {
        // TODO Auto-generated method stub
    	System.out.println("authorization filter exe "+arg0);
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }    

}
