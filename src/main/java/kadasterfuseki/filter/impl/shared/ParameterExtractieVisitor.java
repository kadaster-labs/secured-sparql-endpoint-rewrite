package kadasterfuseki.filter.impl.shared;

import java.util.Hashtable;
import java.util.List;

import org.apache.commons.collections.bag.SynchronizedSortedBag;
import org.apache.jena.graph.Triple;
import org.apache.jena.sparql.core.TriplePath;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.core.VarAlloc;
import org.apache.jena.sparql.function.library.e;
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

public class ParameterExtractieVisitor implements ElementVisitor {

	public ParameterExtractieVisitor() {
		// TODO Auto-generated constructor stub
	}

	public boolean containsBlankNode=false;
	public boolean containsPropertyPath=false;

	@Override
	public void visit(ElementTriplesBlock el) {
		// TODO Auto-generated method stub
		for (Triple tp:el.getPattern().getList())
		{
			
			if (tp.getSubject().isVariable())
			{
				Var subjectVar = Var.alloc(tp.getSubject().toString().replace("?", ""));
				var_var.put(subjectVar, subjectVar);
			}
			if (tp.getSubject().isURI())
			{
				uri_uri.put(tp.getSubject().toString(), tp.getSubject().toString());
			}
			if (tp.getSubject().isBlank())
			{
				// found a blank node.. cannot do tis
				containsBlankNode=true;
			}
		
			if (tp.getObject().isVariable())
			{
				Var subjectVar = Var.alloc(tp.getSubject().toString().replace("?", ""));
				var_var.put(subjectVar, subjectVar);

			}
			if (tp.getObject().isURI())
			{
				uri_uri.put(tp.getObject().toString(), tp.getObject().toString());
			}
		
			if (tp.getObject().isBlank())
			{
				// found a blank node.. cannot do tis
				containsBlankNode=true;
			}
			
			
		}
		
	}

	@Override
	public void visit(ElementPathBlock el) {
		// TODO Auto-generated method stub
		for (TriplePath tp:el.getPattern().getList())
		{
			
			if (tp.getSubject().isVariable())
			{
				if (tp.getSubject().toString().startsWith("??"))
				{
				  //weird.
					containsBlankNode=true;
				}
				else
				{
					Var subjectVar = Var.alloc(tp.getSubject().toString().replace("?", ""));
					var_var.put(subjectVar, subjectVar);
				}

			}
			if (tp.getSubject().isBlank())
			{
				// found a blank node.. cannot do tis
				containsBlankNode=true;
			}
			if (tp.getObject().isVariable())
			{
				if (tp.getObject().toString().startsWith("??"))
				{
				  //weird.
					containsBlankNode=true;
				}
				else
				{
					Var subjectVar = Var.alloc(tp.getObject().toString().replace("?", ""));
					var_var.put(subjectVar, subjectVar);
				}

			}
			
			if (tp.getObject().isBlank())
			{
				// found a blank node.. cannot do tis
				containsBlankNode=true;
			}
			if (tp.getPredicate()==null)
			{
				//must be a propertypath?
				containsPropertyPath=true;
			}
			
			
		}
		
	}

	@Override
	public void visit(ElementFilter el) {
		// TODO Auto-generated method stub
		// nothing
		
	}

	@Override
	public void visit(ElementAssign el) {
		// TODO Auto-generated method stub
	
		 var_var.put(el.getVar(),el.getVar());	
		
	}

	@Override
	public void visit(ElementBind el) 
	{
	   var_var.put(el.getVar(),el.getVar());	
		
	}
	
 public Hashtable<Var,Var> var_var =new Hashtable<Var,Var>();
 public Hashtable<String,String> uri_uri =new Hashtable<String,String>();
 public void addVars(List<Var> vars)
 {
	 for (Var v:vars)
	 {
		 var_var.put(v, v);
	 }
 }
	
	@Override
	public void visit(ElementData el) 
	{
		addVars(	el.getVars());
		
	}

	@Override
	public void visit(ElementUnion el) {
		for (Element el2:el.getElements())
		{
			el2.visit(this);
		}
		
	}

	@Override
	public void visit(ElementOptional el) {
		el.getOptionalElement().visit(this);
		
	}

	@Override
	public void visit(ElementLateral el) {
		// TODO Auto-generated method stub
		el.getLateralElement().visit(this);
		
	}

	@Override
	public void visit(ElementGroup el) {
		for (Element el2:el.getElements())
		{
			el2.visit(this);
		}
		
	}

	@Override
	public void visit(ElementDataset el) {
		el.getElement().visit(this);
		
	}

	@Override
	public void visit(ElementNamedGraph el) {
		el.getElement().visit(this);
		
	}

	@Override
	public void visit(ElementExists el) {
		el.getElement().visit(this);
		
	}

	@Override
	public void visit(ElementNotExists el) {
		el.getElement().visit(this);
		
	}

	@Override
	public void visit(ElementMinus el) {
		el.getMinusElement().visit(this);
		
	}

	@Override
	public void visit(ElementService el) {
		// not necessary?
		
	}

	@Override
	public void visit(ElementSubQuery el) {
		el.getQuery().getQueryPattern().visit(this);
		
	}

}
