/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.util.function.Function;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReactiveAdapter
/*     */ {
/*     */   private final ReactiveTypeDescriptor descriptor;
/*     */   private final Function<Object, Publisher<?>> toPublisherFunction;
/*     */   private final Function<Publisher<?>, Object> fromPublisherFunction;
/*     */   
/*     */   public ReactiveAdapter(ReactiveTypeDescriptor descriptor, Function<Object, Publisher<?>> toPublisherFunction, Function<Publisher<?>, Object> fromPublisherFunction) {
/*  55 */     Assert.notNull(descriptor, "'descriptor' is required");
/*  56 */     Assert.notNull(toPublisherFunction, "'toPublisherFunction' is required");
/*  57 */     Assert.notNull(fromPublisherFunction, "'fromPublisherFunction' is required");
/*     */     
/*  59 */     this.descriptor = descriptor;
/*  60 */     this.toPublisherFunction = toPublisherFunction;
/*  61 */     this.fromPublisherFunction = fromPublisherFunction;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReactiveTypeDescriptor getDescriptor() {
/*  69 */     return this.descriptor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getReactiveType() {
/*  76 */     return getDescriptor().getReactiveType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMultiValue() {
/*  83 */     return getDescriptor().isMultiValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNoValue() {
/*  90 */     return getDescriptor().isNoValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supportsEmpty() {
/*  97 */     return getDescriptor().supportsEmpty();
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
/*     */   public <T> Publisher<T> toPublisher(@Nullable Object source) {
/* 109 */     if (source == null) {
/* 110 */       source = getDescriptor().getEmptyValue();
/*     */     }
/* 112 */     return (Publisher<T>)this.toPublisherFunction.apply(source);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object fromPublisher(Publisher<?> publisher) {
/* 121 */     return this.fromPublisherFunction.apply(publisher);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\ReactiveAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */