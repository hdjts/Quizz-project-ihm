import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Niveau {
    Niveau() {
        JFrame niveau = new JFrame();
        niveau.setSize(800, 650);
        niveau.setLocation(300, 30);

        JPanel pan = new JPanel();
        pan.setBackground(new Color(190, 227, 247));
        pan.setLayout(null);

        JLabel heading = new JLabel("<html>It's time to show off your superpowers!<br></html>");
        heading.setBounds(245, 60, 800, 50);
        heading.setFont(new Font("Mongolian Baiti", Font.BOLD, 18));
        heading.setForeground(new Color(30, 144, 254));

        JLabel heading2 = new JLabel("<html><br>Choose a level that challenges you</html>");
        heading2.setBounds(255, 80, 800, 50);
        heading2.setFont(new Font("Mongolian Baiti", Font.BOLD, 18));
        heading2.setForeground(new Color(30, 144, 254));

        // bouton Facile
        JButton facileButton = new JButton("Easy");
        facileButton.setBackground(new Color(30, 144, 254));
        facileButton.setForeground(Color.WHITE);
        facileButton.setFocusPainted(false);
        facileButton.setBorderPainted(false);
        facileButton.setBounds(325, 250, 120, 25);
        facileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                niveau.setVisible(false);
                new QuizFacile().setVisible(true);
            }
        });

        // Bouton Moyen
        JButton moyenButton = new JButton("Medium");
        moyenButton.setBackground(new Color(30, 144, 254));
        moyenButton.setForeground(Color.WHITE);
        moyenButton.setFocusPainted(false);
        moyenButton.setBorderPainted(false);
        moyenButton.setBounds(325, 300, 120, 25);
        moyenButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                niveau.setVisible(false);
                new QuizMoyen().setVisible(true);
            }
        });

        // Bouton Dificile
        JButton difficileButton = new JButton("Hard");
        difficileButton.setBackground(new Color(30, 144, 254));
        difficileButton.setForeground(Color.WHITE);
        difficileButton.setFocusPainted(false);
        difficileButton.setBorderPainted(false);
        difficileButton.setBounds(325, 350, 120, 25);
        difficileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                niveau.setVisible(false);
                new GeoQuizSwing().setVisible(true);
            }
        });

        pan.add(heading);
        pan.add(heading2);
        pan.add(facileButton);
        pan.add(moyenButton);
        pan.add(difficileButton);
        niveau.add(pan);
        niveau.setVisible(true);
    }

    public static void main(String[] args) {
        new Niveau();

    }

}
