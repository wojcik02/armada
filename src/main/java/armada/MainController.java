package armada;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Light;
import javafx.scene.effect.Light.Point;
import javafx.scene.effect.Lighting;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

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

	static int lineNumber = 0;
	static Ship activeShip;
	static Pane selectedShipTarget;
	static Ship Target;
	static Pane newSelectedShip;
	static Pane selectedShip;
	static boolean isSelected = false;
	static boolean newSelection = true;
	static boolean attack = false;
	boolean wybrany = false;
	static int selectedLine = 2;
	static String activity = "new";
	static HashMap<Pane, Ship> ships = new HashMap<Pane, Ship>();
	ArrayList<Ship> rebelShips = new ArrayList<Ship>();
	ArrayList<Ship> empireShips = new ArrayList<Ship>();

	static GameBoard Board = new GameBoard();
	AnchorPane BattleGround = Board.getBoard();
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
		System.out.println("Robie zdarzenie");

	

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
			activity = "Nothing";
		}

	}

	public static int getSelectedLine() {
		return selectedLine;
	}

	public static Pane getSelectedShip() {
		return selectedShip;
	}

	public void NewFleet(MouseEvent event) {
		Details.selectNewShip();
	}

	
		// ScrollBoard.setVvalue(1.0); Przemieszczanei scrolla, na przysz³oœæ

	// Zaznaczenie wybranego Pane Statku

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
	
	

	public static void selectedTarget(Pane newSelectedShipTarget) {

		selectedShipTarget = newSelectedShipTarget;
		Target = ships.get(selectedShipTarget);
		Lighting lighting = new Lighting();
		lighting.setDiffuseConstant(1.0);
		lighting.setSpecularConstant(0.0);
		lighting.setSpecularExponent(0.0);
		lighting.setSurfaceScale(0.0);
		lighting.setLight(new Light.Distant(100, 100, Color.RED));
		selectedShipTarget.getChildren().get(0).setEffect(lighting);

	}

	// Ruch statku

	public void MoveShip(MouseEvent event) {

		if (isSelected == true) {

			// Pobranie prêdkoœci i k¹tów aktywnego statku
			final int currentSpeed = activeShip.getSpeed();
			final int firstTurnLimit = activeShip.getSpeedTable(1, currentSpeed) * 15;
			final int secoundTurnLimit = activeShip.getSpeedTable(2, currentSpeed) * 15;
			final int thirdTurnLimit = activeShip.getSpeedTable(3, currentSpeed) * 15;
			// final int fourthTurnLimit = activeShip.getSpeedTable(4, currentSpeed) * 15;

			// Dodanie znacznika ruchu
			GameBoard.putToken(BattleGround, activeShip);
			GameBoard.selectMline(BattleGround, selectedLine);
			GameLog.updateLog("Doda³em znacznik ruchu");

			// Kontrola znacznika ruchu z klawiatury
			MainPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
				public void handle(KeyEvent event1) {
					// Prawo
					if (event1.getCode().equals(KeyCode.D)) {
						if (selectedLine == 2 && GameBoard.getFirstAngel() < firstTurnLimit) {
							GameBoard.setFirstAngel(GameBoard.getFirstAngel() + 15);
						}
						if (selectedLine == 3 && GameBoard.getSecoundAngel() < secoundTurnLimit) {
							GameBoard.setSecoundAngel(GameBoard.getSecoundAngel() + 15);
						}
						if (selectedLine == 4 && GameBoard.getThirdAngel() < thirdTurnLimit) {
							GameBoard.setThirdAngel(GameBoard.getThirdAngel() + 15);
						}
						GameBoard.putToken(BattleGround, activeShip);
					}

					// Lewo
					if (event1.getCode().equals(KeyCode.A)) {
						if (selectedLine == 2 && GameBoard.getFirstAngel() > -firstTurnLimit) {
							GameBoard.setFirstAngel(GameBoard.getFirstAngel() - 15);
						}
						if (selectedLine == 3 && GameBoard.getSecoundAngel() > -secoundTurnLimit) {
							GameBoard.setSecoundAngel(GameBoard.getSecoundAngel() - 15);
						}
						if (selectedLine == 4 && GameBoard.getThirdAngel() > -thirdTurnLimit) {
							GameBoard.setThirdAngel(GameBoard.getThirdAngel() - 15);
						}
						GameBoard.putToken(BattleGround, activeShip);
					}
					// Góra
					if (event1.getCode().equals(KeyCode.W)) {
						if (selectedLine < currentSpeed + 1) {
							selectedLine = selectedLine + 1;
							GameBoard.selectMline(BattleGround, selectedLine);

						}
					}
					// Dó³
					if (event1.getCode().equals(KeyCode.S)) {

						if (selectedLine > 2) {
							selectedLine = selectedLine - 1;
							GameBoard.selectMline(BattleGround, selectedLine);

						}
					}
				}
			});

			MainPane.setFocusTraversable(true);
			activity = "End Move";
		}
	}

	public void Attack(MouseEvent event) {
		if (isSelected) {
			activity = "Select Target";

		}

	}

	public void EndMove(MouseEvent event) {

		if (activity.equals("End Move")) {

			GameLog.updateLog("Przmieœci³em statek");
			Point endPoint = GameBoard.getEndPoint();

			// Ustawienie koñcowego k¹ta obrotu po ruchu
			selectedShip.setRotate(selectedShip.getRotate() + GameBoard.getFirstAngel() + GameBoard.getSecoundAngel()
					+ GameBoard.getThirdAngel());

			// Ustawienie koñcowej pozycji po ruchu
			Point leftCorner = GameBoard.getleftCorner(selectedShip);
			selectedShip.setLayoutX(selectedShip.getLayoutX() + endPoint.getX() - leftCorner.getX());
			selectedShip.setLayoutY(selectedShip.getLayoutY() + endPoint.getY() - leftCorner.getY());
			ColorAdjust colorAdjust = new ColorAdjust();
			colorAdjust.setBrightness(0);
			selectedShip.getChildren().get(0).setEffect(colorAdjust);

			isSelected = false;

			// Wyczyszczenie planszy
			GameBoard.RemoveToken(BattleGround);
			Details.cleanDetails();
			// Wy³¹czenie aktywnosci kalwiatury
			MainPane.setDisable(false);
			selectedLine = 2;
		}
	}




}
