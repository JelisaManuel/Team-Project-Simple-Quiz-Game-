package ui;

import model.Question;
import model.Quiz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The App class creates the GUI for the quiz game.
 * It displays questions, accepts user input, and shows results.
 */
public class App extends JFrame {

    private Quiz quiz;
    private JLabel questionLabel;
    private JTextField answerField;
    private JButton submitButton;
    private JLabel feedbackLabel;
    private JLabel scoreLabel;

    /**
     * Constructor initializes the quiz and GUI.
     */
    public App() {
        quiz = new Quiz();
        createUI();
        loadNextQuestion();
    }

    /**
     * Builds the GUI layout and components.
     */
    private void createUI() {
        setTitle("Simple Quiz Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 250);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Question display panel
        JPanel questionPanel = new JPanel(new BorderLayout());
        questionLabel = new JLabel("Question will appear here");
        questionLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        questionPanel.add(questionLabel, BorderLayout.CENTER);

        // Answer input panel
        JPanel answerPanel = new JPanel(new FlowLayout());
        answerPanel.add(new JLabel("Your answer: "));
        answerField = new JTextField(20);
        answerPanel.add(answerField);

        // Submit button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        submitButton = new JButton("Submit");
        buttonPanel.add(submitButton);

        // Feedback + score panel
        JPanel statusPanel = new JPanel(new GridLayout(2, 1));
        feedbackLabel = new JLabel(" ");
        scoreLabel = new JLabel("Score: 0");
        statusPanel.add(feedbackLabel);
        statusPanel.add(scoreLabel);

        // Add panels to window
        add(questionPanel, BorderLayout.NORTH);
        add(answerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(statusPanel, BorderLayout.WEST);

        // Button click event
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSubmit();
            }
        });
    }

    /**
     * Loads the next question or ends the quiz.
     */
    private void loadNextQuestion() {
        if (quiz.hasMoreQuestions()) {
            Question current = quiz.getCurrentQuestion();
            questionLabel.setText(current.getQuestion());
            answerField.setText("");
            feedbackLabel.setText(" ");
        } else {
            endQuiz();
        }
    }

    /**
     * Handles the submit button logic.
     */
    private void handleSubmit() {
        String userAnswer = answerField.getText().trim();

        if (userAnswer.isEmpty()) {
            feedbackLabel.setText("Please enter an answer.");
            return;
        }

        Question current = quiz.getCurrentQuestion();
        String correct = current.getAnswer();

        // Display feedback
        if (userAnswer.equalsIgnoreCase(correct)) {
            feedbackLabel.setText("Correct!");
        } else {
            feedbackLabel.setText("Incorrect. Correct answer: " + correct);
        }

        quiz.checkAnswer(userAnswer);
        scoreLabel.setText("Score: " + quiz.getScore());
        loadNextQuestion();
    }

    /**
     * Displays final score and disables input.
     */
    private void endQuiz() {
        questionLabel.setText("Quiz Finished!");
        answerField.setEnabled(false);
        submitButton.setEnabled(false);
        feedbackLabel.setText("Final Score: " + quiz.getScore() + " / " + quiz.getTotalQuestions());
    }

    /**
     * Main method launches the GUI.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            App app = new App();
            app.setVisible(true);
        });
    }
}