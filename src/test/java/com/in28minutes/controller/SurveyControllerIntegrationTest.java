package com.in28minutes.controller;

import com.Application;
import com.in28minutes.model.Question;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Created by Admin on 2017.01.26..
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SurveyControllerIntegrationTest {
    @LocalServerPort
    private int port;

    private TestRestTemplate template = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();


    @Before
    public void setupJSONAcceptType() {
        headers.add("Authorization",createAuthenticationHeaderValue("user1","secret1"));
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    }

    @Test
    public void retrieveSurveyQuestion() throws Exception {

        String expected = "{id:Question1,description:Largest Country in the World,correctAnswer:Russia,options:[India,Russia,United States,China]}";

        ResponseEntity<String> response = template.exchange(
                createUrlWithRandomPort("/surveys/Survey1/questions/Question1"), HttpMethod.GET, new HttpEntity<>("DUMMY_DOESNT_MATTER", headers), String.class);

        JSONAssert.assertEquals(expected, response.getBody(), false);
        //Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    public void createQuestion() throws Exception {
        Question question = new Question("DOESN'T MATTER", "Smallest Number",
                "1", Arrays.asList("1", "2", "3", "4"));

        ResponseEntity<String> response = template.exchange(
                createUrlWithRandomPort("/surveys/Survey1/questions/"), HttpMethod.POST,
                new HttpEntity<>(question, headers), String.class);

        //assertThat(response.getHeaders().get(HttpHeaders.LOCATION).get(0), containsString("/surveys/Survey1/questions/"));
        Assert.assertEquals(response.getHeaders().get(HttpHeaders.LOCATION).get(0).contains("/surveys/Survey1/questions/"), true);

    }

    private String createUrlWithRandomPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    private String createAuthenticationHeaderValue(String userName, String password) {
        //"Authorization", "Basic " + Base64Encoding(userName" : "password)
        String auth = userName + ":" + password;
        byte[] encodeAuth = Base64.encode(auth.getBytes(Charset.forName("US-ASCII")));
        String headerValue = "Basic " + new String(encodeAuth);

        return headerValue;
    }


}
