import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

class RobotController extends KeyAdapter {
    private final JPanel canvasRobot;
    private final RobotModel robotModel;
    private final FieldModel field;

    private final Toolbar toolbar;

    public RobotController(JPanel canvasRobot, RobotModel robotModel, FieldModel field, Toolbar toolbar) {
        this.canvasRobot = canvasRobot;
        this.robotModel = robotModel;
        this.field = field;
        this.toolbar = toolbar;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        if (e.isAltDown() && e.getKeyCode() == KeyEvent.VK_RIGHT){
            field.goToNextLevel();
            robotModel.setRobotCoordinates(new Coordinates(field.getCurrentMap().getRobotRow(),field.getCurrentMap().getRobotCol()));
            robotModel.setMoves(0);
            robotModel.actions.clear();
            robotModel.redoActions.clear();
        }else if(e.isAltDown() && e.getKeyCode() == KeyEvent.VK_LEFT){
            field.goToPrevLevel();
            robotModel.setRobotCoordinates(new Coordinates(field.getCurrentMap().getRobotRow(),field.getCurrentMap().getRobotCol()));
            robotModel.setMoves(0);
            robotModel.actions.clear();
            robotModel.redoActions.clear();
        }else if (e.isControlDown() && e.getKeyCode()==KeyEvent.VK_R){
            field.restart();
            robotModel.setRobotCoordinates(new Coordinates(field.getCurrentMap().getRobotRow(),field.getCurrentMap().getRobotCol()));
            robotModel.setMoves(0);
            robotModel.actions.clear();
            robotModel.redoActions.clear();
        }else if (e.isControlDown() && e.getKeyCode()==KeyEvent.VK_Z){
            robotModel.undo();
        }else if (e.isControlDown() && e.getKeyCode()==KeyEvent.VK_Y){
            robotModel.redo();
        }
        else {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                case KeyEvent.VK_W:
                    robotModel.moveUp();
                    break;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S:
                    robotModel.moveDown();
                    break;
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_A:
                    robotModel.moveLeft();
                    break;
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_D:
                    robotModel.moveRight();
                    break;
            }
        }
        toolbar.changeText(robotModel.getMoves(), field.progress, field.currentBest, field.getCurrentMap().getBoxesRemain());
        if (field.getCurrentMap().isVictory()) {
            try {
                field.markBest(robotModel);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, "Congratulations! You solved " + field.getCurrentLevel() +
                    " level and made " + robotModel.getMoves() + " moves");
            field.goToNextLevel();
            robotModel.setRobotCoordinates(new Coordinates(field.getCurrentMap().getRobotRow(), field.getCurrentMap().getRobotCol()));
            robotModel.setMoves(0);
            robotModel.actions.clear();
            robotModel.redoActions.clear();
            canvasRobot.repaint();
        }
        canvasRobot.repaint();
    }
}

