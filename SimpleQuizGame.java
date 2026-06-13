import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.*;

// ============================================================================
// 1. MAIN UI CLASS (Must match the filename SimpleQuizGame.java)
// ============================================================================
public class SimpleQuizGame extends JFrame {

    private Quiz quiz;

    // GUI components
    private JLabel titleLabel;
    private JLabel questionLabel;
    private JLabel progressLabel;
    private JLabel scoreLabel;
    private JLabel feedbackLabel;
    private JLabel timerLabel;

    // Timer logic elements
    private Timer countdownTimer;
    private int timeLeft;
    private static final int TIME_LIMIT = 15; 

    private JScrollPane summaryScrollPane;
    private JLabel summaryLabel;

    private JRadioButton[] choiceButtons;
    private ButtonGroup choiceGroup;

    private JButton submitButton;
    private JButton restartButton;
    
    private JPanel centerPanel;
    private JPanel choicesBox;

    /** Constructor initializes quiz and UI. */
    public SimpleQuizGame() {
        quiz = new Quiz();
        createUI();
        initTimerLogic();
        loadNextQuestion();
    }

    private void initTimerLogic() {
        countdownTimer = new Timer(1000, e -> {
            timeLeft--;
            timerLabel.setText("Time Left: " + timeLeft + "s");
            
            if (timeLeft <= 5) {
                timerLabel.setForeground(Color.RED);
            } else {
                timerLabel.setForeground(Color.BLACK);
            }

            if (timeLeft <= 0) {
                handleTimeOut();
            }
        });
    }

    private void createUI() {
        this.setTitle("Simple Quiz Game with Timer");
        this.setSize(650, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout(10, 20));

        // NORTH PANEL
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));

        titleLabel = new JLabel("Simple Quiz Game");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        questionLabel = new JLabel("Question will appear here");
        questionLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        progressLabel = new JLabel("Question 1 of 10");
        progressLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        progressLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        timerLabel = new JLabel("Time Left: " + TIME_LIMIT + "s");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        northPanel.add(Box.createVerticalStrut(10));
        northPanel.add(titleLabel);
        northPanel.add(Box.createVerticalStrut(10));
        northPanel.add(timerLabel);
        northPanel.add(Box.createVerticalStrut(15));
        northPanel.add(questionLabel);
        northPanel.add(progressLabel);

        // CENTER PANEL
        centerPanel = new JPanel(new GridBagLayout());
        choicesBox = new JPanel(new GridLayout(4, 1, 0, 10));
        choicesBox.setPreferredSize(new Dimension(350, 200));

        choiceButtons = new JRadioButton[4];
        choiceGroup = new ButtonGroup();

        for (int i = 0; i < 4; i++) {
            choiceButtons[i] = new JRadioButton();
            choiceButtons[i].setFont(new Font("Arial", Font.PLAIN, 16));
            choiceGroup.add(choiceButtons[i]);
            choicesBox.add(choiceButtons[i]);
        }

        centerPanel.add(choicesBox);
        
        summaryLabel = new JLabel();
        summaryLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        summaryScrollPane = new JScrollPane(summaryLabel);
        summaryScrollPane.setPreferredSize(new Dimension(500, 280));
        summaryScrollPane.setBorder(BorderFactory.createEmptyBorder());

        // SOUTH PANEL
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));

        feedbackLabel = new JLabel(" ");
        feedbackLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setVisible(false);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        submitButton = new JButton("Submit");
        restartButton = new JButton("Restart Quiz");
        restartButton.setEnabled(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.add(submitButton);
        buttonPanel.add(restartButton);

        southPanel.add(feedbackLabel);
        southPanel.add(scoreLabel);
        southPanel.add(Box.createVerticalStrut(10));
        southPanel.add(buttonPanel);
        southPanel.add(Box.createVerticalStrut(15));

        this.add(northPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(southPanel, BorderLayout.SOUTH);

        submitButton.addActionListener(e -> handleSubmit());
        restartButton.addActionListener(e -> restartQuiz());
    }

    private void loadNextQuestion() {
        if (quiz.hasMoreQuestions()) {
            Question current = quiz.getCurrentQuestion();
            questionLabel.setText(current.getQuestion());

            int qNum = quiz.getCurrentIndex() + 1;
            progressLabel.setText("Question " + qNum + " of " + quiz.getTotalQuestions());

            String[] choices = current.getChoices();
            choiceGroup.clearSelection(); 
            
            for (int i = 0; i < 4; i++) {
                choiceButtons[i].setText(choices[i]);
                choiceButtons[i].setEnabled(true);
            }

            feedbackLabel.setText(" ");
            
            timeLeft = TIME_LIMIT;
            timerLabel.setText("Time Left: " + timeLeft + "s");
            timerLabel.setForeground(Color.BLACK);
            countdownTimer.start();

        } else {
            endQuiz();
        }
    }

    private void handleSubmit() {
        Question current = quiz.getCurrentQuestion();
        if (current == null) return;

        String userAnswer = null;
        for (JRadioButton btn : choiceButtons) {
            if (btn.isSelected()) {
                userAnswer = btn.getText();
                break;
            }
        }

        if (userAnswer == null) {
            feedbackLabel.setText("Please select an answer before submitting.");
            return;
        }

        countdownTimer.stop(); 
        quiz.checkAnswer(userAnswer);
        loadNextQuestion();
    }

    private void handleTimeOut() {
        countdownTimer.stop();
        feedbackLabel.setText("Time's up!");
        quiz.checkAnswer(null); 
        
        Timer pause = new Timer(1000, e -> loadNextQuestion());
        pause.setRepeats(false);
        pause.start();
    }

    private void endQuiz() {
        countdownTimer.stop();
        timerLabel.setText("Game Over");

        int total = quiz.getTotalQuestions();
        int score = quiz.getScore();
        int percent = (int) ((score / (double) total) * 100);

        questionLabel.setText("Quiz Finished!");
        progressLabel.setText("Results Summary");

        centerPanel.remove(choicesBox);
        centerPanel.add(summaryScrollPane);

        StringBuilder summary = new StringBuilder("<html><div style='text-align:center; width:380px;'>");
        String[] userAnswers = quiz.getUserAnswers();

        for (int i = 0; i < total; i++) {
            Question q = quiz.getQuestionAt(i);
            String user = userAnswers[i];
            String correct = q.getAnswer();

            summary.append("<b>Q").append(i + 1).append(":</b> ")
                   .append(q.getQuestion()).append("<br>");
            summary.append("Your answer: ").append(user).append("<br>");
            summary.append("Correct answer: ").append(correct).append("<br>");

            if (user.equalsIgnoreCase(correct)) {
                summary.append("<span style='color:green;'>Correct</span><br><br>");
            } else {
                summary.append("<span style='color:red;'>Incorrect</span><br><br>");
            }
        }

        summary.append("<br><b>Final Score: ").append(score)
               .append(" / ").append(total)
               .append(" (").append(percent).append("%)</b><br>");
        summary.append("</div></html>");

        summaryLabel.setText(summary.toString());
        feedbackLabel.setText(" ");

        scoreLabel.setText("Score: " + score);
        scoreLabel.setVisible(true);

        submitButton.setEnabled(false);
        restartButton.setEnabled(true);
        
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    private void restartQuiz() {
        quiz.reset();
        scoreLabel.setVisible(false);
        feedbackLabel.setText(" ");
        submitButton.setEnabled(true);
        restartButton.setEnabled(false);

        centerPanel.remove(summaryScrollPane);
        centerPanel.add(choicesBox);

        for (JRadioButton btn : choiceButtons) {
            btn.setEnabled(true);
        }
        
        centerPanel.revalidate();
        centerPanel.repaint();

        loadNextQuestion();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SimpleQuizGame app = new SimpleQuizGame();
            app.setVisible(true);
        });
    }
}

// ============================================================================
// 2. QUIZ LOGIC CLASS (Notice: No "public" keyword here)
// ============================================================================
class Quiz {

    private Question[] questions;
    private int currentIndex;
    private int score;
    private String[] userAnswers;

    public Quiz() {
        questions = new Question[] {
            new Question("What is the capital of France?", "Paris", new String[]{"Paris", "London", "Berlin", "Rome"}),
            new Question("What is 5 + 7?", "12", new String[]{"10", "11", "12", "13"}),
            new Question("What programming language are we learning?", "Java", new String[]{"Python", "C++", "Java", "JavaScript"}),
            new Question("What is the largest planet in our solar system?", "Jupiter", new String[]{"Earth", "Mars", "Jupiter", "Saturn"}),
            new Question("How many continents are there on Earth?", "7", new String[]{"5", "6", "7", "8"}),
            new Question("What is the boiling point of water in Celsius?", "100", new String[]{"90", "100", "120", "212"}),
            new Question("Which ocean is the largest?", "Pacific", new String[]{"Atlantic", "Indian", "Arctic", "Pacific"}),
            new Question("What component stores data long-term in a computer? (Abbreviation)", "SSD", new String[]{"RAM", "CPU", "SSD", "GPU"}),
            new Question("Who wrote the play 'Romeo and Juliet'?", "William Shakespeare", new String[]{"Charles Dickens", "William Shakespeare", "Mark Twain", "Jane Austen"}),
            new Question("What is the chemical symbol for water?", "H2O", new String[]{"CO2", "H2O", "O2", "NaCl"})
        };
        reset();
    }

    public void reset() {
        currentIndex = 0;
        score = 0;
        userAnswers = new String[questions.length];
        
        List<Question> questionList = Arrays.asList(questions);
        Collections.shuffle(questionList);
        questions = questionList.toArray(new Question[0]);

        for (Question q : questions) {
            q.shuffleChoices();
        }
    }

    public Question getCurrentQuestion() {
        if (currentIndex < questions.length) {
            return questions[currentIndex];
        }
        return null;
    }

    public void checkAnswer(String userAnswer) {
        Question current = getCurrentQuestion();
        if (current != null) {
            userAnswers[currentIndex] = (userAnswer != null) ? userAnswer : "Timed Out / Skipped";
            if (userAnswer != null && userAnswer.equalsIgnoreCase(current.getAnswer())) {
                score++;
            }
            currentIndex++;
        }
    }

    public boolean hasMoreQuestions() {
        return currentIndex < questions.length;
    }

    public int getScore() {
        return score;
    }

    public int getTotalQuestions() {
        return questions.length;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public Question getQuestionAt(int index) {
        return questions[index];
    }

    public String[] getUserAnswers() {
        return userAnswers;
    }
}

// ============================================================================
// 3. QUESTION DATA CLASS (Notice: No "public" keyword here)
// ============================================================================
class Question {

    private String questionText;
    private String correctAnswer;
    private String[] choices;

    public Question(String questionText, String correctAnswer, String[] choices) {
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.choices = choices;
    }

    public String getQuestion() {
        return questionText;
    }

    public String getAnswer() {
        return correctAnswer;
    }

    public String[] getChoices() {
        return choices;
    }

    public void shuffleChoices() {
        if (choices != null) {
            List<String> choiceList = Arrays.asList(choices);
            Collections.shuffle(choiceList);
            choices = choiceList.toArray(new String[0]);
        }
    }
}
