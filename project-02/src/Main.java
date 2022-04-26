import javax.swing.*;
import java.awt.*;



public class Main extends JFrame {
    FieldModel field;
    RobotModel robot;
    JFrame thisFrame = this;
    Coordinates robotC;



    Main() throws Exception{
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setLocationRelativeTo(null);
        field = new FieldModel(0);
        robotC = new Coordinates(field.getCurrentMap().getRobotRow(),field.getCurrentMap().getRobotCol());
        robot = new RobotModel(field, robotC);
        setTitle("Sokoban");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel main = new CanvasRobot(field, robot);
        try{
            Toolbar toolbar = new Toolbar();
            BoxLayout layout = new BoxLayout(toolbar, BoxLayout.Y_AXIS);

            toolbar.setLayout(layout);
            toolbar.paintToolbar(main, thisFrame, field, robot);

            addKeyListener(new RobotController(main, robot, field, toolbar));
            add(toolbar, "East");
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        add(main, "Center");
    }



    public static void main(String[] args) {
        try {
            new Main().setVisible(true);
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

    }
}
