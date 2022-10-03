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
/*     */ public final class RowSpec
/*     */   extends FormSpec
/*     */ {
/*  73 */   public static final FormSpec.DefaultAlignment TOP = FormSpec.TOP_ALIGN;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   public static final FormSpec.DefaultAlignment CENTER = FormSpec.CENTER_ALIGN;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   public static final FormSpec.DefaultAlignment BOTTOM = FormSpec.BOTTOM_ALIGN;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  88 */   public static final FormSpec.DefaultAlignment FILL = FormSpec.FILL_ALIGN;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  93 */   public static final FormSpec.DefaultAlignment DEFAULT = CENTER;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 101 */   private static final Map<String, RowSpec> CACHE = new HashMap<String, RowSpec>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RowSpec(FormSpec.DefaultAlignment defaultAlignment, Size size, double resizeWeight) {
/* 123 */     super(defaultAlignment, size, resizeWeight);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RowSpec(Size size) {
/* 134 */     super(DEFAULT, size, 0.0D);
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
/*     */   private RowSpec(String encodedDescription) {
/* 147 */     super(DEFAULT, encodedDescription);
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
/*     */   public static RowSpec createGap(ConstantSize gapHeight) {
/* 165 */     return new RowSpec(DEFAULT, gapHeight, 0.0D);
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
/*     */   public static RowSpec decode(String encodedRowSpec) {
/* 185 */     return decode(encodedRowSpec, LayoutMap.getRoot());
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
/*     */   public static RowSpec decode(String encodedRowSpec, LayoutMap layoutMap) {
/* 206 */     Preconditions.checkNotBlank(encodedRowSpec, "The encoded row specification must not be null, empty or whitespace.");
/*     */     
/* 208 */     Preconditions.checkNotNull(layoutMap, "The LayoutMap must not be null.");
/*     */     
/* 210 */     String trimmed = encodedRowSpec.trim();
/* 211 */     String lower = trimmed.toLowerCase(Locale.ENGLISH);
/* 212 */     return decodeExpanded(layoutMap.expand(lower, false));
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
/*     */   static RowSpec decodeExpanded(String expandedTrimmedLowerCaseSpec) {
/* 226 */     RowSpec spec = CACHE.get(expandedTrimmedLowerCaseSpec);
/* 227 */     if (spec == null) {
/* 228 */       spec = new RowSpec(expandedTrimmedLowerCaseSpec);
/* 229 */       CACHE.put(expandedTrimmedLowerCaseSpec, spec);
/*     */     } 
/* 231 */     return spec;
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
/*     */   public static RowSpec[] decodeSpecs(String encodedRowSpecs) {
/* 248 */     return decodeSpecs(encodedRowSpecs, LayoutMap.getRoot());
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
/*     */   public static RowSpec[] decodeSpecs(String encodedRowSpecs, LayoutMap layoutMap) {
/* 268 */     return FormSpecParser.parseRowSpecs(encodedRowSpecs, layoutMap);
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
/*     */   protected boolean isHorizontal() {
/* 282 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\forms\layout\RowSpec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */