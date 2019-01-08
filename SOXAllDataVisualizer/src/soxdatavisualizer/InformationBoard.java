package soxdatavisualizer;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.apache.commons.codec.binary.Base64;

import jp.ac.keio.sfc.ht.sox.protocol.TransducerValue;
import jp.ac.keio.sfc.ht.sox.soxlib.SoxConnection;
import jp.ac.keio.sfc.ht.sox.soxlib.SoxDevice;

public class InformationBoard extends JFrame {

	JLabel sensornameLabel;
	JLabel dataLabel;
	JLabel imageLabel;
	
	public static void main(String[] args) {
		new InformationBoard();
	}

	public InformationBoard() {
		this.setLayout(new BorderLayout());
		sensornameLabel = new JLabel("センサーネーム");
		dataLabel = new JLabel("<html>Latitude:hoge<br>Longitude:hoge<br></html>");
		dataLabel.setVerticalAlignment(JLabel.TOP);
		imageLabel = new JLabel("");
		imageLabel.setVerticalAlignment(JLabel.CENTER);
		imageLabel.setHorizontalAlignment(JLabel.CENTER);
		this.add("North", sensornameLabel);
		this.add("Center", dataLabel);
		this.add("South", imageLabel);

		this.setSize(500, 800);
		this.setVisible(true);

	}

	public void setData(String name, List<TransducerValue> values) {

		if (name != null) {
			sensornameLabel.setText("<html><font size=6><b>" + name + "</b></html>");
			String text = "<html><font size='4'>";
			BufferedImage image = null;

			for (TransducerValue value : values) {
				if (value.getRawValue().startsWith("data:image")) {

					byte[] bytes = Base64.decodeBase64(value.getRawValue().split(",")[1].getBytes());
					// System.out.println(strs[1]);
					ByteArrayInputStream input = new ByteArrayInputStream(bytes);

					try {
						image = ImageIO.read(input);
						if (image != null && image.getWidth() > 400) {
							// resize if image size is big
							image = imageResize(image, 400, image.getHeight() * 400 / image.getWidth());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {
					text = text + "[" + value.getId() + "] " + value.getRawValue() + " " + value.getTimestamp()
							+ "<br>";
				}
			}
			text = text + "</html>";

			dataLabel.setText(text);

			if (image != null) {
				ImageIcon imageIcon = new ImageIcon(image);
				imageLabel.setIcon(imageIcon);
			} else {
				imageLabel.setIcon(null);
			}
		}

	}

	public BufferedImage imageResize(BufferedImage img, int width, int height) {
		BufferedImage thumb = new BufferedImage(width, height, img.getType());
		thumb.getGraphics().drawImage(img.getScaledInstance(width, height, img.SCALE_AREA_AVERAGING), 0, 0, width,
				height, null);
		return thumb;
	}

}
