Transit Trip Management System
-------------------------------
The current project, parses a file and provides 3 output files. 
unprocessableTaps.csv : Incorrect data which could not be processed.
summary.csv: Summary of the trip data
trips.csv : Total trips

Getting Started
----------------
These instructions will get you a copy of the project up & running on your local
system for testing purposes.

Prerequisites
--------------
Java 1.8
Maven installation.

Steps:
------
Unzip & Extract the project LittlePayExercise on your local machine.

Running the tests
------------------
1. Go into the folder LittlePayExercise in your local machine.
2. Run command : 
For running the solution from command line: Run command: 
mvn exec:java -Dexec.args="-f <Complete File Path>"

Deployment
-----------
Run mvn package for creation of the jar for the project.

Built With
----------
Maven

Assumptions:
------------
1. For a CANCELLED trip, time is not taken into account. If the Stop Id is same, the trip is assumed as 
cancelled.
2. Timezone is assumed to be same for date checking.
3. For a specified PAN, i do not expect two Taps of OFF to arrive one after another. If its a duplicate row, then it is taken care
of in code.
4. Its assumed that taps.csv is processed hourly or faster for software performance reasons. If huge files are processed then more
efficient algorithms might be needed.

Bugs in Application:
--------------------
1. If the items received in transaction record are less then expected, the unProcessableTaps would not contain all the data,
as the logic to identify data is not present.Due to this correct error is also not generated.
2. Duplicate items are detected and removed but they are currently not logged in unProcessableTaps.csv
3. Duration secs is not yet calculated for trips.csv
