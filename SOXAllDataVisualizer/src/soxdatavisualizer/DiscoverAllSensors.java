package soxdatavisualizer;

import java.io.IOException;
import java.util.List;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import jp.ac.keio.sfc.ht.sox.soxlib.SoxConnection;
import jp.ac.keio.sfc.ht.sox.soxlib.SoxDevice;

public class DiscoverAllSensors {

	public static void main(String[] args){
		new DiscoverAllSensors();
	}
	
	public DiscoverAllSensors(){
		
		try {
			SoxConnection con=null;
			con = new SoxConnection("sox.ht.sfc.keio.ac.jp","allsubscriber","miromiro","smack",true);
			
			List<String> nodeList = con.getAllSensorList(); //get sensor node list from loginned server
			//List<String> nodeList = con.getAllSensorList("takusox.ht.sfc.keio.ac.jp"); //get sensor node list from specific server (for SOX federation)
			
			int i=0;
			for(String node:nodeList){
				try {
					SoxDevice d = new SoxDevice(con, node);
					d.subscribe();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				i++;
				System.out.println(i+"/"+nodeList.size());
				
			}



		} catch (SmackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	
}
