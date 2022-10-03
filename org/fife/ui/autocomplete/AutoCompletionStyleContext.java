/*     */ package org.fife.ui.autocomplete;
/*     */ 
/*     */ import java.awt.Color;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AutoCompletionStyleContext
/*     */ {
/*     */   private Color parameterizedCompletionCursorPositionColor;
/*     */   private Color parameterCopyColor;
/*     */   private Color parameterOutlineColor;
/*     */   
/*     */   public AutoCompletionStyleContext() {
/*  42 */     setParameterOutlineColor(Color.gray);
/*  43 */     setParameterCopyColor(new Color(11851775));
/*  44 */     setParameterizedCompletionCursorPositionColor(new Color(46080));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Color getParameterCopyColor() {
/*  56 */     return this.parameterCopyColor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Color getParameterizedCompletionCursorPositionColor() {
/*  68 */     return this.parameterizedCompletionCursorPositionColor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Color getParameterOutlineColor() {
/*  80 */     return this.parameterOutlineColor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParameterCopyColor(Color color) {
/*  92 */     this.parameterCopyColor = color;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParameterizedCompletionCursorPositionColor(Color color) {
/* 104 */     this.parameterizedCompletionCursorPositionColor = color;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParameterOutlineColor(Color color) {
/* 116 */     this.parameterOutlineColor = color;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\autocomplete\AutoCompletionStyleContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */