/*      */ package com.google.common.util.concurrent;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.errorprone.annotations.concurrent.GuardedBy;
/*      */ import com.google.j2objc.annotations.Weak;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.locks.Condition;
/*      */ import java.util.concurrent.locks.ReentrantLock;
/*      */ import java.util.function.BooleanSupplier;
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
/*      */ @Beta
/*      */ @GwtIncompatible
/*      */ public final class Monitor
/*      */ {
/*      */   private final boolean fair;
/*      */   private final ReentrantLock lock;
/*      */   
/*      */   @Beta
/*      */   public static abstract class Guard
/*      */   {
/*      */     @Weak
/*      */     final Monitor monitor;
/*      */     final Condition condition;
/*      */     @GuardedBy("monitor.lock")
/*  309 */     int waiterCount = 0;
/*      */ 
/*      */     
/*      */     @GuardedBy("monitor.lock")
/*      */     Guard next;
/*      */ 
/*      */ 
/*      */     
/*      */     protected Guard(Monitor monitor) {
/*  318 */       this.monitor = (Monitor)Preconditions.checkNotNull(monitor, "monitor");
/*  319 */       this.condition = monitor.lock.newCondition();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public abstract boolean isSatisfied();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GuardedBy("lock")
/*  340 */   private Guard activeGuards = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Monitor() {
/*  348 */     this(false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Monitor(boolean fair) {
/*  358 */     this.fair = fair;
/*  359 */     this.lock = new ReentrantLock(fair);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Guard newGuard(final BooleanSupplier isSatisfied) {
/*  370 */     Preconditions.checkNotNull(isSatisfied, "isSatisfied");
/*  371 */     return new Guard(this)
/*      */       {
/*      */         public boolean isSatisfied() {
/*  374 */           return isSatisfied.getAsBoolean();
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   public void enter() {
/*  381 */     this.lock.lock();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean enter(long time, TimeUnit unit) {
/*  391 */     long timeoutNanos = toSafeNanos(time, unit);
/*  392 */     ReentrantLock lock = this.lock;
/*  393 */     if (!this.fair && lock.tryLock()) {
/*  394 */       return true;
/*      */     }
/*  396 */     boolean interrupted = Thread.interrupted();
/*      */     try {
/*  398 */       long startTime = System.nanoTime();
/*  399 */       long remainingNanos = timeoutNanos; while (true) {
/*      */         try {
/*  401 */           return lock.tryLock(remainingNanos, TimeUnit.NANOSECONDS);
/*  402 */         } catch (InterruptedException interrupt) {
/*  403 */           interrupted = true;
/*  404 */           remainingNanos = remainingNanos(startTime, timeoutNanos);
/*      */         } 
/*      */       } 
/*      */     } finally {
/*  408 */       if (interrupted) {
/*  409 */         Thread.currentThread().interrupt();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void enterInterruptibly() throws InterruptedException {
/*  420 */     this.lock.lockInterruptibly();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean enterInterruptibly(long time, TimeUnit unit) throws InterruptedException {
/*  431 */     return this.lock.tryLock(time, unit);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean tryEnter() {
/*  442 */     return this.lock.tryLock();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void enterWhen(Guard guard) throws InterruptedException {
/*  451 */     if (guard.monitor != this) {
/*  452 */       throw new IllegalMonitorStateException();
/*      */     }
/*  454 */     ReentrantLock lock = this.lock;
/*  455 */     boolean signalBeforeWaiting = lock.isHeldByCurrentThread();
/*  456 */     lock.lockInterruptibly();
/*      */     
/*  458 */     boolean satisfied = false;
/*      */     try {
/*  460 */       if (!guard.isSatisfied()) {
/*  461 */         await(guard, signalBeforeWaiting);
/*      */       }
/*  463 */       satisfied = true;
/*      */     } finally {
/*  465 */       if (!satisfied) {
/*  466 */         leave();
/*      */       }
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
/*      */   public boolean enterWhen(Guard guard, long time, TimeUnit unit) throws InterruptedException {
/*      */     // Byte code:
/*      */     //   0: lload_2
/*      */     //   1: aload #4
/*      */     //   3: invokestatic toSafeNanos : (JLjava/util/concurrent/TimeUnit;)J
/*      */     //   6: lstore #5
/*      */     //   8: aload_1
/*      */     //   9: getfield monitor : Lcom/google/common/util/concurrent/Monitor;
/*      */     //   12: aload_0
/*      */     //   13: if_acmpeq -> 24
/*      */     //   16: new java/lang/IllegalMonitorStateException
/*      */     //   19: dup
/*      */     //   20: invokespecial <init> : ()V
/*      */     //   23: athrow
/*      */     //   24: aload_0
/*      */     //   25: getfield lock : Ljava/util/concurrent/locks/ReentrantLock;
/*      */     //   28: astore #7
/*      */     //   30: aload #7
/*      */     //   32: invokevirtual isHeldByCurrentThread : ()Z
/*      */     //   35: istore #8
/*      */     //   37: lconst_0
/*      */     //   38: lstore #9
/*      */     //   40: aload_0
/*      */     //   41: getfield fair : Z
/*      */     //   44: ifne -> 72
/*      */     //   47: invokestatic interrupted : ()Z
/*      */     //   50: ifeq -> 61
/*      */     //   53: new java/lang/InterruptedException
/*      */     //   56: dup
/*      */     //   57: invokespecial <init> : ()V
/*      */     //   60: athrow
/*      */     //   61: aload #7
/*      */     //   63: invokevirtual tryLock : ()Z
/*      */     //   66: ifeq -> 72
/*      */     //   69: goto -> 92
/*      */     //   72: lload #5
/*      */     //   74: invokestatic initNanoTime : (J)J
/*      */     //   77: lstore #9
/*      */     //   79: aload #7
/*      */     //   81: lload_2
/*      */     //   82: aload #4
/*      */     //   84: invokevirtual tryLock : (JLjava/util/concurrent/TimeUnit;)Z
/*      */     //   87: ifne -> 92
/*      */     //   90: iconst_0
/*      */     //   91: ireturn
/*      */     //   92: iconst_0
/*      */     //   93: istore #11
/*      */     //   95: iconst_1
/*      */     //   96: istore #12
/*      */     //   98: aload_1
/*      */     //   99: invokevirtual isSatisfied : ()Z
/*      */     //   102: ifne -> 134
/*      */     //   105: aload_0
/*      */     //   106: aload_1
/*      */     //   107: lload #9
/*      */     //   109: lconst_0
/*      */     //   110: lcmp
/*      */     //   111: ifne -> 119
/*      */     //   114: lload #5
/*      */     //   116: goto -> 126
/*      */     //   119: lload #9
/*      */     //   121: lload #5
/*      */     //   123: invokestatic remainingNanos : (JJ)J
/*      */     //   126: iload #8
/*      */     //   128: invokespecial awaitNanos : (Lcom/google/common/util/concurrent/Monitor$Guard;JZ)Z
/*      */     //   131: ifeq -> 138
/*      */     //   134: iconst_1
/*      */     //   135: goto -> 139
/*      */     //   138: iconst_0
/*      */     //   139: istore #11
/*      */     //   141: iconst_0
/*      */     //   142: istore #12
/*      */     //   144: iload #11
/*      */     //   146: istore #13
/*      */     //   148: iload #11
/*      */     //   150: ifne -> 185
/*      */     //   153: iload #12
/*      */     //   155: ifeq -> 167
/*      */     //   158: iload #8
/*      */     //   160: ifne -> 167
/*      */     //   163: aload_0
/*      */     //   164: invokespecial signalNextWaiter : ()V
/*      */     //   167: aload #7
/*      */     //   169: invokevirtual unlock : ()V
/*      */     //   172: goto -> 185
/*      */     //   175: astore #14
/*      */     //   177: aload #7
/*      */     //   179: invokevirtual unlock : ()V
/*      */     //   182: aload #14
/*      */     //   184: athrow
/*      */     //   185: iload #13
/*      */     //   187: ireturn
/*      */     //   188: astore #15
/*      */     //   190: iload #11
/*      */     //   192: ifne -> 227
/*      */     //   195: iload #12
/*      */     //   197: ifeq -> 209
/*      */     //   200: iload #8
/*      */     //   202: ifne -> 209
/*      */     //   205: aload_0
/*      */     //   206: invokespecial signalNextWaiter : ()V
/*      */     //   209: aload #7
/*      */     //   211: invokevirtual unlock : ()V
/*      */     //   214: goto -> 227
/*      */     //   217: astore #16
/*      */     //   219: aload #7
/*      */     //   221: invokevirtual unlock : ()V
/*      */     //   224: aload #16
/*      */     //   226: athrow
/*      */     //   227: aload #15
/*      */     //   229: athrow
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #481	-> 0
/*      */     //   #482	-> 8
/*      */     //   #483	-> 16
/*      */     //   #485	-> 24
/*      */     //   #486	-> 30
/*      */     //   #487	-> 37
/*      */     //   #491	-> 40
/*      */     //   #493	-> 47
/*      */     //   #494	-> 53
/*      */     //   #496	-> 61
/*      */     //   #497	-> 69
/*      */     //   #500	-> 72
/*      */     //   #501	-> 79
/*      */     //   #502	-> 90
/*      */     //   #506	-> 92
/*      */     //   #507	-> 95
/*      */     //   #509	-> 98
/*      */     //   #510	-> 99
/*      */     //   #513	-> 123
/*      */     //   #511	-> 128
/*      */     //   #515	-> 141
/*      */     //   #516	-> 144
/*      */     //   #518	-> 148
/*      */     //   #521	-> 153
/*      */     //   #522	-> 163
/*      */     //   #525	-> 167
/*      */     //   #526	-> 172
/*      */     //   #525	-> 175
/*      */     //   #526	-> 182
/*      */     //   #516	-> 185
/*      */     //   #518	-> 188
/*      */     //   #521	-> 195
/*      */     //   #522	-> 205
/*      */     //   #525	-> 209
/*      */     //   #526	-> 214
/*      */     //   #525	-> 217
/*      */     //   #526	-> 224
/*      */     //   #528	-> 227
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	230	0	this	Lcom/google/common/util/concurrent/Monitor;
/*      */     //   0	230	1	guard	Lcom/google/common/util/concurrent/Monitor$Guard;
/*      */     //   0	230	2	time	J
/*      */     //   0	230	4	unit	Ljava/util/concurrent/TimeUnit;
/*      */     //   8	222	5	timeoutNanos	J
/*      */     //   30	200	7	lock	Ljava/util/concurrent/locks/ReentrantLock;
/*      */     //   37	193	8	reentrant	Z
/*      */     //   40	190	9	startTime	J
/*      */     //   95	135	11	satisfied	Z
/*      */     //   98	132	12	threw	Z
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   98	148	188	finally
/*      */     //   153	167	175	finally
/*      */     //   175	177	175	finally
/*      */     //   188	190	188	finally
/*      */     //   195	209	217	finally
/*      */     //   217	219	217	finally
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void enterWhenUninterruptibly(Guard guard) {
/*  533 */     if (guard.monitor != this) {
/*  534 */       throw new IllegalMonitorStateException();
/*      */     }
/*  536 */     ReentrantLock lock = this.lock;
/*  537 */     boolean signalBeforeWaiting = lock.isHeldByCurrentThread();
/*  538 */     lock.lock();
/*      */     
/*  540 */     boolean satisfied = false;
/*      */     try {
/*  542 */       if (!guard.isSatisfied()) {
/*  543 */         awaitUninterruptibly(guard, signalBeforeWaiting);
/*      */       }
/*  545 */       satisfied = true;
/*      */     } finally {
/*  547 */       if (!satisfied) {
/*  548 */         leave();
/*      */       }
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
/*      */   public boolean enterWhenUninterruptibly(Guard guard, long time, TimeUnit unit) {
/*  561 */     long timeoutNanos = toSafeNanos(time, unit);
/*  562 */     if (guard.monitor != this) {
/*  563 */       throw new IllegalMonitorStateException();
/*      */     }
/*  565 */     ReentrantLock lock = this.lock;
/*  566 */     long startTime = 0L;
/*  567 */     boolean signalBeforeWaiting = lock.isHeldByCurrentThread();
/*  568 */     boolean interrupted = Thread.interrupted();
/*      */     try {
/*  570 */       if (this.fair || !lock.tryLock()) {
/*  571 */         startTime = initNanoTime(timeoutNanos);
/*  572 */         long remainingNanos = timeoutNanos; while (true) {
/*      */           try {
/*  574 */             if (lock.tryLock(remainingNanos, TimeUnit.NANOSECONDS)) {
/*      */               break;
/*      */             }
/*  577 */             return false;
/*      */           }
/*  579 */           catch (InterruptedException interrupt) {
/*  580 */             interrupted = true;
/*  581 */             remainingNanos = remainingNanos(startTime, timeoutNanos);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/*  586 */       boolean satisfied = false;
/*      */       
/*      */       while (true) {
/*      */         try {
/*  590 */           if (guard.isSatisfied()) {
/*  591 */             satisfied = true;
/*      */           } else {
/*      */             long remainingNanos;
/*  594 */             if (startTime == 0L) {
/*  595 */               startTime = initNanoTime(timeoutNanos);
/*  596 */               remainingNanos = timeoutNanos;
/*      */             } else {
/*  598 */               remainingNanos = remainingNanos(startTime, timeoutNanos);
/*      */             } 
/*  600 */             satisfied = awaitNanos(guard, remainingNanos, signalBeforeWaiting);
/*      */           } 
/*  602 */           return satisfied;
/*  603 */         } catch (InterruptedException interrupt) {
/*  604 */           interrupted = true;
/*      */         
/*      */         }
/*      */         finally {
/*      */           
/*  609 */           if (!satisfied)
/*  610 */             lock.unlock(); 
/*      */         } 
/*      */       } 
/*      */     } finally {
/*  614 */       if (interrupted) {
/*  615 */         Thread.currentThread().interrupt();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean enterIf(Guard guard) {
/*  627 */     if (guard.monitor != this) {
/*  628 */       throw new IllegalMonitorStateException();
/*      */     }
/*  630 */     ReentrantLock lock = this.lock;
/*  631 */     lock.lock();
/*      */     
/*  633 */     boolean satisfied = false;
/*      */     try {
/*  635 */       return satisfied = guard.isSatisfied();
/*      */     } finally {
/*  637 */       if (!satisfied) {
/*  638 */         lock.unlock();
/*      */       }
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
/*      */   public boolean enterIf(Guard guard, long time, TimeUnit unit) {
/*  651 */     if (guard.monitor != this) {
/*  652 */       throw new IllegalMonitorStateException();
/*      */     }
/*  654 */     if (!enter(time, unit)) {
/*  655 */       return false;
/*      */     }
/*      */     
/*  658 */     boolean satisfied = false;
/*      */     try {
/*  660 */       return satisfied = guard.isSatisfied();
/*      */     } finally {
/*  662 */       if (!satisfied) {
/*  663 */         this.lock.unlock();
/*      */       }
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
/*      */   public boolean enterIfInterruptibly(Guard guard) throws InterruptedException {
/*  676 */     if (guard.monitor != this) {
/*  677 */       throw new IllegalMonitorStateException();
/*      */     }
/*  679 */     ReentrantLock lock = this.lock;
/*  680 */     lock.lockInterruptibly();
/*      */     
/*  682 */     boolean satisfied = false;
/*      */     try {
/*  684 */       return satisfied = guard.isSatisfied();
/*      */     } finally {
/*  686 */       if (!satisfied) {
/*  687 */         lock.unlock();
/*      */       }
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
/*      */   
/*      */   public boolean enterIfInterruptibly(Guard guard, long time, TimeUnit unit) throws InterruptedException {
/*  701 */     if (guard.monitor != this) {
/*  702 */       throw new IllegalMonitorStateException();
/*      */     }
/*  704 */     ReentrantLock lock = this.lock;
/*  705 */     if (!lock.tryLock(time, unit)) {
/*  706 */       return false;
/*      */     }
/*      */     
/*  709 */     boolean satisfied = false;
/*      */     try {
/*  711 */       return satisfied = guard.isSatisfied();
/*      */     } finally {
/*  713 */       if (!satisfied) {
/*  714 */         lock.unlock();
/*      */       }
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
/*      */   
/*      */   public boolean tryEnterIf(Guard guard) {
/*  728 */     if (guard.monitor != this) {
/*  729 */       throw new IllegalMonitorStateException();
/*      */     }
/*  731 */     ReentrantLock lock = this.lock;
/*  732 */     if (!lock.tryLock()) {
/*  733 */       return false;
/*      */     }
/*      */     
/*  736 */     boolean satisfied = false;
/*      */     try {
/*  738 */       return satisfied = guard.isSatisfied();
/*      */     } finally {
/*  740 */       if (!satisfied) {
/*  741 */         lock.unlock();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void waitFor(Guard guard) throws InterruptedException {
/*  753 */     if ((((guard.monitor == this) ? 1 : 0) & this.lock.isHeldByCurrentThread()) == 0) {
/*  754 */       throw new IllegalMonitorStateException();
/*      */     }
/*  756 */     if (!guard.isSatisfied()) {
/*  757 */       await(guard, true);
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
/*      */   
/*      */   public boolean waitFor(Guard guard, long time, TimeUnit unit) throws InterruptedException {
/*  770 */     long timeoutNanos = toSafeNanos(time, unit);
/*  771 */     if ((((guard.monitor == this) ? 1 : 0) & this.lock.isHeldByCurrentThread()) == 0) {
/*  772 */       throw new IllegalMonitorStateException();
/*      */     }
/*  774 */     if (guard.isSatisfied()) {
/*  775 */       return true;
/*      */     }
/*  777 */     if (Thread.interrupted()) {
/*  778 */       throw new InterruptedException();
/*      */     }
/*  780 */     return awaitNanos(guard, timeoutNanos, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void waitForUninterruptibly(Guard guard) {
/*  788 */     if ((((guard.monitor == this) ? 1 : 0) & this.lock.isHeldByCurrentThread()) == 0) {
/*  789 */       throw new IllegalMonitorStateException();
/*      */     }
/*  791 */     if (!guard.isSatisfied()) {
/*  792 */       awaitUninterruptibly(guard, true);
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
/*      */   public boolean waitForUninterruptibly(Guard guard, long time, TimeUnit unit) {
/*  804 */     long timeoutNanos = toSafeNanos(time, unit);
/*  805 */     if ((((guard.monitor == this) ? 1 : 0) & this.lock.isHeldByCurrentThread()) == 0) {
/*  806 */       throw new IllegalMonitorStateException();
/*      */     }
/*  808 */     if (guard.isSatisfied()) {
/*  809 */       return true;
/*      */     }
/*  811 */     boolean signalBeforeWaiting = true;
/*  812 */     long startTime = initNanoTime(timeoutNanos);
/*  813 */     boolean interrupted = Thread.interrupted();
/*      */     try {
/*  815 */       long remainingNanos = timeoutNanos; while (true) {
/*      */         try {
/*  817 */           return awaitNanos(guard, remainingNanos, signalBeforeWaiting);
/*  818 */         } catch (InterruptedException interrupt) {
/*  819 */           interrupted = true;
/*  820 */           if (guard.isSatisfied()) {
/*  821 */             return true;
/*      */           }
/*  823 */           signalBeforeWaiting = false;
/*  824 */           remainingNanos = remainingNanos(startTime, timeoutNanos);
/*      */         } 
/*      */       } 
/*      */     } finally {
/*  828 */       if (interrupted) {
/*  829 */         Thread.currentThread().interrupt();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void leave() {
/*  836 */     ReentrantLock lock = this.lock;
/*      */     
/*      */     try {
/*  839 */       if (lock.getHoldCount() == 1) {
/*  840 */         signalNextWaiter();
/*      */       }
/*      */     } finally {
/*  843 */       lock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isFair() {
/*  849 */     return this.fair;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isOccupied() {
/*  857 */     return this.lock.isLocked();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isOccupiedByCurrentThread() {
/*  865 */     return this.lock.isHeldByCurrentThread();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getOccupiedDepth() {
/*  873 */     return this.lock.getHoldCount();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getQueueLength() {
/*  883 */     return this.lock.getQueueLength();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasQueuedThreads() {
/*  893 */     return this.lock.hasQueuedThreads();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasQueuedThread(Thread thread) {
/*  903 */     return this.lock.hasQueuedThread(thread);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasWaiters(Guard guard) {
/*  913 */     return (getWaitQueueLength(guard) > 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getWaitQueueLength(Guard guard) {
/*  923 */     if (guard.monitor != this) {
/*  924 */       throw new IllegalMonitorStateException();
/*      */     }
/*  926 */     this.lock.lock();
/*      */     try {
/*  928 */       return guard.waiterCount;
/*      */     } finally {
/*  930 */       this.lock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static long toSafeNanos(long time, TimeUnit unit) {
/*  940 */     long timeoutNanos = unit.toNanos(time);
/*  941 */     return (timeoutNanos <= 0L) ? 0L : ((timeoutNanos > 6917529027641081853L) ? 6917529027641081853L : timeoutNanos);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static long initNanoTime(long timeoutNanos) {
/*  951 */     if (timeoutNanos <= 0L) {
/*  952 */       return 0L;
/*      */     }
/*  954 */     long startTime = System.nanoTime();
/*  955 */     return (startTime == 0L) ? 1L : startTime;
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
/*      */   private static long remainingNanos(long startTime, long timeoutNanos) {
/*  971 */     return (timeoutNanos <= 0L) ? 0L : (timeoutNanos - System.nanoTime() - startTime);
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GuardedBy("lock")
/*      */   private void signalNextWaiter() {
/* 1000 */     for (Guard guard = this.activeGuards; guard != null; guard = guard.next) {
/* 1001 */       if (isSatisfied(guard)) {
/* 1002 */         guard.condition.signal();
/*      */         break;
/*      */       } 
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
/*      */   @GuardedBy("lock")
/*      */   private boolean isSatisfied(Guard guard) {
/*      */     try {
/* 1033 */       return guard.isSatisfied();
/* 1034 */     } catch (Throwable throwable) {
/* 1035 */       signalAllWaiters();
/* 1036 */       throw throwable;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   @GuardedBy("lock")
/*      */   private void signalAllWaiters() {
/* 1043 */     for (Guard guard = this.activeGuards; guard != null; guard = guard.next) {
/* 1044 */       guard.condition.signalAll();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   @GuardedBy("lock")
/*      */   private void beginWaitingFor(Guard guard) {
/* 1051 */     int waiters = guard.waiterCount++;
/* 1052 */     if (waiters == 0) {
/*      */       
/* 1054 */       guard.next = this.activeGuards;
/* 1055 */       this.activeGuards = guard;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   @GuardedBy("lock")
/*      */   private void endWaitingFor(Guard guard) {
/* 1062 */     int waiters = --guard.waiterCount;
/* 1063 */     if (waiters == 0)
/*      */     {
/* 1065 */       for (Guard p = this.activeGuards, pred = null;; pred = p, p = p.next) {
/* 1066 */         if (p == guard) {
/* 1067 */           if (pred == null) {
/* 1068 */             this.activeGuards = p.next;
/*      */           } else {
/* 1070 */             pred.next = p.next;
/*      */           } 
/* 1072 */           p.next = null;
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GuardedBy("lock")
/*      */   private void await(Guard guard, boolean signalBeforeWaiting) throws InterruptedException {
/* 1087 */     if (signalBeforeWaiting) {
/* 1088 */       signalNextWaiter();
/*      */     }
/* 1090 */     beginWaitingFor(guard);
/*      */     try {
/*      */       do {
/* 1093 */         guard.condition.await();
/* 1094 */       } while (!guard.isSatisfied());
/*      */     } finally {
/* 1096 */       endWaitingFor(guard);
/*      */     } 
/*      */   }
/*      */   
/*      */   @GuardedBy("lock")
/*      */   private void awaitUninterruptibly(Guard guard, boolean signalBeforeWaiting) {
/* 1102 */     if (signalBeforeWaiting) {
/* 1103 */       signalNextWaiter();
/*      */     }
/* 1105 */     beginWaitingFor(guard);
/*      */     try {
/*      */       do {
/* 1108 */         guard.condition.awaitUninterruptibly();
/* 1109 */       } while (!guard.isSatisfied());
/*      */     } finally {
/* 1111 */       endWaitingFor(guard);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @GuardedBy("lock")
/*      */   private boolean awaitNanos(Guard guard, long nanos, boolean signalBeforeWaiting) throws InterruptedException {
/* 1119 */     boolean firstTime = true;
/*      */     
/*      */     try { while (true) {
/* 1122 */         if (nanos <= 0L) {
/* 1123 */           return false;
/*      */         }
/* 1125 */         if (firstTime) {
/* 1126 */           if (signalBeforeWaiting) {
/* 1127 */             signalNextWaiter();
/*      */           }
/* 1129 */           beginWaitingFor(guard);
/* 1130 */           firstTime = false;
/*      */         } 
/* 1132 */         nanos = guard.condition.awaitNanos(nanos);
/* 1133 */         if (guard.isSatisfied())
/* 1134 */           return true; 
/*      */       }  }
/* 1136 */     finally { if (!firstTime)
/* 1137 */         endWaitingFor(guard);  }
/*      */   
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\Monitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */