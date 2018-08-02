package armada;

import java.util.HashMap;
import java.util.Map.Entry;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Light.Point;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class GameBoard {

	@FXML
	AnchorPane Board;
	MoveToken MoveToken = new MoveToken();
	
	

	
	
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
	
	public void putToken() {
		cleanBoard();
		MoveToken.setMoveToken();
		MoveToken.putMoveToken();
		System.out.println("K³ade znacznik");
		
	}
	
	public void cleanBoard() {
		this.Board.getChildren().removeAll(MoveToken.Mline1, MoveToken.Mline2, MoveToken.Mline3, MoveToken.Mline4, MoveToken.endMarker);

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

	

	
	



}
