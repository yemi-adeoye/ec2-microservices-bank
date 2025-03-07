# Banks Microservice

This tiny microservices handles accounts transactions such as deposit, withdrawals, interbank transfers, etc

## Processing Transactions

Transactions are written to Redis as soon as they are received. A response is sent to show if the write process was successful or not. Transactions that are successfully written to the database are picked up by the scheduler at some point in time. Once successfully processed, the accounts entities are updated accordingly.
