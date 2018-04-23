package armada;

import java.util.HashMap;
import java.util.Map.Entry;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Light.Point;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class BoardView {
	
	
	public static boolean isSelected;

	@FXML AnchorPane Board;
	
	//INFORMACJE ELEMNTY
	static Label name = new Label();
	static Label speed = new Label();

	
	//MOVE TOKEN ELEMENTY
	static Line Mline1 = new Line();
	static Line Mline2 = new Line();
	static Line Mline3 = new Line();
	static Line Mline4 = new Line();
	static Point endPoint = new Point();
	static double angle = 0;
	static double firstAngel = 0;
	static double secoundAngel = 0;
	static double thirdAngel = 0;

	
	public static void usunInfo(VBox box) {
		box.getChildren().removeAll(name,speed);
		System.out.println("usuwam info");

	}
	
	public static void uzupe³nijInformacje(VBox box, Ship activeShip) {

		name.setText(activeShip.getName());
		speed.setText("Prêdkoœæ "+ activeShip.getSpeed());
			
				box.getChildren().removeAll(name,speed);
				box.getChildren().addAll(name,speed);
	
	}
	
	public static void deselectOnBoard(AnchorPane Board, Ship ship) {
		
		// Odznaczenie statku na planszy
		ColorAdjust colorAdjust = new ColorAdjust();
		colorAdjust.setBrightness(0);
		ship.getShipOnBoard().getChildren().get(0).setEffect(colorAdjust);

		// Wyl¹czenie atywnego statku
		BoardView.setSelected(false);
		

		// Ustawienie aktywnoœci na 0
		MainController.setActivity("Nothing");
		
	}

	

	// Pobranie lewego górnego roku Pane Statku
	public static Point getleftCorner(Pane ship) {
		double angle = ship.getRotate();
		ship.setRotate(0);
		double xo = ship.getLayoutX() + 15;
		double yo = ship.getLayoutY() + 30;
		ship.setRotate(angle);
		double deltaX = Math.sin(
				Math.toRadians((angle - Math.toDegrees(Math.asin(15 / Math.sqrt(Math.pow(15, 2) + Math.pow(30, 2)))))))
				* Math.sqrt(Math.pow(15, 2) + Math.pow(30, 2));
		double deltaY = Math.cos(
				Math.toRadians((angle - Math.toDegrees(Math.asin(15 / Math.sqrt(Math.pow(15, 2) + Math.pow(30, 2)))))))
				* Math.sqrt(Math.pow(15, 2) + Math.pow(30, 2));
		Point leftCorner = new Point();
		leftCorner.setX(xo + deltaX);
		leftCorner.setY(yo - deltaY);
		return leftCorner;
	}

	// Sprawdzenie czy w wybranym miejscu jest inny Statek
	public static boolean isNear(double x, double y, HashMap<Pane, Ship> ships) {
		for (Entry<Pane, Ship> pair : ships.entrySet()) {

			if (Math.abs(pair.getKey().getLayoutX() - x) < 30 && Math.abs(pair.getKey().getLayoutY() - y) < 60) {
				return true;
			}
		}
		return false;
	}
	
	public static void dodajStatek(AnchorPane Board, double x, double y, Ship ship) {

		ship.getShipOnBoard().setLayoutX(x);
		ship.getShipOnBoard().setLayoutY(y);
		Board.getChildren().add(ship.getShipOnBoard());
	}


	// Klikniecie na Pane Statku
	public static void clickOnShip(Pane ship) {
		if (isSelected) {
		} else if (MainController.getActivity().equals("Nothing")) {
			MainController.selectedShip(ship);
			isSelected = true;
			MainController.setActivity("Move Ship");
		}
	}
	


	public static void isSelected() {
		isSelected = true;
	}

	public static void setSelected(boolean isSelected) {
		BoardView.isSelected = isSelected;
	}

	public static boolean getSelected() {
		return isSelected;
	}
	

	public static double getThirdAngel() {
		return thirdAngel;
	}

	public static void setThirdAngel(double thirdAngel) {
		BoardView.thirdAngel = thirdAngel;
	}

	public static double getFirstAngel() {
		return firstAngel;
	}

	public static void setFirstAngel(double firstAngel) {
		BoardView.firstAngel = firstAngel;
	}

	public static double getSecoundAngel() {
		return secoundAngel;
	}

	public static void setSecoundAngel(double secoundAngel) {
		BoardView.secoundAngel = secoundAngel;
	}

	public static void selectMline(AnchorPane Board, int i) {

		Mline1.setStroke(Color.BLACK);
		Mline2.setStroke(Color.BLACK);
		Mline3.setStroke(Color.BLACK);
		Mline4.setStroke(Color.BLACK);

		if (MainController.getSelectedLine() == 1) {
			Mline1.setStroke(Color.RED);
		}
		if (MainController.getSelectedLine() == 2) {
			Mline2.setStroke(Color.RED);
		}
		if (MainController.getSelectedLine() == 3) {
			Mline3.setStroke(Color.RED);
		}
		if (MainController.getSelectedLine() == 4) {
			Mline4.setStroke(Color.RED);
		}
	}

	public static void putToken(AnchorPane Board,  Ship activeShip) {

		Board.getChildren().removeAll(Mline1, Mline2, Mline3, Mline4);
		activeShip.getSpeed();
		 Pane ship = activeShip.getShipOnBoard();
			Point leftCorner = BoardView.getleftCorner(ship);


		Mline1.setStartX(leftCorner.getX());
		Mline1.setStartY(leftCorner.getY());
		Mline1.setEndX(leftCorner.getX() + Math.sin(Math.toRadians(ship.getRotate())) * 45);
		Mline1.setEndY(leftCorner.getY() - Math.cos(Math.toRadians(ship.getRotate())) * 45);
		endPoint.setX(Mline1.getEndX());
		endPoint.setY(Mline1.getEndY());
		Board.getChildren().addAll(Mline1);

		if (activeShip.getSpeed() > 1) {

			Mline2.setStartX(Mline1.getEndX());
			Mline2.setStartY(Mline1.getEndY());
			Mline2.setEndX(Mline1.getEndX() + Math.sin(Math.toRadians(ship.getRotate() + firstAngel)) * 45);
			Mline2.setEndY(Mline1.getEndY() - Math.cos(Math.toRadians(ship.getRotate() + firstAngel)) * 45);
			endPoint.setX(Mline2.getEndX());
			endPoint.setY(Mline2.getEndY());
			Board.getChildren().addAll(Mline2);

		}

		if (activeShip.getSpeed() > 2) {
			Mline3.setStartX(Mline2.getEndX());
			Mline3.setStartY(Mline2.getEndY());
			Mline3.setEndX(Mline2.getEndX() + Math.sin(Math.toRadians(ship.getRotate() + secoundAngel + firstAngel)) * 45);
			Mline3.setEndY(Mline2.getEndY() - Math.cos(Math.toRadians(ship.getRotate() + secoundAngel + firstAngel)) * 45);
			endPoint.setX(Mline3.getEndX());
			endPoint.setY(Mline3.getEndY());
			Board.getChildren().addAll(Mline3);
		}

		if (activeShip.getSpeed() > 3) {
			Mline4.setStartX(Mline3.getEndX());
			Mline4.setStartY(Mline3.getEndY());
			Mline4.setEndX(Mline3.getEndX()+ Math.sin(Math.toRadians(ship.getRotate() + secoundAngel + firstAngel + thirdAngel)) * 45);
			Mline4.setEndY(Mline3.getEndY()- Math.cos(Math.toRadians(ship.getRotate() + secoundAngel + firstAngel + thirdAngel)) * 45);
			endPoint.setX(Mline4.getEndX());
			endPoint.setY(Mline4.getEndY());
			Board.getChildren().addAll(Mline4);
		}
		

	}

	public static Point getEndPoint() {
		return endPoint;
	}

	public static void RemoveToken(AnchorPane Board) {
		firstAngel = 0;
		secoundAngel = 0;
		thirdAngel = 0;
		Board.getChildren().removeAll(Mline1, Mline2, Mline3, Mline4);
	}

}
