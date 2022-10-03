/*     */ package com.formdev.flatlaf.demo;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.ComboBoxModel;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.MutableComboBoxModel;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.basic.BasicComboBoxRenderer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LookAndFeelsComboBox
/*     */   extends JComboBox<UIManager.LookAndFeelInfo>
/*     */ {
/*  36 */   private final PropertyChangeListener lafListener = this::lafChanged;
/*     */ 
/*     */   
/*     */   public LookAndFeelsComboBox() {
/*  40 */     setRenderer(new BasicComboBoxRenderer()
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
/*     */           {
/*  48 */             value = (value != null) ? ((UIManager.LookAndFeelInfo)value).getName() : UIManager.getLookAndFeel().getName();
/*  49 */             return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void addLookAndFeel(String name, String className) {
/*  55 */     getMutableModel().addElement(new UIManager.LookAndFeelInfo(name, className));
/*     */   }
/*     */   
/*     */   public String getSelectedLookAndFeel() {
/*  59 */     Object sel = getSelectedItem();
/*  60 */     return (sel instanceof UIManager.LookAndFeelInfo) ? ((UIManager.LookAndFeelInfo)sel).getClassName() : null;
/*     */   }
/*     */   
/*     */   public void setSelectedLookAndFeel(String className) {
/*  64 */     setSelectedIndex(getIndexOfLookAndFeel(className));
/*     */   }
/*     */   
/*     */   public void selectedCurrentLookAndFeel() {
/*  68 */     setSelectedLookAndFeel(UIManager.getLookAndFeel().getClass().getName());
/*     */   }
/*     */   
/*     */   public void removeLookAndFeel(String className) {
/*  72 */     int index = getIndexOfLookAndFeel(className);
/*  73 */     if (index >= 0)
/*  74 */       getMutableModel().removeElementAt(index); 
/*     */   }
/*     */   
/*     */   public int getIndexOfLookAndFeel(String className) {
/*  78 */     ComboBoxModel<UIManager.LookAndFeelInfo> model = getModel();
/*  79 */     int size = model.getSize();
/*  80 */     for (int i = 0; i < size; i++) {
/*  81 */       if (className.equals(((UIManager.LookAndFeelInfo)model.getElementAt(i)).getClassName()))
/*  82 */         return i; 
/*     */     } 
/*  84 */     return -1;
/*     */   }
/*     */   
/*     */   private MutableComboBoxModel<UIManager.LookAndFeelInfo> getMutableModel() {
/*  88 */     return (MutableComboBoxModel<UIManager.LookAndFeelInfo>)getModel();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addNotify() {
/*  93 */     super.addNotify();
/*     */     
/*  95 */     selectedCurrentLookAndFeel();
/*  96 */     UIManager.addPropertyChangeListener(this.lafListener);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeNotify() {
/* 101 */     super.removeNotify();
/*     */     
/* 103 */     UIManager.removePropertyChangeListener(this.lafListener);
/*     */   }
/*     */   
/*     */   void lafChanged(PropertyChangeEvent e) {
/* 107 */     if ("lookAndFeel".equals(e.getPropertyName()))
/* 108 */       selectedCurrentLookAndFeel(); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\demo\LookAndFeelsComboBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */