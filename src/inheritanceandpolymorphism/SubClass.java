package inheritanceandpolymorphism;

public class SubClass extends SuperClass {

	private String subClassId;
	
	public SubClass(String scId, String subcId) {
		super(scId);
		System.out.println("That´s the subclass constructor");
		this.subClassId = subcId;
	}


	@Override
	public String toString() {
		//System.out.println("That´s the subclass toString method");
		return "The superclasId is:"+super.toString()+" and the subclassId is: "+this.getSubClassId();
	}

	public String showData(){
		String result = subClassId;
		return result;
	}
	public String getSubClassId() {
		return subClassId;
	}



	public void setSubClassId(String subClassId) {
		this.subClassId = subClassId;
	}
}
