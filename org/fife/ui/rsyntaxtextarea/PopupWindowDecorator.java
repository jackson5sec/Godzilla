/*    */ package org.fife.ui.rsyntaxtextarea;
/*    */ 
/*    */ import javax.swing.JWindow;
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
/*    */ public abstract class PopupWindowDecorator
/*    */ {
/*    */   private static PopupWindowDecorator decorator;
/*    */   
/*    */   public abstract void decorate(JWindow paramJWindow);
/*    */   
/*    */   public static PopupWindowDecorator get() {
/* 51 */     return decorator;
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
/*    */   public static void set(PopupWindowDecorator decorator) {
/* 64 */     PopupWindowDecorator.decorator = decorator;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\PopupWindowDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */