/*     */ package com.google.common.net;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Ascii;
/*     */ import com.google.common.base.CharMatcher;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.errorprone.annotations.Immutable;
/*     */ import com.google.thirdparty.publicsuffix.PublicSuffixPatterns;
/*     */ import com.google.thirdparty.publicsuffix.PublicSuffixType;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable
/*     */ @Beta
/*     */ @GwtCompatible
/*     */ public final class InternetDomainName
/*     */ {
/*  79 */   private static final CharMatcher DOTS_MATCHER = CharMatcher.anyOf(".。．｡");
/*  80 */   private static final Splitter DOT_SPLITTER = Splitter.on('.');
/*  81 */   private static final Joiner DOT_JOINER = Joiner.on('.');
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int NO_SUFFIX_FOUND = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int MAX_PARTS = 127;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int MAX_LENGTH = 253;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int MAX_DOMAIN_PART_LENGTH = 63;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String name;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ImmutableList<String> parts;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int publicSuffixIndex;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int registrySuffixIndex;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   InternetDomainName(String name) {
/* 138 */     name = Ascii.toLowerCase(DOTS_MATCHER.replaceFrom(name, '.'));
/*     */     
/* 140 */     if (name.endsWith(".")) {
/* 141 */       name = name.substring(0, name.length() - 1);
/*     */     }
/*     */     
/* 144 */     Preconditions.checkArgument((name.length() <= 253), "Domain name too long: '%s':", name);
/* 145 */     this.name = name;
/*     */     
/* 147 */     this.parts = ImmutableList.copyOf(DOT_SPLITTER.split(name));
/* 148 */     Preconditions.checkArgument((this.parts.size() <= 127), "Domain has too many parts: '%s'", name);
/* 149 */     Preconditions.checkArgument(validateSyntax((List<String>)this.parts), "Not a valid domain name: '%s'", name);
/*     */     
/* 151 */     this.publicSuffixIndex = findSuffixOfType(Optional.absent());
/* 152 */     this.registrySuffixIndex = findSuffixOfType(Optional.of(PublicSuffixType.REGISTRY));
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
/*     */   private int findSuffixOfType(Optional<PublicSuffixType> desiredType) {
/* 165 */     int partsSize = this.parts.size();
/*     */     
/* 167 */     for (int i = 0; i < partsSize; i++) {
/* 168 */       String ancestorName = DOT_JOINER.join((Iterable)this.parts.subList(i, partsSize));
/*     */       
/* 170 */       if (matchesType(desiredType, 
/* 171 */           Optional.fromNullable(PublicSuffixPatterns.EXACT.get(ancestorName)))) {
/* 172 */         return i;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 178 */       if (PublicSuffixPatterns.EXCLUDED.containsKey(ancestorName)) {
/* 179 */         return i + 1;
/*     */       }
/*     */       
/* 182 */       if (matchesWildcardSuffixType(desiredType, ancestorName)) {
/* 183 */         return i;
/*     */       }
/*     */     } 
/*     */     
/* 187 */     return -1;
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
/*     */   public static InternetDomainName from(String domain) {
/* 210 */     return new InternetDomainName((String)Preconditions.checkNotNull(domain));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean validateSyntax(List<String> parts) {
/* 220 */     int lastIndex = parts.size() - 1;
/*     */ 
/*     */ 
/*     */     
/* 224 */     if (!validatePart(parts.get(lastIndex), true)) {
/* 225 */       return false;
/*     */     }
/*     */     
/* 228 */     for (int i = 0; i < lastIndex; i++) {
/* 229 */       String part = parts.get(i);
/* 230 */       if (!validatePart(part, false)) {
/* 231 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 235 */     return true;
/*     */   }
/*     */   
/* 238 */   private static final CharMatcher DASH_MATCHER = CharMatcher.anyOf("-_");
/*     */ 
/*     */   
/* 241 */   private static final CharMatcher PART_CHAR_MATCHER = CharMatcher.javaLetterOrDigit().or(DASH_MATCHER);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean validatePart(String part, boolean isFinalPart) {
/* 256 */     if (part.length() < 1 || part.length() > 63) {
/* 257 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 270 */     String asciiChars = CharMatcher.ascii().retainFrom(part);
/*     */     
/* 272 */     if (!PART_CHAR_MATCHER.matchesAllOf(asciiChars)) {
/* 273 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 278 */     if (DASH_MATCHER.matches(part.charAt(0)) || DASH_MATCHER
/* 279 */       .matches(part.charAt(part.length() - 1))) {
/* 280 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 290 */     if (isFinalPart && CharMatcher.digit().matches(part.charAt(0))) {
/* 291 */       return false;
/*     */     }
/*     */     
/* 294 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableList<String> parts() {
/* 303 */     return this.parts;
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
/*     */   public boolean isPublicSuffix() {
/* 327 */     return (this.publicSuffixIndex == 0);
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
/*     */   public boolean hasPublicSuffix() {
/* 343 */     return (this.publicSuffixIndex != -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InternetDomainName publicSuffix() {
/* 353 */     return hasPublicSuffix() ? ancestor(this.publicSuffixIndex) : null;
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
/*     */   public boolean isUnderPublicSuffix() {
/* 369 */     return (this.publicSuffixIndex > 0);
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
/*     */   public boolean isTopPrivateDomain() {
/* 385 */     return (this.publicSuffixIndex == 1);
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
/*     */   public InternetDomainName topPrivateDomain() {
/* 405 */     if (isTopPrivateDomain()) {
/* 406 */       return this;
/*     */     }
/* 408 */     Preconditions.checkState(isUnderPublicSuffix(), "Not under a public suffix: %s", this.name);
/* 409 */     return ancestor(this.publicSuffixIndex - 1);
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
/*     */   public boolean isRegistrySuffix() {
/* 436 */     return (this.registrySuffixIndex == 0);
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
/*     */   public boolean hasRegistrySuffix() {
/* 451 */     return (this.registrySuffixIndex != -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InternetDomainName registrySuffix() {
/* 461 */     return hasRegistrySuffix() ? ancestor(this.registrySuffixIndex) : null;
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
/*     */   public boolean isUnderRegistrySuffix() {
/* 473 */     return (this.registrySuffixIndex > 0);
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
/*     */   public boolean isTopDomainUnderRegistrySuffix() {
/* 488 */     return (this.registrySuffixIndex == 1);
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
/*     */   public InternetDomainName topDomainUnderRegistrySuffix() {
/* 507 */     if (isTopDomainUnderRegistrySuffix()) {
/* 508 */       return this;
/*     */     }
/* 510 */     Preconditions.checkState(isUnderRegistrySuffix(), "Not under a registry suffix: %s", this.name);
/* 511 */     return ancestor(this.registrySuffixIndex - 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasParent() {
/* 516 */     return (this.parts.size() > 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InternetDomainName parent() {
/* 527 */     Preconditions.checkState(hasParent(), "Domain '%s' has no parent", this.name);
/* 528 */     return ancestor(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private InternetDomainName ancestor(int levels) {
/* 539 */     return from(DOT_JOINER.join((Iterable)this.parts.subList(levels, this.parts.size())));
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
/*     */   public InternetDomainName child(String leftParts) {
/* 552 */     return from((String)Preconditions.checkNotNull(leftParts) + "." + this.name);
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
/*     */   public static boolean isValid(String name) {
/*     */     try {
/* 580 */       from(name);
/* 581 */       return true;
/* 582 */     } catch (IllegalArgumentException e) {
/* 583 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean matchesWildcardSuffixType(Optional<PublicSuffixType> desiredType, String domain) {
/* 593 */     List<String> pieces = DOT_SPLITTER.limit(2).splitToList(domain);
/* 594 */     return (pieces.size() == 2 && 
/* 595 */       matchesType(desiredType, 
/* 596 */         Optional.fromNullable(PublicSuffixPatterns.UNDER.get(pieces.get(1)))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean matchesType(Optional<PublicSuffixType> desiredType, Optional<PublicSuffixType> actualType) {
/* 605 */     return desiredType.isPresent() ? desiredType.equals(actualType) : actualType.isPresent();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 611 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/* 621 */     if (object == this) {
/* 622 */       return true;
/*     */     }
/*     */     
/* 625 */     if (object instanceof InternetDomainName) {
/* 626 */       InternetDomainName that = (InternetDomainName)object;
/* 627 */       return this.name.equals(that.name);
/*     */     } 
/*     */     
/* 630 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 635 */     return this.name.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\net\InternetDomainName.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */