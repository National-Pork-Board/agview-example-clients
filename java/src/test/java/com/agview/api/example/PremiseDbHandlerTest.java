package com.agview.api.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class PremiseDbHandlerTest {

    private PremiseDbHandler sut;

    private static final String USDA_PIN = "00LAT5C";
    private static final String PREM_NAME = "David's Prem";
    private static final String SPECIES = "Bushpig";
    private static final String ICE_CONTACT_PHONE = "12165551234";
    private static final String ICE_CONTACT_EMAIL = "ice@vanila.com";
    private static final String LOCATION_TYPE = "Nursery";
    private static final int SITE_CAPACITY_NUMBER_BARNS = 3;
    private static final int SITE_CAPACITY_NUMBER_ANIMALS = 1000;
    private static final int NUMBER_OF_ANIMALS_ONSITE = 20;
    private static final String STREET_ADDRESS = "951 S. Green Rd";
    private static final String CITY = "South Euclid";
    private static final String STATE = "OH";
    private static final String ZIP = "44121";

    @BeforeEach
    public void setup() {
        var projectRoot = System.getProperty("user.dir");
        sut = new PremiseDbHandler(projectRoot+"/src/main/resources/premise.csv",
                projectRoot+"/src/main/resources/premise_address.csv");
    }

    @Test
    public void getsPremises() {
        var expectedPremise = new Premise(USDA_PIN, PREM_NAME, SPECIES, ICE_CONTACT_PHONE, ICE_CONTACT_EMAIL, LOCATION_TYPE,
                SITE_CAPACITY_NUMBER_BARNS, SITE_CAPACITY_NUMBER_ANIMALS, NUMBER_OF_ANIMALS_ONSITE);

        var actual = sut.getPremisesToLoad();

        assertThat(actual, contains(expectedPremise));
    }

    @Test
    public void getsPremiseAddresses() {
        var expectedAddress = new PremiseAddress(USDA_PIN, STREET_ADDRESS, CITY, STATE, ZIP);

        var actual = sut.getPremiseAddressesToLoad();

        assertThat(actual, contains(expectedAddress));
    }

}
