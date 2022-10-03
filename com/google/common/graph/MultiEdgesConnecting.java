/*    */ package com.google.common.graph;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.common.collect.AbstractIterator;
/*    */ import com.google.common.collect.UnmodifiableIterator;
/*    */ import java.util.AbstractSet;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class MultiEdgesConnecting<E>
/*    */   extends AbstractSet<E>
/*    */ {
/*    */   private final Map<E, ?> outEdgeToNode;
/*    */   private final Object targetNode;
/*    */   
/*    */   MultiEdgesConnecting(Map<E, ?> outEdgeToNode, Object targetNode) {
/* 44 */     this.outEdgeToNode = (Map<E, ?>)Preconditions.checkNotNull(outEdgeToNode);
/* 45 */     this.targetNode = Preconditions.checkNotNull(targetNode);
/*    */   }
/*    */ 
/*    */   
/*    */   public UnmodifiableIterator<E> iterator() {
/* 50 */     final Iterator<? extends Map.Entry<E, ?>> entries = this.outEdgeToNode.entrySet().iterator();
/* 51 */     return (UnmodifiableIterator<E>)new AbstractIterator<E>()
/*    */       {
/*    */         protected E computeNext() {
/* 54 */           while (entries.hasNext()) {
/* 55 */             Map.Entry<E, ?> entry = entries.next();
/* 56 */             if (MultiEdgesConnecting.this.targetNode.equals(entry.getValue())) {
/* 57 */               return entry.getKey();
/*    */             }
/*    */           } 
/* 60 */           return (E)endOfData();
/*    */         }
/*    */       };
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean contains(Object edge) {
/* 67 */     return this.targetNode.equals(this.outEdgeToNode.get(edge));
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\graph\MultiEdgesConnecting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */