import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
//import java.io.IOException;
//import java.net.URL;

public class Login {

    Login() {
        JTextField tfname;
        // La fenetre
        JFrame login = new JFrame();
        login.setSize(1200, 500);
        login.setLocation(50, 90);

        // Le panel
        JPanel pan = new JPanel();
        pan.setBackground(new Color(190, 227, 247));
        pan.setLayout(null);

        // l'image
        Icon img = new ImageIcon("map.jpg");
        JLabel image = new JLabel(img);
        image.setBounds(0, 0, 600, 450);

        // le Texte
        JLabel heading = new JLabel("Dive into the world of geography!");
        heading.setBounds(800, 60, 300, 45);
        heading.setFont(new Font("Mongolian Baiti", Font.BOLD, 18));
        heading.setForeground(new Color(30, 144, 254));

        JLabel name = new JLabel("What's your name, champion?");
        name.setBounds(800, 150, 300, 30);
        name.setFont(new Font("Mongolian Baiti", Font.BOLD, 20));
        name.setForeground(new Color(30, 144, 254));

        tfname = new JTextField();
        tfname.setBounds(790, 200, 300, 25);
        tfname.setFont(new Font("Time New Roman", Font.BOLD, 20));

        JButton button = new JButton("Login");
        button.setBounds(980, 270, 120, 25);
        button.setBackground(new Color(30, 144, 254));
        button.setForeground(Color.WHITE);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String name = tfname.getText();
                login.setVisible(false);
                new Welcome(name);
            }
        });
        button.setFocusPainted(false);
        button.setBorderPainted(false);

        JButton back = new JButton("Quit");
        back.setBounds(780, 270, 120, 25);
        back.setBackground(new Color(30, 144, 254));
        back.setForeground(Color.WHITE);
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                login.setVisible(false);
            }
        });
        back.setFocusPainted(false);
        back.setBorderPainted(false);

        pan.add(image);
        pan.add(heading);
        pan.add(name);
        pan.add(tfname);
        pan.add(button);
        pan.add(back);
        login.add(pan);
        login.setVisible(true);
    }

    public static void main(String[] args) {
        new Login();
    }
}
