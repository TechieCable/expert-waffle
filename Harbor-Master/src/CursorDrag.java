import java.awt.Point;
import java.awt.event.MouseEvent;

public class CursorDrag {
	Point start;
	Point curr;
	boolean dragging;
	int activeBoatID;

	public CursorDrag() {
		start = new Point(-1, -1);
		curr = null;
		dragging = false;
	}

	public void setStart(MouseEvent m) {

		start = m.getPoint();
		curr = null;
		dragging = true;
	}

	public boolean setCurr(MouseEvent m) {
		curr = m.getPoint();
		if (distance() > 20) {
			start = curr;
			return true;
		}
		return false;
	}

	public void end() {
		start = new Point(-1, -1);
		curr = null;
		dragging = false;
	}

//	public Position midPoint(Position p) {
//		
//	}

	public int distance() {
		if (dragging == false) {
			System.err.println("attempted to find distance while not dragging");
			return 0;
		}
		return (int) Math.sqrt(Math.pow(start.x - curr.x, 2) + Math.pow(start.y - curr.y, 2));
	}
}
