/*     */ package com.google.common.eventbus;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.common.cache.CacheBuilder;
/*     */ import com.google.common.cache.CacheLoader;
/*     */ import com.google.common.cache.LoadingCache;
/*     */ import com.google.common.collect.HashMultimap;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Multimap;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.common.reflect.TypeToken;
/*     */ import com.google.common.util.concurrent.UncheckedExecutionException;
/*     */ import com.google.j2objc.annotations.Weak;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.CopyOnWriteArraySet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class SubscriberRegistry
/*     */ {
/*  63 */   private final ConcurrentMap<Class<?>, CopyOnWriteArraySet<Subscriber>> subscribers = Maps.newConcurrentMap();
/*     */   
/*     */   @Weak
/*     */   private final EventBus bus;
/*     */   
/*     */   SubscriberRegistry(EventBus bus) {
/*  69 */     this.bus = (EventBus)Preconditions.checkNotNull(bus);
/*     */   }
/*     */ 
/*     */   
/*     */   void register(Object listener) {
/*  74 */     Multimap<Class<?>, Subscriber> listenerMethods = findAllSubscribers(listener);
/*     */     
/*  76 */     for (Map.Entry<Class<?>, Collection<Subscriber>> entry : (Iterable<Map.Entry<Class<?>, Collection<Subscriber>>>)listenerMethods.asMap().entrySet()) {
/*  77 */       Class<?> eventType = entry.getKey();
/*  78 */       Collection<Subscriber> eventMethodsInListener = entry.getValue();
/*     */       
/*  80 */       CopyOnWriteArraySet<Subscriber> eventSubscribers = this.subscribers.get(eventType);
/*     */       
/*  82 */       if (eventSubscribers == null) {
/*  83 */         CopyOnWriteArraySet<Subscriber> newSet = new CopyOnWriteArraySet<>();
/*     */         
/*  85 */         eventSubscribers = (CopyOnWriteArraySet<Subscriber>)MoreObjects.firstNonNull(this.subscribers.putIfAbsent(eventType, newSet), newSet);
/*     */       } 
/*     */       
/*  88 */       eventSubscribers.addAll(eventMethodsInListener);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void unregister(Object listener) {
/*  94 */     Multimap<Class<?>, Subscriber> listenerMethods = findAllSubscribers(listener);
/*     */     
/*  96 */     for (Map.Entry<Class<?>, Collection<Subscriber>> entry : (Iterable<Map.Entry<Class<?>, Collection<Subscriber>>>)listenerMethods.asMap().entrySet()) {
/*  97 */       Class<?> eventType = entry.getKey();
/*  98 */       Collection<Subscriber> listenerMethodsForType = entry.getValue();
/*     */       
/* 100 */       CopyOnWriteArraySet<Subscriber> currentSubscribers = this.subscribers.get(eventType);
/* 101 */       if (currentSubscribers == null || !currentSubscribers.removeAll(listenerMethodsForType))
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 106 */         throw new IllegalArgumentException("missing event subscriber for an annotated method. Is " + listener + " registered?");
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   Set<Subscriber> getSubscribersForTesting(Class<?> eventType) {
/* 117 */     return (Set<Subscriber>)MoreObjects.firstNonNull(this.subscribers.get(eventType), ImmutableSet.of());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Iterator<Subscriber> getSubscribers(Object event) {
/* 125 */     ImmutableSet<Class<?>> eventTypes = flattenHierarchy(event.getClass());
/*     */ 
/*     */     
/* 128 */     List<Iterator<Subscriber>> subscriberIterators = Lists.newArrayListWithCapacity(eventTypes.size());
/*     */     
/* 130 */     for (UnmodifiableIterator<Class<?>> unmodifiableIterator = eventTypes.iterator(); unmodifiableIterator.hasNext(); ) { Class<?> eventType = unmodifiableIterator.next();
/* 131 */       CopyOnWriteArraySet<Subscriber> eventSubscribers = this.subscribers.get(eventType);
/* 132 */       if (eventSubscribers != null)
/*     */       {
/* 134 */         subscriberIterators.add(eventSubscribers.iterator());
/*     */       } }
/*     */ 
/*     */     
/* 138 */     return Iterators.concat(subscriberIterators.iterator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 148 */   private static final LoadingCache<Class<?>, ImmutableList<Method>> subscriberMethodsCache = CacheBuilder.newBuilder()
/* 149 */     .weakKeys()
/* 150 */     .build(new CacheLoader<Class<?>, ImmutableList<Method>>()
/*     */       {
/*     */         public ImmutableList<Method> load(Class<?> concreteClass) throws Exception
/*     */         {
/* 154 */           return SubscriberRegistry.getAnnotatedMethodsNotCached(concreteClass);
/*     */         }
/*     */       });
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Multimap<Class<?>, Subscriber> findAllSubscribers(Object listener) {
/* 162 */     HashMultimap hashMultimap = HashMultimap.create();
/* 163 */     Class<?> clazz = listener.getClass();
/* 164 */     for (UnmodifiableIterator<Method> unmodifiableIterator = getAnnotatedMethods(clazz).iterator(); unmodifiableIterator.hasNext(); ) { Method method = unmodifiableIterator.next();
/* 165 */       Class<?>[] parameterTypes = method.getParameterTypes();
/* 166 */       Class<?> eventType = parameterTypes[0];
/* 167 */       hashMultimap.put(eventType, Subscriber.create(this.bus, listener, method)); }
/*     */     
/* 169 */     return (Multimap<Class<?>, Subscriber>)hashMultimap;
/*     */   }
/*     */   
/*     */   private static ImmutableList<Method> getAnnotatedMethods(Class<?> clazz) {
/* 173 */     return (ImmutableList<Method>)subscriberMethodsCache.getUnchecked(clazz);
/*     */   }
/*     */   
/*     */   private static ImmutableList<Method> getAnnotatedMethodsNotCached(Class<?> clazz) {
/* 177 */     Set<? extends Class<?>> supertypes = TypeToken.of(clazz).getTypes().rawTypes();
/* 178 */     Map<MethodIdentifier, Method> identifiers = Maps.newHashMap();
/* 179 */     for (Class<?> supertype : supertypes) {
/* 180 */       for (Method method : supertype.getDeclaredMethods()) {
/* 181 */         if (method.isAnnotationPresent((Class)Subscribe.class) && !method.isSynthetic()) {
/*     */           
/* 183 */           Class<?>[] parameterTypes = method.getParameterTypes();
/* 184 */           Preconditions.checkArgument((parameterTypes.length == 1), "Method %s has @Subscribe annotation but has %s parameters.Subscriber methods must have exactly 1 parameter.", method, parameterTypes.length);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 191 */           MethodIdentifier ident = new MethodIdentifier(method);
/* 192 */           if (!identifiers.containsKey(ident)) {
/* 193 */             identifiers.put(ident, method);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 198 */     return ImmutableList.copyOf(identifiers.values());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 203 */   private static final LoadingCache<Class<?>, ImmutableSet<Class<?>>> flattenHierarchyCache = CacheBuilder.newBuilder()
/* 204 */     .weakKeys()
/* 205 */     .build(new CacheLoader<Class<?>, ImmutableSet<Class<?>>>()
/*     */       {
/*     */ 
/*     */         
/*     */         public ImmutableSet<Class<?>> load(Class<?> concreteClass)
/*     */         {
/* 211 */           return ImmutableSet.copyOf(
/* 212 */               TypeToken.of(concreteClass).getTypes().rawTypes());
/*     */         }
/*     */       });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static ImmutableSet<Class<?>> flattenHierarchy(Class<?> concreteClass) {
/*     */     try {
/* 223 */       return (ImmutableSet<Class<?>>)flattenHierarchyCache.getUnchecked(concreteClass);
/* 224 */     } catch (UncheckedExecutionException e) {
/* 225 */       throw Throwables.propagate(e.getCause());
/*     */     } 
/*     */   }
/*     */   
/*     */   private static final class MethodIdentifier
/*     */   {
/*     */     private final String name;
/*     */     private final List<Class<?>> parameterTypes;
/*     */     
/*     */     MethodIdentifier(Method method) {
/* 235 */       this.name = method.getName();
/* 236 */       this.parameterTypes = Arrays.asList(method.getParameterTypes());
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 241 */       return Objects.hashCode(new Object[] { this.name, this.parameterTypes });
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 246 */       if (o instanceof MethodIdentifier) {
/* 247 */         MethodIdentifier ident = (MethodIdentifier)o;
/* 248 */         return (this.name.equals(ident.name) && this.parameterTypes.equals(ident.parameterTypes));
/*     */       } 
/* 250 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\eventbus\SubscriberRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */