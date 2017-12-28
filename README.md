[![License: MIT](https://img.shields.io/badge/license-Apache_2.0-brightgreen.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.codingrodent.microprocessor/Z80Processor/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.codingrodent.microprocessor/Z80Processor)
[![Maven Central](https://img.shields.io/jenkins/s/https/jenkins.qa.ubuntu.com/precise-desktop-amd64_default.svg)](https://img.shields.io/jenkins/s/https/jenkins.qa.ubuntu.com)

# Z80 Processor in Java

Z80Processor is a an implementation of the Mostek / Zilog Z80 processor in Java

## Build

Windows

gradlew clean build test

Linux

./gradlew clean build test

The build may take a few minutes as it includes a comprehensive test suite for the Z80 instruction set.


## Using Jenkins

The project includes a Jenkins file to control a pipeline build.  At present the available version of the Jacoco plugin (2.0.1 at time of writing) does not support a 'publisher'.  The build was tested using a hand built plugin from the master branch of the  [project](https://github.com/jenkinsci/jacoco-plugin)

### Include Using Maven

```
<!-- https://mvnrepository.com/artifact/com.codingrodent.microprocessor/Z80Processor -->
<dependency>
    <groupId>com.codingrodent.microprocessor</groupId>
    <artifactId>Z80Processor</artifactId>
    <version>2.0.2</version>
</dependency>
```

### Include Using Gradle

```
// https://mvnrepository.com/artifact/com.codingrodent.microprocessor/Z80Processor
compile group: 'com.codingrodent.microprocessor', name: 'Z80Processor', version: '2.0.2'
```

## Undocumented instruction

The code attempts to faithfully reproduce the numerous undocumented instructions in the Z80.  I have tested against a real device but if you find any issues, let me know.

## How to make a machine

To make a machine you need three components, the CPU, Memory and I/O.  To see a simple example, look at the test in Z80CoreTest.java.  






