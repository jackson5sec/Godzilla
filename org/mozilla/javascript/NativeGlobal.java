/*     */ package org.mozilla.javascript;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.mozilla.javascript.xml.XMLLib;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NativeGlobal
/*     */   implements Serializable, IdFunctionCall
/*     */ {
/*     */   static final long serialVersionUID = 6080442165748707530L;
/*     */   private static final String URI_DECODE_RESERVED = ";/?:@&=+$,#";
/*     */   private static final int INVALID_UTF8 = 2147483647;
/*     */   
/*     */   public static void init(Context cx, Scriptable scope, boolean sealed) {
/*  30 */     NativeGlobal obj = new NativeGlobal();
/*     */     
/*  32 */     for (int id = 1; id <= 13; id++) {
/*     */       String name;
/*  34 */       int arity = 1;
/*  35 */       switch (id) {
/*     */         case 1:
/*  37 */           name = "decodeURI";
/*     */           break;
/*     */         case 2:
/*  40 */           name = "decodeURIComponent";
/*     */           break;
/*     */         case 3:
/*  43 */           name = "encodeURI";
/*     */           break;
/*     */         case 4:
/*  46 */           name = "encodeURIComponent";
/*     */           break;
/*     */         case 5:
/*  49 */           name = "escape";
/*     */           break;
/*     */         case 6:
/*  52 */           name = "eval";
/*     */           break;
/*     */         case 7:
/*  55 */           name = "isFinite";
/*     */           break;
/*     */         case 8:
/*  58 */           name = "isNaN";
/*     */           break;
/*     */         case 9:
/*  61 */           name = "isXMLName";
/*     */           break;
/*     */         case 10:
/*  64 */           name = "parseFloat";
/*     */           break;
/*     */         case 11:
/*  67 */           name = "parseInt";
/*  68 */           arity = 2;
/*     */           break;
/*     */         case 12:
/*  71 */           name = "unescape";
/*     */           break;
/*     */         case 13:
/*  74 */           name = "uneval";
/*     */           break;
/*     */         default:
/*  77 */           throw Kit.codeBug();
/*     */       } 
/*  79 */       IdFunctionObject f = new IdFunctionObject(obj, FTAG, id, name, arity, scope);
/*     */       
/*  81 */       if (sealed) {
/*  82 */         f.sealObject();
/*     */       }
/*  84 */       f.exportAsScopeProperty();
/*     */     } 
/*     */     
/*  87 */     ScriptableObject.defineProperty(scope, "NaN", ScriptRuntime.NaNobj, 7);
/*     */ 
/*     */     
/*  90 */     ScriptableObject.defineProperty(scope, "Infinity", ScriptRuntime.wrapNumber(Double.POSITIVE_INFINITY), 7);
/*     */ 
/*     */ 
/*     */     
/*  94 */     ScriptableObject.defineProperty(scope, "undefined", Undefined.instance, 7);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 102 */     for (TopLevel.NativeErrors error : TopLevel.NativeErrors.values()) {
/* 103 */       if (error != TopLevel.NativeErrors.Error) {
/*     */ 
/*     */ 
/*     */         
/* 107 */         String name = error.name();
/* 108 */         ScriptableObject errorProto = (ScriptableObject)ScriptRuntime.newBuiltinObject(cx, scope, TopLevel.Builtins.Error, ScriptRuntime.emptyArgs);
/*     */ 
/*     */ 
/*     */         
/* 112 */         errorProto.put("name", errorProto, name);
/* 113 */         errorProto.put("message", errorProto, "");
/* 114 */         IdFunctionObject ctor = new IdFunctionObject(obj, FTAG, 14, name, 1, scope);
/*     */ 
/*     */         
/* 117 */         ctor.markAsConstructor(errorProto);
/* 118 */         errorProto.put("constructor", errorProto, ctor);
/* 119 */         errorProto.setAttributes("constructor", 2);
/* 120 */         if (sealed) {
/* 121 */           errorProto.sealObject();
/* 122 */           ctor.sealObject();
/*     */         } 
/* 124 */         ctor.exportAsScopeProperty();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/* 131 */     if (f.hasTag(FTAG)) {
/* 132 */       String str; boolean result; Object name, value; XMLLib xmlLib; int methodId = f.methodId();
/* 133 */       switch (methodId) {
/*     */         case 1:
/*     */         case 2:
/* 136 */           str = ScriptRuntime.toString(args, 0);
/* 137 */           return decode(str, (methodId == 1));
/*     */ 
/*     */         
/*     */         case 3:
/*     */         case 4:
/* 142 */           str = ScriptRuntime.toString(args, 0);
/* 143 */           return encode(str, (methodId == 3));
/*     */ 
/*     */         
/*     */         case 5:
/* 147 */           return js_escape(args);
/*     */         
/*     */         case 6:
/* 150 */           return js_eval(cx, scope, args);
/*     */ 
/*     */         
/*     */         case 7:
/* 154 */           if (args.length < 1) {
/* 155 */             result = false;
/*     */           } else {
/* 157 */             double d = ScriptRuntime.toNumber(args[0]);
/* 158 */             result = (d == d && d != Double.POSITIVE_INFINITY && d != Double.NEGATIVE_INFINITY);
/*     */           } 
/*     */ 
/*     */           
/* 162 */           return ScriptRuntime.wrapBoolean(result);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case 8:
/* 168 */           if (args.length < 1) {
/* 169 */             result = true;
/*     */           } else {
/* 171 */             double d = ScriptRuntime.toNumber(args[0]);
/* 172 */             result = (d != d);
/*     */           } 
/* 174 */           return ScriptRuntime.wrapBoolean(result);
/*     */ 
/*     */         
/*     */         case 9:
/* 178 */           name = (args.length == 0) ? Undefined.instance : args[0];
/*     */           
/* 180 */           xmlLib = XMLLib.extractFromScope(scope);
/* 181 */           return ScriptRuntime.wrapBoolean(xmlLib.isXMLName(cx, name));
/*     */ 
/*     */ 
/*     */         
/*     */         case 10:
/* 186 */           return js_parseFloat(args);
/*     */         
/*     */         case 11:
/* 189 */           return js_parseInt(args);
/*     */         
/*     */         case 12:
/* 192 */           return js_unescape(args);
/*     */         
/*     */         case 13:
/* 195 */           value = (args.length != 0) ? args[0] : Undefined.instance;
/*     */           
/* 197 */           return ScriptRuntime.uneval(cx, scope, value);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case 14:
/* 203 */           return NativeError.make(cx, scope, f, args);
/*     */       } 
/*     */     } 
/* 206 */     throw f.unknown();
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object js_parseInt(Object[] args) {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: iconst_0
/*     */     //   2: invokestatic toString : ([Ljava/lang/Object;I)Ljava/lang/String;
/*     */     //   5: astore_2
/*     */     //   6: aload_1
/*     */     //   7: iconst_1
/*     */     //   8: invokestatic toInt32 : ([Ljava/lang/Object;I)I
/*     */     //   11: istore_3
/*     */     //   12: aload_2
/*     */     //   13: invokevirtual length : ()I
/*     */     //   16: istore #4
/*     */     //   18: iload #4
/*     */     //   20: ifne -> 27
/*     */     //   23: getstatic org/mozilla/javascript/ScriptRuntime.NaNobj : Ljava/lang/Double;
/*     */     //   26: areturn
/*     */     //   27: iconst_0
/*     */     //   28: istore #5
/*     */     //   30: iconst_0
/*     */     //   31: istore #6
/*     */     //   33: aload_2
/*     */     //   34: iload #6
/*     */     //   36: invokevirtual charAt : (I)C
/*     */     //   39: istore #7
/*     */     //   41: iload #7
/*     */     //   43: invokestatic isStrWhiteSpaceChar : (I)Z
/*     */     //   46: ifne -> 52
/*     */     //   49: goto -> 62
/*     */     //   52: iinc #6, 1
/*     */     //   55: iload #6
/*     */     //   57: iload #4
/*     */     //   59: if_icmplt -> 33
/*     */     //   62: iload #7
/*     */     //   64: bipush #43
/*     */     //   66: if_icmpeq -> 87
/*     */     //   69: iload #7
/*     */     //   71: bipush #45
/*     */     //   73: if_icmpne -> 80
/*     */     //   76: iconst_1
/*     */     //   77: goto -> 81
/*     */     //   80: iconst_0
/*     */     //   81: dup
/*     */     //   82: istore #5
/*     */     //   84: ifeq -> 90
/*     */     //   87: iinc #6, 1
/*     */     //   90: iconst_m1
/*     */     //   91: istore #8
/*     */     //   93: iload_3
/*     */     //   94: ifne -> 102
/*     */     //   97: iconst_m1
/*     */     //   98: istore_3
/*     */     //   99: goto -> 170
/*     */     //   102: iload_3
/*     */     //   103: iconst_2
/*     */     //   104: if_icmplt -> 113
/*     */     //   107: iload_3
/*     */     //   108: bipush #36
/*     */     //   110: if_icmple -> 117
/*     */     //   113: getstatic org/mozilla/javascript/ScriptRuntime.NaNobj : Ljava/lang/Double;
/*     */     //   116: areturn
/*     */     //   117: iload_3
/*     */     //   118: bipush #16
/*     */     //   120: if_icmpne -> 170
/*     */     //   123: iload #4
/*     */     //   125: iload #6
/*     */     //   127: isub
/*     */     //   128: iconst_1
/*     */     //   129: if_icmple -> 170
/*     */     //   132: aload_2
/*     */     //   133: iload #6
/*     */     //   135: invokevirtual charAt : (I)C
/*     */     //   138: bipush #48
/*     */     //   140: if_icmpne -> 170
/*     */     //   143: aload_2
/*     */     //   144: iload #6
/*     */     //   146: iconst_1
/*     */     //   147: iadd
/*     */     //   148: invokevirtual charAt : (I)C
/*     */     //   151: istore #7
/*     */     //   153: iload #7
/*     */     //   155: bipush #120
/*     */     //   157: if_icmpeq -> 167
/*     */     //   160: iload #7
/*     */     //   162: bipush #88
/*     */     //   164: if_icmpne -> 170
/*     */     //   167: iinc #6, 2
/*     */     //   170: iload_3
/*     */     //   171: iconst_m1
/*     */     //   172: if_icmpne -> 251
/*     */     //   175: bipush #10
/*     */     //   177: istore_3
/*     */     //   178: iload #4
/*     */     //   180: iload #6
/*     */     //   182: isub
/*     */     //   183: iconst_1
/*     */     //   184: if_icmple -> 251
/*     */     //   187: aload_2
/*     */     //   188: iload #6
/*     */     //   190: invokevirtual charAt : (I)C
/*     */     //   193: bipush #48
/*     */     //   195: if_icmpne -> 251
/*     */     //   198: aload_2
/*     */     //   199: iload #6
/*     */     //   201: iconst_1
/*     */     //   202: iadd
/*     */     //   203: invokevirtual charAt : (I)C
/*     */     //   206: istore #7
/*     */     //   208: iload #7
/*     */     //   210: bipush #120
/*     */     //   212: if_icmpeq -> 222
/*     */     //   215: iload #7
/*     */     //   217: bipush #88
/*     */     //   219: if_icmpne -> 231
/*     */     //   222: bipush #16
/*     */     //   224: istore_3
/*     */     //   225: iinc #6, 2
/*     */     //   228: goto -> 251
/*     */     //   231: bipush #48
/*     */     //   233: iload #7
/*     */     //   235: if_icmpgt -> 251
/*     */     //   238: iload #7
/*     */     //   240: bipush #57
/*     */     //   242: if_icmpgt -> 251
/*     */     //   245: bipush #8
/*     */     //   247: istore_3
/*     */     //   248: iinc #6, 1
/*     */     //   251: aload_2
/*     */     //   252: iload #6
/*     */     //   254: iload_3
/*     */     //   255: invokestatic stringToNumber : (Ljava/lang/String;II)D
/*     */     //   258: dstore #9
/*     */     //   260: iload #5
/*     */     //   262: ifeq -> 271
/*     */     //   265: dload #9
/*     */     //   267: dneg
/*     */     //   268: goto -> 273
/*     */     //   271: dload #9
/*     */     //   273: invokestatic wrapNumber : (D)Ljava/lang/Number;
/*     */     //   276: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #213	-> 0
/*     */     //   #214	-> 6
/*     */     //   #216	-> 12
/*     */     //   #217	-> 18
/*     */     //   #218	-> 23
/*     */     //   #220	-> 27
/*     */     //   #221	-> 30
/*     */     //   #224	-> 33
/*     */     //   #225	-> 41
/*     */     //   #226	-> 49
/*     */     //   #227	-> 52
/*     */     //   #228	-> 55
/*     */     //   #230	-> 62
/*     */     //   #231	-> 87
/*     */     //   #233	-> 90
/*     */     //   #234	-> 93
/*     */     //   #235	-> 97
/*     */     //   #236	-> 102
/*     */     //   #237	-> 113
/*     */     //   #238	-> 117
/*     */     //   #239	-> 143
/*     */     //   #240	-> 153
/*     */     //   #241	-> 167
/*     */     //   #244	-> 170
/*     */     //   #245	-> 175
/*     */     //   #246	-> 178
/*     */     //   #247	-> 198
/*     */     //   #248	-> 208
/*     */     //   #249	-> 222
/*     */     //   #250	-> 225
/*     */     //   #251	-> 231
/*     */     //   #252	-> 245
/*     */     //   #253	-> 248
/*     */     //   #258	-> 251
/*     */     //   #259	-> 260
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	277	0	this	Lorg/mozilla/javascript/NativeGlobal;
/*     */     //   0	277	1	args	[Ljava/lang/Object;
/*     */     //   6	271	2	s	Ljava/lang/String;
/*     */     //   12	265	3	radix	I
/*     */     //   18	259	4	len	I
/*     */     //   30	247	5	negative	Z
/*     */     //   33	244	6	start	I
/*     */     //   41	236	7	c	C
/*     */     //   93	184	8	NO_RADIX	I
/*     */     //   260	17	9	d	D
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object js_parseFloat(Object[] args) {
/*     */     char c;
/* 269 */     if (args.length < 1) {
/* 270 */       return ScriptRuntime.NaNobj;
/*     */     }
/* 272 */     String s = ScriptRuntime.toString(args[0]);
/* 273 */     int len = s.length();
/* 274 */     int start = 0;
/*     */ 
/*     */     
/*     */     while (true) {
/* 278 */       if (start == len) {
/* 279 */         return ScriptRuntime.NaNobj;
/*     */       }
/* 281 */       c = s.charAt(start);
/* 282 */       if (!ScriptRuntime.isStrWhiteSpaceChar(c)) {
/*     */         break;
/*     */       }
/* 285 */       start++;
/*     */     } 
/*     */     
/* 288 */     int i = start;
/* 289 */     if (c == '+' || c == '-') {
/* 290 */       i++;
/* 291 */       if (i == len) {
/* 292 */         return ScriptRuntime.NaNobj;
/*     */       }
/* 294 */       c = s.charAt(i);
/*     */     } 
/*     */     
/* 297 */     if (c == 'I') {
/*     */       
/* 299 */       if (i + 8 <= len && s.regionMatches(i, "Infinity", 0, 8)) {
/*     */         double d;
/* 301 */         if (s.charAt(start) == '-') {
/* 302 */           d = Double.NEGATIVE_INFINITY;
/*     */         } else {
/* 304 */           d = Double.POSITIVE_INFINITY;
/*     */         } 
/* 306 */         return ScriptRuntime.wrapNumber(d);
/*     */       } 
/* 308 */       return ScriptRuntime.NaNobj;
/*     */     } 
/*     */ 
/*     */     
/* 312 */     int decimal = -1;
/* 313 */     int exponent = -1;
/* 314 */     boolean exponentValid = false;
/* 315 */     for (; i < len; i++) {
/* 316 */       switch (s.charAt(i)) {
/*     */         case '.':
/* 318 */           if (decimal != -1)
/*     */             break; 
/* 320 */           decimal = i;
/*     */           break;
/*     */         
/*     */         case 'E':
/*     */         case 'e':
/* 325 */           if (exponent != -1)
/*     */             break; 
/* 327 */           if (i == len - 1) {
/*     */             break;
/*     */           }
/* 330 */           exponent = i;
/*     */           break;
/*     */ 
/*     */         
/*     */         case '+':
/*     */         case '-':
/* 336 */           if (exponent != i - 1)
/*     */             break; 
/* 338 */           if (i == len - 1)
/* 339 */           { i--; break; }  break;
/*     */         case '0': case '1': case '2': case '3': case '4':
/*     */         case '5':
/*     */         case '6':
/*     */         case '7':
/*     */         case '8':
/*     */         case '9':
/* 346 */           if (exponent != -1) {
/* 347 */             exponentValid = true;
/*     */           }
/*     */           break;
/*     */         
/*     */         default:
/*     */           break;
/*     */       } 
/*     */     
/*     */     } 
/* 356 */     if (exponent != -1 && !exponentValid) {
/* 357 */       i = exponent;
/*     */     }
/* 359 */     s = s.substring(start, i);
/*     */     try {
/* 361 */       return Double.valueOf(s);
/*     */     }
/* 363 */     catch (NumberFormatException ex) {
/* 364 */       return ScriptRuntime.NaNobj;
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
/*     */   
/*     */   private Object js_escape(Object[] args) {
/* 378 */     int URL_XALPHAS = 1;
/* 379 */     int URL_XPALPHAS = 2;
/* 380 */     int URL_PATH = 4;
/*     */     
/* 382 */     String s = ScriptRuntime.toString(args, 0);
/*     */     
/* 384 */     int mask = 7;
/* 385 */     if (args.length > 1) {
/* 386 */       double d = ScriptRuntime.toNumber(args[1]);
/* 387 */       if (d != d || (mask = (int)d) != d || 0 != (mask & 0xFFFFFFF8))
/*     */       {
/*     */         
/* 390 */         throw Context.reportRuntimeError0("msg.bad.esc.mask");
/*     */       }
/*     */     } 
/*     */     
/* 394 */     StringBuilder sb = null;
/* 395 */     int k = 0, L = s.length(); while (true) { if (k != L) {
/* 396 */         int c = s.charAt(k);
/* 397 */         if (mask != 0 && ((c >= 48 && c <= 57) || (c >= 65 && c <= 90) || (c >= 97 && c <= 122) || c == 64 || c == 42 || c == 95 || c == 45 || c == 46 || (0 != (mask & 0x4) && (c == 47 || c == 43)))) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 403 */           if (sb != null) {
/* 404 */             sb.append((char)c);
/*     */           }
/*     */         } else {
/* 407 */           if (sb == null) {
/* 408 */             sb = new StringBuilder(L + 3);
/* 409 */             sb.append(s);
/* 410 */             sb.setLength(k);
/*     */           } 
/*     */ 
/*     */           
/* 414 */           if (c < 256)
/* 415 */           { if (c == 32 && mask == 2)
/* 416 */             { sb.append('+'); }
/*     */             else
/*     */             
/* 419 */             { sb.append('%');
/* 420 */               int hexSize = 2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 428 */               int shift = (hexSize - 1) * 4; }  } else { sb.append('%'); sb.append('u'); int hexSize = 4; int i = (hexSize - 1) * 4; }
/*     */         
/*     */         } 
/*     */       } else {
/*     */         break;
/*     */       } 
/*     */       k++; }
/*     */     
/* 436 */     return (sb == null) ? s : sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object js_unescape(Object[] args) {
/* 445 */     String s = ScriptRuntime.toString(args, 0);
/* 446 */     int firstEscapePos = s.indexOf('%');
/* 447 */     if (firstEscapePos >= 0) {
/* 448 */       int L = s.length();
/* 449 */       char[] buf = s.toCharArray();
/* 450 */       int destination = firstEscapePos;
/* 451 */       for (int k = firstEscapePos; k != L; ) {
/* 452 */         char c = buf[k];
/* 453 */         k++;
/* 454 */         if (c == '%' && k != L) {
/*     */           int end; int start;
/* 456 */           if (buf[k] == 'u') {
/* 457 */             start = k + 1;
/* 458 */             end = k + 5;
/*     */           } else {
/* 460 */             start = k;
/* 461 */             end = k + 2;
/*     */           } 
/* 463 */           if (end <= L) {
/* 464 */             int x = 0;
/* 465 */             for (int i = start; i != end; i++) {
/* 466 */               x = Kit.xDigitToInt(buf[i], x);
/*     */             }
/* 468 */             if (x >= 0) {
/* 469 */               c = (char)x;
/* 470 */               k = end;
/*     */             } 
/*     */           } 
/*     */         } 
/* 474 */         buf[destination] = c;
/* 475 */         destination++;
/*     */       } 
/* 477 */       s = new String(buf, 0, destination);
/*     */     } 
/* 479 */     return s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object js_eval(Context cx, Scriptable scope, Object[] args) {
/* 488 */     Scriptable global = ScriptableObject.getTopLevelScope(scope);
/* 489 */     return ScriptRuntime.evalSpecial(cx, global, global, args, "eval code", 1);
/*     */   }
/*     */ 
/*     */   
/*     */   static boolean isEvalFunction(Object functionObj) {
/* 494 */     if (functionObj instanceof IdFunctionObject) {
/* 495 */       IdFunctionObject function = (IdFunctionObject)functionObj;
/* 496 */       if (function.hasTag(FTAG) && function.methodId() == 6) {
/* 497 */         return true;
/*     */       }
/*     */     } 
/* 500 */     return false;
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
/*     */   @Deprecated
/*     */   public static EcmaError constructError(Context cx, String error, String message, Scriptable scope) {
/* 513 */     return ScriptRuntime.constructError(error, message);
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
/*     */   @Deprecated
/*     */   public static EcmaError constructError(Context cx, String error, String message, Scriptable scope, String sourceName, int lineNumber, int columnNumber, String lineSource) {
/* 531 */     return ScriptRuntime.constructError(error, message, sourceName, lineNumber, lineSource, columnNumber);
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
/*     */   private static String encode(String str, boolean fullUri) {
/* 544 */     byte[] utf8buf = null;
/* 545 */     StringBuilder sb = null;
/*     */     
/* 547 */     for (int k = 0, length = str.length(); k != length; k++) {
/* 548 */       char C = str.charAt(k);
/* 549 */       if (encodeUnescaped(C, fullUri)) {
/* 550 */         if (sb != null)
/* 551 */           sb.append(C); 
/*     */       } else {
/*     */         int V;
/* 554 */         if (sb == null) {
/* 555 */           sb = new StringBuilder(length + 3);
/* 556 */           sb.append(str);
/* 557 */           sb.setLength(k);
/* 558 */           utf8buf = new byte[6];
/*     */         } 
/* 560 */         if ('?' <= C && C <= '?') {
/* 561 */           throw uriError();
/*     */         }
/*     */         
/* 564 */         if (C < '?' || '?' < C) {
/* 565 */           V = C;
/*     */         } else {
/* 567 */           k++;
/* 568 */           if (k == length) {
/* 569 */             throw uriError();
/*     */           }
/* 571 */           char C2 = str.charAt(k);
/* 572 */           if ('?' > C2 || C2 > '?') {
/* 573 */             throw uriError();
/*     */           }
/* 575 */           V = (C - 55296 << 10) + C2 - 56320 + 65536;
/*     */         } 
/* 577 */         int L = oneUcs4ToUtf8Char(utf8buf, V);
/* 578 */         for (int j = 0; j < L; j++) {
/* 579 */           int d = 0xFF & utf8buf[j];
/* 580 */           sb.append('%');
/* 581 */           sb.append(toHexChar(d >>> 4));
/* 582 */           sb.append(toHexChar(d & 0xF));
/*     */         } 
/*     */       } 
/*     */     } 
/* 586 */     return (sb == null) ? str : sb.toString();
/*     */   }
/*     */   
/*     */   private static char toHexChar(int i) {
/* 590 */     if (i >> 4 != 0) Kit.codeBug(); 
/* 591 */     return (char)((i < 10) ? (i + 48) : (i - 10 + 65));
/*     */   }
/*     */   
/*     */   private static int unHex(char c) {
/* 595 */     if ('A' <= c && c <= 'F')
/* 596 */       return c - 65 + 10; 
/* 597 */     if ('a' <= c && c <= 'f')
/* 598 */       return c - 97 + 10; 
/* 599 */     if ('0' <= c && c <= '9') {
/* 600 */       return c - 48;
/*     */     }
/* 602 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int unHex(char c1, char c2) {
/* 607 */     int i1 = unHex(c1);
/* 608 */     int i2 = unHex(c2);
/* 609 */     if (i1 >= 0 && i2 >= 0) {
/* 610 */       return i1 << 4 | i2;
/*     */     }
/* 612 */     return -1;
/*     */   }
/*     */   
/*     */   private static String decode(String str, boolean fullUri) {
/* 616 */     char[] buf = null;
/* 617 */     int bufTop = 0;
/*     */     
/* 619 */     for (int k = 0, length = str.length(); k != length; ) {
/* 620 */       char C = str.charAt(k);
/* 621 */       if (C != '%') {
/* 622 */         if (buf != null) {
/* 623 */           buf[bufTop++] = C;
/*     */         }
/* 625 */         k++; continue;
/*     */       } 
/* 627 */       if (buf == null) {
/*     */ 
/*     */         
/* 630 */         buf = new char[length];
/* 631 */         str.getChars(0, k, buf, 0);
/* 632 */         bufTop = k;
/*     */       } 
/* 634 */       int start = k;
/* 635 */       if (k + 3 > length)
/* 636 */         throw uriError(); 
/* 637 */       int B = unHex(str.charAt(k + 1), str.charAt(k + 2));
/* 638 */       if (B < 0) throw uriError(); 
/* 639 */       k += 3;
/* 640 */       if ((B & 0x80) == 0) {
/* 641 */         C = (char)B;
/*     */       } else {
/*     */         int utf8Tail;
/*     */         int ucs4Char;
/*     */         int minUcs4Char;
/* 646 */         if ((B & 0xC0) == 128)
/*     */         {
/* 648 */           throw uriError(); } 
/* 649 */         if ((B & 0x20) == 0) {
/* 650 */           utf8Tail = 1; ucs4Char = B & 0x1F;
/* 651 */           minUcs4Char = 128;
/* 652 */         } else if ((B & 0x10) == 0) {
/* 653 */           utf8Tail = 2; ucs4Char = B & 0xF;
/* 654 */           minUcs4Char = 2048;
/* 655 */         } else if ((B & 0x8) == 0) {
/* 656 */           utf8Tail = 3; ucs4Char = B & 0x7;
/* 657 */           minUcs4Char = 65536;
/* 658 */         } else if ((B & 0x4) == 0) {
/* 659 */           utf8Tail = 4; ucs4Char = B & 0x3;
/* 660 */           minUcs4Char = 2097152;
/* 661 */         } else if ((B & 0x2) == 0) {
/* 662 */           utf8Tail = 5; ucs4Char = B & 0x1;
/* 663 */           minUcs4Char = 67108864;
/*     */         } else {
/*     */           
/* 666 */           throw uriError();
/*     */         } 
/* 668 */         if (k + 3 * utf8Tail > length)
/* 669 */           throw uriError(); 
/* 670 */         for (int j = 0; j != utf8Tail; j++) {
/* 671 */           if (str.charAt(k) != '%')
/* 672 */             throw uriError(); 
/* 673 */           B = unHex(str.charAt(k + 1), str.charAt(k + 2));
/* 674 */           if (B < 0 || (B & 0xC0) != 128)
/* 675 */             throw uriError(); 
/* 676 */           ucs4Char = ucs4Char << 6 | B & 0x3F;
/* 677 */           k += 3;
/*     */         } 
/*     */         
/* 680 */         if (ucs4Char < minUcs4Char || (ucs4Char >= 55296 && ucs4Char <= 57343)) {
/*     */           
/* 682 */           ucs4Char = Integer.MAX_VALUE;
/* 683 */         } else if (ucs4Char == 65534 || ucs4Char == 65535) {
/* 684 */           ucs4Char = 65533;
/*     */         } 
/* 686 */         if (ucs4Char >= 65536) {
/* 687 */           ucs4Char -= 65536;
/* 688 */           if (ucs4Char > 1048575) {
/* 689 */             throw uriError();
/*     */           }
/* 691 */           char H = (char)((ucs4Char >>> 10) + 55296);
/* 692 */           C = (char)((ucs4Char & 0x3FF) + 56320);
/* 693 */           buf[bufTop++] = H;
/*     */         } else {
/* 695 */           C = (char)ucs4Char;
/*     */         } 
/*     */       } 
/* 698 */       if (fullUri && ";/?:@&=+$,#".indexOf(C) >= 0) {
/* 699 */         for (int x = start; x != k; x++)
/* 700 */           buf[bufTop++] = str.charAt(x); 
/*     */         continue;
/*     */       } 
/* 703 */       buf[bufTop++] = C;
/*     */     } 
/*     */ 
/*     */     
/* 707 */     return (buf == null) ? str : new String(buf, 0, bufTop);
/*     */   }
/*     */   
/*     */   private static boolean encodeUnescaped(char c, boolean fullUri) {
/* 711 */     if (('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z') || ('0' <= c && c <= '9'))
/*     */     {
/* 713 */       return true;
/*     */     }
/* 715 */     if ("-_.!~*'()".indexOf(c) >= 0) {
/* 716 */       return true;
/*     */     }
/* 718 */     if (fullUri) {
/* 719 */       return (";/?:@&=+$,#".indexOf(c) >= 0);
/*     */     }
/* 721 */     return false;
/*     */   }
/*     */   
/*     */   private static EcmaError uriError() {
/* 725 */     return ScriptRuntime.constructError("URIError", ScriptRuntime.getMessage0("msg.bad.uri"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int oneUcs4ToUtf8Char(byte[] utf8Buffer, int ucs4Char) {
/* 736 */     int utf8Length = 1;
/*     */ 
/*     */     
/* 739 */     if ((ucs4Char & 0xFFFFFF80) == 0) {
/* 740 */       utf8Buffer[0] = (byte)ucs4Char;
/*     */     } else {
/*     */       
/* 743 */       int a = ucs4Char >>> 11;
/* 744 */       utf8Length = 2;
/* 745 */       while (a != 0) {
/* 746 */         a >>>= 5;
/* 747 */         utf8Length++;
/*     */       } 
/* 749 */       int i = utf8Length;
/* 750 */       while (--i > 0) {
/* 751 */         utf8Buffer[i] = (byte)(ucs4Char & 0x3F | 0x80);
/* 752 */         ucs4Char >>>= 6;
/*     */       } 
/* 754 */       utf8Buffer[0] = (byte)(256 - (1 << 8 - utf8Length) + ucs4Char);
/*     */     } 
/* 756 */     return utf8Length;
/*     */   }
/*     */   
/* 759 */   private static final Object FTAG = "Global";
/*     */   private static final int Id_decodeURI = 1;
/*     */   private static final int Id_decodeURIComponent = 2;
/*     */   private static final int Id_encodeURI = 3;
/*     */   private static final int Id_encodeURIComponent = 4;
/*     */   private static final int Id_escape = 5;
/*     */   private static final int Id_eval = 6;
/*     */   private static final int Id_isFinite = 7;
/*     */   private static final int Id_isNaN = 8;
/*     */   private static final int Id_isXMLName = 9;
/*     */   private static final int Id_parseFloat = 10;
/*     */   private static final int Id_parseInt = 11;
/*     */   private static final int Id_unescape = 12;
/*     */   private static final int Id_uneval = 13;
/*     */   private static final int LAST_SCOPE_FUNCTION_ID = 13;
/*     */   private static final int Id_new_CommonError = 14;
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\NativeGlobal.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */