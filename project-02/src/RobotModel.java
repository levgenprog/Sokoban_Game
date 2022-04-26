import java.util.Stack;

class RobotModel {
    private Coordinates robotCoordinates;
    private final FieldModel fieldModel;
    private int moves;

    public Stack<Integer> actions;
    protected final Stack<Integer> redoActions;

    public void setSide(States side) {
        this.side = side;
    }

    protected enum States {FRONT, BACK, RIGHT, LEFT}

    protected States side;

    public RobotModel(FieldModel fieldModel, Coordinates coordinates) {
        this.fieldModel = fieldModel;
        robotCoordinates = coordinates;
        moves = 0;
        actions = new Stack<>();
        redoActions = new Stack<>();

        setSide(States.FRONT);
    }

    public Coordinates getRobotCoordinates() {
        return robotCoordinates;
    }

    public void setRobotCoordinates(Coordinates robotCoordinates) {
        this.robotCoordinates = robotCoordinates;
    }

    private boolean isBoxInGoal(Coordinates coordinates) {
        return fieldModel.getCurrentMap().getElementAt(coordinates) == '.';
    }


    private void moveBox(Coordinates coordinates, int dir) {
        Coordinates destination;
        switch (dir) {
            case 1:
                destination = new Coordinates(coordinates.x - 1, robotCoordinates.y);
                break;
            case 2:
                destination = new Coordinates(coordinates.x + 1, robotCoordinates.y);
                break;
            case 3:
                destination = new Coordinates(robotCoordinates.x, coordinates.y - 1);
                break;
            case 4:
                destination = new Coordinates(robotCoordinates.x, coordinates.y + 1);
                break;
            default:
                destination = new Coordinates(coordinates.x, robotCoordinates.y);
        }
        if (fieldModel.getCurrentMap().getElementAt(coordinates) == '$' &&
                fieldModel.isInsideTheField(destination)) {
            fieldModel.getCurrentMap().setData(coordinates, ' ');
            if (isBoxInGoal(destination)) {
                fieldModel.getCurrentMap().setData(destination, '*');
                fieldModel.getCurrentMap().boxesSet++;
            } else {
                fieldModel.getCurrentMap().setData(destination, '$');
            }
            robotCoordinates = coordinates;
            moves++;
            actions.push(dir);
        } else if (fieldModel.getCurrentMap().getElementAt(coordinates) == '*' &&
                fieldModel.isInsideTheField(destination)) {
            if (fieldModel.getCurrentMap().getElementAt(destination) == '.') {
                fieldModel.getCurrentMap().setData(destination, '*');
                fieldModel.getCurrentMap().setData(coordinates, '.');
            } else {
                fieldModel.getCurrentMap().setData(destination, '$');
                fieldModel.getCurrentMap().setData(coordinates, '.');
                fieldModel.getCurrentMap().boxesSet--;
            }

            robotCoordinates = coordinates;
            moves++;
            actions.push(dir);
        }
    }

    void moveLeft() {
        int nextX = robotCoordinates.x - 1;
        if (fieldModel.isInsideTheField(new Coordinates(nextX, robotCoordinates.y))) {
            robotCoordinates.x = nextX;
            moves++;
            actions.push(5);
        }
        moveBox(new Coordinates(nextX, robotCoordinates.y), 1);
        setSide(States.LEFT);
    }

    void moveRight() {
        int nextX = robotCoordinates.x + 1;
        if (fieldModel.isInsideTheField(new Coordinates(nextX, robotCoordinates.y))) {
            robotCoordinates.x = nextX;
            moves++;
            actions.push(6);
        }
        moveBox(new Coordinates(nextX, robotCoordinates.y), 2);

        setSide(States.RIGHT);
    }

    void moveUp() {
        int nextY = robotCoordinates.y - 1;
        if (fieldModel.isInsideTheField(new Coordinates(robotCoordinates.x, nextY))) {
            robotCoordinates.y = nextY;
            moves++;
            actions.push(7);
        }
        moveBox(new Coordinates(robotCoordinates.x, nextY), 3);

        setSide(States.BACK);
    }

    void moveDown() {
        int nextY = robotCoordinates.y + 1;
        if (fieldModel.isInsideTheField(new Coordinates(robotCoordinates.x, nextY))) {
            robotCoordinates.y = nextY;
            moves++;
            actions.push(8);
        }
        moveBox(new Coordinates(robotCoordinates.x, nextY), 4);
        setSide(States.FRONT);
    }

    private boolean canUndo() {
        return !actions.isEmpty() && moves - 1 > 0;
    }

    void undo() {
        if (canUndo()) {
            redoActions.push(actions.get(moves - 1));
            switch (actions.pop()) {
                case 5:
                    moveRight();
                    break;
                case 6:
                    moveLeft();
                    break;
                case 7:
                    moveDown();
                    break;
                case 8:
                    moveUp();
                    break;
                case 1:
                    moveRight();
                    moveBoxBack(new Coordinates(robotCoordinates.x - 1, robotCoordinates.y), 1);
                    break;
                case 2:
                    moveLeft();
                    moveBoxBack(new Coordinates(robotCoordinates.x + 1, robotCoordinates.y), 2);
                    break;
                case 3:
                    moveDown();
                    moveBoxBack(new Coordinates(robotCoordinates.x, robotCoordinates.y - 1), 3);
                    break;
                case 4:
                    moveUp();
                    moveBoxBack(new Coordinates(robotCoordinates.x, robotCoordinates.y + 1), 4);
                    break;
            }
            actions.remove(moves - 2);
            moves -= 2;
        }
    }

    void moveBoxBack(Coordinates coordinates, int dir) {

        Coordinates empty;
        switch (dir) {
            case 1:
                empty = new Coordinates(coordinates.x - 1, coordinates.y);
                break;
            case 2:
                empty = new Coordinates(coordinates.x + 1, coordinates.y);
                break;
            case 3:
                empty = new Coordinates(coordinates.x, coordinates.y - 1);
                break;
            case 4:
                empty = new Coordinates(coordinates.x, coordinates.y + 1);
                break;
            default:
                empty = coordinates;
        }
        if (fieldModel.getCurrentMap().getData(coordinates) == '.' && fieldModel.getCurrentMap().getData(empty) == '*') {
            fieldModel.getCurrentMap().setData(coordinates, '*');
            fieldModel.getCurrentMap().setData(empty, '.');
        } else if (fieldModel.getCurrentMap().getData(coordinates) == '*' && fieldModel.getCurrentMap().getData(empty) == ' ') {
            fieldModel.getCurrentMap().setData(coordinates, '.');
            fieldModel.getCurrentMap().setData(empty, ' ');
            fieldModel.getCurrentMap().boxesSet--;
        } else if (fieldModel.getCurrentMap().getData(coordinates) == ' ' && fieldModel.getCurrentMap().getData(empty) == '*') {
            fieldModel.getCurrentMap().setData(empty, '.');
            fieldModel.getCurrentMap().setData(coordinates, '$');
            fieldModel.getCurrentMap().boxesSet--;
        } else {
            fieldModel.getCurrentMap().setData(coordinates, '$');
            fieldModel.getCurrentMap().setData(empty, ' ');
        }
    }

    private boolean canRedo() {
        return !redoActions.isEmpty();
    }

    void redo() {
        if (canRedo()) {
            switch (redoActions.pop()) {
                case 1:
                case 5:
                    moveLeft();
                    break;
                case 2:
                case 6:
                    moveRight();
                    break;
                case 3:
                case 7:
                    moveUp();
                    break;
                case 4:
                case 8:
                    moveDown();
                    break;
            }
        }
    }

    public String getMoves() {
        return Integer.toString(moves);
    }

    public void setMoves(int moves) {
        this.moves = moves;
    }

}
