/*      */ package org.fife.ui.rsyntaxtextarea.modes;
/*      */ 
/*      */ import javax.swing.text.Segment;
/*      */ import org.fife.ui.rsyntaxtextarea.AbstractTokenMaker;
/*      */ import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;
/*      */ import org.fife.ui.rsyntaxtextarea.Token;
/*      */ import org.fife.ui.rsyntaxtextarea.TokenMap;
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
/*      */ public class UnixShellTokenMaker
/*      */   extends AbstractTokenMaker
/*      */ {
/*      */   private static final String OPERATORS = "=|><&";
/*      */   private static final String SEPARATORS = "()[]";
/*      */   private static final String SEPARATORS2 = ".,;";
/*      */   private static final String shellVariables = "#-?$!*@_";
/*      */   private int currentTokenStart;
/*      */   private int currentTokenType;
/*      */   
/*      */   public void addToken(Segment segment, int start, int end, int tokenType, int startOffset) {
/*      */     int value;
/*   60 */     switch (tokenType) {
/*      */ 
/*      */       
/*      */       case 20:
/*   64 */         value = this.wordsToHighlight.get(segment, start, end);
/*   65 */         if (value != -1) {
/*   66 */           tokenType = value;
/*      */         }
/*      */         break;
/*      */       case 1:
/*      */       case 10:
/*      */       case 13:
/*      */       case 14:
/*      */       case 15:
/*      */       case 17:
/*      */       case 21:
/*      */       case 22:
/*      */       case 23:
/*      */       case 24:
/*      */         break;
/*      */       default:
/*   81 */         tokenType = 20;
/*      */         break;
/*      */     } 
/*      */ 
/*      */     
/*   86 */     super.addToken(segment, start, end, tokenType, startOffset);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] getLineCommentStartAndEnd(int languageIndex) {
/*   96 */     return new String[] { "#", null };
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
/*      */   public boolean getMarkOccurrencesOfTokenType(int type) {
/*  110 */     return (type == 20 || type == 17);
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
/*      */   public TokenMap getWordsToHighlight() {
/*  124 */     TokenMap tokenMap = new TokenMap();
/*      */     
/*  126 */     int reservedWord = 6;
/*  127 */     tokenMap.put("case", reservedWord);
/*  128 */     tokenMap.put("do", reservedWord);
/*  129 */     tokenMap.put("done", reservedWord);
/*  130 */     tokenMap.put("elif", reservedWord);
/*  131 */     tokenMap.put("else", reservedWord);
/*  132 */     tokenMap.put("esac", reservedWord);
/*  133 */     tokenMap.put("fi", reservedWord);
/*  134 */     tokenMap.put("for", reservedWord);
/*  135 */     tokenMap.put("if", reservedWord);
/*  136 */     tokenMap.put("in", reservedWord);
/*  137 */     tokenMap.put("select", reservedWord);
/*  138 */     tokenMap.put("then", reservedWord);
/*  139 */     tokenMap.put("until", reservedWord);
/*  140 */     tokenMap.put("while", reservedWord);
/*      */     
/*  142 */     int function = 8;
/*  143 */     tokenMap.put("addbib", function);
/*  144 */     tokenMap.put("admin", function);
/*  145 */     tokenMap.put("alias", function);
/*  146 */     tokenMap.put("apropos", function);
/*  147 */     tokenMap.put("ar", function);
/*  148 */     tokenMap.put("at", function);
/*  149 */     tokenMap.put("awk", function);
/*  150 */     tokenMap.put("banner", function);
/*  151 */     tokenMap.put("basename", function);
/*  152 */     tokenMap.put("batch", function);
/*  153 */     tokenMap.put("bg", function);
/*  154 */     tokenMap.put("biff", function);
/*  155 */     tokenMap.put("bin-mail", function);
/*  156 */     tokenMap.put("binmail", function);
/*  157 */     tokenMap.put("break", function);
/*  158 */     tokenMap.put("cal", function);
/*  159 */     tokenMap.put("calendar", function);
/*  160 */     tokenMap.put("cancel", function);
/*  161 */     tokenMap.put("cat", function);
/*  162 */     tokenMap.put("cb", function);
/*  163 */     tokenMap.put("cc", function);
/*  164 */     tokenMap.put("cd", function);
/*  165 */     tokenMap.put("cdc", function);
/*  166 */     tokenMap.put("chdir", function);
/*  167 */     tokenMap.put("checkeq", function);
/*  168 */     tokenMap.put("checknr", function);
/*  169 */     tokenMap.put("chfn", function);
/*  170 */     tokenMap.put("chgrp", function);
/*  171 */     tokenMap.put("chmod", function);
/*  172 */     tokenMap.put("chown", function);
/*  173 */     tokenMap.put("chsh", function);
/*  174 */     tokenMap.put("clear", function);
/*  175 */     tokenMap.put("cmp", function);
/*  176 */     tokenMap.put("colcrt", function);
/*  177 */     tokenMap.put("comb", function);
/*  178 */     tokenMap.put("comm", function);
/*  179 */     tokenMap.put("command", function);
/*  180 */     tokenMap.put("compress", function);
/*  181 */     tokenMap.put("continue", function);
/*  182 */     tokenMap.put("cp", function);
/*  183 */     tokenMap.put("cpio", function);
/*  184 */     tokenMap.put("cpp", function);
/*  185 */     tokenMap.put("crontab", function);
/*  186 */     tokenMap.put("csh", function);
/*  187 */     tokenMap.put("ctags", function);
/*  188 */     tokenMap.put("curl", function);
/*  189 */     tokenMap.put("cut", function);
/*  190 */     tokenMap.put("cvs", function);
/*  191 */     tokenMap.put("date", function);
/*  192 */     tokenMap.put("dbx", function);
/*  193 */     tokenMap.put("delta", function);
/*  194 */     tokenMap.put("deroff", function);
/*  195 */     tokenMap.put("df", function);
/*  196 */     tokenMap.put("diff", function);
/*  197 */     tokenMap.put("dtree", function);
/*  198 */     tokenMap.put("du", function);
/*  199 */     tokenMap.put("e", function);
/*  200 */     tokenMap.put("echo", function);
/*  201 */     tokenMap.put("ed", function);
/*  202 */     tokenMap.put("edit", function);
/*  203 */     tokenMap.put("enscript", function);
/*  204 */     tokenMap.put("eqn", function);
/*  205 */     tokenMap.put("error", function);
/*  206 */     tokenMap.put("eval", function);
/*  207 */     tokenMap.put("ex", function);
/*  208 */     tokenMap.put("exec", function);
/*  209 */     tokenMap.put("exit", function);
/*  210 */     tokenMap.put("expand", function);
/*  211 */     tokenMap.put("export", function);
/*  212 */     tokenMap.put("expr", function);
/*  213 */     tokenMap.put("false", function);
/*  214 */     tokenMap.put("fc", function);
/*  215 */     tokenMap.put("fg", function);
/*  216 */     tokenMap.put("file", function);
/*  217 */     tokenMap.put("find", function);
/*  218 */     tokenMap.put("finger", function);
/*  219 */     tokenMap.put("fmt", function);
/*  220 */     tokenMap.put("fmt_mail", function);
/*  221 */     tokenMap.put("fold", function);
/*  222 */     tokenMap.put("ftp", function);
/*  223 */     tokenMap.put("function", function);
/*  224 */     tokenMap.put("gcore", function);
/*  225 */     tokenMap.put("get", function);
/*  226 */     tokenMap.put("getopts", function);
/*  227 */     tokenMap.put("gprof", function);
/*  228 */     tokenMap.put("grep", function);
/*  229 */     tokenMap.put("groups", function);
/*  230 */     tokenMap.put("gunzip", function);
/*  231 */     tokenMap.put("gzip", function);
/*  232 */     tokenMap.put("hashcheck", function);
/*  233 */     tokenMap.put("hashmake", function);
/*  234 */     tokenMap.put("head", function);
/*  235 */     tokenMap.put("help", function);
/*  236 */     tokenMap.put("history", function);
/*  237 */     tokenMap.put("imake", function);
/*  238 */     tokenMap.put("indent", function);
/*  239 */     tokenMap.put("install", function);
/*  240 */     tokenMap.put("jobs", function);
/*  241 */     tokenMap.put("join", function);
/*  242 */     tokenMap.put("kill", function);
/*  243 */     tokenMap.put("last", function);
/*  244 */     tokenMap.put("ld", function);
/*  245 */     tokenMap.put("leave", function);
/*  246 */     tokenMap.put("less", function);
/*  247 */     tokenMap.put("let", function);
/*  248 */     tokenMap.put("lex", function);
/*  249 */     tokenMap.put("lint", function);
/*  250 */     tokenMap.put("ln", function);
/*  251 */     tokenMap.put("login", function);
/*  252 */     tokenMap.put("look", function);
/*  253 */     tokenMap.put("lookbib", function);
/*  254 */     tokenMap.put("lorder", function);
/*  255 */     tokenMap.put("lp", function);
/*  256 */     tokenMap.put("lpq", function);
/*  257 */     tokenMap.put("lpr", function);
/*  258 */     tokenMap.put("lprm", function);
/*  259 */     tokenMap.put("ls", function);
/*  260 */     tokenMap.put("mail", function);
/*  261 */     tokenMap.put("Mail", function);
/*  262 */     tokenMap.put("make", function);
/*  263 */     tokenMap.put("man", function);
/*  264 */     tokenMap.put("md", function);
/*  265 */     tokenMap.put("mesg", function);
/*  266 */     tokenMap.put("mkdir", function);
/*  267 */     tokenMap.put("mkstr", function);
/*  268 */     tokenMap.put("more", function);
/*  269 */     tokenMap.put("mount", function);
/*  270 */     tokenMap.put("mv", function);
/*  271 */     tokenMap.put("nawk", function);
/*  272 */     tokenMap.put("neqn", function);
/*  273 */     tokenMap.put("nice", function);
/*  274 */     tokenMap.put("nm", function);
/*  275 */     tokenMap.put("nroff", function);
/*  276 */     tokenMap.put("od", function);
/*  277 */     tokenMap.put("page", function);
/*  278 */     tokenMap.put("passwd", function);
/*  279 */     tokenMap.put("paste", function);
/*  280 */     tokenMap.put("pr", function);
/*  281 */     tokenMap.put("print", function);
/*  282 */     tokenMap.put("printf", function);
/*  283 */     tokenMap.put("printenv", function);
/*  284 */     tokenMap.put("prof", function);
/*  285 */     tokenMap.put("prs", function);
/*  286 */     tokenMap.put("prt", function);
/*  287 */     tokenMap.put("ps", function);
/*  288 */     tokenMap.put("ptx", function);
/*  289 */     tokenMap.put("pwd", function);
/*  290 */     tokenMap.put("quota", function);
/*  291 */     tokenMap.put("ranlib", function);
/*  292 */     tokenMap.put("rcp", function);
/*  293 */     tokenMap.put("rcs", function);
/*  294 */     tokenMap.put("rcsdiff", function);
/*  295 */     tokenMap.put("read", function);
/*  296 */     tokenMap.put("readonly", function);
/*  297 */     tokenMap.put("red", function);
/*  298 */     tokenMap.put("return", function);
/*  299 */     tokenMap.put("rev", function);
/*  300 */     tokenMap.put("rlogin", function);
/*  301 */     tokenMap.put("rm", function);
/*  302 */     tokenMap.put("rmdel", function);
/*  303 */     tokenMap.put("rmdir", function);
/*  304 */     tokenMap.put("roffbib", function);
/*  305 */     tokenMap.put("rsh", function);
/*  306 */     tokenMap.put("rup", function);
/*  307 */     tokenMap.put("ruptime", function);
/*  308 */     tokenMap.put("rusers", function);
/*  309 */     tokenMap.put("rwall", function);
/*  310 */     tokenMap.put("rwho", function);
/*  311 */     tokenMap.put("sact", function);
/*  312 */     tokenMap.put("sccs", function);
/*  313 */     tokenMap.put("sccsdiff", function);
/*  314 */     tokenMap.put("script", function);
/*  315 */     tokenMap.put("sed", function);
/*  316 */     tokenMap.put("set", function);
/*  317 */     tokenMap.put("setgroups", function);
/*  318 */     tokenMap.put("setsenv", function);
/*  319 */     tokenMap.put("sh", function);
/*  320 */     tokenMap.put("shift", function);
/*  321 */     tokenMap.put("size", function);
/*  322 */     tokenMap.put("sleep", function);
/*  323 */     tokenMap.put("sort", function);
/*  324 */     tokenMap.put("sortbib", function);
/*  325 */     tokenMap.put("spell", function);
/*  326 */     tokenMap.put("split", function);
/*  327 */     tokenMap.put("ssh", function);
/*  328 */     tokenMap.put("strings", function);
/*  329 */     tokenMap.put("strip", function);
/*  330 */     tokenMap.put("stty", function);
/*  331 */     tokenMap.put("su", function);
/*  332 */     tokenMap.put("sudo", function);
/*  333 */     tokenMap.put("symorder", function);
/*  334 */     tokenMap.put("tabs", function);
/*  335 */     tokenMap.put("tail", function);
/*  336 */     tokenMap.put("talk", function);
/*  337 */     tokenMap.put("tar", function);
/*  338 */     tokenMap.put("tbl", function);
/*  339 */     tokenMap.put("tee", function);
/*  340 */     tokenMap.put("telnet", function);
/*  341 */     tokenMap.put("test", function);
/*  342 */     tokenMap.put("tftp", function);
/*  343 */     tokenMap.put("time", function);
/*  344 */     tokenMap.put("times", function);
/*  345 */     tokenMap.put("touch", function);
/*  346 */     tokenMap.put("trap", function);
/*  347 */     tokenMap.put("troff", function);
/*  348 */     tokenMap.put("true", function);
/*  349 */     tokenMap.put("tsort", function);
/*  350 */     tokenMap.put("tty", function);
/*  351 */     tokenMap.put("type", function);
/*  352 */     tokenMap.put("typeset", function);
/*  353 */     tokenMap.put("ue", function);
/*  354 */     tokenMap.put("ul", function);
/*  355 */     tokenMap.put("ulimit", function);
/*  356 */     tokenMap.put("umask", function);
/*  357 */     tokenMap.put("unalias", function);
/*  358 */     tokenMap.put("uncompress", function);
/*  359 */     tokenMap.put("unexpand", function);
/*  360 */     tokenMap.put("unget", function);
/*  361 */     tokenMap.put("unifdef", function);
/*  362 */     tokenMap.put("uniq", function);
/*  363 */     tokenMap.put("units", function);
/*  364 */     tokenMap.put("unset", function);
/*  365 */     tokenMap.put("uptime", function);
/*  366 */     tokenMap.put("users", function);
/*  367 */     tokenMap.put("uucp", function);
/*  368 */     tokenMap.put("uudecode", function);
/*  369 */     tokenMap.put("uuencode", function);
/*  370 */     tokenMap.put("uulog", function);
/*  371 */     tokenMap.put("uuname", function);
/*  372 */     tokenMap.put("uusend", function);
/*  373 */     tokenMap.put("uux", function);
/*  374 */     tokenMap.put("vacation", function);
/*  375 */     tokenMap.put("val", function);
/*  376 */     tokenMap.put("vedit", function);
/*  377 */     tokenMap.put("vgrind", function);
/*  378 */     tokenMap.put("vi", function);
/*  379 */     tokenMap.put("view", function);
/*  380 */     tokenMap.put("vtroff", function);
/*  381 */     tokenMap.put("w", function);
/*  382 */     tokenMap.put("wait", function);
/*  383 */     tokenMap.put("wall", function);
/*  384 */     tokenMap.put("wc", function);
/*  385 */     tokenMap.put("wait", function);
/*  386 */     tokenMap.put("what", function);
/*  387 */     tokenMap.put("whatis", function);
/*  388 */     tokenMap.put("whence", function);
/*  389 */     tokenMap.put("whereis", function);
/*  390 */     tokenMap.put("which", function);
/*  391 */     tokenMap.put("who", function);
/*  392 */     tokenMap.put("whoami", function);
/*  393 */     tokenMap.put("write", function);
/*  394 */     tokenMap.put("xargs", function);
/*  395 */     tokenMap.put("xstr", function);
/*  396 */     tokenMap.put("yacc", function);
/*  397 */     tokenMap.put("yes", function);
/*  398 */     tokenMap.put("zcat", function);
/*      */     
/*  400 */     return tokenMap;
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
/*      */   public Token getTokenList(Segment text, int startTokenType, int startOffset) {
/*  416 */     resetTokenList();
/*      */     
/*  418 */     char[] array = text.array;
/*  419 */     int offset = text.offset;
/*  420 */     int count = text.count;
/*  421 */     int end = offset + count;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  427 */     int newStartOffset = startOffset - offset;
/*      */     
/*  429 */     this.currentTokenStart = offset;
/*  430 */     this.currentTokenType = startTokenType;
/*  431 */     boolean backslash = false;
/*      */ 
/*      */     
/*  434 */     for (int i = offset; i < end; i++) {
/*      */       int indexOf;
/*  436 */       char c = array[i];
/*      */       
/*  438 */       switch (this.currentTokenType) {
/*      */ 
/*      */         
/*      */         case 0:
/*  442 */           this.currentTokenStart = i;
/*      */           
/*  444 */           switch (c) {
/*      */             
/*      */             case '\t':
/*      */             case ' ':
/*  448 */               this.currentTokenType = 21;
/*      */               break;
/*      */             
/*      */             case '`':
/*  452 */               if (backslash) {
/*  453 */                 addToken(text, this.currentTokenStart, i, 20, newStartOffset + this.currentTokenStart);
/*  454 */                 backslash = false;
/*      */                 break;
/*      */               } 
/*  457 */               this.currentTokenType = 15;
/*      */               break;
/*      */ 
/*      */             
/*      */             case '"':
/*  462 */               if (backslash) {
/*  463 */                 addToken(text, this.currentTokenStart, i, 20, newStartOffset + this.currentTokenStart);
/*  464 */                 backslash = false;
/*      */                 break;
/*      */               } 
/*  467 */               this.currentTokenType = 13;
/*      */               break;
/*      */ 
/*      */             
/*      */             case '\'':
/*  472 */               if (backslash) {
/*  473 */                 addToken(text, this.currentTokenStart, i, 20, newStartOffset + this.currentTokenStart);
/*  474 */                 backslash = false;
/*      */                 break;
/*      */               } 
/*  477 */               this.currentTokenType = 14;
/*      */               break;
/*      */ 
/*      */             
/*      */             case '\\':
/*  482 */               addToken(text, this.currentTokenStart, i, 20, newStartOffset + this.currentTokenStart);
/*  483 */               this.currentTokenType = 0;
/*  484 */               backslash = !backslash;
/*      */               break;
/*      */             
/*      */             case '$':
/*  488 */               if (backslash) {
/*  489 */                 addToken(text, this.currentTokenStart, i, 20, newStartOffset + this.currentTokenStart);
/*  490 */                 backslash = false;
/*      */                 break;
/*      */               } 
/*  493 */               this.currentTokenType = 17;
/*      */               break;
/*      */ 
/*      */             
/*      */             case '#':
/*  498 */               backslash = false;
/*  499 */               this.currentTokenType = 1;
/*      */               break;
/*      */           } 
/*      */           
/*  503 */           if (RSyntaxUtilities.isDigit(c)) {
/*  504 */             this.currentTokenType = 10;
/*      */             break;
/*      */           } 
/*  507 */           if (RSyntaxUtilities.isLetter(c) || c == '/' || c == '_') {
/*  508 */             this.currentTokenType = 20;
/*      */             break;
/*      */           } 
/*  511 */           indexOf = "=|><&".indexOf(c, 0);
/*  512 */           if (indexOf > -1) {
/*  513 */             addToken(text, this.currentTokenStart, i, 23, newStartOffset + this.currentTokenStart);
/*  514 */             this.currentTokenType = 0;
/*      */             break;
/*      */           } 
/*  517 */           indexOf = "()[]".indexOf(c, 0);
/*  518 */           if (indexOf > -1) {
/*  519 */             addToken(text, this.currentTokenStart, i, 22, newStartOffset + this.currentTokenStart);
/*  520 */             this.currentTokenType = 0;
/*      */             break;
/*      */           } 
/*  523 */           indexOf = ".,;".indexOf(c, 0);
/*  524 */           if (indexOf > -1) {
/*  525 */             addToken(text, this.currentTokenStart, i, 20, newStartOffset + this.currentTokenStart);
/*  526 */             this.currentTokenType = 0;
/*      */             
/*      */             break;
/*      */           } 
/*  530 */           this.currentTokenType = 20;
/*      */           break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case 21:
/*  540 */           switch (c) {
/*      */             case '\t':
/*      */             case ' ':
/*      */               break;
/*      */ 
/*      */             
/*      */             case '\\':
/*  547 */               addToken(text, this.currentTokenStart, i - 1, 21, newStartOffset + this.currentTokenStart);
/*  548 */               addToken(text, i, i, 20, newStartOffset + i);
/*  549 */               this.currentTokenType = 0;
/*  550 */               backslash = true;
/*      */               break;
/*      */             
/*      */             case '`':
/*  554 */               addToken(text, this.currentTokenStart, i - 1, 21, newStartOffset + this.currentTokenStart);
/*  555 */               this.currentTokenStart = i;
/*  556 */               this.currentTokenType = 15;
/*  557 */               backslash = false;
/*      */               break;
/*      */             
/*      */             case '"':
/*  561 */               addToken(text, this.currentTokenStart, i - 1, 21, newStartOffset + this.currentTokenStart);
/*  562 */               this.currentTokenStart = i;
/*  563 */               this.currentTokenType = 13;
/*  564 */               backslash = false;
/*      */               break;
/*      */             
/*      */             case '\'':
/*  568 */               addToken(text, this.currentTokenStart, i - 1, 21, newStartOffset + this.currentTokenStart);
/*  569 */               this.currentTokenStart = i;
/*  570 */               this.currentTokenType = 14;
/*  571 */               backslash = false;
/*      */               break;
/*      */             
/*      */             case '$':
/*  575 */               addToken(text, this.currentTokenStart, i - 1, 21, newStartOffset + this.currentTokenStart);
/*  576 */               this.currentTokenStart = i;
/*  577 */               this.currentTokenType = 17;
/*  578 */               backslash = false;
/*      */               break;
/*      */             
/*      */             case '#':
/*  582 */               addToken(text, this.currentTokenStart, i - 1, 21, newStartOffset + this.currentTokenStart);
/*  583 */               this.currentTokenStart = i;
/*  584 */               this.currentTokenType = 1;
/*      */               break;
/*      */           } 
/*      */ 
/*      */           
/*  589 */           addToken(text, this.currentTokenStart, i - 1, 21, newStartOffset + this.currentTokenStart);
/*  590 */           this.currentTokenStart = i;
/*      */           
/*  592 */           if (RSyntaxUtilities.isDigit(c)) {
/*  593 */             this.currentTokenType = 10;
/*      */             break;
/*      */           } 
/*  596 */           if (RSyntaxUtilities.isLetter(c) || c == '/' || c == '_') {
/*  597 */             this.currentTokenType = 20;
/*      */             break;
/*      */           } 
/*  600 */           indexOf = "=|><&".indexOf(c, 0);
/*  601 */           if (indexOf > -1) {
/*  602 */             addToken(text, i, i, 23, newStartOffset + i);
/*  603 */             this.currentTokenType = 0;
/*      */             break;
/*      */           } 
/*  606 */           indexOf = "()[]".indexOf(c, 0);
/*  607 */           if (indexOf > -1) {
/*  608 */             addToken(text, i, i, 22, newStartOffset + i);
/*  609 */             this.currentTokenType = 0;
/*      */             break;
/*      */           } 
/*  612 */           indexOf = ".,;".indexOf(c, 0);
/*  613 */           if (indexOf > -1) {
/*  614 */             addToken(text, i, i, 20, newStartOffset + i);
/*  615 */             this.currentTokenType = 0;
/*      */             
/*      */             break;
/*      */           } 
/*  619 */           this.currentTokenType = 20;
/*      */           break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         default:
/*  629 */           switch (c) {
/*      */             
/*      */             case '\t':
/*      */             case ' ':
/*  633 */               addToken(text, this.currentTokenStart, i - 1, 20, newStartOffset + this.currentTokenStart);
/*  634 */               this.currentTokenStart = i;
/*  635 */               this.currentTokenType = 21;
/*      */               break;
/*      */             
/*      */             case '/':
/*  639 */               addToken(text, this.currentTokenStart, i, 20, newStartOffset + this.currentTokenStart);
/*  640 */               this.currentTokenStart = i + 1;
/*  641 */               this.currentTokenType = 0;
/*      */               break;
/*      */             
/*      */             case '`':
/*  645 */               addToken(text, this.currentTokenStart, i - 1, 20, newStartOffset + this.currentTokenStart);
/*  646 */               this.currentTokenStart = i;
/*  647 */               this.currentTokenType = 15;
/*  648 */               backslash = false;
/*      */               break;
/*      */             
/*      */             case '"':
/*  652 */               addToken(text, this.currentTokenStart, i - 1, 20, newStartOffset + this.currentTokenStart);
/*  653 */               this.currentTokenStart = i;
/*  654 */               this.currentTokenType = 13;
/*  655 */               backslash = false;
/*      */               break;
/*      */             
/*      */             case '\'':
/*  659 */               addToken(text, this.currentTokenStart, i - 1, 20, newStartOffset + this.currentTokenStart);
/*  660 */               this.currentTokenStart = i;
/*  661 */               this.currentTokenType = 14;
/*  662 */               backslash = false;
/*      */               break;
/*      */             
/*      */             case '\\':
/*  666 */               addToken(text, this.currentTokenStart, i - 1, 20, newStartOffset + this.currentTokenStart);
/*  667 */               addToken(text, i, i, 20, newStartOffset + i);
/*  668 */               this.currentTokenType = 0;
/*  669 */               backslash = true;
/*      */               break;
/*      */             
/*      */             case '$':
/*  673 */               addToken(text, this.currentTokenStart, i - 1, 20, newStartOffset + this.currentTokenStart);
/*  674 */               this.currentTokenStart = i;
/*  675 */               this.currentTokenType = 17;
/*  676 */               backslash = false;
/*      */               break;
/*      */             
/*      */             case '=':
/*  680 */               addToken(text, this.currentTokenStart, i - 1, 17, newStartOffset + this.currentTokenStart);
/*  681 */               addToken(text, i, i, 23, newStartOffset + i);
/*  682 */               this.currentTokenType = 0;
/*      */               break;
/*      */           } 
/*      */           
/*  686 */           if (RSyntaxUtilities.isLetterOrDigit(c) || c == '/' || c == '_') {
/*      */             break;
/*      */           }
/*  689 */           indexOf = "=|><&".indexOf(c);
/*  690 */           if (indexOf > -1) {
/*  691 */             addToken(text, this.currentTokenStart, i - 1, 20, newStartOffset + this.currentTokenStart);
/*  692 */             addToken(text, i, i, 23, newStartOffset + i);
/*  693 */             this.currentTokenType = 0;
/*      */             break;
/*      */           } 
/*  696 */           indexOf = "()[]".indexOf(c, 0);
/*  697 */           if (indexOf > -1) {
/*  698 */             addToken(text, this.currentTokenStart, i - 1, 20, newStartOffset + this.currentTokenStart);
/*  699 */             addToken(text, i, i, 22, newStartOffset + i);
/*  700 */             this.currentTokenType = 0;
/*      */             break;
/*      */           } 
/*  703 */           indexOf = ".,;".indexOf(c, 0);
/*  704 */           if (indexOf > -1) {
/*  705 */             addToken(text, this.currentTokenStart, i - 1, 20, newStartOffset + this.currentTokenStart);
/*  706 */             addToken(text, i, i, 20, newStartOffset + i);
/*  707 */             this.currentTokenType = 0;
/*      */           } 
/*      */           break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case 10:
/*  718 */           switch (c) {
/*      */             
/*      */             case '\t':
/*      */             case ' ':
/*  722 */               addToken(text, this.currentTokenStart, i - 1, 10, newStartOffset + this.currentTokenStart);
/*  723 */               this.currentTokenStart = i;
/*  724 */               this.currentTokenType = 21;
/*      */               break;
/*      */             
/*      */             case '`':
/*  728 */               addToken(text, this.currentTokenStart, i - 1, 10, newStartOffset + this.currentTokenStart);
/*  729 */               this.currentTokenStart = i;
/*  730 */               this.currentTokenType = 15;
/*  731 */               backslash = false;
/*      */               break;
/*      */             
/*      */             case '"':
/*  735 */               addToken(text, this.currentTokenStart, i - 1, 10, newStartOffset + this.currentTokenStart);
/*  736 */               this.currentTokenStart = i;
/*  737 */               this.currentTokenType = 13;
/*  738 */               backslash = false;
/*      */               break;
/*      */             
/*      */             case '\'':
/*  742 */               addToken(text, this.currentTokenStart, i - 1, 10, newStartOffset + this.currentTokenStart);
/*  743 */               this.currentTokenStart = i;
/*  744 */               this.currentTokenType = 14;
/*  745 */               backslash = false;
/*      */               break;
/*      */             
/*      */             case '$':
/*  749 */               addToken(text, this.currentTokenStart, i - 1, 10, newStartOffset + this.currentTokenStart);
/*  750 */               this.currentTokenStart = i;
/*  751 */               this.currentTokenType = 17;
/*  752 */               backslash = false;
/*      */               break;
/*      */             
/*      */             case '\\':
/*  756 */               addToken(text, this.currentTokenStart, i - 1, 10, newStartOffset + this.currentTokenStart);
/*  757 */               addToken(text, i, i, 20, newStartOffset + i);
/*  758 */               this.currentTokenType = 0;
/*  759 */               backslash = true;
/*      */               break;
/*      */           } 
/*      */ 
/*      */           
/*  764 */           if (RSyntaxUtilities.isDigit(c)) {
/*      */             break;
/*      */           }
/*  767 */           indexOf = "=|><&".indexOf(c);
/*  768 */           if (indexOf > -1) {
/*  769 */             addToken(text, this.currentTokenStart, i - 1, 10, newStartOffset + this.currentTokenStart);
/*  770 */             addToken(text, i, i, 23, newStartOffset + i);
/*  771 */             this.currentTokenType = 0;
/*      */             break;
/*      */           } 
/*  774 */           indexOf = "()[]".indexOf(c);
/*  775 */           if (indexOf > -1) {
/*  776 */             addToken(text, this.currentTokenStart, i - 1, 10, newStartOffset + this.currentTokenStart);
/*  777 */             addToken(text, i, i, 22, newStartOffset + i);
/*  778 */             this.currentTokenType = 0;
/*      */             break;
/*      */           } 
/*  781 */           indexOf = ".,;".indexOf(c);
/*  782 */           if (indexOf > -1) {
/*  783 */             addToken(text, this.currentTokenStart, i - 1, 10, newStartOffset + this.currentTokenStart);
/*  784 */             addToken(text, i, i, 20, newStartOffset + i);
/*  785 */             this.currentTokenType = 0;
/*      */             
/*      */             break;
/*      */           } 
/*      */           
/*  790 */           addToken(text, this.currentTokenStart, i - 1, 10, newStartOffset + this.currentTokenStart);
/*  791 */           i--;
/*  792 */           this.currentTokenType = 0;
/*      */           break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case 17:
/*  802 */           if (c == '{') {
/*  803 */             while (++i < end) {
/*  804 */               if (array[i] == '}') {
/*  805 */                 addToken(text, this.currentTokenStart, i, 17, newStartOffset + this.currentTokenStart);
/*  806 */                 this.currentTokenType = 0;
/*      */                 break;
/*      */               } 
/*      */             } 
/*  810 */             if (i == end) {
/*  811 */               addToken(text, this.currentTokenStart, end - 1, 17, newStartOffset + this.currentTokenStart);
/*  812 */               this.currentTokenType = 0;
/*      */             } 
/*      */             
/*      */             break;
/*      */           } 
/*      */           
/*  818 */           while (i < end) {
/*  819 */             c = array[i];
/*  820 */             if (!RSyntaxUtilities.isLetterOrDigit(c) && "#-?$!*@_".indexOf(c) == -1 && c != '_') {
/*  821 */               addToken(text, this.currentTokenStart, i - 1, 17, newStartOffset + this.currentTokenStart);
/*  822 */               i--;
/*  823 */               this.currentTokenType = 0;
/*      */               break;
/*      */             } 
/*  826 */             i++;
/*      */           } 
/*      */ 
/*      */           
/*  830 */           if (i == end) {
/*  831 */             addToken(text, this.currentTokenStart, i - 1, 17, newStartOffset + this.currentTokenStart);
/*  832 */             this.currentTokenType = 0;
/*      */           } 
/*      */           break;
/*      */ 
/*      */ 
/*      */         
/*      */         case 1:
/*  839 */           if (c == '!')
/*  840 */             this.currentTokenType = 24; 
/*  841 */           i = end - 1;
/*  842 */           addToken(text, this.currentTokenStart, i, this.currentTokenType, newStartOffset + this.currentTokenStart);
/*      */           
/*  844 */           this.currentTokenType = 0;
/*      */           break;
/*      */ 
/*      */ 
/*      */         
/*      */         case 14:
/*  850 */           if (c == '\\') {
/*  851 */             backslash = !backslash;
/*      */             break;
/*      */           } 
/*  854 */           if (c == '\'' && !backslash) {
/*  855 */             addToken(text, this.currentTokenStart, i, 14, newStartOffset + this.currentTokenStart);
/*  856 */             this.currentTokenStart = i + 1;
/*  857 */             this.currentTokenType = 0;
/*      */           } 
/*      */ 
/*      */           
/*  861 */           backslash = false;
/*      */           break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case 15:
/*  870 */           switch (c) {
/*      */             
/*      */             case '\\':
/*  873 */               backslash = !backslash;
/*      */               break;
/*      */             
/*      */             case '`':
/*  877 */               if (!backslash) {
/*  878 */                 addToken(text, this.currentTokenStart, i, 15, newStartOffset + this.currentTokenStart);
/*  879 */                 this.currentTokenType = 0;
/*      */                 
/*      */                 break;
/*      */               } 
/*  883 */               backslash = false;
/*      */               break;
/*      */ 
/*      */ 
/*      */             
/*      */             case '$':
/*  889 */               if (backslash == true) {
/*  890 */                 backslash = false;
/*      */                 
/*      */                 break;
/*      */               } 
/*      */               
/*  895 */               addToken(text, this.currentTokenStart, i - 1, 15, newStartOffset + this.currentTokenStart);
/*  896 */               this.currentTokenType = 17;
/*  897 */               this.currentTokenStart = i;
/*      */ 
/*      */               
/*  900 */               if (i < end - 1 && array[i + 1] == '{') {
/*  901 */                 i++;
/*  902 */                 while (++i < end) {
/*  903 */                   if (array[i] == '}') {
/*  904 */                     addToken(text, this.currentTokenStart, i, 17, newStartOffset + this.currentTokenStart);
/*  905 */                     i++;
/*  906 */                     if (i < end) {
/*  907 */                       c = array[i];
/*  908 */                       if (c == '`') {
/*  909 */                         addToken(text, i, i, 15, newStartOffset + i);
/*  910 */                         this.currentTokenType = 0;
/*      */                         
/*      */                         break;
/*      */                       } 
/*  914 */                       this.currentTokenStart = i;
/*  915 */                       this.currentTokenType = 15;
/*  916 */                       i--;
/*      */                       
/*      */                       break;
/*      */                     } 
/*      */                     
/*  921 */                     this.currentTokenStart = i;
/*  922 */                     this.currentTokenType = 15;
/*      */                     
/*      */                     break;
/*      */                   } 
/*      */                 } 
/*  927 */                 if (i == end) {
/*  928 */                   addToken(text, this.currentTokenStart, end - 1, 17, newStartOffset + this.currentTokenStart);
/*  929 */                   this.currentTokenStart = end;
/*  930 */                   this.currentTokenType = 15;
/*      */                   
/*      */                   break;
/*      */                 } 
/*      */               } 
/*      */               
/*  936 */               if (this.currentTokenType == 0 || this.currentTokenType == 15) {
/*      */                 break;
/*      */               }
/*      */ 
/*      */               
/*  941 */               while (++i < end) {
/*  942 */                 c = array[i];
/*  943 */                 if (!RSyntaxUtilities.isLetterOrDigit(c) && "#-?$!*@_".indexOf(c) == -1 && c != '_') {
/*  944 */                   addToken(text, this.currentTokenStart, i - 1, 17, newStartOffset + this.currentTokenStart);
/*  945 */                   if (c == '`') {
/*  946 */                     addToken(text, i, i, 15, newStartOffset + i);
/*  947 */                     this.currentTokenType = 0;
/*      */                     
/*      */                     break;
/*      */                   } 
/*  951 */                   this.currentTokenStart = i;
/*  952 */                   this.currentTokenType = 15;
/*  953 */                   i--;
/*      */ 
/*      */                   
/*      */                   break;
/*      */                 } 
/*      */               } 
/*      */ 
/*      */               
/*  961 */               if (i == end) {
/*  962 */                 addToken(text, this.currentTokenStart, i - 1, 17, newStartOffset + this.currentTokenStart);
/*  963 */                 this.currentTokenStart = i;
/*  964 */                 this.currentTokenType = 15;
/*      */               } 
/*      */               break;
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/*  971 */           backslash = false;
/*      */           break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case 13:
/*  979 */           switch (c) {
/*      */             
/*      */             case '\\':
/*  982 */               backslash = !backslash;
/*      */               break;
/*      */             
/*      */             case '"':
/*  986 */               if (!backslash) {
/*  987 */                 addToken(text, this.currentTokenStart, i, 13, newStartOffset + this.currentTokenStart);
/*  988 */                 this.currentTokenType = 0;
/*      */                 
/*      */                 break;
/*      */               } 
/*  992 */               backslash = false;
/*      */               break;
/*      */ 
/*      */ 
/*      */             
/*      */             case '$':
/*  998 */               if (backslash == true) {
/*  999 */                 backslash = false;
/*      */                 
/*      */                 break;
/*      */               } 
/*      */               
/* 1004 */               addToken(text, this.currentTokenStart, i - 1, 13, newStartOffset + this.currentTokenStart);
/* 1005 */               this.currentTokenType = 17;
/* 1006 */               this.currentTokenStart = i;
/*      */ 
/*      */               
/* 1009 */               if (i < end - 1 && array[i + 1] == '{') {
/* 1010 */                 i++;
/* 1011 */                 while (++i < end) {
/* 1012 */                   if (array[i] == '}') {
/* 1013 */                     addToken(text, this.currentTokenStart, i, 17, newStartOffset + this.currentTokenStart);
/* 1014 */                     i++;
/* 1015 */                     if (i < end) {
/* 1016 */                       c = array[i];
/* 1017 */                       if (c == '"') {
/* 1018 */                         addToken(text, i, i, 13, newStartOffset + i);
/* 1019 */                         this.currentTokenType = 0;
/*      */                         
/*      */                         break;
/*      */                       } 
/* 1023 */                       this.currentTokenStart = i;
/* 1024 */                       this.currentTokenType = 13;
/* 1025 */                       i--;
/*      */                       
/*      */                       break;
/*      */                     } 
/*      */                     
/* 1030 */                     this.currentTokenStart = i;
/* 1031 */                     this.currentTokenType = 13;
/*      */                     
/*      */                     break;
/*      */                   } 
/*      */                 } 
/* 1036 */                 if (i == end) {
/* 1037 */                   addToken(text, this.currentTokenStart, end - 1, 17, newStartOffset + this.currentTokenStart);
/* 1038 */                   this.currentTokenStart = end;
/* 1039 */                   this.currentTokenType = 13;
/*      */                   
/*      */                   break;
/*      */                 } 
/*      */               } 
/*      */               
/* 1045 */               if (this.currentTokenType == 0 || this.currentTokenType == 13) {
/*      */                 break;
/*      */               }
/*      */ 
/*      */               
/* 1050 */               while (++i < end) {
/* 1051 */                 c = array[i];
/* 1052 */                 if (!RSyntaxUtilities.isLetterOrDigit(c) && "#-?$!*@_".indexOf(c) == -1 && c != '_') {
/* 1053 */                   addToken(text, this.currentTokenStart, i - 1, 17, newStartOffset + this.currentTokenStart);
/* 1054 */                   if (c == '"') {
/* 1055 */                     addToken(text, i, i, 13, newStartOffset + i);
/* 1056 */                     this.currentTokenType = 0;
/*      */                     
/*      */                     break;
/*      */                   } 
/* 1060 */                   this.currentTokenStart = i;
/* 1061 */                   this.currentTokenType = 13;
/* 1062 */                   i--;
/*      */ 
/*      */                   
/*      */                   break;
/*      */                 } 
/*      */               } 
/*      */ 
/*      */               
/* 1070 */               if (i == end) {
/* 1071 */                 addToken(text, this.currentTokenStart, i - 1, 17, newStartOffset + this.currentTokenStart);
/* 1072 */                 this.currentTokenStart = i;
/* 1073 */                 this.currentTokenType = 13;
/*      */               } 
/*      */               break;
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/* 1080 */           backslash = false;
/*      */           break;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     } 
/* 1090 */     switch (this.currentTokenType)
/*      */     
/*      */     { 
/*      */       case 13:
/*      */       case 14:
/*      */       case 15:
/* 1096 */         addToken(text, this.currentTokenStart, end - 1, this.currentTokenType, newStartOffset + this.currentTokenStart);
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
/* 1112 */         return (Token)this.firstToken;case 0: addNullToken(); return (Token)this.firstToken; }  addToken(text, this.currentTokenStart, end - 1, this.currentTokenType, newStartOffset + this.currentTokenStart); addNullToken(); return (Token)this.firstToken;
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\modes\UnixShellTokenMaker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */