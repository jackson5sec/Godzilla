/*     */ package com.jgoodies.forms.layout;
/*     */ 
/*     */ import com.jgoodies.common.base.Preconditions;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ColumnSpec
/*     */   extends FormSpec
/*     */ {
/*  74 */   public static final FormSpec.DefaultAlignment LEFT = FormSpec.LEFT_ALIGN;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  79 */   public static final FormSpec.DefaultAlignment CENTER = FormSpec.CENTER_ALIGN;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  84 */   public static final FormSpec.DefaultAlignment RIGHT = FormSpec.RIGHT_ALIGN;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  89 */   public static final FormSpec.DefaultAlignment FILL = FormSpec.FILL_ALIGN;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  98 */   public static final FormSpec.DefaultAlignment NONE = FormSpec.NO_ALIGN;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 103 */   public static final FormSpec.DefaultAlignment DEFAULT = FILL;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 111 */   private static final Map<String, ColumnSpec> CACHE = new HashMap<String, ColumnSpec>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ColumnSpec(FormSpec.DefaultAlignment defaultAlignment, Size size, double resizeWeight) {
/* 135 */     super(defaultAlignment, size, resizeWeight);
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
/*     */   public ColumnSpec(Size size) {
/* 147 */     super(DEFAULT, size, 0.0D);
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
/*     */   private ColumnSpec(String encodedDescription) {
/* 161 */     super(DEFAULT, encodedDescription);
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
/*     */   public static ColumnSpec createGap(ConstantSize gapWidth) {
/* 179 */     return new ColumnSpec(DEFAULT, gapWidth, 0.0D);
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
/*     */   public static ColumnSpec decode(String encodedColumnSpec) {
/* 199 */     return decode(encodedColumnSpec, LayoutMap.getRoot());
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
/*     */   public static ColumnSpec decode(String encodedColumnSpec, LayoutMap layoutMap) {
/* 222 */     Preconditions.checkNotBlank(encodedColumnSpec, "The encoded column specification must not be null, empty or whitespace.");
/*     */     
/* 224 */     Preconditions.checkNotNull(layoutMap, "The LayoutMap must not be null.");
/* 225 */     String trimmed = encodedColumnSpec.trim();
/* 226 */     String lower = trimmed.toLowerCase(Locale.ENGLISH);
/* 227 */     return decodeExpanded(layoutMap.expand(lower, true));
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
/*     */   static ColumnSpec decodeExpanded(String expandedTrimmedLowerCaseSpec) {
/* 241 */     ColumnSpec spec = CACHE.get(expandedTrimmedLowerCaseSpec);
/* 242 */     if (spec == null) {
/* 243 */       spec = new ColumnSpec(expandedTrimmedLowerCaseSpec);
/* 244 */       CACHE.put(expandedTrimmedLowerCaseSpec, spec);
/*     */     } 
/* 246 */     return spec;
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
/*     */   public static ColumnSpec[] decodeSpecs(String encodedColumnSpecs) {
/* 263 */     return decodeSpecs(encodedColumnSpecs, LayoutMap.getRoot());
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
/*     */   public static ColumnSpec[] decodeSpecs(String encodedColumnSpecs, LayoutMap layoutMap) {
/* 283 */     return FormSpecParser.parseColumnSpecs(encodedColumnSpecs, layoutMap);
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
/*     */   protected boolean isHorizontal() {
/* 298 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\forms\layout\ColumnSpec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */