/*    */ package com.google.common.base;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.io.Serializable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Beta
/*    */ @GwtCompatible
/*    */ final class FunctionalEquivalence<F, T>
/*    */   extends Equivalence<F>
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 0L;
/*    */   private final Function<F, ? extends T> function;
/*    */   private final Equivalence<T> resultEquivalence;
/*    */   
/*    */   FunctionalEquivalence(Function<F, ? extends T> function, Equivalence<T> resultEquivalence) {
/* 40 */     this.function = Preconditions.<Function<F, ? extends T>>checkNotNull(function);
/* 41 */     this.resultEquivalence = Preconditions.<Equivalence<T>>checkNotNull(resultEquivalence);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean doEquivalent(F a, F b) {
/* 46 */     return this.resultEquivalence.equivalent(this.function.apply(a), this.function.apply(b));
/*    */   }
/*    */ 
/*    */   
/*    */   protected int doHash(F a) {
/* 51 */     return this.resultEquivalence.hash(this.function.apply(a));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 56 */     if (obj == this) {
/* 57 */       return true;
/*    */     }
/* 59 */     if (obj instanceof FunctionalEquivalence) {
/* 60 */       FunctionalEquivalence<?, ?> that = (FunctionalEquivalence<?, ?>)obj;
/* 61 */       return (this.function.equals(that.function) && this.resultEquivalence.equals(that.resultEquivalence));
/*    */     } 
/* 63 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 68 */     return Objects.hashCode(new Object[] { this.function, this.resultEquivalence });
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 73 */     return this.resultEquivalence + ".onResultOf(" + this.function + ")";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\base\FunctionalEquivalence.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */