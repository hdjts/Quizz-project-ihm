import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class QuizFacile extends JFrame {

    private static final String SOUND_CORRECT_FILE = "videoplayback.wav";
    private static final String SOUND_INCORRECT_FILE = "WRONG-ANSWER-SOUND-EFFECT.wav";

    private Clip correctClip;
    private Clip incorrectClip;

    private JLabel quizLabel;
    private JLabel questionLabel;
    private JButton[] answerButtons;
    private JButton nextButton;
    private JButton pauseButton;
    private JButton resetButton;
    private JLabel timerLabel;

    private int currentQuestionIndex;
    private int score;

    private String[][] questions = {
        { "What is the capital of Algeria?", "Casablanca", "Tunis", "Algiers", "Rabat", "Algiers" },
        { "Which ocean is to the west of Algeria?", "Atlantic Ocean", "Pacific Ocean", "Mediterranean Sea",
                "Red Sea", "Atlantic Ocean" },
        { "In which continent is Egypt located?", "Asia", "Africa", " Europe",
                " South America", "Africa" },
        { "Name the country where the pyramids of Giza are found.", " Mexico", "Egypt", "Greece",
                "China", "Egypt" },
        { "What is the official language of Japan?", "Mandarin", "Korean", "Japanese",
                "Thai", "Japanese" },
    };

    private Timer timer;
    private int secondsLeft;

    public QuizFacile() {
        setTitle("GEO Quiz");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(190, 227, 247)); 

        initializeComponents();
        startQuiz();

        try {
            AudioInputStream correctStream = AudioSystem.getAudioInputStream(new File(SOUND_CORRECT_FILE));
            correctClip = AudioSystem.getClip();
            correctClip.open(correctStream);

            AudioInputStream incorrectStream = AudioSystem.getAudioInputStream(new File(SOUND_INCORRECT_FILE));
            incorrectClip = AudioSystem.getClip();
            incorrectClip.open(incorrectStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void initializeComponents() {
        JPanel quizPanel = new JPanel();
        quizPanel.setLayout(new BoxLayout(quizPanel, BoxLayout.Y_AXIS));
        quizPanel.setBackground(new Color(190, 227, 247));

        quizLabel = new JLabel("[QUIZ]");
        quizLabel.setFont(new Font("Arial", Font.BOLD, 30));
        quizLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        quizLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        quizPanel.add(quizLabel);

        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        quizPanel.add(questionLabel);

        answerButtons = new JButton[4];
        JPanel answerPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        answerPanel.setBackground(new Color(190, 227, 247));
        for (int i = 0; i < 4; i++) {
            answerButtons[i] = new JButton();
            answerButtons[i].setFont(new Font("Arial", Font.PLAIN, 18));
            answerButtons[i].addActionListener(new AnswerButtonListener());
            answerPanel.add(answerButtons[i]);
        }
        quizPanel.add(answerPanel);

        timerLabel = new JLabel();
        timerLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        quizPanel.add(timerLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(190, 227, 247));

        resetButton = new JButton("Reset");
        resetButton.setBounds(980, 270, 120, 25);
        resetButton.setBackground(new Color(30, 144, 254));
        resetButton.addActionListener(new ResetButtonListener());
        buttonPanel.add(resetButton);

        pauseButton = new JButton("Pause");
        pauseButton.setBounds(980, 270, 120, 25);
        pauseButton.setBackground(new Color(30, 144, 254));
        pauseButton.addActionListener(new PauseButtonListener());
        buttonPanel.add(pauseButton);


        nextButton = new JButton("Next");
        nextButton.addActionListener(new NextButtonListener());
        buttonPanel.add(nextButton);

        quizPanel.add(buttonPanel);

        add(quizPanel, BorderLayout.CENTER);
    }

    private void startQuiz() {
        currentQuestionIndex = 0;
        score = 0;
        secondsLeft = 10;
        initializeTimer();
        showQuestion();
    }

    private void initializeTimer() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secondsLeft--;
                updateTimerLabel();
                if (secondsLeft < 0) {
                    timer.stop();
                    handleNextQuestion();
                }
            }
        });
    }

    private void updateTimerLabel() {
        timerLabel.setText("Time remaining: " + secondsLeft + " seconds");
    }

    private void showQuestion() {
        timer.start();
        updateTimerLabel();

        String[] currentQuestion = questions[currentQuestionIndex];
        quizLabel.setText("[QUIZ]");
        int questionNo = currentQuestionIndex + 1;
        int totalQuestions = questions.length;
        questionLabel.setText("<html><div style='text-align: center;'>" + "Question " + questionNo + " of "
                + totalQuestions + ": " + currentQuestion[0] + "</div></html>");
        for (int i = 0; i < 4; i++) {
            answerButtons[i]
                    .setText("<html><div style='text-align: center;'>" + currentQuestion[i + 1] + "</div></html>");
            answerButtons[i].setEnabled(true);
            answerButtons[i].setBackground(new Color(30, 144, 254));
            answerButtons[i].setForeground(Color.BLACK);
            answerButtons[i].setOpaque(true);
            answerButtons[i].setBorderPainted(true);
        }

        nextButton.setEnabled(false);
    }

    private void showScore() {
        quizLabel.setText("[QUIZ]");
        timerLabel.setText("");

        String message;
        if (score > 0) {
            double percentage = ((double) score / questions.length) * 100;
            message = "Congratulations! You've scored " + score + " out of " + (questions.length) +
                    " (" + new DecimalFormat("##.##").format(percentage) + "%).";

            // Check if the user scored 100%
            if (percentage == 100) {
                playFinalScoreSound();
                // Trigger the confetti effect for 100% score
                startConfettiEffect();
            }
        } else {
            message = "Good luck next time. You didn't score any points.";
        }

        // Display message with custom "Try Again" button
        Object[] options = { "Play Again" };
        int result = JOptionPane.showOptionDialog(this, message, "Quiz Finished",
                JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (result == JOptionPane.YES_OPTION) {
            SwingUtilities.invokeLater(() -> {
                getContentPane().removeAll();
                initializeComponents();
                revalidate();
                repaint();
                startQuiz();
            });
        }
    }

    private void playFinalScoreSound() {
        // You can implement the logic to play a specific sound for the final score here
        // For example, you can use a new Clip and AudioInputStream similar to the
        // existing ones.
        // You can adjust the file path accordingly.
        try {
            AudioInputStream finalScoreStream = AudioSystem.getAudioInputStream(new File(
                    "Congratulations-clapping-and-cheering-video-of-applause-sound-effects-with-fireworks.wav"));
            Clip finalScoreClip = AudioSystem.getClip();
            finalScoreClip.open(finalScoreStream);
            finalScoreClip.setFramePosition(0);
            finalScoreClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void startConfettiEffect() {
        ConfettiPanel confettiPanel = new ConfettiPanel();
        add(confettiPanel);
        confettiPanel.animate();
    }

    private void resetQuiz() {
        remove(quizLabel);
        remove(questionLabel);
        for (int i = 0; i < 4; i++) {
            remove(answerButtons[i]);
        }

        remove(timerLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(nextButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(resetButton);

        add(quizLabel, BorderLayout.NORTH);
        add(questionLabel, BorderLayout.CENTER);
        add(answerButtons[0], BorderLayout.WEST);
        add(answerButtons[1], BorderLayout.EAST);
        add(answerButtons[2], BorderLayout.SOUTH);
        add(answerButtons[3], BorderLayout.SOUTH);
        add(timerLabel, BorderLayout.SOUTH);
        add(buttonPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();

        startQuiz();
    }

    private void handleNextQuestion() {
        timer.stop();
        if (currentQuestionIndex < questions.length - 1) {
            currentQuestionIndex++;
            secondsLeft = 10;
            showQuestion();
        } else {
            showScore();
            currentQuestionIndex = 0;
            score = 0;
            nextButton.setText("Next");
        }
    }

    private class AnswerButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            timer.stop();
            JButton selectedButton = (JButton) e.getSource();
            String selectedAnswer = getButtonTextWithoutHtml(selectedButton);

            for (int i = 0; i < 4; i++) {
                answerButtons[i].setEnabled(false);
            }

            String correctAnswer = questions[currentQuestionIndex][questions[currentQuestionIndex].length - 1];

            if (selectedAnswer.equals(correctAnswer)) {
                selectedButton.setBackground(Color.GREEN);
                score++;
                playCorrectSound();
            } else {
                selectedButton.setBackground(Color.RED);

                for (JButton button : answerButtons) {
                    if (getButtonTextWithoutHtml(button).equals(correctAnswer)) {
                        button.setBackground(Color.GREEN);
                        break;
                    }
                }
                playIncorrectSound();
            }

            nextButton.setEnabled(true);
        }

        private void playCorrectSound() {
            if (correctClip != null) {
                correctClip.setFramePosition(0);
                correctClip.start();
            }
        }

        private void playIncorrectSound() {
            if (incorrectClip != null) {
                incorrectClip.setFramePosition(0);
                incorrectClip.start();
            }
        }

        private String getButtonTextWithoutHtml(JButton button) {
            return button.getText().replaceAll("\\<.*?\\>", "");
        }
    }

    private class NextButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            handleNextQuestion();
        }
    }

    private class PauseButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            timer.stop();
            JOptionPane.showMessageDialog(QuizFacile.this, "Quiz paused. Click OK to resume.", "Paused",
                    JOptionPane.INFORMATION_MESSAGE);
            timer.start();
        }
    }

    private class ResetButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            getContentPane().removeAll();
            initializeComponents();
            revalidate();
            repaint();
            startQuiz();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GeoQuizSwing().setVisible(true));
    }
}

class ConfettiPanel extends JPanel {
    private static final int NUM_CONFETTI = 100;
    private final Confetti[] confetti;

    public ConfettiPanel() {
        setLayout(null);
        confetti = new Confetti[NUM_CONFETTI];
        for (int i = 0; i < NUM_CONFETTI; i++) {
            confetti[i] = new Confetti();
        }
    }

    public void animate() {
        Timer timer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Confetti c : confetti) {
                    c.move();
                }
                repaint();
            }
        });
        timer.setRepeats(true);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Confetti c : confetti) {
            c.draw(g);
        }
    }
}

class Confetti {
    private static final int SIZE = 10;
    private int x, y;
    private int speedX, speedY;
    private Color color;

    public Confetti() {
        x = (int) (Math.random() * 600); // Adjust the width of the frame
        y = (int) (Math.random() * 400); // Adjust the height of the frame
        speedX = (int) (Math.random() * 5) + 1;
        speedY = (int) (Math.random() * 5) + 1;
        color = new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));
    }

    public void move() {
        x += speedX;
        y += speedY;
        if (x < 0 || x > 600) { // Adjust the width of the frame
            speedX *= -1;
        }
        if (y < 0 || y > 400) { // Adjust the height of the frame
            speedY *= -1;
        }
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, SIZE, SIZE);
    }
}
