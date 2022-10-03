/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Spliterator;
/*     */ import java.util.function.Consumer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ final class ImmutableEnumSet<E extends Enum<E>>
/*     */   extends ImmutableSet<E>
/*     */ {
/*     */   private final transient EnumSet<E> delegate;
/*     */   @LazyInit
/*     */   private transient int hashCode;
/*     */   
/*     */   static ImmutableSet asImmutable(EnumSet<?> set) {
/*  37 */     switch (set.size()) {
/*     */       case 0:
/*  39 */         return ImmutableSet.of();
/*     */       case 1:
/*  41 */         return ImmutableSet.of(Iterables.getOnlyElement(set));
/*     */     } 
/*  43 */     return new ImmutableEnumSet(set);
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
/*     */   private ImmutableEnumSet(EnumSet<E> delegate) {
/*  58 */     this.delegate = delegate;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/*  63 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public UnmodifiableIterator<E> iterator() {
/*  68 */     return Iterators.unmodifiableIterator(this.delegate.iterator());
/*     */   }
/*     */ 
/*     */   
/*     */   public Spliterator<E> spliterator() {
/*  73 */     return this.delegate.spliterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(Consumer<? super E> action) {
/*  78 */     this.delegate.forEach(action);
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  83 */     return this.delegate.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object object) {
/*  88 */     return this.delegate.contains(object);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsAll(Collection<?> collection) {
/*  93 */     if (collection instanceof ImmutableEnumSet) {
/*  94 */       collection = ((ImmutableEnumSet)collection).delegate;
/*     */     }
/*  96 */     return this.delegate.containsAll(collection);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 101 */     return this.delegate.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object<E> object) {
/* 106 */     if (object == this) {
/* 107 */       return true;
/*     */     }
/* 109 */     if (object instanceof ImmutableEnumSet) {
/* 110 */       object = (Object<E>)((ImmutableEnumSet)object).delegate;
/*     */     }
/* 112 */     return this.delegate.equals(object);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isHashCodeFast() {
/* 117 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 124 */     int result = this.hashCode;
/* 125 */     return (result == 0) ? (this.hashCode = this.delegate.hashCode()) : result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 130 */     return this.delegate.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 136 */     return new EnumSerializedForm<>(this.delegate);
/*     */   }
/*     */   
/*     */   private static class EnumSerializedForm<E extends Enum<E>>
/*     */     implements Serializable
/*     */   {
/*     */     final EnumSet<E> delegate;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     EnumSerializedForm(EnumSet<E> delegate) {
/* 146 */       this.delegate = delegate;
/*     */     }
/*     */ 
/*     */     
/*     */     Object readResolve() {
/* 151 */       return new ImmutableEnumSet<>(this.delegate.clone());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ImmutableEnumSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */