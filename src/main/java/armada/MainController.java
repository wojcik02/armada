package armada;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class MainController {

	@FXML
	ScrollPane ScrollBoard = new ScrollPane();
	@FXML
	ScrollPane MainBoard = new ScrollPane();
	@FXML
	GridPane MainPane = new GridPane();
	@FXML
	Button newFleet;
	@FXML
	HBox buttons;

	static Ship activeShip;
	static boolean isSelected = false;
	static boolean newSelection = true;
	static boolean attack = false;
	static String activity = "Start";
	
	
	static HashMap<Pane, Ship> ships = new HashMap<Pane, Ship>();
	ArrayList<Ship> rebelShips = new ArrayList<Ship>();
	ArrayList<Ship> empireShips = new ArrayList<Ship>();

	static GameBoard Board = new GameBoard();
	static GameLog GameLog = new GameLog();
	static Details Details = new Details();

	public MainController() {

	}

	public void initialize() {
		
		MainPane.add(GameLog.getLog(),0,0);
		GameLog.updateLog("Zaczynamy");
		MainBoard.setContent(Board.getBoard());
		ScrollBoard.setContent(Details.getDetails());

	}
	

	public static void gameEvent(double x, double y) {

		if (!newSelection&&activity.equals("Nothing")) {

        	Details.cleanDetails();
			activeShip.Deselect();
			isSelected = false;
			
		}

		if (newSelection&&activity.equals("Nothing")) {
			
			GameLog.updateLog("Wybrany statek: " + activeShip.getName());
			isSelected = true;
			newSelection = false;

		}
		
		if (activity.equals("Put Ship")) {
			Ship newShip = new Ship(Details.listOfShips.getSelectionModel().getSelectedItem().toString());
			x = x - newShip.getShipOnBoard().getPrefWidth() / 2;
			y = y - newShip.getShipOnBoard().getPrefHeight() / 2;
			Board.addShip(x, y, newShip);
			ships.put(newShip.getShipOnBoard(), newShip);
			GameLog.updateLog("Doda³em Statek: " + newShip.getName());
			Details.cleanDetails();
			activity = "Nothing";
		}

	}


	public void NewFleet(MouseEvent event) {
		Details.selectNewShip();
	}

	
		// ScrollBoard.setVvalue(1.0); Przemieszczanei scrolla, na przysz³oœæ

	

	public static void selectedShip(Pane newSelectedShip) {

		if (activeShip != null) {
			activeShip.Deselect();

		}
		activeShip = ships.get(newSelectedShip);
		activeShip.Selected();
		Details.selectedShipDetails(activeShip);
		newSelection = true;
		isSelected = true;
		System.out.println(activeShip);

	}
	
	


	// Ruch statku

	public void MoveShip(MouseEvent event) {
		
		activity="Move Ship";

		if (isSelected == true) {
			
			Board.putToken();
			MainPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
				
				public void handle(KeyEvent event1) {
					if(activity.equals("Move Ship")&&isSelected==true) {
					Board.cleanBoard();
					Board.MoveToken.TurnToken(event1);
					Board.putToken();

					}
				}
				
			});
			MainPane.setFocusTraversable(true);
		}
	}


	public void EndMove(MouseEvent event) {

		if (activity.equals("Move Ship")) {

			GameLog.updateLog("Przmieœci³em statek");
			activeShip.Move();
			Board.cleanBoard();
			Details.cleanDetails();
			activity = "Nothing";
			
			
			
		}
	}




}
