package armada;

import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class Ship {
	
	
	static boolean isSelectedShip;
	String name;
	ArrayList<String> speedTable = new ArrayList<String>();
	int speed;
	Pane shipOnBoard;
	Image cardOfShip;
	Image boardModel;
	int size;
	
	public Image getCardOfShip() {
		return cardOfShip;
	}

	public void setCardOfShip(Image cardOfShip) {
		this.cardOfShip = cardOfShip;
	}
	
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
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public Ship(String name) {
		DataBase.connect();
		
		this.name = name;
		this.speedTable.add("123");
		this.speedTable.add("212");
		this.speedTable.add("111");
		this.speed=DataBase.getSpeedDB(name);
		System.out.println("Ustawiam pr�dko�� "+this.speed);
		this.size = DataBase.getSizeDb(name);
		String CardImgUlr= "IMG/"+DataBase.getImgUrlDB(name);
		this.cardOfShip = new Image( CardImgUlr, 285, 490, false, false);
		this.shipOnBoard = new Pane();
		switch(size) {
		case 1: 
			this.boardModel = new Image("IMG/cr92a-base.jpg", 41, 71, false, false);
			shipOnBoard.setPrefSize(41, 71);
        break;
		case 2:
			this.boardModel = new Image("IMG/cr92a-base.jpg", 61, 102, false, false);
			shipOnBoard.setPrefSize(61, 102);
        break;
		case 3: 
			this.boardModel = new Image("IMG/cr92a-base.jpg", 76, 129, false, false);
			shipOnBoard.setPrefSize(76, 129);
        break;
		}
		
		shipOnBoard.setStyle("-fx-background-color: #000000");
		shipOnBoard.setRotate(0);
		shipOnBoard.getChildren().add(new ImageView(boardModel));
		shipOnBoard.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			public void handle(MouseEvent mouseEvent) {

				if (MainController.isSelected == false) {
					BoardView.clickOnShip(shipOnBoard);
					ColorAdjust colorAdjust = new ColorAdjust();
					colorAdjust.setBrightness(0.5);
					shipOnBoard.getChildren().get(0).setEffect(colorAdjust);
				}
			}
		});
		
		
	DataBase.disConnect();	
	}





}