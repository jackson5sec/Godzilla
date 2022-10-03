/*    */ package javassist.bytecode;
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
/*    */ class ExceptionTableEntry
/*    */ {
/*    */   int startPc;
/*    */   int endPc;
/*    */   int handlerPc;
/*    */   int catchType;
/*    */   
/*    */   ExceptionTableEntry(int start, int end, int handle, int type) {
/* 33 */     this.startPc = start;
/* 34 */     this.endPc = end;
/* 35 */     this.handlerPc = handle;
/* 36 */     this.catchType = type;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\ExceptionTableEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */