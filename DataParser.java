import java.net.URL;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Iterator;
 
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DataParser {
  
  @SuppressWarnings({ "deprecation", "unchecked" })
  public static void main(String[] args) {
	JSONParser parser = new JSONParser();
	try {
    /*URL path = DataParser.class.getResource(args[0]);*/
	  URL path = DataParser.class.getResource("data.json");
	  File inputFile = new File(path.getFile());
      Object jsonFile = parser.parse(new FileReader(inputFile));
      JSONObject jsonObject = (JSONObject) jsonFile;
      JSONArray data = (JSONArray) jsonObject.get("data");
      Iterator<JSONObject> dataIterator = data.iterator();
      
      JSONObject outputJson = new JSONObject();
      JSONArray dataSet = new JSONArray();
      while (dataIterator.hasNext()) {
    	JSONObject dataPoint = new JSONObject();
        JSONObject dataNext = dataIterator.next();
        /*String name = (String) dataNext.get("name");*/
        String name = (String) dataNext.get("filename");
        /*double linePos = (long) dataNext.get("linePos");*/
        /*JSONObject pot = (JSONObject) dataNext.get("pot");*/
        /*double height = (long) pot.get("height");*/
        String date = name.substring(9,13) +"-"+ name.substring(13,15)+"-"+name.substring(15,17)+" "+ name.substring(18,20)+":"+name.substring(20,22)+":"+name.substring(22,24);
        /*double coffeeLevel = Math.round((height-linePos)/height*100)/100.0;*/
        double coffeeLevel = ((double) dataNext.get("level"))/100;
        System.out.println("date " + date);
        System.out.println("level " + coffeeLevel);
        dataPoint.put("date", date);
        dataPoint.put("level", coffeeLevel);
        dataSet.add(dataPoint);
      }
      
      outputJson.put("dataset", dataSet);
      File directory = new File(".");
	  String currentDirectory = directory.getAbsolutePath().substring(0, directory.toString().length() - 1);
      try (FileWriter file = new FileWriter(currentDirectory + "datasetbig.json")) {
	    file.write(outputJson.toJSONString());
	    System.out.println("Successfully Parsed JSON Object to DataSet File. Into" + currentDirectory);
	  }
    } catch (Exception e) {
        e.printStackTrace();
    }
  }
}
