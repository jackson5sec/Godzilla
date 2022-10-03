/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.AbstractIterator;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Deque;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ public abstract class Traverser<N>
/*     */ {
/*     */   public static <N> Traverser<N> forGraph(SuccessorsFunction<N> graph) {
/*  92 */     Preconditions.checkNotNull(graph);
/*  93 */     return new GraphTraverser<>(graph);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <N> Traverser<N> forTree(SuccessorsFunction<N> tree) {
/* 170 */     Preconditions.checkNotNull(tree);
/* 171 */     if (tree instanceof BaseGraph) {
/* 172 */       Preconditions.checkArgument(((BaseGraph)tree).isDirected(), "Undirected graphs can never be trees.");
/*     */     }
/* 174 */     if (tree instanceof Network) {
/* 175 */       Preconditions.checkArgument(((Network)tree).isDirected(), "Undirected networks can never be trees.");
/*     */     }
/* 177 */     return new TreeTraverser<>(tree);
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
/*     */   private Traverser() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Iterable<N> breadthFirst(N paramN);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Iterable<N> breadthFirst(Iterable<? extends N> paramIterable);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Iterable<N> depthFirstPreOrder(N paramN);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Iterable<N> depthFirstPreOrder(Iterable<? extends N> paramIterable);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Iterable<N> depthFirstPostOrder(N paramN);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Iterable<N> depthFirstPostOrder(Iterable<? extends N> paramIterable);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class GraphTraverser<N>
/*     */     extends Traverser<N>
/*     */   {
/*     */     private final SuccessorsFunction<N> graph;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     GraphTraverser(SuccessorsFunction<N> graph) {
/* 322 */       this.graph = (SuccessorsFunction<N>)Preconditions.checkNotNull(graph);
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterable<N> breadthFirst(N startNode) {
/* 327 */       Preconditions.checkNotNull(startNode);
/* 328 */       return breadthFirst((Iterable<? extends N>)ImmutableSet.of(startNode));
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterable<N> breadthFirst(final Iterable<? extends N> startNodes) {
/* 333 */       Preconditions.checkNotNull(startNodes);
/* 334 */       if (Iterables.isEmpty(startNodes)) {
/* 335 */         return (Iterable<N>)ImmutableSet.of();
/*     */       }
/* 337 */       for (N startNode : startNodes) {
/* 338 */         checkThatNodeIsInGraph(startNode);
/*     */       }
/* 340 */       return new Iterable<N>()
/*     */         {
/*     */           public Iterator<N> iterator() {
/* 343 */             return (Iterator<N>)new Traverser.GraphTraverser.BreadthFirstIterator(startNodes);
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterable<N> depthFirstPreOrder(N startNode) {
/* 350 */       Preconditions.checkNotNull(startNode);
/* 351 */       return depthFirstPreOrder((Iterable<? extends N>)ImmutableSet.of(startNode));
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterable<N> depthFirstPreOrder(final Iterable<? extends N> startNodes) {
/* 356 */       Preconditions.checkNotNull(startNodes);
/* 357 */       if (Iterables.isEmpty(startNodes)) {
/* 358 */         return (Iterable<N>)ImmutableSet.of();
/*     */       }
/* 360 */       for (N startNode : startNodes) {
/* 361 */         checkThatNodeIsInGraph(startNode);
/*     */       }
/* 363 */       return new Iterable<N>()
/*     */         {
/*     */           public Iterator<N> iterator() {
/* 366 */             return (Iterator<N>)new Traverser.GraphTraverser.DepthFirstIterator(startNodes, Traverser.Order.PREORDER);
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterable<N> depthFirstPostOrder(N startNode) {
/* 373 */       Preconditions.checkNotNull(startNode);
/* 374 */       return depthFirstPostOrder((Iterable<? extends N>)ImmutableSet.of(startNode));
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterable<N> depthFirstPostOrder(final Iterable<? extends N> startNodes) {
/* 379 */       Preconditions.checkNotNull(startNodes);
/* 380 */       if (Iterables.isEmpty(startNodes)) {
/* 381 */         return (Iterable<N>)ImmutableSet.of();
/*     */       }
/* 383 */       for (N startNode : startNodes) {
/* 384 */         checkThatNodeIsInGraph(startNode);
/*     */       }
/* 386 */       return new Iterable<N>()
/*     */         {
/*     */           public Iterator<N> iterator() {
/* 389 */             return (Iterator<N>)new Traverser.GraphTraverser.DepthFirstIterator(startNodes, Traverser.Order.POSTORDER);
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void checkThatNodeIsInGraph(N startNode) {
/* 398 */       this.graph.successors(startNode);
/*     */     }
/*     */     
/*     */     private final class BreadthFirstIterator extends UnmodifiableIterator<N> {
/* 402 */       private final Queue<N> queue = new ArrayDeque<>();
/* 403 */       private final Set<N> visited = new HashSet<>();
/*     */       
/*     */       BreadthFirstIterator(Iterable<? extends N> roots) {
/* 406 */         for (N root : roots) {
/*     */           
/* 408 */           if (this.visited.add(root)) {
/* 409 */             this.queue.add(root);
/*     */           }
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean hasNext() {
/* 416 */         return !this.queue.isEmpty();
/*     */       }
/*     */ 
/*     */       
/*     */       public N next() {
/* 421 */         N current = this.queue.remove();
/* 422 */         for (N neighbor : Traverser.GraphTraverser.this.graph.successors(current)) {
/* 423 */           if (this.visited.add(neighbor)) {
/* 424 */             this.queue.add(neighbor);
/*     */           }
/*     */         } 
/* 427 */         return current;
/*     */       }
/*     */     }
/*     */     
/*     */     private final class DepthFirstIterator extends AbstractIterator<N> {
/* 432 */       private final Deque<NodeAndSuccessors> stack = new ArrayDeque<>();
/* 433 */       private final Set<N> visited = new HashSet<>();
/*     */       private final Traverser.Order order;
/*     */       
/*     */       DepthFirstIterator(Iterable<? extends N> roots, Traverser.Order order) {
/* 437 */         this.stack.push(new NodeAndSuccessors(null, roots));
/* 438 */         this.order = order;
/*     */       }
/*     */ 
/*     */       
/*     */       protected N computeNext() {
/*     */         while (true) {
/* 444 */           if (this.stack.isEmpty()) {
/* 445 */             return (N)endOfData();
/*     */           }
/* 447 */           NodeAndSuccessors nodeAndSuccessors = this.stack.getFirst();
/* 448 */           boolean firstVisit = this.visited.add(nodeAndSuccessors.node);
/* 449 */           boolean lastVisit = !nodeAndSuccessors.successorIterator.hasNext();
/* 450 */           boolean produceNode = ((firstVisit && this.order == Traverser.Order.PREORDER) || (lastVisit && this.order == Traverser.Order.POSTORDER));
/*     */           
/* 452 */           if (lastVisit) {
/* 453 */             this.stack.pop();
/*     */           } else {
/*     */             
/* 456 */             N successor = nodeAndSuccessors.successorIterator.next();
/* 457 */             if (!this.visited.contains(successor)) {
/* 458 */               this.stack.push(withSuccessors(successor));
/*     */             }
/*     */           } 
/* 461 */           if (produceNode && nodeAndSuccessors.node != null) {
/* 462 */             return nodeAndSuccessors.node;
/*     */           }
/*     */         } 
/*     */       }
/*     */       
/*     */       NodeAndSuccessors withSuccessors(N node) {
/* 468 */         return new NodeAndSuccessors(node, Traverser.GraphTraverser.this.graph.successors(node));
/*     */       }
/*     */       
/*     */       private final class NodeAndSuccessors
/*     */       {
/*     */         final N node;
/*     */         final Iterator<? extends N> successorIterator;
/*     */         
/*     */         NodeAndSuccessors(N node, Iterable<? extends N> successors) {
/* 477 */           this.node = node;
/* 478 */           this.successorIterator = successors.iterator();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class TreeTraverser<N> extends Traverser<N> {
/*     */     private final SuccessorsFunction<N> tree;
/*     */     
/*     */     TreeTraverser(SuccessorsFunction<N> tree) {
/* 488 */       this.tree = (SuccessorsFunction<N>)Preconditions.checkNotNull(tree);
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterable<N> breadthFirst(N startNode) {
/* 493 */       Preconditions.checkNotNull(startNode);
/* 494 */       return breadthFirst((Iterable<? extends N>)ImmutableSet.of(startNode));
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterable<N> breadthFirst(final Iterable<? extends N> startNodes) {
/* 499 */       Preconditions.checkNotNull(startNodes);
/* 500 */       if (Iterables.isEmpty(startNodes)) {
/* 501 */         return (Iterable<N>)ImmutableSet.of();
/*     */       }
/* 503 */       for (N startNode : startNodes) {
/* 504 */         checkThatNodeIsInTree(startNode);
/*     */       }
/* 506 */       return new Iterable<N>()
/*     */         {
/*     */           public Iterator<N> iterator() {
/* 509 */             return (Iterator<N>)new Traverser.TreeTraverser.BreadthFirstIterator(startNodes);
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterable<N> depthFirstPreOrder(N startNode) {
/* 516 */       Preconditions.checkNotNull(startNode);
/* 517 */       return depthFirstPreOrder((Iterable<? extends N>)ImmutableSet.of(startNode));
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterable<N> depthFirstPreOrder(final Iterable<? extends N> startNodes) {
/* 522 */       Preconditions.checkNotNull(startNodes);
/* 523 */       if (Iterables.isEmpty(startNodes)) {
/* 524 */         return (Iterable<N>)ImmutableSet.of();
/*     */       }
/* 526 */       for (N node : startNodes) {
/* 527 */         checkThatNodeIsInTree(node);
/*     */       }
/* 529 */       return new Iterable<N>()
/*     */         {
/*     */           public Iterator<N> iterator() {
/* 532 */             return (Iterator<N>)new Traverser.TreeTraverser.DepthFirstPreOrderIterator(startNodes);
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterable<N> depthFirstPostOrder(N startNode) {
/* 539 */       Preconditions.checkNotNull(startNode);
/* 540 */       return depthFirstPostOrder((Iterable<? extends N>)ImmutableSet.of(startNode));
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterable<N> depthFirstPostOrder(final Iterable<? extends N> startNodes) {
/* 545 */       Preconditions.checkNotNull(startNodes);
/* 546 */       if (Iterables.isEmpty(startNodes)) {
/* 547 */         return (Iterable<N>)ImmutableSet.of();
/*     */       }
/* 549 */       for (N startNode : startNodes) {
/* 550 */         checkThatNodeIsInTree(startNode);
/*     */       }
/* 552 */       return new Iterable<N>()
/*     */         {
/*     */           public Iterator<N> iterator() {
/* 555 */             return (Iterator<N>)new Traverser.TreeTraverser.DepthFirstPostOrderIterator(startNodes);
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void checkThatNodeIsInTree(N startNode) {
/* 564 */       this.tree.successors(startNode);
/*     */     }
/*     */     
/*     */     private final class BreadthFirstIterator extends UnmodifiableIterator<N> {
/* 568 */       private final Queue<N> queue = new ArrayDeque<>();
/*     */       
/*     */       BreadthFirstIterator(Iterable<? extends N> roots) {
/* 571 */         for (N root : roots) {
/* 572 */           this.queue.add(root);
/*     */         }
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean hasNext() {
/* 578 */         return !this.queue.isEmpty();
/*     */       }
/*     */ 
/*     */       
/*     */       public N next() {
/* 583 */         N current = this.queue.remove();
/* 584 */         Iterables.addAll(this.queue, Traverser.TreeTraverser.this.tree.successors(current));
/* 585 */         return current;
/*     */       }
/*     */     }
/*     */     
/*     */     private final class DepthFirstPreOrderIterator extends UnmodifiableIterator<N> {
/* 590 */       private final Deque<Iterator<? extends N>> stack = new ArrayDeque<>();
/*     */       
/*     */       DepthFirstPreOrderIterator(Iterable<? extends N> roots) {
/* 593 */         this.stack.addLast(roots.iterator());
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean hasNext() {
/* 598 */         return !this.stack.isEmpty();
/*     */       }
/*     */ 
/*     */       
/*     */       public N next() {
/* 603 */         Iterator<? extends N> iterator = this.stack.getLast();
/* 604 */         N result = (N)Preconditions.checkNotNull(iterator.next());
/* 605 */         if (!iterator.hasNext()) {
/* 606 */           this.stack.removeLast();
/*     */         }
/* 608 */         Iterator<? extends N> childIterator = Traverser.TreeTraverser.this.tree.successors(result).iterator();
/* 609 */         if (childIterator.hasNext()) {
/* 610 */           this.stack.addLast(childIterator);
/*     */         }
/* 612 */         return result;
/*     */       }
/*     */     }
/*     */     
/*     */     private final class DepthFirstPostOrderIterator extends AbstractIterator<N> {
/* 617 */       private final ArrayDeque<NodeAndChildren> stack = new ArrayDeque<>();
/*     */       
/*     */       DepthFirstPostOrderIterator(Iterable<? extends N> roots) {
/* 620 */         this.stack.addLast(new NodeAndChildren(null, roots));
/*     */       }
/*     */ 
/*     */       
/*     */       protected N computeNext() {
/* 625 */         while (!this.stack.isEmpty()) {
/* 626 */           NodeAndChildren top = this.stack.getLast();
/* 627 */           if (top.childIterator.hasNext()) {
/* 628 */             N child = top.childIterator.next();
/* 629 */             this.stack.addLast(withChildren(child)); continue;
/*     */           } 
/* 631 */           this.stack.removeLast();
/* 632 */           if (top.node != null) {
/* 633 */             return top.node;
/*     */           }
/*     */         } 
/*     */         
/* 637 */         return (N)endOfData();
/*     */       }
/*     */       
/*     */       NodeAndChildren withChildren(N node) {
/* 641 */         return new NodeAndChildren(node, Traverser.TreeTraverser.this.tree.successors(node));
/*     */       }
/*     */       
/*     */       private final class NodeAndChildren
/*     */       {
/*     */         final N node;
/*     */         final Iterator<? extends N> childIterator;
/*     */         
/*     */         NodeAndChildren(N node, Iterable<? extends N> children) {
/* 650 */           this.node = node;
/* 651 */           this.childIterator = children.iterator();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private enum Order {
/* 658 */     PREORDER,
/* 659 */     POSTORDER;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\graph\Traverser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */