/*    */ package org.fife.ui.autocomplete;
/*    */ 
/*    */ import java.util.EventObject;
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
/*    */ public class AutoCompletionEvent
/*    */   extends EventObject
/*    */ {
/*    */   private Type type;
/*    */   
/*    */   public AutoCompletionEvent(AutoCompletion source, Type type) {
/* 36 */     super(source);
/* 37 */     this.type = type;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AutoCompletion getAutoCompletion() {
/* 48 */     return (AutoCompletion)getSource();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Type getEventType() {
/* 58 */     return this.type;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public enum Type
/*    */   {
/* 66 */     POPUP_SHOWN,
/* 67 */     POPUP_HIDDEN;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\autocomplete\AutoCompletionEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */