import java.util.HashMap;
import java.util.Map;

public class LRUCache<K, V> {
    private class Node {
        K key;
        V value;
        Node next;
        Node prev;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            next = null;
            prev = null;
        }
    }

    private class LinkedList {
        private Node head;
        private Node tail;

        public LinkedList() {
            head = null;
            tail = null;
        }

        public boolean isEmpty() {
            return head == null;
        }

        public Node addFirst(K key, V value) {
            Node node = new Node(key, value);
            if (isEmpty()) {
                assert head == null && tail == null : "List should be empty";
                head = node;
                tail = node;
            } else {
                assert head != null : "List must not be empty";
                node.next = head;
                head.prev = node;
                head = node;
            }
            assert head == node : "new node must be first";
            return node;
        }

        public Node eraseLast() {
            if (isEmpty()) {
                return null;
            }
            Node node = tail;
            if (tail.prev == null) {
                head = null;
                tail = null;
                assert isEmpty() : "List should be empty";
            } else {
                tail = tail.prev;
                tail.next = null;
            }
            return node;
        }

        public Node getFirst() {
            return head;
        }

        public void moveToFirst(Node node) {
            assert node != null : "Node must not be null";
            assert !isEmpty() : "Cannot apply this to empty list";
            if (head == node) {
                return;
            }
            node.prev.next = node.next;
            if (tail == node) {
                tail = tail.prev;
            } else {
                node.next.prev = node.prev;
            }
            node.next = head;
            node.prev = null;
            head.prev = node;
            head = node;
            assert head == node : "Selected node should be first";
        }
    }

    private int size;
    private final int capacity;
    private final LinkedList elementsList;
    private final Map<K, Node> elementsMap;

    public LRUCache(int capacity) {
        assert capacity > 0 : "Capacity should be positive";
        this.capacity = capacity;
        size = 0;
        elementsList = new LinkedList();
        elementsMap = new HashMap<>();
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public V get(K key) {
        if (!elementsMap.containsKey(key)) {
            return null;
        }
        Node node = elementsMap.get(key);
        elementsList.moveToFirst(node);
        assert elementsList.getFirst() == node : "Requested element should be first";
        return node.value;
    }

    public K getMostRecentKey() {
        return elementsList.getFirst().key;
    }

    public V getMostRecentValue() {
        return elementsList.getFirst().value;
    }

    private void dropOldestElement() {
        K oldestKey = elementsList.eraseLast().key;
        elementsMap.remove(oldestKey);
        size -= 1;
    }

    private void addElement(K key, V value) {
        elementsMap.put(key, elementsList.addFirst(key, value));
        size += 1;
    }

    public V put(K key, V value) {
        assert key != null : "Key must not be null";
        if (elementsMap.containsKey(key)) {
            Node node = elementsMap.get(key);
            V oldValue = node.value;
            node.value = value;
            elementsList.moveToFirst(node);
            assert elementsList.getFirst() == node : "Changed element should be first";
            return oldValue;
        }
        if (size == capacity) {
            dropOldestElement();
        }
        addElement(key, value);
        assert size <= capacity : "Overflow";
        assert elementsList.getFirst().key == key
                && elementsList.getFirst().value == value : "New element should be first";
        return null;
    }
}
