import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Scanner;


public class FieldModel {
    private int currentLevel;
    private Map currentMap;
    protected File levelDir;
    protected File[] levelFiles;
    private File currentFileLevel;
    protected final ArrayList<String> levelsFile;
    StringBuilder mapBuilder;
    ArrayList<StringBuilder> levelStrings;
    protected String progress;

    private final File bestDir = new File("best");
    protected String currentBest;
    File bestToProcess;

    /*private final String[] levels = {
                    "  #####\n" +
                    "###   #\n" +
                    "# $ # ##\n" +
                    "# #  . #\n" +
                    "#    # #\n" +
                    "## #   #\n" +
                    " #@  ###\n" +
                    " #####\n",

                    "  #####\n" +
                    "###   #\n" +
                    "# $ # ##\n" +
                    "# #  . #\n" +
                    "#    # #\n" +
                    "##$#.  #\n" +
                    " #@  ###\n" +
                    " #####\n",

                    "  #####\n" +
                    "###   #\n" +
                    "# $ # ##\n" +
                    "# #  . #\n" +
                    "# .  # #\n" +
                    "##$#.$ #\n" +
                    " #@  ###\n" +
                    " #####\n"
    };*///String reproduction


    FieldModel(int i) throws IOException {
        levelsFile = new ArrayList<>();
        mapBuilder = new StringBuilder();

        try {
            levelDir = new File("levels");
            levelFiles = levelDir.listFiles();
            assert levelFiles != null;
            currentFileLevel = levelFiles[i];
            //old meth
            /*try (Scanner inp = new Scanner(currentFileLevel)) {
                while (inp.hasNextLine()){
                    String line = inp.nextLine();
                    if (line.length()==0){
                        continue;
                    }
                    if (line.startsWith(";")){
                        levelsFile.add(mapBuilder.toString());
                        mapBuilder = new StringBuilder();
                    }else {
                        mapBuilder.append(line).append("\n");
                    }
                }
            }*/
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        levelStrings = new ArrayList<>();
        levelStrings.add(new StringBuilder());

        Files.lines(currentFileLevel.toPath()).forEachOrdered(line -> {
            String trimmed = line.trim();
            int lastIndex = levelStrings.size() - 1;
            if (!trimmed.startsWith(";") && !trimmed.isEmpty()) {
                levelStrings.get(lastIndex).append(line).append('\n');
            } else {
                if (!levelStrings.get(lastIndex).toString().isEmpty()) {
                    levelStrings.add(new StringBuilder());
                }
            }
        });
        int lastIndex = levelStrings.size() - 1;
        if (levelStrings.get(lastIndex).toString().isEmpty()) {
            levelStrings.remove(lastIndex);
        }

        this.currentLevel = 0;
        this.currentMap = new Map(levelStrings.get(currentLevel).toString());
        this.progress = ((currentLevel + 1) + " out of " + levelStrings.size());
        createBestFile();
        currentBest = ("Best score: " + selectCurrentBest());
    }

    private void createBestFile() {
        String st = currentFileLevel.toString();
        String result = st.substring(st.indexOf('\\'), st.indexOf('.')) + ".txt";
        bestToProcess = new File(bestDir + result);
        if (!bestToProcess.exists()) {
            try (
                    PrintWriter out = new PrintWriter(bestToProcess)
            ) {
                for (int j = 0; j < levelStrings.size(); j++) {
                    out.println(j + 1);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    void selectLevel(int i) {
        currentFileLevel = levelFiles[i];
        levelStrings = new ArrayList<>();
        levelStrings.add(new StringBuilder());
        try {
            Files.lines(currentFileLevel.toPath()).forEachOrdered(line -> {
                String trimmed = line.trim();
                int lastIndex = levelStrings.size() - 1;
                if (!trimmed.startsWith(";") && !trimmed.isEmpty()) {
                    levelStrings.get(lastIndex).append(line).append('\n');
                } else {
                    if (!levelStrings.get(lastIndex).toString().isEmpty()) {
                        levelStrings.add(new StringBuilder());
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        int lastIndex = levelStrings.size() - 1;
        if (levelStrings.get(lastIndex).toString().isEmpty()) {
            levelStrings.remove(lastIndex);
        }
        currentLevel = 0;
        currentMap = new Map(levelStrings.get(currentLevel).toString());
        progress = ((currentLevel + 1) + " out of " + levelStrings.size());
        createBestFile();
        currentBest = ("Best score: " + selectCurrentBest());
    }

    void markBest(RobotModel robot) throws IOException {
        File tempFile = File.createTempFile("best.tmp", ".txt");
        try (
                Scanner inp = new Scanner(bestToProcess);
                PrintWriter out = new PrintWriter(tempFile)
        ) {
            int thisLine = 0;
            while (inp.hasNext()) {
                thisLine++;
                String st1 = inp.nextLine();
                int now = currentLevel + 1;
                if (thisLine == now &&
                        st1.split(" ").length == 1) {
                    String st2 = st1 + " " + robot.getMoves();
                    out.println(st2);
                } else if (st1.split(" ").length == 2 &&
                        thisLine == now) {
                    String temp = st1.substring(st1.indexOf(" ") + 1);
                    int min = Math.min(Integer.parseInt(temp), Integer.parseInt(robot.getMoves()));
                    String st2 = st1.replace(temp, Integer.toString(min));
                    out.println(st2);
                } else {
                    out.println(st1);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Files.copy(tempFile.toPath(), bestToProcess.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    int selectCurrentBest() {
        int best = 0;
        try (Scanner inp = new Scanner(bestToProcess)) {
            for (int i = 0; i < currentLevel + 1; i++) {
                String st1 = inp.nextLine();
                if (i + 1 == currentLevel + 1 && st1.split(" ").length == 2) {
                    String st2 = st1.substring(st1.indexOf(" ") + 1);
                    best = Integer.parseInt(st2);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return best;
    }

    public int getCurrentLevel() {
        return currentLevel + 1;
    }

    public Map getCurrentMap() {
        return currentMap;
    }

    boolean isInsideTheField(Coordinates coordinates) {
        return coordinates.x >= 0 && coordinates.x < currentMap.getWidth() &&
                coordinates.y >= 0 && coordinates.y < currentMap.getHeight() &&
                currentMap.getData(coordinates) == ' ' ||
                currentMap.getData(coordinates) == '.' || currentMap.getData(coordinates) == '@';
    }


    public void goToNextLevel() {
        currentLevel++;
        if (currentLevel >= levelStrings.size()) {
            currentLevel = 0;
        }
        currentBest = ("Best score: " + selectCurrentBest());
        currentMap = new Map(levelStrings.get(currentLevel).toString());
        progress = ((currentLevel + 1) + " out of " + levelStrings.size());
    }

    public void goToPrevLevel() {
        currentLevel--;
        if (currentLevel < 0) {
            currentLevel = levelStrings.size() - 1;
        }
        currentBest = ("Best score: " + selectCurrentBest());
        currentMap = new Map(levelStrings.get(currentLevel).toString());
        progress = ((currentLevel + 1) + " out of " + levelStrings.size());
    }

    public void restart() {
        currentMap = new Map(levelStrings.get(currentLevel).toString());
    }
}
