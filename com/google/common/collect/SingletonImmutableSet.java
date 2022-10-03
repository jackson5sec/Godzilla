/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ final class SingletonImmutableSet<E>
/*     */   extends ImmutableSet<E>
/*     */ {
/*     */   final transient E element;
/*     */   @LazyInit
/*     */   private transient int cachedHashCode;
/*     */   
/*     */   SingletonImmutableSet(E element) {
/*  44 */     this.element = (E)Preconditions.checkNotNull(element);
/*     */   }
/*     */ 
/*     */   
/*     */   SingletonImmutableSet(E element, int hashCode) {
/*  49 */     this.element = element;
/*  50 */     this.cachedHashCode = hashCode;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  55 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object target) {
/*  60 */     return this.element.equals(target);
/*     */   }
/*     */ 
/*     */   
/*     */   public UnmodifiableIterator<E> iterator() {
/*  65 */     return Iterators.singletonIterator(this.element);
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableList<E> createAsList() {
/*  70 */     return ImmutableList.of(this.element);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/*  75 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   int copyIntoArray(Object[] dst, int offset) {
/*  80 */     dst[offset] = this.element;
/*  81 */     return offset + 1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final int hashCode() {
/*  87 */     int code = this.cachedHashCode;
/*  88 */     if (code == 0) {
/*  89 */       this.cachedHashCode = code = this.element.hashCode();
/*     */     }
/*  91 */     return code;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isHashCodeFast() {
/*  96 */     return (this.cachedHashCode != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 101 */     return '[' + this.element.toString() + ']';
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\SingletonImmutableSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */