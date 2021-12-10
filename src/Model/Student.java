package Model;

public class Student extends Person {
	private final String OBJ_TYPE = "student";
	
	public Student(int id, int age, String fName, String lName) {
		super(id, age, fName, lName);
	}
	
	public String getType() {
		return OBJ_TYPE;
	}
	
	@Override
	public String toString() {
		return OBJ_TYPE+","+super.toString();
	}
}