/*      */ package com.google.common.io;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Ascii;
/*      */ import com.google.common.base.Objects;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.math.IntMath;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.Reader;
/*      */ import java.io.Writer;
/*      */ import java.math.RoundingMode;
/*      */ import java.util.Arrays;
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
/*      */ public abstract class BaseEncoding
/*      */ {
/*      */   public static final class DecodingException
/*      */     extends IOException
/*      */   {
/*      */     DecodingException(String message) {
/*  138 */       super(message);
/*      */     }
/*      */     
/*      */     DecodingException(Throwable cause) {
/*  142 */       super(cause);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public String encode(byte[] bytes) {
/*  148 */     return encode(bytes, 0, bytes.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final String encode(byte[] bytes, int off, int len) {
/*  156 */     Preconditions.checkPositionIndexes(off, off + len, bytes.length);
/*  157 */     StringBuilder result = new StringBuilder(maxEncodedSize(len));
/*      */     try {
/*  159 */       encodeTo(result, bytes, off, len);
/*  160 */     } catch (IOException impossible) {
/*  161 */       throw new AssertionError(impossible);
/*      */     } 
/*  163 */     return result.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public abstract OutputStream encodingStream(Writer paramWriter);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public final ByteSink encodingSink(final CharSink encodedSink) {
/*  179 */     Preconditions.checkNotNull(encodedSink);
/*  180 */     return new ByteSink()
/*      */       {
/*      */         public OutputStream openStream() throws IOException {
/*  183 */           return BaseEncoding.this.encodingStream(encodedSink.openStream());
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte[] extract(byte[] result, int length) {
/*  191 */     if (length == result.length) {
/*  192 */       return result;
/*      */     }
/*  194 */     byte[] trunc = new byte[length];
/*  195 */     System.arraycopy(result, 0, trunc, 0, length);
/*  196 */     return trunc;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract boolean canDecode(CharSequence paramCharSequence);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final byte[] decode(CharSequence chars) {
/*      */     try {
/*  217 */       return decodeChecked(chars);
/*  218 */     } catch (DecodingException badInput) {
/*  219 */       throw new IllegalArgumentException(badInput);
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
/*      */   final byte[] decodeChecked(CharSequence chars) throws DecodingException {
/*  231 */     chars = trimTrailingPadding(chars);
/*  232 */     byte[] tmp = new byte[maxDecodedSize(chars.length())];
/*  233 */     int len = decodeTo(tmp, chars);
/*  234 */     return extract(tmp, len);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public abstract InputStream decodingStream(Reader paramReader);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public final ByteSource decodingSource(final CharSource encodedSource) {
/*  250 */     Preconditions.checkNotNull(encodedSource);
/*  251 */     return new ByteSource()
/*      */       {
/*      */         public InputStream openStream() throws IOException {
/*  254 */           return BaseEncoding.this.decodingStream(encodedSource.openStream());
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   abstract int maxEncodedSize(int paramInt);
/*      */ 
/*      */   
/*      */   abstract void encodeTo(Appendable paramAppendable, byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException;
/*      */   
/*      */   abstract int maxDecodedSize(int paramInt);
/*      */   
/*      */   abstract int decodeTo(byte[] paramArrayOfbyte, CharSequence paramCharSequence) throws DecodingException;
/*      */   
/*      */   CharSequence trimTrailingPadding(CharSequence chars) {
/*  270 */     return (CharSequence)Preconditions.checkNotNull(chars);
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
/*  320 */   private static final BaseEncoding BASE64 = new Base64Encoding("base64()", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/", 
/*      */       
/*  322 */       Character.valueOf('='));
/*      */ 
/*      */   
/*      */   public abstract BaseEncoding omitPadding();
/*      */ 
/*      */   
/*      */   public abstract BaseEncoding withPadChar(char paramChar);
/*      */ 
/*      */   
/*      */   public abstract BaseEncoding withSeparator(String paramString, int paramInt);
/*      */   
/*      */   public abstract BaseEncoding upperCase();
/*      */   
/*      */   public abstract BaseEncoding lowerCase();
/*      */   
/*      */   public static BaseEncoding base64() {
/*  338 */     return BASE64;
/*      */   }
/*      */   
/*  341 */   private static final BaseEncoding BASE64_URL = new Base64Encoding("base64Url()", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_", 
/*      */       
/*  343 */       Character.valueOf('='));
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
/*      */   public static BaseEncoding base64Url() {
/*  360 */     return BASE64_URL;
/*      */   }
/*      */   
/*  363 */   private static final BaseEncoding BASE32 = new StandardBaseEncoding("base32()", "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567", 
/*  364 */       Character.valueOf('='));
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
/*      */   public static BaseEncoding base32() {
/*  379 */     return BASE32;
/*      */   }
/*      */   
/*  382 */   private static final BaseEncoding BASE32_HEX = new StandardBaseEncoding("base32Hex()", "0123456789ABCDEFGHIJKLMNOPQRSTUV", 
/*  383 */       Character.valueOf('='));
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
/*      */   public static BaseEncoding base32Hex() {
/*  398 */     return BASE32_HEX;
/*      */   }
/*      */   
/*  401 */   private static final BaseEncoding BASE16 = new Base16Encoding("base16()", "0123456789ABCDEF");
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
/*      */   public static BaseEncoding base16() {
/*  417 */     return BASE16;
/*      */   }
/*      */   
/*      */   private static final class Alphabet
/*      */   {
/*      */     private final String name;
/*      */     private final char[] chars;
/*      */     final int mask;
/*      */     final int bitsPerChar;
/*      */     final int charsPerChunk;
/*      */     final int bytesPerChunk;
/*      */     private final byte[] decodabet;
/*      */     private final boolean[] validPadding;
/*      */     
/*      */     Alphabet(String name, char[] chars) {
/*  432 */       this.name = (String)Preconditions.checkNotNull(name);
/*  433 */       this.chars = (char[])Preconditions.checkNotNull(chars);
/*      */       try {
/*  435 */         this.bitsPerChar = IntMath.log2(chars.length, RoundingMode.UNNECESSARY);
/*  436 */       } catch (ArithmeticException e) {
/*  437 */         throw new IllegalArgumentException("Illegal alphabet length " + chars.length, e);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  444 */       int gcd = Math.min(8, Integer.lowestOneBit(this.bitsPerChar));
/*      */       try {
/*  446 */         this.charsPerChunk = 8 / gcd;
/*  447 */         this.bytesPerChunk = this.bitsPerChar / gcd;
/*  448 */       } catch (ArithmeticException e) {
/*  449 */         throw new IllegalArgumentException("Illegal alphabet " + new String(chars), e);
/*      */       } 
/*      */       
/*  452 */       this.mask = chars.length - 1;
/*      */       
/*  454 */       byte[] decodabet = new byte[128];
/*  455 */       Arrays.fill(decodabet, (byte)-1);
/*  456 */       for (int i = 0; i < chars.length; i++) {
/*  457 */         char c = chars[i];
/*  458 */         Preconditions.checkArgument((c < decodabet.length), "Non-ASCII character: %s", c);
/*  459 */         Preconditions.checkArgument((decodabet[c] == -1), "Duplicate character: %s", c);
/*  460 */         decodabet[c] = (byte)i;
/*      */       } 
/*  462 */       this.decodabet = decodabet;
/*      */       
/*  464 */       boolean[] validPadding = new boolean[this.charsPerChunk];
/*  465 */       for (int j = 0; j < this.bytesPerChunk; j++) {
/*  466 */         validPadding[IntMath.divide(j * 8, this.bitsPerChar, RoundingMode.CEILING)] = true;
/*      */       }
/*  468 */       this.validPadding = validPadding;
/*      */     }
/*      */     
/*      */     char encode(int bits) {
/*  472 */       return this.chars[bits];
/*      */     }
/*      */     
/*      */     boolean isValidPaddingStartPosition(int index) {
/*  476 */       return this.validPadding[index % this.charsPerChunk];
/*      */     }
/*      */     
/*      */     boolean canDecode(char ch) {
/*  480 */       return (ch <= '' && this.decodabet[ch] != -1);
/*      */     }
/*      */     
/*      */     int decode(char ch) throws BaseEncoding.DecodingException {
/*  484 */       if (ch > '') {
/*  485 */         throw new BaseEncoding.DecodingException("Unrecognized character: 0x" + Integer.toHexString(ch));
/*      */       }
/*  487 */       int result = this.decodabet[ch];
/*  488 */       if (result == -1) {
/*  489 */         if (ch <= ' ' || ch == '') {
/*  490 */           throw new BaseEncoding.DecodingException("Unrecognized character: 0x" + Integer.toHexString(ch));
/*      */         }
/*  492 */         throw new BaseEncoding.DecodingException("Unrecognized character: " + ch);
/*      */       } 
/*      */       
/*  495 */       return result;
/*      */     }
/*      */     
/*      */     private boolean hasLowerCase() {
/*  499 */       for (char c : this.chars) {
/*  500 */         if (Ascii.isLowerCase(c)) {
/*  501 */           return true;
/*      */         }
/*      */       } 
/*  504 */       return false;
/*      */     }
/*      */     
/*      */     private boolean hasUpperCase() {
/*  508 */       for (char c : this.chars) {
/*  509 */         if (Ascii.isUpperCase(c)) {
/*  510 */           return true;
/*      */         }
/*      */       } 
/*  513 */       return false;
/*      */     }
/*      */     
/*      */     Alphabet upperCase() {
/*  517 */       if (!hasLowerCase()) {
/*  518 */         return this;
/*      */       }
/*  520 */       Preconditions.checkState(!hasUpperCase(), "Cannot call upperCase() on a mixed-case alphabet");
/*  521 */       char[] upperCased = new char[this.chars.length];
/*  522 */       for (int i = 0; i < this.chars.length; i++) {
/*  523 */         upperCased[i] = Ascii.toUpperCase(this.chars[i]);
/*      */       }
/*  525 */       return new Alphabet(this.name + ".upperCase()", upperCased);
/*      */     }
/*      */ 
/*      */     
/*      */     Alphabet lowerCase() {
/*  530 */       if (!hasUpperCase()) {
/*  531 */         return this;
/*      */       }
/*  533 */       Preconditions.checkState(!hasLowerCase(), "Cannot call lowerCase() on a mixed-case alphabet");
/*  534 */       char[] lowerCased = new char[this.chars.length];
/*  535 */       for (int i = 0; i < this.chars.length; i++) {
/*  536 */         lowerCased[i] = Ascii.toLowerCase(this.chars[i]);
/*      */       }
/*  538 */       return new Alphabet(this.name + ".lowerCase()", lowerCased);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/*  543 */       return (c < this.decodabet.length && this.decodabet[c] != -1);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  548 */       return this.name;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object other) {
/*  553 */       if (other instanceof Alphabet) {
/*  554 */         Alphabet that = (Alphabet)other;
/*  555 */         return Arrays.equals(this.chars, that.chars);
/*      */       } 
/*  557 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  562 */       return Arrays.hashCode(this.chars);
/*      */     }
/*      */   }
/*      */   
/*      */   static class StandardBaseEncoding extends BaseEncoding {
/*      */     final BaseEncoding.Alphabet alphabet;
/*      */     final Character paddingChar;
/*      */     private transient BaseEncoding upperCase;
/*      */     private transient BaseEncoding lowerCase;
/*      */     
/*      */     StandardBaseEncoding(String name, String alphabetChars, Character paddingChar) {
/*  573 */       this(new BaseEncoding.Alphabet(name, alphabetChars.toCharArray()), paddingChar);
/*      */     }
/*      */     
/*      */     StandardBaseEncoding(BaseEncoding.Alphabet alphabet, Character paddingChar) {
/*  577 */       this.alphabet = (BaseEncoding.Alphabet)Preconditions.checkNotNull(alphabet);
/*  578 */       Preconditions.checkArgument((paddingChar == null || 
/*  579 */           !alphabet.matches(paddingChar.charValue())), "Padding character %s was already in alphabet", paddingChar);
/*      */ 
/*      */       
/*  582 */       this.paddingChar = paddingChar;
/*      */     }
/*      */ 
/*      */     
/*      */     int maxEncodedSize(int bytes) {
/*  587 */       return this.alphabet.charsPerChunk * IntMath.divide(bytes, this.alphabet.bytesPerChunk, RoundingMode.CEILING);
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     public OutputStream encodingStream(final Writer out) {
/*  593 */       Preconditions.checkNotNull(out);
/*  594 */       return new OutputStream() {
/*  595 */           int bitBuffer = 0;
/*  596 */           int bitBufferLength = 0;
/*  597 */           int writtenChars = 0;
/*      */ 
/*      */           
/*      */           public void write(int b) throws IOException {
/*  601 */             this.bitBuffer <<= 8;
/*  602 */             this.bitBuffer |= b & 0xFF;
/*  603 */             this.bitBufferLength += 8;
/*  604 */             while (this.bitBufferLength >= BaseEncoding.StandardBaseEncoding.this.alphabet.bitsPerChar) {
/*  605 */               int charIndex = this.bitBuffer >> this.bitBufferLength - BaseEncoding.StandardBaseEncoding.this.alphabet.bitsPerChar & BaseEncoding.StandardBaseEncoding.this.alphabet.mask;
/*  606 */               out.write(BaseEncoding.StandardBaseEncoding.this.alphabet.encode(charIndex));
/*  607 */               this.writtenChars++;
/*  608 */               this.bitBufferLength -= BaseEncoding.StandardBaseEncoding.this.alphabet.bitsPerChar;
/*      */             } 
/*      */           }
/*      */ 
/*      */           
/*      */           public void flush() throws IOException {
/*  614 */             out.flush();
/*      */           }
/*      */ 
/*      */           
/*      */           public void close() throws IOException {
/*  619 */             if (this.bitBufferLength > 0) {
/*  620 */               int charIndex = this.bitBuffer << BaseEncoding.StandardBaseEncoding.this.alphabet.bitsPerChar - this.bitBufferLength & BaseEncoding.StandardBaseEncoding.this.alphabet.mask;
/*  621 */               out.write(BaseEncoding.StandardBaseEncoding.this.alphabet.encode(charIndex));
/*  622 */               this.writtenChars++;
/*  623 */               if (BaseEncoding.StandardBaseEncoding.this.paddingChar != null) {
/*  624 */                 while (this.writtenChars % BaseEncoding.StandardBaseEncoding.this.alphabet.charsPerChunk != 0) {
/*  625 */                   out.write(BaseEncoding.StandardBaseEncoding.this.paddingChar.charValue());
/*  626 */                   this.writtenChars++;
/*      */                 } 
/*      */               }
/*      */             } 
/*  630 */             out.close();
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/*      */     void encodeTo(Appendable target, byte[] bytes, int off, int len) throws IOException {
/*  637 */       Preconditions.checkNotNull(target);
/*  638 */       Preconditions.checkPositionIndexes(off, off + len, bytes.length); int i;
/*  639 */       for (i = 0; i < len; i += this.alphabet.bytesPerChunk) {
/*  640 */         encodeChunkTo(target, bytes, off + i, Math.min(this.alphabet.bytesPerChunk, len - i));
/*      */       }
/*      */     }
/*      */     
/*      */     void encodeChunkTo(Appendable target, byte[] bytes, int off, int len) throws IOException {
/*  645 */       Preconditions.checkNotNull(target);
/*  646 */       Preconditions.checkPositionIndexes(off, off + len, bytes.length);
/*  647 */       Preconditions.checkArgument((len <= this.alphabet.bytesPerChunk));
/*  648 */       long bitBuffer = 0L;
/*  649 */       for (int i = 0; i < len; i++) {
/*  650 */         bitBuffer |= (bytes[off + i] & 0xFF);
/*  651 */         bitBuffer <<= 8L;
/*      */       } 
/*      */       
/*  654 */       int bitOffset = (len + 1) * 8 - this.alphabet.bitsPerChar;
/*  655 */       int bitsProcessed = 0;
/*  656 */       while (bitsProcessed < len * 8) {
/*  657 */         int charIndex = (int)(bitBuffer >>> bitOffset - bitsProcessed) & this.alphabet.mask;
/*  658 */         target.append(this.alphabet.encode(charIndex));
/*  659 */         bitsProcessed += this.alphabet.bitsPerChar;
/*      */       } 
/*  661 */       if (this.paddingChar != null) {
/*  662 */         while (bitsProcessed < this.alphabet.bytesPerChunk * 8) {
/*  663 */           target.append(this.paddingChar.charValue());
/*  664 */           bitsProcessed += this.alphabet.bitsPerChar;
/*      */         } 
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     int maxDecodedSize(int chars) {
/*  671 */       return (int)((this.alphabet.bitsPerChar * chars + 7L) / 8L);
/*      */     }
/*      */ 
/*      */     
/*      */     CharSequence trimTrailingPadding(CharSequence chars) {
/*  676 */       Preconditions.checkNotNull(chars);
/*  677 */       if (this.paddingChar == null) {
/*  678 */         return chars;
/*      */       }
/*  680 */       char padChar = this.paddingChar.charValue();
/*      */       int l;
/*  682 */       for (l = chars.length() - 1; l >= 0 && 
/*  683 */         chars.charAt(l) == padChar; l--);
/*      */ 
/*      */ 
/*      */       
/*  687 */       return chars.subSequence(0, l + 1);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canDecode(CharSequence chars) {
/*  692 */       Preconditions.checkNotNull(chars);
/*  693 */       chars = trimTrailingPadding(chars);
/*  694 */       if (!this.alphabet.isValidPaddingStartPosition(chars.length())) {
/*  695 */         return false;
/*      */       }
/*  697 */       for (int i = 0; i < chars.length(); i++) {
/*  698 */         if (!this.alphabet.canDecode(chars.charAt(i))) {
/*  699 */           return false;
/*      */         }
/*      */       } 
/*  702 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     int decodeTo(byte[] target, CharSequence chars) throws BaseEncoding.DecodingException {
/*  707 */       Preconditions.checkNotNull(target);
/*  708 */       chars = trimTrailingPadding(chars);
/*  709 */       if (!this.alphabet.isValidPaddingStartPosition(chars.length())) {
/*  710 */         throw new BaseEncoding.DecodingException("Invalid input length " + chars.length());
/*      */       }
/*  712 */       int bytesWritten = 0; int charIdx;
/*  713 */       for (charIdx = 0; charIdx < chars.length(); charIdx += this.alphabet.charsPerChunk) {
/*  714 */         long chunk = 0L;
/*  715 */         int charsProcessed = 0;
/*  716 */         for (int i = 0; i < this.alphabet.charsPerChunk; i++) {
/*  717 */           chunk <<= this.alphabet.bitsPerChar;
/*  718 */           if (charIdx + i < chars.length()) {
/*  719 */             chunk |= this.alphabet.decode(chars.charAt(charIdx + charsProcessed++));
/*      */           }
/*      */         } 
/*  722 */         int minOffset = this.alphabet.bytesPerChunk * 8 - charsProcessed * this.alphabet.bitsPerChar;
/*  723 */         for (int offset = (this.alphabet.bytesPerChunk - 1) * 8; offset >= minOffset; offset -= 8) {
/*  724 */           target[bytesWritten++] = (byte)(int)(chunk >>> offset & 0xFFL);
/*      */         }
/*      */       } 
/*  727 */       return bytesWritten;
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     public InputStream decodingStream(final Reader reader) {
/*  733 */       Preconditions.checkNotNull(reader);
/*  734 */       return new InputStream() {
/*  735 */           int bitBuffer = 0;
/*  736 */           int bitBufferLength = 0;
/*  737 */           int readChars = 0;
/*      */           
/*      */           boolean hitPadding = false;
/*      */           
/*      */           public int read() throws IOException {
/*      */             while (true) {
/*  743 */               int readChar = reader.read();
/*  744 */               if (readChar == -1) {
/*  745 */                 if (!this.hitPadding && !BaseEncoding.StandardBaseEncoding.this.alphabet.isValidPaddingStartPosition(this.readChars)) {
/*  746 */                   throw new BaseEncoding.DecodingException("Invalid input length " + this.readChars);
/*      */                 }
/*  748 */                 return -1;
/*      */               } 
/*  750 */               this.readChars++;
/*  751 */               char ch = (char)readChar;
/*  752 */               if (BaseEncoding.StandardBaseEncoding.this.paddingChar != null && BaseEncoding.StandardBaseEncoding.this.paddingChar.charValue() == ch) {
/*  753 */                 if (!this.hitPadding && (this.readChars == 1 || 
/*  754 */                   !BaseEncoding.StandardBaseEncoding.this.alphabet.isValidPaddingStartPosition(this.readChars - 1))) {
/*  755 */                   throw new BaseEncoding.DecodingException("Padding cannot start at index " + this.readChars);
/*      */                 }
/*  757 */                 this.hitPadding = true; continue;
/*  758 */               }  if (this.hitPadding) {
/*  759 */                 throw new BaseEncoding.DecodingException("Expected padding character but found '" + ch + "' at index " + this.readChars);
/*      */               }
/*      */               
/*  762 */               this.bitBuffer <<= BaseEncoding.StandardBaseEncoding.this.alphabet.bitsPerChar;
/*  763 */               this.bitBuffer |= BaseEncoding.StandardBaseEncoding.this.alphabet.decode(ch);
/*  764 */               this.bitBufferLength += BaseEncoding.StandardBaseEncoding.this.alphabet.bitsPerChar;
/*      */               
/*  766 */               if (this.bitBufferLength >= 8) {
/*  767 */                 this.bitBufferLength -= 8;
/*  768 */                 return this.bitBuffer >> this.bitBufferLength & 0xFF;
/*      */               } 
/*      */             } 
/*      */           }
/*      */ 
/*      */ 
/*      */           
/*      */           public void close() throws IOException {
/*  776 */             reader.close();
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/*      */     public BaseEncoding omitPadding() {
/*  783 */       return (this.paddingChar == null) ? this : newInstance(this.alphabet, null);
/*      */     }
/*      */ 
/*      */     
/*      */     public BaseEncoding withPadChar(char padChar) {
/*  788 */       if (8 % this.alphabet.bitsPerChar == 0 || (this.paddingChar != null && this.paddingChar
/*  789 */         .charValue() == padChar)) {
/*  790 */         return this;
/*      */       }
/*  792 */       return newInstance(this.alphabet, Character.valueOf(padChar));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public BaseEncoding withSeparator(String separator, int afterEveryChars) {
/*  798 */       for (int i = 0; i < separator.length(); i++) {
/*  799 */         Preconditions.checkArgument(
/*  800 */             !this.alphabet.matches(separator.charAt(i)), "Separator (%s) cannot contain alphabet characters", separator);
/*      */       }
/*      */ 
/*      */       
/*  804 */       if (this.paddingChar != null) {
/*  805 */         Preconditions.checkArgument(
/*  806 */             (separator.indexOf(this.paddingChar.charValue()) < 0), "Separator (%s) cannot contain padding character", separator);
/*      */       }
/*      */ 
/*      */       
/*  810 */       return new BaseEncoding.SeparatedBaseEncoding(this, separator, afterEveryChars);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public BaseEncoding upperCase() {
/*  818 */       BaseEncoding result = this.upperCase;
/*  819 */       if (result == null) {
/*  820 */         BaseEncoding.Alphabet upper = this.alphabet.upperCase();
/*  821 */         result = this.upperCase = (upper == this.alphabet) ? this : newInstance(upper, this.paddingChar);
/*      */       } 
/*  823 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public BaseEncoding lowerCase() {
/*  828 */       BaseEncoding result = this.lowerCase;
/*  829 */       if (result == null) {
/*  830 */         BaseEncoding.Alphabet lower = this.alphabet.lowerCase();
/*  831 */         result = this.lowerCase = (lower == this.alphabet) ? this : newInstance(lower, this.paddingChar);
/*      */       } 
/*  833 */       return result;
/*      */     }
/*      */     
/*      */     BaseEncoding newInstance(BaseEncoding.Alphabet alphabet, Character paddingChar) {
/*  837 */       return new StandardBaseEncoding(alphabet, paddingChar);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  842 */       StringBuilder builder = new StringBuilder("BaseEncoding.");
/*  843 */       builder.append(this.alphabet.toString());
/*  844 */       if (8 % this.alphabet.bitsPerChar != 0) {
/*  845 */         if (this.paddingChar == null) {
/*  846 */           builder.append(".omitPadding()");
/*      */         } else {
/*  848 */           builder.append(".withPadChar('").append(this.paddingChar).append("')");
/*      */         } 
/*      */       }
/*  851 */       return builder.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object other) {
/*  856 */       if (other instanceof StandardBaseEncoding) {
/*  857 */         StandardBaseEncoding that = (StandardBaseEncoding)other;
/*  858 */         return (this.alphabet.equals(that.alphabet) && 
/*  859 */           Objects.equal(this.paddingChar, that.paddingChar));
/*      */       } 
/*  861 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  866 */       return this.alphabet.hashCode() ^ Objects.hashCode(new Object[] { this.paddingChar });
/*      */     }
/*      */   }
/*      */   
/*      */   static final class Base16Encoding extends StandardBaseEncoding {
/*  871 */     final char[] encoding = new char[512];
/*      */     
/*      */     Base16Encoding(String name, String alphabetChars) {
/*  874 */       this(new BaseEncoding.Alphabet(name, alphabetChars.toCharArray()));
/*      */     }
/*      */     
/*      */     private Base16Encoding(BaseEncoding.Alphabet alphabet) {
/*  878 */       super(alphabet, null);
/*  879 */       Preconditions.checkArgument((alphabet.chars.length == 16));
/*  880 */       for (int i = 0; i < 256; i++) {
/*  881 */         this.encoding[i] = alphabet.encode(i >>> 4);
/*  882 */         this.encoding[i | 0x100] = alphabet.encode(i & 0xF);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     void encodeTo(Appendable target, byte[] bytes, int off, int len) throws IOException {
/*  888 */       Preconditions.checkNotNull(target);
/*  889 */       Preconditions.checkPositionIndexes(off, off + len, bytes.length);
/*  890 */       for (int i = 0; i < len; i++) {
/*  891 */         int b = bytes[off + i] & 0xFF;
/*  892 */         target.append(this.encoding[b]);
/*  893 */         target.append(this.encoding[b | 0x100]);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     int decodeTo(byte[] target, CharSequence chars) throws BaseEncoding.DecodingException {
/*  899 */       Preconditions.checkNotNull(target);
/*  900 */       if (chars.length() % 2 == 1) {
/*  901 */         throw new BaseEncoding.DecodingException("Invalid input length " + chars.length());
/*      */       }
/*  903 */       int bytesWritten = 0;
/*  904 */       for (int i = 0; i < chars.length(); i += 2) {
/*  905 */         int decoded = this.alphabet.decode(chars.charAt(i)) << 4 | this.alphabet.decode(chars.charAt(i + 1));
/*  906 */         target[bytesWritten++] = (byte)decoded;
/*      */       } 
/*  908 */       return bytesWritten;
/*      */     }
/*      */ 
/*      */     
/*      */     BaseEncoding newInstance(BaseEncoding.Alphabet alphabet, Character paddingChar) {
/*  913 */       return new Base16Encoding(alphabet);
/*      */     }
/*      */   }
/*      */   
/*      */   static final class Base64Encoding extends StandardBaseEncoding {
/*      */     Base64Encoding(String name, String alphabetChars, Character paddingChar) {
/*  919 */       this(new BaseEncoding.Alphabet(name, alphabetChars.toCharArray()), paddingChar);
/*      */     }
/*      */     
/*      */     private Base64Encoding(BaseEncoding.Alphabet alphabet, Character paddingChar) {
/*  923 */       super(alphabet, paddingChar);
/*  924 */       Preconditions.checkArgument((alphabet.chars.length == 64));
/*      */     }
/*      */ 
/*      */     
/*      */     void encodeTo(Appendable target, byte[] bytes, int off, int len) throws IOException {
/*  929 */       Preconditions.checkNotNull(target);
/*  930 */       Preconditions.checkPositionIndexes(off, off + len, bytes.length);
/*  931 */       int i = off;
/*  932 */       for (int remaining = len; remaining >= 3; remaining -= 3) {
/*  933 */         int chunk = (bytes[i++] & 0xFF) << 16 | (bytes[i++] & 0xFF) << 8 | bytes[i++] & 0xFF;
/*  934 */         target.append(this.alphabet.encode(chunk >>> 18));
/*  935 */         target.append(this.alphabet.encode(chunk >>> 12 & 0x3F));
/*  936 */         target.append(this.alphabet.encode(chunk >>> 6 & 0x3F));
/*  937 */         target.append(this.alphabet.encode(chunk & 0x3F));
/*      */       } 
/*  939 */       if (i < off + len) {
/*  940 */         encodeChunkTo(target, bytes, i, off + len - i);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     int decodeTo(byte[] target, CharSequence chars) throws BaseEncoding.DecodingException {
/*  946 */       Preconditions.checkNotNull(target);
/*  947 */       chars = trimTrailingPadding(chars);
/*  948 */       if (!this.alphabet.isValidPaddingStartPosition(chars.length())) {
/*  949 */         throw new BaseEncoding.DecodingException("Invalid input length " + chars.length());
/*      */       }
/*  951 */       int bytesWritten = 0;
/*  952 */       for (int i = 0; i < chars.length(); ) {
/*  953 */         int chunk = this.alphabet.decode(chars.charAt(i++)) << 18;
/*  954 */         chunk |= this.alphabet.decode(chars.charAt(i++)) << 12;
/*  955 */         target[bytesWritten++] = (byte)(chunk >>> 16);
/*  956 */         if (i < chars.length()) {
/*  957 */           chunk |= this.alphabet.decode(chars.charAt(i++)) << 6;
/*  958 */           target[bytesWritten++] = (byte)(chunk >>> 8 & 0xFF);
/*  959 */           if (i < chars.length()) {
/*  960 */             chunk |= this.alphabet.decode(chars.charAt(i++));
/*  961 */             target[bytesWritten++] = (byte)(chunk & 0xFF);
/*      */           } 
/*      */         } 
/*      */       } 
/*  965 */       return bytesWritten;
/*      */     }
/*      */ 
/*      */     
/*      */     BaseEncoding newInstance(BaseEncoding.Alphabet alphabet, Character paddingChar) {
/*  970 */       return new Base64Encoding(alphabet, paddingChar);
/*      */     }
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   static Reader ignoringReader(final Reader delegate, final String toIgnore) {
/*  976 */     Preconditions.checkNotNull(delegate);
/*  977 */     Preconditions.checkNotNull(toIgnore);
/*  978 */     return new Reader()
/*      */       {
/*      */         public int read() throws IOException {
/*      */           int readChar;
/*      */           do {
/*  983 */             readChar = delegate.read();
/*  984 */           } while (readChar != -1 && toIgnore.indexOf((char)readChar) >= 0);
/*  985 */           return readChar;
/*      */         }
/*      */ 
/*      */         
/*      */         public int read(char[] cbuf, int off, int len) throws IOException {
/*  990 */           throw new UnsupportedOperationException();
/*      */         }
/*      */ 
/*      */         
/*      */         public void close() throws IOException {
/*  995 */           delegate.close();
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   static Appendable separatingAppendable(final Appendable delegate, final String separator, final int afterEveryChars) {
/* 1002 */     Preconditions.checkNotNull(delegate);
/* 1003 */     Preconditions.checkNotNull(separator);
/* 1004 */     Preconditions.checkArgument((afterEveryChars > 0));
/* 1005 */     return new Appendable() {
/* 1006 */         int charsUntilSeparator = afterEveryChars;
/*      */ 
/*      */         
/*      */         public Appendable append(char c) throws IOException {
/* 1010 */           if (this.charsUntilSeparator == 0) {
/* 1011 */             delegate.append(separator);
/* 1012 */             this.charsUntilSeparator = afterEveryChars;
/*      */           } 
/* 1014 */           delegate.append(c);
/* 1015 */           this.charsUntilSeparator--;
/* 1016 */           return this;
/*      */         }
/*      */ 
/*      */         
/*      */         public Appendable append(CharSequence chars, int off, int len) throws IOException {
/* 1021 */           throw new UnsupportedOperationException();
/*      */         }
/*      */ 
/*      */         
/*      */         public Appendable append(CharSequence chars) throws IOException {
/* 1026 */           throw new UnsupportedOperationException();
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   static Writer separatingWriter(final Writer delegate, String separator, int afterEveryChars) {
/* 1035 */     final Appendable seperatingAppendable = separatingAppendable(delegate, separator, afterEveryChars);
/* 1036 */     return new Writer()
/*      */       {
/*      */         public void write(int c) throws IOException {
/* 1039 */           seperatingAppendable.append((char)c);
/*      */         }
/*      */ 
/*      */         
/*      */         public void write(char[] chars, int off, int len) throws IOException {
/* 1044 */           throw new UnsupportedOperationException();
/*      */         }
/*      */ 
/*      */         
/*      */         public void flush() throws IOException {
/* 1049 */           delegate.flush();
/*      */         }
/*      */ 
/*      */         
/*      */         public void close() throws IOException {
/* 1054 */           delegate.close();
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   static final class SeparatedBaseEncoding extends BaseEncoding {
/*      */     private final BaseEncoding delegate;
/*      */     private final String separator;
/*      */     private final int afterEveryChars;
/*      */     
/*      */     SeparatedBaseEncoding(BaseEncoding delegate, String separator, int afterEveryChars) {
/* 1065 */       this.delegate = (BaseEncoding)Preconditions.checkNotNull(delegate);
/* 1066 */       this.separator = (String)Preconditions.checkNotNull(separator);
/* 1067 */       this.afterEveryChars = afterEveryChars;
/* 1068 */       Preconditions.checkArgument((afterEveryChars > 0), "Cannot add a separator after every %s chars", afterEveryChars);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     CharSequence trimTrailingPadding(CharSequence chars) {
/* 1074 */       return this.delegate.trimTrailingPadding(chars);
/*      */     }
/*      */ 
/*      */     
/*      */     int maxEncodedSize(int bytes) {
/* 1079 */       int unseparatedSize = this.delegate.maxEncodedSize(bytes);
/* 1080 */       return unseparatedSize + this.separator
/* 1081 */         .length() * IntMath.divide(Math.max(0, unseparatedSize - 1), this.afterEveryChars, RoundingMode.FLOOR);
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     public OutputStream encodingStream(Writer output) {
/* 1087 */       return this.delegate.encodingStream(separatingWriter(output, this.separator, this.afterEveryChars));
/*      */     }
/*      */ 
/*      */     
/*      */     void encodeTo(Appendable target, byte[] bytes, int off, int len) throws IOException {
/* 1092 */       this.delegate.encodeTo(separatingAppendable(target, this.separator, this.afterEveryChars), bytes, off, len);
/*      */     }
/*      */ 
/*      */     
/*      */     int maxDecodedSize(int chars) {
/* 1097 */       return this.delegate.maxDecodedSize(chars);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canDecode(CharSequence chars) {
/* 1102 */       StringBuilder builder = new StringBuilder();
/* 1103 */       for (int i = 0; i < chars.length(); i++) {
/* 1104 */         char c = chars.charAt(i);
/* 1105 */         if (this.separator.indexOf(c) < 0) {
/* 1106 */           builder.append(c);
/*      */         }
/*      */       } 
/* 1109 */       return this.delegate.canDecode(builder);
/*      */     }
/*      */ 
/*      */     
/*      */     int decodeTo(byte[] target, CharSequence chars) throws BaseEncoding.DecodingException {
/* 1114 */       StringBuilder stripped = new StringBuilder(chars.length());
/* 1115 */       for (int i = 0; i < chars.length(); i++) {
/* 1116 */         char c = chars.charAt(i);
/* 1117 */         if (this.separator.indexOf(c) < 0) {
/* 1118 */           stripped.append(c);
/*      */         }
/*      */       } 
/* 1121 */       return this.delegate.decodeTo(target, stripped);
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     public InputStream decodingStream(Reader reader) {
/* 1127 */       return this.delegate.decodingStream(ignoringReader(reader, this.separator));
/*      */     }
/*      */ 
/*      */     
/*      */     public BaseEncoding omitPadding() {
/* 1132 */       return this.delegate.omitPadding().withSeparator(this.separator, this.afterEveryChars);
/*      */     }
/*      */ 
/*      */     
/*      */     public BaseEncoding withPadChar(char padChar) {
/* 1137 */       return this.delegate.withPadChar(padChar).withSeparator(this.separator, this.afterEveryChars);
/*      */     }
/*      */ 
/*      */     
/*      */     public BaseEncoding withSeparator(String separator, int afterEveryChars) {
/* 1142 */       throw new UnsupportedOperationException("Already have a separator");
/*      */     }
/*      */ 
/*      */     
/*      */     public BaseEncoding upperCase() {
/* 1147 */       return this.delegate.upperCase().withSeparator(this.separator, this.afterEveryChars);
/*      */     }
/*      */ 
/*      */     
/*      */     public BaseEncoding lowerCase() {
/* 1152 */       return this.delegate.lowerCase().withSeparator(this.separator, this.afterEveryChars);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1157 */       return this.delegate + ".withSeparator(\"" + this.separator + "\", " + this.afterEveryChars + ")";
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\io\BaseEncoding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */