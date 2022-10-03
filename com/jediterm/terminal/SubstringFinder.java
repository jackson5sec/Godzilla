/*     */ package com.jediterm.terminal;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.jediterm.terminal.model.CharBuffer;
/*     */ import com.jediterm.terminal.model.SubCharBuffer;
/*     */ import com.jediterm.terminal.util.Pair;
/*     */ import java.awt.Point;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ import org.jetbrains.annotations.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SubstringFinder
/*     */ {
/*     */   private final String myPattern;
/*     */   private final int myPatternHash;
/*     */   private int myCurrentHash;
/*     */   private int myCurrentLength;
/*  26 */   private final ArrayList<TextToken> myTokens = Lists.newArrayList();
/*     */   private int myFirstIndex;
/*  28 */   private int myPower = 0;
/*     */   
/*  30 */   private final FindResult myResult = new FindResult();
/*     */   
/*     */   private boolean myIgnoreCase;
/*     */   
/*     */   public SubstringFinder(String pattern, boolean ignoreCase) {
/*  35 */     this.myIgnoreCase = ignoreCase;
/*  36 */     this.myPattern = ignoreCase ? pattern.toLowerCase() : pattern;
/*  37 */     this.myPatternHash = this.myPattern.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public void nextChar(int x, int y, CharBuffer characters, int index) {
/*  42 */     if (this.myTokens.size() == 0 || ((TextToken)this.myTokens.get(this.myTokens.size() - 1)).buf != characters) {
/*  43 */       this.myTokens.add(new TextToken(x, y, characters));
/*     */     }
/*     */     
/*  46 */     if (this.myCurrentLength == this.myPattern.length()) {
/*  47 */       this.myCurrentHash -= hashCodeForChar(((TextToken)this.myTokens.get(0)).buf.charAt(this.myFirstIndex));
/*  48 */       if (this.myFirstIndex + 1 == ((TextToken)this.myTokens.get(0)).buf.length()) {
/*  49 */         this.myFirstIndex = 0;
/*  50 */         this.myTokens.remove(0);
/*     */       } else {
/*  52 */         this.myFirstIndex++;
/*     */       } 
/*     */     } else {
/*  55 */       this.myCurrentLength++;
/*  56 */       if (this.myPower == 0) {
/*  57 */         this.myPower = 1;
/*     */       } else {
/*  59 */         this.myPower *= 31;
/*     */       } 
/*     */     } 
/*     */     
/*  63 */     this.myCurrentHash = 31 * this.myCurrentHash + charHash(characters.charAt(index));
/*     */     
/*  65 */     if (this.myCurrentLength == this.myPattern.length() && this.myCurrentHash == this.myPatternHash) {
/*  66 */       FindResult.FindItem item = new FindResult.FindItem(this.myTokens, this.myFirstIndex, index, -1);
/*  67 */       String itemText = item.getText();
/*  68 */       boolean matched = this.myPattern.equals(this.myIgnoreCase ? itemText.toLowerCase() : itemText);
/*  69 */       if (matched && accept(item)) {
/*  70 */         this.myResult.patternMatched(this.myTokens, this.myFirstIndex, index);
/*  71 */         this.myCurrentHash = 0;
/*  72 */         this.myCurrentLength = 0;
/*  73 */         this.myPower = 0;
/*  74 */         this.myTokens.clear();
/*  75 */         if (index + 1 < characters.length()) {
/*  76 */           this.myFirstIndex = index + 1;
/*  77 */           this.myTokens.add(new TextToken(x, y, characters));
/*     */         } else {
/*  79 */           this.myFirstIndex = 0;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean accept(@NotNull FindResult.FindItem item) {
/*  86 */     if (item == null) $$$reportNull$$$0(0);  return true;
/*     */   }
/*     */   
/*     */   private int charHash(char c) {
/*  90 */     return this.myIgnoreCase ? Character.toLowerCase(c) : c;
/*     */   }
/*     */   
/*     */   private int hashCodeForChar(char charAt) {
/*  94 */     return this.myPower * charHash(charAt);
/*     */   }
/*     */   
/*     */   public FindResult getResult() {
/*  98 */     return this.myResult;
/*     */   }
/*     */   
/*     */   public static class FindResult {
/* 102 */     private final List<FindItem> items = Lists.newArrayList();
/* 103 */     private final Map<CharBuffer, List<Pair<Integer, Integer>>> ranges = Maps.newHashMap();
/* 104 */     private int currentFindItem = 0;
/*     */     
/*     */     public List<Pair<Integer, Integer>> getRanges(CharBuffer characters) {
/* 107 */       if (characters instanceof SubCharBuffer) {
/* 108 */         SubCharBuffer subCharBuffer = (SubCharBuffer)characters;
/* 109 */         List<Pair<Integer, Integer>> pairs = this.ranges.get(subCharBuffer.getParent());
/* 110 */         if (pairs != null) {
/* 111 */           List<Pair<Integer, Integer>> filtered = new ArrayList<>();
/* 112 */           for (Pair<Integer, Integer> pair : pairs) {
/* 113 */             Pair<Integer, Integer> intersected = intersect(pair, subCharBuffer.getOffset(), subCharBuffer.getOffset() + subCharBuffer.length());
/* 114 */             if (intersected != null) {
/* 115 */               filtered.add(Pair.create(Integer.valueOf(((Integer)intersected.first).intValue() - subCharBuffer.getOffset()), Integer.valueOf(((Integer)intersected.second).intValue() - subCharBuffer.getOffset())));
/*     */             }
/*     */           } 
/* 118 */           return filtered;
/*     */         } 
/* 120 */         return null;
/*     */       } 
/* 122 */       return this.ranges.get(characters);
/*     */     }
/*     */     @Nullable
/*     */     private Pair<Integer, Integer> intersect(@NotNull Pair<Integer, Integer> interval, int a, int b) {
/* 126 */       if (interval == null) $$$reportNull$$$0(0);  int start = Math.max(((Integer)interval.first).intValue(), a);
/* 127 */       int end = Math.min(((Integer)interval.second).intValue(), b);
/* 128 */       return (start < end) ? Pair.create(Integer.valueOf(start), Integer.valueOf(end)) : null;
/*     */     }
/*     */ 
/*     */     
/*     */     public static class FindItem
/*     */     {
/*     */       final ArrayList<SubstringFinder.TextToken> tokens;
/*     */       final int firstIndex;
/*     */       final int lastIndex;
/*     */       final int index;
/*     */       
/*     */       private FindItem(ArrayList<SubstringFinder.TextToken> tokens, int firstIndex, int lastIndex, int index) {
/* 140 */         this.tokens = Lists.newArrayList(tokens);
/* 141 */         this.firstIndex = firstIndex;
/* 142 */         this.lastIndex = lastIndex;
/* 143 */         this.index = index;
/*     */       }
/*     */       
/*     */       @NotNull
/*     */       public String getText() {
/* 148 */         StringBuilder b = new StringBuilder();
/*     */         
/* 150 */         if (this.tokens.size() > 1) {
/* 151 */           Pair<Integer, Integer> range = Pair.create(Integer.valueOf(this.firstIndex), Integer.valueOf(((SubstringFinder.TextToken)this.tokens.get(0)).buf.length()));
/* 152 */           b.append((CharSequence)((SubstringFinder.TextToken)this.tokens.get(0)).buf.subBuffer(range));
/*     */         } else {
/* 154 */           Pair<Integer, Integer> range = Pair.create(Integer.valueOf(this.firstIndex), Integer.valueOf(this.lastIndex + 1));
/* 155 */           b.append((CharSequence)((SubstringFinder.TextToken)this.tokens.get(0)).buf.subBuffer(range));
/*     */         } 
/*     */         
/* 158 */         for (int i = 1; i < this.tokens.size() - 1; i++) {
/* 159 */           b.append((CharSequence)((SubstringFinder.TextToken)this.tokens.get(i)).buf);
/*     */         }
/*     */         
/* 162 */         if (this.tokens.size() > 1) {
/* 163 */           Pair<Integer, Integer> range = Pair.create(Integer.valueOf(0), Integer.valueOf(this.lastIndex + 1));
/* 164 */           b.append((CharSequence)((SubstringFinder.TextToken)this.tokens.get(this.tokens.size() - 1)).buf.subBuffer(range));
/*     */         } 
/*     */         
/* 167 */         if (b.toString() == null) $$$reportNull$$$0(0);  return b.toString();
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 172 */         return getText();
/*     */       }
/*     */ 
/*     */       
/*     */       public int getIndex() {
/* 177 */         return this.index;
/*     */       }
/*     */       
/*     */       public Point getStart() {
/* 181 */         return new Point(((SubstringFinder.TextToken)this.tokens.get(0)).x + this.firstIndex, ((SubstringFinder.TextToken)this.tokens.get(0)).y);
/*     */       }
/*     */       
/*     */       public Point getEnd() {
/* 185 */         return new Point(((SubstringFinder.TextToken)this.tokens.get(this.tokens.size() - 1)).x + this.lastIndex, ((SubstringFinder.TextToken)this.tokens.get(this.tokens.size() - 1)).y);
/*     */       }
/*     */     }
/*     */     
/*     */     public void patternMatched(ArrayList<SubstringFinder.TextToken> tokens, int firstIndex, int lastIndex) {
/* 190 */       if (tokens.size() > 1) {
/* 191 */         Pair<Integer, Integer> range = Pair.create(Integer.valueOf(firstIndex), Integer.valueOf(((SubstringFinder.TextToken)tokens.get(0)).buf.length()));
/* 192 */         put(((SubstringFinder.TextToken)tokens.get(0)).buf, range);
/*     */       } else {
/* 194 */         Pair<Integer, Integer> range = Pair.create(Integer.valueOf(firstIndex), Integer.valueOf(lastIndex + 1));
/* 195 */         put(((SubstringFinder.TextToken)tokens.get(0)).buf, range);
/*     */       } 
/*     */       
/* 198 */       for (int i = 1; i < tokens.size() - 1; i++) {
/* 199 */         put(((SubstringFinder.TextToken)tokens.get(i)).buf, Pair.create(Integer.valueOf(0), Integer.valueOf(((SubstringFinder.TextToken)tokens.get(i)).buf.length())));
/*     */       }
/*     */       
/* 202 */       if (tokens.size() > 1) {
/* 203 */         Pair<Integer, Integer> range = Pair.create(Integer.valueOf(0), Integer.valueOf(lastIndex + 1));
/* 204 */         put(((SubstringFinder.TextToken)tokens.get(tokens.size() - 1)).buf, range);
/*     */       } 
/*     */       
/* 207 */       this.items.add(new FindItem(tokens, firstIndex, lastIndex, this.items.size() + 1));
/*     */     }
/*     */ 
/*     */     
/*     */     private void put(CharBuffer characters, Pair<Integer, Integer> range) {
/* 212 */       if (this.ranges.containsKey(characters)) {
/* 213 */         ((List<Pair<Integer, Integer>>)this.ranges.get(characters)).add(range);
/*     */       } else {
/* 215 */         this.ranges.put(characters, Lists.newArrayList((Object[])new Pair[] { range }));
/*     */       } 
/*     */     }
/*     */     
/*     */     public List<FindItem> getItems() {
/* 220 */       return this.items;
/*     */     }
/*     */     
/*     */     public FindItem prevFindItem() {
/* 224 */       if (this.items.isEmpty()) {
/* 225 */         return null;
/*     */       }
/* 227 */       this.currentFindItem++;
/* 228 */       this.currentFindItem %= this.items.size();
/* 229 */       return this.items.get(this.currentFindItem);
/*     */     }
/*     */     
/*     */     public FindItem nextFindItem() {
/* 233 */       if (this.items.isEmpty()) {
/* 234 */         return null;
/*     */       }
/* 236 */       this.currentFindItem--;
/*     */       
/* 238 */       this.currentFindItem = (this.currentFindItem + this.items.size()) % this.items.size();
/* 239 */       return this.items.get(this.currentFindItem);
/*     */     }
/*     */   }
/*     */   public static class FindItem {
/*     */     final ArrayList<SubstringFinder.TextToken> tokens;
/*     */     final int firstIndex; final int lastIndex; final int index; private FindItem(ArrayList<SubstringFinder.TextToken> tokens, int firstIndex, int lastIndex, int index) { this.tokens = Lists.newArrayList(tokens); this.firstIndex = firstIndex; this.lastIndex = lastIndex; this.index = index; } @NotNull public String getText() { StringBuilder b = new StringBuilder(); if (this.tokens.size() > 1) { Pair<Integer, Integer> range = Pair.create(Integer.valueOf(this.firstIndex), Integer.valueOf(((SubstringFinder.TextToken)this.tokens.get(0)).buf.length())); b.append((CharSequence)((SubstringFinder.TextToken)this.tokens.get(0)).buf.subBuffer(range)); } else { Pair<Integer, Integer> range = Pair.create(Integer.valueOf(this.firstIndex), Integer.valueOf(this.lastIndex + 1)); b.append((CharSequence)((SubstringFinder.TextToken)this.tokens.get(0)).buf.subBuffer(range)); }  for (int i = 1; i < this.tokens.size() - 1; i++)
/*     */         b.append((CharSequence)((SubstringFinder.TextToken)this.tokens.get(i)).buf);  if (this.tokens.size() > 1) { Pair<Integer, Integer> range = Pair.create(Integer.valueOf(0), Integer.valueOf(this.lastIndex + 1)); b.append((CharSequence)((SubstringFinder.TextToken)this.tokens.get(this.tokens.size() - 1)).buf.subBuffer(range)); }
/*     */        if (b.toString() == null)
/*     */         $$$reportNull$$$0(0);  return b.toString(); } public String toString() { return getText(); } public int getIndex() { return this.index; } public Point getStart() { return new Point(((SubstringFinder.TextToken)this.tokens.get(0)).x + this.firstIndex, ((SubstringFinder.TextToken)this.tokens.get(0)).y); } public Point getEnd() { return new Point(((SubstringFinder.TextToken)this.tokens.get(this.tokens.size() - 1)).x + this.lastIndex, ((SubstringFinder.TextToken)this.tokens.get(this.tokens.size() - 1)).y); }
/*     */   } private static class TextToken {
/* 249 */     private TextToken(int x, int y, CharBuffer buf) { this.x = x;
/* 250 */       this.y = y;
/* 251 */       this.buf = buf; }
/*     */ 
/*     */     
/*     */     final CharBuffer buf;
/*     */     final int x;
/*     */     final int y;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\SubstringFinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */