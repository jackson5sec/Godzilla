/*    */ package com.jediterm.terminal.util;
/*    */ 
/*    */ import org.jetbrains.annotations.NotNull;
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
/*    */ public class Pair<A, B>
/*    */ {
/*    */   public final A first;
/*    */   public final B second;
/*    */   
/*    */   @NotNull
/*    */   public static <A, B> Pair<A, B> create(A first, B second) {
/* 26 */     return new Pair<>(first, second);
/*    */   }
/*    */   
/*    */   public static <T> T getFirst(Pair<T, ?> pair) {
/* 30 */     return (pair != null) ? (T)pair.first : null;
/*    */   }
/*    */   
/*    */   public static <T> T getSecond(Pair<?, T> pair) {
/* 34 */     return (pair != null) ? (T)pair.second : null;
/*    */   }
/*    */ 
/*    */   
/* 38 */   private static final Pair EMPTY = create(null, null);
/*    */ 
/*    */   
/*    */   public static <A, B> Pair<A, B> empty() {
/* 42 */     return EMPTY;
/*    */   }
/*    */   
/*    */   public Pair(A first, B second) {
/* 46 */     this.first = first;
/* 47 */     this.second = second;
/*    */   }
/*    */   
/*    */   public final A getFirst() {
/* 51 */     return this.first;
/*    */   }
/*    */   
/*    */   public final B getSecond() {
/* 55 */     return this.second;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 60 */     if (this == o) return true; 
/* 61 */     if (o == null || getClass() != o.getClass()) return false;
/*    */     
/* 63 */     Pair pair = (Pair)o;
/*    */     
/* 65 */     if ((this.first != null) ? !this.first.equals(pair.first) : (pair.first != null)) return false; 
/* 66 */     if ((this.second != null) ? !this.second.equals(pair.second) : (pair.second != null)) return false;
/*    */     
/* 68 */     return true;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 72 */     int result = (this.first != null) ? this.first.hashCode() : 0;
/* 73 */     result = 31 * result + ((this.second != null) ? this.second.hashCode() : 0);
/* 74 */     return result;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 78 */     return "<" + this.first + "," + this.second + ">";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\termina\\util\Pair.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */