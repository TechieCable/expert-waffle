import java.awt.Color;

/**
 * @author TechieCable
 * @version 2.0
 *
 */
public class Message {
	private String message;
	private int time;
	private Color c;

	/**
	 * takes a message, an erase time, and a color
	 * 
	 * @param message
	 * @param time
	 * @param c
	 */
	public Message(String message, int time, Color c) {
		this.message = message;
		this.time = time;
		this.c = c;
	}

	/**
	 * takes a message and an erase time and sets the color to white
	 * 
	 * @param message
	 * @param time
	 */
	public Message(String message, int time) {
		this(message, time, Color.WHITE);
	}

	/**
	 * takes a message and a color and sets the time to 100
	 * 
	 * @param message
	 * @param c
	 */
	public Message(String message, Color c) {
		this(message, 100, c);
	}

	/**
	 * takes a message and sets the time to 100
	 * 
	 * color is set by the default color constructor
	 * 
	 * @param message
	 */
	public Message(String message) {
		this(message, 100);
	}

	/**
	 * decrease erase time
	 */
	public void incTime() {
		time--;
	}

	/**
	 * @return erase time
	 */
	public int time() {
		return time;
	}

	/**
	 * @return message
	 */
	public String message() {
		return message;
	}

	/**
	 * @return color
	 */
	public Color color() {
		return c;
	}

}