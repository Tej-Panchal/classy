package DataBase;

import java.io.*;
import java.security.KeyPair;
import java.time.LocalDate;
import java.time.LocalTime;

import Model.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.github.lgooddatepicker.zinternaltools.Pair;

// Singleton DataBase
public class DataBase {
	public static final String DATA = "assets/Data.txt";
	public static final String DELIMITER = ",";
	private static final int LIMIT = 1;
	private static int trackIDs = 0;
	private static int count = 0;
	private static DataBase dataBase = null;
	private static Teacher teacher;
	private static Map<Integer, Student> students = new HashMap<Integer, Student>();
	private static Map<Integer, Assignment> assignments = new HashMap<Integer, Assignment>();
	private static Map<Integer, Map<Integer, Integer>> grades = new HashMap<Integer, Map<Integer, Integer>>();

	private DataBase() {
	}

	public static synchronized DataBase getDB() {
		if (count < LIMIT) {
			dataBase = new DataBase();
			count += 1;

			loadData();

			return dataBase;
		} else if (dataBase != null) {
			return dataBase;
		}

		return null;
	}

	public static Teacher getTeacher() {
		return teacher;
	}

	public static Map<Integer, Student> getStudents() {
		return students;
	}

	public static Student getStudentByID(int id){
		return students.get(id);
	}
	
	public static Map<Integer, Assignment> getAssignments() {
		return assignments;
	}
	
	public static Assignment getAssignmentByID(int id){
		return assignments.get(id);
	}

	public static Map<Integer, Map<Integer, Integer>> getGrades() {
		return grades;
	}

	private void setTeacher(String[] tempArr) {
		teacher = new Teacher(Integer.parseInt(tempArr[1]), Integer.parseInt(tempArr[2]), tempArr[3], tempArr[4]);
	}

	private void setStudents(String[] tempArr) {
		students.put(Integer.parseInt(tempArr[1]),
				new Student(Integer.parseInt(tempArr[1]), Integer.parseInt(tempArr[2]), tempArr[3], tempArr[4]));
	}

	private static void setAssignments(String[] tempArr) {
		ArrayList<Integer> tempList = new ArrayList<Integer>();

		// The 7th spot in this array indicates if the assignment has any associated
		// files
		if (!tempArr[7].equals("NONE")) {
			for (int i = 7; i < tempArr.length; i++) {
				tempList.add(Integer.parseInt(tempArr[i]));
			}
		}
		assignments.put(Integer.parseInt(tempArr[1]), new Assignment(Integer.parseInt(tempArr[1]), tempArr[2],
				Integer.parseInt(tempArr[3]), String.valueOf(tempArr[4]), tempArr[5], tempArr[6], tempList));
	}

	private static void setGrades(String[] tempArr) {
		Map<Integer, Integer> assignmentGrade;

		// Check if a map has been created for the assignment, if not make one else, get
		// the map and store the grade
		if (grades.get(Integer.valueOf(tempArr[1])) == null) {
			assignmentGrade = new HashMap<Integer, Integer>();
			assignmentGrade.put(Integer.parseInt(tempArr[2]), Integer.parseInt(tempArr[3]));
			grades.put(Integer.parseInt(tempArr[1]), assignmentGrade);
		} else {
			assignmentGrade = grades.get(Integer.parseInt(tempArr[1]));
			assignmentGrade.put(Integer.parseInt(tempArr[2]), Integer.parseInt(tempArr[3]));
		}
	}

	// Store all student grades in a map by student id, then store that map by
	// assignment id
	private static void setGrades(Integer assignmentID, Object[] studentIDS) {
		System.out.println("lenth: "+studentIDS.length);
		Map<Integer, Integer> assignmentGrades = new HashMap<Integer, Integer>();
		for (Object id : studentIDS) {
			System.out.println("id "+String.valueOf(id));
			assignmentGrades.put(Integer.valueOf((int)id), Integer.valueOf(-1));
		}

		grades.put(assignmentID, assignmentGrades);
	}

	public static Pair<String, Integer> addAssignement(String name, int points, String description, LocalDate dueDate,
			LocalTime dueTime, ArrayList<Integer> fileIDs) {
		int tempID = getNewID();
		assignments.put(tempID, new Assignment(tempID, name, points, description, dueDate, dueTime, fileIDs));
		setGrades(Integer.valueOf(tempID), students.keySet().toArray());
		return new Pair<String, Integer>(name, tempID);
	}

	public static void deleteAssignment(int assignmentID) {
		System.out.println("deleting");

		if (assignments.get(assignmentID) != null) {
			System.out.println("Id " + assignmentID + " found and deleted");
			assignments.remove(assignmentID);
			grades.remove(assignmentID);
		}
	}

	private static void setID(int ID) {
		trackIDs = ID;
		System.out.println(ID);
	}

	private static int getNewID() {
		System.out.println(++trackIDs);
		return trackIDs;
	}

	// Loads csv fomated data from a txt file
	private static void loadData() {
		try {
			File file = new File(DATA);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line = "";
			String[] tempArr;
			while ((line = br.readLine()) != null) {
				tempArr = line.split(DELIMITER);
				if (tempArr.length == 1) {
					setID(Integer.parseInt(tempArr[0]));
				} else if (tempArr[0].equals("teacher")) {
					dataBase.setTeacher(tempArr);
				} else if (tempArr[0].equals("student")) {
					dataBase.setStudents(tempArr);
				} else if (tempArr[0].equals("assignment")) {
					dataBase.setAssignments(tempArr);
				} else if (tempArr[0].equals("grade")) {
					dataBase.setGrades(tempArr);
				}
			}
			br.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	// Stores data in a txt file in CSV format
	public static void saveData() {
		try {
			FileWriter fw = new FileWriter(DATA);
			fw.write(Integer.toString(trackIDs));
			fw.write('\n' + teacher.toString());

			if (!students.isEmpty()) {
				for (Map.Entry<Integer, Student> student : students.entrySet()) {
					fw.write('\n' + student.getValue().toString());
				}
			}

			if (!assignments.isEmpty()) {
				for (Map.Entry<Integer, Assignment> assignment : assignments.entrySet()) {
					fw.write('\n'+assignment.getValue().toString());
				}
			}

			if (!grades.isEmpty()) {
				for (Map.Entry<Integer, Map<Integer, Integer>> assignments : grades.entrySet()) {
					Map<Integer, Integer> studentGrades = assignments.getValue();
					for (Map.Entry<Integer, Integer> grade : studentGrades.entrySet()) {
						fw.write("\ngrade," + assignments.getKey().toString() + "," + grade.getKey().toString() + ","
								+ grade.getValue().toString());
					}
				}
			}
			fw.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
}