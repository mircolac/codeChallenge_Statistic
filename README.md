# BASIC OPERATIONS

Everytime a value is sent to the POST endpoint it can be

- discarded if the timestamp is older than 60 seconds (will return a 204)
- added to the database if the timestamp is recent (will return a 201)

If the value is added, the statistics will be updated and a scheduler will 
remove that value after 60 seconds.

When the value will be removed, statistics will be updated again.

Everytime the GET endpoint is called it will respond with a JSON string
containing the statistics.

# How to use:

- Clone or download the repository
- >mvn clean install
- navigate to 'target' folder
- >java -jar gs-rest-service-0.1.0.jar

Example correct POST request to the /transaction endpoint (must be *application/json Content-type*)

  {
  	"timestamp": 1506809503307,
  	"amount" : 25.0
  }


# Notes

- To produce the statistics, the Java DoubleSummaryStatistics class was used.


