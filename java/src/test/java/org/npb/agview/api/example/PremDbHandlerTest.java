package org.npb.agview.api.example;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.npb.agview.api.example.Constants.*;

class PremDbHandlerTest {

    private PremDbHandler sut;

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
        sut = new PremDbHandler(DB_DIRECTORY+"/prem.csv",
                DB_DIRECTORY+"/prem_address.csv", new DbHandler());
    }

    @Test
    public void getsPrems() {
        var expectedPrem1 = new Prem(USDA_PIN1, PREM_NAME1, SPECIES, ICE_CONTACT_PHONE, ICE_CONTACT_EMAIL, LOCATION_TYPE,
                SITE_CAPACITY_NUMBER_BARNS, SITE_CAPACITY_NUMBER_ANIMALS, NUMBER_OF_ANIMALS_ONSITE);
        var expectedPrem2 = new Prem(USDA_PIN2, PREM_NAME2, SPECIES, ICE_CONTACT_PHONE, ICE_CONTACT_EMAIL, LOCATION_TYPE,
                SITE_CAPACITY_NUMBER_BARNS, SITE_CAPACITY_NUMBER_ANIMALS, NUMBER_OF_ANIMALS_ONSITE);


        var actual = sut.getPremsToLoad();

        assertThat(actual, Matchers.containsInAnyOrder(expectedPrem1, expectedPrem2));
    }

    @Test
    public void GetsPremColumnNames() {
        var actual = sut.getPremColumnNames();

        assertThat(actual, contains("usda_pin","prem_name","site_capacity_number_animals","ice_contact_email","ice_contact_phone","location_type","species","site_capacity_number_barns","number_of_animals_on_site"));
    }

    @Test
    public void getsPremAddresses() {
        var expectedAddress1 = new PremAddress(USDA_PIN1, STREET_ADDRESS1, CITY1, STATE1, ZIP1);
        var expectedAddress2 = new PremAddress(USDA_PIN2, STREET_ADDRESS2, CITY2, STATE2, ZIP2);

        var actual = sut.getPremAddressesToLoad();

        assertThat(actual, containsInAnyOrder(expectedAddress1, expectedAddress2));
    }

    @Test
    public void GetsPremAddressColumnNames() {
        var actual = sut.getPremAddressesColumnNames();

        assertThat(actual, contains("usda_pin","street_address","city","state","zip"));
    }

}
