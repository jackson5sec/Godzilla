/*    */ package org.fife.ui.rsyntaxtextarea;
/*    */ 
/*    */ import javax.swing.KeyStroke;
/*    */ import org.fife.ui.rtextarea.RTADefaultInputMap;
/*    */ import org.fife.ui.rtextarea.RTextArea;
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
/*    */ public class RSyntaxTextAreaDefaultInputMap
/*    */   extends RTADefaultInputMap
/*    */ {
/*    */   public RSyntaxTextAreaDefaultInputMap() {
/* 40 */     int defaultMod = RTextArea.getDefaultModifier();
/* 41 */     int shift = 64;
/* 42 */     int defaultShift = defaultMod | shift;
/*    */     
/* 44 */     put(KeyStroke.getKeyStroke(9, shift), "RSTA.DecreaseIndentAction");
/* 45 */     put(KeyStroke.getKeyStroke('}'), "RSTA.CloseCurlyBraceAction");
/*    */     
/* 47 */     put(KeyStroke.getKeyStroke('/'), "RSTA.CloseMarkupTagAction");
/* 48 */     int os = RSyntaxUtilities.getOS();
/* 49 */     if (os == 1 || os == 2)
/*    */     {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 60 */       put(KeyStroke.getKeyStroke(47, defaultMod), "RSTA.ToggleCommentAction");
/*    */     }
/*    */     
/* 63 */     put(KeyStroke.getKeyStroke(91, defaultMod), "RSTA.GoToMatchingBracketAction");
/* 64 */     put(KeyStroke.getKeyStroke(109, defaultMod), "RSTA.CollapseFoldAction");
/* 65 */     put(KeyStroke.getKeyStroke(107, defaultMod), "RSTA.ExpandFoldAction");
/* 66 */     put(KeyStroke.getKeyStroke(111, defaultMod), "RSTA.CollapseAllFoldsAction");
/* 67 */     put(KeyStroke.getKeyStroke(106, defaultMod), "RSTA.ExpandAllFoldsAction");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 73 */     put(KeyStroke.getKeyStroke(32, defaultShift), "RSTA.TemplateAction");
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\RSyntaxTextAreaDefaultInputMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */