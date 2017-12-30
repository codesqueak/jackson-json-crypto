
# Jackson Crypto

A Jackson module to support JSON encryption/descryption 

## Build

Windows

gradlew clean build test

Linux

./gradlew clean build test

## How to use

### Register the module

Add the crypto module to your project.

```java
ObjectMapper objectMapper = new ObjectMapper().registerModule(new CryptoModule()); // add crypto module
EncryptionService.getInstance(objectMapper); // Initialise the crypto service

```

### Encrypt a field

Any field that is required to be encrypted has to be marked as such.  This can be done by either annotating the getter() or 
by annotating the field definition.

This ...

```java
    private String critical;
    ...
    @JsonProperty
    @Encrypt
    public String getCritical() {
        return this.critical;
    }
```

... or ...

```java
    @JsonProperty
    @Encrypt
    private String critical;
```

## Java Cryptography Extension (JCE) Unlimited Strength

As shipped, Java has limitations on the strength of encryption allowable.  To allow full strength encryption to be used, you will need to
enable the Unlimited Strength Jurisdiction Policy.

[Policy Files](http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html)

Problems with encyption strength will usually show as an error in the following form.
```
com.codingrodent.jackson.crypto.EncryptionException: java.security.InvalidKeyException: Illegal key size or default parameters
```

## Using Jenkins

The project includes a Jenkins file to control a pipeline build.

### Include Using Maven

TBD

### Include Using Gradle

TBD

