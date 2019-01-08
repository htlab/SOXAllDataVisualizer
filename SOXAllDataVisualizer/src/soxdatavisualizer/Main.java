package soxdatavisualizer;

import java.util.List;

import javax.swing.JFrame;

import org.jfree.ui.RefineryUtilities;

import jp.ac.keio.sfc.ht.sox.protocol.TransducerValue;
import jp.ac.keio.sfc.ht.sox.soxlib.SoxConnection;
import jp.ac.keio.sfc.ht.sox.soxlib.SoxDevice;
import jp.ac.keio.sfc.ht.sox.soxlib.event.AllSoxEventListener;
import jp.ac.keio.sfc.ht.sox.soxlib.event.SoxEvent;
import jp.ac.keio.sfc.ht.sox.soxlib.event.SoxEventListener;

public class Main implements AllSoxEventListener {

	InformationBoard b1, b2, b3;
	List<TransducerValue> v1, v2, v3;
	String name1, name2, name3;
	DynamicDataDemo demo;
	MapVisualizer map;
	BallVisualizer ball;

	public static void main(String args[]) {
		new Main();
	}

	public Main(MapVisualizer _map) {
		map = _map;

		demo = new DynamicDataDemo("Num of data received ");
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);

		b1 = new InformationBoard();
		b1.setTitle("Sensor Data");
		
		//Ball Visualizer
		ball = new BallVisualizer();
		ball.init();
		JFrame frame = new JFrame();
		frame.setSize(1200,600);
		javax.swing.JPanel panel = new javax.swing.JPanel();
		panel.setBounds(0, -0, ball.width, ball.height);
		panel.add(ball);
		frame.add(panel);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setTitle("Ball Visualizer");
		
		// SOX
		try {
			SoxConnection con = new SoxConnection("sox.ht.sfc.keio.ac.jp", "allsubscriber", "miromiro", "smack", false);
			con.addAllSoxEventListener(this);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Main() {

		demo = new DynamicDataDemo("Num of data received ");
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);

		b1 = new InformationBoard();
		b1.setTitle("1");
		b2 = new InformationBoard();
		b2.setTitle("2");
		b3 = new InformationBoard();
		b3.setTitle("3");

		// SOXへの接続
		try {
			SoxConnection con = new SoxConnection("sox.ht.sfc.keio.ac.jp", "allsubscriber", "miromiro", "smack", false);
			con.addAllSoxEventListener(this); //イベントリスナへ登録

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleAllPublishedSoxEvent(SoxEvent e) {
		// TODO Auto-generated method stub

		try{
		System.out.println(":::::Received Data:::::");

		System.out.println(e.getNodeID() + " from: " + e.getOriginServer());
		List<TransducerValue> values = e.getTransducerValues();

		float lat=0,lon=0;

	
		for (TransducerValue value : values) {
			if(value.getId().startsWith("lat")||value.getId().startsWith("Lat")){
				lat = new Float(value.getRawValue()).floatValue();
			}
			if(value.getId().startsWith("lon")||value.getId().startsWith("Lon")){
				lon = new Float(value.getRawValue()).floatValue();
			}
		
		 }
		

		b1.setData(e.getNodeID(), values);

		name1 = e.getNodeID();
		v1 = values;

		demo.plus();
		
		if(lat!=0){
			if(map!=null){
				map.addMarker(lat, lon);
			}
		}
		
		ball.addBall(e.getNodeID(), values);
		}catch(Exception ex){
			ex.printStackTrace();
		}

	}
}
