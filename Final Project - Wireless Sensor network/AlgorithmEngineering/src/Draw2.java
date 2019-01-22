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

public class Draw2 extends Canvas{

	private static Draw2 draw2 = null;
	private DegreeInformation degreeInformation;
	private int pointSize = 3;
	private int maxColor2_1 = -1, maxColor2_2 = -1, order2 = -1, size2 = -1;
	private double domination2 = 0;
	
	
	public static Draw2 getInstance() {
		if(draw2 == null) {
			draw2 = new Draw2();
			return draw2;
		}
		return draw2;
	}
	public Draw2() {
		degreeInformation = DegreeInformation.getInstance();
		degreeInformation.inititialDegreeInformationForAllPointsAfterDeletion();
		JFrame frame = new JFrame("Backbone Determination in Wireless Sensor Network");
		this.setSize(1400, 900);
		frame.add(this);
		frame.pack();
		frame.setBackground(Color.WHITE);
		frame.setVisible(true);
	}

	public void setData(int maxColor2_1, int maxColor2_2, double domination2, int order2) {
		this.maxColor2_1 = maxColor2_1;
		this.maxColor2_2 = maxColor2_2;
		this.domination2 = domination2;
		this.order2 = order2;
	}

	
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
	
	public void paint(Graphics g) {
		if(maxColor2_1 == -1 || maxColor2_2 == -1) {
			return;
		}
		
		ArrayList<SensorPoint> finalpair = implementBFS(maxColor2_1, maxColor2_2);
		degreeInformation.inititialDegreeInformationForAllPointsAfterBFS();
		this.size2 = finalpair.size();
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
			sp1.calculateColorNeighborAfterBFSFinal(finalpair, maxColor2_1, maxColor2_2, g);
		}
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		g.setColor(Color.WHITE);
		degreeInformation.deleteDegreeDataInListBFSFinal(maxColor2_1, maxColor2_2, finalpair, g);
		

		bfsQueue.clear();
		bfsQueueDeleted.clear();
		
		g.setFont(new Font("TimesNewRoman", Font.PLAIN, 15)); 
		g.setColor(Color.BLACK);

		g.drawString("PHASE 3 INFORMATION", 915, 25);
		g.drawString("BACKBONE 2", 915, 50);
		g.drawString("Order 2(Number of Unique Edges) = " + order2, 915, 75);
		g.drawString("Size 2(Number of Vertex) = " + size2, 915, 100);
		g.drawString("Domination 2 = " + domination2 + " %", 915, 125);
		
		
	}
}
