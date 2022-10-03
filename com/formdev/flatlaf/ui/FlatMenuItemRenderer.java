/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import com.formdev.flatlaf.FlatLaf;
/*     */ import com.formdev.flatlaf.util.Graphics2DProxy;
/*     */ import com.formdev.flatlaf.util.HiDPIUtils;
/*     */ import com.formdev.flatlaf.util.SystemInfo;
/*     */ import com.formdev.flatlaf.util.UIScale;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Paint;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.InputEvent;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.text.AttributedCharacterIterator;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.text.View;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FlatMenuItemRenderer
/*     */ {
/*     */   protected final JMenuItem menuItem;
/*     */   protected final Icon checkIcon;
/*     */   protected final Icon arrowIcon;
/*     */   protected final Font acceleratorFont;
/*     */   protected final String acceleratorDelimiter;
/*  72 */   protected final int minimumWidth = UIManager.getInt("MenuItem.minimumWidth");
/*     */   protected final Dimension minimumIconSize;
/*  74 */   protected final int textAcceleratorGap = FlatUIUtils.getUIInt("MenuItem.textAcceleratorGap", 28);
/*  75 */   protected final int textNoAcceleratorGap = FlatUIUtils.getUIInt("MenuItem.textNoAcceleratorGap", 6);
/*  76 */   protected final int acceleratorArrowGap = FlatUIUtils.getUIInt("MenuItem.acceleratorArrowGap", 2);
/*     */   
/*  78 */   protected final Color checkBackground = UIManager.getColor("MenuItem.checkBackground");
/*  79 */   protected final Insets checkMargins = UIManager.getInsets("MenuItem.checkMargins");
/*     */   
/*  81 */   protected final Color underlineSelectionBackground = UIManager.getColor("MenuItem.underlineSelectionBackground");
/*  82 */   protected final Color underlineSelectionCheckBackground = UIManager.getColor("MenuItem.underlineSelectionCheckBackground");
/*  83 */   protected final Color underlineSelectionColor = UIManager.getColor("MenuItem.underlineSelectionColor");
/*  84 */   protected final int underlineSelectionHeight = UIManager.getInt("MenuItem.underlineSelectionHeight");
/*     */   
/*  86 */   protected final Color selectionBackground = UIManager.getColor("MenuItem.selectionBackground"); private KeyStroke cachedAccelerator;
/*     */   private String cachedAcceleratorText;
/*     */   private boolean cachedAcceleratorLeftToRight;
/*     */   
/*     */   protected FlatMenuItemRenderer(JMenuItem menuItem, Icon checkIcon, Icon arrowIcon, Font acceleratorFont, String acceleratorDelimiter) {
/*  91 */     this.menuItem = menuItem;
/*  92 */     this.checkIcon = checkIcon;
/*  93 */     this.arrowIcon = arrowIcon;
/*  94 */     this.acceleratorFont = acceleratorFont;
/*  95 */     this.acceleratorDelimiter = acceleratorDelimiter;
/*     */     
/*  97 */     Dimension minimumIconSize = UIManager.getDimension("MenuItem.minimumIconSize");
/*  98 */     this.minimumIconSize = (minimumIconSize != null) ? minimumIconSize : new Dimension(16, 16);
/*     */   }
/*     */   private static final char controlGlyph = '⌃'; private static final char optionGlyph = '⌥'; private static final char shiftGlyph = '⇧'; private static final char commandGlyph = '⌘';
/*     */   protected Dimension getPreferredMenuItemSize() {
/* 102 */     int width = 0;
/* 103 */     int height = 0;
/* 104 */     boolean isTopLevelMenu = isTopLevelMenu(this.menuItem);
/*     */     
/* 106 */     Rectangle viewRect = new Rectangle(0, 0, 2147483647, 2147483647);
/* 107 */     Rectangle iconRect = new Rectangle();
/* 108 */     Rectangle textRect = new Rectangle();
/*     */ 
/*     */     
/* 111 */     SwingUtilities.layoutCompoundLabel(this.menuItem, this.menuItem
/* 112 */         .getFontMetrics(this.menuItem.getFont()), this.menuItem.getText(), getIconForLayout(), this.menuItem
/* 113 */         .getVerticalAlignment(), this.menuItem.getHorizontalAlignment(), this.menuItem
/* 114 */         .getVerticalTextPosition(), this.menuItem.getHorizontalTextPosition(), viewRect, iconRect, textRect, 
/* 115 */         UIScale.scale(this.menuItem.getIconTextGap()));
/*     */ 
/*     */     
/* 118 */     Rectangle labelRect = iconRect.union(textRect);
/* 119 */     width += labelRect.width;
/* 120 */     height = Math.max(labelRect.height, height);
/*     */ 
/*     */     
/* 123 */     String accelText = getAcceleratorText();
/* 124 */     if (accelText != null) {
/*     */       
/* 126 */       width += UIScale.scale(!isTopLevelMenu ? this.textAcceleratorGap : this.menuItem.getIconTextGap());
/*     */       
/* 128 */       FontMetrics accelFm = this.menuItem.getFontMetrics(this.acceleratorFont);
/* 129 */       width += SwingUtilities.computeStringWidth(accelFm, accelText);
/* 130 */       height = Math.max(accelFm.getHeight(), height);
/*     */     } 
/*     */ 
/*     */     
/* 134 */     if (!isTopLevelMenu && this.arrowIcon != null) {
/*     */       
/* 136 */       if (accelText == null) {
/* 137 */         width += UIScale.scale(this.textNoAcceleratorGap);
/*     */       }
/*     */       
/* 140 */       width += UIScale.scale(this.acceleratorArrowGap);
/*     */       
/* 142 */       width += this.arrowIcon.getIconWidth();
/* 143 */       height = Math.max(this.arrowIcon.getIconHeight(), height);
/*     */     } 
/*     */ 
/*     */     
/* 147 */     Insets insets = this.menuItem.getInsets();
/* 148 */     width += insets.left + insets.right;
/* 149 */     height += insets.top + insets.bottom;
/*     */ 
/*     */     
/* 152 */     if (!isTopLevelMenu) {
/* 153 */       int minimumWidth = FlatUIUtils.minimumWidth(this.menuItem, this.minimumWidth);
/* 154 */       width = Math.max(width, UIScale.scale(minimumWidth));
/*     */     } 
/*     */     
/* 157 */     return new Dimension(width, height);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void layout(Rectangle viewRect, Rectangle iconRect, Rectangle textRect, Rectangle accelRect, Rectangle arrowRect, Rectangle labelRect) {
/* 163 */     boolean isTopLevelMenu = isTopLevelMenu(this.menuItem);
/*     */ 
/*     */     
/* 166 */     if (!isTopLevelMenu && this.arrowIcon != null) {
/* 167 */       arrowRect.width = this.arrowIcon.getIconWidth();
/* 168 */       arrowRect.height = this.arrowIcon.getIconHeight();
/*     */     } else {
/* 170 */       arrowRect.setSize(0, 0);
/* 171 */     }  viewRect.y += centerOffset(viewRect.height, arrowRect.height);
/*     */ 
/*     */     
/* 174 */     String accelText = getAcceleratorText();
/* 175 */     if (accelText != null) {
/* 176 */       FontMetrics accelFm = this.menuItem.getFontMetrics(this.acceleratorFont);
/* 177 */       accelRect.width = SwingUtilities.computeStringWidth(accelFm, accelText);
/* 178 */       accelRect.height = accelFm.getHeight();
/*     */       
/* 180 */       viewRect.y += centerOffset(viewRect.height, accelRect.height);
/*     */     } else {
/* 182 */       accelRect.setBounds(0, 0, 0, 0);
/*     */     } 
/*     */     
/* 185 */     int accelArrowGap = !isTopLevelMenu ? UIScale.scale(this.acceleratorArrowGap) : 0;
/* 186 */     if (this.menuItem.getComponentOrientation().isLeftToRight()) {
/*     */       
/* 188 */       arrowRect.x = viewRect.x + viewRect.width - arrowRect.width;
/* 189 */       accelRect.x = arrowRect.x - accelArrowGap - accelRect.width;
/*     */     } else {
/*     */       
/* 192 */       arrowRect.x = viewRect.x;
/* 193 */       accelRect.x = arrowRect.x + accelArrowGap + arrowRect.width;
/*     */     } 
/*     */ 
/*     */     
/* 197 */     int accelArrowWidth = accelRect.width + arrowRect.width;
/* 198 */     if (accelText != null)
/* 199 */       accelArrowWidth += UIScale.scale(!isTopLevelMenu ? this.textAcceleratorGap : this.menuItem.getIconTextGap()); 
/* 200 */     if (!isTopLevelMenu && this.arrowIcon != null) {
/* 201 */       if (accelText == null)
/* 202 */         accelArrowWidth += UIScale.scale(this.textNoAcceleratorGap); 
/* 203 */       accelArrowWidth += UIScale.scale(this.acceleratorArrowGap);
/*     */     } 
/*     */ 
/*     */     
/* 207 */     labelRect.setBounds(viewRect);
/* 208 */     labelRect.width -= accelArrowWidth;
/* 209 */     if (!this.menuItem.getComponentOrientation().isLeftToRight()) {
/* 210 */       labelRect.x += accelArrowWidth;
/*     */     }
/*     */     
/* 213 */     SwingUtilities.layoutCompoundLabel(this.menuItem, this.menuItem
/* 214 */         .getFontMetrics(this.menuItem.getFont()), this.menuItem.getText(), getIconForLayout(), this.menuItem
/* 215 */         .getVerticalAlignment(), this.menuItem.getHorizontalAlignment(), this.menuItem
/* 216 */         .getVerticalTextPosition(), this.menuItem.getHorizontalTextPosition(), labelRect, iconRect, textRect, 
/* 217 */         UIScale.scale(this.menuItem.getIconTextGap()));
/*     */   }
/*     */   
/*     */   private static int centerOffset(int wh1, int wh2) {
/* 221 */     return wh1 / 2 - wh2 / 2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void paintMenuItem(Graphics g, Color selectionBackground, Color selectionForeground, Color disabledForeground, Color acceleratorForeground, Color acceleratorSelectionForeground) {
/* 227 */     Rectangle viewRect = new Rectangle(this.menuItem.getWidth(), this.menuItem.getHeight());
/*     */ 
/*     */     
/* 230 */     Insets insets = this.menuItem.getInsets();
/* 231 */     viewRect.x += insets.left;
/* 232 */     viewRect.y += insets.top;
/* 233 */     viewRect.width -= insets.left + insets.right;
/* 234 */     viewRect.height -= insets.top + insets.bottom;
/*     */     
/* 236 */     Rectangle iconRect = new Rectangle();
/* 237 */     Rectangle textRect = new Rectangle();
/* 238 */     Rectangle accelRect = new Rectangle();
/* 239 */     Rectangle arrowRect = new Rectangle();
/* 240 */     Rectangle labelRect = new Rectangle();
/*     */     
/* 242 */     layout(viewRect, iconRect, textRect, accelRect, arrowRect, labelRect);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 253 */     boolean underlineSelection = isUnderlineSelection();
/* 254 */     paintBackground(g, underlineSelection ? this.underlineSelectionBackground : selectionBackground);
/* 255 */     if (underlineSelection && isArmedOrSelected(this.menuItem))
/* 256 */       paintUnderlineSelection(g, this.underlineSelectionColor, this.underlineSelectionHeight); 
/* 257 */     paintIcon(g, iconRect, getIconForPainting(), underlineSelection ? this.underlineSelectionCheckBackground : this.checkBackground);
/* 258 */     paintText(g, textRect, this.menuItem.getText(), selectionForeground, disabledForeground);
/* 259 */     paintAccelerator(g, accelRect, getAcceleratorText(), acceleratorForeground, acceleratorSelectionForeground, disabledForeground);
/* 260 */     if (!isTopLevelMenu(this.menuItem))
/* 261 */       paintArrowIcon(g, arrowRect, this.arrowIcon); 
/*     */   }
/*     */   
/*     */   protected void paintBackground(Graphics g, Color selectionBackground) {
/* 265 */     boolean armedOrSelected = isArmedOrSelected(this.menuItem);
/* 266 */     if (this.menuItem.isOpaque() || armedOrSelected) {
/*     */       
/* 268 */       g.setColor(armedOrSelected ? 
/* 269 */           deriveBackground(selectionBackground) : this.menuItem
/* 270 */           .getBackground());
/* 271 */       g.fillRect(0, 0, this.menuItem.getWidth(), this.menuItem.getHeight());
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void paintUnderlineSelection(Graphics g, Color underlineSelectionColor, int underlineSelectionHeight) {
/* 276 */     int width = this.menuItem.getWidth();
/* 277 */     int height = this.menuItem.getHeight();
/*     */     
/* 279 */     int underlineHeight = UIScale.scale(underlineSelectionHeight);
/* 280 */     g.setColor(underlineSelectionColor);
/* 281 */     if (isTopLevelMenu(this.menuItem)) {
/*     */       
/* 283 */       g.fillRect(0, height - underlineHeight, width, underlineHeight);
/* 284 */     } else if (this.menuItem.getComponentOrientation().isLeftToRight()) {
/*     */       
/* 286 */       g.fillRect(0, 0, underlineHeight, height);
/*     */     } else {
/*     */       
/* 289 */       g.fillRect(width - underlineHeight, 0, underlineHeight, height);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected Color deriveBackground(Color background) {
/* 294 */     if (!(background instanceof com.formdev.flatlaf.util.DerivedColor)) {
/* 295 */       return background;
/*     */     }
/*     */ 
/*     */     
/* 299 */     Color baseColor = this.menuItem.isOpaque() ? this.menuItem.getBackground() : FlatUIUtils.getParentBackground(this.menuItem);
/*     */     
/* 301 */     return FlatUIUtils.deriveColor(background, baseColor);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void paintIcon(Graphics g, Rectangle iconRect, Icon icon, Color checkBackground) {
/* 307 */     if (this.menuItem.isSelected() && this.checkIcon != null && icon != this.checkIcon) {
/* 308 */       Rectangle r = FlatUIUtils.addInsets(iconRect, UIScale.scale(this.checkMargins));
/* 309 */       g.setColor(FlatUIUtils.deriveColor(checkBackground, this.selectionBackground));
/* 310 */       g.fillRect(r.x, r.y, r.width, r.height);
/*     */     } 
/*     */     
/* 313 */     paintIcon(g, this.menuItem, icon, iconRect);
/*     */   }
/*     */   
/*     */   protected void paintText(Graphics g, Rectangle textRect, String text, Color selectionForeground, Color disabledForeground) {
/* 317 */     View htmlView = (View)this.menuItem.getClientProperty("html");
/* 318 */     if (htmlView != null) {
/* 319 */       paintHTMLText(g, this.menuItem, textRect, htmlView, isUnderlineSelection() ? null : selectionForeground);
/*     */       
/*     */       return;
/*     */     } 
/* 323 */     int mnemonicIndex = FlatLaf.isShowMnemonics() ? this.menuItem.getDisplayedMnemonicIndex() : -1;
/* 324 */     Color foreground = (isTopLevelMenu(this.menuItem) ? this.menuItem.getParent() : this.menuItem).getForeground();
/*     */     
/* 326 */     paintText(g, this.menuItem, textRect, text, mnemonicIndex, this.menuItem.getFont(), foreground, 
/* 327 */         isUnderlineSelection() ? foreground : selectionForeground, disabledForeground);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void paintAccelerator(Graphics g, Rectangle accelRect, String accelText, Color foreground, Color selectionForeground, Color disabledForeground) {
/* 333 */     paintText(g, this.menuItem, accelRect, accelText, -1, this.acceleratorFont, foreground, 
/* 334 */         isUnderlineSelection() ? foreground : selectionForeground, disabledForeground);
/*     */   }
/*     */   
/*     */   protected void paintArrowIcon(Graphics g, Rectangle arrowRect, Icon arrowIcon) {
/* 338 */     paintIcon(g, this.menuItem, arrowIcon, arrowRect);
/*     */   }
/*     */   
/*     */   protected static void paintIcon(Graphics g, JMenuItem menuItem, Icon icon, Rectangle iconRect) {
/* 342 */     if (icon == null) {
/*     */       return;
/*     */     }
/*     */     
/* 346 */     int x = iconRect.x + centerOffset(iconRect.width, icon.getIconWidth());
/* 347 */     int y = iconRect.y + centerOffset(iconRect.height, icon.getIconHeight());
/*     */ 
/*     */     
/* 350 */     icon.paintIcon(menuItem, g, x, y);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void paintText(Graphics g, JMenuItem menuItem, Rectangle textRect, String text, int mnemonicIndex, Font font, Color foreground, Color selectionForeground, Color disabledForeground) {
/* 357 */     if (text == null || text.isEmpty()) {
/*     */       return;
/*     */     }
/* 360 */     FontMetrics fm = menuItem.getFontMetrics(font);
/*     */     
/* 362 */     Font oldFont = g.getFont();
/* 363 */     g.setFont(font);
/* 364 */     g.setColor(!menuItem.isEnabled() ? disabledForeground : (
/*     */         
/* 366 */         isArmedOrSelected(menuItem) ? selectionForeground : foreground));
/*     */ 
/*     */ 
/*     */     
/* 370 */     FlatUIUtils.drawStringUnderlineCharAt(menuItem, g, text, mnemonicIndex, textRect.x, textRect.y + fm
/* 371 */         .getAscent());
/*     */     
/* 373 */     g.setFont(oldFont);
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void paintHTMLText(Graphics g, JMenuItem menuItem, Rectangle textRect, View htmlView, Color selectionForeground) {
/*     */     GraphicsProxyWithTextColor graphicsProxyWithTextColor;
/* 379 */     if (isArmedOrSelected(menuItem) && selectionForeground != null) {
/* 380 */       graphicsProxyWithTextColor = new GraphicsProxyWithTextColor((Graphics2D)g, selectionForeground);
/*     */     }
/* 382 */     htmlView.paint(HiDPIUtils.createGraphicsTextYCorrection((Graphics2D)graphicsProxyWithTextColor), textRect);
/*     */   }
/*     */   
/*     */   protected static boolean isArmedOrSelected(JMenuItem menuItem) {
/* 386 */     return (menuItem.isArmed() || (menuItem instanceof JMenu && menuItem.isSelected()));
/*     */   }
/*     */   
/*     */   protected static boolean isTopLevelMenu(JMenuItem menuItem) {
/* 390 */     return (menuItem instanceof JMenu && ((JMenu)menuItem).isTopLevelMenu());
/*     */   }
/*     */   
/*     */   protected boolean isUnderlineSelection() {
/* 394 */     return "underline".equals(UIManager.getString("MenuItem.selectionType"));
/*     */   }
/*     */   
/*     */   private Icon getIconForPainting() {
/* 398 */     Icon icon = this.menuItem.getIcon();
/*     */     
/* 400 */     if (icon == null && this.checkIcon != null && !isTopLevelMenu(this.menuItem)) {
/* 401 */       return this.checkIcon;
/*     */     }
/* 403 */     if (icon == null) {
/* 404 */       return null;
/*     */     }
/* 406 */     if (!this.menuItem.isEnabled()) {
/* 407 */       return this.menuItem.getDisabledIcon();
/*     */     }
/* 409 */     if (this.menuItem.getModel().isPressed() && this.menuItem.isArmed()) {
/* 410 */       Icon pressedIcon = this.menuItem.getPressedIcon();
/* 411 */       if (pressedIcon != null) {
/* 412 */         return pressedIcon;
/*     */       }
/*     */     } 
/* 415 */     return icon;
/*     */   }
/*     */   
/*     */   private Icon getIconForLayout() {
/* 419 */     Icon icon = this.menuItem.getIcon();
/*     */     
/* 421 */     if (isTopLevelMenu(this.menuItem)) {
/* 422 */       return (icon != null) ? new MinSizeIcon(icon) : null;
/*     */     }
/* 424 */     return new MinSizeIcon((icon != null) ? icon : this.checkIcon);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getAcceleratorText() {
/* 432 */     KeyStroke accelerator = this.menuItem.getAccelerator();
/* 433 */     if (accelerator == null) {
/* 434 */       return null;
/*     */     }
/* 436 */     boolean leftToRight = this.menuItem.getComponentOrientation().isLeftToRight();
/*     */     
/* 438 */     if (accelerator == this.cachedAccelerator && leftToRight == this.cachedAcceleratorLeftToRight) {
/* 439 */       return this.cachedAcceleratorText;
/*     */     }
/* 441 */     this.cachedAccelerator = accelerator;
/* 442 */     this.cachedAcceleratorText = getTextForAccelerator(accelerator);
/* 443 */     this.cachedAcceleratorLeftToRight = leftToRight;
/*     */     
/* 445 */     return this.cachedAcceleratorText;
/*     */   }
/*     */   
/*     */   protected String getTextForAccelerator(KeyStroke accelerator) {
/* 449 */     StringBuilder buf = new StringBuilder();
/* 450 */     boolean leftToRight = this.menuItem.getComponentOrientation().isLeftToRight();
/*     */ 
/*     */     
/* 453 */     int modifiers = accelerator.getModifiers();
/* 454 */     if (modifiers != 0) {
/* 455 */       if (SystemInfo.isMacOS) {
/* 456 */         if (leftToRight)
/* 457 */           buf.append(getMacOSModifiersExText(modifiers, leftToRight)); 
/*     */       } else {
/* 459 */         buf.append(InputEvent.getModifiersExText(modifiers)).append(this.acceleratorDelimiter);
/*     */       } 
/*     */     }
/*     */     
/* 463 */     int keyCode = accelerator.getKeyCode();
/* 464 */     if (keyCode != 0) {
/* 465 */       buf.append(KeyEvent.getKeyText(keyCode));
/*     */     } else {
/* 467 */       buf.append(accelerator.getKeyChar());
/*     */     } 
/*     */     
/* 470 */     if (modifiers != 0 && !leftToRight && SystemInfo.isMacOS) {
/* 471 */       buf.append(getMacOSModifiersExText(modifiers, leftToRight));
/*     */     }
/* 473 */     return buf.toString();
/*     */   }
/*     */   
/*     */   protected String getMacOSModifiersExText(int modifiers, boolean leftToRight) {
/* 477 */     StringBuilder buf = new StringBuilder();
/*     */     
/* 479 */     if ((modifiers & 0x80) != 0)
/* 480 */       buf.append('⌃'); 
/* 481 */     if ((modifiers & 0x2200) != 0)
/* 482 */       buf.append('⌥'); 
/* 483 */     if ((modifiers & 0x40) != 0)
/* 484 */       buf.append('⇧'); 
/* 485 */     if ((modifiers & 0x100) != 0) {
/* 486 */       buf.append('⌘');
/*     */     }
/*     */     
/* 489 */     if (!leftToRight) {
/* 490 */       buf.reverse();
/*     */     }
/* 492 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class MinSizeIcon
/*     */     implements Icon
/*     */   {
/*     */     private final Icon delegate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     MinSizeIcon(Icon delegate) {
/* 509 */       this.delegate = delegate;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getIconWidth() {
/* 514 */       int iconWidth = (this.delegate != null) ? this.delegate.getIconWidth() : 0;
/* 515 */       return Math.max(iconWidth, UIScale.scale(FlatMenuItemRenderer.this.minimumIconSize.width));
/*     */     }
/*     */ 
/*     */     
/*     */     public int getIconHeight() {
/* 520 */       int iconHeight = (this.delegate != null) ? this.delegate.getIconHeight() : 0;
/* 521 */       return Math.max(iconHeight, UIScale.scale(FlatMenuItemRenderer.this.minimumIconSize.height));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void paintIcon(Component c, Graphics g, int x, int y) {}
/*     */   }
/*     */ 
/*     */   
/*     */   private static class GraphicsProxyWithTextColor
/*     */     extends Graphics2DProxy
/*     */   {
/*     */     private final Color textColor;
/*     */ 
/*     */     
/*     */     GraphicsProxyWithTextColor(Graphics2D delegate, Color textColor) {
/* 537 */       super(delegate);
/* 538 */       this.textColor = textColor;
/*     */     }
/*     */ 
/*     */     
/*     */     public void drawString(String str, int x, int y) {
/* 543 */       Paint oldPaint = getPaint();
/* 544 */       setPaint(this.textColor);
/* 545 */       super.drawString(str, x, y);
/* 546 */       setPaint(oldPaint);
/*     */     }
/*     */ 
/*     */     
/*     */     public void drawString(String str, float x, float y) {
/* 551 */       Paint oldPaint = getPaint();
/* 552 */       setPaint(this.textColor);
/* 553 */       super.drawString(str, x, y);
/* 554 */       setPaint(oldPaint);
/*     */     }
/*     */ 
/*     */     
/*     */     public void drawString(AttributedCharacterIterator iterator, int x, int y) {
/* 559 */       Paint oldPaint = getPaint();
/* 560 */       setPaint(this.textColor);
/* 561 */       super.drawString(iterator, x, y);
/* 562 */       setPaint(oldPaint);
/*     */     }
/*     */ 
/*     */     
/*     */     public void drawString(AttributedCharacterIterator iterator, float x, float y) {
/* 567 */       Paint oldPaint = getPaint();
/* 568 */       setPaint(this.textColor);
/* 569 */       super.drawString(iterator, x, y);
/* 570 */       setPaint(oldPaint);
/*     */     }
/*     */ 
/*     */     
/*     */     public void drawChars(char[] data, int offset, int length, int x, int y) {
/* 575 */       Paint oldPaint = getPaint();
/* 576 */       setPaint(this.textColor);
/* 577 */       super.drawChars(data, offset, length, x, y);
/* 578 */       setPaint(oldPaint);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatMenuItemRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */