package armada;

import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class Ship {

	static boolean isSelectedShip;
	String name;
	ArrayList<String> speedTable = new ArrayList<String>();
	int speed;
	int maxSpeed;
	Pane shipOnBoard;
	Image cardOfShip;
	Image boardModel;
	int size;
	String fraction;

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
		String biere = Character.toString(speedTable.get(currentSpeed - 1).charAt(step - 1));
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
		this.speedTable.add(DataBase.getSpeedTableDB(name).substring(0, 4));
		this.speedTable.add(DataBase.getSpeedTableDB(name).substring(4, 8));
		this.speedTable.add(DataBase.getSpeedTableDB(name).substring(8, 12));
		this.speedTable.add(DataBase.getSpeedTableDB(name).substring(12, 16));
		
		System.out.println(DataBase.getSpeedTableDB(name).substring(0, 4));
		System.out.println(DataBase.getSpeedTableDB(name).substring(4, 8));
		System.out.println(DataBase.getSpeedTableDB(name).substring(8, 12));



		this.speed = 3;
		this.size = DataBase.getSizeDb(name);
		this.maxSpeed = DataBase.getMaxSpeedDB(name);
		String CardImgUlr = "IMG/" + DataBase.getImgUrlDB(name);
		this.cardOfShip = new Image(CardImgUlr, 285, 490, false, false);
		this.fraction = DataBase.getFraction(name);
		this.shipOnBoard = new Pane();
		switch (size) {
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

		shipOnBoard.setRotate(0);
		shipOnBoard.getChildren().add(new ImageView(boardModel));
		shipOnBoard.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			
			public void handle(MouseEvent mouseEvent) {
				MainController.newSelection = true;
				MainController.newSelectedShip = shipOnBoard;
			}
		});

		DataBase.disConnect();
	}

}