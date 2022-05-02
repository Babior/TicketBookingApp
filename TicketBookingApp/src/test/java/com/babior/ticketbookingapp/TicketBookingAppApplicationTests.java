package com.babior.ticketbookingapp;

import com.babior.ticketbookingapp.business.entity.Movie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TicketBookingAppApplicationTests {

    @Test
    void contextLoads() {
    }


    @Test
    public void testMovie() {

        Movie e1 = new Movie();
    }

}
