
set VER="2.2.0"

gpg -ab jackson-json-crypto-%VER%.jar
gpg -ab jackson-json-crypto-%VER%.pom
gpg -ab jackson-json-crypto-%VER%-javadoc.jar
gpg -ab jackson-json-crypto-%VER%-sources.jar

jar -cvf bundle.jar jackson-json-crypto-%VER%.pom jackson-json-crypto-%VER%.pom.asc jackson-json-crypto-%VER%.jar jackson-json-crypto-%VER%.jar.asc jackson-json-crypto-%VER%-javadoc.jar jackson-json-crypto-%VER%-javadoc.jar.asc jackson-json-crypto-%VER%-sources.jar jackson-json-crypto-%VER%-sources.jar.asc

