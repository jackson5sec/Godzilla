/*    */ package org.springframework.cglib.beans;
/*    */ 
/*    */ import java.util.AbstractSet;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collections;
/*    */ import java.util.HashSet;
/*    */ import java.util.Iterator;
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
/*    */ public class FixedKeySet
/*    */   extends AbstractSet
/*    */ {
/*    */   private Set set;
/*    */   private int size;
/*    */   
/*    */   public FixedKeySet(String[] keys) {
/* 25 */     this.size = keys.length;
/* 26 */     this.set = Collections.unmodifiableSet(new HashSet(Arrays.asList((Object[])keys)));
/*    */   }
/*    */   
/*    */   public Iterator iterator() {
/* 30 */     return this.set.iterator();
/*    */   }
/*    */   
/*    */   public int size() {
/* 34 */     return this.size;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\beans\FixedKeySet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */