#Examples for obtaining and renewing API (OAuth) access token and creating Premises and Movements.

For simplicity, this example uses CSV files as database tables.  Your environment probably will have real database tables.
However, the concept of taking your data from multiple tables and creating POST requests needed by our API should be the
same regardless of your database.

##To run the application from CLI
`./gradlew run --args="--baseUrl <protocol://hostname:port> --apiKey <your API key> --apiSecret <your API secret>"`

##To run tests from CLI
* Set-up your NPB_BASE_URL, NPB_API_KEY, and NPB_API_SECRET environment variables
* `./gradlew test`
