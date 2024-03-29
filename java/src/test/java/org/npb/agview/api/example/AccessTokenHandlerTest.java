package org.npb.agview.api.example;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


class AccessTokenHandlerTest {

    private AccessTokenHandler sut;

    private final String BASE_URL = System.getenv("NPB_BASE_URL");
    private final String API_KEY = System.getenv("NPB_API_KEY");
    private final String API_SECRET = System.getenv("NPB_API_SECRET");
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final int TEN_SECONDS = 10;
    private final int TEN_THOUSAND_SECONDS = 10000;

    @BeforeEach
    public void setup() {
        sut = new AccessTokenHandler(httpClient, new Arguments(BASE_URL, API_KEY, API_SECRET));
    }

    @Test
    public void getsNewAccessToken() {
        var actual = sut.getNewAccessToken();

        assertThat(actual.getAccess(), is(notNullValue()));
        assertThat(actual.getExp(), is(greaterThan(0L)));
    }

    @Test
    public void reusesExistingNonExpiredAccessToken() {
        var originalAccessToken = sut.getNewAccessToken();

        var actual = sut.getNonExpiredOrNewAccessToken(originalAccessToken, TEN_SECONDS);

        assertThat(actual, Matchers.is(originalAccessToken));
    }

    @Test
    public void getsNewAccessTokenWhenExistingOneIsOutsideOfValidityRange() {
        var originalAccessToken = sut.getNewAccessToken();

        var actual = sut.getNonExpiredOrNewAccessToken(originalAccessToken, TEN_THOUSAND_SECONDS);

        assertThat(actual, is(Matchers.not(originalAccessToken)));
    }

}


