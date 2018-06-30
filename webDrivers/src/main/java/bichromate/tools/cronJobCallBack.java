package bichromate.tools;

public class cronJobCallBack implements healthCheckCallBack{

	public void callback_method(String info) {
		System.out.println("FROM CALLBACK:" +info);
		
	}

	

}
