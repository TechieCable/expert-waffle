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

	/**
	 * set the starting point of the drag
	 * 
	 * @param MouseEvent
	 */
	public void setStart(MouseEvent m) {
		start = m.getPoint();
		curr = null;
		dragging = true;
	}

	/**
	 * set the latest drag point, returns if the distance between points is greater
	 * than value
	 * 
	 * @param m
	 * @return did reset
	 */
	public boolean setCurr(MouseEvent m) {
		curr = m.getPoint();
		if (distance() > 10) {
			start = curr;
			return true;
		}
		return false;
	}

	/**
	 * end the drag session
	 */
	public void end() {
		start = new Point(-1, -1);
		curr = null;
		dragging = false;
	}

	/**
	 * returns the distance between start and current points
	 * 
	 * @return distance
	 */
	public int distance() {
		if (dragging == false) {
			return -1;
		}
		return (int) Math.sqrt(Math.pow(start.x - curr.x, 2) + Math.pow(start.y - curr.y, 2));
	}
}
