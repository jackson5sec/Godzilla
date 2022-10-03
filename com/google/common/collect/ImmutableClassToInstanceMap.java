/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Primitives;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.Immutable;
/*     */ import java.io.Serializable;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable(containerOf = {"B"})
/*     */ @GwtIncompatible
/*     */ public final class ImmutableClassToInstanceMap<B>
/*     */   extends ForwardingMap<Class<? extends B>, B>
/*     */   implements ClassToInstanceMap<B>, Serializable
/*     */ {
/*  41 */   private static final ImmutableClassToInstanceMap<Object> EMPTY = new ImmutableClassToInstanceMap(
/*  42 */       ImmutableMap.of());
/*     */ 
/*     */ 
/*     */   
/*     */   private final ImmutableMap<Class<? extends B>, B> delegate;
/*     */ 
/*     */ 
/*     */   
/*     */   public static <B> ImmutableClassToInstanceMap<B> of() {
/*  51 */     return (ImmutableClassToInstanceMap)EMPTY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <B, T extends B> ImmutableClassToInstanceMap<B> of(Class<T> type, T value) {
/*  60 */     ImmutableMap<Class<? extends B>, B> map = ImmutableMap.of(type, (B)value);
/*  61 */     return new ImmutableClassToInstanceMap<>(map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <B> Builder<B> builder() {
/*  69 */     return new Builder<>();
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
/*     */   public static final class Builder<B>
/*     */   {
/*  90 */     private final ImmutableMap.Builder<Class<? extends B>, B> mapBuilder = ImmutableMap.builder();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public <T extends B> Builder<B> put(Class<T> key, T value) {
/*  98 */       this.mapBuilder.put(key, (B)value);
/*  99 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public <T extends B> Builder<B> putAll(Map<? extends Class<? extends T>, ? extends T> map) {
/* 111 */       for (Map.Entry<? extends Class<? extends T>, ? extends T> entry : map.entrySet()) {
/* 112 */         Class<? extends T> type = entry.getKey();
/* 113 */         T value = entry.getValue();
/* 114 */         this.mapBuilder.put(type, cast((Class)type, value));
/*     */       } 
/* 116 */       return this;
/*     */     }
/*     */     
/*     */     private static <B, T extends B> T cast(Class<T> type, B value) {
/* 120 */       return Primitives.wrap(type).cast(value);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableClassToInstanceMap<B> build() {
/* 130 */       ImmutableMap<Class<? extends B>, B> map = this.mapBuilder.build();
/* 131 */       if (map.isEmpty()) {
/* 132 */         return ImmutableClassToInstanceMap.of();
/*     */       }
/* 134 */       return new ImmutableClassToInstanceMap<>(map);
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
/*     */   public static <B, S extends B> ImmutableClassToInstanceMap<B> copyOf(Map<? extends Class<? extends S>, ? extends S> map) {
/* 152 */     if (map instanceof ImmutableClassToInstanceMap) {
/*     */ 
/*     */       
/* 155 */       ImmutableClassToInstanceMap<B> cast = (ImmutableClassToInstanceMap)map;
/* 156 */       return cast;
/*     */     } 
/* 158 */     return (new Builder<>()).<S>putAll(map).build();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private ImmutableClassToInstanceMap(ImmutableMap<Class<? extends B>, B> delegate) {
/* 164 */     this.delegate = delegate;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Map<Class<? extends B>, B> delegate() {
/* 169 */     return this.delegate;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends B> T getInstance(Class<T> type) {
/* 175 */     return (T)this.delegate.get(Preconditions.checkNotNull(type));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public <T extends B> T putInstance(Class<T> type, T value) {
/* 188 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   Object readResolve() {
/* 192 */     return isEmpty() ? of() : this;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ImmutableClassToInstanceMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */