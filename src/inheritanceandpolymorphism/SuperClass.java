package inheritanceandpolymorphism;

public class SuperClass {

	private String superClassID;

	
	public SuperClass(String scId){
		System.out.println("That´s the superclass constructor");
		this.superClassID = scId;
	}
	
	

//	@Override
//	public String toString() {
//		//System.out.println("That´s the superclass toString method");
//		return superClassID;
//	}

//	public String showData(){
//		return "";//superClassID;
//	}

	public String getSuperClassID() {
		return superClassID;
	}

	public void setSuperClassID(String superClassID) {
		this.superClassID = superClassID;
	}
	
	public static void main(String[] args){
		SuperClass obj1 = new SubClass("1", "1");
		System.out.println(((SubClass) obj1).showData());
	}
	
}
