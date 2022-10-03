/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import com.formdev.flatlaf.util.DerivedColor;
/*     */ import com.formdev.flatlaf.util.UIScale;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import java.awt.KeyboardFocusManager;
/*     */ import java.awt.Paint;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JSpinner;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.JTree;
/*     */ import javax.swing.JViewport;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.basic.BasicBorders;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FlatBorder
/*     */   extends BasicBorders.MarginBorder
/*     */ {
/*  70 */   protected final int focusWidth = UIManager.getInt("Component.focusWidth");
/*  71 */   protected final float innerFocusWidth = FlatUIUtils.getUIFloat("Component.innerFocusWidth", 0.0F);
/*  72 */   protected final float innerOutlineWidth = FlatUIUtils.getUIFloat("Component.innerOutlineWidth", 0.0F);
/*  73 */   protected final Color focusColor = UIManager.getColor("Component.focusColor");
/*  74 */   protected final Color borderColor = UIManager.getColor("Component.borderColor");
/*  75 */   protected final Color disabledBorderColor = UIManager.getColor("Component.disabledBorderColor");
/*  76 */   protected final Color focusedBorderColor = UIManager.getColor("Component.focusedBorderColor");
/*     */   
/*  78 */   protected final Color errorBorderColor = UIManager.getColor("Component.error.borderColor");
/*  79 */   protected final Color errorFocusedBorderColor = UIManager.getColor("Component.error.focusedBorderColor");
/*  80 */   protected final Color warningBorderColor = UIManager.getColor("Component.warning.borderColor");
/*  81 */   protected final Color warningFocusedBorderColor = UIManager.getColor("Component.warning.focusedBorderColor");
/*  82 */   protected final Color customBorderColor = UIManager.getColor("Component.custom.borderColor");
/*     */ 
/*     */   
/*     */   public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
/*  86 */     Graphics2D g2 = (Graphics2D)g.create();
/*     */     try {
/*  88 */       FlatUIUtils.setRenderingHints(g2);
/*     */       
/*  90 */       float focusWidth = UIScale.scale(getFocusWidth(c));
/*  91 */       float borderWidth = UIScale.scale(getBorderWidth(c));
/*  92 */       float arc = UIScale.scale(getArc(c));
/*  93 */       Color outlineColor = getOutlineColor(c);
/*     */ 
/*     */       
/*  96 */       if (outlineColor != null || isFocused(c)) {
/*  97 */         float innerWidth = (!isCellEditor(c) && !(c instanceof JScrollPane)) ? ((outlineColor != null) ? this.innerOutlineWidth : this.innerFocusWidth) : 0.0F;
/*     */ 
/*     */ 
/*     */         
/* 101 */         g2.setColor((outlineColor != null) ? outlineColor : getFocusColor(c));
/* 102 */         FlatUIUtils.paintComponentOuterBorder(g2, x, y, width, height, focusWidth, borderWidth + 
/* 103 */             UIScale.scale(innerWidth), arc);
/*     */       } 
/*     */ 
/*     */       
/* 107 */       g2.setPaint((outlineColor != null) ? outlineColor : getBorderColor(c));
/* 108 */       FlatUIUtils.paintComponentBorder(g2, x, y, width, height, focusWidth, borderWidth, arc);
/*     */     } finally {
/* 110 */       g2.dispose();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Color getOutlineColor(Component c) {
/* 119 */     if (!(c instanceof JComponent)) {
/* 120 */       return null;
/*     */     }
/* 122 */     Object outline = ((JComponent)c).getClientProperty("JComponent.outline");
/* 123 */     if (outline instanceof String)
/* 124 */     { switch ((String)outline) {
/*     */         case "error":
/* 126 */           return isFocused(c) ? this.errorFocusedBorderColor : this.errorBorderColor;
/*     */         
/*     */         case "warning":
/* 129 */           return isFocused(c) ? this.warningFocusedBorderColor : this.warningBorderColor;
/*     */       }  }
/* 131 */     else { if (outline instanceof Color) {
/* 132 */         Color color = (Color)outline;
/*     */         
/* 134 */         if (!isFocused(c) && this.customBorderColor instanceof DerivedColor)
/* 135 */           color = ((DerivedColor)this.customBorderColor).derive(color); 
/* 136 */         return color;
/* 137 */       }  if (outline instanceof Color[] && ((Color[])outline).length >= 2)
/* 138 */         return ((Color[])outline)[isFocused(c) ? 0 : 1];  }
/*     */     
/* 140 */     return null;
/*     */   }
/*     */   
/*     */   protected Color getFocusColor(Component c) {
/* 144 */     return this.focusColor;
/*     */   }
/*     */   
/*     */   protected Paint getBorderColor(Component c) {
/* 148 */     return isEnabled(c) ? (
/* 149 */       isFocused(c) ? this.focusedBorderColor : this.borderColor) : this.disabledBorderColor;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isEnabled(Component c) {
/* 154 */     if (c instanceof JScrollPane) {
/*     */       
/* 156 */       JViewport viewport = ((JScrollPane)c).getViewport();
/* 157 */       Component view = (viewport != null) ? viewport.getView() : null;
/* 158 */       if (view != null && !isEnabled(view)) {
/* 159 */         return false;
/*     */       }
/*     */     } 
/* 162 */     return (c.isEnabled() && (!(c instanceof JTextComponent) || ((JTextComponent)c).isEditable()));
/*     */   }
/*     */   
/*     */   protected boolean isFocused(Component c) {
/* 166 */     if (c instanceof JScrollPane) {
/* 167 */       JViewport viewport = ((JScrollPane)c).getViewport();
/* 168 */       Component view = (viewport != null) ? viewport.getView() : null;
/* 169 */       if (view != null) {
/* 170 */         if (FlatUIUtils.isPermanentFocusOwner(view)) {
/* 171 */           return true;
/*     */         }
/* 173 */         if ((view instanceof JTable && ((JTable)view).isEditing()) || (view instanceof JTree && ((JTree)view)
/* 174 */           .isEditing())) {
/*     */           
/* 176 */           Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
/* 177 */           if (focusOwner != null)
/* 178 */             return SwingUtilities.isDescendingFrom(focusOwner, view); 
/*     */         } 
/*     */       } 
/* 181 */       return false;
/* 182 */     }  if (c instanceof JComboBox && ((JComboBox)c).isEditable()) {
/* 183 */       Component editorComponent = ((JComboBox)c).getEditor().getEditorComponent();
/* 184 */       return (editorComponent != null) ? FlatUIUtils.isPermanentFocusOwner(editorComponent) : false;
/* 185 */     }  if (c instanceof JSpinner) {
/* 186 */       if (FlatUIUtils.isPermanentFocusOwner(c)) {
/* 187 */         return true;
/*     */       }
/* 189 */       JComponent editor = ((JSpinner)c).getEditor();
/* 190 */       if (editor instanceof JSpinner.DefaultEditor) {
/* 191 */         JTextField textField = ((JSpinner.DefaultEditor)editor).getTextField();
/* 192 */         if (textField != null)
/* 193 */           return FlatUIUtils.isPermanentFocusOwner(textField); 
/*     */       } 
/* 195 */       return false;
/*     */     } 
/* 197 */     return FlatUIUtils.isPermanentFocusOwner(c);
/*     */   }
/*     */   
/*     */   protected boolean isCellEditor(Component c) {
/* 201 */     return FlatUIUtils.isCellEditor(c);
/*     */   }
/*     */ 
/*     */   
/*     */   public Insets getBorderInsets(Component c, Insets insets) {
/* 206 */     float focusWidth = UIScale.scale(getFocusWidth(c));
/* 207 */     float ow = focusWidth + UIScale.scale(getLineWidth(c));
/*     */     
/* 209 */     insets = super.getBorderInsets(c, insets);
/* 210 */     insets.top = Math.round(UIScale.scale(insets.top) + ow);
/* 211 */     insets.left = Math.round(UIScale.scale(insets.left) + ow);
/* 212 */     insets.bottom = Math.round(UIScale.scale(insets.bottom) + ow);
/* 213 */     insets.right = Math.round(UIScale.scale(insets.right) + ow);
/*     */     
/* 215 */     if (isCellEditor(c)) {
/*     */       
/* 217 */       insets.top = insets.bottom = 0;
/*     */ 
/*     */       
/* 220 */       if (c.getComponentOrientation().isLeftToRight()) {
/* 221 */         insets.right = 0;
/*     */       } else {
/* 223 */         insets.left = 0;
/*     */       } 
/*     */     } 
/* 226 */     return insets;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getFocusWidth(Component c) {
/* 233 */     if (isCellEditor(c)) {
/* 234 */       return 0;
/*     */     }
/* 236 */     return this.focusWidth;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getLineWidth(Component c) {
/* 244 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getBorderWidth(Component c) {
/* 252 */     return getLineWidth(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getArc(Component c) {
/* 259 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatBorder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */