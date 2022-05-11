# TicketBookingApp

## Setup for MacOS
### Global requirements
* Installed Java and JDK
* Installed Maven
* Installed Git
* Installed and running Docker

## Data
### PostgreSQL on Docker
#### Pull Docker image
```bash
sudo docker pull postgres
```
#### Create postgres container
```bash
sudo docker run --name postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres
```
#### Run the container
```bash
sudo docker exec -it postgres psql -U postgres
```
#### Create database with name "multiplex"
```bash
CREATE DATABASE multiplex;
exit
```

## Application
Clone repository
```bash
git clone https://github.com/Babior/TicketBookingApp.git
```
Open project folder
```bash
cd TicketBookingApp
```
Build and run application
```bash
sudo sh ./buildAndRun.sh
```
Run endpoints
```bash
sudo sh ./test.sh
```

## Demo

### Endpoints
**1. List available movies for the chosen date** <br/>
The user selects the day and the time when he/she would like to see the movie. 
The system lists movies available in the given time interval - title and screening
  times.
```bash
curl -X GET localhost:8080/screenings/after/2022-05-09T15:49:01.549
```

**2. Show the information about the particular screening** <br/>
The user chooses a particular screening.
The system gives information regarding screening room and available seats.
```bash
curl -X GET localhost:8080/screenings/18/seats
```

**3. Make a booking for chosen seats** <br/>
The user chooses seats, and gives the name of the person doing the reservation
  (name and surname).The system gives back the total amount to pay and reservation expiration time.

```bash
curl -X POST localhost:8080/screenings/18 -H 'Content-type:application/json' -d '
{
    "firstName":"Lizaveta", 
    "lastName":"Babior", 
    "seats": {
        "1":"ADULT", 
        "2":"CHILD"
    }
}'
```

Bash scripts to run endpoints:

Step into project directory and run script with tests.
```bash
cd TicketBookingApp
./test.sh
```