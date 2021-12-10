package Model;

public class Teacher extends Person {
	private final String OBJ_TYPE = "teacher";
	
	public Teacher(int id, int age, String fName, String lName) {
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


