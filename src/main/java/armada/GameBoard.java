package armada;

import java.util.HashMap;
import java.util.Map.Entry;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Light.Point;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class GameBoard {

	@FXML
	AnchorPane Board;
	
	

	// INFORMACJE ELEMNTY
	static Label name = new Label();
	static Label speed = new Label();
	static ImageView card;
	static TextArea info;

	static // MOVE TOKEN ELEMENTY
	int moveTokenLength = 65;
	static Line Mline1 = new Line();
	static Line Mline2 = new Line();
	static Line Mline3 = new Line();
	static Line Mline4 = new Line();
	static Point endPoint = new Point();
	static double angle = 0;
	static double firstAngel = 0;
	static double secoundAngel = 0;
	static double thirdAngel = 0;
	static Pane endMarker = new Pane();
	
	
	public GameBoard() {
		
		this.Board = new AnchorPane();
		ImageView Background = new ImageView(new Image("IMG/space2.jpg", 1800, 900, false, false));
		Board.getChildren().add(Background);
		ColorAdjust colorAdjust = new ColorAdjust();
		colorAdjust.setBrightness(0.2);
		Board.getChildren().get(0).setEffect(colorAdjust);
		Board.setOnMouseClicked(new BoardClickHandle());
		System.out.println("Stworzy³em planszê");
	}
	
	
	
	
	 private class BoardClickHandle implements EventHandler<MouseEvent>
	    {
	        
	        public void handle(MouseEvent event)
	        {
	        	double x = event.getX();
	        	double y = event.getY();
	        	MainController.gameEvent(x,y);
	        }
	        
	        
	    }
	
	public  AnchorPane getBoard() {
		return Board;
	}
	
	public void addShip(double x, double y, Ship newShip) {
		newShip.getShipOnBoard().setLayoutX(x);
		newShip.getShipOnBoard().setLayoutY(y);
		System.out.println("Dodaje statek");
	    this.Board.getChildren().add(newShip.getShipOnBoard());
	}



	
	
	
	public static void usunInfo(VBox box) {
		box.getChildren().removeAll(info, card);
	}

	public static void uzupe³nijInformacje(VBox box, Ship activeShip) {

		box.getChildren().removeAll(card);
		card = new ImageView(activeShip.getCardOfShip());
		box.getChildren().addAll(card);

	}

	public static void deselectOnBoard(AnchorPane Board, Ship ship) {

		// Odznaczenie statku na planszy

	}

	// Pobranie lewego górnego roku Pane Statku
	public static Point getleftCorner(Pane ship) {
		double angle = ship.getRotate();
		ship.setRotate(0);
		double ShipH = ship.getPrefHeight() / 2;
		double ShipV = ship.getPrefWidth() / 2;

		double xo = ship.getLayoutX() + ShipV;
		double yo = ship.getLayoutY() + ShipH;
		ship.setRotate(angle);
		double deltaX = Math.sin(Math.toRadians(
				(angle - Math.toDegrees(Math.asin(ShipV / Math.sqrt(Math.pow(ShipV, 2) + Math.pow(ShipH, 2)))))))
				* Math.sqrt(Math.pow(ShipV, 2) + Math.pow(ShipH, 2));
		double deltaY = Math.cos(Math.toRadians(
				(angle - Math.toDegrees(Math.asin(ShipV / Math.sqrt(Math.pow(ShipV, 2) + Math.pow(ShipH, 2)))))))
				* Math.sqrt(Math.pow(ShipV, 2) + Math.pow(ShipH, 2));
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

	

	public static double getThirdAngel() {
		return thirdAngel;
	}

	public static void setThirdAngel(double thirdAngel) {
		GameBoard.thirdAngel = thirdAngel;
	}

	public static double getFirstAngel() {
		return firstAngel;
	}

	public static void setFirstAngel(double firstAngel) {
		GameBoard.firstAngel = firstAngel;
	}

	public static double getSecoundAngel() {
		return secoundAngel;
	}

	public static void setSecoundAngel(double secoundAngel) {
		GameBoard.secoundAngel = secoundAngel;
	}

	public static void selectMline(AnchorPane Board, int i) {

		Mline1.setStroke(Color.GREY);
		Mline2.setStroke(Color.GREY);
		Mline3.setStroke(Color.GREY);
		Mline4.setStroke(Color.GREY);
		Mline1.setStrokeWidth(4);
		Mline2.setStrokeWidth(4);
		Mline3.setStrokeWidth(4);
		Mline4.setStrokeWidth(4);

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

	public static void putToken(AnchorPane Board, Ship activeShip) {

		Board.getChildren().removeAll(Mline1, Mline2, Mline3, Mline4, endMarker);
		activeShip.getSpeed();
		Pane ship = activeShip.getShipOnBoard();
		Point leftCorner = GameBoard.getleftCorner(ship);

		Mline1.setStartX(leftCorner.getX());
		Mline1.setStartY(leftCorner.getY());
		Mline1.setEndX(leftCorner.getX() + Math.sin(Math.toRadians(ship.getRotate())) * moveTokenLength);
		Mline1.setEndY(leftCorner.getY() - Math.cos(Math.toRadians(ship.getRotate())) * moveTokenLength);

		Mline2.setStartX(Mline1.getEndX());
		Mline2.setStartY(Mline1.getEndY());
		Mline2.setEndX(Mline1.getEndX() + Math.sin(Math.toRadians(ship.getRotate() + firstAngel)) * moveTokenLength);
		Mline2.setEndY(Mline1.getEndY() - Math.cos(Math.toRadians(ship.getRotate() + firstAngel)) * moveTokenLength);
		
		Mline3.setStartX(Mline2.getEndX());
		Mline3.setStartY(Mline2.getEndY());
		Mline3.setEndX(Mline2.getEndX()
				+ Math.sin(Math.toRadians(ship.getRotate() + secoundAngel + firstAngel)) * moveTokenLength);
		Mline3.setEndY(Mline2.getEndY()
				- Math.cos(Math.toRadians(ship.getRotate() + secoundAngel + firstAngel)) * moveTokenLength);
		
		Mline4.setStartX(Mline3.getEndX());
		Mline4.setStartY(Mline3.getEndY());
		Mline4.setEndX(Mline3.getEndX()
				+ Math.sin(Math.toRadians(ship.getRotate() + secoundAngel + firstAngel + thirdAngel))
						* moveTokenLength);
		Mline4.setEndY(Mline3.getEndY()
				- Math.cos(Math.toRadians(ship.getRotate() + secoundAngel + firstAngel + thirdAngel))
						* moveTokenLength);

		endPoint.setX(Mline1.getEndX());
		endPoint.setY(Mline1.getEndY());
		Board.getChildren().addAll(Mline1, Mline2, Mline3, Mline4);

		if (activeShip.getSpeed() > 1) {
			
			endPoint.setX(Mline2.getEndX());
			endPoint.setY(Mline2.getEndY());
		}

		if (activeShip.getSpeed() > 2) {
			
			endPoint.setX(Mline3.getEndX());
			endPoint.setY(Mline3.getEndY());
		}
		

			endMarker.setPrefHeight(activeShip.getShipOnBoard().getPrefHeight());
			endMarker.setLayoutX(activeShip.getShipOnBoard().getLayoutX());
			endMarker.setLayoutY(activeShip.getShipOnBoard().getLayoutY());
			endMarker.setRotate(activeShip.getShipOnBoard().getRotate());

			endMarker.setPrefWidth(activeShip.getShipOnBoard().getPrefWidth());
			endMarker.setStyle("-fx-background-color: rgba(0, 100, 100, 0.5); -fx-background-radius: 5;");

			endMarker.setRotate(endMarker.getRotate() + GameBoard.getFirstAngel() + GameBoard.getSecoundAngel()
					+ GameBoard.getThirdAngel());

			// Ustawienie koñcowej pozycji po ruchu
			Point leftCornerMarker = GameBoard.getleftCorner(endMarker);
			endMarker.setLayoutX(activeShip.getShipOnBoard().getLayoutX() + endPoint.getX() - leftCornerMarker.getX());
			endMarker.setLayoutY(activeShip.getShipOnBoard().getLayoutY() + endPoint.getY() - leftCornerMarker.getY());
			Board.getChildren().addAll(endMarker);

		

	}

	public static Point getEndPoint() {
		return endPoint;
	}

	public static void RemoveToken(AnchorPane Board) {
		firstAngel = 0;
		secoundAngel = 0;
		thirdAngel = 0;
		Board.getChildren().removeAll(Mline1, Mline2, Mline3, Mline4, endMarker);
	}

	



}
