package kadasterfuseki.filter;

import java.util.Iterator;



import org.apache.jena.graph.Node;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.sparql.algebra.Algebra;
import org.apache.jena.sparql.algebra.Op;
import org.apache.jena.sparql.algebra.OpAsQuery;
import org.apache.jena.sparql.algebra.optimize.Optimize;
import org.apache.jena.sparql.core.DatasetGraph;
import org.apache.jena.sparql.core.DatasetGraphMapLink;
import org.apache.jena.sparql.core.DatasetGraphOne;
import org.apache.jena.sparql.core.Quad;
import org.apache.jena.sparql.engine.Plan;
import org.apache.jena.sparql.engine.QueryEngineFactory;

import org.apache.jena.sparql.engine.QueryEngineFactoryWrapper;
import org.apache.jena.sparql.engine.binding.Binding;
import org.apache.jena.sparql.util.Context;
import org.apache.jena.sparql.util.Symbol;
import org.apache.jena.tdb.transaction.DatasetGraphTransaction;

import kadasterfuseki.filter.user.User;
import kadasterfuseki.filter.user.UserFactory;

public class SparqlFilter 
implements QueryEngineFactory
{

	public SparqlFilter() {
		// TODO Auto-generated constructor stub
		
	}

	@Override
	public boolean accept(Query arg0, DatasetGraph arg1, Context arg2) {
		// TODO Auto-generated method stub
		// fuseki config
		if (arg1 instanceof DatasetGraphOne)
			{
			return false; // fuseki config queries
			}
		if (arg1 instanceof DatasetGraphMapLink)
		{
			return false; // fuseki config queries
		}
		if (arg1 instanceof DatasetGraphTransaction)
		{
			return false; // fuseki config queries
		}
		
		 if (true)   // secure it
		   {
			 if ((arg0.getQueryType()==Query.QueryTypeSelect) || (arg0.getQueryType()==Query.QueryTypeConstruct))
			 {
			//	System.out.println("rewrite query ");
			 User user =UserFactory.getUser(arg0);
			 new EnrichSparqlQuery(arg0,user);
			 System.out.println(arg0);
			 }
			 else
			 {
				 return true;// gives error so not processing
			 }
			 
	
		   }
		
	
		
		
		
		return false; //weird
	}



	@Override
	public Plan create(Query arg0, DatasetGraph arg1, Binding arg2, Context arg3)
	{
		// TODO Auto-generated method stubarg\\
		
		System.out.println("create op plan "+arg2);
		//arg0 = QueryFactory.create("select distinct ?a ?b ?c where {?a ?b ?c} limit 10");
		Op op = Algebra.compile(arg0);
		Op optimizedOp = Optimize.optimize(op,arg3);
		Plan plan  =QueryEngineFactoryWrapper.get().create(optimizedOp, arg1, arg2, arg3);
	//	System.out.println(plan);
		return plan;
	}

	@Override
	public boolean accept(Op arg0, DatasetGraph arg1, Context arg2) {
		// TODO Auto-generated method stub
		System.out.println("accept op 222112121 "+arg0);
		
		return true;
	}

	@Override
	public Plan create(Op arg0, DatasetGraph arg1, Binding arg2, Context arg3) {
		// TODO Auto-generated method stub
		System.out.println("create plan ");
		return  QueryEngineFactoryWrapper.get().create(arg0, arg1, arg2, arg3);
	}
}
	
	
	
	
