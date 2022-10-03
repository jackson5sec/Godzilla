/*      */ package com.google.common.base;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import java.util.Arrays;
/*      */ import java.util.BitSet;
/*      */ import java.util.function.Predicate;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @GwtCompatible(emulated = true)
/*      */ public abstract class CharMatcher
/*      */   implements Predicate<Character>
/*      */ {
/*      */   private static final int DISTINCT_CHARS = 65536;
/*      */   
/*      */   public static CharMatcher any() {
/*  118 */     return Any.INSTANCE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher none() {
/*  127 */     return None.INSTANCE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher whitespace() {
/*  145 */     return Whitespace.INSTANCE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher breakingWhitespace() {
/*  156 */     return BreakingWhitespace.INSTANCE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher ascii() {
/*  165 */     return Ascii.INSTANCE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static CharMatcher digit() {
/*  178 */     return Digit.INSTANCE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static CharMatcher javaDigit() {
/*  191 */     return JavaDigit.INSTANCE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static CharMatcher javaLetter() {
/*  204 */     return JavaLetter.INSTANCE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static CharMatcher javaLetterOrDigit() {
/*  216 */     return JavaLetterOrDigit.INSTANCE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static CharMatcher javaUpperCase() {
/*  229 */     return JavaUpperCase.INSTANCE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static CharMatcher javaLowerCase() {
/*  242 */     return JavaLowerCase.INSTANCE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher javaIsoControl() {
/*  254 */     return JavaIsoControl.INSTANCE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static CharMatcher invisible() {
/*  270 */     return Invisible.INSTANCE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static CharMatcher singleWidth() {
/*  288 */     return SingleWidth.INSTANCE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher is(char match) {
/*  295 */     return new Is(match);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher isNot(char match) {
/*  304 */     return new IsNot(match);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher anyOf(CharSequence sequence) {
/*  312 */     switch (sequence.length()) {
/*      */       case 0:
/*  314 */         return none();
/*      */       case 1:
/*  316 */         return is(sequence.charAt(0));
/*      */       case 2:
/*  318 */         return isEither(sequence.charAt(0), sequence.charAt(1));
/*      */     } 
/*      */ 
/*      */     
/*  322 */     return new AnyOf(sequence);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher noneOf(CharSequence sequence) {
/*  331 */     return anyOf(sequence).negate();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher inRange(char startInclusive, char endInclusive) {
/*  342 */     return new InRange(startInclusive, endInclusive);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher forPredicate(Predicate<? super Character> predicate) {
/*  350 */     return (predicate instanceof CharMatcher) ? (CharMatcher)predicate : new ForPredicate(predicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CharMatcher negate() {
/*  372 */     return new Negated(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CharMatcher and(CharMatcher other) {
/*  379 */     return new And(this, other);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CharMatcher or(CharMatcher other) {
/*  386 */     return new Or(this, other);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CharMatcher precomputed() {
/*  399 */     return Platform.precomputeCharMatcher(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   CharMatcher precomputedInternal() {
/*  416 */     BitSet table = new BitSet();
/*  417 */     setBits(table);
/*  418 */     int totalCharacters = table.cardinality();
/*  419 */     if (totalCharacters * 2 <= 65536) {
/*  420 */       return precomputedPositive(totalCharacters, table, toString());
/*      */     }
/*      */     
/*  423 */     table.flip(0, 65536);
/*  424 */     int negatedCharacters = 65536 - totalCharacters;
/*  425 */     String suffix = ".negate()";
/*  426 */     final String description = toString();
/*      */ 
/*      */     
/*  429 */     String negatedDescription = description.endsWith(suffix) ? description.substring(0, description.length() - suffix.length()) : (description + suffix);
/*      */     
/*  431 */     return new NegatedFastMatcher(
/*  432 */         precomputedPositive(negatedCharacters, table, negatedDescription))
/*      */       {
/*      */         public String toString() {
/*  435 */           return description;
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   private static CharMatcher precomputedPositive(int totalCharacters, BitSet table, String description) {
/*      */     char c1;
/*      */     char c2;
/*  447 */     switch (totalCharacters) {
/*      */       case 0:
/*  449 */         return none();
/*      */       case 1:
/*  451 */         return is((char)table.nextSetBit(0));
/*      */       case 2:
/*  453 */         c1 = (char)table.nextSetBit(0);
/*  454 */         c2 = (char)table.nextSetBit(c1 + 1);
/*  455 */         return isEither(c1, c2);
/*      */     } 
/*  457 */     return isSmall(totalCharacters, table.length()) ? 
/*  458 */       SmallCharMatcher.from(table, description) : new BitSetMatcher(table, description);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   private static boolean isSmall(int totalCharacters, int tableLength) {
/*  465 */     return (totalCharacters <= 1023 && tableLength > totalCharacters * 4 * 16);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   void setBits(BitSet table) {
/*  473 */     for (int c = 65535; c >= 0; c--) {
/*  474 */       if (matches((char)c)) {
/*  475 */         table.set(c);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean matchesAnyOf(CharSequence sequence) {
/*  494 */     return !matchesNoneOf(sequence);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean matchesAllOf(CharSequence sequence) {
/*  508 */     for (int i = sequence.length() - 1; i >= 0; i--) {
/*  509 */       if (!matches(sequence.charAt(i))) {
/*  510 */         return false;
/*      */       }
/*      */     } 
/*  513 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean matchesNoneOf(CharSequence sequence) {
/*  528 */     return (indexIn(sequence) == -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int indexIn(CharSequence sequence) {
/*  542 */     return indexIn(sequence, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int indexIn(CharSequence sequence, int start) {
/*  561 */     int length = sequence.length();
/*  562 */     Preconditions.checkPositionIndex(start, length);
/*  563 */     for (int i = start; i < length; i++) {
/*  564 */       if (matches(sequence.charAt(i))) {
/*  565 */         return i;
/*      */       }
/*      */     } 
/*  568 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int lastIndexIn(CharSequence sequence) {
/*  582 */     for (int i = sequence.length() - 1; i >= 0; i--) {
/*  583 */       if (matches(sequence.charAt(i))) {
/*  584 */         return i;
/*      */       }
/*      */     } 
/*  587 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int countIn(CharSequence sequence) {
/*  596 */     int count = 0;
/*  597 */     for (int i = 0; i < sequence.length(); i++) {
/*  598 */       if (matches(sequence.charAt(i))) {
/*  599 */         count++;
/*      */       }
/*      */     } 
/*  602 */     return count;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String removeFrom(CharSequence sequence) {
/*  616 */     String string = sequence.toString();
/*  617 */     int pos = indexIn(string);
/*  618 */     if (pos == -1) {
/*  619 */       return string;
/*      */     }
/*      */     
/*  622 */     char[] chars = string.toCharArray();
/*  623 */     int spread = 1;
/*      */ 
/*      */ 
/*      */     
/*      */     while (true) {
/*  628 */       pos++;
/*      */       
/*  630 */       while (pos != chars.length) {
/*      */ 
/*      */         
/*  633 */         if (matches(chars[pos]))
/*      */         
/*      */         { 
/*      */ 
/*      */ 
/*      */           
/*  639 */           spread++; continue; }  chars[pos - spread] = chars[pos]; pos++;
/*      */       }  break;
/*  641 */     }  return new String(chars, 0, pos - spread);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String retainFrom(CharSequence sequence) {
/*  655 */     return negate().removeFrom(sequence);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replaceFrom(CharSequence sequence, char replacement) {
/*  678 */     String string = sequence.toString();
/*  679 */     int pos = indexIn(string);
/*  680 */     if (pos == -1) {
/*  681 */       return string;
/*      */     }
/*  683 */     char[] chars = string.toCharArray();
/*  684 */     chars[pos] = replacement;
/*  685 */     for (int i = pos + 1; i < chars.length; i++) {
/*  686 */       if (matches(chars[i])) {
/*  687 */         chars[i] = replacement;
/*      */       }
/*      */     } 
/*  690 */     return new String(chars);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replaceFrom(CharSequence sequence, CharSequence replacement) {
/*  712 */     int replacementLen = replacement.length();
/*  713 */     if (replacementLen == 0) {
/*  714 */       return removeFrom(sequence);
/*      */     }
/*  716 */     if (replacementLen == 1) {
/*  717 */       return replaceFrom(sequence, replacement.charAt(0));
/*      */     }
/*      */     
/*  720 */     String string = sequence.toString();
/*  721 */     int pos = indexIn(string);
/*  722 */     if (pos == -1) {
/*  723 */       return string;
/*      */     }
/*      */     
/*  726 */     int len = string.length();
/*  727 */     StringBuilder buf = new StringBuilder(len * 3 / 2 + 16);
/*      */     
/*  729 */     int oldpos = 0;
/*      */     do {
/*  731 */       buf.append(string, oldpos, pos);
/*  732 */       buf.append(replacement);
/*  733 */       oldpos = pos + 1;
/*  734 */       pos = indexIn(string, oldpos);
/*  735 */     } while (pos != -1);
/*      */     
/*  737 */     buf.append(string, oldpos, len);
/*  738 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String trimFrom(CharSequence sequence) {
/*  760 */     int len = sequence.length();
/*      */     
/*      */     int first;
/*      */     
/*  764 */     for (first = 0; first < len && 
/*  765 */       matches(sequence.charAt(first)); first++);
/*      */     
/*      */     int last;
/*      */     
/*  769 */     for (last = len - 1; last > first && 
/*  770 */       matches(sequence.charAt(last)); last--);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  775 */     return sequence.subSequence(first, last + 1).toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String trimLeadingFrom(CharSequence sequence) {
/*  789 */     int len = sequence.length();
/*  790 */     for (int first = 0; first < len; first++) {
/*  791 */       if (!matches(sequence.charAt(first))) {
/*  792 */         return sequence.subSequence(first, len).toString();
/*      */       }
/*      */     } 
/*  795 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String trimTrailingFrom(CharSequence sequence) {
/*  809 */     int len = sequence.length();
/*  810 */     for (int last = len - 1; last >= 0; last--) {
/*  811 */       if (!matches(sequence.charAt(last))) {
/*  812 */         return sequence.subSequence(0, last + 1).toString();
/*      */       }
/*      */     } 
/*  815 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String collapseFrom(CharSequence sequence, char replacement) {
/*  839 */     int len = sequence.length();
/*  840 */     for (int i = 0; i < len; i++) {
/*  841 */       char c = sequence.charAt(i);
/*  842 */       if (matches(c)) {
/*  843 */         if (c == replacement && (i == len - 1 || !matches(sequence.charAt(i + 1)))) {
/*      */           
/*  845 */           i++;
/*      */         } else {
/*  847 */           StringBuilder builder = (new StringBuilder(len)).append(sequence, 0, i).append(replacement);
/*  848 */           return finishCollapseFrom(sequence, i + 1, len, replacement, builder, true);
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/*  853 */     return sequence.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String trimAndCollapseFrom(CharSequence sequence, char replacement) {
/*  863 */     int len = sequence.length();
/*  864 */     int first = 0;
/*  865 */     int last = len - 1;
/*      */     
/*  867 */     while (first < len && matches(sequence.charAt(first))) {
/*  868 */       first++;
/*      */     }
/*      */     
/*  871 */     while (last > first && matches(sequence.charAt(last))) {
/*  872 */       last--;
/*      */     }
/*      */     
/*  875 */     return (first == 0 && last == len - 1) ? 
/*  876 */       collapseFrom(sequence, replacement) : 
/*  877 */       finishCollapseFrom(sequence, first, last + 1, replacement, new StringBuilder(last + 1 - first), false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String finishCollapseFrom(CharSequence sequence, int start, int end, char replacement, StringBuilder builder, boolean inMatchingGroup) {
/*  888 */     for (int i = start; i < end; i++) {
/*  889 */       char c = sequence.charAt(i);
/*  890 */       if (matches(c)) {
/*  891 */         if (!inMatchingGroup) {
/*  892 */           builder.append(replacement);
/*  893 */           inMatchingGroup = true;
/*      */         } 
/*      */       } else {
/*  896 */         builder.append(c);
/*  897 */         inMatchingGroup = false;
/*      */       } 
/*      */     } 
/*  900 */     return builder.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public boolean apply(Character character) {
/*  910 */     return matches(character.charValue());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/*  919 */     return super.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String showCharacter(char c) {
/*  927 */     String hex = "0123456789ABCDEF";
/*  928 */     char[] tmp = { '\\', 'u', Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE };
/*  929 */     for (int i = 0; i < 4; i++) {
/*  930 */       tmp[5 - i] = hex.charAt(c & 0xF);
/*  931 */       c = (char)(c >> 4);
/*      */     } 
/*  933 */     return String.copyValueOf(tmp);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static abstract class FastMatcher
/*      */     extends CharMatcher
/*      */   {
/*      */     public final CharMatcher precomputed() {
/*  943 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher negate() {
/*  948 */       return new CharMatcher.NegatedFastMatcher(this);
/*      */     }
/*      */   }
/*      */   
/*      */   static abstract class NamedFastMatcher
/*      */     extends FastMatcher
/*      */   {
/*      */     private final String description;
/*      */     
/*      */     NamedFastMatcher(String description) {
/*  958 */       this.description = Preconditions.<String>checkNotNull(description);
/*      */     }
/*      */ 
/*      */     
/*      */     public final String toString() {
/*  963 */       return this.description;
/*      */     }
/*      */   }
/*      */   
/*      */   static class NegatedFastMatcher
/*      */     extends Negated
/*      */   {
/*      */     NegatedFastMatcher(CharMatcher original) {
/*  971 */       super(original);
/*      */     }
/*      */ 
/*      */     
/*      */     public final CharMatcher precomputed() {
/*  976 */       return this;
/*      */     }
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   private static final class BitSetMatcher
/*      */     extends NamedFastMatcher
/*      */   {
/*      */     private final BitSet table;
/*      */     
/*      */     private BitSetMatcher(BitSet table, String description) {
/*  987 */       super(description);
/*  988 */       if (table.length() + 64 < table.size()) {
/*  989 */         table = (BitSet)table.clone();
/*      */       }
/*      */       
/*  992 */       this.table = table;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/*  997 */       return this.table.get(c);
/*      */     }
/*      */ 
/*      */     
/*      */     void setBits(BitSet bitSet) {
/* 1002 */       bitSet.or(this.table);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class Any
/*      */     extends NamedFastMatcher
/*      */   {
/* 1011 */     static final Any INSTANCE = new Any();
/*      */     
/*      */     private Any() {
/* 1014 */       super("CharMatcher.any()");
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1019 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public int indexIn(CharSequence sequence) {
/* 1024 */       return (sequence.length() == 0) ? -1 : 0;
/*      */     }
/*      */ 
/*      */     
/*      */     public int indexIn(CharSequence sequence, int start) {
/* 1029 */       int length = sequence.length();
/* 1030 */       Preconditions.checkPositionIndex(start, length);
/* 1031 */       return (start == length) ? -1 : start;
/*      */     }
/*      */ 
/*      */     
/*      */     public int lastIndexIn(CharSequence sequence) {
/* 1036 */       return sequence.length() - 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matchesAllOf(CharSequence sequence) {
/* 1041 */       Preconditions.checkNotNull(sequence);
/* 1042 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matchesNoneOf(CharSequence sequence) {
/* 1047 */       return (sequence.length() == 0);
/*      */     }
/*      */ 
/*      */     
/*      */     public String removeFrom(CharSequence sequence) {
/* 1052 */       Preconditions.checkNotNull(sequence);
/* 1053 */       return "";
/*      */     }
/*      */ 
/*      */     
/*      */     public String replaceFrom(CharSequence sequence, char replacement) {
/* 1058 */       char[] array = new char[sequence.length()];
/* 1059 */       Arrays.fill(array, replacement);
/* 1060 */       return new String(array);
/*      */     }
/*      */ 
/*      */     
/*      */     public String replaceFrom(CharSequence sequence, CharSequence replacement) {
/* 1065 */       StringBuilder result = new StringBuilder(sequence.length() * replacement.length());
/* 1066 */       for (int i = 0; i < sequence.length(); i++) {
/* 1067 */         result.append(replacement);
/*      */       }
/* 1069 */       return result.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public String collapseFrom(CharSequence sequence, char replacement) {
/* 1074 */       return (sequence.length() == 0) ? "" : String.valueOf(replacement);
/*      */     }
/*      */ 
/*      */     
/*      */     public String trimFrom(CharSequence sequence) {
/* 1079 */       Preconditions.checkNotNull(sequence);
/* 1080 */       return "";
/*      */     }
/*      */ 
/*      */     
/*      */     public int countIn(CharSequence sequence) {
/* 1085 */       return sequence.length();
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher and(CharMatcher other) {
/* 1090 */       return Preconditions.<CharMatcher>checkNotNull(other);
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher or(CharMatcher other) {
/* 1095 */       Preconditions.checkNotNull(other);
/* 1096 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher negate() {
/* 1101 */       return none();
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class None
/*      */     extends NamedFastMatcher
/*      */   {
/* 1108 */     static final None INSTANCE = new None();
/*      */     
/*      */     private None() {
/* 1111 */       super("CharMatcher.none()");
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1116 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public int indexIn(CharSequence sequence) {
/* 1121 */       Preconditions.checkNotNull(sequence);
/* 1122 */       return -1;
/*      */     }
/*      */ 
/*      */     
/*      */     public int indexIn(CharSequence sequence, int start) {
/* 1127 */       int length = sequence.length();
/* 1128 */       Preconditions.checkPositionIndex(start, length);
/* 1129 */       return -1;
/*      */     }
/*      */ 
/*      */     
/*      */     public int lastIndexIn(CharSequence sequence) {
/* 1134 */       Preconditions.checkNotNull(sequence);
/* 1135 */       return -1;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matchesAllOf(CharSequence sequence) {
/* 1140 */       return (sequence.length() == 0);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matchesNoneOf(CharSequence sequence) {
/* 1145 */       Preconditions.checkNotNull(sequence);
/* 1146 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public String removeFrom(CharSequence sequence) {
/* 1151 */       return sequence.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public String replaceFrom(CharSequence sequence, char replacement) {
/* 1156 */       return sequence.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public String replaceFrom(CharSequence sequence, CharSequence replacement) {
/* 1161 */       Preconditions.checkNotNull(replacement);
/* 1162 */       return sequence.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public String collapseFrom(CharSequence sequence, char replacement) {
/* 1167 */       return sequence.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public String trimFrom(CharSequence sequence) {
/* 1172 */       return sequence.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public String trimLeadingFrom(CharSequence sequence) {
/* 1177 */       return sequence.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public String trimTrailingFrom(CharSequence sequence) {
/* 1182 */       return sequence.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public int countIn(CharSequence sequence) {
/* 1187 */       Preconditions.checkNotNull(sequence);
/* 1188 */       return 0;
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher and(CharMatcher other) {
/* 1193 */       Preconditions.checkNotNull(other);
/* 1194 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher or(CharMatcher other) {
/* 1199 */       return Preconditions.<CharMatcher>checkNotNull(other);
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher negate() {
/* 1204 */       return any();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   static final class Whitespace
/*      */     extends NamedFastMatcher
/*      */   {
/*      */     static final String TABLE = " 　\r   　 \013　   　 \t     \f 　 　　 \n 　";
/*      */     
/*      */     static final int MULTIPLIER = 1682554634;
/*      */     
/* 1218 */     static final int SHIFT = Integer.numberOfLeadingZeros(" 　\r   　 \013　   　 \t     \f 　 　　 \n 　".length() - 1);
/*      */     
/* 1220 */     static final Whitespace INSTANCE = new Whitespace();
/*      */     
/*      */     Whitespace() {
/* 1223 */       super("CharMatcher.whitespace()");
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1228 */       return (" 　\r   　 \013　   　 \t     \f 　 　　 \n 　".charAt(1682554634 * c >>> SHIFT) == c);
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     void setBits(BitSet table) {
/* 1234 */       for (int i = 0; i < " 　\r   　 \013　   　 \t     \f 　 　　 \n 　".length(); i++) {
/* 1235 */         table.set(" 　\r   　 \013　   　 \t     \f 　 　　 \n 　".charAt(i));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class BreakingWhitespace
/*      */     extends CharMatcher
/*      */   {
/* 1243 */     static final CharMatcher INSTANCE = new BreakingWhitespace();
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1247 */       switch (c) {
/*      */         case '\t':
/*      */         case '\n':
/*      */         case '\013':
/*      */         case '\f':
/*      */         case '\r':
/*      */         case ' ':
/*      */         case '':
/*      */         case ' ':
/*      */         case ' ':
/*      */         case ' ':
/*      */         case ' ':
/*      */         case '　':
/* 1260 */           return true;
/*      */         case ' ':
/* 1262 */           return false;
/*      */       } 
/* 1264 */       return (c >= ' ' && c <= ' ');
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1270 */       return "CharMatcher.breakingWhitespace()";
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class Ascii
/*      */     extends NamedFastMatcher
/*      */   {
/* 1277 */     static final Ascii INSTANCE = new Ascii();
/*      */     
/*      */     Ascii() {
/* 1280 */       super("CharMatcher.ascii()");
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1285 */       return (c <= '');
/*      */     }
/*      */   }
/*      */   
/*      */   private static class RangesMatcher
/*      */     extends CharMatcher
/*      */   {
/*      */     private final String description;
/*      */     private final char[] rangeStarts;
/*      */     private final char[] rangeEnds;
/*      */     
/*      */     RangesMatcher(String description, char[] rangeStarts, char[] rangeEnds) {
/* 1297 */       this.description = description;
/* 1298 */       this.rangeStarts = rangeStarts;
/* 1299 */       this.rangeEnds = rangeEnds;
/* 1300 */       Preconditions.checkArgument((rangeStarts.length == rangeEnds.length));
/* 1301 */       for (int i = 0; i < rangeStarts.length; i++) {
/* 1302 */         Preconditions.checkArgument((rangeStarts[i] <= rangeEnds[i]));
/* 1303 */         if (i + 1 < rangeStarts.length) {
/* 1304 */           Preconditions.checkArgument((rangeEnds[i] < rangeStarts[i + 1]));
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1311 */       int index = Arrays.binarySearch(this.rangeStarts, c);
/* 1312 */       if (index >= 0) {
/* 1313 */         return true;
/*      */       }
/* 1315 */       index = (index ^ 0xFFFFFFFF) - 1;
/* 1316 */       return (index >= 0 && c <= this.rangeEnds[index]);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1322 */       return this.description;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class Digit
/*      */     extends RangesMatcher
/*      */   {
/*      */     private static final String ZEROES = "0٠۰߀०০੦૦୦௦౦೦൦෦๐໐༠၀႐០᠐᥆᧐᪀᪐᭐᮰᱀᱐꘠꣐꤀꧐꧰꩐꯰０";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private static char[] zeroes() {
/* 1340 */       return "0٠۰߀०০੦૦୦௦౦೦൦෦๐໐༠၀႐០᠐᥆᧐᪀᪐᭐᮰᱀᱐꘠꣐꤀꧐꧰꩐꯰０".toCharArray();
/*      */     }
/*      */     
/*      */     private static char[] nines() {
/* 1344 */       char[] nines = new char["0٠۰߀०০੦૦୦௦౦೦൦෦๐໐༠၀႐០᠐᥆᧐᪀᪐᭐᮰᱀᱐꘠꣐꤀꧐꧰꩐꯰０".length()];
/* 1345 */       for (int i = 0; i < "0٠۰߀०০੦૦୦௦౦೦൦෦๐໐༠၀႐០᠐᥆᧐᪀᪐᭐᮰᱀᱐꘠꣐꤀꧐꧰꩐꯰０".length(); i++) {
/* 1346 */         nines[i] = (char)("0٠۰߀०০੦૦୦௦౦೦൦෦๐໐༠၀႐០᠐᥆᧐᪀᪐᭐᮰᱀᱐꘠꣐꤀꧐꧰꩐꯰０".charAt(i) + 9);
/*      */       }
/* 1348 */       return nines;
/*      */     }
/*      */     
/* 1351 */     static final Digit INSTANCE = new Digit();
/*      */     
/*      */     private Digit() {
/* 1354 */       super("CharMatcher.digit()", zeroes(), nines());
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class JavaDigit
/*      */     extends CharMatcher
/*      */   {
/* 1361 */     static final JavaDigit INSTANCE = new JavaDigit();
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1365 */       return Character.isDigit(c);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1370 */       return "CharMatcher.javaDigit()";
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class JavaLetter
/*      */     extends CharMatcher
/*      */   {
/* 1377 */     static final JavaLetter INSTANCE = new JavaLetter();
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1381 */       return Character.isLetter(c);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1386 */       return "CharMatcher.javaLetter()";
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class JavaLetterOrDigit
/*      */     extends CharMatcher
/*      */   {
/* 1393 */     static final JavaLetterOrDigit INSTANCE = new JavaLetterOrDigit();
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1397 */       return Character.isLetterOrDigit(c);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1402 */       return "CharMatcher.javaLetterOrDigit()";
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class JavaUpperCase
/*      */     extends CharMatcher
/*      */   {
/* 1409 */     static final JavaUpperCase INSTANCE = new JavaUpperCase();
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1413 */       return Character.isUpperCase(c);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1418 */       return "CharMatcher.javaUpperCase()";
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class JavaLowerCase
/*      */     extends CharMatcher
/*      */   {
/* 1425 */     static final JavaLowerCase INSTANCE = new JavaLowerCase();
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1429 */       return Character.isLowerCase(c);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1434 */       return "CharMatcher.javaLowerCase()";
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class JavaIsoControl
/*      */     extends NamedFastMatcher
/*      */   {
/* 1441 */     static final JavaIsoControl INSTANCE = new JavaIsoControl();
/*      */     
/*      */     private JavaIsoControl() {
/* 1444 */       super("CharMatcher.javaIsoControl()");
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1449 */       return (c <= '\037' || (c >= '' && c <= ''));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class Invisible
/*      */     extends RangesMatcher
/*      */   {
/*      */     private static final String RANGE_STARTS = "\000­؀؜۝܏࣢ ᠎   ⁦　?﻿￹";
/*      */ 
/*      */ 
/*      */     
/*      */     private static final String RANGE_ENDS = "  ­؅؜۝܏࣢ ᠎‏ ⁤⁯　﻿￻";
/*      */ 
/*      */     
/* 1466 */     static final Invisible INSTANCE = new Invisible();
/*      */     
/*      */     private Invisible() {
/* 1469 */       super("CharMatcher.invisible()", "\000­؀؜۝܏࣢ ᠎   ⁦　?﻿￹".toCharArray(), "  ­؅؜۝܏࣢ ᠎‏ ⁤⁯　﻿￻".toCharArray());
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class SingleWidth
/*      */     extends RangesMatcher
/*      */   {
/* 1476 */     static final SingleWidth INSTANCE = new SingleWidth();
/*      */     
/*      */     private SingleWidth() {
/* 1479 */       super("CharMatcher.singleWidth()", "\000־א׳؀ݐ฀Ḁ℀ﭐﹰ｡"
/*      */           
/* 1481 */           .toCharArray(), "ӹ־ת״ۿݿ๿₯℺﷿﻿ￜ"
/* 1482 */           .toCharArray());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class Negated
/*      */     extends CharMatcher
/*      */   {
/*      */     final CharMatcher original;
/*      */ 
/*      */     
/*      */     Negated(CharMatcher original) {
/* 1494 */       this.original = Preconditions.<CharMatcher>checkNotNull(original);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1499 */       return !this.original.matches(c);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matchesAllOf(CharSequence sequence) {
/* 1504 */       return this.original.matchesNoneOf(sequence);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matchesNoneOf(CharSequence sequence) {
/* 1509 */       return this.original.matchesAllOf(sequence);
/*      */     }
/*      */ 
/*      */     
/*      */     public int countIn(CharSequence sequence) {
/* 1514 */       return sequence.length() - this.original.countIn(sequence);
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     void setBits(BitSet table) {
/* 1520 */       BitSet tmp = new BitSet();
/* 1521 */       this.original.setBits(tmp);
/* 1522 */       tmp.flip(0, 65536);
/* 1523 */       table.or(tmp);
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher negate() {
/* 1528 */       return this.original;
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1533 */       return this.original + ".negate()";
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class And
/*      */     extends CharMatcher
/*      */   {
/*      */     final CharMatcher first;
/*      */     final CharMatcher second;
/*      */     
/*      */     And(CharMatcher a, CharMatcher b) {
/* 1544 */       this.first = Preconditions.<CharMatcher>checkNotNull(a);
/* 1545 */       this.second = Preconditions.<CharMatcher>checkNotNull(b);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1550 */       return (this.first.matches(c) && this.second.matches(c));
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     void setBits(BitSet table) {
/* 1556 */       BitSet tmp1 = new BitSet();
/* 1557 */       this.first.setBits(tmp1);
/* 1558 */       BitSet tmp2 = new BitSet();
/* 1559 */       this.second.setBits(tmp2);
/* 1560 */       tmp1.and(tmp2);
/* 1561 */       table.or(tmp1);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1566 */       return "CharMatcher.and(" + this.first + ", " + this.second + ")";
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class Or
/*      */     extends CharMatcher
/*      */   {
/*      */     final CharMatcher first;
/*      */     final CharMatcher second;
/*      */     
/*      */     Or(CharMatcher a, CharMatcher b) {
/* 1577 */       this.first = Preconditions.<CharMatcher>checkNotNull(a);
/* 1578 */       this.second = Preconditions.<CharMatcher>checkNotNull(b);
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     void setBits(BitSet table) {
/* 1584 */       this.first.setBits(table);
/* 1585 */       this.second.setBits(table);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1590 */       return (this.first.matches(c) || this.second.matches(c));
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1595 */       return "CharMatcher.or(" + this.first + ", " + this.second + ")";
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static final class Is
/*      */     extends FastMatcher
/*      */   {
/*      */     private final char match;
/*      */ 
/*      */     
/*      */     Is(char match) {
/* 1607 */       this.match = match;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1612 */       return (c == this.match);
/*      */     }
/*      */ 
/*      */     
/*      */     public String replaceFrom(CharSequence sequence, char replacement) {
/* 1617 */       return sequence.toString().replace(this.match, replacement);
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher and(CharMatcher other) {
/* 1622 */       return other.matches(this.match) ? this : none();
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher or(CharMatcher other) {
/* 1627 */       return other.matches(this.match) ? other : super.or(other);
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher negate() {
/* 1632 */       return isNot(this.match);
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     void setBits(BitSet table) {
/* 1638 */       table.set(this.match);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1643 */       return "CharMatcher.is('" + CharMatcher.showCharacter(this.match) + "')";
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class IsNot
/*      */     extends FastMatcher
/*      */   {
/*      */     private final char match;
/*      */     
/*      */     IsNot(char match) {
/* 1653 */       this.match = match;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1658 */       return (c != this.match);
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher and(CharMatcher other) {
/* 1663 */       return other.matches(this.match) ? super.and(other) : other;
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher or(CharMatcher other) {
/* 1668 */       return other.matches(this.match) ? any() : this;
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     void setBits(BitSet table) {
/* 1674 */       table.set(0, this.match);
/* 1675 */       table.set(this.match + 1, 65536);
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher negate() {
/* 1680 */       return is(this.match);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1685 */       return "CharMatcher.isNot('" + CharMatcher.showCharacter(this.match) + "')";
/*      */     }
/*      */   }
/*      */   
/*      */   private static IsEither isEither(char c1, char c2) {
/* 1690 */     return new IsEither(c1, c2);
/*      */   }
/*      */   
/*      */   public abstract boolean matches(char paramChar);
/*      */   
/*      */   private static final class IsEither extends FastMatcher {
/*      */     private final char match1;
/*      */     private final char match2;
/*      */     
/*      */     IsEither(char match1, char match2) {
/* 1700 */       this.match1 = match1;
/* 1701 */       this.match2 = match2;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1706 */       return (c == this.match1 || c == this.match2);
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     void setBits(BitSet table) {
/* 1712 */       table.set(this.match1);
/* 1713 */       table.set(this.match2);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1718 */       return "CharMatcher.anyOf(\"" + CharMatcher.showCharacter(this.match1) + CharMatcher.showCharacter(this.match2) + "\")";
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class AnyOf
/*      */     extends CharMatcher
/*      */   {
/*      */     private final char[] chars;
/*      */     
/*      */     public AnyOf(CharSequence chars) {
/* 1728 */       this.chars = chars.toString().toCharArray();
/* 1729 */       Arrays.sort(this.chars);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1734 */       return (Arrays.binarySearch(this.chars, c) >= 0);
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     void setBits(BitSet table) {
/* 1740 */       for (char c : this.chars) {
/* 1741 */         table.set(c);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1747 */       StringBuilder description = new StringBuilder("CharMatcher.anyOf(\"");
/* 1748 */       for (char c : this.chars) {
/* 1749 */         description.append(CharMatcher.showCharacter(c));
/*      */       }
/* 1751 */       description.append("\")");
/* 1752 */       return description.toString();
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class InRange
/*      */     extends FastMatcher
/*      */   {
/*      */     private final char startInclusive;
/*      */     private final char endInclusive;
/*      */     
/*      */     InRange(char startInclusive, char endInclusive) {
/* 1763 */       Preconditions.checkArgument((endInclusive >= startInclusive));
/* 1764 */       this.startInclusive = startInclusive;
/* 1765 */       this.endInclusive = endInclusive;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1770 */       return (this.startInclusive <= c && c <= this.endInclusive);
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     void setBits(BitSet table) {
/* 1776 */       table.set(this.startInclusive, this.endInclusive + 1);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1781 */       return "CharMatcher.inRange('" + CharMatcher
/* 1782 */         .showCharacter(this.startInclusive) + "', '" + CharMatcher
/*      */         
/* 1784 */         .showCharacter(this.endInclusive) + "')";
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static final class ForPredicate
/*      */     extends CharMatcher
/*      */   {
/*      */     private final Predicate<? super Character> predicate;
/*      */     
/*      */     ForPredicate(Predicate<? super Character> predicate) {
/* 1795 */       this.predicate = Preconditions.<Predicate<? super Character>>checkNotNull(predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1800 */       return this.predicate.apply(Character.valueOf(c));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean apply(Character character) {
/* 1806 */       return this.predicate.apply(Preconditions.checkNotNull(character));
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1811 */       return "CharMatcher.forPredicate(" + this.predicate + ")";
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\base\CharMatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */