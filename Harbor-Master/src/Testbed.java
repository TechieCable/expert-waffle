import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Testbed extends JPanel implements ActionListener, KeyListener, MouseListener, MouseMotionListener {
	public static int screenW = 1920, screenH = 1080;

	public static int maxBoats = 1;

	static Label stat;
	Boat b = new Boat(screenW / 2, screenH / 2, 1);

	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, screenW, screenH);
		b.paint(g);
		b.dockInfo.docked = true;
		double tempAngle = Math.PI * 2 - b.angle;
		tempAngle %= Math.PI * 2;
		display("" + (tempAngle * 180 / Math.PI));
		g.setColor(Color.WHITE);
		g.drawString("height = " + b.height, 100, 50);
		g.drawString("width = " + b.width, 100, 75);
		g.drawString("height * cos(angle) = " + Math.abs(b.height * Math.cos(tempAngle)), 100, 100);
		g.drawString("height * sin(angle) = " + Math.abs(b.height * Math.sin(tempAngle)), 100, 125);
		g.drawString("width * cos(angle) = " + Math.abs(b.width * Math.cos(tempAngle)), 100, 150);
		g.drawString("width * sin(angle) = " + Math.abs(b.width * Math.sin(tempAngle)), 100, 175);

		g.drawString(
				"suggested height = "
						+ (Math.abs(b.width * Math.sin(tempAngle)) + Math.abs(b.height * Math.sin(tempAngle))),
				100, 225);
		g.drawString(
				"suggested width = "
						+ (Math.abs(b.width * Math.cos(tempAngle)) + Math.abs(b.height * Math.cos(tempAngle))),
				100, 250);

	}

	public static void main(String[] arg) {
		@SuppressWarnings("unused")
		Testbed d = new Testbed();
	}

	public Testbed() {
		JFrame frame = new JFrame("Harbor Master TestBed");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Testbed.class.getResource("/imgs/boat1-0.png")));
		frame.setSize(screenW, screenH);
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addKeyListener(this);
		frame.addMouseListener(this);
		frame.addMouseMotionListener(this);
		frame.getContentPane().add(BorderLayout.NORTH, stat = new Label());
		Testbed.stat.setSize(frame.getSize().width, stat.getSize().height);
		t.start();

		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setUndecorated(true);

		generate();

		frame.setVisible(true);

		try {
			frame.getContentPane().setCursor(Toolkit.getDefaultToolkit()
					.createCustomCursor(ImageIO.read(new File("Cursor.png")), new Point(0, 0), "blank cursor"));
		} catch (Exception e) {
		}
	}

	public void generate() {
	}

	public static void display(String s) {
		System.out.println(s);
		stat.setText(s);
	}

	public static void setStatus(String s) {
		stat.setText(s);
	}

	Timer t = new Timer(16, this);

	public void actionPerformed(ActionEvent m) {
		repaint();
	}

	public void keyPressed(KeyEvent m) {
		switch (m.getKeyCode()) {
		case 37:
			b.angle -= Math.PI / 12;
			break;
		case 39:
			b.angle += Math.PI / 12;
			break;

		default:
			break;
		}

	}

	public void keyReleased(KeyEvent m) {

	}

	public void keyTyped(KeyEvent m) {

	}

	public void mouseClicked(MouseEvent m) {

	}

	public void mouseEntered(MouseEvent m) {
	}

	public void mouseExited(MouseEvent m) {
	}

	public void mousePressed(MouseEvent m) {
	}

	public void mouseReleased(MouseEvent m) {
	}

	public void mouseDragged(MouseEvent m) {
	}

	public void mouseMoved(MouseEvent m) {
	}

}