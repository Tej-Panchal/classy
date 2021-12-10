package Model;

public abstract class Person {
	int id;
	int age;
	String fName;
	String lName;
	
	protected Person(int id, int age, String fName, String lName) {
		this.id = id;
		this.age = age;
		this.fName = fName;
		this.lName = lName;
	}
	
	public String getName() {
		return this.fName +" "+this.lName;
	}
	@Override
	public String toString() {
		return this.id +","+ this.age +","+ this.fName +","+ this.lName; 
	}
}