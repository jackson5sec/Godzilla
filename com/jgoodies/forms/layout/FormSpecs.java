/*     */ package com.jgoodies.forms.layout;
/*     */ 
/*     */ import com.jgoodies.forms.util.LayoutStyle;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FormSpecs
/*     */ {
/*  62 */   public static final ColumnSpec MIN_COLSPEC = new ColumnSpec(Sizes.MINIMUM);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   public static final ColumnSpec PREF_COLSPEC = new ColumnSpec(Sizes.PREFERRED);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  88 */   public static final ColumnSpec DEFAULT_COLSPEC = new ColumnSpec(Sizes.DEFAULT);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  99 */   public static final ColumnSpec GLUE_COLSPEC = new ColumnSpec(ColumnSpec.DEFAULT, Sizes.ZERO, 1.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 115 */   public static final ColumnSpec LABEL_COMPONENT_GAP_COLSPEC = ColumnSpec.createGap(LayoutStyle.getCurrent().getLabelComponentPadX());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 129 */   public static final ColumnSpec RELATED_GAP_COLSPEC = ColumnSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadX());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 141 */   public static final ColumnSpec UNRELATED_GAP_COLSPEC = ColumnSpec.createGap(LayoutStyle.getCurrent().getUnrelatedComponentsPadX());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 154 */   public static final ColumnSpec BUTTON_COLSPEC = new ColumnSpec(Sizes.bounded(Sizes.PREFERRED, LayoutStyle.getCurrent().getDefaultButtonWidth(), null));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 169 */   public static final ColumnSpec GROWING_BUTTON_COLSPEC = new ColumnSpec(ColumnSpec.DEFAULT, BUTTON_COLSPEC.getSize(), 1.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 184 */   public static final RowSpec MIN_ROWSPEC = new RowSpec(Sizes.MINIMUM);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 195 */   public static final RowSpec PREF_ROWSPEC = new RowSpec(Sizes.PREFERRED);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 210 */   public static final RowSpec DEFAULT_ROWSPEC = new RowSpec(Sizes.DEFAULT);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 221 */   public static final RowSpec GLUE_ROWSPEC = new RowSpec(RowSpec.DEFAULT, Sizes.ZERO, 1.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 237 */   public static final RowSpec LABEL_COMPONENT_GAP_ROWSPEC = RowSpec.createGap(LayoutStyle.getCurrent().getLabelComponentPadY());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 251 */   public static final RowSpec RELATED_GAP_ROWSPEC = RowSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadY());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 263 */   public static final RowSpec UNRELATED_GAP_ROWSPEC = RowSpec.createGap(LayoutStyle.getCurrent().getUnrelatedComponentsPadY());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 278 */   public static final RowSpec NARROW_LINE_GAP_ROWSPEC = RowSpec.createGap(LayoutStyle.getCurrent().getNarrowLinePad());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 292 */   public static final RowSpec LINE_GAP_ROWSPEC = RowSpec.createGap(LayoutStyle.getCurrent().getLinePad());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 306 */   public static final RowSpec PARAGRAPH_GAP_ROWSPEC = RowSpec.createGap(LayoutStyle.getCurrent().getParagraphPad());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 319 */   public static final RowSpec BUTTON_ROWSPEC = new RowSpec(Sizes.bounded(Sizes.PREFERRED, LayoutStyle.getCurrent().getDefaultButtonHeight(), null));
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\forms\layout\FormSpecs.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */