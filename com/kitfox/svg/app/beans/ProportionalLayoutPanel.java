/*     */ package com.kitfox.svg.app.beans;
/*     */ 
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.ComponentAdapter;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProportionalLayoutPanel
/*     */   extends JPanel
/*     */ {
/*     */   public static final long serialVersionUID = 1L;
/*     */   float topMargin;
/*     */   float bottomMargin;
/*     */   float leftMargin;
/*     */   float rightMargin;
/*     */   private JPanel jPanel1;
/*     */   
/*     */   public ProportionalLayoutPanel() {
/*  62 */     initComponents();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addNotify() {
/*  68 */     super.addNotify();
/*     */     
/*  70 */     Rectangle rect = getBounds();
/*  71 */     JOptionPane.showMessageDialog(this, "" + rect);
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
/*     */   private void initComponents() {
/*  83 */     this.jPanel1 = new JPanel();
/*     */     
/*  85 */     setLayout((LayoutManager)null);
/*     */     
/*  87 */     addComponentListener(new ComponentAdapter()
/*     */         {
/*     */           
/*     */           public void componentResized(ComponentEvent evt)
/*     */           {
/*  92 */             ProportionalLayoutPanel.this.formComponentResized(evt);
/*     */           }
/*     */ 
/*     */           
/*     */           public void componentShown(ComponentEvent evt) {
/*  97 */             ProportionalLayoutPanel.this.formComponentShown(evt);
/*     */           }
/*     */         });
/*     */     
/* 101 */     add(this.jPanel1);
/* 102 */     this.jPanel1.setBounds(80, 90, 280, 160);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void formComponentShown(ComponentEvent evt) {
/* 109 */     JOptionPane.showMessageDialog(this, "" + getWidth() + ", " + getHeight());
/*     */   }
/*     */   
/*     */   private void formComponentResized(ComponentEvent evt) {}
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\app\beans\ProportionalLayoutPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */