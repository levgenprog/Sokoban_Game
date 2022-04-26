import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Images {
    BufferedImage robotFront;
    BufferedImage robotBack;
    BufferedImage robotRight;
    BufferedImage robotLeft;
    BufferedImage wall;
    BufferedImage ground;
    BufferedImage goal;
    BufferedImage box;
    BufferedImage setBox;

    Images() throws IOException {
        robotFront = ImageIO.read(new File("images/Robot.png"));
        robotBack = ImageIO.read(new File("images/RobotU.png"));
        robotRight = ImageIO.read(new File("images/RobotR.png"));
        robotLeft = ImageIO.read(new File("images/RobotL.png"));
        wall = ImageIO.read(new File("images/Wall.png"));
        ground = ImageIO.read(new File("images/Ground.png"));
        goal = ImageIO.read(new File("images/Goal.png"));
        box = ImageIO.read(new File("images/BoxBlue.png"));
        setBox = ImageIO.read(new File("images/BoxRed.png"));
    }

}
