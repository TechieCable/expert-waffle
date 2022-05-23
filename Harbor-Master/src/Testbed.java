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
	Boat b = new Boat(screenW / 2, screenH / 2, 4);
	int oW = 10;

	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, screenW, screenH);
		b.paint(g);
		b.dockInfo.docked = true;

		g.setColor(Color.CYAN);
		g.fillOval((int) (b.ax() - b.height / 2 * Math.cos(b.angle) - oW),
				(int) (b.ay() - b.height / 2 * Math.sin(b.angle) - oW), oW * 2, oW * 2);

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