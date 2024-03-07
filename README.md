# Secured SPARQL Endpoint based on Fuseki applying the Rewrite SPARQL Query approach

This repo is an experimental implementation using the [Apache Jena
Fuseki2](https://jena.apache.org/documentation/fuseki2/) server. This implementation tries to apply
a **rewrite** of the SPARQL Query which applies the **Authorization Onthology**. This onthology is
under research in the [Lock-Unlock Project](https://labs.kadaster.nl/cases/lockunlock).

Other relevant repositories of the [Lock-Unlock Project](https://labs.kadaster.nl/cases/lockunlock) are:

- [Lock-Unlock Docs](https://github.com/kadaster-labs/lock-unlock-docs) including onthologies
  - Authorization Onthology (in research)
  - Logging Onthology (in research)
- [Lock-Unlock Testdata](https://github.com/kadaster-labs/lock-unlock-testdata)
- [Secured SPARQL Endpoint Sub Graph](https://github.com/kadaster-labs/secured-sparql-endpoint-subgraph) (based on Apache Jena & SpringBoot)
- (this repo) [Secured SPARQL Endpoint Rewrite (SPARQL Query)](https://github.com/kadaster-labs/secured-sparql-endpoint-rewrite) (based on Fuseki)
- [Lock-Unlock Helm Charts](https://github.com/kadaster-labs/lock-unlock-helm-charts)

## Rewrite implementation

This implementation is rewriting the incoming user SPARQL query and adds extra and constraining
snippets to it. These snippets are based upon the Authorization Configuration available somewhere.
This is still under discussion. The idea would be that each SPARQL endpoint with data also includes
their own Authorization Configuration as Linked Data.

This experimental set up is building the [Apache Jena
Fuseki2](https://jena.apache.org/documentation/fuseki2/) server. To apply the authorization
rewriting of the incoming SPARQL queries in 'the Fuseki style' would mean a
[FusekiAutoModule](https://jena.apache.org/documentation/fuseki2/fuseki-modules.html#automatically-loaded).
Although this might seem a great way there's still prototyping ongoing which is needed inside the
Fuseki development to get this working; see [JENA-2106](https://github.com/apache/jena/issues/2106).
Therefor this implementation simply adds a custom servlet directly into the `web.xml`. This is
originally extracted from the `jena-fuseki-fulljar:4.10.0`.

## Development

To build locally use [Maven](https://sdkman.io/sdks#maven):

```bash
mvn compile
```

To run locally from within your IDE, start the server with the test class (either in run or debug mode):
[`StartServer.main()`](src/test/java/nl/kadaster/labs/lock_unlock/StartServer.java)

## License

Licensed under [EUPL-1.2](LICENSE.md)
