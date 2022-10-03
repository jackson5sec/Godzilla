/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import com.formdev.flatlaf.FlatClientProperties;
/*     */ import com.formdev.flatlaf.FlatLaf;
/*     */ import com.formdev.flatlaf.util.UIScale;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.GradientPaint;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.geom.RoundRectangle2D;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.util.function.Supplier;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicButtonListener;
/*     */ import javax.swing.plaf.basic.BasicButtonUI;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FlatButtonUI
/*     */   extends BasicButtonUI
/*     */ {
/*     */   protected int minimumWidth;
/*     */   protected int iconTextGap;
/*     */   protected Color background;
/*     */   protected Color foreground;
/*     */   protected Color startBackground;
/*     */   protected Color endBackground;
/*     */   protected Color focusedBackground;
/*     */   protected Color hoverBackground;
/*     */   protected Color pressedBackground;
/*     */   protected Color selectedBackground;
/*     */   protected Color selectedForeground;
/*     */   protected Color disabledBackground;
/*     */   protected Color disabledText;
/*     */   protected Color disabledSelectedBackground;
/*     */   protected Color defaultBackground;
/*     */   protected Color defaultEndBackground;
/*     */   protected Color defaultForeground;
/*     */   protected Color defaultFocusedBackground;
/*     */   protected Color defaultHoverBackground;
/*     */   protected Color defaultPressedBackground;
/*     */   protected boolean defaultBoldText;
/*     */   protected int shadowWidth;
/*     */   protected Color shadowColor;
/*     */   protected Color defaultShadowColor;
/*     */   protected Insets toolbarSpacingInsets;
/*     */   protected Color toolbarHoverBackground;
/*     */   protected Color toolbarPressedBackground;
/*     */   protected Color toolbarSelectedBackground;
/*     */   private Icon helpButtonIcon;
/*     */   private boolean defaults_initialized = false;
/*     */   static final int TYPE_OTHER = -1;
/*     */   static final int TYPE_SQUARE = 0;
/*     */   static final int TYPE_ROUND_RECT = 1;
/*     */   
/*     */   public static ComponentUI createUI(JComponent c) {
/* 136 */     return FlatUIUtils.createSharedUI(FlatButtonUI.class, FlatButtonUI::new);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void installDefaults(AbstractButton b) {
/* 141 */     super.installDefaults(b);
/*     */     
/* 143 */     if (!this.defaults_initialized) {
/* 144 */       String prefix = getPropertyPrefix();
/*     */       
/* 146 */       this.minimumWidth = UIManager.getInt(prefix + "minimumWidth");
/* 147 */       this.iconTextGap = FlatUIUtils.getUIInt(prefix + "iconTextGap", 4);
/*     */       
/* 149 */       this.background = UIManager.getColor(prefix + "background");
/* 150 */       this.foreground = UIManager.getColor(prefix + "foreground");
/*     */       
/* 152 */       this.startBackground = UIManager.getColor(prefix + "startBackground");
/* 153 */       this.endBackground = UIManager.getColor(prefix + "endBackground");
/* 154 */       this.focusedBackground = UIManager.getColor(prefix + "focusedBackground");
/* 155 */       this.hoverBackground = UIManager.getColor(prefix + "hoverBackground");
/* 156 */       this.pressedBackground = UIManager.getColor(prefix + "pressedBackground");
/* 157 */       this.selectedBackground = UIManager.getColor(prefix + "selectedBackground");
/* 158 */       this.selectedForeground = UIManager.getColor(prefix + "selectedForeground");
/* 159 */       this.disabledBackground = UIManager.getColor(prefix + "disabledBackground");
/* 160 */       this.disabledText = UIManager.getColor(prefix + "disabledText");
/* 161 */       this.disabledSelectedBackground = UIManager.getColor(prefix + "disabledSelectedBackground");
/*     */       
/* 163 */       if (UIManager.getBoolean("Button.paintShadow")) {
/* 164 */         this.shadowWidth = FlatUIUtils.getUIInt("Button.shadowWidth", 2);
/* 165 */         this.shadowColor = UIManager.getColor("Button.shadowColor");
/* 166 */         this.defaultShadowColor = UIManager.getColor("Button.default.shadowColor");
/*     */       } else {
/* 168 */         this.shadowWidth = 0;
/* 169 */         this.shadowColor = null;
/* 170 */         this.defaultShadowColor = null;
/*     */       } 
/*     */       
/* 173 */       this.defaultBackground = FlatUIUtils.getUIColor("Button.default.startBackground", "Button.default.background");
/* 174 */       this.defaultEndBackground = UIManager.getColor("Button.default.endBackground");
/* 175 */       this.defaultForeground = UIManager.getColor("Button.default.foreground");
/* 176 */       this.defaultFocusedBackground = UIManager.getColor("Button.default.focusedBackground");
/* 177 */       this.defaultHoverBackground = UIManager.getColor("Button.default.hoverBackground");
/* 178 */       this.defaultPressedBackground = UIManager.getColor("Button.default.pressedBackground");
/* 179 */       this.defaultBoldText = UIManager.getBoolean("Button.default.boldText");
/*     */       
/* 181 */       this.toolbarSpacingInsets = UIManager.getInsets("Button.toolbar.spacingInsets");
/* 182 */       this.toolbarHoverBackground = UIManager.getColor(prefix + "toolbar.hoverBackground");
/* 183 */       this.toolbarPressedBackground = UIManager.getColor(prefix + "toolbar.pressedBackground");
/* 184 */       this.toolbarSelectedBackground = UIManager.getColor(prefix + "toolbar.selectedBackground");
/*     */       
/* 186 */       this.helpButtonIcon = UIManager.getIcon("HelpButton.icon");
/*     */       
/* 188 */       this.defaults_initialized = true;
/*     */     } 
/*     */     
/* 191 */     if (this.startBackground != null) {
/* 192 */       Color bg = b.getBackground();
/* 193 */       if (bg == null || bg instanceof javax.swing.plaf.UIResource) {
/* 194 */         b.setBackground(this.startBackground);
/*     */       }
/*     */     } 
/* 197 */     LookAndFeel.installProperty(b, "opaque", Boolean.valueOf(false));
/* 198 */     LookAndFeel.installProperty(b, "iconTextGap", Integer.valueOf(UIScale.scale(this.iconTextGap)));
/*     */     
/* 200 */     MigLayoutVisualPadding.install(b);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void uninstallDefaults(AbstractButton b) {
/* 205 */     super.uninstallDefaults(b);
/*     */     
/* 207 */     MigLayoutVisualPadding.uninstall(b);
/* 208 */     this.defaults_initialized = false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BasicButtonListener createButtonListener(AbstractButton b) {
/* 213 */     return new FlatButtonListener(b);
/*     */   }
/*     */   
/*     */   protected void propertyChange(AbstractButton b, PropertyChangeEvent e) {
/* 217 */     switch (e.getPropertyName()) {
/*     */       case "JButton.squareSize":
/*     */       case "JComponent.minimumWidth":
/*     */       case "JComponent.minimumHeight":
/* 221 */         b.revalidate();
/*     */         break;
/*     */       
/*     */       case "JButton.buttonType":
/* 225 */         b.revalidate();
/* 226 */         b.repaint();
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   static boolean isContentAreaFilled(Component c) {
/* 232 */     return (!(c instanceof AbstractButton) || ((AbstractButton)c).isContentAreaFilled());
/*     */   }
/*     */   
/*     */   public static boolean isFocusPainted(Component c) {
/* 236 */     return (!(c instanceof AbstractButton) || ((AbstractButton)c).isFocusPainted());
/*     */   }
/*     */   
/*     */   static boolean isDefaultButton(Component c) {
/* 240 */     return (c instanceof JButton && ((JButton)c).isDefaultButton());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isIconOnlyOrSingleCharacterButton(Component c) {
/* 248 */     if (!(c instanceof JButton) && !(c instanceof javax.swing.JToggleButton)) {
/* 249 */       return false;
/*     */     }
/* 251 */     Icon icon = ((AbstractButton)c).getIcon();
/* 252 */     String text = ((AbstractButton)c).getText();
/* 253 */     return ((icon != null && (text == null || text.isEmpty())) || (icon == null && text != null && ("..."
/*     */       
/* 255 */       .equals(text) || text
/* 256 */       .length() == 1 || (text
/* 257 */       .length() == 2 && Character.isSurrogatePair(text.charAt(0), text.charAt(1))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int getButtonType(Component c) {
/* 265 */     if (!(c instanceof AbstractButton)) {
/* 266 */       return -1;
/*     */     }
/* 268 */     Object value = ((AbstractButton)c).getClientProperty("JButton.buttonType");
/* 269 */     if (!(value instanceof String)) {
/* 270 */       return -1;
/*     */     }
/* 272 */     switch ((String)value) { case "square":
/* 273 */         return 0;
/* 274 */       case "roundRect": return 1; }
/* 275 */      return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   static boolean isHelpButton(Component c) {
/* 280 */     return (c instanceof JButton && FlatClientProperties.clientPropertyEquals((JButton)c, "JButton.buttonType", "help"));
/*     */   }
/*     */   
/*     */   static boolean isToolBarButton(Component c) {
/* 284 */     return (c.getParent() instanceof javax.swing.JToolBar || (c instanceof AbstractButton && 
/* 285 */       FlatClientProperties.clientPropertyEquals((AbstractButton)c, "JButton.buttonType", "toolBarButton")));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void update(Graphics g, JComponent c) {
/* 291 */     if (c.isOpaque()) {
/* 292 */       FlatUIUtils.paintParentBackground(g, c);
/*     */     }
/* 294 */     if (isHelpButton(c)) {
/* 295 */       this.helpButtonIcon.paintIcon(c, g, 0, 0);
/*     */       
/*     */       return;
/*     */     } 
/* 299 */     if (isContentAreaFilled(c)) {
/* 300 */       paintBackground(g, c);
/*     */     }
/* 302 */     paint(g, c);
/*     */   }
/*     */   
/*     */   protected void paintBackground(Graphics g, JComponent c) {
/* 306 */     Color background = getBackground(c);
/* 307 */     if (background == null) {
/*     */       return;
/*     */     }
/* 310 */     Graphics2D g2 = (Graphics2D)g.create();
/*     */     try {
/* 312 */       FlatUIUtils.setRenderingHints(g2);
/*     */       
/* 314 */       boolean isToolBarButton = isToolBarButton(c);
/* 315 */       float focusWidth = isToolBarButton ? 0.0F : FlatUIUtils.getBorderFocusWidth(c);
/* 316 */       float arc = FlatUIUtils.getBorderArc(c);
/*     */       
/* 318 */       boolean def = isDefaultButton(c);
/*     */       
/* 320 */       int x = 0;
/* 321 */       int y = 0;
/* 322 */       int width = c.getWidth();
/* 323 */       int height = c.getHeight();
/*     */       
/* 325 */       if (isToolBarButton) {
/* 326 */         Insets spacing = UIScale.scale(this.toolbarSpacingInsets);
/* 327 */         x += spacing.left;
/* 328 */         y += spacing.top;
/* 329 */         width -= spacing.left + spacing.right;
/* 330 */         height -= spacing.top + spacing.bottom;
/*     */       } 
/*     */ 
/*     */       
/* 334 */       Color shadowColor = def ? this.defaultShadowColor : this.shadowColor;
/* 335 */       if (!isToolBarButton && shadowColor != null && this.shadowWidth > 0 && focusWidth > 0.0F && (
/* 336 */         !isFocusPainted(c) || !FlatUIUtils.isPermanentFocusOwner(c)) && c.isEnabled()) {
/*     */         
/* 338 */         g2.setColor(shadowColor);
/* 339 */         g2.fill(new RoundRectangle2D.Float(focusWidth, focusWidth + UIScale.scale(this.shadowWidth), width - focusWidth * 2.0F, height - focusWidth * 2.0F, arc, arc));
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 344 */       Color startBg = def ? this.defaultBackground : this.startBackground;
/* 345 */       Color endBg = def ? this.defaultEndBackground : this.endBackground;
/* 346 */       if (background == startBg && endBg != null && !startBg.equals(endBg)) {
/* 347 */         g2.setPaint(new GradientPaint(0.0F, 0.0F, startBg, 0.0F, height, endBg));
/*     */       } else {
/* 349 */         g2.setColor(FlatUIUtils.deriveColor(background, getBackgroundBase(c, def)));
/*     */       } 
/* 351 */       FlatUIUtils.paintComponentBackground(g2, x, y, width, height, focusWidth, arc);
/*     */     } finally {
/* 353 */       g2.dispose();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void paint(Graphics g, JComponent c) {
/* 359 */     super.paint(FlatLabelUI.createGraphicsHTMLTextYCorrection(g, c), c);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void paintText(Graphics g, AbstractButton b, Rectangle textRect, String text) {
/* 364 */     if (isHelpButton(b)) {
/*     */       return;
/*     */     }
/* 367 */     if (this.defaultBoldText && isDefaultButton(b) && b.getFont() instanceof javax.swing.plaf.UIResource) {
/* 368 */       Font boldFont = g.getFont().deriveFont(1);
/* 369 */       g.setFont(boldFont);
/*     */       
/* 371 */       int boldWidth = b.getFontMetrics(boldFont).stringWidth(text);
/* 372 */       if (boldWidth > textRect.width) {
/* 373 */         textRect.x -= (boldWidth - textRect.width) / 2;
/* 374 */         textRect.width = boldWidth;
/*     */       } 
/*     */     } 
/*     */     
/* 378 */     paintText(g, b, textRect, text, getForeground(b));
/*     */   }
/*     */   
/*     */   public static void paintText(Graphics g, AbstractButton b, Rectangle textRect, String text, Color foreground) {
/* 382 */     FontMetrics fm = b.getFontMetrics(b.getFont());
/* 383 */     int mnemonicIndex = FlatLaf.isShowMnemonics() ? b.getDisplayedMnemonicIndex() : -1;
/*     */     
/* 385 */     g.setColor(foreground);
/* 386 */     FlatUIUtils.drawStringUnderlineCharAt(b, g, text, mnemonicIndex, textRect.x, textRect.y + fm
/* 387 */         .getAscent());
/*     */   }
/*     */   
/*     */   protected Color getBackground(JComponent c) {
/* 391 */     if (((AbstractButton)c).isSelected()) {
/*     */ 
/*     */       
/* 394 */       boolean toolBarButton = isToolBarButton(c);
/* 395 */       return buttonStateColor(c, toolBarButton ? this.toolbarSelectedBackground : this.selectedBackground, toolBarButton ? this.toolbarSelectedBackground : this.disabledSelectedBackground, null, null, toolBarButton ? this.toolbarPressedBackground : this.pressedBackground);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 402 */     if (!c.isEnabled()) {
/* 403 */       return this.disabledBackground;
/*     */     }
/*     */     
/* 406 */     if (isToolBarButton(c)) {
/* 407 */       ButtonModel model = ((AbstractButton)c).getModel();
/* 408 */       if (model.isPressed())
/* 409 */         return this.toolbarPressedBackground; 
/* 410 */       if (model.isRollover()) {
/* 411 */         return this.toolbarHoverBackground;
/*     */       }
/*     */       
/* 414 */       Color bg = c.getBackground();
/* 415 */       if (isCustomBackground(bg)) {
/* 416 */         return bg;
/*     */       }
/*     */       
/* 419 */       return null;
/*     */     } 
/*     */     
/* 422 */     boolean def = isDefaultButton(c);
/* 423 */     return buttonStateColor(c, 
/* 424 */         getBackgroundBase(c, def), null, 
/*     */         
/* 426 */         isCustomBackground(c.getBackground()) ? null : (def ? this.defaultFocusedBackground : this.focusedBackground), def ? this.defaultHoverBackground : this.hoverBackground, def ? this.defaultPressedBackground : this.pressedBackground);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Color getBackgroundBase(JComponent c, boolean def) {
/* 433 */     Color bg = c.getBackground();
/* 434 */     if (isCustomBackground(bg)) {
/* 435 */       return bg;
/*     */     }
/* 437 */     return def ? this.defaultBackground : bg;
/*     */   }
/*     */   
/*     */   protected boolean isCustomBackground(Color bg) {
/* 441 */     return (bg != this.background && (this.startBackground == null || bg != this.startBackground));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Color buttonStateColor(Component c, Color enabledColor, Color disabledColor, Color focusedColor, Color hoverColor, Color pressedColor) {
/* 447 */     AbstractButton b = (c instanceof AbstractButton) ? (AbstractButton)c : null;
/*     */     
/* 449 */     if (!c.isEnabled()) {
/* 450 */       return disabledColor;
/*     */     }
/* 452 */     if (pressedColor != null && b != null && b.getModel().isPressed()) {
/* 453 */       return pressedColor;
/*     */     }
/* 455 */     if (hoverColor != null && b != null && b.getModel().isRollover()) {
/* 456 */       return hoverColor;
/*     */     }
/* 458 */     if (focusedColor != null && isFocusPainted(c) && FlatUIUtils.isPermanentFocusOwner(c)) {
/* 459 */       return focusedColor;
/*     */     }
/* 461 */     return enabledColor;
/*     */   }
/*     */   
/*     */   protected Color getForeground(JComponent c) {
/* 465 */     if (!c.isEnabled()) {
/* 466 */       return this.disabledText;
/*     */     }
/* 468 */     if (((AbstractButton)c).isSelected() && !isToolBarButton(c)) {
/* 469 */       return this.selectedForeground;
/*     */     }
/*     */     
/* 472 */     Color fg = c.getForeground();
/* 473 */     if (isCustomForeground(fg)) {
/* 474 */       return fg;
/*     */     }
/* 476 */     boolean def = isDefaultButton(c);
/* 477 */     return def ? this.defaultForeground : fg;
/*     */   }
/*     */   
/*     */   protected boolean isCustomForeground(Color fg) {
/* 481 */     return (fg != this.foreground);
/*     */   }
/*     */ 
/*     */   
/*     */   public Dimension getPreferredSize(JComponent c) {
/* 486 */     if (isHelpButton(c)) {
/* 487 */       return new Dimension(this.helpButtonIcon.getIconWidth(), this.helpButtonIcon.getIconHeight());
/*     */     }
/* 489 */     Dimension prefSize = super.getPreferredSize(c);
/* 490 */     if (prefSize == null) {
/* 491 */       return null;
/*     */     }
/*     */     
/* 494 */     boolean isIconOnlyOrSingleCharacter = isIconOnlyOrSingleCharacterButton(c);
/* 495 */     if (FlatClientProperties.clientPropertyBoolean(c, "JButton.squareSize", false)) {
/*     */       
/* 497 */       prefSize.width = prefSize.height = Math.max(prefSize.width, prefSize.height);
/* 498 */     } else if (isIconOnlyOrSingleCharacter && ((AbstractButton)c).getIcon() == null) {
/*     */       
/* 500 */       prefSize.width = Math.max(prefSize.width, prefSize.height);
/* 501 */     } else if (!isIconOnlyOrSingleCharacter && !isToolBarButton(c) && c.getBorder() instanceof FlatButtonBorder) {
/*     */       
/* 503 */       float focusWidth = FlatUIUtils.getBorderFocusWidth(c);
/* 504 */       prefSize.width = Math.max(prefSize.width, UIScale.scale(FlatUIUtils.minimumWidth(c, this.minimumWidth)) + Math.round(focusWidth * 2.0F));
/* 505 */       prefSize.height = Math.max(prefSize.height, UIScale.scale(FlatUIUtils.minimumHeight(c, 0)) + Math.round(focusWidth * 2.0F));
/*     */     } 
/*     */     
/* 508 */     return prefSize;
/*     */   }
/*     */ 
/*     */   
/*     */   protected class FlatButtonListener
/*     */     extends BasicButtonListener
/*     */   {
/*     */     private final AbstractButton b;
/*     */ 
/*     */     
/*     */     protected FlatButtonListener(AbstractButton b) {
/* 519 */       super(b);
/* 520 */       this.b = b;
/*     */     }
/*     */ 
/*     */     
/*     */     public void propertyChange(PropertyChangeEvent e) {
/* 525 */       super.propertyChange(e);
/* 526 */       FlatButtonUI.this.propertyChange(this.b, e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatButtonUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */