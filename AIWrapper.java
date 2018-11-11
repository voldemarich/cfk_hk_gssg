import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class AIWrapper {

	@SuppressWarnings({ "unchecked", "deprecation" })
	public static void main(String[] args) {
		JSONParser parser = new JSONParser();
		try {
	    /*URL path = DataParser.class.getResource(args[0]);*/
		  URL path = DataParser.class.getResource("datasetbig.json");
		  File inputFile = new File(path.getFile());
		  Object jsonFile = parser.parse(new FileReader(inputFile));
		  JSONObject jsonObject = (JSONObject) jsonFile;
		  JSONArray data = (JSONArray) jsonObject.get("dataset");
	      Iterator<JSONObject> dataIterator = data.iterator();
	      String output = "";
	      while (dataIterator.hasNext()) {
	        JSONObject dataNext = dataIterator.next();
	        String inputDate = (String)dataNext.get("date");
	        System.out.println(inputDate);
	        SimpleDateFormat Dateparser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        Date date = Dateparser.parse(inputDate);
	        System.out.println(date);
	        double allTime = 24*60*60;
			    double time = (date.getHours()*60 + date.getMinutes())*60 + date.getSeconds();
	        double timeSin = Math.round(Math.sin(2*Math.PI*time/allTime)*10000.0)/10000.0;
	        output+=timeSin+",";
	        double timeCos = Math.round(Math.cos(2*Math.PI*time/allTime)*10000.0)/10000.0;
	        output+=timeCos+",";
	        /*for (int i=0; i<24;i++) {
	          if(date.getHours()==i)
	            output+="1,";
	          else
	            output+="0,";
	        }
	         for (int i=0; i<60;i++) {
	            if(date.getMinutes()==i)
	              output+="1,";
	            else
	              output+="0,";
	          }*/
	        for(int i=0; i<12; i++) {
	        	if(date.getMonth() == i)
	        	  output += "1,";
	        	else
	        	  output+= "0,";
	        }
	        for(int i=1;i<32;i++) {
	          if(date.getDate() == i)
	            output+="1,";
	          else
	            output+="0,";
	        }
	        for(int i=0;i<7;i++) {
	          if(date.getDay()==i)
	            output+="1,";
	          else
	            output+="0,";
	        }
	        Double level = (Double) dataNext.get("level");
	        output+=level+"\n";
	      }
	      
	      File directory = new File(".");
		  String currentDirectory = directory.getAbsolutePath().substring(0, directory.toString().length() - 1);
	      try (FileWriter file = new FileWriter(currentDirectory + "datasetbig.txt")) {
		    file.write(output);
		    System.out.println("Successfully Created DataSet File. Into" + currentDirectory);
		  }
		}
		catch (Exception e) {
		  e.printStackTrace();
		}
	
	}

}
