import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.control.TextArea;
//Garden class that is at top of hierarchy
public class Garden {
    //instance variables for garden class
    public Plant[][] plot;
    public Map<String, ArrayList<String>> plantTypes = new HashMap<String, ArrayList<String>>();
    public Map<String, Plant> comTypes = new HashMap<String, Plant>();
    public Garden(int row, int col, String type) {//constructor for a garden
        plot = new Plant[row][col];
        ArrayList<String> veggies = new ArrayList<String>();
        ArrayList<String> trees = new ArrayList<String>();
        ArrayList<String> flowers = new ArrayList<String>();
        ArrayList<String> cacti = new ArrayList<String>();
        String typeSet = "garlic zucchini tomato yam lettuce";
        veggies.add(typeSet);
        String typeSet2 = "oak willow banana coconut pine";
        trees.add(typeSet2);
        String typeSet3 = "iris lily rose daisy tulip sunflower";
        String typeSet4 = "saguaro barrel cholla devil pear";
        flowers.add(typeSet3);
        cacti.add(typeSet4);
        plantTypes.put("vegetable", veggies);
        plantTypes.put("flower", flowers);
        plantTypes.put("tree", trees);
        plantTypes.put("cactus", cacti);
        comTypes.put("harvest", new Plant(1, 0));
        comTypes.put("pick", new Plant(2, 0));
        comTypes.put("cut", new Plant(3, 0));
        
    }
    public Garden(int row, int col) {//default construct

    }

    // process commands from infile
    public Plant ProcessCommand(String[] command, Garden myGarden,
            TextArea command2) {
        Plant myPlant = null;
        if (myGarden.plot[0].length > 16) {
            System.out.println("Too many plot columns.");
            command2.appendText("Too many plot columns.");
            System.exit(1);
        }
        //grow command read in 
        if (command[0].toLowerCase().compareTo("grow") == 0) {
            System.out.print("> GROW " + command[1]);
            if (command.length != 2) {
                System.out.println(" " + command[2]);
            } else {
                System.out.println();
            }
            boolean check = Garden.Grow(myGarden, command, command2);
            if (!check) {
                System.out.println();
                System.out.println("Can't grow there.");
                command2.appendText("Can't grow there.");
            }
            System.out.println();
        }
        //plant command read in
        else if (command[0].toLowerCase().compareTo("plant") == 0) {
            myPlant = Garden.PlantinGarden(command);
        }
        //cut, pick, or harvest command read in
        else if (command[0].compareTo("") != 0) {
            System.out.print("> " + command[0].toUpperCase());
            if (command.length != 1) {
                System.out.println(" " + command[1]);
            }
            else {
            System.out.println();
            }
            Garden.HCOrPCommand(command, myGarden, command2);
            System.out.println();
        }
        return myPlant;

    }
    //function that determines whether command was harvest cut or pick
    private static void HCOrPCommand(String[] command, Garden myGarden,
            TextArea command2) {
        Plant commandType = myGarden.comTypes.get(command[0].toLowerCase());
        //returns a veg, tree, or flower obj and then calls that pick/grow/harvest fct
        commandType.pickGrowOrHarvest(command, myGarden, commandType.gardenRow,
                command2);
      
    }

    // put the created plant into garden spot
    private static Plant PlantinGarden(String[] commands) {
        String[] rowCol;
        String row = null, col = null;
        rowCol = SplitRowandCommands(row, col, commands, 1);
        row = rowCol[0];
        col = rowCol[1];
        int rowI = Integer.parseInt(row);
        int colI = Integer.parseInt(col);
        Plant newPlant;
        newPlant = new Plant(rowI, colI, commands[2]);
        return newPlant;

    }

    // determines what type of grow command coming from infile
    private static boolean Grow(Garden myGarden, String[] command,
            TextArea com) {
        int growAmt = Integer.parseInt(command[1]);
        int result = 0;
        boolean check = false;
        for (int i = 0; i < myGarden.plot.length; i++) {
            for (int j = 0; j < myGarden.plot[i].length; j++) {
                // reg grow all command
                if (result != 1 && result != 3) {
                if (myGarden.plot[i][j] != null && command.length == 2) {
                        myGarden.plot[i][j].GROW(growAmt);
                        check = true;
                }
                if (myGarden.plot[i][j] != null && command.length != 2) {
                    // grow command at position or certain type of plant
                    result = myGarden.plot[i][j].GROW(growAmt, command,
                                myGarden, com);
                        check = true;
                }
                    check = true;
            }
                else {
                    break;
                }
        }
        }
        return check;
        // determines if it is a valid spot/thing to grow
    }

    // splits row and command form (,r,c) to r,c
    protected static String[] SplitRowandCommands(String row, String col,
            String[] commands, int i) {
        String[] rowcolList;
        String result[] = new String[2];
        rowcolList = commands[i].split(",");
        row = rowcolList[0].replace("()", "");
        col = rowcolList[1].replace("()", "");
        row = row.replace("(", "");
        col = col.replace(")", "");
        result[0] = row;
        result[1] = col;
        return result;
    }

    // finds all positions of a certain plant type either veg/tree/flower
    public static List<Integer> findPostionOfPlant(Garden myGarden,
            String name) {
        List<Integer> result = new ArrayList<Integer>();
        for (int i = 0; i < myGarden.plot.length; i++) {
            for (int j = 0; j < myGarden.plot[i].length; j++) {
                if (myGarden.plot[i][j] != null && myGarden.plot[i][j].name
                        .toLowerCase()
                        .compareTo(name.toLowerCase()) == 0) {
                    int temp = myGarden.plot[i][j]
                            .checkPlantType(myGarden.plot[i][j].name);
                    int temp2 = myGarden.plot[i][j].checkPlantType(name);
                    if (temp == temp2) {
                    result.add(i);
                    result.add(j);
                    }
                }
            }
        }
        return result;
    }

    // finds all positions of a certain plant type subclasses i.e yam
    public List<Integer> findPostionOfSubPlant(Garden myGarden, String type) {
        List<Integer> result = new ArrayList<Integer>();
        ArrayList<String> myList = myGarden.plantTypes.get(type.toLowerCase());
        String[] myList2 = myList.get(0).split(" ");
        for (int i = 0; i < myGarden.plot.length; i++) {
            for (int j = 0; j < myGarden.plot[i].length; j++) {
                if (myGarden.plot[i][j] != null
                        && isCorrectPlant(myList2,
                                myGarden.plot[i][j].name.toLowerCase())) {
                    result.add(i);
                    result.add(j);
                }
            }
        }
        return result;
    }

    // determies if the subclass is a part of the correct Plant class that
    // findPositionOfSubPlant is looking for
    private boolean isCorrectPlant(String[] myList2, String type) {
        for (int i = 0; i < myList2.length; i++) {
            if (myList2[i].compareTo(type) == 0) {
                return true;
            }
        }
        return false;
    }

}
