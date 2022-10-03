/*     */ package org.fife.ui.rsyntaxtextarea.parser;
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
/*     */ public interface ParserNotice
/*     */   extends Comparable<ParserNotice>
/*     */ {
/*     */   boolean containsPosition(int paramInt);
/*     */   
/*     */   Color getColor();
/*     */   
/*     */   int getLength();
/*     */   
/*     */   Level getLevel();
/*     */   
/*     */   int getLine();
/*     */   
/*     */   boolean getKnowsOffsetAndLength();
/*     */   
/*     */   String getMessage();
/*     */   
/*     */   int getOffset();
/*     */   
/*     */   Parser getParser();
/*     */   
/*     */   boolean getShowInEditor();
/*     */   
/*     */   String getToolTipText();
/*     */   
/*     */   public enum Level
/*     */   {
/* 144 */     INFO(2),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 149 */     WARNING(1),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 154 */     ERROR(0);
/*     */     
/*     */     private int value;
/*     */     
/*     */     Level(int value) {
/* 159 */       this.value = value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getNumericValue() {
/* 168 */       return this.value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isEqualToOrWorseThan(Level other) {
/* 179 */       return (this.value <= other.getNumericValue());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\parser\ParserNotice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */