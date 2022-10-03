/*     */ package com.jediterm.terminal.emulator;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.jediterm.terminal.TerminalDataStream;
/*     */ import com.jediterm.terminal.util.CharUtils;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ControlSequence
/*     */ {
/*     */   private int myArgc;
/*     */   private int[] myArgv;
/*     */   private char myFinalChar;
/*     */   private ArrayList<Character> myUnhandledChars;
/*     */   private boolean myStartsWithQuestionMark = false;
/*     */   private boolean myStartsWithMoreMark = false;
/*  25 */   private final StringBuilder mySequenceString = new StringBuilder();
/*     */ 
/*     */   
/*     */   ControlSequence(TerminalDataStream channel) throws IOException {
/*  29 */     this.myArgv = new int[5];
/*  30 */     this.myArgc = 0;
/*     */     
/*  32 */     readControlSequence(channel);
/*     */   }
/*     */   
/*     */   private void readControlSequence(TerminalDataStream channel) throws IOException {
/*  36 */     this.myArgc = 0;
/*     */     
/*  38 */     int digit = 0;
/*  39 */     int seenDigit = 0;
/*  40 */     int pos = -1;
/*     */     
/*     */     while (true) {
/*  43 */       char b = channel.getChar();
/*  44 */       this.mySequenceString.append(b);
/*  45 */       pos++;
/*  46 */       if (b == '?' && pos == 0) {
/*  47 */         this.myStartsWithQuestionMark = true; continue;
/*     */       } 
/*  49 */       if (b == '>' && pos == 0) {
/*  50 */         this.myStartsWithMoreMark = true; continue;
/*     */       } 
/*  52 */       if (b == ';') {
/*  53 */         if (digit > 0) {
/*  54 */           this.myArgc++;
/*  55 */           if (this.myArgc == this.myArgv.length) {
/*  56 */             int[] replacement = new int[this.myArgv.length * 2];
/*  57 */             System.arraycopy(this.myArgv, 0, replacement, 0, this.myArgv.length);
/*  58 */             this.myArgv = replacement;
/*     */           } 
/*  60 */           this.myArgv[this.myArgc] = 0;
/*  61 */           digit = 0;
/*     */         }  continue;
/*     */       } 
/*  64 */       if ('0' <= b && b <= '9') {
/*  65 */         this.myArgv[this.myArgc] = this.myArgv[this.myArgc] * 10 + b - 48;
/*  66 */         digit++;
/*  67 */         seenDigit = 1; continue;
/*     */       } 
/*  69 */       if (':' <= b && b <= '?') {
/*  70 */         addUnhandled(b); continue;
/*     */       } 
/*  72 */       if ('@' <= b && b <= '~') {
/*  73 */         this.myFinalChar = b;
/*     */         
/*     */         break;
/*     */       } 
/*  77 */       addUnhandled(b);
/*     */     } 
/*     */     
/*  80 */     this.myArgc += seenDigit;
/*     */   }
/*     */   
/*     */   private void addUnhandled(char b) {
/*  84 */     if (this.myUnhandledChars == null) {
/*  85 */       this.myUnhandledChars = Lists.newArrayList();
/*     */     }
/*  87 */     this.myUnhandledChars.add(Character.valueOf(b));
/*     */   }
/*     */   
/*     */   public boolean pushBackReordered(TerminalDataStream channel) throws IOException {
/*  91 */     if (this.myUnhandledChars == null) return false; 
/*  92 */     char[] bytes = new char[1024];
/*  93 */     int i = 0;
/*  94 */     for (Iterator<Character> iterator = this.myUnhandledChars.iterator(); iterator.hasNext(); ) { char b = ((Character)iterator.next()).charValue();
/*  95 */       bytes[i++] = b; }
/*     */     
/*  97 */     bytes[i++] = '\033';
/*  98 */     bytes[i++] = '[';
/*     */     
/* 100 */     if (this.myStartsWithQuestionMark) {
/* 101 */       bytes[i++] = '?';
/*     */     }
/*     */     
/* 104 */     if (this.myStartsWithMoreMark) {
/* 105 */       bytes[i++] = '>';
/*     */     }
/*     */     
/* 108 */     for (int argi = 0; argi < this.myArgc; argi++) {
/* 109 */       if (argi != 0) bytes[i++] = ';'; 
/* 110 */       String s = Integer.toString(this.myArgv[argi]);
/* 111 */       for (int j = 0; j < s.length(); j++) {
/* 112 */         bytes[i++] = s.charAt(j);
/*     */       }
/*     */     } 
/* 115 */     bytes[i++] = this.myFinalChar;
/* 116 */     channel.pushBackBuffer(bytes, i);
/* 117 */     return true;
/*     */   }
/*     */   
/*     */   int getCount() {
/* 121 */     return this.myArgc;
/*     */   }
/*     */   
/*     */   final int getArg(int index, int defaultValue) {
/* 125 */     if (index >= this.myArgc) {
/* 126 */       return defaultValue;
/*     */     }
/* 128 */     return this.myArgv[index];
/*     */   }
/*     */   
/*     */   public String appendTo(String str) {
/* 132 */     StringBuilder sb = new StringBuilder(str);
/* 133 */     appendToBuffer(sb);
/* 134 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public final void appendToBuffer(StringBuilder sb) {
/* 138 */     sb.append("ESC[");
/*     */     
/* 140 */     if (this.myStartsWithQuestionMark) {
/* 141 */       sb.append("?");
/*     */     }
/*     */     
/* 144 */     if (this.myStartsWithMoreMark) {
/* 145 */       sb.append(">");
/*     */     }
/*     */     
/* 148 */     String sep = "";
/* 149 */     for (int i = 0; i < this.myArgc; i++) {
/* 150 */       sb.append(sep);
/* 151 */       sb.append(this.myArgv[i]);
/* 152 */       sep = ";";
/*     */     } 
/* 154 */     sb.append(this.myFinalChar);
/*     */     
/* 156 */     if (this.myUnhandledChars != null) {
/* 157 */       sb.append(" Unhandled:");
/* 158 */       CharUtils.CharacterType last = CharUtils.CharacterType.NONE;
/* 159 */       for (Iterator<Character> iterator = this.myUnhandledChars.iterator(); iterator.hasNext(); ) { char b = ((Character)iterator.next()).charValue();
/* 160 */         last = CharUtils.appendChar(sb, last, b); }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 167 */     StringBuilder sb = new StringBuilder();
/* 168 */     appendToBuffer(sb);
/* 169 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public char getFinalChar() {
/* 173 */     return this.myFinalChar;
/*     */   }
/*     */   
/*     */   public boolean startsWithQuestionMark() {
/* 177 */     return this.myStartsWithQuestionMark;
/*     */   }
/*     */   
/*     */   public boolean startsWithMoreMark() {
/* 181 */     return this.myStartsWithMoreMark;
/*     */   }
/*     */   
/*     */   public String getSequenceString() {
/* 185 */     return this.mySequenceString.toString();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\emulator\ControlSequence.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */