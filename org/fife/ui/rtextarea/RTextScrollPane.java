/*     */ package org.fife.ui.rtextarea;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Font;
/*     */ import java.util.Arrays;
/*     */ import java.util.Stack;
/*     */ import javax.swing.JScrollPane;
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
/*     */ public class RTextScrollPane
/*     */   extends JScrollPane
/*     */ {
/*     */   private Gutter gutter;
/*     */   
/*     */   public RTextScrollPane() {
/*  51 */     this((RTextArea)null, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RTextScrollPane(RTextArea textArea) {
/*  61 */     this(textArea, true);
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
/*     */   public RTextScrollPane(Component comp) {
/*  77 */     this(comp, true);
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
/*     */   public RTextScrollPane(RTextArea textArea, boolean lineNumbers) {
/*  91 */     this(textArea, lineNumbers, Color.GRAY);
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
/*     */   public RTextScrollPane(Component comp, boolean lineNumbers) {
/* 108 */     this(comp, lineNumbers, Color.GRAY);
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
/*     */   
/*     */   public RTextScrollPane(Component comp, boolean lineNumbers, Color lineNumberColor) {
/* 128 */     super(comp);
/*     */     
/* 130 */     RTextArea textArea = getFirstRTextAreaDescendant(comp);
/*     */ 
/*     */     
/* 133 */     Font defaultFont = new Font("Monospaced", 0, 12);
/* 134 */     this.gutter = new Gutter(textArea);
/* 135 */     this.gutter.setLineNumberFont(defaultFont);
/* 136 */     this.gutter.setLineNumberColor(lineNumberColor);
/* 137 */     setLineNumbersEnabled(lineNumbers);
/*     */ 
/*     */     
/* 140 */     setVerticalScrollBarPolicy(22);
/* 141 */     setHorizontalScrollBarPolicy(30);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkGutterVisibility() {
/* 150 */     int count = this.gutter.getComponentCount();
/* 151 */     if (count == 0) {
/* 152 */       if (getRowHeader() != null && getRowHeader().getView() == this.gutter) {
/* 153 */         setRowHeaderView(null);
/*     */       
/*     */       }
/*     */     }
/* 157 */     else if (getRowHeader() == null || getRowHeader().getView() == null) {
/* 158 */       setRowHeaderView(this.gutter);
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
/*     */   public Gutter getGutter() {
/* 170 */     return this.gutter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getLineNumbersEnabled() {
/* 181 */     return this.gutter.getLineNumbersEnabled();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RTextArea getTextArea() {
/* 192 */     return (RTextArea)getViewport().getView();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFoldIndicatorEnabled() {
/* 203 */     return this.gutter.isFoldIndicatorEnabled();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isIconRowHeaderEnabled() {
/* 214 */     return this.gutter.isIconRowHeaderEnabled();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFoldIndicatorEnabled(boolean enabled) {
/* 225 */     this.gutter.setFoldIndicatorEnabled(enabled);
/* 226 */     checkGutterVisibility();
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
/*     */   public void setIconRowHeaderEnabled(boolean enabled) {
/* 238 */     this.gutter.setIconRowHeaderEnabled(enabled);
/* 239 */     checkGutterVisibility();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLineNumbersEnabled(boolean enabled) {
/* 250 */     this.gutter.setLineNumbersEnabled(enabled);
/* 251 */     checkGutterVisibility();
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
/*     */   public void setViewportView(Component view) {
/* 264 */     RTextArea rtaCandidate = null;
/*     */     
/* 266 */     if (!(view instanceof RTextArea)) {
/* 267 */       rtaCandidate = getFirstRTextAreaDescendant(view);
/* 268 */       if (rtaCandidate == null) {
/* 269 */         throw new IllegalArgumentException("view must be either an RTextArea or a JLayer wrapping one");
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 274 */       rtaCandidate = (RTextArea)view;
/*     */     } 
/* 276 */     super.setViewportView(view);
/* 277 */     if (this.gutter != null) {
/* 278 */       this.gutter.setTextArea(rtaCandidate);
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
/*     */   
/*     */   private static RTextArea getFirstRTextAreaDescendant(Component comp) {
/* 293 */     Stack<Component> stack = new Stack<>();
/* 294 */     stack.add(comp);
/* 295 */     while (!stack.isEmpty()) {
/* 296 */       Component current = stack.pop();
/* 297 */       if (current instanceof RTextArea) {
/* 298 */         return (RTextArea)current;
/*     */       }
/* 300 */       if (current instanceof Container) {
/* 301 */         Container container = (Container)current;
/* 302 */         stack.addAll(Arrays.asList(container.getComponents()));
/*     */       } 
/*     */     } 
/* 305 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rtextarea\RTextScrollPane.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */