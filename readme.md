# Setting up

Download Visual Studio Code From [Here](https://code.visualstudio.com/).

Once downloaded. Go to extensions marketplace (5th icon on the left menu), search and download the below extensions.
- Spring boot tools
- Spring boot extension pack
- Java extension pack

## To Run the Code

1) Open up visual studio code and open the project from it's root directory.

2) Click on the 4th icon (debugging mode). on the top left of the editor there will be a play button. click on it and the server will run.

3) Once the services is running. you can query it using the below endpoints. Feel free to change the parameters and check on the program. 

- To get exchange rate with a specific amount. (http://localhost:7888/exchange?from=sgd&to=usd&amount=100)
- To get exchange rate. (http://localhost:7888/exchangeRate?from=USD&to=SGD)
- To complete the exchange transaction. (http://localhost:7888/exchange/confirm?from=usd&to=sgd&amount=21)


|Query String property|Description                          |E.g                         |
|----------------|-------------------------------|-----------------------------|
|from|`'Exchanging from which currency'`            |USD,SGD,MYR and etc..            |
|to          |`"Exchanging to which currency"`            |USD,SGD,MYR and etc..            |
|amount|`total amount of currency to exchange`|100|


4) To check is the data saved in a file. Kindly view project/target/classes/result.csv file.

## Improvement

If provided with more time. 
- Will surely return all services with a standard base response 
example: 
{ 
	"errorCode": 0,
	"errorMessage": "Success"
}

- will group all error into a method to handle all errors.

