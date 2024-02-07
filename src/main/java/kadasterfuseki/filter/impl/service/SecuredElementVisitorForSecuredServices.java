package kadasterfuseki.filter.impl.service;

import java.util.Vector;

import org.apache.jena.sparql.syntax.Element;
import org.apache.jena.sparql.syntax.ElementAssign;
import org.apache.jena.sparql.syntax.ElementBind;
import org.apache.jena.sparql.syntax.ElementData;
import org.apache.jena.sparql.syntax.ElementDataset;
import org.apache.jena.sparql.syntax.ElementExists;
import org.apache.jena.sparql.syntax.ElementFilter;
import org.apache.jena.sparql.syntax.ElementGroup;
import org.apache.jena.sparql.syntax.ElementLateral;
import org.apache.jena.sparql.syntax.ElementMinus;
import org.apache.jena.sparql.syntax.ElementNamedGraph;
import org.apache.jena.sparql.syntax.ElementNotExists;
import org.apache.jena.sparql.syntax.ElementOptional;
import org.apache.jena.sparql.syntax.ElementPathBlock;
import org.apache.jena.sparql.syntax.ElementService;
import org.apache.jena.sparql.syntax.ElementSubQuery;
import org.apache.jena.sparql.syntax.ElementTriplesBlock;
import org.apache.jena.sparql.syntax.ElementUnion;
import org.apache.jena.sparql.syntax.ElementVisitor;
import org.apache.jena.sparql.syntax.ElementWalker;
import org.apache.jena.sparql.syntax.syntaxtransform.QueryTransformOps;

import kadasterfuseki.filter.user.ServiceFilter;


public class SecuredElementVisitorForSecuredServices implements ElementVisitor  {
	
	
	public boolean runThisQuery=true;
ServiceFilter serviceFilter=null;

	public SecuredElementVisitorForSecuredServices(ServiceFilter sf) {
		// TODO Auto-generated constructor stub
		this.serviceFilter=sf;
	}

	public Vector<String> foundNotAllowedServices =new Vector<String>();
	public Vector<String> foundNotAllowedServicesStartingWith =new Vector<String>();

	
	@Override
	public void visit(ElementService el) {
		// TODO Auto-generated method stub
		if (serviceFilter==null) return;
		try
		{
			//System.out.println("found a service clause!!!!");
			
			String service =el.getServiceNode().toString();
			if (service.startsWith("?")) 
			{
				System.out.println("Not doing this. parameter as service. Could be implemented");
				runThisQuery=false;
				
				
			}
			
			if (this.serviceFilter.notAllowedServices!=null)
			{
				for (String filterS:this.serviceFilter.notAllowedServices)
				{
					if (filterS.equalsIgnoreCase(service))
					{
						//System.out.println("found an illegal service");
						runThisQuery=false;
						foundNotAllowedServices.add(service);
						
					}
				}
			}
			if (this.serviceFilter.notAllowedStartingWith!=null)
			{
			 //  String serviceLC = service.toLowerCase();
				for (String filterS:this.serviceFilter.notAllowedStartingWith)
				{
					if (service.toLowerCase().startsWith(filterS))
					{
						System.out.println("found an illegal starswith service");
						runThisQuery=false;
						foundNotAllowedServicesStartingWith.add(service);
						
					}
				}
			}
				
		}
		catch(Exception e)
		{
			e.printStackTrace();
			runThisQuery=false;
		}
		
		
	}



	@Override
	public void visit(ElementTriplesBlock el) 
	{
		
	   el.visit(this);	
	}



	@Override
	public void visit(ElementPathBlock el) {
		// TODO Auto-generated method stub
		
	//	System.out.println(el.toString());
		
		//System.out.println(el.toString());
		// el.visit(this);	
		
	}



	@Override
	public void visit(ElementFilter el) {
		// TODO Auto-generated method stub
		
		// el.visit(this);	
		
	}



	@Override
	public void visit(ElementAssign el) {
		// TODO Auto-generated method stub
		 el.visit(this);	
		
	}



	@Override
	public void visit(ElementBind el) {
		// TODO Auto-generated method stub
		
		// el.visit(this);	
		
	}



	@Override
	public void visit(ElementData el) {
		// TODO Auto-generated method stub
	//	System.out.println(el.toString());
		//System.out.println(el.toString());
		 //el.visit(this);	
		
	}



	@Override
	public void visit(ElementUnion el) {
		// TODO Auto-generated method stub
		for (Element l:el.getElements()) l.visit(this);
		
	}



	@Override
	public void visit(ElementOptional el) {
		// TODO Auto-generated method stub
		if (el.getOptionalElement()!=null)
		el.getOptionalElement().visit(this);
		
	}



	@Override
	public void visit(ElementLateral el) {
		
		if (el.getLateralElement()!=null)
			el.getLateralElement().visit(this);
	}



	@Override
	public void visit(ElementGroup el) {
		// TODO Auto-generated method stub
		for (Element l:el.getElements()) l.visit(this);
	}



	@Override
	public void visit(ElementDataset el) {
		 if ( el.getElement() != null )
             el.getElement().visit(this);
		
	}



	@Override
	public void visit(ElementNamedGraph el) {
		 if ( el.getElement() != null )
             el.getElement().visit(this);
		
	}



	@Override
	public void visit(ElementExists el) {
		// TODO Auto-generated method stub
		el.visit(this);
	}



	@Override
	public void visit(ElementNotExists el) {
		// TODO Auto-generated method stub
		el.visit(this);
		
	}



	@Override
	public void visit(ElementMinus el) {
		// TODO Auto-generated method stub
		if (el.getMinusElement()!=null) el.visit(this);
		
		
	}



	@Override
	public void visit(ElementSubQuery el) {
		// TODO Auto-generated method stub
		
		el.getQuery().getQueryPattern().visit(this);
		
	}

	

}
