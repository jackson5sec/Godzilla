/*      */ package org.mozilla.javascript;
/*      */ 
/*      */ import java.text.DateFormat;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.Date;
/*      */ import java.util.TimeZone;
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
/*      */ final class NativeDate
/*      */   extends IdScriptableObject
/*      */ {
/*      */   static final long serialVersionUID = -8307438915861678966L;
/*   24 */   private static final Object DATE_TAG = "Date"; private static final String js_NaN_date_str = "Invalid Date"; private static final double HalfTimeDomain = 8.64E15D; private static final double HoursPerDay = 24.0D; private static final double MinutesPerHour = 60.0D; private static final double SecondsPerMinute = 60.0D; private static final double msPerSecond = 1000.0D; private static final double MinutesPerDay = 1440.0D; private static final double SecondsPerDay = 86400.0D; private static final double SecondsPerHour = 3600.0D; private static final double msPerDay = 8.64E7D; private static final double msPerHour = 3600000.0D; private static final double msPerMinute = 60000.0D; private static final int MAXARGS = 7; private static final int ConstructorId_now = -3; private static final int ConstructorId_parse = -2; private static final int ConstructorId_UTC = -1; private static final int Id_constructor = 1; private static final int Id_toString = 2; private static final int Id_toTimeString = 3; private static final int Id_toDateString = 4; private static final int Id_toLocaleString = 5; private static final int Id_toLocaleTimeString = 6; private static final int Id_toLocaleDateString = 7; private static final int Id_toUTCString = 8; private static final int Id_toSource = 9; private static final int Id_valueOf = 10; private static final int Id_getTime = 11; private static final int Id_getYear = 12; private static final int Id_getFullYear = 13; private static final int Id_getUTCFullYear = 14; private static final int Id_getMonth = 15; private static final int Id_getUTCMonth = 16; private static final int Id_getDate = 17;
/*      */   private static final int Id_getUTCDate = 18;
/*      */   private static final int Id_getDay = 19;
/*      */   private static final int Id_getUTCDay = 20;
/*      */   
/*      */   static void init(Scriptable scope, boolean sealed) {
/*   30 */     NativeDate obj = new NativeDate();
/*      */     
/*   32 */     obj.date = ScriptRuntime.NaN;
/*   33 */     obj.exportAsJSClass(47, scope, sealed);
/*      */   }
/*      */   private static final int Id_getHours = 21; private static final int Id_getUTCHours = 22; private static final int Id_getMinutes = 23; private static final int Id_getUTCMinutes = 24; private static final int Id_getSeconds = 25; private static final int Id_getUTCSeconds = 26; private static final int Id_getMilliseconds = 27; private static final int Id_getUTCMilliseconds = 28; private static final int Id_getTimezoneOffset = 29; private static final int Id_setTime = 30; private static final int Id_setMilliseconds = 31; private static final int Id_setUTCMilliseconds = 32; private static final int Id_setSeconds = 33; private static final int Id_setUTCSeconds = 34; private static final int Id_setMinutes = 35; private static final int Id_setUTCMinutes = 36; private static final int Id_setHours = 37; private static final int Id_setUTCHours = 38; private static final int Id_setDate = 39; private static final int Id_setUTCDate = 40; private static final int Id_setMonth = 41; private static final int Id_setUTCMonth = 42; private static final int Id_setFullYear = 43; private static final int Id_setUTCFullYear = 44; private static final int Id_setYear = 45; private static final int Id_toISOString = 46; private static final int Id_toJSON = 47; private static final int MAX_PROTOTYPE_ID = 47; private static final int Id_toGMTString = 8; private static TimeZone thisTimeZone; private static double LocalTZA; private static DateFormat timeZoneFormatter; private static DateFormat localeDateTimeFormatter; private static DateFormat localeDateFormatter; private static DateFormat localeTimeFormatter; private double date;
/*      */   
/*      */   private NativeDate() {
/*   38 */     if (thisTimeZone == null) {
/*      */ 
/*      */       
/*   41 */       thisTimeZone = TimeZone.getDefault();
/*   42 */       LocalTZA = thisTimeZone.getRawOffset();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String getClassName() {
/*   49 */     return "Date";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getDefaultValue(Class<?> typeHint) {
/*   55 */     if (typeHint == null)
/*   56 */       typeHint = ScriptRuntime.StringClass; 
/*   57 */     return super.getDefaultValue(typeHint);
/*      */   }
/*      */ 
/*      */   
/*      */   double getJSTimeValue() {
/*   62 */     return this.date;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void fillConstructorProperties(IdFunctionObject ctor) {
/*   68 */     addIdFunctionProperty(ctor, DATE_TAG, -3, "now", 0);
/*      */     
/*   70 */     addIdFunctionProperty(ctor, DATE_TAG, -2, "parse", 1);
/*      */     
/*   72 */     addIdFunctionProperty(ctor, DATE_TAG, -1, "UTC", 7);
/*      */     
/*   74 */     super.fillConstructorProperties(ctor);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void initPrototypeId(int id)
/*      */   {
/*      */     String s;
/*      */     int arity;
/*   82 */     switch (id) { case 1:
/*   83 */         arity = 7; s = "constructor"; break;
/*   84 */       case 2: arity = 0; s = "toString"; break;
/*   85 */       case 3: arity = 0; s = "toTimeString"; break;
/*   86 */       case 4: arity = 0; s = "toDateString"; break;
/*   87 */       case 5: arity = 0; s = "toLocaleString"; break;
/*   88 */       case 6: arity = 0; s = "toLocaleTimeString"; break;
/*   89 */       case 7: arity = 0; s = "toLocaleDateString"; break;
/*   90 */       case 8: arity = 0; s = "toUTCString"; break;
/*   91 */       case 9: arity = 0; s = "toSource"; break;
/*   92 */       case 10: arity = 0; s = "valueOf"; break;
/*   93 */       case 11: arity = 0; s = "getTime"; break;
/*   94 */       case 12: arity = 0; s = "getYear"; break;
/*   95 */       case 13: arity = 0; s = "getFullYear"; break;
/*   96 */       case 14: arity = 0; s = "getUTCFullYear"; break;
/*   97 */       case 15: arity = 0; s = "getMonth"; break;
/*   98 */       case 16: arity = 0; s = "getUTCMonth"; break;
/*   99 */       case 17: arity = 0; s = "getDate"; break;
/*  100 */       case 18: arity = 0; s = "getUTCDate"; break;
/*  101 */       case 19: arity = 0; s = "getDay"; break;
/*  102 */       case 20: arity = 0; s = "getUTCDay"; break;
/*  103 */       case 21: arity = 0; s = "getHours"; break;
/*  104 */       case 22: arity = 0; s = "getUTCHours"; break;
/*  105 */       case 23: arity = 0; s = "getMinutes"; break;
/*  106 */       case 24: arity = 0; s = "getUTCMinutes"; break;
/*  107 */       case 25: arity = 0; s = "getSeconds"; break;
/*  108 */       case 26: arity = 0; s = "getUTCSeconds"; break;
/*  109 */       case 27: arity = 0; s = "getMilliseconds"; break;
/*  110 */       case 28: arity = 0; s = "getUTCMilliseconds"; break;
/*  111 */       case 29: arity = 0; s = "getTimezoneOffset"; break;
/*  112 */       case 30: arity = 1; s = "setTime"; break;
/*  113 */       case 31: arity = 1; s = "setMilliseconds"; break;
/*  114 */       case 32: arity = 1; s = "setUTCMilliseconds"; break;
/*  115 */       case 33: arity = 2; s = "setSeconds"; break;
/*  116 */       case 34: arity = 2; s = "setUTCSeconds"; break;
/*  117 */       case 35: arity = 3; s = "setMinutes"; break;
/*  118 */       case 36: arity = 3; s = "setUTCMinutes"; break;
/*  119 */       case 37: arity = 4; s = "setHours"; break;
/*  120 */       case 38: arity = 4; s = "setUTCHours"; break;
/*  121 */       case 39: arity = 1; s = "setDate"; break;
/*  122 */       case 40: arity = 1; s = "setUTCDate"; break;
/*  123 */       case 41: arity = 2; s = "setMonth"; break;
/*  124 */       case 42: arity = 2; s = "setUTCMonth"; break;
/*  125 */       case 43: arity = 3; s = "setFullYear"; break;
/*  126 */       case 44: arity = 3; s = "setUTCFullYear"; break;
/*  127 */       case 45: arity = 1; s = "setYear"; break;
/*  128 */       case 46: arity = 0; s = "toISOString"; break;
/*  129 */       case 47: arity = 1; s = "toJSON"; break;
/*  130 */       default: throw new IllegalArgumentException(String.valueOf(id)); }
/*      */     
/*  132 */     initPrototypeMethod(DATE_TAG, id, s, arity); } public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/*      */     String dataStr, toISOString;
/*      */     Scriptable o;
/*      */     Object tv, toISO;
/*      */     double year;
/*      */     String msg;
/*      */     Object result;
/*  139 */     if (!f.hasTag(DATE_TAG)) {
/*  140 */       return super.execIdCall(f, cx, scope, thisObj, args);
/*      */     }
/*  142 */     int id = f.methodId();
/*  143 */     switch (id) {
/*      */       case -3:
/*  145 */         return ScriptRuntime.wrapNumber(now());
/*      */ 
/*      */       
/*      */       case -2:
/*  149 */         dataStr = ScriptRuntime.toString(args, 0);
/*  150 */         return ScriptRuntime.wrapNumber(date_parseString(dataStr));
/*      */ 
/*      */       
/*      */       case -1:
/*  154 */         return ScriptRuntime.wrapNumber(jsStaticFunction_UTC(args));
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 1:
/*  160 */         if (thisObj != null)
/*  161 */           return date_format(now(), 2); 
/*  162 */         return jsConstructor(args);
/*      */ 
/*      */ 
/*      */       
/*      */       case 47:
/*  167 */         toISOString = "toISOString";
/*      */         
/*  169 */         o = ScriptRuntime.toObject(cx, scope, thisObj);
/*  170 */         tv = ScriptRuntime.toPrimitive(o, ScriptRuntime.NumberClass);
/*  171 */         if (tv instanceof Number) {
/*  172 */           double d = ((Number)tv).doubleValue();
/*  173 */           if (d != d || Double.isInfinite(d)) {
/*  174 */             return null;
/*      */           }
/*      */         } 
/*  177 */         toISO = ScriptableObject.getProperty(o, "toISOString");
/*  178 */         if (toISO == NOT_FOUND) {
/*  179 */           throw ScriptRuntime.typeError2("msg.function.not.found.in", "toISOString", ScriptRuntime.toString(o));
/*      */         }
/*      */ 
/*      */         
/*  183 */         if (!(toISO instanceof Callable)) {
/*  184 */           throw ScriptRuntime.typeError3("msg.isnt.function.in", "toISOString", ScriptRuntime.toString(o), ScriptRuntime.toString(toISO));
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  189 */         result = ((Callable)toISO).call(cx, scope, o, ScriptRuntime.emptyArgs);
/*      */         
/*  191 */         if (!ScriptRuntime.isPrimitive(result)) {
/*  192 */           throw ScriptRuntime.typeError1("msg.toisostring.must.return.primitive", ScriptRuntime.toString(result));
/*      */         }
/*      */         
/*  195 */         return result;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  202 */     if (!(thisObj instanceof NativeDate))
/*  203 */       throw incompatibleCallError(f); 
/*  204 */     NativeDate realThis = (NativeDate)thisObj;
/*  205 */     double t = realThis.date;
/*      */     
/*  207 */     switch (id) {
/*      */       
/*      */       case 2:
/*      */       case 3:
/*      */       case 4:
/*  212 */         if (t == t) {
/*  213 */           return date_format(t, id);
/*      */         }
/*  215 */         return "Invalid Date";
/*      */       
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/*  220 */         if (t == t) {
/*  221 */           return toLocale_helper(t, id);
/*      */         }
/*  223 */         return "Invalid Date";
/*      */       
/*      */       case 8:
/*  226 */         if (t == t) {
/*  227 */           return js_toUTCString(t);
/*      */         }
/*  229 */         return "Invalid Date";
/*      */       
/*      */       case 9:
/*  232 */         return "(new Date(" + ScriptRuntime.toString(t) + "))";
/*      */       
/*      */       case 10:
/*      */       case 11:
/*  236 */         return ScriptRuntime.wrapNumber(t);
/*      */       
/*      */       case 12:
/*      */       case 13:
/*      */       case 14:
/*  241 */         if (t == t) {
/*  242 */           if (id != 14) t = LocalTime(t); 
/*  243 */           t = YearFromTime(t);
/*  244 */           if (id == 12) {
/*  245 */             if (cx.hasFeature(1)) {
/*  246 */               if (1900.0D <= t && t < 2000.0D) {
/*  247 */                 t -= 1900.0D;
/*      */               }
/*      */             } else {
/*  250 */               t -= 1900.0D;
/*      */             } 
/*      */           }
/*      */         } 
/*  254 */         return ScriptRuntime.wrapNumber(t);
/*      */       
/*      */       case 15:
/*      */       case 16:
/*  258 */         if (t == t) {
/*  259 */           if (id == 15) t = LocalTime(t); 
/*  260 */           t = MonthFromTime(t);
/*      */         } 
/*  262 */         return ScriptRuntime.wrapNumber(t);
/*      */       
/*      */       case 17:
/*      */       case 18:
/*  266 */         if (t == t) {
/*  267 */           if (id == 17) t = LocalTime(t); 
/*  268 */           t = DateFromTime(t);
/*      */         } 
/*  270 */         return ScriptRuntime.wrapNumber(t);
/*      */       
/*      */       case 19:
/*      */       case 20:
/*  274 */         if (t == t) {
/*  275 */           if (id == 19) t = LocalTime(t); 
/*  276 */           t = WeekDay(t);
/*      */         } 
/*  278 */         return ScriptRuntime.wrapNumber(t);
/*      */       
/*      */       case 21:
/*      */       case 22:
/*  282 */         if (t == t) {
/*  283 */           if (id == 21) t = LocalTime(t); 
/*  284 */           t = HourFromTime(t);
/*      */         } 
/*  286 */         return ScriptRuntime.wrapNumber(t);
/*      */       
/*      */       case 23:
/*      */       case 24:
/*  290 */         if (t == t) {
/*  291 */           if (id == 23) t = LocalTime(t); 
/*  292 */           t = MinFromTime(t);
/*      */         } 
/*  294 */         return ScriptRuntime.wrapNumber(t);
/*      */       
/*      */       case 25:
/*      */       case 26:
/*  298 */         if (t == t) {
/*  299 */           if (id == 25) t = LocalTime(t); 
/*  300 */           t = SecFromTime(t);
/*      */         } 
/*  302 */         return ScriptRuntime.wrapNumber(t);
/*      */       
/*      */       case 27:
/*      */       case 28:
/*  306 */         if (t == t) {
/*  307 */           if (id == 27) t = LocalTime(t); 
/*  308 */           t = msFromTime(t);
/*      */         } 
/*  310 */         return ScriptRuntime.wrapNumber(t);
/*      */       
/*      */       case 29:
/*  313 */         if (t == t) {
/*  314 */           t = (t - LocalTime(t)) / 60000.0D;
/*      */         }
/*  316 */         return ScriptRuntime.wrapNumber(t);
/*      */       
/*      */       case 30:
/*  319 */         t = TimeClip(ScriptRuntime.toNumber(args, 0));
/*  320 */         realThis.date = t;
/*  321 */         return ScriptRuntime.wrapNumber(t);
/*      */       
/*      */       case 31:
/*      */       case 32:
/*      */       case 33:
/*      */       case 34:
/*      */       case 35:
/*      */       case 36:
/*      */       case 37:
/*      */       case 38:
/*  331 */         t = makeTime(t, args, id);
/*  332 */         realThis.date = t;
/*  333 */         return ScriptRuntime.wrapNumber(t);
/*      */       
/*      */       case 39:
/*      */       case 40:
/*      */       case 41:
/*      */       case 42:
/*      */       case 43:
/*      */       case 44:
/*  341 */         t = makeDate(t, args, id);
/*  342 */         realThis.date = t;
/*  343 */         return ScriptRuntime.wrapNumber(t);
/*      */ 
/*      */       
/*      */       case 45:
/*  347 */         year = ScriptRuntime.toNumber(args, 0);
/*      */         
/*  349 */         if (year != year || Double.isInfinite(year)) {
/*  350 */           t = ScriptRuntime.NaN;
/*      */         } else {
/*  352 */           if (t != t) {
/*  353 */             t = 0.0D;
/*      */           } else {
/*  355 */             t = LocalTime(t);
/*      */           } 
/*      */           
/*  358 */           if (year >= 0.0D && year <= 99.0D) {
/*  359 */             year += 1900.0D;
/*      */           }
/*  361 */           double day = MakeDay(year, MonthFromTime(t), DateFromTime(t));
/*      */           
/*  363 */           t = MakeDate(day, TimeWithinDay(t));
/*  364 */           t = internalUTC(t);
/*  365 */           t = TimeClip(t);
/*      */         } 
/*      */         
/*  368 */         realThis.date = t;
/*  369 */         return ScriptRuntime.wrapNumber(t);
/*      */       
/*      */       case 46:
/*  372 */         if (t == t) {
/*  373 */           return js_toISOString(t);
/*      */         }
/*  375 */         msg = ScriptRuntime.getMessage0("msg.invalid.date");
/*  376 */         throw ScriptRuntime.constructError("RangeError", msg);
/*      */     } 
/*  378 */     throw new IllegalArgumentException(String.valueOf(id));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static double Day(double t) {
/*  399 */     return Math.floor(t / 8.64E7D);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static double TimeWithinDay(double t) {
/*  405 */     double result = t % 8.64E7D;
/*  406 */     if (result < 0.0D)
/*  407 */       result += 8.64E7D; 
/*  408 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean IsLeapYear(int year) {
/*  413 */     return (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static double DayFromYear(double y) {
/*  421 */     return 365.0D * (y - 1970.0D) + Math.floor((y - 1969.0D) / 4.0D) - Math.floor((y - 1901.0D) / 100.0D) + Math.floor((y - 1601.0D) / 400.0D);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static double TimeFromYear(double y) {
/*  427 */     return DayFromYear(y) * 8.64E7D;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int YearFromTime(double t) {
/*  432 */     int lo = (int)Math.floor(t / 8.64E7D / 366.0D) + 1970;
/*  433 */     int hi = (int)Math.floor(t / 8.64E7D / 365.0D) + 1970;
/*      */ 
/*      */ 
/*      */     
/*  437 */     if (hi < lo) {
/*  438 */       int temp = lo;
/*  439 */       lo = hi;
/*  440 */       hi = temp;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  449 */     while (hi > lo) {
/*  450 */       int mid = (hi + lo) / 2;
/*  451 */       if (TimeFromYear(mid) > t) {
/*  452 */         hi = mid - 1; continue;
/*      */       } 
/*  454 */       lo = mid + 1;
/*  455 */       if (TimeFromYear(lo) > t) {
/*  456 */         return mid;
/*      */       }
/*      */     } 
/*      */     
/*  460 */     return lo;
/*      */   }
/*      */ 
/*      */   
/*      */   private static double DayFromMonth(int m, int year) {
/*  465 */     int day = m * 30;
/*      */     
/*  467 */     if (m >= 7) { day += m / 2 - 1; }
/*  468 */     else if (m >= 2) { day += (m - 1) / 2 - 1; }
/*  469 */     else { day += m; }
/*      */     
/*  471 */     if (m >= 2 && IsLeapYear(year)) day++;
/*      */     
/*  473 */     return day;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static int DaysInMonth(int year, int month) {
/*  479 */     if (month == 2)
/*  480 */       return IsLeapYear(year) ? 29 : 28; 
/*  481 */     return (month >= 8) ? (31 - (month & 0x1)) : (30 + (month & 0x1));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int MonthFromTime(double t) {
/*  488 */     int mstart, year = YearFromTime(t);
/*  489 */     int d = (int)(Day(t) - DayFromYear(year));
/*      */     
/*  491 */     d -= 59;
/*  492 */     if (d < 0) {
/*  493 */       return (d < -28) ? 0 : 1;
/*      */     }
/*      */     
/*  496 */     if (IsLeapYear(year)) {
/*  497 */       if (d == 0)
/*  498 */         return 1; 
/*  499 */       d--;
/*      */     } 
/*      */ 
/*      */     
/*  503 */     int estimate = d / 30;
/*      */     
/*  505 */     switch (estimate) { case 0:
/*  506 */         return 2;
/*  507 */       case 1: mstart = 31; break;
/*  508 */       case 2: mstart = 61; break;
/*  509 */       case 3: mstart = 92; break;
/*  510 */       case 4: mstart = 122; break;
/*  511 */       case 5: mstart = 153; break;
/*  512 */       case 6: mstart = 184; break;
/*  513 */       case 7: mstart = 214; break;
/*  514 */       case 8: mstart = 245; break;
/*  515 */       case 9: mstart = 275; break;
/*  516 */       case 10: return 11;
/*  517 */       default: throw Kit.codeBug(); }
/*      */ 
/*      */     
/*  520 */     return (d >= mstart) ? (estimate + 2) : (estimate + 1);
/*      */   }
/*      */ 
/*      */   
/*      */   private static int DateFromTime(double t) {
/*  525 */     int mdays, mstart, year = YearFromTime(t);
/*  526 */     int d = (int)(Day(t) - DayFromYear(year));
/*      */     
/*  528 */     d -= 59;
/*  529 */     if (d < 0) {
/*  530 */       return (d < -28) ? (d + 31 + 28 + 1) : (d + 28 + 1);
/*      */     }
/*      */     
/*  533 */     if (IsLeapYear(year)) {
/*  534 */       if (d == 0)
/*  535 */         return 29; 
/*  536 */       d--;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  541 */     switch (d / 30) { case 0:
/*  542 */         return d + 1;
/*  543 */       case 1: mdays = 31; mstart = 31; break;
/*  544 */       case 2: mdays = 30; mstart = 61; break;
/*  545 */       case 3: mdays = 31; mstart = 92; break;
/*  546 */       case 4: mdays = 30; mstart = 122; break;
/*  547 */       case 5: mdays = 31; mstart = 153; break;
/*  548 */       case 6: mdays = 31; mstart = 184; break;
/*  549 */       case 7: mdays = 30; mstart = 214; break;
/*  550 */       case 8: mdays = 31; mstart = 245; break;
/*  551 */       case 9: mdays = 30; mstart = 275; break;
/*      */       case 10:
/*  553 */         return d - 275 + 1;
/*  554 */       default: throw Kit.codeBug(); }
/*      */     
/*  556 */     d -= mstart;
/*  557 */     if (d < 0)
/*      */     {
/*  559 */       d += mdays;
/*      */     }
/*  561 */     return d + 1;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static int WeekDay(double t) {
/*  567 */     double result = Day(t) + 4.0D;
/*  568 */     result %= 7.0D;
/*  569 */     if (result < 0.0D)
/*  570 */       result += 7.0D; 
/*  571 */     return (int)result;
/*      */   }
/*      */ 
/*      */   
/*      */   private static double now() {
/*  576 */     return System.currentTimeMillis();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static double DaylightSavingTA(double t) {
/*  585 */     if (t < 0.0D) {
/*  586 */       int year = EquivalentYear(YearFromTime(t));
/*  587 */       double day = MakeDay(year, MonthFromTime(t), DateFromTime(t));
/*  588 */       t = MakeDate(day, TimeWithinDay(t));
/*      */     } 
/*  590 */     Date date = new Date((long)t);
/*  591 */     if (thisTimeZone.inDaylightTime(date)) {
/*  592 */       return 3600000.0D;
/*      */     }
/*  594 */     return 0.0D;
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
/*      */   private static int EquivalentYear(int year) {
/*  606 */     int day = (int)DayFromYear(year) + 4;
/*  607 */     day %= 7;
/*  608 */     if (day < 0) {
/*  609 */       day += 7;
/*      */     }
/*  611 */     if (IsLeapYear(year)) {
/*  612 */       switch (day) { case 0:
/*  613 */           return 1984;
/*  614 */         case 1: return 1996;
/*  615 */         case 2: return 1980;
/*  616 */         case 3: return 1992;
/*  617 */         case 4: return 1976;
/*  618 */         case 5: return 1988;
/*  619 */         case 6: return 1972; }
/*      */     
/*      */     } else {
/*  622 */       switch (day) { case 0:
/*  623 */           return 1978;
/*  624 */         case 1: return 1973;
/*  625 */         case 2: return 1985;
/*  626 */         case 3: return 1986;
/*  627 */         case 4: return 1981;
/*  628 */         case 5: return 1971;
/*  629 */         case 6: return 1977; }
/*      */ 
/*      */     
/*      */     } 
/*  633 */     throw Kit.codeBug();
/*      */   }
/*      */ 
/*      */   
/*      */   private static double LocalTime(double t) {
/*  638 */     return t + LocalTZA + DaylightSavingTA(t);
/*      */   }
/*      */ 
/*      */   
/*      */   private static double internalUTC(double t) {
/*  643 */     return t - LocalTZA - DaylightSavingTA(t - LocalTZA);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static int HourFromTime(double t) {
/*  649 */     double result = Math.floor(t / 3600000.0D) % 24.0D;
/*  650 */     if (result < 0.0D)
/*  651 */       result += 24.0D; 
/*  652 */     return (int)result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static int MinFromTime(double t) {
/*  658 */     double result = Math.floor(t / 60000.0D) % 60.0D;
/*  659 */     if (result < 0.0D)
/*  660 */       result += 60.0D; 
/*  661 */     return (int)result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static int SecFromTime(double t) {
/*  667 */     double result = Math.floor(t / 1000.0D) % 60.0D;
/*  668 */     if (result < 0.0D)
/*  669 */       result += 60.0D; 
/*  670 */     return (int)result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static int msFromTime(double t) {
/*  676 */     double result = t % 1000.0D;
/*  677 */     if (result < 0.0D)
/*  678 */       result += 1000.0D; 
/*  679 */     return (int)result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static double MakeTime(double hour, double min, double sec, double ms) {
/*  685 */     return ((hour * 60.0D + min) * 60.0D + sec) * 1000.0D + ms;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static double MakeDay(double year, double month, double date) {
/*  691 */     year += Math.floor(month / 12.0D);
/*      */     
/*  693 */     month %= 12.0D;
/*  694 */     if (month < 0.0D) {
/*  695 */       month += 12.0D;
/*      */     }
/*  697 */     double yearday = Math.floor(TimeFromYear(year) / 8.64E7D);
/*  698 */     double monthday = DayFromMonth((int)month, (int)year);
/*      */     
/*  700 */     return yearday + monthday + date - 1.0D;
/*      */   }
/*      */ 
/*      */   
/*      */   private static double MakeDate(double day, double time) {
/*  705 */     return day * 8.64E7D + time;
/*      */   }
/*      */ 
/*      */   
/*      */   private static double TimeClip(double d) {
/*  710 */     if (d != d || d == Double.POSITIVE_INFINITY || d == Double.NEGATIVE_INFINITY || Math.abs(d) > 8.64E15D)
/*      */     {
/*      */ 
/*      */ 
/*      */       
/*  715 */       return ScriptRuntime.NaN;
/*      */     }
/*  717 */     if (d > 0.0D) {
/*  718 */       return Math.floor(d + 0.0D);
/*      */     }
/*  720 */     return Math.ceil(d + 0.0D);
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
/*      */   private static double date_msecFromDate(double year, double mon, double mday, double hour, double min, double sec, double msec) {
/*  735 */     double day = MakeDay(year, mon, mday);
/*  736 */     double time = MakeTime(hour, min, sec, msec);
/*  737 */     double result = MakeDate(day, time);
/*  738 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static double date_msecFromArgs(Object[] args) {
/*  745 */     double[] array = new double[7];
/*      */ 
/*      */ 
/*      */     
/*  749 */     for (int loop = 0; loop < 7; loop++) {
/*  750 */       if (loop < args.length) {
/*  751 */         double d = ScriptRuntime.toNumber(args[loop]);
/*  752 */         if (d != d || Double.isInfinite(d)) {
/*  753 */           return ScriptRuntime.NaN;
/*      */         }
/*  755 */         array[loop] = ScriptRuntime.toInteger(args[loop]);
/*      */       }
/*  757 */       else if (loop == 2) {
/*  758 */         array[loop] = 1.0D;
/*      */       } else {
/*  760 */         array[loop] = 0.0D;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  766 */     if (array[0] >= 0.0D && array[0] <= 99.0D) {
/*  767 */       array[0] = array[0] + 1900.0D;
/*      */     }
/*  769 */     return date_msecFromDate(array[0], array[1], array[2], array[3], array[4], array[5], array[6]);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static double jsStaticFunction_UTC(Object[] args) {
/*  775 */     return TimeClip(date_msecFromArgs(args));
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
/*      */   private static double parseISOString(String s) {
/*  788 */     int ERROR = -1;
/*  789 */     int YEAR = 0, MONTH = 1, DAY = 2;
/*  790 */     int HOUR = 3, MIN = 4, SEC = 5, MSEC = 6;
/*  791 */     int TZHOUR = 7, TZMIN = 8;
/*  792 */     int state = 0;
/*      */     
/*  794 */     int[] values = { 1970, 1, 1, 0, 0, 0, 0, -1, -1 };
/*  795 */     int yearlen = 4, yearmod = 1, tzmod = 1;
/*  796 */     int i = 0, len = s.length();
/*  797 */     if (len != 0) {
/*  798 */       char c = s.charAt(0);
/*  799 */       if (c == '+' || c == '-') {
/*      */         
/*  801 */         i++;
/*  802 */         yearlen = 6;
/*  803 */         yearmod = (c == '-') ? -1 : 1;
/*  804 */       } else if (c == 'T') {
/*      */         
/*  806 */         i++;
/*  807 */         state = 3;
/*      */       } 
/*      */     } 
/*  810 */     label126: while (state != -1) {
/*  811 */       int m = i + ((state == 0) ? yearlen : ((state == 6) ? 3 : 2));
/*  812 */       if (m > len) {
/*  813 */         state = -1;
/*      */         
/*      */         break;
/*      */       } 
/*  817 */       int value = 0;
/*  818 */       for (; i < m; i++) {
/*  819 */         char c1 = s.charAt(i);
/*  820 */         if (c1 < '0' || c1 > '9') { state = -1; break label126; }
/*  821 */          value = 10 * value + c1 - 48;
/*      */       } 
/*  823 */       values[state] = value;
/*      */       
/*  825 */       if (i == len) {
/*      */         
/*  827 */         switch (state) {
/*      */           case 3:
/*      */           case 7:
/*  830 */             state = -1;
/*      */             break;
/*      */         } 
/*      */         break;
/*      */       } 
/*  835 */       char c = s.charAt(i++);
/*  836 */       if (c == 'Z') {
/*      */         
/*  838 */         values[7] = 0;
/*  839 */         values[8] = 0;
/*  840 */         switch (state) {
/*      */           case 4:
/*      */           case 5:
/*      */           case 6:
/*      */             break;
/*      */         } 
/*  846 */         state = -1;
/*      */ 
/*      */         
/*      */         break;
/*      */       } 
/*      */       
/*  852 */       switch (state) {
/*      */         case 0:
/*      */         case 1:
/*  855 */           state = (c == '-') ? (state + 1) : ((c == 'T') ? 3 : -1);
/*      */           break;
/*      */         case 2:
/*  858 */           state = (c == 'T') ? 3 : -1;
/*      */           break;
/*      */         case 3:
/*  861 */           state = (c == ':') ? 4 : -1;
/*      */           break;
/*      */ 
/*      */         
/*      */         case 7:
/*  866 */           if (c != ':')
/*      */           {
/*  868 */             i--;
/*      */           }
/*  870 */           state = 8;
/*      */           break;
/*      */         case 4:
/*  873 */           state = (c == ':') ? 5 : ((c == '+' || c == '-') ? 7 : -1);
/*      */           break;
/*      */         case 5:
/*  876 */           state = (c == '.') ? 6 : ((c == '+' || c == '-') ? 7 : -1);
/*      */           break;
/*      */         case 6:
/*  879 */           state = (c == '+' || c == '-') ? 7 : -1;
/*      */           break;
/*      */         case 8:
/*  882 */           state = -1;
/*      */           break;
/*      */       } 
/*  885 */       if (state == 7)
/*      */       {
/*  887 */         tzmod = (c == '-') ? -1 : 1;
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  893 */     if (state != -1 && i == len) {
/*      */ 
/*      */       
/*  896 */       int year = values[0], month = values[1], day = values[2];
/*  897 */       int hour = values[3], min = values[4], sec = values[5], msec = values[6];
/*  898 */       int tzhour = values[7], tzmin = values[8];
/*  899 */       if (year <= 275943 && month >= 1 && month <= 12 && day >= 1 && day <= DaysInMonth(year, month) && hour <= 24 && (hour != 24 || (min <= 0 && sec <= 0 && msec <= 0)) && min <= 59 && sec <= 59 && tzhour <= 23 && tzmin <= 59) {
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
/*  912 */         double date = date_msecFromDate((year * yearmod), (month - 1), day, hour, min, sec, msec);
/*      */         
/*  914 */         if (tzhour != -1)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  920 */           date -= (tzhour * 60 + tzmin) * 60000.0D * tzmod;
/*      */         }
/*      */         
/*  923 */         if (date >= -8.64E15D && date <= 8.64E15D) {
/*  924 */           return date;
/*      */         }
/*      */       } 
/*      */     } 
/*  928 */     return ScriptRuntime.NaN;
/*      */   }
/*      */ 
/*      */   
/*      */   private static double date_parseString(String s) {
/*  933 */     double d = parseISOString(s);
/*  934 */     if (d == d) {
/*  935 */       return d;
/*      */     }
/*      */     
/*  938 */     int year = -1;
/*  939 */     int mon = -1;
/*  940 */     int mday = -1;
/*  941 */     int hour = -1;
/*  942 */     int min = -1;
/*  943 */     int sec = -1;
/*  944 */     char c = Character.MIN_VALUE;
/*  945 */     char si = Character.MIN_VALUE;
/*  946 */     int i = 0;
/*  947 */     int n = -1;
/*  948 */     double tzoffset = -1.0D;
/*  949 */     char prevc = Character.MIN_VALUE;
/*  950 */     int limit = 0;
/*  951 */     boolean seenplusminus = false;
/*      */     
/*  953 */     limit = s.length();
/*  954 */     while (i < limit) {
/*  955 */       c = s.charAt(i);
/*  956 */       i++;
/*  957 */       if (c <= ' ' || c == ',' || c == '-') {
/*  958 */         if (i < limit) {
/*  959 */           si = s.charAt(i);
/*  960 */           if (c == '-' && '0' <= si && si <= '9') {
/*  961 */             prevc = c;
/*      */           }
/*      */         } 
/*      */         continue;
/*      */       } 
/*  966 */       if (c == '(') {
/*  967 */         int depth = 1;
/*  968 */         while (i < limit) {
/*  969 */           c = s.charAt(i);
/*  970 */           i++;
/*  971 */           if (c == '(') {
/*  972 */             depth++; continue;
/*  973 */           }  if (c == ')' && 
/*  974 */             --depth <= 0)
/*      */             break; 
/*      */         } 
/*      */         continue;
/*      */       } 
/*  979 */       if ('0' <= c && c <= '9') {
/*  980 */         n = c - 48;
/*  981 */         while (i < limit && '0' <= (c = s.charAt(i)) && c <= '9') {
/*  982 */           n = n * 10 + c - 48;
/*  983 */           i++;
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  993 */         if (prevc == '+' || prevc == '-')
/*      */         
/*  995 */         { seenplusminus = true;
/*      */ 
/*      */           
/*  998 */           if (n < 24) {
/*  999 */             n *= 60;
/*      */           } else {
/* 1001 */             n = n % 100 + n / 100 * 60;
/* 1002 */           }  if (prevc == '+')
/* 1003 */             n = -n; 
/* 1004 */           if (tzoffset != 0.0D && tzoffset != -1.0D)
/* 1005 */             return ScriptRuntime.NaN; 
/* 1006 */           tzoffset = n; }
/* 1007 */         else if (n >= 70 || (prevc == '/' && mon >= 0 && mday >= 0 && year < 0))
/*      */         
/*      */         { 
/*      */           
/* 1011 */           if (year >= 0)
/* 1012 */             return ScriptRuntime.NaN; 
/* 1013 */           if (c <= ' ' || c == ',' || c == '/' || i >= limit)
/* 1014 */           { year = (n < 100) ? (n + 1900) : n; }
/*      */           else
/* 1016 */           { return ScriptRuntime.NaN; }  }
/* 1017 */         else if (c == ':')
/* 1018 */         { if (hour < 0)
/* 1019 */           { hour = n; }
/* 1020 */           else if (min < 0)
/* 1021 */           { min = n; }
/*      */           else
/* 1023 */           { return ScriptRuntime.NaN; }  }
/* 1024 */         else if (c == '/')
/* 1025 */         { if (mon < 0)
/* 1026 */           { mon = n - 1; }
/* 1027 */           else if (mday < 0)
/* 1028 */           { mday = n; }
/*      */           else
/* 1030 */           { return ScriptRuntime.NaN; }  }
/* 1031 */         else { if (i < limit && c != ',' && c > ' ' && c != '-')
/* 1032 */             return ScriptRuntime.NaN; 
/* 1033 */           if (seenplusminus && n < 60) {
/* 1034 */             if (tzoffset < 0.0D)
/* 1035 */             { tzoffset -= n; }
/*      */             else
/* 1037 */             { tzoffset += n; } 
/* 1038 */           } else if (hour >= 0 && min < 0) {
/* 1039 */             min = n;
/* 1040 */           } else if (min >= 0 && sec < 0) {
/* 1041 */             sec = n;
/* 1042 */           } else if (mday < 0) {
/* 1043 */             mday = n;
/*      */           } else {
/* 1045 */             return ScriptRuntime.NaN;
/*      */           }  }
/* 1047 */          prevc = Character.MIN_VALUE; continue;
/* 1048 */       }  if (c == '/' || c == ':' || c == '+' || c == '-') {
/* 1049 */         prevc = c; continue;
/*      */       } 
/* 1051 */       int st = i - 1;
/* 1052 */       while (i < limit) {
/* 1053 */         c = s.charAt(i);
/* 1054 */         if (('A' > c || c > 'Z') && ('a' > c || c > 'z'))
/*      */           break; 
/* 1056 */         i++;
/*      */       } 
/* 1058 */       int letterCount = i - st;
/* 1059 */       if (letterCount < 2) {
/* 1060 */         return ScriptRuntime.NaN;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1066 */       String wtb = "am;pm;monday;tuesday;wednesday;thursday;friday;saturday;sunday;january;february;march;april;may;june;july;august;september;october;november;december;gmt;ut;utc;est;edt;cst;cdt;mst;mdt;pst;pdt;";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1072 */       int index = 0;
/* 1073 */       int wtbOffset = 0; while (true) {
/* 1074 */         int wtbNext = wtb.indexOf(';', wtbOffset);
/* 1075 */         if (wtbNext < 0)
/* 1076 */           return ScriptRuntime.NaN; 
/* 1077 */         if (wtb.regionMatches(true, wtbOffset, s, st, letterCount))
/*      */           break; 
/* 1079 */         wtbOffset = wtbNext + 1;
/* 1080 */         index++;
/*      */       } 
/* 1082 */       if (index < 2) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1087 */         if (hour > 12 || hour < 0)
/* 1088 */           return ScriptRuntime.NaN; 
/* 1089 */         if (index == 0) {
/*      */           
/* 1091 */           if (hour == 12)
/* 1092 */             hour = 0; 
/*      */           continue;
/*      */         } 
/* 1095 */         if (hour != 12)
/* 1096 */           hour += 12;  continue;
/*      */       } 
/* 1098 */       index -= 2; if (index < 7)
/*      */         continue; 
/* 1100 */       index -= 7; if (index < 12) {
/*      */         
/* 1102 */         if (mon < 0) {
/* 1103 */           mon = index; continue;
/*      */         } 
/* 1105 */         return ScriptRuntime.NaN;
/*      */       } 
/*      */       
/* 1108 */       index -= 12;
/*      */       
/* 1110 */       switch (index) { case 0:
/* 1111 */           tzoffset = 0.0D; continue;
/* 1112 */         case 1: tzoffset = 0.0D; continue;
/* 1113 */         case 2: tzoffset = 0.0D; continue;
/* 1114 */         case 3: tzoffset = 300.0D; continue;
/* 1115 */         case 4: tzoffset = 240.0D; continue;
/* 1116 */         case 5: tzoffset = 360.0D; continue;
/* 1117 */         case 6: tzoffset = 300.0D; continue;
/* 1118 */         case 7: tzoffset = 420.0D; continue;
/* 1119 */         case 8: tzoffset = 360.0D; continue;
/* 1120 */         case 9: tzoffset = 480.0D; continue;
/* 1121 */         case 10: tzoffset = 420.0D; continue; }
/* 1122 */        Kit.codeBug();
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1127 */     if (year < 0 || mon < 0 || mday < 0)
/* 1128 */       return ScriptRuntime.NaN; 
/* 1129 */     if (sec < 0)
/* 1130 */       sec = 0; 
/* 1131 */     if (min < 0)
/* 1132 */       min = 0; 
/* 1133 */     if (hour < 0) {
/* 1134 */       hour = 0;
/*      */     }
/* 1136 */     double msec = date_msecFromDate(year, mon, mday, hour, min, sec, 0.0D);
/* 1137 */     if (tzoffset == -1.0D) {
/* 1138 */       return internalUTC(msec);
/*      */     }
/* 1140 */     return msec + tzoffset * 60000.0D;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static String date_format(double t, int methodId) {
/* 1146 */     StringBuilder result = new StringBuilder(60);
/* 1147 */     double local = LocalTime(t);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1153 */     if (methodId != 3) {
/* 1154 */       appendWeekDayName(result, WeekDay(local));
/* 1155 */       result.append(' ');
/* 1156 */       appendMonthName(result, MonthFromTime(local));
/* 1157 */       result.append(' ');
/* 1158 */       append0PaddedUint(result, DateFromTime(local), 2);
/* 1159 */       result.append(' ');
/* 1160 */       int year = YearFromTime(local);
/* 1161 */       if (year < 0) {
/* 1162 */         result.append('-');
/* 1163 */         year = -year;
/*      */       } 
/* 1165 */       append0PaddedUint(result, year, 4);
/* 1166 */       if (methodId != 4) {
/* 1167 */         result.append(' ');
/*      */       }
/*      */     } 
/* 1170 */     if (methodId != 4) {
/* 1171 */       append0PaddedUint(result, HourFromTime(local), 2);
/* 1172 */       result.append(':');
/* 1173 */       append0PaddedUint(result, MinFromTime(local), 2);
/* 1174 */       result.append(':');
/* 1175 */       append0PaddedUint(result, SecFromTime(local), 2);
/*      */ 
/*      */ 
/*      */       
/* 1179 */       int minutes = (int)Math.floor((LocalTZA + DaylightSavingTA(t)) / 60000.0D);
/*      */ 
/*      */       
/* 1182 */       int offset = minutes / 60 * 100 + minutes % 60;
/* 1183 */       if (offset > 0) {
/* 1184 */         result.append(" GMT+");
/*      */       } else {
/* 1186 */         result.append(" GMT-");
/* 1187 */         offset = -offset;
/*      */       } 
/* 1189 */       append0PaddedUint(result, offset, 4);
/*      */       
/* 1191 */       if (timeZoneFormatter == null) {
/* 1192 */         timeZoneFormatter = new SimpleDateFormat("zzz");
/*      */       }
/*      */ 
/*      */       
/* 1196 */       if (t < 0.0D) {
/* 1197 */         int equiv = EquivalentYear(YearFromTime(local));
/* 1198 */         double day = MakeDay(equiv, MonthFromTime(t), DateFromTime(t));
/* 1199 */         t = MakeDate(day, TimeWithinDay(t));
/*      */       } 
/* 1201 */       result.append(" (");
/* 1202 */       Date date = new Date((long)t);
/* 1203 */       synchronized (timeZoneFormatter) {
/* 1204 */         result.append(timeZoneFormatter.format(date));
/*      */       } 
/* 1206 */       result.append(')');
/*      */     } 
/* 1208 */     return result.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static Object jsConstructor(Object[] args) {
/* 1214 */     NativeDate obj = new NativeDate();
/*      */ 
/*      */ 
/*      */     
/* 1218 */     if (args.length == 0) {
/* 1219 */       obj.date = now();
/* 1220 */       return obj;
/*      */     } 
/*      */ 
/*      */     
/* 1224 */     if (args.length == 1) {
/* 1225 */       double date; Object arg0 = args[0];
/* 1226 */       if (arg0 instanceof Scriptable) {
/* 1227 */         arg0 = ((Scriptable)arg0).getDefaultValue(null);
/*      */       }
/* 1229 */       if (arg0 instanceof CharSequence) {
/*      */         
/* 1231 */         date = date_parseString(arg0.toString());
/*      */       } else {
/*      */         
/* 1234 */         date = ScriptRuntime.toNumber(arg0);
/*      */       } 
/* 1236 */       obj.date = TimeClip(date);
/* 1237 */       return obj;
/*      */     } 
/*      */     
/* 1240 */     double time = date_msecFromArgs(args);
/*      */     
/* 1242 */     if (!Double.isNaN(time) && !Double.isInfinite(time)) {
/* 1243 */       time = TimeClip(internalUTC(time));
/*      */     }
/* 1245 */     obj.date = time;
/*      */     
/* 1247 */     return obj;
/*      */   }
/*      */ 
/*      */   
/*      */   private static String toLocale_helper(double t, int methodId) {
/*      */     DateFormat formatter;
/* 1253 */     switch (methodId) {
/*      */       case 5:
/* 1255 */         if (localeDateTimeFormatter == null) {
/* 1256 */           localeDateTimeFormatter = DateFormat.getDateTimeInstance(1, 1);
/*      */         }
/*      */ 
/*      */         
/* 1260 */         formatter = localeDateTimeFormatter;
/*      */         break;
/*      */       case 6:
/* 1263 */         if (localeTimeFormatter == null) {
/* 1264 */           localeTimeFormatter = DateFormat.getTimeInstance(1);
/*      */         }
/*      */         
/* 1267 */         formatter = localeTimeFormatter;
/*      */         break;
/*      */       case 7:
/* 1270 */         if (localeDateFormatter == null) {
/* 1271 */           localeDateFormatter = DateFormat.getDateInstance(1);
/*      */         }
/*      */         
/* 1274 */         formatter = localeDateFormatter; break;
/*      */       default:
/* 1276 */         throw new AssertionError();
/*      */     } 
/*      */     
/* 1279 */     synchronized (formatter) {
/* 1280 */       return formatter.format(new Date((long)t));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static String js_toUTCString(double date) {
/* 1286 */     StringBuilder result = new StringBuilder(60);
/*      */     
/* 1288 */     appendWeekDayName(result, WeekDay(date));
/* 1289 */     result.append(", ");
/* 1290 */     append0PaddedUint(result, DateFromTime(date), 2);
/* 1291 */     result.append(' ');
/* 1292 */     appendMonthName(result, MonthFromTime(date));
/* 1293 */     result.append(' ');
/* 1294 */     int year = YearFromTime(date);
/* 1295 */     if (year < 0) {
/* 1296 */       result.append('-'); year = -year;
/*      */     } 
/* 1298 */     append0PaddedUint(result, year, 4);
/* 1299 */     result.append(' ');
/* 1300 */     append0PaddedUint(result, HourFromTime(date), 2);
/* 1301 */     result.append(':');
/* 1302 */     append0PaddedUint(result, MinFromTime(date), 2);
/* 1303 */     result.append(':');
/* 1304 */     append0PaddedUint(result, SecFromTime(date), 2);
/* 1305 */     result.append(" GMT");
/* 1306 */     return result.toString();
/*      */   }
/*      */   
/*      */   private static String js_toISOString(double t) {
/* 1310 */     StringBuilder result = new StringBuilder(27);
/*      */     
/* 1312 */     int year = YearFromTime(t);
/* 1313 */     if (year < 0) {
/* 1314 */       result.append('-');
/* 1315 */       append0PaddedUint(result, -year, 6);
/* 1316 */     } else if (year > 9999) {
/* 1317 */       append0PaddedUint(result, year, 6);
/*      */     } else {
/* 1319 */       append0PaddedUint(result, year, 4);
/*      */     } 
/* 1321 */     result.append('-');
/* 1322 */     append0PaddedUint(result, MonthFromTime(t) + 1, 2);
/* 1323 */     result.append('-');
/* 1324 */     append0PaddedUint(result, DateFromTime(t), 2);
/* 1325 */     result.append('T');
/* 1326 */     append0PaddedUint(result, HourFromTime(t), 2);
/* 1327 */     result.append(':');
/* 1328 */     append0PaddedUint(result, MinFromTime(t), 2);
/* 1329 */     result.append(':');
/* 1330 */     append0PaddedUint(result, SecFromTime(t), 2);
/* 1331 */     result.append('.');
/* 1332 */     append0PaddedUint(result, msFromTime(t), 3);
/* 1333 */     result.append('Z');
/* 1334 */     return result.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private static void append0PaddedUint(StringBuilder sb, int i, int minWidth) {
/* 1339 */     if (i < 0) Kit.codeBug(); 
/* 1340 */     int scale = 1;
/* 1341 */     minWidth--;
/* 1342 */     if (i >= 10) {
/* 1343 */       if (i < 1000000000) {
/*      */         while (true) {
/* 1345 */           int newScale = scale * 10;
/* 1346 */           if (i < newScale)
/* 1347 */             break;  minWidth--;
/* 1348 */           scale = newScale;
/*      */         } 
/*      */       } else {
/*      */         
/* 1352 */         minWidth -= 9;
/* 1353 */         scale = 1000000000;
/*      */       } 
/*      */     }
/* 1356 */     while (minWidth > 0) {
/* 1357 */       sb.append('0');
/* 1358 */       minWidth--;
/*      */     } 
/* 1360 */     while (scale != 1) {
/* 1361 */       sb.append((char)(48 + i / scale));
/* 1362 */       i %= scale;
/* 1363 */       scale /= 10;
/*      */     } 
/* 1365 */     sb.append((char)(48 + i));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void appendMonthName(StringBuilder sb, int index) {
/* 1373 */     String months = "JanFebMarAprMayJunJulAugSepOctNovDec";
/*      */     
/* 1375 */     index *= 3;
/* 1376 */     for (int i = 0; i != 3; i++) {
/* 1377 */       sb.append(months.charAt(index + i));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static void appendWeekDayName(StringBuilder sb, int index) {
/* 1383 */     String days = "SunMonTueWedThuFriSat";
/* 1384 */     index *= 3;
/* 1385 */     for (int i = 0; i != 3; i++)
/* 1386 */       sb.append(days.charAt(index + i)); 
/*      */   }
/*      */   
/*      */   private static double makeTime(double date, Object[] args, int methodId) {
/*      */     int maxargs;
/*      */     double hour, min, sec, msec, lorutime;
/* 1392 */     if (args.length == 0)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1402 */       return ScriptRuntime.NaN;
/*      */     }
/*      */ 
/*      */     
/* 1406 */     boolean local = true;
/* 1407 */     switch (methodId) {
/*      */       case 32:
/* 1409 */         local = false;
/*      */       
/*      */       case 31:
/* 1412 */         maxargs = 1;
/*      */         break;
/*      */       
/*      */       case 34:
/* 1416 */         local = false;
/*      */       
/*      */       case 33:
/* 1419 */         maxargs = 2;
/*      */         break;
/*      */       
/*      */       case 36:
/* 1423 */         local = false;
/*      */       
/*      */       case 35:
/* 1426 */         maxargs = 3;
/*      */         break;
/*      */       
/*      */       case 38:
/* 1430 */         local = false;
/*      */       
/*      */       case 37:
/* 1433 */         maxargs = 4;
/*      */         break;
/*      */       
/*      */       default:
/* 1437 */         throw Kit.codeBug();
/*      */     } 
/*      */     
/* 1440 */     boolean hasNaN = false;
/* 1441 */     int numNums = (args.length < maxargs) ? args.length : maxargs;
/* 1442 */     assert numNums <= 4;
/* 1443 */     double[] nums = new double[4]; int i;
/* 1444 */     for (i = 0; i < numNums; i++) {
/* 1445 */       double d = ScriptRuntime.toNumber(args[i]);
/* 1446 */       if (d != d || Double.isInfinite(d)) {
/* 1447 */         hasNaN = true;
/*      */       } else {
/* 1449 */         nums[i] = ScriptRuntime.toInteger(d);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1455 */     if (hasNaN || date != date) {
/* 1456 */       return ScriptRuntime.NaN;
/*      */     }
/*      */     
/* 1459 */     i = 0; int stop = numNums;
/*      */ 
/*      */ 
/*      */     
/* 1463 */     if (local) {
/* 1464 */       lorutime = LocalTime(date);
/*      */     } else {
/* 1466 */       lorutime = date;
/*      */     } 
/* 1468 */     if (maxargs >= 4 && i < stop) {
/* 1469 */       hour = nums[i++];
/*      */     } else {
/* 1471 */       hour = HourFromTime(lorutime);
/*      */     } 
/* 1473 */     if (maxargs >= 3 && i < stop) {
/* 1474 */       min = nums[i++];
/*      */     } else {
/* 1476 */       min = MinFromTime(lorutime);
/*      */     } 
/* 1478 */     if (maxargs >= 2 && i < stop) {
/* 1479 */       sec = nums[i++];
/*      */     } else {
/* 1481 */       sec = SecFromTime(lorutime);
/*      */     } 
/* 1483 */     if (maxargs >= 1 && i < stop) {
/* 1484 */       msec = nums[i++];
/*      */     } else {
/* 1486 */       msec = msFromTime(lorutime);
/*      */     } 
/* 1488 */     double time = MakeTime(hour, min, sec, msec);
/* 1489 */     double result = MakeDate(Day(lorutime), time);
/*      */     
/* 1491 */     if (local) {
/* 1492 */       result = internalUTC(result);
/*      */     }
/* 1494 */     return TimeClip(result);
/*      */   }
/*      */   
/*      */   private static double makeDate(double date, Object[] args, int methodId) {
/*      */     int maxargs;
/*      */     double year, month, lorutime;
/* 1500 */     if (args.length == 0) {
/* 1501 */       return ScriptRuntime.NaN;
/*      */     }
/*      */ 
/*      */     
/* 1505 */     boolean local = true;
/* 1506 */     switch (methodId) {
/*      */       case 40:
/* 1508 */         local = false;
/*      */       
/*      */       case 39:
/* 1511 */         maxargs = 1;
/*      */         break;
/*      */       
/*      */       case 42:
/* 1515 */         local = false;
/*      */       
/*      */       case 41:
/* 1518 */         maxargs = 2;
/*      */         break;
/*      */       
/*      */       case 44:
/* 1522 */         local = false;
/*      */       
/*      */       case 43:
/* 1525 */         maxargs = 3;
/*      */         break;
/*      */       
/*      */       default:
/* 1529 */         throw Kit.codeBug();
/*      */     } 
/*      */     
/* 1532 */     boolean hasNaN = false;
/* 1533 */     int numNums = (args.length < maxargs) ? args.length : maxargs;
/* 1534 */     assert 1 <= numNums && numNums <= 3;
/* 1535 */     double[] nums = new double[3]; int i;
/* 1536 */     for (i = 0; i < numNums; i++) {
/* 1537 */       double d = ScriptRuntime.toNumber(args[i]);
/* 1538 */       if (d != d || Double.isInfinite(d)) {
/* 1539 */         hasNaN = true;
/*      */       } else {
/* 1541 */         nums[i] = ScriptRuntime.toInteger(d);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1546 */     if (hasNaN) {
/* 1547 */       return ScriptRuntime.NaN;
/*      */     }
/*      */     
/* 1550 */     i = 0; int stop = numNums;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1556 */     if (date != date) {
/* 1557 */       if (maxargs < 3) {
/* 1558 */         return ScriptRuntime.NaN;
/*      */       }
/* 1560 */       lorutime = 0.0D;
/*      */     
/*      */     }
/* 1563 */     else if (local) {
/* 1564 */       lorutime = LocalTime(date);
/*      */     } else {
/* 1566 */       lorutime = date;
/*      */     } 
/*      */     
/* 1569 */     if (maxargs >= 3 && i < stop) {
/* 1570 */       year = nums[i++];
/*      */     } else {
/* 1572 */       year = YearFromTime(lorutime);
/*      */     } 
/* 1574 */     if (maxargs >= 2 && i < stop) {
/* 1575 */       month = nums[i++];
/*      */     } else {
/* 1577 */       month = MonthFromTime(lorutime);
/*      */     } 
/* 1579 */     if (maxargs >= 1 && i < stop) {
/* 1580 */       day = nums[i++];
/*      */     } else {
/* 1582 */       day = DateFromTime(lorutime);
/*      */     } 
/* 1584 */     double day = MakeDay(year, month, day);
/* 1585 */     double result = MakeDate(day, TimeWithinDay(lorutime));
/*      */     
/* 1587 */     if (local) {
/* 1588 */       result = internalUTC(result);
/*      */     }
/* 1590 */     return TimeClip(result);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int findPrototypeId(String s) {
/* 1600 */     int c, id = 0; String X = null;
/* 1601 */     switch (s.length()) { case 6:
/* 1602 */         c = s.charAt(0);
/* 1603 */         if (c == 103) { X = "getDay"; id = 19; break; }
/* 1604 */          if (c == 116) { X = "toJSON"; id = 47; }  break;
/*      */       case 7:
/* 1606 */         switch (s.charAt(3)) { case 'D':
/* 1607 */             c = s.charAt(0);
/* 1608 */             if (c == 103) { X = "getDate"; id = 17; break; }
/* 1609 */              if (c == 115) { X = "setDate"; id = 39; }  break;
/*      */           case 'T':
/* 1611 */             c = s.charAt(0);
/* 1612 */             if (c == 103) { X = "getTime"; id = 11; break; }
/* 1613 */              if (c == 115) { X = "setTime"; id = 30; }  break;
/*      */           case 'Y':
/* 1615 */             c = s.charAt(0);
/* 1616 */             if (c == 103) { X = "getYear"; id = 12; break; }
/* 1617 */              if (c == 115) { X = "setYear"; id = 45; }  break;
/*      */           case 'u':
/* 1619 */             X = "valueOf"; id = 10; break; }  break;
/*      */       case 8:
/* 1621 */         switch (s.charAt(3)) { case 'H':
/* 1622 */             c = s.charAt(0);
/* 1623 */             if (c == 103) { X = "getHours"; id = 21; break; }
/* 1624 */              if (c == 115) { X = "setHours"; id = 37; }  break;
/*      */           case 'M':
/* 1626 */             c = s.charAt(0);
/* 1627 */             if (c == 103) { X = "getMonth"; id = 15; break; }
/* 1628 */              if (c == 115) { X = "setMonth"; id = 41; }  break;
/*      */           case 'o':
/* 1630 */             X = "toSource"; id = 9; break;
/* 1631 */           case 't': X = "toString"; id = 2; break; }  break;
/*      */       case 9:
/* 1633 */         X = "getUTCDay"; id = 20; break;
/* 1634 */       case 10: c = s.charAt(3);
/* 1635 */         if (c == 77) {
/* 1636 */           c = s.charAt(0);
/* 1637 */           if (c == 103) { X = "getMinutes"; id = 23; break; }
/* 1638 */            if (c == 115) { X = "setMinutes"; id = 35; }
/*      */            break;
/* 1640 */         }  if (c == 83) {
/* 1641 */           c = s.charAt(0);
/* 1642 */           if (c == 103) { X = "getSeconds"; id = 25; break; }
/* 1643 */            if (c == 115) { X = "setSeconds"; id = 33; }
/*      */            break;
/* 1645 */         }  if (c == 85) {
/* 1646 */           c = s.charAt(0);
/* 1647 */           if (c == 103) { X = "getUTCDate"; id = 18; break; }
/* 1648 */            if (c == 115) { X = "setUTCDate"; id = 40; } 
/*      */         }  break;
/*      */       case 11:
/* 1651 */         switch (s.charAt(3)) { case 'F':
/* 1652 */             c = s.charAt(0);
/* 1653 */             if (c == 103) { X = "getFullYear"; id = 13; break; }
/* 1654 */              if (c == 115) { X = "setFullYear"; id = 43; }  break;
/*      */           case 'M':
/* 1656 */             X = "toGMTString"; id = 8; break;
/* 1657 */           case 'S': X = "toISOString"; id = 46; break;
/* 1658 */           case 'T': X = "toUTCString"; id = 8; break;
/* 1659 */           case 'U': c = s.charAt(0);
/* 1660 */             if (c == 103) {
/* 1661 */               c = s.charAt(9);
/* 1662 */               if (c == 114) { X = "getUTCHours"; id = 22; break; }
/* 1663 */                if (c == 116) { X = "getUTCMonth"; id = 16; }
/*      */                break;
/* 1665 */             }  if (c == 115) {
/* 1666 */               c = s.charAt(9);
/* 1667 */               if (c == 114) { X = "setUTCHours"; id = 38; break; }
/* 1668 */                if (c == 116) { X = "setUTCMonth"; id = 42; } 
/*      */             }  break;
/*      */           case 's':
/* 1671 */             X = "constructor"; id = 1; break; }  break;
/*      */       case 12:
/* 1673 */         c = s.charAt(2);
/* 1674 */         if (c == 68) { X = "toDateString"; id = 4; break; }
/* 1675 */          if (c == 84) { X = "toTimeString"; id = 3; }  break;
/*      */       case 13:
/* 1677 */         c = s.charAt(0);
/* 1678 */         if (c == 103) {
/* 1679 */           c = s.charAt(6);
/* 1680 */           if (c == 77) { X = "getUTCMinutes"; id = 24; break; }
/* 1681 */            if (c == 83) { X = "getUTCSeconds"; id = 26; }
/*      */            break;
/* 1683 */         }  if (c == 115) {
/* 1684 */           c = s.charAt(6);
/* 1685 */           if (c == 77) { X = "setUTCMinutes"; id = 36; break; }
/* 1686 */            if (c == 83) { X = "setUTCSeconds"; id = 34; } 
/*      */         }  break;
/*      */       case 14:
/* 1689 */         c = s.charAt(0);
/* 1690 */         if (c == 103) { X = "getUTCFullYear"; id = 14; break; }
/* 1691 */          if (c == 115) { X = "setUTCFullYear"; id = 44; break; }
/* 1692 */          if (c == 116) { X = "toLocaleString"; id = 5; }  break;
/*      */       case 15:
/* 1694 */         c = s.charAt(0);
/* 1695 */         if (c == 103) { X = "getMilliseconds"; id = 27; break; }
/* 1696 */          if (c == 115) { X = "setMilliseconds"; id = 31; }  break;
/*      */       case 17:
/* 1698 */         X = "getTimezoneOffset"; id = 29; break;
/* 1699 */       case 18: c = s.charAt(0);
/* 1700 */         if (c == 103) { X = "getUTCMilliseconds"; id = 28; break; }
/* 1701 */          if (c == 115) { X = "setUTCMilliseconds"; id = 32; break; }
/* 1702 */          if (c == 116) {
/* 1703 */           c = s.charAt(8);
/* 1704 */           if (c == 68) { X = "toLocaleDateString"; id = 7; break; }
/* 1705 */            if (c == 84) { X = "toLocaleTimeString"; id = 6; }
/*      */         
/*      */         }  break; }
/*      */     
/* 1709 */     if (X != null && X != s && !X.equals(s)) id = 0;
/*      */ 
/*      */ 
/*      */     
/* 1713 */     return id;
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\NativeDate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */