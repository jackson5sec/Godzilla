/*     */ package com.jgoodies.common.collect;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
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
/*     */ public final class ArrayListModel<E>
/*     */   extends ArrayList<E>
/*     */   implements ObservableList2<E>
/*     */ {
/*     */   private static final long serialVersionUID = -6165677201152015546L;
/*     */   private EventListenerList listenerList;
/*     */   
/*     */   public ArrayListModel() {
/*  63 */     this(10);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayListModel(int initialCapacity) {
/*  74 */     super(initialCapacity);
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
/*     */   public ArrayListModel(Collection<? extends E> c) {
/*  89 */     super(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void add(int index, E element) {
/*  97 */     super.add(index, element);
/*  98 */     fireIntervalAdded(index, index);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean add(E e) {
/* 104 */     int newIndex = size();
/* 105 */     super.add(e);
/* 106 */     fireIntervalAdded(newIndex, newIndex);
/* 107 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean addAll(int index, Collection<? extends E> c) {
/* 113 */     boolean changed = super.addAll(index, c);
/* 114 */     if (changed) {
/* 115 */       int lastIndex = index + c.size() - 1;
/* 116 */       fireIntervalAdded(index, lastIndex);
/*     */     } 
/* 118 */     return changed;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean addAll(Collection<? extends E> c) {
/* 124 */     int firstIndex = size();
/* 125 */     boolean changed = super.addAll(c);
/* 126 */     if (changed) {
/* 127 */       int lastIndex = firstIndex + c.size() - 1;
/* 128 */       fireIntervalAdded(firstIndex, lastIndex);
/*     */     } 
/* 130 */     return changed;
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
/* 161 */     boolean modified = false;
/* 162 */     Iterator<?> e = iterator();
/* 163 */     while (e.hasNext()) {
/* 164 */       if (c.contains(e.next())) {
/* 165 */         e.remove();
/* 166 */         modified = true;
/*     */       } 
/*     */     } 
/* 169 */     return modified;
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
/* 202 */     boolean modified = false;
/* 203 */     Iterator<E> e = iterator();
/* 204 */     while (e.hasNext()) {
/* 205 */       if (!c.contains(e.next())) {
/* 206 */         e.remove();
/* 207 */         modified = true;
/*     */       } 
/*     */     } 
/* 210 */     return modified;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void clear() {
/* 216 */     if (isEmpty()) {
/*     */       return;
/*     */     }
/* 219 */     int oldLastIndex = size() - 1;
/* 220 */     super.clear();
/* 221 */     fireIntervalRemoved(0, oldLastIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final E remove(int index) {
/* 227 */     E removedElement = super.remove(index);
/* 228 */     fireIntervalRemoved(index, index);
/* 229 */     return removedElement;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean remove(Object o) {
/* 235 */     int index = indexOf(o);
/* 236 */     boolean contained = (index != -1);
/* 237 */     if (contained) {
/* 238 */       remove(index);
/*     */     }
/* 240 */     return contained;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void removeRange(int fromIndex, int toIndex) {
/* 246 */     super.removeRange(fromIndex, toIndex);
/* 247 */     fireIntervalRemoved(fromIndex, toIndex - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final E set(int index, E element) {
/* 253 */     E previousElement = super.set(index, element);
/* 254 */     fireContentsChanged(index, index);
/* 255 */     return previousElement;
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
/* 275 */     getEventListenerList().add(ListDataListener.class, l);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void removeListDataListener(ListDataListener l) {
/* 281 */     getEventListenerList().remove(ListDataListener.class, l);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Object getElementAt(int index) {
/* 287 */     return get(index);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getSize() {
/* 293 */     return size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void fireContentsChanged(int index) {
/* 301 */     fireContentsChanged(index, index);
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
/* 312 */     Object[] listeners = getEventListenerList().getListenerList();
/* 313 */     ListDataEvent e = null;
/*     */     
/* 315 */     for (int i = listeners.length - 2; i >= 0; i -= 2) {
/* 316 */       if (listeners[i] == ListDataListener.class) {
/* 317 */         if (e == null) {
/* 318 */           e = new ListDataEvent(this, 0, index0, index1);
/*     */         }
/*     */         
/* 321 */         ((ListDataListener)listeners[i + 1]).contentsChanged(e);
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
/* 341 */     return getEventListenerList().<ListDataListener>getListeners(ListDataListener.class);
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
/* 356 */     Object[] listeners = getEventListenerList().getListenerList();
/* 357 */     ListDataEvent e = null;
/*     */     
/* 359 */     for (int i = listeners.length - 2; i >= 0; i -= 2) {
/* 360 */       if (listeners[i] == ListDataListener.class) {
/* 361 */         if (e == null) {
/* 362 */           e = new ListDataEvent(this, 1, index0, index1);
/*     */         }
/* 364 */         ((ListDataListener)listeners[i + 1]).intervalAdded(e);
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
/* 384 */     Object[] listeners = getEventListenerList().getListenerList();
/* 385 */     ListDataEvent e = null;
/*     */     
/* 387 */     for (int i = listeners.length - 2; i >= 0; i -= 2) {
/* 388 */       if (listeners[i] == ListDataListener.class) {
/* 389 */         if (e == null) {
/* 390 */           e = new ListDataEvent(this, 2, index0, index1);
/*     */         }
/* 392 */         ((ListDataListener)listeners[i + 1]).intervalRemoved(e);
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
/* 405 */     if (this.listenerList == null) {
/* 406 */       this.listenerList = new EventListenerList();
/*     */     }
/* 408 */     return this.listenerList;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\common\collect\ArrayListModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */