package com.agview.api.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class DbHandlerTest {

    private DbHandler sut;

    private static final String USDA_PIN = "12345";
    private static final String PREM_NAME = "David's Prem";
    private static final String SPECIES = "Bushpig";
    private static final String ICE_CONTACT_PHONE = "12165551234";
    private static final String ICE_CONTACT_EMAIL = "ice@vanila.com";
    private static final String LOCATION_TYPE = "Nursery";
    private static final int SITE_CAPACITY_NUMBER_BARNS = 3;
    private static final int SITE_CAPACITY_NUMBER_ANIMALS = 1000;
    private static final int NUMBER_OF_ANIMALS_ONSITE = 20;

    @BeforeEach
    public void setup() {
        sut = new DbHandler(System.getProperty("user.dir")+"/src/main/resources/premise.csv");
    }

    @Test
    public void getsPremises() {
        var expectedPremise = new Premise(USDA_PIN, PREM_NAME, SPECIES, ICE_CONTACT_PHONE, ICE_CONTACT_EMAIL, LOCATION_TYPE,
                SITE_CAPACITY_NUMBER_BARNS, SITE_CAPACITY_NUMBER_ANIMALS, NUMBER_OF_ANIMALS_ONSITE);

        var actual = sut.getPremisesToLoad();

        assertThat(actual, contains(expectedPremise));
    }

}
