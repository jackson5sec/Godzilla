/*     */ package com.jgoodies.forms.util;
/*     */ 
/*     */ import com.jgoodies.forms.layout.ConstantSize;
/*     */ import com.jgoodies.forms.layout.Size;
/*     */ import com.jgoodies.forms.layout.Sizes;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class WindowsLayoutStyle
/*     */   extends LayoutStyle
/*     */ {
/*  46 */   static final WindowsLayoutStyle INSTANCE = new WindowsLayoutStyle();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  55 */   private static final Size BUTTON_WIDTH = (Size)Sizes.dluX(50);
/*  56 */   private static final Size BUTTON_HEIGHT = (Size)Sizes.dluY(14);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   private static final ConstantSize DIALOG_MARGIN_X = Sizes.DLUX7;
/*  62 */   private static final ConstantSize DIALOG_MARGIN_Y = Sizes.DLUY7;
/*     */   
/*  64 */   private static final ConstantSize TABBED_DIALOG_MARGIN_X = Sizes.DLUX4;
/*  65 */   private static final ConstantSize TABBED_DIALOG_MARGIN_Y = Sizes.DLUY4;
/*     */   
/*  67 */   private static final ConstantSize LABEL_COMPONENT_PADX = Sizes.DLUX3;
/*  68 */   private static final ConstantSize RELATED_COMPONENTS_PADX = Sizes.DLUX4;
/*  69 */   private static final ConstantSize UNRELATED_COMPONENTS_PADX = Sizes.DLUX7;
/*     */   
/*  71 */   private static final ConstantSize LABEL_COMPONENT_PADY = Sizes.DLUY2;
/*  72 */   private static final ConstantSize RELATED_COMPONENTS_PADY = Sizes.DLUY4;
/*  73 */   private static final ConstantSize UNRELATED_COMPONENTS_PADY = Sizes.DLUY7;
/*  74 */   private static final ConstantSize NARROW_LINE_PAD = Sizes.DLUY2;
/*  75 */   private static final ConstantSize LINE_PAD = Sizes.DLUY3;
/*  76 */   private static final ConstantSize PARAGRAPH_PAD = Sizes.DLUY9;
/*  77 */   private static final ConstantSize BUTTON_BAR_PAD = Sizes.DLUY5;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Size getDefaultButtonWidth() {
/*  84 */     return BUTTON_WIDTH;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Size getDefaultButtonHeight() {
/*  90 */     return BUTTON_HEIGHT;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstantSize getDialogMarginX() {
/*  96 */     return DIALOG_MARGIN_X;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstantSize getDialogMarginY() {
/* 102 */     return DIALOG_MARGIN_Y;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstantSize getTabbedDialogMarginX() {
/* 108 */     return TABBED_DIALOG_MARGIN_X;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstantSize getTabbedDialogMarginY() {
/* 114 */     return TABBED_DIALOG_MARGIN_Y;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstantSize getLabelComponentPadX() {
/* 120 */     return LABEL_COMPONENT_PADX;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstantSize getLabelComponentPadY() {
/* 126 */     return LABEL_COMPONENT_PADY;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstantSize getRelatedComponentsPadX() {
/* 132 */     return RELATED_COMPONENTS_PADX;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstantSize getRelatedComponentsPadY() {
/* 138 */     return RELATED_COMPONENTS_PADY;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstantSize getUnrelatedComponentsPadX() {
/* 144 */     return UNRELATED_COMPONENTS_PADX;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstantSize getUnrelatedComponentsPadY() {
/* 150 */     return UNRELATED_COMPONENTS_PADY;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstantSize getNarrowLinePad() {
/* 156 */     return NARROW_LINE_PAD;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstantSize getLinePad() {
/* 162 */     return LINE_PAD;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstantSize getParagraphPad() {
/* 168 */     return PARAGRAPH_PAD;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstantSize getButtonBarPad() {
/* 174 */     return BUTTON_BAR_PAD;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\form\\util\WindowsLayoutStyle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */