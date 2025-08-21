# Vulnerable Demo (for practice)

This Spring Boot app intentionally includes outdated libraries so you can practice **security remediation** in a Jenkins + SonarQube pipeline.

## What to do

1. Run the Jenkins pipeline (`Jenkinsfile`). It builds, tests, and runs Sonar analysis.
2. Review the security findings.
3. Remediate by upgrading **these dependencies in `pom.xml`**:
   - **Guava 19.0** → upgrade to a safe line (e.g. `32.1.3-jre` or newer LTS at your org)
   - **Apache HttpClient 4.3.1** → upgrade to `4.5.14` (or migrate to `httpclient5`)
   - **commons-collections 3.2.1** → replace with `commons-collections4:4.4` (preferred) or at least `3.2.2`

> Tip: After each upgrade, re-run the pipeline to ensure tests pass and vulnerabilities close.

## Build locally

```bash
mvn -v
mvn clean package
java -jar target/vulnerable-demo-0.0.1-SNAPSHOT.jar
# open http://localhost:8085/api/hello
```

## Why these libs
They are widely known to have CVEs in older versions and are frequently flagged by enterprise scanners (Sonar, OWASP, Snyk, etc.).

## Notes
- Project uses **Spring Boot 2.7.18** and **Java 17** to keep compatibility stable while you upgrade third-party libs.
- The code references those libraries in `LegacyLibUsage` so they are present on the classpath and visible to scanners.
