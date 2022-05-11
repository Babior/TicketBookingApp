# list screenings after selected day nad time
curl -X GET localhost:8080/screenings/after/2020-05-10T15:49:01.549 | json_pp
# choose one of the screenings to show available seats
curl -X GET localhost:8080/screenings/3/seats | json_pp
# book seats
curl -X POST localhost:8080/bookings/screenings/3 -H 'Content-type:application/json' -d '
{
    "firstName":"Lizaveta",
    "lastName":"Babior",
    "seats": {
        "9":"ADULT",
        "10":"CHILD"
    }
}'



