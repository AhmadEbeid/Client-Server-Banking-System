package serverBank;

public class SendData {
	String property;
	String gsonData;
	
	SendData(String p, String d){
		property = p;
		gsonData = d;
	}
	
	String getProperty() {
		return property;
	}
	
	String getGsonData() {
		return gsonData;
	}
}
