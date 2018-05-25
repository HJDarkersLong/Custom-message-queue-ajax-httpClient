package com.xian.common.util;

import java.util.Arrays;

/**
 * 环形队列
 *  
 */
public class CircularQueue<E> {
	
    private static final int DEFAULT_CAPACITY = 4;

    private static final int DEFAULT_MASK = DEFAULT_CAPACITY - 1;

    private Object[] items;

    private int mask;

    private int first = 0;

    private int last = 0;

    private int size = 0;

    public CircularQueue() {
        items = new Object[DEFAULT_CAPACITY];
        mask = DEFAULT_MASK;
    }
    
    public int capacity() {
        return items.length;
    }

    public void clear()  {
        Arrays.fill(items, null);
        first = 0;
        last = 0;
        size = 0;
    }
    
	@SuppressWarnings("unchecked")
	public E pop() {
        if (size == 0) {
            return null;
        }

        Object ret = items[first];
        items[first] = null;
        decreaseSize();

        return (E) ret;
    }
	
	public E peek() {
        return first();
    }
	
	@SuppressWarnings("unchecked")
	public E popLast() {
        if (size == 0) {
            return null;
        }

        Object ret = items[last];
        items[last] = null;
        size--;

        return (E) ret;
    }
	
	public E peekLast() {
        return last();
    }

    public void push(E item) {
        if (item == null) {
            throw new NullPointerException("item");
        }
        
        ensureCapacity();
        items[last] = item;
        increaseSize();
    }

    @SuppressWarnings("unchecked")
    E first() {
        if (size == 0) {
            return null;
        }

        return (E) items[first];
    }

    @SuppressWarnings("unchecked")
    E last() {
        if (size == 0) {
            return null;
        }

        return (E)items[(last + items.length - 1) & mask];
    }
    
    public boolean isEmpty() {
        return (size == 0);
    }

    public int size() {
        return size;
    }
    
    private void increaseSize() {
        last = (last + 1) & mask;
        size++;
    }

    private void decreaseSize() {
        first = (first + 1) & mask;
        size--;
    }

    private void ensureCapacity() {
        if (size < items.length) {
            return;
        }
        
        final int oldLen = items.length;
        Object[] tmp = new Object[oldLen << 1];

        if (first < last) {
            System.arraycopy(items, first, tmp, 0, last - first);
        }
        else {
            System.arraycopy(items, first, tmp, 0, oldLen - first);
            System.arraycopy(items, 0, tmp, oldLen - first, last);
        }

        first = 0;
        last = oldLen;
        items = tmp;
        mask = tmp.length - 1;
    }

    @Override
    public String toString() {
    	return String.format("first=%s, last=%s, size=%s, mask=%s", first, last, size, mask);
    }
}