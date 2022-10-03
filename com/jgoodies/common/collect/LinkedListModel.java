/*     */ package com.jgoodies.common.collect;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.ListIterator;
/*     */ import javax.swing.event.EventListenerList;
/*     */ import javax.swing.event.ListDataEvent;
/*     */ import javax.swing.event.ListDataListener;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class LinkedListModel<E>
/*     */   extends LinkedList<E>
/*     */   implements ObservableList2<E>
/*     */ {
/*     */   private static final long serialVersionUID = 5753378113505707237L;
/*     */   private EventListenerList listenerList;
/*     */   
/*     */   public LinkedListModel() {}
/*     */   
/*     */   public LinkedListModel(Collection<? extends E> c) {
/*  78 */     super(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void add(int index, E element) {
/*  86 */     super.add(index, element);
/*  87 */     fireIntervalAdded(index, index);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean add(E e) {
/*  93 */     int newIndex = size();
/*  94 */     super.add(e);
/*  95 */     fireIntervalAdded(newIndex, newIndex);
/*  96 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean addAll(int index, Collection<? extends E> c) {
/* 102 */     boolean changed = super.addAll(index, c);
/* 103 */     if (changed) {
/* 104 */       int lastIndex = index + c.size() - 1;
/* 105 */       fireIntervalAdded(index, lastIndex);
/*     */     } 
/* 107 */     return changed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean removeAll(Collection<?> c) {
/* 138 */     boolean modified = false;
/* 139 */     Iterator<?> e = iterator();
/* 140 */     while (e.hasNext()) {
/* 141 */       if (c.contains(e.next())) {
/* 142 */         e.remove();
/* 143 */         modified = true;
/*     */       } 
/*     */     } 
/* 146 */     return modified;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean retainAll(Collection<?> c) {
/* 179 */     boolean modified = false;
/* 180 */     Iterator<E> e = iterator();
/* 181 */     while (e.hasNext()) {
/* 182 */       if (!c.contains(e.next())) {
/* 183 */         e.remove();
/* 184 */         modified = true;
/*     */       } 
/*     */     } 
/* 187 */     return modified;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void addFirst(E e) {
/* 193 */     super.addFirst(e);
/* 194 */     fireIntervalAdded(0, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void addLast(E e) {
/* 200 */     int newIndex = size();
/* 201 */     super.addLast(e);
/* 202 */     fireIntervalAdded(newIndex, newIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void clear() {
/* 208 */     if (isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 212 */     int oldLastIndex = size() - 1;
/* 213 */     super.clear();
/* 214 */     fireIntervalRemoved(0, oldLastIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final E remove(int index) {
/* 220 */     E removedElement = super.remove(index);
/* 221 */     fireIntervalRemoved(index, index);
/* 222 */     return removedElement;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean remove(Object o) {
/* 228 */     int index = indexOf(o);
/* 229 */     if (index == -1) {
/* 230 */       return false;
/*     */     }
/* 232 */     remove(index);
/* 233 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final E removeFirst() {
/* 239 */     E first = super.removeFirst();
/* 240 */     fireIntervalRemoved(0, 0);
/* 241 */     return first;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final E removeLast() {
/* 247 */     int lastIndex = size() - 1;
/* 248 */     E last = super.removeLast();
/* 249 */     fireIntervalRemoved(lastIndex, lastIndex);
/* 250 */     return last;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void removeRange(int fromIndex, int toIndex) {
/* 256 */     super.removeRange(fromIndex, toIndex);
/* 257 */     fireIntervalRemoved(fromIndex, toIndex - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final E set(int index, E element) {
/* 263 */     E previousElement = super.set(index, element);
/* 264 */     fireContentsChanged(index, index);
/* 265 */     return previousElement;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final ListIterator<E> listIterator(int index) {
/* 271 */     return new ReportingListIterator(super.listIterator(index));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void addListDataListener(ListDataListener l) {
/* 291 */     getEventListenerList().add(ListDataListener.class, l);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void removeListDataListener(ListDataListener l) {
/* 297 */     getEventListenerList().remove(ListDataListener.class, l);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final E getElementAt(int index) {
/* 303 */     return get(index);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getSize() {
/* 309 */     return size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void fireContentsChanged(int index) {
/* 317 */     fireContentsChanged(index, index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void fireContentsChanged(int index0, int index1) {
/* 328 */     Object[] listeners = getEventListenerList().getListenerList();
/* 329 */     ListDataEvent e = null;
/*     */     
/* 331 */     for (int i = listeners.length - 2; i >= 0; i -= 2) {
/* 332 */       if (listeners[i] == ListDataListener.class) {
/* 333 */         if (e == null) {
/* 334 */           e = new ListDataEvent(this, 0, index0, index1);
/*     */         }
/*     */         
/* 337 */         ((ListDataListener)listeners[i + 1]).contentsChanged(e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ListDataListener[] getListDataListeners() {
/* 357 */     return getEventListenerList().<ListDataListener>getListeners(ListDataListener.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void fireIntervalAdded(int index0, int index1) {
/* 372 */     Object[] listeners = getEventListenerList().getListenerList();
/* 373 */     ListDataEvent e = null;
/*     */     
/* 375 */     for (int i = listeners.length - 2; i >= 0; i -= 2) {
/* 376 */       if (listeners[i] == ListDataListener.class) {
/* 377 */         if (e == null) {
/* 378 */           e = new ListDataEvent(this, 1, index0, index1);
/*     */         }
/* 380 */         ((ListDataListener)listeners[i + 1]).intervalAdded(e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void fireIntervalRemoved(int index0, int index1) {
/* 400 */     Object[] listeners = getEventListenerList().getListenerList();
/* 401 */     ListDataEvent e = null;
/*     */     
/* 403 */     for (int i = listeners.length - 2; i >= 0; i -= 2) {
/* 404 */       if (listeners[i] == ListDataListener.class) {
/* 405 */         if (e == null) {
/* 406 */           e = new ListDataEvent(this, 2, index0, index1);
/*     */         }
/* 408 */         ((ListDataListener)listeners[i + 1]).intervalRemoved(e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private EventListenerList getEventListenerList() {
/* 421 */     if (this.listenerList == null) {
/* 422 */       this.listenerList = new EventListenerList();
/*     */     }
/* 424 */     return this.listenerList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final class ReportingListIterator
/*     */     implements ListIterator<E>
/*     */   {
/*     */     private final ListIterator<E> delegate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private int lastReturnedIndex;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     ReportingListIterator(ListIterator<E> delegate) {
/* 448 */       this.delegate = delegate;
/* 449 */       this.lastReturnedIndex = -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 454 */       return this.delegate.hasNext();
/*     */     }
/*     */ 
/*     */     
/*     */     public E next() {
/* 459 */       this.lastReturnedIndex = nextIndex();
/* 460 */       return this.delegate.next();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasPrevious() {
/* 465 */       return this.delegate.hasPrevious();
/*     */     }
/*     */ 
/*     */     
/*     */     public E previous() {
/* 470 */       this.lastReturnedIndex = previousIndex();
/* 471 */       return this.delegate.previous();
/*     */     }
/*     */ 
/*     */     
/*     */     public int nextIndex() {
/* 476 */       return this.delegate.nextIndex();
/*     */     }
/*     */ 
/*     */     
/*     */     public int previousIndex() {
/* 481 */       return this.delegate.previousIndex();
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 486 */       int oldSize = LinkedListModel.this.size();
/* 487 */       this.delegate.remove();
/* 488 */       int newSize = LinkedListModel.this.size();
/* 489 */       if (newSize < oldSize) {
/* 490 */         LinkedListModel.this.fireIntervalRemoved(this.lastReturnedIndex, this.lastReturnedIndex);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void set(E e) {
/* 496 */       this.delegate.set(e);
/* 497 */       LinkedListModel.this.fireContentsChanged(this.lastReturnedIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public void add(E e) {
/* 502 */       this.delegate.add(e);
/* 503 */       int newIndex = previousIndex();
/* 504 */       LinkedListModel.this.fireIntervalAdded(newIndex, newIndex);
/* 505 */       this.lastReturnedIndex = -1;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\common\collect\LinkedListModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */