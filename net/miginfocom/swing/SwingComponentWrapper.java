/*     */ package net.miginfocom.swing;
/*     */ 
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.HeadlessException;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Point;
/*     */ import java.awt.ScrollPane;
/*     */ import java.awt.geom.Point2D;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.Border;
/*     */ import net.miginfocom.layout.ComponentWrapper;
/*     */ import net.miginfocom.layout.ContainerWrapper;
/*     */ import net.miginfocom.layout.PlatformDefaults;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SwingComponentWrapper
/*     */   implements ComponentWrapper
/*     */ {
/*     */   private static boolean maxSet = false;
/*     */   private static boolean vp = true;
/*  60 */   private static final Color DB_COMP_OUTLINE = new Color(0, 0, 200);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   private static final String VISUAL_PADDING_PROPERTY = PlatformDefaults.VISUAL_PADDING_PROPERTY;
/*     */   
/*     */   private final Component c;
/*  69 */   private int compType = -1;
/*  70 */   private Boolean bl = null;
/*     */   
/*     */   private boolean prefCalled = false;
/*     */   
/*     */   public SwingComponentWrapper(Component c) {
/*  75 */     this.c = c;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getBaseline(int width, int height) {
/*  81 */     int h = height;
/*  82 */     int[] visPad = getVisualPadding();
/*  83 */     if (h < 0) {
/*  84 */       h = this.c.getHeight();
/*  85 */     } else if (visPad != null) {
/*  86 */       h = height + visPad[0] + visPad[2];
/*     */     } 
/*  88 */     int baseLine = this.c.getBaseline(Math.max(0, (width < 0) ? this.c.getWidth() : width), Math.max(0, h));
/*  89 */     if (baseLine != -1 && visPad != null) {
/*  90 */       baseLine -= visPad[0];
/*     */     }
/*  92 */     return baseLine;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Object getComponent() {
/*  98 */     return this.c;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 103 */   private static final IdentityHashMap<FontMetrics, Point2D.Float> FM_MAP = new IdentityHashMap<>(4);
/* 104 */   private static final Font SUBST_FONT = new Font("sansserif", 0, 11); public final float getPixelUnitFactor(boolean isHor) { Font font; FontMetrics fm; Point2D.Float p;
/*     */     Float s;
/*     */     float scaleFactor;
/*     */     Object lafScaleFactorObj;
/*     */     float screenScale;
/* 109 */     switch (PlatformDefaults.getLogicalPixelBase()) {
/*     */       case 100:
/* 111 */         font = this.c.getFont();
/* 112 */         fm = this.c.getFontMetrics((font != null) ? font : SUBST_FONT);
/* 113 */         p = FM_MAP.get(fm);
/* 114 */         if (p == null) {
/* 115 */           Rectangle2D r = fm.getStringBounds("X", this.c.getGraphics());
/* 116 */           p = new Point2D.Float((float)r.getWidth() / 6.0F, (float)r.getHeight() / 13.277344F);
/* 117 */           FM_MAP.put(fm, p);
/*     */         } 
/* 119 */         return isHor ? p.x : p.y;
/*     */ 
/*     */       
/*     */       case 101:
/* 123 */         s = isHor ? PlatformDefaults.getHorizontalScaleFactor() : PlatformDefaults.getVerticalScaleFactor();
/* 124 */         scaleFactor = (s != null) ? s.floatValue() : 1.0F;
/*     */ 
/*     */ 
/*     */         
/* 128 */         lafScaleFactorObj = UIManager.get("laf.scaleFactor");
/* 129 */         if (lafScaleFactorObj instanceof Number) {
/* 130 */           float lafScaleFactor = ((Number)lafScaleFactorObj).floatValue();
/* 131 */           return scaleFactor * lafScaleFactor;
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 140 */         screenScale = isJava9orLater ? 1.0F : ((isHor ? getHorizontalScreenDPI() : getVerticalScreenDPI()) / PlatformDefaults.getDefaultDPI());
/* 141 */         return scaleFactor * screenScale;
/*     */     } 
/*     */     
/* 144 */     return 1.0F; }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isJava9orLater;
/*     */   
/*     */   static {
/*     */     try {
/* 152 */       StringTokenizer st = new StringTokenizer(System.getProperty("java.version"), "._-+");
/* 153 */       int majorVersion = Integer.parseInt(st.nextToken());
/* 154 */       isJava9orLater = (majorVersion >= 9);
/* 155 */     } catch (Exception exception) {}
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
/*     */   public final int getX() {
/* 184 */     return this.c.getX();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getY() {
/* 190 */     return this.c.getY();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getHeight() {
/* 196 */     return this.c.getHeight();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getWidth() {
/* 202 */     return this.c.getWidth();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getScreenLocationX() {
/* 208 */     Point p = new Point();
/* 209 */     SwingUtilities.convertPointToScreen(p, this.c);
/* 210 */     return p.x;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getScreenLocationY() {
/* 216 */     Point p = new Point();
/* 217 */     SwingUtilities.convertPointToScreen(p, this.c);
/* 218 */     return p.y;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getMinimumHeight(int sz) {
/* 224 */     if (!this.prefCalled) {
/* 225 */       this.c.getPreferredSize();
/* 226 */       this.prefCalled = true;
/*     */     } 
/* 228 */     return (this.c.getMinimumSize()).height;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getMinimumWidth(int sz) {
/* 234 */     if (!this.prefCalled) {
/* 235 */       this.c.getPreferredSize();
/* 236 */       this.prefCalled = true;
/*     */     } 
/* 238 */     return (this.c.getMinimumSize()).width;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getPreferredHeight(int sz) {
/* 244 */     if (this.c.getWidth() == 0 && this.c.getHeight() == 0 && sz != -1) {
/* 245 */       this.c.setBounds(this.c.getX(), this.c.getY(), sz, 1);
/*     */     }
/* 247 */     return (this.c.getPreferredSize()).height;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getPreferredWidth(int sz) {
/* 254 */     if (this.c.getWidth() == 0 && this.c.getHeight() == 0 && sz != -1) {
/* 255 */       this.c.setBounds(this.c.getX(), this.c.getY(), 1, sz);
/*     */     }
/* 257 */     return (this.c.getPreferredSize()).width;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getMaximumHeight(int sz) {
/* 263 */     if (!isMaxSet(this.c)) {
/* 264 */       return Integer.MAX_VALUE;
/*     */     }
/* 266 */     return (this.c.getMaximumSize()).height;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getMaximumWidth(int sz) {
/* 272 */     if (!isMaxSet(this.c)) {
/* 273 */       return Integer.MAX_VALUE;
/*     */     }
/* 275 */     return (this.c.getMaximumSize()).width;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isMaxSet(Component c) {
/* 281 */     return c.isMaximumSizeSet();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final ContainerWrapper getParent() {
/* 287 */     Container p = this.c.getParent();
/* 288 */     return (p != null) ? new SwingContainerWrapper(p) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getHorizontalScreenDPI() {
/*     */     try {
/* 294 */       return this.c.getToolkit().getScreenResolution();
/* 295 */     } catch (HeadlessException ex) {
/* 296 */       return PlatformDefaults.getDefaultDPI();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getVerticalScreenDPI() {
/*     */     try {
/* 304 */       return this.c.getToolkit().getScreenResolution();
/* 305 */     } catch (HeadlessException ex) {
/* 306 */       return PlatformDefaults.getDefaultDPI();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getScreenWidth() {
/*     */     try {
/* 314 */       return (this.c.getToolkit().getScreenSize()).width;
/* 315 */     } catch (HeadlessException ex) {
/* 316 */       return 1024;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getScreenHeight() {
/*     */     try {
/* 324 */       return (this.c.getToolkit().getScreenSize()).height;
/* 325 */     } catch (HeadlessException ex) {
/* 326 */       return 768;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean hasBaseline() {
/* 333 */     if (this.bl == null) {
/*     */       try {
/* 335 */         if (this.c instanceof javax.swing.JLabel && ((JComponent)this.c).getClientProperty("html") != null) {
/* 336 */           this.bl = Boolean.FALSE;
/*     */         } else {
/* 338 */           this.bl = Boolean.valueOf((getBaseline(8192, 8192) > -1));
/*     */ 
/*     */ 
/*     */         
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 348 */       catch (Throwable ex) {
/* 349 */         this.bl = Boolean.FALSE;
/*     */       } 
/*     */     }
/* 352 */     return this.bl.booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getLinkId() {
/* 358 */     return this.c.getName();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setBounds(int x, int y, int width, int height) {
/* 364 */     this.c.setBounds(x, y, width, height);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isVisible() {
/* 370 */     return this.c.isVisible();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final int[] getVisualPadding() {
/* 376 */     int[] padding = null;
/* 377 */     if (isVisualPaddingEnabled())
/*     */     {
/* 379 */       if (this.c instanceof JComponent) {
/* 380 */         JComponent component = (JComponent)this.c;
/* 381 */         Object padValue = component.getClientProperty(VISUAL_PADDING_PROPERTY);
/*     */         
/* 383 */         if (padValue instanceof int[]) {
/*     */           
/* 385 */           padding = (int[])padValue;
/* 386 */         } else if (padValue instanceof Insets) {
/*     */           
/* 388 */           Insets padInsets = (Insets)padValue;
/* 389 */           padding = new int[] { padInsets.top, padInsets.left, padInsets.bottom, padInsets.right };
/*     */         } 
/*     */         
/* 392 */         if (padding == null) {
/*     */           String classID;
/*     */           
/*     */           Border border;
/* 396 */           switch (getComponentType(false)) {
/*     */             case 5:
/* 398 */               border = component.getBorder();
/* 399 */               if (border != null && border.getClass().getName().startsWith("com.apple.laf.AquaButtonBorder")) {
/* 400 */                 if (PlatformDefaults.getPlatform() == 1) {
/* 401 */                   String str1; Object buttonType = component.getClientProperty("JButton.buttonType");
/* 402 */                   if (buttonType == null) {
/* 403 */                     str1 = (component.getHeight() < 33) ? "Button" : "Button.bevel";
/*     */                   } else {
/* 405 */                     str1 = "Button." + buttonType;
/*     */                   } 
/* 407 */                   if (((AbstractButton)component).getIcon() != null)
/* 408 */                     str1 = str1 + ".icon";  break;
/*     */                 } 
/* 410 */                 String str = "Button";
/*     */                 break;
/*     */               } 
/* 413 */               classID = "";
/*     */               break;
/*     */ 
/*     */             
/*     */             case 16:
/* 418 */               border = component.getBorder();
/* 419 */               if (border != null && border.getClass().getName().startsWith("com.apple.laf.AquaButtonBorder")) {
/* 420 */                 Object size = component.getClientProperty("JComponent.sizeVariant");
/* 421 */                 if (size != null && !size.toString().equals("regular")) {
/* 422 */                   size = "." + size;
/*     */                 } else {
/* 424 */                   size = "";
/*     */                 } 
/*     */                 
/* 427 */                 if (component instanceof javax.swing.JRadioButton) {
/* 428 */                   classID = "RadioButton" + size; break;
/* 429 */                 }  if (component instanceof javax.swing.JCheckBox) {
/* 430 */                   classID = "CheckBox" + size; break;
/*     */                 } 
/* 432 */                 classID = "ToggleButton" + size;
/*     */                 break;
/*     */               } 
/* 435 */               classID = "";
/*     */               break;
/*     */ 
/*     */             
/*     */             case 11:
/* 440 */               if (PlatformDefaults.getPlatform() == 1) {
/* 441 */                 if (((JComboBox)component).isEditable()) {
/* 442 */                   Object object = component.getClientProperty("JComboBox.isSquare");
/* 443 */                   if (object != null && object.toString().equals("true")) {
/* 444 */                     classID = "ComboBox.editable.isSquare"; break;
/*     */                   } 
/* 446 */                   classID = "ComboBox.editable";
/*     */                   
/*     */                   break;
/*     */                 } 
/* 450 */                 Object isSquare = component.getClientProperty("JComboBox.isSquare");
/* 451 */                 Object isPopDown = component.getClientProperty("JComboBox.isPopDown");
/*     */                 
/* 453 */                 if (isSquare != null && isSquare.toString().equals("true")) {
/* 454 */                   classID = "ComboBox.isSquare"; break;
/* 455 */                 }  if (isPopDown != null && isPopDown.toString().equals("true")) {
/* 456 */                   classID = "ComboBox.isPopDown"; break;
/*     */                 } 
/* 458 */                 classID = "ComboBox";
/*     */                 
/*     */                 break;
/*     */               } 
/* 462 */               classID = "ComboBox";
/*     */               break;
/*     */             
/*     */             case 1:
/* 466 */               classID = "Container";
/*     */               break;
/*     */             case 9:
/* 469 */               classID = "Image";
/*     */               break;
/*     */             case 2:
/* 472 */               classID = "Label";
/*     */               break;
/*     */             case 6:
/* 475 */               classID = "List";
/*     */               break;
/*     */             case 10:
/* 478 */               classID = "Panel";
/*     */               break;
/*     */             case 14:
/* 481 */               classID = "ProgressBar";
/*     */               break;
/*     */             case 17:
/* 484 */               classID = "ScrollBar";
/*     */               break;
/*     */             case 8:
/* 487 */               classID = "ScrollPane";
/*     */               break;
/*     */             case 18:
/* 490 */               classID = "Separator";
/*     */               break;
/*     */             case 12:
/* 493 */               classID = "Slider";
/*     */               break;
/*     */             case 13:
/* 496 */               classID = "Spinner";
/*     */               break;
/*     */             case 7:
/* 499 */               classID = "Table";
/*     */               break;
/*     */             case 19:
/* 502 */               classID = "TabbedPane";
/*     */               break;
/*     */             case 4:
/* 505 */               classID = "TextArea";
/*     */               break;
/*     */             case 3:
/* 508 */               border = component.getBorder();
/* 509 */               if (!component.isOpaque() && border != null && border.getClass().getSimpleName().equals("AquaTextFieldBorder")) {
/* 510 */                 classID = "TextField"; break;
/*     */               } 
/* 512 */               classID = "";
/*     */               break;
/*     */             
/*     */             case 15:
/* 516 */               classID = "Tree";
/*     */               break;
/*     */             case 0:
/* 519 */               classID = "Other";
/*     */               break;
/*     */             
/*     */             default:
/* 523 */               classID = "";
/*     */               break;
/*     */           } 
/*     */           
/* 527 */           padValue = PlatformDefaults.getDefaultVisualPadding(classID + "." + VISUAL_PADDING_PROPERTY);
/* 528 */           if (padValue instanceof int[]) {
/*     */             
/* 530 */             padding = (int[])padValue;
/* 531 */           } else if (padValue instanceof Insets) {
/*     */             
/* 533 */             Insets padInsets = (Insets)padValue;
/* 534 */             padding = new int[] { padInsets.top, padInsets.left, padInsets.bottom, padInsets.right };
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/* 539 */     return padding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isMaxSizeSetOn1_4() {
/* 547 */     return maxSet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setMaxSizeSetOn1_4(boolean b) {
/* 555 */     maxSet = b;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isVisualPaddingEnabled() {
/* 560 */     return vp;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void setVisualPaddingEnabled(boolean b) {
/* 565 */     vp = b;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void paintDebugOutline(boolean showVisualPadding) {
/* 571 */     if (!this.c.isShowing()) {
/*     */       return;
/*     */     }
/* 574 */     Graphics2D g = (Graphics2D)this.c.getGraphics();
/* 575 */     if (g == null) {
/*     */       return;
/*     */     }
/* 578 */     g.setPaint(DB_COMP_OUTLINE);
/* 579 */     g.setStroke(new BasicStroke(1.0F, 2, 0, 10.0F, new float[] { 2.0F, 4.0F }, 0.0F));
/* 580 */     g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
/*     */     
/* 582 */     if (showVisualPadding && isVisualPaddingEnabled()) {
/* 583 */       int[] padding = getVisualPadding();
/* 584 */       if (padding != null) {
/* 585 */         g.setColor(Color.GREEN);
/* 586 */         g.drawRect(padding[1], padding[0], getWidth() - 1 - padding[1] + padding[3], getHeight() - 1 - padding[0] + padding[2]);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getComponentType(boolean disregardScrollPane) {
/* 594 */     if (this.compType == -1) {
/* 595 */       this.compType = checkType(disregardScrollPane);
/*     */     }
/* 597 */     return this.compType;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLayoutHashCode() {
/* 603 */     Dimension d = this.c.getMaximumSize();
/* 604 */     int hash = d.width + (d.height << 5);
/*     */     
/* 606 */     d = this.c.getPreferredSize();
/* 607 */     hash += (d.width << 10) + (d.height << 15);
/*     */     
/* 609 */     d = this.c.getMinimumSize();
/* 610 */     hash += (d.width << 20) + (d.height << 25);
/*     */     
/* 612 */     if (this.c.isVisible()) {
/* 613 */       hash += 1324511;
/*     */     }
/* 615 */     String id = getLinkId();
/* 616 */     if (id != null) {
/* 617 */       hash += id.hashCode();
/*     */     }
/* 619 */     return hash;
/*     */   }
/*     */ 
/*     */   
/*     */   private int checkType(boolean disregardScrollPane) {
/* 624 */     Component c = this.c;
/*     */     
/* 626 */     if (disregardScrollPane) {
/* 627 */       if (c instanceof JScrollPane) {
/* 628 */         c = ((JScrollPane)c).getViewport().getView();
/* 629 */       } else if (c instanceof ScrollPane) {
/* 630 */         c = ((ScrollPane)c).getComponent(0);
/*     */       } 
/*     */     }
/*     */     
/* 634 */     if (c instanceof javax.swing.JTextField || c instanceof java.awt.TextField)
/* 635 */       return 3; 
/* 636 */     if (c instanceof javax.swing.JLabel || c instanceof java.awt.Label)
/* 637 */       return 2; 
/* 638 */     if (c instanceof javax.swing.JCheckBox || c instanceof javax.swing.JRadioButton || c instanceof java.awt.Checkbox)
/* 639 */       return 16; 
/* 640 */     if (c instanceof AbstractButton || c instanceof java.awt.Button)
/* 641 */       return 5; 
/* 642 */     if (c instanceof JComboBox || c instanceof java.awt.Choice)
/* 643 */       return 11; 
/* 644 */     if (c instanceof javax.swing.text.JTextComponent || c instanceof java.awt.TextComponent)
/* 645 */       return 4; 
/* 646 */     if (c instanceof javax.swing.JPanel || c instanceof java.awt.Canvas)
/* 647 */       return 10; 
/* 648 */     if (c instanceof javax.swing.JList || c instanceof java.awt.List)
/* 649 */       return 6; 
/* 650 */     if (c instanceof javax.swing.JTable)
/* 651 */       return 7; 
/* 652 */     if (c instanceof javax.swing.JSeparator)
/* 653 */       return 18; 
/* 654 */     if (c instanceof javax.swing.JSpinner)
/* 655 */       return 13; 
/* 656 */     if (c instanceof javax.swing.JTabbedPane)
/* 657 */       return 19; 
/* 658 */     if (c instanceof javax.swing.JProgressBar)
/* 659 */       return 14; 
/* 660 */     if (c instanceof javax.swing.JSlider)
/* 661 */       return 12; 
/* 662 */     if (c instanceof JScrollPane)
/* 663 */       return 8; 
/* 664 */     if (c instanceof javax.swing.JScrollBar || c instanceof java.awt.Scrollbar)
/* 665 */       return 17; 
/* 666 */     if (c instanceof Container) {
/* 667 */       return 1;
/*     */     }
/* 669 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final int hashCode() {
/* 675 */     return getComponent().hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean equals(Object o) {
/* 681 */     if (!(o instanceof ComponentWrapper)) {
/* 682 */       return false;
/*     */     }
/* 684 */     return this.c.equals(((ComponentWrapper)o).getComponent());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getContentBias() {
/* 690 */     return (this.c instanceof javax.swing.JTextArea || this.c instanceof javax.swing.JEditorPane || (this.c instanceof JComponent && Boolean.TRUE.equals(((JComponent)this.c).getClientProperty("migLayout.dynamicAspectRatio")))) ? 0 : -1;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\net\miginfocom\swing\SwingComponentWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */