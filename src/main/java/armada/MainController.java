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
import javafx.scene.effect.Light.Point;
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

public class MainController {

	@FXML
	AnchorPane Board = new AnchorPane();
	@FXML
	ScrollPane ScrollBoard = new ScrollPane();
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
	ImageView card;

	static Ship activeShip;
	static Pane selectedShip;
	int i = 1;
	static boolean isSelected = false;
	static boolean wybrany = false;
	static int selectedLine = 1;
	static String activity = "new";
	static HashMap<Pane, Ship> ships = new HashMap<Pane, Ship>();
	ComboBox<String> comboBox = new ComboBox<String>();
	ArrayList<Ship> rebelShips = new ArrayList<Ship>();
	ArrayList<Ship> empireShips = new ArrayList<Ship>();

	public MainController() {
		updateLog("Zaczynamy");
		DataBase.connect();
		comboBox = new ComboBox<String>(DataBase.getShipList());
		DataBase.disConnect();

	}

	public static int getSelectedLine() {
		return selectedLine;
	}

	public static Pane getSelectedShip() {
		return selectedShip;
	}

	public void NewFleet(MouseEvent event) {

		try {
			BoardView.deselectOnBoard(Board, activeShip);
			BoardView.usunInfo(Right);
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
					BoardView.deselectOnBoard(Board, activeShip);
					BoardView.usunInfo(Right);
					isSelected = false;
					System.out.println("Odznaczony Main 1");
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

	public void AddShip(MouseEvent event) {

		// ScrollBoard.setVvalue(1.0); Przemieszczanei scrolla, na przysz³oœæ

		// TO DO Odznaczanie przez wcisniecie na plansze

		if (isSelected) {

			BoardView.deselectOnBoard(Board, activeShip);
			BoardView.usunInfo(Right);
			Right.getChildren().removeAll(card, comboBox);
			isSelected = false;
			wybrany = false;
		}

		if (wybrany) {
			Right.getChildren().removeAll(card);

			BoardView.uzupe³nijInformacje(Right, activeShip);
			updateLog("Wybrany statek: " + activeShip.getName());

			isSelected = true;
		}

		double x = event.getX() - 15;
		double y = event.getY() - 30;

		if (BoardView.isNear(x, y, ships) == false && activity.equals("Put Ship")) {

			Ship newShip = new Ship(comboBox.getSelectionModel().getSelectedItem().toString());
			DataBase.disConnect();
			x = event.getX() - newShip.getShipOnBoard().getPrefWidth() / 2;
			y = event.getY() - newShip.getShipOnBoard().getPrefHeight() / 2;
			BoardView.dodajStatek(Board, x, y, newShip);
			ships.put(newShip.getShipOnBoard(), newShip);
			updateLog("Doda³em Statek: " + newShip.getName());
			Right.getChildren().removeAll(card, comboBox);
			buttons.getChildren().add(0, newFleet);
			activity = "Nothing";
		}

	}

	public void NewBoard(MouseEvent event) {

		ImageView Background = new ImageView(new Image("IMG/space2.jpg", 1800, 900, false, false));
		Board.getChildren().add(Background);
		ColorAdjust colorAdjust = new ColorAdjust();
		colorAdjust.setBrightness(0.2);
		Board.getChildren().get(0).setEffect(colorAdjust);
		newBoard.setDisable(true);

	}

	// Zaznaczenie wybranego Pane Statku

	public static void selectedShip(Pane ship) {

		wybrany = true;
		selectedShip = ship;
		activeShip = ships.get(selectedShip);

	}

	// Ruch statku

	public void MoveShip(MouseEvent event) {

		if (activity.equals("Move Ship")) {

			// Pobranie prêdkoœci i k¹tów aktywnego statku
			final int currentSpeed = activeShip.getSpeed();
			final int firstTurnLimit = activeShip.getSpeedTable(1, currentSpeed) * 15;

			final int secoundTurnLimit = activeShip.getSpeedTable(2, currentSpeed) * 15;
			final int thirdTurnLimit = activeShip.getSpeedTable(3, currentSpeed) * 15;

			// Dodanie znacznika ruchu
			BoardView.putToken(Board, activeShip);

			BoardView.selectMline(Board, selectedLine);

			updateLog("Doda³em znacznik ruchu");

			// Kontrola znacznika ruchu z klawiatury
			MainPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
				public void handle(KeyEvent event1) {
					// Prawo
					if (event1.getCode().equals(KeyCode.D)) {
						if (selectedLine == 2 && BoardView.getFirstAngel() < firstTurnLimit) {
							BoardView.setFirstAngel(BoardView.getFirstAngel() + 15);
						}
						if (selectedLine == 3 && BoardView.getSecoundAngel() < secoundTurnLimit) {
							BoardView.setSecoundAngel(BoardView.getSecoundAngel() + 15);
						}
						if (selectedLine == 4 && BoardView.getThirdAngel() < thirdTurnLimit) {
							BoardView.setThirdAngel(BoardView.getThirdAngel() + 15);
						}
						BoardView.putToken(Board, activeShip);
					}

					// Lewo
					if (event1.getCode().equals(KeyCode.A)) {
						if (selectedLine == 2 && BoardView.getFirstAngel() > -firstTurnLimit) {
							BoardView.setFirstAngel(BoardView.getFirstAngel() - 15);
						}
						if (selectedLine == 3 && BoardView.getSecoundAngel() > -secoundTurnLimit) {
							BoardView.setSecoundAngel(BoardView.getSecoundAngel() - 15);
						}
						if (selectedLine == 4 && BoardView.getThirdAngel() > -thirdTurnLimit) {
							BoardView.setThirdAngel(BoardView.getThirdAngel() - 15);
						}
						BoardView.putToken(Board, activeShip);
					}
					// Góra
					if (event1.getCode().equals(KeyCode.W)) {
						if (selectedLine < currentSpeed) {
							selectedLine = selectedLine + 1;
							BoardView.selectMline(Board, selectedLine);

						}
					}
					// Dó³
					if (event1.getCode().equals(KeyCode.S)) {

						if (selectedLine > 1) {
							selectedLine = selectedLine - 1;
							BoardView.selectMline(Board, selectedLine);

						}
					}
				}
			});

			MainPane.setFocusTraversable(true);
			activity = "End Move";
		}
	}

	public void EndMove(MouseEvent event) {
		if (activity.equals("End Move")) {

			updateLog("Przmieœci³em statek");
			Point endPoint = BoardView.getEndPoint();

			// Ustawienie koñcowego k¹ta obrotu po ruchu
			selectedShip.setRotate(selectedShip.getRotate() + BoardView.getFirstAngel() + BoardView.getSecoundAngel()
					+ BoardView.getThirdAngel());

			// Ustawienie koñcowej pozycji po ruchu
			Point leftCorner = BoardView.getleftCorner(selectedShip);
			selectedShip.setLayoutX(selectedShip.getLayoutX() + endPoint.getX() - leftCorner.getX());
			selectedShip.setLayoutY(selectedShip.getLayoutY() + endPoint.getY() - leftCorner.getY());

			BoardView.deselectOnBoard(Board, activeShip);
			BoardView.usunInfo(Right);
			isSelected = false;
			System.out.println("Odznaczony Main 3");

			// Wyczyszczenie planszy
			BoardView.RemoveToken(Board);

			// Ustawienie aktywnoœci na 0
			activity = "Nothing";

			// Wy³¹czenie aktywnosci kalwiatury
			MainPane.setDisable(false);
			selectedLine = 1;
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

	int lineNumber = 0;

	public void updateLog(String newText) {

        

		Log.setText(Log.getText() + "\n" + lineNumber + ": " + newText);
		Log.selectPositionCaret(Log.getLength());
		Log.deselect();
		lineNumber++;

	}

}
