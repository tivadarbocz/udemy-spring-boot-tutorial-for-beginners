package com.in28minutes.controller;

import com.in28minutes.model.Question;
import com.in28minutes.service.SurveyService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Admin on 2017.01.26..
 */
@RunWith(SpringRunner.class)
//We only want to launch only SurveyVontroller instead of entire app
@WebMvcTest(value = SurveyController.class, secure = false)
public class SurveyControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;
    //Mock @Autowired
    @MockBean
    private SurveyService surveyService;

    @Test
    public void retrieveDetailesForQuestion() throws Exception {
        Question mockQuestion = new Question("Question1",
                "Largest Country in the World", "Russia", Arrays.asList(
                "India", "Russia", "United States", "China"));

        Mockito.when(surveyService.retrieveQuestion(Mockito.anyString(), Mockito.anyString())).thenReturn(mockQuestion);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/surveys/Survey1/questions/Question1").accept(MediaType.APPLICATION_JSON);
        //Make a call a service
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{id:Question1,description:Largest Country in the World,correctAnswer:Russia}";
        //Assert
        JSONAssert.assertEquals(expected, mvcResult.getResponse()
                .getContentAsString(), false);

    }

    @Test
    public void retrieveSurveyQuestions() throws Exception {
        List<Question> mockList = Arrays.asList(
                new Question("Question1", "First Alphabet", "A", Arrays.asList(
                        "A", "B", "C", "D")),
                new Question("Question2", "Last Alphabet", "Z", Arrays.asList(
                        "A", "X", "Y", "Z")));

        Mockito.when(surveyService.retrieveQuestions(Mockito.anyString())).thenReturn(mockList);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/surveys/Survey1/questions").accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        String expected = "["
                + "{id:Question1,description:First Alphabet,correctAnswer:A,options:[A,B,C,D]},"
                + "{id:Question2,description:Last Alphabet,correctAnswer:Z,options:[A,X,Y,Z]}"
                + "]";

        JSONAssert.assertEquals(expected, result.getResponse()
                .getContentAsString(), false);
    }

    @Test
    public void createSurveyQuestion() throws Exception {
        Question mockQuestion = new Question("1", "Smallest Number", "1", Arrays.asList("1", "2", "3", "4"));

        String questionJson = "{\"description\":\"Smallest Number\",\"correctAnswer\":\"1\",\"options\":[\"1\",\"2\",\"3\",\"4\"]}";
        //surveyService.addQuestion to respond back with mockQuestion
        Mockito.when(surveyService.addQuestion(Mockito.anyString(), Mockito.any(Question.class))).thenReturn(mockQuestion);

        //Send question as body to /surveys/Survey1/questions
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/surveys/Survey1/questions").accept(MediaType.APPLICATION_JSON).content(questionJson).contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        Assert.assertEquals(HttpStatus.CREATED.value(), response.getStatus());

        Assert.assertEquals("http://localhost/surveys/Survey1/questions/1", response.getHeader(HttpHeaders.LOCATION));

    }
}
