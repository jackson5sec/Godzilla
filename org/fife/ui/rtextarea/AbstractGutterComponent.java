/*     */ package org.fife.ui.rtextarea;
/*     */ 
/*     */ import java.awt.Container;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import javax.swing.text.View;
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
/*     */ abstract class AbstractGutterComponent
/*     */   extends JPanel
/*     */ {
/*     */   protected RTextArea textArea;
/*     */   protected int currentLineCount;
/*     */   
/*     */   AbstractGutterComponent(RTextArea textArea) {
/*  45 */     init();
/*  46 */     setTextArea(textArea);
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
/*     */   protected static Rectangle getChildViewBounds(View parent, int line, Rectangle editorRect) {
/*  62 */     Shape alloc = parent.getChildAllocation(line, editorRect);
/*  63 */     if (alloc == null)
/*     */     {
/*     */       
/*  66 */       return new Rectangle();
/*     */     }
/*  68 */     return (alloc instanceof Rectangle) ? (Rectangle)alloc : alloc
/*  69 */       .getBounds();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Gutter getGutter() {
/*  79 */     Container parent = getParent();
/*  80 */     return (parent instanceof Gutter) ? (Gutter)parent : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract void handleDocumentEvent(DocumentEvent paramDocumentEvent);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void init() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract void lineHeightsChanged();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTextArea(RTextArea textArea) {
/* 116 */     this.textArea = textArea;
/* 117 */     int lineCount = (textArea == null) ? 0 : textArea.getLineCount();
/* 118 */     if (this.currentLineCount != lineCount) {
/* 119 */       this.currentLineCount = lineCount;
/* 120 */       repaint();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rtextarea\AbstractGutterComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */