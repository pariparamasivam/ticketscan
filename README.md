# ticketscan application
Spring boot application to calculate ticket scanning error rate based on rules and nearby tickets value data provided in text document.

The application shall process only the text file with extension .txt and in below data format

class: 1-3 or 5-7

row: 6-11 or 33-44

seat: 13-40 or 45-50


your ticket:

7,1,14


nearby tickets:

7,3

40,47,4,50

55,2,20

38,6,12


here, 
•	First section(rules section) starts from first line of file . This section shall contain text with minimum 4 integers to represent two ranges of integers. Following same format as in above example is preferred.
•	After first section empty line is expected.
•	Second section (your ticket section) starts from next line, text  "your ticket:" shall be present in this line. In the next line your ticket values shall be presented as comma separated integers.
•	After second section empty line is expected.
•	Third section (nearby tickets section) starts from next line, text "nearby tickets:" shall be present in this line . From the next line nearby tickets value shall be presented. Each line of coma separated integers shall represent a nearby ticket value.
 
 To test in Postman,
 Endpoint URL
  http://localhost:8080/ticketscan/v1/api/errorrate
  provide input in "Body --> form-data" "key as 'file'" and choose file

•	For valid input, response shall contain error rate value calculated by the service and exception details as null.
Example,
{
    "errorRate": 71,
    "exceptionDetails": null
}
•	For invalid input, response shall contain error rate value as 0 and exception details from server side.
Example,
{
    "errorRate": null,
    "exceptionDetails": {
        "errorMessage": "Invalid Input file type",
        "status": "406 NOT_ACCEPTABLE"
    }
}
