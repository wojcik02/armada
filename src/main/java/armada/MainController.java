package armada;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Light;
import javafx.scene.effect.Light.Point;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class MainController {

	@FXML
	ScrollPane ScrollBoard = new ScrollPane();
	@FXML
	ScrollPane MainBoard = new ScrollPane();
	@FXML
	GridPane MainPane = new GridPane();
	@FXML
	TextArea Log = new TextArea();
	@FXML

	VBox Right;
	@FXML
	Button newFleet;
	@FXML
	Button newBoard;
	@FXML
	HBox buttons;
	static ImageView card;

	int lineNumber = 0;
	static Ship activeShip;
	static Pane selectedShipTarget;
	static Ship Target;
	static Pane newSelectedShip;
	static Pane selectedShip;
	int i = 1;
	static boolean isSelected = false;
	static boolean newSelection = false;
	static boolean attack = false;
	boolean wybrany = false;
	static int selectedLine = 2;
	static String activity = "new";
	static HashMap<Pane, Ship> ships = new HashMap<Pane, Ship>();
	static ComboBox<String> comboBox = new ComboBox<String>();
	ArrayList<Ship> rebelShips = new ArrayList<Ship>();
	ArrayList<Ship> empireShips = new ArrayList<Ship>();

	static GameBoard Board = new GameBoard();
	AnchorPane BattleGround = Board.getBoard();

	public MainController() {

	}

	public void initialize() {

		MainBoard.setContent(Board.getBoard());
		DataBase.connect();
		comboBox = new ComboBox<String>(DataBase.getShipList());
		DataBase.disConnect();
		

	}

	public static void gameEvent(double x, double y) {
		System.out.println("Robie zdarzenie");

			if(activity.equals("Put Ship")) {
			Ship newShip = new Ship(comboBox.getSelectionModel().getSelectedItem().toString());
			x = x - newShip.getShipOnBoard().getPrefWidth() / 2;
			y = y - newShip.getShipOnBoard().getPrefHeight() / 2;
			Board.addShip(x, y, newShip);
			ships.put(newShip.getShipOnBoard(), newShip);
		    //updateLog("Doda³em Statek: " + newShip.getName());
			// Right.getChildren().removeAll(card, comboBox);
		   // buttons.getChildren().add(0, newFleet);
			activity = "Nothing";
			}
			
			if (isSelected && activity.equals("Nothing")) {
			//	GameBoard.deselectOnBoard(BattleGround, activeShip);
				activeShip.Deselect();
			
				//GameBoard.usunInfo(Right);
			//	Right.getChildren().removeAll(card, comboBox);
				isSelected = false;
			//	GameBoard.RemoveToken(BattleGround);
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

		try {

			GameBoard.deselectOnBoard(BattleGround, activeShip);
			GameBoard.usunInfo(Right);
			Right.getChildren().removeAll(card, comboBox);
			isSelected = false;
			wybrany = false;
		} catch (Exception e) {
		}

		// TODO po³¹czenie z baz¹ przy budowaniu floty raz, zamkniecie przy rozpoczeciu
		// rozgrywki
		comboBox.setPrefWidth(250);
		comboBox.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				DataBase.connect();

				if (isSelected) {
					GameBoard.deselectOnBoard(BattleGround, activeShip);
					GameBoard.usunInfo(Right);
					isSelected = false;
				}

				activity = "Put Ship";
				Right.getChildren().removeAll(card);
				card = new ImageView(new Image(
						"IMG/" + DataBase.getImgUrlDB(comboBox.getSelectionModel().getSelectedItem().toString()), 285,
						490, false, false));
				Right.getChildren().addAll(card);
				DataBase.disConnect();
			}
		});

		Right.getChildren().add(comboBox);
		buttons.getChildren().remove(newFleet);

	}

	// Klikniecie na plaszê

	public void AddShip(MouseEvent event) {

		// ScrollBoard.setVvalue(1.0); Przemieszczanei scrolla, na przysz³oœæ

		if (isSelected && activity.equals("Nothing")) {
			GameBoard.deselectOnBoard(BattleGround, activeShip);
			activeShip.Deselect();
			ColorAdjust colorAdjust = new ColorAdjust();
			colorAdjust.setBrightness(0);
			selectedShip.getChildren().get(0).setEffect(colorAdjust);
			GameBoard.usunInfo(Right);
			Right.getChildren().removeAll(card, comboBox);
			isSelected = false;
			GameBoard.RemoveToken(BattleGround);
			activity = "Nothing";

		}

		if (newSelection) {
			MainController.selectedShip(newSelectedShip);
			Right.getChildren().removeAll(card);
			GameBoard.uzupe³nijInformacje(Right, activeShip);
			updateLog("Wybrany statek: " + activeShip.getName());
			isSelected = true;
			newSelection = false;
		}

	}

	// Zaznaczenie wybranego Pane Statku

	public static void selectedShip(Pane newSelectedShip) {

		activeShip = ships.get(newSelectedShip);
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
			updateLog("Doda³em znacznik ruchu");

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

			updateLog("Przmieœci³em statek");
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
			GameBoard.usunInfo(Right);

			// Wy³¹czenie aktywnosci kalwiatury
			MainPane.setDisable(false);
			selectedLine = 2;
		}
	}

	public static String getActivity() {
		return activity;
	}

	public void cleanRight() {
		Right.getChildren().removeAll();
	}

	public static void setActivity(String activity) {
		MainController.activity = activity;
	}

	public void updateLog(String newText) {
		Log.setText(Log.getText() + "\n" + lineNumber + ": " + newText);
		Log.selectPositionCaret(Log.getLength());
		Log.deselect();
		lineNumber++;
	}

}
