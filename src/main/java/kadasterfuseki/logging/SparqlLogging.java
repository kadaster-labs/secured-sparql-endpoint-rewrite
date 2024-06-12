package kadasterfuseki.logging;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;

import jakarta.servlet.http.HttpServletRequest;
import kadasterfuseki.filter.user.User;
import kadasterfuseki.filter.user.db.TAccess;

public class SparqlLogging {

	String endpoint = null;
	String graph = "http://labs.kadaster.nl/logging";
	String uri = null;

	public static String logPersona = null;

	public static String getLogPersona() {
		if (logPersona == null) {
			logPersona = UUID.randomUUID().toString();
		}
		return logPersona;
	}

	public static boolean shouldLog(TAccess ta, boolean b) {
		return true;
		/*
		 * try { ResultSet r=ta.
		 * select("select distinct ?a where {graph <http://log.log/log>  {?a ?b ?c}} limit 1"
		 * ); if (r.hasNext()) return true;
		 * 
		 * } catch(Exception e) { e.printStackTrace(); }
		 * 
		 * return false;
		 */
	}

	public static void addLog(HttpServletRequest request, String logString) {
		new SparqlLogging(request.getRequestURL().toString()).addLog(logString);

	}

	public SparqlLogging(String endpoint) {
		endpoint = endpoint.split("\\?")[0];
		this.endpoint = endpoint.replace("/query", "").replace("/sparql", "") + "?persona=" + getLogPersona();

		// System.out.println("endpoint is "+this.endpoint);
		uri = "<http://labs.kadaster.nl/logging/log" + UUID.randomUUID().toString() + ">";

	}

	public void addLog(String msg) {
		String updateQuery = "INSERT DATA {graph<" + graph + ">{ " + uri
				+ " a <https://data.federatief.datastelsel.nl/lock-unlock/logging/model/def/AppEvent>." + uri
				+ "<https://data.federatief.datastelsel.nl/lock-unlock/logging/model/def/message> \"" + msg + "\"."
				+ uri + "<https://data.federatief.datastelsel.nl/lock-unlock/logging/model/def/date> " + getDate() + "."
				+ "}}";
		UpdateProcessor processor = UpdateExecutionFactory.createRemote(UpdateFactory.create(updateQuery), endpoint);

		// Execute the update
		processor.execute();

	}

	public String getDate() {
		Date d = new Date();
		int m = d.getMonth() + 1;
		String ms = m + "";
		if (m < 10)
			ms = "0" + ms;
		int day = d.getDate();
		String dayS = day + "";
		if (day < 10)
			dayS = "0" + day;
		int hour = d.getHours() + 1;
		String hourS = hour + "";
		if (hour < 10)
			hourS = "0" + hourS;
		String minS = d.getMinutes() + "";
		if (d.getMinutes() < 10)
			minS = "0" + minS;

		String sS = d.getSeconds() + "";
		if (d.getSeconds() < 10)
			sS = "0" + sS;

		String date = (d.getYear() + 1900) + "-" + ms + "-" + dayS + "T" + hourS + ":" + minS + ":" + sS + ".000";
		return "\"" + date + "\"^^<http://www.w3.org/2001/XMLSchema#dateTime>";
	}

	public void addQueries(String query, String pquery, User user) {
		try {
			query = StringEscapeUtils.escapeJava(query);
			pquery = StringEscapeUtils.escapeJava(pquery);

			String updateQuery = "INSERT DATA {graph<" + graph + ">{ " + uri
					+ " a <https://data.federatief.datastelsel.nl/lock-unlock/logging/model/def/SparqlSelectEvent>."
					+ uri + "<https://data.federatief.datastelsel.nl/lock-unlock/logging/model/def/by_user> <"
					+ user.getUri() + ">." + uri
					+ "<https://data.federatief.datastelsel.nl/lock-unlock/logging/model/def/sparqlquery> \"" + query
					+ "\"." + uri
					+ "<https://data.federatief.datastelsel.nl/lock-unlock/logging/model/def/executedQuery> \"" + pquery
					+ "\"." + uri + "<https://data.federatief.datastelsel.nl/lock-unlock/logging/model/def/startDate> "
					+ getDate() + "."

					+ "  }}";
			// System.out.println(updateQuery);
			UpdateProcessor processor = UpdateExecutionFactory.createRemote(UpdateFactory.create(updateQuery),
					endpoint);

			// Execute the update
			processor.execute();
		} catch (Exception e) {
			System.err.println("error adding querie to triplestore");
			e.printStackTrace();
		}

	}

	public void addTripleQuery(String uri, String pred, String litral) {
		System.out.println("not yet implemented");
	}

}
