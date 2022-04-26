import javax.swing.*;
import java.awt.*;
import java.io.File;

class GameButton extends JButton {
    public GameButton(String name) {
        super(name);
        setBackground(new Color(103, 227, 0));
        setForeground(new Color(125, 50, 50));
        setMinimumSize(new Dimension(130, 40));
        setMaximumSize(new Dimension(130, 40));
        setSize(130, 40);

        setFocusable(false);
    }
}

public class Toolbar extends JPanel {
    private final Dimension bigDimension = new Dimension(230, 40);
    private JLabel moves;
    private JComboBox<String> levels;
    private GameButton progress;
    private GameButton bestScore;
    private GameButton boxes;

    Toolbar() {

    }

    private void restartField(JPanel main, JFrame frame, FieldModel fieldModel, RobotModel robot) {
        int file = levels.getSelectedIndex();
        fieldModel.selectLevel(file);
        frame.setTitle("Sokoban");
        robot.setRobotCoordinates(new Coordinates(fieldModel.getCurrentMap().getRobotRow(), fieldModel.getCurrentMap().getRobotCol()));
        robot.setMoves(0);
        robot.actions.clear();
        robot.redoActions.clear();
        changeText(robot.getMoves(), fieldModel.progress, fieldModel.currentBest, fieldModel.getCurrentMap().getBoxesRemain());
        main.repaint();
    }

    public void paintToolbar(JPanel main, JFrame frame, FieldModel field, RobotModel robot) {
        levels = new JComboBox<>();
        levels.setEditable(true);
        for (File file : field.levelFiles) {
            String st = file.toString();
            String result = st.substring(st.indexOf('\\') + 1, st.indexOf('.'));
            levels.addItem(result);
        }
        levels.setFocusable(false);
        levels.setEditable(false);
        levels.setPreferredSize(bigDimension);
        levels.setMaximumSize(bigDimension);
        levels.addActionListener(event -> {
            try {
                restartField(main, frame, field, robot);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        JButton reset = new JButton("Reset (CTRL+R)");
        reset.addActionListener(event -> {
            field.restart();
            robot.setRobotCoordinates(new Coordinates(field.getCurrentMap().getRobotRow(), field.getCurrentMap().getRobotCol()));
            robot.setMoves(0);
            changeText(robot.getMoves(), field.progress, field.currentBest, field.getCurrentMap().getBoxesRemain());
            main.repaint();
        });
        reset.setFocusable(false);
        reset.setPreferredSize(bigDimension);
        reset.setMinimumSize(bigDimension);
        reset.setMaximumSize(bigDimension);
        reset.setForeground(Color.black);
        reset.setBackground(Color.orange);

        GameButton undo = new GameButton("Undo (CTRL+Z)");
        undo.addActionListener(event -> {
            robot.undo();
            changeText(robot.getMoves(), field.progress, field.currentBest, field.getCurrentMap().getBoxesRemain());
            main.repaint();
        });


        GameButton redo = new GameButton("Redo (CTRL+Y)");
        redo.addActionListener(event -> {
            robot.redo();
            changeText(robot.getMoves(), field.progress, field.currentBest, field.getCurrentMap().getBoxesRemain());
            main.repaint();
        });


        GameButton nextLevel = new GameButton("Next");
        nextLevel.addActionListener(event -> {
            field.goToNextLevel();
            frame.setTitle(Integer.toString(field.getCurrentLevel()));
            robot.setRobotCoordinates(new Coordinates(field.getCurrentMap().getRobotRow(), field.getCurrentMap().getRobotCol()));
            robot.setMoves(0);
            changeText(robot.getMoves(), field.progress, field.currentBest, field.getCurrentMap().getBoxesRemain());
            main.repaint();
        });
        nextLevel.setBackground(new Color(125, 50, 50));
        nextLevel.setForeground(new Color(103, 227, 0));


        GameButton prevLevel = new GameButton("Back");
        prevLevel.addActionListener(event -> {
            field.goToPrevLevel();
            frame.setTitle(Integer.toString(field.getCurrentLevel()));
            robot.setRobotCoordinates(new Coordinates(field.getCurrentMap().getRobotRow(), field.getCurrentMap().getRobotCol()));
            robot.setMoves(0);
            changeText(robot.getMoves(), field.progress, field.currentBest, field.getCurrentMap().getBoxesRemain());
            main.repaint();
        });
        prevLevel.setBackground(new Color(125, 50, 50));
        prevLevel.setForeground(new Color(103, 227, 0));

        progress = new GameButton(" ");
        progress.setForeground(Color.BLACK);
        progress.setPreferredSize(bigDimension);
        progress.setMinimumSize(bigDimension);
        progress.setMaximumSize(bigDimension);

        bestScore = new GameButton(" ");
        bestScore.setForeground(Color.BLACK);
        bestScore.setPreferredSize(bigDimension);
        bestScore.setMinimumSize(bigDimension);
        bestScore.setMaximumSize(bigDimension);

        boxes = new GameButton(" ");

        moves = new JLabel();
        JLabel boxLabel = new JLabel("Boxes remaining: ");
        changeText(robot.getMoves(), field.progress, field.currentBest, field.getCurrentMap().getBoxesRemain());

        add(Box.createRigidArea(new Dimension(50, 100)));
        add(levels);
        add(Box.createRigidArea(new Dimension(50, 200)));
        add(progress);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(bestScore);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(boxLabel);
        add(boxes);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(reset);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(moves);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(undo);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(redo);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(nextLevel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(prevLevel);

        main.setBackground(Color.GRAY);
        setBackground(Color.LIGHT_GRAY);
    }

    void changeText(String s, String s2, String s3, String s4) {
        moves.setText("Moves: " + s);
        moves.setSize(15, 15);
        progress.setText("Your progress is: " + s2);
        bestScore.setText(s3);
        boxes.setText(s4);
    }
}
