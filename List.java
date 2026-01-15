/** A linked list of character data objects.
 *  (Actually, a list of Node objects, each holding a reference to a character data object.
 *  However, users of this class are not aware of the Node objects. As far as they are concerned,
 *  the class represents a list of CharData objects. Likwise, the API of the class does not
 *  mention the existence of the Node objects). */
public class List {

    // Points to the first node in this list
    private Node first;

    // The number of elements in this list
    private int size;
	
    /** Constructs an empty list. */
    public List() {
        first = null;
        size = 0;
    }
    
    /** Returns the number of elements in this list. */
    public int getSize() {
 	      return size;
    }

    /** Returns the CharData of the first element in this list. */
    public CharData getFirst() {
        return first.cp;
    }

    /** GIVE Adds a CharData object with the given character to the beginning of this list. */
    public void addFirst(char chr) {
        Node head = new Node(new CharData(chr), first);
        first = head;
        size++;
    }
    
    /** GIVE Textual representation of this list. */
    public String toString() {
        if(size == 0) return "()";
        Node curr = first;
        String ans = "(";
        while(curr != null) {
            ans += curr + " ";
            curr = curr.next;
        }
        return ans.substring(0, ans.length() - 1) + ")";
    }

    /** Returns the index of the first CharData object in this list
      that has the same chr value as the given char,
      or -1 if there is no such object in this list. */
    public int indexOf(char chr) {
        int index = 0;
        Node curr = first;
        while(curr != null) {
            if(curr.cp.equals(chr))
                return index;
            curr = curr.next;
            index++;
        }
        return -1;
    }

    /** If the given character exists in one of the CharData objects in this list,
     *  increments its counter. Otherwise, adds a new CharData object with the
     *  given chr to the BEGINNING of this list. */
    public void update(char chr) {
        int index = this.indexOf(chr);
        if(index == -1) {
            this.addFirst(chr);
            size++;
        } else {
            CharData inc = this.get(index);
            inc.count++;
        }
    }

    /** GIVE If the given character exists in one of the CharData objects
     *  in this list, removes this CharData object from the list and returns
     *  true. Otherwise, returns false. */
    public boolean remove(char chr) {
        Node curr = first;
        Node prev = curr;
        while(curr != null) {
            if(curr.cp.equals(chr)) {
                if(curr.equals(first))
                    first = curr.next;
                else
                    prev.next = curr.next;
                size--;
                return true;
            }
            prev = curr;
            curr = curr.next;
        }
        return false;
    }

    /** Returns the CharData object at the specified index in this list. 
     *  If the index is negative or is greater than the size of this list, 
     *  throws an IndexOutOfBoundsException. */
    public CharData get(int index) {
        if(index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
        Node curr = first;
        int counter = 0;
        while(counter < index && curr != null) {
            curr = curr.next;
            counter++;
        }
        return curr.cp;
    }

    /** Returns an array of CharData objects, containing all the CharData objects in this list. */
    public CharData[] toArray() {
	    CharData[] arr = new CharData[size];
	    Node current = first;
	    int i = 0;
        while (current != null) {
    	    arr[i++]  = current.cp;
    	    current = current.next;
        }
        return arr;
    }

    /** Returns an iterator over the elements in this list, starting at the given index. */
    public ListIterator listIterator(int index) {
	    // If the list is empty, there is nothing to iterate   
	    if (size == 0) return null;
	    // Gets the element in position index of this list
	    Node current = first;
	    int i = 0;
        while (i < index) {
            current = current.next;
            i++;
        }
        // Returns an iterator that starts in that element
	    return new ListIterator(current);
    }
}