# AOM
# Account and Order Management (AOM)

The Account and Order Management (AOM) function serves as the middleware between all frontend applications and the system's databases.

## AOM Features

1. **RESTful API Provision**: Provides all required RESTful APIs for frontend applications.
2. **Transaction Management**: Handles transactions with VoltDB and OracleDB, ensuring synchronization between the two databases.
3. **Request Validation**: Validates requests to ensure data integrity before making database transactions.
4. **Authorization**: Ensures that customers making requests are authorized to access the data.

## RESTful API endpoints examples

### **Register New Customer**
POST Request body:
```
{
    "msisdn": "5000000000",
    "packageID": 2,
    "name": "MTH",
    "surname": "Course",
    "email": "mth@gmail.com",
    "password": "mth@1445",
    "securityKey": "0000"
}
```
Response:
```
{
    "message": "successfully registered",
    "msisdn": "5100000000"
}
```
### **Login Customer**
POST Request body:
```
{
    "msisdn": "5000000000",
    "password": "mth@1445"
}
```
Response:
```
{
    "msisdn": "5000000000",
    "token": "eyJhbGciOiJIUzUxMiJ9eyJzdWIiOiI1MD"
}
```
### **Get All Packages**
GET Request, Response:
```
[
    {
        "id": 1,
        "name": "default pack",
        "period": 30,
        "price": 300.0,
        "dataAmount": 15,
        "minAmount": 750,
        "smsAmount": 100
    },
    {
        "id": 2,
        "name": "mennan pack",
        "period": 30,
        "price": 350.0,
        "dataAmount": 20,
        "minAmount": 1000,
        "smsAmount": 150
    }
]
```
### **Get Remaining Balance for Customer**
GET Request:
```
//Parameters
MSISDN=5000000000

//headers
Authorization = eyJhbGciOiJIUzUxMiJ9eyJzdWIiOiI1MD
```
Response:
```
{
    "package": {
        "id": 2,
        "name": "mennan pack",
        "period": 30,
        "price": 350.0,
        "dataAmount": 20,
        "minAmount": 1000,
        "smsAmount": 150
    },
    "balance": {
        "remData": 20,
        "remSms": 150,
        "remMin": 1000,
        "remMoney": 100.0,
        "endDate": 1718648443,
        "startDate": 1716056443,
        "price": 350.000000000000,
        "packageId": 2
    }
}
```

