/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import com.formdev.flatlaf.util.UIScale;
/*     */ import java.awt.Insets;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.util.function.Function;
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
/*     */ public class MigLayoutVisualPadding
/*     */ {
/*  42 */   public static String VISUAL_PADDING_PROPERTY = "visualPadding";
/*     */   
/*  44 */   private static final FlatMigInsets ZERO = new FlatMigInsets(0, 0, 0, 0);
/*     */   
/*     */   private static final boolean migLayoutAvailable;
/*     */   
/*     */   static {
/*  49 */     boolean available = false;
/*     */     try {
/*  51 */       Class.forName("net.miginfocom.swing.MigLayout");
/*  52 */       available = true;
/*  53 */     } catch (ClassNotFoundException classNotFoundException) {}
/*     */ 
/*     */     
/*  56 */     migLayoutAvailable = available;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void install(JComponent c, Insets insets) {
/*  63 */     if (!migLayoutAvailable) {
/*     */       return;
/*     */     }
/*  66 */     setVisualPadding(c, insets);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void install(JComponent c) {
/*  73 */     if (!migLayoutAvailable) {
/*     */       return;
/*     */     }
/*  76 */     install(c, c2 -> { FlatBorder border = FlatUIUtils.getOutsideFlatBorder(c2); if (border != null) { int focusWidth = border.getFocusWidth(c2); return new Insets(focusWidth, focusWidth, focusWidth, focusWidth); }  return null; }new String[] { "border" });
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
/*     */   public static void install(JComponent c, Function<JComponent, Insets> getPaddingFunction, String... propertyNames) {
/*  92 */     if (!migLayoutAvailable) {
/*     */       return;
/*     */     }
/*     */     
/*  96 */     setVisualPadding(c, getPaddingFunction.apply(c));
/*     */ 
/*     */     
/*  99 */     c.addPropertyChangeListener(e -> {
/*     */           String propertyName = e.getPropertyName();
/*     */           for (String name : propertyNames) {
/*     */             if (name == propertyName) {
/*     */               setVisualPadding(c, getPaddingFunction.apply(c));
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private static void setVisualPadding(JComponent c, Insets visualPadding) {
/* 111 */     Object oldPadding = c.getClientProperty(VISUAL_PADDING_PROPERTY);
/* 112 */     if (oldPadding == null || oldPadding instanceof FlatMigInsets) {
/*     */ 
/*     */       
/* 115 */       FlatMigInsets flatVisualPadding = (visualPadding != null) ? new FlatMigInsets(UIScale.scale2(visualPadding.top), UIScale.scale2(visualPadding.left), UIScale.scale2(visualPadding.bottom), UIScale.scale2(visualPadding.right)) : ZERO;
/*     */ 
/*     */       
/* 118 */       c.putClientProperty(VISUAL_PADDING_PROPERTY, flatVisualPadding);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void uninstall(JComponent c) {
/* 126 */     if (!migLayoutAvailable) {
/*     */       return;
/*     */     }
/*     */     
/* 130 */     for (PropertyChangeListener l : c.getPropertyChangeListeners()) {
/* 131 */       if (l instanceof FlatMigListener) {
/* 132 */         c.removePropertyChangeListener(l);
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*     */     
/* 138 */     if (c.getClientProperty(VISUAL_PADDING_PROPERTY) instanceof FlatMigInsets) {
/* 139 */       c.putClientProperty(VISUAL_PADDING_PROPERTY, (Object)null);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static interface FlatMigListener
/*     */     extends PropertyChangeListener {}
/*     */ 
/*     */   
/*     */   private static class FlatMigInsets
/*     */     extends Insets
/*     */   {
/*     */     FlatMigInsets(int top, int left, int bottom, int right) {
/* 152 */       super(top, left, bottom, right);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\MigLayoutVisualPadding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */