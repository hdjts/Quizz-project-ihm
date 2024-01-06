import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Welcome {
    String name;

    Welcome(String name) {
        this.name = name;
        JFrame welcome = new JFrame();
        welcome.setSize(800, 650);
        welcome.setLocation(300, 30);

        JPanel pan = new JPanel();
        pan.setBackground(new Color(190, 227, 247));
        pan.setLayout(null);

        JLabel heading = new JLabel("Welcome " + name);
        heading.setBounds(250, 280, 700, 50);
        heading.setFont(new Font("Mongolian Baiti", Font.BOLD, 40));
        heading.setForeground(new Color(30, 144, 254));

        JButton start = new JButton("Start !");
        start.setBounds(450, 350, 120, 25);
        start.setBackground(new Color(30, 144, 254));
        start.setForeground(Color.WHITE);
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                welcome.setVisible(false);
                new Niveau();
            }
        });
        start.setFocusPainted(false);
        start.setBorderPainted(false);

        JButton back = new JButton("Back");
        back.setBounds(200, 350, 120, 25);
        back.setBackground(new Color(30, 144, 254));
        back.setForeground(Color.WHITE);
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                welcome.setVisible(true);
                new Login();
            }
        });
        back.setFocusPainted(false);
        back.setBorderPainted(false);
        pan.add(heading);
        pan.add(back);
        pan.add(start);
        welcome.add(pan);
        welcome.setVisible(true);

    }

    public static void main(String[] args) {
        new Welcome("User");
    }
}
