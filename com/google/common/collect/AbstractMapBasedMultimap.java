/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.base.Preconditions;
/*      */ import java.io.Serializable;
/*      */ import java.util.AbstractCollection;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.ConcurrentModificationException;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Map;
/*      */ import java.util.NavigableMap;
/*      */ import java.util.NavigableSet;
/*      */ import java.util.RandomAccess;
/*      */ import java.util.Set;
/*      */ import java.util.SortedMap;
/*      */ import java.util.SortedSet;
/*      */ import java.util.Spliterator;
/*      */ import java.util.function.BiConsumer;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @GwtCompatible
/*      */ abstract class AbstractMapBasedMultimap<K, V>
/*      */   extends AbstractMultimap<K, V>
/*      */   implements Serializable
/*      */ {
/*      */   private transient Map<K, Collection<V>> map;
/*      */   private transient int totalSize;
/*      */   private static final long serialVersionUID = 2447537837011683357L;
/*      */   
/*      */   protected AbstractMapBasedMultimap(Map<K, Collection<V>> map) {
/*  118 */     Preconditions.checkArgument(map.isEmpty());
/*  119 */     this.map = map;
/*      */   }
/*      */ 
/*      */   
/*      */   final void setMap(Map<K, Collection<V>> map) {
/*  124 */     this.map = map;
/*  125 */     this.totalSize = 0;
/*  126 */     for (Collection<V> values : map.values()) {
/*  127 */       Preconditions.checkArgument(!values.isEmpty());
/*  128 */       this.totalSize += values.size();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Collection<V> createUnmodifiableEmptyCollection() {
/*  138 */     return unmodifiableCollectionSubclass(createCollection());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Collection<V> createCollection(K key) {
/*  162 */     return createCollection();
/*      */   }
/*      */   
/*      */   Map<K, Collection<V>> backingMap() {
/*  166 */     return this.map;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int size() {
/*  173 */     return this.totalSize;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean containsKey(Object key) {
/*  178 */     return this.map.containsKey(key);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean put(K key, V value) {
/*  185 */     Collection<V> collection = this.map.get(key);
/*  186 */     if (collection == null) {
/*  187 */       collection = createCollection(key);
/*  188 */       if (collection.add(value)) {
/*  189 */         this.totalSize++;
/*  190 */         this.map.put(key, collection);
/*  191 */         return true;
/*      */       } 
/*  193 */       throw new AssertionError("New Collection violated the Collection spec");
/*      */     } 
/*  195 */     if (collection.add(value)) {
/*  196 */       this.totalSize++;
/*  197 */       return true;
/*      */     } 
/*  199 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private Collection<V> getOrCreateCollection(K key) {
/*  204 */     Collection<V> collection = this.map.get(key);
/*  205 */     if (collection == null) {
/*  206 */       collection = createCollection(key);
/*  207 */       this.map.put(key, collection);
/*      */     } 
/*  209 */     return collection;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<V> replaceValues(K key, Iterable<? extends V> values) {
/*  221 */     Iterator<? extends V> iterator = values.iterator();
/*  222 */     if (!iterator.hasNext()) {
/*  223 */       return removeAll(key);
/*      */     }
/*      */ 
/*      */     
/*  227 */     Collection<V> collection = getOrCreateCollection(key);
/*  228 */     Collection<V> oldValues = createCollection();
/*  229 */     oldValues.addAll(collection);
/*      */     
/*  231 */     this.totalSize -= collection.size();
/*  232 */     collection.clear();
/*      */     
/*  234 */     while (iterator.hasNext()) {
/*  235 */       if (collection.add(iterator.next())) {
/*  236 */         this.totalSize++;
/*      */       }
/*      */     } 
/*      */     
/*  240 */     return unmodifiableCollectionSubclass(oldValues);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<V> removeAll(Object key) {
/*  250 */     Collection<V> collection = this.map.remove(key);
/*      */     
/*  252 */     if (collection == null) {
/*  253 */       return createUnmodifiableEmptyCollection();
/*      */     }
/*      */     
/*  256 */     Collection<V> output = createCollection();
/*  257 */     output.addAll(collection);
/*  258 */     this.totalSize -= collection.size();
/*  259 */     collection.clear();
/*      */     
/*  261 */     return unmodifiableCollectionSubclass(output);
/*      */   }
/*      */   
/*      */   <E> Collection<E> unmodifiableCollectionSubclass(Collection<E> collection) {
/*  265 */     return Collections.unmodifiableCollection(collection);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void clear() {
/*  271 */     for (Collection<V> collection : this.map.values()) {
/*  272 */       collection.clear();
/*      */     }
/*  274 */     this.map.clear();
/*  275 */     this.totalSize = 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<V> get(K key) {
/*  287 */     Collection<V> collection = this.map.get(key);
/*  288 */     if (collection == null) {
/*  289 */       collection = createCollection(key);
/*      */     }
/*  291 */     return wrapCollection(key, collection);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Collection<V> wrapCollection(K key, Collection<V> collection) {
/*  299 */     return new WrappedCollection(key, collection, null);
/*      */   }
/*      */   
/*      */   final List<V> wrapList(K key, List<V> list, WrappedCollection ancestor) {
/*  303 */     return (list instanceof RandomAccess) ? new RandomAccessWrappedList(key, list, ancestor) : new WrappedList(key, list, ancestor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   class WrappedCollection
/*      */     extends AbstractCollection<V>
/*      */   {
/*      */     final K key;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Collection<V> delegate;
/*      */ 
/*      */ 
/*      */     
/*      */     final WrappedCollection ancestor;
/*      */ 
/*      */ 
/*      */     
/*      */     final Collection<V> ancestorDelegate;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     WrappedCollection(K key, Collection<V> delegate, WrappedCollection ancestor) {
/*  332 */       this.key = key;
/*  333 */       this.delegate = delegate;
/*  334 */       this.ancestor = ancestor;
/*  335 */       this.ancestorDelegate = (ancestor == null) ? null : ancestor.getDelegate();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void refreshIfEmpty() {
/*  346 */       if (this.ancestor != null) {
/*  347 */         this.ancestor.refreshIfEmpty();
/*  348 */         if (this.ancestor.getDelegate() != this.ancestorDelegate) {
/*  349 */           throw new ConcurrentModificationException();
/*      */         }
/*  351 */       } else if (this.delegate.isEmpty()) {
/*  352 */         Collection<V> newDelegate = (Collection<V>)AbstractMapBasedMultimap.this.map.get(this.key);
/*  353 */         if (newDelegate != null) {
/*  354 */           this.delegate = newDelegate;
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void removeIfEmpty() {
/*  364 */       if (this.ancestor != null) {
/*  365 */         this.ancestor.removeIfEmpty();
/*  366 */       } else if (this.delegate.isEmpty()) {
/*  367 */         AbstractMapBasedMultimap.this.map.remove(this.key);
/*      */       } 
/*      */     }
/*      */     
/*      */     K getKey() {
/*  372 */       return this.key;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void addToMap() {
/*  382 */       if (this.ancestor != null) {
/*  383 */         this.ancestor.addToMap();
/*      */       } else {
/*  385 */         AbstractMapBasedMultimap.this.map.put(this.key, this.delegate);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  391 */       refreshIfEmpty();
/*  392 */       return this.delegate.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object object) {
/*  397 */       if (object == this) {
/*  398 */         return true;
/*      */       }
/*  400 */       refreshIfEmpty();
/*  401 */       return this.delegate.equals(object);
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  406 */       refreshIfEmpty();
/*  407 */       return this.delegate.hashCode();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  412 */       refreshIfEmpty();
/*  413 */       return this.delegate.toString();
/*      */     }
/*      */     
/*      */     Collection<V> getDelegate() {
/*  417 */       return this.delegate;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<V> iterator() {
/*  422 */       refreshIfEmpty();
/*  423 */       return new WrappedIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public Spliterator<V> spliterator() {
/*  428 */       refreshIfEmpty();
/*  429 */       return this.delegate.spliterator();
/*      */     }
/*      */     
/*      */     class WrappedIterator
/*      */       implements Iterator<V> {
/*      */       final Iterator<V> delegateIterator;
/*  435 */       final Collection<V> originalDelegate = AbstractMapBasedMultimap.WrappedCollection.this.delegate;
/*      */       
/*      */       WrappedIterator() {
/*  438 */         this.delegateIterator = AbstractMapBasedMultimap.iteratorOrListIterator(AbstractMapBasedMultimap.WrappedCollection.this.delegate);
/*      */       }
/*      */       
/*      */       WrappedIterator(Iterator<V> delegateIterator) {
/*  442 */         this.delegateIterator = delegateIterator;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       void validateIterator() {
/*  449 */         AbstractMapBasedMultimap.WrappedCollection.this.refreshIfEmpty();
/*  450 */         if (AbstractMapBasedMultimap.WrappedCollection.this.delegate != this.originalDelegate) {
/*  451 */           throw new ConcurrentModificationException();
/*      */         }
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean hasNext() {
/*  457 */         validateIterator();
/*  458 */         return this.delegateIterator.hasNext();
/*      */       }
/*      */ 
/*      */       
/*      */       public V next() {
/*  463 */         validateIterator();
/*  464 */         return this.delegateIterator.next();
/*      */       }
/*      */ 
/*      */       
/*      */       public void remove() {
/*  469 */         this.delegateIterator.remove();
/*  470 */         AbstractMapBasedMultimap.this.totalSize--;
/*  471 */         AbstractMapBasedMultimap.WrappedCollection.this.removeIfEmpty();
/*      */       }
/*      */       
/*      */       Iterator<V> getDelegateIterator() {
/*  475 */         validateIterator();
/*  476 */         return this.delegateIterator;
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean add(V value) {
/*  482 */       refreshIfEmpty();
/*  483 */       boolean wasEmpty = this.delegate.isEmpty();
/*  484 */       boolean changed = this.delegate.add(value);
/*  485 */       if (changed) {
/*  486 */         AbstractMapBasedMultimap.this.totalSize++;
/*  487 */         if (wasEmpty) {
/*  488 */           addToMap();
/*      */         }
/*      */       } 
/*  491 */       return changed;
/*      */     }
/*      */     
/*      */     WrappedCollection getAncestor() {
/*  495 */       return this.ancestor;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean addAll(Collection<? extends V> collection) {
/*  502 */       if (collection.isEmpty()) {
/*  503 */         return false;
/*      */       }
/*  505 */       int oldSize = size();
/*  506 */       boolean changed = this.delegate.addAll(collection);
/*  507 */       if (changed) {
/*  508 */         int newSize = this.delegate.size();
/*  509 */         AbstractMapBasedMultimap.this.totalSize = AbstractMapBasedMultimap.this.totalSize + newSize - oldSize;
/*  510 */         if (oldSize == 0) {
/*  511 */           addToMap();
/*      */         }
/*      */       } 
/*  514 */       return changed;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/*  519 */       refreshIfEmpty();
/*  520 */       return this.delegate.contains(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsAll(Collection<?> c) {
/*  525 */       refreshIfEmpty();
/*  526 */       return this.delegate.containsAll(c);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/*  531 */       int oldSize = size();
/*  532 */       if (oldSize == 0) {
/*      */         return;
/*      */       }
/*  535 */       this.delegate.clear();
/*  536 */       AbstractMapBasedMultimap.this.totalSize = AbstractMapBasedMultimap.this.totalSize - oldSize;
/*  537 */       removeIfEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/*  542 */       refreshIfEmpty();
/*  543 */       boolean changed = this.delegate.remove(o);
/*  544 */       if (changed) {
/*  545 */         AbstractMapBasedMultimap.this.totalSize--;
/*  546 */         removeIfEmpty();
/*      */       } 
/*  548 */       return changed;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeAll(Collection<?> c) {
/*  553 */       if (c.isEmpty()) {
/*  554 */         return false;
/*      */       }
/*  556 */       int oldSize = size();
/*  557 */       boolean changed = this.delegate.removeAll(c);
/*  558 */       if (changed) {
/*  559 */         int newSize = this.delegate.size();
/*  560 */         AbstractMapBasedMultimap.this.totalSize = AbstractMapBasedMultimap.this.totalSize + newSize - oldSize;
/*  561 */         removeIfEmpty();
/*      */       } 
/*  563 */       return changed;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean retainAll(Collection<?> c) {
/*  568 */       Preconditions.checkNotNull(c);
/*  569 */       int oldSize = size();
/*  570 */       boolean changed = this.delegate.retainAll(c);
/*  571 */       if (changed) {
/*  572 */         int newSize = this.delegate.size();
/*  573 */         AbstractMapBasedMultimap.this.totalSize = AbstractMapBasedMultimap.this.totalSize + newSize - oldSize;
/*  574 */         removeIfEmpty();
/*      */       } 
/*  576 */       return changed;
/*      */     }
/*      */   }
/*      */   
/*      */   private static <E> Iterator<E> iteratorOrListIterator(Collection<E> collection) {
/*  581 */     return (collection instanceof List) ? ((List<E>)collection)
/*  582 */       .listIterator() : collection
/*  583 */       .iterator();
/*      */   }
/*      */   
/*      */   class WrappedSet
/*      */     extends WrappedCollection
/*      */     implements Set<V> {
/*      */     WrappedSet(K key, Set<V> delegate) {
/*  590 */       super(key, delegate, null);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeAll(Collection<?> c) {
/*  595 */       if (c.isEmpty()) {
/*  596 */         return false;
/*      */       }
/*  598 */       int oldSize = size();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  603 */       boolean changed = Sets.removeAllImpl((Set)this.delegate, c);
/*  604 */       if (changed) {
/*  605 */         int newSize = this.delegate.size();
/*  606 */         AbstractMapBasedMultimap.this.totalSize = AbstractMapBasedMultimap.this.totalSize + newSize - oldSize;
/*  607 */         removeIfEmpty();
/*      */       } 
/*  609 */       return changed;
/*      */     }
/*      */   }
/*      */   
/*      */   class WrappedSortedSet
/*      */     extends WrappedCollection
/*      */     implements SortedSet<V> {
/*      */     WrappedSortedSet(K key, SortedSet<V> delegate, AbstractMapBasedMultimap<K, V>.WrappedCollection ancestor) {
/*  617 */       super(key, delegate, ancestor);
/*      */     }
/*      */     
/*      */     SortedSet<V> getSortedSetDelegate() {
/*  621 */       return (SortedSet<V>)getDelegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super V> comparator() {
/*  626 */       return getSortedSetDelegate().comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     public V first() {
/*  631 */       refreshIfEmpty();
/*  632 */       return getSortedSetDelegate().first();
/*      */     }
/*      */ 
/*      */     
/*      */     public V last() {
/*  637 */       refreshIfEmpty();
/*  638 */       return getSortedSetDelegate().last();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<V> headSet(V toElement) {
/*  643 */       refreshIfEmpty();
/*  644 */       return new WrappedSortedSet(
/*  645 */           getKey(), 
/*  646 */           getSortedSetDelegate().headSet(toElement), 
/*  647 */           (getAncestor() == null) ? this : getAncestor());
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<V> subSet(V fromElement, V toElement) {
/*  652 */       refreshIfEmpty();
/*  653 */       return new WrappedSortedSet(
/*  654 */           getKey(), 
/*  655 */           getSortedSetDelegate().subSet(fromElement, toElement), 
/*  656 */           (getAncestor() == null) ? this : getAncestor());
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<V> tailSet(V fromElement) {
/*  661 */       refreshIfEmpty();
/*  662 */       return new WrappedSortedSet(
/*  663 */           getKey(), 
/*  664 */           getSortedSetDelegate().tailSet(fromElement), 
/*  665 */           (getAncestor() == null) ? this : getAncestor());
/*      */     }
/*      */   }
/*      */   
/*      */   class WrappedNavigableSet
/*      */     extends WrappedSortedSet
/*      */     implements NavigableSet<V> {
/*      */     WrappedNavigableSet(K key, NavigableSet<V> delegate, AbstractMapBasedMultimap<K, V>.WrappedCollection ancestor) {
/*  673 */       super(key, delegate, ancestor);
/*      */     }
/*      */ 
/*      */     
/*      */     NavigableSet<V> getSortedSetDelegate() {
/*  678 */       return (NavigableSet<V>)super.getSortedSetDelegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public V lower(V v) {
/*  683 */       return getSortedSetDelegate().lower(v);
/*      */     }
/*      */ 
/*      */     
/*      */     public V floor(V v) {
/*  688 */       return getSortedSetDelegate().floor(v);
/*      */     }
/*      */ 
/*      */     
/*      */     public V ceiling(V v) {
/*  693 */       return getSortedSetDelegate().ceiling(v);
/*      */     }
/*      */ 
/*      */     
/*      */     public V higher(V v) {
/*  698 */       return getSortedSetDelegate().higher(v);
/*      */     }
/*      */ 
/*      */     
/*      */     public V pollFirst() {
/*  703 */       return Iterators.pollNext(iterator());
/*      */     }
/*      */ 
/*      */     
/*      */     public V pollLast() {
/*  708 */       return Iterators.pollNext(descendingIterator());
/*      */     }
/*      */     
/*      */     private NavigableSet<V> wrap(NavigableSet<V> wrapped) {
/*  712 */       return new WrappedNavigableSet(this.key, wrapped, (getAncestor() == null) ? this : getAncestor());
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<V> descendingSet() {
/*  717 */       return wrap(getSortedSetDelegate().descendingSet());
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<V> descendingIterator() {
/*  722 */       return new AbstractMapBasedMultimap.WrappedCollection.WrappedIterator(this, getSortedSetDelegate().descendingIterator());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<V> subSet(V fromElement, boolean fromInclusive, V toElement, boolean toInclusive) {
/*  728 */       return wrap(
/*  729 */           getSortedSetDelegate().subSet(fromElement, fromInclusive, toElement, toInclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<V> headSet(V toElement, boolean inclusive) {
/*  734 */       return wrap(getSortedSetDelegate().headSet(toElement, inclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<V> tailSet(V fromElement, boolean inclusive) {
/*  739 */       return wrap(getSortedSetDelegate().tailSet(fromElement, inclusive));
/*      */     }
/*      */   }
/*      */   
/*      */   class WrappedList
/*      */     extends WrappedCollection
/*      */     implements List<V> {
/*      */     WrappedList(K key, List<V> delegate, AbstractMapBasedMultimap<K, V>.WrappedCollection ancestor) {
/*  747 */       super(key, delegate, ancestor);
/*      */     }
/*      */     
/*      */     List<V> getListDelegate() {
/*  751 */       return (List<V>)getDelegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean addAll(int index, Collection<? extends V> c) {
/*  756 */       if (c.isEmpty()) {
/*  757 */         return false;
/*      */       }
/*  759 */       int oldSize = size();
/*  760 */       boolean changed = getListDelegate().addAll(index, c);
/*  761 */       if (changed) {
/*  762 */         int newSize = getDelegate().size();
/*  763 */         AbstractMapBasedMultimap.this.totalSize = AbstractMapBasedMultimap.this.totalSize + newSize - oldSize;
/*  764 */         if (oldSize == 0) {
/*  765 */           addToMap();
/*      */         }
/*      */       } 
/*  768 */       return changed;
/*      */     }
/*      */ 
/*      */     
/*      */     public V get(int index) {
/*  773 */       refreshIfEmpty();
/*  774 */       return getListDelegate().get(index);
/*      */     }
/*      */ 
/*      */     
/*      */     public V set(int index, V element) {
/*  779 */       refreshIfEmpty();
/*  780 */       return getListDelegate().set(index, element);
/*      */     }
/*      */ 
/*      */     
/*      */     public void add(int index, V element) {
/*  785 */       refreshIfEmpty();
/*  786 */       boolean wasEmpty = getDelegate().isEmpty();
/*  787 */       getListDelegate().add(index, element);
/*  788 */       AbstractMapBasedMultimap.this.totalSize++;
/*  789 */       if (wasEmpty) {
/*  790 */         addToMap();
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public V remove(int index) {
/*  796 */       refreshIfEmpty();
/*  797 */       V value = getListDelegate().remove(index);
/*  798 */       AbstractMapBasedMultimap.this.totalSize--;
/*  799 */       removeIfEmpty();
/*  800 */       return value;
/*      */     }
/*      */ 
/*      */     
/*      */     public int indexOf(Object o) {
/*  805 */       refreshIfEmpty();
/*  806 */       return getListDelegate().indexOf(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public int lastIndexOf(Object o) {
/*  811 */       refreshIfEmpty();
/*  812 */       return getListDelegate().lastIndexOf(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public ListIterator<V> listIterator() {
/*  817 */       refreshIfEmpty();
/*  818 */       return new WrappedListIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public ListIterator<V> listIterator(int index) {
/*  823 */       refreshIfEmpty();
/*  824 */       return new WrappedListIterator(index);
/*      */     }
/*      */ 
/*      */     
/*      */     public List<V> subList(int fromIndex, int toIndex) {
/*  829 */       refreshIfEmpty();
/*  830 */       return AbstractMapBasedMultimap.this.wrapList(
/*  831 */           getKey(), 
/*  832 */           getListDelegate().subList(fromIndex, toIndex), 
/*  833 */           (getAncestor() == null) ? this : getAncestor());
/*      */     }
/*      */     
/*      */     private class WrappedListIterator
/*      */       extends AbstractMapBasedMultimap<K, V>.WrappedCollection.WrappedIterator implements ListIterator<V> {
/*      */       WrappedListIterator() {}
/*      */       
/*      */       public WrappedListIterator(int index) {
/*  841 */         super(AbstractMapBasedMultimap.WrappedList.this.getListDelegate().listIterator(index));
/*      */       }
/*      */       
/*      */       private ListIterator<V> getDelegateListIterator() {
/*  845 */         return (ListIterator<V>)getDelegateIterator();
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean hasPrevious() {
/*  850 */         return getDelegateListIterator().hasPrevious();
/*      */       }
/*      */ 
/*      */       
/*      */       public V previous() {
/*  855 */         return getDelegateListIterator().previous();
/*      */       }
/*      */ 
/*      */       
/*      */       public int nextIndex() {
/*  860 */         return getDelegateListIterator().nextIndex();
/*      */       }
/*      */ 
/*      */       
/*      */       public int previousIndex() {
/*  865 */         return getDelegateListIterator().previousIndex();
/*      */       }
/*      */ 
/*      */       
/*      */       public void set(V value) {
/*  870 */         getDelegateListIterator().set(value);
/*      */       }
/*      */ 
/*      */       
/*      */       public void add(V value) {
/*  875 */         boolean wasEmpty = AbstractMapBasedMultimap.WrappedList.this.isEmpty();
/*  876 */         getDelegateListIterator().add(value);
/*  877 */         AbstractMapBasedMultimap.this.totalSize++;
/*  878 */         if (wasEmpty) {
/*  879 */           AbstractMapBasedMultimap.WrappedList.this.addToMap();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private class RandomAccessWrappedList
/*      */     extends WrappedList
/*      */     implements RandomAccess
/*      */   {
/*      */     RandomAccessWrappedList(K key, List<V> delegate, AbstractMapBasedMultimap<K, V>.WrappedCollection ancestor) {
/*  892 */       super(key, delegate, ancestor);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   Set<K> createKeySet() {
/*  898 */     return new KeySet(this.map);
/*      */   }
/*      */   
/*      */   final Set<K> createMaybeNavigableKeySet() {
/*  902 */     if (this.map instanceof NavigableMap)
/*  903 */       return new NavigableKeySet((NavigableMap<K, Collection<V>>)this.map); 
/*  904 */     if (this.map instanceof SortedMap) {
/*  905 */       return new SortedKeySet((SortedMap<K, Collection<V>>)this.map);
/*      */     }
/*  907 */     return new KeySet(this.map);
/*      */   }
/*      */   
/*      */   private class KeySet
/*      */     extends Maps.KeySet<K, Collection<V>>
/*      */   {
/*      */     KeySet(Map<K, Collection<V>> subMap) {
/*  914 */       super(subMap);
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<K> iterator() {
/*  919 */       final Iterator<Map.Entry<K, Collection<V>>> entryIterator = map().entrySet().iterator();
/*  920 */       return new Iterator<K>()
/*      */         {
/*      */           Map.Entry<K, Collection<V>> entry;
/*      */           
/*      */           public boolean hasNext() {
/*  925 */             return entryIterator.hasNext();
/*      */           }
/*      */ 
/*      */           
/*      */           public K next() {
/*  930 */             this.entry = entryIterator.next();
/*  931 */             return this.entry.getKey();
/*      */           }
/*      */ 
/*      */           
/*      */           public void remove() {
/*  936 */             CollectPreconditions.checkRemove((this.entry != null));
/*  937 */             Collection<V> collection = this.entry.getValue();
/*  938 */             entryIterator.remove();
/*  939 */             AbstractMapBasedMultimap.this.totalSize = AbstractMapBasedMultimap.this.totalSize - collection.size();
/*  940 */             collection.clear();
/*  941 */             this.entry = null;
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Spliterator<K> spliterator() {
/*  950 */       return map().keySet().spliterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object key) {
/*  955 */       int count = 0;
/*  956 */       Collection<V> collection = map().remove(key);
/*  957 */       if (collection != null) {
/*  958 */         count = collection.size();
/*  959 */         collection.clear();
/*  960 */         AbstractMapBasedMultimap.this.totalSize = AbstractMapBasedMultimap.this.totalSize - count;
/*      */       } 
/*  962 */       return (count > 0);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/*  967 */       Iterators.clear(iterator());
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsAll(Collection<?> c) {
/*  972 */       return map().keySet().containsAll(c);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object object) {
/*  977 */       return (this == object || map().keySet().equals(object));
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  982 */       return map().keySet().hashCode();
/*      */     }
/*      */   }
/*      */   
/*      */   private class SortedKeySet
/*      */     extends KeySet
/*      */     implements SortedSet<K> {
/*      */     SortedKeySet(SortedMap<K, Collection<V>> subMap) {
/*  990 */       super(subMap);
/*      */     }
/*      */     
/*      */     SortedMap<K, Collection<V>> sortedMap() {
/*  994 */       return (SortedMap<K, Collection<V>>)map();
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super K> comparator() {
/*  999 */       return sortedMap().comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     public K first() {
/* 1004 */       return sortedMap().firstKey();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> headSet(K toElement) {
/* 1009 */       return new SortedKeySet(sortedMap().headMap(toElement));
/*      */     }
/*      */ 
/*      */     
/*      */     public K last() {
/* 1014 */       return sortedMap().lastKey();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> subSet(K fromElement, K toElement) {
/* 1019 */       return new SortedKeySet(sortedMap().subMap(fromElement, toElement));
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> tailSet(K fromElement) {
/* 1024 */       return new SortedKeySet(sortedMap().tailMap(fromElement));
/*      */     }
/*      */   }
/*      */   
/*      */   class NavigableKeySet
/*      */     extends SortedKeySet implements NavigableSet<K> {
/*      */     NavigableKeySet(NavigableMap<K, Collection<V>> subMap) {
/* 1031 */       super(subMap);
/*      */     }
/*      */ 
/*      */     
/*      */     NavigableMap<K, Collection<V>> sortedMap() {
/* 1036 */       return (NavigableMap<K, Collection<V>>)super.sortedMap();
/*      */     }
/*      */ 
/*      */     
/*      */     public K lower(K k) {
/* 1041 */       return sortedMap().lowerKey(k);
/*      */     }
/*      */ 
/*      */     
/*      */     public K floor(K k) {
/* 1046 */       return sortedMap().floorKey(k);
/*      */     }
/*      */ 
/*      */     
/*      */     public K ceiling(K k) {
/* 1051 */       return sortedMap().ceilingKey(k);
/*      */     }
/*      */ 
/*      */     
/*      */     public K higher(K k) {
/* 1056 */       return sortedMap().higherKey(k);
/*      */     }
/*      */ 
/*      */     
/*      */     public K pollFirst() {
/* 1061 */       return Iterators.pollNext(iterator());
/*      */     }
/*      */ 
/*      */     
/*      */     public K pollLast() {
/* 1066 */       return Iterators.pollNext(descendingIterator());
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> descendingSet() {
/* 1071 */       return new NavigableKeySet(sortedMap().descendingMap());
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<K> descendingIterator() {
/* 1076 */       return descendingSet().iterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> headSet(K toElement) {
/* 1081 */       return headSet(toElement, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> headSet(K toElement, boolean inclusive) {
/* 1086 */       return new NavigableKeySet(sortedMap().headMap(toElement, inclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> subSet(K fromElement, K toElement) {
/* 1091 */       return subSet(fromElement, true, toElement, false);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<K> subSet(K fromElement, boolean fromInclusive, K toElement, boolean toInclusive) {
/* 1097 */       return new NavigableKeySet(
/* 1098 */           sortedMap().subMap(fromElement, fromInclusive, toElement, toInclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> tailSet(K fromElement) {
/* 1103 */       return tailSet(fromElement, true);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> tailSet(K fromElement, boolean inclusive) {
/* 1108 */       return new NavigableKeySet(sortedMap().tailMap(fromElement, inclusive));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void removeValuesForKey(Object key) {
/* 1114 */     Collection<V> collection = Maps.<Collection<V>>safeRemove(this.map, key);
/*      */     
/* 1116 */     if (collection != null) {
/* 1117 */       int count = collection.size();
/* 1118 */       collection.clear();
/* 1119 */       this.totalSize -= count;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private abstract class Itr<T>
/*      */     implements Iterator<T>
/*      */   {
/* 1130 */     final Iterator<Map.Entry<K, Collection<V>>> keyIterator = AbstractMapBasedMultimap.this.map.entrySet().iterator();
/* 1131 */     K key = null;
/* 1132 */     Collection<V> collection = null;
/* 1133 */     Iterator<V> valueIterator = Iterators.emptyModifiableIterator();
/*      */ 
/*      */     
/*      */     abstract T output(K param1K, V param1V);
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/* 1140 */       return (this.keyIterator.hasNext() || this.valueIterator.hasNext());
/*      */     }
/*      */ 
/*      */     
/*      */     public T next() {
/* 1145 */       if (!this.valueIterator.hasNext()) {
/* 1146 */         Map.Entry<K, Collection<V>> mapEntry = this.keyIterator.next();
/* 1147 */         this.key = mapEntry.getKey();
/* 1148 */         this.collection = mapEntry.getValue();
/* 1149 */         this.valueIterator = this.collection.iterator();
/*      */       } 
/* 1151 */       return output(this.key, this.valueIterator.next());
/*      */     }
/*      */ 
/*      */     
/*      */     public void remove() {
/* 1156 */       this.valueIterator.remove();
/* 1157 */       if (this.collection.isEmpty()) {
/* 1158 */         this.keyIterator.remove();
/*      */       }
/* 1160 */       AbstractMapBasedMultimap.this.totalSize--;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<V> values() {
/* 1172 */     return super.values();
/*      */   }
/*      */ 
/*      */   
/*      */   Collection<V> createValues() {
/* 1177 */     return new AbstractMultimap.Values(this);
/*      */   }
/*      */ 
/*      */   
/*      */   Iterator<V> valueIterator() {
/* 1182 */     return new Itr<V>()
/*      */       {
/*      */         V output(K key, V value) {
/* 1185 */           return value;
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   Spliterator<V> valueSpliterator() {
/* 1192 */     return CollectSpliterators.flatMap(this.map
/* 1193 */         .values().spliterator(), Collection::spliterator, 64, size());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Multiset<K> createKeys() {
/* 1204 */     return new Multimaps.Keys<>(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<Map.Entry<K, V>> entries() {
/* 1218 */     return super.entries();
/*      */   }
/*      */ 
/*      */   
/*      */   Collection<Map.Entry<K, V>> createEntries() {
/* 1223 */     if (this instanceof SetMultimap) {
/* 1224 */       return new AbstractMultimap.EntrySet(this);
/*      */     }
/* 1226 */     return new AbstractMultimap.Entries(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Iterator<Map.Entry<K, V>> entryIterator() {
/* 1240 */     return new Itr<Map.Entry<K, V>>()
/*      */       {
/*      */         Map.Entry<K, V> output(K key, V value) {
/* 1243 */           return Maps.immutableEntry(key, value);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   Spliterator<Map.Entry<K, V>> entrySpliterator() {
/* 1250 */     return CollectSpliterators.flatMap(this.map
/* 1251 */         .entrySet().spliterator(), keyToValueCollectionEntry -> { K key = (K)keyToValueCollectionEntry.getKey(); Collection<V> valueCollection = (Collection<V>)keyToValueCollectionEntry.getValue(); return CollectSpliterators.map(valueCollection.spliterator(), ()); }64, 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1259 */         size());
/*      */   }
/*      */ 
/*      */   
/*      */   public void forEach(BiConsumer<? super K, ? super V> action) {
/* 1264 */     Preconditions.checkNotNull(action);
/* 1265 */     this.map.forEach((key, valueCollection) -> valueCollection.forEach(()));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   Map<K, Collection<V>> createAsMap() {
/* 1271 */     return new AsMap(this.map);
/*      */   }
/*      */   
/*      */   final Map<K, Collection<V>> createMaybeNavigableAsMap() {
/* 1275 */     if (this.map instanceof NavigableMap)
/* 1276 */       return new NavigableAsMap((NavigableMap<K, Collection<V>>)this.map); 
/* 1277 */     if (this.map instanceof SortedMap) {
/* 1278 */       return new SortedAsMap((SortedMap<K, Collection<V>>)this.map);
/*      */     }
/* 1280 */     return new AsMap(this.map);
/*      */   }
/*      */ 
/*      */   
/*      */   abstract Collection<V> createCollection();
/*      */ 
/*      */   
/*      */   private class AsMap
/*      */     extends Maps.ViewCachingAbstractMap<K, Collection<V>>
/*      */   {
/*      */     final transient Map<K, Collection<V>> submap;
/*      */     
/*      */     AsMap(Map<K, Collection<V>> submap) {
/* 1293 */       this.submap = submap;
/*      */     }
/*      */ 
/*      */     
/*      */     protected Set<Map.Entry<K, Collection<V>>> createEntrySet() {
/* 1298 */       return new AsMapEntries();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean containsKey(Object key) {
/* 1305 */       return Maps.safeContainsKey(this.submap, key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> get(Object key) {
/* 1310 */       Collection<V> collection = Maps.<Collection<V>>safeGet(this.submap, key);
/* 1311 */       if (collection == null) {
/* 1312 */         return null;
/*      */       }
/*      */       
/* 1315 */       K k = (K)key;
/* 1316 */       return AbstractMapBasedMultimap.this.wrapCollection(k, collection);
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/* 1321 */       return AbstractMapBasedMultimap.this.keySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1326 */       return this.submap.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> remove(Object key) {
/* 1331 */       Collection<V> collection = this.submap.remove(key);
/* 1332 */       if (collection == null) {
/* 1333 */         return null;
/*      */       }
/*      */       
/* 1336 */       Collection<V> output = AbstractMapBasedMultimap.this.createCollection();
/* 1337 */       output.addAll(collection);
/* 1338 */       AbstractMapBasedMultimap.this.totalSize = AbstractMapBasedMultimap.this.totalSize - collection.size();
/* 1339 */       collection.clear();
/* 1340 */       return output;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object object) {
/* 1345 */       return (this == object || this.submap.equals(object));
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1350 */       return this.submap.hashCode();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1355 */       return this.submap.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1360 */       if (this.submap == AbstractMapBasedMultimap.this.map) {
/* 1361 */         AbstractMapBasedMultimap.this.clear();
/*      */       } else {
/* 1363 */         Iterators.clear(new AsMapIterator());
/*      */       } 
/*      */     }
/*      */     
/*      */     Map.Entry<K, Collection<V>> wrapEntry(Map.Entry<K, Collection<V>> entry) {
/* 1368 */       K key = entry.getKey();
/* 1369 */       return Maps.immutableEntry(key, AbstractMapBasedMultimap.this.wrapCollection(key, entry.getValue()));
/*      */     }
/*      */     
/*      */     class AsMapEntries
/*      */       extends Maps.EntrySet<K, Collection<V>>
/*      */     {
/*      */       Map<K, Collection<V>> map() {
/* 1376 */         return AbstractMapBasedMultimap.AsMap.this;
/*      */       }
/*      */ 
/*      */       
/*      */       public Iterator<Map.Entry<K, Collection<V>>> iterator() {
/* 1381 */         return new AbstractMapBasedMultimap.AsMap.AsMapIterator();
/*      */       }
/*      */ 
/*      */       
/*      */       public Spliterator<Map.Entry<K, Collection<V>>> spliterator() {
/* 1386 */         return CollectSpliterators.map(AbstractMapBasedMultimap.AsMap.this.submap.entrySet().spliterator(), AbstractMapBasedMultimap.AsMap.this::wrapEntry);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public boolean contains(Object o) {
/* 1393 */         return Collections2.safeContains(AbstractMapBasedMultimap.AsMap.this.submap.entrySet(), o);
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean remove(Object o) {
/* 1398 */         if (!contains(o)) {
/* 1399 */           return false;
/*      */         }
/* 1401 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 1402 */         AbstractMapBasedMultimap.this.removeValuesForKey(entry.getKey());
/* 1403 */         return true;
/*      */       }
/*      */     }
/*      */     
/*      */     class AsMapIterator
/*      */       implements Iterator<Map.Entry<K, Collection<V>>> {
/* 1409 */       final Iterator<Map.Entry<K, Collection<V>>> delegateIterator = AbstractMapBasedMultimap.AsMap.this.submap.entrySet().iterator();
/*      */       
/*      */       Collection<V> collection;
/*      */       
/*      */       public boolean hasNext() {
/* 1414 */         return this.delegateIterator.hasNext();
/*      */       }
/*      */ 
/*      */       
/*      */       public Map.Entry<K, Collection<V>> next() {
/* 1419 */         Map.Entry<K, Collection<V>> entry = this.delegateIterator.next();
/* 1420 */         this.collection = entry.getValue();
/* 1421 */         return AbstractMapBasedMultimap.AsMap.this.wrapEntry(entry);
/*      */       }
/*      */ 
/*      */       
/*      */       public void remove() {
/* 1426 */         CollectPreconditions.checkRemove((this.collection != null));
/* 1427 */         this.delegateIterator.remove();
/* 1428 */         AbstractMapBasedMultimap.this.totalSize = AbstractMapBasedMultimap.this.totalSize - this.collection.size();
/* 1429 */         this.collection.clear();
/* 1430 */         this.collection = null;
/*      */       } }
/*      */   }
/*      */   
/*      */   private class SortedAsMap extends AsMap implements SortedMap<K, Collection<V>> {
/*      */     SortedSet<K> sortedKeySet;
/*      */     
/*      */     SortedAsMap(SortedMap<K, Collection<V>> submap) {
/* 1438 */       super(submap);
/*      */     }
/*      */     
/*      */     SortedMap<K, Collection<V>> sortedMap() {
/* 1442 */       return (SortedMap<K, Collection<V>>)this.submap;
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super K> comparator() {
/* 1447 */       return sortedMap().comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     public K firstKey() {
/* 1452 */       return sortedMap().firstKey();
/*      */     }
/*      */ 
/*      */     
/*      */     public K lastKey() {
/* 1457 */       return sortedMap().lastKey();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, Collection<V>> headMap(K toKey) {
/* 1462 */       return new SortedAsMap(sortedMap().headMap(toKey));
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, Collection<V>> subMap(K fromKey, K toKey) {
/* 1467 */       return new SortedAsMap(sortedMap().subMap(fromKey, toKey));
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, Collection<V>> tailMap(K fromKey) {
/* 1472 */       return new SortedAsMap(sortedMap().tailMap(fromKey));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public SortedSet<K> keySet() {
/* 1481 */       SortedSet<K> result = this.sortedKeySet;
/* 1482 */       return (result == null) ? (this.sortedKeySet = createKeySet()) : result;
/*      */     }
/*      */ 
/*      */     
/*      */     SortedSet<K> createKeySet() {
/* 1487 */       return new AbstractMapBasedMultimap.SortedKeySet(sortedMap());
/*      */     }
/*      */   }
/*      */   
/*      */   class NavigableAsMap
/*      */     extends SortedAsMap implements NavigableMap<K, Collection<V>> {
/*      */     NavigableAsMap(NavigableMap<K, Collection<V>> submap) {
/* 1494 */       super(submap);
/*      */     }
/*      */ 
/*      */     
/*      */     NavigableMap<K, Collection<V>> sortedMap() {
/* 1499 */       return (NavigableMap<K, Collection<V>>)super.sortedMap();
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, Collection<V>> lowerEntry(K key) {
/* 1504 */       Map.Entry<K, Collection<V>> entry = sortedMap().lowerEntry(key);
/* 1505 */       return (entry == null) ? null : wrapEntry(entry);
/*      */     }
/*      */ 
/*      */     
/*      */     public K lowerKey(K key) {
/* 1510 */       return sortedMap().lowerKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, Collection<V>> floorEntry(K key) {
/* 1515 */       Map.Entry<K, Collection<V>> entry = sortedMap().floorEntry(key);
/* 1516 */       return (entry == null) ? null : wrapEntry(entry);
/*      */     }
/*      */ 
/*      */     
/*      */     public K floorKey(K key) {
/* 1521 */       return sortedMap().floorKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, Collection<V>> ceilingEntry(K key) {
/* 1526 */       Map.Entry<K, Collection<V>> entry = sortedMap().ceilingEntry(key);
/* 1527 */       return (entry == null) ? null : wrapEntry(entry);
/*      */     }
/*      */ 
/*      */     
/*      */     public K ceilingKey(K key) {
/* 1532 */       return sortedMap().ceilingKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, Collection<V>> higherEntry(K key) {
/* 1537 */       Map.Entry<K, Collection<V>> entry = sortedMap().higherEntry(key);
/* 1538 */       return (entry == null) ? null : wrapEntry(entry);
/*      */     }
/*      */ 
/*      */     
/*      */     public K higherKey(K key) {
/* 1543 */       return sortedMap().higherKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, Collection<V>> firstEntry() {
/* 1548 */       Map.Entry<K, Collection<V>> entry = sortedMap().firstEntry();
/* 1549 */       return (entry == null) ? null : wrapEntry(entry);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, Collection<V>> lastEntry() {
/* 1554 */       Map.Entry<K, Collection<V>> entry = sortedMap().lastEntry();
/* 1555 */       return (entry == null) ? null : wrapEntry(entry);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, Collection<V>> pollFirstEntry() {
/* 1560 */       return pollAsMapEntry(entrySet().iterator());
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, Collection<V>> pollLastEntry() {
/* 1565 */       return pollAsMapEntry(descendingMap().entrySet().iterator());
/*      */     }
/*      */     
/*      */     Map.Entry<K, Collection<V>> pollAsMapEntry(Iterator<Map.Entry<K, Collection<V>>> entryIterator) {
/* 1569 */       if (!entryIterator.hasNext()) {
/* 1570 */         return null;
/*      */       }
/* 1572 */       Map.Entry<K, Collection<V>> entry = entryIterator.next();
/* 1573 */       Collection<V> output = AbstractMapBasedMultimap.this.createCollection();
/* 1574 */       output.addAll(entry.getValue());
/* 1575 */       entryIterator.remove();
/* 1576 */       return Maps.immutableEntry(entry.getKey(), AbstractMapBasedMultimap.this.unmodifiableCollectionSubclass(output));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, Collection<V>> descendingMap() {
/* 1581 */       return new NavigableAsMap(sortedMap().descendingMap());
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> keySet() {
/* 1586 */       return (NavigableSet<K>)super.keySet();
/*      */     }
/*      */ 
/*      */     
/*      */     NavigableSet<K> createKeySet() {
/* 1591 */       return new AbstractMapBasedMultimap.NavigableKeySet(sortedMap());
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> navigableKeySet() {
/* 1596 */       return keySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> descendingKeySet() {
/* 1601 */       return descendingMap().navigableKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, Collection<V>> subMap(K fromKey, K toKey) {
/* 1606 */       return subMap(fromKey, true, toKey, false);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableMap<K, Collection<V>> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
/* 1612 */       return new NavigableAsMap(sortedMap().subMap(fromKey, fromInclusive, toKey, toInclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, Collection<V>> headMap(K toKey) {
/* 1617 */       return headMap(toKey, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, Collection<V>> headMap(K toKey, boolean inclusive) {
/* 1622 */       return new NavigableAsMap(sortedMap().headMap(toKey, inclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, Collection<V>> tailMap(K fromKey) {
/* 1627 */       return tailMap(fromKey, true);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, Collection<V>> tailMap(K fromKey, boolean inclusive) {
/* 1632 */       return new NavigableAsMap(sortedMap().tailMap(fromKey, inclusive));
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\AbstractMapBasedMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */