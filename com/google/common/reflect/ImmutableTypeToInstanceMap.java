/*     */ package com.google.common.reflect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.collect.ForwardingMap;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
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
/*     */ @Beta
/*     */ public final class ImmutableTypeToInstanceMap<B>
/*     */   extends ForwardingMap<TypeToken<? extends B>, B>
/*     */   implements TypeToInstanceMap<B>
/*     */ {
/*     */   private final ImmutableMap<TypeToken<? extends B>, B> delegate;
/*     */   
/*     */   public static <B> ImmutableTypeToInstanceMap<B> of() {
/*  36 */     return new ImmutableTypeToInstanceMap<>(ImmutableMap.of());
/*     */   }
/*     */ 
/*     */   
/*     */   public static <B> Builder<B> builder() {
/*  41 */     return new Builder<>();
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
/*     */   @Beta
/*     */   public static final class Builder<B>
/*     */   {
/*  63 */     private final ImmutableMap.Builder<TypeToken<? extends B>, B> mapBuilder = ImmutableMap.builder();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public <T extends B> Builder<B> put(Class<T> key, T value) {
/*  73 */       this.mapBuilder.put(TypeToken.of(key), value);
/*  74 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public <T extends B> Builder<B> put(TypeToken<T> key, T value) {
/*  83 */       this.mapBuilder.put(key.rejectTypeVariables(), value);
/*  84 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableTypeToInstanceMap<B> build() {
/*  93 */       return new ImmutableTypeToInstanceMap<>(this.mapBuilder.build());
/*     */     }
/*     */     
/*     */     private Builder() {}
/*     */   }
/*     */   
/*     */   private ImmutableTypeToInstanceMap(ImmutableMap<TypeToken<? extends B>, B> delegate) {
/* 100 */     this.delegate = delegate;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends B> T getInstance(TypeToken<T> type) {
/* 105 */     return trustedGet(type.rejectTypeVariables());
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends B> T getInstance(Class<T> type) {
/* 110 */     return trustedGet(TypeToken.of(type));
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
/*     */   public <T extends B> T putInstance(TypeToken<T> type, T value) {
/* 123 */     throw new UnsupportedOperationException();
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
/* 136 */     throw new UnsupportedOperationException();
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
/*     */   public B put(TypeToken<? extends B> key, B value) {
/* 149 */     throw new UnsupportedOperationException();
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
/*     */   public void putAll(Map<? extends TypeToken<? extends B>, ? extends B> map) {
/* 161 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Map<TypeToken<? extends B>, B> delegate() {
/* 166 */     return (Map<TypeToken<? extends B>, B>)this.delegate;
/*     */   }
/*     */ 
/*     */   
/*     */   private <T extends B> T trustedGet(TypeToken<T> type) {
/* 171 */     return (T)this.delegate.get(type);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\reflect\ImmutableTypeToInstanceMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */