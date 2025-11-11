# gitinspector

Simple Git Inspector in Java

Searches for ASVS comments in files at directory DIR and returns who committed that line (blame)

Example:

<pre>
<code>
C:\Users\119667631\git\gitinspector>java -jar target\gitinspector-1.0.0.jar C:\Users\119667631\git\HCOM_202501_CodificacaoSeguraV2pub
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
27              // ASVS 5.0.0 item 2.2.2        LeonardoKenjiShikida
</code>
</pre>

see Main.class for details
