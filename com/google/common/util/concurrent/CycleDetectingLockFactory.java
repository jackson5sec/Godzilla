/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.MapMaker;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.j2objc.annotations.Weak;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @CanIgnoreReturnValue
/*     */ @GwtIncompatible
/*     */ public class CycleDetectingLockFactory
/*     */ {
/*     */   @Beta
/*     */   public enum Policies
/*     */     implements Policy
/*     */   {
/* 201 */     THROW
/*     */     {
/*     */       public void handlePotentialDeadlock(CycleDetectingLockFactory.PotentialDeadlockException e) {
/* 204 */         throw e;
/*     */       }
/*     */     },
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 213 */     WARN
/*     */     {
/*     */       public void handlePotentialDeadlock(CycleDetectingLockFactory.PotentialDeadlockException e) {
/* 216 */         CycleDetectingLockFactory.logger.log(Level.SEVERE, "Detected potential deadlock", e);
/*     */       }
/*     */     },
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 228 */     DISABLED
/*     */     {
/*     */       public void handlePotentialDeadlock(CycleDetectingLockFactory.PotentialDeadlockException e) {}
/*     */     };
/*     */   }
/*     */ 
/*     */   
/*     */   public static CycleDetectingLockFactory newInstance(Policy policy) {
/* 236 */     return new CycleDetectingLockFactory(policy);
/*     */   }
/*     */ 
/*     */   
/*     */   public ReentrantLock newReentrantLock(String lockName) {
/* 241 */     return newReentrantLock(lockName, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReentrantLock newReentrantLock(String lockName, boolean fair) {
/* 249 */     return (this.policy == Policies.DISABLED) ? new ReentrantLock(fair) : new CycleDetectingReentrantLock(new LockGraphNode(lockName), fair);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReentrantReadWriteLock newReentrantReadWriteLock(String lockName) {
/* 256 */     return newReentrantReadWriteLock(lockName, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReentrantReadWriteLock newReentrantReadWriteLock(String lockName, boolean fair) {
/* 265 */     return (this.policy == Policies.DISABLED) ? new ReentrantReadWriteLock(fair) : new CycleDetectingReentrantReadWriteLock(new LockGraphNode(lockName), fair);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 272 */   private static final ConcurrentMap<Class<? extends Enum>, Map<? extends Enum, LockGraphNode>> lockGraphNodesPerType = (new MapMaker()).weakKeys().makeMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Enum<E>> WithExplicitOrdering<E> newInstanceWithExplicitOrdering(Class<E> enumClass, Policy policy) {
/* 279 */     Preconditions.checkNotNull(enumClass);
/* 280 */     Preconditions.checkNotNull(policy);
/*     */     
/* 282 */     Map<E, LockGraphNode> lockGraphNodes = (Map)getOrCreateNodes(enumClass);
/* 283 */     return new WithExplicitOrdering<>(policy, lockGraphNodes);
/*     */   }
/*     */   
/*     */   private static Map<? extends Enum, LockGraphNode> getOrCreateNodes(Class<? extends Enum> clazz) {
/* 287 */     Map<? extends Enum, LockGraphNode> existing = lockGraphNodesPerType.get(clazz);
/* 288 */     if (existing != null) {
/* 289 */       return existing;
/*     */     }
/* 291 */     Map<? extends Enum, LockGraphNode> created = createNodes(clazz);
/* 292 */     existing = lockGraphNodesPerType.putIfAbsent(clazz, created);
/* 293 */     return (Map<? extends Enum, LockGraphNode>)MoreObjects.firstNonNull(existing, created);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static <E extends Enum<E>> Map<E, LockGraphNode> createNodes(Class<E> clazz) {
/* 304 */     EnumMap<E, LockGraphNode> map = Maps.newEnumMap(clazz);
/* 305 */     Enum[] arrayOfEnum = (Enum[])clazz.getEnumConstants();
/* 306 */     int numKeys = arrayOfEnum.length;
/* 307 */     ArrayList<LockGraphNode> nodes = Lists.newArrayListWithCapacity(numKeys);
/*     */     
/* 309 */     for (Enum<?> enum_ : arrayOfEnum) {
/* 310 */       LockGraphNode node = new LockGraphNode(getLockName(enum_));
/* 311 */       nodes.add(node);
/* 312 */       map.put((E)enum_, node);
/*     */     } 
/*     */     int i;
/* 315 */     for (i = 1; i < numKeys; i++) {
/* 316 */       ((LockGraphNode)nodes.get(i)).checkAcquiredLocks(Policies.THROW, nodes.subList(0, i));
/*     */     }
/*     */     
/* 319 */     for (i = 0; i < numKeys - 1; i++) {
/* 320 */       ((LockGraphNode)nodes.get(i)).checkAcquiredLocks(Policies.DISABLED, nodes.subList(i + 1, numKeys));
/*     */     }
/* 322 */     return Collections.unmodifiableMap(map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getLockName(Enum<?> rank) {
/* 330 */     return rank.getDeclaringClass().getSimpleName() + "." + rank.name();
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
/*     */   @Beta
/*     */   public static final class WithExplicitOrdering<E extends Enum<E>>
/*     */     extends CycleDetectingLockFactory
/*     */   {
/*     */     private final Map<E, CycleDetectingLockFactory.LockGraphNode> lockGraphNodes;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @VisibleForTesting
/*     */     WithExplicitOrdering(CycleDetectingLockFactory.Policy policy, Map<E, CycleDetectingLockFactory.LockGraphNode> lockGraphNodes) {
/* 400 */       super(policy);
/* 401 */       this.lockGraphNodes = lockGraphNodes;
/*     */     }
/*     */ 
/*     */     
/*     */     public ReentrantLock newReentrantLock(E rank) {
/* 406 */       return newReentrantLock(rank, false);
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
/*     */     public ReentrantLock newReentrantLock(E rank, boolean fair) {
/* 418 */       return (this.policy == CycleDetectingLockFactory.Policies.DISABLED) ? new ReentrantLock(fair) : new CycleDetectingLockFactory.CycleDetectingReentrantLock(this.lockGraphNodes
/*     */           
/* 420 */           .get(rank), fair);
/*     */     }
/*     */ 
/*     */     
/*     */     public ReentrantReadWriteLock newReentrantReadWriteLock(E rank) {
/* 425 */       return newReentrantReadWriteLock(rank, false);
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
/*     */     public ReentrantReadWriteLock newReentrantReadWriteLock(E rank, boolean fair) {
/* 437 */       return (this.policy == CycleDetectingLockFactory.Policies.DISABLED) ? new ReentrantReadWriteLock(fair) : new CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock(this.lockGraphNodes
/*     */           
/* 439 */           .get(rank), fair);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 445 */   private static final Logger logger = Logger.getLogger(CycleDetectingLockFactory.class.getName());
/*     */   
/*     */   final Policy policy;
/*     */   
/*     */   private CycleDetectingLockFactory(Policy policy) {
/* 450 */     this.policy = (Policy)Preconditions.checkNotNull(policy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 459 */   private static final ThreadLocal<ArrayList<LockGraphNode>> acquiredLocks = new ThreadLocal<ArrayList<LockGraphNode>>()
/*     */     {
/*     */       protected ArrayList<CycleDetectingLockFactory.LockGraphNode> initialValue()
/*     */       {
/* 463 */         return Lists.newArrayListWithCapacity(3);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ExampleStackTrace
/*     */     extends IllegalStateException
/*     */   {
/* 482 */     static final StackTraceElement[] EMPTY_STACK_TRACE = new StackTraceElement[0];
/*     */ 
/*     */     
/* 485 */     static final ImmutableSet<String> EXCLUDED_CLASS_NAMES = ImmutableSet.of(CycleDetectingLockFactory.class
/* 486 */         .getName(), ExampleStackTrace.class
/* 487 */         .getName(), CycleDetectingLockFactory.LockGraphNode.class
/* 488 */         .getName());
/*     */     
/*     */     ExampleStackTrace(CycleDetectingLockFactory.LockGraphNode node1, CycleDetectingLockFactory.LockGraphNode node2) {
/* 491 */       super(node1.getLockName() + " -> " + node2.getLockName());
/* 492 */       StackTraceElement[] origStackTrace = getStackTrace();
/* 493 */       for (int i = 0, n = origStackTrace.length; i < n; i++) {
/* 494 */         if (CycleDetectingLockFactory.WithExplicitOrdering.class.getName().equals(origStackTrace[i].getClassName())) {
/*     */           
/* 496 */           setStackTrace(EMPTY_STACK_TRACE);
/*     */           break;
/*     */         } 
/* 499 */         if (!EXCLUDED_CLASS_NAMES.contains(origStackTrace[i].getClassName())) {
/* 500 */           setStackTrace(Arrays.<StackTraceElement>copyOfRange(origStackTrace, i, n));
/*     */           break;
/*     */         } 
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
/*     */   @Beta
/*     */   public static final class PotentialDeadlockException
/*     */     extends ExampleStackTrace
/*     */   {
/*     */     private final CycleDetectingLockFactory.ExampleStackTrace conflictingStackTrace;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private PotentialDeadlockException(CycleDetectingLockFactory.LockGraphNode node1, CycleDetectingLockFactory.LockGraphNode node2, CycleDetectingLockFactory.ExampleStackTrace conflictingStackTrace) {
/* 534 */       super(node1, node2);
/* 535 */       this.conflictingStackTrace = conflictingStackTrace;
/* 536 */       initCause(conflictingStackTrace);
/*     */     }
/*     */     
/*     */     public CycleDetectingLockFactory.ExampleStackTrace getConflictingStackTrace() {
/* 540 */       return this.conflictingStackTrace;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getMessage() {
/* 549 */       StringBuilder message = new StringBuilder(super.getMessage());
/* 550 */       for (Throwable t = this.conflictingStackTrace; t != null; t = t.getCause()) {
/* 551 */         message.append(", ").append(t.getMessage());
/*     */       }
/* 553 */       return message.toString();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class LockGraphNode
/*     */   {
/* 581 */     final Map<LockGraphNode, CycleDetectingLockFactory.ExampleStackTrace> allowedPriorLocks = (new MapMaker())
/* 582 */       .weakKeys().makeMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 588 */     final Map<LockGraphNode, CycleDetectingLockFactory.PotentialDeadlockException> disallowedPriorLocks = (new MapMaker())
/* 589 */       .weakKeys().makeMap();
/*     */     
/*     */     final String lockName;
/*     */     
/*     */     LockGraphNode(String lockName) {
/* 594 */       this.lockName = (String)Preconditions.checkNotNull(lockName);
/*     */     }
/*     */     
/*     */     String getLockName() {
/* 598 */       return this.lockName;
/*     */     }
/*     */     
/*     */     void checkAcquiredLocks(CycleDetectingLockFactory.Policy policy, List<LockGraphNode> acquiredLocks) {
/* 602 */       for (int i = 0, size = acquiredLocks.size(); i < size; i++) {
/* 603 */         checkAcquiredLock(policy, acquiredLocks.get(i));
/*     */       }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void checkAcquiredLock(CycleDetectingLockFactory.Policy policy, LockGraphNode acquiredLock) {
/* 623 */       Preconditions.checkState((this != acquiredLock), "Attempted to acquire multiple locks with the same rank %s", acquiredLock
/*     */ 
/*     */           
/* 626 */           .getLockName());
/*     */       
/* 628 */       if (this.allowedPriorLocks.containsKey(acquiredLock)) {
/*     */         return;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 634 */       CycleDetectingLockFactory.PotentialDeadlockException previousDeadlockException = this.disallowedPriorLocks.get(acquiredLock);
/* 635 */       if (previousDeadlockException != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 641 */         CycleDetectingLockFactory.PotentialDeadlockException exception = new CycleDetectingLockFactory.PotentialDeadlockException(acquiredLock, this, previousDeadlockException.getConflictingStackTrace());
/* 642 */         policy.handlePotentialDeadlock(exception);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 647 */       Set<LockGraphNode> seen = Sets.newIdentityHashSet();
/* 648 */       CycleDetectingLockFactory.ExampleStackTrace path = acquiredLock.findPathTo(this, seen);
/*     */       
/* 650 */       if (path == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 659 */         this.allowedPriorLocks.put(acquiredLock, new CycleDetectingLockFactory.ExampleStackTrace(acquiredLock, this));
/*     */       }
/*     */       else {
/*     */         
/* 663 */         CycleDetectingLockFactory.PotentialDeadlockException exception = new CycleDetectingLockFactory.PotentialDeadlockException(acquiredLock, this, path);
/*     */         
/* 665 */         this.disallowedPriorLocks.put(acquiredLock, exception);
/* 666 */         policy.handlePotentialDeadlock(exception);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private CycleDetectingLockFactory.ExampleStackTrace findPathTo(LockGraphNode node, Set<LockGraphNode> seen) {
/* 678 */       if (!seen.add(this)) {
/* 679 */         return null;
/*     */       }
/* 681 */       CycleDetectingLockFactory.ExampleStackTrace found = this.allowedPriorLocks.get(node);
/* 682 */       if (found != null) {
/* 683 */         return found;
/*     */       }
/*     */       
/* 686 */       for (Map.Entry<LockGraphNode, CycleDetectingLockFactory.ExampleStackTrace> entry : this.allowedPriorLocks.entrySet()) {
/* 687 */         LockGraphNode preAcquiredLock = entry.getKey();
/* 688 */         found = preAcquiredLock.findPathTo(node, seen);
/* 689 */         if (found != null) {
/*     */ 
/*     */ 
/*     */           
/* 693 */           CycleDetectingLockFactory.ExampleStackTrace path = new CycleDetectingLockFactory.ExampleStackTrace(preAcquiredLock, this);
/* 694 */           path.setStackTrace(((CycleDetectingLockFactory.ExampleStackTrace)entry.getValue()).getStackTrace());
/* 695 */           path.initCause(found);
/* 696 */           return path;
/*     */         } 
/*     */       } 
/* 699 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void aboutToAcquire(CycleDetectingLock lock) {
/* 707 */     if (!lock.isAcquiredByCurrentThread()) {
/* 708 */       ArrayList<LockGraphNode> acquiredLockList = acquiredLocks.get();
/* 709 */       LockGraphNode node = lock.getLockGraphNode();
/* 710 */       node.checkAcquiredLocks(this.policy, acquiredLockList);
/* 711 */       acquiredLockList.add(node);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void lockStateChanged(CycleDetectingLock lock) {
/* 721 */     if (!lock.isAcquiredByCurrentThread()) {
/* 722 */       ArrayList<LockGraphNode> acquiredLockList = acquiredLocks.get();
/* 723 */       LockGraphNode node = lock.getLockGraphNode();
/*     */ 
/*     */       
/* 726 */       for (int i = acquiredLockList.size() - 1; i >= 0; i--) {
/* 727 */         if (acquiredLockList.get(i) == node) {
/* 728 */           acquiredLockList.remove(i);
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   final class CycleDetectingReentrantLock
/*     */     extends ReentrantLock implements CycleDetectingLock {
/*     */     private final CycleDetectingLockFactory.LockGraphNode lockGraphNode;
/*     */     
/*     */     private CycleDetectingReentrantLock(CycleDetectingLockFactory.LockGraphNode lockGraphNode, boolean fair) {
/* 740 */       super(fair);
/* 741 */       this.lockGraphNode = (CycleDetectingLockFactory.LockGraphNode)Preconditions.checkNotNull(lockGraphNode);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public CycleDetectingLockFactory.LockGraphNode getLockGraphNode() {
/* 748 */       return this.lockGraphNode;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isAcquiredByCurrentThread() {
/* 753 */       return isHeldByCurrentThread();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void lock() {
/* 760 */       CycleDetectingLockFactory.this.aboutToAcquire(this);
/*     */       try {
/* 762 */         super.lock();
/*     */       } finally {
/* 764 */         CycleDetectingLockFactory.lockStateChanged(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void lockInterruptibly() throws InterruptedException {
/* 770 */       CycleDetectingLockFactory.this.aboutToAcquire(this);
/*     */       try {
/* 772 */         super.lockInterruptibly();
/*     */       } finally {
/* 774 */         CycleDetectingLockFactory.lockStateChanged(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryLock() {
/* 780 */       CycleDetectingLockFactory.this.aboutToAcquire(this);
/*     */       try {
/* 782 */         return super.tryLock();
/*     */       } finally {
/* 784 */         CycleDetectingLockFactory.lockStateChanged(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
/* 790 */       CycleDetectingLockFactory.this.aboutToAcquire(this);
/*     */       try {
/* 792 */         return super.tryLock(timeout, unit);
/*     */       } finally {
/* 794 */         CycleDetectingLockFactory.lockStateChanged(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void unlock() {
/*     */       try {
/* 801 */         super.unlock();
/*     */       } finally {
/* 803 */         CycleDetectingLockFactory.lockStateChanged(this);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   final class CycleDetectingReentrantReadWriteLock
/*     */     extends ReentrantReadWriteLock
/*     */     implements CycleDetectingLock
/*     */   {
/*     */     private final CycleDetectingLockFactory.CycleDetectingReentrantReadLock readLock;
/*     */     
/*     */     private final CycleDetectingLockFactory.CycleDetectingReentrantWriteLock writeLock;
/*     */     
/*     */     private final CycleDetectingLockFactory.LockGraphNode lockGraphNode;
/*     */ 
/*     */     
/*     */     private CycleDetectingReentrantReadWriteLock(CycleDetectingLockFactory.LockGraphNode lockGraphNode, boolean fair) {
/* 821 */       super(fair);
/* 822 */       this.readLock = new CycleDetectingLockFactory.CycleDetectingReentrantReadLock(this);
/* 823 */       this.writeLock = new CycleDetectingLockFactory.CycleDetectingReentrantWriteLock(this);
/* 824 */       this.lockGraphNode = (CycleDetectingLockFactory.LockGraphNode)Preconditions.checkNotNull(lockGraphNode);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ReentrantReadWriteLock.ReadLock readLock() {
/* 831 */       return this.readLock;
/*     */     }
/*     */ 
/*     */     
/*     */     public ReentrantReadWriteLock.WriteLock writeLock() {
/* 836 */       return this.writeLock;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public CycleDetectingLockFactory.LockGraphNode getLockGraphNode() {
/* 843 */       return this.lockGraphNode;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isAcquiredByCurrentThread() {
/* 848 */       return (isWriteLockedByCurrentThread() || getReadHoldCount() > 0);
/*     */     }
/*     */   }
/*     */   
/*     */   private class CycleDetectingReentrantReadLock
/*     */     extends ReentrantReadWriteLock.ReadLock {
/*     */     @Weak
/*     */     final CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock readWriteLock;
/*     */     
/*     */     CycleDetectingReentrantReadLock(CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock readWriteLock) {
/* 858 */       this.readWriteLock = readWriteLock;
/*     */     }
/*     */ 
/*     */     
/*     */     public void lock() {
/* 863 */       CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
/*     */       try {
/* 865 */         super.lock();
/*     */       } finally {
/* 867 */         CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void lockInterruptibly() throws InterruptedException {
/* 873 */       CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
/*     */       try {
/* 875 */         super.lockInterruptibly();
/*     */       } finally {
/* 877 */         CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryLock() {
/* 883 */       CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
/*     */       try {
/* 885 */         return super.tryLock();
/*     */       } finally {
/* 887 */         CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
/* 893 */       CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
/*     */       try {
/* 895 */         return super.tryLock(timeout, unit);
/*     */       } finally {
/* 897 */         CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void unlock() {
/*     */       try {
/* 904 */         super.unlock();
/*     */       } finally {
/* 906 */         CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private class CycleDetectingReentrantWriteLock
/*     */     extends ReentrantReadWriteLock.WriteLock {
/*     */     @Weak
/*     */     final CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock readWriteLock;
/*     */     
/*     */     CycleDetectingReentrantWriteLock(CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock readWriteLock) {
/* 917 */       this.readWriteLock = readWriteLock;
/*     */     }
/*     */ 
/*     */     
/*     */     public void lock() {
/* 922 */       CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
/*     */       try {
/* 924 */         super.lock();
/*     */       } finally {
/* 926 */         CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void lockInterruptibly() throws InterruptedException {
/* 932 */       CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
/*     */       try {
/* 934 */         super.lockInterruptibly();
/*     */       } finally {
/* 936 */         CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryLock() {
/* 942 */       CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
/*     */       try {
/* 944 */         return super.tryLock();
/*     */       } finally {
/* 946 */         CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
/* 952 */       CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
/*     */       try {
/* 954 */         return super.tryLock(timeout, unit);
/*     */       } finally {
/* 956 */         CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void unlock() {
/*     */       try {
/* 963 */         super.unlock();
/*     */       } finally {
/* 965 */         CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static interface CycleDetectingLock {
/*     */     CycleDetectingLockFactory.LockGraphNode getLockGraphNode();
/*     */     
/*     */     boolean isAcquiredByCurrentThread();
/*     */   }
/*     */   
/*     */   @Beta
/*     */   public static interface Policy {
/*     */     void handlePotentialDeadlock(CycleDetectingLockFactory.PotentialDeadlockException param1PotentialDeadlockException);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\CycleDetectingLockFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */