package com.simjava.collections;

import com.sun.tools.corba.se.idl.Generator;

import java.util.Iterator;

import com.simjava.core.Event;
import java.util.Iterable;

// public class EventQueue{
public class EventQueue extends Iterable<EventQueueNode> {

    int _numNodes;
    EventQueueNode[] _nodes;
    long _numNodesEverEnqueued;


// Instantiate a new Priority Queue
    public EventQueue(int maxNodes) {
        _numNodes = 0;
        _nodes = new EventQueueNode[maxNodes + 1];
        _numNodesEverEnqueued = 0;
    }

// Returns the number of nodes in the queue.  O(1)
    public int count() {
        return _numNodes;
    }

// Returns the maximum number of items that can be enqueued at once in this queue.  Once you hit this number (ie. once Count == MaxSize),
    public int maxSize() {
        return _nodes.length - 1;
    }

// Removes every node from the queue.  O(n) (So, don't do this often!)
    public void clear() {
        _nodes = new EventQueueNode[]{};
        _numNodes = 0;
    }

// Returns (in O(1)!) whether the given node is in the queue.  O(1)
    public boolean contains(EventQueueNode node) {
        return _nodes[node.queueIndex] == node;
    }

/// Enqueue a node - .Priority must be set beforehand!  O(log n)
    public EventQueueNode Enqueue(int primaryPriority, Event event, int secondaryPriority) {
        var node = new EventQueueNode(primaryPriority, secondaryPriority, event, _numNodesEverEnqueued+1, _numNodes+1);
        _nodes[_numNodes] = node;
        CascadeUp(_nodes[_numNodes]);
        return node;
    }

    private void Swap(EventQueueNode node1, EventQueueNode node2) {
        //Swap the nodes
        _nodes[node1.queueIndex] = node2;
        _nodes[node2.queueIndex] = node1;

        //Swap their indicies
        int temp = node1.queueIndex;
        node1.queueIndex = node2.queueIndex;
        node2.queueIndex = temp;
    }

//Performance appears to be slightly better when this is NOT inlined o_O
    private void CascadeUp(EventQueueNode node) {
        //aka Heapify-up
        int parent = node.queueIndex() / 2;
        while (parent >= 1) {
            EventQueueNode parentNode = _nodes[parent];
            if (HasHigherPriority(parentNode, node)) break;

            //Node has lower priority value, so move it up the heap
            Swap(node, parentNode); //For some reason, this is faster with Swap() rather than (less..?) individual operations, like in CascadeDown()

            parent = node.queueIndex() / 2;
        }
    }

    private void CascadeDown(EventQueueNode node) {
        //aka Heapify-down
        EventQueueNode newParent;
        int finalQueueIndex = node.queueIndex;
        while (true) {
            newParent = node;
            int childLeftIndex = 2 * finalQueueIndex;

            //Check if the left-child is higher-priority than the current node
            if (childLeftIndex > _numNodes) {
            //This could be placed outside the loop, but then we'd have to check newParent != node twice
                node.queueIndex = finalQueueIndex;
                _nodes[finalQueueIndex] = node;
                break;
            }

            EventQueueNode childLeft = _nodes[childLeftIndex];
            if (HasHigherPriority(childLeft, newParent)) {
                newParent = childLeft;
            }

        //Check if the right-child is higher-priority than either the current node or the left child
            int childRightIndex = childLeftIndex + 1;
            if (childRightIndex <= _numNodes) {
                EventQueueNode childRight = _nodes[childRightIndex];
                if (HasHigherPriority(childRight, newParent)) {
                    newParent = childRight;
                }
            }

        //If either of the children has higher (smaller) priority, swap and continue cascading
            if (newParent != node) {
            //Move new parent to its new index.  node will be moved once, at the end
            //Doing it this way is one less assignment operation than calling Swap()
                _nodes[finalQueueIndex] = newParent;

                int temp = newParent.queueIndex;
                newParent.queueIndex = finalQueueIndex;
                finalQueueIndex = temp;
            } else {
            //See note above
                node.queueIndex = finalQueueIndex;
                _nodes[finalQueueIndex] = node;
                break;
            }
        }
    }

    // Returns true if 'higher' has higher priority than 'lower', false otherwise.
    // Note that calling HasHigherPriority(node, node) (ie. both arguments the same node) will return false

    private boolean HasHigherPriority(EventQueueNode higher, EventQueueNode lower) {
        return (higher.primaryPriority < lower.primaryPriority ||
        (higher.primaryPriority == lower.primaryPriority
        && (higher.secondaryPriority < lower.secondaryPriority ||
        (higher.secondaryPriority == lower.secondaryPriority
        && higher.insertionIndex < lower.insertionIndex))));
    }

// Removes the head of the queue (node with highest priority; ties are broken by order of insertion), and returns it.  O(log n)
    public EventQueueNode Dequeue() {
        EventQueueNode returnMe = _nodes[1];
        Remove(returnMe);
        return returnMe;
    }

// Returns the head of the queue, without removing it (use Dequeue() for that).  O(1)
    public EventQueueNode First() {
        return _nodes[1];
    }

    // This method must be called on a node every time its priority changes while it is in the queue.
    // <b>Forgetting to call this method will result in a corrupted queue!</b>
    // O(log n)

    public void UpdatePriority(EventQueueNode node, int primaryPriority, int secondaryPriority) {
        node.primaryPriority = primaryPriority;
        node.secondaryPriority = secondaryPriority;
        OnNodeUpdated(node);
    }

    void OnNodeUpdated(EventQueueNode node) {
        //Bubble the updated node up or down as appropriate
        int parentIndex = node.queueIndex() / 2;
        EventQueueNode parentNode = _nodes[parentIndex];

        if (parentIndex > 0 && HasHigherPriority(node, parentNode)) {
            CascadeUp(node);
        } else {
            //Note that CascadeDown will be called if parentNode == node (that is, node is the root)
            CascadeDown(node);
        }
    }

// Removes a node from the queue.  Note that the node does not need to be the head of the queue.  O(log n)
    public void Remove(EventQueueNode node) {
        if (!Contains(node)) {
            return;
        }
        if (_numNodes <= 1) {
            _nodes[1] = null;
            _numNodes = 0;
            return;
        }

        //Make sure the node is the last node in the queue
        boolean wasSwapped = false;
        EventQueueNode formerLastNode = _nodes[_numNodes];
        if (node.queueIndex != _numNodes) {
            //Swap the node with the last node
            Swap(node, formerLastNode);
            wasSwapped = true;
        }

        _numNodes = _numNodes - 1;
        _nodes[node.queueIndex] = null;

        if (wasSwapped) {
        //Now bubble formerLastNode (which is no longer the last node) up or down as appropriate
            OnNodeUpdated(formerLastNode);
        }
    }


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

