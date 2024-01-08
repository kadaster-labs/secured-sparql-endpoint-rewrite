package kadasterfuseki.filter.impl.predicates;

import java.util.List;
import java.util.Vector;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Node_URI;
import org.apache.jena.sparql.core.PathBlock;
import org.apache.jena.sparql.core.TriplePath;
import org.apache.jena.sparql.expr.Expr;
import org.apache.jena.sparql.path.P_Link;
import org.apache.jena.sparql.path.Path;
import org.apache.jena.sparql.path.PathFactory;
import org.apache.jena.sparql.path.PathParser;
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
import org.apache.jena.sparql.util.ExprUtils;
import org.apache.jena.sparql.util.PrefixMapping2;

import kadasterfuseki.filter.user.PredicateFilter;

public class SecuredElementVisitor implements ElementVisitor {

	Element element;
	PredicateFilter pf;
	
	public boolean doNotRunNoImplementation=false;
	public boolean changed =false;
	SecuredElementVisitor previous=null;
	
	
	public SecuredElementVisitor(SecuredElementVisitor previous, Element el, PredicateFilter pf) {
		// TODO Auto-generated constructor stub
		this.element=el;
		this.pf=pf;
		this.previous=previous;
	}
	//post process
	Vector<ElementFilter> add = new Vector<ElementFilter>();
	Vector<Element> remove = new Vector<Element>();
	public void changeElement()
	{
		changeElement(this);
	}
	public void doNotRun()
	{
		doNotRunNoImplementation=true;
		SecuredElementVisitor sev=this;
		while(sev.previous!=null)
		{
			sev=sev.previous;
		}
		sev.doNotRunNoImplementation=true;
		
	}
	public void changeElement(SecuredElementVisitor sev)
	{
		if (sev.element instanceof ElementGroup)
		{
			ElementGroup eg = (ElementGroup) sev.element;
			for (ElementFilter ef:add)  
			{
				eg.addElement(ef);
			}
			return;
		}
		if (sev.element instanceof ElementUnion)
		{
			ElementUnion eg = (ElementUnion) sev.element;
			for (ElementFilter ef:add)  
			{
				eg.addElement(ef);
			}
			return;
		}
		if (sev.element instanceof ElementPathBlock)
		{
			if (sev.previous!=null)
			{
				changeElement(sev.previous);
				return;
			}
		}
		
		System.err.println("no implementation to add filter "+this.element);
		doNotRun();
	}
	
	// end post process

	@Override
	public void visit(ElementTriplesBlock arg0) {
		// TODO Auto-generated method stub
		System.out.println("Element triples block not implemented");
		
		doNotRun();
	
	}
	
	
	private TriplePath processTriplePath(TriplePath t)
	{
		System.out.println("process triple path");
		Node pred =t.getPredicate();
		TriplePath rt =t;
		
		boolean processed=false;
		if (pred!=null)
		{
		
			if (pred.isVariable())
			{
				System.out.println("variable.. so we need to a add a filter "+pred.getName());
				if (pf.hasConditions())
				{
					//if pred == secured pred then make sure it fulfils the conditions
					System.out.println("pred variable found we need to make sure it is not a secured pred except when it meets the available conditions. This is not yet implemented");
					doNotRun();
					return rt;
				}
				else
				{
				      // variable can not be of secured predicate so add ing extra filter
					Expr expr =new ExprUtils().parse("?"+pred.getName()+"!=<"+this.pf.predicate+">");
					ElementFilter ef = new ElementFilter(expr);
					add.add(ef);
					processed=true;
					return rt;
				}
				
			}
			
			if (pred.isURI())
			{
				 if (pred.toString().equalsIgnoreCase(pf.predicate))
				 {
					 		P_Link newPredicate = new P_Link(NodeFactory.createURI(PredicateFilter.notexistingPred));
					 		rt = new TriplePath(t.getSubject(),newPredicate,t.getObject());
					 		processed=true;
				 }
				 return rt;
				
			}
			System.out.println("weird. what is pred then?");
			doNotRun();
			return rt;
			
		}
			if (t.getPath()!=null)
			{
		      if (!pf.hasConditions())
		      {
		    	  ContainsPathVisitor cp = new ContainsPathVisitor(this.pf,true);
					t.getPath().visit(cp);
					if  ( (cp.contains))  
					{
						String s=t.getPath().toString();
						String old=s;
						while (true)
						{
						   s=old.replace(this.pf.predicate, PredicateFilter.notexistingPred);
						   if (old.equalsIgnoreCase(s)) break;
						   old=s;
						}
						
					  ;	
					  
						rt = new TriplePath(t.getSubject(),PathParser.parse(s,PrefixMapping2.Standard ),t.getObject());
				         return rt;		
					 	
				    }
					  System.out.println("path contains illegal predicate");
					   doNotRun();
					
		      }
		      else
		      {
				System.out.println("property path with conditions.. not yet implemented");
				doNotRun();
				if (false)
				{
					
				}
		      }
				processed=true;
			}
		
	
	
    return rt;
	}

	@Override
	public void visit(ElementPathBlock arg0) {
		// TODO Auto-generated method stub
		System.out.println("visit Element path block");
		
		PathBlock pb =arg0.getPattern();
		Vector<TriplePath> remove =new Vector<TriplePath>(); 
		Vector<TriplePath> add =new Vector<TriplePath>(); 
	
		for (TriplePath t:pb.getList())
		{
			remove.add(t);
		   add.add(processTriplePath(t));	
		}
		for (TriplePath t:remove) pb.getList().remove(t);
		for (TriplePath t:add) pb.getList().add(t);
		
	
	}

	@Override
	public void visit(ElementFilter arg0) {
		// TODO Auto-generated method stub
		System.out.println("interesting.. .a filter. not checking "+arg0);
	}

	@Override
	public void visit(ElementAssign arg0) {
		// TODO Auto-generated method stub
		System.out.println("element assign "+arg0);
		doNotRun();
	
	}

	@Override
	public void visit(ElementBind arg0) {
		// TODO Auto-generated method stub
		
		System.out.println("bind. not necessary to process? "+arg0);
	}

	@Override
	public void visit(ElementData arg0) {
		// TODO Auto-generated method stub
		System.out.println("element data not implemented "+arg0);
		doNotRun();
	
	}

	@Override
	public void visit(ElementUnion arg0) {
		// TODO Auto-generated method stub
		System.out.println("visit Element union");
		visitElements(arg0.getElements());
	
	
	}

	@Override
	public void visit(ElementOptional arg0) {
		// TODO Auto-generated method stub
		System.out.println("vist Element optional");
		Vector<Element> els= new Vector<Element>();
		els.add(arg0.getOptionalElement());
		visitElements(els);
		
	
		
	}

	@Override
	public void visit(ElementLateral arg0) {
		// TODO Auto-generated method stub
		System.out.println("Element Lateral not implemented "+arg0);
		doNotRun();
		
	}

	private void visitElements(List<Element> elements)
	{
		Vector<SecuredElementVisitor> sevs =new Vector <SecuredElementVisitor>();
		for (Element e:elements)
		{
			SecuredElementVisitor sev =new SecuredElementVisitor(this,e,pf);
			sevs.add(sev);
			e.visit(sev);
		}
		
		for (SecuredElementVisitor sev:sevs)sev.changeElement();
	}
	@Override
	public void visit(ElementGroup arg0) {
		// TODO Auto-generated method stub
		System.out.println("visit elemengroup");
		visitElements(arg0.getElements());
		
	
	
	}

	@Override
	public void visit(ElementDataset arg0) {
		// TODO Auto-generated method stub
		System.out.println("Element Dataset try out "+arg0);
		Vector<Element> els= new Vector<Element>();
		els.add(arg0.getElement());
		visitElements(els);
		
	
	}

	@Override
	public void visit(ElementNamedGraph arg0) {
		// TODO Auto-generated method stub
		System.out.println("Named graph tryout");
		
		Vector<Element> els= new Vector<Element>();
		els.add(arg0.getElement());
		visitElements(els);
	
	}

	@Override
	public void visit(ElementExists arg0) {
		// TODO Auto-generated method stub
		System.out.println("Element exists tryout");
		Vector<Element> els= new Vector<Element>();
		els.add(arg0.getElement());
		visitElements(els);
	
	}

	@Override
	public void visit(ElementNotExists arg0) {
		// TODO Auto-generated method stub
		System.out.println("Element not exists tryout");
		Vector<Element> els= new Vector<Element>();
		els.add(arg0.getElement());
		visitElements(els);
	
	}

	@Override
	public void visit(ElementMinus arg0) {
		// TODO Auto-generated method stub
		System.out.println("Element minus tryout");
		Vector<Element> els= new Vector<Element>();
		els.add(arg0.getMinusElement());
		visitElements(els);
		
		
	
	}

	@Override
	public void visit(ElementService arg0) {
		// TODO Auto-generated method stub
		System.out.println("Element service not need to visit this part ");
		
		
	}

	@Override
	public void visit(ElementSubQuery arg0) {
		// TODO Auto-generated method stub
		System.out.println("Element subquery tryout "+arg0);
		Vector<Element> els= new Vector<Element>();
		els.add(arg0.getQuery().getQueryPattern());
		visitElements(els);
		
		
	
		//arg0.getQuery().getQueryPattern().visit(this);;
	}

}
