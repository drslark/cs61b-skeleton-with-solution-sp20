/**
 * Scheme-like pairs that can be used to form a list of integers.
 *
 * @author P. N. Hilfinger; updated by Vivant Sakore (1/29/2020)
 */
public class IntDList {

    /**
     * First and last nodes of list.
     */
    protected DNode _front, _back;

    /**
     * An empty list.
     */
    public IntDList() {
        _front = _back = null;
    }

    /**
     * @param values the ints to be placed in the IntDList.
     */
    public IntDList(Integer... values) {
        _front = _back = null;
        for (int val : values) {
            insertBack(val);
        }
    }

    /**
     * @return The first value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getFront() {
        return _front._val;
    }

    /**
     * @return The last value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getBack() {
        return _back._val;
    }

    /**
     * @return The number of elements in this list.
     */
    public int size() {
        int size = 0;
        DNode place = _front;
        while (place != null) {
            size += 1;
            place = place._next;
        }
        return size;
    }

    /**
     * @param i index of element to return,
     *          where i = 0 returns the first element,
     *          i = 1 returns the second element,
     *          i = -1 returns the last element,
     *          i = -2 returns the second to last element, and so on.
     *          You can assume i will always be a valid index, i.e 0 <= i < size for positive indices
     *          and -size <= i <= -1 for negative indices.
     * @return The integer value at index i
     */
    public int get(int i) {
        if (i < 0)
            i += size();
        DNode start = _front;
        for (int j = 0; j < i; j += 1) {
            start = start._next;
        }
        return start._val;
    }

    /**
     * @param d value to be inserted in the front
     */
    public void insertFront(int d) {
        if (_front == null && _back == null) {
            DNode first = new DNode(d);
            _front = first;
            _back = first;
        } else {
            DNode link = new DNode(null, d, _front);
            _front._prev = link;
            _front = link;
        }
    }

    /**
     * @param d value to be inserted in the back
     */
    public void insertBack(int d) {
        if (_front == null && _back == null) {
            DNode first = new DNode(d);
            _front = first;
            _back = first;
        } else {
            DNode link = new DNode(_back, d, null);
            _back._next = link;
            _back = link;
        }
    }

    /**
     * @param d     value to be inserted
     * @param index index at which the value should be inserted
     *              where index = 0 inserts at the front,
     *              index = 1 inserts at the second position,
     *              index = -1 inserts at the back,
     *              index = -2 inserts at the second to last position, and so on.
     *              You can assume index will always be a valid index,
     *              i.e 0 <= index <= size for positive indices (including insertions at front and back)
     *              and -(size+1) <= index <= -1 for negative indices (including insertions at front and back).
     */
    public void insertAtIndex(int d, int index) {
        if (index == 0 || index == -(size() + 1) )
            insertFront(d);
        else if (index == -1 || index == size())
            insertBack(d);
        else {
            DNode curr;
            if (index < 0) {
                curr = _back._prev;
                for (int j = -2; j > index; j -= 1) {
                    curr = curr._prev;
                }
            } else {
                curr = _front._next;
                for (int j = 1; j < index - 1; j += 1) {
                    curr = curr._next;
                }
            }
            DNode newNode = new DNode(curr, d, curr._next);
            curr._next._prev = newNode;
            curr._next = newNode;
        }
    }

    /**
     * Removes the first item in the IntDList and returns it.
     *
     * @return the item that was deleted
     */
    public int deleteFront() {
        int value = get(0);
        _front = _front._next;
        if (_front == null){
            _back = null;
        } else {
            _front._prev = null;
        }
        return value;
    }

    /**
     * Removes the last item in the IntDList and returns it.
     *
     * @return the item that was deleted
     */
    public int deleteBack() {
        int value = get(-1);
        _back = _back._prev;
        if (_back == null){
            _front = null;
        } else {
            _back._next = null;
        }
        return value;
    }

    /**
     * @param index index of element to be deleted,
     *          where index = 0 returns the first element,
     *          index = 1 will delete the second element,
     *          index = -1 will delete the last element,
     *          index = -2 will delete the second to last element, and so on.
     *          You can assume index will always be a valid index,
     *              i.e 0 <= index < size for positive indices (including deletions at front and back)
     *              and -size <= index <= -1 for negative indices (including deletions at front and back).
     * @return the item that was deleted
     */
    public int deleteAtIndex(int index) {
        if (index == 0 || index == -size())
            return deleteFront();
        else if (index == -1 || index == size() - 1)
            return deleteBack();
        else {
            DNode curr;
            if (index < 0) {
                curr = _back._prev;
                for (int j = -2; j > index; j -= 1) {
                    curr = curr._prev;
                }
            } else {
                curr = _front._next;
                for (int j = 1; j < index; j += 1) {
                    curr = curr._next;
                }
            }
            curr._prev._next = curr._next;
            curr._next._prev = curr._prev;
            curr._next = curr._prev = null;
            return curr._val;
        }

    }

    /**
     * @return a string representation of the IntDList in the form
     * [] (empty list) or [1, 2], etc.
     * Hint:
     * String a = "a";
     * a += "b";
     * System.out.println(a); //prints ab
     */
    public String toString() {
        DNode place = _front;
        String str = "[";
        for (; place != null; place = place._next) {
             str += place._val;
             if (place._next != null) {
                 str += ", ";
             }
        }
        str += "]";
        return str;
    }

    /**
     * DNode is a "static nested class", because we're only using it inside
     * IntDList, so there's no need to put it outside (and "pollute the
     * namespace" with it. This is also referred to as encapsulation.
     * Look it up for more information!
     */
    static class DNode {
        /** Previous DNode. */
        protected DNode _prev;
        /** Next DNode. */
        protected DNode _next;
        /** Value contained in DNode. */
        protected int _val;

        /**
         * @param val the int to be placed in DNode.
         */
        protected DNode(int val) {
            this(null, val, null);
        }

        /**
         * @param prev previous DNode.
         * @param val  value to be stored in DNode.
         * @param next next DNode.
         */
        protected DNode(DNode prev, int val, DNode next) {
            _prev = prev;
            _val = val;
            _next = next;
        }
    }

}
