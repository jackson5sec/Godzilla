/*     */ package org.springframework.core;
/*     */ 
/*     */ import io.reactivex.BackpressureStrategy;
/*     */ import io.reactivex.Completable;
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.Maybe;
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.Single;
/*     */ import io.reactivex.rxjava3.core.BackpressureStrategy;
/*     */ import io.reactivex.rxjava3.core.Completable;
/*     */ import io.reactivex.rxjava3.core.Flowable;
/*     */ import io.reactivex.rxjava3.core.Maybe;
/*     */ import io.reactivex.rxjava3.core.Observable;
/*     */ import io.reactivex.rxjava3.core.Single;
/*     */ import io.smallrye.mutiny.Multi;
/*     */ import io.smallrye.mutiny.Uni;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import kotlinx.coroutines.CompletableDeferredKt;
/*     */ import kotlinx.coroutines.Deferred;
/*     */ import kotlinx.coroutines.flow.Flow;
/*     */ import kotlinx.coroutines.flow.FlowKt;
/*     */ import kotlinx.coroutines.reactive.ReactiveFlowKt;
/*     */ import kotlinx.coroutines.reactor.ReactorFlowKt;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import reactor.blockhound.BlockHound;
/*     */ import reactor.blockhound.integration.BlockHoundIntegration;
/*     */ import reactor.core.publisher.Flux;
/*     */ import reactor.core.publisher.Mono;
/*     */ import rx.Completable;
/*     */ import rx.Observable;
/*     */ import rx.RxReactiveStreams;
/*     */ import rx.Single;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReactiveAdapterRegistry
/*     */ {
/*     */   @Nullable
/*     */   private static volatile ReactiveAdapterRegistry sharedInstance;
/*     */   private static final boolean reactorPresent;
/*     */   private static final boolean rxjava1Present;
/*     */   private static final boolean rxjava2Present;
/*     */   private static final boolean rxjava3Present;
/*     */   private static final boolean flowPublisherPresent;
/*     */   private static final boolean kotlinCoroutinesPresent;
/*     */   private static final boolean mutinyPresent;
/*     */   
/*     */   static {
/*  77 */     ClassLoader classLoader = ReactiveAdapterRegistry.class.getClassLoader();
/*  78 */     reactorPresent = ClassUtils.isPresent("reactor.core.publisher.Flux", classLoader);
/*  79 */     flowPublisherPresent = ClassUtils.isPresent("java.util.concurrent.Flow.Publisher", classLoader);
/*     */     
/*  81 */     rxjava1Present = (ClassUtils.isPresent("rx.Observable", classLoader) && ClassUtils.isPresent("rx.RxReactiveStreams", classLoader));
/*  82 */     rxjava2Present = ClassUtils.isPresent("io.reactivex.Flowable", classLoader);
/*  83 */     rxjava3Present = ClassUtils.isPresent("io.reactivex.rxjava3.core.Flowable", classLoader);
/*  84 */     kotlinCoroutinesPresent = ClassUtils.isPresent("kotlinx.coroutines.reactor.MonoKt", classLoader);
/*  85 */     mutinyPresent = ClassUtils.isPresent("io.smallrye.mutiny.Multi", classLoader);
/*     */   }
/*     */   
/*  88 */   private final List<ReactiveAdapter> adapters = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReactiveAdapterRegistry() {
/*  97 */     if (reactorPresent) {
/*  98 */       (new ReactorRegistrar()).registerAdapters(this);
/*  99 */       if (flowPublisherPresent)
/*     */       {
/* 101 */         (new ReactorJdkFlowAdapterRegistrar()).registerAdapter(this);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 106 */     if (rxjava1Present) {
/* 107 */       (new RxJava1Registrar()).registerAdapters(this);
/*     */     }
/* 109 */     if (rxjava2Present) {
/* 110 */       (new RxJava2Registrar()).registerAdapters(this);
/*     */     }
/* 112 */     if (rxjava3Present) {
/* 113 */       (new RxJava3Registrar()).registerAdapters(this);
/*     */     }
/*     */ 
/*     */     
/* 117 */     if (reactorPresent && kotlinCoroutinesPresent) {
/* 118 */       (new CoroutinesRegistrar()).registerAdapters(this);
/*     */     }
/*     */ 
/*     */     
/* 122 */     if (mutinyPresent) {
/* 123 */       (new MutinyRegistrar()).registerAdapters(this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasAdapters() {
/* 132 */     return !this.adapters.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerReactiveType(ReactiveTypeDescriptor descriptor, Function<Object, Publisher<?>> toAdapter, Function<Publisher<?>, Object> fromAdapter) {
/* 143 */     if (reactorPresent) {
/* 144 */       this.adapters.add(new ReactorAdapter(descriptor, toAdapter, fromAdapter));
/*     */     } else {
/*     */       
/* 147 */       this.adapters.add(new ReactiveAdapter(descriptor, toAdapter, fromAdapter));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ReactiveAdapter getAdapter(Class<?> reactiveType) {
/* 157 */     return getAdapter(reactiveType, null);
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
/*     */   @Nullable
/*     */   public ReactiveAdapter getAdapter(@Nullable Class<?> reactiveType, @Nullable Object source) {
/* 171 */     if (this.adapters.isEmpty()) {
/* 172 */       return null;
/*     */     }
/*     */     
/* 175 */     Object sourceToUse = (source instanceof Optional) ? ((Optional)source).orElse(null) : source;
/* 176 */     Class<?> clazz = (sourceToUse != null) ? sourceToUse.getClass() : reactiveType;
/* 177 */     if (clazz == null) {
/* 178 */       return null;
/*     */     }
/* 180 */     for (ReactiveAdapter adapter : this.adapters) {
/* 181 */       if (adapter.getReactiveType() == clazz) {
/* 182 */         return adapter;
/*     */       }
/*     */     } 
/* 185 */     for (ReactiveAdapter adapter : this.adapters) {
/* 186 */       if (adapter.getReactiveType().isAssignableFrom(clazz)) {
/* 187 */         return adapter;
/*     */       }
/*     */     } 
/* 190 */     return null;
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
/*     */   public static ReactiveAdapterRegistry getSharedInstance() {
/* 205 */     ReactiveAdapterRegistry registry = sharedInstance;
/* 206 */     if (registry == null) {
/* 207 */       synchronized (ReactiveAdapterRegistry.class) {
/* 208 */         registry = sharedInstance;
/* 209 */         if (registry == null) {
/* 210 */           registry = new ReactiveAdapterRegistry();
/* 211 */           sharedInstance = registry;
/*     */         } 
/*     */       } 
/*     */     }
/* 215 */     return registry;
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
/*     */   private static class ReactorAdapter
/*     */     extends ReactiveAdapter
/*     */   {
/*     */     ReactorAdapter(ReactiveTypeDescriptor descriptor, Function<Object, Publisher<?>> toPublisherFunction, Function<Publisher<?>, Object> fromPublisherFunction) {
/* 231 */       super(descriptor, toPublisherFunction, fromPublisherFunction);
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> Publisher<T> toPublisher(@Nullable Object source) {
/* 236 */       Publisher<T> publisher = super.toPublisher(source);
/* 237 */       return isMultiValue() ? (Publisher<T>)Flux.from(publisher) : (Publisher<T>)Mono.from(publisher);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class ReactorRegistrar
/*     */   {
/*     */     private ReactorRegistrar() {}
/*     */     
/*     */     void registerAdapters(ReactiveAdapterRegistry registry) {
/* 247 */       registry.registerReactiveType(
/* 248 */           ReactiveTypeDescriptor.singleOptionalValue(Mono.class, Mono::empty), source -> (Publisher)source, Mono::from);
/*     */ 
/*     */ 
/*     */       
/* 252 */       registry.registerReactiveType(
/* 253 */           ReactiveTypeDescriptor.multiValue(Flux.class, Flux::empty), source -> (Publisher)source, Flux::from);
/*     */ 
/*     */ 
/*     */       
/* 257 */       registry.registerReactiveType(
/* 258 */           ReactiveTypeDescriptor.multiValue(Publisher.class, Flux::empty), source -> (Publisher)source, source -> source);
/*     */ 
/*     */ 
/*     */       
/* 262 */       registry.registerReactiveType(
/* 263 */           ReactiveTypeDescriptor.nonDeferredAsyncValue(CompletionStage.class, EmptyCompletableFuture::new), source -> Mono.fromCompletionStage((CompletionStage)source), source -> Mono.from(source).toFuture());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class EmptyCompletableFuture<T>
/*     */     extends CompletableFuture<T>
/*     */   {
/*     */     EmptyCompletableFuture() {
/* 273 */       complete(null);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class ReactorJdkFlowAdapterRegistrar
/*     */   {
/*     */     private ReactorJdkFlowAdapterRegistrar() {}
/*     */     
/*     */     void registerAdapter(ReactiveAdapterRegistry registry) {
/*     */       try {
/* 284 */         String publisherName = "java.util.concurrent.Flow.Publisher";
/* 285 */         Class<?> publisherClass = ClassUtils.forName(publisherName, getClass().getClassLoader());
/*     */         
/* 287 */         String adapterName = "reactor.adapter.JdkFlowAdapter";
/* 288 */         Class<?> flowAdapterClass = ClassUtils.forName(adapterName, getClass().getClassLoader());
/*     */         
/* 290 */         Method toFluxMethod = flowAdapterClass.getMethod("flowPublisherToFlux", new Class[] { publisherClass });
/* 291 */         Method toFlowMethod = flowAdapterClass.getMethod("publisherToFlowPublisher", new Class[] { Publisher.class });
/* 292 */         Object emptyFlow = ReflectionUtils.invokeMethod(toFlowMethod, null, new Object[] { Flux.empty() });
/*     */         
/* 294 */         registry.registerReactiveType(
/* 295 */             ReactiveTypeDescriptor.multiValue(publisherClass, () -> emptyFlow), source -> (Publisher)ReflectionUtils.invokeMethod(toFluxMethod, null, new Object[] { source }), publisher -> ReflectionUtils.invokeMethod(toFlowMethod, null, new Object[] { publisher }));
/*     */ 
/*     */       
/*     */       }
/* 299 */       catch (Throwable throwable) {}
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class RxJava1Registrar
/*     */   {
/*     */     private RxJava1Registrar() {}
/*     */     
/*     */     void registerAdapters(ReactiveAdapterRegistry registry) {
/* 309 */       registry.registerReactiveType(
/* 310 */           ReactiveTypeDescriptor.multiValue(Observable.class, Observable::empty), source -> RxReactiveStreams.toPublisher((Observable)source), RxReactiveStreams::toObservable);
/*     */ 
/*     */ 
/*     */       
/* 314 */       registry.registerReactiveType(
/* 315 */           ReactiveTypeDescriptor.singleRequiredValue(Single.class), source -> RxReactiveStreams.toPublisher((Single)source), RxReactiveStreams::toSingle);
/*     */ 
/*     */ 
/*     */       
/* 319 */       registry.registerReactiveType(
/* 320 */           ReactiveTypeDescriptor.noValue(Completable.class, Completable::complete), source -> RxReactiveStreams.toPublisher((Completable)source), RxReactiveStreams::toCompletable);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class RxJava2Registrar
/*     */   {
/*     */     private RxJava2Registrar() {}
/*     */     
/*     */     void registerAdapters(ReactiveAdapterRegistry registry) {
/* 330 */       registry.registerReactiveType(
/* 331 */           ReactiveTypeDescriptor.multiValue(Flowable.class, Flowable::empty), source -> (Publisher)source, Flowable::fromPublisher);
/*     */ 
/*     */ 
/*     */       
/* 335 */       registry.registerReactiveType(
/* 336 */           ReactiveTypeDescriptor.multiValue(Observable.class, Observable::empty), source -> ((Observable)source).toFlowable(BackpressureStrategy.BUFFER), Observable::fromPublisher);
/*     */ 
/*     */ 
/*     */       
/* 340 */       registry.registerReactiveType(
/* 341 */           ReactiveTypeDescriptor.singleRequiredValue(Single.class), source -> ((Single)source).toFlowable(), Single::fromPublisher);
/*     */ 
/*     */ 
/*     */       
/* 345 */       registry.registerReactiveType(
/* 346 */           ReactiveTypeDescriptor.singleOptionalValue(Maybe.class, Maybe::empty), source -> ((Maybe)source).toFlowable(), source -> Flowable.fromPublisher(source).toObservable().singleElement());
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 351 */       registry.registerReactiveType(
/* 352 */           ReactiveTypeDescriptor.noValue(Completable.class, Completable::complete), source -> ((Completable)source).toFlowable(), Completable::fromPublisher);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class RxJava3Registrar
/*     */   {
/*     */     private RxJava3Registrar() {}
/*     */     
/*     */     void registerAdapters(ReactiveAdapterRegistry registry) {
/* 362 */       registry.registerReactiveType(
/* 363 */           ReactiveTypeDescriptor.multiValue(Flowable.class, Flowable::empty), source -> (Publisher)source, Flowable::fromPublisher);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 369 */       registry.registerReactiveType(
/* 370 */           ReactiveTypeDescriptor.multiValue(Observable.class, Observable::empty), source -> ((Observable)source).toFlowable(BackpressureStrategy.BUFFER), Observable::fromPublisher);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 377 */       registry.registerReactiveType(
/* 378 */           ReactiveTypeDescriptor.singleRequiredValue(Single.class), source -> ((Single)source).toFlowable(), Single::fromPublisher);
/*     */ 
/*     */ 
/*     */       
/* 382 */       registry.registerReactiveType(
/* 383 */           ReactiveTypeDescriptor.singleOptionalValue(Maybe.class, Maybe::empty), source -> ((Maybe)source).toFlowable(), Maybe::fromPublisher);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 389 */       registry.registerReactiveType(
/* 390 */           ReactiveTypeDescriptor.noValue(Completable.class, Completable::complete), source -> ((Completable)source).toFlowable(), Completable::fromPublisher);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class CoroutinesRegistrar
/*     */   {
/*     */     private CoroutinesRegistrar() {}
/*     */ 
/*     */ 
/*     */     
/*     */     void registerAdapters(ReactiveAdapterRegistry registry) {
/* 403 */       registry.registerReactiveType(
/* 404 */           ReactiveTypeDescriptor.singleOptionalValue(Deferred.class, () -> CompletableDeferredKt.CompletableDeferred(null)), source -> CoroutinesUtils.deferredToMono((Deferred)source), source -> CoroutinesUtils.monoToDeferred(Mono.from(source)));
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 409 */       registry.registerReactiveType(
/* 410 */           ReactiveTypeDescriptor.multiValue(Flow.class, FlowKt::emptyFlow), source -> ReactorFlowKt.asFlux((Flow)source), ReactiveFlowKt::asFlow);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class MutinyRegistrar
/*     */   {
/*     */     private MutinyRegistrar() {}
/*     */     
/*     */     void registerAdapters(ReactiveAdapterRegistry registry) {
/* 420 */       registry.registerReactiveType(
/* 421 */           ReactiveTypeDescriptor.singleOptionalValue(Uni.class, () -> Uni.createFrom().nothing()), uni -> ((Uni)uni).convert().toPublisher(), publisher -> Uni.createFrom().publisher(publisher));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 427 */       registry.registerReactiveType(
/* 428 */           ReactiveTypeDescriptor.multiValue(Multi.class, () -> Multi.createFrom().empty()), multi -> (Publisher)multi, publisher -> Multi.createFrom().publisher(publisher));
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
/*     */   public static class SpringCoreBlockHoundIntegration
/*     */     implements BlockHoundIntegration
/*     */   {
/*     */     public void applyTo(BlockHound.Builder builder) {
/* 452 */       builder.allowBlockingCallsInside("org.springframework.core.LocalVariableTableParameterNameDiscoverer", "inspectClass");
/*     */ 
/*     */       
/* 455 */       String className = "org.springframework.util.ConcurrentReferenceHashMap$Segment";
/* 456 */       builder.allowBlockingCallsInside(className, "doTask");
/* 457 */       builder.allowBlockingCallsInside(className, "clear");
/* 458 */       builder.allowBlockingCallsInside(className, "restructure");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\ReactiveAdapterRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */