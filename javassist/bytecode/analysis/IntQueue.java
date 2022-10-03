/*    */ package javassist.bytecode.analysis;
/*    */ 
/*    */ import java.util.NoSuchElementException;
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
/*    */ class IntQueue
/*    */ {
/*    */   private Entry head;
/*    */   private Entry tail;
/*    */   
/*    */   private static class Entry
/*    */   {
/*    */     private Entry next;
/*    */     private int value;
/*    */     
/*    */     private Entry(int value) {
/* 25 */       this.value = value;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void add(int value) {
/* 33 */     Entry entry = new Entry(value);
/* 34 */     if (this.tail != null)
/* 35 */       this.tail.next = entry; 
/* 36 */     this.tail = entry;
/*    */     
/* 38 */     if (this.head == null)
/* 39 */       this.head = entry; 
/*    */   }
/*    */   
/*    */   boolean isEmpty() {
/* 43 */     return (this.head == null);
/*    */   }
/*    */   
/*    */   int take() {
/* 47 */     if (this.head == null) {
/* 48 */       throw new NoSuchElementException();
/*    */     }
/* 50 */     int value = this.head.value;
/* 51 */     this.head = this.head.next;
/* 52 */     if (this.head == null) {
/* 53 */       this.tail = null;
/*    */     }
/* 55 */     return value;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\analysis\IntQueue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */