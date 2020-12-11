Java & R interoperability with GraalVM
======================================

Repo to demonstrate the interoperability between Java and R using GraalVM

Example to determine multiple change points in data-set using `e.divisive` from the R `ecp` package

Install
-------

This demo relies on GraalVM with the R runtime installed

1. Install SDK manager (https://sdkman.io/install)
 
   `$ curl -s "https://get.sdkman.io" | bash`
 
2. Install GraalVM 
 
   Find available GraalVM distributions 
 
   `$ sdk list java | grep grl`
   
   Install latest GraalVM distribution
 
   `$ sdk install java x.y.z.r11-grl` 
 
3. Set GraalVM to current java runtime
 
   `$ sdk use java 20.3.0.r11-grl`
 
4. Install GraalVM R

    `$ gu install R`
    
    R should be available in your PATH
    
    ```shell script
    $ R --version
    R version 3.6.1 (FastR)
    ```
   
5. Install `ecp` package 
    
    The ecp package contains the `e.divisive` function used to calculate change points in datasets

    `$ Rscript -e 'install.packages("ecp")'`

6. Run tests
 
    `$ mvn clean test`
    
N.B. if you see errors in the maven build e.g. ` package org.graalvm.polyglot does not exist` please ensure that you are using GraalVM sdk see step 3
```shell script
$ java --version
  openjdk 11.0.9 2020-10-20
  OpenJDK Runtime Environment GraalVM CE 20.3.0 (build 11.0.9+10-jvmci-20.3-b06)
  OpenJDK 64-Bit Server VM GraalVM CE 20.3.0 (build 11.0.9+10-jvmci-20.3-b06, mixed mode, sharing)


``` 