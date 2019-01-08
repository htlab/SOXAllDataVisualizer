package soxdatavisualizer;
import processing.core.PApplet;
import processing.core.PImage;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;
import jp.ac.keio.sfc.ht.sox.protocol.TransducerValue;


/**
 * Hello Unfolding World.
 * 
 * Download the distribution with examples for many more examples and features.
 */
public class BallVisualizer extends PApplet {

	ArrayList<Ball> balls;
	int ballWidth = 38;
	int w=1200;
	int h=600;
	List<TransducerValue> sampleList;

	public void setup() {
	  size(w, h);
	  noStroke();

	  sampleList = new ArrayList<TransducerValue>();
	  TransducerValue v = new TransducerValue();
	  v.setCurrentTimestamp();
	  v.setId("temperature");
	  v.setRawValue("25");
	  sampleList.add(v);
	  
	  TransducerValue v2 = new TransducerValue();
	  v2.setCurrentTimestamp();
	  v2.setId("humidity");
	  v2.setRawValue("60");
	  sampleList.add(v2);
	  
	  
	  
	  // Create an empty ArrayList (will store Ball objects)
	  balls = new ArrayList<Ball>();
	  
	  // Start by adding one element
	  //balls.add(new Ball(width/2, 0, ballWidth));
	}

	public void draw() {
	  background(255);

	  // With an array, we say balls.length, with an ArrayList, we say balls.size()
	  // The length of an ArrayList is dynamic
	  // Notice how we are looping through the ArrayList backwards
	  // This is because we are deleting elements from the list  
	  for (int i = balls.size()-1; i >= 0; i--) { 
	    // An ArrayList doesn't know what it is storing so we have to cast the object coming out
	    Ball ball = balls.get(i);
	    ball.move();
	    ball.display();
	    if (ball.finished()) {
	      // Items can be deleted with remove()
	      balls.remove(i);
	    }
	    
	  }  
	  
	}

	public void mousePressed() {
	  // A new ball object is added to the ArrayList (by default to the end)
	  balls.add(new Ball(mouseX, mouseY, ballWidth,"hoge",sampleList,null));
	}

	public void addBall(String _sensorId,List<TransducerValue> _values){
		
		BufferedImage image = null;

		for (TransducerValue value : _values) {
			if (value.getRawValue().startsWith("data:image")) {

				byte[] bytes = Base64.decodeBase64(value.getRawValue().split(",")[1].getBytes());
				// System.out.println(strs[1]);
				ByteArrayInputStream input = new ByteArrayInputStream(bytes);

				try {
					image = ImageIO.read(input);
					if (image != null && image.getWidth() > 75) {
						// resize if image size is big
						image = imageResize(image, 75, image.getHeight() * 75 / image.getWidth());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		if(image!=null){
			balls.add(new Ball((int)(w*Math.random()),(int)(20+50*Math.random()),ballWidth,_sensorId,_values,BImage2PImage(image)));
		}else{
			balls.add(new Ball((int)(w*Math.random()),(int)(20+50*Math.random()),ballWidth,_sensorId,_values,null));
		}
	}


	// Simple bouncing ball class

	class Ball {
	  
	  float x;
	  float y;
	  float speed;
	  float gravity;
	  float w;
	  float life = 255;
	  List<TransducerValue> values;
	  String sensorId;
	  
	  float r=random(0,255);// 0≦ｒ≦255
	  float g=random(0,255);// 0≦ｇ≦255
	  float b=random(0,255);// 0≦ｂ≦255
	  PImage pimg;

	  
	  Ball(float tempX, float tempY, float tempW, String _sensorId,List<TransducerValue> _values,PImage _pimg) {
	    x = tempX;
	    y = tempY;
	    w = tempW;
	    speed = 0;
	    gravity = 0.1f;
	    sensorId = _sensorId;
	    values = _values;
	    pimg = _pimg;
	  }
	  
	    void move() {
	    // Add gravity to speed
	    speed = speed + gravity;
	    // Add speed to y location
	    y = y + speed;
	    // If square reaches the bottom
	    // Reverse speed
	    if (y > height) {
	      // Dampening
	      speed = speed * -0.6f; //default is -0.8
	      y = height;
	    }
	  }
	  
	  boolean finished() {
	    // Balls fade out
	    life--;
	    if (life < 0) {
	      return true;
	    } else {
	      return false;
	    }
	  }
	  
	  void display() {
	    // Display the circle
	    fill(r,g,b,life);
	    //stroke(0,life);
	    ellipse(x,y,w,w);
	    	    
	    fill(0,life);
	    int textline=0;
	    text(sensorId,x-w,y-w/2-10);
	    for(int i=0;i<values.size();i++){
	    	if(!values.get(i).getRawValue().startsWith("data:image")||values.get(i).getId().startsWith("url")){
	    		text(values.get(i).getId()+":"+values.get(i).getRawValue(),x,y+w+10*textline);
	    		textline++;
	    	}
	    }
	    
	    if(pimg!=null){
	    	tint(255,life);
	    	image(pimg,x+w,y-w/2-10);
	    }
	    
	    
	  }
	} 
	
	  public BufferedImage imageResize(BufferedImage img, int width, int height) {
			BufferedImage thumb = new BufferedImage(width, height, img.getType());
			thumb.getGraphics().drawImage(
					img.getScaledInstance(width, height, img.SCALE_AREA_AVERAGING),
					0, 0, width, height, null);
			return thumb;
		}
	  
	private PImage BImage2PImage(BufferedImage image) {
		// TODO Auto-generated method stub
		PImage pImg = createImage(image.getWidth(), image.getHeight(),this.ARGB);
		for (int y = 0; y < pImg.height; y++) {
			for (int x = 0; x < pImg.width; x++) {
				pImg.pixels[y * pImg.width + x] = image.getRGB(x, y);
			}
		}
		return pImg;
	}
}
