/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.MoreObjects;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.primitives.Ints;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.Comparator;
/*      */ import java.util.ConcurrentModificationException;
/*      */ import java.util.Iterator;
/*      */ import java.util.NavigableSet;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
/*      */ import java.util.function.ObjIntConsumer;
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
/*      */ @GwtCompatible(emulated = true)
/*      */ public final class TreeMultiset<E>
/*      */   extends AbstractSortedMultiset<E>
/*      */   implements Serializable
/*      */ {
/*      */   private final transient Reference<AvlNode<E>> rootReference;
/*      */   private final transient GeneralRange<E> range;
/*      */   private final transient AvlNode<E> header;
/*      */   @GwtIncompatible
/*      */   private static final long serialVersionUID = 1L;
/*      */   
/*      */   public static <E extends Comparable> TreeMultiset<E> create() {
/*   75 */     return new TreeMultiset<>(Ordering.natural());
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
/*      */   public static <E> TreeMultiset<E> create(Comparator<? super E> comparator) {
/*   91 */     return (comparator == null) ? new TreeMultiset<>(
/*   92 */         Ordering.natural()) : new TreeMultiset<>(comparator);
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
/*      */   public static <E extends Comparable> TreeMultiset<E> create(Iterable<? extends E> elements) {
/*  106 */     TreeMultiset<E> multiset = create();
/*  107 */     Iterables.addAll(multiset, elements);
/*  108 */     return multiset;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   TreeMultiset(Reference<AvlNode<E>> rootReference, GeneralRange<E> range, AvlNode<E> endLink) {
/*  116 */     super(range.comparator());
/*  117 */     this.rootReference = rootReference;
/*  118 */     this.range = range;
/*  119 */     this.header = endLink;
/*      */   }
/*      */   
/*      */   TreeMultiset(Comparator<? super E> comparator) {
/*  123 */     super(comparator);
/*  124 */     this.range = GeneralRange.all(comparator);
/*  125 */     this.header = new AvlNode<>(null, 1);
/*  126 */     successor(this.header, this.header);
/*  127 */     this.rootReference = new Reference<>();
/*      */   }
/*      */   
/*      */   private enum Aggregate
/*      */   {
/*  132 */     SIZE
/*      */     {
/*      */       int nodeAggregate(TreeMultiset.AvlNode<?> node) {
/*  135 */         return node.elemCount;
/*      */       }
/*      */ 
/*      */       
/*      */       long treeAggregate(TreeMultiset.AvlNode<?> root) {
/*  140 */         return (root == null) ? 0L : root.totalCount;
/*      */       }
/*      */     },
/*  143 */     DISTINCT
/*      */     {
/*      */       int nodeAggregate(TreeMultiset.AvlNode<?> node) {
/*  146 */         return 1;
/*      */       }
/*      */ 
/*      */       
/*      */       long treeAggregate(TreeMultiset.AvlNode<?> root) {
/*  151 */         return (root == null) ? 0L : root.distinctElements;
/*      */       }
/*      */     };
/*      */     
/*      */     abstract int nodeAggregate(TreeMultiset.AvlNode<?> param1AvlNode);
/*      */     
/*      */     abstract long treeAggregate(TreeMultiset.AvlNode<?> param1AvlNode);
/*      */   }
/*      */   
/*      */   private long aggregateForEntries(Aggregate aggr) {
/*  161 */     AvlNode<E> root = this.rootReference.get();
/*  162 */     long total = aggr.treeAggregate(root);
/*  163 */     if (this.range.hasLowerBound()) {
/*  164 */       total -= aggregateBelowRange(aggr, root);
/*      */     }
/*  166 */     if (this.range.hasUpperBound()) {
/*  167 */       total -= aggregateAboveRange(aggr, root);
/*      */     }
/*  169 */     return total;
/*      */   }
/*      */   
/*      */   private long aggregateBelowRange(Aggregate aggr, AvlNode<E> node) {
/*  173 */     if (node == null) {
/*  174 */       return 0L;
/*      */     }
/*  176 */     int cmp = comparator().compare(this.range.getLowerEndpoint(), node.elem);
/*  177 */     if (cmp < 0)
/*  178 */       return aggregateBelowRange(aggr, node.left); 
/*  179 */     if (cmp == 0) {
/*  180 */       switch (this.range.getLowerBoundType()) {
/*      */         case OPEN:
/*  182 */           return aggr.nodeAggregate(node) + aggr.treeAggregate(node.left);
/*      */         case CLOSED:
/*  184 */           return aggr.treeAggregate(node.left);
/*      */       } 
/*  186 */       throw new AssertionError();
/*      */     } 
/*      */     
/*  189 */     return aggr.treeAggregate(node.left) + aggr
/*  190 */       .nodeAggregate(node) + 
/*  191 */       aggregateBelowRange(aggr, node.right);
/*      */   }
/*      */ 
/*      */   
/*      */   private long aggregateAboveRange(Aggregate aggr, AvlNode<E> node) {
/*  196 */     if (node == null) {
/*  197 */       return 0L;
/*      */     }
/*  199 */     int cmp = comparator().compare(this.range.getUpperEndpoint(), node.elem);
/*  200 */     if (cmp > 0)
/*  201 */       return aggregateAboveRange(aggr, node.right); 
/*  202 */     if (cmp == 0) {
/*  203 */       switch (this.range.getUpperBoundType()) {
/*      */         case OPEN:
/*  205 */           return aggr.nodeAggregate(node) + aggr.treeAggregate(node.right);
/*      */         case CLOSED:
/*  207 */           return aggr.treeAggregate(node.right);
/*      */       } 
/*  209 */       throw new AssertionError();
/*      */     } 
/*      */     
/*  212 */     return aggr.treeAggregate(node.right) + aggr
/*  213 */       .nodeAggregate(node) + 
/*  214 */       aggregateAboveRange(aggr, node.left);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int size() {
/*  220 */     return Ints.saturatedCast(aggregateForEntries(Aggregate.SIZE));
/*      */   }
/*      */ 
/*      */   
/*      */   int distinctElements() {
/*  225 */     return Ints.saturatedCast(aggregateForEntries(Aggregate.DISTINCT));
/*      */   }
/*      */   
/*      */   static int distinctElements(AvlNode<?> node) {
/*  229 */     return (node == null) ? 0 : node.distinctElements;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int count(Object element) {
/*      */     try {
/*  236 */       E e = (E)element;
/*  237 */       AvlNode<E> root = this.rootReference.get();
/*  238 */       if (!this.range.contains(e) || root == null) {
/*  239 */         return 0;
/*      */       }
/*  241 */       return root.count(comparator(), e);
/*  242 */     } catch (ClassCastException|NullPointerException e) {
/*  243 */       return 0;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public int add(E element, int occurrences) {
/*  250 */     CollectPreconditions.checkNonnegative(occurrences, "occurrences");
/*  251 */     if (occurrences == 0) {
/*  252 */       return count(element);
/*      */     }
/*  254 */     Preconditions.checkArgument(this.range.contains(element));
/*  255 */     AvlNode<E> root = this.rootReference.get();
/*  256 */     if (root == null) {
/*  257 */       comparator().compare(element, element);
/*  258 */       AvlNode<E> avlNode = new AvlNode<>(element, occurrences);
/*  259 */       successor(this.header, avlNode, this.header);
/*  260 */       this.rootReference.checkAndSet(root, avlNode);
/*  261 */       return 0;
/*      */     } 
/*  263 */     int[] result = new int[1];
/*  264 */     AvlNode<E> newRoot = root.add(comparator(), element, occurrences, result);
/*  265 */     this.rootReference.checkAndSet(root, newRoot);
/*  266 */     return result[0];
/*      */   }
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public int remove(Object element, int occurrences) {
/*      */     AvlNode<E> newRoot;
/*  272 */     CollectPreconditions.checkNonnegative(occurrences, "occurrences");
/*  273 */     if (occurrences == 0) {
/*  274 */       return count(element);
/*      */     }
/*  276 */     AvlNode<E> root = this.rootReference.get();
/*  277 */     int[] result = new int[1];
/*      */ 
/*      */     
/*      */     try {
/*  281 */       E e = (E)element;
/*  282 */       if (!this.range.contains(e) || root == null) {
/*  283 */         return 0;
/*      */       }
/*  285 */       newRoot = root.remove(comparator(), e, occurrences, result);
/*  286 */     } catch (ClassCastException|NullPointerException e) {
/*  287 */       return 0;
/*      */     } 
/*  289 */     this.rootReference.checkAndSet(root, newRoot);
/*  290 */     return result[0];
/*      */   }
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public int setCount(E element, int count) {
/*  296 */     CollectPreconditions.checkNonnegative(count, "count");
/*  297 */     if (!this.range.contains(element)) {
/*  298 */       Preconditions.checkArgument((count == 0));
/*  299 */       return 0;
/*      */     } 
/*      */     
/*  302 */     AvlNode<E> root = this.rootReference.get();
/*  303 */     if (root == null) {
/*  304 */       if (count > 0) {
/*  305 */         add(element, count);
/*      */       }
/*  307 */       return 0;
/*      */     } 
/*  309 */     int[] result = new int[1];
/*  310 */     AvlNode<E> newRoot = root.setCount(comparator(), element, count, result);
/*  311 */     this.rootReference.checkAndSet(root, newRoot);
/*  312 */     return result[0];
/*      */   }
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public boolean setCount(E element, int oldCount, int newCount) {
/*  318 */     CollectPreconditions.checkNonnegative(newCount, "newCount");
/*  319 */     CollectPreconditions.checkNonnegative(oldCount, "oldCount");
/*  320 */     Preconditions.checkArgument(this.range.contains(element));
/*      */     
/*  322 */     AvlNode<E> root = this.rootReference.get();
/*  323 */     if (root == null) {
/*  324 */       if (oldCount == 0) {
/*  325 */         if (newCount > 0) {
/*  326 */           add(element, newCount);
/*      */         }
/*  328 */         return true;
/*      */       } 
/*  330 */       return false;
/*      */     } 
/*      */     
/*  333 */     int[] result = new int[1];
/*  334 */     AvlNode<E> newRoot = root.setCount(comparator(), element, oldCount, newCount, result);
/*  335 */     this.rootReference.checkAndSet(root, newRoot);
/*  336 */     return (result[0] == oldCount);
/*      */   }
/*      */ 
/*      */   
/*      */   public void clear() {
/*  341 */     if (!this.range.hasLowerBound() && !this.range.hasUpperBound()) {
/*      */       
/*  343 */       for (AvlNode<E> current = this.header.succ; current != this.header; ) {
/*  344 */         AvlNode<E> next = current.succ;
/*      */         
/*  346 */         current.elemCount = 0;
/*      */         
/*  348 */         current.left = null;
/*  349 */         current.right = null;
/*  350 */         current.pred = null;
/*  351 */         current.succ = null;
/*      */         
/*  353 */         current = next;
/*      */       } 
/*  355 */       successor(this.header, this.header);
/*  356 */       this.rootReference.clear();
/*      */     } else {
/*      */       
/*  359 */       Iterators.clear(entryIterator());
/*      */     } 
/*      */   }
/*      */   
/*      */   private Multiset.Entry<E> wrapEntry(final AvlNode<E> baseEntry) {
/*  364 */     return new Multisets.AbstractEntry<E>()
/*      */       {
/*      */         public E getElement() {
/*  367 */           return baseEntry.getElement();
/*      */         }
/*      */ 
/*      */         
/*      */         public int getCount() {
/*  372 */           int result = baseEntry.getCount();
/*  373 */           if (result == 0) {
/*  374 */             return TreeMultiset.this.count(getElement());
/*      */           }
/*  376 */           return result;
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private AvlNode<E> firstNode() {
/*  384 */     AvlNode<E> node, root = this.rootReference.get();
/*  385 */     if (root == null) {
/*  386 */       return null;
/*      */     }
/*      */     
/*  389 */     if (this.range.hasLowerBound()) {
/*  390 */       E endpoint = this.range.getLowerEndpoint();
/*  391 */       node = ((AvlNode)this.rootReference.get()).ceiling(comparator(), endpoint);
/*  392 */       if (node == null) {
/*  393 */         return null;
/*      */       }
/*  395 */       if (this.range.getLowerBoundType() == BoundType.OPEN && 
/*  396 */         comparator().compare(endpoint, node.getElement()) == 0) {
/*  397 */         node = node.succ;
/*      */       }
/*      */     } else {
/*  400 */       node = this.header.succ;
/*      */     } 
/*  402 */     return (node == this.header || !this.range.contains(node.getElement())) ? null : node;
/*      */   }
/*      */   
/*      */   private AvlNode<E> lastNode() {
/*  406 */     AvlNode<E> node, root = this.rootReference.get();
/*  407 */     if (root == null) {
/*  408 */       return null;
/*      */     }
/*      */     
/*  411 */     if (this.range.hasUpperBound()) {
/*  412 */       E endpoint = this.range.getUpperEndpoint();
/*  413 */       node = ((AvlNode)this.rootReference.get()).floor(comparator(), endpoint);
/*  414 */       if (node == null) {
/*  415 */         return null;
/*      */       }
/*  417 */       if (this.range.getUpperBoundType() == BoundType.OPEN && 
/*  418 */         comparator().compare(endpoint, node.getElement()) == 0) {
/*  419 */         node = node.pred;
/*      */       }
/*      */     } else {
/*  422 */       node = this.header.pred;
/*      */     } 
/*  424 */     return (node == this.header || !this.range.contains(node.getElement())) ? null : node;
/*      */   }
/*      */ 
/*      */   
/*      */   Iterator<E> elementIterator() {
/*  429 */     return Multisets.elementIterator(entryIterator());
/*      */   }
/*      */ 
/*      */   
/*      */   Iterator<Multiset.Entry<E>> entryIterator() {
/*  434 */     return (Iterator)new Iterator<Multiset.Entry<Multiset.Entry<E>>>() {
/*  435 */         TreeMultiset.AvlNode<E> current = TreeMultiset.this.firstNode();
/*      */         
/*      */         Multiset.Entry<E> prevEntry;
/*      */         
/*      */         public boolean hasNext() {
/*  440 */           if (this.current == null)
/*  441 */             return false; 
/*  442 */           if (TreeMultiset.this.range.tooHigh(this.current.getElement())) {
/*  443 */             this.current = null;
/*  444 */             return false;
/*      */           } 
/*  446 */           return true;
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*      */         public Multiset.Entry<E> next() {
/*  452 */           if (!hasNext()) {
/*  453 */             throw new NoSuchElementException();
/*      */           }
/*  455 */           Multiset.Entry<E> result = TreeMultiset.this.wrapEntry(this.current);
/*  456 */           this.prevEntry = result;
/*  457 */           if (this.current.succ == TreeMultiset.this.header) {
/*  458 */             this.current = null;
/*      */           } else {
/*  460 */             this.current = this.current.succ;
/*      */           } 
/*  462 */           return result;
/*      */         }
/*      */ 
/*      */         
/*      */         public void remove() {
/*  467 */           CollectPreconditions.checkRemove((this.prevEntry != null));
/*  468 */           TreeMultiset.this.setCount(this.prevEntry.getElement(), 0);
/*  469 */           this.prevEntry = null;
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   Iterator<Multiset.Entry<E>> descendingEntryIterator() {
/*  476 */     return (Iterator)new Iterator<Multiset.Entry<Multiset.Entry<E>>>() {
/*  477 */         TreeMultiset.AvlNode<E> current = TreeMultiset.this.lastNode();
/*  478 */         Multiset.Entry<E> prevEntry = null;
/*      */ 
/*      */         
/*      */         public boolean hasNext() {
/*  482 */           if (this.current == null)
/*  483 */             return false; 
/*  484 */           if (TreeMultiset.this.range.tooLow(this.current.getElement())) {
/*  485 */             this.current = null;
/*  486 */             return false;
/*      */           } 
/*  488 */           return true;
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*      */         public Multiset.Entry<E> next() {
/*  494 */           if (!hasNext()) {
/*  495 */             throw new NoSuchElementException();
/*      */           }
/*  497 */           Multiset.Entry<E> result = TreeMultiset.this.wrapEntry(this.current);
/*  498 */           this.prevEntry = result;
/*  499 */           if (this.current.pred == TreeMultiset.this.header) {
/*  500 */             this.current = null;
/*      */           } else {
/*  502 */             this.current = this.current.pred;
/*      */           } 
/*  504 */           return result;
/*      */         }
/*      */ 
/*      */         
/*      */         public void remove() {
/*  509 */           CollectPreconditions.checkRemove((this.prevEntry != null));
/*  510 */           TreeMultiset.this.setCount(this.prevEntry.getElement(), 0);
/*  511 */           this.prevEntry = null;
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   public void forEachEntry(ObjIntConsumer<? super E> action) {
/*  518 */     Preconditions.checkNotNull(action);
/*  519 */     AvlNode<E> node = firstNode();
/*  520 */     for (; node != this.header && node != null && !this.range.tooHigh(node.getElement()); 
/*  521 */       node = node.succ) {
/*  522 */       action.accept(node.getElement(), node.getCount());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public Iterator<E> iterator() {
/*  528 */     return Multisets.iteratorImpl(this);
/*      */   }
/*      */ 
/*      */   
/*      */   public SortedMultiset<E> headMultiset(E upperBound, BoundType boundType) {
/*  533 */     return new TreeMultiset(this.rootReference, this.range
/*      */         
/*  535 */         .intersect(GeneralRange.upTo(comparator(), upperBound, boundType)), this.header);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public SortedMultiset<E> tailMultiset(E lowerBound, BoundType boundType) {
/*  541 */     return new TreeMultiset(this.rootReference, this.range
/*      */         
/*  543 */         .intersect(GeneralRange.downTo(comparator(), lowerBound, boundType)), this.header);
/*      */   }
/*      */   
/*      */   private static final class Reference<T>
/*      */   {
/*      */     private T value;
/*      */     
/*      */     public T get() {
/*  551 */       return this.value;
/*      */     }
/*      */     private Reference() {}
/*      */     public void checkAndSet(T expected, T newValue) {
/*  555 */       if (this.value != expected) {
/*  556 */         throw new ConcurrentModificationException();
/*      */       }
/*  558 */       this.value = newValue;
/*      */     }
/*      */     
/*      */     void clear() {
/*  562 */       this.value = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static final class AvlNode<E>
/*      */   {
/*      */     private final E elem;
/*      */     
/*      */     private int elemCount;
/*      */     private int distinctElements;
/*      */     private long totalCount;
/*      */     private int height;
/*      */     private AvlNode<E> left;
/*      */     private AvlNode<E> right;
/*      */     private AvlNode<E> pred;
/*      */     private AvlNode<E> succ;
/*      */     
/*      */     AvlNode(E elem, int elemCount) {
/*  581 */       Preconditions.checkArgument((elemCount > 0));
/*  582 */       this.elem = elem;
/*  583 */       this.elemCount = elemCount;
/*  584 */       this.totalCount = elemCount;
/*  585 */       this.distinctElements = 1;
/*  586 */       this.height = 1;
/*  587 */       this.left = null;
/*  588 */       this.right = null;
/*      */     }
/*      */     
/*      */     public int count(Comparator<? super E> comparator, E e) {
/*  592 */       int cmp = comparator.compare(e, this.elem);
/*  593 */       if (cmp < 0)
/*  594 */         return (this.left == null) ? 0 : this.left.count(comparator, e); 
/*  595 */       if (cmp > 0) {
/*  596 */         return (this.right == null) ? 0 : this.right.count(comparator, e);
/*      */       }
/*  598 */       return this.elemCount;
/*      */     }
/*      */ 
/*      */     
/*      */     private AvlNode<E> addRightChild(E e, int count) {
/*  603 */       this.right = new AvlNode(e, count);
/*  604 */       TreeMultiset.successor(this, this.right, this.succ);
/*  605 */       this.height = Math.max(2, this.height);
/*  606 */       this.distinctElements++;
/*  607 */       this.totalCount += count;
/*  608 */       return this;
/*      */     }
/*      */     
/*      */     private AvlNode<E> addLeftChild(E e, int count) {
/*  612 */       this.left = new AvlNode(e, count);
/*  613 */       TreeMultiset.successor(this.pred, this.left, this);
/*  614 */       this.height = Math.max(2, this.height);
/*  615 */       this.distinctElements++;
/*  616 */       this.totalCount += count;
/*  617 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     AvlNode<E> add(Comparator<? super E> comparator, E e, int count, int[] result) {
/*  625 */       int cmp = comparator.compare(e, this.elem);
/*  626 */       if (cmp < 0) {
/*  627 */         AvlNode<E> initLeft = this.left;
/*  628 */         if (initLeft == null) {
/*  629 */           result[0] = 0;
/*  630 */           return addLeftChild(e, count);
/*      */         } 
/*  632 */         int initHeight = initLeft.height;
/*      */         
/*  634 */         this.left = initLeft.add(comparator, e, count, result);
/*  635 */         if (result[0] == 0) {
/*  636 */           this.distinctElements++;
/*      */         }
/*  638 */         this.totalCount += count;
/*  639 */         return (this.left.height == initHeight) ? this : rebalance();
/*  640 */       }  if (cmp > 0) {
/*  641 */         AvlNode<E> initRight = this.right;
/*  642 */         if (initRight == null) {
/*  643 */           result[0] = 0;
/*  644 */           return addRightChild(e, count);
/*      */         } 
/*  646 */         int initHeight = initRight.height;
/*      */         
/*  648 */         this.right = initRight.add(comparator, e, count, result);
/*  649 */         if (result[0] == 0) {
/*  650 */           this.distinctElements++;
/*      */         }
/*  652 */         this.totalCount += count;
/*  653 */         return (this.right.height == initHeight) ? this : rebalance();
/*      */       } 
/*      */ 
/*      */       
/*  657 */       result[0] = this.elemCount;
/*  658 */       long resultCount = this.elemCount + count;
/*  659 */       Preconditions.checkArgument((resultCount <= 2147483647L));
/*  660 */       this.elemCount += count;
/*  661 */       this.totalCount += count;
/*  662 */       return this;
/*      */     }
/*      */     
/*      */     AvlNode<E> remove(Comparator<? super E> comparator, E e, int count, int[] result) {
/*  666 */       int cmp = comparator.compare(e, this.elem);
/*  667 */       if (cmp < 0) {
/*  668 */         AvlNode<E> initLeft = this.left;
/*  669 */         if (initLeft == null) {
/*  670 */           result[0] = 0;
/*  671 */           return this;
/*      */         } 
/*      */         
/*  674 */         this.left = initLeft.remove(comparator, e, count, result);
/*      */         
/*  676 */         if (result[0] > 0) {
/*  677 */           if (count >= result[0]) {
/*  678 */             this.distinctElements--;
/*  679 */             this.totalCount -= result[0];
/*      */           } else {
/*  681 */             this.totalCount -= count;
/*      */           } 
/*      */         }
/*  684 */         return (result[0] == 0) ? this : rebalance();
/*  685 */       }  if (cmp > 0) {
/*  686 */         AvlNode<E> initRight = this.right;
/*  687 */         if (initRight == null) {
/*  688 */           result[0] = 0;
/*  689 */           return this;
/*      */         } 
/*      */         
/*  692 */         this.right = initRight.remove(comparator, e, count, result);
/*      */         
/*  694 */         if (result[0] > 0) {
/*  695 */           if (count >= result[0]) {
/*  696 */             this.distinctElements--;
/*  697 */             this.totalCount -= result[0];
/*      */           } else {
/*  699 */             this.totalCount -= count;
/*      */           } 
/*      */         }
/*  702 */         return rebalance();
/*      */       } 
/*      */ 
/*      */       
/*  706 */       result[0] = this.elemCount;
/*  707 */       if (count >= this.elemCount) {
/*  708 */         return deleteMe();
/*      */       }
/*  710 */       this.elemCount -= count;
/*  711 */       this.totalCount -= count;
/*  712 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     AvlNode<E> setCount(Comparator<? super E> comparator, E e, int count, int[] result) {
/*  717 */       int cmp = comparator.compare(e, this.elem);
/*  718 */       if (cmp < 0) {
/*  719 */         AvlNode<E> initLeft = this.left;
/*  720 */         if (initLeft == null) {
/*  721 */           result[0] = 0;
/*  722 */           return (count > 0) ? addLeftChild(e, count) : this;
/*      */         } 
/*      */         
/*  725 */         this.left = initLeft.setCount(comparator, e, count, result);
/*      */         
/*  727 */         if (count == 0 && result[0] != 0) {
/*  728 */           this.distinctElements--;
/*  729 */         } else if (count > 0 && result[0] == 0) {
/*  730 */           this.distinctElements++;
/*      */         } 
/*      */         
/*  733 */         this.totalCount += (count - result[0]);
/*  734 */         return rebalance();
/*  735 */       }  if (cmp > 0) {
/*  736 */         AvlNode<E> initRight = this.right;
/*  737 */         if (initRight == null) {
/*  738 */           result[0] = 0;
/*  739 */           return (count > 0) ? addRightChild(e, count) : this;
/*      */         } 
/*      */         
/*  742 */         this.right = initRight.setCount(comparator, e, count, result);
/*      */         
/*  744 */         if (count == 0 && result[0] != 0) {
/*  745 */           this.distinctElements--;
/*  746 */         } else if (count > 0 && result[0] == 0) {
/*  747 */           this.distinctElements++;
/*      */         } 
/*      */         
/*  750 */         this.totalCount += (count - result[0]);
/*  751 */         return rebalance();
/*      */       } 
/*      */ 
/*      */       
/*  755 */       result[0] = this.elemCount;
/*  756 */       if (count == 0) {
/*  757 */         return deleteMe();
/*      */       }
/*  759 */       this.totalCount += (count - this.elemCount);
/*  760 */       this.elemCount = count;
/*  761 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     AvlNode<E> setCount(Comparator<? super E> comparator, E e, int expectedCount, int newCount, int[] result) {
/*  770 */       int cmp = comparator.compare(e, this.elem);
/*  771 */       if (cmp < 0) {
/*  772 */         AvlNode<E> initLeft = this.left;
/*  773 */         if (initLeft == null) {
/*  774 */           result[0] = 0;
/*  775 */           if (expectedCount == 0 && newCount > 0) {
/*  776 */             return addLeftChild(e, newCount);
/*      */           }
/*  778 */           return this;
/*      */         } 
/*      */         
/*  781 */         this.left = initLeft.setCount(comparator, e, expectedCount, newCount, result);
/*      */         
/*  783 */         if (result[0] == expectedCount) {
/*  784 */           if (newCount == 0 && result[0] != 0) {
/*  785 */             this.distinctElements--;
/*  786 */           } else if (newCount > 0 && result[0] == 0) {
/*  787 */             this.distinctElements++;
/*      */           } 
/*  789 */           this.totalCount += (newCount - result[0]);
/*      */         } 
/*  791 */         return rebalance();
/*  792 */       }  if (cmp > 0) {
/*  793 */         AvlNode<E> initRight = this.right;
/*  794 */         if (initRight == null) {
/*  795 */           result[0] = 0;
/*  796 */           if (expectedCount == 0 && newCount > 0) {
/*  797 */             return addRightChild(e, newCount);
/*      */           }
/*  799 */           return this;
/*      */         } 
/*      */         
/*  802 */         this.right = initRight.setCount(comparator, e, expectedCount, newCount, result);
/*      */         
/*  804 */         if (result[0] == expectedCount) {
/*  805 */           if (newCount == 0 && result[0] != 0) {
/*  806 */             this.distinctElements--;
/*  807 */           } else if (newCount > 0 && result[0] == 0) {
/*  808 */             this.distinctElements++;
/*      */           } 
/*  810 */           this.totalCount += (newCount - result[0]);
/*      */         } 
/*  812 */         return rebalance();
/*      */       } 
/*      */ 
/*      */       
/*  816 */       result[0] = this.elemCount;
/*  817 */       if (expectedCount == this.elemCount) {
/*  818 */         if (newCount == 0) {
/*  819 */           return deleteMe();
/*      */         }
/*  821 */         this.totalCount += (newCount - this.elemCount);
/*  822 */         this.elemCount = newCount;
/*      */       } 
/*  824 */       return this;
/*      */     }
/*      */     
/*      */     private AvlNode<E> deleteMe() {
/*  828 */       int oldElemCount = this.elemCount;
/*  829 */       this.elemCount = 0;
/*  830 */       TreeMultiset.successor(this.pred, this.succ);
/*  831 */       if (this.left == null)
/*  832 */         return this.right; 
/*  833 */       if (this.right == null)
/*  834 */         return this.left; 
/*  835 */       if (this.left.height >= this.right.height) {
/*  836 */         AvlNode<E> avlNode = this.pred;
/*      */         
/*  838 */         avlNode.left = this.left.removeMax(avlNode);
/*  839 */         avlNode.right = this.right;
/*  840 */         this.distinctElements--;
/*  841 */         this.totalCount -= oldElemCount;
/*  842 */         return avlNode.rebalance();
/*      */       } 
/*  844 */       AvlNode<E> newTop = this.succ;
/*  845 */       newTop.right = this.right.removeMin(newTop);
/*  846 */       newTop.left = this.left;
/*  847 */       this.distinctElements--;
/*  848 */       this.totalCount -= oldElemCount;
/*  849 */       return newTop.rebalance();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private AvlNode<E> removeMin(AvlNode<E> node) {
/*  855 */       if (this.left == null) {
/*  856 */         return this.right;
/*      */       }
/*  858 */       this.left = this.left.removeMin(node);
/*  859 */       this.distinctElements--;
/*  860 */       this.totalCount -= node.elemCount;
/*  861 */       return rebalance();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private AvlNode<E> removeMax(AvlNode<E> node) {
/*  867 */       if (this.right == null) {
/*  868 */         return this.left;
/*      */       }
/*  870 */       this.right = this.right.removeMax(node);
/*  871 */       this.distinctElements--;
/*  872 */       this.totalCount -= node.elemCount;
/*  873 */       return rebalance();
/*      */     }
/*      */ 
/*      */     
/*      */     private void recomputeMultiset() {
/*  878 */       this
/*  879 */         .distinctElements = 1 + TreeMultiset.distinctElements(this.left) + TreeMultiset.distinctElements(this.right);
/*  880 */       this.totalCount = this.elemCount + totalCount(this.left) + totalCount(this.right);
/*      */     }
/*      */     
/*      */     private void recomputeHeight() {
/*  884 */       this.height = 1 + Math.max(height(this.left), height(this.right));
/*      */     }
/*      */     
/*      */     private void recompute() {
/*  888 */       recomputeMultiset();
/*  889 */       recomputeHeight();
/*      */     }
/*      */     
/*      */     private AvlNode<E> rebalance() {
/*  893 */       switch (balanceFactor()) {
/*      */         case -2:
/*  895 */           if (this.right.balanceFactor() > 0) {
/*  896 */             this.right = this.right.rotateRight();
/*      */           }
/*  898 */           return rotateLeft();
/*      */         case 2:
/*  900 */           if (this.left.balanceFactor() < 0) {
/*  901 */             this.left = this.left.rotateLeft();
/*      */           }
/*  903 */           return rotateRight();
/*      */       } 
/*  905 */       recomputeHeight();
/*  906 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     private int balanceFactor() {
/*  911 */       return height(this.left) - height(this.right);
/*      */     }
/*      */     
/*      */     private AvlNode<E> rotateLeft() {
/*  915 */       Preconditions.checkState((this.right != null));
/*  916 */       AvlNode<E> newTop = this.right;
/*  917 */       this.right = newTop.left;
/*  918 */       newTop.left = this;
/*  919 */       newTop.totalCount = this.totalCount;
/*  920 */       newTop.distinctElements = this.distinctElements;
/*  921 */       recompute();
/*  922 */       newTop.recomputeHeight();
/*  923 */       return newTop;
/*      */     }
/*      */     
/*      */     private AvlNode<E> rotateRight() {
/*  927 */       Preconditions.checkState((this.left != null));
/*  928 */       AvlNode<E> newTop = this.left;
/*  929 */       this.left = newTop.right;
/*  930 */       newTop.right = this;
/*  931 */       newTop.totalCount = this.totalCount;
/*  932 */       newTop.distinctElements = this.distinctElements;
/*  933 */       recompute();
/*  934 */       newTop.recomputeHeight();
/*  935 */       return newTop;
/*      */     }
/*      */     
/*      */     private static long totalCount(AvlNode<?> node) {
/*  939 */       return (node == null) ? 0L : node.totalCount;
/*      */     }
/*      */     
/*      */     private static int height(AvlNode<?> node) {
/*  943 */       return (node == null) ? 0 : node.height;
/*      */     }
/*      */     
/*      */     private AvlNode<E> ceiling(Comparator<? super E> comparator, E e) {
/*  947 */       int cmp = comparator.compare(e, this.elem);
/*  948 */       if (cmp < 0)
/*  949 */         return (this.left == null) ? this : (AvlNode<E>)MoreObjects.firstNonNull(this.left.ceiling(comparator, e), this); 
/*  950 */       if (cmp == 0) {
/*  951 */         return this;
/*      */       }
/*  953 */       return (this.right == null) ? null : this.right.ceiling(comparator, e);
/*      */     }
/*      */ 
/*      */     
/*      */     private AvlNode<E> floor(Comparator<? super E> comparator, E e) {
/*  958 */       int cmp = comparator.compare(e, this.elem);
/*  959 */       if (cmp > 0)
/*  960 */         return (this.right == null) ? this : (AvlNode<E>)MoreObjects.firstNonNull(this.right.floor(comparator, e), this); 
/*  961 */       if (cmp == 0) {
/*  962 */         return this;
/*      */       }
/*  964 */       return (this.left == null) ? null : this.left.floor(comparator, e);
/*      */     }
/*      */ 
/*      */     
/*      */     E getElement() {
/*  969 */       return this.elem;
/*      */     }
/*      */     
/*      */     int getCount() {
/*  973 */       return this.elemCount;
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  978 */       return Multisets.<E>immutableEntry(getElement(), getCount()).toString();
/*      */     }
/*      */   }
/*      */   
/*      */   private static <T> void successor(AvlNode<T> a, AvlNode<T> b) {
/*  983 */     a.succ = b;
/*  984 */     b.pred = a;
/*      */   }
/*      */   
/*      */   private static <T> void successor(AvlNode<T> a, AvlNode<T> b, AvlNode<T> c) {
/*  988 */     successor(a, b);
/*  989 */     successor(b, c);
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
/*      */   @GwtIncompatible
/*      */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 1004 */     stream.defaultWriteObject();
/* 1005 */     stream.writeObject(elementSet().comparator());
/* 1006 */     Serialization.writeMultiset(this, stream);
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 1011 */     stream.defaultReadObject();
/*      */ 
/*      */     
/* 1014 */     Comparator<? super E> comparator = (Comparator<? super E>)stream.readObject();
/* 1015 */     Serialization.<AbstractSortedMultiset>getFieldSetter(AbstractSortedMultiset.class, "comparator").set(this, comparator);
/* 1016 */     Serialization.<TreeMultiset<E>>getFieldSetter((Class)TreeMultiset.class, "range")
/* 1017 */       .set(this, GeneralRange.all(comparator));
/* 1018 */     Serialization.<TreeMultiset<E>>getFieldSetter((Class)TreeMultiset.class, "rootReference")
/* 1019 */       .set(this, new Reference());
/* 1020 */     AvlNode<E> header = new AvlNode<>(null, 1);
/* 1021 */     Serialization.<TreeMultiset<E>>getFieldSetter((Class)TreeMultiset.class, "header").set(this, header);
/* 1022 */     successor(header, header);
/* 1023 */     Serialization.populateMultiset(this, stream);
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\TreeMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */