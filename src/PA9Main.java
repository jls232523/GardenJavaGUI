import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

//Pa8 code that produces a visual gui output for PA6 Garden code. An example input file looks like,
//main function calls all helper functions to run program
/* this prgm creates a gui that accepts both GUI_in and FUI_out
* By reading in commands such as print,grow,plant,cut,harvest,and pick
* the program executes differnt tasks depending on the given command*/
/* *rows: 7
cols: 7
commands are grow plant cut harvest pick with a variety of types from those as well
*/
public class PA9Main extends Application {
    private final static int TEXT_S = 240, RECT_S = 20;
    private static int SIZE_A = 100, SIZE_D = 200;
    private static List<String> commands;
    public static Scanner in;
    public static Garden myGar;
    public static int comCou = 0;
    public static TextArea command = new TextArea();
    public static String[] argC;
    public static void main(String[] args) {
        argC = args;
        launch(args);
    }

    // start method that starts application
    @Override
    public void start(Stage primaryStage) {
        readArgs();
        myGar = new Garden(SIZE_D, SIZE_A, " ");
        SIZE_A = SIZE_A * 5 * RECT_S;
        SIZE_D = SIZE_D * 5 * RECT_S;
        Button button = new Button("Process");
        Button button2 = new Button("Reset");
        TextField cmd_in = new TextField();
        ObservableList<String> co = FXCollections.observableArrayList();
        ChoiceBox<String> cb = new ChoiceBox(co);
        GraphicsContext gc = setupStage(primaryStage, SIZE_A, SIZE_D, command,
                button, cmd_in, cb, button2);
        gc.setFill(Color.GREEN);
        gc.fillRect(0, 0, SIZE_A, SIZE_D);
        button.setOnAction((event) -> {
            System.out.println(cmd_in.getText());
            try {
            simulateGarden(gc, cmd_in);
            } catch (Exception e) {
                command.appendText("BAD Command " + cmd_in.getText() + "\n");
            }
        });
        button2.setOnAction((event) -> {
            cmd_in.setText("");
        });
        cb.setOnAction((event) -> {
            cmd_in.setText(
                    cmd_in.getText() + cb.getValue().toLowerCase() + " ");
        });
        primaryStage.show();
    }

    // reads the arguments form args
    private void readArgs() {
        SIZE_A = Integer.parseInt(argC[0]);
        SIZE_D = Integer.parseInt(argC[1]);

    }

    // sets up the stage for the gui output
    public GraphicsContext setupStage(Stage primaryStage, int canvas_width,
            int canvas_height, TextArea command, Button button,
            TextField cmd_in, ChoiceBox cb, Button button2) {
        BorderPane p = new BorderPane();
        Canvas canvas = new Canvas(SIZE_A, SIZE_D);
        command.setPrefHeight(TEXT_S);
        command.setEditable(false);
        createField(p);
        Border b;
        button.defaultButtonProperty().bind(button.focusedProperty());
        p.setBorder(new Border(
                new BorderStroke(Color.BLACK, BorderStrokeStyle.DASHED,
                        CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        p.setCenter(canvas);
        p.setRight(command);
        cb.getItems().addAll("Plant", "Grow", "Cut", "Pick", "Harvest");
        primaryStage.setTitle("Garden");
        final int num_items = 3;
        HBox input_box = new HBox(num_items);
        button.setTextFill(Color.GREEN);
        button2.setTextFill(Color.RED);
        p.setTop(cb);
        input_box.getChildren().add(cmd_in);
        input_box.getChildren().add(button);
        input_box.getChildren().add(button2);
        p.setBottom(input_box);
        primaryStage.setScene(new Scene(p));
        return canvas.getGraphicsContext2D();
    }

    // creates the text command that gives instructions
    private void createField(BorderPane p) {
        VBox v = new VBox(6);
        Text o = new Text("Command Types:");
        Text t = new Text("Plant");
        Text to = new Text("Grow");
        Text too = new Text("Cut");
        Text e = new Text("Pick");
        Text tooo = new Text("Harvest");
        v.getChildren().add(o);
        v.getChildren().add(t);
        v.getChildren().add(to);
        v.getChildren().add(too);
        v.getChildren().add(e);
        v.getChildren().add(tooo);
        p.setLeft(v);
    }

    // simulates the garden by taking in the command
    private void simulateGarden(GraphicsContext gc, TextField cmd_in) {
        Plant myPlant = null;
        int result = 0;
        // runs through all the commands
        String[] splitCom = cmd_in.getText().split(" ");
            myPlant = myGar.ProcessCommand(splitCom, myGar, command);
            if (splitCom[0].toLowerCase().compareTo("plant") == 0) {
                // if it is a plant command and location=valid put in garden
                if (myPlant.gardenCol < myGar.plot[myPlant.gardenRow].length
                        && myPlant.gardenRow < myGar.plot.length
                        && myPlant.name != null) {
                    myGar.plot[myPlant.gardenRow][myPlant.gardenCol] = myPlant;
                gardenDraw(gc);
                command.appendText(cmd_in.getText() + "\n");
                }
                else {
                command.appendText("BAD Command " + cmd_in.getText() + "\n");
                }
            }
        else {
            gardenDraw(gc);
            command.appendText(cmd_in.getText() + "\n");
            gardenDraw(gc);
        }
    }

    // draws the garden to the output
    private void gardenDraw(GraphicsContext gc) {
        for (int j = 0; j < myGar.plot.length; j++) {
            for (int i = 0; i < myGar.plot[j].length; i++) {
                if (myGar.plot[j][i] != null && myGar.plot[j][i].checkPlantType(
                        myGar.plot[j][i].name.toLowerCase()) == 1) {// veg
                    gc.setFill(Color.BLUE);
                    colorSquares(gc, myGar.plot[j][i], myGar.plot[j][i].name);
                } else if (myGar.plot[j][i] != null
                        && myGar.plot[j][i].checkPlantType(
                                myGar.plot[j][i].name.toLowerCase()) == 2) {// flo
                    gc.setFill(Color.RED);
                    colorSquares(gc, myGar.plot[j][i], myGar.plot[j][i].name);
                } else if (myGar.plot[j][i] != null
                        && myGar.plot[j][i].checkPlantType(
                                myGar.plot[j][i].name.toLowerCase()) == 3) {// tree
                    gc.setFill(Color.YELLOW);
                    colorSquares(gc, myGar.plot[j][i], myGar.plot[j][i].name);
                } else if (myGar.plot[j][i] != null
                        && myGar.plot[j][i].checkPlantType(
                                myGar.plot[j][i].name.toLowerCase()) == 4) {// cac
                    gc.setFill(Color.GREEN);
                    colorSquares(gc, myGar.plot[j][i], myGar.plot[j][i].name);
                } else {
                    // fills the background
                    gc.setFill(Color.GREEN);
                    int rSize = j * 100;
                    int cSize = i * 100;
                    gc.fillRect(rSize, cSize, 100, 100);
                }
            }
        }

    }

    // colors squares for the garden
    private void colorSquares(GraphicsContext gc, Plant plant, String type) {
        HashMap<String, Color> colorWay = new HashMap<String, Color>();
        colorWay = makeHash();
        int rSize = 0;
        int cSize = 0;
        for (int i = 0; i < plant.plantPlot.length; i++) {
            for (int j = 0; j < plant.plantPlot[i].length; j++) {
                if (plant.plantPlot[j][i] != '.') {
                    gc.setFill(colorWay.get(type.toLowerCase()));
                    rSize = plant.gardenRow * 100 + (i * RECT_S);
                    cSize = plant.gardenCol * 100 + (j * RECT_S);
                    gc.fillRect(rSize, cSize, RECT_S, RECT_S);
                    // plant
                } else {
                    gc.setFill(Color.BROWN);
                    rSize = plant.gardenRow * 100 + (i * RECT_S);
                    cSize = plant.gardenCol * 100 + (j * RECT_S);
                    gc.fillRect(rSize, cSize, RECT_S, RECT_S);
                    // dirt
                }
            }
        }
    }

    // creates hasmap of plants and colors
    private HashMap<String, Color> makeHash() {
        HashMap<String, Color> colorWay = new HashMap<String, Color>();
        colorWay.put("oak", Color.BEIGE);
        colorWay.put("willow", Color.BURLYWOOD);
        colorWay.put("banana", Color.YELLOW);
        colorWay.put("coconut", Color.WHITE);
        colorWay.put("pine", Color.GREEN);
        colorWay.put("saguaro", Color.LIMEGREEN);
        colorWay.put("barrel", Color.LIME);
        colorWay.put("cholla", Color.GREENYELLOW);
        colorWay.put("devil", Color.DARKOLIVEGREEN);
        colorWay.put("pear", Color.MEDIUMSPRINGGREEN);
        colorWay.put("garlic", Color.NAVAJOWHITE);
        colorWay.put("zucchini", Color.PALEGREEN);
        colorWay.put("tomato", Color.DARKORANGE);
        colorWay.put("yam", Color.LIGHTYELLOW);
        colorWay.put("lettuce", Color.LAWNGREEN);
        colorWay.put("iris", Color.MEDIUMPURPLE);
        colorWay.put("lily", Color.DEEPPINK);
        colorWay.put("rose", Color.RED);
        colorWay.put("daisy", Color.FLORALWHITE);
        colorWay.put("tulip", Color.ORANGE);
        colorWay.put("sunflower", Color.BLACK);
        return colorWay;
    }
}
