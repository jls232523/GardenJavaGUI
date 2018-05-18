import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.control.TextArea;

//Plant Class that extends Garden used to create Plant obj
public class Plant extends Garden {
    char plantPlot[][] = new char[5][5];
    char Id;
    int gardenRow;
    int gardenCol;
    String name;
    static Map<Integer, String> hcpCom = new HashMap<Integer, String>();

    // constructor for plants
    public Plant(int i, int j, String type) {
        super(i, j, type);
        Id = type.toLowerCase().charAt(0);
        int plantType = checkPlantType(type);
        // vegetable
        if (plantType == 1) {
            plantPlot = new Vegetable(i, j, Id).plantPlot;
        }
        // flower
        if (plantType == 2) {
            plantPlot = new Flower(i, j, Id).plantPlot;
        }
        // tree
        if (plantType == 3) {
            plantPlot = new Tree(i, j, Id).plantPlot;
        }
        if (plantType == 4) {
            plantPlot = new Cactus(i, j, Id).plantPlot;
        }
        hcpCom.put(1, "harvest");
        hcpCom.put(2, "pick");
        hcpCom.put(3, "cut");
        gardenRow = i;
        gardenCol = j;
        name = type;
    }

    // default constructor
    public Plant(int row, int col) {
        super(row, col);
        gardenRow = row;
        gardenCol = col;
    }

    // creates beginning plot for any plant obj
    protected void createPlot(int row, int col, char id) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (row == i && col == j) {
                    plantPlot[i][j] = id;
                } else {
                    plantPlot[i][j] = '.';
                }
            }
        }
    }

    // determines what type of plant it is
    public int checkPlantType(String type) {
        String[] vegList;
        String[] treeList;
        String[] flowList;
        String[] cactList;
        vegList = super.plantTypes.get("vegetable").get(0).split(" ");
        treeList = super.plantTypes.get("tree").get(0).split(" ");
        flowList = super.plantTypes.get("flower").get(0).split(" ");
        cactList = super.plantTypes.get("cactus").get(0).split(" ");
        for (int i = 0; i < flowList.length; i++) {
            if (i != 5 && type.toLowerCase().compareTo(vegList[i]) == 0) {
                return 1;
            }
            if (type.toLowerCase().compareTo(flowList[i]) == 0) {
                return 2;
            }
            if (i != 5 && type.toLowerCase().compareTo(treeList[i]) == 0) {
                return 3;
            }
            if (i != 5 && type.toLowerCase().compareTo(cactList[i]) == 0) {
                return 4;
            }
        }
        return 0;
    }

    // grow everything in the garden by num amount
    public char[][] GROW(int num) {
        int plantType = checkPlantType(this.name);
        if (plantType == 1) {// veg
            Vegetable.grow(num, this);
        }
        if (plantType == 2) {// flower
            Flower.grow(num, this);
        }
        if (plantType == 3) {// tree
            this.plantPlot = Tree.grow(num, this);
        }
        if (plantType == 4) {// tree
            this.plantPlot = Cactus.grow(num, this);
        }
        return this.plantPlot;

    }

    // growing at a certain position/type
    public int GROW(int num, String[] type, Garden myGarden, TextArea com) {
        if (type[2].endsWith(")") == true) {// growing at position
            String row = null, col = null;
            String[] result;
            result = super.SplitRowandCommands(row, col, type, 2);
            int rowG = Integer.parseInt(result[0]);
            int colG = Integer.parseInt(result[1]);
            if (rowG < myGarden.plot.length
                    && myGarden.plot[rowG][colG] != null) {// bound checking
            String name = myGarden.plot[rowG][colG].name;
                int plantType = checkPlantType(name);// determine which plant it
                // call the respective grow method depending on plantType
                determinePlantGrow(plantType, myGarden, rowG, colG, num);
            return 1;
            }
            else if (rowG >= myGarden.plot.length) {// out of bounds
                System.out.println();
                System.out.println("Can't grow there.");
                com.appendText("Can't grow there.");

                return 3;
            }
        }
        else {// growing a type
            growCertainType(myGarden, type, num);
            return 1;
        }
        return 0;
    }

    // grows a certain type of plant in the garden
    private void growCertainType(Garden myGarden, String[] type, int num) {
        int plantType = checkPlantType(type[2]);
        int rowGa = 0;
        int colGa = 0;
        int j = 0;
        List<Integer> rowCol = super.findPostionOfPlant(myGarden,
                type[2].toLowerCase());
        // returns all the positions of type
        for (int i = 0; i < rowCol.size() / 2; i++) {
            rowGa = rowCol.get(j);
            colGa = rowCol.get(j + 1);
            j += 2;
            // call respective grow method depending on plantType
            determinePlantGrow(plantType, myGarden, rowGa, colGa, num);
        }
        if (plantType == 0) {
            // if want to grow all veg,flowers,and trees
            if (myGarden.plantTypes.containsKey(type[2])) {
                rowCol = super.findPostionOfSubPlant(myGarden, type[2]);
                // finds all positions in garden of v/f/t
                Plant.growHelper(rowCol, myGarden, num, type[2]);
            }
        }
    }

    // function that determines which plant to grow
    private void determinePlantGrow(int plantType, Garden myGarden, int rowGa,
            int colGa, int num) {
        if (plantType == 1 && myGarden.plot[rowGa][colGa] != null) {// veg
            Vegetable.grow(num, myGarden.plot[rowGa][colGa]);
        }
        if (plantType == 2 && myGarden.plot[rowGa][colGa] != null) {// flower
            Flower.grow(num, myGarden.plot[rowGa][colGa]);
        }
        if (plantType == 3 && myGarden.plot[rowGa][colGa] != null) {// tree
            myGarden.plot[rowGa][colGa].plantPlot = Tree.grow(num,
                    myGarden.plot[rowGa][colGa]);
        }
        if (plantType == 4 && myGarden.plot[rowGa][colGa] != null) {// tree
            myGarden.plot[rowGa][colGa].plantPlot = Cactus.grow(num,
                    myGarden.plot[rowGa][colGa]);
        }

    }
    // Func used to grow all types of a certain plant
    private static void growHelper(List<Integer> x, Garden myGarden, int num,
            String type) {
        int j = 0;
        for (int i = 0; i < x.size() / 2; i++) {
            int rowGa = x.get(j);
            int colGa = x.get(j + 1);
            j += 2;
            // call respective grow method
            if (type.toLowerCase().compareTo("vegetable") == 0) {
                Vegetable.grow(num, myGarden.plot[rowGa][colGa]);
            }
            if (type.toLowerCase().compareTo("tree") == 0) {
                myGarden.plot[rowGa][colGa].plantPlot = Tree.grow(num,
                        myGarden.plot[rowGa][colGa]);
            }
            if (type.toLowerCase().compareTo("flower") == 0) {
                Flower.grow(num, myGarden.plot[rowGa][colGa]);
            }
            if (type.toLowerCase().compareTo("cactus") == 0) {
                Cactus.grow(num, myGarden.plot[rowGa][colGa]);
            }
        }
    }

    // determines which command to call pick grow or harvest
    public void pickGrowOrHarvest(String[] command, Garden myGarden, int x,
            TextArea command2) {
        if (x == 1) {
            Vegetable.harvest(command, myGarden, command2);
        } else if (x == 2) {
            Flower.pick(command, myGarden, command2);
        } else if (x == 3) {
            Tree.cut(command, myGarden, command2);
            Cactus.cut(command, myGarden, command2);
        }
    }
}

