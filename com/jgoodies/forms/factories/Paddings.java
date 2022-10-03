/*     */ package com.jgoodies.forms.factories;
/*     */ 
/*     */ import com.jgoodies.common.base.Preconditions;
/*     */ import com.jgoodies.common.base.Strings;
/*     */ import com.jgoodies.forms.layout.ConstantSize;
/*     */ import com.jgoodies.forms.layout.Sizes;
/*     */ import com.jgoodies.forms.util.LayoutStyle;
/*     */ import java.awt.Component;
/*     */ import java.awt.Insets;
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
/*     */ 
/*     */ 
/*     */ public final class Paddings
/*     */ {
/*  79 */   public static final EmptyBorder EMPTY = new EmptyBorder(0, 0, 0, 0);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  86 */   public static final Padding DLU2 = createPadding(Sizes.DLUY2, Sizes.DLUX2, Sizes.DLUY2, Sizes.DLUX2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   public static final Padding DLU4 = createPadding(Sizes.DLUY4, Sizes.DLUX4, Sizes.DLUY4, Sizes.DLUX4);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  99 */   public static final Padding DLU7 = createPadding(Sizes.DLUY7, Sizes.DLUX7, Sizes.DLUY7, Sizes.DLUX7);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   public static final Padding DLU9 = createPadding(Sizes.DLUY9, Sizes.DLUX9, Sizes.DLUY9, Sizes.DLUX9);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 113 */   public static final Padding DLU14 = createPadding(Sizes.DLUY14, Sizes.DLUX14, Sizes.DLUY14, Sizes.DLUX14);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 120 */   public static final Padding DLU21 = createPadding(Sizes.DLUY21, Sizes.DLUX21, Sizes.DLUY21, Sizes.DLUX21);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 128 */   public static final Padding BUTTON_BAR_PAD = createPadding(LayoutStyle.getCurrent().getButtonBarPad(), Sizes.dluX(0), Sizes.dluY(0), Sizes.dluX(0));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 141 */   public static final Padding DIALOG = createPadding(LayoutStyle.getCurrent().getDialogMarginY(), LayoutStyle.getCurrent().getDialogMarginX(), LayoutStyle.getCurrent().getDialogMarginY(), LayoutStyle.getCurrent().getDialogMarginX());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 155 */   public static final Padding TABBED_DIALOG = createPadding(LayoutStyle.getCurrent().getTabbedDialogMarginY(), LayoutStyle.getCurrent().getTabbedDialogMarginX(), LayoutStyle.getCurrent().getTabbedDialogMarginY(), LayoutStyle.getCurrent().getTabbedDialogMarginX());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Padding createPadding(ConstantSize top, ConstantSize left, ConstantSize bottom, ConstantSize right) {
/* 182 */     return new Padding(top, left, bottom, right);
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
/*     */   public static Padding createPadding(String encodedSizes, Object... args) {
/* 199 */     String formattedSizes = Strings.get(encodedSizes, args);
/* 200 */     String[] token = formattedSizes.split("\\s*,\\s*");
/* 201 */     int tokenCount = token.length;
/* 202 */     Preconditions.checkArgument((token.length == 4), "The padding requires 4 sizes, but \"%s\" has %d.", new Object[] { formattedSizes, Integer.valueOf(tokenCount) });
/*     */     
/* 204 */     ConstantSize top = Sizes.constant(token[0], false);
/* 205 */     ConstantSize left = Sizes.constant(token[1], true);
/* 206 */     ConstantSize bottom = Sizes.constant(token[2], false);
/* 207 */     ConstantSize right = Sizes.constant(token[3], true);
/* 208 */     return createPadding(top, left, bottom, right);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Padding
/*     */     extends EmptyBorder
/*     */   {
/*     */     private final ConstantSize topMargin;
/*     */ 
/*     */     
/*     */     private final ConstantSize leftMargin;
/*     */ 
/*     */     
/*     */     private final ConstantSize bottomMargin;
/*     */     
/*     */     private final ConstantSize rightMargin;
/*     */ 
/*     */     
/*     */     private Padding(ConstantSize top, ConstantSize left, ConstantSize bottom, ConstantSize right) {
/* 228 */       super(0, 0, 0, 0);
/* 229 */       if (top == null || left == null || bottom == null || right == null)
/*     */       {
/*     */ 
/*     */         
/* 233 */         throw new NullPointerException("The top, left, bottom, and right must not be null.");
/*     */       }
/* 235 */       this.topMargin = top;
/* 236 */       this.leftMargin = left;
/* 237 */       this.bottomMargin = bottom;
/* 238 */       this.rightMargin = right;
/*     */     }
/*     */ 
/*     */     
/*     */     public Insets getBorderInsets() {
/* 243 */       return getBorderInsets(null);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Insets getBorderInsets(Component c) {
/* 249 */       return getBorderInsets(c, new Insets(0, 0, 0, 0));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Insets getBorderInsets(Component c, Insets insets) {
/* 255 */       insets.top = this.topMargin.getPixelSize(c);
/* 256 */       insets.left = this.leftMargin.getPixelSize(c);
/* 257 */       insets.bottom = this.bottomMargin.getPixelSize(c);
/* 258 */       insets.right = this.rightMargin.getPixelSize(c);
/* 259 */       return insets;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\forms\factories\Paddings.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */