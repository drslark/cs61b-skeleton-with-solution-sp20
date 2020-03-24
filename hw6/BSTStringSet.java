import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Implementation of a BST based String Set.
 * @author Amit Bhat
 */
public class BSTStringSet implements SortedStringSet, Iterable<String> {
    /** Creates a new empty set. */
    public BSTStringSet() {
        _root = null;
    }

    @Override
    public void put(String s) {
        _root = putHelper(_root, s);

    }

    private Node putHelper(Node node, String s) {
        if (node == null) {
            node = new Node(s);
        } else if (s.compareTo(node.s) < 0) {
            node.left = putHelper(node.left, s);
        } else if (s.compareTo(node.s) > 0) {
            node.right = putHelper(node.right, s);
        }
        return node;
    }

    @Override
    public boolean contains(String s) {
        return containsHelper(_root, s);
    }

    private static boolean containsHelper(Node node, String s) {
        if (node != null) {
            if (node.s.equals(s)) {
                return true;
            } else if (s.compareTo(node.s) < 0) {
                return containsHelper(node.left, s);
            } else if (s.compareTo(node.s) > 0) {
                return containsHelper(node.right, s);
            }
        }
        return false;
    }

    @Override
    public List<String> asList() {
        ArrayList<String> items = new ArrayList<String>();
        asListHelper(items, _root);
        return items;
    }

    public void asListHelper(List<String> items, Node node) {
        if (node != null) {
            asListHelper(items, node.left);
            items.add(node.s);
            asListHelper(items, node.right);
        }
    }


    /** Represents a single Node of the tree. */
    private static class Node {
        /** String stored in this Node. */
        private String s;
        /** Left child of this Node. */
        private Node left;
        /** Right child of this Node. */
        private Node right;

        /** Creates a Node containing SP. */
        Node(String sp) {
            s = sp;
        }
    }

    /** An iterator over BSTs. */
    private static class BSTIterator implements Iterator<String> {
        /** Stack of nodes to be delivered.  The values to be delivered
         *  are (a) the label of the top of the stack, then (b)
         *  the labels of the right child of the top of the stack inorder,
         *  then (c) the nodes in the rest of the stack (i.e., the result
         *  of recursively applying this rule to the result of popping
         *  the stack. */
        private Stack<Node> _toDo = new Stack<>();

        /** A new iterator over the labels in NODE. */
        BSTIterator(Node node) {
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            addTree(node.right);
            return node.s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /** Add the relevant subtrees of the tree rooted at NODE. */
        private void addTree(Node node) {
            while (node != null) {
                _toDo.push(node);
                node = node.left;
            }
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new BSTIterator(_root);
    }

    // FIXME: UNCOMMENT THE NEXT LINE FOR PART B
    // @Override
    public Iterator<String> iterator(String low, String high) {
        return new BSTRangeIterator(_root, low, high);
    }

    private static class BSTRangeIterator implements Iterator<String> {
        private Stack<Node> _toDo = new Stack<>();
        private String _low;
        private String _high;

        /** A new iterator over the labels in NODE. */
        BSTRangeIterator(Node node, String low, String high) {
            _low = low;
            _high = high;
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            return node.s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        private void addTree(Node node) {
            while (node != null) {
                addTree(node.right);
                if (_low.compareTo(node.s) <= 0 && _high.compareTo(node.s) >= 0) {
                    _toDo.push(node);
                }
                node = node.left;
            }
        }
    }

    /** Root node of the tree. */
    private Node _root;
}
