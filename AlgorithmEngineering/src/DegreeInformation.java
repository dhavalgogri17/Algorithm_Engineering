import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DegreeInformation {

	private int minDegree = 0;
	private int maxDegree = 0;
	private SensorPoint minDegreePoint;
	private SensorPoint maxDegreePoint;
	private int totalDegree = 0;
	private ColorInformation colorInformation = ColorInformation.getInstance();
	private int termClique = 0;
	private int sensorToBeDeleted = -1;

	private static DegreeInformation degreeInformation = null;
	private ArrayList<ArrayList<SensorPoint>> degreeInformationWhenPointDeleted = new ArrayList<ArrayList<SensorPoint>>();
	//	private Map<Integer, Integer> map = new HashMap<Integer, Integer>();
	//	private Map<SensorPoint, Integer> degreeMap = new HashMap<SensorPoint, Integer>();
	private ArrayList<ArrayList<SensorPoint>> degreeInformationForAllPointsAfterDeletion = new ArrayList<ArrayList<SensorPoint>>();
	private ArrayList<SensorPoint> termCliqueArrayList = new ArrayList<SensorPoint>();
	public static DegreeInformation getInstance() {
		if(degreeInformation == null) {
			degreeInformation = new DegreeInformation();
			return degreeInformation;
		}
		return degreeInformation;
	}

	public ArrayList<ArrayList<SensorPoint>> getDegreeInformationForAllPointsAfterDeletion() {
		return degreeInformationForAllPointsAfterDeletion;
	}
	
	
	public void inititialDegreeInformationForAllPointsAfterDeletion()
	{
		for(int i = 0; i < 500; i++) {
			ArrayList<SensorPoint> sp = new ArrayList<SensorPoint>();
			degreeInformationWhenPointDeleted.add(sp);
		}
		for(int i = 0; i < 500; i++) {
			ArrayList<SensorPoint> sp = new ArrayList<SensorPoint>();
			degreeInformationForAllPointsAfterDeletion.add(sp);
		}
	}

	public void addDegreeDataInList(int degree, SensorPoint sp){
		degreeInformationForAllPointsAfterDeletion.get(degree).add(sp);
	}
	
	public void addDegreeDataInListWhenPointDeleted(int degree, SensorPoint sp){
		degreeInformationWhenPointDeleted.get(degree).add(sp);
	}
	
	public ArrayList<ArrayList<SensorPoint>> getDegreeDataInListWhenPointDeleted() {
		return degreeInformationWhenPointDeleted;
	}

	public void deleteDegreeDataInList(SensorPoint sp){
		degreeInformationWhenPointDeleted.get(sp.getDegreeWhenDeleted()).add(sp);
		degreeInformationForAllPointsAfterDeletion.get(sensorToBeDeleted).remove(sp);
		sensorToBeDeleted = -1;
		int count = 0;
		int tc = 0;
		
		for(int i = 1; i < maxDegree; i++) {
			if(degreeInformationForAllPointsAfterDeletion.get(i).size() > 0) {
				count++;
				tc = i;
			}
		}
		if(count == 1 && tc > termClique) {
			termClique = tc;
			termCliqueArrayList.clear();
			termCliqueArrayList.addAll(degreeInformationForAllPointsAfterDeletion.get(tc));
//			System.out.println("TermClique Size ArrayList = " + termCliqueArrayList.size());		
			
		}
	}

	public ArrayList<SensorPoint> getArrayListOfTerminalClique() {
		System.out.println("TermClique Size = " + termClique);
		return termCliqueArrayList;
	}
	
	public int getTermCliqueNumber() {
		return termClique;
	}
	
	public void changeDegreeDataInList(int oldDegree, int newDegree, SensorPoint sp) {
		degreeInformationForAllPointsAfterDeletion.get(oldDegree).remove(sp);
		degreeInformationForAllPointsAfterDeletion.get(newDegree).add(sp);
	}

	public SensorPoint getSensorPointForColoring() {
		SensorPoint sp = null;
		for(int i = 0; i<degreeInformationForAllPointsAfterDeletion.size(); i++) {
			if(degreeInformationForAllPointsAfterDeletion.get(i).size()>0) {
				sensorToBeDeleted = i;
				sp = degreeInformationForAllPointsAfterDeletion.get(i).get(0);
				break;
			}
		}
		return sp;
	}




	public int getMinDegree() {
		return minDegree;
	}

	public void setMinDegree(int minDegree) {
		this.minDegree = minDegree;
	}

	public int getMaxDegree() {
		return maxDegree;
	}

	public void setMaxDegree(int maxDegree) {
		this.maxDegree = maxDegree;
	}

	public SensorPoint getMinDegreePoint() {
		return minDegreePoint;
	}

	public void setMinDegreePoint(SensorPoint minDegreePoint) {
		this.minDegreePoint = minDegreePoint;
	}

	public SensorPoint getMaxDegreePoint() {
		return maxDegreePoint;
	}

	public void setMaxDegreePoint(SensorPoint maxDegreePoint) {
		this.maxDegreePoint = maxDegreePoint;
	}

	public int getTotalDegree() {
		return totalDegree;
	}

	public void addTotalDegree(int totalDegree) {
		this.totalDegree = this.totalDegree + totalDegree;
	}

	public double getAverageDegree(int totalPoints) {
		return (double)(totalDegree/totalPoints);
	}


	
	
	
//	Degree Information After BFS i.e. Destroying Tail 
	private ArrayList<ArrayList<SensorPoint>> degreeInformationForAllPointsAfterBFS = new ArrayList<ArrayList<SensorPoint>>();
	private int totalDegreeBeforeDeletingTails = 0;
	private int totalDegreeAfterDeletingTails = 0;
	
	public void inititialDegreeInformationForAllPointsAfterBFS()
	{
		totalDegreeBeforeDeletingTails = 0;
		totalDegreeAfterDeletingTails = 0;
		degreeInformationForAllPointsAfterBFS.clear();
		for(int i = 0; i < 100; i++) {
			ArrayList<SensorPoint> sp = new ArrayList<SensorPoint>();
			degreeInformationForAllPointsAfterBFS.add(sp);
		}
	}
	
	public void addDegreeDataInListBFS(int degree, SensorPoint sp){
		degreeInformationForAllPointsAfterBFS.get(degree).add(sp);
	}
	
	public void deleteDegreeDataInListBFS(int color1, int color2, ArrayList<SensorPoint> sssp){
		totalDegreeAfterDeletingTails = totalDegreeBeforeDeletingTails;
		while(degreeInformationForAllPointsAfterBFS.get(0).size() > 0 || degreeInformationForAllPointsAfterBFS.get(1).size() > 0) {
			if(degreeInformationForAllPointsAfterBFS.get(0).size() > 0) {
				SensorPoint sp = degreeInformationForAllPointsAfterBFS.get(0).get(0);
				if(sp.getColorID() == color1) {
					sp.changeNeighbourInformationWhenThisPointDeletedBFS(color2);
				}
				else {
					sp.changeNeighbourInformationWhenThisPointDeletedBFS(color1);
				}
				sssp.remove(sp);
				degreeInformationForAllPointsAfterBFS.get(0).remove(0);
			}
			else if(degreeInformationForAllPointsAfterBFS.get(1).size() > 0) {
				SensorPoint sp = degreeInformationForAllPointsAfterBFS.get(1).get(0);
				if(sp.getColorID() == color1) {
					sp.changeNeighbourInformationWhenThisPointDeletedBFS(color2);
				}
				else {
					sp.changeNeighbourInformationWhenThisPointDeletedBFS(color1);
				}
				sssp.remove(sp);
				degreeInformationForAllPointsAfterBFS.get(1).remove(0);
			}
		}
	}
	
	
	
	public void deleteDegreeDataInListBFSFinal(int color1, int color2, ArrayList<SensorPoint> sssp, Graphics g){
		totalDegreeAfterDeletingTails = totalDegreeBeforeDeletingTails;
		while(degreeInformationForAllPointsAfterBFS.get(0).size() > 0 || degreeInformationForAllPointsAfterBFS.get(1).size() > 0) {
			if(degreeInformationForAllPointsAfterBFS.get(0).size() > 0) {
				SensorPoint sp = degreeInformationForAllPointsAfterBFS.get(0).get(0);
				if(sp.getColorID() == color1) {
					sp.changeNeighbourInformationWhenThisPointDeletedBFSFinal(color2, g);
				}
				else {
					sp.changeNeighbourInformationWhenThisPointDeletedBFSFinal(color1, g);
				}
				sssp.remove(sp);
				g.fillOval(sp.getX(), sp.getY(), 3, 3);
				degreeInformationForAllPointsAfterBFS.get(0).remove(0);
			}
			else if(degreeInformationForAllPointsAfterBFS.get(1).size() > 0) {
				SensorPoint sp = degreeInformationForAllPointsAfterBFS.get(1).get(0);
				if(sp.getColorID() == color1) {
					sp.changeNeighbourInformationWhenThisPointDeletedBFSFinal(color2, g);
				}
				else {
					sp.changeNeighbourInformationWhenThisPointDeletedBFSFinal(color1, g);
				}
				sssp.remove(sp);
				g.fillOval(sp.getX(), sp.getY(), 3, 3);
				degreeInformationForAllPointsAfterBFS.get(1).remove(0);
//				try {
//					Thread.sleep(100);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}
			
		}
	}

	
	public void changeDegreeDataInListBFS(int oldDegree, int newDegree, SensorPoint sp) {
		totalDegreeAfterDeletingTails--;
		degreeInformationForAllPointsAfterBFS.get(oldDegree).remove(sp);
		degreeInformationForAllPointsAfterBFS.get(newDegree).add(sp);
	}
	
	public ArrayList<ArrayList<SensorPoint>> getFinalData(){
		return degreeInformationForAllPointsAfterBFS;
	}

	public void addTotalDegreeAfterAdding(int total) {
		totalDegreeBeforeDeletingTails = totalDegreeBeforeDeletingTails + total;
	}
	
	public int getTotalDegreeBeforeDeletingTails() {
		return totalDegreeBeforeDeletingTails;
	}
	
	public int getTotalDegreeAfterDeletingTails() {
		return totalDegreeAfterDeletingTails;
	}
	
	

}
