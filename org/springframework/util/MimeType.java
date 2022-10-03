/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.BitSet;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.TreeSet;
/*     */ import org.springframework.lang.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MimeType
/*     */   implements Comparable<MimeType>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 4085923477777865903L;
/*     */   protected static final String WILDCARD_TYPE = "*";
/*     */   private static final String PARAM_CHARSET = "charset";
/*     */   
/*     */   static {
/*  68 */     BitSet ctl = new BitSet(128);
/*  69 */     for (int i = 0; i <= 31; i++) {
/*  70 */       ctl.set(i);
/*     */     }
/*  72 */     ctl.set(127);
/*     */     
/*  74 */     BitSet separators = new BitSet(128);
/*  75 */     separators.set(40);
/*  76 */     separators.set(41);
/*  77 */     separators.set(60);
/*  78 */     separators.set(62);
/*  79 */     separators.set(64);
/*  80 */     separators.set(44);
/*  81 */     separators.set(59);
/*  82 */     separators.set(58);
/*  83 */     separators.set(92);
/*  84 */     separators.set(34);
/*  85 */     separators.set(47);
/*  86 */     separators.set(91);
/*  87 */     separators.set(93);
/*  88 */     separators.set(63);
/*  89 */     separators.set(61);
/*  90 */     separators.set(123);
/*  91 */     separators.set(125);
/*  92 */     separators.set(32);
/*  93 */     separators.set(9);
/*     */   }
/*  95 */   private static final BitSet TOKEN = new BitSet(128); static {
/*  96 */     TOKEN.set(0, 128);
/*  97 */     TOKEN.andNot(ctl);
/*  98 */     TOKEN.andNot(separators);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String type;
/*     */ 
/*     */   
/*     */   private final String subtype;
/*     */ 
/*     */   
/*     */   private final Map<String, String> parameters;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private transient Charset resolvedCharset;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private volatile String toStringValue;
/*     */ 
/*     */ 
/*     */   
/*     */   public MimeType(String type) {
/* 123 */     this(type, "*");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MimeType(String type, String subtype) {
/* 134 */     this(type, subtype, Collections.emptyMap());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MimeType(String type, String subtype, Charset charset) {
/* 145 */     this(type, subtype, Collections.singletonMap("charset", charset.name()));
/* 146 */     this.resolvedCharset = charset;
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
/*     */   public MimeType(MimeType other, Charset charset) {
/* 158 */     this(other.getType(), other.getSubtype(), addCharsetParameter(charset, other.getParameters()));
/* 159 */     this.resolvedCharset = charset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MimeType(MimeType other, @Nullable Map<String, String> parameters) {
/* 170 */     this(other.getType(), other.getSubtype(), parameters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MimeType(String type, String subtype, @Nullable Map<String, String> parameters) {
/* 181 */     Assert.hasLength(type, "'type' must not be empty");
/* 182 */     Assert.hasLength(subtype, "'subtype' must not be empty");
/* 183 */     checkToken(type);
/* 184 */     checkToken(subtype);
/* 185 */     this.type = type.toLowerCase(Locale.ENGLISH);
/* 186 */     this.subtype = subtype.toLowerCase(Locale.ENGLISH);
/* 187 */     if (!CollectionUtils.isEmpty(parameters)) {
/* 188 */       Map<String, String> map = new LinkedCaseInsensitiveMap<>(parameters.size(), Locale.ENGLISH);
/* 189 */       parameters.forEach((parameter, value) -> {
/*     */             checkParameters(parameter, value);
/*     */             map.put(parameter, value);
/*     */           });
/* 193 */       this.parameters = Collections.unmodifiableMap(map);
/*     */     } else {
/*     */       
/* 196 */       this.parameters = Collections.emptyMap();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MimeType(MimeType other) {
/* 207 */     this.type = other.type;
/* 208 */     this.subtype = other.subtype;
/* 209 */     this.parameters = other.parameters;
/* 210 */     this.resolvedCharset = other.resolvedCharset;
/* 211 */     this.toStringValue = other.toStringValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkToken(String token) {
/* 221 */     for (int i = 0; i < token.length(); i++) {
/* 222 */       char ch = token.charAt(i);
/* 223 */       if (!TOKEN.get(ch)) {
/* 224 */         throw new IllegalArgumentException("Invalid token character '" + ch + "' in token \"" + token + "\"");
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void checkParameters(String parameter, String value) {
/* 230 */     Assert.hasLength(parameter, "'parameter' must not be empty");
/* 231 */     Assert.hasLength(value, "'value' must not be empty");
/* 232 */     checkToken(parameter);
/* 233 */     if ("charset".equals(parameter)) {
/* 234 */       if (this.resolvedCharset == null) {
/* 235 */         this.resolvedCharset = Charset.forName(unquote(value));
/*     */       }
/*     */     }
/* 238 */     else if (!isQuotedString(value)) {
/* 239 */       checkToken(value);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isQuotedString(String s) {
/* 244 */     if (s.length() < 2) {
/* 245 */       return false;
/*     */     }
/*     */     
/* 248 */     return ((s.startsWith("\"") && s.endsWith("\"")) || (s.startsWith("'") && s.endsWith("'")));
/*     */   }
/*     */ 
/*     */   
/*     */   protected String unquote(String s) {
/* 253 */     return isQuotedString(s) ? s.substring(1, s.length() - 1) : s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWildcardType() {
/* 261 */     return "*".equals(getType());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWildcardSubtype() {
/* 271 */     return ("*".equals(getSubtype()) || getSubtype().startsWith("*+"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConcrete() {
/* 280 */     return (!isWildcardType() && !isWildcardSubtype());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType() {
/* 287 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSubtype() {
/* 294 */     return this.subtype;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getSubtypeSuffix() {
/* 303 */     int suffixIndex = this.subtype.lastIndexOf('+');
/* 304 */     if (suffixIndex != -1 && this.subtype.length() > suffixIndex) {
/* 305 */       return this.subtype.substring(suffixIndex + 1);
/*     */     }
/* 307 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Charset getCharset() {
/* 317 */     return this.resolvedCharset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getParameter(String name) {
/* 327 */     return this.parameters.get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> getParameters() {
/* 335 */     return this.parameters;
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
/*     */   public boolean includes(@Nullable MimeType other) {
/* 348 */     if (other == null) {
/* 349 */       return false;
/*     */     }
/* 351 */     if (isWildcardType())
/*     */     {
/* 353 */       return true;
/*     */     }
/* 355 */     if (getType().equals(other.getType())) {
/* 356 */       if (getSubtype().equals(other.getSubtype())) {
/* 357 */         return true;
/*     */       }
/* 359 */       if (isWildcardSubtype()) {
/*     */         
/* 361 */         int thisPlusIdx = getSubtype().lastIndexOf('+');
/* 362 */         if (thisPlusIdx == -1) {
/* 363 */           return true;
/*     */         }
/*     */ 
/*     */         
/* 367 */         int otherPlusIdx = other.getSubtype().lastIndexOf('+');
/* 368 */         if (otherPlusIdx != -1) {
/* 369 */           String thisSubtypeNoSuffix = getSubtype().substring(0, thisPlusIdx);
/* 370 */           String thisSubtypeSuffix = getSubtype().substring(thisPlusIdx + 1);
/* 371 */           String otherSubtypeSuffix = other.getSubtype().substring(otherPlusIdx + 1);
/* 372 */           if (thisSubtypeSuffix.equals(otherSubtypeSuffix) && "*".equals(thisSubtypeNoSuffix)) {
/* 373 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 379 */     return false;
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
/*     */   public boolean isCompatibleWith(@Nullable MimeType other) {
/* 392 */     if (other == null) {
/* 393 */       return false;
/*     */     }
/* 395 */     if (isWildcardType() || other.isWildcardType()) {
/* 396 */       return true;
/*     */     }
/* 398 */     if (getType().equals(other.getType())) {
/* 399 */       if (getSubtype().equals(other.getSubtype())) {
/* 400 */         return true;
/*     */       }
/* 402 */       if (isWildcardSubtype() || other.isWildcardSubtype()) {
/* 403 */         String thisSuffix = getSubtypeSuffix();
/* 404 */         String otherSuffix = other.getSubtypeSuffix();
/* 405 */         if (getSubtype().equals("*") || other.getSubtype().equals("*")) {
/* 406 */           return true;
/*     */         }
/* 408 */         if (isWildcardSubtype() && thisSuffix != null) {
/* 409 */           return (thisSuffix.equals(other.getSubtype()) || thisSuffix.equals(otherSuffix));
/*     */         }
/* 411 */         if (other.isWildcardSubtype() && otherSuffix != null) {
/* 412 */           return (getSubtype().equals(otherSuffix) || otherSuffix.equals(thisSuffix));
/*     */         }
/*     */       } 
/*     */     } 
/* 416 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equalsTypeAndSubtype(@Nullable MimeType other) {
/* 427 */     if (other == null) {
/* 428 */       return false;
/*     */     }
/* 430 */     return (this.type.equalsIgnoreCase(other.type) && this.subtype.equalsIgnoreCase(other.subtype));
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
/*     */   public boolean isPresentIn(Collection<? extends MimeType> mimeTypes) {
/* 442 */     for (MimeType mimeType : mimeTypes) {
/* 443 */       if (mimeType.equalsTypeAndSubtype(this)) {
/* 444 */         return true;
/*     */       }
/*     */     } 
/* 447 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/* 453 */     if (this == other) {
/* 454 */       return true;
/*     */     }
/* 456 */     if (!(other instanceof MimeType)) {
/* 457 */       return false;
/*     */     }
/* 459 */     MimeType otherType = (MimeType)other;
/* 460 */     return (this.type.equalsIgnoreCase(otherType.type) && this.subtype
/* 461 */       .equalsIgnoreCase(otherType.subtype) && 
/* 462 */       parametersAreEqual(otherType));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean parametersAreEqual(MimeType other) {
/* 472 */     if (this.parameters.size() != other.parameters.size()) {
/* 473 */       return false;
/*     */     }
/*     */     
/* 476 */     for (Map.Entry<String, String> entry : this.parameters.entrySet()) {
/* 477 */       String key = entry.getKey();
/* 478 */       if (!other.parameters.containsKey(key)) {
/* 479 */         return false;
/*     */       }
/* 481 */       if ("charset".equals(key)) {
/* 482 */         if (!ObjectUtils.nullSafeEquals(getCharset(), other.getCharset()))
/* 483 */           return false; 
/*     */         continue;
/*     */       } 
/* 486 */       if (!ObjectUtils.nullSafeEquals(entry.getValue(), other.parameters.get(key))) {
/* 487 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 491 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 496 */     int result = this.type.hashCode();
/* 497 */     result = 31 * result + this.subtype.hashCode();
/* 498 */     result = 31 * result + this.parameters.hashCode();
/* 499 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 504 */     String value = this.toStringValue;
/* 505 */     if (value == null) {
/* 506 */       StringBuilder builder = new StringBuilder();
/* 507 */       appendTo(builder);
/* 508 */       value = builder.toString();
/* 509 */       this.toStringValue = value;
/*     */     } 
/* 511 */     return value;
/*     */   }
/*     */   
/*     */   protected void appendTo(StringBuilder builder) {
/* 515 */     builder.append(this.type);
/* 516 */     builder.append('/');
/* 517 */     builder.append(this.subtype);
/* 518 */     appendTo(this.parameters, builder);
/*     */   }
/*     */   
/*     */   private void appendTo(Map<String, String> map, StringBuilder builder) {
/* 522 */     map.forEach((key, val) -> {
/*     */           builder.append(';');
/*     */           builder.append(key);
/*     */           builder.append('=');
/*     */           builder.append(val);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(MimeType other) {
/* 537 */     int comp = getType().compareToIgnoreCase(other.getType());
/* 538 */     if (comp != 0) {
/* 539 */       return comp;
/*     */     }
/* 541 */     comp = getSubtype().compareToIgnoreCase(other.getSubtype());
/* 542 */     if (comp != 0) {
/* 543 */       return comp;
/*     */     }
/* 545 */     comp = getParameters().size() - other.getParameters().size();
/* 546 */     if (comp != 0) {
/* 547 */       return comp;
/*     */     }
/*     */     
/* 550 */     TreeSet<String> thisAttributes = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
/* 551 */     thisAttributes.addAll(getParameters().keySet());
/* 552 */     TreeSet<String> otherAttributes = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
/* 553 */     otherAttributes.addAll(other.getParameters().keySet());
/* 554 */     Iterator<String> thisAttributesIterator = thisAttributes.iterator();
/* 555 */     Iterator<String> otherAttributesIterator = otherAttributes.iterator();
/*     */     
/* 557 */     while (thisAttributesIterator.hasNext()) {
/* 558 */       String thisAttribute = thisAttributesIterator.next();
/* 559 */       String otherAttribute = otherAttributesIterator.next();
/* 560 */       comp = thisAttribute.compareToIgnoreCase(otherAttribute);
/* 561 */       if (comp != 0) {
/* 562 */         return comp;
/*     */       }
/* 564 */       if ("charset".equals(thisAttribute)) {
/* 565 */         Charset thisCharset = getCharset();
/* 566 */         Charset otherCharset = other.getCharset();
/* 567 */         if (thisCharset != otherCharset) {
/* 568 */           if (thisCharset == null) {
/* 569 */             return -1;
/*     */           }
/* 571 */           if (otherCharset == null) {
/* 572 */             return 1;
/*     */           }
/* 574 */           comp = thisCharset.compareTo(otherCharset);
/* 575 */           if (comp != 0) {
/* 576 */             return comp;
/*     */           }
/*     */         } 
/*     */         continue;
/*     */       } 
/* 581 */       String thisValue = getParameters().get(thisAttribute);
/* 582 */       String otherValue = other.getParameters().get(otherAttribute);
/* 583 */       if (otherValue == null) {
/* 584 */         otherValue = "";
/*     */       }
/* 586 */       comp = thisValue.compareTo(otherValue);
/* 587 */       if (comp != 0) {
/* 588 */         return comp;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 593 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
/* 598 */     ois.defaultReadObject();
/*     */ 
/*     */     
/* 601 */     String charsetName = getParameter("charset");
/* 602 */     if (charsetName != null) {
/* 603 */       this.resolvedCharset = Charset.forName(unquote(charsetName));
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
/*     */   public static MimeType valueOf(String value) {
/* 615 */     return MimeTypeUtils.parseMimeType(value);
/*     */   }
/*     */   
/*     */   private static Map<String, String> addCharsetParameter(Charset charset, Map<String, String> parameters) {
/* 619 */     Map<String, String> map = new LinkedHashMap<>(parameters);
/* 620 */     map.put("charset", charset.name());
/* 621 */     return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class SpecificityComparator<T extends MimeType>
/*     */     implements Comparator<T>
/*     */   {
/*     */     public int compare(T mimeType1, T mimeType2) {
/* 634 */       if (mimeType1.isWildcardType() && !mimeType2.isWildcardType()) {
/* 635 */         return 1;
/*     */       }
/* 637 */       if (mimeType2.isWildcardType() && !mimeType1.isWildcardType()) {
/* 638 */         return -1;
/*     */       }
/*     */       
/* 641 */       if (mimeType1.isWildcardSubtype() && !mimeType2.isWildcardSubtype()) {
/* 642 */         return 1;
/*     */       }
/* 644 */       if (mimeType2.isWildcardSubtype() && !mimeType1.isWildcardSubtype()) {
/* 645 */         return -1;
/*     */       }
/*     */       
/* 648 */       return compareParameters(mimeType1, mimeType2);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected int compareParameters(T mimeType1, T mimeType2) {
/* 654 */       int paramsSize1 = mimeType1.getParameters().size();
/* 655 */       int paramsSize2 = mimeType2.getParameters().size();
/* 656 */       return Integer.compare(paramsSize2, paramsSize1);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\MimeType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */