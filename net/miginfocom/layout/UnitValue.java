/*     */ package net.miginfocom.layout;
/*     */ 
/*     */ import java.beans.Encoder;
/*     */ import java.beans.Expression;
/*     */ import java.beans.PersistenceDelegate;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.ObjectStreamException;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
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
/*     */ public final class UnitValue
/*     */   implements Serializable
/*     */ {
/*  45 */   private static final HashMap<String, Integer> UNIT_MAP = new HashMap<>(32);
/*     */   
/*  47 */   private static final ArrayList<UnitConverter> CONVERTERS = new ArrayList<>();
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int STATIC = 100;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int ADD = 101;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int SUB = 102;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int MUL = 103;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int DIV = 104;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int MIN = 105;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int MAX = 106;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int MID = 107;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int PIXEL = 0;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int LPX = 1;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int LPY = 2;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int MM = 3;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int CM = 4;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int INCH = 5;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int PERCENT = 6;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int PT = 7;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int SPX = 8;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int SPY = 9;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int ALIGN = 12;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int MIN_SIZE = 13;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int PREF_SIZE = 14;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int MAX_SIZE = 15;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int BUTTON = 16;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int LINK_X = 18;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int LINK_Y = 19;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int LINK_W = 20;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int LINK_H = 21;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int LINK_X2 = 22;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int LINK_Y2 = 23;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int LINK_XPOS = 24;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int LINK_YPOS = 25;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int LOOKUP = 26;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int LABEL_ALIGN = 27;
/*     */ 
/*     */   
/*     */   private static final int IDENTITY = -1;
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 187 */     UNIT_MAP.put("px", Integer.valueOf(0));
/* 188 */     UNIT_MAP.put("lpx", Integer.valueOf(1));
/* 189 */     UNIT_MAP.put("lpy", Integer.valueOf(2));
/* 190 */     UNIT_MAP.put("%", Integer.valueOf(6));
/* 191 */     UNIT_MAP.put("cm", Integer.valueOf(4));
/* 192 */     UNIT_MAP.put("in", Integer.valueOf(5));
/* 193 */     UNIT_MAP.put("spx", Integer.valueOf(8));
/* 194 */     UNIT_MAP.put("spy", Integer.valueOf(9));
/* 195 */     UNIT_MAP.put("al", Integer.valueOf(12));
/* 196 */     UNIT_MAP.put("mm", Integer.valueOf(3));
/* 197 */     UNIT_MAP.put("pt", Integer.valueOf(7));
/* 198 */     UNIT_MAP.put("min", Integer.valueOf(13));
/* 199 */     UNIT_MAP.put("minimum", Integer.valueOf(13));
/* 200 */     UNIT_MAP.put("p", Integer.valueOf(14));
/* 201 */     UNIT_MAP.put("pref", Integer.valueOf(14));
/* 202 */     UNIT_MAP.put("max", Integer.valueOf(15));
/* 203 */     UNIT_MAP.put("maximum", Integer.valueOf(15));
/* 204 */     UNIT_MAP.put("button", Integer.valueOf(16));
/* 205 */     UNIT_MAP.put("label", Integer.valueOf(27));
/*     */   }
/*     */   
/* 208 */   static final UnitValue ZERO = new UnitValue(0.0F, null, 0, true, 100, null, null, "0px");
/* 209 */   static final UnitValue TOP = new UnitValue(0.0F, null, 6, false, 100, null, null, "top");
/* 210 */   static final UnitValue LEADING = new UnitValue(0.0F, null, 6, true, 100, null, null, "leading");
/* 211 */   static final UnitValue LEFT = new UnitValue(0.0F, null, 6, true, 100, null, null, "left");
/* 212 */   static final UnitValue CENTER = new UnitValue(50.0F, null, 6, true, 100, null, null, "center");
/* 213 */   static final UnitValue TRAILING = new UnitValue(100.0F, null, 6, true, 100, null, null, "trailing");
/* 214 */   static final UnitValue RIGHT = new UnitValue(100.0F, null, 6, true, 100, null, null, "right");
/* 215 */   static final UnitValue BOTTOM = new UnitValue(100.0F, null, 6, false, 100, null, null, "bottom");
/* 216 */   static final UnitValue LABEL = new UnitValue(0.0F, null, 27, false, 100, null, null, "label");
/*     */   
/* 218 */   static final UnitValue INF = new UnitValue(2097051.0F, null, 0, true, 100, null, null, "inf");
/*     */   
/* 220 */   static final UnitValue BASELINE_IDENTITY = new UnitValue(0.0F, null, -1, false, 100, null, null, "baseline");
/*     */   
/*     */   private final transient float value;
/*     */   private final transient int unit;
/*     */   private final transient int oper;
/*     */   private final transient String unitStr;
/* 226 */   private transient String linkId = null;
/*     */   
/*     */   private final transient boolean isHor;
/*     */   
/*     */   private final transient UnitValue[] subUnits;
/*     */   
/*     */   public UnitValue(float value) {
/* 233 */     this(value, null, 0, true, 100, null, null, value + "px");
/*     */   }
/*     */ 
/*     */   
/*     */   public UnitValue(float value, int unit, String createString) {
/* 238 */     this(value, null, unit, true, 100, null, null, createString);
/*     */   }
/*     */ 
/*     */   
/*     */   public UnitValue(float value, String unitStr, boolean isHor, int oper, String createString) {
/* 243 */     this(value, unitStr, -1, isHor, oper, null, null, createString);
/*     */   }
/*     */ 
/*     */   
/*     */   UnitValue(boolean isHor, int oper, UnitValue sub1, UnitValue sub2, String createString) {
/* 248 */     this(0.0F, "", -1, isHor, oper, sub1, sub2, createString);
/* 249 */     if (sub1 == null || sub2 == null) {
/* 250 */       throw new IllegalArgumentException("Sub units is null!");
/*     */     }
/*     */   }
/*     */   
/*     */   private UnitValue(float value, String unitStr, int unit, boolean isHor, int oper, UnitValue sub1, UnitValue sub2, String createString) {
/* 255 */     if (oper < 100 || oper > 107) {
/* 256 */       throw new IllegalArgumentException("Unknown Operation: " + oper);
/*     */     }
/* 258 */     if (oper >= 101 && oper <= 107 && (sub1 == null || sub2 == null)) {
/* 259 */       throw new IllegalArgumentException(oper + " Operation may not have null sub-UnitValues.");
/*     */     }
/* 261 */     this.value = value;
/* 262 */     this.oper = oper;
/* 263 */     this.isHor = isHor;
/* 264 */     this.unitStr = unitStr;
/* 265 */     this.unit = (unitStr != null) ? parseUnitString() : unit;
/* 266 */     (new UnitValue[2])[0] = sub1; (new UnitValue[2])[1] = sub2; this.subUnits = (sub1 != null && sub2 != null) ? new UnitValue[2] : null;
/*     */     
/* 268 */     LayoutUtil.putCCString(this, createString);
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
/*     */   public final int getPixels(float refValue, ContainerWrapper parent, ComponentWrapper comp) {
/* 281 */     return Math.round(getPixelsExact(refValue, parent, comp));
/*     */   }
/*     */   
/* 284 */   private static final float[] SCALE = new float[] { 25.4F, 2.54F, 1.0F, 0.0F, 72.0F };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long serialVersionUID = 1L;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final float getPixelsExact(float refValue, ContainerWrapper parent, ComponentWrapper comp) {
/* 295 */     if (parent == null) {
/* 296 */       return 1.0F;
/*     */     }
/* 298 */     if (this.oper == 100) {
/* 299 */       float f; Float s; Integer st; Integer sz; Integer v; float res; switch (this.unit) {
/*     */         case 0:
/* 301 */           return this.value;
/*     */         
/*     */         case 1:
/*     */         case 2:
/* 305 */           return parent.getPixelUnitFactor((this.unit == 1)) * this.value;
/*     */         
/*     */         case 3:
/*     */         case 4:
/*     */         case 5:
/*     */         case 7:
/* 311 */           f = SCALE[this.unit - 3];
/* 312 */           s = this.isHor ? PlatformDefaults.getHorizontalScaleFactor() : PlatformDefaults.getVerticalScaleFactor();
/* 313 */           if (s != null) {
/* 314 */             f *= s.floatValue();
/*     */           }
/* 316 */           return (this.isHor ? parent.getHorizontalScreenDPI() : parent.getVerticalScreenDPI()) * this.value / f;
/*     */         
/*     */         case 6:
/* 319 */           return this.value * refValue * 0.01F;
/*     */         
/*     */         case 8:
/*     */         case 9:
/* 323 */           return ((this.unit == 8) ? parent.getScreenWidth() : parent.getScreenHeight()) * this.value * 0.01F;
/*     */         
/*     */         case 12:
/* 326 */           st = LinkHandler.getValue(parent.getLayout(), "visual", this.isHor ? 0 : 1);
/* 327 */           sz = LinkHandler.getValue(parent.getLayout(), "visual", this.isHor ? 2 : 3);
/* 328 */           if (st == null || sz == null)
/* 329 */             return 0.0F; 
/* 330 */           return this.value * (Math.max(0, sz.intValue()) - refValue) + st.intValue();
/*     */         
/*     */         case 13:
/* 333 */           if (comp == null)
/* 334 */             return 0.0F; 
/* 335 */           return this.isHor ? comp.getMinimumWidth(comp.getHeight()) : comp.getMinimumHeight(comp.getWidth());
/*     */         
/*     */         case 14:
/* 338 */           if (comp == null)
/* 339 */             return 0.0F; 
/* 340 */           return this.isHor ? comp.getPreferredWidth(comp.getHeight()) : comp.getPreferredHeight(comp.getWidth());
/*     */         
/*     */         case 15:
/* 343 */           if (comp == null)
/* 344 */             return 0.0F; 
/* 345 */           return this.isHor ? comp.getMaximumWidth(comp.getHeight()) : comp.getMaximumHeight(comp.getWidth());
/*     */         
/*     */         case 16:
/* 348 */           return PlatformDefaults.getMinimumButtonWidthIncludingPadding(refValue, parent, comp);
/*     */         
/*     */         case 18:
/*     */         case 19:
/*     */         case 20:
/*     */         case 21:
/*     */         case 22:
/*     */         case 23:
/*     */         case 24:
/*     */         case 25:
/* 358 */           v = LinkHandler.getValue(parent.getLayout(), getLinkTargetId(), this.unit - ((this.unit >= 24) ? 24 : 18));
/* 359 */           if (v == null) {
/* 360 */             return 0.0F;
/*     */           }
/* 362 */           if (this.unit == 24)
/* 363 */             return (parent.getScreenLocationX() + v.intValue()); 
/* 364 */           if (this.unit == 25) {
/* 365 */             return (parent.getScreenLocationY() + v.intValue());
/*     */           }
/* 367 */           return v.intValue();
/*     */         
/*     */         case 26:
/* 370 */           res = lookup(refValue, parent, comp);
/* 371 */           if (res != -8.7654312E7F) {
/* 372 */             return res;
/*     */           }
/*     */         case 27:
/* 375 */           return PlatformDefaults.getLabelAlignPercentage() * refValue;
/*     */       } 
/*     */ 
/*     */       
/* 379 */       throw new IllegalArgumentException("Unknown/illegal unit: " + this.unit + ", unitStr: " + this.unitStr);
/*     */     } 
/*     */     
/* 382 */     if (this.subUnits != null && this.subUnits.length == 2) {
/* 383 */       float r1 = this.subUnits[0].getPixelsExact(refValue, parent, comp);
/* 384 */       float r2 = this.subUnits[1].getPixelsExact(refValue, parent, comp);
/* 385 */       switch (this.oper) {
/*     */         case 101:
/* 387 */           return r1 + r2;
/*     */         case 102:
/* 389 */           return r1 - r2;
/*     */         case 103:
/* 391 */           return r1 * r2;
/*     */         case 104:
/* 393 */           return r1 / r2;
/*     */         case 105:
/* 395 */           return (r1 < r2) ? r1 : r2;
/*     */         case 106:
/* 397 */           return (r1 > r2) ? r1 : r2;
/*     */         case 107:
/* 399 */           return (r1 + r2) * 0.5F;
/*     */       } 
/*     */     
/*     */     } 
/* 403 */     throw new IllegalArgumentException("Internal: Unknown Oper: " + this.oper);
/*     */   }
/*     */ 
/*     */   
/*     */   private float lookup(float refValue, ContainerWrapper parent, ComponentWrapper comp) {
/* 408 */     float res = -8.7654312E7F;
/* 409 */     for (int i = CONVERTERS.size() - 1; i >= 0; i--) {
/* 410 */       res = ((UnitConverter)CONVERTERS.get(i)).convertToPixels(this.value, this.unitStr, this.isHor, refValue, parent, comp);
/* 411 */       if (res != -8.7654312E7F)
/* 412 */         return res; 
/*     */     } 
/* 414 */     return PlatformDefaults.convertToPixels(this.value, this.unitStr, this.isHor, refValue, parent, comp);
/*     */   }
/*     */ 
/*     */   
/*     */   private int parseUnitString() {
/* 419 */     int len = this.unitStr.length();
/* 420 */     if (len == 0) {
/* 421 */       return this.isHor ? PlatformDefaults.getDefaultHorizontalUnit() : PlatformDefaults.getDefaultVerticalUnit();
/*     */     }
/* 423 */     Integer u = UNIT_MAP.get(this.unitStr);
/* 424 */     if (u != null) {
/* 425 */       if (!this.isHor && (u.intValue() == 16 || u.intValue() == 27)) {
/* 426 */         throw new IllegalArgumentException("Not valid in vertical contexts: '" + this.unitStr + "'");
/*     */       }
/* 428 */       return u.intValue();
/*     */     } 
/*     */     
/* 431 */     if (this.unitStr.equals("lp")) {
/* 432 */       return this.isHor ? 1 : 2;
/*     */     }
/* 434 */     if (this.unitStr.equals("sp")) {
/* 435 */       return this.isHor ? 8 : 9;
/*     */     }
/* 437 */     if (lookup(0.0F, null, null) != -8.7654312E7F) {
/* 438 */       return 26;
/*     */     }
/*     */ 
/*     */     
/* 442 */     int pIx = this.unitStr.indexOf('.');
/* 443 */     if (pIx != -1) {
/* 444 */       this.linkId = this.unitStr.substring(0, pIx);
/* 445 */       String e = this.unitStr.substring(pIx + 1);
/*     */       
/* 447 */       if (e.equals("x"))
/* 448 */         return 18; 
/* 449 */       if (e.equals("y"))
/* 450 */         return 19; 
/* 451 */       if (e.equals("w") || e.equals("width"))
/* 452 */         return 20; 
/* 453 */       if (e.equals("h") || e.equals("height"))
/* 454 */         return 21; 
/* 455 */       if (e.equals("x2"))
/* 456 */         return 22; 
/* 457 */       if (e.equals("y2"))
/* 458 */         return 23; 
/* 459 */       if (e.equals("xpos"))
/* 460 */         return 24; 
/* 461 */       if (e.equals("ypos")) {
/* 462 */         return 25;
/*     */       }
/*     */     } 
/* 465 */     throw new IllegalArgumentException("Unknown keyword: " + this.unitStr);
/*     */   }
/*     */ 
/*     */   
/*     */   final boolean isAbsolute() {
/* 470 */     switch (this.unit) {
/*     */       case 0:
/*     */       case 1:
/*     */       case 2:
/*     */       case 3:
/*     */       case 4:
/*     */       case 5:
/*     */       case 7:
/* 478 */         return true;
/*     */       
/*     */       case 6:
/*     */       case 8:
/*     */       case 9:
/*     */       case 12:
/*     */       case 13:
/*     */       case 14:
/*     */       case 15:
/*     */       case 16:
/*     */       case 18:
/*     */       case 19:
/*     */       case 20:
/*     */       case 21:
/*     */       case 22:
/*     */       case 23:
/*     */       case 24:
/*     */       case 25:
/*     */       case 26:
/*     */       case 27:
/* 498 */         return false;
/*     */     } 
/*     */ 
/*     */     
/* 502 */     throw new IllegalArgumentException("Unknown/illegal unit: " + this.unit + ", unitStr: " + this.unitStr);
/*     */   }
/*     */ 
/*     */   
/*     */   final boolean isAbsoluteDeep() {
/* 507 */     if (this.subUnits != null)
/* 508 */       for (UnitValue subUnit : this.subUnits) {
/* 509 */         if (subUnit.isAbsoluteDeep()) {
/* 510 */           return true;
/*     */         }
/*     */       }  
/* 513 */     return isAbsolute();
/*     */   }
/*     */ 
/*     */   
/*     */   final boolean isLinked() {
/* 518 */     return (this.linkId != null);
/*     */   }
/*     */ 
/*     */   
/*     */   final boolean isLinkedDeep() {
/* 523 */     if (this.subUnits != null)
/* 524 */       for (UnitValue subUnit : this.subUnits) {
/* 525 */         if (subUnit.isLinkedDeep()) {
/* 526 */           return true;
/*     */         }
/*     */       }  
/* 529 */     return isLinked();
/*     */   }
/*     */ 
/*     */   
/*     */   final String getLinkTargetId() {
/* 534 */     return this.linkId;
/*     */   }
/*     */ 
/*     */   
/*     */   final UnitValue getSubUnitValue(int i) {
/* 539 */     return this.subUnits[i];
/*     */   }
/*     */ 
/*     */   
/*     */   final int getSubUnitCount() {
/* 544 */     return (this.subUnits != null) ? this.subUnits.length : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public final UnitValue[] getSubUnits() {
/* 549 */     return (this.subUnits != null) ? (UnitValue[])this.subUnits.clone() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getUnit() {
/* 554 */     return this.unit;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getUnitString() {
/* 559 */     return this.unitStr;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getOperation() {
/* 564 */     return this.oper;
/*     */   }
/*     */ 
/*     */   
/*     */   public final float getValue() {
/* 569 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isHorizontal() {
/* 574 */     return this.isHor;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final String toString() {
/* 580 */     return getClass().getName() + ". Value=" + this.value + ", unit=" + this.unit + ", unitString: " + this.unitStr + ", oper=" + this.oper + ", isHor: " + this.isHor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getConstraintString() {
/* 589 */     return LayoutUtil.getCCString(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final int hashCode() {
/* 595 */     return (int)(this.value * 12345.0F) + (this.oper >>> 5) + this.unit >>> 17;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized void addGlobalUnitConverter(UnitConverter conv) {
/* 606 */     if (conv == null)
/* 607 */       throw new NullPointerException(); 
/* 608 */     CONVERTERS.add(conv);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized boolean removeGlobalUnitConverter(UnitConverter unit) {
/* 617 */     return CONVERTERS.remove(unit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized UnitConverter[] getGlobalUnitConverters() {
/* 625 */     return CONVERTERS.<UnitConverter>toArray(new UnitConverter[CONVERTERS.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getDefaultUnit() {
/* 636 */     return PlatformDefaults.getDefaultHorizontalUnit();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setDefaultUnit(int unit) {
/* 647 */     PlatformDefaults.setDefaultHorizontalUnit(unit);
/* 648 */     PlatformDefaults.setDefaultVerticalUnit(unit);
/*     */   }
/*     */   
/*     */   static {
/* 652 */     if (LayoutUtil.HAS_BEANS) {
/* 653 */       LayoutUtil.setDelegate(UnitValue.class, new PersistenceDelegate()
/*     */           {
/*     */             protected Expression instantiate(Object oldInstance, Encoder out)
/*     */             {
/* 657 */               UnitValue uv = (UnitValue)oldInstance;
/* 658 */               String cs = uv.getConstraintString();
/* 659 */               if (cs == null) {
/* 660 */                 throw new IllegalStateException("Design time must be on to use XML persistence. See LayoutUtil.");
/*     */               }
/* 662 */               return new Expression(oldInstance, ConstraintParser.class, "parseUnitValueOrAlign", new Object[] { uv
/* 663 */                     .getConstraintString(), uv.isHorizontal() ? Boolean.TRUE : Boolean.FALSE, null });
/*     */             }
/*     */           });
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
/*     */   private Object readResolve() throws ObjectStreamException {
/* 678 */     return LayoutUtil.getSerializedObject(this);
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 683 */     if (getClass() == UnitValue.class) {
/* 684 */       LayoutUtil.writeAsXML(out, this);
/*     */     }
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 689 */     LayoutUtil.setSerializedObject(this, LayoutUtil.readAsXML(in));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\net\miginfocom\layout\UnitValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */