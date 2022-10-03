/*     */ package net.miginfocom.layout;
/*     */ 
/*     */ import java.awt.Toolkit;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class PlatformDefaults
/*     */ {
/*  49 */   public static String VISUAL_PADDING_PROPERTY = "visualPadding";
/*     */   
/*  51 */   private static int DEF_H_UNIT = 1;
/*  52 */   private static int DEF_V_UNIT = 2;
/*     */   
/*  54 */   private static InCellGapProvider GAP_PROVIDER = null;
/*     */   
/*  56 */   private static volatile int MOD_COUNT = 0;
/*     */ 
/*     */ 
/*     */   
/*  60 */   private static final UnitValue LPX6 = new UnitValue(6.0F, 1, null);
/*  61 */   private static final UnitValue LPX7 = new UnitValue(7.0F, 1, null);
/*     */ 
/*     */ 
/*     */   
/*  65 */   private static final UnitValue LPX11 = new UnitValue(11.0F, 1, null);
/*  66 */   private static final UnitValue LPX12 = new UnitValue(12.0F, 1, null);
/*     */   
/*  68 */   private static final UnitValue LPX16 = new UnitValue(16.0F, 1, null);
/*  69 */   private static final UnitValue LPX18 = new UnitValue(18.0F, 1, null);
/*  70 */   private static final UnitValue LPX20 = new UnitValue(20.0F, 1, null);
/*     */ 
/*     */ 
/*     */   
/*  74 */   private static final UnitValue LPY6 = new UnitValue(6.0F, 2, null);
/*  75 */   private static final UnitValue LPY7 = new UnitValue(7.0F, 2, null);
/*     */ 
/*     */ 
/*     */   
/*  79 */   private static final UnitValue LPY11 = new UnitValue(11.0F, 2, null);
/*  80 */   private static final UnitValue LPY12 = new UnitValue(12.0F, 2, null);
/*     */   
/*  82 */   private static final UnitValue LPY16 = new UnitValue(16.0F, 2, null);
/*  83 */   private static final UnitValue LPY18 = new UnitValue(18.0F, 2, null);
/*  84 */   private static final UnitValue LPY20 = new UnitValue(20.0F, 2, null);
/*     */   
/*     */   public static final int WINDOWS_XP = 0;
/*     */   
/*     */   public static final int MAC_OSX = 1;
/*     */   
/*     */   public static final int GNOME = 2;
/*  91 */   private static int CUR_PLAF = 0;
/*     */ 
/*     */   
/*  94 */   private static final UnitValue[] PANEL_INS = new UnitValue[4];
/*  95 */   private static final UnitValue[] DIALOG_INS = new UnitValue[4];
/*     */   
/*  97 */   private static String BUTTON_FORMAT = null;
/*     */   
/*  99 */   private static final HashMap<String, UnitValue> HOR_DEFS = new HashMap<>(32);
/* 100 */   private static final HashMap<String, UnitValue> VER_DEFS = new HashMap<>(32);
/* 101 */   private static BoundSize DEF_VGAP = null; private static BoundSize DEF_HGAP = null;
/* 102 */   static BoundSize RELATED_X = null; static BoundSize RELATED_Y = null; static BoundSize UNRELATED_X = null; static BoundSize UNRELATED_Y = null;
/* 103 */   private static UnitValue BUTT_WIDTH = null;
/* 104 */   private static UnitValue BUTT_PADDING = null;
/*     */   
/* 106 */   private static Float horScale = null; private static Float verScale = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int BASE_FONT_SIZE = 100;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int BASE_SCALE_FACTOR = 101;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int BASE_REAL_PIXEL = 102;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 137 */   private static int LP_BASE = 101;
/*     */   
/* 139 */   private static Integer BASE_DPI_FORCED = null;
/* 140 */   private static int BASE_DPI = 96;
/*     */   
/*     */   private static boolean dra = true;
/*     */   
/* 144 */   private static final HashMap<String, int[]> VISUAL_BOUNDS = (HashMap)new HashMap<>(64);
/*     */   
/*     */   static {
/* 147 */     setPlatform(getCurrentPlatform());
/* 148 */     MOD_COUNT = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getCurrentPlatform() {
/* 156 */     String os = System.getProperty("os.name");
/* 157 */     if (os.startsWith("Mac OS"))
/* 158 */       return 1; 
/* 159 */     if (os.startsWith("Linux")) {
/* 160 */       return 2;
/*     */     }
/* 162 */     return 0;
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
/*     */   public static void setPlatform(int plaf) {
/* 177 */     switch (plaf) {
/*     */       case 0:
/* 179 */         setDefaultVisualPadding("TabbedPane." + VISUAL_PADDING_PROPERTY, new int[] { 1, 0, 1, 2 });
/* 180 */         setRelatedGap(LPX7, LPY7);
/* 181 */         setUnrelatedGap(LPX11, LPY11);
/* 182 */         setParagraphGap(LPX20, LPY20);
/* 183 */         setIndentGap(LPX11, LPY11);
/* 184 */         setGridCellGap(LPX7, LPY7);
/*     */         
/* 186 */         setMinimumButtonWidth(new UnitValue(75.0F, 1, null));
/* 187 */         setButtonOrder("L_E+U+YNBXOCAH_I_R");
/* 188 */         setDialogInsets(LPY11, LPX11, LPY11, LPX11);
/* 189 */         setPanelInsets(LPY7, LPX7, LPY7, LPX7);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 1:
/* 194 */         setDefaultVisualPadding("Button." + VISUAL_PADDING_PROPERTY, new int[] { 3, 6, 5, 6 });
/* 195 */         setDefaultVisualPadding("Button.icon." + VISUAL_PADDING_PROPERTY, new int[] { 3, 2, 3, 2 });
/* 196 */         setDefaultVisualPadding("Button.square." + VISUAL_PADDING_PROPERTY, new int[] { 4, 4, 4, 4 });
/* 197 */         setDefaultVisualPadding("Button.square.icon." + VISUAL_PADDING_PROPERTY, new int[] { 4, 4, 4, 4 });
/* 198 */         setDefaultVisualPadding("Button.gradient." + VISUAL_PADDING_PROPERTY, new int[] { 5, 4, 5, 4 });
/* 199 */         setDefaultVisualPadding("Button.gradient.icon." + VISUAL_PADDING_PROPERTY, new int[] { 5, 4, 5, 4 });
/* 200 */         setDefaultVisualPadding("Button.bevel." + VISUAL_PADDING_PROPERTY, new int[] { 2, 2, 3, 2 });
/* 201 */         setDefaultVisualPadding("Button.bevel.icon." + VISUAL_PADDING_PROPERTY, new int[] { 2, 2, 3, 2 });
/* 202 */         setDefaultVisualPadding("Button.textured." + VISUAL_PADDING_PROPERTY, new int[] { 3, 2, 3, 2 });
/* 203 */         setDefaultVisualPadding("Button.textured.icon." + VISUAL_PADDING_PROPERTY, new int[] { 3, 2, 3, 2 });
/* 204 */         setDefaultVisualPadding("Button.roundRect." + VISUAL_PADDING_PROPERTY, new int[] { 5, 4, 5, 4 });
/* 205 */         setDefaultVisualPadding("Button.roundRect.icon." + VISUAL_PADDING_PROPERTY, new int[] { 5, 4, 5, 4 });
/* 206 */         setDefaultVisualPadding("Button.recessed." + VISUAL_PADDING_PROPERTY, new int[] { 5, 4, 5, 4 });
/* 207 */         setDefaultVisualPadding("Button.recessed.icon." + VISUAL_PADDING_PROPERTY, new int[] { 5, 4, 5, 4 });
/* 208 */         setDefaultVisualPadding("Button.help." + VISUAL_PADDING_PROPERTY, new int[] { 4, 3, 3, 4 });
/* 209 */         setDefaultVisualPadding("Button.help.icon." + VISUAL_PADDING_PROPERTY, new int[] { 4, 3, 3, 4 });
/*     */         
/* 211 */         setDefaultVisualPadding("ComboBox." + VISUAL_PADDING_PROPERTY, new int[] { 2, 4, 4, 5 });
/* 212 */         setDefaultVisualPadding("ComboBox.isPopDown." + VISUAL_PADDING_PROPERTY, new int[] { 2, 5, 4, 5 });
/* 213 */         setDefaultVisualPadding("ComboBox.isSquare." + VISUAL_PADDING_PROPERTY, new int[] { 1, 6, 5, 7 });
/*     */         
/* 215 */         setDefaultVisualPadding("ComboBox.editable." + VISUAL_PADDING_PROPERTY, new int[] { 3, 3, 3, 2 });
/* 216 */         setDefaultVisualPadding("ComboBox.editable.isSquare." + VISUAL_PADDING_PROPERTY, new int[] { 3, 3, 3, 1 });
/*     */         
/* 218 */         setDefaultVisualPadding("TextField." + VISUAL_PADDING_PROPERTY, new int[] { 3, 3, 3, 3 });
/* 219 */         setDefaultVisualPadding("TabbedPane." + VISUAL_PADDING_PROPERTY, new int[] { 4, 8, 11, 8 });
/*     */         
/* 221 */         setDefaultVisualPadding("Spinner." + VISUAL_PADDING_PROPERTY, new int[] { 3, 3, 3, 1 });
/*     */         
/* 223 */         setDefaultVisualPadding("RadioButton." + VISUAL_PADDING_PROPERTY, new int[] { 4, 6, 3, 5 });
/* 224 */         setDefaultVisualPadding("RadioButton.small." + VISUAL_PADDING_PROPERTY, new int[] { 4, 6, 3, 5 });
/* 225 */         setDefaultVisualPadding("RadioButton.mini." + VISUAL_PADDING_PROPERTY, new int[] { 5, 7, 4, 5 });
/* 226 */         setDefaultVisualPadding("CheckBox." + VISUAL_PADDING_PROPERTY, new int[] { 5, 7, 4, 5 });
/* 227 */         setDefaultVisualPadding("CheckBox.small." + VISUAL_PADDING_PROPERTY, new int[] { 5, 7, 4, 5 });
/* 228 */         setDefaultVisualPadding("CheckBox.mini." + VISUAL_PADDING_PROPERTY, new int[] { 6, 7, 3, 5 });
/*     */         
/* 230 */         setRelatedGap(LPX7, LPY7);
/* 231 */         setUnrelatedGap(LPX11, LPY11);
/* 232 */         setParagraphGap(LPX20, LPY20);
/* 233 */         setIndentGap(LPX11, LPY11);
/* 234 */         setGridCellGap(LPX7, LPY7);
/*     */         
/* 236 */         setMinimumButtonWidth(new UnitValue(70.0F, 1, null));
/* 237 */         setMinimumButtonPadding(new UnitValue(8.0F, 1, null));
/* 238 */         setButtonOrder("L_HE+U+NYBXCOA_I_R");
/* 239 */         setDialogInsets(LPY20, LPX20, LPY20, LPX20);
/* 240 */         setPanelInsets(LPY16, LPX16, LPY16, LPX16);
/*     */         break;
/*     */       
/*     */       case 2:
/* 244 */         setRelatedGap(LPX6, LPY6);
/* 245 */         setUnrelatedGap(LPX12, LPY12);
/* 246 */         setParagraphGap(LPX18, LPY18);
/* 247 */         setIndentGap(LPX12, LPY12);
/* 248 */         setGridCellGap(LPX6, LPY6);
/*     */ 
/*     */         
/* 251 */         setMinimumButtonWidth(new UnitValue(85.0F, 1, null));
/* 252 */         setButtonOrder("L_HE+UNYACBXO_I_R");
/* 253 */         setDialogInsets(LPY12, LPX12, LPY12, LPX12);
/* 254 */         setPanelInsets(LPY6, LPX6, LPY6, LPX6);
/*     */         break;
/*     */       default:
/* 257 */         throw new IllegalArgumentException("Unknown platform: " + plaf);
/*     */     } 
/* 259 */     CUR_PLAF = plaf;
/* 260 */     BASE_DPI = (BASE_DPI_FORCED != null) ? BASE_DPI_FORCED.intValue() : getPlatformDPI(plaf);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setDefaultVisualPadding(String key, int[] insets) {
/* 270 */     VISUAL_BOUNDS.put(key, insets);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int[] getDefaultVisualPadding(String key) {
/* 280 */     return VISUAL_BOUNDS.get(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getPlatformDPI(int plaf) {
/* 285 */     switch (plaf) {
/*     */       case 0:
/*     */       case 2:
/* 288 */         return 96;
/*     */       case 1:
/*     */         try {
/* 291 */           return Toolkit.getDefaultToolkit().getScreenResolution();
/* 292 */         } catch (Throwable t) {
/* 293 */           return 72;
/*     */         } 
/*     */     } 
/* 296 */     throw new IllegalArgumentException("Unknown platform: " + plaf);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getPlatform() {
/* 305 */     return CUR_PLAF;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getDefaultDPI() {
/* 310 */     return BASE_DPI;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setDefaultDPI(Integer dpi) {
/* 321 */     BASE_DPI = (dpi != null) ? dpi.intValue() : getPlatformDPI(CUR_PLAF);
/* 322 */     BASE_DPI_FORCED = dpi;
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
/*     */   public static Float getHorizontalScaleFactor() {
/* 334 */     return horScale;
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
/*     */   public static void setHorizontalScaleFactor(Float f) {
/* 346 */     if (!LayoutUtil.equals(horScale, f)) {
/* 347 */       horScale = f;
/* 348 */       MOD_COUNT++;
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
/*     */   public static Float getVerticalScaleFactor() {
/* 361 */     return verScale;
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
/*     */   public static void setVerticalScaleFactor(Float f) {
/* 373 */     if (!LayoutUtil.equals(verScale, f)) {
/* 374 */       verScale = f;
/* 375 */       MOD_COUNT++;
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
/*     */   public static int getLogicalPixelBase() {
/* 387 */     return LP_BASE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setLogicalPixelBase(int base) {
/* 398 */     if (LP_BASE != base) {
/* 399 */       if (base < 100 || base > 102) {
/* 400 */         throw new IllegalArgumentException("Unrecognized base: " + base);
/*     */       }
/* 402 */       LP_BASE = base;
/* 403 */       MOD_COUNT++;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setRelatedGap(UnitValue x, UnitValue y) {
/* 413 */     setUnitValue(new String[] { "r", "rel", "related" }, x, y);
/*     */     
/* 415 */     RELATED_X = new BoundSize(x, x, null, "rel:rel");
/* 416 */     RELATED_Y = new BoundSize(y, y, null, "rel:rel");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setUnrelatedGap(UnitValue x, UnitValue y) {
/* 425 */     setUnitValue(new String[] { "u", "unrel", "unrelated" }, x, y);
/*     */     
/* 427 */     UNRELATED_X = new BoundSize(x, x, null, "unrel:unrel");
/* 428 */     UNRELATED_Y = new BoundSize(y, y, null, "unrel:unrel");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setParagraphGap(UnitValue x, UnitValue y) {
/* 437 */     setUnitValue(new String[] { "p", "para", "paragraph" }, x, y);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setIndentGap(UnitValue x, UnitValue y) {
/* 446 */     setUnitValue(new String[] { "i", "ind", "indent" }, x, y);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setGridCellGap(UnitValue x, UnitValue y) {
/* 456 */     if (x != null) {
/* 457 */       DEF_HGAP = new BoundSize(x, x, null, null);
/*     */     }
/* 459 */     if (y != null) {
/* 460 */       DEF_VGAP = new BoundSize(y, y, null, null);
/*     */     }
/* 462 */     MOD_COUNT++;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setMinimumButtonWidth(UnitValue width) {
/* 470 */     BUTT_WIDTH = width;
/* 471 */     MOD_COUNT++;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UnitValue getMinimumButtonWidth() {
/* 479 */     return BUTT_WIDTH;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void setMinimumButtonPadding(UnitValue padding) {
/* 484 */     BUTT_PADDING = padding;
/* 485 */     MOD_COUNT++;
/*     */   }
/*     */ 
/*     */   
/*     */   public static UnitValue getMinimumButtonPadding() {
/* 490 */     return BUTT_PADDING;
/*     */   }
/*     */ 
/*     */   
/*     */   public static float getMinimumButtonWidthIncludingPadding(float refValue, ContainerWrapper parent, ComponentWrapper comp) {
/* 495 */     int buttonMinWidth = getMinimumButtonWidth().getPixels(refValue, parent, comp);
/* 496 */     if (comp != null && getMinimumButtonPadding() != null) {
/* 497 */       return Math.max(comp.getMinimumWidth(comp.getWidth()) + getMinimumButtonPadding().getPixels(refValue, parent, comp) * 2, buttonMinWidth);
/*     */     }
/* 499 */     return buttonMinWidth;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UnitValue getUnitValueX(String unit) {
/* 509 */     return HOR_DEFS.get(unit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UnitValue getUnitValueY(String unit) {
/* 518 */     return VER_DEFS.get(unit);
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
/*     */   public static void setUnitValue(String[] unitStrings, UnitValue x, UnitValue y) {
/* 531 */     for (String unitString : unitStrings) {
/* 532 */       String s = unitString.toLowerCase().trim();
/* 533 */       if (x != null)
/* 534 */         HOR_DEFS.put(s, x); 
/* 535 */       if (y != null)
/* 536 */         VER_DEFS.put(s, y); 
/*     */     } 
/* 538 */     MOD_COUNT++;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int convertToPixels(float value, String unit, boolean isHor, float ref, ContainerWrapper parent, ComponentWrapper comp) {
/* 545 */     UnitValue uv = (isHor ? HOR_DEFS : VER_DEFS).get(unit);
/* 546 */     return (uv != null) ? Math.round(value * uv.getPixels(ref, parent, comp)) : -87654312;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getButtonOrder() {
/* 555 */     return BUTTON_FORMAT;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setButtonOrder(String order) {
/* 598 */     BUTTON_FORMAT = order;
/* 599 */     MOD_COUNT++;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String getTagForChar(char c) {
/* 608 */     switch (c) {
/*     */       case 'o':
/* 610 */         return "ok";
/*     */       case 'c':
/* 612 */         return "cancel";
/*     */       case 'h':
/* 614 */         return "help";
/*     */       case 'e':
/* 616 */         return "help2";
/*     */       case 'y':
/* 618 */         return "yes";
/*     */       case 'n':
/* 620 */         return "no";
/*     */       case 'a':
/* 622 */         return "apply";
/*     */       case 'x':
/* 624 */         return "next";
/*     */       case 'b':
/* 626 */         return "back";
/*     */       case 'i':
/* 628 */         return "finish";
/*     */       case 'l':
/* 630 */         return "left";
/*     */       case 'r':
/* 632 */         return "right";
/*     */       case 'u':
/* 634 */         return "other";
/*     */     } 
/* 636 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BoundSize getGridGapX() {
/* 645 */     return DEF_HGAP;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BoundSize getGridGapY() {
/* 653 */     return DEF_VGAP;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UnitValue getDialogInsets(int side) {
/* 662 */     return DIALOG_INS[side];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setDialogInsets(UnitValue top, UnitValue left, UnitValue bottom, UnitValue right) {
/* 673 */     if (top != null) {
/* 674 */       DIALOG_INS[0] = top;
/*     */     }
/* 676 */     if (left != null) {
/* 677 */       DIALOG_INS[1] = left;
/*     */     }
/* 679 */     if (bottom != null) {
/* 680 */       DIALOG_INS[2] = bottom;
/*     */     }
/* 682 */     if (right != null) {
/* 683 */       DIALOG_INS[3] = right;
/*     */     }
/* 685 */     MOD_COUNT++;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UnitValue getPanelInsets(int side) {
/* 694 */     return PANEL_INS[side];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setPanelInsets(UnitValue top, UnitValue left, UnitValue bottom, UnitValue right) {
/* 705 */     if (top != null) {
/* 706 */       PANEL_INS[0] = top;
/*     */     }
/* 708 */     if (left != null) {
/* 709 */       PANEL_INS[1] = left;
/*     */     }
/* 711 */     if (bottom != null) {
/* 712 */       PANEL_INS[2] = bottom;
/*     */     }
/* 714 */     if (right != null) {
/* 715 */       PANEL_INS[3] = right;
/*     */     }
/* 717 */     MOD_COUNT++;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float getLabelAlignPercentage() {
/* 725 */     return (CUR_PLAF == 1) ? 1.0F : 0.0F;
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
/*     */   static BoundSize getDefaultComponentGap(ComponentWrapper comp, ComponentWrapper adjacentComp, int adjacentSide, String tag, boolean isLTR) {
/* 739 */     if (GAP_PROVIDER != null) {
/* 740 */       return GAP_PROVIDER.getDefaultGap(comp, adjacentComp, adjacentSide, tag, isLTR);
/*     */     }
/* 742 */     if (adjacentComp == null) {
/* 743 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 749 */     return (adjacentSide == 2 || adjacentSide == 4) ? RELATED_X : RELATED_Y;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static InCellGapProvider getGapProvider() {
/* 757 */     return GAP_PROVIDER;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setGapProvider(InCellGapProvider provider) {
/* 765 */     GAP_PROVIDER = provider;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getModCount() {
/* 774 */     return MOD_COUNT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void invalidate() {
/* 781 */     MOD_COUNT++;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getDefaultHorizontalUnit() {
/* 791 */     return DEF_H_UNIT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setDefaultHorizontalUnit(int unit) {
/* 801 */     if (unit < 0 || unit > 27) {
/* 802 */       throw new IllegalArgumentException("Illegal Unit: " + unit);
/*     */     }
/* 804 */     if (DEF_H_UNIT != unit) {
/* 805 */       DEF_H_UNIT = unit;
/* 806 */       MOD_COUNT++;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getDefaultVerticalUnit() {
/* 817 */     return DEF_V_UNIT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setDefaultVerticalUnit(int unit) {
/* 827 */     if (unit < 0 || unit > 27) {
/* 828 */       throw new IllegalArgumentException("Illegal Unit: " + unit);
/*     */     }
/* 830 */     if (DEF_V_UNIT != unit) {
/* 831 */       DEF_V_UNIT = unit;
/* 832 */       MOD_COUNT++;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean getDefaultRowAlignmentBaseline() {
/* 843 */     return dra;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setDefaultRowAlignmentBaseline(boolean b) {
/* 853 */     dra = b;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\net\miginfocom\layout\PlatformDefaults.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */