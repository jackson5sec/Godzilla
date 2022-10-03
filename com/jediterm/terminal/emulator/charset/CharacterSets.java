/*     */ package com.jediterm.terminal.emulator.charset;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CharacterSets
/*     */ {
/*     */   private static final int C0_START = 0;
/*     */   private static final int C0_END = 31;
/*     */   private static final int C1_START = 128;
/*     */   private static final int C1_END = 159;
/*     */   private static final int GL_START = 32;
/*     */   private static final int GL_END = 127;
/*     */   private static final int GR_START = 160;
/*     */   private static final int GR_END = 255;
/*  18 */   public static final String[] ASCII_NAMES = new String[] { "<nul>", "<soh>", "<stx>", "<etx>", "<eot>", "<enq>", "<ack>", "<bell>", "\b", "\t", "\n", "<vt>", "<ff>", "\r", "<so>", "<si>", "<dle>", "<dc1>", "<dc2>", "<dc3>", "<dc4>", "<nak>", "<syn>", "<etb>", "<can>", "<em>", "<sub>", "<esc>", "<fs>", "<gs>", "<rs>", "<us>", " ", "!", "\"", "#", "$", "%", "&", "'", "(", ")", "*", "+", ",", "-", ".", "/", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ":", ";", "<", "=", ">", "?", "@", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "[", "\\", "]", "^", "_", "`", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "{", "|", "}", "~", "<del>" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  30 */   public static final Object[][] C0_CHARS = new Object[][] { { Integer.valueOf(0), "nul"
/*  31 */       }, { Integer.valueOf(0), "soh"
/*  32 */       }, { Integer.valueOf(0), "stx"
/*  33 */       }, { Integer.valueOf(0), "etx"
/*  34 */       }, { Integer.valueOf(0), "eot"
/*  35 */       }, { Integer.valueOf(0), "enq"
/*  36 */       }, { Integer.valueOf(0), "ack"
/*  37 */       }, { Integer.valueOf(0), "bel"
/*  38 */       }, { Integer.valueOf(8), "bs"
/*  39 */       }, { Integer.valueOf(9), "ht" }, 
/*  40 */       { Integer.valueOf(10), "lf"
/*  41 */       }, { Integer.valueOf(0), "vt"
/*  42 */       }, { Integer.valueOf(0), "ff"
/*  43 */       }, { Integer.valueOf(13), "cr"
/*  44 */       }, { Integer.valueOf(0), "so"
/*  45 */       }, { Integer.valueOf(0), "si"
/*  46 */       }, { Integer.valueOf(0), "dle"
/*  47 */       }, { Integer.valueOf(0), "dc1"
/*  48 */       }, { Integer.valueOf(0), "dc2"
/*  49 */       }, { Integer.valueOf(0), "dc3" }, 
/*  50 */       { Integer.valueOf(0), "dc4"
/*  51 */       }, { Integer.valueOf(0), "nak"
/*  52 */       }, { Integer.valueOf(0), "syn"
/*  53 */       }, { Integer.valueOf(0), "etb"
/*  54 */       }, { Integer.valueOf(0), "can"
/*  55 */       }, { Integer.valueOf(0), "em"
/*  56 */       }, { Integer.valueOf(0), "sub"
/*  57 */       }, { Integer.valueOf(0), "esq"
/*  58 */       }, { Integer.valueOf(0), "fs"
/*  59 */       }, { Integer.valueOf(0), "gs" }, 
/*  60 */       { Integer.valueOf(0), "rs"
/*  61 */       }, { Integer.valueOf(0), "us" } };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   public static final Object[][] C1_CHARS = new Object[][] { { Integer.valueOf(0), null
/*  67 */       }, { Integer.valueOf(0), null
/*  68 */       }, { Integer.valueOf(0), null
/*  69 */       }, { Integer.valueOf(0), null
/*  70 */       }, { Integer.valueOf(0), "ind"
/*  71 */       }, { Integer.valueOf(0), "nel"
/*  72 */       }, { Integer.valueOf(0), "ssa"
/*  73 */       }, { Integer.valueOf(0), "esa"
/*  74 */       }, { Integer.valueOf(0), "hts"
/*  75 */       }, { Integer.valueOf(0), "htj" }, 
/*  76 */       { Integer.valueOf(0), "vts"
/*  77 */       }, { Integer.valueOf(0), "pld"
/*  78 */       }, { Integer.valueOf(0), "plu"
/*  79 */       }, { Integer.valueOf(0), "ri"
/*  80 */       }, { Integer.valueOf(0), "ss2"
/*  81 */       }, { Integer.valueOf(0), "ss3"
/*  82 */       }, { Integer.valueOf(0), "dcs"
/*  83 */       }, { Integer.valueOf(0), "pu1"
/*  84 */       }, { Integer.valueOf(0), "pu2"
/*  85 */       }, { Integer.valueOf(0), "sts" }, 
/*  86 */       { Integer.valueOf(0), "cch"
/*  87 */       }, { Integer.valueOf(0), "mw"
/*  88 */       }, { Integer.valueOf(0), "spa"
/*  89 */       }, { Integer.valueOf(0), "epa"
/*  90 */       }, { Integer.valueOf(0), null
/*  91 */       }, { Integer.valueOf(0), null
/*  92 */       }, { Integer.valueOf(0), null
/*  93 */       }, { Integer.valueOf(0), "csi"
/*  94 */       }, { Integer.valueOf(0), "st"
/*  95 */       }, { Integer.valueOf(0), "osc" }, 
/*  96 */       { Integer.valueOf(0), "pm"
/*  97 */       }, { Integer.valueOf(0), "apc" } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 103 */   public static final Object[][] DEC_SPECIAL_CHARS = new Object[][] { { Character.valueOf('◆'), null
/* 104 */       }, { Character.valueOf('▒'), null
/* 105 */       }, { Character.valueOf('␉'), null
/* 106 */       }, { Character.valueOf('␌'), null
/* 107 */       }, { Character.valueOf('␍'), null
/* 108 */       }, { Character.valueOf('␊'), null
/* 109 */       }, { Character.valueOf('°'), null
/* 110 */       }, { Character.valueOf('±'), null
/* 111 */       }, { Character.valueOf('␤'), null
/* 112 */       }, { Character.valueOf('␋'), null }, 
/* 113 */       { Character.valueOf('┘'), Character.valueOf('┛')
/* 114 */       }, { Character.valueOf('┐'), Character.valueOf('┓')
/* 115 */       }, { Character.valueOf('┌'), Character.valueOf('┏')
/* 116 */       }, { Character.valueOf('└'), Character.valueOf('┗')
/* 117 */       }, { Character.valueOf('┼'), Character.valueOf('╋')
/* 118 */       }, { Character.valueOf('⎺'), null
/* 119 */       }, { Character.valueOf('⎻'), null
/* 120 */       }, { Character.valueOf('─'), Character.valueOf('━')
/* 121 */       }, { Character.valueOf('⎼'), null
/* 122 */       }, { Character.valueOf('⎽'), null }, 
/* 123 */       { Character.valueOf('├'), Character.valueOf('┣')
/* 124 */       }, { Character.valueOf('┤'), Character.valueOf('┫')
/* 125 */       }, { Character.valueOf('┴'), Character.valueOf('┻')
/* 126 */       }, { Character.valueOf('┬'), Character.valueOf('┳')
/* 127 */       }, { Character.valueOf('│'), Character.valueOf('┃')
/* 128 */       }, { Character.valueOf('≤'), null
/* 129 */       }, { Character.valueOf('≥'), null
/* 130 */       }, { Character.valueOf('π'), null
/* 131 */       }, { Character.valueOf('≠'), null
/* 132 */       }, { Character.valueOf('£'), null }, 
/* 133 */       { Character.valueOf('·'), null
/* 134 */       }, { Character.valueOf(' '), null } };
/*     */ 
/*     */   
/*     */   public static boolean isDecBoxChar(char c) {
/* 138 */     if (c < '─' || c >= '▀') {
/* 139 */       return false;
/*     */     }
/* 141 */     for (Object[] o : DEC_SPECIAL_CHARS) {
/* 142 */       if (c == ((Character)o[0]).charValue()) {
/* 143 */         return true;
/*     */       }
/*     */     } 
/* 146 */     return false;
/*     */   }
/*     */   
/*     */   public static char getHeavyDecBoxChar(char c) {
/* 150 */     if (c < '─' || c >= '▀') {
/* 151 */       return c;
/*     */     }
/* 153 */     for (Object[] o : DEC_SPECIAL_CHARS) {
/* 154 */       if (c == ((Character)o[0]).charValue()) {
/* 155 */         return (o[1] != null) ? ((Character)o[1]).charValue() : c;
/*     */       }
/*     */     } 
/* 158 */     return c;
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
/*     */   public static char getChar(char original, GraphicSet gl, GraphicSet gr) {
/* 180 */     Object[] mapping = getMapping(original, gl, gr);
/*     */     
/* 182 */     int ch = ((Integer)mapping[0]).intValue();
/* 183 */     if (ch > 0) {
/* 184 */       return (char)ch;
/*     */     }
/*     */     
/* 187 */     return Character.MIN_VALUE;
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
/*     */   public static String getCharName(char original, GraphicSet gl, GraphicSet gr) {
/* 200 */     Object[] mapping = getMapping(original, gl, gr);
/*     */     
/* 202 */     String name = (String)mapping[1];
/* 203 */     if (name == null) {
/* 204 */       name = String.format("<%d>", new Object[] { Integer.valueOf(original) });
/*     */     }
/*     */     
/* 207 */     return name;
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
/*     */   private static Object[] getMapping(char original, GraphicSet gl, GraphicSet gr) {
/* 220 */     int mappedChar = original;
/* 221 */     if (original >= '\000' && original <= '\037') {
/* 222 */       int idx = original - 0;
/* 223 */       return C0_CHARS[idx];
/*     */     } 
/* 225 */     if (original >= '' && original <= '') {
/* 226 */       int idx = original - 128;
/* 227 */       return C1_CHARS[idx];
/*     */     } 
/* 229 */     if (original >= ' ' && original <= '') {
/* 230 */       int idx = original - 32;
/* 231 */       mappedChar = gl.map(original, idx);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 241 */     return new Object[] { Integer.valueOf(mappedChar), null };
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\emulator\charset\CharacterSets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */