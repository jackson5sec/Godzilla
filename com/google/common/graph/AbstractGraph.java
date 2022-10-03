/*    */ package com.google.common.graph;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Beta
/*    */ public abstract class AbstractGraph<N>
/*    */   extends AbstractBaseGraph<N>
/*    */   implements Graph<N>
/*    */ {
/*    */   public final boolean equals(Object obj) {
/* 35 */     if (obj == this) {
/* 36 */       return true;
/*    */     }
/* 38 */     if (!(obj instanceof Graph)) {
/* 39 */       return false;
/*    */     }
/* 41 */     Graph<?> other = (Graph)obj;
/*    */     
/* 43 */     return (isDirected() == other.isDirected() && 
/* 44 */       nodes().equals(other.nodes()) && 
/* 45 */       edges().equals(other.edges()));
/*    */   }
/*    */ 
/*    */   
/*    */   public final int hashCode() {
/* 50 */     return edges().hashCode();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 56 */     return "isDirected: " + 
/* 57 */       isDirected() + ", allowsSelfLoops: " + 
/*    */       
/* 59 */       allowsSelfLoops() + ", nodes: " + 
/*    */       
/* 61 */       nodes() + ", edges: " + 
/*    */       
/* 63 */       edges();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\graph\AbstractGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */