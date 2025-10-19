## Release v1.0.2

### Maven projects

The project is not in maven central yet. But it can be installed to your local
maven repository. Please look at the script
[install_as_local_maven.sh](install_as_local_maven.sh) Use
this script if you are on Linux or Mac. If you are on Windows, look at the
script and run `mvn install:install-file` manually.

Then addd the following dependency in your project's pom.xml

```
   <dependency>
        <groupId>com.muquit.gpw</groupId>
        <artifactId>gpw</artifactId>
        <version>1.0.2</version>
   </dependency>
```
For non-maven projects, down gpw-1.0.2.jar jar from here.
