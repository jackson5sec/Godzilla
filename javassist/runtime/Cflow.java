/*    */ package javassist.runtime;
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
/*    */ public class Cflow
/*    */   extends ThreadLocal<Cflow.Depth>
/*    */ {
/*    */   protected static class Depth
/*    */   {
/* 29 */     private int depth = 0;
/* 30 */     int value() { return this.depth; }
/* 31 */     void inc() { this.depth++; } void dec() {
/* 32 */       this.depth--;
/*    */     }
/*    */   }
/*    */   
/*    */   protected synchronized Depth initialValue() {
/* 37 */     return new Depth();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void enter() {
/* 43 */     get().inc();
/*    */   }
/*    */ 
/*    */   
/*    */   public void exit() {
/* 48 */     get().dec();
/*    */   }
/*    */ 
/*    */   
/*    */   public int value() {
/* 53 */     return get().value();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\runtime\Cflow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */