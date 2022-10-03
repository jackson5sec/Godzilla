/*     */ package com.jediterm.terminal.model;
/*     */ 
/*     */ import com.jediterm.terminal.util.CharUtils;
/*     */ import com.jediterm.terminal.util.Pair;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CharBuffer
/*     */   implements Iterable<Character>, CharSequence
/*     */ {
/*  16 */   public static final CharBuffer EMPTY = new CharBuffer(new char[0], 0, 0);
/*     */   
/*     */   private final char[] myBuf;
/*     */   private final int myStart;
/*     */   private final int myLength;
/*     */   
/*     */   public CharBuffer(@NotNull char[] buf, int start, int length) {
/*  23 */     if (start + length > buf.length) {
/*  24 */       throw new IllegalArgumentException(String.format("Out ouf bounds %d+%d>%d", new Object[] { Integer.valueOf(start), Integer.valueOf(length), Integer.valueOf(buf.length) }));
/*     */     }
/*  26 */     this.myBuf = buf;
/*  27 */     this.myStart = start;
/*  28 */     this.myLength = length;
/*     */     
/*  30 */     if (this.myLength < 0) {
/*  31 */       throw new IllegalStateException("Length can't be negative: " + this.myLength);
/*     */     }
/*     */     
/*  34 */     if (this.myStart < 0) {
/*  35 */       throw new IllegalStateException("Start position can't be negative: " + this.myStart);
/*     */     }
/*     */     
/*  38 */     if (this.myStart + this.myLength > this.myBuf.length) {
/*  39 */       throw new IllegalStateException(String.format("Interval is out of array bounds: %d+%d>%d", new Object[] { Integer.valueOf(this.myStart), Integer.valueOf(this.myLength), Integer.valueOf(this.myBuf.length) }));
/*     */     }
/*     */   }
/*     */   
/*     */   public CharBuffer(char c, int count) {
/*  44 */     this(new char[count], 0, count);
/*  45 */     assert !CharUtils.isDoubleWidthCharacter(c, false);
/*  46 */     Arrays.fill(this.myBuf, c);
/*     */   }
/*     */   
/*     */   public CharBuffer(@NotNull String str) {
/*  50 */     this(str.toCharArray(), 0, str.length());
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<Character> iterator() {
/*  55 */     return new Iterator<Character>() {
/*  56 */         private int myCurPosition = CharBuffer.this.myStart;
/*     */ 
/*     */         
/*     */         public boolean hasNext() {
/*  60 */           return (this.myCurPosition < CharBuffer.this.myBuf.length && this.myCurPosition < CharBuffer.this.myStart + CharBuffer.this.myLength);
/*     */         }
/*     */ 
/*     */         
/*     */         public Character next() {
/*  65 */           return Character.valueOf(CharBuffer.this.myBuf[this.myCurPosition]);
/*     */         }
/*     */ 
/*     */         
/*     */         public void remove() {
/*  70 */           throw new IllegalStateException("Can't remove from buffer");
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public char[] getBuf() {
/*  76 */     return this.myBuf;
/*     */   }
/*     */   
/*     */   public int getStart() {
/*  80 */     return this.myStart;
/*     */   }
/*     */   
/*     */   public CharBuffer subBuffer(int start, int length) {
/*  84 */     return new CharBuffer(this.myBuf, getStart() + start, length);
/*     */   }
/*     */   
/*     */   public CharBuffer subBuffer(Pair<Integer, Integer> range) {
/*  88 */     return new CharBuffer(this.myBuf, getStart() + ((Integer)range.first).intValue(), ((Integer)range.second).intValue() - ((Integer)range.first).intValue());
/*     */   }
/*     */   
/*     */   public boolean isNul() {
/*  92 */     return (this.myLength > 0 && this.myBuf[0] == '\000');
/*     */   }
/*     */   
/*     */   public void unNullify() {
/*  96 */     Arrays.fill(this.myBuf, ' ');
/*     */   }
/*     */ 
/*     */   
/*     */   public int length() {
/* 101 */     return this.myLength;
/*     */   }
/*     */ 
/*     */   
/*     */   public char charAt(int index) {
/* 106 */     return this.myBuf[this.myStart + index];
/*     */   }
/*     */ 
/*     */   
/*     */   public CharSequence subSequence(int start, int end) {
/* 111 */     return new CharBuffer(this.myBuf, this.myStart + start, end - start);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 116 */     return new String(this.myBuf, this.myStart, this.myLength);
/*     */   }
/*     */   
/*     */   public CharBuffer clone() {
/* 120 */     char[] newBuf = Arrays.copyOfRange(this.myBuf, this.myStart, this.myStart + this.myLength);
/*     */     
/* 122 */     return new CharBuffer(newBuf, 0, this.myLength);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\model\CharBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */