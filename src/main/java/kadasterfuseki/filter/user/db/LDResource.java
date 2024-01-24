package kadasterfuseki.filter.user.db;

import java.util.Hashtable;
import java.util.Vector;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;

public class LDResource {

	String uri=null;
	TAccess ta=null;
	Hashtable<String, Vector<RDFNode>> pred_v=new Hashtable<String, Vector<RDFNode>>();
	
	public static String baseUSER="https://data.federatief.datastelsel.nl/lock-unlock/authentication/model/def/";
	public static String baseAuth="https://data.federatief.datastelsel.nl/lock-unlock/authorisation/model/def/";
	
	public LDResource(TAccess ta, String uri)
	{
		this.uri=uri;
		this.ta=ta;
		getBasisData();
		
	}
	public boolean contains(String pred,String value)
	{
		try
		{
			 Vector<RDFNode> nodes=pred_v.get(pred);
			 for (RDFNode node:nodes)
			 {
				 if (node.toString().equalsIgnoreCase(value)) return true;
			 }
		}
		catch(Exception e)
		{
			
		}
		return false;
	}
	
	
	
	private void getBasisData()
	{
		String s = "Select distinct ?pred ?value where { {graph ?g {<"+uri+"> ?pred ?value.  } }  union {<"+uri+"> ?pred ?value} }";
		
		ResultSet rs =ta.select(s);
		while (rs.hasNext())
		{
			QuerySolution qs  =rs.nextSolution();
			String pred =qs.get("pred").toString();
			RDFNode n=qs.get("value");
			 Vector<RDFNode> nodes=pred_v.get(pred);
			 if (nodes==null)
			 {
				 nodes= new Vector<RDFNode>();
				 pred_v.put(pred, nodes);
			 }
			 nodes.add(n);
		}
	}
	public String getFirstLabel()
	{
		return getFirstAsString("http://www.w3.org/2000/01/rdf-schema#label");
	}
	
	public String getFirstAsString(String pred)
	{
		try
		{
			//Object o=pred_v.get("http://www.w3.org/2000/01/rdf-schema#label");
			
			return pred_v.get(pred).firstElement().toString();
		}
		catch(Exception e) {e.printStackTrace();}
		return null;
	}
		
	
	public static Vector<String> getAuthUrisOfCls(TAccess ta,String cls)
	{
		Vector<String> uris  =new Vector<String> ();
		String s = "Select distinct ?uri where { {graph ?g {?uri a <"+cls+">.  } } union {?uri a <"+cls+">} }";
		
		//System.out.println(s);
		
		ResultSet rs =ta.select(s);
		while (rs.hasNext())
		{
			QuerySolution qs  =rs.nextSolution();
			String uri =qs.get("uri").toString();
			uris.add(uri);
			
		}
		return uris;
	}
	
	public  Vector<LDResource> getRelations(String pred)
	{
		 Vector<LDResource> rv =new  Vector<LDResource>();
		 try
		 {
			 Vector<RDFNode> nodes=pred_v.get(pred);
			 for (RDFNode n:nodes)
			 {
				 String uri =n.toString();
				 rv.add(new LDResource(ta, uri));
			 }
		 }
		 catch(Exception e) {}
		 return rv;
	}
	
	public boolean isOfType(String type)
	{
		for (RDFNode t: pred_v.get("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"))
		{
			if (t.toString().equalsIgnoreCase(type))
			{
				return true;
			}
		}
		return false;
	}

}
