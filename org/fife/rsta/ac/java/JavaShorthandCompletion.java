/*     */ package org.fife.rsta.ac.java;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import javax.swing.Icon;
/*     */ import org.fife.ui.autocomplete.CompletionProvider;
/*     */ import org.fife.ui.autocomplete.ShorthandCompletion;
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
/*     */ class JavaShorthandCompletion
/*     */   extends ShorthandCompletion
/*     */   implements JavaSourceCompletion
/*     */ {
/*  30 */   private static final Color SHORTHAND_COLOR = new Color(0, 127, 174);
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
/*     */   public JavaShorthandCompletion(CompletionProvider provider, String inputText, String replacementText) {
/*  42 */     super(provider, inputText, replacementText);
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
/*     */ 
/*     */   
/*     */   public JavaShorthandCompletion(CompletionProvider provider, String inputText, String replacementText, String shortDesc) {
/*  56 */     super(provider, inputText, replacementText, shortDesc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Icon getIcon() {
/*  65 */     return IconFactory.get().getIcon("templateIcon");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rendererText(Graphics g, int x, int y, boolean selected) {
/*  74 */     renderText(g, getInputText(), getReplacementText(), x, y, selected);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void renderText(Graphics g, String input, String shortDesc, int x, int y, boolean selected) {
/*  90 */     Color orig = g.getColor();
/*  91 */     if (!selected && shortDesc != null) {
/*  92 */       g.setColor(SHORTHAND_COLOR);
/*     */     }
/*  94 */     g.drawString(input, x, y);
/*  95 */     if (shortDesc != null) {
/*  96 */       x += g.getFontMetrics().stringWidth(input);
/*  97 */       if (!selected) {
/*  98 */         g.setColor(orig);
/*     */       }
/* 100 */       String temp = " - ";
/* 101 */       g.drawString(temp, x, y);
/* 102 */       x += g.getFontMetrics().stringWidth(temp);
/* 103 */       if (!selected) {
/* 104 */         g.setColor(Color.GRAY);
/*     */       }
/* 106 */       g.drawString(shortDesc, x, y);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\JavaShorthandCompletion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */