import java.util.List;

import javafx.scene.control.TextArea;

//Vegetable class that is a subclass of Plant
public class Vegetable extends Plant {
    static int startRow = 0;
    static int startCol = 2;
    int gardenRow;
    int gardenCol;

    // vegetable constructor
    public Vegetable(int row, int col, char id) {
        super(startRow, startCol);
        super.createPlot(startRow, startCol, id);
        gardenRow = row;
        gardenCol = col;
    }

    // grow function for vegetable objects
    public static void grow(int num, Plant myPlant) {
        for (int i = 0; i < num; i++) {
            int result = findBotVeggie(myPlant);
            // finds current depth of veg
            if (result >= 5) {
                result = 4;
            }
            myPlant.plantPlot[result][2] = myPlant.Id;
        }

    }

    // finds the current depth
    private static int findBotVeggie(Plant myPlant) {
        int result = 0;
        for (int i = myPlant.plantPlot.length - 1; i >= 0; i--) {
            for (int j = 0; j < myPlant.plantPlot[i].length; j++) {
                if (myPlant.plantPlot[i][j] == myPlant.Id) {
                    result += 1;
                }
            }
        }
        return result;
    }

    // harvests out veggies from the garden
    public static void harvest(String[] command, Garden myGarden,
            TextArea command2) {
        int rowG = 0,colG = 0;
        int check =0;
        if (command.length == 1) {
            // harvests all of the veggies
            check = 1;
            for (int i = 0; i < myGarden.plot.length; i++) {
                for (int j = 0; j < myGarden.plot[i].length; j++) {
                    if (myGarden.plot[i][j] != null && myGarden.plot[i][j]
                            .checkPlantType(myGarden.plot[i][j].name) == 1) {
                        myGarden.plot[i][j] = null;
                    }
                }
            }
        }
        else {
            check = harvestHelper(command, myGarden, command2);
        }
        if (check != 1) {
            System.out.println("Can't harvest there.");
            command2.appendText("Can't harvest there.");
        }
    }

    // helps harvest at a certain pos in the garden
    private static int harvestHelper(String[] command, Garden myGarden,
            TextArea command2) {
        int rowG = 0,colG = 0;
        int check =0;
        if (command[1].endsWith(")") == true) {
            // harvests veggie at certain position
            String row = null, col = null;
            String[] result;
            Vegetable temp = new Vegetable(0, 0, ' ');
            result = myGarden.SplitRowandCommands(row, col, command, 1);
             rowG = Integer.parseInt(result[0]);
             colG = Integer.parseInt(result[1]);
            if (myGarden.plot[rowG][colG] != null) {
                check = 1;
                List<Integer> rowCol = findPostionOfPlant(myGarden,
                        myGarden.plot[rowG][colG].name);
                if (temp.checkTypeValid(myGarden.plot[rowG][colG],
                        myGarden, 1)) {
                myGarden.plot[rowG][colG] = null;
            }
                else {// not a valid place
                    System.out.println();
                    System.out.println("Can't harvest there.");
                    command2.appendText("Can't harvest there.");
                }
            }
        } else {// harvests a certain type of veggie
            check = 1;
            List<Integer> rowCol = findPostionOfPlant(myGarden, command[1]);
            removeVeggie(rowCol, myGarden);
        }
        return check;
    }

    // removes all veggies of certain type
    private static void removeVeggie(List<Integer> rowCol, Garden myGarden) {
        int j = 0;
        int rowGa, colGa;
        for (int i = 0; i < rowCol.size() / 2; i++) {
            rowGa = rowCol.get(j);
            colGa = rowCol.get(j + 1);
            j += 2;
            myGarden.plot[rowGa][colGa] = null;
        }
    }

    // determines if it is a valid vegetable
    boolean checkTypeValid(Plant plot, Garden myGarden, int pType) {
        int result = plot.checkPlantType(plot.name);
        if (result == pType) {
                return true;
        }
        return false;
    }
}
