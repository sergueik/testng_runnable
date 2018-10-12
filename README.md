### Info

This directory contains project
based on [crossbrowsertesting/selenium-testng](https://github.com/crossbrowsertesting/selenium-testng)
and [testng documentation](http://testng.org/doc/documentation-main.html#running-testng-programmatically)
and [http://www.baeldung.com/executable-jar-with-maven](http://www.baeldung.com/executable-jar-with-maven)
See also [jinahya/executable-jar-with-maven-example](https://github.com/jinahya/executable-jar-with-maven-example).

### Note

To enable single runnable jar with dependencies (at a cost of the size), use `runnable-jar-with-dependencies` profile:

```cmd
mvn -P single clean package
```
and run
```cmd
set DUMMYDIR=c:\temp
copy target\runnable_testng-jar-with-dependencies.jar %DUMMYDIR%
mkdir %DUMMYDIR%\target\classes
copy target\classes\Test.xlsx %DUMMYDIR%\target\classes
```
The `%DUMMYDIR%` represents the Jenkins workspace directory

Running the test from `%DUMMYDIR%`
```cmd
pushd %DUMMYDIR%
java -jar runnable_testng-jar-with-dependencies.jar
```

would produce:

```cmd
Test Name: failedLoginPage

Test Name: loginPage

===============================================
Command line suite
Total tests run: 2, Failures: 0, Skips: 0
===============================================
```

Alternatively the jar can be moved to a Vagrant box mounted directory which would make it appear in the target instance - this is WIP.

When bulilt with `default` profile, dependencies are copied to the `target/lib` during `package` [phase](http://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html),
and can be stored anywhere in the system

```cmd
mvn clean package
robocopy target\lib c:\temp\shared /s
rd /s/q target\lib
java -cp c:\temp\shared\*;target\runnable_testng-0.2-SNAPSHOT.jar demo.EntryPoint
```


This would also produce:
```cmd

Test Name: failedLoginPage
Test Name: loginPage

===============================================
Command line suite
Total tests run: 2, Failures: 0, Skips: 0
===============================================

```
Note that this example project has the data parameter file `Tests.xlsx` which is loaded from file system like below:

```java
File file = new File(System.getProperty("user.dir") + File.separator
    + "target\\classes\\Test.xlsx");
```
therefore one has to make it avalable from the specific path relative to the directory jar is placed.
It has to be checked in to `src/main/resources`.

The `default` profile makes the project version part of resulting jar filename,
the `runnable-jar-with-dependencies` profile strips it away.

The packaging of all and every dependencies into one jar has a disadvantage of
seriously increasing the size of that jar file, in our case from under 15 K
```cmd
02/27/2018  08:55 AM            14,445 runnable-testng-0.2-SNAPSHOT.jar
```
to over 36M:
```cmd
02/27/2018  08:57 AM        36,901,791 monolithic_runnable_testng.jar
```

NOTE: One cannot run the test class(es) as th regular testng test:
```cmd
mvn clean test

[WARNING] No processor claimed any of these annotations: org.testng.annotations.
BeforeSuite,org.testng.annotations.BeforeMethod,org.testng.annotations.Test,org.
testng.annotations.AfterSuite,org.testng.annotations.AfterMethod

```
For test annonated classes to be found, require rearranging the project files to store tests under `src/test/java` directory
but this will prevent the bootstrap class from seeing those (due to separation of build phases handling `main` and `test`):
```cmd
mvn compile
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin:3.6.0:
compile (default-compile) on project runnable_testng: Compilation failure
[ERROR] /c:/developer/sergueik/testng_runnable/src/main/java/demo/EntryPoint.java:[5,12] cannot find symbol
[ERROR] symbol:   class TestWithData
```
For running Maven testNG tests from a jar without dedicated bootstrap class see [this](https://niharikawhatsnext.wordpress.com/2015/03/11/running-maven-testng-tests-as-a-jar/).

### See also:

  * [maven vagrant plugin](https://github.com/nicoulaj/vagrant-maven-plugin)

#### TODO
Make it possible to build the jar in a different os than tests are going to run.

### License
This project is licensed under the terms of the MIT license.

### Author
[Serguei Kouzmine](kouzmine_serguei@yahoo.com)
