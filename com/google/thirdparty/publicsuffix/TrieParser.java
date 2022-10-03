/*     */ package com.google.thirdparty.publicsuffix;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Lists;
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
/*     */ @GwtCompatible
/*     */ final class TrieParser
/*     */ {
/*  26 */   private static final Joiner PREFIX_JOINER = Joiner.on("");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static ImmutableMap<String, PublicSuffixType> parseTrie(CharSequence encoded) {
/*  33 */     ImmutableMap.Builder<String, PublicSuffixType> builder = ImmutableMap.builder();
/*  34 */     int encodedLen = encoded.length();
/*  35 */     int idx = 0;
/*  36 */     while (idx < encodedLen) {
/*  37 */       idx += doParseTrieToBuilder(Lists.newLinkedList(), encoded, idx, builder);
/*     */     }
/*  39 */     return builder.build();
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
/*     */   private static int doParseTrieToBuilder(List<CharSequence> stack, CharSequence encoded, int start, ImmutableMap.Builder<String, PublicSuffixType> builder) {
/*  58 */     int encodedLen = encoded.length();
/*  59 */     int idx = start;
/*  60 */     char c = Character.MIN_VALUE;
/*     */ 
/*     */     
/*  63 */     for (; idx < encodedLen; idx++) {
/*  64 */       c = encoded.charAt(idx);
/*  65 */       if (c == '&' || c == '?' || c == '!' || c == ':' || c == ',') {
/*     */         break;
/*     */       }
/*     */     } 
/*     */     
/*  70 */     stack.add(0, reverse(encoded.subSequence(start, idx)));
/*     */     
/*  72 */     if (c == '!' || c == '?' || c == ':' || c == ',') {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  77 */       String domain = PREFIX_JOINER.join(stack);
/*  78 */       if (domain.length() > 0) {
/*  79 */         builder.put(domain, PublicSuffixType.fromCode(c));
/*     */       }
/*     */     } 
/*  82 */     idx++;
/*     */     
/*  84 */     if (c != '?' && c != ',') {
/*  85 */       while (idx < encodedLen) {
/*     */         
/*  87 */         idx += doParseTrieToBuilder(stack, encoded, idx, builder);
/*  88 */         if (encoded.charAt(idx) == '?' || encoded.charAt(idx) == ',') {
/*     */           
/*  90 */           idx++;
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/*  95 */     stack.remove(0);
/*  96 */     return idx - start;
/*     */   }
/*     */   
/*     */   private static CharSequence reverse(CharSequence s) {
/* 100 */     return (new StringBuilder(s)).reverse();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\thirdparty\publicsuffix\TrieParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */