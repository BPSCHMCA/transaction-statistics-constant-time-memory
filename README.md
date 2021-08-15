# Transaction Statistics In Constant Time And Memory
It calculates the statistics of the transactions done in a given time and ensures the solution must run with constant time complexity and memory.

### The API has the following end-points:
*	*POST /transactions – used to make a new transaction*
*	*GET /statistics – used to return the statistic of the transactions done in the last given seconds.*
*	*DELETE /transactions – deletes all transactions' statistics.*
 
### Specifications

#### POST: /transactions
This end-point is called to create a new transaction. It MUST execute in constant time and memory (O(1)).
```
Body:
{
  "amount": "1.23",
  "timestamp": "2021-08-07T07:53:43.123Z"
}
```
Description:
*	*amount – transaction amount; a string of arbitrary length that is parsable as a BigDecimal*
*	*timestamp – transaction time in the ISO 8601 format YYYY-MM-DDThh:mm:ss.sssZ in the UTC timezone*
 
Response: Empty body with one of the following response code:
*	*201 – in case of success*
*	*204 – if the transaction is older than 60 seconds*
*	*400 – if the JSON is invalid*
*	*422 – if any of the fields are not parsable or the transaction date is in the future*
 
#### GET: /statistics
This API returns the statistics of all the transactions that happened in the last given seconds. It must execute in constant time and memory (O(1)).

Sample response:
{
  "sum": "123.45",
  "avg": "45.12",
  "max": "30.32",
  "min": "2.34",
  "count": 25
}
 
Description:
*	*sum – a BigDecimal specifying the sum of transaction amount in the last given seconds*
*	*avg – a BigDecimal specifying the average of transaction amount in the last given seconds*
*	*max – a BigDecimal specifying the highest transaction amount in the last given seconds*
*	*min – a BigDecimal specifying the lowest transaction amount in the last given seconds*
*   *count – a long specifying the total number of transactions which are executed in the last given seconds*

All BigDecimal values always contain exactly two decimal places and use `HALF_ROUND_UP` rounding. eg: 12.345 is returned as 12.35, 12.3 is returned as 12.30
 
#### DELETE: /transactions
This API deletes the statistics of all the transactions done so far.

The API accepts an empty request body and return a 204 status code.
 
## Application setup
To Build: 
mvn clean install 

To Run:
spring-boot:run

## Technical Specifications
* Spring boot application
* Java 8 Date and Streams concept
