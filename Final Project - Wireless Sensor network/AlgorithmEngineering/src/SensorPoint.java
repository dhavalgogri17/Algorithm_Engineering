import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;

public class SensorPoint {
	private int x;
	private int y;
	private ArrayList<SensorPoint> neighbourSensorPoints = new ArrayList<SensorPoint>();
	private ArrayList<SensorPoint> neighbourSensorPointsBeforeDeleted = new ArrayList<SensorPoint>();
	private int cellNumberX = -1;
	private int cellNumberY = -1;
	private int numberOfSubCells = -1;
	private int degree = 0;
	private int oldDegreeWhenDeleted = 0;
	private int degreeWhenDeleted = 0;
	private int colorID = Integer.MAX_VALUE;
	private double sensorRadius = 0;
	private ArrayList<ArrayList<SensorPoint>> neighborColorPoints = new ArrayList<ArrayList<SensorPoint>>();
	private ArrayList<ArrayList<SensorPoint>> neighborColorPointsAfterBFS = new ArrayList<ArrayList<SensorPoint>>();


	public ArrayList<SensorPoint> getNeighbourSensorPoints()
	{
		return neighbourSensorPoints;
	}

	public ArrayList<SensorPoint> getNeighbourSensorPointsBeforeDeleted()
	{
		return neighbourSensorPointsBeforeDeleted;
	}

	public void setNeighbourSensorPoints(ArrayList<SensorPoint> neighbourSensorPoints)
	{
		this.neighbourSensorPoints = neighbourSensorPoints;
	}

	public void addNeighbour(SensorPoint p)
	{
		neighbourSensorPoints.add(p);
	}

	public SensorPoint(int x, int y, int cellNumberX, int cellNumberY, int numberOfSubCells)
	{
		this.x = x;
		this.y = y;
		this.cellNumberX = cellNumberX;
		this.cellNumberY = cellNumberY;
		this.numberOfSubCells = numberOfSubCells;
	}

	public int getX()
	{
		return this.x;
	}

	public void setX(int x)
	{
		this.x = x;
	}

	public int getY()
	{
		return this.y;
	}

	public void setY(int y)
	{
		this.y = y;
	}


	public ArrayList<SensorPoint> getNeighbours(Cell cell[][], double senorRadius)
	{
		this.sensorRadius = senorRadius;
		ArrayList<SensorPoint> sensorPoints = new ArrayList<SensorPoint>();

		sensorPoints.addAll(cell[cellNumberX-1][cellNumberY-1].getSensorPoints());
		sensorPoints.addAll(cell[cellNumberX][cellNumberY-1].getSensorPoints());
		sensorPoints.addAll(cell[cellNumberX+1][cellNumberY-1].getSensorPoints());
		sensorPoints.addAll(cell[cellNumberX-1][cellNumberY].getSensorPoints());
		sensorPoints.addAll(cell[cellNumberX][cellNumberY].getSensorPoints());
		sensorPoints.addAll(cell[cellNumberX+1][cellNumberY].getSensorPoints());
		sensorPoints.addAll(cell[cellNumberX-1][cellNumberY+1].getSensorPoints());
		sensorPoints.addAll(cell[cellNumberX][cellNumberY+1].getSensorPoints());
		sensorPoints.addAll(cell[cellNumberX+1][cellNumberY+1].getSensorPoints());

		sensorPoints.remove(this);
//		System.out.println("Point Name : " + pointName);
		for(int i = 0; i < sensorPoints.size(); i++) {
			int nX = sensorPoints.get(i).getX();
			int nY = sensorPoints.get(i).getY();
			double dist = (double) Math.sqrt(((Math.pow(x - nX,2)) + (Math.pow(y-nY,2))));
			if (dist <= senorRadius) {
				degree++;
				neighbourSensorPoints.add(sensorPoints.get(i));
				neighbourSensorPointsBeforeDeleted.add(sensorPoints.get(i));
//				System.out.println("Neighbor Name : " + sensorPoints.get(i).getPointName());
			}
		}
		//		neighbourSensorPointsBeforeDeleted.addAll(neighbourSensorPoints);
		degreeWhenDeleted = degree;
		oldDegreeWhenDeleted = degree;
		DegreeInformation.getInstance().addTotalDegree(degree);
		DegreeInformation.getInstance().addDegreeDataInList(degree, this);
//		System.out.println(" ");
//		System.out.println(" ");
//		System.out.println(" ");
		return neighbourSensorPoints;
	}


	public void changeNeighbourInformationWhenThisPointDeleted(Graphics g) {
		for(int i = 0; i < neighbourSensorPointsBeforeDeleted.size(); i++) {
			neighbourSensorPointsBeforeDeleted.get(i).deleteNeighbour(this, g);
		}
	}


	public void deleteNeighbour(SensorPoint sp, Graphics g) {
		g.drawLine(sp.getX(), sp.getY(), x, y);
		neighbourSensorPointsBeforeDeleted.remove(sp);
		degreeWhenDeleted = degreeWhenDeleted - 1;
		DegreeInformation.getInstance().changeDegreeDataInList(oldDegreeWhenDeleted, degreeWhenDeleted, this);
		oldDegreeWhenDeleted = degreeWhenDeleted;
	}


	public int getDegree() {
		return degree;
	}

	public void setDegree(int degree) {
		this.degree = degree;
	}


	public int getDegreeWhenDeleted() {
		return degreeWhenDeleted;
	}

	public void setDegreeWhenDeleted(int degreeWhenDeleted) {
		this.degreeWhenDeleted = degreeWhenDeleted;
	}

	public int getColorID() {
		return colorID;
	}

	public void setColorID(int colorID) {
		this.colorID = colorID;
	}

	public int getColorInformation() {
		int c = 1;
		ArrayList<Integer> color = new ArrayList<Integer>();
		for(int i = 0; i < neighbourSensorPoints.size(); i++) {
			color.add(neighbourSensorPoints.get(i).getColorID());
		}
		Collections.sort(color);
		for(int i = 0;i<color.size();i++) {
			int temp = color.get(i);
			if(c < temp) {
				colorID = c;
				break;
			}
			else {
				c = temp + 1;
			}
		}
		colorID = c;
		return colorID;
	}


	public void assignNeighbour(SensorPoint sp) {
		neighbourSensorPoints.add(sp);
	}




	

	public void calculateColorNeighborFromOriginalNeighbor() {
		for(int i = 0; i < 5; i++) {
			ArrayList<SensorPoint> sp = new ArrayList<SensorPoint>();
			neighborColorPoints.add(i, sp);
		}
		for(int i = 0; i < neighbourSensorPoints.size(); i++) {
			SensorPoint sp = neighbourSensorPoints.get(i);
			int neighborColor = sp.getColorID();
			if(colorID != neighborColor && neighborColor < 5) {
				neighborColorPoints.get(neighborColor).add(sp);
			}
		}
	}
	
	
		
	

	public ArrayList<ArrayList<SensorPoint>> getNeighborColorPoints() {
		return neighborColorPoints;
	}
	
	
	public ArrayList<SensorPoint> getNeighborColorPointsForRespectiveColor(int color) {
		return neighborColorPoints.get(color);
	}
	
	
	
	public void drawNeighborsAfterBFS(Graphics g, int color) {
		ArrayList<SensorPoint> n1 = getNeighborColorPointsForRespectiveColor(color);
		for(int i = 0; i < n1.size(); i++) {
			g.drawLine(x, y, n1.get(i).getX(), n1.get(i).getY());
		}
	}
	
	
	public ArrayList<SensorPoint> getNeighborColorPointsColor1() {
		return neighborColorPoints.get(1);
	}
	
	public ArrayList<SensorPoint> getNeighborColorPointsColor2() {
		return neighborColorPoints.get(2);
	}
	
	public ArrayList<SensorPoint> getNeighborColorPointsColor3() {
		return neighborColorPoints.get(3);
	}
	
	public ArrayList<SensorPoint> getNeighborColorPointsColor4() {
		return neighborColorPoints.get(4);
	}

	
	
	
	
	private int degreeWhenDeletedBFS = 0;
	private int oldDegreeWhenDeletedBFS = 0;
	public void calculateColorNeighborAfterBFS(ArrayList<SensorPoint> asp, int color1, int color2) {
		degreeWhenDeletedBFS = 0;
		oldDegreeWhenDeletedBFS = 0;
		neighborColorPointsAfterBFS.clear();
		for(int i = 0; i < 5; i++) {
			ArrayList<SensorPoint> sp = new ArrayList<SensorPoint>();
			neighborColorPointsAfterBFS.add(i, sp);
		}
		for(int i = 0; i < neighbourSensorPoints.size(); i++) {
			SensorPoint sp = neighbourSensorPoints.get(i);
			int neighborColor = sp.getColorID();
			if(neighborColor < 5 && colorID != neighborColor && asp.contains(sp)) {
				neighborColorPointsAfterBFS.get(neighborColor).add(sp);
				if(neighborColor == color1 || neighborColor == color2) {
					degreeWhenDeletedBFS++;
				}
			}
		}
		DegreeInformation.getInstance().addDegreeDataInListBFS(degreeWhenDeletedBFS, this);
		DegreeInformation.getInstance().addTotalDegreeAfterAdding(degreeWhenDeletedBFS);
	}
	
	

	
	public void changeNeighbourInformationWhenThisPointDeletedBFS(int color) {
		for(int i = 0; i < neighborColorPointsAfterBFS.get(color).size(); i++) {
			neighborColorPointsAfterBFS.get(color).get(i).deleteNeighbourBFS(this, colorID);
		}
	}

	 
	public void deleteNeighbourBFS(SensorPoint sp, int color) {
		neighborColorPointsAfterBFS.get(color).remove(sp);
		degreeWhenDeletedBFS = degreeWhenDeletedBFS - 1;
		DegreeInformation.getInstance().changeDegreeDataInListBFS(oldDegreeWhenDeletedBFS, degreeWhenDeletedBFS, this);
		oldDegreeWhenDeletedBFS = degreeWhenDeletedBFS;
	}

	
	
	public void calculateColorNeighborAfterBFSFinal(ArrayList<SensorPoint> asp, int color1, int color2, Graphics g) {
		degreeWhenDeletedBFS = 0;
		oldDegreeWhenDeletedBFS = 0;
		neighborColorPointsAfterBFS.clear();
		for(int i = 0; i < 5; i++) {
			ArrayList<SensorPoint> sp = new ArrayList<SensorPoint>();
			neighborColorPointsAfterBFS.add(i, sp);
		}
		for(int i = 0; i < neighbourSensorPoints.size(); i++) {
			SensorPoint sp = neighbourSensorPoints.get(i);
			int neighborColor = sp.getColorID();
			if(neighborColor < 5 && colorID != neighborColor && asp.contains(sp)) {
				neighborColorPointsAfterBFS.get(neighborColor).add(sp);
				if(neighborColor == color1 || neighborColor == color2) {
					g.drawLine(x, y, sp.getX(), sp.getY());
					degreeWhenDeletedBFS++;
				}
			}
		}
		DegreeInformation.getInstance().addDegreeDataInListBFS(degreeWhenDeletedBFS, this);
		DegreeInformation.getInstance().addTotalDegreeAfterAdding(degreeWhenDeletedBFS);
	}
	
	public void changeNeighbourInformationWhenThisPointDeletedBFSFinal(int color, Graphics g) {
		for(int i = 0; i < neighborColorPointsAfterBFS.get(color).size(); i++) {
			neighborColorPointsAfterBFS.get(color).get(i).deleteNeighbourBFSFinal(this, colorID, g);
		}
	}

	 
	public void deleteNeighbourBFSFinal(SensorPoint sp, int color, Graphics g) {
		neighborColorPointsAfterBFS.get(color).remove(sp);
		g.drawLine(x, y, sp.getX(), sp.getY());
		degreeWhenDeletedBFS = degreeWhenDeletedBFS - 1;
		DegreeInformation.getInstance().changeDegreeDataInListBFS(oldDegreeWhenDeletedBFS, degreeWhenDeletedBFS, this);
		oldDegreeWhenDeletedBFS = degreeWhenDeletedBFS;
	}
	
	public ArrayList<SensorPoint> getNeighborColorPointsColor1AfterBFS(int color) {
		return neighborColorPointsAfterBFS.get(color);
	}
	
	private String pointName;
	public void setPointName(String pointName) {
		this.pointName = pointName;
	}
	
	public String getPointName() {
		return pointName;
	}
	

}
