
# Jackson JSON Crypto Module

A Jackson module to support JSON encryption and decryption operations.  Encryption is via AES. Key generation is password based.

Keyword: Jackson, JSON, AES, PKCS5, Encryption, Salt, Initialization Vector, IV, Java

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

These examples are demonstrated in the ```CryptoDemo``` unit test class

### Option 1 - Very Quick and Easy

Add the crypto module to your project. Common use case with a cipher of AES/CBC/PKCS5Padding and a key factory algorithm of PBKDF2WithHmacSHA512

Just supply a password.

```java
ObjectMapper objectMapper = EncryptionService.getInstance(new PasswordCryptoContext("Password1"));
```


### Option 2 - Quick and Easy

Similar to Option 1, but you already have a ObjectMapper 

```java
ObjectMapper objectMapper = ...
EncryptionService encryptionService = 
   new EncryptionService(objectMapper, new PasswordCryptoContext("Password1"));
objectMapper.registerModule(new CryptoModule().addEncryptionService(encryptionService));
```


### Option 3 - Configure Everything

Where you just need full control. 

```java
// get an object mapper
ObjectMapper objectMapper = new ObjectMapper();
// set up a custom crypto context - Defines teh interface to the crypto algorithms used
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
  <version>1.1.0</version>
</dependency>
```

## Include Using Gradle

```
compile group: 'com.codingrodent', name: 'jackson-json-crypto', version: '1.1.0'
```

