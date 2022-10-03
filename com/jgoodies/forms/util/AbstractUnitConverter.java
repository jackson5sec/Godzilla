/*     */ package com.jgoodies.forms.util;
/*     */ 
/*     */ import com.jgoodies.common.bean.Bean;
/*     */ import java.awt.Component;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Toolkit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractUnitConverter
/*     */   extends Bean
/*     */   implements UnitConverter
/*     */ {
/*     */   private static final int DTP_RESOLUTION = 72;
/*     */   
/*     */   public int inchAsPixel(double in, Component component) {
/*  66 */     return inchAsPixel(in, getScreenResolution(component));
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
/*     */   public int millimeterAsPixel(double mm, Component component) {
/*  80 */     return millimeterAsPixel(mm, getScreenResolution(component));
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
/*     */   public int centimeterAsPixel(double cm, Component component) {
/*  94 */     return centimeterAsPixel(cm, getScreenResolution(component));
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
/*     */   public int pointAsPixel(int pt, Component component) {
/* 108 */     return pointAsPixel(pt, getScreenResolution(component));
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
/*     */   public int dialogUnitXAsPixel(int dluX, Component c) {
/* 121 */     return dialogUnitXAsPixel(dluX, getDialogBaseUnitsX(c));
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
/*     */   public int dialogUnitYAsPixel(int dluY, Component c) {
/* 135 */     return dialogUnitYAsPixel(dluY, getDialogBaseUnitsY(c));
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
/*     */   protected abstract double getDialogBaseUnitsX(Component paramComponent);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract double getDialogBaseUnitsY(Component paramComponent);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final int inchAsPixel(double in, int dpi) {
/* 173 */     return (int)Math.round(dpi * in);
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
/*     */   protected static final int millimeterAsPixel(double mm, int dpi) {
/* 185 */     return (int)Math.round(dpi * mm * 10.0D / 254.0D);
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
/*     */   protected static final int centimeterAsPixel(double cm, int dpi) {
/* 197 */     return (int)Math.round(dpi * cm * 100.0D / 254.0D);
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
/*     */   protected static final int pointAsPixel(double pt, int dpi) {
/* 209 */     return (int)Math.round(dpi * pt / 72.0D);
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
/*     */   protected int dialogUnitXAsPixel(int dluX, double dialogBaseUnitsX) {
/* 221 */     return (int)Math.round(dluX * dialogBaseUnitsX / 4.0D);
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
/*     */   protected int dialogUnitYAsPixel(int dluY, double dialogBaseUnitsY) {
/* 233 */     return (int)Math.round(dluY * dialogBaseUnitsY / 8.0D);
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
/*     */   protected double computeAverageCharWidth(FontMetrics metrics, String testString) {
/* 251 */     int width = metrics.stringWidth(testString);
/* 252 */     double average = width / testString.length();
/*     */     
/* 254 */     return average;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getScreenResolution(Component c) {
/* 265 */     if (c == null) {
/* 266 */       return getDefaultScreenResolution();
/*     */     }
/*     */     
/* 269 */     Toolkit toolkit = c.getToolkit();
/* 270 */     return (toolkit != null) ? toolkit.getScreenResolution() : getDefaultScreenResolution();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 276 */   private static int defaultScreenResolution = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getDefaultScreenResolution() {
/* 285 */     if (defaultScreenResolution == -1) {
/* 286 */       defaultScreenResolution = Toolkit.getDefaultToolkit().getScreenResolution();
/*     */     }
/*     */     
/* 289 */     return defaultScreenResolution;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\form\\util\AbstractUnitConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */