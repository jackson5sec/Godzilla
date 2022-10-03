/*     */ package org.mozilla.javascript.regexp;
/*     */ import org.mozilla.javascript.Context;
/*     */ import org.mozilla.javascript.RegExpProxy;
/*     */ import org.mozilla.javascript.ScriptRuntime;
/*     */ import org.mozilla.javascript.Scriptable;
/*     */ 
/*     */ public class RegExpImpl implements RegExpProxy {
/*     */   protected String input;
/*     */   protected boolean multiline;
/*     */   protected SubString[] parens;
/*     */   protected SubString lastMatch;
/*     */   protected SubString lastParen;
/*     */   protected SubString leftContext;
/*     */   protected SubString rightContext;
/*     */   
/*     */   public boolean isRegExp(Scriptable obj) {
/*  17 */     return obj instanceof NativeRegExp;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object compileRegExp(Context cx, String source, String flags) {
/*  22 */     return NativeRegExp.compileRE(cx, source, flags, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Scriptable wrapRegExp(Context cx, Scriptable scope, Object compiled) {
/*  28 */     return (Scriptable)new NativeRegExp(scope, (RECompiled)compiled); } public Object action(Context cx, Scriptable scope, Scriptable thisObj, Object[] args, int actionType) { NativeRegExp re; boolean useRE; Object rval; NativeRegExp nativeRegExp1;
/*     */     String search;
/*     */     Object arg1;
/*     */     String repstr;
/*     */     Function lambda;
/*     */     Object val;
/*     */     SubString rc;
/*  35 */     GlobData data = new GlobData();
/*  36 */     data.mode = actionType;
/*  37 */     data.str = ScriptRuntime.toString(thisObj);
/*     */     
/*  39 */     switch (actionType) {
/*     */       
/*     */       case 1:
/*  42 */         re = createRegExp(cx, scope, args, 1, false);
/*  43 */         rval = matchOrReplace(cx, scope, thisObj, args, this, data, re);
/*     */         
/*  45 */         return (data.arrayobj == null) ? rval : data.arrayobj;
/*     */ 
/*     */ 
/*     */       
/*     */       case 3:
/*  50 */         re = createRegExp(cx, scope, args, 1, false);
/*  51 */         return matchOrReplace(cx, scope, thisObj, args, this, data, re);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 2:
/*  57 */         useRE = ((args.length > 0 && args[0] instanceof NativeRegExp) || args.length > 2);
/*     */         
/*  59 */         nativeRegExp1 = null;
/*  60 */         search = null;
/*  61 */         if (useRE) {
/*  62 */           nativeRegExp1 = createRegExp(cx, scope, args, 2, true);
/*     */         } else {
/*  64 */           Object arg0 = (args.length < 1) ? Undefined.instance : args[0];
/*  65 */           search = ScriptRuntime.toString(arg0);
/*     */         } 
/*     */         
/*  68 */         arg1 = (args.length < 2) ? Undefined.instance : args[1];
/*  69 */         repstr = null;
/*  70 */         lambda = null;
/*  71 */         if (arg1 instanceof Function) {
/*  72 */           lambda = (Function)arg1;
/*     */         } else {
/*  74 */           repstr = ScriptRuntime.toString(arg1);
/*     */         } 
/*     */         
/*  77 */         data.lambda = lambda;
/*  78 */         data.repstr = repstr;
/*  79 */         data.dollar = (repstr == null) ? -1 : repstr.indexOf('$');
/*  80 */         data.charBuf = null;
/*  81 */         data.leftIndex = 0;
/*     */ 
/*     */         
/*  84 */         if (useRE) {
/*  85 */           val = matchOrReplace(cx, scope, thisObj, args, this, data, nativeRegExp1);
/*     */         } else {
/*     */           
/*  88 */           String str = data.str;
/*  89 */           int index = str.indexOf(search);
/*  90 */           if (index >= 0) {
/*  91 */             int slen = search.length();
/*  92 */             this.lastParen = null;
/*  93 */             this.leftContext = new SubString(str, 0, index);
/*  94 */             this.lastMatch = new SubString(str, index, slen);
/*  95 */             this.rightContext = new SubString(str, index + slen, str.length() - index - slen);
/*  96 */             val = Boolean.TRUE;
/*     */           } else {
/*  98 */             val = Boolean.FALSE;
/*     */           } 
/*     */         } 
/*     */         
/* 102 */         if (data.charBuf == null) {
/* 103 */           if (data.global || val == null || !val.equals(Boolean.TRUE))
/*     */           {
/*     */ 
/*     */             
/* 107 */             return data.str;
/*     */           }
/* 109 */           SubString lc = this.leftContext;
/* 110 */           replace_glob(data, cx, scope, this, lc.index, lc.length);
/*     */         } 
/* 112 */         rc = this.rightContext;
/* 113 */         data.charBuf.append(rc.str, rc.index, rc.index + rc.length);
/* 114 */         return data.charBuf.toString();
/*     */     } 
/*     */ 
/*     */     
/* 118 */     throw Kit.codeBug(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static NativeRegExp createRegExp(Context cx, Scriptable scope, Object[] args, int optarg, boolean forceFlat) {
/*     */     NativeRegExp re;
/* 127 */     Scriptable topScope = ScriptableObject.getTopLevelScope(scope);
/* 128 */     if (args.length == 0 || args[0] == Undefined.instance) {
/* 129 */       RECompiled compiled = NativeRegExp.compileRE(cx, "", "", false);
/* 130 */       re = new NativeRegExp(topScope, compiled);
/* 131 */     } else if (args[0] instanceof NativeRegExp) {
/* 132 */       re = (NativeRegExp)args[0];
/*     */     } else {
/* 134 */       String opt, src = ScriptRuntime.toString(args[0]);
/*     */       
/* 136 */       if (optarg < args.length) {
/* 137 */         args[0] = src;
/* 138 */         opt = ScriptRuntime.toString(args[optarg]);
/*     */       } else {
/* 140 */         opt = null;
/*     */       } 
/* 142 */       RECompiled compiled = NativeRegExp.compileRE(cx, src, opt, forceFlat);
/* 143 */       re = new NativeRegExp(topScope, compiled);
/*     */     } 
/* 145 */     return re;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Object matchOrReplace(Context cx, Scriptable scope, Scriptable thisObj, Object[] args, RegExpImpl reImpl, GlobData data, NativeRegExp re) {
/* 156 */     String str = data.str;
/* 157 */     data.global = ((re.getFlags() & 0x1) != 0);
/* 158 */     int[] indexp = { 0 };
/* 159 */     Object result = null;
/* 160 */     if (data.mode == 3) {
/* 161 */       result = re.executeRegExp(cx, scope, reImpl, str, indexp, 0);
/*     */       
/* 163 */       if (result != null && result.equals(Boolean.TRUE))
/* 164 */       { result = Integer.valueOf(reImpl.leftContext.length); }
/*     */       else
/* 166 */       { result = Integer.valueOf(-1); } 
/* 167 */     } else if (data.global) {
/* 168 */       re.lastIndex = Double.valueOf(0.0D);
/* 169 */       for (int count = 0; indexp[0] <= str.length(); count++) {
/* 170 */         result = re.executeRegExp(cx, scope, reImpl, str, indexp, 0);
/*     */         
/* 172 */         if (result == null || !result.equals(Boolean.TRUE))
/*     */           break; 
/* 174 */         if (data.mode == 1) {
/* 175 */           match_glob(data, cx, scope, count, reImpl);
/*     */         } else {
/* 177 */           if (data.mode != 2) Kit.codeBug(); 
/* 178 */           SubString lastMatch = reImpl.lastMatch;
/* 179 */           int leftIndex = data.leftIndex;
/* 180 */           int leftlen = lastMatch.index - leftIndex;
/* 181 */           data.leftIndex = lastMatch.index + lastMatch.length;
/* 182 */           replace_glob(data, cx, scope, reImpl, leftIndex, leftlen);
/*     */         } 
/* 184 */         if (reImpl.lastMatch.length == 0) {
/* 185 */           if (indexp[0] == str.length())
/*     */             break; 
/* 187 */           indexp[0] = indexp[0] + 1;
/*     */         } 
/*     */       } 
/*     */     } else {
/* 191 */       result = re.executeRegExp(cx, scope, reImpl, str, indexp, (data.mode == 2) ? 0 : 1);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 197 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int find_split(Context cx, Scriptable scope, String target, String separator, Scriptable reObj, int[] ip, int[] matchlen, boolean[] matched, String[][] parensp) {
/* 207 */     int result, i = ip[0];
/* 208 */     int length = target.length();
/*     */ 
/*     */     
/* 211 */     int version = cx.getLanguageVersion();
/* 212 */     NativeRegExp re = (NativeRegExp)reObj;
/*     */ 
/*     */     
/*     */     while (true) {
/* 216 */       int ipsave = ip[0];
/* 217 */       ip[0] = i;
/* 218 */       Object ret = re.executeRegExp(cx, scope, this, target, ip, 0);
/*     */       
/* 220 */       if (ret != Boolean.TRUE) {
/*     */         
/* 222 */         ip[0] = ipsave;
/* 223 */         matchlen[0] = 1;
/* 224 */         matched[0] = false;
/* 225 */         return length;
/*     */       } 
/* 227 */       i = ip[0];
/* 228 */       ip[0] = ipsave;
/* 229 */       matched[0] = true;
/*     */       
/* 231 */       SubString sep = this.lastMatch;
/* 232 */       matchlen[0] = sep.length;
/* 233 */       if (matchlen[0] == 0)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 240 */         if (i == ip[0]) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 247 */           if (i == length) {
/* 248 */             if (version == 120) {
/* 249 */               matchlen[0] = 1;
/* 250 */               int k = i;
/*     */               break;
/*     */             } 
/* 253 */             int j = -1;
/*     */             break;
/*     */           } 
/* 256 */           i++;
/*     */           
/*     */           continue;
/*     */         } 
/*     */       }
/* 261 */       result = i - matchlen[0];
/*     */       break;
/*     */     } 
/* 264 */     int size = (this.parens == null) ? 0 : this.parens.length;
/* 265 */     parensp[0] = new String[size];
/* 266 */     for (int num = 0; num < size; num++) {
/* 267 */       SubString parsub = getParenSubString(num);
/* 268 */       parensp[0][num] = parsub.toString();
/*     */     } 
/* 270 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   SubString getParenSubString(int i) {
/* 279 */     if (this.parens != null && i < this.parens.length) {
/* 280 */       SubString parsub = this.parens[i];
/* 281 */       if (parsub != null) {
/* 282 */         return parsub;
/*     */       }
/*     */     } 
/* 285 */     return SubString.emptySubString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void match_glob(GlobData mdata, Context cx, Scriptable scope, int count, RegExpImpl reImpl) {
/* 295 */     if (mdata.arrayobj == null) {
/* 296 */       mdata.arrayobj = cx.newArray(scope, 0);
/*     */     }
/* 298 */     SubString matchsub = reImpl.lastMatch;
/* 299 */     String matchstr = matchsub.toString();
/* 300 */     mdata.arrayobj.put(count, mdata.arrayobj, matchstr);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void replace_glob(GlobData rdata, Context cx, Scriptable scope, RegExpImpl reImpl, int leftIndex, int leftlen) {
/*     */     int replen;
/*     */     String lambdaStr;
/* 312 */     if (rdata.lambda != null) {
/*     */ 
/*     */       
/* 315 */       SubString[] parens = reImpl.parens;
/* 316 */       int parenCount = (parens == null) ? 0 : parens.length;
/* 317 */       Object[] args = new Object[parenCount + 3];
/* 318 */       args[0] = reImpl.lastMatch.toString();
/* 319 */       for (int i = 0; i < parenCount; i++) {
/* 320 */         SubString sub = parens[i];
/* 321 */         if (sub != null) {
/* 322 */           args[i + 1] = sub.toString();
/*     */         } else {
/* 324 */           args[i + 1] = Undefined.instance;
/*     */         } 
/*     */       } 
/* 327 */       args[parenCount + 1] = Integer.valueOf(reImpl.leftContext.length);
/* 328 */       args[parenCount + 2] = rdata.str;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 333 */       if (reImpl != ScriptRuntime.getRegExpProxy(cx)) Kit.codeBug(); 
/* 334 */       RegExpImpl re2 = new RegExpImpl();
/* 335 */       re2.multiline = reImpl.multiline;
/* 336 */       re2.input = reImpl.input;
/* 337 */       ScriptRuntime.setRegExpProxy(cx, re2);
/*     */       try {
/* 339 */         Scriptable parent = ScriptableObject.getTopLevelScope(scope);
/* 340 */         Object result = rdata.lambda.call(cx, parent, parent, args);
/* 341 */         lambdaStr = ScriptRuntime.toString(result);
/*     */       } finally {
/* 343 */         ScriptRuntime.setRegExpProxy(cx, reImpl);
/*     */       } 
/* 345 */       replen = lambdaStr.length();
/*     */     } else {
/* 347 */       lambdaStr = null;
/* 348 */       replen = rdata.repstr.length();
/* 349 */       if (rdata.dollar >= 0) {
/* 350 */         int[] skip = new int[1];
/* 351 */         int dp = rdata.dollar;
/*     */         do {
/* 353 */           SubString sub = interpretDollar(cx, reImpl, rdata.repstr, dp, skip);
/*     */           
/* 355 */           if (sub != null) {
/* 356 */             replen += sub.length - skip[0];
/* 357 */             dp += skip[0];
/*     */           } else {
/* 359 */             dp++;
/*     */           } 
/* 361 */           dp = rdata.repstr.indexOf('$', dp);
/* 362 */         } while (dp >= 0);
/*     */       } 
/*     */     } 
/*     */     
/* 366 */     int growth = leftlen + replen + reImpl.rightContext.length;
/* 367 */     StringBuilder charBuf = rdata.charBuf;
/* 368 */     if (charBuf == null) {
/* 369 */       charBuf = new StringBuilder(growth);
/* 370 */       rdata.charBuf = charBuf;
/*     */     } else {
/* 372 */       charBuf.ensureCapacity(rdata.charBuf.length() + growth);
/*     */     } 
/*     */     
/* 375 */     charBuf.append(reImpl.leftContext.str, leftIndex, leftIndex + leftlen);
/* 376 */     if (rdata.lambda != null) {
/* 377 */       charBuf.append(lambdaStr);
/*     */     } else {
/* 379 */       do_replace(rdata, cx, reImpl);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static SubString interpretDollar(Context cx, RegExpImpl res, String da, int dp, int[] skip) {
/* 389 */     if (da.charAt(dp) != '$') Kit.codeBug();
/*     */ 
/*     */     
/* 392 */     int version = cx.getLanguageVersion();
/* 393 */     if (version != 0 && version <= 140)
/*     */     {
/*     */       
/* 396 */       if (dp > 0 && da.charAt(dp - 1) == '\\')
/* 397 */         return null; 
/*     */     }
/* 399 */     int daL = da.length();
/* 400 */     if (dp + 1 >= daL) {
/* 401 */       return null;
/*     */     }
/* 403 */     char dc = da.charAt(dp + 1);
/* 404 */     if (NativeRegExp.isDigit(dc)) {
/*     */       int num, cp;
/* 406 */       if (version != 0 && version <= 140) {
/*     */ 
/*     */         
/* 409 */         if (dc == '0') {
/* 410 */           return null;
/*     */         }
/* 412 */         num = 0;
/* 413 */         cp = dp;
/* 414 */         while (++cp < daL && NativeRegExp.isDigit(dc = da.charAt(cp))) {
/*     */           
/* 416 */           int tmp = 10 * num + dc - 48;
/* 417 */           if (tmp < num)
/*     */             break; 
/* 419 */           num = tmp;
/*     */         } 
/*     */       } else {
/*     */         
/* 423 */         int parenCount = (res.parens == null) ? 0 : res.parens.length;
/* 424 */         num = dc - 48;
/* 425 */         if (num > parenCount)
/* 426 */           return null; 
/* 427 */         cp = dp + 2;
/* 428 */         if (dp + 2 < daL) {
/* 429 */           dc = da.charAt(dp + 2);
/* 430 */           if (NativeRegExp.isDigit(dc)) {
/* 431 */             int tmp = 10 * num + dc - 48;
/* 432 */             if (tmp <= parenCount) {
/* 433 */               cp++;
/* 434 */               num = tmp;
/*     */             } 
/*     */           } 
/*     */         } 
/* 438 */         if (num == 0) return null;
/*     */       
/*     */       } 
/* 441 */       num--;
/* 442 */       skip[0] = cp - dp;
/* 443 */       return res.getParenSubString(num);
/*     */     } 
/*     */     
/* 446 */     skip[0] = 2;
/* 447 */     switch (dc) {
/*     */       case '$':
/* 449 */         return new SubString("$");
/*     */       case '&':
/* 451 */         return res.lastMatch;
/*     */       case '+':
/* 453 */         return res.lastParen;
/*     */       case '`':
/* 455 */         if (version == 120) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 463 */           res.leftContext.index = 0;
/* 464 */           res.leftContext.length = res.lastMatch.index;
/*     */         } 
/* 466 */         return res.leftContext;
/*     */       case '\'':
/* 468 */         return res.rightContext;
/*     */     } 
/* 470 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void do_replace(GlobData rdata, Context cx, RegExpImpl regExpImpl) {
/* 479 */     StringBuilder charBuf = rdata.charBuf;
/* 480 */     int cp = 0;
/* 481 */     String da = rdata.repstr;
/* 482 */     int dp = rdata.dollar;
/* 483 */     if (dp != -1) {
/* 484 */       int[] skip = new int[1];
/*     */       do {
/* 486 */         int len = dp - cp;
/* 487 */         charBuf.append(da.substring(cp, dp));
/* 488 */         cp = dp;
/* 489 */         SubString sub = interpretDollar(cx, regExpImpl, da, dp, skip);
/*     */         
/* 491 */         if (sub != null) {
/* 492 */           len = sub.length;
/* 493 */           if (len > 0) {
/* 494 */             charBuf.append(sub.str, sub.index, sub.index + len);
/*     */           }
/* 496 */           cp += skip[0];
/* 497 */           dp += skip[0];
/*     */         } else {
/* 499 */           dp++;
/*     */         } 
/* 501 */         dp = da.indexOf('$', dp);
/* 502 */       } while (dp >= 0);
/*     */     } 
/* 504 */     int daL = da.length();
/* 505 */     if (daL > cp) {
/* 506 */       charBuf.append(da.substring(cp, daL));
/*     */     }
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
/*     */   public Object js_split(Context cx, Scriptable scope, String target, Object[] args) {
/* 519 */     Scriptable result = cx.newArray(scope, 0);
/*     */ 
/*     */     
/* 522 */     boolean limited = (args.length > 1 && args[1] != Undefined.instance);
/* 523 */     long limit = 0L;
/* 524 */     if (limited) {
/*     */       
/* 526 */       limit = ScriptRuntime.toUint32(args[1]);
/* 527 */       if (limit > target.length()) {
/* 528 */         limit = (1 + target.length());
/*     */       }
/*     */     } 
/*     */     
/* 532 */     if (args.length < 1 || args[0] == Undefined.instance) {
/* 533 */       result.put(0, result, target);
/* 534 */       return result;
/*     */     } 
/*     */     
/* 537 */     String separator = null;
/* 538 */     int[] matchlen = new int[1];
/* 539 */     Scriptable re = null;
/* 540 */     RegExpProxy reProxy = null;
/* 541 */     if (args[0] instanceof Scriptable) {
/* 542 */       reProxy = ScriptRuntime.getRegExpProxy(cx);
/* 543 */       if (reProxy != null) {
/* 544 */         Scriptable test = (Scriptable)args[0];
/* 545 */         if (reProxy.isRegExp(test)) {
/* 546 */           re = test;
/*     */         }
/*     */       } 
/*     */     } 
/* 550 */     if (re == null) {
/* 551 */       separator = ScriptRuntime.toString(args[0]);
/* 552 */       matchlen[0] = separator.length();
/*     */     } 
/*     */ 
/*     */     
/* 556 */     int[] ip = { 0 };
/*     */     
/* 558 */     int len = 0;
/* 559 */     boolean[] matched = { false };
/* 560 */     String[][] parens = { null };
/* 561 */     int version = cx.getLanguageVersion();
/*     */     
/*     */     int match;
/* 564 */     while ((match = find_split(cx, scope, target, separator, version, reProxy, re, ip, matchlen, matched, parens)) >= 0) {
/*     */       String substr;
/* 566 */       if ((limited && len >= limit) || match > target.length()) {
/*     */         break;
/*     */       }
/*     */       
/* 570 */       if (target.length() == 0) {
/* 571 */         substr = target;
/*     */       } else {
/* 573 */         substr = target.substring(ip[0], match);
/*     */       } 
/* 575 */       result.put(len, result, substr);
/* 576 */       len++;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 582 */       if (re != null && matched[0] == true) {
/* 583 */         int size = (parens[0]).length;
/* 584 */         for (int num = 0; num < size && (
/* 585 */           !limited || len < limit); num++) {
/*     */           
/* 587 */           result.put(len, result, parens[0][num]);
/* 588 */           len++;
/*     */         } 
/* 590 */         matched[0] = false;
/*     */       } 
/* 592 */       ip[0] = match + matchlen[0];
/*     */       
/* 594 */       if (version < 130 && version != 0)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 601 */         if (!limited && ip[0] == target.length())
/*     */           break; 
/*     */       }
/*     */     } 
/* 605 */     return result;
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
/*     */   private static int find_split(Context cx, Scriptable scope, String target, String separator, int version, RegExpProxy reProxy, Scriptable re, int[] ip, int[] matchlen, boolean[] matched, String[][] parensp) {
/* 626 */     int i = ip[0];
/* 627 */     int length = target.length();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 634 */     if (version == 120 && re == null && separator.length() == 1 && separator.charAt(0) == ' ') {
/*     */ 
/*     */ 
/*     */       
/* 638 */       if (i == 0) {
/* 639 */         while (i < length && Character.isWhitespace(target.charAt(i)))
/* 640 */           i++; 
/* 641 */         ip[0] = i;
/*     */       } 
/*     */ 
/*     */       
/* 645 */       if (i == length) {
/* 646 */         return -1;
/*     */       }
/*     */ 
/*     */       
/* 650 */       while (i < length && !Character.isWhitespace(target.charAt(i))) {
/* 651 */         i++;
/*     */       }
/*     */       
/* 654 */       int j = i;
/* 655 */       while (j < length && Character.isWhitespace(target.charAt(j))) {
/* 656 */         j++;
/*     */       }
/*     */       
/* 659 */       matchlen[0] = j - i;
/* 660 */       return i;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 673 */     if (i > length) {
/* 674 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 681 */     if (re != null) {
/* 682 */       return reProxy.find_split(cx, scope, target, separator, re, ip, matchlen, matched, parensp);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 691 */     if (version != 0 && version < 130 && length == 0)
/*     */     {
/* 693 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 705 */     if (separator.length() == 0) {
/* 706 */       if (version == 120) {
/* 707 */         if (i == length) {
/* 708 */           matchlen[0] = 1;
/* 709 */           return i;
/*     */         } 
/* 711 */         return i + 1;
/*     */       } 
/* 713 */       return (i == length) ? -1 : (i + 1);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 719 */     if (ip[0] >= length) {
/* 720 */       return length;
/*     */     }
/* 722 */     i = target.indexOf(separator, ip[0]);
/*     */     
/* 724 */     return (i != -1) ? i : length;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\regexp\RegExpImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */