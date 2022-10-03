/*     */ package org.mozilla.javascript;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Decompiler
/*     */ {
/*     */   public static final int ONLY_BODY_FLAG = 1;
/*     */   public static final int TO_SOURCE_FLAG = 2;
/*     */   public static final int INITIAL_INDENT_PROP = 1;
/*     */   public static final int INDENT_GAP_PROP = 2;
/*     */   public static final int CASE_GAP_PROP = 3;
/*     */   private static final int FUNCTION_END = 164;
/*     */   
/*     */   String getEncodedSource() {
/*  78 */     return sourceToString(0);
/*     */   }
/*     */ 
/*     */   
/*     */   int getCurrentOffset() {
/*  83 */     return this.sourceTop;
/*     */   }
/*     */ 
/*     */   
/*     */   int markFunctionStart(int functionType) {
/*  88 */     int savedOffset = getCurrentOffset();
/*  89 */     addToken(109);
/*  90 */     append((char)functionType);
/*  91 */     return savedOffset;
/*     */   }
/*     */ 
/*     */   
/*     */   int markFunctionEnd(int functionStart) {
/*  96 */     int offset = getCurrentOffset();
/*  97 */     append('¤');
/*  98 */     return offset;
/*     */   }
/*     */ 
/*     */   
/*     */   void addToken(int token) {
/* 103 */     if (0 > token || token > 163) {
/* 104 */       throw new IllegalArgumentException();
/*     */     }
/* 106 */     append((char)token);
/*     */   }
/*     */ 
/*     */   
/*     */   void addEOL(int token) {
/* 111 */     if (0 > token || token > 163) {
/* 112 */       throw new IllegalArgumentException();
/*     */     }
/* 114 */     append((char)token);
/* 115 */     append('\001');
/*     */   }
/*     */ 
/*     */   
/*     */   void addName(String str) {
/* 120 */     addToken(39);
/* 121 */     appendString(str);
/*     */   }
/*     */ 
/*     */   
/*     */   void addString(String str) {
/* 126 */     addToken(41);
/* 127 */     appendString(str);
/*     */   }
/*     */ 
/*     */   
/*     */   void addRegexp(String regexp, String flags) {
/* 132 */     addToken(48);
/* 133 */     appendString('/' + regexp + '/' + flags);
/*     */   }
/*     */ 
/*     */   
/*     */   void addNumber(double n) {
/* 138 */     addToken(40);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 157 */     long lbits = (long)n;
/* 158 */     if (lbits != n) {
/*     */ 
/*     */       
/* 161 */       lbits = Double.doubleToLongBits(n);
/* 162 */       append('D');
/* 163 */       append((char)(int)(lbits >> 48L));
/* 164 */       append((char)(int)(lbits >> 32L));
/* 165 */       append((char)(int)(lbits >> 16L));
/* 166 */       append((char)(int)lbits);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 171 */       if (lbits < 0L) Kit.codeBug();
/*     */ 
/*     */ 
/*     */       
/* 175 */       if (lbits <= 65535L) {
/* 176 */         append('S');
/* 177 */         append((char)(int)lbits);
/*     */       } else {
/*     */         
/* 180 */         append('J');
/* 181 */         append((char)(int)(lbits >> 48L));
/* 182 */         append((char)(int)(lbits >> 32L));
/* 183 */         append((char)(int)(lbits >> 16L));
/* 184 */         append((char)(int)lbits);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void appendString(String str) {
/* 191 */     int L = str.length();
/* 192 */     int lengthEncodingSize = 1;
/* 193 */     if (L >= 32768) {
/* 194 */       lengthEncodingSize = 2;
/*     */     }
/* 196 */     int nextTop = this.sourceTop + lengthEncodingSize + L;
/* 197 */     if (nextTop > this.sourceBuffer.length) {
/* 198 */       increaseSourceCapacity(nextTop);
/*     */     }
/* 200 */     if (L >= 32768) {
/*     */ 
/*     */       
/* 203 */       this.sourceBuffer[this.sourceTop] = (char)(0x8000 | L >>> 16);
/* 204 */       this.sourceTop++;
/*     */     } 
/* 206 */     this.sourceBuffer[this.sourceTop] = (char)L;
/* 207 */     this.sourceTop++;
/* 208 */     str.getChars(0, L, this.sourceBuffer, this.sourceTop);
/* 209 */     this.sourceTop = nextTop;
/*     */   }
/*     */ 
/*     */   
/*     */   private void append(char c) {
/* 214 */     if (this.sourceTop == this.sourceBuffer.length) {
/* 215 */       increaseSourceCapacity(this.sourceTop + 1);
/*     */     }
/* 217 */     this.sourceBuffer[this.sourceTop] = c;
/* 218 */     this.sourceTop++;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void increaseSourceCapacity(int minimalCapacity) {
/* 224 */     if (minimalCapacity <= this.sourceBuffer.length) Kit.codeBug(); 
/* 225 */     int newCapacity = this.sourceBuffer.length * 2;
/* 226 */     if (newCapacity < minimalCapacity) {
/* 227 */       newCapacity = minimalCapacity;
/*     */     }
/* 229 */     char[] tmp = new char[newCapacity];
/* 230 */     System.arraycopy(this.sourceBuffer, 0, tmp, 0, this.sourceTop);
/* 231 */     this.sourceBuffer = tmp;
/*     */   }
/*     */ 
/*     */   
/*     */   private String sourceToString(int offset) {
/* 236 */     if (offset < 0 || this.sourceTop < offset) Kit.codeBug(); 
/* 237 */     return new String(this.sourceBuffer, offset, this.sourceTop - offset);
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
/*     */   public static String decompile(String source, int flags, UintMap properties) {
/* 259 */     int topFunctionType, length = source.length();
/* 260 */     if (length == 0) return "";
/*     */     
/* 262 */     int indent = properties.getInt(1, 0);
/* 263 */     if (indent < 0) throw new IllegalArgumentException(); 
/* 264 */     int indentGap = properties.getInt(2, 4);
/* 265 */     if (indentGap < 0) throw new IllegalArgumentException(); 
/* 266 */     int caseGap = properties.getInt(3, 2);
/* 267 */     if (caseGap < 0) throw new IllegalArgumentException();
/*     */     
/* 269 */     StringBuilder result = new StringBuilder();
/* 270 */     boolean justFunctionBody = (0 != (flags & 0x1));
/* 271 */     boolean toSource = (0 != (flags & 0x2));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 300 */     int braceNesting = 0;
/* 301 */     boolean afterFirstEOL = false;
/* 302 */     int i = 0;
/*     */     
/* 304 */     if (source.charAt(i) == '') {
/* 305 */       i++;
/* 306 */       topFunctionType = -1;
/*     */     } else {
/* 308 */       topFunctionType = source.charAt(i + 1);
/*     */     } 
/*     */     
/* 311 */     if (!toSource) {
/*     */       
/* 313 */       result.append('\n');
/* 314 */       for (int j = 0; j < indent; j++) {
/* 315 */         result.append(' ');
/*     */       }
/* 317 */     } else if (topFunctionType == 2) {
/* 318 */       result.append('(');
/*     */     } 
/*     */ 
/*     */     
/* 322 */     while (i < length) {
/* 323 */       boolean newLine; switch (source.charAt(i)) {
/*     */         case '':
/*     */         case '':
/* 326 */           result.append((source.charAt(i) == '') ? "get " : "set ");
/* 327 */           i++;
/* 328 */           i = printSourceString(source, i + 1, false, result);
/*     */           
/* 330 */           i++;
/*     */           break;
/*     */         
/*     */         case '\'':
/*     */         case '0':
/* 335 */           i = printSourceString(source, i + 1, false, result);
/*     */           continue;
/*     */         
/*     */         case ')':
/* 339 */           i = printSourceString(source, i + 1, true, result);
/*     */           continue;
/*     */         
/*     */         case '(':
/* 343 */           i = printSourceNumber(source, i + 1, result);
/*     */           continue;
/*     */         
/*     */         case '-':
/* 347 */           result.append("true");
/*     */           break;
/*     */         
/*     */         case ',':
/* 351 */           result.append("false");
/*     */           break;
/*     */         
/*     */         case '*':
/* 355 */           result.append("null");
/*     */           break;
/*     */         
/*     */         case '+':
/* 359 */           result.append("this");
/*     */           break;
/*     */         
/*     */         case 'm':
/* 363 */           i++;
/* 364 */           result.append("function ");
/*     */           break;
/*     */ 
/*     */         
/*     */         case '¤':
/*     */           break;
/*     */         
/*     */         case 'Y':
/* 372 */           result.append(", ");
/*     */           break;
/*     */         
/*     */         case 'U':
/* 376 */           braceNesting++;
/* 377 */           if (1 == getNext(source, length, i))
/* 378 */             indent += indentGap; 
/* 379 */           result.append('{');
/*     */           break;
/*     */         
/*     */         case 'V':
/* 383 */           braceNesting--;
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 388 */           if (justFunctionBody && braceNesting == 0) {
/*     */             break;
/*     */           }
/* 391 */           result.append('}');
/* 392 */           switch (getNext(source, length, i)) {
/*     */             case 1:
/*     */             case 164:
/* 395 */               indent -= indentGap;
/*     */               break;
/*     */             case 113:
/*     */             case 117:
/* 399 */               indent -= indentGap;
/* 400 */               result.append(' ');
/*     */               break;
/*     */           } 
/*     */           
/*     */           break;
/*     */         case 'W':
/* 406 */           result.append('(');
/*     */           break;
/*     */         
/*     */         case 'X':
/* 410 */           result.append(')');
/* 411 */           if (85 == getNext(source, length, i)) {
/* 412 */             result.append(' ');
/*     */           }
/*     */           break;
/*     */         case 'S':
/* 416 */           result.append('[');
/*     */           break;
/*     */         
/*     */         case 'T':
/* 420 */           result.append(']');
/*     */           break;
/*     */         
/*     */         case '\001':
/* 424 */           if (toSource)
/* 425 */             break;  newLine = true;
/* 426 */           if (!afterFirstEOL) {
/* 427 */             afterFirstEOL = true;
/* 428 */             if (justFunctionBody) {
/*     */ 
/*     */ 
/*     */               
/* 432 */               result.setLength(0);
/* 433 */               indent -= indentGap;
/* 434 */               newLine = false;
/*     */             } 
/*     */           } 
/* 437 */           if (newLine) {
/* 438 */             result.append('\n');
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 445 */           if (i + 1 < length) {
/* 446 */             int less = 0;
/* 447 */             int nextToken = source.charAt(i + 1);
/* 448 */             if (nextToken == 115 || nextToken == 116) {
/*     */ 
/*     */               
/* 451 */               less = indentGap - caseGap;
/* 452 */             } else if (nextToken == 86) {
/* 453 */               less = indentGap;
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             }
/* 459 */             else if (nextToken == 39) {
/* 460 */               int afterName = getSourceStringEnd(source, i + 2);
/* 461 */               if (source.charAt(afterName) == 'g') {
/* 462 */                 less = indentGap;
/*     */               }
/*     */             } 
/* 465 */             for (; less < indent; less++) {
/* 466 */               result.append(' ');
/*     */             }
/*     */           } 
/*     */           break;
/*     */         case 'l':
/* 471 */           result.append('.');
/*     */           break;
/*     */         
/*     */         case '\036':
/* 475 */           result.append("new ");
/*     */           break;
/*     */         
/*     */         case '\037':
/* 479 */           result.append("delete ");
/*     */           break;
/*     */         
/*     */         case 'p':
/* 483 */           result.append("if ");
/*     */           break;
/*     */         
/*     */         case 'q':
/* 487 */           result.append("else ");
/*     */           break;
/*     */         
/*     */         case 'w':
/* 491 */           result.append("for ");
/*     */           break;
/*     */         
/*     */         case '4':
/* 495 */           result.append(" in ");
/*     */           break;
/*     */         
/*     */         case '{':
/* 499 */           result.append("with ");
/*     */           break;
/*     */         
/*     */         case 'u':
/* 503 */           result.append("while ");
/*     */           break;
/*     */         
/*     */         case 'v':
/* 507 */           result.append("do ");
/*     */           break;
/*     */         
/*     */         case 'Q':
/* 511 */           result.append("try ");
/*     */           break;
/*     */         
/*     */         case '|':
/* 515 */           result.append("catch ");
/*     */           break;
/*     */         
/*     */         case '}':
/* 519 */           result.append("finally ");
/*     */           break;
/*     */         
/*     */         case '2':
/* 523 */           result.append("throw ");
/*     */           break;
/*     */         
/*     */         case 'r':
/* 527 */           result.append("switch ");
/*     */           break;
/*     */         
/*     */         case 'x':
/* 531 */           result.append("break");
/* 532 */           if (39 == getNext(source, length, i)) {
/* 533 */             result.append(' ');
/*     */           }
/*     */           break;
/*     */         case 'y':
/* 537 */           result.append("continue");
/* 538 */           if (39 == getNext(source, length, i)) {
/* 539 */             result.append(' ');
/*     */           }
/*     */           break;
/*     */         case 's':
/* 543 */           result.append("case ");
/*     */           break;
/*     */         
/*     */         case 't':
/* 547 */           result.append("default");
/*     */           break;
/*     */         
/*     */         case '\004':
/* 551 */           result.append("return");
/* 552 */           if (82 != getNext(source, length, i)) {
/* 553 */             result.append(' ');
/*     */           }
/*     */           break;
/*     */         case 'z':
/* 557 */           result.append("var ");
/*     */           break;
/*     */         
/*     */         case '':
/* 561 */           result.append("let ");
/*     */           break;
/*     */         
/*     */         case 'R':
/* 565 */           result.append(';');
/* 566 */           if (1 != getNext(source, length, i))
/*     */           {
/* 568 */             result.append(' ');
/*     */           }
/*     */           break;
/*     */         
/*     */         case 'Z':
/* 573 */           result.append(" = ");
/*     */           break;
/*     */         
/*     */         case 'a':
/* 577 */           result.append(" += ");
/*     */           break;
/*     */         
/*     */         case 'b':
/* 581 */           result.append(" -= ");
/*     */           break;
/*     */         
/*     */         case 'c':
/* 585 */           result.append(" *= ");
/*     */           break;
/*     */         
/*     */         case 'd':
/* 589 */           result.append(" /= ");
/*     */           break;
/*     */         
/*     */         case 'e':
/* 593 */           result.append(" %= ");
/*     */           break;
/*     */         
/*     */         case '[':
/* 597 */           result.append(" |= ");
/*     */           break;
/*     */         
/*     */         case '\\':
/* 601 */           result.append(" ^= ");
/*     */           break;
/*     */         
/*     */         case ']':
/* 605 */           result.append(" &= ");
/*     */           break;
/*     */         
/*     */         case '^':
/* 609 */           result.append(" <<= ");
/*     */           break;
/*     */         
/*     */         case '_':
/* 613 */           result.append(" >>= ");
/*     */           break;
/*     */         
/*     */         case '`':
/* 617 */           result.append(" >>>= ");
/*     */           break;
/*     */         
/*     */         case 'f':
/* 621 */           result.append(" ? ");
/*     */           break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case 'B':
/* 630 */           result.append(": ");
/*     */           break;
/*     */         
/*     */         case 'g':
/* 634 */           if (1 == getNext(source, length, i)) {
/*     */             
/* 636 */             result.append(':');
/*     */             break;
/*     */           } 
/* 639 */           result.append(" : ");
/*     */           break;
/*     */         
/*     */         case 'h':
/* 643 */           result.append(" || ");
/*     */           break;
/*     */         
/*     */         case 'i':
/* 647 */           result.append(" && ");
/*     */           break;
/*     */         
/*     */         case '\t':
/* 651 */           result.append(" | ");
/*     */           break;
/*     */         
/*     */         case '\n':
/* 655 */           result.append(" ^ ");
/*     */           break;
/*     */         
/*     */         case '\013':
/* 659 */           result.append(" & ");
/*     */           break;
/*     */         
/*     */         case '.':
/* 663 */           result.append(" === ");
/*     */           break;
/*     */         
/*     */         case '/':
/* 667 */           result.append(" !== ");
/*     */           break;
/*     */         
/*     */         case '\f':
/* 671 */           result.append(" == ");
/*     */           break;
/*     */         
/*     */         case '\r':
/* 675 */           result.append(" != ");
/*     */           break;
/*     */         
/*     */         case '\017':
/* 679 */           result.append(" <= ");
/*     */           break;
/*     */         
/*     */         case '\016':
/* 683 */           result.append(" < ");
/*     */           break;
/*     */         
/*     */         case '\021':
/* 687 */           result.append(" >= ");
/*     */           break;
/*     */         
/*     */         case '\020':
/* 691 */           result.append(" > ");
/*     */           break;
/*     */         
/*     */         case '5':
/* 695 */           result.append(" instanceof ");
/*     */           break;
/*     */         
/*     */         case '\022':
/* 699 */           result.append(" << ");
/*     */           break;
/*     */         
/*     */         case '\023':
/* 703 */           result.append(" >> ");
/*     */           break;
/*     */         
/*     */         case '\024':
/* 707 */           result.append(" >>> ");
/*     */           break;
/*     */         
/*     */         case ' ':
/* 711 */           result.append("typeof ");
/*     */           break;
/*     */         
/*     */         case '~':
/* 715 */           result.append("void ");
/*     */           break;
/*     */         
/*     */         case '':
/* 719 */           result.append("const ");
/*     */           break;
/*     */         
/*     */         case 'H':
/* 723 */           result.append("yield ");
/*     */           break;
/*     */         
/*     */         case '\032':
/* 727 */           result.append('!');
/*     */           break;
/*     */         
/*     */         case '\033':
/* 731 */           result.append('~');
/*     */           break;
/*     */         
/*     */         case '\034':
/* 735 */           result.append('+');
/*     */           break;
/*     */         
/*     */         case '\035':
/* 739 */           result.append('-');
/*     */           break;
/*     */         
/*     */         case 'j':
/* 743 */           result.append("++");
/*     */           break;
/*     */         
/*     */         case 'k':
/* 747 */           result.append("--");
/*     */           break;
/*     */         
/*     */         case '\025':
/* 751 */           result.append(" + ");
/*     */           break;
/*     */         
/*     */         case '\026':
/* 755 */           result.append(" - ");
/*     */           break;
/*     */         
/*     */         case '\027':
/* 759 */           result.append(" * ");
/*     */           break;
/*     */         
/*     */         case '\030':
/* 763 */           result.append(" / ");
/*     */           break;
/*     */         
/*     */         case '\031':
/* 767 */           result.append(" % ");
/*     */           break;
/*     */         
/*     */         case '':
/* 771 */           result.append("::");
/*     */           break;
/*     */         
/*     */         case '':
/* 775 */           result.append("..");
/*     */           break;
/*     */         
/*     */         case '':
/* 779 */           result.append(".(");
/*     */           break;
/*     */         
/*     */         case '':
/* 783 */           result.append('@');
/*     */           break;
/*     */         
/*     */         case ' ':
/* 787 */           result.append("debugger;\n");
/*     */           break;
/*     */ 
/*     */         
/*     */         default:
/* 792 */           throw new RuntimeException("Token: " + Token.name(source.charAt(i)));
/*     */       } 
/*     */       
/* 795 */       i++;
/*     */     } 
/*     */     
/* 798 */     if (!toSource) {
/*     */       
/* 800 */       if (!justFunctionBody) {
/* 801 */         result.append('\n');
/*     */       }
/* 803 */     } else if (topFunctionType == 2) {
/* 804 */       result.append(')');
/*     */     } 
/*     */ 
/*     */     
/* 808 */     return result.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private static int getNext(String source, int length, int i) {
/* 813 */     return (i + 1 < length) ? source.charAt(i + 1) : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int getSourceStringEnd(String source, int offset) {
/* 818 */     return printSourceString(source, offset, false, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int printSourceString(String source, int offset, boolean asQuotedString, StringBuilder sb) {
/* 825 */     int length = source.charAt(offset);
/* 826 */     offset++;
/* 827 */     if ((0x8000 & length) != 0) {
/* 828 */       length = (0x7FFF & length) << 16 | source.charAt(offset);
/* 829 */       offset++;
/*     */     } 
/* 831 */     if (sb != null) {
/* 832 */       String str = source.substring(offset, offset + length);
/* 833 */       if (!asQuotedString) {
/* 834 */         sb.append(str);
/*     */       } else {
/* 836 */         sb.append('"');
/* 837 */         sb.append(ScriptRuntime.escapeString(str));
/* 838 */         sb.append('"');
/*     */       } 
/*     */     } 
/* 841 */     return offset + length;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int printSourceNumber(String source, int offset, StringBuilder sb) {
/* 847 */     double number = 0.0D;
/* 848 */     char type = source.charAt(offset);
/* 849 */     offset++;
/* 850 */     if (type == 'S') {
/* 851 */       if (sb != null) {
/* 852 */         int ival = source.charAt(offset);
/* 853 */         number = ival;
/*     */       } 
/* 855 */       offset++;
/* 856 */     } else if (type == 'J' || type == 'D') {
/* 857 */       if (sb != null) {
/*     */         
/* 859 */         long lbits = source.charAt(offset) << 48L;
/* 860 */         lbits |= source.charAt(offset + 1) << 32L;
/* 861 */         lbits |= source.charAt(offset + 2) << 16L;
/* 862 */         lbits |= source.charAt(offset + 3);
/* 863 */         if (type == 'J') {
/* 864 */           number = lbits;
/*     */         } else {
/* 866 */           number = Double.longBitsToDouble(lbits);
/*     */         } 
/*     */       } 
/* 869 */       offset += 4;
/*     */     } else {
/*     */       
/* 872 */       throw new RuntimeException();
/*     */     } 
/* 874 */     if (sb != null) {
/* 875 */       sb.append(ScriptRuntime.numberToString(number, 10));
/*     */     }
/* 877 */     return offset;
/*     */   }
/*     */   
/* 880 */   private char[] sourceBuffer = new char[128];
/*     */   private int sourceTop;
/*     */   private static final boolean printSource = false;
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\Decompiler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */