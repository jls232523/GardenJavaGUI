import java.util.List;

import javafx.scene.control.TextArea;

public class Cactus extends Plant {

    static int startRow = 4;
    static int startCol = 2;
    int gardenRow;
    int gardenCol;

    // tree constructor
    public Cactus(int row, int col, char id) {
        super(startRow, startCol);
        super.createPlot(startRow, startCol, id);
        gardenRow = row;
        gardenCol = col;
    }

    // grow function for tree objects
    public static char[][] grow(int num, Plant myPlant) {
        for (int i = 0; i < num; i++) {
            int result = findTopTree(myPlant);
            // finds current height of tree
            if (result == 1) {
                myPlant.plantPlot[3][2] = myPlant.Id;
                myPlant.plantPlot[3][1] = myPlant.Id;
                myPlant.plantPlot[3][3] = myPlant.Id;
            }
            if (result == 4) {
                myPlant.plantPlot[2][2] = myPlant.Id;
                myPlant.plantPlot[3][3] = myPlant.Id;
                myPlant.plantPlot[2][0] = myPlant.Id;
                myPlant.plantPlot[2][4] = myPlant.Id;
            }
            if (result == 7) {
                myPlant.plantPlot[1][2] = myPlant.Id;
                myPlant.plantPlot[3][0] = myPlant.Id;
                myPlant.plantPlot[3][4] = myPlant.Id;
            }
            if (result == 10) {
                myPlant.plantPlot[0][2] = myPlant.Id;
            }
        }
        return myPlant.plantPlot;
    }

    // function that finds top of the tree at that point
    private static int findTopTree(Plant myPlant) {
        int result = -1;
        for (int i = myPlant.plantPlot.length - 1; i >= 0; i--) {
            for (int j = 0; j < myPlant.plantPlot[i].length; j++) {
                if (myPlant.plantPlot[i][j] == myPlant.Id) {
                    result += 1;
                }
            }
        }
        return result + 1;
    }

    // function that cuts trees out of the garden
    public static void cut(String[] command, Garden myGarden,
            TextArea command2) {
        int rowG = 0, colG = 0, check = 0;
        if (command.length == 1) {
            // cut all trees out of the garden
            check = 1;
            for (int i = 0; i < myGarden.plot.length; i++) {
                for (int j = 0; j < myGarden.plot[i].length; j++) {
                    if (myGarden.plot[i][j] != null && myGarden.plot[i][j]
                            .checkPlantType(myGarden.plot[i][j].name) == 4) {
                        myGarden.plot[i][j] = null;
                        // not empty and is a tree then cut it down
                    }
                }
            }
        } else {
            check = cutHelper(command, myGarden, command2);
        }
        if (myGarden.plot[rowG][colG] == null && check == 0) {
            // if empty and was empty previously
            System.out.println();
            System.out.println("Can't cut there.");
            command2.appendText(" Can't cut there.");
        }
    }

    // helps cut func with cutting at a certain position
    private static int cutHelper(String[] command, Garden myGarden,
            TextArea command2) {
        int check = 0;
        if (command[1].endsWith(")") == true) {
            // cuts at a certain position
            String row = null, col = null;
            String[] result;
            int rowG, colG;
            check = 0;
            Cactus temp = new Cactus(0, 0, ' ');
            result = myGarden.SplitRowandCommands(row, col, command, 1);
            rowG = Integer.parseInt(result[0]);
            colG = Integer.parseInt(result[1]);
            if (myGarden.plot[rowG][colG] != null) {
                List<Integer> rowCol = findPostionOfPlant(myGarden,
                        myGarden.plot[rowG][colG].name);
                if (temp.checkTypeValid(myGarden.plot[rowG][colG], myGarden)) {
                    // if position is valid and it is a tree then cut
                    myGarden.plot[rowG][colG] = null;
                    check = 1;
                } else {
                    // not valid pos or plant type
                    System.out.println();
                    System.out.println("Can't cut there.");
                    command2.appendText("Can't cut there.");
                }
                return check;
            }
        } else {
            // cutting a certain type
            int rowGa, colGa;
            check = 0;
            List<Integer> rowCol = findPostionOfPlant(myGarden, command[1]);
            check = removeTree(rowCol, myGarden);
            return check;
        }
        return check;
    }

    // function that makes sure it is a valid tree obj
    private boolean checkTypeValid(Plant plot, Garden myGarden) {
        int result = plot.checkPlantType(plot.name);
        if (result == 3) {
            return true;
        }
        return false;
    }
    // removes trees from the garden
    private static int removeTree(List<Integer> rowCol, Garden myGarden) {
        // returns all positions of tree type
        int j = 0, rowGa, colGa, check = 0;
        for (int i = 0; i < rowCol.size() / 2; i++) {
            rowGa = rowCol.get(j);
            colGa = rowCol.get(j + 1);
            j += 2;
            myGarden.plot[rowGa][colGa] = null;
            check = 1;
        }
        return check;
    }

}
