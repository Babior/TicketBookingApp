# list screenings after provided day nad time
printf "\n\nLooking up screenings after 2020-05-10T15:49:01.549\n\n"
curl -X GET localhost:8080/screenings/after/2020-05-10T15:49:01.549 | json_pp
# choose one of the screenings to show available seats
printf "\n\nChecking available seats for screeningId = 3\n\n"
curl -X GET localhost:8080/screenings/3/seats | json_pp
# book seats
printf "\n\nBook seats 9 and 10 for screening3\n\n"
curl -X POST localhost:8080/bookings/screenings/3 -H 'Content-type:application/json' -d '
{
    "firstName":"Lizaveta",
    "lastName":"Babior",
    "seats": {
        "9":"ADULT",
        "10":"CHILD"
    }
}'



