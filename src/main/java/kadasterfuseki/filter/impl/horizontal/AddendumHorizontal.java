package kadasterfuseki.filter.impl.horizontal;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.syntax.Element;
import org.apache.jena.sparql.syntax.ElementGroup;
import org.apache.jena.sparql.syntax.ElementVisitorBase;

import kadasterfuseki.filter.impl.shared.ParameterExtractieVisitor;
import kadasterfuseki.filter.user.HorizontalFilter;

public class AddendumHorizontal {

	HorizontalFilter hf = null;
	
	public AddendumHorizontal(HorizontalFilter hf) {
		this.hf=hf;
		this.hfilter=hf.objectValue;
		this.hCls=hf.ofClass;
		this.hpred=hf.hpred;
		this.inGraph=hf.inGraph;
	}
	
	public List<Var> projectVars = null;
	public ParameterExtractieVisitor pev = null;
	
	public void processQuery(Query query)
	{
		if (pev==null) pev =extractVariables(query); 
		projectVars=query.getProjectVars();
		Set<Var> vars = pev.var_var.keySet();
		System.out.println("\n");
		for (Var v:vars)
		{
			System.out.println("property:"+v);
			addHorizontalRestrictions(query, v);
		}
		System.out.println("\n");
		for (String uri:pev.uri_uri.keySet())
		{
			System.out.println("uri:"+uri);
			addHorizontalRestrictions(query, uri);
		}
		
		System.out.println("blank nodes:"+pev.containsBlankNode);
		System.out.println("propertypaths:"+pev.containsPropertyPath);
		System.out.println("\n");
		
		System.out.println("#Persona_All");
		System.out.println(query.toString());
		
		
	}
	
	
	protected static ParameterExtractieVisitor extractVariables(Query query) {
        Set<Var> variables = new HashSet<>();

        // Visit the query's WHERE clause to extract variables
        ParameterExtractieVisitor visitor = new ParameterExtractieVisitor() ;

        Element element = query.getQueryPattern();
        element.visit(visitor);
        
       return visitor;

        
    }
	
	public String  hfilter=null; //<https://brk.basisregistraties.overheid.nl/brk2/id/kadastraleGemeente/25>
	public String hCls=null;
	public String hpred=null;
	public String inGraph=null;
	
	
	public boolean addHorizontalRestrictions(Query query, Var v)
	{
		return addHorizontalRestrictions(query,v.toString());
	}
	static int g=0;
	public boolean addHorizontalRestrictions(Query query, String v)
	{
		
	  
		Element e=query.getQueryPattern();
		//ElementGroup eg  = new ElementGroup();
	//	eg.addElement(query.getQueryPattern());
	
		if (e instanceof ElementGroup)
		{
			g++;
			ElementGroup eg = (ElementGroup) e;
			
			String s="select * where {"
					+ " optional "
					+ "    {";
				
			   if (false)
			   {
					if (inGraph!=null)
					{
						s+=" graph <"+this.inGraph+"> {";
					}
					else
					{
						s+=" graph ?g"+g+" {";			
					}
			   }
					s+= "       "+v.toString()+" a  <"+hCls+">"
					+ "      bind (false as "+v.toString()+"HFRT) \n"
			
					+ "optional{\n"
				
					+ v.toString()+" <"+hpred+"> <"+hfilter+">\n"
				
					+ "bind (true as "+v.toString()+"HFRValidT)\n}}"
				
					 +"filter (coalesce("+v.toString()+"HFRValidT,"+v.toString()+"HFRT,true) )";

					//+ "filter(if(BOUND("+v.toString()+"HFRT),if(BOUND("+v.toString()+"HFRValidT),true,false),false) )";
				
					
				
				
			s+="\n\n}";
		//	s+="\n\n}";  when using graph
			
		s=s.replace("\u200B", " ");
			
			
		//	System.out.println(s);
			Query q  = QueryFactory.create(s);
			
			ElementGroup elg =(ElementGroup) q.getQueryPattern();
			
			for (Element el:elg.getElements())	((ElementGroup) e).addElement(el);
			return true;
		}
		
		System.err.println("could not add horizontal restrictions");
		return false;
	}
	
	
	

}
