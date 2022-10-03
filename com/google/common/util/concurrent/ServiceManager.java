/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicates;
/*     */ import com.google.common.base.Stopwatch;
/*     */ import com.google.common.collect.Collections2;
/*     */ import com.google.common.collect.ImmutableCollection;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableMultimap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.ImmutableSetMultimap;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.MultimapBuilder;
/*     */ import com.google.common.collect.Multimaps;
/*     */ import com.google.common.collect.Multiset;
/*     */ import com.google.common.collect.Ordering;
/*     */ import com.google.common.collect.SetMultimap;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.GuardedBy;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
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
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public final class ServiceManager
/*     */ {
/* 125 */   private static final Logger logger = Logger.getLogger(ServiceManager.class.getName());
/* 126 */   private static final ListenerCallQueue.Event<Listener> HEALTHY_EVENT = new ListenerCallQueue.Event<Listener>()
/*     */     {
/*     */       public void call(ServiceManager.Listener listener)
/*     */       {
/* 130 */         listener.healthy();
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 135 */         return "healthy()";
/*     */       }
/*     */     };
/* 138 */   private static final ListenerCallQueue.Event<Listener> STOPPED_EVENT = new ListenerCallQueue.Event<Listener>()
/*     */     {
/*     */       public void call(ServiceManager.Listener listener)
/*     */       {
/* 142 */         listener.stopped();
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 147 */         return "stopped()";
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
/*     */   private final ServiceManagerState state;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ImmutableList<Service> services;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static abstract class Listener
/*     */   {
/*     */     public void healthy() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void stopped() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void failure(Service service) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServiceManager(Iterable<? extends Service> services) {
/* 205 */     ImmutableList<Service> copy = ImmutableList.copyOf(services);
/* 206 */     if (copy.isEmpty()) {
/*     */ 
/*     */       
/* 209 */       logger.log(Level.WARNING, "ServiceManager configured with no services.  Is your application configured properly?", new EmptyServiceManagerWarning());
/*     */ 
/*     */ 
/*     */       
/* 213 */       copy = ImmutableList.of(new NoOpService());
/*     */     } 
/* 215 */     this.state = new ServiceManagerState((ImmutableCollection<Service>)copy);
/* 216 */     this.services = copy;
/* 217 */     WeakReference<ServiceManagerState> stateReference = new WeakReference<>(this.state);
/* 218 */     for (UnmodifiableIterator<Service> unmodifiableIterator = copy.iterator(); unmodifiableIterator.hasNext(); ) { Service service = unmodifiableIterator.next();
/* 219 */       service.addListener(new ServiceListener(service, stateReference), MoreExecutors.directExecutor());
/*     */ 
/*     */       
/* 222 */       Preconditions.checkArgument((service.state() == Service.State.NEW), "Can only manage NEW services, %s", service); }
/*     */ 
/*     */ 
/*     */     
/* 226 */     this.state.markReady();
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
/*     */   public void addListener(Listener listener, Executor executor) {
/* 253 */     this.state.addListener(listener, executor);
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
/*     */   public void addListener(Listener listener) {
/* 273 */     this.state.addListener(listener, MoreExecutors.directExecutor());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public ServiceManager startAsync() {
/*     */     UnmodifiableIterator<Service> unmodifiableIterator;
/* 286 */     for (unmodifiableIterator = this.services.iterator(); unmodifiableIterator.hasNext(); ) { Service service = unmodifiableIterator.next();
/* 287 */       Service.State state = service.state();
/* 288 */       Preconditions.checkState((state == Service.State.NEW), "Service %s is %s, cannot start it.", service, state); }
/*     */     
/* 290 */     for (unmodifiableIterator = this.services.iterator(); unmodifiableIterator.hasNext(); ) { Service service = unmodifiableIterator.next();
/*     */       try {
/* 292 */         this.state.tryStartTiming(service);
/* 293 */         service.startAsync();
/* 294 */       } catch (IllegalStateException e) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 299 */         logger.log(Level.WARNING, "Unable to start Service " + service, e);
/*     */       }  }
/*     */     
/* 302 */     return this;
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
/*     */   public void awaitHealthy() {
/* 314 */     this.state.awaitHealthy();
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
/*     */   public void awaitHealthy(long timeout, TimeUnit unit) throws TimeoutException {
/* 330 */     this.state.awaitHealthy(timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public ServiceManager stopAsync() {
/* 341 */     for (UnmodifiableIterator<Service> unmodifiableIterator = this.services.iterator(); unmodifiableIterator.hasNext(); ) { Service service = unmodifiableIterator.next();
/* 342 */       service.stopAsync(); }
/*     */     
/* 344 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void awaitStopped() {
/* 353 */     this.state.awaitStopped();
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
/*     */   public void awaitStopped(long timeout, TimeUnit unit) throws TimeoutException {
/* 367 */     this.state.awaitStopped(timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isHealthy() {
/* 377 */     for (UnmodifiableIterator<Service> unmodifiableIterator = this.services.iterator(); unmodifiableIterator.hasNext(); ) { Service service = unmodifiableIterator.next();
/* 378 */       if (!service.isRunning()) {
/* 379 */         return false;
/*     */       } }
/*     */     
/* 382 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableMultimap<Service.State, Service> servicesByState() {
/* 392 */     return this.state.servicesByState();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableMap<Service, Long> startupTimes() {
/* 403 */     return this.state.startupTimes();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 408 */     return MoreObjects.toStringHelper(ServiceManager.class)
/* 409 */       .add("services", Collections2.filter((Collection)this.services, Predicates.not(Predicates.instanceOf(NoOpService.class))))
/* 410 */       .toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class ServiceManagerState
/*     */   {
/* 418 */     final Monitor monitor = new Monitor();
/*     */ 
/*     */     
/*     */     @GuardedBy("monitor")
/* 422 */     final SetMultimap<Service.State, Service> servicesByState = MultimapBuilder.enumKeys(Service.State.class).linkedHashSetValues().build();
/*     */     @GuardedBy("monitor")
/* 424 */     final Multiset<Service.State> states = this.servicesByState
/* 425 */       .keys();
/*     */     
/*     */     @GuardedBy("monitor")
/* 428 */     final Map<Service, Stopwatch> startupTimers = Maps.newIdentityHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @GuardedBy("monitor")
/*     */     boolean ready;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @GuardedBy("monitor")
/*     */     boolean transitioned;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final int numberOfServices;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 455 */     final Monitor.Guard awaitHealthGuard = new AwaitHealthGuard();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final class AwaitHealthGuard
/*     */       extends Monitor.Guard
/*     */     {
/*     */       @GuardedBy("ServiceManagerState.this.monitor")
/*     */       public boolean isSatisfied() {
/* 467 */         return (ServiceManager.ServiceManagerState.this.states.count(Service.State.RUNNING) == ServiceManager.ServiceManagerState.this.numberOfServices || ServiceManager.ServiceManagerState.this.states
/* 468 */           .contains(Service.State.STOPPING) || ServiceManager.ServiceManagerState.this.states
/* 469 */           .contains(Service.State.TERMINATED) || ServiceManager.ServiceManagerState.this.states
/* 470 */           .contains(Service.State.FAILED));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 475 */     final Monitor.Guard stoppedGuard = new StoppedGuard();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final class StoppedGuard
/*     */       extends Monitor.Guard
/*     */     {
/*     */       @GuardedBy("ServiceManagerState.this.monitor")
/*     */       public boolean isSatisfied() {
/* 486 */         return (ServiceManager.ServiceManagerState.this.states.count(Service.State.TERMINATED) + ServiceManager.ServiceManagerState.this.states.count(Service.State.FAILED) == ServiceManager.ServiceManagerState.this.numberOfServices);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 491 */     final ListenerCallQueue<ServiceManager.Listener> listeners = new ListenerCallQueue<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     ServiceManagerState(ImmutableCollection<Service> services) {
/* 500 */       this.numberOfServices = services.size();
/* 501 */       this.servicesByState.putAll(Service.State.NEW, (Iterable)services);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void tryStartTiming(Service service) {
/* 509 */       this.monitor.enter();
/*     */       try {
/* 511 */         Stopwatch stopwatch = this.startupTimers.get(service);
/* 512 */         if (stopwatch == null) {
/* 513 */           this.startupTimers.put(service, Stopwatch.createStarted());
/*     */         }
/*     */       } finally {
/* 516 */         this.monitor.leave();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void markReady() {
/* 525 */       this.monitor.enter();
/*     */       try {
/* 527 */         if (!this.transitioned) {
/*     */           
/* 529 */           this.ready = true;
/*     */         } else {
/*     */           
/* 532 */           List<Service> servicesInBadStates = Lists.newArrayList();
/* 533 */           for (UnmodifiableIterator<Service> unmodifiableIterator = servicesByState().values().iterator(); unmodifiableIterator.hasNext(); ) { Service service = unmodifiableIterator.next();
/* 534 */             if (service.state() != Service.State.NEW) {
/* 535 */               servicesInBadStates.add(service);
/*     */             } }
/*     */           
/* 538 */           throw new IllegalArgumentException("Services started transitioning asynchronously before the ServiceManager was constructed: " + servicesInBadStates);
/*     */         }
/*     */       
/*     */       }
/*     */       finally {
/*     */         
/* 544 */         this.monitor.leave();
/*     */       } 
/*     */     }
/*     */     
/*     */     void addListener(ServiceManager.Listener listener, Executor executor) {
/* 549 */       this.listeners.addListener(listener, executor);
/*     */     }
/*     */     
/*     */     void awaitHealthy() {
/* 553 */       this.monitor.enterWhenUninterruptibly(this.awaitHealthGuard);
/*     */       try {
/* 555 */         checkHealthy();
/*     */       } finally {
/* 557 */         this.monitor.leave();
/*     */       } 
/*     */     }
/*     */     
/*     */     void awaitHealthy(long timeout, TimeUnit unit) throws TimeoutException {
/* 562 */       this.monitor.enter();
/*     */       try {
/* 564 */         if (!this.monitor.waitForUninterruptibly(this.awaitHealthGuard, timeout, unit)) {
/* 565 */           throw new TimeoutException("Timeout waiting for the services to become healthy. The following services have not started: " + 
/*     */ 
/*     */               
/* 568 */               Multimaps.filterKeys(this.servicesByState, Predicates.in(ImmutableSet.of(Service.State.NEW, Service.State.STARTING))));
/*     */         }
/* 570 */         checkHealthy();
/*     */       } finally {
/* 572 */         this.monitor.leave();
/*     */       } 
/*     */     }
/*     */     
/*     */     void awaitStopped() {
/* 577 */       this.monitor.enterWhenUninterruptibly(this.stoppedGuard);
/* 578 */       this.monitor.leave();
/*     */     }
/*     */     
/*     */     void awaitStopped(long timeout, TimeUnit unit) throws TimeoutException {
/* 582 */       this.monitor.enter();
/*     */       try {
/* 584 */         if (!this.monitor.waitForUninterruptibly(this.stoppedGuard, timeout, unit)) {
/* 585 */           throw new TimeoutException("Timeout waiting for the services to stop. The following services have not stopped: " + 
/*     */ 
/*     */               
/* 588 */               Multimaps.filterKeys(this.servicesByState, Predicates.not(Predicates.in(EnumSet.of(Service.State.TERMINATED, Service.State.FAILED)))));
/*     */         }
/*     */       } finally {
/* 591 */         this.monitor.leave();
/*     */       } 
/*     */     }
/*     */     
/*     */     ImmutableMultimap<Service.State, Service> servicesByState() {
/* 596 */       ImmutableSetMultimap.Builder<Service.State, Service> builder = ImmutableSetMultimap.builder();
/* 597 */       this.monitor.enter();
/*     */       try {
/* 599 */         for (Map.Entry<Service.State, Service> entry : (Iterable<Map.Entry<Service.State, Service>>)this.servicesByState.entries()) {
/* 600 */           if (!(entry.getValue() instanceof ServiceManager.NoOpService)) {
/* 601 */             builder.put(entry);
/*     */           }
/*     */         } 
/*     */       } finally {
/* 605 */         this.monitor.leave();
/*     */       } 
/* 607 */       return (ImmutableMultimap<Service.State, Service>)builder.build();
/*     */     }
/*     */     
/*     */     ImmutableMap<Service, Long> startupTimes() {
/*     */       List<Map.Entry<Service, Long>> loadTimes;
/* 612 */       this.monitor.enter();
/*     */       try {
/* 614 */         loadTimes = Lists.newArrayListWithCapacity(this.startupTimers.size());
/*     */         
/* 616 */         for (Map.Entry<Service, Stopwatch> entry : this.startupTimers.entrySet()) {
/* 617 */           Service service = entry.getKey();
/* 618 */           Stopwatch stopWatch = entry.getValue();
/* 619 */           if (!stopWatch.isRunning() && !(service instanceof ServiceManager.NoOpService)) {
/* 620 */             loadTimes.add(Maps.immutableEntry(service, Long.valueOf(stopWatch.elapsed(TimeUnit.MILLISECONDS))));
/*     */           }
/*     */         } 
/*     */       } finally {
/* 624 */         this.monitor.leave();
/*     */       } 
/* 626 */       Collections.sort(loadTimes, 
/*     */           
/* 628 */           (Comparator<? super Map.Entry<Service, Long>>)Ordering.natural()
/* 629 */           .onResultOf(new Function<Map.Entry<Service, Long>, Long>()
/*     */             {
/*     */               public Long apply(Map.Entry<Service, Long> input)
/*     */               {
/* 633 */                 return input.getValue();
/*     */               }
/*     */             }));
/* 636 */       return ImmutableMap.copyOf(loadTimes);
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
/*     */     void transitionService(Service service, Service.State from, Service.State to) {
/* 652 */       Preconditions.checkNotNull(service);
/* 653 */       Preconditions.checkArgument((from != to));
/* 654 */       this.monitor.enter();
/*     */       try {
/* 656 */         this.transitioned = true;
/* 657 */         if (!this.ready) {
/*     */           return;
/*     */         }
/*     */         
/* 661 */         Preconditions.checkState(this.servicesByState
/* 662 */             .remove(from, service), "Service %s not at the expected location in the state map %s", service, from);
/*     */ 
/*     */ 
/*     */         
/* 666 */         Preconditions.checkState(this.servicesByState
/* 667 */             .put(to, service), "Service %s in the state map unexpectedly at %s", service, to);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 672 */         Stopwatch stopwatch = this.startupTimers.get(service);
/* 673 */         if (stopwatch == null) {
/*     */           
/* 675 */           stopwatch = Stopwatch.createStarted();
/* 676 */           this.startupTimers.put(service, stopwatch);
/*     */         } 
/* 678 */         if (to.compareTo(Service.State.RUNNING) >= 0 && stopwatch.isRunning()) {
/*     */           
/* 680 */           stopwatch.stop();
/* 681 */           if (!(service instanceof ServiceManager.NoOpService)) {
/* 682 */             ServiceManager.logger.log(Level.FINE, "Started {0} in {1}.", new Object[] { service, stopwatch });
/*     */           }
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 688 */         if (to == Service.State.FAILED) {
/* 689 */           enqueueFailedEvent(service);
/*     */         }
/*     */         
/* 692 */         if (this.states.count(Service.State.RUNNING) == this.numberOfServices) {
/*     */ 
/*     */           
/* 695 */           enqueueHealthyEvent();
/* 696 */         } else if (this.states.count(Service.State.TERMINATED) + this.states.count(Service.State.FAILED) == this.numberOfServices) {
/* 697 */           enqueueStoppedEvent();
/*     */         } 
/*     */       } finally {
/* 700 */         this.monitor.leave();
/*     */         
/* 702 */         dispatchListenerEvents();
/*     */       } 
/*     */     }
/*     */     
/*     */     void enqueueStoppedEvent() {
/* 707 */       this.listeners.enqueue(ServiceManager.STOPPED_EVENT);
/*     */     }
/*     */     
/*     */     void enqueueHealthyEvent() {
/* 711 */       this.listeners.enqueue(ServiceManager.HEALTHY_EVENT);
/*     */     }
/*     */     
/*     */     void enqueueFailedEvent(final Service service) {
/* 715 */       this.listeners.enqueue(new ListenerCallQueue.Event<ServiceManager.Listener>()
/*     */           {
/*     */             public void call(ServiceManager.Listener listener)
/*     */             {
/* 719 */               listener.failure(service);
/*     */             }
/*     */ 
/*     */             
/*     */             public String toString() {
/* 724 */               return "failed({service=" + service + "})";
/*     */             }
/*     */           });
/*     */     }
/*     */ 
/*     */     
/*     */     void dispatchListenerEvents() {
/* 731 */       Preconditions.checkState(
/* 732 */           !this.monitor.isOccupiedByCurrentThread(), "It is incorrect to execute listeners with the monitor held.");
/*     */       
/* 734 */       this.listeners.dispatch();
/*     */     }
/*     */     
/*     */     @GuardedBy("monitor")
/*     */     void checkHealthy() {
/* 739 */       if (this.states.count(Service.State.RUNNING) != this.numberOfServices) {
/*     */ 
/*     */ 
/*     */         
/* 743 */         IllegalStateException exception = new IllegalStateException("Expected to be healthy after starting. The following services are not running: " + Multimaps.filterKeys(this.servicesByState, Predicates.not(Predicates.equalTo(Service.State.RUNNING))));
/* 744 */         for (Service service : this.servicesByState.get(Service.State.FAILED)) {
/* 745 */           exception.addSuppressed(new ServiceManager.FailedService(service));
/*     */         }
/* 747 */         throw exception;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class ServiceListener
/*     */     extends Service.Listener
/*     */   {
/*     */     final Service service;
/*     */ 
/*     */     
/*     */     final WeakReference<ServiceManager.ServiceManagerState> state;
/*     */ 
/*     */     
/*     */     ServiceListener(Service service, WeakReference<ServiceManager.ServiceManagerState> state) {
/* 764 */       this.service = service;
/* 765 */       this.state = state;
/*     */     }
/*     */ 
/*     */     
/*     */     public void starting() {
/* 770 */       ServiceManager.ServiceManagerState state = this.state.get();
/* 771 */       if (state != null) {
/* 772 */         state.transitionService(this.service, Service.State.NEW, Service.State.STARTING);
/* 773 */         if (!(this.service instanceof ServiceManager.NoOpService)) {
/* 774 */           ServiceManager.logger.log(Level.FINE, "Starting {0}.", this.service);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void running() {
/* 781 */       ServiceManager.ServiceManagerState state = this.state.get();
/* 782 */       if (state != null) {
/* 783 */         state.transitionService(this.service, Service.State.STARTING, Service.State.RUNNING);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void stopping(Service.State from) {
/* 789 */       ServiceManager.ServiceManagerState state = this.state.get();
/* 790 */       if (state != null) {
/* 791 */         state.transitionService(this.service, from, Service.State.STOPPING);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void terminated(Service.State from) {
/* 797 */       ServiceManager.ServiceManagerState state = this.state.get();
/* 798 */       if (state != null) {
/* 799 */         if (!(this.service instanceof ServiceManager.NoOpService)) {
/* 800 */           ServiceManager.logger.log(Level.FINE, "Service {0} has terminated. Previous state was: {1}", new Object[] { this.service, from });
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 805 */         state.transitionService(this.service, from, Service.State.TERMINATED);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void failed(Service.State from, Throwable failure) {
/* 811 */       ServiceManager.ServiceManagerState state = this.state.get();
/* 812 */       if (state != null) {
/*     */ 
/*     */         
/* 815 */         boolean log = !(this.service instanceof ServiceManager.NoOpService);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 820 */         int i = log & ((from != Service.State.STARTING) ? 1 : 0);
/* 821 */         if (i != 0) {
/* 822 */           ServiceManager.logger.log(Level.SEVERE, "Service " + this.service + " has failed in the " + from + " state.", failure);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 827 */         state.transitionService(this.service, from, Service.State.FAILED);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class NoOpService
/*     */     extends AbstractService
/*     */   {
/*     */     private NoOpService() {}
/*     */ 
/*     */ 
/*     */     
/*     */     protected void doStart() {
/* 843 */       notifyStarted();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void doStop() {
/* 848 */       notifyStopped();
/*     */     } }
/*     */   
/*     */   private static final class EmptyServiceManagerWarning extends Throwable {
/*     */     private EmptyServiceManagerWarning() {}
/*     */   }
/*     */   
/*     */   private static final class FailedService extends Throwable {
/*     */     FailedService(Service service) {
/* 857 */       super(service
/* 858 */           .toString(), service
/* 859 */           .failureCause(), false, false);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\ServiceManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */