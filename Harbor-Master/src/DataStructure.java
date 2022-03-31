import java.util.ArrayList;

public class DataStructure {
	protected int size;

	/**
	 * length of the data
	 * 
	 * @return size
	 */
	public int size() {
		return size;
	}

	/**
	 * push an element to the data structure
	 * 
	 * @param elem
	 */
	public void push(Position elem) {
	}

	/**
	 * pop an element from the data structure
	 * 
	 * @return elem
	 */
	public Position pop() {
		return null;
	}

	/**
	 * peek at an element from the data structure
	 * 
	 * does not remove the element
	 * 
	 * @return elem
	 */
	public Position peek() {
		return null;
	}

	/**
	 * remove all elements from the data structure
	 */
	public void clear() {
	}

	/**
	 * returns a string representation of the data
	 * 
	 * return data as string
	 */
	public String toString() {
		return null;
	}
}

class Stack extends DataStructure {
	public ArrayList<Position> data;

	public Stack() {
		data = new ArrayList<Position>();
		size = 0;
	}

	// push - add element to top of list
	public void push(Position elem) {
		data.add(elem);
		size++;
	}

	// pop - remove and return element from top of list
	public Position pop() {
		if (size < 1) {
			return null;
		}

		size--;
		return data.remove(size);
	}

	public Position peek() {
		if (size < 1) {
			return null;
		}

		return data.get(size - 1);
	}

	public void clear() {
		data.clear();
	}

	public String toString() {
		if (size < 1) {
			return "";
		}

		String res = "[";
		for (int i = 0; i < size; i++) {
			res += data.get(i) + "";
			if (i != size - 1) {
				res += ", ";
			}
		}
		return res + "]";
	}

}

class Queue extends DataStructure {
	public Stack in, out;

	public Queue() {
		in = new Stack();
		out = new Stack();
		size = 0;
	}

	public void push(Position elem) {
		in.push(elem);
		size++;
	}

	public Position pop() {
		if (in.size() == 0 && out.size() == 0) {
			return null;
		}
		// when out stack is empty
		if (out.size() == 0) {
			// pop in stack into out stack, reverses order
			while (in.size() > 0) {
				out.push(in.pop());
			}
		}
		// increment size and pop element
		size--;
		return out.pop();
	}

	public Position peek() {
		if (in.size() == 0 && out.size() == 0) {
			return null;
		}
		// when out stack is empty
		if (out.size() == 0) {
			// pop in stack into out stack, reverses order
			while (in.size() > 0) {
				out.push(in.pop());
			}
		}
		Position res = out.pop();
		out.push(res);
		return res;
	}

	public void clear() {
		in.clear();
		out.clear();
	}

	public String toString() {
		if (size < 1) {
			return "";
		}

		String res = "[";
		for (int i = out.size() - 1; i >= 0; i++) {
			res += out.data.get(i) + ", ";
		}
		for (int i = 0; i < in.size(); i++) {
			res += in.data.get(i) + ", ";
		}
		return res + "]";
	}

}
