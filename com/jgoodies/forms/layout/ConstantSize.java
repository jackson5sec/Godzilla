/*     */ package com.jgoodies.forms.layout;
/*     */ 
/*     */ import com.jgoodies.common.base.Preconditions;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.io.Serializable;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ConstantSize
/*     */   implements Size, Serializable
/*     */ {
/*  80 */   public static final Unit PIXEL = new Unit("Pixel", "px", null, true);
/*  81 */   public static final Unit POINT = new Unit("Point", "pt", null, true);
/*  82 */   public static final Unit DIALOG_UNITS_X = new Unit("Dialog units X", "dluX", "dlu", true);
/*  83 */   public static final Unit DIALOG_UNITS_Y = new Unit("Dialog units Y", "dluY", "dlu", true);
/*  84 */   public static final Unit MILLIMETER = new Unit("Millimeter", "mm", null, false);
/*  85 */   public static final Unit CENTIMETER = new Unit("Centimeter", "cm", null, false);
/*  86 */   public static final Unit INCH = new Unit("Inch", "in", null, false);
/*     */   
/*  88 */   public static final Unit PX = PIXEL;
/*  89 */   public static final Unit PT = POINT;
/*  90 */   public static final Unit DLUX = DIALOG_UNITS_X;
/*  91 */   public static final Unit DLUY = DIALOG_UNITS_Y;
/*  92 */   public static final Unit MM = MILLIMETER;
/*  93 */   public static final Unit CM = CENTIMETER;
/*  94 */   public static final Unit IN = INCH;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 100 */   private static final Unit[] VALUES = new Unit[] { PIXEL, POINT, DIALOG_UNITS_X, DIALOG_UNITS_Y, MILLIMETER, CENTIMETER, INCH };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final double value;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Unit unit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstantSize(int value, Unit unit) {
/* 121 */     this.value = value;
/* 122 */     this.unit = unit;
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
/*     */   public ConstantSize(double value, Unit unit) {
/* 135 */     this.value = value;
/* 136 */     this.unit = unit;
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
/*     */   static ConstantSize valueOf(String encodedValueAndUnit, boolean horizontal) {
/* 153 */     String[] split = splitValueAndUnit(encodedValueAndUnit);
/* 154 */     String encodedValue = split[0];
/* 155 */     String encodedUnit = split[1];
/* 156 */     Unit unit = Unit.valueOf(encodedUnit, horizontal);
/* 157 */     double value = Double.parseDouble(encodedValue);
/* 158 */     if (unit.requiresIntegers) {
/* 159 */       Preconditions.checkArgument((value == (int)value), "%s value %s must be an integer.", new Object[] { unit, encodedValue });
/*     */     }
/*     */     
/* 162 */     return new ConstantSize(value, unit);
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
/*     */   static ConstantSize dluX(int value) {
/* 174 */     return new ConstantSize(value, DLUX);
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
/*     */   static ConstantSize dluY(int value) {
/* 186 */     return new ConstantSize(value, DLUY);
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
/*     */   public double getValue() {
/* 200 */     return this.value;
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
/*     */   public Unit getUnit() {
/* 212 */     return this.unit;
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
/*     */   public int getPixelSize(Component component) {
/* 225 */     if (this.unit == PIXEL)
/* 226 */       return intValue(); 
/* 227 */     if (this.unit == DIALOG_UNITS_X)
/* 228 */       return Sizes.dialogUnitXAsPixel(intValue(), component); 
/* 229 */     if (this.unit == DIALOG_UNITS_Y)
/* 230 */       return Sizes.dialogUnitYAsPixel(intValue(), component); 
/* 231 */     if (this.unit == POINT)
/* 232 */       return Sizes.pointAsPixel(intValue(), component); 
/* 233 */     if (this.unit == INCH)
/* 234 */       return Sizes.inchAsPixel(this.value, component); 
/* 235 */     if (this.unit == MILLIMETER)
/* 236 */       return Sizes.millimeterAsPixel(this.value, component); 
/* 237 */     if (this.unit == CENTIMETER) {
/* 238 */       return Sizes.centimeterAsPixel(this.value, component);
/*     */     }
/* 240 */     throw new IllegalStateException("Invalid unit " + this.unit);
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
/*     */   public int maximumSize(Container container, List components, FormLayout.Measure minMeasure, FormLayout.Measure prefMeasure, FormLayout.Measure defaultMeasure) {
/* 267 */     return getPixelSize(container);
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
/*     */   public boolean compressible() {
/* 284 */     return false;
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
/*     */   public boolean equals(Object o) {
/* 302 */     if (this == o) {
/* 303 */       return true;
/*     */     }
/* 305 */     if (!(o instanceof ConstantSize)) {
/* 306 */       return false;
/*     */     }
/* 308 */     ConstantSize size = (ConstantSize)o;
/* 309 */     return (this.value == size.value && this.unit == size.unit);
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
/*     */   public int hashCode() {
/* 326 */     return (new Double(this.value)).hashCode() + 37 * this.unit.hashCode();
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
/*     */   public String toString() {
/* 341 */     return (this.value == intValue()) ? (Integer.toString(intValue()) + this.unit.abbreviation()) : (Double.toString(this.value) + this.unit.abbreviation());
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
/*     */   public String encode() {
/* 356 */     return (this.value == intValue()) ? (Integer.toString(intValue()) + this.unit.encode()) : (Double.toString(this.value) + this.unit.encode());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int intValue() {
/* 365 */     return (int)Math.round(this.value);
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
/*     */   private static String[] splitValueAndUnit(String encodedValueAndUnit) {
/* 378 */     String[] result = new String[2];
/* 379 */     int len = encodedValueAndUnit.length();
/* 380 */     int firstLetterIndex = len;
/*     */     
/* 382 */     while (firstLetterIndex > 0 && Character.isLetter(encodedValueAndUnit.charAt(firstLetterIndex - 1))) {
/* 383 */       firstLetterIndex--;
/*     */     }
/* 385 */     result[0] = encodedValueAndUnit.substring(0, firstLetterIndex);
/* 386 */     result[1] = encodedValueAndUnit.substring(firstLetterIndex);
/* 387 */     return result;
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
/*     */   public static final class Unit
/*     */     implements Serializable
/*     */   {
/*     */     private final transient String name;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final transient String abbreviation;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final transient String parseAbbreviation;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final transient boolean requiresIntegers;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Unit(String name, String abbreviation, String parseAbbreviation, boolean requiresIntegers) {
/* 491 */       this.ordinal = nextOrdinal++; this.name = name;
/*     */       this.abbreviation = abbreviation;
/*     */       this.parseAbbreviation = parseAbbreviation;
/* 494 */       this.requiresIntegers = requiresIntegers; } private Object readResolve() { return ConstantSize.VALUES[this.ordinal]; }
/*     */ 
/*     */     
/*     */     static Unit valueOf(String name, boolean horizontal) {
/*     */       if (name.length() == 0) {
/*     */         Unit defaultUnit = Sizes.getDefaultUnit();
/*     */         if (defaultUnit != null)
/*     */           return defaultUnit; 
/*     */         return horizontal ? ConstantSize.DIALOG_UNITS_X : ConstantSize.DIALOG_UNITS_Y;
/*     */       } 
/*     */       if (name.equals("px"))
/*     */         return ConstantSize.PIXEL; 
/*     */       if (name.equals("dlu"))
/*     */         return horizontal ? ConstantSize.DIALOG_UNITS_X : ConstantSize.DIALOG_UNITS_Y; 
/*     */       if (name.equals("pt"))
/*     */         return ConstantSize.POINT; 
/*     */       if (name.equals("in"))
/*     */         return ConstantSize.INCH; 
/*     */       if (name.equals("mm"))
/*     */         return ConstantSize.MILLIMETER; 
/*     */       if (name.equals("cm"))
/*     */         return ConstantSize.CENTIMETER; 
/*     */       throw new IllegalArgumentException("Invalid unit name '" + name + "'. Must be one of: " + "px, dlu, pt, mm, cm, in");
/*     */     }
/*     */     
/*     */     public String toString() {
/*     */       return this.name;
/*     */     }
/*     */     
/*     */     public String encode() {
/*     */       return (this.parseAbbreviation != null) ? this.parseAbbreviation : this.abbreviation;
/*     */     }
/*     */     
/*     */     public String abbreviation() {
/*     */       return this.abbreviation;
/*     */     }
/*     */     
/*     */     private static int nextOrdinal = 0;
/*     */     private final int ordinal;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\forms\layout\ConstantSize.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */