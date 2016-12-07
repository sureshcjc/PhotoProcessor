# PhotoProcessor

PhotoProcessor is a standalone application that retrieves image keys from s3 bucket and processes the EXIF metadata for each image and to store the metadata in a database for further analysis and reporting.

Application config (application.properties):
- Use propety s3.bucket.url to setup the s3 bucket url
- Use bulk.process.size for the size of batch process to do bulk operations

Database config (application.properties):
- modify the following properties
spring.datasource.url=jdbc:mysql://localhost/test
spring.datasource.username=root
spring.datasource.password=roadster
- the application will create the table the first time it runs

How to Run:
- Build and package as "mvn package"
- Goto "target" folder
- Run as "java -jar PhotoProcesser-0.0.1-SNAPSHOT.jar"

