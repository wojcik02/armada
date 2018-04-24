package armada;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.effect.Light.Point;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class MainController {

	@FXML
	AnchorPane Board;
	@FXML
	VBox MainPane;
	@FXML
	TextArea Log;
	@FXML
	VBox Right;
	@FXML
	Button addShipButton = new Button();
	@FXML Button newFleet;
	@FXML HBox buttons;

	static Ship activeShip;
	static Pane selectedShip;
	boolean wybrany;
	int i = 1;
	boolean isSelected = false;
	static String activity = "new";
	static HashMap<Pane, Ship> ships = new HashMap<Pane, Ship>();
	ComboBox<String> comboBox = new ComboBox<String>();
	

	ArrayList<Ship> rebelShips = new ArrayList<Ship>();
	ArrayList<Ship> empireShips = new ArrayList<Ship>();

	public MainController() {
		System.out.println("Odpalamy gre");

	}

	static int selectedLine = 1;

	public static int getSelectedLine() {
		return selectedLine;
	}

	public static Pane getSelectedShip() {
		return selectedShip;
	}

	public static void setSelectedShip(Pane selectedShip) {
		MainController.selectedShip = selectedShip;
	}

	public void NewFleet(MouseEvent event) {
		
		DataBase.connect();
		comboBox = new ComboBox<String>(DataBase.getShipList());
		DataBase.disConnect();

		Right.getChildren().add(comboBox);
		buttons.getChildren().remove(newFleet);
		addShipButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if (wybrany) {
					BoardView.deselectOnBoard(Board, activeShip);
					BoardView.usunInfo(Right);
					wybrany = false;
				}
				DataBase.connect();
				activity = "Put Ship";
			}
		});
		addShipButton.setText("Dodaj");
		Right.getChildren().add(addShipButton);

	}

	public void AddShip(MouseEvent event) {

		// TO DO Odznaczanie przez wcisniecie na plansze

		if (wybrany) {
			
			BoardView.deselectOnBoard(Board, activeShip);
			BoardView.usunInfo(Right);
			wybrany = false;
		}

		double x = event.getX() - 15;
		double y = event.getY() - 30;

		if (BoardView.isNear(x, y, ships) == false && activity.equals("Put Ship")) {
			
			Ship newShip = new Ship(comboBox.getSelectionModel().getSelectedItem().toString());
			DataBase.disConnect();
			BoardView.dodajStatek(Board, x, y, newShip);

			ships.put(newShip.getShipOnBoard(), newShip);
			Log.setText("Doda³em statek");
			activity = "Nothing";
		}

		// TODO

		if (BoardView.getSelected()) {
			BoardView.uzupe³nijInformacje(Right, activeShip);
			wybrany = true;

		}
	}

	// Zaznaczenie wybranego Pane Statku

	public static void selectedShip(Pane ship) {
		BoardView.isSelected();
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

			Log.setText("Doda³êm znacznik ruchu");

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
						if (selectedLine == 4 &&BoardView.getThirdAngel() < thirdTurnLimit) {
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

			Log.setText("Przemieœci³em statek");
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
			wybrany = false;
			
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
	
	

	public static void setActivity(String activity) {
		MainController.activity = activity;
	}

}
