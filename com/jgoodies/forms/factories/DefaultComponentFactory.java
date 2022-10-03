/*     */ package com.jgoodies.forms.factories;
/*     */ 
/*     */ import com.jgoodies.common.base.Preconditions;
/*     */ import com.jgoodies.common.base.Strings;
/*     */ import com.jgoodies.common.swing.MnemonicUtils;
/*     */ import com.jgoodies.forms.layout.Sizes;
/*     */ import com.jgoodies.forms.util.FormUtils;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JSeparator;
/*     */ import javax.swing.UIManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultComponentFactory
/*     */   implements ComponentFactory
/*     */ {
/*  82 */   private static final DefaultComponentFactory INSTANCE = new DefaultComponentFactory();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DefaultComponentFactory getInstance() {
/*  94 */     return INSTANCE;
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
/*     */ 
/*     */   
/*     */   public JLabel createLabel(String textWithMnemonic) {
/* 116 */     JLabel label = new FormsLabel();
/* 117 */     MnemonicUtils.configure(label, textWithMnemonic);
/* 118 */     return label;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JLabel createReadOnlyLabel(String textWithMnemonic) {
/* 142 */     JLabel label = new ReadOnlyLabel();
/* 143 */     MnemonicUtils.configure(label, textWithMnemonic);
/* 144 */     return label;
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
/*     */   public JButton createButton(Action action) {
/* 162 */     return new JButton(action);
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
/*     */   
/*     */   public JLabel createTitle(String textWithMnemonic) {
/* 183 */     JLabel label = new TitleLabel();
/* 184 */     MnemonicUtils.configure(label, textWithMnemonic);
/* 185 */     label.setVerticalAlignment(0);
/* 186 */     return label;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JLabel createHeaderLabel(String markedText) {
/* 192 */     return createTitle(markedText);
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
/*     */   
/*     */   public JComponent createSeparator(String textWithMnemonic) {
/* 213 */     return createSeparator(textWithMnemonic, 2);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JComponent createSeparator(String textWithMnemonic, int alignment) {
/* 238 */     if (Strings.isBlank(textWithMnemonic)) {
/* 239 */       return new JSeparator();
/*     */     }
/* 241 */     JLabel title = createTitle(textWithMnemonic);
/* 242 */     title.setHorizontalAlignment(alignment);
/* 243 */     return createSeparator(title);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JComponent createSeparator(JLabel label) {
/* 276 */     Preconditions.checkNotNull(label, "The label must not be null.");
/* 277 */     int horizontalAlignment = label.getHorizontalAlignment();
/* 278 */     Preconditions.checkArgument((horizontalAlignment == 2 || horizontalAlignment == 0 || horizontalAlignment == 4), "The label's horizontal alignment must be one of: LEFT, CENTER, RIGHT.");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 283 */     JPanel panel = new JPanel(new TitledSeparatorLayout(!FormUtils.isLafAqua()));
/* 284 */     panel.setOpaque(false);
/* 285 */     panel.add(label);
/* 286 */     panel.add(new JSeparator());
/* 287 */     if (horizontalAlignment == 0) {
/* 288 */       panel.add(new JSeparator());
/*     */     }
/* 290 */     return panel;
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
/*     */   private static class FormsLabel
/*     */     extends JLabel
/*     */   {
/*     */     private FormsLabel() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public AccessibleContext getAccessibleContext() {
/* 315 */       if (this.accessibleContext == null) {
/* 316 */         this.accessibleContext = new AccessibleFormsLabel();
/*     */       }
/* 318 */       return this.accessibleContext;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final class AccessibleFormsLabel
/*     */       extends JLabel.AccessibleJLabel
/*     */     {
/*     */       private AccessibleFormsLabel() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public String getAccessibleName() {
/* 339 */         if (this.accessibleName != null) {
/* 340 */           return this.accessibleName;
/*     */         }
/* 342 */         String text = DefaultComponentFactory.FormsLabel.this.getText();
/* 343 */         if (text == null) {
/* 344 */           return super.getAccessibleName();
/*     */         }
/* 346 */         return text.endsWith(":") ? text.substring(0, text.length() - 1) : text;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class ReadOnlyLabel
/*     */     extends FormsLabel
/*     */   {
/*     */     private ReadOnlyLabel() {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 362 */     private static final String[] UIMANAGER_KEYS = new String[] { "Label.disabledForeground", "Label.disabledText", "Label[Disabled].textForeground", "textInactiveText" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void updateUI() {
/* 370 */       super.updateUI();
/* 371 */       setForeground(getDisabledForeground());
/*     */     }
/*     */ 
/*     */     
/*     */     private static Color getDisabledForeground() {
/* 376 */       for (String key : UIMANAGER_KEYS) {
/* 377 */         Color foreground = UIManager.getColor(key);
/* 378 */         if (foreground != null)
/*     */         {
/* 380 */           return foreground;
/*     */         }
/*     */       } 
/* 383 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class TitleLabel
/*     */     extends FormsLabel
/*     */   {
/*     */     private TitleLabel() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void updateUI() {
/* 404 */       super.updateUI();
/* 405 */       Color foreground = getTitleColor();
/* 406 */       if (foreground != null) {
/* 407 */         setForeground(foreground);
/*     */       }
/* 409 */       setFont(getTitleFont());
/*     */     }
/*     */     
/*     */     private static Color getTitleColor() {
/* 413 */       return UIManager.getColor("TitledBorder.titleColor");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static Font getTitleFont() {
/* 427 */       return FormUtils.isLafAqua() ? UIManager.getFont("Label.font").deriveFont(1) : UIManager.getFont("TitledBorder.font");
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
/*     */   private static final class TitledSeparatorLayout
/*     */     implements LayoutManager
/*     */   {
/*     */     private final boolean centerSeparators;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private TitledSeparatorLayout(boolean centerSeparators) {
/* 450 */       this.centerSeparators = centerSeparators;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void addLayoutComponent(String name, Component comp) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void removeLayoutComponent(Component comp) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Dimension minimumLayoutSize(Container parent) {
/* 489 */       return preferredLayoutSize(parent);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Dimension preferredLayoutSize(Container parent) {
/* 503 */       Component label = getLabel(parent);
/* 504 */       Dimension labelSize = label.getPreferredSize();
/* 505 */       Insets insets = parent.getInsets();
/* 506 */       int width = labelSize.width + insets.left + insets.right;
/* 507 */       int height = labelSize.height + insets.top + insets.bottom;
/* 508 */       return new Dimension(width, height);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void layoutContainer(Container parent) {
/* 518 */       synchronized (parent.getTreeLock()) {
/*     */         
/* 520 */         Dimension size = parent.getSize();
/* 521 */         Insets insets = parent.getInsets();
/* 522 */         int width = size.width - insets.left - insets.right;
/*     */ 
/*     */         
/* 525 */         JLabel label = getLabel(parent);
/* 526 */         Dimension labelSize = label.getPreferredSize();
/* 527 */         int labelWidth = labelSize.width;
/* 528 */         int labelHeight = labelSize.height;
/* 529 */         Component separator1 = parent.getComponent(1);
/* 530 */         int separatorHeight = (separator1.getPreferredSize()).height;
/*     */         
/* 532 */         FontMetrics metrics = label.getFontMetrics(label.getFont());
/* 533 */         int ascent = metrics.getMaxAscent();
/* 534 */         int hGapDlu = this.centerSeparators ? 3 : 1;
/* 535 */         int hGap = Sizes.dialogUnitXAsPixel(hGapDlu, label);
/* 536 */         int vOffset = this.centerSeparators ? (1 + (labelHeight - separatorHeight) / 2) : (ascent - separatorHeight / 2);
/*     */ 
/*     */ 
/*     */         
/* 540 */         int alignment = label.getHorizontalAlignment();
/* 541 */         int y = insets.top;
/* 542 */         if (alignment == 2) {
/* 543 */           int x = insets.left;
/* 544 */           label.setBounds(x, y, labelWidth, labelHeight);
/* 545 */           x += labelWidth;
/* 546 */           x += hGap;
/* 547 */           int separatorWidth = size.width - insets.right - x;
/* 548 */           separator1.setBounds(x, y + vOffset, separatorWidth, separatorHeight);
/* 549 */         } else if (alignment == 4) {
/* 550 */           int x = insets.left + width - labelWidth;
/* 551 */           label.setBounds(x, y, labelWidth, labelHeight);
/* 552 */           x -= hGap;
/* 553 */           x--;
/* 554 */           int separatorWidth = x - insets.left;
/* 555 */           separator1.setBounds(insets.left, y + vOffset, separatorWidth, separatorHeight);
/*     */         } else {
/* 557 */           int xOffset = (width - labelWidth - 2 * hGap) / 2;
/* 558 */           int x = insets.left;
/* 559 */           separator1.setBounds(x, y + vOffset, xOffset - 1, separatorHeight);
/* 560 */           x += xOffset;
/* 561 */           x += hGap;
/* 562 */           label.setBounds(x, y, labelWidth, labelHeight);
/* 563 */           x += labelWidth;
/* 564 */           x += hGap;
/* 565 */           Component separator2 = parent.getComponent(2);
/* 566 */           int separatorWidth = size.width - insets.right - x;
/* 567 */           separator2.setBounds(x, y + vOffset, separatorWidth, separatorHeight);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private static JLabel getLabel(Container parent) {
/* 573 */       return (JLabel)parent.getComponent(0);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\forms\factories\DefaultComponentFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */