/*     */ package org.yaml.snakeyaml.external.biz.binaryEscape;
/*     */ 
/*     */ import java.util.LinkedList;
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
/*     */ class StringIterator
/*     */ {
/* 112 */   protected int position = 0;
/*     */   
/*     */   protected int lineNo;
/*     */   
/*     */   protected char[] text;
/*     */   
/*     */   protected String texts;
/*     */   
/* 120 */   protected int begin = 0;
/*     */   
/* 122 */   protected LinkedList mark1 = new LinkedList();
/*     */   
/* 124 */   protected LinkedList mark2 = new LinkedList();
/*     */   public StringIterator(String paramString) {
/* 126 */     this(paramString, 0);
/*     */   } public String toString() {
/* 128 */     return this.texts;
/*     */   }
/*     */   public StringIterator(String paramString, int paramInt) {
/* 131 */     this.texts = paramString;
/* 132 */     this.text = paramString.toCharArray();
/* 133 */     this.lineNo = paramInt;
/*     */   }
/*     */   public boolean hasNext() {
/* 136 */     return (this.position < this.text.length);
/*     */   } public boolean hasNext(int paramInt) {
/* 138 */     return (this.position + paramInt - 1 < this.text.length);
/*     */   } public int getLineNumber() {
/* 140 */     return this.lineNo;
/*     */   } public String getErrorToken() {
/* 142 */     return String.format(" EntireLine:%s LineNumber:%d LineMarker:%d", new Object[] { getEntireLine(), Integer.valueOf(getLineNumber()), Integer.valueOf(getLineMarker()) });
/*     */   }
/*     */   public String getEntireLine() {
/*     */     int i;
/* 146 */     for (i = this.position; i < this.text.length && this.text[i] != '\n'; i++);
/* 147 */     return this.texts.substring(this.begin, i);
/*     */   }
/*     */   public int getLineMarker() {
/* 150 */     return this.position - this.begin;
/*     */   } public boolean isNextString(String paramString) {
/* 152 */     return (this.position + paramString.length() <= this.text.length && this.texts.substring(this.position, this.position + paramString.length()).equals(paramString));
/*     */   } public boolean isNextChar(char paramChar) {
/* 154 */     return (hasNext() && this.text[this.position] == paramChar);
/*     */   } public char peek() {
/* 156 */     return hasNext() ? this.text[this.position] : Character.MIN_VALUE;
/*     */   } public void skip(int paramInt) {
/* 158 */     this.position += paramInt;
/*     */   }
/*     */   public String next(int paramInt) {
/* 161 */     StringBuffer stringBuffer = new StringBuffer();
/* 162 */     for (byte b = 0; b < paramInt; b = (byte)(b + 1))
/* 163 */       stringBuffer.append(next()); 
/* 164 */     return stringBuffer.toString();
/*     */   }
/*     */   
/*     */   public char next() {
/* 168 */     char c = this.text[this.position];
/* 169 */     if (this.position > 0 && this.text[this.position - 1] == '\n') {
/* 170 */       this.lineNo++;
/* 171 */       this.begin = this.position;
/*     */     } 
/* 173 */     this.position++;
/* 174 */     return c;
/*     */   }
/*     */   
/*     */   public void mark() {
/* 178 */     this.mark1.add(0, new Integer(this.position));
/* 179 */     this.mark2.add(0, new Integer(this.lineNo));
/*     */   }
/*     */   
/*     */   public String reset() {
/* 183 */     Integer integer1 = this.mark1.removeFirst();
/* 184 */     Integer integer2 = this.mark2.removeFirst();
/* 185 */     return this.texts.substring(integer1.intValue(), this.position);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\yaml\snakeyaml\external\biz\binaryEscape\StringIterator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */