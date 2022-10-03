/*     */ package com.formdev.flatlaf.icons;
/*     */ 
/*     */ import com.formdev.flatlaf.FlatClientProperties;
/*     */ import com.formdev.flatlaf.ui.FlatButtonUI;
/*     */ import com.formdev.flatlaf.ui.FlatUIUtils;
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.geom.Path2D;
/*     */ import java.awt.geom.RoundRectangle2D;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.JComponent;
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
/*     */ public class FlatCheckBoxIcon
/*     */   extends FlatAbstractIcon
/*     */ {
/*  69 */   protected final String style = UIManager.getString("CheckBox.icon.style");
/*  70 */   public final int focusWidth = getUIInt("CheckBox.icon.focusWidth", 
/*  71 */       UIManager.getInt("Component.focusWidth"), this.style);
/*  72 */   protected final Color focusColor = FlatUIUtils.getUIColor("CheckBox.icon.focusColor", 
/*  73 */       UIManager.getColor("Component.focusColor"));
/*  74 */   protected final int arc = FlatUIUtils.getUIInt("CheckBox.arc", 2);
/*     */ 
/*     */   
/*  77 */   protected final Color borderColor = getUIColor("CheckBox.icon.borderColor", this.style);
/*  78 */   protected final Color background = getUIColor("CheckBox.icon.background", this.style);
/*  79 */   protected final Color selectedBorderColor = getUIColor("CheckBox.icon.selectedBorderColor", this.style);
/*  80 */   protected final Color selectedBackground = getUIColor("CheckBox.icon.selectedBackground", this.style);
/*  81 */   protected final Color checkmarkColor = getUIColor("CheckBox.icon.checkmarkColor", this.style);
/*     */ 
/*     */   
/*  84 */   protected final Color disabledBorderColor = getUIColor("CheckBox.icon.disabledBorderColor", this.style);
/*  85 */   protected final Color disabledBackground = getUIColor("CheckBox.icon.disabledBackground", this.style);
/*  86 */   protected final Color disabledCheckmarkColor = getUIColor("CheckBox.icon.disabledCheckmarkColor", this.style);
/*     */ 
/*     */   
/*  89 */   protected final Color focusedBorderColor = getUIColor("CheckBox.icon.focusedBorderColor", this.style);
/*  90 */   protected final Color focusedBackground = getUIColor("CheckBox.icon.focusedBackground", this.style);
/*  91 */   protected final Color selectedFocusedBorderColor = getUIColor("CheckBox.icon.selectedFocusedBorderColor", this.style);
/*  92 */   protected final Color selectedFocusedBackground = getUIColor("CheckBox.icon.selectedFocusedBackground", this.style);
/*  93 */   protected final Color selectedFocusedCheckmarkColor = getUIColor("CheckBox.icon.selectedFocusedCheckmarkColor", this.style);
/*     */ 
/*     */   
/*  96 */   protected final Color hoverBorderColor = getUIColor("CheckBox.icon.hoverBorderColor", this.style);
/*  97 */   protected final Color hoverBackground = getUIColor("CheckBox.icon.hoverBackground", this.style);
/*  98 */   protected final Color selectedHoverBackground = getUIColor("CheckBox.icon.selectedHoverBackground", this.style);
/*     */ 
/*     */   
/* 101 */   protected final Color pressedBackground = getUIColor("CheckBox.icon.pressedBackground", this.style);
/* 102 */   protected final Color selectedPressedBackground = getUIColor("CheckBox.icon.selectedPressedBackground", this.style);
/*     */   
/*     */   protected static Color getUIColor(String key, String style) {
/* 105 */     if (style != null) {
/* 106 */       Color color = UIManager.getColor(styleKey(key, style));
/* 107 */       if (color != null)
/* 108 */         return color; 
/*     */     } 
/* 110 */     return UIManager.getColor(key);
/*     */   }
/*     */   static final int ICON_SIZE = 15;
/*     */   protected static int getUIInt(String key, int defaultValue, String style) {
/* 114 */     if (style != null) {
/* 115 */       Object value = UIManager.get(styleKey(key, style));
/* 116 */       if (value instanceof Integer)
/* 117 */         return ((Integer)value).intValue(); 
/*     */     } 
/* 119 */     return FlatUIUtils.getUIInt(key, defaultValue);
/*     */   }
/*     */   
/*     */   private static String styleKey(String key, String style) {
/* 123 */     return key.replace(".icon.", ".icon[" + style + "].");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public FlatCheckBoxIcon() {
/* 129 */     super(15, 15, null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void paintIcon(Component c, Graphics2D g) {
/* 134 */     boolean indeterminate = isIndeterminate(c);
/* 135 */     boolean selected = (indeterminate || isSelected(c));
/* 136 */     boolean isFocused = FlatUIUtils.isPermanentFocusOwner(c);
/*     */ 
/*     */     
/* 139 */     if (isFocused && this.focusWidth > 0 && FlatButtonUI.isFocusPainted(c)) {
/* 140 */       g.setColor(getFocusColor(c));
/* 141 */       paintFocusBorder(c, g);
/*     */     } 
/*     */ 
/*     */     
/* 145 */     g.setColor(getBorderColor(c, selected));
/* 146 */     paintBorder(c, g);
/*     */ 
/*     */     
/* 149 */     g.setColor(FlatUIUtils.deriveColor(getBackground(c, selected), selected ? this.selectedBackground : this.background));
/*     */     
/* 151 */     paintBackground(c, g);
/*     */ 
/*     */     
/* 154 */     if (selected || indeterminate) {
/* 155 */       g.setColor(getCheckmarkColor(c, selected, isFocused));
/* 156 */       if (indeterminate) {
/* 157 */         paintIndeterminate(c, g);
/*     */       } else {
/* 159 */         paintCheckmark(c, g);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void paintFocusBorder(Component c, Graphics2D g) {
/* 165 */     int wh = 14 + this.focusWidth * 2;
/* 166 */     int arcwh = this.arc + this.focusWidth * 2;
/* 167 */     g.fillRoundRect(-this.focusWidth + 1, -this.focusWidth, wh, wh, arcwh, arcwh);
/*     */   }
/*     */   
/*     */   protected void paintBorder(Component c, Graphics2D g) {
/* 171 */     int arcwh = this.arc;
/* 172 */     g.fillRoundRect(1, 0, 14, 14, arcwh, arcwh);
/*     */   }
/*     */   
/*     */   protected void paintBackground(Component c, Graphics2D g) {
/* 176 */     int arcwh = this.arc - 1;
/* 177 */     g.fillRoundRect(2, 1, 12, 12, arcwh, arcwh);
/*     */   }
/*     */   
/*     */   protected void paintCheckmark(Component c, Graphics2D g) {
/* 181 */     Path2D.Float path = new Path2D.Float();
/* 182 */     path.moveTo(4.5F, 7.5F);
/* 183 */     path.lineTo(6.6F, 10.0F);
/* 184 */     path.lineTo(11.25F, 3.5F);
/*     */     
/* 186 */     g.setStroke(new BasicStroke(1.9F, 1, 1));
/* 187 */     g.draw(path);
/*     */   }
/*     */   
/*     */   protected void paintIndeterminate(Component c, Graphics2D g) {
/* 191 */     g.fill(new RoundRectangle2D.Float(3.75F, 5.75F, 8.5F, 2.5F, 2.0F, 2.0F));
/*     */   }
/*     */   
/*     */   protected boolean isIndeterminate(Component c) {
/* 195 */     return (c instanceof JComponent && FlatClientProperties.clientPropertyEquals((JComponent)c, "JButton.selectedState", "indeterminate"));
/*     */   }
/*     */   
/*     */   protected boolean isSelected(Component c) {
/* 199 */     return (c instanceof AbstractButton && ((AbstractButton)c).isSelected());
/*     */   }
/*     */   
/*     */   protected Color getFocusColor(Component c) {
/* 203 */     return this.focusColor;
/*     */   }
/*     */   
/*     */   protected Color getBorderColor(Component c, boolean selected) {
/* 207 */     return FlatButtonUI.buttonStateColor(c, selected ? this.selectedBorderColor : this.borderColor, this.disabledBorderColor, (selected && this.selectedFocusedBorderColor != null) ? this.selectedFocusedBorderColor : this.focusedBorderColor, this.hoverBorderColor, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Color getBackground(Component c, boolean selected) {
/* 216 */     return FlatButtonUI.buttonStateColor(c, selected ? this.selectedBackground : this.background, this.disabledBackground, (selected && this.selectedFocusedBackground != null) ? this.selectedFocusedBackground : this.focusedBackground, (selected && this.selectedHoverBackground != null) ? this.selectedHoverBackground : this.hoverBackground, (selected && this.selectedPressedBackground != null) ? this.selectedPressedBackground : this.pressedBackground);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Color getCheckmarkColor(Component c, boolean selected, boolean isFocused) {
/* 225 */     return c.isEnabled() ? ((selected && isFocused && this.selectedFocusedCheckmarkColor != null) ? this.selectedFocusedCheckmarkColor : this.checkmarkColor) : this.disabledCheckmarkColor;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\icons\FlatCheckBoxIcon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */