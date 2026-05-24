package model;

/**
 * The Quiz class manages the quiz flow, including
 * tracking questions, checking answers, and updating score.
 */
public class Quiz {

    private Question[] questions;
    private int currentIndex;
    private int score;

    /**
     * Constructor initializes the quiz with sample questions.
     */
    public Quiz() {
        questions = new Question[] {
            new Question("What is the capital of France?", "Paris"),
            new Question("What is 5 + 7?", "12"),
            new Question("What programming language are we learning?", "Java")
        };

        currentIndex = 0;
        score = 0;
    }

    /**
     * Returns the current question object.
     */
    public Question getCurrentQuestion() {
        if (currentIndex < questions.length) {
            return questions[currentIndex];
        }
        return null;
    }

    /**
     * Checks the user's answer and updates score.
     */
    public void checkAnswer(String userAnswer) {
        Question current = getCurrentQuestion();

        if (current != null) {
            if (userAnswer != null && userAnswer.equalsIgnoreCase(current.getAnswer())) {
                score++; // correct answer
            }
            currentIndex++; // move to next question
        }
    }

    /**
     * Returns true if there are more questions.
     */
    public boolean hasMoreQuestions() {
        return currentIndex < questions.length;
    }

    /**
     * Returns the user's current score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Returns the total number of questions.
     */
    public int getTotalQuestions() {
        return questions.length;
    }
}
