import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
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
	Boat b = new Boat(screenW / 2, screenH / 2, 4);
	Boat b2 = new Boat(screenW / 2, screenH / 2, 1);
	Polygon p = new Polygon(new int[] { 823, 739, 1000, 1283, 1177 }, new int[] { 466, 742, 906, 659, 321 }, 5);

	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, screenW, screenH);
		b.paint(g);
		b2.paint(g);
//		b.dockInfo.docked = true;

		double tempAngle = Math.PI * 2 - b.angle;
		tempAngle %= Math.PI * 2;
		int maxWidth = (int) Math.max(Math.abs(b.width * Math.sin(tempAngle)),
				Math.abs(b.height * Math.cos(tempAngle)));
		int maxHeight = (int) Math.max(Math.abs(b.width * Math.cos(tempAngle)),
				Math.abs(b.height * Math.sin(tempAngle)));
		Rectangle boatR = new Rectangle(b.ax() - maxWidth / 2, b.ay() - maxHeight / 2, maxWidth, maxHeight);

		double tempAngle2 = Math.PI * 2 - b2.angle;
		tempAngle2 %= Math.PI * 2;
		int maxWidth2 = (int) Math.max(Math.abs(b2.width * Math.sin(tempAngle2)),
				Math.abs(b2.height * Math.cos(tempAngle2)));
		int maxHeight2 = (int) Math.max(Math.abs(b2.width * Math.cos(tempAngle2)),
				Math.abs(b2.height * Math.sin(tempAngle2)));

		Rectangle boatR2 = new Rectangle(b2.ax() - maxWidth2 / 2, b2.ay() - maxHeight2 / 2, maxWidth2, maxHeight2);
		boatR.intersects(boatR2);

		g.setColor(Color.white);
		if (boatR.intersects(boatR2)) {
			g.setColor(Color.RED);
			display("boat collision");
		} else {
			display("");
		}

		if (p.intersects(boatR) || p.intersects(boatR2)) {
			g.setColor(Color.red);
			display("boat on land!");
		} else {
			display("");
		}
		g.drawRect(boatR.x, boatR.y, boatR.width, boatR.height);
		g.drawRect(boatR2.x, boatR2.y, boatR2.width, boatR2.height);

		g.drawPolygon(p);

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
		case 38:
			b2.angle -= Math.PI / 12;
			break;
		case 40:
			b2.angle += Math.PI / 12;
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
		display(m.getPoint().toString());
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