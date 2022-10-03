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
/*    */ 
/*    */ @GwtCompatible
/*    */ final class Absent<T>
/*    */   extends Optional<T>
/*    */ {
/* 27 */   static final Absent<Object> INSTANCE = new Absent();
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   static <T> Optional<T> withType() {
/* 31 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isPresent() {
/* 38 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public T get() {
/* 43 */     throw new IllegalStateException("Optional.get() cannot be called on an absent value");
/*    */   }
/*    */ 
/*    */   
/*    */   public T or(T defaultValue) {
/* 48 */     return Preconditions.checkNotNull(defaultValue, "use Optional.orNull() instead of Optional.or(null)");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Optional<T> or(Optional<? extends T> secondChoice) {
/* 54 */     return (Optional<T>)Preconditions.<Optional<? extends T>>checkNotNull(secondChoice);
/*    */   }
/*    */ 
/*    */   
/*    */   public T or(Supplier<? extends T> supplier) {
/* 59 */     return Preconditions.checkNotNull(supplier
/* 60 */         .get(), "use Optional.orNull() instead of a Supplier that returns null");
/*    */   }
/*    */ 
/*    */   
/*    */   public T orNull() {
/* 65 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<T> asSet() {
/* 70 */     return Collections.emptySet();
/*    */   }
/*    */ 
/*    */   
/*    */   public <V> Optional<V> transform(Function<? super T, V> function) {
/* 75 */     Preconditions.checkNotNull(function);
/* 76 */     return Optional.absent();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object object) {
/* 81 */     return (object == this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 86 */     return 2040732332;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 91 */     return "Optional.absent()";
/*    */   }
/*    */   
/*    */   private Object readResolve() {
/* 95 */     return INSTANCE;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\base\Absent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */