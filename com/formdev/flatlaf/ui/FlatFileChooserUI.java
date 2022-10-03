/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import com.formdev.flatlaf.util.ScaledImageIcon;
/*     */ import com.formdev.flatlaf.util.UIScale;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Insets;
/*     */ import java.io.File;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JFileChooser;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.filechooser.FileView;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicFileChooserUI;
/*     */ import javax.swing.plaf.metal.MetalFileChooserUI;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FlatFileChooserUI
/*     */   extends MetalFileChooserUI
/*     */ {
/* 137 */   private final FlatFileView fileView = new FlatFileView();
/*     */   
/*     */   public static ComponentUI createUI(JComponent c) {
/* 140 */     return new FlatFileChooserUI((JFileChooser)c);
/*     */   }
/*     */   
/*     */   public FlatFileChooserUI(JFileChooser filechooser) {
/* 144 */     super(filechooser);
/*     */   }
/*     */ 
/*     */   
/*     */   public void installComponents(JFileChooser fc) {
/* 149 */     super.installComponents(fc);
/*     */     
/* 151 */     patchUI(fc);
/*     */   }
/*     */ 
/*     */   
/*     */   private void patchUI(JFileChooser fc) {
/* 156 */     Component topPanel = fc.getComponent(0);
/* 157 */     if (topPanel instanceof JPanel && ((JPanel)topPanel)
/* 158 */       .getLayout() instanceof java.awt.BorderLayout) {
/*     */       
/* 160 */       Component topButtonPanel = ((JPanel)topPanel).getComponent(0);
/* 161 */       if (topButtonPanel instanceof JPanel && ((JPanel)topButtonPanel)
/* 162 */         .getLayout() instanceof javax.swing.BoxLayout) {
/*     */         
/* 164 */         Insets margin = UIManager.getInsets("Button.margin");
/* 165 */         Component[] comps = ((JPanel)topButtonPanel).getComponents();
/* 166 */         for (int i = comps.length - 1; i >= 0; i--) {
/* 167 */           Component c = comps[i];
/* 168 */           if (c instanceof javax.swing.JButton || c instanceof javax.swing.JToggleButton) {
/* 169 */             AbstractButton b = (AbstractButton)c;
/* 170 */             b.putClientProperty("JButton.buttonType", "toolBarButton");
/*     */             
/* 172 */             b.setMargin(margin);
/* 173 */             b.setFocusable(false);
/* 174 */           } else if (c instanceof javax.swing.Box.Filler) {
/* 175 */             ((JPanel)topButtonPanel).remove(i);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*     */     try {
/* 182 */       Component directoryComboBox = ((JPanel)topPanel).getComponent(2);
/* 183 */       if (directoryComboBox instanceof JComboBox) {
/* 184 */         int maximumRowCount = UIManager.getInt("ComboBox.maximumRowCount");
/* 185 */         if (maximumRowCount > 0)
/* 186 */           ((JComboBox)directoryComboBox).setMaximumRowCount(maximumRowCount); 
/*     */       } 
/* 188 */     } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Dimension getPreferredSize(JComponent c) {
/* 195 */     return UIScale.scale(super.getPreferredSize(c));
/*     */   }
/*     */ 
/*     */   
/*     */   public Dimension getMinimumSize(JComponent c) {
/* 200 */     return UIScale.scale(super.getMinimumSize(c));
/*     */   }
/*     */ 
/*     */   
/*     */   public FileView getFileView(JFileChooser fc) {
/* 205 */     return this.fileView;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearIconCache() {
/* 210 */     this.fileView.clearIconCache();
/*     */   }
/*     */ 
/*     */   
/*     */   private class FlatFileView
/*     */     extends BasicFileChooserUI.BasicFileView
/*     */   {
/*     */     private FlatFileView() {}
/*     */     
/*     */     public Icon getIcon(File f) {
/*     */       ScaledImageIcon scaledImageIcon;
/* 221 */       Icon icon = getCachedIcon(f);
/* 222 */       if (icon != null) {
/* 223 */         return icon;
/*     */       }
/*     */       
/* 226 */       if (f != null) {
/* 227 */         icon = FlatFileChooserUI.this.getFileChooser().getFileSystemView().getSystemIcon(f);
/*     */         
/* 229 */         if (icon != null) {
/* 230 */           if (icon instanceof ImageIcon)
/* 231 */             scaledImageIcon = new ScaledImageIcon((ImageIcon)icon); 
/* 232 */           cacheIcon(f, (Icon)scaledImageIcon);
/* 233 */           return (Icon)scaledImageIcon;
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 238 */       icon = super.getIcon(f);
/*     */       
/* 240 */       if (icon instanceof ImageIcon) {
/* 241 */         scaledImageIcon = new ScaledImageIcon((ImageIcon)icon);
/* 242 */         cacheIcon(f, (Icon)scaledImageIcon);
/*     */       } 
/*     */       
/* 245 */       return (Icon)scaledImageIcon;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatFileChooserUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */