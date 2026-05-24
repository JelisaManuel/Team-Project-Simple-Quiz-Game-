package model;

/**
 * The Question class stores a single quiz question
 * and its correct answer. It provides access methods
 * for retrieving the question text and answer.
 */
public class Question {

    private String questionText;
    private String correctAnswer;

    /**
     * Constructor initializes the question and answer.
     */
    public Question(String questionText, String correctAnswer) {
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
    }

    /**
     * Returns the question text.
     */
    public String getQuestion() {
        return questionText;
    }

    /**
     * Returns the correct answer.
     */
    public String getAnswer() {
        return correctAnswer;
    }
}
