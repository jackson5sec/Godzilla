/*    */ package com.google.common.base;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.Collections;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible
/*    */ final class Present<T>
/*    */   extends Optional<T>
/*    */ {
/*    */   private final T reference;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   Present(T reference) {
/* 30 */     this.reference = reference;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isPresent() {
/* 35 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public T get() {
/* 40 */     return this.reference;
/*    */   }
/*    */ 
/*    */   
/*    */   public T or(T defaultValue) {
/* 45 */     Preconditions.checkNotNull(defaultValue, "use Optional.orNull() instead of Optional.or(null)");
/* 46 */     return this.reference;
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<T> or(Optional<? extends T> secondChoice) {
/* 51 */     Preconditions.checkNotNull(secondChoice);
/* 52 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public T or(Supplier<? extends T> supplier) {
/* 57 */     Preconditions.checkNotNull(supplier);
/* 58 */     return this.reference;
/*    */   }
/*    */ 
/*    */   
/*    */   public T orNull() {
/* 63 */     return this.reference;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<T> asSet() {
/* 68 */     return Collections.singleton(this.reference);
/*    */   }
/*    */ 
/*    */   
/*    */   public <V> Optional<V> transform(Function<? super T, V> function) {
/* 73 */     return new Present(
/* 74 */         Preconditions.checkNotNull((T)function
/* 75 */           .apply(this.reference), "the Function passed to Optional.transform() must not return null."));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object object) {
/* 81 */     if (object instanceof Present) {
/* 82 */       Present<?> other = (Present)object;
/* 83 */       return this.reference.equals(other.reference);
/*    */     } 
/* 85 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 90 */     return 1502476572 + this.reference.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 95 */     return "Optional.of(" + this.reference + ")";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\base\Present.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */