/*     */ package org.mozilla.javascript.tools.idswitch;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class CodePrinter
/*     */ {
/*     */   private static final int LITERAL_CHAR_MAX_SIZE = 6;
/*  13 */   private String lineTerminator = "\n";
/*     */   
/*  15 */   private int indentStep = 4;
/*  16 */   private int indentTabSize = 8;
/*     */   
/*  18 */   private char[] buffer = new char[4096];
/*     */   private int offset;
/*     */   
/*  21 */   public String getLineTerminator() { return this.lineTerminator; } public void setLineTerminator(String value) {
/*  22 */     this.lineTerminator = value;
/*     */   }
/*  24 */   public int getIndentStep() { return this.indentStep; } public void setIndentStep(int char_count) {
/*  25 */     this.indentStep = char_count;
/*     */   }
/*  27 */   public int getIndentTabSize() { return this.indentTabSize; } public void setIndentTabSize(int tab_size) {
/*  28 */     this.indentTabSize = tab_size;
/*     */   }
/*     */   public void clear() {
/*  31 */     this.offset = 0;
/*     */   }
/*     */   
/*     */   private int ensure_area(int area_size) {
/*  35 */     int begin = this.offset;
/*  36 */     int end = begin + area_size;
/*  37 */     if (end > this.buffer.length) {
/*  38 */       int new_capacity = this.buffer.length * 2;
/*  39 */       if (end > new_capacity) new_capacity = end; 
/*  40 */       char[] tmp = new char[new_capacity];
/*  41 */       System.arraycopy(this.buffer, 0, tmp, 0, begin);
/*  42 */       this.buffer = tmp;
/*     */     } 
/*  44 */     return begin;
/*     */   }
/*     */   
/*     */   private int add_area(int area_size) {
/*  48 */     int pos = ensure_area(area_size);
/*  49 */     this.offset = pos + area_size;
/*  50 */     return pos;
/*     */   }
/*     */   
/*     */   public int getOffset() {
/*  54 */     return this.offset;
/*     */   }
/*     */   
/*     */   public int getLastChar() {
/*  58 */     return (this.offset == 0) ? -1 : this.buffer[this.offset - 1];
/*     */   }
/*     */   
/*     */   public void p(char c) {
/*  62 */     int pos = add_area(1);
/*  63 */     this.buffer[pos] = c;
/*     */   }
/*     */   
/*     */   public void p(String s) {
/*  67 */     int l = s.length();
/*  68 */     int pos = add_area(l);
/*  69 */     s.getChars(0, l, this.buffer, pos);
/*     */   }
/*     */   
/*     */   public final void p(char[] array) {
/*  73 */     p(array, 0, array.length);
/*     */   }
/*     */   
/*     */   public void p(char[] array, int begin, int end) {
/*  77 */     int l = end - begin;
/*  78 */     int pos = add_area(l);
/*  79 */     System.arraycopy(array, begin, this.buffer, pos, l);
/*     */   }
/*     */   
/*     */   public void p(int i) {
/*  83 */     p(Integer.toString(i));
/*     */   }
/*     */   
/*     */   public void qchar(int c) {
/*  87 */     int pos = ensure_area(8);
/*  88 */     this.buffer[pos] = '\'';
/*  89 */     pos = put_string_literal_char(pos + 1, c, false);
/*  90 */     this.buffer[pos] = '\'';
/*  91 */     this.offset = pos + 1;
/*     */   }
/*     */   
/*     */   public void qstring(String s) {
/*  95 */     int l = s.length();
/*  96 */     int pos = ensure_area(2 + 6 * l);
/*  97 */     this.buffer[pos] = '"';
/*  98 */     pos++;
/*  99 */     for (int i = 0; i != l; i++) {
/* 100 */       pos = put_string_literal_char(pos, s.charAt(i), true);
/*     */     }
/* 102 */     this.buffer[pos] = '"';
/* 103 */     this.offset = pos + 1;
/*     */   }
/*     */   
/*     */   private int put_string_literal_char(int pos, int c, boolean in_string) {
/* 107 */     boolean backslash_symbol = true;
/* 108 */     switch (c) { case 8:
/* 109 */         c = 98; break;
/* 110 */       case 9: c = 116; break;
/* 111 */       case 10: c = 110; break;
/* 112 */       case 12: c = 102; break;
/* 113 */       case 13: c = 114; break;
/* 114 */       case 39: backslash_symbol = !in_string; break;
/* 115 */       case 34: backslash_symbol = in_string; break;
/* 116 */       default: backslash_symbol = false;
/*     */         break; }
/*     */     
/* 119 */     if (backslash_symbol) {
/* 120 */       this.buffer[pos] = '\\';
/* 121 */       this.buffer[pos + 1] = (char)c;
/* 122 */       pos += 2;
/*     */     }
/* 124 */     else if (32 <= c && c <= 126) {
/* 125 */       this.buffer[pos] = (char)c;
/* 126 */       pos++;
/*     */     } else {
/*     */       
/* 129 */       this.buffer[pos] = '\\';
/* 130 */       this.buffer[pos + 1] = 'u';
/* 131 */       this.buffer[pos + 2] = digit_to_hex_letter(0xF & c >> 12);
/* 132 */       this.buffer[pos + 3] = digit_to_hex_letter(0xF & c >> 8);
/* 133 */       this.buffer[pos + 4] = digit_to_hex_letter(0xF & c >> 4);
/* 134 */       this.buffer[pos + 5] = digit_to_hex_letter(0xF & c);
/* 135 */       pos += 6;
/*     */     } 
/* 137 */     return pos;
/*     */   }
/*     */   
/*     */   private static char digit_to_hex_letter(int d) {
/* 141 */     return (char)((d < 10) ? (48 + d) : (55 + d));
/*     */   }
/*     */   
/*     */   public void indent(int level) {
/* 145 */     int indent_size, tab_count, visible_size = this.indentStep * level;
/*     */     
/* 147 */     if (this.indentTabSize <= 0) {
/* 148 */       tab_count = 0; indent_size = visible_size;
/*     */     } else {
/*     */       
/* 151 */       tab_count = visible_size / this.indentTabSize;
/* 152 */       indent_size = tab_count + visible_size % this.indentTabSize;
/*     */     } 
/* 154 */     int pos = add_area(indent_size);
/* 155 */     int tab_end = pos + tab_count;
/* 156 */     int indent_end = pos + indent_size;
/* 157 */     while (pos != tab_end) { this.buffer[pos] = '\t'; pos++; }
/* 158 */      while (pos != indent_end) { this.buffer[pos] = ' '; pos++; }
/*     */   
/*     */   }
/*     */   public void nl() {
/* 162 */     p('\n');
/*     */   }
/*     */   
/*     */   public void line(int indent_level, String s) {
/* 166 */     indent(indent_level); p(s); nl();
/*     */   }
/*     */   
/*     */   public void erase(int begin, int end) {
/* 170 */     System.arraycopy(this.buffer, end, this.buffer, begin, this.offset - end);
/* 171 */     this.offset -= end - begin;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 176 */     return new String(this.buffer, 0, this.offset);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\tools\idswitch\CodePrinter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */