package armada;

import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class Ship {

	String name;
	ArrayList<String> speedTable = new ArrayList<String>();
	int speed;
	Pane shipOnBoard;
	Image cardOfShip;
	Image boardModel;
	
	public Pane getShipOnBoard() {
		return shipOnBoard;
	}

	public void setShipOnBoard(Pane shipOnBoard) {
		this.shipOnBoard = shipOnBoard;
	}

	public int getSpeedTable(int step, int currentSpeed) {
		String biere = Character.toString(speedTable.get(currentSpeed - 2).charAt(step - 1));
		return Integer.parseInt(biere);
		
	}

	public void setSpeedTable(ArrayList<String> speedTable) {
		this.speedTable = speedTable;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}


	public Ship(String name, int speed) {
		
		this.name = name;
		this.speedTable.add("123");
		this.speedTable.add("212");
		this.speedTable.add("111");
		this.speed=speed;
		this.boardModel = new Image("IMG/cr92a-base.jpg", 30, 60, false, false);
		
		this.shipOnBoard = new Pane();
		shipOnBoard.setStyle("-fx-background-color: #000000");
		shipOnBoard.setPrefSize(30, 60);
		shipOnBoard.setRotate(0);
		shipOnBoard.getChildren().add(new ImageView(boardModel));
		shipOnBoard.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			public void handle(MouseEvent mouseEvent) {
				if (BoardView.getSelected() == false) {
					BoardView.clickOnShip(shipOnBoard);
					ColorAdjust colorAdjust = new ColorAdjust();
					colorAdjust.setBrightness(0.5);
					shipOnBoard.getChildren().get(0).setEffect(colorAdjust);
				}
			}
		});
		
		
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



}