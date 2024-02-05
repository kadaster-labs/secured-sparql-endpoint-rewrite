package kadasterfuseki.filter.impl.graphs;

import java.util.Vector;

import org.apache.jena.query.Query;
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


public class SecureGraphsVisitor  implements ElementVisitor  {
{
}

@Override
public void visit(ElementTriplesBlock el) {
	// TODO Auto-generated method stub
	el.visit(this);
}

@Override
public void visit(ElementPathBlock el) {
	// TODO Auto-generated method stub
	
	
	
}

@Override
public void visit(ElementFilter el) {
	// TODO Auto-generated method stub
	
}

@Override
public void visit(ElementAssign el) {
	// TODO Auto-generated method stub

}

@Override
public void visit(ElementBind el) {
	// TODO Auto-generated method stub
	
}

@Override
public void visit(ElementData el) {
	// TODO Auto-generated method stub

	
}

@Override
public void visit(ElementUnion el) {
	// TODO Auto-generated method stub
	for (Element sel:el.getElements())
	{
		sel.visit(this);
	}
	
}

@Override
public void visit(ElementOptional el) {
	// TODO Auto-generated method stub
	el.getOptionalElement().visit(this);
	
}

@Override
public void visit(ElementLateral el) {
	// TODO Auto-generated method stub
	el.getLateralElement().visit(this);
	
	
}

@Override
public void visit(ElementGroup el) {
	// TODO Auto-generated method stub
	for (Element el2:el.getElements())
	{
		el2.visit(this);
	}
}

@Override
public void visit(ElementDataset el) {
	// TODO Auto-generated method stub
	
}
public Vector<String> namedGraphs = new Vector<String>();

@Override
public void visit(ElementNamedGraph el) {
	// TODO Auto-generated method stub
	el.getElement().visit(this);
	namedGraphs.add(el.getGraphNameNode().toString());
}

@Override
public void visit(ElementExists el) {
	// TODO Auto-generated method stub
	el.getElement().visit(this);
}

@Override
public void visit(ElementNotExists el) {
	// TODO Auto-generated method stub
	el.getElement().visit(this);
}

@Override
public void visit(ElementMinus el) {
	// TODO Auto-generated method stub
	el.getMinusElement().visit(this);
}

@Override
public void visit(ElementService el) {
	// TODO Auto-generated method stub
	el.getElement().visit(this);
	
	
	
}
public Vector<Query> qs = new Vector<Query>();
@Override
public void visit(ElementSubQuery el) 
{
   Query q =el.getQuery();
   qs.add(q);
    q.getQueryPattern().visit(this);
	
}
}
