## Failure Simulation

Payment service supports production failure simulation.

### Redis Failure

POST

/api/payments?failureType=REDIS


### Database Failure

POST

/api/payments?failureType=DATABASE


### Payment Gateway Failure

POST

/api/payments?failureType=GATEWAY


### Bank API Failure

POST

/api/payments?failureType=BANK