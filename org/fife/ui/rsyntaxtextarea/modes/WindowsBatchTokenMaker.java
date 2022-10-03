/*     */ package org.fife.ui.rsyntaxtextarea.modes;
/*     */ 
/*     */ import javax.swing.text.Segment;
/*     */ import org.fife.ui.rsyntaxtextarea.AbstractTokenMaker;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;
/*     */ import org.fife.ui.rsyntaxtextarea.Token;
/*     */ import org.fife.ui.rsyntaxtextarea.TokenMap;
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
/*     */ public class WindowsBatchTokenMaker
/*     */   extends AbstractTokenMaker
/*     */ {
/*     */   private static final String OPERATORS = "@:*<>=?";
/*     */   private int currentTokenStart;
/*     */   private int currentTokenType;
/*     */   private VariableType varType;
/*     */   
/*     */   public void addToken(Segment segment, int start, int end, int tokenType, int startOffset) {
/*     */     int value;
/*  55 */     switch (tokenType) {
/*     */ 
/*     */ 
/*     */       
/*     */       case 20:
/*  60 */         value = this.wordsToHighlight.get(segment, start, end);
/*  61 */         if (value != -1) {
/*  62 */           tokenType = value;
/*     */         }
/*     */         break;
/*     */     } 
/*  66 */     super.addToken(segment, start, end, tokenType, startOffset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getLineCommentStartAndEnd(int languageIndex) {
/*  76 */     return new String[] { "rem ", null };
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
/*     */   public boolean getMarkOccurrencesOfTokenType(int type) {
/*  90 */     return (type == 20 || type == 17);
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
/*     */   public TokenMap getWordsToHighlight() {
/* 104 */     TokenMap tokenMap = new TokenMap(true);
/* 105 */     int reservedWord = 6;
/*     */ 
/*     */     
/* 108 */     tokenMap.put("goto", reservedWord);
/* 109 */     tokenMap.put("if", reservedWord);
/* 110 */     tokenMap.put("shift", reservedWord);
/* 111 */     tokenMap.put("start", reservedWord);
/*     */ 
/*     */     
/* 114 */     tokenMap.put("ansi.sys", reservedWord);
/* 115 */     tokenMap.put("append", reservedWord);
/* 116 */     tokenMap.put("arp", reservedWord);
/* 117 */     tokenMap.put("assign", reservedWord);
/* 118 */     tokenMap.put("assoc", reservedWord);
/* 119 */     tokenMap.put("at", reservedWord);
/* 120 */     tokenMap.put("attrib", reservedWord);
/* 121 */     tokenMap.put("break", reservedWord);
/* 122 */     tokenMap.put("cacls", reservedWord);
/* 123 */     tokenMap.put("call", reservedWord);
/* 124 */     tokenMap.put("cd", reservedWord);
/* 125 */     tokenMap.put("chcp", reservedWord);
/* 126 */     tokenMap.put("chdir", reservedWord);
/* 127 */     tokenMap.put("chkdsk", reservedWord);
/* 128 */     tokenMap.put("chknfts", reservedWord);
/* 129 */     tokenMap.put("choice", reservedWord);
/* 130 */     tokenMap.put("cls", reservedWord);
/* 131 */     tokenMap.put("cmd", reservedWord);
/* 132 */     tokenMap.put("color", reservedWord);
/* 133 */     tokenMap.put("comp", reservedWord);
/* 134 */     tokenMap.put("compact", reservedWord);
/* 135 */     tokenMap.put("control", reservedWord);
/* 136 */     tokenMap.put("convert", reservedWord);
/* 137 */     tokenMap.put("copy", reservedWord);
/* 138 */     tokenMap.put("ctty", reservedWord);
/* 139 */     tokenMap.put("date", reservedWord);
/* 140 */     tokenMap.put("debug", reservedWord);
/* 141 */     tokenMap.put("defrag", reservedWord);
/* 142 */     tokenMap.put("del", reservedWord);
/* 143 */     tokenMap.put("deltree", reservedWord);
/* 144 */     tokenMap.put("dir", reservedWord);
/* 145 */     tokenMap.put("diskcomp", reservedWord);
/* 146 */     tokenMap.put("diskcopy", reservedWord);
/* 147 */     tokenMap.put("do", reservedWord);
/* 148 */     tokenMap.put("doskey", reservedWord);
/* 149 */     tokenMap.put("dosshell", reservedWord);
/* 150 */     tokenMap.put("drivparm", reservedWord);
/* 151 */     tokenMap.put("echo", reservedWord);
/* 152 */     tokenMap.put("edit", reservedWord);
/* 153 */     tokenMap.put("edlin", reservedWord);
/* 154 */     tokenMap.put("emm386", reservedWord);
/* 155 */     tokenMap.put("erase", reservedWord);
/* 156 */     tokenMap.put("exist", reservedWord);
/* 157 */     tokenMap.put("exit", reservedWord);
/* 158 */     tokenMap.put("expand", reservedWord);
/* 159 */     tokenMap.put("extract", reservedWord);
/* 160 */     tokenMap.put("fasthelp", reservedWord);
/* 161 */     tokenMap.put("fc", reservedWord);
/* 162 */     tokenMap.put("fdisk", reservedWord);
/* 163 */     tokenMap.put("find", reservedWord);
/* 164 */     tokenMap.put("for", reservedWord);
/* 165 */     tokenMap.put("format", reservedWord);
/* 166 */     tokenMap.put("ftp", reservedWord);
/* 167 */     tokenMap.put("graftabl", reservedWord);
/* 168 */     tokenMap.put("help", reservedWord);
/* 169 */     tokenMap.put("ifshlp.sys", reservedWord);
/* 170 */     tokenMap.put("in", reservedWord);
/* 171 */     tokenMap.put("ipconfig", reservedWord);
/* 172 */     tokenMap.put("keyb", reservedWord);
/* 173 */     tokenMap.put("kill", reservedWord);
/* 174 */     tokenMap.put("label", reservedWord);
/* 175 */     tokenMap.put("lh", reservedWord);
/* 176 */     tokenMap.put("loadfix", reservedWord);
/* 177 */     tokenMap.put("loadhigh", reservedWord);
/* 178 */     tokenMap.put("lock", reservedWord);
/* 179 */     tokenMap.put("md", reservedWord);
/* 180 */     tokenMap.put("mem", reservedWord);
/* 181 */     tokenMap.put("mkdir", reservedWord);
/* 182 */     tokenMap.put("mklink", reservedWord);
/* 183 */     tokenMap.put("mode", reservedWord);
/* 184 */     tokenMap.put("more", reservedWord);
/* 185 */     tokenMap.put("move", reservedWord);
/* 186 */     tokenMap.put("msav", reservedWord);
/* 187 */     tokenMap.put("msd", reservedWord);
/* 188 */     tokenMap.put("mscdex", reservedWord);
/* 189 */     tokenMap.put("nbtstat", reservedWord);
/* 190 */     tokenMap.put("net", reservedWord);
/* 191 */     tokenMap.put("netstat", reservedWord);
/* 192 */     tokenMap.put("nlsfunc", reservedWord);
/* 193 */     tokenMap.put("not", reservedWord);
/* 194 */     tokenMap.put("nslookup", reservedWord);
/* 195 */     tokenMap.put("path", reservedWord);
/* 196 */     tokenMap.put("pathping", reservedWord);
/* 197 */     tokenMap.put("pause", reservedWord);
/* 198 */     tokenMap.put("ping", reservedWord);
/* 199 */     tokenMap.put("power", reservedWord);
/* 200 */     tokenMap.put("print", reservedWord);
/* 201 */     tokenMap.put("prompt", reservedWord);
/* 202 */     tokenMap.put("pushd", reservedWord);
/* 203 */     tokenMap.put("popd", reservedWord);
/* 204 */     tokenMap.put("qbasic", reservedWord);
/* 205 */     tokenMap.put("rd", reservedWord);
/* 206 */     tokenMap.put("ren", reservedWord);
/* 207 */     tokenMap.put("rename", reservedWord);
/* 208 */     tokenMap.put("rmdir", reservedWord);
/* 209 */     tokenMap.put("route", reservedWord);
/* 210 */     tokenMap.put("sc", reservedWord);
/* 211 */     tokenMap.put("scandisk", reservedWord);
/* 212 */     tokenMap.put("scandreg", reservedWord);
/* 213 */     tokenMap.put("set", reservedWord);
/* 214 */     tokenMap.put("setx", reservedWord);
/* 215 */     tokenMap.put("setver", reservedWord);
/* 216 */     tokenMap.put("share", reservedWord);
/* 217 */     tokenMap.put("shutdown", reservedWord);
/* 218 */     tokenMap.put("smartdrv", reservedWord);
/* 219 */     tokenMap.put("sort", reservedWord);
/* 220 */     tokenMap.put("subset", reservedWord);
/* 221 */     tokenMap.put("switches", reservedWord);
/* 222 */     tokenMap.put("sys", reservedWord);
/* 223 */     tokenMap.put("time", reservedWord);
/* 224 */     tokenMap.put("tracert", reservedWord);
/* 225 */     tokenMap.put("tree", reservedWord);
/* 226 */     tokenMap.put("type", reservedWord);
/* 227 */     tokenMap.put("undelete", reservedWord);
/* 228 */     tokenMap.put("unformat", reservedWord);
/* 229 */     tokenMap.put("unlock", reservedWord);
/* 230 */     tokenMap.put("ver", reservedWord);
/* 231 */     tokenMap.put("verify", reservedWord);
/* 232 */     tokenMap.put("vol", reservedWord);
/* 233 */     tokenMap.put("xcopy", reservedWord);
/*     */     
/* 235 */     return tokenMap;
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
/*     */   public Token getTokenList(Segment text, int startTokenType, int startOffset) {
/* 251 */     resetTokenList();
/*     */     
/* 253 */     char[] array = text.array;
/* 254 */     int offset = text.offset;
/* 255 */     int count = text.count;
/* 256 */     int end = offset + count;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 262 */     int newStartOffset = startOffset - offset;
/*     */     
/* 264 */     this.currentTokenStart = offset;
/* 265 */     this.currentTokenType = startTokenType;
/*     */ 
/*     */     
/* 268 */     for (int i = offset; i < end; i++) {
/*     */       int indexOf;
/* 270 */       char c = array[i];
/*     */       
/* 272 */       switch (this.currentTokenType) {
/*     */ 
/*     */         
/*     */         case 0:
/* 276 */           this.currentTokenStart = i;
/*     */           
/* 278 */           switch (c) {
/*     */             
/*     */             case '\t':
/*     */             case ' ':
/* 282 */               this.currentTokenType = 21;
/*     */               break;
/*     */             
/*     */             case '"':
/* 286 */               this.currentTokenType = 37;
/*     */               break;
/*     */             
/*     */             case '%':
/* 290 */               this.currentTokenType = 17;
/*     */               break;
/*     */ 
/*     */             
/*     */             case '(':
/*     */             case ')':
/* 296 */               addToken(text, this.currentTokenStart, i, 22, newStartOffset + this.currentTokenStart);
/* 297 */               this.currentTokenType = 0;
/*     */               break;
/*     */ 
/*     */             
/*     */             case ',':
/*     */             case ';':
/* 303 */               addToken(text, this.currentTokenStart, i, 20, newStartOffset + this.currentTokenStart);
/* 304 */               this.currentTokenType = 0;
/*     */               break;
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             case ':':
/* 311 */               if (this.firstToken == null) {
/* 312 */                 if (i < end - 1 && array[i + 1] == ':') {
/* 313 */                   this.currentTokenType = 1;
/*     */                   break;
/*     */                 } 
/* 316 */                 this.currentTokenType = 24;
/*     */                 
/*     */                 break;
/*     */               } 
/* 320 */               this.currentTokenType = 20;
/*     */               break;
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 327 */           if (RSyntaxUtilities.isLetterOrDigit(c) || c == '\\') {
/* 328 */             this.currentTokenType = 20;
/*     */             
/*     */             break;
/*     */           } 
/* 332 */           indexOf = "@:*<>=?".indexOf(c, 0);
/* 333 */           if (indexOf > -1) {
/* 334 */             addToken(text, this.currentTokenStart, i, 23, newStartOffset + this.currentTokenStart);
/* 335 */             this.currentTokenType = 0;
/*     */             
/*     */             break;
/*     */           } 
/* 339 */           this.currentTokenType = 20;
/*     */           break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case 21:
/* 349 */           switch (c) {
/*     */             case '\t':
/*     */             case ' ':
/*     */               break;
/*     */ 
/*     */             
/*     */             case '"':
/* 356 */               addToken(text, this.currentTokenStart, i - 1, 21, newStartOffset + this.currentTokenStart);
/* 357 */               this.currentTokenStart = i;
/* 358 */               this.currentTokenType = 37;
/*     */               break;
/*     */             
/*     */             case '%':
/* 362 */               addToken(text, this.currentTokenStart, i - 1, 21, newStartOffset + this.currentTokenStart);
/* 363 */               this.currentTokenStart = i;
/* 364 */               this.currentTokenType = 17;
/*     */               break;
/*     */ 
/*     */             
/*     */             case '(':
/*     */             case ')':
/* 370 */               addToken(text, this.currentTokenStart, i - 1, 21, newStartOffset + this.currentTokenStart);
/* 371 */               addToken(text, i, i, 22, newStartOffset + i);
/* 372 */               this.currentTokenType = 0;
/*     */               break;
/*     */ 
/*     */             
/*     */             case ',':
/*     */             case ';':
/* 378 */               addToken(text, this.currentTokenStart, i - 1, 21, newStartOffset + this.currentTokenStart);
/* 379 */               addToken(text, i, i, 20, newStartOffset + i);
/* 380 */               this.currentTokenType = 0;
/*     */               break;
/*     */ 
/*     */             
/*     */             case ':':
/* 385 */               addToken(text, this.currentTokenStart, i - 1, 21, newStartOffset + this.currentTokenStart);
/* 386 */               this.currentTokenStart = i;
/*     */ 
/*     */               
/* 389 */               if (this.firstToken.getNextToken() == null) {
/* 390 */                 if (i < end - 1 && array[i + 1] == ':') {
/* 391 */                   this.currentTokenType = 1;
/*     */                   break;
/*     */                 } 
/* 394 */                 this.currentTokenType = 24;
/*     */                 
/*     */                 break;
/*     */               } 
/* 398 */               this.currentTokenType = 20;
/*     */               break;
/*     */           } 
/*     */ 
/*     */ 
/*     */           
/* 404 */           addToken(text, this.currentTokenStart, i - 1, 21, newStartOffset + this.currentTokenStart);
/* 405 */           this.currentTokenStart = i;
/*     */ 
/*     */           
/* 408 */           if (RSyntaxUtilities.isLetterOrDigit(c) || c == '\\') {
/* 409 */             this.currentTokenType = 20;
/*     */             
/*     */             break;
/*     */           } 
/* 413 */           indexOf = "@:*<>=?".indexOf(c, 0);
/* 414 */           if (indexOf > -1) {
/* 415 */             addToken(text, this.currentTokenStart, i, 23, newStartOffset + this.currentTokenStart);
/* 416 */             this.currentTokenType = 0;
/*     */             
/*     */             break;
/*     */           } 
/* 420 */           this.currentTokenType = 20;
/*     */           break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         default:
/* 430 */           switch (c) {
/*     */ 
/*     */             
/*     */             case '\t':
/*     */             case ' ':
/* 435 */               if (i - this.currentTokenStart == 3 && (array[i - 3] == 'r' || array[i - 3] == 'R') && (array[i - 2] == 'e' || array[i - 2] == 'E') && (array[i - 1] == 'm' || array[i - 1] == 'M')) {
/*     */ 
/*     */ 
/*     */                 
/* 439 */                 this.currentTokenType = 1;
/*     */                 break;
/*     */               } 
/* 442 */               addToken(text, this.currentTokenStart, i - 1, 20, newStartOffset + this.currentTokenStart);
/* 443 */               this.currentTokenStart = i;
/* 444 */               this.currentTokenType = 21;
/*     */               break;
/*     */             
/*     */             case '"':
/* 448 */               addToken(text, this.currentTokenStart, i - 1, 20, newStartOffset + this.currentTokenStart);
/* 449 */               this.currentTokenStart = i;
/* 450 */               this.currentTokenType = 37;
/*     */               break;
/*     */             
/*     */             case '%':
/* 454 */               addToken(text, this.currentTokenStart, i - 1, 20, newStartOffset + this.currentTokenStart);
/* 455 */               this.currentTokenStart = i;
/* 456 */               this.currentTokenType = 17;
/*     */               break;
/*     */ 
/*     */ 
/*     */             
/*     */             case '\\':
/* 462 */               if (i - this.currentTokenStart == 3 && (array[i - 3] == 'r' || array[i - 3] == 'R') && (array[i - 2] == 'e' || array[i - 2] == 'E') && (array[i - 1] == 'm' || array[i - 1] == 'M'))
/*     */               {
/*     */ 
/*     */                 
/* 466 */                 this.currentTokenType = 1;
/*     */               }
/*     */               break;
/*     */ 
/*     */             
/*     */             case '.':
/*     */             case '_':
/*     */               break;
/*     */             
/*     */             case '(':
/*     */             case ')':
/* 477 */               addToken(text, this.currentTokenStart, i - 1, 20, newStartOffset + this.currentTokenStart);
/* 478 */               addToken(text, i, i, 22, newStartOffset + i);
/* 479 */               this.currentTokenType = 0;
/*     */               break;
/*     */ 
/*     */             
/*     */             case ',':
/*     */             case ';':
/* 485 */               addToken(text, this.currentTokenStart, i - 1, 20, newStartOffset + this.currentTokenStart);
/* 486 */               addToken(text, i, i, 20, newStartOffset + i);
/* 487 */               this.currentTokenType = 0;
/*     */               break;
/*     */           } 
/*     */ 
/*     */ 
/*     */           
/* 493 */           if (RSyntaxUtilities.isLetterOrDigit(c) || c == '\\') {
/*     */             break;
/*     */           }
/*     */           
/* 497 */           indexOf = "@:*<>=?".indexOf(c);
/* 498 */           if (indexOf > -1) {
/* 499 */             addToken(text, this.currentTokenStart, i - 1, 20, newStartOffset + this.currentTokenStart);
/* 500 */             addToken(text, i, i, 23, newStartOffset + i);
/* 501 */             this.currentTokenType = 0;
/*     */           } 
/*     */           break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case 1:
/* 512 */           i = end - 1;
/* 513 */           addToken(text, this.currentTokenStart, i, 1, newStartOffset + this.currentTokenStart);
/*     */           
/* 515 */           this.currentTokenType = 0;
/*     */           break;
/*     */         
/*     */         case 24:
/* 519 */           i = end - 1;
/* 520 */           addToken(text, this.currentTokenStart, i, 24, newStartOffset + this.currentTokenStart);
/*     */           
/* 522 */           this.currentTokenType = 0;
/*     */           break;
/*     */ 
/*     */         
/*     */         case 37:
/* 527 */           if (c == '"') {
/* 528 */             addToken(text, this.currentTokenStart, i, 13, newStartOffset + this.currentTokenStart);
/* 529 */             this.currentTokenStart = i + 1;
/* 530 */             this.currentTokenType = 0;
/*     */           } 
/*     */           break;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case 17:
/* 538 */           if (i == this.currentTokenStart + 1) {
/* 539 */             this.varType = VariableType.NORMAL_VAR;
/* 540 */             switch (c) {
/*     */               case '{':
/* 542 */                 this.varType = VariableType.BRACKET_VAR;
/*     */                 break;
/*     */               case '~':
/* 545 */                 this.varType = VariableType.TILDE_VAR;
/*     */                 break;
/*     */               case '%':
/* 548 */                 this.varType = VariableType.DOUBLE_PERCENT_VAR;
/*     */                 break;
/*     */             } 
/* 551 */             if (RSyntaxUtilities.isLetter(c) || c == '_' || c == ' ') {
/*     */               break;
/*     */             }
/* 554 */             if (RSyntaxUtilities.isDigit(c)) {
/* 555 */               addToken(text, this.currentTokenStart, i, 17, newStartOffset + this.currentTokenStart);
/* 556 */               this.currentTokenType = 0;
/*     */               
/*     */               break;
/*     */             } 
/* 560 */             addToken(text, this.currentTokenStart, i - 1, 17, newStartOffset + this.currentTokenStart);
/* 561 */             i--;
/* 562 */             this.currentTokenType = 0;
/*     */ 
/*     */             
/*     */             break;
/*     */           } 
/*     */           
/* 568 */           switch (this.varType) {
/*     */             case BRACKET_VAR:
/* 570 */               if (c == '}') {
/* 571 */                 addToken(text, this.currentTokenStart, i, 17, newStartOffset + this.currentTokenStart);
/* 572 */                 this.currentTokenType = 0;
/*     */               } 
/*     */               break;
/*     */             case TILDE_VAR:
/* 576 */               if (!RSyntaxUtilities.isLetterOrDigit(c)) {
/* 577 */                 addToken(text, this.currentTokenStart, i - 1, 17, newStartOffset + this.currentTokenStart);
/* 578 */                 i--;
/* 579 */                 this.currentTokenType = 0;
/*     */               } 
/*     */               break;
/*     */ 
/*     */             
/*     */             case DOUBLE_PERCENT_VAR:
/* 585 */               if (c == '%') {
/* 586 */                 if (i < end - 1 && array[i + 1] == '%') {
/* 587 */                   i++;
/* 588 */                   addToken(text, this.currentTokenStart, i, 17, newStartOffset + this.currentTokenStart);
/* 589 */                   this.currentTokenType = 0;
/*     */                 }  break;
/*     */               } 
/* 592 */               if (!RSyntaxUtilities.isLetterOrDigit(c) && c != ':' && c != '~' && c != ',' && c != '-') {
/* 593 */                 addToken(text, this.currentTokenStart, i - 1, 17, newStartOffset + this.currentTokenStart);
/* 594 */                 this.currentTokenType = 0;
/* 595 */                 i--;
/*     */               } 
/*     */               break;
/*     */           } 
/* 599 */           if (c == '%') {
/* 600 */             addToken(text, this.currentTokenStart, i, 17, newStartOffset + this.currentTokenStart);
/* 601 */             this.currentTokenType = 0;
/*     */           } 
/*     */           break;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     } 
/* 613 */     if (this.currentTokenType != 0) {
/*     */ 
/*     */       
/* 616 */       if (end - this.currentTokenStart == 3 && (array[end - 3] == 'r' || array[end - 3] == 'R') && (array[end - 2] == 'e' || array[end - 2] == 'E') && (array[end - 1] == 'm' || array[end - 1] == 'M'))
/*     */       {
/*     */ 
/*     */         
/* 620 */         this.currentTokenType = 1;
/*     */       }
/*     */       
/* 623 */       addToken(text, this.currentTokenStart, end - 1, this.currentTokenType, newStartOffset + this.currentTokenStart);
/*     */     } 
/*     */     
/* 626 */     addNullToken();
/*     */ 
/*     */     
/* 629 */     return (Token)this.firstToken;
/*     */   }
/*     */ 
/*     */   
/*     */   private enum VariableType
/*     */   {
/* 635 */     BRACKET_VAR,
/* 636 */     TILDE_VAR,
/* 637 */     NORMAL_VAR,
/* 638 */     DOUBLE_PERCENT_VAR;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\modes\WindowsBatchTokenMaker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */