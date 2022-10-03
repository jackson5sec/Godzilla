/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class Splitter
/*     */ {
/*     */   private final CharMatcher trimmer;
/*     */   private final boolean omitEmptyStrings;
/*     */   private final Strategy strategy;
/*     */   private final int limit;
/*     */   
/*     */   private Splitter(Strategy strategy) {
/* 107 */     this(strategy, false, CharMatcher.none(), 2147483647);
/*     */   }
/*     */   
/*     */   private Splitter(Strategy strategy, boolean omitEmptyStrings, CharMatcher trimmer, int limit) {
/* 111 */     this.strategy = strategy;
/* 112 */     this.omitEmptyStrings = omitEmptyStrings;
/* 113 */     this.trimmer = trimmer;
/* 114 */     this.limit = limit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Splitter on(char separator) {
/* 125 */     return on(CharMatcher.is(separator));
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
/*     */   public static Splitter on(final CharMatcher separatorMatcher) {
/* 139 */     Preconditions.checkNotNull(separatorMatcher);
/*     */     
/* 141 */     return new Splitter(new Strategy()
/*     */         {
/*     */           public Splitter.SplittingIterator iterator(Splitter splitter, CharSequence toSplit)
/*     */           {
/* 145 */             return new Splitter.SplittingIterator(splitter, toSplit)
/*     */               {
/*     */                 int separatorStart(int start) {
/* 148 */                   return separatorMatcher.indexIn(this.toSplit, start);
/*     */                 }
/*     */ 
/*     */                 
/*     */                 int separatorEnd(int separatorPosition) {
/* 153 */                   return separatorPosition + 1;
/*     */                 }
/*     */               };
/*     */           }
/*     */         });
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
/*     */   public static Splitter on(final String separator) {
/* 169 */     Preconditions.checkArgument((separator.length() != 0), "The separator may not be the empty string.");
/* 170 */     if (separator.length() == 1) {
/* 171 */       return on(separator.charAt(0));
/*     */     }
/* 173 */     return new Splitter(new Strategy()
/*     */         {
/*     */           public Splitter.SplittingIterator iterator(Splitter splitter, CharSequence toSplit)
/*     */           {
/* 177 */             return new Splitter.SplittingIterator(splitter, toSplit)
/*     */               {
/*     */                 public int separatorStart(int start) {
/* 180 */                   int separatorLength = separator.length();
/*     */ 
/*     */                   
/* 183 */                   for (int p = start, last = this.toSplit.length() - separatorLength; p <= last; p++) {
/* 184 */                     int i = 0; while (true) { if (i < separatorLength) {
/* 185 */                         if (this.toSplit.charAt(i + p) != separator.charAt(i))
/*     */                           break;  i++;
/*     */                         continue;
/*     */                       } 
/* 189 */                       return p; }
/*     */                   
/* 191 */                   }  return -1;
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public int separatorEnd(int separatorPosition) {
/* 196 */                   return separatorPosition + separator.length();
/*     */                 }
/*     */               };
/*     */           }
/*     */         });
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
/*     */   @GwtIncompatible
/*     */   public static Splitter on(Pattern separatorPattern) {
/* 215 */     return on(new JdkPattern(separatorPattern));
/*     */   }
/*     */   
/*     */   private static Splitter on(final CommonPattern separatorPattern) {
/* 219 */     Preconditions.checkArgument(
/* 220 */         !separatorPattern.matcher("").matches(), "The pattern may not match the empty string: %s", separatorPattern);
/*     */ 
/*     */ 
/*     */     
/* 224 */     return new Splitter(new Strategy()
/*     */         {
/*     */           public Splitter.SplittingIterator iterator(Splitter splitter, CharSequence toSplit)
/*     */           {
/* 228 */             final CommonMatcher matcher = separatorPattern.matcher(toSplit);
/* 229 */             return new Splitter.SplittingIterator(splitter, toSplit)
/*     */               {
/*     */                 public int separatorStart(int start) {
/* 232 */                   return matcher.find(start) ? matcher.start() : -1;
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public int separatorEnd(int separatorPosition) {
/* 237 */                   return matcher.end();
/*     */                 }
/*     */               };
/*     */           }
/*     */         });
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
/*     */   @GwtIncompatible
/*     */   public static Splitter onPattern(String separatorPattern) {
/* 258 */     return on(Platform.compilePattern(separatorPattern));
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
/*     */   public static Splitter fixedLength(final int length) {
/* 280 */     Preconditions.checkArgument((length > 0), "The length may not be less than 1");
/*     */     
/* 282 */     return new Splitter(new Strategy()
/*     */         {
/*     */           public Splitter.SplittingIterator iterator(Splitter splitter, CharSequence toSplit)
/*     */           {
/* 286 */             return new Splitter.SplittingIterator(splitter, toSplit)
/*     */               {
/*     */                 public int separatorStart(int start) {
/* 289 */                   int nextChunkStart = start + length;
/* 290 */                   return (nextChunkStart < this.toSplit.length()) ? nextChunkStart : -1;
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public int separatorEnd(int separatorPosition) {
/* 295 */                   return separatorPosition;
/*     */                 }
/*     */               };
/*     */           }
/*     */         });
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
/*     */   public Splitter omitEmptyStrings() {
/* 319 */     return new Splitter(this.strategy, true, this.trimmer, this.limit);
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
/*     */   public Splitter limit(int limit) {
/* 339 */     Preconditions.checkArgument((limit > 0), "must be greater than zero: %s", limit);
/* 340 */     return new Splitter(this.strategy, this.omitEmptyStrings, this.trimmer, limit);
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
/*     */   public Splitter trimResults() {
/* 353 */     return trimResults(CharMatcher.whitespace());
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
/*     */   public Splitter trimResults(CharMatcher trimmer) {
/* 368 */     Preconditions.checkNotNull(trimmer);
/* 369 */     return new Splitter(this.strategy, this.omitEmptyStrings, trimmer, this.limit);
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
/*     */   public Iterable<String> split(final CharSequence sequence) {
/* 381 */     Preconditions.checkNotNull(sequence);
/*     */     
/* 383 */     return new Iterable<String>()
/*     */       {
/*     */         public Iterator<String> iterator() {
/* 386 */           return Splitter.this.splittingIterator(sequence);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 391 */           return Joiner.on(", ")
/* 392 */             .appendTo((new StringBuilder()).append('['), this)
/* 393 */             .append(']')
/* 394 */             .toString();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private Iterator<String> splittingIterator(CharSequence sequence) {
/* 400 */     return this.strategy.iterator(this, sequence);
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
/*     */   public List<String> splitToList(CharSequence sequence) {
/* 412 */     Preconditions.checkNotNull(sequence);
/*     */     
/* 414 */     Iterator<String> iterator = splittingIterator(sequence);
/* 415 */     List<String> result = new ArrayList<>();
/*     */     
/* 417 */     while (iterator.hasNext()) {
/* 418 */       result.add(iterator.next());
/*     */     }
/*     */     
/* 421 */     return Collections.unmodifiableList(result);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public MapSplitter withKeyValueSeparator(String separator) {
/* 432 */     return withKeyValueSeparator(on(separator));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public MapSplitter withKeyValueSeparator(char separator) {
/* 443 */     return withKeyValueSeparator(on(separator));
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
/*     */   @Beta
/*     */   public MapSplitter withKeyValueSeparator(Splitter keyValueSplitter) {
/* 467 */     return new MapSplitter(this, keyValueSplitter);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static final class MapSplitter
/*     */   {
/*     */     private static final String INVALID_ENTRY_MESSAGE = "Chunk [%s] is not a valid entry";
/*     */ 
/*     */     
/*     */     private final Splitter outerSplitter;
/*     */ 
/*     */     
/*     */     private final Splitter entrySplitter;
/*     */ 
/*     */     
/*     */     private MapSplitter(Splitter outerSplitter, Splitter entrySplitter) {
/* 485 */       this.outerSplitter = outerSplitter;
/* 486 */       this.entrySplitter = Preconditions.<Splitter>checkNotNull(entrySplitter);
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
/*     */     
/*     */     public Map<String, String> split(CharSequence sequence) {
/* 501 */       Map<String, String> map = new LinkedHashMap<>();
/* 502 */       for (String entry : this.outerSplitter.split(sequence)) {
/* 503 */         Iterator<String> entryFields = this.entrySplitter.splittingIterator(entry);
/*     */         
/* 505 */         Preconditions.checkArgument(entryFields.hasNext(), "Chunk [%s] is not a valid entry", entry);
/* 506 */         String key = entryFields.next();
/* 507 */         Preconditions.checkArgument(!map.containsKey(key), "Duplicate key [%s] found.", key);
/*     */         
/* 509 */         Preconditions.checkArgument(entryFields.hasNext(), "Chunk [%s] is not a valid entry", entry);
/* 510 */         String value = entryFields.next();
/* 511 */         map.put(key, value);
/*     */         
/* 513 */         Preconditions.checkArgument(!entryFields.hasNext(), "Chunk [%s] is not a valid entry", entry);
/*     */       } 
/* 515 */       return Collections.unmodifiableMap(map);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static interface Strategy
/*     */   {
/*     */     Iterator<String> iterator(Splitter param1Splitter, CharSequence param1CharSequence);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static abstract class SplittingIterator
/*     */     extends AbstractIterator<String>
/*     */   {
/*     */     final CharSequence toSplit;
/*     */ 
/*     */     
/*     */     final CharMatcher trimmer;
/*     */ 
/*     */     
/*     */     final boolean omitEmptyStrings;
/*     */ 
/*     */     
/* 540 */     int offset = 0;
/*     */     int limit;
/*     */     
/*     */     protected SplittingIterator(Splitter splitter, CharSequence toSplit) {
/* 544 */       this.trimmer = splitter.trimmer;
/* 545 */       this.omitEmptyStrings = splitter.omitEmptyStrings;
/* 546 */       this.limit = splitter.limit;
/* 547 */       this.toSplit = toSplit;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected String computeNext() {
/* 557 */       int nextStart = this.offset;
/* 558 */       while (this.offset != -1) {
/* 559 */         int end, start = nextStart;
/*     */ 
/*     */         
/* 562 */         int separatorPosition = separatorStart(this.offset);
/* 563 */         if (separatorPosition == -1) {
/* 564 */           end = this.toSplit.length();
/* 565 */           this.offset = -1;
/*     */         } else {
/* 567 */           end = separatorPosition;
/* 568 */           this.offset = separatorEnd(separatorPosition);
/*     */         } 
/* 570 */         if (this.offset == nextStart) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 577 */           this.offset++;
/* 578 */           if (this.offset > this.toSplit.length()) {
/* 579 */             this.offset = -1;
/*     */           }
/*     */           
/*     */           continue;
/*     */         } 
/* 584 */         while (start < end && this.trimmer.matches(this.toSplit.charAt(start))) {
/* 585 */           start++;
/*     */         }
/* 587 */         while (end > start && this.trimmer.matches(this.toSplit.charAt(end - 1))) {
/* 588 */           end--;
/*     */         }
/*     */         
/* 591 */         if (this.omitEmptyStrings && start == end) {
/*     */           
/* 593 */           nextStart = this.offset;
/*     */           
/*     */           continue;
/*     */         } 
/* 597 */         if (this.limit == 1) {
/*     */ 
/*     */ 
/*     */           
/* 601 */           end = this.toSplit.length();
/* 602 */           this.offset = -1;
/*     */           
/* 604 */           while (end > start && this.trimmer.matches(this.toSplit.charAt(end - 1))) {
/* 605 */             end--;
/*     */           }
/*     */         } else {
/* 608 */           this.limit--;
/*     */         } 
/*     */         
/* 611 */         return this.toSplit.subSequence(start, end).toString();
/*     */       } 
/* 613 */       return endOfData();
/*     */     }
/*     */     
/*     */     abstract int separatorStart(int param1Int);
/*     */     
/*     */     abstract int separatorEnd(int param1Int);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\base\Splitter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */