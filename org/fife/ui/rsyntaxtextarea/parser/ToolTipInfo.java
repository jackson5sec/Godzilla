/*    */ package org.fife.ui.rsyntaxtextarea.parser;
/*    */ 
/*    */ import java.net.URL;
/*    */ import javax.swing.event.HyperlinkListener;
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
/*    */ public class ToolTipInfo
/*    */ {
/*    */   private String text;
/*    */   private HyperlinkListener listener;
/*    */   private URL imageBase;
/*    */   
/*    */   public ToolTipInfo(String text, HyperlinkListener listener) {
/* 40 */     this(text, listener, null);
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
/*    */   public ToolTipInfo(String text, HyperlinkListener l, URL imageBase) {
/* 53 */     this.text = text;
/* 54 */     this.listener = l;
/* 55 */     this.imageBase = imageBase;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public HyperlinkListener getHyperlinkListener() {
/* 66 */     return this.listener;
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
/*    */   
/*    */   public URL getImageBase() {
/* 80 */     return this.imageBase;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getToolTipText() {
/* 90 */     return this.text;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\parser\ToolTipInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */