import javax.swing.*;
import java.awt.*;
public class Tutorial{
    private JFrame window;
    private JPanel allPanel, background1, background2, background3, background4, next1, next2, next3, next4;

    public Tutorial(int width, int height, GameView parent){
        Image tutorialBackground1 = Toolkit.getDefaultToolkit().createImage("img/Tutorial1.png");
        Image tutorialBackground2 = Toolkit.getDefaultToolkit().createImage("img/Tutorial2.png");
        Image tutorialBackground3 = Toolkit.getDefaultToolkit().createImage("img/Tutorial3.png");
        Image tutorialBackground4 = Toolkit.getDefaultToolkit().createImage("img/Tutorial4.png");
        Image nextBackground = Toolkit.getDefaultToolkit().createImage("img/next-01.png");

        window = new JFrame();
        allPanel = new JPanel();
        background1 = new ImagePanel(tutorialBackground1, width, height);
        background2 = new ImagePanel(tutorialBackground2, width, height);
        background3 = new ImagePanel(tutorialBackground3, width, height);
        background4 = new ImagePanel(tutorialBackground4, width, height);
        next1 = new ImagePanel(nextBackground, height/6, height/12);
        next2 = new ImagePanel(nextBackground, height/6, height/12);
        next3 = new ImagePanel(nextBackground, height/6, height/12);
        next4 = new ImagePanel(nextBackground, height/6, height/12);
        
        next1.setPreferredSize(new Dimension(height/6, height/12));
        next1.setBackground(new Color(0,0,0,0));
        next2.setPreferredSize(new Dimension(height/6, height/12));
        next2.setBackground(new Color(0,0,0,0));
        next3.setPreferredSize(new Dimension(height/6, height/12));
        next3.setBackground(new Color(0,0,0,0));
        next4.setPreferredSize(new Dimension(height/6, height/12));
        next4.setBackground(new Color(0,0,0,0));
        
        background1.setLayout(new FlowLayout());
        background1.add(Box.createRigidArea(new Dimension(height/2*3, height/6)));
        background1.add(next1);
        
        background2.setLayout(new FlowLayout());
        background2.add(Box.createRigidArea(new Dimension(height/2*3, height/6)));
        background2.add(next2);
        
        background3.setLayout(new FlowLayout());
        background3.add(Box.createRigidArea(new Dimension(height/2*3, height/6)));
        background3.add(next3);
        
        background4.setLayout(new FlowLayout());
        background4.add(Box.createRigidArea(new Dimension(height/2*3, height/6)));
        background4.add(next4);
        
        allPanel.setLayout(new BorderLayout());
        allPanel.add(background1, BorderLayout.CENTER);
        
        window.add(allPanel);
        
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setUndecorated(true);
        window.setBackground(new Color(0,0,0, 0));
        window.setSize(width, height);
        window.setLocation(parent.getX(), parent.getY());
        window.setVisible(true);
    }

    public JFrame getWindow() {
        return window;
    }

    public JPanel getAllPanel() {
        return allPanel;
    }

    public JPanel getBackground1() {
        return background1;
    }

    public JPanel getBackground2() {
        return background2;
    }

    public JPanel getBackground3() {
        return background3;
    }

    public JPanel getBackground4() {
        return background4;
    }

    public JPanel getNext1() {
        return next1;
    }

    public JPanel getNext2() {
        return next2;
    }

    public JPanel getNext3() {
        return next3;
    }

    public JPanel getNext4() {
        return next4;
    }

    
}
