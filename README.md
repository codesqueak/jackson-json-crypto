
# Jackson JSON Crypto

A Jackson module to support JSON encryption/decryption 

Based on an idea from [meltmedia](https://github.com/meltmedia/jackson-crypto)

## Build

Windows
```
gradlew clean build test
```
Linux
```
./gradlew clean build test
```
## How to use

### Register the module

Add the crypto module to your project.

```java
ObjectMapper objectMapper = new ObjectMapper(); // Get your object mapper instance
// Initialise the crypto service and add the module - You probably want to change the password!
new EncryptionService(objectMapper, new PasswordCryptoContext("Password1")); 
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

## Output JSON Format
```json
{  
   "critical":{  
      "salt":"IRqsz99no75sx9SCGrzOSEdoMVw=",
      "iv":"bfKvxBhq7X5su9VtvDdOGQ==",
      "value":"pXWsFPzCnmPieitbGfkvofeQE3fj0Kb4mSP7e28+Jc0="
   }
}
```

## Java Cryptography Extension (JCE) Unlimited Strength

As shipped, Java has limitations on the strength of encryption allowable.  To allow full strength encryption to be used, you will need to
enable the Unlimited Strength Jurisdiction Policy.

[Policy Files](http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html)

Problems with encyption strength will usually show as an error in the following form.
```
com.codingrodent.jackson.crypto.EncryptionException: java.security.InvalidKeyException: 
   Illegal key size or default parameters
```

## Using Jenkins

The project includes a Jenkins file to control a pipeline build.

## Include Using Maven
```
<dependency>
  <groupId>com.codingrodent</groupId>
  <artifactId>jackson-json-crypto</artifactId>
  <version>1.0.0</version>
</dependency>
```

## Include Using Gradle

```
compile group: 'com.codingrodent', name: 'jackson-json-crypto', version: '1.0.0'
```

