/*     */ package org.fife.ui.rsyntaxtextarea;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import javax.swing.Timer;
/*     */ import javax.swing.event.CaretEvent;
/*     */ import javax.swing.event.CaretListener;
/*     */ import javax.swing.text.Caret;
/*     */ import org.fife.ui.rtextarea.SmartHighlightPainter;
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
/*     */ class MarkOccurrencesSupport
/*     */   implements CaretListener, ActionListener
/*     */ {
/*     */   private RSyntaxTextArea textArea;
/*     */   private Timer timer;
/*     */   private SmartHighlightPainter p;
/*  41 */   static final Color DEFAULT_COLOR = new Color(224, 224, 224);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final int DEFAULT_DELAY_MS = 1000;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   MarkOccurrencesSupport() {
/*  53 */     this(1000);
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
/*     */   MarkOccurrencesSupport(int delay) {
/*  65 */     this(delay, DEFAULT_COLOR);
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
/*     */   MarkOccurrencesSupport(int delay, Color color) {
/*  79 */     this.timer = new Timer(delay, this);
/*  80 */     this.timer.setRepeats(false);
/*  81 */     this.p = new SmartHighlightPainter();
/*  82 */     setColor(color);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void actionPerformed(ActionEvent e) {
/* 101 */     Caret c = this.textArea.getCaret();
/* 102 */     if (c.getDot() != c.getMark()) {
/*     */       return;
/*     */     }
/*     */     
/* 106 */     RSyntaxDocument doc = (RSyntaxDocument)this.textArea.getDocument();
/* 107 */     OccurrenceMarker occurrenceMarker = doc.getOccurrenceMarker();
/* 108 */     boolean occurrencesChanged = false;
/*     */     
/* 110 */     if (occurrenceMarker != null) {
/*     */       
/* 112 */       doc.readLock();
/*     */       
/*     */       try {
/* 115 */         Token t = occurrenceMarker.getTokenToMark(this.textArea);
/*     */         
/* 117 */         if (t != null && occurrenceMarker.isValidType(this.textArea, t) && 
/* 118 */           !RSyntaxUtilities.isNonWordChar(t)) {
/* 119 */           clear();
/*     */           
/* 121 */           RSyntaxTextAreaHighlighter h = (RSyntaxTextAreaHighlighter)this.textArea.getHighlighter();
/* 122 */           occurrenceMarker.markOccurrences(doc, t, h, this.p);
/*     */ 
/*     */ 
/*     */           
/* 126 */           occurrencesChanged = true;
/*     */         } else {
/* 128 */           clear();
/*     */         } 
/*     */       } finally {
/*     */         
/* 132 */         doc.readUnlock();
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 139 */     if (occurrencesChanged) {
/* 140 */       this.textArea.fireMarkedOccurrencesChanged();
/*     */     }
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
/*     */   public void caretUpdate(CaretEvent e) {
/* 153 */     this.timer.restart();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void clear() {
/* 161 */     if (this.textArea != null) {
/*     */       
/* 163 */       RSyntaxTextAreaHighlighter h = (RSyntaxTextAreaHighlighter)this.textArea.getHighlighter();
/* 164 */       h.clearMarkOccurrencesHighlights();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doMarkOccurrences() {
/* 174 */     this.timer.stop();
/* 175 */     actionPerformed(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Color getColor() {
/* 186 */     return (Color)this.p.getPaint();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDelay() {
/* 197 */     return this.timer.getDelay();
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
/*     */   public boolean getPaintBorder() {
/* 209 */     return this.p.getPaintBorder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void install(RSyntaxTextArea textArea) {
/* 220 */     if (this.textArea != null) {
/* 221 */       uninstall();
/*     */     }
/* 223 */     this.textArea = textArea;
/* 224 */     textArea.addCaretListener(this);
/* 225 */     if (textArea.getMarkOccurrencesColor() != null) {
/* 226 */       setColor(textArea.getMarkOccurrencesColor());
/*     */     }
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
/*     */   public void setColor(Color color) {
/* 239 */     this.p.setPaint(color);
/* 240 */     if (this.textArea != null) {
/* 241 */       clear();
/* 242 */       caretUpdate(null);
/*     */     } 
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
/*     */   public void setDelay(int delay) {
/* 256 */     this.timer.setInitialDelay(delay);
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
/*     */   public void setPaintBorder(boolean paint) {
/* 268 */     if (paint != this.p.getPaintBorder()) {
/* 269 */       this.p.setPaintBorder(paint);
/* 270 */       if (this.textArea != null) {
/* 271 */         this.textArea.repaint();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void uninstall() {
/* 284 */     if (this.textArea != null) {
/* 285 */       clear();
/* 286 */       this.textArea.removeCaretListener(this);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\MarkOccurrencesSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */