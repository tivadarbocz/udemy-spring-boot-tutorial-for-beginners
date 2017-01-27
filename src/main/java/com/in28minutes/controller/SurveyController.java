package com.in28minutes.controller;

import com.in28minutes.model.Question;
import com.in28minutes.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Created by Admin on 2017.01.25..
 */
@RestController
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    @GetMapping("/surveys/{surveyId}/questions")
    public List<Question> retrieveQuestionsForSurvey(@PathVariable String surveyId){
        return surveyService.retrieveQuestions(surveyId);
    }

    @GetMapping("/surveys/{surveyId}/questions/{questionId}")
    public Question retrieveDetailesForQuestion(@PathVariable String surveyId,@PathVariable String questionId){
        return surveyService.retrieveQuestion(surveyId, questionId);
    }

    @PostMapping("/surveys/{surveyId}/questions")
    ResponseEntity<?> addQuestionToSurvey(@PathVariable String surveyId, @RequestBody Question question) {

        Question createdQuestion = surveyService.addQuestion(surveyId, question);

        if (createdQuestion == null) {
            return ResponseEntity.noContent().build();
        }


        // URI -> /surveys/{surveyId}/questions/{questionId}
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(createdQuestion.getId()).toUri();

        return ResponseEntity.created(location).build();

    }
}
