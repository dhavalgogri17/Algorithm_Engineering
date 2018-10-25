import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ColorInformation {

	private static ColorInformation colorInformation = null;
	private Map<SensorPoint, Integer> colorMap = new HashMap<SensorPoint, Integer>();
	private ArrayList<SensorPoint> listOfPointsWithLessDegree = new ArrayList<SensorPoint>();
	private Map<Integer, ArrayList<SensorPoint>> colorList = new HashMap<Integer, ArrayList<SensorPoint>>();
//	private int pair12 = 0;
//	private int pair13 = 0;
//	private int pair14 = 0;
//	private int pair23 = 0;
//	private int pair24 = 0;
//	private int pair34 = 0;
//	private int color1 = -1;
//	private int color2 = -1;


	public static ColorInformation getInstance() {
		if(colorInformation == null) {
			colorInformation = new ColorInformation();
			return colorInformation;
		}
		return colorInformation;
	}

	public void addColorDataInMap(SensorPoint sp, int color) {
		colorMap.put(sp, color);
	}

	public void addDataInListOfPointsWithLessDegree(SensorPoint sp) {
		listOfPointsWithLessDegree.add(sp);
	}

	public ArrayList<SensorPoint> getListOfPointsWithLessDegree(){
		return listOfPointsWithLessDegree; 
	}

	public void addArrayListColorData(SensorPoint sp, int color) {
		if(colorList.get(color) != null) {
			ArrayList<SensorPoint> s = colorList.get(color);
			s.add(sp);
			colorList.put(color, s);
		}
		else{
			ArrayList<SensorPoint> s = new ArrayList<SensorPoint>();
			s.add(sp);
			colorList.put(color, s);
		}
	}

	public Map<Integer, ArrayList<SensorPoint>> getColorPoint(){
		return colorList;
	}




}
