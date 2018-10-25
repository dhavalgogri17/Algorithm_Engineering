import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashSet;

public class Cell {

	ArrayList<SensorPoint> sensorPoints = new ArrayList<SensorPoint>();

	public ArrayList<SensorPoint> getSensorPoints()
	{
		return sensorPoints;
	}
	
	public void setSensorPoints(ArrayList<SensorPoint> sensorPoints)
	{
		this.sensorPoints = sensorPoints;
	}
	
	public void addSensorPoints(SensorPoint p) {
		sensorPoints.add(p);
	}

}
