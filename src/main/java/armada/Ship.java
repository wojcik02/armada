package armada;

import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Light.Point;
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
	
	public void Move() {
		
		Point endPoint = MoveToken.endPoint;

		this.shipOnBoard.setRotate(this.shipOnBoard.getRotate() + MoveToken.firstAngel + MoveToken.secoundAngel+ MoveToken.thirdAngel);

		Point leftCorner = GameBoard.getleftCorner(this.shipOnBoard);
		this.shipOnBoard.setLayoutX(this.shipOnBoard.getLayoutX() + endPoint.getX() - leftCorner.getX());
		this.shipOnBoard.setLayoutY(this.shipOnBoard.getLayoutY() + endPoint.getY() - leftCorner.getY());
		Deselect();
	}

	public void Selected() {

		System.out.println("Klik na statek : " + this.shipOnBoard);
		ColorAdjust colorAdjust = new ColorAdjust();
		colorAdjust.setBrightness(0.5);
		this.shipOnBoard.getChildren().get(0).setEffect(colorAdjust);

	}

	public void Deselect() {

		ColorAdjust colorAdjust = new ColorAdjust();
		colorAdjust.setBrightness(0);
		this.shipOnBoard.getChildren().get(0).setEffect(colorAdjust);
		MainController.isSelected = false;
		MainController.activity="Nothing";
		MainController.Board.cleanBoard();

	}

	public Ship(String name) {
		DataBase.connect();

		this.name = name;
		this.speedTable.add(DataBase.getSpeedTableDB(name).substring(0, 4));
		this.speedTable.add(DataBase.getSpeedTableDB(name).substring(4, 8));
		this.speedTable.add(DataBase.getSpeedTableDB(name).substring(8, 12));
		this.speedTable.add(DataBase.getSpeedTableDB(name).substring(12, 16));
		this.speed = 3;
		this.size = DataBase.getSizeDb(name);
		this.maxSpeed = DataBase.getMaxSpeedDB(name);
		this.cardOfShip = new Image("IMG/" + DataBase.getImgUrlDB(name), 285, 490, false, false);
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
			this.shipOnBoard.setPrefSize(76, 129);
			break;
		}

		shipOnBoard.setRotate(0);
		shipOnBoard.getChildren().add(new ImageView(boardModel));
		shipOnBoard.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

			public void handle(MouseEvent mouseEvent) {
				MainController.selectedShip(shipOnBoard);
			}
		});

		DataBase.disConnect();
	}


}