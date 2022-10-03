/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Primitives;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtIncompatible
/*     */ public final class MutableClassToInstanceMap<B>
/*     */   extends ForwardingMap<Class<? extends B>, B>
/*     */   implements ClassToInstanceMap<B>, Serializable
/*     */ {
/*     */   private final Map<Class<? extends B>, B> delegate;
/*     */   
/*     */   public static <B> MutableClassToInstanceMap<B> create() {
/*  53 */     return new MutableClassToInstanceMap<>(new HashMap<>());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <B> MutableClassToInstanceMap<B> create(Map<Class<? extends B>, B> backingMap) {
/*  62 */     return new MutableClassToInstanceMap<>(backingMap);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private MutableClassToInstanceMap(Map<Class<? extends B>, B> delegate) {
/*  68 */     this.delegate = (Map<Class<? extends B>, B>)Preconditions.checkNotNull(delegate);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Map<Class<? extends B>, B> delegate() {
/*  73 */     return this.delegate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <B> Map.Entry<Class<? extends B>, B> checkedEntry(final Map.Entry<Class<? extends B>, B> entry) {
/*  81 */     return new ForwardingMapEntry<Class<? extends B>, B>()
/*     */       {
/*     */         protected Map.Entry<Class<? extends B>, B> delegate() {
/*  84 */           return entry;
/*     */         }
/*     */ 
/*     */         
/*     */         public B setValue(B value) {
/*  89 */           return super.setValue((B)MutableClassToInstanceMap.cast((Class)getKey(), value));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<Class<? extends B>, B>> entrySet() {
/*  96 */     return new ForwardingSet<Map.Entry<Class<? extends B>, B>>()
/*     */       {
/*     */         protected Set<Map.Entry<Class<? extends B>, B>> delegate()
/*     */         {
/* 100 */           return MutableClassToInstanceMap.this.delegate().entrySet();
/*     */         }
/*     */ 
/*     */         
/*     */         public Spliterator<Map.Entry<Class<? extends B>, B>> spliterator() {
/* 105 */           return CollectSpliterators.map(
/* 106 */               delegate().spliterator(), x$0 -> MutableClassToInstanceMap.checkedEntry(x$0));
/*     */         }
/*     */ 
/*     */         
/*     */         public Iterator<Map.Entry<Class<? extends B>, B>> iterator() {
/* 111 */           return new TransformedIterator<Map.Entry<Class<? extends B>, B>, Map.Entry<Class<? extends B>, B>>(
/* 112 */               delegate().iterator())
/*     */             {
/*     */               Map.Entry<Class<? extends B>, B> transform(Map.Entry<Class<? extends B>, B> from) {
/* 115 */                 return MutableClassToInstanceMap.checkedEntry(from);
/*     */               }
/*     */             };
/*     */         }
/*     */ 
/*     */         
/*     */         public Object[] toArray() {
/* 122 */           return standardToArray();
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> T[] toArray(T[] array) {
/* 127 */           return (T[])standardToArray((Object[])array);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public B put(Class<? extends B> key, B value) {
/* 135 */     return super.put(key, cast((Class)key, value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends Class<? extends B>, ? extends B> map) {
/* 140 */     Map<Class<? extends B>, B> copy = new LinkedHashMap<>(map);
/* 141 */     for (Map.Entry<? extends Class<? extends B>, B> entry : copy.entrySet()) {
/* 142 */       cast(entry.getKey(), entry.getValue());
/*     */     }
/* 144 */     super.putAll(copy);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public <T extends B> T putInstance(Class<T> type, T value) {
/* 150 */     return cast(type, put(type, (B)value));
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends B> T getInstance(Class<T> type) {
/* 155 */     return cast(type, get(type));
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   private static <B, T extends B> T cast(Class<T> type, B value) {
/* 160 */     return Primitives.wrap(type).cast(value);
/*     */   }
/*     */   
/*     */   private Object writeReplace() {
/* 164 */     return new SerializedForm<>(delegate());
/*     */   }
/*     */   
/*     */   private static final class SerializedForm<B> implements Serializable {
/*     */     private final Map<Class<? extends B>, B> backingMap;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     SerializedForm(Map<Class<? extends B>, B> backingMap) {
/* 172 */       this.backingMap = backingMap;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 176 */       return MutableClassToInstanceMap.create(this.backingMap);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\MutableClassToInstanceMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */