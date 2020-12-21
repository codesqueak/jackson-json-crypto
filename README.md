![CodeQL](https://github.com/codesqueak/jackson-json-crypto/workflows/CodeQL/badge.svg) 
![Java CI with Gradle](https://github.com/codesqueak/jackson-json-crypto/workflows/Java%20CI%20with%20Gradle/badge.svg)
[![Maven Central](https://img.shields.io/maven-central/v/com.codingrodent/jackson-json-crypto.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.codingrodent%22%20AND%20a:%22jackson-json-crypto%22)

# Jackson JSON Crypto Module

A Jackson module to support JSON encryption and decryption operations. Encryption is via AES. Key generation is password
based.

Keyword: Jackson, JSON, AES, PKCS5, Encryption, Salt, Initialization Vector, IV, Java

Based on an idea from [meltmedia](https://github.com/meltmedia/jackson-crypto)

If you find this project useful, you may want to [__Buy me a Coffee!__ :coffee:](https://www.buymeacoffee.com/codesqueak) Thanks :thumbsup:

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

These examples are demonstrated in the ```CryptoDemo``` unit test class

### Option 1 - Very Quick and Easy

Add the crypto module to your project. Common use case with a cipher of AES/CBC/PKCS5Padding and a key factory algorithm of PBKDF2WithHmacSHA512

Just supply a password.

```java
ObjectMapper objectMapper = EncryptionService.getInstance("Password1");
```


### Option 2 - Quick and Easy

Similar to Option 1, but you already have a ObjectMapper 

```java
ObjectMapper objectMapper = ...
EncryptionService encryptionService = 
   new EncryptionService(objectMapper, new PasswordCryptoContext("Password1");
objectMapper.registerModule(new CryptoModule().addEncryptionService(encryptionService));
```


### Option 3 - Configure Everything

Where you just need full control. 

```java
// get an object mapper
ObjectMapper objectMapper = new ObjectMapper();
// set up a custom crypto context - Defines the interface to the crypto algorithms used
ICryptoContext cryptoContext = new PasswordCryptoContext("Password");
// The encryption service holds functionality to map clear to / from encrypted JSON
EncryptionService encryptionService = new EncryptionService(objectMapper, cryptoContext);
// Create a Jackson module and tell it about the encryption service
CryptoModule cryptoModule = new CryptoModule().addEncryptionService(encryptionService);
 // Tell Jackson about the new module
objectMapper.registerModule(cryptoModule);
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

## Using Jenkins

The project includes a Jenkins file to control a pipeline build.

## Include Using Maven
```
<dependency>
  <groupId>com.codingrodent</groupId>
  <artifactId>jackson-json-crypto</artifactId>
  <version>2.2.0</version>
</dependency>
```

## Include Using Gradle

```
compile group: 'com.codingrodent', name: 'jackson-json-crypto', version: '2.2.0'
```

