/*     */ package org.fife.rsta.ui;
/*     */ 
/*     */ import java.awt.Dialog;
/*     */ import java.awt.Frame;
/*     */ import java.awt.event.ActionEvent;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.InputMap;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JRootPane;
/*     */ import javax.swing.KeyStroke;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class EscapableDialog
/*     */   extends JDialog
/*     */ {
/*     */   private static final String ESCAPE_KEY = "OnEsc";
/*     */   
/*     */   public EscapableDialog() {
/*  44 */     init();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EscapableDialog(Dialog owner) {
/*  54 */     super(owner);
/*  55 */     init();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EscapableDialog(Dialog owner, boolean modal) {
/*  66 */     super(owner, modal);
/*  67 */     init();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EscapableDialog(Dialog owner, String title) {
/*  78 */     super(owner, title);
/*  79 */     init();
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
/*     */   public EscapableDialog(Dialog owner, String title, boolean modal) {
/*  91 */     super(owner, title, modal);
/*  92 */     init();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EscapableDialog(Frame owner) {
/* 102 */     super(owner);
/* 103 */     init();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EscapableDialog(Frame owner, boolean modal) {
/* 114 */     super(owner, modal);
/* 115 */     init();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EscapableDialog(Frame owner, String title) {
/* 126 */     super(owner, title);
/* 127 */     init();
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
/*     */   public EscapableDialog(Frame owner, String title, boolean modal) {
/* 139 */     super(owner, title, modal);
/* 140 */     init();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void escapePressed() {
/* 150 */     setVisible(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void init() {
/* 158 */     setEscapeClosesDialog(true);
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
/*     */   public void setEscapeClosesDialog(boolean closes) {
/* 171 */     JRootPane rootPane = getRootPane();
/* 172 */     InputMap im = rootPane.getInputMap(2);
/*     */     
/* 174 */     ActionMap actionMap = rootPane.getActionMap();
/* 175 */     KeyStroke ks = KeyStroke.getKeyStroke(27, 0);
/*     */     
/* 177 */     if (closes) {
/* 178 */       im.put(ks, "OnEsc");
/* 179 */       actionMap.put("OnEsc", new AbstractAction()
/*     */           {
/*     */             public void actionPerformed(ActionEvent e) {
/* 182 */               EscapableDialog.this.escapePressed();
/*     */             }
/*     */           });
/*     */     } else {
/*     */       
/* 187 */       im.remove(ks);
/* 188 */       actionMap.remove("OnEsc");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rst\\ui\EscapableDialog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */