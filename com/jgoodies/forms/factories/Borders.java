/*     */ package com.jgoodies.forms.factories;
/*     */ 
/*     */ import com.jgoodies.forms.layout.ConstantSize;
/*     */ import javax.swing.border.EmptyBorder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public final class Borders
/*     */ {
/*  72 */   public static final EmptyBorder EMPTY = new EmptyBorder(0, 0, 0, 0);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  79 */   public static final Paddings.Padding DLU2 = Paddings.DLU2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   public static final Paddings.Padding DLU4 = Paddings.DLU4;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  91 */   public static final Paddings.Padding DLU7 = Paddings.DLU7;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  99 */   public static final Paddings.Padding DLU9 = Paddings.DLU9;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 105 */   public static final Paddings.Padding DLU14 = Paddings.DLU14;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 113 */   public static final Paddings.Padding DLU21 = Paddings.DLU21;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 120 */   public static final Paddings.Padding BUTTON_BAR_PAD = Paddings.BUTTON_BAR_PAD;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 129 */   public static final Paddings.Padding DIALOG = Paddings.DIALOG;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 138 */   public static final Paddings.Padding TABBED_DIALOG = Paddings.TABBED_DIALOG;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static Paddings.Padding createEmptyBorder(ConstantSize top, ConstantSize left, ConstantSize bottom, ConstantSize right) {
/* 162 */     return Paddings.createPadding(top, left, bottom, right);
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
/*     */   @Deprecated
/*     */   public static Paddings.Padding createEmptyBorder(String encodedSizes) {
/* 179 */     return Paddings.createPadding(encodedSizes, new Object[0]);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\forms\factories\Borders.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */