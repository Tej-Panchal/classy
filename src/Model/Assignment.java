package Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class Assignment {
	private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	private final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
	private final String OBJ_TYPE = "assignment";
	private int id;
	private String name;
	private int points;
	private String description;
	private LocalDate dueDate;
	private LocalTime dueTime;
	private ArrayList<Integer> fileIDs = new ArrayList<Integer>();
	private boolean graded = false;
	
	//Constructor - from file
	public Assignment(int id, String name, int points, String description, String dueDate, String dueTime, ArrayList<Integer> fileIDs) {
		this.id = id;
		this.name = name;
		this.points = points;
		this.description = strFilterIn(description);	
		this.dueDate = LocalDate.parse(dueDate, dateFormat);
		this.dueTime = LocalTime.parse(dueTime, timeFormat);
		this.fileIDs = fileIDs;
	}

	//Constructor - from file
	public Assignment(int id, String name, int points, String description, LocalDate dueDate, LocalTime dueTime, ArrayList<Integer> fileIDs) {
		this.id = id;
		this.name = name;
		this.points = points;
		this.description = strFilterIn(description);	
		this.dueDate = dueDate;
		this.dueTime = dueTime;
		this.fileIDs = fileIDs;
	}
	
	//Getters
	public String getType() {
		return OBJ_TYPE;
	}
	
	public int getID() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getPoints() {
		return this.points;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public LocalDate getDueDate() {
		return this.dueDate;
	}
	
	public LocalTime getDueTime() {
		return this.dueTime;
	}
	
	public ArrayList<Integer> getFileIDs() {
		return fileIDs;
	}
	
	public boolean getGraded() {
		return graded;
	}
	
	public String strFilterOut(String str) {
		return (str.equals(""))?"NONE": str;
	}
	
	public String strFilterIn(String str) {
		return (str.equals("NONE"))?"": str;
	}
	
	public String fileIDsToString() {
		String str = "";
		
		if(!this.fileIDs.isEmpty())
		{
			for(int i = 0; i < this.fileIDs.size() ;i++) {
				if(i+1 < this.fileIDs.size()) {
					str +=this.fileIDs.get(i).toString()+",";
				}
				else {
					str +=this.fileIDs.get(i).toString();
				}
			}			
		}
		return strFilterOut(str);
	}
	
	//Setters
	public void setID(int id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setPoints(int points) {
		this.points = points;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}
	
	public void setDueTime(LocalTime dueTime) {
		this.dueTime = dueTime;
	}
	
	public void setFileIDs(ArrayList<Integer> fileIDs) {
		this.fileIDs = fileIDs;
	}
	
	public void getGraded(boolean graded) {
		this.graded = graded;
	}
	
	@Override
	public String toString() {
		return OBJ_TYPE+","+this.id+","+this.name+","+this.points+","+strFilterOut(this.description)+","+dateFormat.format(dueDate)+","+timeFormat.format(dueTime)+","+fileIDsToString();
	}
}