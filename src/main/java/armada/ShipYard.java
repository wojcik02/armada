package armada;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ShipYard {
	
	 ObservableList<String> shipNames1 =  FXCollections.observableArrayList( );
	 ArrayList<ArrayList<String>> shipDetails = new ArrayList<ArrayList<String>>();
	 HashMap<?, ?> shipDBase = new HashMap<Object, Object>();
		
	
	public ShipYard() {
		this.shipNames1= FXCollections.observableArrayList(   "Nebulon B",
			        "Niszczyciel",
			        "Victory");
		//this.shipDetails.add(3);
		
	}
	
	
	
	
	
	static ObservableList<String> shipNames = 
		    FXCollections.observableArrayList(
		        "Nebulon B",
		        "Niszczyciel",
		        "Victory"
		    );
	
	
	



	public ObservableList<String> getShipNames() {
		return shipNames1;
	}

	public void setShipNames(ObservableList<String> shipNames) {
		this.shipNames1 = shipNames;
	}
	


}
