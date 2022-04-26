import java.util.ArrayList;
import java.util.Stack;

public class Map {
    private char[][] data;
    protected static int height;
    protected static int width;
    private int robotRow;
    private int robotCol;
    private static ArrayList<Coordinates> allCoordinates;
    private final ArrayList<Coordinates> goals;
    protected int boxesSet;
    private final Stack<char[][]> moveHistory;


    Map(String levelInfo) {
        String[] split = levelInfo.split("\n");
        int maxLength = 0;
        for (String line : split) {
            maxLength = Math.max(line.length(), maxLength);
        }
        height = split.length;
        width = maxLength;
        this.data = new char[width][height];
        allCoordinates = new ArrayList<>();
        ArrayList<Coordinates> boxes = new ArrayList<>();
        goals = new ArrayList<>();
        boxesSet = 0;

        for (int y = 0; y < height; y++) {
            int c = 0;
            while (split[y].charAt(c) == ' ' && c < split[y].length()) {
                c++;
            }
            for (int x = c; x < split[y].length(); x++) {
                data[x][y] = split[y].charAt(x);
                allCoordinates.add(new Coordinates(x, y));
                if (data[x][y] == '@' || data[x][y] == '+') {
                    robotRow = x;
                    robotCol = y;
                    data[x][y] = ' ';
                } else if (data[x][y] == '.' || data[x][y] == '*') {
                    goals.add(new Coordinates(x, y));
                }
                if (data[x][y] == '$' || data[x][y] == '*') {
                    boxes.add(new Coordinates(x, y));
                }
                if (data[x][y] == '*') {
                    boxesSet++;
                }
            }
        }

        moveHistory = new Stack<>();
        //moveHistory.push(data);
    }

    public String getBoxesRemain() {
        int boxesRemain = goals.size() - boxesSet;
        return Integer.toString(boxesRemain);
    }

    public void pushToHistory(char[][] array) {
        moveHistory.push(array);
    }


    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public char getElementAt(Coordinates coordinates) {
        return data[coordinates.x][coordinates.y];
    }

    public static ArrayList<Coordinates> getAllCoordinates() {
        return allCoordinates;
    }

    public char getData(Coordinates coordinates) {
        return data[coordinates.x][coordinates.y];
    }

    public int getRobotRow() {
        return robotRow;
    }

    public int getRobotCol() {
        return robotCol;
    }

    public void setRobotRow(int robotRow) {
        this.robotRow = robotRow;
    }

    public void setRobotCol(int robotCol) {
        this.robotCol = robotCol;
    }

    public void setData(Coordinates coordinates, char c) {
        data[coordinates.x][coordinates.y] = c;
    }

    public boolean isVictory() {
        return boxesSet == goals.size();
    }

    public char[][] getAllTheData() {
        return data;
    }

    public void setAllData(char[][] array) {
        data = array;
    }

    @Override
    public String toString() {
        return "Map{" +
                "height=" + height +
                ", width=" + width +
                '}';
    }
}
