import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

import javax.swing.JFrame;

public class MainClass{

	private static final int screenSize = 900;
	private static int noOfSensors;
	private static double averageDegree;
	private static double r = 0;
	private static ArrayList<SensorPoint> sensorPoints = new ArrayList<SensorPoint>();
	private static Cell cell[][] = null;
	private static final boolean testCase = false;


	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		Random rand = new Random();

		System.out.println("Enter Number of Sensors");
		noOfSensors = sc.nextInt(); 

		System.out.println("Enter Average Degree");
		averageDegree = sc.nextDouble();

		System.out.println("Enter Topology, 1 for Square and 2 for Disk");
		int topologySelection = sc.nextInt();


		if (topologySelection == 1) {
			System.out.print("Radius = " + (double)Math.sqrt(averageDegree/(double)(noOfSensors*Math.PI)));
			r = (double)Math.sqrt(averageDegree/(double)(noOfSensors*Math.PI)) * (double)screenSize;
		}
		else {
			System.out.print("Radius = " + (double)Math.sqrt(averageDegree/(double)(noOfSensors)));
			r = (double)Math.sqrt(averageDegree/(double)(noOfSensors)) * (double)screenSize/2;
		}

		
		if(testCase) {
			noOfSensors = 20;
			r = 0.4 * (double)screenSize;
			if(topologySelection == 1) {
//				r = (double)Math.sqrt(averageDegree/(double)(noOfSensors*Math.PI)) * (double)screenSize;
				averageDegree = (int)(0.4*0.4*noOfSensors * Math.PI);
			}
			else {
				averageDegree = (int)(0.4*0.4*noOfSensors);
			}
		}
		
		double d = r;
		System.out.println(d + "    " + averageDegree);
		
		
		int numberofSubCells = (int) Math.ceil((screenSize)/(d));
		//		System.out.println("Total Sub Cells = " + numberofSubCells + "   " + r + "   " + noOfSensors + "   ACT R = " + (double)Math.sqrt(averageDegree/(double)(noOfSensors*Math.PI)));



		DrawClass drawClass = null;
//		DrawClass2 drawClass2 = null;
		cell = new Cell[numberofSubCells + 2][numberofSubCells + 2];
		for (int i = 0; i < numberofSubCells + 2; i++)
		{
			for (int j = 0; j < numberofSubCells + 2; j++)
			{
				cell[i][j] = new Cell();
			}
		}


		


		if(topologySelection == 1) {
			//SQUARE
			for(int i = 0; i< noOfSensors; i = i + 1) {
				int rand_int1 = rand.nextInt(screenSize);
				int rand_int2 = rand.nextInt(screenSize);

				int a = (int)(Math.floor(rand_int1/d)) + 1;
				int b = (int)(Math.floor(rand_int2/d)) + 1;
				//				int p = ((int)(Math.floor(rand_int1/d)) + (int)(Math.floor(rand_int2/d) * numberofSubCells));

				SensorPoint sp = new SensorPoint(rand_int1, rand_int2, a, b, numberofSubCells);
				sp.setPointName("Point " + i);
				sensorPoints.add(sp);
				cell[a][b].addSensorPoints(sp);
				
			}
			drawClass = DrawClass.getInstance();
			drawClass.setData(cell, sensorPoints, r, averageDegree, (double)Math.sqrt(averageDegree/(double)(noOfSensors*Math.PI)));
		}
		else {
			//https://stackoverflow.com/questions/2508704/draw-a-circle-with-a-radius-and-points-around-the-edge
			//Disk
			for(int i = 0; i< noOfSensors; i = i + 1) {
				int x1 = rand.nextInt(screenSize);
				int y1 = rand.nextInt(screenSize);
				int distance = (int) (Math.pow(screenSize/2 - x1,2) + Math.pow(screenSize/2-y1,2));
				if(distance < (screenSize*screenSize/4)) {
					//					int p = ((int)(Math.floor(x1/d)) + ((int)(Math.floor(y1/d)) * numberofSubCells));
					int a = (int)(Math.floor(x1/d)) + 1;
					int b = (int)(Math.floor(y1/d)) + 1;
					//					
					SensorPoint sp = new SensorPoint(x1, y1, a, b, numberofSubCells);
					sensorPoints.add(sp);
					cell[a][b].addSensorPoints(sp);
				}
				else {
					i--;
				}
			}
			drawClass = DrawClass.getInstance();
			drawClass.setData(cell, sensorPoints, r, averageDegree, (double)Math.sqrt(averageDegree/(double)(noOfSensors)));
		}


	}

}