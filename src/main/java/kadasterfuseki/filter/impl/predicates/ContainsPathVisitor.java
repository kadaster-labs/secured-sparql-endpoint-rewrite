package kadasterfuseki.filter.impl.predicates;

import org.apache.jena.graph.BlankNodeId;
import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeVisitor;
import org.apache.jena.graph.Node_ANY;
import org.apache.jena.graph.Node_Blank;
import org.apache.jena.graph.Node_Graph;
import org.apache.jena.graph.Node_Literal;
import org.apache.jena.graph.Node_Triple;
import org.apache.jena.graph.Node_URI;
import org.apache.jena.graph.Node_Variable;
import org.apache.jena.graph.Triple;
import org.apache.jena.graph.impl.LiteralLabel;
import org.apache.jena.sparql.path.P_Alt;
import org.apache.jena.sparql.path.P_Distinct;
import org.apache.jena.sparql.path.P_FixedLength;
import org.apache.jena.sparql.path.P_Inverse;
import org.apache.jena.sparql.path.P_Link;
import org.apache.jena.sparql.path.P_Mod;
import org.apache.jena.sparql.path.P_Multi;
import org.apache.jena.sparql.path.P_NegPropSet;
import org.apache.jena.sparql.path.P_OneOrMore1;
import org.apache.jena.sparql.path.P_OneOrMoreN;
import org.apache.jena.sparql.path.P_ReverseLink;
import org.apache.jena.sparql.path.P_Seq;
import org.apache.jena.sparql.path.P_Shortest;
import org.apache.jena.sparql.path.P_ZeroOrMore1;
import org.apache.jena.sparql.path.P_ZeroOrMoreN;
import org.apache.jena.sparql.path.P_ZeroOrOne;
import org.apache.jena.sparql.path.Path;
import org.apache.jena.sparql.path.PathVisitor;

import kadasterfuseki.filter.user.PredicateFilter;

public class ContainsPathVisitor implements PathVisitor {

	PredicateFilter pf=null;
	public boolean contains=false;
	public boolean isSimple=false;
	
	public ContainsPathVisitor(PredicateFilter pf)
	{
	 this.pf=pf;
	}

	public  boolean containsPred(String s)
	{
		if (s.toString().contains(this.pf.predicate))
			{
			contains=true;
			return true;
			}
		return false;
		
	}
	
	public  boolean changeIfNecessary(Path p)
	{
		if (p.toString().contains(this.pf.predicate))
		{
			
				contains=true;
				return true;
		}
		return false;
	}
	
	
	
	@Override
	public void visit(P_Link arg0) {
		// TODO Auto-generated method stub
		System.out.println("P LINK "+arg0);
		if (changeIfNecessary(arg0))
			{
			   
			    
			}
		
	}

	@Override
	public void visit(P_ReverseLink arg0) {
		// TODO Auto-generated method stub
		if (changeIfNecessary(arg0))
		{
			
		}
		
	}

	@Override
	public void visit(P_NegPropSet arg0) {
		// TODO Auto-generated method stub
		System.out.println("P_NegPropSet"+arg0);
		containsPred(arg0.toString());
		
	}

	@Override
	public void visit(P_Inverse arg0) {
		// TODO Auto-generated method stub
		System.out.println("P_Inverse"+arg0);
		containsPred(arg0.toString());
	}

	@Override
	public void visit(P_Mod arg0) {
		// TODO Auto-generated method stub
		System.out.println("P_Mod"+arg0);
		arg0.getSubPath().visit(this);
		
		
	}

	@Override
	public void visit(P_FixedLength arg0) {
		// TODO Auto-generated method stub
		System.out.println("P_FixedLength"+arg0);
		arg0.getSubPath().visit(this);
		
	}

	@Override
	public void visit(P_Distinct arg0) {
		// TODO Auto-generated method stub
		System.out.println("P_distinct"+arg0);
		arg0.getSubPath().visit(this);
		
	}

	@Override
	public void visit(P_Multi arg0) {
		// TODO Auto-generated method stub
		System.out.println("P_Multi"+arg0);
		arg0.getSubPath().visit(this);
		
	     
		
		//containsPred(arg0.toString());
	}

	@Override
	public void visit(P_Shortest arg0) {
		// TODO Auto-generated method stub
		System.out.println("P_Shortest"+arg0);
		arg0.getSubPath().visit(this);
		
		
	}

	@Override
	public void visit(P_ZeroOrOne arg0) {
		// TODO Auto-generated method stub
		System.out.println("P_ZeroOrOne"+arg0);
		arg0.getSubPath().visit(this);
		
		
	}

	@Override
	public void visit(P_ZeroOrMore1 arg0) {
		// TODO Auto-generated method stub
		System.out.println("P_ZeroOrMore1"+arg0);
		arg0.getSubPath().visit(this);
		
	}

	@Override
	public void visit(P_ZeroOrMoreN arg0) {
		// TODO Auto-generated method stub
		System.out.println("P_ZeroOrMoreN"+arg0);
		arg0.getSubPath().visit(this);
		
	}

	@Override
	public void visit(P_OneOrMore1 arg0) {
		// TODO Auto-generated method stub
		System.out.println("P_OneOrMore1"+arg0);
		arg0.getSubPath().visit(this);
		
	}

	@Override
	public void visit(P_OneOrMoreN arg0) {
		// TODO Auto-generated method stub
		System.out.println("P_OneOrMoreN"+arg0);
		arg0.getSubPath().visit(this);
		
	}

	@Override
	public void visit(P_Alt arg0) {
		// TODO Auto-generated method stub
		System.out.println("P_alt"+arg0);
		containsPred(arg0.toString());
		
	}

	@Override
	public void visit(P_Seq arg0) {
		// TODO Auto-generated method stub
		System.out.println("P_Seq"+arg0);
		arg0.getLeft().visit(this);
		arg0.getRight().visit(this);
		
		
		
		//arg0.visit(this);
	}

}
