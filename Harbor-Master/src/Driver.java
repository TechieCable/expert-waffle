import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Driver extends JPanel implements ActionListener, KeyListener, MouseListener {
	static final int screenW = 1080, screenH = 1920;

	Picture p = new Picture(100, 100, "boat1-0.png", 1);

	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, screenW, screenH);

		p.paint(g);
	}

	public static void main(String[] arg) {
		@SuppressWarnings("unused")
		Driver d = new Driver();
	}

	public Driver() {
		JFrame frame = new JFrame("Space Invaders");
		// TODO: add icon image
//		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(""));
		frame.setSize(screenW, screenH);
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addKeyListener(this);
		frame.addMouseListener(this);
		t.start();

		generate();

		frame.setVisible(true);
	}

	public void generate() {
	}

	Timer t = new Timer(16, this);

	public void actionPerformed(ActionEvent m) {
		repaint();
	}

	public void keyPressed(KeyEvent arg0) {

	}

	public void keyReleased(KeyEvent arg0) {

	}

	public void keyTyped(KeyEvent arg0) {

	}

	public void mouseClicked(MouseEvent arg0) {
//		System.out.println(arg0);
		switch (arg0.getButton()) {
		case 1:
			p.rotate(10);
			break;
		case 3:
			p.rotate(-10);
			break;
		default:
			break;
		}
	}

	public void mouseEntered(MouseEvent arg0) {

	}

	public void mouseExited(MouseEvent arg0) {

	}

	public void mousePressed(MouseEvent arg0) {

	}

	public void mouseReleased(MouseEvent arg0) {

	}

}