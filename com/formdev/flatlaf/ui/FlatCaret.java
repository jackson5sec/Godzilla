/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import java.awt.EventQueue;
/*     */ import java.awt.event.FocusEvent;
/*     */ import java.awt.event.MouseEvent;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.text.DefaultCaret;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.JTextComponent;
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
/*     */ public class FlatCaret
/*     */   extends DefaultCaret
/*     */   implements UIResource
/*     */ {
/*     */   private final String selectAllOnFocusPolicy;
/*     */   private final boolean selectAllOnMouseClick;
/*     */   private boolean wasFocused;
/*     */   private boolean wasTemporaryLost;
/*     */   private boolean isMousePressed;
/*     */   
/*     */   public FlatCaret(String selectAllOnFocusPolicy, boolean selectAllOnMouseClick) {
/*  46 */     this.selectAllOnFocusPolicy = selectAllOnFocusPolicy;
/*  47 */     this.selectAllOnMouseClick = selectAllOnMouseClick;
/*     */   }
/*     */ 
/*     */   
/*     */   public void install(JTextComponent c) {
/*  52 */     super.install(c);
/*     */ 
/*     */ 
/*     */     
/*  56 */     Document doc = c.getDocument();
/*  57 */     if (doc != null && getDot() == 0 && getMark() == 0) {
/*  58 */       int length = doc.getLength();
/*  59 */       if (length > 0) {
/*  60 */         setDot(length);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void focusGained(FocusEvent e) {
/*  66 */     if (!this.wasTemporaryLost && (!this.isMousePressed || this.selectAllOnMouseClick))
/*  67 */       selectAllOnFocusGained(); 
/*  68 */     this.wasTemporaryLost = false;
/*  69 */     this.wasFocused = true;
/*     */     
/*  71 */     super.focusGained(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public void focusLost(FocusEvent e) {
/*  76 */     this.wasTemporaryLost = e.isTemporary();
/*  77 */     super.focusLost(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public void mousePressed(MouseEvent e) {
/*  82 */     this.isMousePressed = true;
/*  83 */     super.mousePressed(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public void mouseReleased(MouseEvent e) {
/*  88 */     this.isMousePressed = false;
/*  89 */     super.mouseReleased(e);
/*     */   }
/*     */   
/*     */   protected void selectAllOnFocusGained() {
/*  93 */     JTextComponent c = getComponent();
/*  94 */     Document doc = c.getDocument();
/*  95 */     if (doc == null || !c.isEnabled() || !c.isEditable()) {
/*     */       return;
/*     */     }
/*  98 */     Object selectAllOnFocusPolicy = c.getClientProperty("JTextField.selectAllOnFocusPolicy");
/*  99 */     if (selectAllOnFocusPolicy == null) {
/* 100 */       selectAllOnFocusPolicy = this.selectAllOnFocusPolicy;
/*     */     }
/* 102 */     if ("never".equals(selectAllOnFocusPolicy)) {
/*     */       return;
/*     */     }
/* 105 */     if (!"always".equals(selectAllOnFocusPolicy)) {
/*     */ 
/*     */ 
/*     */       
/* 109 */       if (this.wasFocused) {
/*     */         return;
/*     */       }
/*     */       
/* 113 */       int dot = getDot();
/* 114 */       int mark = getMark();
/* 115 */       if (dot != mark || dot != doc.getLength()) {
/*     */         return;
/*     */       }
/*     */     } 
/*     */     
/* 120 */     if (c instanceof javax.swing.JFormattedTextField) {
/* 121 */       EventQueue.invokeLater(() -> {
/*     */             setDot(0);
/*     */             moveDot(doc.getLength());
/*     */           });
/*     */     } else {
/* 126 */       setDot(0);
/* 127 */       moveDot(doc.getLength());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatCaret.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */