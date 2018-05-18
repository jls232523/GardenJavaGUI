import java.util.List;

import javafx.scene.control.TextArea;

//Flower class that is a subclass of Plant
public class Flower extends Plant {
    static int startRow = 2;
    static int startCol = 2;
    int gardenRow;
    int gardenCol;

    // Flower constructor
    public Flower(int row, int col, char id) {
        super(startRow, startCol);
        super.createPlot(startRow, startCol, id);
        gardenRow = row;
        gardenCol = col;
    }

    // flower grow function
    public static void grow(int num, Plant myPlant) {
        for (int i = 0; i < num; i++) {
            int result = findFlowerSize(myPlant);
            if (result == 1) {
                myPlant.plantPlot = fillPlot1(myPlant);
            }
            if (result == 5) {
                myPlant.plantPlot = fillPlot2(myPlant);
            }
            if (result == 13) {
                myPlant.plantPlot = fillPlot3(myPlant);
            }
            if (result == 21) {
                myPlant.plantPlot = fillAllPlot(myPlant);
            }
        }
    }

    // function that determines what size the flower is b4 growth
    private static int findFlowerSize(Plant myPlant) {
        int result = 0;
        for (int i = 0; i < myPlant.plantPlot.length; i++) {
            for (int j = 0; j < myPlant.plantPlot[i].length; j++) {
                if (myPlant.plantPlot[i][j] != '.') {
                    result += 1;
                }
            }
        }
        return result;
    }

    // fills out the flower
    private static char[][] fillPlot3(Plant myPlant) {
        myPlant.plantPlot[0][1] = myPlant.Id;
        myPlant.plantPlot[0][3] = myPlant.Id;
        myPlant.plantPlot[1][0] = myPlant.Id;
        myPlant.plantPlot[1][4] = myPlant.Id;
        myPlant.plantPlot[3][0] = myPlant.Id;
        myPlant.plantPlot[3][4] = myPlant.Id;
        myPlant.plantPlot[4][1] = myPlant.Id;
        myPlant.plantPlot[4][3] = myPlant.Id;
        myPlant.plantPlot[4][2] = myPlant.Id;
        return myPlant.plantPlot;
    }

    // fills out the flower
    private static char[][] fillPlot2(Plant myPlant) {
        myPlant.plantPlot[0][2] = myPlant.Id;
        myPlant.plantPlot[1][1] = myPlant.Id;
        myPlant.plantPlot[1][3] = myPlant.Id;
        myPlant.plantPlot[2][0] = myPlant.Id;
        myPlant.plantPlot[2][4] = myPlant.Id;
        myPlant.plantPlot[3][1] = myPlant.Id;
        myPlant.plantPlot[3][3] = myPlant.Id;
        myPlant.plantPlot[4][2] = myPlant.Id;
        return myPlant.plantPlot;
    }

    // fills out the flower
    private static char[][] fillPlot1(Plant myPlant) {
        myPlant.plantPlot[1][2] = myPlant.Id;
        myPlant.plantPlot[3][2] = myPlant.Id;
        myPlant.plantPlot[2][1] = myPlant.Id;
        myPlant.plantPlot[2][3] = myPlant.Id;
        return myPlant.plantPlot;
    }

    // fills out the flower for max size
    private static char[][] fillAllPlot(Plant myPlant) {
        for (int j = 0; j < 5; j++) {
            for (int k = 0; k < 5; k++) {
                myPlant.plantPlot[j][k] = myPlant.Id;
            }
        }
        return myPlant.plantPlot;
    }

    // function that picks a flower out of the garden
    public static void pick(String[] command, Garden myGarden,
            TextArea command2) {
        if (command.length == 1) {
            // pick all flowers out of the garden
            for (int i = 0; i < myGarden.plot.length; i++) {
                for (int j = 0; j < myGarden.plot[i].length; j++) {
                    if (myGarden.plot[i][j] != null && myGarden.plot[i][j]
                            .checkPlantType(myGarden.plot[i][j].name) == 2) {
                        // if its a flower then make it null(pick)
                        myGarden.plot[i][j] = null;
                    }
                }
            }
        }
        else {
            pickHelper(command, myGarden, command2);

        }
    }

    // pick helper that determines if it is a pos command or a name
    private static void pickHelper(String[] command, Garden myGarden,
            TextArea command2) {
        // determine if it is a position pick or name pick
        int check = 0;
        if (command[1].endsWith(")") == true) {
            // position pick
            String row = null, col = null;
            String[] result;
            Flower temp = new Flower(0, 0, ' ');
            result = myGarden.SplitRowandCommands(row, col, command, 1);
            int rowG = Integer.parseInt(result[0]);
            int colG = Integer.parseInt(result[1]);
            // gets position
            if (myGarden.plot[rowG][colG] != null) {
                List<Integer> rowCol = findPostionOfPlant(myGarden,
                        myGarden.plot[rowG][colG].name);
                if (temp.checkTypeValid(myGarden.plot[rowG][colG], myGarden)) {
                    myGarden.plot[rowG][colG] = null;
                    // removes if it is a flower
                    check = 1;
                } else {
                    System.out.println();
                    System.out.println("Can't pick there.");
                    command2.appendText("Can't pick there.");
                    // wasn't a flower
                }
            }
        } else {// name pick
            int rowGa, colGa;
            List<Integer> rowCol = findPostionOfPlant(myGarden, command[1]);
            removeFlower(rowCol, myGarden);
            check = 1;
        }
        if (check == 0) {
        command2.appendText("Can't pick there.");
        }
    }

    // removes a flower from the garden
    private static void removeFlower(List<Integer> rowCol, Garden myGarden) {
        // finds all positions of the name
        int j = 0;
        int rowGa, colGa;
        for (int i = 0; i < rowCol.size() / 2; i++) {
            rowGa = rowCol.get(j);
            colGa = rowCol.get(j + 1);
            j += 2;
            myGarden.plot[rowGa][colGa] = null;
        }
    }


    // function that makes sure the plant is actually a flower
    private boolean checkTypeValid(Plant plot, Garden myGarden) {
        int result = plot.checkPlantType(plot.name);
        if (result == 2) {
                return true;
        }

        return false;
    }

}
