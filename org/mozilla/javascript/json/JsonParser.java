/*     */ package org.mozilla.javascript.json;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.mozilla.javascript.Context;
/*     */ import org.mozilla.javascript.ScriptRuntime;
/*     */ import org.mozilla.javascript.Scriptable;
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
/*     */ public class JsonParser
/*     */ {
/*     */   private Context cx;
/*     */   private Scriptable scope;
/*     */   private int pos;
/*     */   private int length;
/*     */   private String src;
/*     */   
/*     */   public JsonParser(Context cx, Scriptable scope) {
/*  33 */     this.cx = cx;
/*  34 */     this.scope = scope;
/*     */   }
/*     */   
/*     */   public synchronized Object parseValue(String json) throws ParseException {
/*  38 */     if (json == null) {
/*  39 */       throw new ParseException("Input string may not be null");
/*     */     }
/*  41 */     this.pos = 0;
/*  42 */     this.length = json.length();
/*  43 */     this.src = json;
/*  44 */     Object value = readValue();
/*  45 */     consumeWhitespace();
/*  46 */     if (this.pos < this.length) {
/*  47 */       throw new ParseException("Expected end of stream at char " + this.pos);
/*     */     }
/*  49 */     return value;
/*     */   }
/*     */   
/*     */   private Object readValue() throws ParseException {
/*  53 */     consumeWhitespace();
/*  54 */     if (this.pos < this.length) {
/*  55 */       char c = this.src.charAt(this.pos++);
/*  56 */       switch (c) {
/*     */         case '{':
/*  58 */           return readObject();
/*     */         case '[':
/*  60 */           return readArray();
/*     */         case 't':
/*  62 */           return readTrue();
/*     */         case 'f':
/*  64 */           return readFalse();
/*     */         case '"':
/*  66 */           return readString();
/*     */         case 'n':
/*  68 */           return readNull();
/*     */         case '-':
/*     */         case '0':
/*     */         case '1':
/*     */         case '2':
/*     */         case '3':
/*     */         case '4':
/*     */         case '5':
/*     */         case '6':
/*     */         case '7':
/*     */         case '8':
/*     */         case '9':
/*  80 */           return readNumber(c);
/*     */       } 
/*  82 */       throw new ParseException("Unexpected token: " + c);
/*     */     } 
/*     */     
/*  85 */     throw new ParseException("Empty JSON string");
/*     */   }
/*     */   
/*     */   private Object readObject() throws ParseException {
/*  89 */     consumeWhitespace();
/*  90 */     Scriptable object = this.cx.newObject(this.scope);
/*     */     
/*  92 */     if (this.pos < this.length && this.src.charAt(this.pos) == '}') {
/*  93 */       this.pos++;
/*  94 */       return object;
/*     */     } 
/*     */ 
/*     */     
/*  98 */     boolean needsComma = false;
/*  99 */     while (this.pos < this.length) {
/* 100 */       String id; Object value; long index; char c = this.src.charAt(this.pos++);
/* 101 */       switch (c) {
/*     */         case '}':
/* 103 */           if (!needsComma) {
/* 104 */             throw new ParseException("Unexpected comma in object literal");
/*     */           }
/* 106 */           return object;
/*     */         case ',':
/* 108 */           if (!needsComma) {
/* 109 */             throw new ParseException("Unexpected comma in object literal");
/*     */           }
/* 111 */           needsComma = false;
/*     */           break;
/*     */         case '"':
/* 114 */           if (needsComma) {
/* 115 */             throw new ParseException("Missing comma in object literal");
/*     */           }
/* 117 */           id = readString();
/* 118 */           consume(':');
/* 119 */           value = readValue();
/*     */           
/* 121 */           index = ScriptRuntime.indexFromString(id);
/* 122 */           if (index < 0L) {
/* 123 */             object.put(id, object, value);
/*     */           } else {
/* 125 */             object.put((int)index, object, value);
/*     */           } 
/*     */           
/* 128 */           needsComma = true;
/*     */           break;
/*     */         default:
/* 131 */           throw new ParseException("Unexpected token in object literal");
/*     */       } 
/* 133 */       consumeWhitespace();
/*     */     } 
/* 135 */     throw new ParseException("Unterminated object literal");
/*     */   }
/*     */   
/*     */   private Object readArray() throws ParseException {
/* 139 */     consumeWhitespace();
/*     */     
/* 141 */     if (this.pos < this.length && this.src.charAt(this.pos) == ']') {
/* 142 */       this.pos++;
/* 143 */       return this.cx.newArray(this.scope, 0);
/*     */     } 
/* 145 */     List<Object> list = new ArrayList();
/* 146 */     boolean needsComma = false;
/* 147 */     while (this.pos < this.length) {
/* 148 */       char c = this.src.charAt(this.pos);
/* 149 */       switch (c) {
/*     */         case ']':
/* 151 */           if (!needsComma) {
/* 152 */             throw new ParseException("Unexpected comma in array literal");
/*     */           }
/* 154 */           this.pos++;
/* 155 */           return this.cx.newArray(this.scope, list.toArray());
/*     */         case ',':
/* 157 */           if (!needsComma) {
/* 158 */             throw new ParseException("Unexpected comma in array literal");
/*     */           }
/* 160 */           needsComma = false;
/* 161 */           this.pos++;
/*     */           break;
/*     */         default:
/* 164 */           if (needsComma) {
/* 165 */             throw new ParseException("Missing comma in array literal");
/*     */           }
/* 167 */           list.add(readValue());
/* 168 */           needsComma = true; break;
/*     */       } 
/* 170 */       consumeWhitespace();
/*     */     } 
/* 172 */     throw new ParseException("Unterminated array literal");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String readString() throws ParseException {
/* 180 */     int stringStart = this.pos;
/* 181 */     while (this.pos < this.length) {
/* 182 */       char c = this.src.charAt(this.pos++);
/* 183 */       if (c <= '\037')
/* 184 */         throw new ParseException("String contains control character"); 
/* 185 */       if (c == '\\')
/*     */         break; 
/* 187 */       if (c == '"') {
/* 188 */         return this.src.substring(stringStart, this.pos - 1);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 197 */     StringBuilder b = new StringBuilder();
/* 198 */     while (this.pos < this.length) {
/* 199 */       int code; assert this.src.charAt(this.pos - 1) == '\\';
/* 200 */       b.append(this.src, stringStart, this.pos - 1);
/* 201 */       if (this.pos >= this.length) {
/* 202 */         throw new ParseException("Unterminated string");
/*     */       }
/* 204 */       char c = this.src.charAt(this.pos++);
/* 205 */       switch (c) {
/*     */         case '"':
/* 207 */           b.append('"');
/*     */           break;
/*     */         case '\\':
/* 210 */           b.append('\\');
/*     */           break;
/*     */         case '/':
/* 213 */           b.append('/');
/*     */           break;
/*     */         case 'b':
/* 216 */           b.append('\b');
/*     */           break;
/*     */         case 'f':
/* 219 */           b.append('\f');
/*     */           break;
/*     */         case 'n':
/* 222 */           b.append('\n');
/*     */           break;
/*     */         case 'r':
/* 225 */           b.append('\r');
/*     */           break;
/*     */         case 't':
/* 228 */           b.append('\t');
/*     */           break;
/*     */         case 'u':
/* 231 */           if (this.length - this.pos < 5) {
/* 232 */             throw new ParseException("Invalid character code: \\u" + this.src.substring(this.pos));
/*     */           }
/* 234 */           code = fromHex(this.src.charAt(this.pos + 0)) << 12 | fromHex(this.src.charAt(this.pos + 1)) << 8 | fromHex(this.src.charAt(this.pos + 2)) << 4 | fromHex(this.src.charAt(this.pos + 3));
/*     */ 
/*     */ 
/*     */           
/* 238 */           if (code < 0) {
/* 239 */             throw new ParseException("Invalid character code: " + this.src.substring(this.pos, this.pos + 4));
/*     */           }
/* 241 */           this.pos += 4;
/* 242 */           b.append((char)code);
/*     */           break;
/*     */         default:
/* 245 */           throw new ParseException("Unexpected character in string: '\\" + c + "'");
/*     */       } 
/* 247 */       stringStart = this.pos;
/* 248 */       while (this.pos < this.length) {
/* 249 */         c = this.src.charAt(this.pos++);
/* 250 */         if (c <= '\037')
/* 251 */           throw new ParseException("String contains control character"); 
/* 252 */         if (c == '\\')
/*     */           break; 
/* 254 */         if (c == '"') {
/* 255 */           b.append(this.src, stringStart, this.pos - 1);
/* 256 */           return b.toString();
/*     */         } 
/*     */       } 
/*     */     } 
/* 260 */     throw new ParseException("Unterminated string literal");
/*     */   }
/*     */   
/*     */   private int fromHex(char c) {
/* 264 */     return (c >= '0' && c <= '9') ? (c - 48) : ((c >= 'A' && c <= 'F') ? (c - 65 + 10) : ((c >= 'a' && c <= 'f') ? (c - 97 + 10) : -1));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Number readNumber(char c) throws ParseException {
/* 271 */     assert c == '-' || (c >= '0' && c <= '9');
/* 272 */     int numberStart = this.pos - 1;
/* 273 */     if (c == '-') {
/* 274 */       c = nextOrNumberError(numberStart);
/* 275 */       if (c < '0' || c > '9') {
/* 276 */         throw numberError(numberStart, this.pos);
/*     */       }
/*     */     } 
/* 279 */     if (c != '0') {
/* 280 */       readDigits();
/*     */     }
/*     */     
/* 283 */     if (this.pos < this.length) {
/* 284 */       c = this.src.charAt(this.pos);
/* 285 */       if (c == '.') {
/* 286 */         this.pos++;
/* 287 */         c = nextOrNumberError(numberStart);
/* 288 */         if (c < '0' || c > '9') {
/* 289 */           throw numberError(numberStart, this.pos);
/*     */         }
/* 291 */         readDigits();
/*     */       } 
/*     */     } 
/*     */     
/* 295 */     if (this.pos < this.length) {
/* 296 */       c = this.src.charAt(this.pos);
/* 297 */       if (c == 'e' || c == 'E') {
/* 298 */         this.pos++;
/* 299 */         c = nextOrNumberError(numberStart);
/* 300 */         if (c == '-' || c == '+') {
/* 301 */           c = nextOrNumberError(numberStart);
/*     */         }
/* 303 */         if (c < '0' || c > '9') {
/* 304 */           throw numberError(numberStart, this.pos);
/*     */         }
/* 306 */         readDigits();
/*     */       } 
/*     */     } 
/* 309 */     String num = this.src.substring(numberStart, this.pos);
/* 310 */     double dval = Double.parseDouble(num);
/* 311 */     int ival = (int)dval;
/* 312 */     if (ival == dval) {
/* 313 */       return Integer.valueOf(ival);
/*     */     }
/* 315 */     return Double.valueOf(dval);
/*     */   }
/*     */ 
/*     */   
/*     */   private ParseException numberError(int start, int end) {
/* 320 */     return new ParseException("Unsupported number format: " + this.src.substring(start, end));
/*     */   }
/*     */   
/*     */   private char nextOrNumberError(int numberStart) throws ParseException {
/* 324 */     if (this.pos >= this.length) {
/* 325 */       throw numberError(numberStart, this.length);
/*     */     }
/* 327 */     return this.src.charAt(this.pos++);
/*     */   }
/*     */   
/*     */   private void readDigits() {
/* 331 */     for (; this.pos < this.length; this.pos++) {
/* 332 */       char c = this.src.charAt(this.pos);
/* 333 */       if (c < '0' || c > '9') {
/*     */         break;
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private Boolean readTrue() throws ParseException {
/* 340 */     if (this.length - this.pos < 3 || this.src.charAt(this.pos) != 'r' || this.src.charAt(this.pos + 1) != 'u' || this.src.charAt(this.pos + 2) != 'e')
/*     */     {
/*     */ 
/*     */       
/* 344 */       throw new ParseException("Unexpected token: t");
/*     */     }
/* 346 */     this.pos += 3;
/* 347 */     return Boolean.TRUE;
/*     */   }
/*     */   
/*     */   private Boolean readFalse() throws ParseException {
/* 351 */     if (this.length - this.pos < 4 || this.src.charAt(this.pos) != 'a' || this.src.charAt(this.pos + 1) != 'l' || this.src.charAt(this.pos + 2) != 's' || this.src.charAt(this.pos + 3) != 'e')
/*     */     {
/*     */ 
/*     */ 
/*     */       
/* 356 */       throw new ParseException("Unexpected token: f");
/*     */     }
/* 358 */     this.pos += 4;
/* 359 */     return Boolean.FALSE;
/*     */   }
/*     */   
/*     */   private Object readNull() throws ParseException {
/* 363 */     if (this.length - this.pos < 3 || this.src.charAt(this.pos) != 'u' || this.src.charAt(this.pos + 1) != 'l' || this.src.charAt(this.pos + 2) != 'l')
/*     */     {
/*     */ 
/*     */       
/* 367 */       throw new ParseException("Unexpected token: n");
/*     */     }
/* 369 */     this.pos += 3;
/* 370 */     return null;
/*     */   }
/*     */   
/*     */   private void consumeWhitespace() {
/* 374 */     while (this.pos < this.length) {
/* 375 */       char c = this.src.charAt(this.pos);
/* 376 */       switch (c) {
/*     */         case '\t':
/*     */         case '\n':
/*     */         case '\r':
/*     */         case ' ':
/* 381 */           this.pos++;
/*     */           continue;
/*     */       } 
/*     */       return;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void consume(char token) throws ParseException {
/* 390 */     consumeWhitespace();
/* 391 */     if (this.pos >= this.length) {
/* 392 */       throw new ParseException("Expected " + token + " but reached end of stream");
/*     */     }
/* 394 */     char c = this.src.charAt(this.pos++);
/* 395 */     if (c == token) {
/*     */       return;
/*     */     }
/* 398 */     throw new ParseException("Expected " + token + " found " + c);
/*     */   }
/*     */   
/*     */   public static class ParseException
/*     */     extends Exception
/*     */   {
/*     */     static final long serialVersionUID = 4804542791749920772L;
/*     */     
/*     */     ParseException(String message) {
/* 407 */       super(message);
/*     */     }
/*     */     
/*     */     ParseException(Exception cause) {
/* 411 */       super(cause);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\json\JsonParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */