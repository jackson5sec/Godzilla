/*     */ package com.kitfox.svg.xml;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NumberWithUnits
/*     */   implements Serializable
/*     */ {
/*     */   public static final long serialVersionUID = 0L;
/*     */   public static final int UT_UNITLESS = 0;
/*     */   public static final int UT_PX = 1;
/*     */   public static final int UT_CM = 2;
/*     */   public static final int UT_MM = 3;
/*     */   public static final int UT_IN = 4;
/*     */   public static final int UT_EM = 5;
/*     */   public static final int UT_EX = 6;
/*     */   public static final int UT_PT = 7;
/*     */   public static final int UT_PC = 8;
/*     */   public static final int UT_PERCENT = 9;
/*  60 */   float value = 0.0F;
/*  61 */   int unitType = 0;
/*     */ 
/*     */ 
/*     */   
/*     */   public NumberWithUnits() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public NumberWithUnits(String value) {
/*  70 */     set(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public NumberWithUnits(float value, int unitType) {
/*  75 */     this.value = value;
/*  76 */     this.unitType = unitType;
/*     */   }
/*     */   
/*  79 */   public float getValue() { return this.value; } public int getUnits() {
/*  80 */     return this.unitType;
/*     */   }
/*     */   
/*     */   public void set(String value) {
/*  84 */     this.value = XMLParseUtil.findFloat(value);
/*  85 */     this.unitType = 0;
/*     */     
/*  87 */     if (value.indexOf("px") != -1) { this.unitType = 1; return; }
/*  88 */      if (value.indexOf("cm") != -1) { this.unitType = 2; return; }
/*  89 */      if (value.indexOf("mm") != -1) { this.unitType = 3; return; }
/*  90 */      if (value.indexOf("in") != -1) { this.unitType = 4; return; }
/*  91 */      if (value.indexOf("em") != -1) { this.unitType = 5; return; }
/*  92 */      if (value.indexOf("ex") != -1) { this.unitType = 6; return; }
/*  93 */      if (value.indexOf("pt") != -1) { this.unitType = 7; return; }
/*  94 */      if (value.indexOf("pc") != -1) { this.unitType = 8; return; }
/*  95 */      if (value.indexOf("%") != -1) { this.unitType = 9;
/*     */       return; }
/*     */   
/*     */   }
/*     */   public static String unitsAsString(int unitIdx) {
/* 100 */     switch (unitIdx)
/*     */     
/*     */     { default:
/* 103 */         return "";
/*     */       case 1:
/* 105 */         return "px";
/*     */       case 2:
/* 107 */         return "cm";
/*     */       case 3:
/* 109 */         return "mm";
/*     */       case 4:
/* 111 */         return "in";
/*     */       case 5:
/* 113 */         return "em";
/*     */       case 6:
/* 115 */         return "ex";
/*     */       case 7:
/* 117 */         return "pt";
/*     */       case 8:
/* 119 */         return "pc";
/*     */       case 9:
/* 121 */         break; }  return "%";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 128 */     return "" + this.value + unitsAsString(this.unitType);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 134 */     if (obj == null) {
/* 135 */       return false;
/*     */     }
/* 137 */     if (getClass() != obj.getClass()) {
/* 138 */       return false;
/*     */     }
/* 140 */     NumberWithUnits other = (NumberWithUnits)obj;
/* 141 */     if (Float.floatToIntBits(this.value) != Float.floatToIntBits(other.value)) {
/* 142 */       return false;
/*     */     }
/* 144 */     if (this.unitType != other.unitType) {
/* 145 */       return false;
/*     */     }
/* 147 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 153 */     int hash = 5;
/* 154 */     hash = 37 * hash + Float.floatToIntBits(this.value);
/* 155 */     hash = 37 * hash + this.unitType;
/* 156 */     return hash;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\xml\NumberWithUnits.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */