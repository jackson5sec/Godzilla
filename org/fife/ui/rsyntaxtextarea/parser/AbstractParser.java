/*    */ package org.fife.ui.rsyntaxtextarea.parser;
/*    */ 
/*    */ import java.net.URL;
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
/*    */ public abstract class AbstractParser
/*    */   implements Parser
/*    */ {
/*    */   private boolean enabled;
/*    */   private ExtendedHyperlinkListener linkListener;
/*    */   
/*    */   protected AbstractParser() {
/* 41 */     setEnabled(true);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public ExtendedHyperlinkListener getHyperlinkListener() {
/* 47 */     return this.linkListener;
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
/*    */   public URL getImageBase() {
/* 59 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isEnabled() {
/* 65 */     return this.enabled;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setEnabled(boolean enabled) {
/* 76 */     this.enabled = enabled;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setHyperlinkListener(ExtendedHyperlinkListener listener) {
/* 87 */     this.linkListener = listener;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\parser\AbstractParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */