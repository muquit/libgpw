## Release v1.0.2

### Maven projects

The project is not in maven central yet. But it can be installed to your local
maven repository. 
To install it to your local maven repo, do the following:

```bash
mvn install:install-file \
   -Dfile=./target/gpw-1.0.2.jar \
   -DgroupId=com.muquit.gpw \
   -DartifactId=gpw \
   -Dversion=1.0.2 \
   -Dpackaging=jar \
   -DgeneratePom=true
```

Then add the following dependency in your project's pom.xml

```bash
   <dependency>
        <groupId>com.muquit.gpw</groupId>
        <artifactId>gpw</artifactId>
        <version>1.0.2</version>
   </dependency>
```
For non-maven projects, download gpw-1.0.2.jar jar from here.
