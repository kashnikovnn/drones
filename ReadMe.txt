
Getting Started
To get started with this project, you will need to clone it onto your local machine. To do so, open your terminal and enter the following command:

git clone https://oauth:glpat-wavqhSZ97Hgh2H-X2-Ge@gitlab.com/musala-coding-tasks-solutions/nikolay-kashnikov.git
Once you have cloned the repository, you can navigate into the project directory:
cd drones

Build Instructions
To build the project, you will need to have Maven installed on your machine. Once you have Maven installed, run the following command from the project directory:

mvn clean install
This will compile the Java code and create a JAR file in the target directory.

Run Instructions
To run the application, you can use the following command from the project directory:

java -jar target/drones-1.0-SNAPSHOT.jar
This will start the application and you should see some output in your terminal.

Test Instructions
To run the tests for the application, you can use the following command from the project directory:

mvn test

This will run all of the tests for the application and you should see the results in your terminal.

Here are some example curl requests for each method in the DroneController:

Register Drone
Registers a new drone with the given serialNumber, modelName, weightLimit, and batteryCapacity.
curl -X POST \
  http://localhost:8080/api/drones \
  -H 'Content-Type: application/json' \
  -d '{
        "serialNumber": "DRN0001",
        "model": "Middleweight",
        "weightLimit": 250,
        "batteryCapacity": 80
  }'

Get Drone
Returns the drone with the given id.
curl -X GET \
  http://localhost:8080/api/drones/1


Get Loading Available Drones
Returns a list of all drones that are currently available for loading.
curl -X GET \
  http://localhost:8080/api/drones/loading-available


Get Battery Level
Returns the battery level of the drone with the given id.
curl -X GET \
  http://localhost:8080/api/drones/1/battery-level


Load Drone
Loads the given medications onto the drone with the given droneId.
curl -X POST \
  http://localhost:8080/api/drones/loading \
  -H 'Content-Type: application/json' \
  -d '{
        "droneId": 1,
        "medications": [
            {
                "medicationId": 1,
                "quantity": 3
            },
            {
                "medicationId": 2,
                "quantity": 1
            }
        ]
    }'

Get Drone Loading
Returns the loading information for the drone with the given id.
curl -X GET \
  http://localhost:8080/api/drones/1/loading

Save Medication
Saves a new medication with name, weight, and code. Optionally accepts an image file to be uploaded.
curl -X POST 'http://localhost:8080/medications' \
  -H 'Content-Type: multipart/form-data' \
  -F 'medication={"name":"MedicationName","weight":50,"code":"MED1234"};type=application/json' \
  -F 'image=@/path/to/image.jpeg;type=image/jpeg'

Get Medication by ID
Retrieves medication information with the given ID.
curl -X GET http://localhost:8080/medications/1


Get Medications by IDs List
Retrieves medication information for the given list of IDs.
curl -X GET http://localhost:8080/medications/list/1,2,3
