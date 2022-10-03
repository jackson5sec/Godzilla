/*     */ package org.fife.rsta.ac;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.ActionEvent;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import javax.swing.text.TextAction;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GoToMemberAction
/*     */   extends TextAction
/*     */ {
/*     */   private Class<?> outlineTreeClass;
/*     */   
/*     */   public GoToMemberAction(Class<?> outlineTreeClass) {
/*  56 */     super("GoToType");
/*  57 */     this.outlineTreeClass = outlineTreeClass;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void actionPerformed(ActionEvent e) {
/*  63 */     AbstractSourceTree tree = createTree();
/*  64 */     if (tree == null) {
/*  65 */       UIManager.getLookAndFeel().provideErrorFeedback(null);
/*     */       return;
/*     */     } 
/*  68 */     JTextComponent tc = getTextComponent(e);
/*  69 */     if (tc instanceof RSyntaxTextArea) {
/*  70 */       RSyntaxTextArea textArea = (RSyntaxTextArea)tc;
/*  71 */       Window parent = SwingUtilities.getWindowAncestor((Component)textArea);
/*  72 */       GoToMemberWindow gtmw = new GoToMemberWindow(parent, textArea, tree);
/*  73 */       setLocationBasedOn(gtmw, textArea);
/*  74 */       gtmw.setVisible(true);
/*     */     } else {
/*     */       
/*  77 */       UIManager.getLookAndFeel().provideErrorFeedback(null);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private AbstractSourceTree createTree() {
/*  88 */     AbstractSourceTree tree = null;
/*     */     try {
/*  90 */       tree = (AbstractSourceTree)this.outlineTreeClass.newInstance();
/*  91 */       tree.setSorted(true);
/*  92 */     } catch (RuntimeException re) {
/*  93 */       throw re;
/*  94 */     } catch (Exception e) {
/*  95 */       e.printStackTrace();
/*     */     } 
/*  97 */     return tree;
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
/*     */   private void setLocationBasedOn(GoToMemberWindow gtmw, RSyntaxTextArea textArea) {
/* 109 */     Rectangle visibleRect = textArea.getVisibleRect();
/* 110 */     Dimension gtmwPS = gtmw.getPreferredSize();
/* 111 */     int x = visibleRect.x + (visibleRect.width - gtmwPS.width) / 2;
/* 112 */     int y = visibleRect.y + (visibleRect.height - gtmwPS.height) / 2;
/* 113 */     Point p = new Point(x, y);
/* 114 */     SwingUtilities.convertPointToScreen(p, (Component)textArea);
/* 115 */     gtmw.setLocation(p);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\GoToMemberAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */