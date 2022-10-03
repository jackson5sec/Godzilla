/*     */ package org.fife.rsta.ui;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Image;
/*     */ import java.awt.event.FocusEvent;
/*     */ import java.awt.event.FocusListener;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import org.fife.rsta.ui.search.AbstractSearchDialog;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AssistanceIconPanel
/*     */   extends DecorativeIconPanel
/*     */   implements PropertyChangeListener
/*     */ {
/*     */   private static String assistanceAvailable;
/*     */   
/*     */   public AssistanceIconPanel(JComponent comp) {
/*  49 */     init(comp);
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
/*     */   public AssistanceIconPanel(JComponent comp, int iconWidth) {
/*  63 */     super(iconWidth);
/*  64 */     init(comp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void init(JComponent comp) {
/*  71 */     if (comp != null) {
/*     */       
/*  73 */       ComponentListener listener = new ComponentListener();
/*     */       
/*  75 */       if (comp instanceof JComboBox) {
/*  76 */         JComboBox<?> combo = (JComboBox)comp;
/*  77 */         Component c = combo.getEditor().getEditorComponent();
/*  78 */         if (c instanceof JTextComponent) {
/*  79 */           JTextComponent tc = (JTextComponent)c;
/*  80 */           tc.addFocusListener(listener);
/*     */         } 
/*     */       } else {
/*     */         
/*  84 */         comp.addFocusListener(listener);
/*     */       } 
/*     */       
/*  87 */       comp.addPropertyChangeListener("AssistanceImage", this);
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
/*     */   static String getAssistanceAvailableText() {
/* 102 */     if (assistanceAvailable == null) {
/* 103 */       assistanceAvailable = AbstractSearchDialog.getString("ContentAssistAvailable");
/*     */     }
/* 105 */     return assistanceAvailable;
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
/*     */   public void propertyChange(PropertyChangeEvent e) {
/* 118 */     Image img = (Image)e.getNewValue();
/* 119 */     setAssistanceEnabled(img);
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
/*     */   public void setAssistanceEnabled(Image img) {
/* 131 */     if (img == null) {
/* 132 */       setIcon(null);
/* 133 */       setToolTipText(null);
/*     */     } else {
/*     */       
/* 136 */       setIcon(new ImageIcon(img));
/* 137 */       setToolTipText(getAssistanceAvailableText());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class ComponentListener
/*     */     implements FocusListener
/*     */   {
/*     */     private ComponentListener() {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void focusGained(FocusEvent e) {
/* 154 */       AssistanceIconPanel.this.setShowIcon(true);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void focusLost(FocusEvent e) {
/* 164 */       AssistanceIconPanel.this.setShowIcon(false);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rst\\ui\AssistanceIconPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */