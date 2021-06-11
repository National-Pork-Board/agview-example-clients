package org.npb.agview.api.example;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class PremiseDbHandlerTest {

    private PremiseDbHandler sut;

    private static final String USDA_PIN1 = "1277XH9";
    private static final String USDA_PIN2 = "0615PKA";
    private static final String PREM_NAME1 = "David's Prem";
    private static final String PREM_NAME2 = "Nate's Prem";
    private static final String SPECIES = "Swine";
    private static final String ICE_CONTACT_PHONE = "12165551234";
    private static final String ICE_CONTACT_EMAIL = "ice@vanila.com";
    private static final String LOCATION_TYPE = "Nursery";
    private static final int SITE_CAPACITY_NUMBER_BARNS = 3;
    private static final int SITE_CAPACITY_NUMBER_ANIMALS = 1000;
    private static final int NUMBER_OF_ANIMALS_ONSITE = 20;
    private static final String STREET_ADDRESS1 = "951 S. Green Rd";
    private static final String CITY1 = "South Euclid";
    private static final String STATE1 = "OH";
    private static final String ZIP1 = "44121";
    private static final String STREET_ADDRESS2 = "555 Superior Ave";
    private static final String CITY2 = "Cleveland";
    private static final String STATE2 = "OH";
    private static final String ZIP2 = "44108";

    @BeforeEach
    public void setup() {
        var projectRoot = System.getProperty("user.dir");
        sut = new PremiseDbHandler(projectRoot+"/src/main/resources/premise.csv",
                projectRoot+"/src/main/resources/premise_address.csv", new DbHandler());
    }

    @Test
    public void getsPremises() {
        var expectedPremise1 = new Premise(USDA_PIN1, PREM_NAME1, SPECIES, ICE_CONTACT_PHONE, ICE_CONTACT_EMAIL, LOCATION_TYPE,
                SITE_CAPACITY_NUMBER_BARNS, SITE_CAPACITY_NUMBER_ANIMALS, NUMBER_OF_ANIMALS_ONSITE);
        var expectedPremise2 = new Premise(USDA_PIN2, PREM_NAME2, SPECIES, ICE_CONTACT_PHONE, ICE_CONTACT_EMAIL, LOCATION_TYPE,
                SITE_CAPACITY_NUMBER_BARNS, SITE_CAPACITY_NUMBER_ANIMALS, NUMBER_OF_ANIMALS_ONSITE);


        var actual = sut.getPremisesToLoad();

        assertThat(actual, Matchers.containsInAnyOrder(expectedPremise1, expectedPremise2));
    }

    @Test
    public void getsPremiseAddresses() {
        var expectedAddress1 = new PremiseAddress(USDA_PIN1, STREET_ADDRESS1, CITY1, STATE1, ZIP1);
        var expectedAddress2 = new PremiseAddress(USDA_PIN2, STREET_ADDRESS2, CITY2, STATE2, ZIP2);

        var actual = sut.getPremiseAddressesToLoad();

        assertThat(actual, containsInAnyOrder(expectedAddress1, expectedAddress2));
    }

}
