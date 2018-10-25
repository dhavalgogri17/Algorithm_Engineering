import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class DrawClass extends Canvas{

	private static DrawClass drawClass = null;
	private Cell[][] cell = null;
	private double rAct = 0;
	private double averageDegree = 0;
	private ArrayList<SensorPoint> sensorPoint = new ArrayList<SensorPoint>();
	private int minDegree = 100000;
	private int maxDegree = 0;
	private SensorPoint minDegreePoint;
	private SensorPoint maxDegreePoint;
	private double realR = 0;
	private DegreeInformation degreeInformation;
	private int pointSize = 3;
	
	
//	Phase 2 Info
	private int terminalClique = 0;
	private int numberOfColors = 0;
	private int maxDegreeWhenDeleted = 0;
	
	
//	Phase 3 Info
	private int order1 = 0;
	private int order2 = 0;
	private int size1 = 0;
	private int size2 = 0;
	private double domination1 = 0;
	private double domination2 = 0;

	public static DrawClass getInstance() {
		if(drawClass == null) {
			drawClass = new DrawClass();
			return drawClass;
		}
		return drawClass;
	}


	public void setData(Cell[][] cell, ArrayList<SensorPoint> sensorPoint,double rAct, double averageDegree, double realR){
		this.cell = cell;
		this.sensorPoint.addAll(sensorPoint);
		this.rAct = rAct;
		this.realR = realR;
		this.averageDegree = averageDegree;
	}

	public DrawClass() {
		degreeInformation = DegreeInformation.getInstance();
		degreeInformation.inititialDegreeInformationForAllPointsAfterDeletion();
		JFrame frame = new JFrame("Backbone Determination in Wireless Sensor Network");
		this.setSize(1400, 900);
		frame.add(this);
		frame.pack();
		frame.setBackground(Color.WHITE);
		frame.setVisible(true);
	}


	public void paint(Graphics g) {
		if(cell == null) {
			return;
		}
		super.paint(g);
		long start = System.currentTimeMillis();

		drawPoints(g);
//				try {
//					Thread.sleep(10000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}

		drawNeighbours(g);
//				try {
//					Thread.sleep(10000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}

		drawMinAndMaxDegree(g);
//		try {
//			Thread.sleep(10000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		startColoring(g);



		long end = System.currentTimeMillis();
		System.out.println("Time Calculation");
		g.setFont(new Font("TimesNewRoman", Font.PLAIN, 15)); 
		g.setColor(Color.BLACK);

		g.drawString("No of Sensor = " + sensorPoint.size(), 915, 25);
		g.drawString("Entered Avg Degree = " + averageDegree , 915, 50);
		g.drawString("Value of Radius = " + realR, 915, 75);
		g.drawString("Value of Min Degree = " + minDegree, 915, 100);
		g.drawString("Value of Max Degree = " + maxDegree, 915, 125);
		g.drawString("Calculated Average Degree = " + degreeInformation.getAverageDegree(sensorPoint.size()), 915, 150);
		g.drawString("number of Unique Edges Before Color= " + degreeInformation.getTotalDegree()/2, 915, 175);
		
//		Phase 2
		g.drawString("PHASE 2 INFORMATION BELOW", 915, 225);
		g.drawString("Max Degree When Deleted = " + maxDegreeWhenDeleted, 915, 250);
		g.drawString("Terminal Clique = " + terminalClique, 915, 275);
		g.drawString("Colors Used = " + numberOfColors, 915, 300);
		
		
//		Phase 3
		g.drawString("PHASE 3 INFORMATION BELOW", 915, 350);
		g.drawString("BACKBONE 1", 915, 375);
		g.drawString("Order 1(Number of Unique Edges) = " + order1, 915, 400);
		g.drawString("Size 1(Number of Vertex) = " + size1, 915, 425);
		g.drawString("Domination 1 = " + domination1 + " %", 915, 450);
		
		

		NumberFormat formatter = new DecimalFormat("#0.00000");
		//		String timeTaken = formatter.format((end - start) / 1000d);
		//		g.drawString("Execution time is " + formatter.format((end - start) / 1000d) + " seconds", 915, 175);
		System.out.println("Execution time1 is " + formatter.format((end - start) / 1000d) + " seconds");
		System.out.println("FINISHED DRAW 1 CLASS");




	}

	private void drawPoints(Graphics g) {

		g.setColor(Color.BLACK);
		for(int i = 0; i< sensorPoint.size(); i++) {
			//			g.setColor(Color.RED);
			//			g.drawString("Point " + i, sensorPoint.get(i).getX() - 5, sensorPoint.get(i).getY() - 5);
			g.fillOval(sensorPoint.get(i).getX(), sensorPoint.get(i).getY(), pointSize, pointSize);
		}

	}



	private void drawNeighbours(Graphics g) {
		g.setColor(Color.BLACK);
		if(sensorPoint.size() <= 8000) {
			for(int i = 0; i< sensorPoint.size(); i++) {
				int x = sensorPoint.get(i).getX();
				int y = sensorPoint.get(i).getY();
				ArrayList<SensorPoint> np = sensorPoint.get(i).getNeighbours(cell, rAct);
				int degreeSize = np.size();
				if(degreeSize>maxDegree) {
					maxDegree = np.size();
					maxDegreePoint = sensorPoint.get(i);
				}
				if(degreeSize<minDegree) {
					minDegree = np.size();
					minDegreePoint = sensorPoint.get(i);
				}
				for(int j = 0; j< np.size(); j++) {
					g.drawLine(np.get(j).getX(), np.get(j).getY(), x, y);
				}

			}
		}
		else {
			for(int i = 0; i< sensorPoint.size(); i++) {
				//				int x = sensorPoint.get(i).getX();
				//				int y = sensorPoint.get(i).getY();
				ArrayList<SensorPoint> np = sensorPoint.get(i).getNeighbours(cell, rAct);
				int degreeSize = np.size();
				if(degreeSize>maxDegree) {
					maxDegree = np.size();
					maxDegreePoint = sensorPoint.get(i);
				}
				if(degreeSize<minDegree) {
					minDegree = np.size();
					minDegreePoint = sensorPoint.get(i);
				}
			}
		}


		degreeInformation.setMinDegree(minDegree);
		degreeInformation.setMaxDegree(maxDegree);
		degreeInformation.setMaxDegreePoint(maxDegreePoint);
		degreeInformation.setMinDegreePoint(minDegreePoint);
		
		
	}
	//	
	private void drawMinAndMaxDegree(Graphics g) {

		System.out.println("The Average degree calculated = " + degreeInformation.getAverageDegree(sensorPoint.size()));
		g.setColor(Color.GREEN);
		ArrayList<SensorPoint> npMax = maxDegreePoint.getNeighbourSensorPoints();
		for(int i = 0;i<npMax.size(); i++) {
			g.drawLine(npMax.get(i).getX(), npMax.get(i).getY(), maxDegreePoint.getX(), maxDegreePoint.getY());
		}
		g.setColor(Color.RED);
		ArrayList<SensorPoint> npMin = minDegreePoint.getNeighbourSensorPoints();
		for(int i = 0;i<npMin.size(); i++) {
			g.drawLine(npMin.get(i).getX(), npMin.get(i).getY(), minDegreePoint.getX(), minDegreePoint.getY());
		}

	}



	private void startColoring(Graphics g) {

		ColorInformation colorInformation = ColorInformation.getInstance();
		g.setColor(Color.WHITE);

		for(int i = 0; i < sensorPoint.size(); i++) {
			SensorPoint key = degreeInformation.getSensorPointForColoring();
			colorInformation.addDataInListOfPointsWithLessDegree(key);
			key.changeNeighbourInformationWhenThisPointDeleted(g);
			degreeInformation.deleteDegreeDataInList(key);
			//			System.out.println("Deleted Point Name = "+key.getPointName());
			//			for(int j = 0; j < sensorPoint.size(); j++) {
			//				System.out.println("Point Name = " + sensorPoint.get(j).getPointName() + "   DegreeInfo: " + sensorPoint.get(j).getDegreeWhenDeleted());
			//			}
			//			System.out.println("");
			//			System.out.println("");
			//			System.out.println("");
			g.fillOval(sensorPoint.get(i).getX(), sensorPoint.get(i).getY(), pointSize, pointSize);
			//			try {
			//				Thread.sleep(10000);
			//			} catch (InterruptedException e) {
			//				// TODO Auto-generated catch block
			//				e.printStackTrace();
			//			}
		}

		
		

		ArrayList<SensorPoint> listOfPointsWithLessDegree = colorInformation.getInstance().getListOfPointsWithLessDegree(); 
		System.out.println("Size ====  "+listOfPointsWithLessDegree.size());
		for(int i = sensorPoint.size()-1; i >= 0; i--) {
			SensorPoint sp = listOfPointsWithLessDegree.get(i);
			if(sp == null) {
				break;
			}
			int color = sp.getColorInformation();
			colorInformation.addArrayListColorData(sp, color);
			colorInformation.addColorDataInMap(sp, color);
//			System.out.println("Point Name = " + sp.getPointName() + "   Color: " + color);

			switch(color)
			{
			case 1:
				g.setColor(Color.RED);
				g.fillOval(sp.getX(), sp.getY(), pointSize, pointSize);
				break;
			case 2:
				g.setColor(Color.GREEN);
				g.fillOval(sp.getX(), sp.getY(), pointSize, pointSize);
				break;
			case 3:
				g.setColor(Color.BLUE);
				g.fillOval(sp.getX(), sp.getY(), pointSize, pointSize);
				break;
			case 4:
				g.setColor(Color.YELLOW);
				g.fillOval(sp.getX(), sp.getY(), pointSize, pointSize);
				break;
				//				Below code for simulation of 20 Vertex For Coloring Algorithm
				//			case 5:
				//				g.setColor(Color.DARK_GRAY);
				//				g.fillOval(sp.getX(), sp.getY(), pointSize, pointSize);
				//				break;
				//			case 6:
				//				g.setColor(Color.MAGENTA);
				//				g.fillOval(sp.getX(), sp.getY(), pointSize, pointSize);
				//				break;
				//			case 7:
				//				g.setColor(Color.orange);
				//				g.fillOval(sp.getX(), sp.getY(), pointSize, pointSize);
				//				break;
				//			case 8:
				//				g.setColor(Color.BLACK);
				//				g.fillOval(sp.getX(), sp.getY(), pointSize, pointSize);
				//				break;
				//			case 9:
				//				g.setColor(Color.PINK);
				//				g.fillOval(sp.getX(), sp.getY(), pointSize, pointSize);
				//				break;
			default:
				break;
			}
			//			try {
			//				Thread.sleep(10000);
			//			} catch (InterruptedException e) {
			//				// TODO Auto-generated catch block
			//				e.printStackTrace();
			//			}
		}


//		try {
//			Thread.sleep(10000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}


		int maxDegree = 0;
		int minDegree = Integer.MAX_VALUE;

		int colorUsed = 0;
		for(int i = 0; i < sensorPoint.size(); i++) {
			if(sensorPoint.get(i).getNeighbourSensorPointsBeforeDeleted().size() > maxDegree) {
				maxDegree = sensorPoint.get(i).getNeighbourSensorPointsBeforeDeleted().size();
			}
			if(sensorPoint.get(i).getNeighbourSensorPointsBeforeDeleted().size() < minDegree) {
				minDegree = sensorPoint.get(i).getNeighbourSensorPointsBeforeDeleted().size();
			}
			if(sensorPoint.get(i).getColorID() > colorUsed && sensorPoint.get(i).getColorID() != Integer.MAX_VALUE) {
				colorUsed = sensorPoint.get(i).getColorID();
			}
		}



		Map<Integer, ArrayList<SensorPoint>> data = colorInformation.getColorPoint();
		int count = 0;
		for(int i = 1; i<5;i++) {
			switch(i)
			{
			case 1:
				g.setColor(Color.RED);
				break;
			case 2:
				g.setColor(Color.GREEN);
				break;
			case 3:
				g.setColor(Color.BLUE);
				break;
			case 4:
				g.setColor(Color.YELLOW);
				break;
			default:
				break;
			}

			for(int j = 0; j < data.get(i).size(); j++) {
				SensorPoint sp = data.get(i).get(j);
				//				g.setColor(Color.BLACK);
				sp.calculateColorNeighborFromOriginalNeighbor();
				count++;
				//				g.fillRect(data.get(i).get(j).getX(), data.get(i).get(j).getY(), 2, 2);
			}
		}

		

		
		System.out.println("Count = " + count);
		System.out.println("Max Degree when Deleted = " + maxDegree);
		System.out.println("Min Degree when Deleted = " + minDegree);
		System.out.println("ColorUsed = " + colorUsed);
		this.maxDegreeWhenDeleted = maxDegree;
		this.numberOfColors = colorUsed;
		this.terminalClique = degreeInformation.getTermCliqueNumber();

		//		g.setColor(Color.BLACK);
		//		ArrayList<SensorPoint> terminalCliqueData = degreeInformation.getArrayListOfTerminalClique();
		//		for(int i = 0; i<terminalCliqueData.size()-1; i++) {
		//			int x1 = terminalCliqueData.get(i).getX();
		//			int y1 = terminalCliqueData.get(i).getY();
		//			for(int j = i+1; j<terminalCliqueData.size(); j++) {
		//				int x2 = terminalCliqueData.get(j).getX();
		//				int y2 = terminalCliqueData.get(j).getY();
		//				//				g.drawLine(x1, y1, x2, y2);
		//			}
		//		}


		SensorPoint sp = data.get(1).get(1);
		ArrayList<ArrayList<SensorPoint>> neighborColorPoints = sp.getNeighborColorPoints();
		System.out.println("Neighbor Color Size = "+neighborColorPoints.get(2).size());


		int maxColor1_1 = -1;
		int maxColor2_1 = -1;
		int maxEdgeBeforeBFS_1 = 0;
		int maxEdgeAfterBFSTailDeletion_1 = 0;

		
		int maxColor1_2 = -1;
		int maxColor2_2 = -1;
		int maxEdgeBeforeBFS_2 = 0;
		int maxEdgeAfterBFSTailDeletion_2 = 0;
		
		
		

		g.clearRect(0, 0, 1200, 910);
		


		
		ArrayList<SensorPoint> pair12 = implementBFS(1, 2);
		int totalDegreeBeforeDeletingTailsPair12 = 0;
		int totalDegreeAfterDeletingTailsPair12 = 0;
		degreeInformation.inititialDegreeInformationForAllPointsAfterBFS();
		for(int i = 0; i < pair12.size(); i++) {
			SensorPoint sp1 = pair12.get(i);
			sp1.calculateColorNeighborAfterBFS(pair12, 1, 2);
		}
		degreeInformation.deleteDegreeDataInListBFS(1, 2, pair12);
		totalDegreeBeforeDeletingTailsPair12 = degreeInformation.getTotalDegreeBeforeDeletingTails();
		totalDegreeAfterDeletingTailsPair12 = degreeInformation.getTotalDegreeAfterDeletingTails();
		for(int i = 0; i < pair12.size(); i++) {
			SensorPoint sp1 = pair12.get(i);
			int color = sp1.getColorID();
			if(color == 1) {
				g.setColor(Color.RED);
			}
			else {
				g.setColor(Color.GREEN);
			}
			g.fillOval(sp1.getX(), sp1.getY(), 3, 3);
		}

		g.setColor(Color.BLACK);
		for(int i = 0; i < pair12.size(); i++) {
			SensorPoint sp1 = pair12.get(i);
			ArrayList<SensorPoint> c1;
			if(sp1.getColorID() == 1) {
				c1 = sp1.getNeighborColorPointsColor1AfterBFS(2);
			}
			else {
				c1 = sp1.getNeighborColorPointsColor1AfterBFS(1);
			}
			for(int j = 0; j < c1.size(); j++) {
				g.drawLine(sp1.getX(), sp1.getY(), c1.get(j).getX(), c1.get(j).getY());
			}

		}

		bfsQueue.clear();
		bfsQueueDeleted.clear();
		if(maxEdgeAfterBFSTailDeletion_1 < totalDegreeAfterDeletingTailsPair12) {
			maxColor1_1 = 1;
			maxColor2_1 = 2;
			maxEdgeAfterBFSTailDeletion_1 = totalDegreeAfterDeletingTailsPair12;
			maxEdgeBeforeBFS_1 = totalDegreeBeforeDeletingTailsPair12;
		}
		else if(maxEdgeAfterBFSTailDeletion_2 < totalDegreeAfterDeletingTailsPair12) {
			maxColor1_2 = 1;
			maxColor2_2 = 2;
			maxEdgeAfterBFSTailDeletion_2 = totalDegreeAfterDeletingTailsPair12;
			maxEdgeBeforeBFS_2 = totalDegreeBeforeDeletingTailsPair12;
		}
		
//		System.out.println("Before Tail = "+totalDegreeBeforeDeletingTailsPair12);
//		System.out.println("After Tail = "+totalDegreeAfterDeletingTailsPair12);


		g.clearRect(0, 0, 1200, 910);




		//		Pair 13
		ArrayList<SensorPoint> pair13 = implementBFS(1, 3);
		int totalDegreeBeforeDeletingTailsPair13 = 0;
		int totalDegreeAfterDeletingTailsPair13 = 0;
		degreeInformation.inititialDegreeInformationForAllPointsAfterBFS();
		for(int i = 0; i < pair13.size(); i++) {
			SensorPoint sp1 = pair13.get(i);
			sp1.calculateColorNeighborAfterBFS(pair13, 1, 3);
		}
		degreeInformation.deleteDegreeDataInListBFS(1, 3, pair13);
		totalDegreeBeforeDeletingTailsPair13 = degreeInformation.getTotalDegreeBeforeDeletingTails();
		totalDegreeAfterDeletingTailsPair13 = degreeInformation.getTotalDegreeAfterDeletingTails();
		for(int i = 0; i < pair13.size(); i++) {
			SensorPoint sp1 = pair13.get(i);
			int color = sp1.getColorID();
			if(color == 1) {
				g.setColor(Color.RED);
			}
			else {
				g.setColor(Color.BLUE);
			}
			g.fillOval(sp1.getX(), sp1.getY(), 3, 3);
		}
		g.setColor(Color.BLACK);
		for(int i = 0; i < pair13.size(); i++) {
			SensorPoint sp1 = pair13.get(i);
			ArrayList<SensorPoint> c1;
			if(sp1.getColorID() == 1) {
				c1 = sp1.getNeighborColorPointsColor1AfterBFS(3);
			}
			else {
				c1 = sp1.getNeighborColorPointsColor1AfterBFS(1);
			}
			for(int j = 0; j < c1.size(); j++) {
				g.drawLine(sp1.getX(), sp1.getY(), c1.get(j).getX(), c1.get(j).getY());
			}

		}

		bfsQueue.clear();
		bfsQueueDeleted.clear();
		if(maxEdgeAfterBFSTailDeletion_1 < totalDegreeAfterDeletingTailsPair13) {
			maxColor1_2 = maxColor1_1;
			maxColor2_2 = maxColor2_1;
			maxEdgeAfterBFSTailDeletion_2 = maxEdgeAfterBFSTailDeletion_1;
			maxEdgeBeforeBFS_2 = maxEdgeBeforeBFS_1;
			
			
			maxColor1_1 = 1;
			maxColor2_1 = 3;
			maxEdgeAfterBFSTailDeletion_1 = totalDegreeAfterDeletingTailsPair13;
			maxEdgeBeforeBFS_1 = totalDegreeBeforeDeletingTailsPair13;
		}
		else if(maxEdgeAfterBFSTailDeletion_2 < totalDegreeAfterDeletingTailsPair13) {
			maxColor1_2 = 1;
			maxColor2_2 = 3;
			maxEdgeAfterBFSTailDeletion_2 = totalDegreeAfterDeletingTailsPair13;
			maxEdgeBeforeBFS_2 = totalDegreeBeforeDeletingTailsPair13;
		}
//		System.out.println("Before Tail = "+totalDegreeBeforeDeletingTailsPair13);
//		System.out.println("After Tail = "+totalDegreeAfterDeletingTailsPair13);

		g.clearRect(0, 0, 1200, 910);





		//		Pair 14
		ArrayList<SensorPoint> pair14 = implementBFS(1, 4);
		int totalDegreeBeforeDeletingTailsPair14 = 0;
		int totalDegreeAfterDeletingTailsPair14 = 0;
		degreeInformation.inititialDegreeInformationForAllPointsAfterBFS();
		for(int i = 0; i < pair14.size(); i++) {
			SensorPoint sp1 = pair14.get(i);
			sp1.calculateColorNeighborAfterBFS(pair14, 1, 4);
		}
		degreeInformation.deleteDegreeDataInListBFS(1, 4, pair14);
		totalDegreeBeforeDeletingTailsPair14 = degreeInformation.getTotalDegreeBeforeDeletingTails();
		totalDegreeAfterDeletingTailsPair14 = degreeInformation.getTotalDegreeAfterDeletingTails();
		for(int i = 0; i < pair14.size(); i++) {
			SensorPoint sp1 = pair14.get(i);
			int color = sp1.getColorID();
			if(color == 1) {
				g.setColor(Color.RED);
			}
			else {
				g.setColor(Color.YELLOW);
			}
			g.fillOval(sp1.getX(), sp1.getY(), 3, 3);
		}
		g.setColor(Color.BLACK);
		for(int i = 0; i < pair14.size(); i++) {
			SensorPoint sp1 = pair14.get(i);
			ArrayList<SensorPoint> c1;
			if(sp1.getColorID() == 1) {
				c1 = sp1.getNeighborColorPointsColor1AfterBFS(4);
			}
			else {
				c1 = sp1.getNeighborColorPointsColor1AfterBFS(1);
			}
			for(int j = 0; j < c1.size(); j++) {
				g.drawLine(sp1.getX(), sp1.getY(), c1.get(j).getX(), c1.get(j).getY());
			}

		}

		bfsQueue.clear();
		bfsQueueDeleted.clear();
		if(maxEdgeAfterBFSTailDeletion_1 < totalDegreeAfterDeletingTailsPair14) {
			maxColor1_2 = maxColor1_1;
			maxColor2_2 = maxColor2_1;
			maxEdgeAfterBFSTailDeletion_2 = maxEdgeAfterBFSTailDeletion_1;
			maxEdgeBeforeBFS_2 = maxEdgeBeforeBFS_1;
			
			maxColor1_1 = 1;
			maxColor2_1 = 4;
			maxEdgeAfterBFSTailDeletion_1 = totalDegreeAfterDeletingTailsPair14;
			maxEdgeBeforeBFS_1 = totalDegreeBeforeDeletingTailsPair14;
		}
		else if(maxEdgeAfterBFSTailDeletion_2 < totalDegreeAfterDeletingTailsPair14) {
			maxColor1_2 = 1;
			maxColor2_2 = 4;
			maxEdgeAfterBFSTailDeletion_2 = totalDegreeAfterDeletingTailsPair14;
			maxEdgeBeforeBFS_2 = totalDegreeBeforeDeletingTailsPair14;
		}
		System.out.println("Before Tail = "+totalDegreeBeforeDeletingTailsPair14);
		System.out.println("After Tail = "+totalDegreeAfterDeletingTailsPair14);

		g.clearRect(0, 0, 1200, 910);










		//	Pair 23
		//		
		ArrayList<SensorPoint> pair23 = implementBFS(2, 3);
		int totalDegreeBeforeDeletingTailsPair23 = 0;
		int totalDegreeAfterDeletingTailsPair23 = 0;
		degreeInformation.inititialDegreeInformationForAllPointsAfterBFS();
		for(int i = 0; i < pair23.size(); i++) {
			SensorPoint sp1 = pair23.get(i);
			sp1.calculateColorNeighborAfterBFS(pair23, 2, 3);
		}
		degreeInformation.deleteDegreeDataInListBFS(2, 3, pair23);
		totalDegreeBeforeDeletingTailsPair23 = degreeInformation.getTotalDegreeBeforeDeletingTails();
		totalDegreeAfterDeletingTailsPair23 = degreeInformation.getTotalDegreeAfterDeletingTails();
		for(int i = 0; i < pair23.size(); i++) {
			SensorPoint sp1 = pair23.get(i);
			int color = sp1.getColorID();
			if(color == 2) {
				g.setColor(Color.GREEN);
			}
			else {
				g.setColor(Color.BLUE);
			}
			g.fillOval(sp1.getX(), sp1.getY(), 3, 3);
		}
		g.setColor(Color.BLACK);
		for(int i = 0; i < pair23.size(); i++) {
			SensorPoint sp1 = pair23.get(i);
			ArrayList<SensorPoint> c1;
			if(sp1.getColorID() == 2) {
				c1 = sp1.getNeighborColorPointsColor1AfterBFS(3);
			}
			else {
				c1 = sp1.getNeighborColorPointsColor1AfterBFS(2);
			}
			for(int j = 0; j < c1.size(); j++) {
				g.drawLine(sp1.getX(), sp1.getY(), c1.get(j).getX(), c1.get(j).getY());
			}

		}

		bfsQueue.clear();
		bfsQueueDeleted.clear();
		if(maxEdgeAfterBFSTailDeletion_1 < totalDegreeAfterDeletingTailsPair23) {
			maxColor1_2 = maxColor1_1;
			maxColor2_2 = maxColor2_1;
			maxEdgeAfterBFSTailDeletion_2 = maxEdgeAfterBFSTailDeletion_1;
			maxEdgeBeforeBFS_2 = maxEdgeBeforeBFS_1;
			
			maxColor1_1 = 2;
			maxColor2_1 = 3;
			maxEdgeAfterBFSTailDeletion_1 = totalDegreeAfterDeletingTailsPair23;
			maxEdgeBeforeBFS_1 = totalDegreeBeforeDeletingTailsPair23;
		}
		else if(maxEdgeAfterBFSTailDeletion_2 < totalDegreeAfterDeletingTailsPair23) {
			maxColor1_2 = 2;
			maxColor2_2 = 3;
			maxEdgeAfterBFSTailDeletion_2 = totalDegreeAfterDeletingTailsPair23;
			maxEdgeBeforeBFS_2 = totalDegreeBeforeDeletingTailsPair23;
		}
//		System.out.println("Before Tail = "+totalDegreeBeforeDeletingTailsPair23);
//		System.out.println("After Tail = "+totalDegreeAfterDeletingTailsPair23);

		g.clearRect(0, 0, 1200, 910);







		//		Pair 24
		//		
		ArrayList<SensorPoint> pair24 = implementBFS(2, 4);
		int totalDegreeBeforeDeletingTailsPair24 = 0;
		int totalDegreeAfterDeletingTailsPair24 = 0;
		degreeInformation.inititialDegreeInformationForAllPointsAfterBFS();
		for(int i = 0; i < pair24.size(); i++) {
			SensorPoint sp1 = pair24.get(i);
			sp1.calculateColorNeighborAfterBFS(pair24, 2, 4);
		}
		degreeInformation.deleteDegreeDataInListBFS(2, 4, pair24);
		totalDegreeBeforeDeletingTailsPair24 = degreeInformation.getTotalDegreeBeforeDeletingTails();
		totalDegreeAfterDeletingTailsPair24 = degreeInformation.getTotalDegreeAfterDeletingTails();
		for(int i = 0; i < pair24.size(); i++) {
			SensorPoint sp1 = pair24.get(i);
			int color = sp1.getColorID();
			if(color == 2) {
				g.setColor(Color.GREEN);
			}
			else {
				g.setColor(Color.YELLOW);
			}
			g.fillOval(sp1.getX(), sp1.getY(), 3, 3);
		}
		g.setColor(Color.BLACK);
		for(int i = 0; i < pair24.size(); i++) {
			SensorPoint sp1 = pair24.get(i);
			ArrayList<SensorPoint> c1;
			if(sp1.getColorID() == 2) {
				c1 = sp1.getNeighborColorPointsColor1AfterBFS(4);
			}
			else {
				c1 = sp1.getNeighborColorPointsColor1AfterBFS(2);
			}
			for(int j = 0; j < c1.size(); j++) {
				g.drawLine(sp1.getX(), sp1.getY(), c1.get(j).getX(), c1.get(j).getY());
			}

		}

		bfsQueue.clear();
		bfsQueueDeleted.clear();
		if(maxEdgeAfterBFSTailDeletion_1 < totalDegreeAfterDeletingTailsPair24) {
			maxColor1_2 = maxColor1_1;
			maxColor2_2 = maxColor2_1;
			maxEdgeAfterBFSTailDeletion_2 = maxEdgeAfterBFSTailDeletion_1;
			maxEdgeBeforeBFS_2 = maxEdgeBeforeBFS_1;
			
			maxColor1_1 = 2;
			maxColor2_1 = 4;
			maxEdgeAfterBFSTailDeletion_1 = totalDegreeAfterDeletingTailsPair24;
			maxEdgeBeforeBFS_1 = totalDegreeBeforeDeletingTailsPair24;
		}
		else if(maxEdgeAfterBFSTailDeletion_2 < totalDegreeAfterDeletingTailsPair24) {
			maxColor1_2 = 2;
			maxColor2_2 = 4;
			maxEdgeAfterBFSTailDeletion_2 = totalDegreeAfterDeletingTailsPair24;
			maxEdgeBeforeBFS_2 = totalDegreeBeforeDeletingTailsPair24;
		}
//		System.out.println("Before Tail = "+totalDegreeBeforeDeletingTailsPair24);
//		System.out.println("After Tail = "+totalDegreeAfterDeletingTailsPair24);

		g.clearRect(0, 0, 1200, 910);







		//		Pair 34
		ArrayList<SensorPoint> pair34 = implementBFS(3, 4);
		int totalDegreeBeforeDeletingTailsPair34 = 0;
		int totalDegreeAfterDeletingTailsPair34 = 0;
		degreeInformation.inititialDegreeInformationForAllPointsAfterBFS();
		for(int i = 0; i < pair34.size(); i++) {
			SensorPoint sp1 = pair34.get(i);
			sp1.calculateColorNeighborAfterBFS(pair34, 3, 4);
		}
		degreeInformation.deleteDegreeDataInListBFS(3, 4, pair34);
		totalDegreeBeforeDeletingTailsPair34 = degreeInformation.getTotalDegreeBeforeDeletingTails();
		totalDegreeAfterDeletingTailsPair34 = degreeInformation.getTotalDegreeAfterDeletingTails();
		for(int i = 0; i < pair34.size(); i++) {
			SensorPoint sp1 = pair34.get(i);
			int color = sp1.getColorID();
			if(color == 3) {
				g.setColor(Color.BLUE);
			}
			else {
				g.setColor(Color.YELLOW);
			}
			g.fillOval(sp1.getX(), sp1.getY(), 3, 3);
		}
		g.setColor(Color.BLACK);
		for(int i = 0; i < pair34.size(); i++) {
			SensorPoint sp1 = pair34.get(i);
			ArrayList<SensorPoint> c1;
			if(sp1.getColorID() == 3) {
				c1 = sp1.getNeighborColorPointsColor1AfterBFS(4);
			}
			else {
				c1 = sp1.getNeighborColorPointsColor1AfterBFS(3);
			}
			for(int j = 0; j < c1.size(); j++) {
				g.drawLine(sp1.getX(), sp1.getY(), c1.get(j).getX(), c1.get(j).getY());
			}

		}

		bfsQueue.clear();
		bfsQueueDeleted.clear();
		if(maxEdgeAfterBFSTailDeletion_1 < totalDegreeAfterDeletingTailsPair34) {
			maxColor1_2 = maxColor1_1;
			maxColor2_2 = maxColor2_1;
			maxEdgeAfterBFSTailDeletion_2 = maxEdgeAfterBFSTailDeletion_1;
			maxEdgeBeforeBFS_2 = maxEdgeBeforeBFS_1;
			
			maxColor1_1 = 3;
			maxColor2_1 = 4;
			maxEdgeAfterBFSTailDeletion_1 = totalDegreeAfterDeletingTailsPair34;
			maxEdgeBeforeBFS_1 = totalDegreeBeforeDeletingTailsPair34;
		}
		else if(maxEdgeAfterBFSTailDeletion_2 < totalDegreeAfterDeletingTailsPair34) {
			maxColor1_2 = 3;
			maxColor2_2 = 4;
			maxEdgeAfterBFSTailDeletion_2 = totalDegreeAfterDeletingTailsPair34;
			maxEdgeBeforeBFS_2 = totalDegreeBeforeDeletingTailsPair34;
		}
//		System.out.println("Before Tail = "+totalDegreeBeforeDeletingTailsPair34);
//		System.out.println("After Tail = "+totalDegreeAfterDeletingTailsPair34);



		g.clearRect(0, 0, 1200, 910);






		//		Final Implementation
		//	Need to show 2 backbones




		ArrayList<SensorPoint> finalpair = implementBFS(maxColor1_1, maxColor2_1);
		degreeInformation.inititialDegreeInformationForAllPointsAfterBFS();
		for(int i = 0; i < finalpair.size(); i++) {
			SensorPoint sp1 = finalpair.get(i);
			int color = sp1.getColorID();
			switch(color)
			{
			case 1:
				g.setColor(Color.RED);
				break;
			case 2:
				g.setColor(Color.GREEN);
				break;
			case 3:
				g.setColor(Color.BLUE);
				break;
			case 4:
				g.setColor(Color.YELLOW);
				break;
			default:
				break;
			}
			g.fillOval(sp1.getX(), sp1.getY(), 3, 3);
			g.setColor(Color.BLACK);
			sp1.calculateColorNeighborAfterBFSFinal(finalpair, maxColor1_1, maxColor2_1, g);
		}
//		try {
//			Thread.sleep(10000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		g.setColor(Color.WHITE);
		degreeInformation.deleteDegreeDataInListBFSFinal(maxColor1_1, maxColor2_1, finalpair, g);
		

		bfsQueue.clear();
		bfsQueueDeleted.clear();

//		try {
//			Thread.sleep(10000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		System.out.println("Color1 =  = " + maxColor1_1);
		System.out.println("Color2 =  = " + maxColor2_1);
		System.out.println("Max Edge Before BFS =  = " + maxEdgeBeforeBFS_1);
		System.out.println("Max Edge After BFS =  = " + maxEdgeAfterBFSTailDeletion_1);
		System.out.println("Domination =  = " + ((double)((double)maxEdgeAfterBFSTailDeletion_1/(double)maxEdgeBeforeBFS_1)*100));
		System.out.println("Domination =  = " + ((double)((double)maxEdgeAfterBFSTailDeletion_2/(double)maxEdgeBeforeBFS_2)*100));
		
		domination1 = ((double)((double)maxEdgeAfterBFSTailDeletion_1/(double)maxEdgeBeforeBFS_1)*100);
		domination2 = ((double)((double)maxEdgeAfterBFSTailDeletion_2/(double)maxEdgeBeforeBFS_2)*100);
		
		order1 = maxEdgeAfterBFSTailDeletion_1/2;
		order2 = maxEdgeAfterBFSTailDeletion_2/2;
		
		size1 = finalpair.size();
		
		Draw2 draw2 = Draw2.getInstance();
		draw2.setData(maxColor2_1, maxColor2_2, domination2, order2);
		
	}

//	private void drawNeighborsAfterBFS(ArrayList<SensorPoint> pair, int color1, int color2, Graphics g) {
//		for(int i = 0; i < pair.size(); i++) {
//			SensorPoint sp1 = pair.get(i);
//			if(sp1.getColorID() == color1) {
//				sp1.drawNeighborsAfterBFS(g, color2);
//			}
//			else if(sp1.getColorID() == color2) {
//				sp1.drawNeighborsAfterBFS(g, color1);
//			}
//		}
//	}



	private ArrayList<SensorPoint> bfsQueue = new ArrayList<SensorPoint>();
	private ArrayList<SensorPoint> bfsQueueDeleted = new ArrayList<SensorPoint>();

	private ArrayList<SensorPoint> implementBFS(int color1, int color2) {


		ColorInformation colorInformation = ColorInformation.getInstance();
		ArrayList<SensorPoint> pair = colorInformation.getColorPoint().get(color1);
		pair.addAll(colorInformation.getColorPoint().get(color2));
		int count = 0;
		bfsQueue.add(pair.get(0));
		do {
			SensorPoint sp = bfsQueue.get(0);
			int color = sp.getColorID();
			if(color == color1) {
				ArrayList<SensorPoint> spn = sp.getNeighborColorPointsForRespectiveColor(color2);
				for(int i = 0; i < spn.size(); i++) {
					if(!bfsQueue.contains(spn.get(i)) && !bfsQueueDeleted.contains(spn.get(i))) {
						bfsQueue.add(spn.get(i));
					}
				}
			}
			else if(color == color2){
				ArrayList<SensorPoint> spn = sp.getNeighborColorPointsForRespectiveColor(color1);
				for(int i = 0; i < spn.size(); i++) {
					if(!bfsQueue.contains(spn.get(i)) && !bfsQueueDeleted.contains(spn.get(i))) {
						bfsQueue.add(spn.get(i));
					}
				}
			}

			bfsQueueDeleted.add(sp);
			bfsQueue.remove(0);
			count++;
		}while(bfsQueue.size()>0);

		System.out.println("Color and Size = " + color1 + "  " + color2 + "  " + pair.size() + "  " + bfsQueueDeleted.size());
		System.out.println("Count = " + count);


		return (ArrayList<SensorPoint>) bfsQueueDeleted.clone();

	}

}
