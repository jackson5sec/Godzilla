/*     */ package com.jediterm.terminal;
/*     */ 
/*     */ import com.jediterm.terminal.ui.UIUtil;
/*     */ import com.jediterm.terminal.util.CharUtils;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TerminalKeyEncoder
/*     */ {
/*     */   private static final int ESC = 27;
/*  21 */   private final Map<KeyCodeAndModifier, byte[]> myKeyCodes = (Map)new HashMap<>();
/*     */   
/*     */   private boolean myAltSendsEscape = true;
/*     */   private boolean myMetaSendsEscape = false;
/*     */   
/*     */   public TerminalKeyEncoder() {
/*  27 */     setAutoNewLine(false);
/*  28 */     arrowKeysApplicationSequences();
/*  29 */     keypadAnsiSequences();
/*  30 */     putCode(8, new int[] { 127 });
/*  31 */     putCode(112, new int[] { 27, 79, 80 });
/*  32 */     putCode(113, new int[] { 27, 79, 81 });
/*  33 */     putCode(114, new int[] { 27, 79, 82 });
/*  34 */     putCode(115, new int[] { 27, 79, 83 });
/*  35 */     putCode(116, new int[] { 27, 91, 49, 53, 126 });
/*  36 */     putCode(117, new int[] { 27, 91, 49, 55, 126 });
/*  37 */     putCode(118, new int[] { 27, 91, 49, 56, 126 });
/*  38 */     putCode(119, new int[] { 27, 91, 49, 57, 126 });
/*  39 */     putCode(120, new int[] { 27, 91, 50, 48, 126 });
/*  40 */     putCode(121, new int[] { 27, 91, 50, 49, 126 });
/*  41 */     putCode(122, new int[] { 27, 91, 50, 51, 126, 27 });
/*  42 */     putCode(123, new int[] { 27, 91, 50, 52, 126, 8 });
/*     */     
/*  44 */     putCode(155, new int[] { 27, 91, 50, 126 });
/*  45 */     putCode(127, new int[] { 27, 91, 51, 126 });
/*     */     
/*  47 */     putCode(33, new int[] { 27, 91, 53, 126 });
/*  48 */     putCode(34, new int[] { 27, 91, 54, 126 });
/*     */     
/*  50 */     putCode(36, new int[] { 27, 91, 72 });
/*  51 */     putCode(35, new int[] { 27, 91, 70 });
/*     */   }
/*     */   
/*     */   public void arrowKeysApplicationSequences() {
/*  55 */     putCode(38, new int[] { 27, 79, 65 });
/*  56 */     putCode(40, new int[] { 27, 79, 66 });
/*  57 */     putCode(39, new int[] { 27, 79, 67 });
/*  58 */     putCode(37, new int[] { 27, 79, 68 });
/*     */     
/*  60 */     if (UIUtil.isLinux) {
/*  61 */       putCode(new KeyCodeAndModifier(39, 2), new int[] { 27, 91, 49, 59, 53, 67 });
/*  62 */       putCode(new KeyCodeAndModifier(37, 2), new int[] { 27, 91, 49, 59, 53, 68 });
/*  63 */       putCode(new KeyCodeAndModifier(39, 8), new int[] { 27, 91, 49, 59, 51, 67 });
/*  64 */       putCode(new KeyCodeAndModifier(37, 8), new int[] { 27, 91, 49, 59, 51, 68 });
/*     */     } else {
/*     */       
/*  67 */       putCode(new KeyCodeAndModifier(39, 8), new int[] { 27, 102 });
/*  68 */       putCode(new KeyCodeAndModifier(37, 8), new int[] { 27, 98 });
/*     */     } 
/*     */   }
/*     */   
/*     */   public void arrowKeysAnsiCursorSequences() {
/*  73 */     putCode(38, new int[] { 27, 91, 65 });
/*  74 */     putCode(40, new int[] { 27, 91, 66 });
/*  75 */     putCode(39, new int[] { 27, 91, 67 });
/*  76 */     putCode(37, new int[] { 27, 91, 68 });
/*  77 */     if (UIUtil.isMac) {
/*  78 */       putCode(new KeyCodeAndModifier(39, 8), new int[] { 27, 102 });
/*  79 */       putCode(new KeyCodeAndModifier(37, 8), new int[] { 27, 98 });
/*     */     } 
/*     */   }
/*     */   
/*     */   public void keypadApplicationSequences() {
/*  84 */     putCode(225, new int[] { 27, 79, 66 });
/*  85 */     putCode(226, new int[] { 27, 79, 68 });
/*  86 */     putCode(227, new int[] { 27, 79, 67 });
/*  87 */     putCode(224, new int[] { 27, 79, 65 });
/*     */     
/*  89 */     putCode(36, new int[] { 27, 79, 72 });
/*  90 */     putCode(35, new int[] { 27, 79, 70 });
/*     */   }
/*     */   
/*     */   public void keypadAnsiSequences() {
/*  94 */     putCode(225, new int[] { 27, 91, 66 });
/*  95 */     putCode(226, new int[] { 27, 91, 68 });
/*  96 */     putCode(227, new int[] { 27, 91, 67 });
/*  97 */     putCode(224, new int[] { 27, 91, 65 });
/*     */     
/*  99 */     putCode(36, new int[] { 27, 91, 72 });
/* 100 */     putCode(35, new int[] { 27, 91, 70 });
/*     */   }
/*     */   
/*     */   void putCode(int code, int... bytesAsInt) {
/* 104 */     this.myKeyCodes.put(new KeyCodeAndModifier(code, 0), CharUtils.makeCode(bytesAsInt));
/*     */   }
/*     */   
/*     */   private void putCode(@NotNull KeyCodeAndModifier key, int... bytesAsInt) {
/* 108 */     if (key == null) $$$reportNull$$$0(0);  this.myKeyCodes.put(key, CharUtils.makeCode(bytesAsInt));
/*     */   }
/*     */   
/*     */   public byte[] getCode(int key, int modifiers) {
/* 112 */     byte[] bytes = this.myKeyCodes.get(new KeyCodeAndModifier(key, modifiers));
/* 113 */     if (bytes != null) {
/* 114 */       return bytes;
/*     */     }
/* 116 */     bytes = this.myKeyCodes.get(new KeyCodeAndModifier(key, 0));
/* 117 */     if (bytes == null) {
/* 118 */       return null;
/*     */     }
/*     */     
/* 121 */     if ((this.myAltSendsEscape || alwaysSendEsc(key)) && (modifiers & 0x8) != 0) {
/* 122 */       return insertCodeAt(bytes, CharUtils.makeCode(new int[] { 27 }, ), 0);
/*     */     }
/*     */     
/* 125 */     if ((this.myMetaSendsEscape || alwaysSendEsc(key)) && (modifiers & 0x4) != 0) {
/* 126 */       return insertCodeAt(bytes, CharUtils.makeCode(new int[] { 27 }, ), 0);
/*     */     }
/*     */     
/* 129 */     if (isCursorKey(key)) {
/* 130 */       return getCodeWithModifiers(bytes, modifiers);
/*     */     }
/*     */     
/* 133 */     return bytes;
/*     */   }
/*     */   
/*     */   private boolean alwaysSendEsc(int key) {
/* 137 */     return (isCursorKey(key) || key == 8);
/*     */   }
/*     */   
/*     */   private boolean isCursorKey(int key) {
/* 141 */     return (key == 40 || key == 38 || key == 37 || key == 39 || key == 36 || key == 35);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private byte[] getCodeWithModifiers(byte[] bytes, int modifiers) {
/* 148 */     int code = modifiersToCode(modifiers);
/*     */     
/* 150 */     if (code > 0) {
/* 151 */       return insertCodeAt(bytes, Integer.toString(code).getBytes(), bytes.length - 1);
/*     */     }
/* 153 */     return bytes;
/*     */   }
/*     */   
/*     */   private static byte[] insertCodeAt(byte[] bytes, byte[] code, int at) {
/* 157 */     byte[] res = new byte[bytes.length + code.length];
/* 158 */     System.arraycopy(bytes, 0, res, 0, bytes.length);
/* 159 */     System.arraycopy(bytes, at, res, at + code.length, bytes.length - at);
/* 160 */     System.arraycopy(code, 0, res, at, code.length);
/* 161 */     return res;
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
/*     */   private static int modifiersToCode(int modifiers) {
/* 188 */     int code = 0;
/* 189 */     if ((modifiers & 0x1) != 0) {
/* 190 */       code |= 0x1;
/*     */     }
/* 192 */     if ((modifiers & 0x8) != 0) {
/* 193 */       code |= 0x2;
/*     */     }
/* 195 */     if ((modifiers & 0x2) != 0) {
/* 196 */       code |= 0x4;
/*     */     }
/* 198 */     if ((modifiers & 0x4) != 0) {
/* 199 */       code |= 0x8;
/*     */     }
/* 201 */     return (code != 0) ? (code + 1) : code;
/*     */   }
/*     */   
/*     */   public void setAutoNewLine(boolean enabled) {
/* 205 */     if (enabled) {
/* 206 */       putCode(10, new int[] { 13, 10 });
/*     */     } else {
/*     */       
/* 209 */       putCode(10, new int[] { 13 });
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setAltSendsEscape(boolean altSendsEscape) {
/* 214 */     this.myAltSendsEscape = altSendsEscape;
/*     */   }
/*     */   
/*     */   public void setMetaSendsEscape(boolean metaSendsEscape) {
/* 218 */     this.myMetaSendsEscape = metaSendsEscape;
/*     */   }
/*     */   
/*     */   private static class KeyCodeAndModifier {
/*     */     private final int myCode;
/*     */     private final int myModifier;
/*     */     
/*     */     public KeyCodeAndModifier(int code, int modifier) {
/* 226 */       this.myCode = code;
/* 227 */       this.myModifier = modifier;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 232 */       if (this == o) return true; 
/* 233 */       if (o == null || getClass() != o.getClass()) return false; 
/* 234 */       KeyCodeAndModifier that = (KeyCodeAndModifier)o;
/* 235 */       return (this.myCode == that.myCode && this.myModifier == that.myModifier);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 240 */       return Objects.hash(new Object[] { Integer.valueOf(this.myCode), Integer.valueOf(this.myModifier) });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\TerminalKeyEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */