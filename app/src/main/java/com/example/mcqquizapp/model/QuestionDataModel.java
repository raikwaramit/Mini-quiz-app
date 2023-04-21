package com.example.mcqquizapp.model;

import java.util.List;

/**
 * DataModel Class for holding Individual Data Of question fetched.
 *
 * @Property id Id of the question.
 * @Property question Question text of the given question.
 * @Property options Multiple choice options for the question.
 * @Property correctOption Correct option out of all the options.
 * @Property userAnswer Answer by the user.
 * @Property bookmarkStatus Question bookmark status if question is saved for the later recheck.
 */
public class QuestionDataModel {
    private final int id;
    private final String question;
    private final List<String> options;
    private final String correctOption;
    private String userAnswer = "";
    public boolean isBookmarked = false;

    // Constructor for initialising the object with fetched data.
    public QuestionDataModel(int id, String question, List<String> options, String correctOption) {
        this.id = id;
        this.question = question;
        this.options = options;
        this.correctOption = correctOption;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    // Returns boolean whether question is answered by the user.
    public boolean isQuestionAnswered(){
        return userAnswer.isEmpty();
    }


    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getCorrectOption() {
        return correctOption;
    }
}
