/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractSequentialList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public class LinkedListMultimap<K, V>
/*     */   extends AbstractMultimap<K, V>
/*     */   implements ListMultimap<K, V>, Serializable
/*     */ {
/*     */   private transient Node<K, V> head;
/*     */   private transient Node<K, V> tail;
/*     */   private transient Map<K, KeyList<K, V>> keyToKeyList;
/*     */   private transient int size;
/*     */   private transient int modCount;
/*     */   @GwtIncompatible
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   private static final class Node<K, V>
/*     */     extends AbstractMapEntry<K, V>
/*     */   {
/*     */     final K key;
/*     */     V value;
/*     */     Node<K, V> next;
/*     */     Node<K, V> previous;
/*     */     Node<K, V> nextSibling;
/*     */     Node<K, V> previousSibling;
/*     */     
/*     */     Node(K key, V value) {
/* 117 */       this.key = key;
/* 118 */       this.value = value;
/*     */     }
/*     */ 
/*     */     
/*     */     public K getKey() {
/* 123 */       return this.key;
/*     */     }
/*     */ 
/*     */     
/*     */     public V getValue() {
/* 128 */       return this.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public V setValue(V newValue) {
/* 133 */       V result = this.value;
/* 134 */       this.value = newValue;
/* 135 */       return result;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class KeyList<K, V> {
/*     */     LinkedListMultimap.Node<K, V> head;
/*     */     LinkedListMultimap.Node<K, V> tail;
/*     */     int count;
/*     */     
/*     */     KeyList(LinkedListMultimap.Node<K, V> firstNode) {
/* 145 */       this.head = firstNode;
/* 146 */       this.tail = firstNode;
/* 147 */       firstNode.previousSibling = null;
/* 148 */       firstNode.nextSibling = null;
/* 149 */       this.count = 1;
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
/*     */   public static <K, V> LinkedListMultimap<K, V> create() {
/* 167 */     return new LinkedListMultimap<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> LinkedListMultimap<K, V> create(int expectedKeys) {
/* 178 */     return new LinkedListMultimap<>(expectedKeys);
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
/*     */   public static <K, V> LinkedListMultimap<K, V> create(Multimap<? extends K, ? extends V> multimap) {
/* 190 */     return new LinkedListMultimap<>(multimap);
/*     */   }
/*     */   
/*     */   LinkedListMultimap() {
/* 194 */     this(12);
/*     */   }
/*     */   
/*     */   private LinkedListMultimap(int expectedKeys) {
/* 198 */     this.keyToKeyList = Platform.newHashMapWithExpectedSize(expectedKeys);
/*     */   }
/*     */   
/*     */   private LinkedListMultimap(Multimap<? extends K, ? extends V> multimap) {
/* 202 */     this(multimap.keySet().size());
/* 203 */     putAll(multimap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   private Node<K, V> addNode(K key, V value, Node<K, V> nextSibling) {
/* 213 */     Node<K, V> node = new Node<>(key, value);
/* 214 */     if (this.head == null) {
/* 215 */       this.head = this.tail = node;
/* 216 */       this.keyToKeyList.put(key, new KeyList<>(node));
/* 217 */       this.modCount++;
/* 218 */     } else if (nextSibling == null) {
/* 219 */       this.tail.next = node;
/* 220 */       node.previous = this.tail;
/* 221 */       this.tail = node;
/* 222 */       KeyList<K, V> keyList = this.keyToKeyList.get(key);
/* 223 */       if (keyList == null) {
/* 224 */         this.keyToKeyList.put(key, keyList = new KeyList<>(node));
/* 225 */         this.modCount++;
/*     */       } else {
/* 227 */         keyList.count++;
/* 228 */         Node<K, V> keyTail = keyList.tail;
/* 229 */         keyTail.nextSibling = node;
/* 230 */         node.previousSibling = keyTail;
/* 231 */         keyList.tail = node;
/*     */       } 
/*     */     } else {
/* 234 */       KeyList<K, V> keyList = this.keyToKeyList.get(key);
/* 235 */       keyList.count++;
/* 236 */       node.previous = nextSibling.previous;
/* 237 */       node.previousSibling = nextSibling.previousSibling;
/* 238 */       node.next = nextSibling;
/* 239 */       node.nextSibling = nextSibling;
/* 240 */       if (nextSibling.previousSibling == null) {
/* 241 */         ((KeyList)this.keyToKeyList.get(key)).head = node;
/*     */       } else {
/* 243 */         nextSibling.previousSibling.nextSibling = node;
/*     */       } 
/* 245 */       if (nextSibling.previous == null) {
/* 246 */         this.head = node;
/*     */       } else {
/* 248 */         nextSibling.previous.next = node;
/*     */       } 
/* 250 */       nextSibling.previous = node;
/* 251 */       nextSibling.previousSibling = node;
/*     */     } 
/* 253 */     this.size++;
/* 254 */     return node;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void removeNode(Node<K, V> node) {
/* 262 */     if (node.previous != null) {
/* 263 */       node.previous.next = node.next;
/*     */     } else {
/* 265 */       this.head = node.next;
/*     */     } 
/* 267 */     if (node.next != null) {
/* 268 */       node.next.previous = node.previous;
/*     */     } else {
/* 270 */       this.tail = node.previous;
/*     */     } 
/* 272 */     if (node.previousSibling == null && node.nextSibling == null) {
/* 273 */       KeyList<K, V> keyList = this.keyToKeyList.remove(node.key);
/* 274 */       keyList.count = 0;
/* 275 */       this.modCount++;
/*     */     } else {
/* 277 */       KeyList<K, V> keyList = this.keyToKeyList.get(node.key);
/* 278 */       keyList.count--;
/*     */       
/* 280 */       if (node.previousSibling == null) {
/* 281 */         keyList.head = node.nextSibling;
/*     */       } else {
/* 283 */         node.previousSibling.nextSibling = node.nextSibling;
/*     */       } 
/*     */       
/* 286 */       if (node.nextSibling == null) {
/* 287 */         keyList.tail = node.previousSibling;
/*     */       } else {
/* 289 */         node.nextSibling.previousSibling = node.previousSibling;
/*     */       } 
/*     */     } 
/* 292 */     this.size--;
/*     */   }
/*     */ 
/*     */   
/*     */   private void removeAllNodes(Object key) {
/* 297 */     Iterators.clear(new ValueForKeyIterator(key));
/*     */   }
/*     */ 
/*     */   
/*     */   private static void checkElement(Object node) {
/* 302 */     if (node == null)
/* 303 */       throw new NoSuchElementException(); 
/*     */   }
/*     */   
/*     */   private class NodeIterator
/*     */     implements ListIterator<Map.Entry<K, V>>
/*     */   {
/*     */     int nextIndex;
/*     */     LinkedListMultimap.Node<K, V> next;
/*     */     LinkedListMultimap.Node<K, V> current;
/*     */     LinkedListMultimap.Node<K, V> previous;
/* 313 */     int expectedModCount = LinkedListMultimap.this.modCount;
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
/*     */     private void checkForConcurrentModification() {
/* 334 */       if (LinkedListMultimap.this.modCount != this.expectedModCount) {
/* 335 */         throw new ConcurrentModificationException();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 341 */       checkForConcurrentModification();
/* 342 */       return (this.next != null);
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public LinkedListMultimap.Node<K, V> next() {
/* 348 */       checkForConcurrentModification();
/* 349 */       LinkedListMultimap.checkElement(this.next);
/* 350 */       this.previous = this.current = this.next;
/* 351 */       this.next = this.next.next;
/* 352 */       this.nextIndex++;
/* 353 */       return this.current;
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 358 */       checkForConcurrentModification();
/* 359 */       CollectPreconditions.checkRemove((this.current != null));
/* 360 */       if (this.current != this.next) {
/* 361 */         this.previous = this.current.previous;
/* 362 */         this.nextIndex--;
/*     */       } else {
/* 364 */         this.next = this.current.next;
/*     */       } 
/* 366 */       LinkedListMultimap.this.removeNode(this.current);
/* 367 */       this.current = null;
/* 368 */       this.expectedModCount = LinkedListMultimap.this.modCount;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasPrevious() {
/* 373 */       checkForConcurrentModification();
/* 374 */       return (this.previous != null);
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public LinkedListMultimap.Node<K, V> previous() {
/* 380 */       checkForConcurrentModification();
/* 381 */       LinkedListMultimap.checkElement(this.previous);
/* 382 */       this.next = this.current = this.previous;
/* 383 */       this.previous = this.previous.previous;
/* 384 */       this.nextIndex--;
/* 385 */       return this.current;
/*     */     }
/*     */ 
/*     */     
/*     */     public int nextIndex() {
/* 390 */       return this.nextIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     public int previousIndex() {
/* 395 */       return this.nextIndex - 1;
/*     */     }
/*     */ 
/*     */     
/*     */     public void set(Map.Entry<K, V> e) {
/* 400 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public void add(Map.Entry<K, V> e) {
/* 405 */       throw new UnsupportedOperationException(); }
/*     */     NodeIterator(int index) { int size = LinkedListMultimap.this.size(); Preconditions.checkPositionIndex(index, size); if (index >= size / 2) { this.previous = LinkedListMultimap.this.tail; this.nextIndex = size; while (index++ < size)
/*     */           previous();  } else { this.next = LinkedListMultimap.this.head; while (index-- > 0)
/*     */           next();  }
/* 409 */        this.current = null; } void setValue(V value) { Preconditions.checkState((this.current != null));
/* 410 */       this.current.value = value; }
/*     */   
/*     */   }
/*     */   
/*     */   private class DistinctKeyIterator
/*     */     implements Iterator<K> {
/* 416 */     final Set<K> seenKeys = Sets.newHashSetWithExpectedSize(LinkedListMultimap.this.keySet().size());
/* 417 */     LinkedListMultimap.Node<K, V> next = LinkedListMultimap.this.head;
/*     */     LinkedListMultimap.Node<K, V> current;
/* 419 */     int expectedModCount = LinkedListMultimap.this.modCount;
/*     */     
/*     */     private void checkForConcurrentModification() {
/* 422 */       if (LinkedListMultimap.this.modCount != this.expectedModCount) {
/* 423 */         throw new ConcurrentModificationException();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 429 */       checkForConcurrentModification();
/* 430 */       return (this.next != null);
/*     */     }
/*     */ 
/*     */     
/*     */     public K next() {
/* 435 */       checkForConcurrentModification();
/* 436 */       LinkedListMultimap.checkElement(this.next);
/* 437 */       this.current = this.next;
/* 438 */       this.seenKeys.add(this.current.key);
/*     */       do {
/* 440 */         this.next = this.next.next;
/* 441 */       } while (this.next != null && !this.seenKeys.add(this.next.key));
/* 442 */       return this.current.key;
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 447 */       checkForConcurrentModification();
/* 448 */       CollectPreconditions.checkRemove((this.current != null));
/* 449 */       LinkedListMultimap.this.removeAllNodes(this.current.key);
/* 450 */       this.current = null;
/* 451 */       this.expectedModCount = LinkedListMultimap.this.modCount;
/*     */     }
/*     */     
/*     */     private DistinctKeyIterator() {}
/*     */   }
/*     */   
/*     */   private class ValueForKeyIterator implements ListIterator<V> {
/*     */     final Object key;
/*     */     int nextIndex;
/*     */     LinkedListMultimap.Node<K, V> next;
/*     */     LinkedListMultimap.Node<K, V> current;
/*     */     LinkedListMultimap.Node<K, V> previous;
/*     */     
/*     */     ValueForKeyIterator(Object key) {
/* 465 */       this.key = key;
/* 466 */       LinkedListMultimap.KeyList<K, V> keyList = (LinkedListMultimap.KeyList<K, V>)LinkedListMultimap.this.keyToKeyList.get(key);
/* 467 */       this.next = (keyList == null) ? null : keyList.head;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ValueForKeyIterator(Object key, int index) {
/* 479 */       LinkedListMultimap.KeyList<K, V> keyList = (LinkedListMultimap.KeyList<K, V>)LinkedListMultimap.this.keyToKeyList.get(key);
/* 480 */       int size = (keyList == null) ? 0 : keyList.count;
/* 481 */       Preconditions.checkPositionIndex(index, size);
/* 482 */       if (index >= size / 2) {
/* 483 */         this.previous = (keyList == null) ? null : keyList.tail;
/* 484 */         this.nextIndex = size;
/* 485 */         while (index++ < size) {
/* 486 */           previous();
/*     */         }
/*     */       } else {
/* 489 */         this.next = (keyList == null) ? null : keyList.head;
/* 490 */         while (index-- > 0) {
/* 491 */           next();
/*     */         }
/*     */       } 
/* 494 */       this.key = key;
/* 495 */       this.current = null;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 500 */       return (this.next != null);
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public V next() {
/* 506 */       LinkedListMultimap.checkElement(this.next);
/* 507 */       this.previous = this.current = this.next;
/* 508 */       this.next = this.next.nextSibling;
/* 509 */       this.nextIndex++;
/* 510 */       return this.current.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasPrevious() {
/* 515 */       return (this.previous != null);
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public V previous() {
/* 521 */       LinkedListMultimap.checkElement(this.previous);
/* 522 */       this.next = this.current = this.previous;
/* 523 */       this.previous = this.previous.previousSibling;
/* 524 */       this.nextIndex--;
/* 525 */       return this.current.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public int nextIndex() {
/* 530 */       return this.nextIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     public int previousIndex() {
/* 535 */       return this.nextIndex - 1;
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 540 */       CollectPreconditions.checkRemove((this.current != null));
/* 541 */       if (this.current != this.next) {
/* 542 */         this.previous = this.current.previousSibling;
/* 543 */         this.nextIndex--;
/*     */       } else {
/* 545 */         this.next = this.current.nextSibling;
/*     */       } 
/* 547 */       LinkedListMultimap.this.removeNode(this.current);
/* 548 */       this.current = null;
/*     */     }
/*     */ 
/*     */     
/*     */     public void set(V value) {
/* 553 */       Preconditions.checkState((this.current != null));
/* 554 */       this.current.value = value;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void add(V value) {
/* 560 */       this.previous = LinkedListMultimap.this.addNode((K)this.key, value, this.next);
/* 561 */       this.nextIndex++;
/* 562 */       this.current = null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 570 */     return this.size;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 575 */     return (this.head == null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/* 580 */     return this.keyToKeyList.containsKey(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 585 */     return values().contains(value);
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
/*     */   @CanIgnoreReturnValue
/*     */   public boolean put(K key, V value) {
/* 600 */     addNode(key, value, null);
/* 601 */     return true;
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
/*     */   @CanIgnoreReturnValue
/*     */   public List<V> replaceValues(K key, Iterable<? extends V> values) {
/* 617 */     List<V> oldValues = getCopy(key);
/* 618 */     ListIterator<V> keyValues = new ValueForKeyIterator(key);
/* 619 */     Iterator<? extends V> newValues = values.iterator();
/*     */ 
/*     */     
/* 622 */     while (keyValues.hasNext() && newValues.hasNext()) {
/* 623 */       keyValues.next();
/* 624 */       keyValues.set(newValues.next());
/*     */     } 
/*     */ 
/*     */     
/* 628 */     while (keyValues.hasNext()) {
/* 629 */       keyValues.next();
/* 630 */       keyValues.remove();
/*     */     } 
/*     */ 
/*     */     
/* 634 */     while (newValues.hasNext()) {
/* 635 */       keyValues.add(newValues.next());
/*     */     }
/*     */     
/* 638 */     return oldValues;
/*     */   }
/*     */   
/*     */   private List<V> getCopy(Object key) {
/* 642 */     return Collections.unmodifiableList(Lists.newArrayList(new ValueForKeyIterator(key)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public List<V> removeAll(Object key) {
/* 653 */     List<V> oldValues = getCopy(key);
/* 654 */     removeAllNodes(key);
/* 655 */     return oldValues;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 660 */     this.head = null;
/* 661 */     this.tail = null;
/* 662 */     this.keyToKeyList.clear();
/* 663 */     this.size = 0;
/* 664 */     this.modCount++;
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
/*     */   public List<V> get(final K key) {
/* 680 */     return new AbstractSequentialList<V>()
/*     */       {
/*     */         public int size() {
/* 683 */           LinkedListMultimap.KeyList<K, V> keyList = (LinkedListMultimap.KeyList<K, V>)LinkedListMultimap.this.keyToKeyList.get(key);
/* 684 */           return (keyList == null) ? 0 : keyList.count;
/*     */         }
/*     */ 
/*     */         
/*     */         public ListIterator<V> listIterator(int index) {
/* 689 */           return new LinkedListMultimap.ValueForKeyIterator(key, index);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   Set<K> createKeySet() {
/*     */     class KeySetImpl
/*     */       extends Sets.ImprovedAbstractSet<K>
/*     */     {
/*     */       public int size() {
/* 700 */         return LinkedListMultimap.this.keyToKeyList.size();
/*     */       }
/*     */ 
/*     */       
/*     */       public Iterator<K> iterator() {
/* 705 */         return new LinkedListMultimap.DistinctKeyIterator();
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean contains(Object key) {
/* 710 */         return LinkedListMultimap.this.containsKey(key);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean remove(Object o) {
/* 715 */         return !LinkedListMultimap.this.removeAll(o).isEmpty();
/*     */       }
/*     */     };
/* 718 */     return new KeySetImpl();
/*     */   }
/*     */ 
/*     */   
/*     */   Multiset<K> createKeys() {
/* 723 */     return new Multimaps.Keys<>(this);
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
/*     */   public List<V> values() {
/* 736 */     return (List<V>)super.values();
/*     */   }
/*     */ 
/*     */   
/*     */   List<V> createValues() {
/*     */     class ValuesImpl
/*     */       extends AbstractSequentialList<V>
/*     */     {
/*     */       public int size() {
/* 745 */         return LinkedListMultimap.this.size;
/*     */       }
/*     */ 
/*     */       
/*     */       public ListIterator<V> listIterator(int index) {
/* 750 */         final LinkedListMultimap<K, V>.NodeIterator nodeItr = new LinkedListMultimap.NodeIterator(index);
/* 751 */         return new TransformedListIterator<Map.Entry<K, V>, V>(nodeItr)
/*     */           {
/*     */             V transform(Map.Entry<K, V> entry) {
/* 754 */               return entry.getValue();
/*     */             }
/*     */ 
/*     */             
/*     */             public void set(V value) {
/* 759 */               nodeItr.setValue(value);
/*     */             }
/*     */           };
/*     */       }
/*     */     };
/* 764 */     return new ValuesImpl();
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
/*     */   public List<Map.Entry<K, V>> entries() {
/* 785 */     return (List<Map.Entry<K, V>>)super.entries();
/*     */   }
/*     */ 
/*     */   
/*     */   List<Map.Entry<K, V>> createEntries() {
/*     */     class EntriesImpl
/*     */       extends AbstractSequentialList<Map.Entry<K, V>>
/*     */     {
/*     */       public int size() {
/* 794 */         return LinkedListMultimap.this.size;
/*     */       }
/*     */ 
/*     */       
/*     */       public ListIterator<Map.Entry<K, V>> listIterator(int index) {
/* 799 */         return new LinkedListMultimap.NodeIterator(index);
/*     */       }
/*     */ 
/*     */       
/*     */       public void forEach(Consumer<? super Map.Entry<K, V>> action) {
/* 804 */         Preconditions.checkNotNull(action);
/* 805 */         for (LinkedListMultimap.Node<K, V> node = LinkedListMultimap.this.head; node != null; node = node.next) {
/* 806 */           action.accept(node);
/*     */         }
/*     */       }
/*     */     };
/* 810 */     return new EntriesImpl();
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<Map.Entry<K, V>> entryIterator() {
/* 815 */     throw new AssertionError("should never be called");
/*     */   }
/*     */ 
/*     */   
/*     */   Map<K, Collection<V>> createAsMap() {
/* 820 */     return new Multimaps.AsMap<>(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 830 */     stream.defaultWriteObject();
/* 831 */     stream.writeInt(size());
/* 832 */     for (Map.Entry<K, V> entry : entries()) {
/* 833 */       stream.writeObject(entry.getKey());
/* 834 */       stream.writeObject(entry.getValue());
/*     */     } 
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 840 */     stream.defaultReadObject();
/* 841 */     this.keyToKeyList = Maps.newLinkedHashMap();
/* 842 */     int size = stream.readInt();
/* 843 */     for (int i = 0; i < size; i++) {
/*     */       
/* 845 */       K key = (K)stream.readObject();
/*     */       
/* 847 */       V value = (V)stream.readObject();
/* 848 */       put(key, value);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\LinkedListMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */