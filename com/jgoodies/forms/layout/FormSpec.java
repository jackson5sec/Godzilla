/*     */ package com.jgoodies.forms.layout;
/*     */ 
/*     */ import com.jgoodies.common.base.Preconditions;
/*     */ import java.awt.Container;
/*     */ import java.io.Serializable;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class FormSpec
/*     */   implements Serializable
/*     */ {
/*  71 */   static final DefaultAlignment LEFT_ALIGN = new DefaultAlignment("left");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   static final DefaultAlignment RIGHT_ALIGN = new DefaultAlignment("right");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   static final DefaultAlignment TOP_ALIGN = new DefaultAlignment("top");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  86 */   static final DefaultAlignment BOTTOM_ALIGN = new DefaultAlignment("bottom");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  91 */   static final DefaultAlignment CENTER_ALIGN = new DefaultAlignment("center");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  96 */   static final DefaultAlignment FILL_ALIGN = new DefaultAlignment("fill");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 102 */   static final DefaultAlignment NO_ALIGN = new DefaultAlignment("none");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   private static final DefaultAlignment[] VALUES = new DefaultAlignment[] { LEFT_ALIGN, RIGHT_ALIGN, TOP_ALIGN, BOTTOM_ALIGN, CENTER_ALIGN, FILL_ALIGN, NO_ALIGN };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final double NO_GROW = 0.0D;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final double DEFAULT_GROW = 1.0D;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 127 */   private static final Pattern TOKEN_SEPARATOR_PATTERN = Pattern.compile(":");
/*     */ 
/*     */   
/* 130 */   private static final Pattern BOUNDS_SEPARATOR_PATTERN = Pattern.compile("\\s*,\\s*");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DefaultAlignment defaultAlignment;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean defaultAlignmentExplicitlySet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Size size;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private double resizeWeight;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FormSpec(DefaultAlignment defaultAlignment, Size size, double resizeWeight) {
/* 179 */     Preconditions.checkNotNull(size, "The size must not be null.");
/* 180 */     Preconditions.checkArgument((resizeWeight >= 0.0D), "The resize weight must be non-negative.");
/* 181 */     this.defaultAlignment = defaultAlignment;
/* 182 */     this.size = size;
/* 183 */     this.resizeWeight = resizeWeight;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FormSpec(DefaultAlignment defaultAlignment, String encodedDescription) {
/* 194 */     this(defaultAlignment, Sizes.DEFAULT, 0.0D);
/* 195 */     parseAndInitValues(encodedDescription.toLowerCase(Locale.ENGLISH));
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
/*     */   public final DefaultAlignment getDefaultAlignment() {
/* 207 */     return this.defaultAlignment;
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
/*     */   public final boolean getDefaultAlignmentExplictlySet() {
/* 219 */     return this.defaultAlignmentExplicitlySet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Size getSize() {
/* 229 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final double getResizeWeight() {
/* 238 */     return this.resizeWeight;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final boolean canGrow() {
/* 249 */     return (getResizeWeight() != 0.0D);
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
/*     */   void setDefaultAlignment(DefaultAlignment defaultAlignment) {
/* 267 */     this.defaultAlignment = defaultAlignment;
/* 268 */     this.defaultAlignmentExplicitlySet = true;
/*     */   }
/*     */ 
/*     */   
/*     */   void setSize(Size size) {
/* 273 */     this.size = size;
/*     */   }
/*     */ 
/*     */   
/*     */   void setResizeWeight(double resizeWeight) {
/* 278 */     this.resizeWeight = resizeWeight;
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
/*     */   private void parseAndInitValues(String encodedDescription) {
/* 295 */     Preconditions.checkNotBlank(encodedDescription, "The encoded form specification must not be null, empty or whitespace.");
/*     */     
/* 297 */     String[] token = TOKEN_SEPARATOR_PATTERN.split(encodedDescription);
/* 298 */     Preconditions.checkArgument((token.length > 0), "The form spec must not be empty.");
/* 299 */     int nextIndex = 0;
/* 300 */     String next = token[nextIndex++];
/*     */ 
/*     */     
/* 303 */     DefaultAlignment alignment = DefaultAlignment.valueOf(next, isHorizontal());
/* 304 */     if (alignment != null) {
/* 305 */       setDefaultAlignment(alignment);
/* 306 */       Preconditions.checkArgument((token.length > 1), "The form spec must provide a size.");
/* 307 */       next = token[nextIndex++];
/*     */     } 
/* 309 */     setSize(parseSize(next));
/* 310 */     if (nextIndex < token.length) {
/* 311 */       setResizeWeight(parseResizeWeight(token[nextIndex]));
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
/*     */   private Size parseSize(String token) {
/* 323 */     if (token.startsWith("[") && token.endsWith("]")) {
/* 324 */       return parseBoundedSize(token);
/*     */     }
/* 326 */     if (token.startsWith("max(") && token.endsWith(")")) {
/* 327 */       return parseOldBoundedSize(token, false);
/*     */     }
/* 329 */     if (token.startsWith("min(") && token.endsWith(")")) {
/* 330 */       return parseOldBoundedSize(token, true);
/*     */     }
/* 332 */     return parseAtomicSize(token);
/*     */   }
/*     */ 
/*     */   
/*     */   private Size parseBoundedSize(String token) {
/* 337 */     String content = token.substring(1, token.length() - 1);
/* 338 */     String[] subtoken = BOUNDS_SEPARATOR_PATTERN.split(content);
/* 339 */     Size basis = null;
/* 340 */     Size lower = null;
/* 341 */     Size upper = null;
/* 342 */     if (subtoken.length == 2) {
/* 343 */       Size size1 = parseAtomicSize(subtoken[0]);
/* 344 */       Size size2 = parseAtomicSize(subtoken[1]);
/* 345 */       if (isConstant(size1)) {
/* 346 */         if (isConstant(size2)) {
/* 347 */           lower = size1;
/* 348 */           basis = size2;
/* 349 */           upper = size2;
/*     */         } else {
/* 351 */           lower = size1;
/* 352 */           basis = size2;
/*     */         } 
/*     */       } else {
/* 355 */         basis = size1;
/* 356 */         upper = size2;
/*     */       } 
/* 358 */     } else if (subtoken.length == 3) {
/* 359 */       lower = parseAtomicSize(subtoken[0]);
/* 360 */       basis = parseAtomicSize(subtoken[1]);
/* 361 */       upper = parseAtomicSize(subtoken[2]);
/*     */     } 
/* 363 */     if ((lower == null || isConstant(lower)) && (upper == null || isConstant(upper)))
/*     */     {
/* 365 */       return new BoundedSize(basis, lower, upper);
/*     */     }
/* 367 */     throw new IllegalArgumentException("Illegal bounded size '" + token + "'. Must be one of:" + "\n[<constant size>,<logical size>]                 // lower bound" + "\n[<logical size>,<constant size>]                 // upper bound" + "\n[<constant size>,<logical size>,<constant size>] // lower and upper bound." + "\nExamples:" + "\n[50dlu,pref]                                     // lower bound" + "\n[pref,200dlu]                                    // upper bound" + "\n[50dlu,pref,200dlu]                              // lower and upper bound.");
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
/*     */ 
/*     */   
/*     */   private Size parseOldBoundedSize(String token, boolean setMax) {
/* 392 */     int semicolonIndex = token.indexOf(';');
/* 393 */     String sizeToken1 = token.substring(4, semicolonIndex);
/* 394 */     String sizeToken2 = token.substring(semicolonIndex + 1, token.length() - 1);
/*     */     
/* 396 */     Size size1 = parseAtomicSize(sizeToken1);
/* 397 */     Size size2 = parseAtomicSize(sizeToken2);
/*     */ 
/*     */     
/* 400 */     if (isConstant(size1)) {
/* 401 */       if (size2 instanceof Sizes.ComponentSize) {
/* 402 */         return new BoundedSize(size2, setMax ? null : size1, setMax ? size1 : null);
/*     */       }
/*     */       
/* 405 */       throw new IllegalArgumentException("Bounded sizes must not be both constants.");
/*     */     } 
/*     */     
/* 408 */     if (isConstant(size2)) {
/* 409 */       return new BoundedSize(size1, setMax ? null : size2, setMax ? size2 : null);
/*     */     }
/*     */     
/* 412 */     throw new IllegalArgumentException("Bounded sizes must not be both logical.");
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
/*     */   private Size parseAtomicSize(String token) {
/* 425 */     String trimmedToken = token.trim();
/* 426 */     if (trimmedToken.startsWith("'") && trimmedToken.endsWith("'")) {
/* 427 */       int length = trimmedToken.length();
/* 428 */       if (length < 2) {
/* 429 */         throw new IllegalArgumentException("Missing closing \"'\" for prototype.");
/*     */       }
/* 431 */       return new PrototypeSize(trimmedToken.substring(1, length - 1));
/*     */     } 
/* 433 */     Sizes.ComponentSize componentSize = Sizes.ComponentSize.valueOf(trimmedToken);
/* 434 */     if (componentSize != null) {
/* 435 */       return componentSize;
/*     */     }
/* 437 */     return ConstantSize.valueOf(trimmedToken, isHorizontal());
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
/*     */   private static double parseResizeWeight(String token) {
/* 451 */     if (token.equals("g") || token.equals("grow")) {
/* 452 */       return 1.0D;
/*     */     }
/* 454 */     if (token.equals("n") || token.equals("nogrow") || token.equals("none")) {
/* 455 */       return 0.0D;
/*     */     }
/*     */     
/* 458 */     if ((token.startsWith("grow(") || token.startsWith("g(")) && token.endsWith(")")) {
/*     */       
/* 460 */       int leftParen = token.indexOf('(');
/* 461 */       int rightParen = token.indexOf(')');
/* 462 */       String substring = token.substring(leftParen + 1, rightParen);
/* 463 */       return Double.parseDouble(substring);
/*     */     } 
/* 465 */     throw new IllegalArgumentException("The resize argument '" + token + "' is invalid. " + " Must be one of: grow, g, none, n, grow(<double>), g(<double>)");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isConstant(Size aSize) {
/* 472 */     return (aSize instanceof ConstantSize || aSize instanceof PrototypeSize);
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
/*     */   
/*     */   public final String toString() {
/* 496 */     StringBuffer buffer = new StringBuffer();
/* 497 */     buffer.append(this.defaultAlignment);
/*     */     
/* 499 */     buffer.append(":");
/* 500 */     buffer.append(this.size.toString());
/* 501 */     buffer.append(':');
/* 502 */     if (this.resizeWeight == 0.0D) {
/* 503 */       buffer.append("noGrow");
/* 504 */     } else if (this.resizeWeight == 1.0D) {
/* 505 */       buffer.append("grow");
/*     */     } else {
/* 507 */       buffer.append("grow(");
/* 508 */       buffer.append(this.resizeWeight);
/* 509 */       buffer.append(')');
/*     */     } 
/* 511 */     return buffer.toString();
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
/*     */   public final String toShortString() {
/* 531 */     StringBuffer buffer = new StringBuffer();
/* 532 */     buffer.append(this.defaultAlignment.abbreviation());
/*     */     
/* 534 */     buffer.append(":");
/* 535 */     buffer.append(this.size.toString());
/* 536 */     buffer.append(':');
/* 537 */     if (this.resizeWeight == 0.0D) {
/* 538 */       buffer.append("n");
/* 539 */     } else if (this.resizeWeight == 1.0D) {
/* 540 */       buffer.append("g");
/*     */     } else {
/* 542 */       buffer.append("g(");
/* 543 */       buffer.append(this.resizeWeight);
/* 544 */       buffer.append(')');
/*     */     } 
/* 546 */     return buffer.toString();
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
/*     */   public final String encode() {
/* 562 */     StringBuffer buffer = new StringBuffer();
/* 563 */     DefaultAlignment alignmentDefault = isHorizontal() ? ColumnSpec.DEFAULT : RowSpec.DEFAULT;
/*     */ 
/*     */     
/* 566 */     if (!alignmentDefault.equals(this.defaultAlignment)) {
/* 567 */       buffer.append(this.defaultAlignment.abbreviation());
/* 568 */       buffer.append(":");
/*     */     } 
/* 570 */     buffer.append(this.size.encode());
/* 571 */     if (this.resizeWeight != 0.0D)
/*     */     {
/* 573 */       if (this.resizeWeight == 1.0D) {
/* 574 */         buffer.append(':');
/* 575 */         buffer.append("g");
/*     */       } else {
/* 577 */         buffer.append(':');
/* 578 */         buffer.append("g(");
/* 579 */         buffer.append(this.resizeWeight);
/* 580 */         buffer.append(')');
/*     */       }  } 
/* 582 */     return buffer.toString();
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
/*     */   
/*     */   final int maximumSize(Container container, List components, FormLayout.Measure minMeasure, FormLayout.Measure prefMeasure, FormLayout.Measure defaultMeasure) {
/* 606 */     return this.size.maximumSize(container, components, minMeasure, prefMeasure, defaultMeasure);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract boolean isHorizontal();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class DefaultAlignment
/*     */     implements Serializable
/*     */   {
/*     */     private final transient String name;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private DefaultAlignment(String name) {
/* 685 */       this.ordinal = nextOrdinal++;
/*     */       this.name = name;
/*     */     } private Object readResolve() {
/* 688 */       return FormSpec.VALUES[this.ordinal];
/*     */     }
/*     */     
/*     */     private static DefaultAlignment valueOf(String str, boolean isHorizontal) {
/*     */       if (str.equals("f") || str.equals("fill"))
/*     */         return FormSpec.FILL_ALIGN; 
/*     */       if (str.equals("c") || str.equals("center"))
/*     */         return FormSpec.CENTER_ALIGN; 
/*     */       if (isHorizontal) {
/*     */         if (str.equals("r") || str.equals("right"))
/*     */           return FormSpec.RIGHT_ALIGN; 
/*     */         if (str.equals("l") || str.equals("left"))
/*     */           return FormSpec.LEFT_ALIGN; 
/*     */         if (str.equals("none"))
/*     */           return FormSpec.NO_ALIGN; 
/*     */         return null;
/*     */       } 
/*     */       if (str.equals("t") || str.equals("top"))
/*     */         return FormSpec.TOP_ALIGN; 
/*     */       if (str.equals("b") || str.equals("bottom"))
/*     */         return FormSpec.BOTTOM_ALIGN; 
/*     */       return null;
/*     */     }
/*     */     
/*     */     public String toString() {
/*     */       return this.name;
/*     */     }
/*     */     
/*     */     public char abbreviation() {
/*     */       return this.name.charAt(0);
/*     */     }
/*     */     
/*     */     private static int nextOrdinal = 0;
/*     */     private final int ordinal;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\forms\layout\FormSpec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */