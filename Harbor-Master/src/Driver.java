import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Driver extends JPanel implements ActionListener, KeyListener, MouseListener, MouseMotionListener {
	public static int screenW = 1920, screenH = 1080;

	Boat b = new Boat(500, 500, "boat4-0.png");
	ArrayList<Boat> boats = new ArrayList<Boat>();
	CursorDrag d = new CursorDrag();
	Map m = new Map("map1.png");

	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, screenW, screenH);
		
		m.paint(g);

		b.paint(g);

	}

	public static void main(String[] arg) {
		@SuppressWarnings("unused")
		Driver d = new Driver();
	}

	public Driver() {
		JFrame frame = new JFrame("Harbor Master");
		// TODO: add icon image
//		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(""));
		frame.setSize(screenW, screenH);
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addKeyListener(this);
		frame.addMouseListener(this);
		frame.addMouseMotionListener(this);
		t.start();

		generate();

//		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//		frame.setUndecorated(true);

		frame.setVisible(true);
	}

	public void generate() {
	}

	Timer t = new Timer(16, this);

	public void actionPerformed(ActionEvent m) {
		repaint();
	}

	public void keyPressed(KeyEvent m) {

	}

	public void keyReleased(KeyEvent m) {

	}

	public void keyTyped(KeyEvent m) {

	}

	public void mouseClicked(MouseEvent m) {
//		b.addMove(new Position(m.getX(), m.getY()));
	}

	public void mouseEntered(MouseEvent m) {

	}

	public void mouseExited(MouseEvent m) {

	}

	public void mousePressed(MouseEvent m) {
		b.clearMoves();
		d.setStart(m);
		b.addMove(new Position(d.start));
	}

	public void mouseReleased(MouseEvent m) {
		d.end();
	}

	public void mouseDragged(MouseEvent m) {
		if (d.setCurr(m)) {
			b.addMove(new Position(d.start));
		}

	}

	public void mouseMoved(MouseEvent m) {

	}

}