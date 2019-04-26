package com.simjava.collections;


import com.simjava.yield.ClosableIterator;
import com.simjava.yield.Yielderable;
import com.simjava.core.Event;

public class EventQueue{
//public class EventQueue extends Yielderable<EventQueueNode> {

    private int _numNodes;
    private EventQueueNode[] _nodes;
    private long _numNodesEverEnqueued;

    public int get_numNodes() {
        return _numNodes;
    }

    public void set_numNodes(int _numNodes) {
        this._numNodes = _numNodes;
    }

    public EventQueueNode[] get_nodes() {
        return _nodes;
    }

    public void set_nodes(EventQueueNode[] _nodes) {
        this._nodes = _nodes;
    }

    public long get_numNodesEverEnqueued() {
        return _numNodesEverEnqueued;
    }

    public void set_numNodesEverEnqueued(long _numNodesEverEnqueued) {
        this._numNodesEverEnqueued = _numNodesEverEnqueued;
    }

    /// <summary>
/// Instantiate a new Priority Queue
/// </summary>
/// <param name="maxNodes">EventQueueNodehe max nodes ever allowed to be enqueued (going over this will cause an exception)</param>
    public EventQueue(int maxNodes) {
        _numNodes = 0;
        _nodes = new EventQueueNode[maxNodes + 1];
        _numNodesEverEnqueued = 0;
        }

/// <summary>
/// Returns the number of nodes in the queue.  O(1)
/// </summary>
    public int Count() {
        return get_numNodes();
    }

/// <summary>
/// Returns the maximum number of items that can be enqueued at once in this queue.  Once you hit this number (ie. once Count == MaxSize),
/// attempting to enqueue another item will throw an exception.  O(1)
/// </summary>
    public int MaxSize() {
        return get_nodes().length - 1;
    }

/// <summary>
/// Removes every node from the queue.  O(n) (So, don't do this often!)
/// </summary>
    public void Clear() {
        set_nodes(new EventQueueNode[]{});
        _numNodes = 0;
    }

/// <summary>
/// Returns (in O(1)!) whether the given node is in the queue.  O(1)
/// </summary>
    public boolean Contains(EventQueueNode node) {
        return (get_nodes()[node.getQueueIndex()] == node);
    }

/// <summary>
/// Enqueue a node - .Priority must be set beforehand!  O(log n)
/// </summary>
    public EventQueueNode Enqueue(int primaryPriority, Event event, int secondaryPriority) {
        var node = new EventQueueNode(primaryPriority, secondaryPriority, event, get_numNodesEverEnqueued()+1, get_numNodes()+1);
        get_nodes()[get_numNodes()] = node;
        CascadeUp(get_nodes()[get_numNodes()]);
        return node;
        }

    private void Swap(EventQueueNode node1, EventQueueNode node2) {
        //Swap the nodes
        get_nodes()[node1.getQueueIndex()] = node2;
        get_nodes()[node2.getQueueIndex()] = node1;

        //Swap their indicies
        int temp = node1.getQueueIndex();
        node1.setQueueIndex(node2.getQueueIndex());
        node2.setQueueIndex(temp);
        }

//Performance appears to be slightly better when this is NOT inlined o_O
    private void CascadeUp(EventQueueNode node) {
        //aka Heapify-up
        int parent = node.getQueueIndex() / 2;
        while (parent >= 1) {
        EventQueueNode parentNode = get_nodes()[parent];
        if (HasHigherPriority(parentNode, node))
        break;

        //Node has lower priority value, so move it up the heap
        Swap(node, parentNode); //For some reason, this is faster with Swap() rather than (less..?) individual operations, like in CascadeDown()

        parent = node.getQueueIndex() / 2;
        }
        }


    private void CascadeDown(EventQueueNode node) {
        //aka Heapify-down
        EventQueueNode newParent;
        int finalQueueIndex = node.getQueueIndex();
        while (true) {
        newParent = node;
        int childLeftIndex = 2 * finalQueueIndex;

        //Check if the left-child is higher-priority than the current node
        if (childLeftIndex > _numNodes) {
        //This could be placed outside the loop, but then we'd have to check newParent != node twice
        node.setQueueIndex(finalQueueIndex);
        get_nodes()[finalQueueIndex] = node;
        break;
        }

        EventQueueNode childLeft = get_nodes()[childLeftIndex];
        if (HasHigherPriority(childLeft, newParent)) {
        newParent = childLeft;
        }

        //Check if the right-child is higher-priority than either the current node or the left child
        int childRightIndex = childLeftIndex + 1;
        if (childRightIndex <= get_numNodes()) {
            EventQueueNode childRight = get_nodes()[childRightIndex];
            if (HasHigherPriority(childRight, newParent)) {
                newParent = childRight;
            }
        }

        //If either of the children has higher (smaller) priority, swap and continue cascading
        if (newParent != node) {
        //Move new parent to its new index.  node will be moved once, at the end
        //Doing it this way is one less assignment operation than calling Swap()
            get_nodes()[finalQueueIndex] = newParent;

            int temp = newParent.getQueueIndex();
            newParent.setQueueIndex(finalQueueIndex);
            finalQueueIndex = temp;
        } else {
        //See note above
            node.setQueueIndex(finalQueueIndex);
            get_nodes()[finalQueueIndex] = node;
            break;
        }
        }
        }

        /// <summary>
        /// Returns true if 'higher' has higher priority than 'lower', false otherwise.
        /// Note that calling HasHigherPriority(node, node) (ie. both arguments the same node) will return false
        /// </summary>

    private boolean HasHigherPriority(EventQueueNode higher, EventQueueNode lower) {
        return (higher.getPrimaryPriority() < lower.getPrimaryPriority() ||
        (higher.getPrimaryPriority() == lower.getPrimaryPriority()
        && (higher.getSecondaryPriority() < lower.getSecondaryPriority() ||
        (higher.getSecondaryPriority() == lower.getSecondaryPriority()
        && higher.getInsertionIndex() < lower.getInsertionIndex()))));
    }

/// <summary>
/// Removes the head of the queue (node with highest priority; ties are broken by order of insertion), and returns it.  O(log n)
/// </summary>
    public EventQueueNode Dequeue() {
        EventQueueNode returnMe = get_nodes()[1];
        Remove(returnMe);
        return returnMe;
    }

/// <summary>
/// Returns the head of the queue, without removing it (use Dequeue() for that).  O(1)
/// </summary>
    public EventQueueNode First() {
        return get_nodes()[1];
    }

        /// <summary>
        /// This method must be called on a node every time its priority changes while it is in the queue.
        /// <b>Forgetting to call this method will result in a corrupted queue!</b>
        /// O(log n)
        /// </summary>

public void UpdatePriority(EventQueueNode node, int primaryPriority, int secondaryPriority) {
        node.setPrimaryPriority(primaryPriority);
        node.setSecondaryPriority(secondaryPriority);
        OnNodeUpdated(node);
        }

        void OnNodeUpdated(EventQueueNode node) {
        //Bubble the updated node up or down as appropriate
            int parentIndex = node.getQueueIndex() / 2;
            EventQueueNode parentNode = get_nodes()[parentIndex];

            if (parentIndex > 0 && HasHigherPriority(node, parentNode)) {
                CascadeUp(node);
            } else {
                //Note that CascadeDown will be called if parentNode == node (that is, node is the root)
                CascadeDown(node);
            }
        }

/// <summary>
/// Removes a node from the queue.  Note that the node does not need to be the head of the queue.  O(log n)
/// </summary>
    public void Remove(EventQueueNode node) {
        if (!Contains(node)) {
        return;
        }
        if (get_numNodes() <= 1) {
        get_nodes()[1] = null;
        set_numNodes(0);
        return;
        }

        //Make sure the node is the last node in the queue
        boolean wasSwapped = false;
        EventQueueNode formerLastNode = get_nodes()[get_numNodes()];
        if (node.getQueueIndex() != get_numNodes()) {
            //Swap the node with the last node
            Swap(node, formerLastNode);
            wasSwapped = true;
        }

        set_numNodes(get_numNodes()-1);
        get_nodes()[node.getQueueIndex()] = null;

        if (wasSwapped) {
        //Now bubble formerLastNode (which is no longer the last node) up or down as appropriate
            OnNodeUpdated(formerLastNode);
        }
    }

//    @Override
//    public ClosableIterator<EventQueueNode> iterator(){
//        for (int i = 1; i <= get_numNodes(); i++){
//            return get_nodes()[i];
//            }
//    }
//
//    public IEnumerator<EventQueueNode> GetEnumerator() {
//        for (int i = 1; i <= _numNodes; i++)
//        yield return _nodes[i];
//        }
//
//        IEnumerator IEnumerable.GetEnumerator() {
//        return GetEnumerator();
//        }

/// <summary>
/// <b>Should not be called in production code.</b>
/// Checks to make sure the queue is still in a valid state.  Used for testing/debugging the queue.
/// </summary>
    public boolean IsValidQueue() {
        for (int i = 1; i < _nodes.length; i++) {
        if (_nodes[i] != null) {
            int childLeftIndex = 2 * i;
            if (childLeftIndex < _nodes.length && _nodes[childLeftIndex] != null && HasHigherPriority(_nodes[childLeftIndex], _nodes[i]))
                return false;

            int childRightIndex = childLeftIndex + 1;
            if (childRightIndex < _nodes.length && _nodes[childRightIndex] != null && HasHigherPriority(_nodes[childRightIndex], _nodes[i]))
                return false;
        }
        }
        return true;
    }
}

