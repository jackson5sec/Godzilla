/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import com.formdev.flatlaf.util.UIScale;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import java.awt.event.FocusEvent;
/*     */ import java.awt.event.FocusListener;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JSpinner;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicSpinnerUI;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FlatSpinnerUI
/*     */   extends BasicSpinnerUI
/*     */ {
/*     */   private Handler handler;
/*     */   protected int minimumWidth;
/*     */   protected String buttonStyle;
/*     */   protected String arrowType;
/*     */   protected boolean isIntelliJTheme;
/*     */   protected Color borderColor;
/*     */   protected Color disabledBorderColor;
/*     */   protected Color disabledBackground;
/*     */   protected Color disabledForeground;
/*     */   protected Color buttonBackground;
/*     */   protected Color buttonArrowColor;
/*     */   protected Color buttonDisabledArrowColor;
/*     */   protected Color buttonHoverArrowColor;
/*     */   protected Color buttonPressedArrowColor;
/*     */   protected Insets padding;
/*     */   
/*     */   public static ComponentUI createUI(JComponent c) {
/*  98 */     return new FlatSpinnerUI();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void installDefaults() {
/* 103 */     super.installDefaults();
/*     */     
/* 105 */     LookAndFeel.installProperty(this.spinner, "opaque", Boolean.valueOf(false));
/*     */     
/* 107 */     this.minimumWidth = UIManager.getInt("Component.minimumWidth");
/* 108 */     this.buttonStyle = UIManager.getString("Spinner.buttonStyle");
/* 109 */     this.arrowType = UIManager.getString("Component.arrowType");
/* 110 */     this.isIntelliJTheme = UIManager.getBoolean("Component.isIntelliJTheme");
/* 111 */     this.borderColor = UIManager.getColor("Component.borderColor");
/* 112 */     this.disabledBorderColor = UIManager.getColor("Component.disabledBorderColor");
/* 113 */     this.disabledBackground = UIManager.getColor("Spinner.disabledBackground");
/* 114 */     this.disabledForeground = UIManager.getColor("Spinner.disabledForeground");
/* 115 */     this.buttonBackground = UIManager.getColor("Spinner.buttonBackground");
/* 116 */     this.buttonArrowColor = UIManager.getColor("Spinner.buttonArrowColor");
/* 117 */     this.buttonDisabledArrowColor = UIManager.getColor("Spinner.buttonDisabledArrowColor");
/* 118 */     this.buttonHoverArrowColor = UIManager.getColor("Spinner.buttonHoverArrowColor");
/* 119 */     this.buttonPressedArrowColor = UIManager.getColor("Spinner.buttonPressedArrowColor");
/* 120 */     this.padding = UIManager.getInsets("Spinner.padding");
/*     */ 
/*     */     
/* 123 */     this.padding = UIScale.scale(this.padding);
/*     */     
/* 125 */     MigLayoutVisualPadding.install(this.spinner);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void uninstallDefaults() {
/* 130 */     super.uninstallDefaults();
/*     */     
/* 132 */     this.borderColor = null;
/* 133 */     this.disabledBorderColor = null;
/* 134 */     this.disabledBackground = null;
/* 135 */     this.disabledForeground = null;
/* 136 */     this.buttonBackground = null;
/* 137 */     this.buttonArrowColor = null;
/* 138 */     this.buttonDisabledArrowColor = null;
/* 139 */     this.buttonHoverArrowColor = null;
/* 140 */     this.buttonPressedArrowColor = null;
/* 141 */     this.padding = null;
/*     */     
/* 143 */     MigLayoutVisualPadding.uninstall(this.spinner);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void installListeners() {
/* 148 */     super.installListeners();
/*     */     
/* 150 */     addEditorFocusListener(this.spinner.getEditor());
/* 151 */     this.spinner.addFocusListener(getHandler());
/* 152 */     this.spinner.addPropertyChangeListener(getHandler());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void uninstallListeners() {
/* 157 */     super.uninstallListeners();
/*     */     
/* 159 */     removeEditorFocusListener(this.spinner.getEditor());
/* 160 */     this.spinner.removeFocusListener(getHandler());
/* 161 */     this.spinner.removePropertyChangeListener(getHandler());
/*     */     
/* 163 */     this.handler = null;
/*     */   }
/*     */   
/*     */   private Handler getHandler() {
/* 167 */     if (this.handler == null)
/* 168 */       this.handler = new Handler(); 
/* 169 */     return this.handler;
/*     */   }
/*     */ 
/*     */   
/*     */   protected JComponent createEditor() {
/* 174 */     JComponent editor = super.createEditor();
/*     */ 
/*     */     
/* 177 */     editor.setOpaque(false);
/* 178 */     JTextField textField = getEditorTextField(editor);
/* 179 */     if (textField != null) {
/* 180 */       textField.setOpaque(false);
/*     */     }
/* 182 */     updateEditorColors();
/* 183 */     return editor;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void replaceEditor(JComponent oldEditor, JComponent newEditor) {
/* 188 */     super.replaceEditor(oldEditor, newEditor);
/*     */     
/* 190 */     removeEditorFocusListener(oldEditor);
/* 191 */     addEditorFocusListener(newEditor);
/* 192 */     updateEditorColors();
/*     */   }
/*     */   
/*     */   private void addEditorFocusListener(JComponent editor) {
/* 196 */     JTextField textField = getEditorTextField(editor);
/* 197 */     if (textField != null)
/* 198 */       textField.addFocusListener(getHandler()); 
/*     */   }
/*     */   
/*     */   private void removeEditorFocusListener(JComponent editor) {
/* 202 */     JTextField textField = getEditorTextField(editor);
/* 203 */     if (textField != null)
/* 204 */       textField.removeFocusListener(getHandler()); 
/*     */   }
/*     */   
/*     */   private void updateEditorColors() {
/* 208 */     JTextField textField = getEditorTextField(this.spinner.getEditor());
/* 209 */     if (textField != null) {
/*     */ 
/*     */ 
/*     */       
/* 213 */       textField.setForeground(FlatUIUtils.nonUIResource(getForeground(true)));
/* 214 */       textField.setDisabledTextColor(FlatUIUtils.nonUIResource(getForeground(false)));
/*     */     } 
/*     */   }
/*     */   
/*     */   private static JTextField getEditorTextField(JComponent editor) {
/* 219 */     return (editor instanceof JSpinner.DefaultEditor) ? ((JSpinner.DefaultEditor)editor)
/* 220 */       .getTextField() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Color getBackground(boolean enabled) {
/* 225 */     return enabled ? this.spinner
/* 226 */       .getBackground() : (this.isIntelliJTheme ? 
/* 227 */       FlatUIUtils.getParentBackground(this.spinner) : this.disabledBackground);
/*     */   }
/*     */   
/*     */   protected Color getForeground(boolean enabled) {
/* 231 */     return enabled ? this.spinner.getForeground() : this.disabledForeground;
/*     */   }
/*     */ 
/*     */   
/*     */   protected LayoutManager createLayout() {
/* 236 */     return getHandler();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Component createNextButton() {
/* 241 */     return createArrowButton(1, "Spinner.nextButton");
/*     */   }
/*     */ 
/*     */   
/*     */   protected Component createPreviousButton() {
/* 246 */     return createArrowButton(5, "Spinner.previousButton");
/*     */   }
/*     */   
/*     */   private Component createArrowButton(int direction, String name) {
/* 250 */     FlatArrowButton button = new FlatArrowButton(direction, this.arrowType, this.buttonArrowColor, this.buttonDisabledArrowColor, this.buttonHoverArrowColor, null, this.buttonPressedArrowColor, null);
/*     */     
/* 252 */     button.setName(name);
/* 253 */     button.setYOffset((direction == 1) ? 1 : -1);
/* 254 */     if (direction == 1) {
/* 255 */       installNextButtonListeners(button);
/*     */     } else {
/* 257 */       installPreviousButtonListeners(button);
/* 258 */     }  return button;
/*     */   }
/*     */ 
/*     */   
/*     */   public void update(Graphics g, JComponent c) {
/* 263 */     float focusWidth = FlatUIUtils.getBorderFocusWidth(c);
/* 264 */     float arc = FlatUIUtils.getBorderArc(c);
/*     */ 
/*     */     
/* 267 */     if (c.isOpaque() && (focusWidth > 0.0F || arc > 0.0F)) {
/* 268 */       FlatUIUtils.paintParentBackground(g, c);
/*     */     }
/* 270 */     Graphics2D g2 = (Graphics2D)g;
/* 271 */     Object[] oldRenderingHints = FlatUIUtils.setRenderingHints(g2);
/*     */     
/* 273 */     int width = c.getWidth();
/* 274 */     int height = c.getHeight();
/* 275 */     boolean enabled = this.spinner.isEnabled();
/*     */ 
/*     */     
/* 278 */     g2.setColor(getBackground(enabled));
/* 279 */     FlatUIUtils.paintComponentBackground(g2, 0, 0, width, height, focusWidth, arc);
/*     */ 
/*     */     
/* 282 */     boolean paintButton = !"none".equals(this.buttonStyle);
/* 283 */     Handler handler = getHandler();
/* 284 */     if (paintButton && (handler.nextButton != null || handler.previousButton != null)) {
/* 285 */       Component button = (handler.nextButton != null) ? handler.nextButton : handler.previousButton;
/* 286 */       int arrowX = button.getX();
/* 287 */       int arrowWidth = button.getWidth();
/* 288 */       boolean isLeftToRight = this.spinner.getComponentOrientation().isLeftToRight();
/*     */ 
/*     */       
/* 291 */       if (enabled) {
/* 292 */         g2.setColor(this.buttonBackground);
/* 293 */         Shape oldClip = g2.getClip();
/* 294 */         if (isLeftToRight) {
/* 295 */           g2.clipRect(arrowX, 0, width - arrowX, height);
/*     */         } else {
/* 297 */           g2.clipRect(0, 0, arrowX + arrowWidth, height);
/* 298 */         }  FlatUIUtils.paintComponentBackground(g2, 0, 0, width, height, focusWidth, arc);
/* 299 */         g2.setClip(oldClip);
/*     */       } 
/*     */ 
/*     */       
/* 303 */       g2.setColor(enabled ? this.borderColor : this.disabledBorderColor);
/* 304 */       float lw = UIScale.scale(1.0F);
/* 305 */       float lx = isLeftToRight ? arrowX : ((arrowX + arrowWidth) - lw);
/* 306 */       g2.fill(new Rectangle2D.Float(lx, focusWidth, lw, (height - 1) - focusWidth * 2.0F));
/*     */     } 
/*     */     
/* 309 */     paint(g, c);
/*     */     
/* 311 */     FlatUIUtils.resetRenderingHints(g, oldRenderingHints);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class Handler
/*     */     implements LayoutManager, FocusListener, PropertyChangeListener
/*     */   {
/* 321 */     private Component editor = null;
/*     */     
/*     */     private Component nextButton;
/*     */     private Component previousButton;
/*     */     
/*     */     public void addLayoutComponent(String name, Component c) {
/* 327 */       switch (name) { case "Editor":
/* 328 */           this.editor = c; break;
/* 329 */         case "Next": this.nextButton = c; break;
/* 330 */         case "Previous": this.previousButton = c;
/*     */           break; }
/*     */     
/*     */     }
/*     */     
/*     */     public void removeLayoutComponent(Component c) {
/* 336 */       if (c == this.editor) {
/* 337 */         this.editor = null;
/* 338 */       } else if (c == this.nextButton) {
/* 339 */         this.nextButton = null;
/* 340 */       } else if (c == this.previousButton) {
/* 341 */         this.previousButton = null;
/*     */       } 
/*     */     }
/*     */     
/*     */     public Dimension preferredLayoutSize(Container parent) {
/* 346 */       Insets insets = parent.getInsets();
/* 347 */       Dimension editorSize = (this.editor != null) ? this.editor.getPreferredSize() : new Dimension(0, 0);
/*     */ 
/*     */       
/* 350 */       int minimumWidth = FlatUIUtils.minimumWidth(FlatSpinnerUI.this.spinner, FlatSpinnerUI.this.minimumWidth);
/* 351 */       int innerHeight = editorSize.height + FlatSpinnerUI.this.padding.top + FlatSpinnerUI.this.padding.bottom;
/* 352 */       float focusWidth = FlatUIUtils.getBorderFocusWidth(FlatSpinnerUI.this.spinner);
/* 353 */       return new Dimension(
/* 354 */           Math.max(insets.left + insets.right + editorSize.width + FlatSpinnerUI.this.padding.left + FlatSpinnerUI.this.padding.right + innerHeight, UIScale.scale(minimumWidth) + Math.round(focusWidth * 2.0F)), insets.top + insets.bottom + innerHeight);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Dimension minimumLayoutSize(Container parent) {
/* 360 */       return preferredLayoutSize(parent);
/*     */     }
/*     */ 
/*     */     
/*     */     public void layoutContainer(Container parent) {
/* 365 */       Dimension size = parent.getSize();
/* 366 */       Insets insets = parent.getInsets();
/* 367 */       Rectangle r = FlatUIUtils.subtractInsets(new Rectangle(size), insets);
/*     */       
/* 369 */       if (this.nextButton == null && this.previousButton == null) {
/* 370 */         if (this.editor != null) {
/* 371 */           this.editor.setBounds(FlatUIUtils.subtractInsets(r, FlatSpinnerUI.this.padding));
/*     */         }
/*     */         return;
/*     */       } 
/* 375 */       Rectangle editorRect = new Rectangle(r);
/* 376 */       Rectangle buttonsRect = new Rectangle(r);
/*     */ 
/*     */       
/* 379 */       int buttonsWidth = r.height;
/* 380 */       buttonsRect.width = buttonsWidth;
/*     */       
/* 382 */       if (parent.getComponentOrientation().isLeftToRight()) {
/* 383 */         editorRect.width -= buttonsWidth;
/* 384 */         buttonsRect.x += editorRect.width;
/*     */       } else {
/* 386 */         editorRect.x += buttonsWidth;
/* 387 */         editorRect.width -= buttonsWidth;
/*     */       } 
/*     */       
/* 390 */       if (this.editor != null) {
/* 391 */         this.editor.setBounds(FlatUIUtils.subtractInsets(editorRect, FlatSpinnerUI.this.padding));
/*     */       }
/* 393 */       int nextHeight = buttonsRect.height / 2 + buttonsRect.height % 2;
/* 394 */       if (this.nextButton != null)
/* 395 */         this.nextButton.setBounds(buttonsRect.x, buttonsRect.y, buttonsRect.width, nextHeight); 
/* 396 */       if (this.previousButton != null) {
/*     */ 
/*     */         
/* 399 */         int previousY = buttonsRect.y + buttonsRect.height - nextHeight;
/* 400 */         this.previousButton.setBounds(buttonsRect.x, previousY, buttonsRect.width, nextHeight);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void focusGained(FocusEvent e) {
/* 408 */       FlatSpinnerUI.this.spinner.repaint();
/*     */ 
/*     */       
/* 411 */       if (e.getComponent() == FlatSpinnerUI.this.spinner) {
/* 412 */         JTextField textField = FlatSpinnerUI.getEditorTextField(FlatSpinnerUI.this.spinner.getEditor());
/* 413 */         if (textField != null) {
/* 414 */           textField.requestFocusInWindow();
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     public void focusLost(FocusEvent e) {
/* 420 */       FlatSpinnerUI.this.spinner.repaint();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void propertyChange(PropertyChangeEvent e) {
/* 427 */       switch (e.getPropertyName()) {
/*     */         case "foreground":
/*     */         case "enabled":
/* 430 */           FlatSpinnerUI.this.updateEditorColors();
/*     */           break;
/*     */         
/*     */         case "JComponent.roundRect":
/* 434 */           FlatSpinnerUI.this.spinner.repaint();
/*     */           break;
/*     */         
/*     */         case "JComponent.minimumWidth":
/* 438 */           FlatSpinnerUI.this.spinner.revalidate();
/*     */           break;
/*     */       } 
/*     */     }
/*     */     
/*     */     private Handler() {}
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatSpinnerUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */