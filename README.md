# Secured SPARQL Endpoint based on Fuseki applying the Rewrite SPARQL Query approach

This repo is an experimental implementation using the [Apache Jena
Fuseki2](https://jena.apache.org/documentation/fuseki2/) server. This implementation tries to apply
a **rewrite** of the SPARQL Query which applies the **Authorization Onthology**. This onthology is
under research in the [Lock-Unlock Project](https://labs.kadaster.nl/cases/lockunlock).

Other relevant repositories are:

- [Lock-Unlock Onthologies](https://github.com/kadaster-labs/lock-unlock-onthologies)
  - Authorization Onthology (in research)
  - Logging Onthology (in research)
- [Lock-Unlock Testdata](https://github.com/kadaster-labs/lock-unlock-testdata)
- [Secured SPARQL Endpoint Sub Graph](https://github.com/kadaster-labs/secured-sparql-endpoint) (based on Apache Jena & SpringBoot)
- (this repo) [Secured SPARQL Endpoint Rewrite (SPARQL Query)](https://github.com/kadaster-labs/secured-sparql-endpoint-rewrite) (based on Fuseki)

## Development

To build locally use [Maven](https://sdkman.io/sdks#maven):

```bash
mvn compile
```

To run locally from within your IDE, start the server with the test class (either in run or debug mode):
[`StartServer.main()`](src/test/java/nl/kadaster/labs/lock_unlock/StartServer.java)

## License

Licensed under [EUPL-1.2](LICENSE.md)
