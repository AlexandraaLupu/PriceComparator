# Price Comparator

Application in Spring Boot using PostgreSQL for the database. The tables are populated using the CSVs from *resources/data* directory.

For most of the endpoints the date when the user makes the API call should be provided. The endpoints can also be tested using Swagger.

## How to install
- clone the repository
- create a database called **PriceComparator**
- in **application.properties** add your username and password for the database connection

## Assumptions made
- the CSVs already exist in *resources/data*
- in CsvImportService you must comment the **@PostConstruct** annotation if you don't want the tables populated at the start of the application

## API endpoints

- **POST** /import/
```
filename: *name_of_the_csv*
```
### Feature 1
- **GET** /offers/optimize/ 
```
{
  "productIds": ["P001", "P002"], // list of strings
  "date": "2025-05-01"
}
```
### Feature 2
- **GET** /discounts/top/ 
```
date: *date_of_the_request*
```
### Feature 3
- **GET** /discounts/recent/ 
```
date: *date_of_the_request*
```
### Feature 4
- **GET** /trend/product/{product_id}/range/ 
```
store: *name_of_the_store* (optional)
startDate: *start_date*
endDate: *end_date*
```
### Feature 5
- **GET** /trend/best-deals/ 
```
date: *date_of_the_request*
```
### Feature 6
- **POST** /alert/ 
```
{
  "userId": 1,
  "productId": "P001",
  "targetPrice": 9
}
```
- **GET** /alert/
```
userId: *user_id*
date: *date_of_the_request*
```