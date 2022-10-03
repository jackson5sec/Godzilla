/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.EventQueue;
/*     */ import java.awt.event.FocusEvent;
/*     */ import java.awt.event.FocusListener;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicListUI;
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
/*     */ public class FlatListUI
/*     */   extends BasicListUI
/*     */ {
/*     */   protected Color selectionBackground;
/*     */   protected Color selectionForeground;
/*     */   protected Color selectionInactiveBackground;
/*     */   protected Color selectionInactiveForeground;
/*     */   
/*     */   public static ComponentUI createUI(JComponent c) {
/*  73 */     return new FlatListUI();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void installDefaults() {
/*  78 */     super.installDefaults();
/*     */     
/*  80 */     this.selectionBackground = UIManager.getColor("List.selectionBackground");
/*  81 */     this.selectionForeground = UIManager.getColor("List.selectionForeground");
/*  82 */     this.selectionInactiveBackground = UIManager.getColor("List.selectionInactiveBackground");
/*  83 */     this.selectionInactiveForeground = UIManager.getColor("List.selectionInactiveForeground");
/*     */     
/*  85 */     toggleSelectionColors();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void uninstallDefaults() {
/*  90 */     super.uninstallDefaults();
/*     */     
/*  92 */     this.selectionBackground = null;
/*  93 */     this.selectionForeground = null;
/*  94 */     this.selectionInactiveBackground = null;
/*  95 */     this.selectionInactiveForeground = null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected FocusListener createFocusListener() {
/* 100 */     return new BasicListUI.FocusHandler()
/*     */       {
/*     */         public void focusGained(FocusEvent e) {
/* 103 */           super.focusGained(e);
/* 104 */           FlatListUI.this.toggleSelectionColors();
/*     */         }
/*     */ 
/*     */         
/*     */         public void focusLost(FocusEvent e) {
/* 109 */           super.focusLost(e);
/*     */ 
/*     */           
/* 112 */           EventQueue.invokeLater(() -> FlatListUI.this.toggleSelectionColors());
/*     */         }
/*     */       };
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
/*     */   private void toggleSelectionColors() {
/* 129 */     if (this.list == null) {
/*     */       return;
/*     */     }
/* 132 */     if (FlatUIUtils.isPermanentFocusOwner(this.list)) {
/* 133 */       if (this.list.getSelectionBackground() == this.selectionInactiveBackground)
/* 134 */         this.list.setSelectionBackground(this.selectionBackground); 
/* 135 */       if (this.list.getSelectionForeground() == this.selectionInactiveForeground)
/* 136 */         this.list.setSelectionForeground(this.selectionForeground); 
/*     */     } else {
/* 138 */       if (this.list.getSelectionBackground() == this.selectionBackground)
/* 139 */         this.list.setSelectionBackground(this.selectionInactiveBackground); 
/* 140 */       if (this.list.getSelectionForeground() == this.selectionForeground)
/* 141 */         this.list.setSelectionForeground(this.selectionInactiveForeground); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatListUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */