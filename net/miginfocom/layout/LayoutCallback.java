/*    */ package net.miginfocom.layout;
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
/*    */ public abstract class LayoutCallback
/*    */ {
/*    */   public UnitValue[] getPosition(ComponentWrapper comp) {
/* 54 */     return null;
/*    */   }
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
/*    */   public BoundSize[] getSize(ComponentWrapper comp) {
/* 67 */     return null;
/*    */   }
/*    */   
/*    */   public void correctBounds(ComponentWrapper comp) {}
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\net\miginfocom\layout\LayoutCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */