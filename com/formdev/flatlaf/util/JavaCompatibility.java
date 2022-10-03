/*     */ package com.formdev.flatlaf.util;
/*     */ 
/*     */ import com.formdev.flatlaf.FlatLaf;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.swing.JComponent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JavaCompatibility
/*     */ {
/*     */   private static Method drawStringUnderlineCharAtMethod;
/*     */   private static Method getClippedStringMethod;
/*     */   
/*     */   public static void drawStringUnderlineCharAt(JComponent c, Graphics g, String text, int underlinedIndex, int x, int y) {
/*  51 */     synchronized (JavaCompatibility.class) {
/*  52 */       if (drawStringUnderlineCharAtMethod == null) {
/*     */         try {
/*  54 */           Class<?> cls = Class.forName(SystemInfo.isJava_9_orLater ? "javax.swing.plaf.basic.BasicGraphicsUtils" : "sun.swing.SwingUtilities2");
/*     */ 
/*     */           
/*  57 */           (new Class[6])[0] = JComponent.class; (new Class[6])[1] = Graphics2D.class; (new Class[6])[2] = String.class; (new Class[6])[3] = int.class; (new Class[6])[4] = float.class; (new Class[6])[5] = float.class; (new Class[6])[0] = JComponent.class; (new Class[6])[1] = Graphics.class; (new Class[6])[2] = String.class; (new Class[6])[3] = int.class; (new Class[6])[4] = int.class; (new Class[6])[5] = int.class; drawStringUnderlineCharAtMethod = cls.getMethod("drawStringUnderlineCharAt", SystemInfo.isJava_9_orLater ? new Class[6] : new Class[6]);
/*     */         
/*     */         }
/*  60 */         catch (Exception ex) {
/*  61 */           Logger.getLogger(FlatLaf.class.getName()).log(Level.SEVERE, (String)null, ex);
/*  62 */           throw new RuntimeException(ex);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/*     */     try {
/*  68 */       if (SystemInfo.isJava_9_orLater)
/*  69 */       { drawStringUnderlineCharAtMethod.invoke(null, new Object[] { c, g, text, Integer.valueOf(underlinedIndex), Float.valueOf(x), Float.valueOf(y) }); }
/*     */       else
/*  71 */       { drawStringUnderlineCharAtMethod.invoke(null, new Object[] { c, g, text, Integer.valueOf(underlinedIndex), Integer.valueOf(x), Integer.valueOf(y) }); } 
/*  72 */     } catch (IllegalAccessException|IllegalArgumentException|java.lang.reflect.InvocationTargetException ex) {
/*  73 */       Logger.getLogger(FlatLaf.class.getName()).log(Level.SEVERE, (String)null, ex);
/*  74 */       throw new RuntimeException(ex);
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
/*     */   public static String getClippedString(JComponent c, FontMetrics fm, String string, int availTextWidth) {
/*  86 */     synchronized (JavaCompatibility.class) {
/*  87 */       if (getClippedStringMethod == null) {
/*     */         try {
/*  89 */           Class<?> cls = Class.forName(SystemInfo.isJava_9_orLater ? "javax.swing.plaf.basic.BasicGraphicsUtils" : "sun.swing.SwingUtilities2");
/*     */ 
/*     */           
/*  92 */           getClippedStringMethod = cls.getMethod(SystemInfo.isJava_9_orLater ? "getClippedString" : "clipStringIfNecessary", new Class[] { JComponent.class, FontMetrics.class, String.class, int.class });
/*     */ 
/*     */         
/*     */         }
/*  96 */         catch (Exception ex) {
/*  97 */           Logger.getLogger(FlatLaf.class.getName()).log(Level.SEVERE, (String)null, ex);
/*  98 */           throw new RuntimeException(ex);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/*     */     try {
/* 104 */       return (String)getClippedStringMethod.invoke(null, new Object[] { c, fm, string, Integer.valueOf(availTextWidth) });
/* 105 */     } catch (IllegalAccessException|IllegalArgumentException|java.lang.reflect.InvocationTargetException ex) {
/* 106 */       Logger.getLogger(FlatLaf.class.getName()).log(Level.SEVERE, (String)null, ex);
/* 107 */       throw new RuntimeException(ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\util\JavaCompatibility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */