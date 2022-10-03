/*     */ package org.mozilla.javascript;
/*     */ 
/*     */ import java.text.Collator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class NativeString
/*     */   extends IdScriptableObject
/*     */ {
/*     */   static final long serialVersionUID = 920268368584188687L;
/*  27 */   private static final Object STRING_TAG = "String"; private static final int Id_length = 1; private static final int MAX_INSTANCE_ID = 1; private static final int ConstructorId_fromCharCode = -1; private static final int Id_constructor = 1; private static final int Id_toString = 2; private static final int Id_toSource = 3; private static final int Id_valueOf = 4; private static final int Id_charAt = 5; private static final int Id_charCodeAt = 6; private static final int Id_indexOf = 7; private static final int Id_lastIndexOf = 8; private static final int Id_split = 9; private static final int Id_substring = 10; private static final int Id_toLowerCase = 11; private static final int Id_toUpperCase = 12; private static final int Id_substr = 13; private static final int Id_concat = 14; private static final int Id_slice = 15; private static final int Id_bold = 16; private static final int Id_italics = 17; private static final int Id_fixed = 18; private static final int Id_strike = 19; private static final int Id_small = 20; private static final int Id_big = 21; private static final int Id_blink = 22; private static final int Id_sup = 23; private static final int Id_sub = 24; private static final int Id_fontsize = 25; private static final int Id_fontcolor = 26; private static final int Id_link = 27; private static final int Id_anchor = 28; private static final int Id_equals = 29; private static final int Id_equalsIgnoreCase = 30; private static final int Id_match = 31; private static final int Id_search = 32; private static final int Id_replace = 33; private static final int Id_localeCompare = 34; private static final int Id_toLocaleLowerCase = 35; private static final int Id_toLocaleUpperCase = 36; private static final int Id_trim = 37; private static final int Id_trimLeft = 38; private static final int Id_trimRight = 39; private static final int MAX_PROTOTYPE_ID = 39; private static final int ConstructorId_charAt = -5; private static final int ConstructorId_charCodeAt = -6; private static final int ConstructorId_indexOf = -7; private static final int ConstructorId_lastIndexOf = -8; private static final int ConstructorId_split = -9; private static final int ConstructorId_substring = -10; private static final int ConstructorId_toLowerCase = -11; private static final int ConstructorId_toUpperCase = -12; private static final int ConstructorId_substr = -13; private static final int ConstructorId_concat = -14; private static final int ConstructorId_slice = -15; private static final int ConstructorId_equalsIgnoreCase = -30; private static final int ConstructorId_match = -31; private static final int ConstructorId_search = -32; private static final int ConstructorId_replace = -33; private static final int ConstructorId_localeCompare = -34; private static final int ConstructorId_toLocaleLowerCase = -35;
/*     */   private CharSequence string;
/*     */   
/*     */   static void init(Scriptable scope, boolean sealed) {
/*  31 */     NativeString obj = new NativeString("");
/*  32 */     obj.exportAsJSClass(39, scope, sealed);
/*     */   }
/*     */   
/*     */   NativeString(CharSequence s) {
/*  36 */     this.string = s;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  41 */     return "String";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getMaxInstanceId() {
/*  51 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int findInstanceIdInfo(String s) {
/*  57 */     if (s.equals("length")) {
/*  58 */       return instanceIdInfo(7, 1);
/*     */     }
/*  60 */     return super.findInstanceIdInfo(s);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getInstanceIdName(int id) {
/*  66 */     if (id == 1) return "length"; 
/*  67 */     return super.getInstanceIdName(id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object getInstanceIdValue(int id) {
/*  73 */     if (id == 1) {
/*  74 */       return ScriptRuntime.wrapInt(this.string.length());
/*     */     }
/*  76 */     return super.getInstanceIdValue(id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void fillConstructorProperties(IdFunctionObject ctor) {
/*  82 */     addIdFunctionProperty(ctor, STRING_TAG, -1, "fromCharCode", 1);
/*     */     
/*  84 */     addIdFunctionProperty(ctor, STRING_TAG, -5, "charAt", 2);
/*     */     
/*  86 */     addIdFunctionProperty(ctor, STRING_TAG, -6, "charCodeAt", 2);
/*     */     
/*  88 */     addIdFunctionProperty(ctor, STRING_TAG, -7, "indexOf", 2);
/*     */     
/*  90 */     addIdFunctionProperty(ctor, STRING_TAG, -8, "lastIndexOf", 2);
/*     */     
/*  92 */     addIdFunctionProperty(ctor, STRING_TAG, -9, "split", 3);
/*     */     
/*  94 */     addIdFunctionProperty(ctor, STRING_TAG, -10, "substring", 3);
/*     */     
/*  96 */     addIdFunctionProperty(ctor, STRING_TAG, -11, "toLowerCase", 1);
/*     */     
/*  98 */     addIdFunctionProperty(ctor, STRING_TAG, -12, "toUpperCase", 1);
/*     */     
/* 100 */     addIdFunctionProperty(ctor, STRING_TAG, -13, "substr", 3);
/*     */     
/* 102 */     addIdFunctionProperty(ctor, STRING_TAG, -14, "concat", 2);
/*     */     
/* 104 */     addIdFunctionProperty(ctor, STRING_TAG, -15, "slice", 3);
/*     */     
/* 106 */     addIdFunctionProperty(ctor, STRING_TAG, -30, "equalsIgnoreCase", 2);
/*     */     
/* 108 */     addIdFunctionProperty(ctor, STRING_TAG, -31, "match", 2);
/*     */     
/* 110 */     addIdFunctionProperty(ctor, STRING_TAG, -32, "search", 2);
/*     */     
/* 112 */     addIdFunctionProperty(ctor, STRING_TAG, -33, "replace", 2);
/*     */     
/* 114 */     addIdFunctionProperty(ctor, STRING_TAG, -34, "localeCompare", 2);
/*     */     
/* 116 */     addIdFunctionProperty(ctor, STRING_TAG, -35, "toLocaleLowerCase", 1);
/*     */     
/* 118 */     super.fillConstructorProperties(ctor);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initPrototypeId(int id) {
/*     */     String s;
/*     */     int arity;
/* 126 */     switch (id) { case 1:
/* 127 */         arity = 1; s = "constructor"; break;
/* 128 */       case 2: arity = 0; s = "toString"; break;
/* 129 */       case 3: arity = 0; s = "toSource"; break;
/* 130 */       case 4: arity = 0; s = "valueOf"; break;
/* 131 */       case 5: arity = 1; s = "charAt"; break;
/* 132 */       case 6: arity = 1; s = "charCodeAt"; break;
/* 133 */       case 7: arity = 1; s = "indexOf"; break;
/* 134 */       case 8: arity = 1; s = "lastIndexOf"; break;
/* 135 */       case 9: arity = 2; s = "split"; break;
/* 136 */       case 10: arity = 2; s = "substring"; break;
/* 137 */       case 11: arity = 0; s = "toLowerCase"; break;
/* 138 */       case 12: arity = 0; s = "toUpperCase"; break;
/* 139 */       case 13: arity = 2; s = "substr"; break;
/* 140 */       case 14: arity = 1; s = "concat"; break;
/* 141 */       case 15: arity = 2; s = "slice"; break;
/* 142 */       case 16: arity = 0; s = "bold"; break;
/* 143 */       case 17: arity = 0; s = "italics"; break;
/* 144 */       case 18: arity = 0; s = "fixed"; break;
/* 145 */       case 19: arity = 0; s = "strike"; break;
/* 146 */       case 20: arity = 0; s = "small"; break;
/* 147 */       case 21: arity = 0; s = "big"; break;
/* 148 */       case 22: arity = 0; s = "blink"; break;
/* 149 */       case 23: arity = 0; s = "sup"; break;
/* 150 */       case 24: arity = 0; s = "sub"; break;
/* 151 */       case 25: arity = 0; s = "fontsize"; break;
/* 152 */       case 26: arity = 0; s = "fontcolor"; break;
/* 153 */       case 27: arity = 0; s = "link"; break;
/* 154 */       case 28: arity = 0; s = "anchor"; break;
/* 155 */       case 29: arity = 1; s = "equals"; break;
/* 156 */       case 30: arity = 1; s = "equalsIgnoreCase"; break;
/* 157 */       case 31: arity = 1; s = "match"; break;
/* 158 */       case 32: arity = 1; s = "search"; break;
/* 159 */       case 33: arity = 2; s = "replace"; break;
/* 160 */       case 34: arity = 1; s = "localeCompare"; break;
/* 161 */       case 35: arity = 0; s = "toLocaleLowerCase"; break;
/* 162 */       case 36: arity = 0; s = "toLocaleUpperCase"; break;
/* 163 */       case 37: arity = 0; s = "trim"; break;
/* 164 */       case 38: arity = 0; s = "trimLeft"; break;
/* 165 */       case 39: arity = 0; s = "trimRight"; break;
/* 166 */       default: throw new IllegalArgumentException(String.valueOf(id)); }
/*     */     
/* 168 */     initPrototypeMethod(STRING_TAG, id, s, arity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/* 175 */     if (!f.hasTag(STRING_TAG)) {
/* 176 */       return super.execIdCall(f, cx, scope, thisObj, args);
/*     */     }
/* 178 */     int id = f.methodId(); while (true) {
/*     */       int N; CharSequence s; CharSequence cs; StringBuilder sb; CharSequence charSequence1; CharSequence target; String s1; int actionType; Collator collator; String str; int i; double pos; String s2; char[] chars; int start; char c;
/*     */       int end;
/* 181 */       switch (id) {
/*     */         case -35:
/*     */         case -34:
/*     */         case -33:
/*     */         case -32:
/*     */         case -31:
/*     */         case -30:
/*     */         case -15:
/*     */         case -14:
/*     */         case -13:
/*     */         case -12:
/*     */         case -11:
/*     */         case -10:
/*     */         case -9:
/*     */         case -8:
/*     */         case -7:
/*     */         case -6:
/*     */         case -5:
/* 199 */           if (args.length > 0) {
/* 200 */             thisObj = ScriptRuntime.toObject(cx, scope, ScriptRuntime.toCharSequence(args[0]));
/*     */             
/* 202 */             Object[] newArgs = new Object[args.length - 1];
/* 203 */             for (int j = 0; j < newArgs.length; j++)
/* 204 */               newArgs[j] = args[j + 1]; 
/* 205 */             args = newArgs;
/*     */           } else {
/* 207 */             thisObj = ScriptRuntime.toObject(cx, scope, ScriptRuntime.toCharSequence(thisObj));
/*     */           } 
/*     */           
/* 210 */           id = -id;
/*     */           continue;
/*     */ 
/*     */         
/*     */         case -1:
/* 215 */           N = args.length;
/* 216 */           if (N < 1)
/* 217 */             return ""; 
/* 218 */           sb = new StringBuilder(N);
/* 219 */           for (i = 0; i != N; i++) {
/* 220 */             sb.append(ScriptRuntime.toUint16(args[i]));
/*     */           }
/* 222 */           return sb.toString();
/*     */ 
/*     */         
/*     */         case 1:
/* 226 */           s = (args.length >= 1) ? ScriptRuntime.toCharSequence(args[0]) : "";
/*     */           
/* 228 */           if (thisObj == null)
/*     */           {
/* 230 */             return new NativeString(s);
/*     */           }
/*     */           
/* 233 */           return (s instanceof String) ? s : s.toString();
/*     */ 
/*     */ 
/*     */         
/*     */         case 2:
/*     */         case 4:
/* 239 */           cs = (realThis(thisObj, f)).string;
/* 240 */           return (cs instanceof String) ? cs : cs.toString();
/*     */         
/*     */         case 3:
/* 243 */           charSequence1 = (realThis(thisObj, f)).string;
/* 244 */           return "(new String(\"" + ScriptRuntime.escapeString(charSequence1.toString()) + "\"))";
/*     */ 
/*     */ 
/*     */         
/*     */         case 5:
/*     */         case 6:
/* 250 */           target = ScriptRuntime.toCharSequence(thisObj);
/* 251 */           pos = ScriptRuntime.toInteger(args, 0);
/* 252 */           if (pos < 0.0D || pos >= target.length()) {
/* 253 */             if (id == 5) return ""; 
/* 254 */             return ScriptRuntime.NaNobj;
/*     */           } 
/* 256 */           c = target.charAt((int)pos);
/* 257 */           if (id == 5) return String.valueOf(c); 
/* 258 */           return ScriptRuntime.wrapInt(c);
/*     */ 
/*     */         
/*     */         case 7:
/* 262 */           return ScriptRuntime.wrapInt(js_indexOf(ScriptRuntime.toString(thisObj), args));
/*     */ 
/*     */         
/*     */         case 8:
/* 266 */           return ScriptRuntime.wrapInt(js_lastIndexOf(ScriptRuntime.toString(thisObj), args));
/*     */ 
/*     */         
/*     */         case 9:
/* 270 */           return ScriptRuntime.checkRegExpProxy(cx).js_split(cx, scope, ScriptRuntime.toString(thisObj), args);
/*     */ 
/*     */ 
/*     */         
/*     */         case 10:
/* 275 */           return js_substring(cx, ScriptRuntime.toCharSequence(thisObj), args);
/*     */ 
/*     */         
/*     */         case 11:
/* 279 */           return ScriptRuntime.toString(thisObj).toLowerCase(ScriptRuntime.ROOT_LOCALE);
/*     */ 
/*     */ 
/*     */         
/*     */         case 12:
/* 284 */           return ScriptRuntime.toString(thisObj).toUpperCase(ScriptRuntime.ROOT_LOCALE);
/*     */ 
/*     */         
/*     */         case 13:
/* 288 */           return js_substr(ScriptRuntime.toCharSequence(thisObj), args);
/*     */         
/*     */         case 14:
/* 291 */           return js_concat(ScriptRuntime.toString(thisObj), args);
/*     */         
/*     */         case 15:
/* 294 */           return js_slice(ScriptRuntime.toCharSequence(thisObj), args);
/*     */         
/*     */         case 16:
/* 297 */           return tagify(thisObj, "b", (String)null, (Object[])null);
/*     */         
/*     */         case 17:
/* 300 */           return tagify(thisObj, "i", (String)null, (Object[])null);
/*     */         
/*     */         case 18:
/* 303 */           return tagify(thisObj, "tt", (String)null, (Object[])null);
/*     */         
/*     */         case 19:
/* 306 */           return tagify(thisObj, "strike", (String)null, (Object[])null);
/*     */         
/*     */         case 20:
/* 309 */           return tagify(thisObj, "small", (String)null, (Object[])null);
/*     */         
/*     */         case 21:
/* 312 */           return tagify(thisObj, "big", (String)null, (Object[])null);
/*     */         
/*     */         case 22:
/* 315 */           return tagify(thisObj, "blink", (String)null, (Object[])null);
/*     */         
/*     */         case 23:
/* 318 */           return tagify(thisObj, "sup", (String)null, (Object[])null);
/*     */         
/*     */         case 24:
/* 321 */           return tagify(thisObj, "sub", (String)null, (Object[])null);
/*     */         
/*     */         case 25:
/* 324 */           return tagify(thisObj, "font", "size", args);
/*     */         
/*     */         case 26:
/* 327 */           return tagify(thisObj, "font", "color", args);
/*     */         
/*     */         case 27:
/* 330 */           return tagify(thisObj, "a", "href", args);
/*     */         
/*     */         case 28:
/* 333 */           return tagify(thisObj, "a", "name", args);
/*     */         
/*     */         case 29:
/*     */         case 30:
/* 337 */           s1 = ScriptRuntime.toString(thisObj);
/* 338 */           s2 = ScriptRuntime.toString(args, 0);
/* 339 */           return ScriptRuntime.wrapBoolean((id == 29) ? s1.equals(s2) : s1.equalsIgnoreCase(s2));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case 31:
/*     */         case 32:
/*     */         case 33:
/* 349 */           if (id == 31) {
/* 350 */             actionType = 1;
/* 351 */           } else if (id == 32) {
/* 352 */             actionType = 3;
/*     */           } else {
/* 354 */             actionType = 2;
/*     */           } 
/* 356 */           return ScriptRuntime.checkRegExpProxy(cx).action(cx, scope, thisObj, args, actionType);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case 34:
/* 366 */           collator = Collator.getInstance(cx.getLocale());
/* 367 */           collator.setStrength(3);
/* 368 */           collator.setDecomposition(1);
/* 369 */           return ScriptRuntime.wrapNumber(collator.compare(ScriptRuntime.toString(thisObj), ScriptRuntime.toString(args, 0)));
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case 35:
/* 375 */           return ScriptRuntime.toString(thisObj).toLowerCase(cx.getLocale());
/*     */ 
/*     */ 
/*     */         
/*     */         case 36:
/* 380 */           return ScriptRuntime.toString(thisObj).toUpperCase(cx.getLocale());
/*     */ 
/*     */ 
/*     */         
/*     */         case 37:
/* 385 */           str = ScriptRuntime.toString(thisObj);
/* 386 */           chars = str.toCharArray();
/*     */           
/* 388 */           start = 0;
/* 389 */           while (start < chars.length && ScriptRuntime.isJSWhitespaceOrLineTerminator(chars[start])) {
/* 390 */             start++;
/*     */           }
/* 392 */           end = chars.length;
/* 393 */           while (end > start && ScriptRuntime.isJSWhitespaceOrLineTerminator(chars[end - 1])) {
/* 394 */             end--;
/*     */           }
/*     */           
/* 397 */           return str.substring(start, end);
/*     */ 
/*     */         
/*     */         case 38:
/* 401 */           str = ScriptRuntime.toString(thisObj);
/* 402 */           chars = str.toCharArray();
/*     */           
/* 404 */           start = 0;
/* 405 */           while (start < chars.length && ScriptRuntime.isJSWhitespaceOrLineTerminator(chars[start])) {
/* 406 */             start++;
/*     */           }
/* 408 */           end = chars.length;
/*     */           
/* 410 */           return str.substring(start, end);
/*     */ 
/*     */         
/*     */         case 39:
/* 414 */           str = ScriptRuntime.toString(thisObj);
/* 415 */           chars = str.toCharArray();
/*     */           
/* 417 */           start = 0;
/*     */           
/* 419 */           end = chars.length;
/* 420 */           while (end > start && ScriptRuntime.isJSWhitespaceOrLineTerminator(chars[end - 1])) {
/* 421 */             end--;
/*     */           }
/*     */           
/* 424 */           return str.substring(start, end);
/*     */       }  break;
/*     */     } 
/* 427 */     throw new IllegalArgumentException(String.valueOf(id));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static NativeString realThis(Scriptable thisObj, IdFunctionObject f) {
/* 433 */     if (!(thisObj instanceof NativeString))
/* 434 */       throw incompatibleCallError(f); 
/* 435 */     return (NativeString)thisObj;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String tagify(Object thisObj, String tag, String attribute, Object[] args) {
/* 444 */     String str = ScriptRuntime.toString(thisObj);
/* 445 */     StringBuilder result = new StringBuilder();
/* 446 */     result.append('<');
/* 447 */     result.append(tag);
/* 448 */     if (attribute != null) {
/* 449 */       result.append(' ');
/* 450 */       result.append(attribute);
/* 451 */       result.append("=\"");
/* 452 */       result.append(ScriptRuntime.toString(args, 0));
/* 453 */       result.append('"');
/*     */     } 
/* 455 */     result.append('>');
/* 456 */     result.append(str);
/* 457 */     result.append("</");
/* 458 */     result.append(tag);
/* 459 */     result.append('>');
/* 460 */     return result.toString();
/*     */   }
/*     */   
/*     */   public CharSequence toCharSequence() {
/* 464 */     return this.string;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 469 */     return (this.string instanceof String) ? (String)this.string : this.string.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object get(int index, Scriptable start) {
/* 477 */     if (0 <= index && index < this.string.length()) {
/* 478 */       return String.valueOf(this.string.charAt(index));
/*     */     }
/* 480 */     return super.get(index, start);
/*     */   }
/*     */ 
/*     */   
/*     */   public void put(int index, Scriptable start, Object value) {
/* 485 */     if (0 <= index && index < this.string.length()) {
/*     */       return;
/*     */     }
/* 488 */     super.put(index, start, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int js_indexOf(String target, Object[] args) {
/* 497 */     String search = ScriptRuntime.toString(args, 0);
/* 498 */     double begin = ScriptRuntime.toInteger(args, 1);
/*     */     
/* 500 */     if (begin > target.length()) {
/* 501 */       return -1;
/*     */     }
/* 503 */     if (begin < 0.0D)
/* 504 */       begin = 0.0D; 
/* 505 */     return target.indexOf(search, (int)begin);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int js_lastIndexOf(String target, Object[] args) {
/* 515 */     String search = ScriptRuntime.toString(args, 0);
/* 516 */     double end = ScriptRuntime.toNumber(args, 1);
/*     */     
/* 518 */     if (end != end || end > target.length()) {
/* 519 */       end = target.length();
/* 520 */     } else if (end < 0.0D) {
/* 521 */       end = 0.0D;
/*     */     } 
/* 523 */     return target.lastIndexOf(search, (int)end);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static CharSequence js_substring(Context cx, CharSequence target, Object[] args) {
/*     */     double end;
/* 533 */     int length = target.length();
/* 534 */     double start = ScriptRuntime.toInteger(args, 0);
/*     */ 
/*     */     
/* 537 */     if (start < 0.0D) {
/* 538 */       start = 0.0D;
/* 539 */     } else if (start > length) {
/* 540 */       start = length;
/*     */     } 
/* 542 */     if (args.length <= 1 || args[1] == Undefined.instance) {
/* 543 */       end = length;
/*     */     } else {
/* 545 */       end = ScriptRuntime.toInteger(args[1]);
/* 546 */       if (end < 0.0D) {
/* 547 */         end = 0.0D;
/* 548 */       } else if (end > length) {
/* 549 */         end = length;
/*     */       } 
/*     */       
/* 552 */       if (end < start) {
/* 553 */         if (cx.getLanguageVersion() != 120) {
/* 554 */           double temp = start;
/* 555 */           start = end;
/* 556 */           end = temp;
/*     */         } else {
/*     */           
/* 559 */           end = start;
/*     */         } 
/*     */       }
/*     */     } 
/* 563 */     return target.subSequence((int)start, (int)end);
/*     */   }
/*     */   
/*     */   int getLength() {
/* 567 */     return this.string.length();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static CharSequence js_substr(CharSequence target, Object[] args) {
/*     */     double end;
/* 574 */     if (args.length < 1) {
/* 575 */       return target;
/*     */     }
/* 577 */     double begin = ScriptRuntime.toInteger(args[0]);
/*     */     
/* 579 */     int length = target.length();
/*     */     
/* 581 */     if (begin < 0.0D) {
/* 582 */       begin += length;
/* 583 */       if (begin < 0.0D)
/* 584 */         begin = 0.0D; 
/* 585 */     } else if (begin > length) {
/* 586 */       begin = length;
/*     */     } 
/*     */     
/* 589 */     if (args.length == 1) {
/* 590 */       end = length;
/*     */     } else {
/* 592 */       end = ScriptRuntime.toInteger(args[1]);
/* 593 */       if (end < 0.0D)
/* 594 */         end = 0.0D; 
/* 595 */       end += begin;
/* 596 */       if (end > length) {
/* 597 */         end = length;
/*     */       }
/*     */     } 
/* 600 */     return target.subSequence((int)begin, (int)end);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String js_concat(String target, Object[] args) {
/* 607 */     int N = args.length;
/* 608 */     if (N == 0) return target; 
/* 609 */     if (N == 1) {
/* 610 */       String arg = ScriptRuntime.toString(args[0]);
/* 611 */       return target.concat(arg);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 616 */     int size = target.length();
/* 617 */     String[] argsAsStrings = new String[N];
/* 618 */     for (int i = 0; i != N; i++) {
/* 619 */       String s = ScriptRuntime.toString(args[i]);
/* 620 */       argsAsStrings[i] = s;
/* 621 */       size += s.length();
/*     */     } 
/*     */     
/* 624 */     StringBuilder result = new StringBuilder(size);
/* 625 */     result.append(target);
/* 626 */     for (int j = 0; j != N; j++) {
/* 627 */       result.append(argsAsStrings[j]);
/*     */     }
/* 629 */     return result.toString();
/*     */   }
/*     */   
/*     */   private static CharSequence js_slice(CharSequence target, Object[] args) {
/* 633 */     double end, begin = (args.length < 1) ? 0.0D : ScriptRuntime.toInteger(args[0]);
/*     */     
/* 635 */     int length = target.length();
/* 636 */     if (begin < 0.0D) {
/* 637 */       begin += length;
/* 638 */       if (begin < 0.0D)
/* 639 */         begin = 0.0D; 
/* 640 */     } else if (begin > length) {
/* 641 */       begin = length;
/*     */     } 
/*     */     
/* 644 */     if (args.length < 2 || args[1] == Undefined.instance) {
/* 645 */       end = length;
/*     */     } else {
/* 647 */       end = ScriptRuntime.toInteger(args[1]);
/* 648 */       if (end < 0.0D) {
/* 649 */         end += length;
/* 650 */         if (end < 0.0D)
/* 651 */           end = 0.0D; 
/* 652 */       } else if (end > length) {
/* 653 */         end = length;
/*     */       } 
/* 655 */       if (end < begin)
/* 656 */         end = begin; 
/*     */     } 
/* 658 */     return target.subSequence((int)begin, (int)end);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int findPrototypeId(String s) {
/* 668 */     int c, id = 0; String X = null;
/* 669 */     switch (s.length()) { case 3:
/* 670 */         c = s.charAt(2);
/* 671 */         if (c == 98) { if (s.charAt(0) == 's' && s.charAt(1) == 'u') { id = 24;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 732 */             return id; }  break; }  if (c == 103) { if (s.charAt(0) == 'b' && s.charAt(1) == 'i') { id = 21; return id; }  break; }  if (c == 112 && s.charAt(0) == 's' && s.charAt(1) == 'u') { id = 23; return id; }  break;case 4: c = s.charAt(0); if (c == 98) { X = "bold"; id = 16; break; }  if (c == 108) { X = "link"; id = 27; break; }  if (c == 116) { X = "trim"; id = 37; }  break;case 5: switch (s.charAt(4)) { case 'd': X = "fixed"; id = 18; break;case 'e': X = "slice"; id = 15; break;case 'h': X = "match"; id = 31; break;case 'k': X = "blink"; id = 22; break;case 'l': X = "small"; id = 20; break;case 't': X = "split"; id = 9; break; }  break;case 6: switch (s.charAt(1)) { case 'e': X = "search"; id = 32; break;case 'h': X = "charAt"; id = 5; break;case 'n': X = "anchor"; id = 28; break;case 'o': X = "concat"; id = 14; break;case 'q': X = "equals"; id = 29; break;case 't': X = "strike"; id = 19; break;case 'u': X = "substr"; id = 13; break; }  break;case 7: switch (s.charAt(1)) { case 'a': X = "valueOf"; id = 4; break;case 'e': X = "replace"; id = 33; break;case 'n': X = "indexOf"; id = 7; break;case 't': X = "italics"; id = 17; break; }  break;case 8: switch (s.charAt(4)) { case 'L': X = "trimLeft"; id = 38; break;case 'r': X = "toString"; id = 2; break;case 's': X = "fontsize"; id = 25; break;case 'u': X = "toSource"; id = 3; break; }  break;case 9: c = s.charAt(0); if (c == 102) { X = "fontcolor"; id = 26; break; }  if (c == 115) { X = "substring"; id = 10; break; }  if (c == 116) { X = "trimRight"; id = 39; }  break;case 10: X = "charCodeAt"; id = 6; break;case 11: switch (s.charAt(2)) { case 'L': X = "toLowerCase"; id = 11; break;case 'U': X = "toUpperCase"; id = 12; break;case 'n': X = "constructor"; id = 1; break;case 's': X = "lastIndexOf"; id = 8; break; }  break;case 13: X = "localeCompare"; id = 34; break;case 16: X = "equalsIgnoreCase"; id = 30; break;case 17: c = s.charAt(8); if (c == 76) { X = "toLocaleLowerCase"; id = 35; break; }  if (c == 85) { X = "toLocaleUpperCase"; id = 36; }  break; }  if (X != null && X != s && !X.equals(s)) id = 0;  return id;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\NativeString.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */