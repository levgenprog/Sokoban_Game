import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

class CanvasRobot extends JPanel {
    private final FieldModel field;
    private final RobotModel robot;
    Images images;

    public CanvasRobot(FieldModel field, RobotModel robot) throws IOException {
        this.field = field;
        this.robot = robot;
        this.images = new Images();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        //Field
        int cellSize = Math.min(getWidth() / field.getCurrentMap().getWidth(),
                getHeight() / field.getCurrentMap().getHeight());
        if (cellSize > 80){
            cellSize = images.wall.getHeight();
        }
        int fieldX = field.getCurrentMap().getWidth() * cellSize;
        int fieldY = field.getCurrentMap().getHeight() * cellSize;
        int centerShiftX = (int) ((getWidth() - fieldX) / 2.0f);
        int centerShiftY = (int) ((getHeight() - fieldY) / 2.0f);
        int robotX = (centerShiftX + robot.getRobotCoordinates().x * cellSize);
        int robotY = (centerShiftY + robot.getRobotCoordinates().y * cellSize);


        for (Coordinates coordinates : Map.getAllCoordinates()) {
            int coordinateX = centerShiftX + cellSize * coordinates.x;
            int coordinateY = centerShiftY + cellSize * coordinates.y;
            switch (field.getCurrentMap().getElementAt(coordinates)){
                case '#':
                    drawImage(g, images.wall, coordinateX, coordinateY, cellSize);
                    break;
                case ' ':
                case '@':
                    drawImage(g, images.ground, coordinateX, coordinateY, cellSize);
                    break;
                case '.':
                    drawImage(g, images.ground, coordinateX, coordinateY, cellSize);
                    drawImage(g, images.goal, coordinateX, coordinateY, cellSize);
                    break;
                case '$':
                    drawImage(g, images.box, coordinateX, coordinateY, cellSize);
                    break;
                case '*':
                    drawImage(g, images.setBox, coordinateX, coordinateY, cellSize);
                    break;
            }
        }
        BufferedImage robotImage;
        switch (robot.side){
            case BACK:
                robotImage = images.robotBack;
                break;
            case LEFT:
                robotImage = images.robotLeft;
                break;
            case RIGHT:
                robotImage = images.robotRight;
                break;
            case FRONT:
            default:
                robotImage = images.robotFront;
        }
        drawImage(g, robotImage, robotX, robotY, cellSize);
    }

    private void drawImage(Graphics g, BufferedImage image, int x, int y, int size){
        int topX = x + size/2 - image.getWidth()/2;
        int topY = y + size/2 - image.getHeight()/2;
        int width = size;
        int height = size;

            if (images.goal.equals(image)) {
                width = ((int) (size * 0.5));
                height = ((int) (size * 0.5));
            } else if (images.robotBack.equals(image) || images.robotFront.equals(image)) {
                width = ((int) (size * 0.579));
                height = ((int) (size * 0.93));
            } else if (images.robotLeft.equals(image) || images.robotRight.equals(image)) {
                width = ((int) (size * 0.6563));
                height = ((int) (size * 0.93));
            }

        g.drawImage(image, topX, topY, width, height, null);
    }
}
