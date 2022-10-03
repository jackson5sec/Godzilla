/*      */ package org.mozilla.javascript;
/*      */ 
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.text.MessageFormat;
/*      */ import java.util.Locale;
/*      */ import java.util.MissingResourceException;
/*      */ import java.util.ResourceBundle;
/*      */ import org.mozilla.javascript.v8dtoa.DoubleConversion;
/*      */ import org.mozilla.javascript.v8dtoa.FastDtoa;
/*      */ import org.mozilla.javascript.xml.XMLLib;
/*      */ import org.mozilla.javascript.xml.XMLObject;
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
/*      */ public class ScriptRuntime
/*      */ {
/*      */   @Deprecated
/*      */   public static BaseFunction typeErrorThrower() {
/*   44 */     return typeErrorThrower(Context.getCurrentContext());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BaseFunction typeErrorThrower(Context cx) {
/*   52 */     if (cx.typeErrorThrower == null) {
/*   53 */       BaseFunction thrower = new BaseFunction()
/*      */         {
/*      */           static final long serialVersionUID = -5891740962154902286L;
/*      */           
/*      */           public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/*   58 */             throw ScriptRuntime.typeError0("msg.op.not.allowed");
/*      */           }
/*      */           
/*      */           public int getLength() {
/*   62 */             return 0;
/*      */           }
/*      */         };
/*   65 */       setFunctionProtoAndParent(thrower, cx.topCallScope);
/*   66 */       thrower.preventExtensions();
/*   67 */       cx.typeErrorThrower = thrower;
/*      */     } 
/*   69 */     return cx.typeErrorThrower;
/*      */   }
/*      */   
/*      */   static class NoSuchMethodShim
/*      */     implements Callable {
/*      */     String methodName;
/*      */     Callable noSuchMethodMethod;
/*      */     
/*      */     NoSuchMethodShim(Callable noSuchMethodMethod, String methodName) {
/*   78 */       this.noSuchMethodMethod = noSuchMethodMethod;
/*   79 */       this.methodName = methodName;
/*      */     }
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
/*      */     public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/*   93 */       Object[] nestedArgs = new Object[2];
/*      */       
/*   95 */       nestedArgs[0] = this.methodName;
/*   96 */       nestedArgs[1] = ScriptRuntime.newArrayLiteral(args, null, cx, scope);
/*   97 */       return this.noSuchMethodMethod.call(cx, scope, thisObj, nestedArgs);
/*      */     }
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
/*  112 */   public static final Class<?> BooleanClass = Kit.classOrNull("java.lang.Boolean");
/*  113 */   public static final Class<?> ByteClass = Kit.classOrNull("java.lang.Byte");
/*  114 */   public static final Class<?> CharacterClass = Kit.classOrNull("java.lang.Character");
/*  115 */   public static final Class<?> ClassClass = Kit.classOrNull("java.lang.Class");
/*  116 */   public static final Class<?> DoubleClass = Kit.classOrNull("java.lang.Double");
/*  117 */   public static final Class<?> FloatClass = Kit.classOrNull("java.lang.Float");
/*  118 */   public static final Class<?> IntegerClass = Kit.classOrNull("java.lang.Integer");
/*  119 */   public static final Class<?> LongClass = Kit.classOrNull("java.lang.Long");
/*  120 */   public static final Class<?> NumberClass = Kit.classOrNull("java.lang.Number");
/*  121 */   public static final Class<?> ObjectClass = Kit.classOrNull("java.lang.Object");
/*  122 */   public static final Class<?> ShortClass = Kit.classOrNull("java.lang.Short");
/*  123 */   public static final Class<?> StringClass = Kit.classOrNull("java.lang.String");
/*  124 */   public static final Class<?> DateClass = Kit.classOrNull("java.util.Date");
/*      */ 
/*      */   
/*  127 */   public static final Class<?> ContextClass = Kit.classOrNull("org.mozilla.javascript.Context");
/*      */   
/*  129 */   public static final Class<?> ContextFactoryClass = Kit.classOrNull("org.mozilla.javascript.ContextFactory");
/*      */   
/*  131 */   public static final Class<?> FunctionClass = Kit.classOrNull("org.mozilla.javascript.Function");
/*      */   
/*  133 */   public static final Class<?> ScriptableObjectClass = Kit.classOrNull("org.mozilla.javascript.ScriptableObject");
/*      */   
/*  135 */   public static final Class<Scriptable> ScriptableClass = Scriptable.class;
/*      */ 
/*      */ 
/*      */   
/*  139 */   public static Locale ROOT_LOCALE = new Locale("");
/*      */   
/*  141 */   private static final Object LIBRARY_SCOPE_KEY = "LIBRARY_SCOPE";
/*      */ 
/*      */   
/*      */   public static boolean isRhinoRuntimeType(Class<?> cl) {
/*  145 */     if (cl.isPrimitive()) {
/*  146 */       return (cl != char.class);
/*      */     }
/*  148 */     return (cl == StringClass || cl == BooleanClass || NumberClass.isAssignableFrom(cl) || ScriptableClass.isAssignableFrom(cl));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ScriptableObject initSafeStandardObjects(Context cx, ScriptableObject scope, boolean sealed) {
/*  158 */     if (scope == null) {
/*  159 */       scope = new NativeObject();
/*      */     }
/*  161 */     scope.associateValue(LIBRARY_SCOPE_KEY, scope);
/*  162 */     (new ClassCache()).associate(scope);
/*      */     
/*  164 */     BaseFunction.init(scope, sealed);
/*  165 */     NativeObject.init(scope, sealed);
/*      */     
/*  167 */     Scriptable objectProto = ScriptableObject.getObjectPrototype(scope);
/*      */ 
/*      */     
/*  170 */     Scriptable functionProto = ScriptableObject.getClassPrototype(scope, "Function");
/*  171 */     functionProto.setPrototype(objectProto);
/*      */ 
/*      */     
/*  174 */     if (scope.getPrototype() == null) {
/*  175 */       scope.setPrototype(objectProto);
/*      */     }
/*      */     
/*  178 */     NativeError.init(scope, sealed);
/*  179 */     NativeGlobal.init(cx, scope, sealed);
/*      */     
/*  181 */     NativeArray.init(scope, sealed);
/*  182 */     if (cx.getOptimizationLevel() > 0)
/*      */     {
/*      */ 
/*      */       
/*  186 */       NativeArray.setMaximumInitialCapacity(200000);
/*      */     }
/*  188 */     NativeString.init(scope, sealed);
/*  189 */     NativeBoolean.init(scope, sealed);
/*  190 */     NativeNumber.init(scope, sealed);
/*  191 */     NativeDate.init(scope, sealed);
/*  192 */     NativeMath.init(scope, sealed);
/*  193 */     NativeJSON.init(scope, sealed);
/*      */     
/*  195 */     NativeWith.init(scope, sealed);
/*  196 */     NativeCall.init(scope, sealed);
/*  197 */     NativeScript.init(scope, sealed);
/*      */     
/*  199 */     NativeIterator.init(scope, sealed);
/*      */     
/*  201 */     boolean withXml = (cx.hasFeature(6) && cx.getE4xImplementationFactory() != null);
/*      */ 
/*      */ 
/*      */     
/*  205 */     new LazilyLoadedCtor(scope, "RegExp", "org.mozilla.javascript.regexp.NativeRegExp", sealed, true);
/*      */     
/*  207 */     new LazilyLoadedCtor(scope, "Continuation", "org.mozilla.javascript.NativeContinuation", sealed, true);
/*      */ 
/*      */     
/*  210 */     if (withXml) {
/*  211 */       String xmlImpl = cx.getE4xImplementationFactory().getImplementationClassName();
/*  212 */       new LazilyLoadedCtor(scope, "XML", xmlImpl, sealed, true);
/*  213 */       new LazilyLoadedCtor(scope, "XMLList", xmlImpl, sealed, true);
/*  214 */       new LazilyLoadedCtor(scope, "Namespace", xmlImpl, sealed, true);
/*  215 */       new LazilyLoadedCtor(scope, "QName", xmlImpl, sealed, true);
/*      */     } 
/*      */     
/*  218 */     if (cx.getLanguageVersion() >= 180 && cx.hasFeature(14)) {
/*      */       
/*  220 */       new LazilyLoadedCtor(scope, "ArrayBuffer", "org.mozilla.javascript.typedarrays.NativeArrayBuffer", sealed, true);
/*      */ 
/*      */       
/*  223 */       new LazilyLoadedCtor(scope, "Int8Array", "org.mozilla.javascript.typedarrays.NativeInt8Array", sealed, true);
/*      */ 
/*      */       
/*  226 */       new LazilyLoadedCtor(scope, "Uint8Array", "org.mozilla.javascript.typedarrays.NativeUint8Array", sealed, true);
/*      */ 
/*      */       
/*  229 */       new LazilyLoadedCtor(scope, "Uint8ClampedArray", "org.mozilla.javascript.typedarrays.NativeUint8ClampedArray", sealed, true);
/*      */ 
/*      */       
/*  232 */       new LazilyLoadedCtor(scope, "Int16Array", "org.mozilla.javascript.typedarrays.NativeInt16Array", sealed, true);
/*      */ 
/*      */       
/*  235 */       new LazilyLoadedCtor(scope, "Uint16Array", "org.mozilla.javascript.typedarrays.NativeUint16Array", sealed, true);
/*      */ 
/*      */       
/*  238 */       new LazilyLoadedCtor(scope, "Int32Array", "org.mozilla.javascript.typedarrays.NativeInt32Array", sealed, true);
/*      */ 
/*      */       
/*  241 */       new LazilyLoadedCtor(scope, "Uint32Array", "org.mozilla.javascript.typedarrays.NativeUint32Array", sealed, true);
/*      */ 
/*      */       
/*  244 */       new LazilyLoadedCtor(scope, "Float32Array", "org.mozilla.javascript.typedarrays.NativeFloat32Array", sealed, true);
/*      */ 
/*      */       
/*  247 */       new LazilyLoadedCtor(scope, "Float64Array", "org.mozilla.javascript.typedarrays.NativeFloat64Array", sealed, true);
/*      */ 
/*      */       
/*  250 */       new LazilyLoadedCtor(scope, "DataView", "org.mozilla.javascript.typedarrays.NativeDataView", sealed, true);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  255 */     if (scope instanceof TopLevel) {
/*  256 */       ((TopLevel)scope).cacheBuiltins();
/*      */     }
/*      */     
/*  259 */     return scope;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ScriptableObject initStandardObjects(Context cx, ScriptableObject scope, boolean sealed) {
/*  266 */     ScriptableObject s = initSafeStandardObjects(cx, scope, sealed);
/*      */     
/*  268 */     new LazilyLoadedCtor(s, "Packages", "org.mozilla.javascript.NativeJavaTopPackage", sealed, true);
/*      */     
/*  270 */     new LazilyLoadedCtor(s, "getClass", "org.mozilla.javascript.NativeJavaTopPackage", sealed, true);
/*      */     
/*  272 */     new LazilyLoadedCtor(s, "JavaAdapter", "org.mozilla.javascript.JavaAdapter", sealed, true);
/*      */     
/*  274 */     new LazilyLoadedCtor(s, "JavaImporter", "org.mozilla.javascript.ImporterTopLevel", sealed, true);
/*      */ 
/*      */     
/*  277 */     for (String packageName : getTopPackageNames()) {
/*  278 */       new LazilyLoadedCtor(s, packageName, "org.mozilla.javascript.NativeJavaTopPackage", sealed, true);
/*      */     }
/*      */ 
/*      */     
/*  282 */     return s;
/*      */   }
/*      */ 
/*      */   
/*      */   static String[] getTopPackageNames() {
/*  287 */     (new String[7])[0] = "java"; (new String[7])[1] = "javax"; (new String[7])[2] = "org"; (new String[7])[3] = "com"; (new String[7])[4] = "edu"; (new String[7])[5] = "net"; (new String[7])[6] = "android"; (new String[6])[0] = "java"; (new String[6])[1] = "javax"; (new String[6])[2] = "org"; (new String[6])[3] = "com"; (new String[6])[4] = "edu"; (new String[6])[5] = "net"; return "Dalvik".equals(System.getProperty("java.vm.name")) ? new String[7] : new String[6];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ScriptableObject getLibraryScopeOrNull(Scriptable scope) {
/*  295 */     ScriptableObject libScope = (ScriptableObject)ScriptableObject.getTopScopeValue(scope, LIBRARY_SCOPE_KEY);
/*      */     
/*  297 */     return libScope;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isJSLineTerminator(int c) {
/*  305 */     if ((c & 0xDFD0) != 0) {
/*  306 */       return false;
/*      */     }
/*  308 */     return (c == 10 || c == 13 || c == 8232 || c == 8233);
/*      */   }
/*      */   
/*      */   public static boolean isJSWhitespaceOrLineTerminator(int c) {
/*  312 */     return (isStrWhiteSpaceChar(c) || isJSLineTerminator(c));
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
/*      */   static boolean isStrWhiteSpaceChar(int c) {
/*  332 */     switch (c) {
/*      */       case 9:
/*      */       case 10:
/*      */       case 11:
/*      */       case 12:
/*      */       case 13:
/*      */       case 32:
/*      */       case 160:
/*      */       case 8232:
/*      */       case 8233:
/*      */       case 65279:
/*  343 */         return true;
/*      */     } 
/*  345 */     return (Character.getType(c) == 12);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Boolean wrapBoolean(boolean b) {
/*  351 */     return b ? Boolean.TRUE : Boolean.FALSE;
/*      */   }
/*      */ 
/*      */   
/*      */   public static Integer wrapInt(int i) {
/*  356 */     return Integer.valueOf(i);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Number wrapNumber(double x) {
/*  361 */     if (x != x) {
/*  362 */       return NaNobj;
/*      */     }
/*  364 */     return new Double(x);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean toBoolean(Object val) {
/*      */     while (true) {
/*  375 */       if (val instanceof Boolean)
/*  376 */         return ((Boolean)val).booleanValue(); 
/*  377 */       if (val == null || val == Undefined.instance)
/*  378 */         return false; 
/*  379 */       if (val instanceof CharSequence)
/*  380 */         return (((CharSequence)val).length() != 0); 
/*  381 */       if (val instanceof Number) {
/*  382 */         double d = ((Number)val).doubleValue();
/*  383 */         return (d == d && d != 0.0D);
/*      */       } 
/*  385 */       if (val instanceof Scriptable) {
/*  386 */         if (val instanceof ScriptableObject && ((ScriptableObject)val).avoidObjectDetection())
/*      */         {
/*      */           
/*  389 */           return false;
/*      */         }
/*  391 */         if (Context.getContext().isVersionECMA1())
/*      */         {
/*  393 */           return true;
/*      */         }
/*      */         
/*  396 */         val = ((Scriptable)val).getDefaultValue(BooleanClass);
/*  397 */         if (val instanceof Scriptable)
/*  398 */           throw errorWithClassName("msg.primitive.expected", val);  continue;
/*      */       }  break;
/*      */     } 
/*  401 */     warnAboutNonJSObject(val);
/*  402 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double toNumber(Object val) {
/*      */     while (true) {
/*  414 */       if (val instanceof Number)
/*  415 */         return ((Number)val).doubleValue(); 
/*  416 */       if (val == null)
/*  417 */         return 0.0D; 
/*  418 */       if (val == Undefined.instance)
/*  419 */         return NaN; 
/*  420 */       if (val instanceof String)
/*  421 */         return toNumber((String)val); 
/*  422 */       if (val instanceof CharSequence)
/*  423 */         return toNumber(val.toString()); 
/*  424 */       if (val instanceof Boolean)
/*  425 */         return ((Boolean)val).booleanValue() ? 1.0D : 0.0D; 
/*  426 */       if (val instanceof Scriptable) {
/*  427 */         val = ((Scriptable)val).getDefaultValue(NumberClass);
/*  428 */         if (val instanceof Scriptable)
/*  429 */           throw errorWithClassName("msg.primitive.expected", val);  continue;
/*      */       }  break;
/*      */     } 
/*  432 */     warnAboutNonJSObject(val);
/*  433 */     return NaN;
/*      */   }
/*      */ 
/*      */   
/*      */   public static double toNumber(Object[] args, int index) {
/*  438 */     return (index < args.length) ? toNumber(args[index]) : NaN;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  446 */   public static final double NaN = Double.longBitsToDouble(9221120237041090560L);
/*      */ 
/*      */ 
/*      */   
/*  450 */   public static final double negativeZero = Double.longBitsToDouble(Long.MIN_VALUE);
/*      */   
/*  452 */   public static final Double NaNobj = new Double(NaN);
/*      */   private static final String DEFAULT_NS_TAG = "__default_namespace__";
/*      */   public static final int ENUMERATE_KEYS = 0;
/*      */   public static final int ENUMERATE_VALUES = 1;
/*      */   
/*      */   static double stringToNumber(String s, int start, int radix) {
/*  458 */     char digitMax = '9';
/*  459 */     char lowerCaseBound = 'a';
/*  460 */     char upperCaseBound = 'A';
/*  461 */     int len = s.length();
/*  462 */     if (radix < 10) {
/*  463 */       digitMax = (char)(48 + radix - 1);
/*      */     }
/*  465 */     if (radix > 10) {
/*  466 */       lowerCaseBound = (char)(97 + radix - 10);
/*  467 */       upperCaseBound = (char)(65 + radix - 10);
/*      */     } 
/*      */     
/*  470 */     double sum = 0.0D; int end;
/*  471 */     for (end = start; end < len; end++) {
/*  472 */       int newDigit; char c = s.charAt(end);
/*      */       
/*  474 */       if ('0' <= c && c <= digitMax) {
/*  475 */         newDigit = c - 48;
/*  476 */       } else if ('a' <= c && c < lowerCaseBound) {
/*  477 */         newDigit = c - 97 + 10;
/*  478 */       } else if ('A' <= c && c < upperCaseBound) {
/*  479 */         newDigit = c - 65 + 10;
/*      */       } else {
/*      */         break;
/*  482 */       }  sum = sum * radix + newDigit;
/*      */     } 
/*  484 */     if (start == end) {
/*  485 */       return NaN;
/*      */     }
/*  487 */     if (sum >= 9.007199254740992E15D) {
/*  488 */       if (radix == 10)
/*      */         
/*      */         try {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  495 */           return Double.parseDouble(s.substring(start, end));
/*  496 */         } catch (NumberFormatException nfe) {
/*  497 */           return NaN;
/*      */         }  
/*  499 */       if (radix == 2 || radix == 4 || radix == 8 || radix == 16 || radix == 32) {
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
/*  512 */         int bitShiftInChar = 1;
/*  513 */         int digit = 0;
/*      */         
/*  515 */         int SKIP_LEADING_ZEROS = 0;
/*  516 */         int FIRST_EXACT_53_BITS = 1;
/*  517 */         int AFTER_BIT_53 = 2;
/*  518 */         int ZEROS_AFTER_54 = 3;
/*  519 */         int MIXED_AFTER_54 = 4;
/*      */         
/*  521 */         int state = 0;
/*  522 */         int exactBitsLimit = 53;
/*  523 */         double factor = 0.0D;
/*  524 */         boolean bit53 = false;
/*      */         
/*  526 */         boolean bit54 = false;
/*      */         
/*      */         while (true) {
/*  529 */           if (bitShiftInChar == 1) {
/*  530 */             if (start == end)
/*      */               break; 
/*  532 */             digit = s.charAt(start++);
/*  533 */             if (48 <= digit && digit <= 57) {
/*  534 */               digit -= 48;
/*  535 */             } else if (97 <= digit && digit <= 122) {
/*  536 */               digit -= 87;
/*      */             } else {
/*  538 */               digit -= 55;
/*  539 */             }  bitShiftInChar = radix;
/*      */           } 
/*  541 */           bitShiftInChar >>= 1;
/*  542 */           boolean bit = ((digit & bitShiftInChar) != 0);
/*      */           
/*  544 */           switch (state) {
/*      */             case 0:
/*  546 */               if (bit) {
/*  547 */                 exactBitsLimit--;
/*  548 */                 sum = 1.0D;
/*  549 */                 state = 1;
/*      */               } 
/*      */             
/*      */             case 1:
/*  553 */               sum *= 2.0D;
/*  554 */               if (bit)
/*  555 */                 sum++; 
/*  556 */               exactBitsLimit--;
/*  557 */               if (exactBitsLimit == 0) {
/*  558 */                 bit53 = bit;
/*  559 */                 state = 2;
/*      */               } 
/*      */             
/*      */             case 2:
/*  563 */               bit54 = bit;
/*  564 */               factor = 2.0D;
/*  565 */               state = 3;
/*      */             
/*      */             case 3:
/*  568 */               if (bit) {
/*  569 */                 state = 4;
/*      */               }
/*      */             
/*      */             case 4:
/*  573 */               factor *= 2.0D;
/*      */           } 
/*      */         
/*      */         } 
/*  577 */         switch (state) {
/*      */           case 0:
/*  579 */             sum = 0.0D;
/*      */             break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           case 3:
/*  588 */             if (bit54 & bit53)
/*  589 */               sum++; 
/*  590 */             sum *= factor;
/*      */             break;
/*      */ 
/*      */           
/*      */           case 4:
/*  595 */             if (bit54)
/*  596 */               sum++; 
/*  597 */             sum *= factor;
/*      */             break;
/*      */         } 
/*      */       
/*      */       } 
/*      */     } 
/*  603 */     return sum;
/*      */   }
/*      */   
/*      */   public static final int ENUMERATE_ARRAY = 2;
/*      */   public static final int ENUMERATE_KEYS_NO_ITERATOR = 3;
/*      */   public static final int ENUMERATE_VALUES_NO_ITERATOR = 4;
/*      */   public static final int ENUMERATE_ARRAY_NO_ITERATOR = 5;
/*      */   
/*      */   public static double toNumber(String s) {
/*      */     char startChar;
/*  613 */     int len = s.length();
/*  614 */     int start = 0;
/*      */     
/*      */     while (true) {
/*  617 */       if (start == len)
/*      */       {
/*  619 */         return 0.0D;
/*      */       }
/*  621 */       startChar = s.charAt(start);
/*  622 */       if (!isStrWhiteSpaceChar(startChar))
/*      */         break; 
/*  624 */       start++;
/*      */     } 
/*      */     
/*  627 */     if (startChar == '0') {
/*  628 */       if (start + 2 < len) {
/*  629 */         int c1 = s.charAt(start + 1);
/*  630 */         if (c1 == 120 || c1 == 88)
/*      */         {
/*  632 */           return stringToNumber(s, start + 2, 16);
/*      */         }
/*      */       } 
/*  635 */     } else if ((startChar == '+' || startChar == '-') && 
/*  636 */       start + 3 < len && s.charAt(start + 1) == '0') {
/*  637 */       int c2 = s.charAt(start + 2);
/*  638 */       if (c2 == 120 || c2 == 88) {
/*      */         
/*  640 */         double val = stringToNumber(s, start + 3, 16);
/*  641 */         return (startChar == '-') ? -val : val;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  646 */     int end = len - 1;
/*      */     char endChar;
/*  648 */     while (isStrWhiteSpaceChar(endChar = s.charAt(end)))
/*  649 */       end--; 
/*  650 */     if (endChar == 'y') {
/*      */       
/*  652 */       if (startChar == '+' || startChar == '-')
/*  653 */         start++; 
/*  654 */       if (start + 7 == end && s.regionMatches(start, "Infinity", 0, 8)) {
/*  655 */         return (startChar == '-') ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
/*      */       }
/*      */       
/*  658 */       return NaN;
/*      */     } 
/*      */ 
/*      */     
/*  662 */     String sub = s.substring(start, end + 1);
/*      */ 
/*      */     
/*  665 */     for (int i = sub.length() - 1; i >= 0; ) {
/*  666 */       char c = sub.charAt(i);
/*  667 */       if (('0' <= c && c <= '9') || c == '.' || c == 'e' || c == 'E' || c == '+' || c == '-') {
/*      */         i--;
/*      */         continue;
/*      */       } 
/*  671 */       return NaN;
/*      */     } 
/*      */     try {
/*  674 */       return Double.parseDouble(sub);
/*  675 */     } catch (NumberFormatException ex) {
/*  676 */       return NaN;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object[] padArguments(Object[] args, int count) {
/*  687 */     if (count < args.length) {
/*  688 */       return args;
/*      */     }
/*      */     
/*  691 */     Object[] result = new Object[count]; int i;
/*  692 */     for (i = 0; i < args.length; i++) {
/*  693 */       result[i] = args[i];
/*      */     }
/*      */     
/*  696 */     for (; i < count; i++) {
/*  697 */       result[i] = Undefined.instance;
/*      */     }
/*      */     
/*  700 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public static String escapeString(String s) {
/*  705 */     return escapeString(s, '"');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String escapeString(String s, char escapeQuote) {
/*  714 */     if (escapeQuote != '"' && escapeQuote != '\'') Kit.codeBug(); 
/*  715 */     StringBuilder sb = null;
/*      */     
/*  717 */     for (int i = 0, L = s.length(); i != L; i++) {
/*  718 */       int c = s.charAt(i);
/*      */       
/*  720 */       if (32 <= c && c <= 126 && c != escapeQuote && c != 92) {
/*      */ 
/*      */         
/*  723 */         if (sb != null) {
/*  724 */           sb.append((char)c);
/*      */         }
/*      */       } else {
/*      */         
/*  728 */         if (sb == null) {
/*  729 */           sb = new StringBuilder(L + 3);
/*  730 */           sb.append(s);
/*  731 */           sb.setLength(i);
/*      */         } 
/*      */         
/*  734 */         int escape = -1;
/*  735 */         switch (c) { case 8:
/*  736 */             escape = 98; break;
/*  737 */           case 12: escape = 102; break;
/*  738 */           case 10: escape = 110; break;
/*  739 */           case 13: escape = 114; break;
/*  740 */           case 9: escape = 116; break;
/*  741 */           case 11: escape = 118; break;
/*  742 */           case 32: escape = 32; break;
/*  743 */           case 92: escape = 92; break; }
/*      */         
/*  745 */         if (escape >= 0) {
/*      */           
/*  747 */           sb.append('\\');
/*  748 */           sb.append((char)escape);
/*  749 */         } else if (c == escapeQuote) {
/*  750 */           sb.append('\\');
/*  751 */           sb.append(escapeQuote);
/*      */         } else {
/*      */           int hexSize;
/*  754 */           if (c < 256) {
/*      */             
/*  756 */             sb.append("\\x");
/*  757 */             hexSize = 2;
/*      */           } else {
/*      */             
/*  760 */             sb.append("\\u");
/*  761 */             hexSize = 4;
/*      */           } 
/*      */           
/*  764 */           for (int shift = (hexSize - 1) * 4; shift >= 0; shift -= 4) {
/*  765 */             int digit = 0xF & c >> shift;
/*  766 */             int hc = (digit < 10) ? (48 + digit) : (87 + digit);
/*  767 */             sb.append((char)hc);
/*      */           } 
/*      */         } 
/*      */       } 
/*  771 */     }  return (sb == null) ? s : sb.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean isValidIdentifierName(String s) {
/*  776 */     int L = s.length();
/*  777 */     if (L == 0)
/*  778 */       return false; 
/*  779 */     if (!Character.isJavaIdentifierStart(s.charAt(0)))
/*  780 */       return false; 
/*  781 */     for (int i = 1; i != L; i++) {
/*  782 */       if (!Character.isJavaIdentifierPart(s.charAt(i)))
/*  783 */         return false; 
/*      */     } 
/*  785 */     return !TokenStream.isKeyword(s);
/*      */   }
/*      */   
/*      */   public static CharSequence toCharSequence(Object val) {
/*  789 */     if (val instanceof NativeString) {
/*  790 */       return ((NativeString)val).toCharSequence();
/*      */     }
/*  792 */     return (val instanceof CharSequence) ? (CharSequence)val : toString(val);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(Object val) {
/*      */     while (true) {
/*  802 */       if (val == null) {
/*  803 */         return "null";
/*      */       }
/*  805 */       if (val == Undefined.instance) {
/*  806 */         return "undefined";
/*      */       }
/*  808 */       if (val instanceof String) {
/*  809 */         return (String)val;
/*      */       }
/*  811 */       if (val instanceof CharSequence) {
/*  812 */         return val.toString();
/*      */       }
/*  814 */       if (val instanceof Number)
/*      */       {
/*      */         
/*  817 */         return numberToString(((Number)val).doubleValue(), 10);
/*      */       }
/*  819 */       if (val instanceof Scriptable) {
/*  820 */         val = ((Scriptable)val).getDefaultValue(StringClass);
/*  821 */         if (val instanceof Scriptable)
/*  822 */           throw errorWithClassName("msg.primitive.expected", val);  continue;
/*      */       } 
/*      */       break;
/*      */     } 
/*  826 */     return val.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static String defaultObjectToString(Scriptable obj) {
/*  832 */     return "[object " + obj.getClassName() + ']';
/*      */   }
/*      */ 
/*      */   
/*      */   public static String toString(Object[] args, int index) {
/*  837 */     return (index < args.length) ? toString(args[index]) : "undefined";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(double val) {
/*  844 */     return numberToString(val, 10);
/*      */   }
/*      */   
/*      */   public static String numberToString(double d, int base) {
/*  848 */     if (base < 2 || base > 36) {
/*  849 */       throw Context.reportRuntimeError1("msg.bad.radix", Integer.toString(base));
/*      */     }
/*      */ 
/*      */     
/*  853 */     if (d != d)
/*  854 */       return "NaN"; 
/*  855 */     if (d == Double.POSITIVE_INFINITY)
/*  856 */       return "Infinity"; 
/*  857 */     if (d == Double.NEGATIVE_INFINITY)
/*  858 */       return "-Infinity"; 
/*  859 */     if (d == 0.0D) {
/*  860 */       return "0";
/*      */     }
/*  862 */     if (base != 10) {
/*  863 */       return DToA.JS_dtobasestr(base, d);
/*      */     }
/*      */ 
/*      */     
/*  867 */     String result = FastDtoa.numberToString(d);
/*  868 */     if (result != null) {
/*  869 */       return result;
/*      */     }
/*  871 */     StringBuilder buffer = new StringBuilder();
/*  872 */     DToA.JS_dtostr(buffer, 0, 0, d);
/*  873 */     return buffer.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static String uneval(Context cx, Scriptable scope, Object value) {
/*  880 */     if (value == null) {
/*  881 */       return "null";
/*      */     }
/*  883 */     if (value == Undefined.instance) {
/*  884 */       return "undefined";
/*      */     }
/*  886 */     if (value instanceof CharSequence) {
/*  887 */       String escaped = escapeString(value.toString());
/*  888 */       StringBuilder sb = new StringBuilder(escaped.length() + 2);
/*  889 */       sb.append('"');
/*  890 */       sb.append(escaped);
/*  891 */       sb.append('"');
/*  892 */       return sb.toString();
/*      */     } 
/*  894 */     if (value instanceof Number) {
/*  895 */       double d = ((Number)value).doubleValue();
/*  896 */       if (d == 0.0D && 1.0D / d < 0.0D) {
/*  897 */         return "-0";
/*      */       }
/*  899 */       return toString(d);
/*      */     } 
/*  901 */     if (value instanceof Boolean) {
/*  902 */       return toString(value);
/*      */     }
/*  904 */     if (value instanceof Scriptable) {
/*  905 */       Scriptable obj = (Scriptable)value;
/*      */ 
/*      */       
/*  908 */       if (ScriptableObject.hasProperty(obj, "toSource")) {
/*  909 */         Object v = ScriptableObject.getProperty(obj, "toSource");
/*  910 */         if (v instanceof Function) {
/*  911 */           Function f = (Function)v;
/*  912 */           return toString(f.call(cx, scope, obj, emptyArgs));
/*      */         } 
/*      */       } 
/*  915 */       return toString(value);
/*      */     } 
/*  917 */     warnAboutNonJSObject(value);
/*  918 */     return value.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static String defaultObjectToSource(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/*      */     boolean toplevel, iterating;
/*  925 */     if (cx.iterating == null) {
/*  926 */       toplevel = true;
/*  927 */       iterating = false;
/*  928 */       cx.iterating = new ObjToIntMap(31);
/*      */     } else {
/*  930 */       toplevel = false;
/*  931 */       iterating = cx.iterating.has(thisObj);
/*      */     } 
/*      */     
/*  934 */     StringBuilder result = new StringBuilder(128);
/*  935 */     if (toplevel) {
/*  936 */       result.append("(");
/*      */     }
/*  938 */     result.append('{');
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  943 */       if (!iterating) {
/*  944 */         cx.iterating.intern(thisObj);
/*  945 */         Object[] ids = thisObj.getIds();
/*  946 */         for (int i = 0; i < ids.length; i++) {
/*  947 */           Object value, id = ids[i];
/*      */           
/*  949 */           if (id instanceof Integer) {
/*  950 */             int intId = ((Integer)id).intValue();
/*  951 */             value = thisObj.get(intId, thisObj);
/*  952 */             if (value == Scriptable.NOT_FOUND)
/*      */               continue; 
/*  954 */             if (i > 0)
/*  955 */               result.append(", "); 
/*  956 */             result.append(intId);
/*      */           } else {
/*  958 */             String strId = (String)id;
/*  959 */             value = thisObj.get(strId, thisObj);
/*  960 */             if (value == Scriptable.NOT_FOUND)
/*      */               continue; 
/*  962 */             if (i > 0)
/*  963 */               result.append(", "); 
/*  964 */             if (isValidIdentifierName(strId)) {
/*  965 */               result.append(strId);
/*      */             } else {
/*  967 */               result.append('\'');
/*  968 */               result.append(escapeString(strId, '\''));
/*      */               
/*  970 */               result.append('\'');
/*      */             } 
/*      */           } 
/*  973 */           result.append(':');
/*  974 */           result.append(uneval(cx, scope, value)); continue;
/*      */         } 
/*      */       } 
/*      */     } finally {
/*  978 */       if (toplevel) {
/*  979 */         cx.iterating = null;
/*      */       }
/*      */     } 
/*      */     
/*  983 */     result.append('}');
/*  984 */     if (toplevel) {
/*  985 */       result.append(')');
/*      */     }
/*  987 */     return result.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   public static Scriptable toObject(Scriptable scope, Object val) {
/*  992 */     if (val instanceof Scriptable) {
/*  993 */       return (Scriptable)val;
/*      */     }
/*  995 */     return toObject(Context.getContext(), scope, val);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static Scriptable toObjectOrNull(Context cx, Object obj) {
/* 1007 */     if (obj instanceof Scriptable)
/* 1008 */       return (Scriptable)obj; 
/* 1009 */     if (obj != null && obj != Undefined.instance) {
/* 1010 */       return toObject(cx, getTopCallScope(cx), obj);
/*      */     }
/* 1012 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Scriptable toObjectOrNull(Context cx, Object obj, Scriptable scope) {
/* 1021 */     if (obj instanceof Scriptable)
/* 1022 */       return (Scriptable)obj; 
/* 1023 */     if (obj != null && obj != Undefined.instance) {
/* 1024 */       return toObject(cx, scope, obj);
/*      */     }
/* 1026 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static Scriptable toObject(Scriptable scope, Object val, Class<?> staticClass) {
/* 1036 */     if (val instanceof Scriptable) {
/* 1037 */       return (Scriptable)val;
/*      */     }
/* 1039 */     return toObject(Context.getContext(), scope, val);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Scriptable toObject(Context cx, Scriptable scope, Object val) {
/* 1049 */     if (val instanceof Scriptable) {
/* 1050 */       return (Scriptable)val;
/*      */     }
/* 1052 */     if (val instanceof CharSequence) {
/*      */       
/* 1054 */       NativeString result = new NativeString((CharSequence)val);
/* 1055 */       setBuiltinProtoAndParent(result, scope, TopLevel.Builtins.String);
/* 1056 */       return result;
/*      */     } 
/* 1058 */     if (val instanceof Number) {
/* 1059 */       NativeNumber result = new NativeNumber(((Number)val).doubleValue());
/* 1060 */       setBuiltinProtoAndParent(result, scope, TopLevel.Builtins.Number);
/* 1061 */       return result;
/*      */     } 
/* 1063 */     if (val instanceof Boolean) {
/* 1064 */       NativeBoolean result = new NativeBoolean(((Boolean)val).booleanValue());
/* 1065 */       setBuiltinProtoAndParent(result, scope, TopLevel.Builtins.Boolean);
/* 1066 */       return result;
/*      */     } 
/* 1068 */     if (val == null) {
/* 1069 */       throw typeError0("msg.null.to.object");
/*      */     }
/* 1071 */     if (val == Undefined.instance) {
/* 1072 */       throw typeError0("msg.undef.to.object");
/*      */     }
/*      */ 
/*      */     
/* 1076 */     Object wrapped = cx.getWrapFactory().wrap(cx, scope, val, null);
/* 1077 */     if (wrapped instanceof Scriptable)
/* 1078 */       return (Scriptable)wrapped; 
/* 1079 */     throw errorWithClassName("msg.invalid.type", val);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static Scriptable toObject(Context cx, Scriptable scope, Object val, Class<?> staticClass) {
/* 1089 */     return toObject(cx, scope, val);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static Object call(Context cx, Object fun, Object thisArg, Object[] args, Scriptable scope) {
/* 1099 */     if (!(fun instanceof Function)) {
/* 1100 */       throw notFunctionError(toString(fun));
/*      */     }
/* 1102 */     Function function = (Function)fun;
/* 1103 */     Scriptable thisObj = toObjectOrNull(cx, thisArg, scope);
/* 1104 */     if (thisObj == null) {
/* 1105 */       throw undefCallError(thisObj, "function");
/*      */     }
/* 1107 */     return function.call(cx, scope, thisObj, args);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Scriptable newObject(Context cx, Scriptable scope, String constructorName, Object[] args) {
/* 1113 */     scope = ScriptableObject.getTopLevelScope(scope);
/* 1114 */     Function ctor = getExistingCtor(cx, scope, constructorName);
/* 1115 */     if (args == null) args = emptyArgs; 
/* 1116 */     return ctor.construct(cx, scope, args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Scriptable newBuiltinObject(Context cx, Scriptable scope, TopLevel.Builtins type, Object[] args) {
/* 1123 */     scope = ScriptableObject.getTopLevelScope(scope);
/* 1124 */     Function ctor = TopLevel.getBuiltinCtor(cx, scope, type);
/* 1125 */     if (args == null) args = emptyArgs; 
/* 1126 */     return ctor.construct(cx, scope, args);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static Scriptable newNativeError(Context cx, Scriptable scope, TopLevel.NativeErrors type, Object[] args) {
/* 1132 */     scope = ScriptableObject.getTopLevelScope(scope);
/* 1133 */     Function ctor = TopLevel.getNativeErrorCtor(cx, scope, type);
/* 1134 */     if (args == null) args = emptyArgs; 
/* 1135 */     return ctor.construct(cx, scope, args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double toInteger(Object val) {
/* 1143 */     return toInteger(toNumber(val));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static double toInteger(double d) {
/* 1149 */     if (d != d) {
/* 1150 */       return 0.0D;
/*      */     }
/* 1152 */     if (d == 0.0D || d == Double.POSITIVE_INFINITY || d == Double.NEGATIVE_INFINITY)
/*      */     {
/*      */       
/* 1155 */       return d;
/*      */     }
/* 1157 */     if (d > 0.0D) {
/* 1158 */       return Math.floor(d);
/*      */     }
/* 1160 */     return Math.ceil(d);
/*      */   }
/*      */   
/*      */   public static double toInteger(Object[] args, int index) {
/* 1164 */     return (index < args.length) ? toInteger(args[index]) : 0.0D;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int toInt32(Object val) {
/* 1174 */     if (val instanceof Integer) {
/* 1175 */       return ((Integer)val).intValue();
/*      */     }
/* 1177 */     return toInt32(toNumber(val));
/*      */   }
/*      */   
/*      */   public static int toInt32(Object[] args, int index) {
/* 1181 */     return (index < args.length) ? toInt32(args[index]) : 0;
/*      */   }
/*      */   
/*      */   public static int toInt32(double d) {
/* 1185 */     return DoubleConversion.doubleToInt32(d);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long toUint32(double d) {
/* 1193 */     return DoubleConversion.doubleToInt32(d) & 0xFFFFFFFFL;
/*      */   }
/*      */   
/*      */   public static long toUint32(Object val) {
/* 1197 */     return toUint32(toNumber(val));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char toUint16(Object val) {
/* 1205 */     double d = toNumber(val);
/* 1206 */     return (char)DoubleConversion.doubleToInt32(d);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object setDefaultNamespace(Object namespace, Context cx) {
/* 1215 */     Scriptable scope = cx.currentActivationCall;
/* 1216 */     if (scope == null) {
/* 1217 */       scope = getTopCallScope(cx);
/*      */     }
/*      */     
/* 1220 */     XMLLib xmlLib = currentXMLLib(cx);
/* 1221 */     Object ns = xmlLib.toDefaultXmlNamespace(cx, namespace);
/*      */ 
/*      */     
/* 1224 */     if (!scope.has("__default_namespace__", scope)) {
/*      */       
/* 1226 */       ScriptableObject.defineProperty(scope, "__default_namespace__", ns, 6);
/*      */     }
/*      */     else {
/*      */       
/* 1230 */       scope.put("__default_namespace__", scope, ns);
/*      */     } 
/*      */     
/* 1233 */     return Undefined.instance;
/*      */   }
/*      */   
/*      */   public static Object searchDefaultNamespace(Context cx) {
/*      */     Object nsObject;
/* 1238 */     Scriptable scope = cx.currentActivationCall;
/* 1239 */     if (scope == null) {
/* 1240 */       scope = getTopCallScope(cx);
/*      */     }
/*      */     
/*      */     while (true) {
/* 1244 */       Scriptable parent = scope.getParentScope();
/* 1245 */       if (parent == null) {
/* 1246 */         Object object = ScriptableObject.getProperty(scope, "__default_namespace__");
/* 1247 */         if (object == Scriptable.NOT_FOUND) {
/* 1248 */           return null;
/*      */         }
/*      */         break;
/*      */       } 
/* 1252 */       nsObject = scope.get("__default_namespace__", scope);
/* 1253 */       if (nsObject != Scriptable.NOT_FOUND) {
/*      */         break;
/*      */       }
/* 1256 */       scope = parent;
/*      */     } 
/* 1258 */     return nsObject;
/*      */   }
/*      */   
/*      */   public static Object getTopLevelProp(Scriptable scope, String id) {
/* 1262 */     scope = ScriptableObject.getTopLevelScope(scope);
/* 1263 */     return ScriptableObject.getProperty(scope, id);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static Function getExistingCtor(Context cx, Scriptable scope, String constructorName) {
/* 1269 */     Object ctorVal = ScriptableObject.getProperty(scope, constructorName);
/* 1270 */     if (ctorVal instanceof Function) {
/* 1271 */       return (Function)ctorVal;
/*      */     }
/* 1273 */     if (ctorVal == Scriptable.NOT_FOUND) {
/* 1274 */       throw Context.reportRuntimeError1("msg.ctor.not.found", constructorName);
/*      */     }
/*      */     
/* 1277 */     throw Context.reportRuntimeError1("msg.not.ctor", constructorName);
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
/*      */   public static long indexFromString(String str) {
/* 1291 */     int MAX_VALUE_LENGTH = 10;
/*      */     
/* 1293 */     int len = str.length();
/* 1294 */     if (len > 0) {
/* 1295 */       int i = 0;
/* 1296 */       boolean negate = false;
/* 1297 */       int c = str.charAt(0);
/* 1298 */       if (c == 45 && 
/* 1299 */         len > 1) {
/* 1300 */         c = str.charAt(1);
/* 1301 */         if (c == 48) return -1L; 
/* 1302 */         i = 1;
/* 1303 */         negate = true;
/*      */       } 
/*      */       
/* 1306 */       c -= 48;
/* 1307 */       if (0 <= c && c <= 9 && len <= (negate ? 11 : 10)) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1313 */         int index = -c;
/* 1314 */         int oldIndex = 0;
/* 1315 */         i++;
/* 1316 */         if (index != 0)
/*      */         {
/* 1318 */           while (i != len && 0 <= (c = str.charAt(i) - 48) && c <= 9) {
/*      */             
/* 1320 */             oldIndex = index;
/* 1321 */             index = 10 * index - c;
/* 1322 */             i++;
/*      */           } 
/*      */         }
/*      */ 
/*      */         
/* 1327 */         if (i == len && (oldIndex > -214748364 || (oldIndex == -214748364 && c <= (negate ? 8 : 7))))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1333 */           return 0xFFFFFFFFL & (negate ? index : -index);
/*      */         }
/*      */       } 
/*      */     } 
/* 1337 */     return -1L;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long testUint32String(String str) {
/* 1348 */     int MAX_VALUE_LENGTH = 10;
/*      */     
/* 1350 */     int len = str.length();
/* 1351 */     if (1 <= len && len <= 10) {
/* 1352 */       int c = str.charAt(0);
/* 1353 */       c -= 48;
/* 1354 */       if (c == 0)
/*      */       {
/* 1356 */         return (len == 1) ? 0L : -1L;
/*      */       }
/* 1358 */       if (1 <= c && c <= 9) {
/* 1359 */         long v = c;
/* 1360 */         for (int i = 1; i != len; i++) {
/* 1361 */           c = str.charAt(i) - 48;
/* 1362 */           if (0 > c || c > 9) {
/* 1363 */             return -1L;
/*      */           }
/* 1365 */           v = 10L * v + c;
/*      */         } 
/*      */         
/* 1368 */         if (v >>> 32L == 0L) {
/* 1369 */           return v;
/*      */         }
/*      */       } 
/*      */     } 
/* 1373 */     return -1L;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Object getIndexObject(String s) {
/* 1382 */     long indexTest = indexFromString(s);
/* 1383 */     if (indexTest >= 0L) {
/* 1384 */       return Integer.valueOf((int)indexTest);
/*      */     }
/* 1386 */     return s;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Object getIndexObject(double d) {
/* 1395 */     int i = (int)d;
/* 1396 */     if (i == d) {
/* 1397 */       return Integer.valueOf(i);
/*      */     }
/* 1399 */     return toString(d);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static String toStringIdOrIndex(Context cx, Object id) {
/*      */     String s;
/* 1409 */     if (id instanceof Number) {
/* 1410 */       double d = ((Number)id).doubleValue();
/* 1411 */       int index = (int)d;
/* 1412 */       if (index == d) {
/* 1413 */         storeIndexResult(cx, index);
/* 1414 */         return null;
/*      */       } 
/* 1416 */       return toString(id);
/*      */     } 
/*      */     
/* 1419 */     if (id instanceof String) {
/* 1420 */       s = (String)id;
/*      */     } else {
/* 1422 */       s = toString(id);
/*      */     } 
/* 1424 */     long indexTest = indexFromString(s);
/* 1425 */     if (indexTest >= 0L) {
/* 1426 */       storeIndexResult(cx, (int)indexTest);
/* 1427 */       return null;
/*      */     } 
/* 1429 */     return s;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static Object getObjectElem(Object obj, Object elem, Context cx) {
/* 1441 */     return getObjectElem(obj, elem, cx, getTopCallScope(cx));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object getObjectElem(Object obj, Object elem, Context cx, Scriptable scope) {
/* 1449 */     Scriptable sobj = toObjectOrNull(cx, obj, scope);
/* 1450 */     if (sobj == null) {
/* 1451 */       throw undefReadError(obj, elem);
/*      */     }
/* 1453 */     return getObjectElem(sobj, elem, cx);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object getObjectElem(Scriptable obj, Object elem, Context cx) {
/*      */     Object result;
/* 1462 */     if (obj instanceof XMLObject) {
/* 1463 */       result = ((XMLObject)obj).get(cx, elem);
/*      */     } else {
/* 1465 */       String s = toStringIdOrIndex(cx, elem);
/* 1466 */       if (s == null) {
/* 1467 */         int index = lastIndexResult(cx);
/* 1468 */         result = ScriptableObject.getProperty(obj, index);
/*      */       } else {
/* 1470 */         result = ScriptableObject.getProperty(obj, s);
/*      */       } 
/*      */     } 
/*      */     
/* 1474 */     if (result == Scriptable.NOT_FOUND) {
/* 1475 */       result = Undefined.instance;
/*      */     }
/*      */     
/* 1478 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static Object getObjectProp(Object obj, String property, Context cx) {
/* 1490 */     return getObjectProp(obj, property, cx, getTopCallScope(cx));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object getObjectProp(Object obj, String property, Context cx, Scriptable scope) {
/* 1501 */     Scriptable sobj = toObjectOrNull(cx, obj, scope);
/* 1502 */     if (sobj == null) {
/* 1503 */       throw undefReadError(obj, property);
/*      */     }
/* 1505 */     return getObjectProp(sobj, property, cx);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object getObjectProp(Scriptable obj, String property, Context cx) {
/* 1512 */     Object result = ScriptableObject.getProperty(obj, property);
/* 1513 */     if (result == Scriptable.NOT_FOUND) {
/* 1514 */       if (cx.hasFeature(11)) {
/* 1515 */         Context.reportWarning(getMessage1("msg.ref.undefined.prop", property));
/*      */       }
/*      */       
/* 1518 */       result = Undefined.instance;
/*      */     } 
/*      */     
/* 1521 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static Object getObjectPropNoWarn(Object obj, String property, Context cx) {
/* 1531 */     return getObjectPropNoWarn(obj, property, cx, getTopCallScope(cx));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object getObjectPropNoWarn(Object obj, String property, Context cx, Scriptable scope) {
/* 1537 */     Scriptable sobj = toObjectOrNull(cx, obj, scope);
/* 1538 */     if (sobj == null) {
/* 1539 */       throw undefReadError(obj, property);
/*      */     }
/* 1541 */     Object result = ScriptableObject.getProperty(sobj, property);
/* 1542 */     if (result == Scriptable.NOT_FOUND) {
/* 1543 */       return Undefined.instance;
/*      */     }
/* 1545 */     return result;
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
/*      */   @Deprecated
/*      */   public static Object getObjectIndex(Object obj, double dblIndex, Context cx) {
/* 1558 */     return getObjectIndex(obj, dblIndex, cx, getTopCallScope(cx));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object getObjectIndex(Object obj, double dblIndex, Context cx, Scriptable scope) {
/* 1568 */     Scriptable sobj = toObjectOrNull(cx, obj, scope);
/* 1569 */     if (sobj == null) {
/* 1570 */       throw undefReadError(obj, toString(dblIndex));
/*      */     }
/*      */     
/* 1573 */     int index = (int)dblIndex;
/* 1574 */     if (index == dblIndex) {
/* 1575 */       return getObjectIndex(sobj, index, cx);
/*      */     }
/* 1577 */     String s = toString(dblIndex);
/* 1578 */     return getObjectProp(sobj, s, cx);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object getObjectIndex(Scriptable obj, int index, Context cx) {
/* 1585 */     Object result = ScriptableObject.getProperty(obj, index);
/* 1586 */     if (result == Scriptable.NOT_FOUND) {
/* 1587 */       result = Undefined.instance;
/*      */     }
/*      */     
/* 1590 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static Object setObjectElem(Object obj, Object elem, Object value, Context cx) {
/* 1602 */     return setObjectElem(obj, elem, value, cx, getTopCallScope(cx));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object setObjectElem(Object obj, Object elem, Object value, Context cx, Scriptable scope) {
/* 1611 */     Scriptable sobj = toObjectOrNull(cx, obj, scope);
/* 1612 */     if (sobj == null) {
/* 1613 */       throw undefWriteError(obj, elem, value);
/*      */     }
/* 1615 */     return setObjectElem(sobj, elem, value, cx);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object setObjectElem(Scriptable obj, Object elem, Object value, Context cx) {
/* 1621 */     if (obj instanceof XMLObject) {
/* 1622 */       ((XMLObject)obj).put(cx, elem, value);
/*      */     } else {
/* 1624 */       String s = toStringIdOrIndex(cx, elem);
/* 1625 */       if (s == null) {
/* 1626 */         int index = lastIndexResult(cx);
/* 1627 */         ScriptableObject.putProperty(obj, index, value);
/*      */       } else {
/* 1629 */         ScriptableObject.putProperty(obj, s, value);
/*      */       } 
/*      */     } 
/*      */     
/* 1633 */     return value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static Object setObjectProp(Object obj, String property, Object value, Context cx) {
/* 1645 */     return setObjectProp(obj, property, value, cx, getTopCallScope(cx));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object setObjectProp(Object obj, String property, Object value, Context cx, Scriptable scope) {
/* 1655 */     Scriptable sobj = toObjectOrNull(cx, obj, scope);
/* 1656 */     if (sobj == null) {
/* 1657 */       throw undefWriteError(obj, property, value);
/*      */     }
/* 1659 */     return setObjectProp(sobj, property, value, cx);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object setObjectProp(Scriptable obj, String property, Object value, Context cx) {
/* 1665 */     ScriptableObject.putProperty(obj, property, value);
/* 1666 */     return value;
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
/*      */   @Deprecated
/*      */   public static Object setObjectIndex(Object obj, double dblIndex, Object value, Context cx) {
/* 1679 */     return setObjectIndex(obj, dblIndex, value, cx, getTopCallScope(cx));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object setObjectIndex(Object obj, double dblIndex, Object value, Context cx, Scriptable scope) {
/* 1690 */     Scriptable sobj = toObjectOrNull(cx, obj, scope);
/* 1691 */     if (sobj == null) {
/* 1692 */       throw undefWriteError(obj, String.valueOf(dblIndex), value);
/*      */     }
/*      */     
/* 1695 */     int index = (int)dblIndex;
/* 1696 */     if (index == dblIndex) {
/* 1697 */       return setObjectIndex(sobj, index, value, cx);
/*      */     }
/* 1699 */     String s = toString(dblIndex);
/* 1700 */     return setObjectProp(sobj, s, value, cx);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object setObjectIndex(Scriptable obj, int index, Object value, Context cx) {
/* 1707 */     ScriptableObject.putProperty(obj, index, value);
/* 1708 */     return value;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean deleteObjectElem(Scriptable target, Object elem, Context cx) {
/* 1714 */     String s = toStringIdOrIndex(cx, elem);
/* 1715 */     if (s == null) {
/* 1716 */       int index = lastIndexResult(cx);
/* 1717 */       target.delete(index);
/* 1718 */       return !target.has(index, target);
/*      */     } 
/* 1720 */     target.delete(s);
/* 1721 */     return !target.has(s, target);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean hasObjectElem(Scriptable target, Object elem, Context cx) {
/*      */     boolean result;
/* 1730 */     String s = toStringIdOrIndex(cx, elem);
/* 1731 */     if (s == null) {
/* 1732 */       int index = lastIndexResult(cx);
/* 1733 */       result = ScriptableObject.hasProperty(target, index);
/*      */     } else {
/* 1735 */       result = ScriptableObject.hasProperty(target, s);
/*      */     } 
/*      */     
/* 1738 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public static Object refGet(Ref ref, Context cx) {
/* 1743 */     return ref.get(cx);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static Object refSet(Ref ref, Object value, Context cx) {
/* 1752 */     return refSet(ref, value, cx, getTopCallScope(cx));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object refSet(Ref ref, Object value, Context cx, Scriptable scope) {
/* 1758 */     return ref.set(cx, scope, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Object refDel(Ref ref, Context cx) {
/* 1763 */     return wrapBoolean(ref.delete(cx));
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean isSpecialProperty(String s) {
/* 1768 */     return (s.equals("__proto__") || s.equals("__parent__"));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static Ref specialRef(Object obj, String specialProperty, Context cx) {
/* 1778 */     return specialRef(obj, specialProperty, cx, getTopCallScope(cx));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Ref specialRef(Object obj, String specialProperty, Context cx, Scriptable scope) {
/* 1784 */     return SpecialRef.createSpecial(cx, scope, obj, specialProperty);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static Object delete(Object obj, Object id, Context cx) {
/* 1793 */     return delete(obj, id, cx, false);
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
/*      */   @Deprecated
/*      */   public static Object delete(Object obj, Object id, Context cx, boolean isName) {
/* 1812 */     return delete(obj, id, cx, getTopCallScope(cx), isName);
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
/*      */   public static Object delete(Object obj, Object id, Context cx, Scriptable scope, boolean isName) {
/* 1829 */     Scriptable sobj = toObjectOrNull(cx, obj, scope);
/* 1830 */     if (sobj == null) {
/* 1831 */       if (isName) {
/* 1832 */         return Boolean.TRUE;
/*      */       }
/* 1834 */       throw undefDeleteError(obj, id);
/*      */     } 
/* 1836 */     boolean result = deleteObjectElem(sobj, id, cx);
/* 1837 */     return wrapBoolean(result);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object name(Context cx, Scriptable scope, String name) {
/* 1845 */     Scriptable parent = scope.getParentScope();
/* 1846 */     if (parent == null) {
/* 1847 */       Object result = topScopeName(cx, scope, name);
/* 1848 */       if (result == Scriptable.NOT_FOUND) {
/* 1849 */         throw notFoundError(scope, name);
/*      */       }
/* 1851 */       return result;
/*      */     } 
/*      */     
/* 1854 */     return nameOrFunction(cx, scope, parent, name, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Object nameOrFunction(Context cx, Scriptable scope, Scriptable parentScope, String name, boolean asFunctionCall) {
/*      */     Object result;
/* 1862 */     Scriptable thisObj = scope;
/*      */     
/* 1864 */     XMLObject firstXMLObject = null;
/*      */     while (true) {
/* 1866 */       if (scope instanceof NativeWith) {
/* 1867 */         Scriptable withObj = scope.getPrototype();
/* 1868 */         if (withObj instanceof XMLObject) {
/* 1869 */           XMLObject xmlObj = (XMLObject)withObj;
/* 1870 */           if (xmlObj.has(name, (Scriptable)xmlObj)) {
/*      */             
/* 1872 */             XMLObject xMLObject = xmlObj;
/* 1873 */             result = xmlObj.get(name, (Scriptable)xmlObj);
/*      */             break;
/*      */           } 
/* 1876 */           if (firstXMLObject == null) {
/* 1877 */             firstXMLObject = xmlObj;
/*      */           }
/*      */         } else {
/* 1880 */           result = ScriptableObject.getProperty(withObj, name);
/* 1881 */           if (result != Scriptable.NOT_FOUND) {
/*      */             
/* 1883 */             thisObj = withObj;
/*      */             break;
/*      */           } 
/*      */         } 
/* 1887 */       } else if (scope instanceof NativeCall) {
/*      */ 
/*      */         
/* 1890 */         result = scope.get(name, scope);
/* 1891 */         if (result != Scriptable.NOT_FOUND) {
/* 1892 */           if (asFunctionCall)
/*      */           {
/*      */             
/* 1895 */             thisObj = ScriptableObject.getTopLevelScope(parentScope);
/*      */           }
/*      */ 
/*      */ 
/*      */           
/*      */           break;
/*      */         } 
/*      */       } else {
/* 1903 */         result = ScriptableObject.getProperty(scope, name);
/* 1904 */         if (result != Scriptable.NOT_FOUND) {
/* 1905 */           thisObj = scope;
/*      */           break;
/*      */         } 
/*      */       } 
/* 1909 */       scope = parentScope;
/* 1910 */       parentScope = parentScope.getParentScope();
/* 1911 */       if (parentScope == null) {
/* 1912 */         result = topScopeName(cx, scope, name);
/* 1913 */         if (result == Scriptable.NOT_FOUND) {
/* 1914 */           if (firstXMLObject == null || asFunctionCall) {
/* 1915 */             throw notFoundError(scope, name);
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1921 */           result = firstXMLObject.get(name, (Scriptable)firstXMLObject);
/*      */         } 
/*      */         
/* 1924 */         thisObj = scope;
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/* 1929 */     if (asFunctionCall) {
/* 1930 */       if (!(result instanceof Callable)) {
/* 1931 */         throw notFunctionError(result, name);
/*      */       }
/* 1933 */       storeScriptable(cx, thisObj);
/*      */     } 
/*      */     
/* 1936 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static Object topScopeName(Context cx, Scriptable scope, String name) {
/* 1942 */     if (cx.useDynamicScope) {
/* 1943 */       scope = checkDynamicScope(cx.topCallScope, scope);
/*      */     }
/* 1945 */     return ScriptableObject.getProperty(scope, name);
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
/*      */   public static Scriptable bind(Context cx, Scriptable scope, String id) {
/*      */     XMLObject xMLObject;
/* 1964 */     Scriptable firstXMLObject = null;
/* 1965 */     Scriptable parent = scope.getParentScope();
/* 1966 */     if (parent != null) {
/*      */       while (true) {
/* 1968 */         if (scope instanceof NativeWith) {
/* 1969 */           Scriptable withObj = scope.getPrototype();
/* 1970 */           if (withObj instanceof XMLObject) {
/* 1971 */             XMLObject xmlObject = (XMLObject)withObj;
/* 1972 */             if (xmlObject.has(cx, id)) {
/* 1973 */               return (Scriptable)xmlObject;
/*      */             }
/* 1975 */             if (firstXMLObject == null) {
/* 1976 */               xMLObject = xmlObject;
/*      */             }
/*      */           }
/* 1979 */           else if (ScriptableObject.hasProperty(withObj, id)) {
/* 1980 */             return withObj;
/*      */           } 
/*      */           
/* 1983 */           scope = parent;
/* 1984 */           parent = parent.getParentScope();
/* 1985 */           if (parent == null)
/*      */             break; 
/*      */           continue;
/*      */         } 
/*      */         do {
/* 1990 */           if (ScriptableObject.hasProperty(scope, id)) {
/* 1991 */             return scope;
/*      */           }
/* 1993 */           scope = parent;
/* 1994 */           parent = parent.getParentScope();
/* 1995 */         } while (parent != null);
/*      */         
/*      */         break;
/*      */       } 
/*      */     }
/*      */     
/* 2001 */     if (cx.useDynamicScope) {
/* 2002 */       scope = checkDynamicScope(cx.topCallScope, scope);
/*      */     }
/* 2004 */     if (ScriptableObject.hasProperty(scope, id)) {
/* 2005 */       return scope;
/*      */     }
/*      */ 
/*      */     
/* 2009 */     return (Scriptable)xMLObject;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object setName(Scriptable bound, Object value, Context cx, Scriptable scope, String id) {
/* 2015 */     if (bound != null) {
/*      */ 
/*      */       
/* 2018 */       ScriptableObject.putProperty(bound, id, value);
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 2023 */       if (cx.hasFeature(11) || cx.hasFeature(8))
/*      */       {
/*      */         
/* 2026 */         Context.reportWarning(getMessage1("msg.assn.create.strict", id));
/*      */       }
/*      */ 
/*      */       
/* 2030 */       bound = ScriptableObject.getTopLevelScope(scope);
/* 2031 */       if (cx.useDynamicScope) {
/* 2032 */         bound = checkDynamicScope(cx.topCallScope, bound);
/*      */       }
/* 2034 */       bound.put(id, bound, value);
/*      */     } 
/* 2036 */     return value;
/*      */   }
/*      */ 
/*      */   
/*      */   public static Object strictSetName(Scriptable bound, Object value, Context cx, Scriptable scope, String id) {
/* 2041 */     if (bound != null) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2050 */       ScriptableObject.putProperty(bound, id, value);
/* 2051 */       return value;
/*      */     } 
/*      */     
/* 2054 */     String msg = "Assignment to undefined \"" + id + "\" in strict mode";
/* 2055 */     throw constructError("ReferenceError", msg);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object setConst(Scriptable bound, Object value, Context cx, String id) {
/* 2062 */     if (bound instanceof XMLObject) {
/* 2063 */       bound.put(id, bound, value);
/*      */     } else {
/* 2065 */       ScriptableObject.putConstProperty(bound, id, value);
/*      */     } 
/* 2067 */     return value;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class IdEnumeration
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */ 
/*      */     
/*      */     Scriptable obj;
/*      */ 
/*      */     
/*      */     Object[] ids;
/*      */ 
/*      */     
/*      */     int index;
/*      */ 
/*      */     
/*      */     ObjToIntMap used;
/*      */ 
/*      */     
/*      */     Object currentId;
/*      */ 
/*      */     
/*      */     int enumType;
/*      */     
/*      */     boolean enumNumbers;
/*      */     
/*      */     Scriptable iterator;
/*      */ 
/*      */     
/*      */     private IdEnumeration() {}
/*      */   }
/*      */ 
/*      */   
/*      */   public static Scriptable toIterator(Context cx, Scriptable scope, Scriptable obj, boolean keyOnly) {
/* 2105 */     if (ScriptableObject.hasProperty(obj, "__iterator__")) {
/*      */ 
/*      */       
/* 2108 */       Object v = ScriptableObject.getProperty(obj, "__iterator__");
/*      */       
/* 2110 */       if (!(v instanceof Callable)) {
/* 2111 */         throw typeError0("msg.invalid.iterator");
/*      */       }
/* 2113 */       Callable f = (Callable)v;
/* 2114 */       Object[] args = { keyOnly ? Boolean.TRUE : Boolean.FALSE };
/*      */       
/* 2116 */       v = f.call(cx, scope, obj, args);
/* 2117 */       if (!(v instanceof Scriptable)) {
/* 2118 */         throw typeError0("msg.iterator.primitive");
/*      */       }
/* 2120 */       return (Scriptable)v;
/*      */     } 
/* 2122 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static Object enumInit(Object value, Context cx, boolean enumValues) {
/* 2133 */     return enumInit(value, cx, enumValues ? 1 : 0);
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
/*      */   @Deprecated
/*      */   public static Object enumInit(Object value, Context cx, int enumType) {
/* 2150 */     return enumInit(value, cx, getTopCallScope(cx), enumType);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object enumInit(Object value, Context cx, Scriptable scope, int enumType) {
/* 2156 */     IdEnumeration x = new IdEnumeration();
/* 2157 */     x.obj = toObjectOrNull(cx, value, scope);
/* 2158 */     if (x.obj == null)
/*      */     {
/*      */       
/* 2161 */       return x;
/*      */     }
/* 2163 */     x.enumType = enumType;
/* 2164 */     x.iterator = null;
/* 2165 */     if (enumType != 3 && enumType != 4 && enumType != 5)
/*      */     {
/*      */ 
/*      */       
/* 2169 */       x.iterator = toIterator(cx, x.obj.getParentScope(), x.obj, (enumType == 0));
/*      */     }
/*      */     
/* 2172 */     if (x.iterator == null)
/*      */     {
/*      */       
/* 2175 */       enumChangeObject(x);
/*      */     }
/*      */     
/* 2178 */     return x;
/*      */   }
/*      */   
/*      */   public static void setEnumNumbers(Object enumObj, boolean enumNumbers) {
/* 2182 */     ((IdEnumeration)enumObj).enumNumbers = enumNumbers;
/*      */   }
/*      */ 
/*      */   
/*      */   public static Boolean enumNext(Object enumObj) {
/* 2187 */     IdEnumeration x = (IdEnumeration)enumObj;
/* 2188 */     if (x.iterator != null) {
/* 2189 */       Object v = ScriptableObject.getProperty(x.iterator, "next");
/* 2190 */       if (!(v instanceof Callable))
/* 2191 */         return Boolean.FALSE; 
/* 2192 */       Callable f = (Callable)v;
/* 2193 */       Context cx = Context.getContext();
/*      */       try {
/* 2195 */         x.currentId = f.call(cx, x.iterator.getParentScope(), x.iterator, emptyArgs);
/*      */         
/* 2197 */         return Boolean.TRUE;
/* 2198 */       } catch (JavaScriptException e) {
/* 2199 */         if (e.getValue() instanceof NativeIterator.StopIteration) {
/* 2200 */           return Boolean.FALSE;
/*      */         }
/* 2202 */         throw e;
/*      */       } 
/*      */     } 
/*      */     while (true) {
/* 2206 */       if (x.obj == null) {
/* 2207 */         return Boolean.FALSE;
/*      */       }
/* 2209 */       if (x.index == x.ids.length) {
/* 2210 */         x.obj = x.obj.getPrototype();
/* 2211 */         enumChangeObject(x);
/*      */         continue;
/*      */       } 
/* 2214 */       Object id = x.ids[x.index++];
/* 2215 */       if (x.used != null && x.used.has(id)) {
/*      */         continue;
/*      */       }
/* 2218 */       if (id instanceof String) {
/* 2219 */         String strId = (String)id;
/* 2220 */         if (!x.obj.has(strId, x.obj))
/*      */           continue; 
/* 2222 */         x.currentId = strId; break;
/*      */       } 
/* 2224 */       int intId = ((Number)id).intValue();
/* 2225 */       if (!x.obj.has(intId, x.obj))
/*      */         continue; 
/* 2227 */       x.currentId = x.enumNumbers ? Integer.valueOf(intId) : String.valueOf(intId);
/*      */       break;
/*      */     } 
/* 2230 */     return Boolean.TRUE;
/*      */   }
/*      */ 
/*      */   
/*      */   public static Object enumId(Object enumObj, Context cx) {
/*      */     Object[] elements;
/* 2236 */     IdEnumeration x = (IdEnumeration)enumObj;
/* 2237 */     if (x.iterator != null) {
/* 2238 */       return x.currentId;
/*      */     }
/* 2240 */     switch (x.enumType) {
/*      */       case 0:
/*      */       case 3:
/* 2243 */         return x.currentId;
/*      */       case 1:
/*      */       case 4:
/* 2246 */         return enumValue(enumObj, cx);
/*      */       case 2:
/*      */       case 5:
/* 2249 */         elements = new Object[] { x.currentId, enumValue(enumObj, cx) };
/* 2250 */         return cx.newArray(ScriptableObject.getTopLevelScope(x.obj), elements);
/*      */     } 
/* 2252 */     throw Kit.codeBug();
/*      */   }
/*      */   
/*      */   public static Object enumValue(Object enumObj, Context cx) {
/*      */     Object result;
/* 2257 */     IdEnumeration x = (IdEnumeration)enumObj;
/*      */ 
/*      */ 
/*      */     
/* 2261 */     String s = toStringIdOrIndex(cx, x.currentId);
/* 2262 */     if (s == null) {
/* 2263 */       int index = lastIndexResult(cx);
/* 2264 */       result = x.obj.get(index, x.obj);
/*      */     } else {
/* 2266 */       result = x.obj.get(s, x.obj);
/*      */     } 
/*      */     
/* 2269 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   private static void enumChangeObject(IdEnumeration x) {
/* 2274 */     Object[] ids = null;
/* 2275 */     while (x.obj != null) {
/* 2276 */       ids = x.obj.getIds();
/* 2277 */       if (ids.length != 0) {
/*      */         break;
/*      */       }
/* 2280 */       x.obj = x.obj.getPrototype();
/*      */     } 
/* 2282 */     if (x.obj != null && x.ids != null) {
/* 2283 */       Object[] previous = x.ids;
/* 2284 */       int L = previous.length;
/* 2285 */       if (x.used == null) {
/* 2286 */         x.used = new ObjToIntMap(L);
/*      */       }
/* 2288 */       for (int i = 0; i != L; i++) {
/* 2289 */         x.used.intern(previous[i]);
/*      */       }
/*      */     } 
/* 2292 */     x.ids = ids;
/* 2293 */     x.index = 0;
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
/*      */   public static Callable getNameFunctionAndThis(String name, Context cx, Scriptable scope) {
/* 2307 */     Scriptable parent = scope.getParentScope();
/* 2308 */     if (parent == null) {
/* 2309 */       Object result = topScopeName(cx, scope, name);
/* 2310 */       if (!(result instanceof Callable)) {
/* 2311 */         if (result == Scriptable.NOT_FOUND) {
/* 2312 */           throw notFoundError(scope, name);
/*      */         }
/* 2314 */         throw notFunctionError(result, name);
/*      */       } 
/*      */ 
/*      */       
/* 2318 */       Scriptable thisObj = scope;
/* 2319 */       storeScriptable(cx, thisObj);
/* 2320 */       return (Callable)result;
/*      */     } 
/*      */ 
/*      */     
/* 2324 */     return (Callable)nameOrFunction(cx, scope, parent, name, true);
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
/*      */   @Deprecated
/*      */   public static Callable getElemFunctionAndThis(Object obj, Object elem, Context cx) {
/* 2341 */     return getElemFunctionAndThis(obj, elem, cx, getTopCallScope(cx));
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
/*      */   public static Callable getElemFunctionAndThis(Object obj, Object elem, Context cx, Scriptable scope) {
/* 2354 */     String str = toStringIdOrIndex(cx, elem);
/* 2355 */     if (str != null) {
/* 2356 */       return getPropFunctionAndThis(obj, str, cx, scope);
/*      */     }
/* 2358 */     int index = lastIndexResult(cx);
/*      */     
/* 2360 */     Scriptable thisObj = toObjectOrNull(cx, obj, scope);
/* 2361 */     if (thisObj == null) {
/* 2362 */       throw undefCallError(obj, String.valueOf(index));
/*      */     }
/*      */     
/* 2365 */     Object value = ScriptableObject.getProperty(thisObj, index);
/* 2366 */     if (!(value instanceof Callable)) {
/* 2367 */       throw notFunctionError(value, elem);
/*      */     }
/*      */     
/* 2370 */     storeScriptable(cx, thisObj);
/* 2371 */     return (Callable)value;
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
/*      */   @Deprecated
/*      */   public static Callable getPropFunctionAndThis(Object obj, String property, Context cx) {
/* 2390 */     return getPropFunctionAndThis(obj, property, cx, getTopCallScope(cx));
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
/*      */   public static Callable getPropFunctionAndThis(Object obj, String property, Context cx, Scriptable scope) {
/* 2404 */     Scriptable thisObj = toObjectOrNull(cx, obj, scope);
/* 2405 */     return getPropFunctionAndThisHelper(obj, property, cx, thisObj);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static Callable getPropFunctionAndThisHelper(Object obj, String property, Context cx, Scriptable thisObj) {
/* 2411 */     if (thisObj == null) {
/* 2412 */       throw undefCallError(obj, property);
/*      */     }
/*      */     
/* 2415 */     Object value = ScriptableObject.getProperty(thisObj, property);
/* 2416 */     if (!(value instanceof Callable)) {
/* 2417 */       Object noSuchMethod = ScriptableObject.getProperty(thisObj, "__noSuchMethod__");
/* 2418 */       if (noSuchMethod instanceof Callable) {
/* 2419 */         value = new NoSuchMethodShim((Callable)noSuchMethod, property);
/*      */       }
/*      */     } 
/* 2422 */     if (!(value instanceof Callable)) {
/* 2423 */       throw notFunctionError(thisObj, value, property);
/*      */     }
/*      */     
/* 2426 */     storeScriptable(cx, thisObj);
/* 2427 */     return (Callable)value;
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
/*      */   public static Callable getValueFunctionAndThis(Object value, Context cx) {
/* 2439 */     if (!(value instanceof Callable)) {
/* 2440 */       throw notFunctionError(value);
/*      */     }
/*      */     
/* 2443 */     Callable f = (Callable)value;
/* 2444 */     Scriptable thisObj = null;
/* 2445 */     if (f instanceof Scriptable) {
/* 2446 */       thisObj = ((Scriptable)f).getParentScope();
/*      */     }
/* 2448 */     if (thisObj == null) {
/* 2449 */       if (cx.topCallScope == null) throw new IllegalStateException(); 
/* 2450 */       thisObj = cx.topCallScope;
/*      */     } 
/* 2452 */     if (thisObj.getParentScope() != null && 
/* 2453 */       !(thisObj instanceof NativeWith))
/*      */     {
/*      */       
/* 2456 */       if (thisObj instanceof NativeCall)
/*      */       {
/* 2458 */         thisObj = ScriptableObject.getTopLevelScope(thisObj);
/*      */       }
/*      */     }
/* 2461 */     storeScriptable(cx, thisObj);
/* 2462 */     return f;
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
/*      */   public static Ref callRef(Callable function, Scriptable thisObj, Object[] args, Context cx) {
/* 2477 */     if (function instanceof RefCallable) {
/* 2478 */       RefCallable rfunction = (RefCallable)function;
/* 2479 */       Ref ref = rfunction.refCall(cx, thisObj, args);
/* 2480 */       if (ref == null) {
/* 2481 */         throw new IllegalStateException(rfunction.getClass().getName() + ".refCall() returned null");
/*      */       }
/* 2483 */       return ref;
/*      */     } 
/*      */     
/* 2486 */     String msg = getMessage1("msg.no.ref.from.function", toString(function));
/*      */     
/* 2488 */     throw constructError("ReferenceError", msg);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Scriptable newObject(Object fun, Context cx, Scriptable scope, Object[] args) {
/* 2499 */     if (!(fun instanceof Function)) {
/* 2500 */       throw notFunctionError(fun);
/*      */     }
/* 2502 */     Function function = (Function)fun;
/* 2503 */     return function.construct(cx, scope, args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object callSpecial(Context cx, Callable fun, Scriptable thisObj, Object[] args, Scriptable scope, Scriptable callerThis, int callType, String filename, int lineNumber) {
/* 2512 */     if (callType == 1) {
/* 2513 */       if (thisObj.getParentScope() == null && NativeGlobal.isEvalFunction(fun)) {
/* 2514 */         return evalSpecial(cx, scope, callerThis, args, filename, lineNumber);
/*      */       }
/*      */     }
/* 2517 */     else if (callType == 2) {
/* 2518 */       if (NativeWith.isWithFunction(fun)) {
/* 2519 */         throw Context.reportRuntimeError1("msg.only.from.new", "With");
/*      */       }
/*      */     } else {
/*      */       
/* 2523 */       throw Kit.codeBug();
/*      */     } 
/*      */     
/* 2526 */     return fun.call(cx, scope, thisObj, args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object newSpecial(Context cx, Object fun, Object[] args, Scriptable scope, int callType) {
/* 2533 */     if (callType == 1) {
/* 2534 */       if (NativeGlobal.isEvalFunction(fun)) {
/* 2535 */         throw typeError1("msg.not.ctor", "eval");
/*      */       }
/* 2537 */     } else if (callType == 2) {
/* 2538 */       if (NativeWith.isWithFunction(fun)) {
/* 2539 */         return NativeWith.newWithSpecial(cx, scope, args);
/*      */       }
/*      */     } else {
/* 2542 */       throw Kit.codeBug();
/*      */     } 
/*      */     
/* 2545 */     return newObject(fun, cx, scope, args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object applyOrCall(boolean isApply, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/*      */     Object[] callArgs;
/* 2557 */     int L = args.length;
/* 2558 */     Callable function = getCallable(thisObj);
/*      */     
/* 2560 */     Scriptable callThis = null;
/* 2561 */     if (L != 0) {
/* 2562 */       callThis = toObjectOrNull(cx, args[0], scope);
/*      */     }
/* 2564 */     if (callThis == null)
/*      */     {
/* 2566 */       callThis = getTopCallScope(cx);
/*      */     }
/*      */ 
/*      */     
/* 2570 */     if (isApply) {
/*      */       
/* 2572 */       callArgs = (L <= 1) ? emptyArgs : getApplyArguments(cx, args[1]);
/*      */ 
/*      */     
/*      */     }
/* 2576 */     else if (L <= 1) {
/* 2577 */       callArgs = emptyArgs;
/*      */     } else {
/* 2579 */       callArgs = new Object[L - 1];
/* 2580 */       System.arraycopy(args, 1, callArgs, 0, L - 1);
/*      */     } 
/*      */ 
/*      */     
/* 2584 */     return function.call(cx, scope, callThis, callArgs);
/*      */   }
/*      */ 
/*      */   
/*      */   static Object[] getApplyArguments(Context cx, Object arg1) {
/* 2589 */     if (arg1 == null || arg1 == Undefined.instance)
/* 2590 */       return emptyArgs; 
/* 2591 */     if (arg1 instanceof NativeArray || arg1 instanceof Arguments) {
/* 2592 */       return cx.getElements((Scriptable)arg1);
/*      */     }
/* 2594 */     throw typeError0("msg.arg.isnt.array");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static Callable getCallable(Scriptable thisObj) {
/*      */     Callable function;
/* 2601 */     if (thisObj instanceof Callable) {
/* 2602 */       function = (Callable)thisObj;
/*      */     } else {
/* 2604 */       Object value = thisObj.getDefaultValue(FunctionClass);
/* 2605 */       if (!(value instanceof Callable)) {
/* 2606 */         throw notFunctionError(value, thisObj);
/*      */       }
/* 2608 */       function = (Callable)value;
/*      */     } 
/* 2610 */     return function;
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
/*      */   public static Object evalSpecial(Context cx, Scriptable scope, Object thisArg, Object[] args, String filename, int lineNumber) {
/* 2622 */     if (args.length < 1)
/* 2623 */       return Undefined.instance; 
/* 2624 */     Object x = args[0];
/* 2625 */     if (!(x instanceof CharSequence)) {
/* 2626 */       if (cx.hasFeature(11) || cx.hasFeature(9))
/*      */       {
/*      */         
/* 2629 */         throw Context.reportRuntimeError0("msg.eval.nonstring.strict");
/*      */       }
/* 2631 */       String message = getMessage0("msg.eval.nonstring");
/* 2632 */       Context.reportWarning(message);
/* 2633 */       return x;
/*      */     } 
/* 2635 */     if (filename == null) {
/* 2636 */       int[] linep = new int[1];
/* 2637 */       filename = Context.getSourcePositionFromStack(linep);
/* 2638 */       if (filename != null) {
/* 2639 */         lineNumber = linep[0];
/*      */       } else {
/* 2641 */         filename = "";
/*      */       } 
/*      */     } 
/* 2644 */     String sourceName = makeUrlForGeneratedScript(true, filename, lineNumber);
/*      */ 
/*      */ 
/*      */     
/* 2648 */     ErrorReporter reporter = DefaultErrorReporter.forEval(cx.getErrorReporter());
/*      */     
/* 2650 */     Evaluator evaluator = Context.createInterpreter();
/* 2651 */     if (evaluator == null) {
/* 2652 */       throw new JavaScriptException("Interpreter not present", filename, lineNumber);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2658 */     Script script = cx.compileString(x.toString(), evaluator, reporter, sourceName, 1, null);
/*      */     
/* 2660 */     evaluator.setEvalScriptFlag(script);
/* 2661 */     Callable c = (Callable)script;
/* 2662 */     return c.call(cx, scope, (Scriptable)thisArg, emptyArgs);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String typeof(Object value) {
/* 2670 */     if (value == null)
/* 2671 */       return "object"; 
/* 2672 */     if (value == Undefined.instance)
/* 2673 */       return "undefined"; 
/* 2674 */     if (value instanceof ScriptableObject)
/* 2675 */       return ((ScriptableObject)value).getTypeOf(); 
/* 2676 */     if (value instanceof Scriptable)
/* 2677 */       return (value instanceof Callable) ? "function" : "object"; 
/* 2678 */     if (value instanceof CharSequence)
/* 2679 */       return "string"; 
/* 2680 */     if (value instanceof Number)
/* 2681 */       return "number"; 
/* 2682 */     if (value instanceof Boolean)
/* 2683 */       return "boolean"; 
/* 2684 */     throw errorWithClassName("msg.invalid.type", value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String typeofName(Scriptable scope, String id) {
/* 2692 */     Context cx = Context.getContext();
/* 2693 */     Scriptable val = bind(cx, scope, id);
/* 2694 */     if (val == null)
/* 2695 */       return "undefined"; 
/* 2696 */     return typeof(getObjectProp(val, id, cx));
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
/*      */   public static Object add(Object val1, Object val2, Context cx) {
/* 2713 */     if (val1 instanceof Number && val2 instanceof Number) {
/* 2714 */       return wrapNumber(((Number)val1).doubleValue() + ((Number)val2).doubleValue());
/*      */     }
/*      */     
/* 2717 */     if (val1 instanceof XMLObject) {
/* 2718 */       Object test = ((XMLObject)val1).addValues(cx, true, val2);
/* 2719 */       if (test != Scriptable.NOT_FOUND) {
/* 2720 */         return test;
/*      */       }
/*      */     } 
/* 2723 */     if (val2 instanceof XMLObject) {
/* 2724 */       Object test = ((XMLObject)val2).addValues(cx, false, val1);
/* 2725 */       if (test != Scriptable.NOT_FOUND) {
/* 2726 */         return test;
/*      */       }
/*      */     } 
/* 2729 */     if (val1 instanceof Scriptable)
/* 2730 */       val1 = ((Scriptable)val1).getDefaultValue(null); 
/* 2731 */     if (val2 instanceof Scriptable)
/* 2732 */       val2 = ((Scriptable)val2).getDefaultValue(null); 
/* 2733 */     if (!(val1 instanceof CharSequence) && !(val2 instanceof CharSequence)) {
/* 2734 */       if (val1 instanceof Number && val2 instanceof Number) {
/* 2735 */         return wrapNumber(((Number)val1).doubleValue() + ((Number)val2).doubleValue());
/*      */       }
/*      */       
/* 2738 */       return wrapNumber(toNumber(val1) + toNumber(val2));
/* 2739 */     }  return new ConsString(toCharSequence(val1), toCharSequence(val2));
/*      */   }
/*      */   
/*      */   public static CharSequence add(CharSequence val1, Object val2) {
/* 2743 */     return new ConsString(val1, toCharSequence(val2));
/*      */   }
/*      */   
/*      */   public static CharSequence add(Object val1, CharSequence val2) {
/* 2747 */     return new ConsString(toCharSequence(val1), val2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static Object nameIncrDecr(Scriptable scopeChain, String id, int incrDecrMask) {
/* 2759 */     return nameIncrDecr(scopeChain, id, Context.getContext(), incrDecrMask);
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
/*      */   public static Object nameIncrDecr(Scriptable scopeChain, String id, Context cx, int incrDecrMask) {
/*      */     // Byte code:
/*      */     //   0: aload_2
/*      */     //   1: getfield useDynamicScope : Z
/*      */     //   4: ifeq -> 25
/*      */     //   7: aload_0
/*      */     //   8: invokeinterface getParentScope : ()Lorg/mozilla/javascript/Scriptable;
/*      */     //   13: ifnonnull -> 25
/*      */     //   16: aload_2
/*      */     //   17: getfield topCallScope : Lorg/mozilla/javascript/Scriptable;
/*      */     //   20: aload_0
/*      */     //   21: invokestatic checkDynamicScope : (Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Scriptable;
/*      */     //   24: astore_0
/*      */     //   25: aload_0
/*      */     //   26: astore #4
/*      */     //   28: aload #4
/*      */     //   30: instanceof org/mozilla/javascript/NativeWith
/*      */     //   33: ifeq -> 52
/*      */     //   36: aload #4
/*      */     //   38: invokeinterface getPrototype : ()Lorg/mozilla/javascript/Scriptable;
/*      */     //   43: instanceof org/mozilla/javascript/xml/XMLObject
/*      */     //   46: ifeq -> 52
/*      */     //   49: goto -> 88
/*      */     //   52: aload #4
/*      */     //   54: aload_1
/*      */     //   55: aload_0
/*      */     //   56: invokeinterface get : (Ljava/lang/String;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;
/*      */     //   61: astore #5
/*      */     //   63: aload #5
/*      */     //   65: getstatic org/mozilla/javascript/Scriptable.NOT_FOUND : Ljava/lang/Object;
/*      */     //   68: if_acmpeq -> 74
/*      */     //   71: goto -> 105
/*      */     //   74: aload #4
/*      */     //   76: invokeinterface getPrototype : ()Lorg/mozilla/javascript/Scriptable;
/*      */     //   81: astore #4
/*      */     //   83: aload #4
/*      */     //   85: ifnonnull -> 28
/*      */     //   88: aload_0
/*      */     //   89: invokeinterface getParentScope : ()Lorg/mozilla/javascript/Scriptable;
/*      */     //   94: astore_0
/*      */     //   95: aload_0
/*      */     //   96: ifnonnull -> 0
/*      */     //   99: aload_0
/*      */     //   100: aload_1
/*      */     //   101: invokestatic notFoundError : (Lorg/mozilla/javascript/Scriptable;Ljava/lang/String;)Ljava/lang/RuntimeException;
/*      */     //   104: athrow
/*      */     //   105: aload #4
/*      */     //   107: aload_1
/*      */     //   108: aload_0
/*      */     //   109: aload #5
/*      */     //   111: iload_3
/*      */     //   112: invokestatic doScriptableIncrDecr : (Lorg/mozilla/javascript/Scriptable;Ljava/lang/String;Lorg/mozilla/javascript/Scriptable;Ljava/lang/Object;I)Ljava/lang/Object;
/*      */     //   115: areturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #2769	-> 0
/*      */     //   #2770	-> 16
/*      */     //   #2772	-> 25
/*      */     //   #2774	-> 28
/*      */     //   #2776	-> 49
/*      */     //   #2778	-> 52
/*      */     //   #2779	-> 63
/*      */     //   #2780	-> 71
/*      */     //   #2782	-> 74
/*      */     //   #2783	-> 83
/*      */     //   #2784	-> 88
/*      */     //   #2785	-> 95
/*      */     //   #2786	-> 99
/*      */     //   #2788	-> 105
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   63	25	5	value	Ljava/lang/Object;
/*      */     //   0	116	0	scopeChain	Lorg/mozilla/javascript/Scriptable;
/*      */     //   0	116	1	id	Ljava/lang/String;
/*      */     //   0	116	2	cx	Lorg/mozilla/javascript/Context;
/*      */     //   0	116	3	incrDecrMask	I
/*      */     //   28	88	4	target	Lorg/mozilla/javascript/Scriptable;
/*      */     //   105	11	5	value	Ljava/lang/Object;
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
/*      */   @Deprecated
/*      */   public static Object propIncrDecr(Object obj, String id, Context cx, int incrDecrMask) {
/* 2799 */     return propIncrDecr(obj, id, cx, getTopCallScope(cx), incrDecrMask);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object propIncrDecr(Object obj, String id, Context cx, Scriptable scope, int incrDecrMask) {
/*      */     Object value;
/* 2806 */     Scriptable start = toObjectOrNull(cx, obj, scope);
/* 2807 */     if (start == null) {
/* 2808 */       throw undefReadError(obj, id);
/*      */     }
/*      */     
/* 2811 */     Scriptable target = start;
/*      */ 
/*      */     
/*      */     while (true) {
/* 2815 */       value = target.get(id, start);
/* 2816 */       if (value != Scriptable.NOT_FOUND) {
/*      */         break;
/*      */       }
/* 2819 */       target = target.getPrototype();
/* 2820 */       if (target == null) {
/* 2821 */         start.put(id, start, NaNobj);
/* 2822 */         return NaNobj;
/*      */       } 
/* 2824 */     }  return doScriptableIncrDecr(target, id, start, value, incrDecrMask);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Object doScriptableIncrDecr(Scriptable target, String id, Scriptable protoChainStart, Object value, int incrDecrMask) {
/*      */     double number;
/* 2834 */     boolean post = ((incrDecrMask & 0x2) != 0);
/*      */     
/* 2836 */     if (value instanceof Number) {
/* 2837 */       number = ((Number)value).doubleValue();
/*      */     } else {
/* 2839 */       number = toNumber(value);
/* 2840 */       if (post)
/*      */       {
/* 2842 */         value = wrapNumber(number);
/*      */       }
/*      */     } 
/* 2845 */     if ((incrDecrMask & 0x1) == 0) {
/* 2846 */       number++;
/*      */     } else {
/* 2848 */       number--;
/*      */     } 
/* 2850 */     Number result = wrapNumber(number);
/* 2851 */     target.put(id, protoChainStart, result);
/* 2852 */     if (post) {
/* 2853 */       return value;
/*      */     }
/* 2855 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static Object elemIncrDecr(Object obj, Object index, Context cx, int incrDecrMask) {
/* 2866 */     return elemIncrDecr(obj, index, cx, getTopCallScope(cx), incrDecrMask);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object elemIncrDecr(Object obj, Object index, Context cx, Scriptable scope, int incrDecrMask) {
/*      */     double number;
/* 2873 */     Object value = getObjectElem(obj, index, cx, scope);
/* 2874 */     boolean post = ((incrDecrMask & 0x2) != 0);
/*      */     
/* 2876 */     if (value instanceof Number) {
/* 2877 */       number = ((Number)value).doubleValue();
/*      */     } else {
/* 2879 */       number = toNumber(value);
/* 2880 */       if (post)
/*      */       {
/* 2882 */         value = wrapNumber(number);
/*      */       }
/*      */     } 
/* 2885 */     if ((incrDecrMask & 0x1) == 0) {
/* 2886 */       number++;
/*      */     } else {
/* 2888 */       number--;
/*      */     } 
/* 2890 */     Number result = wrapNumber(number);
/* 2891 */     setObjectElem(obj, index, result, cx, scope);
/* 2892 */     if (post) {
/* 2893 */       return value;
/*      */     }
/* 2895 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static Object refIncrDecr(Ref ref, Context cx, int incrDecrMask) {
/* 2905 */     return refIncrDecr(ref, cx, getTopCallScope(cx), incrDecrMask);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Object refIncrDecr(Ref ref, Context cx, Scriptable scope, int incrDecrMask) {
/*      */     double number;
/* 2911 */     Object value = ref.get(cx);
/* 2912 */     boolean post = ((incrDecrMask & 0x2) != 0);
/*      */     
/* 2914 */     if (value instanceof Number) {
/* 2915 */       number = ((Number)value).doubleValue();
/*      */     } else {
/* 2917 */       number = toNumber(value);
/* 2918 */       if (post)
/*      */       {
/* 2920 */         value = wrapNumber(number);
/*      */       }
/*      */     } 
/* 2923 */     if ((incrDecrMask & 0x1) == 0) {
/* 2924 */       number++;
/*      */     } else {
/* 2926 */       number--;
/*      */     } 
/* 2928 */     Number result = wrapNumber(number);
/* 2929 */     ref.set(cx, scope, result);
/* 2930 */     if (post) {
/* 2931 */       return value;
/*      */     }
/* 2933 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public static Object toPrimitive(Object val) {
/* 2938 */     return toPrimitive(val, null);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Object toPrimitive(Object val, Class<?> typeHint) {
/* 2943 */     if (!(val instanceof Scriptable)) {
/* 2944 */       return val;
/*      */     }
/* 2946 */     Scriptable s = (Scriptable)val;
/* 2947 */     Object result = s.getDefaultValue(typeHint);
/* 2948 */     if (result instanceof Scriptable)
/* 2949 */       throw typeError0("msg.bad.default.value"); 
/* 2950 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean eq(Object x, Object y) {
/* 2960 */     if (x == null || x == Undefined.instance) {
/* 2961 */       if (y == null || y == Undefined.instance) {
/* 2962 */         return true;
/*      */       }
/* 2964 */       if (y instanceof ScriptableObject) {
/* 2965 */         Object test = ((ScriptableObject)y).equivalentValues(x);
/* 2966 */         if (test != Scriptable.NOT_FOUND) {
/* 2967 */           return ((Boolean)test).booleanValue();
/*      */         }
/*      */       } 
/* 2970 */       return false;
/* 2971 */     }  if (x instanceof Number)
/* 2972 */       return eqNumber(((Number)x).doubleValue(), y); 
/* 2973 */     if (x == y)
/* 2974 */       return true; 
/* 2975 */     if (x instanceof CharSequence)
/* 2976 */       return eqString((CharSequence)x, y); 
/* 2977 */     if (x instanceof Boolean) {
/* 2978 */       boolean b = ((Boolean)x).booleanValue();
/* 2979 */       if (y instanceof Boolean) {
/* 2980 */         return (b == ((Boolean)y).booleanValue());
/*      */       }
/* 2982 */       if (y instanceof ScriptableObject) {
/* 2983 */         Object test = ((ScriptableObject)y).equivalentValues(x);
/* 2984 */         if (test != Scriptable.NOT_FOUND) {
/* 2985 */           return ((Boolean)test).booleanValue();
/*      */         }
/*      */       } 
/* 2988 */       return eqNumber(b ? 1.0D : 0.0D, y);
/* 2989 */     }  if (x instanceof Scriptable) {
/* 2990 */       if (y instanceof Scriptable) {
/* 2991 */         if (x instanceof ScriptableObject) {
/* 2992 */           Object test = ((ScriptableObject)x).equivalentValues(y);
/* 2993 */           if (test != Scriptable.NOT_FOUND) {
/* 2994 */             return ((Boolean)test).booleanValue();
/*      */           }
/*      */         } 
/* 2997 */         if (y instanceof ScriptableObject) {
/* 2998 */           Object test = ((ScriptableObject)y).equivalentValues(x);
/* 2999 */           if (test != Scriptable.NOT_FOUND) {
/* 3000 */             return ((Boolean)test).booleanValue();
/*      */           }
/*      */         } 
/* 3003 */         if (x instanceof Wrapper && y instanceof Wrapper) {
/*      */ 
/*      */           
/* 3006 */           Object unwrappedX = ((Wrapper)x).unwrap();
/* 3007 */           Object unwrappedY = ((Wrapper)y).unwrap();
/* 3008 */           return (unwrappedX == unwrappedY || (isPrimitive(unwrappedX) && isPrimitive(unwrappedY) && eq(unwrappedX, unwrappedY)));
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 3013 */         return false;
/* 3014 */       }  if (y instanceof Boolean) {
/* 3015 */         if (x instanceof ScriptableObject) {
/* 3016 */           Object test = ((ScriptableObject)x).equivalentValues(y);
/* 3017 */           if (test != Scriptable.NOT_FOUND) {
/* 3018 */             return ((Boolean)test).booleanValue();
/*      */           }
/*      */         } 
/* 3021 */         double d = ((Boolean)y).booleanValue() ? 1.0D : 0.0D;
/* 3022 */         return eqNumber(d, x);
/* 3023 */       }  if (y instanceof Number)
/* 3024 */         return eqNumber(((Number)y).doubleValue(), x); 
/* 3025 */       if (y instanceof CharSequence) {
/* 3026 */         return eqString((CharSequence)y, x);
/*      */       }
/*      */       
/* 3029 */       return false;
/*      */     } 
/* 3031 */     warnAboutNonJSObject(x);
/* 3032 */     return (x == y);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isPrimitive(Object obj) {
/* 3037 */     return (obj == null || obj == Undefined.instance || obj instanceof Number || obj instanceof String || obj instanceof Boolean);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean eqNumber(double x, Object y) {
/*      */     while (true) {
/* 3045 */       if (y == null || y == Undefined.instance)
/* 3046 */         return false; 
/* 3047 */       if (y instanceof Number)
/* 3048 */         return (x == ((Number)y).doubleValue()); 
/* 3049 */       if (y instanceof CharSequence)
/* 3050 */         return (x == toNumber(y)); 
/* 3051 */       if (y instanceof Boolean)
/* 3052 */         return (x == (((Boolean)y).booleanValue() ? 1.0D : 0.0D)); 
/* 3053 */       if (y instanceof Scriptable) {
/* 3054 */         if (y instanceof ScriptableObject) {
/* 3055 */           Object xval = wrapNumber(x);
/* 3056 */           Object test = ((ScriptableObject)y).equivalentValues(xval);
/* 3057 */           if (test != Scriptable.NOT_FOUND) {
/* 3058 */             return ((Boolean)test).booleanValue();
/*      */           }
/*      */         } 
/* 3061 */         y = toPrimitive(y); continue;
/*      */       }  break;
/* 3063 */     }  warnAboutNonJSObject(y);
/* 3064 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean eqString(CharSequence x, Object y) {
/*      */     while (true) {
/* 3072 */       if (y == null || y == Undefined.instance)
/* 3073 */         return false; 
/* 3074 */       if (y instanceof CharSequence) {
/* 3075 */         CharSequence c = (CharSequence)y;
/* 3076 */         return (x.length() == c.length() && x.toString().equals(c.toString()));
/* 3077 */       }  if (y instanceof Number)
/* 3078 */         return (toNumber(x.toString()) == ((Number)y).doubleValue()); 
/* 3079 */       if (y instanceof Boolean)
/* 3080 */         return (toNumber(x.toString()) == (((Boolean)y).booleanValue() ? 1.0D : 0.0D)); 
/* 3081 */       if (y instanceof Scriptable) {
/* 3082 */         if (y instanceof ScriptableObject) {
/* 3083 */           Object test = ((ScriptableObject)y).equivalentValues(x.toString());
/* 3084 */           if (test != Scriptable.NOT_FOUND) {
/* 3085 */             return ((Boolean)test).booleanValue();
/*      */           }
/*      */         } 
/* 3088 */         y = toPrimitive(y); continue;
/*      */       }  break;
/*      */     } 
/* 3091 */     warnAboutNonJSObject(y);
/* 3092 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean shallowEq(Object x, Object y) {
/* 3098 */     if (x == y) {
/* 3099 */       if (!(x instanceof Number)) {
/* 3100 */         return true;
/*      */       }
/*      */       
/* 3103 */       double d = ((Number)x).doubleValue();
/* 3104 */       return (d == d);
/*      */     } 
/* 3106 */     if (x == null || x == Undefined.instance)
/* 3107 */       return false; 
/* 3108 */     if (x instanceof Number) {
/* 3109 */       if (y instanceof Number) {
/* 3110 */         return (((Number)x).doubleValue() == ((Number)y).doubleValue());
/*      */       }
/* 3112 */     } else if (x instanceof CharSequence) {
/* 3113 */       if (y instanceof CharSequence) {
/* 3114 */         return x.toString().equals(y.toString());
/*      */       }
/* 3116 */     } else if (x instanceof Boolean) {
/* 3117 */       if (y instanceof Boolean) {
/* 3118 */         return x.equals(y);
/*      */       }
/* 3120 */     } else if (x instanceof Scriptable) {
/* 3121 */       if (x instanceof Wrapper && y instanceof Wrapper) {
/* 3122 */         return (((Wrapper)x).unwrap() == ((Wrapper)y).unwrap());
/*      */       }
/*      */     } else {
/* 3125 */       warnAboutNonJSObject(x);
/* 3126 */       return (x == y);
/*      */     } 
/* 3128 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean instanceOf(Object a, Object b, Context cx) {
/* 3139 */     if (!(b instanceof Scriptable)) {
/* 3140 */       throw typeError0("msg.instanceof.not.object");
/*      */     }
/*      */ 
/*      */     
/* 3144 */     if (!(a instanceof Scriptable)) {
/* 3145 */       return false;
/*      */     }
/* 3147 */     return ((Scriptable)b).hasInstance((Scriptable)a);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean jsDelegatesTo(Scriptable lhs, Scriptable rhs) {
/* 3156 */     Scriptable proto = lhs.getPrototype();
/*      */     
/* 3158 */     while (proto != null) {
/* 3159 */       if (proto.equals(rhs)) return true; 
/* 3160 */       proto = proto.getPrototype();
/*      */     } 
/*      */     
/* 3163 */     return false;
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
/*      */   public static boolean in(Object a, Object b, Context cx) {
/* 3182 */     if (!(b instanceof Scriptable)) {
/* 3183 */       throw typeError0("msg.in.not.object");
/*      */     }
/*      */     
/* 3186 */     return hasObjectElem((Scriptable)b, a, cx);
/*      */   }
/*      */   
/*      */   public static boolean cmp_LT(Object val1, Object val2) {
/*      */     double d1;
/*      */     double d2;
/* 3192 */     if (val1 instanceof Number && val2 instanceof Number) {
/* 3193 */       d1 = ((Number)val1).doubleValue();
/* 3194 */       d2 = ((Number)val2).doubleValue();
/*      */     } else {
/* 3196 */       if (val1 instanceof Scriptable)
/* 3197 */         val1 = ((Scriptable)val1).getDefaultValue(NumberClass); 
/* 3198 */       if (val2 instanceof Scriptable)
/* 3199 */         val2 = ((Scriptable)val2).getDefaultValue(NumberClass); 
/* 3200 */       if (val1 instanceof CharSequence && val2 instanceof CharSequence) {
/* 3201 */         return (val1.toString().compareTo(val2.toString()) < 0);
/*      */       }
/* 3203 */       d1 = toNumber(val1);
/* 3204 */       d2 = toNumber(val2);
/*      */     } 
/* 3206 */     return (d1 < d2);
/*      */   }
/*      */   
/*      */   public static boolean cmp_LE(Object val1, Object val2) {
/*      */     double d1;
/*      */     double d2;
/* 3212 */     if (val1 instanceof Number && val2 instanceof Number) {
/* 3213 */       d1 = ((Number)val1).doubleValue();
/* 3214 */       d2 = ((Number)val2).doubleValue();
/*      */     } else {
/* 3216 */       if (val1 instanceof Scriptable)
/* 3217 */         val1 = ((Scriptable)val1).getDefaultValue(NumberClass); 
/* 3218 */       if (val2 instanceof Scriptable)
/* 3219 */         val2 = ((Scriptable)val2).getDefaultValue(NumberClass); 
/* 3220 */       if (val1 instanceof CharSequence && val2 instanceof CharSequence) {
/* 3221 */         return (val1.toString().compareTo(val2.toString()) <= 0);
/*      */       }
/* 3223 */       d1 = toNumber(val1);
/* 3224 */       d2 = toNumber(val2);
/*      */     } 
/* 3226 */     return (d1 <= d2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ScriptableObject getGlobal(Context cx) {
/* 3234 */     String GLOBAL_CLASS = "org.mozilla.javascript.tools.shell.Global";
/* 3235 */     Class<?> globalClass = Kit.classOrNull("org.mozilla.javascript.tools.shell.Global");
/* 3236 */     if (globalClass != null) {
/*      */       try {
/* 3238 */         Class<?>[] parm = new Class[] { ContextClass };
/* 3239 */         Constructor<?> globalClassCtor = globalClass.getConstructor(parm);
/* 3240 */         Object[] arg = { cx };
/* 3241 */         return (ScriptableObject)globalClassCtor.newInstance(arg);
/*      */       }
/* 3243 */       catch (RuntimeException e) {
/* 3244 */         throw e;
/*      */       }
/* 3246 */       catch (Exception e) {}
/*      */     }
/*      */ 
/*      */     
/* 3250 */     return new ImporterTopLevel(cx);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean hasTopCall(Context cx) {
/* 3255 */     return (cx.topCallScope != null);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Scriptable getTopCallScope(Context cx) {
/* 3260 */     Scriptable scope = cx.topCallScope;
/* 3261 */     if (scope == null) {
/* 3262 */       throw new IllegalStateException();
/*      */     }
/* 3264 */     return scope;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object doTopCall(Callable callable, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/*      */     Object result;
/* 3271 */     if (scope == null)
/* 3272 */       throw new IllegalArgumentException(); 
/* 3273 */     if (cx.topCallScope != null) throw new IllegalStateException();
/*      */ 
/*      */     
/* 3276 */     cx.topCallScope = ScriptableObject.getTopLevelScope(scope);
/* 3277 */     cx.useDynamicScope = cx.hasFeature(7);
/* 3278 */     ContextFactory f = cx.getFactory();
/*      */     try {
/* 3280 */       result = f.doTopCall(callable, cx, scope, thisObj, args);
/*      */     } finally {
/* 3282 */       cx.topCallScope = null;
/*      */       
/* 3284 */       cx.cachedXMLLib = null;
/*      */       
/* 3286 */       if (cx.currentActivationCall != null)
/*      */       {
/*      */         
/* 3289 */         throw new IllegalStateException();
/*      */       }
/*      */     } 
/* 3292 */     return result;
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
/*      */   static Scriptable checkDynamicScope(Scriptable possibleDynamicScope, Scriptable staticTopScope) {
/* 3305 */     if (possibleDynamicScope == staticTopScope) {
/* 3306 */       return possibleDynamicScope;
/*      */     }
/* 3308 */     Scriptable proto = possibleDynamicScope;
/*      */     while (true) {
/* 3310 */       proto = proto.getPrototype();
/* 3311 */       if (proto == staticTopScope) {
/* 3312 */         return possibleDynamicScope;
/*      */       }
/* 3314 */       if (proto == null) {
/* 3315 */         return staticTopScope;
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void addInstructionCount(Context cx, int instructionsToAdd) {
/* 3322 */     cx.instructionCount += instructionsToAdd;
/* 3323 */     if (cx.instructionCount > cx.instructionThreshold) {
/*      */       
/* 3325 */       cx.observeInstructionCount(cx.instructionCount);
/* 3326 */       cx.instructionCount = 0;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void initScript(NativeFunction funObj, Scriptable thisObj, Context cx, Scriptable scope, boolean evalScript) {
/* 3334 */     if (cx.topCallScope == null) {
/* 3335 */       throw new IllegalStateException();
/*      */     }
/* 3337 */     int varCount = funObj.getParamAndVarCount();
/* 3338 */     if (varCount != 0) {
/*      */       
/* 3340 */       Scriptable varScope = scope;
/*      */ 
/*      */       
/* 3343 */       while (varScope instanceof NativeWith) {
/* 3344 */         varScope = varScope.getParentScope();
/*      */       }
/*      */       
/* 3347 */       for (int i = varCount; i-- != 0; ) {
/* 3348 */         String name = funObj.getParamOrVarName(i);
/* 3349 */         boolean isConst = funObj.getParamOrVarConst(i);
/*      */ 
/*      */         
/* 3352 */         if (!ScriptableObject.hasProperty(scope, name)) {
/* 3353 */           if (isConst) {
/* 3354 */             ScriptableObject.defineConstProperty(varScope, name); continue;
/* 3355 */           }  if (!evalScript) {
/*      */             
/* 3357 */             ScriptableObject.defineProperty(varScope, name, Undefined.instance, 4);
/*      */             
/*      */             continue;
/*      */           } 
/* 3361 */           varScope.put(name, varScope, Undefined.instance);
/*      */           continue;
/*      */         } 
/* 3364 */         ScriptableObject.redefineProperty(scope, name, isConst);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Scriptable createFunctionActivation(NativeFunction funObj, Scriptable scope, Object[] args) {
/* 3374 */     return new NativeCall(funObj, scope, args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void enterActivationFunction(Context cx, Scriptable scope) {
/* 3381 */     if (cx.topCallScope == null)
/* 3382 */       throw new IllegalStateException(); 
/* 3383 */     NativeCall call = (NativeCall)scope;
/* 3384 */     call.parentActivationCall = cx.currentActivationCall;
/* 3385 */     cx.currentActivationCall = call;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void exitActivationFunction(Context cx) {
/* 3390 */     NativeCall call = cx.currentActivationCall;
/* 3391 */     cx.currentActivationCall = call.parentActivationCall;
/* 3392 */     call.parentActivationCall = null;
/*      */   }
/*      */ 
/*      */   
/*      */   static NativeCall findFunctionActivation(Context cx, Function f) {
/* 3397 */     NativeCall call = cx.currentActivationCall;
/* 3398 */     while (call != null) {
/* 3399 */       if (call.function == f)
/* 3400 */         return call; 
/* 3401 */       call = call.parentActivationCall;
/*      */     } 
/* 3403 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Scriptable newCatchScope(Throwable t, Scriptable lastCatchScope, String exceptionName, Context cx, Scriptable scope) {
/*      */     Object obj;
/*      */     boolean cacheObj;
/* 3415 */     if (t instanceof JavaScriptException) {
/* 3416 */       cacheObj = false;
/* 3417 */       obj = ((JavaScriptException)t).getValue();
/*      */     } else {
/* 3419 */       cacheObj = true;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3424 */       if (lastCatchScope != null) {
/* 3425 */         NativeObject last = (NativeObject)lastCatchScope;
/* 3426 */         obj = last.getAssociatedValue(t);
/* 3427 */         if (obj == null) Kit.codeBug();
/*      */       
/*      */       } else {
/*      */         RhinoException re;
/*      */         TopLevel.NativeErrors type;
/*      */         String errorMsg;
/*      */         Object[] args;
/* 3434 */         Throwable javaException = null;
/*      */         
/* 3436 */         if (t instanceof EcmaError) {
/* 3437 */           EcmaError ee = (EcmaError)t;
/* 3438 */           re = ee;
/* 3439 */           type = TopLevel.NativeErrors.valueOf(ee.getName());
/* 3440 */           errorMsg = ee.getErrorMessage();
/* 3441 */         } else if (t instanceof WrappedException) {
/* 3442 */           WrappedException we = (WrappedException)t;
/* 3443 */           re = we;
/* 3444 */           javaException = we.getWrappedException();
/* 3445 */           type = TopLevel.NativeErrors.JavaException;
/* 3446 */           errorMsg = javaException.getClass().getName() + ": " + javaException.getMessage();
/*      */         }
/* 3448 */         else if (t instanceof EvaluatorException) {
/*      */           
/* 3450 */           EvaluatorException ee = (EvaluatorException)t;
/* 3451 */           re = ee;
/* 3452 */           type = TopLevel.NativeErrors.InternalError;
/* 3453 */           errorMsg = ee.getMessage();
/* 3454 */         } else if (cx.hasFeature(13)) {
/*      */ 
/*      */           
/* 3457 */           re = new WrappedException(t);
/* 3458 */           type = TopLevel.NativeErrors.JavaException;
/* 3459 */           errorMsg = t.toString();
/*      */         }
/*      */         else {
/*      */           
/* 3463 */           throw Kit.codeBug();
/*      */         } 
/*      */         
/* 3466 */         String sourceUri = re.sourceName();
/* 3467 */         if (sourceUri == null) {
/* 3468 */           sourceUri = "";
/*      */         }
/* 3470 */         int line = re.lineNumber();
/*      */         
/* 3472 */         if (line > 0) {
/* 3473 */           args = new Object[] { errorMsg, sourceUri, Integer.valueOf(line) };
/*      */         } else {
/* 3475 */           args = new Object[] { errorMsg, sourceUri };
/*      */         } 
/*      */         
/* 3478 */         Scriptable errorObject = newNativeError(cx, scope, type, args);
/*      */         
/* 3480 */         if (errorObject instanceof NativeError) {
/* 3481 */           ((NativeError)errorObject).setStackProvider(re);
/*      */         }
/*      */         
/* 3484 */         if (javaException != null && isVisible(cx, javaException)) {
/* 3485 */           Object wrap = cx.getWrapFactory().wrap(cx, scope, javaException, null);
/*      */           
/* 3487 */           ScriptableObject.defineProperty(errorObject, "javaException", wrap, 5);
/*      */         } 
/*      */ 
/*      */         
/* 3491 */         if (isVisible(cx, re)) {
/* 3492 */           Object wrap = cx.getWrapFactory().wrap(cx, scope, re, null);
/* 3493 */           ScriptableObject.defineProperty(errorObject, "rhinoException", wrap, 5);
/*      */         } 
/*      */ 
/*      */         
/* 3497 */         obj = errorObject;
/*      */       } 
/*      */     } 
/* 3500 */     NativeObject catchScopeObject = new NativeObject();
/*      */     
/* 3502 */     catchScopeObject.defineProperty(exceptionName, obj, 4);
/*      */ 
/*      */     
/* 3505 */     if (isVisible(cx, t))
/*      */     {
/*      */ 
/*      */       
/* 3509 */       catchScopeObject.defineProperty("__exception__", Context.javaToJS(t, scope), 6);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 3514 */     if (cacheObj) {
/* 3515 */       catchScopeObject.associateValue(t, obj);
/*      */     }
/* 3517 */     return catchScopeObject;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Scriptable wrapException(Throwable t, Scriptable scope, Context cx) {
/*      */     RhinoException re;
/*      */     String errorName, errorMsg;
/*      */     Object[] args;
/* 3526 */     Throwable javaException = null;
/*      */     
/* 3528 */     if (t instanceof EcmaError) {
/* 3529 */       EcmaError ee = (EcmaError)t;
/* 3530 */       re = ee;
/* 3531 */       errorName = ee.getName();
/* 3532 */       errorMsg = ee.getErrorMessage();
/* 3533 */     } else if (t instanceof WrappedException) {
/* 3534 */       WrappedException we = (WrappedException)t;
/* 3535 */       re = we;
/* 3536 */       javaException = we.getWrappedException();
/* 3537 */       errorName = "JavaException";
/* 3538 */       errorMsg = javaException.getClass().getName() + ": " + javaException.getMessage();
/*      */     }
/* 3540 */     else if (t instanceof EvaluatorException) {
/*      */       
/* 3542 */       EvaluatorException ee = (EvaluatorException)t;
/* 3543 */       re = ee;
/* 3544 */       errorName = "InternalError";
/* 3545 */       errorMsg = ee.getMessage();
/* 3546 */     } else if (cx.hasFeature(13)) {
/*      */ 
/*      */       
/* 3549 */       re = new WrappedException(t);
/* 3550 */       errorName = "JavaException";
/* 3551 */       errorMsg = t.toString();
/*      */     }
/*      */     else {
/*      */       
/* 3555 */       throw Kit.codeBug();
/*      */     } 
/*      */     
/* 3558 */     String sourceUri = re.sourceName();
/* 3559 */     if (sourceUri == null) {
/* 3560 */       sourceUri = "";
/*      */     }
/* 3562 */     int line = re.lineNumber();
/*      */     
/* 3564 */     if (line > 0) {
/* 3565 */       args = new Object[] { errorMsg, sourceUri, Integer.valueOf(line) };
/*      */     } else {
/* 3567 */       args = new Object[] { errorMsg, sourceUri };
/*      */     } 
/*      */     
/* 3570 */     Scriptable errorObject = cx.newObject(scope, errorName, args);
/* 3571 */     ScriptableObject.putProperty(errorObject, "name", errorName);
/*      */     
/* 3573 */     if (errorObject instanceof NativeError) {
/* 3574 */       ((NativeError)errorObject).setStackProvider(re);
/*      */     }
/*      */     
/* 3577 */     if (javaException != null && isVisible(cx, javaException)) {
/* 3578 */       Object wrap = cx.getWrapFactory().wrap(cx, scope, javaException, null);
/*      */       
/* 3580 */       ScriptableObject.defineProperty(errorObject, "javaException", wrap, 5);
/*      */     } 
/*      */ 
/*      */     
/* 3584 */     if (isVisible(cx, re)) {
/* 3585 */       Object wrap = cx.getWrapFactory().wrap(cx, scope, re, null);
/* 3586 */       ScriptableObject.defineProperty(errorObject, "rhinoException", wrap, 5);
/*      */     } 
/*      */ 
/*      */     
/* 3590 */     return errorObject;
/*      */   }
/*      */   
/*      */   private static boolean isVisible(Context cx, Object obj) {
/* 3594 */     ClassShutter shutter = cx.getClassShutter();
/* 3595 */     return (shutter == null || shutter.visibleToScripts(obj.getClass().getName()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Scriptable enterWith(Object obj, Context cx, Scriptable scope) {
/* 3602 */     Scriptable sobj = toObjectOrNull(cx, obj, scope);
/* 3603 */     if (sobj == null) {
/* 3604 */       throw typeError1("msg.undef.with", toString(obj));
/*      */     }
/* 3606 */     if (sobj instanceof XMLObject) {
/* 3607 */       XMLObject xmlObject = (XMLObject)sobj;
/* 3608 */       return xmlObject.enterWith(scope);
/*      */     } 
/* 3610 */     return new NativeWith(scope, sobj);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Scriptable leaveWith(Scriptable scope) {
/* 3615 */     NativeWith nw = (NativeWith)scope;
/* 3616 */     return nw.getParentScope();
/*      */   }
/*      */ 
/*      */   
/*      */   public static Scriptable enterDotQuery(Object value, Scriptable scope) {
/* 3621 */     if (!(value instanceof XMLObject)) {
/* 3622 */       throw notXmlError(value);
/*      */     }
/* 3624 */     XMLObject object = (XMLObject)value;
/* 3625 */     return object.enterDotQuery(scope);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object updateDotQuery(boolean value, Scriptable scope) {
/* 3631 */     NativeWith nw = (NativeWith)scope;
/* 3632 */     return nw.updateDotQuery(value);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Scriptable leaveDotQuery(Scriptable scope) {
/* 3637 */     NativeWith nw = (NativeWith)scope;
/* 3638 */     return nw.getParentScope();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setFunctionProtoAndParent(BaseFunction fn, Scriptable scope) {
/* 3644 */     fn.setParentScope(scope);
/* 3645 */     fn.setPrototype(ScriptableObject.getFunctionPrototype(scope));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setObjectProtoAndParent(ScriptableObject object, Scriptable scope) {
/* 3652 */     scope = ScriptableObject.getTopLevelScope(scope);
/* 3653 */     object.setParentScope(scope);
/* 3654 */     Scriptable proto = ScriptableObject.getClassPrototype(scope, object.getClassName());
/*      */     
/* 3656 */     object.setPrototype(proto);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setBuiltinProtoAndParent(ScriptableObject object, Scriptable scope, TopLevel.Builtins type) {
/* 3663 */     scope = ScriptableObject.getTopLevelScope(scope);
/* 3664 */     object.setParentScope(scope);
/* 3665 */     object.setPrototype(TopLevel.getBuiltinPrototype(scope, type));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void initFunction(Context cx, Scriptable scope, NativeFunction function, int type, boolean fromEvalCode) {
/* 3673 */     if (type == 1) {
/* 3674 */       String name = function.getFunctionName();
/* 3675 */       if (name != null && name.length() != 0) {
/* 3676 */         if (!fromEvalCode) {
/*      */ 
/*      */           
/* 3679 */           ScriptableObject.defineProperty(scope, name, function, 4);
/*      */         } else {
/*      */           
/* 3682 */           scope.put(name, scope, function);
/*      */         } 
/*      */       }
/* 3685 */     } else if (type == 3) {
/* 3686 */       String name = function.getFunctionName();
/* 3687 */       if (name != null && name.length() != 0) {
/*      */ 
/*      */ 
/*      */         
/* 3691 */         while (scope instanceof NativeWith) {
/* 3692 */           scope = scope.getParentScope();
/*      */         }
/* 3694 */         scope.put(name, scope, function);
/*      */       } 
/*      */     } else {
/* 3697 */       throw Kit.codeBug();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Scriptable newArrayLiteral(Object[] objects, int[] skipIndices, Context cx, Scriptable scope) {
/* 3705 */     int SKIP_DENSITY = 2;
/* 3706 */     int count = objects.length;
/* 3707 */     int skipCount = 0;
/* 3708 */     if (skipIndices != null) {
/* 3709 */       skipCount = skipIndices.length;
/*      */     }
/* 3711 */     int length = count + skipCount;
/* 3712 */     if (length > 1 && skipCount * 2 < length) {
/*      */       Object[] sparse;
/*      */       
/* 3715 */       if (skipCount == 0) {
/* 3716 */         sparse = objects;
/*      */       } else {
/* 3718 */         sparse = new Object[length];
/* 3719 */         int k = 0;
/* 3720 */         for (int m = 0, n = 0; m != length; m++) {
/* 3721 */           if (k != skipCount && skipIndices[k] == m) {
/* 3722 */             sparse[m] = Scriptable.NOT_FOUND;
/* 3723 */             k++;
/*      */           } else {
/*      */             
/* 3726 */             sparse[m] = objects[n];
/* 3727 */             n++;
/*      */           } 
/*      */         } 
/* 3730 */       }  return cx.newArray(scope, sparse);
/*      */     } 
/*      */     
/* 3733 */     Scriptable array = cx.newArray(scope, length);
/*      */     
/* 3735 */     int skip = 0;
/* 3736 */     for (int i = 0, j = 0; i != length; i++) {
/* 3737 */       if (skip != skipCount && skipIndices[skip] == i) {
/* 3738 */         skip++;
/*      */       } else {
/*      */         
/* 3741 */         ScriptableObject.putProperty(array, i, objects[j]);
/* 3742 */         j++;
/*      */       } 
/* 3744 */     }  return array;
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
/*      */   @Deprecated
/*      */   public static Scriptable newObjectLiteral(Object[] propertyIds, Object[] propertyValues, Context cx, Scriptable scope) {
/* 3760 */     return newObjectLiteral(propertyIds, propertyValues, null, cx, scope);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Scriptable newObjectLiteral(Object[] propertyIds, Object[] propertyValues, int[] getterSetters, Context cx, Scriptable scope) {
/* 3768 */     Scriptable object = cx.newObject(scope);
/* 3769 */     for (int i = 0, end = propertyIds.length; i != end; i++) {
/* 3770 */       Object id = propertyIds[i];
/* 3771 */       int getterSetter = (getterSetters == null) ? 0 : getterSetters[i];
/* 3772 */       Object value = propertyValues[i];
/* 3773 */       if (id instanceof String) {
/* 3774 */         if (getterSetter == 0) {
/* 3775 */           if (isSpecialProperty((String)id)) {
/* 3776 */             Ref ref = specialRef(object, (String)id, cx, scope);
/* 3777 */             ref.set(cx, scope, value);
/*      */           } else {
/* 3779 */             object.put((String)id, object, value);
/*      */           } 
/*      */         } else {
/* 3782 */           ScriptableObject so = (ScriptableObject)object;
/* 3783 */           Callable getterOrSetter = (Callable)value;
/* 3784 */           boolean isSetter = (getterSetter == 1);
/* 3785 */           so.setGetterOrSetter((String)id, 0, getterOrSetter, isSetter);
/*      */         } 
/*      */       } else {
/* 3788 */         int index = ((Integer)id).intValue();
/* 3789 */         object.put(index, object, value);
/*      */       } 
/*      */     } 
/* 3792 */     return object;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isArrayObject(Object obj) {
/* 3797 */     return (obj instanceof NativeArray || obj instanceof Arguments);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Object[] getArrayElements(Scriptable object) {
/* 3802 */     Context cx = Context.getContext();
/* 3803 */     long longLen = NativeArray.getLengthProperty(cx, object);
/* 3804 */     if (longLen > 2147483647L)
/*      */     {
/* 3806 */       throw new IllegalArgumentException();
/*      */     }
/* 3808 */     int len = (int)longLen;
/* 3809 */     if (len == 0) {
/* 3810 */       return emptyArgs;
/*      */     }
/* 3812 */     Object[] result = new Object[len];
/* 3813 */     for (int i = 0; i < len; i++) {
/* 3814 */       Object elem = ScriptableObject.getProperty(object, i);
/* 3815 */       result[i] = (elem == Scriptable.NOT_FOUND) ? Undefined.instance : elem;
/*      */     } 
/*      */     
/* 3818 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   static void checkDeprecated(Context cx, String name) {
/* 3823 */     int version = cx.getLanguageVersion();
/* 3824 */     if (version >= 140 || version == 0) {
/* 3825 */       String msg = getMessage1("msg.deprec.ctor", name);
/* 3826 */       if (version == 0) {
/* 3827 */         Context.reportWarning(msg);
/*      */       } else {
/* 3829 */         throw Context.reportRuntimeError(msg);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public static String getMessage0(String messageId) {
/* 3835 */     return getMessage(messageId, null);
/*      */   }
/*      */ 
/*      */   
/*      */   public static String getMessage1(String messageId, Object arg1) {
/* 3840 */     Object[] arguments = { arg1 };
/* 3841 */     return getMessage(messageId, arguments);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getMessage2(String messageId, Object arg1, Object arg2) {
/* 3847 */     Object[] arguments = { arg1, arg2 };
/* 3848 */     return getMessage(messageId, arguments);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getMessage3(String messageId, Object arg1, Object arg2, Object arg3) {
/* 3854 */     Object[] arguments = { arg1, arg2, arg3 };
/* 3855 */     return getMessage(messageId, arguments);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getMessage4(String messageId, Object arg1, Object arg2, Object arg3, Object arg4) {
/* 3861 */     Object[] arguments = { arg1, arg2, arg3, arg4 };
/* 3862 */     return getMessage(messageId, arguments);
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
/*      */   
/* 3883 */   public static MessageProvider messageProvider = new DefaultMessageProvider();
/*      */ 
/*      */   
/*      */   public static String getMessage(String messageId, Object[] arguments) {
/* 3887 */     return messageProvider.getMessage(messageId, arguments);
/*      */   }
/*      */   
/*      */   public static interface MessageProvider {
/*      */     String getMessage(String param1String, Object[] param1ArrayOfObject); }
/*      */   
/*      */   private static class DefaultMessageProvider implements MessageProvider { private DefaultMessageProvider() {}
/*      */     
/*      */     public String getMessage(String messageId, Object[] arguments) {
/* 3896 */       String formatString, defaultResource = "org.mozilla.javascript.resources.Messages";
/*      */ 
/*      */       
/* 3899 */       Context cx = Context.getCurrentContext();
/* 3900 */       Locale locale = (cx != null) ? cx.getLocale() : Locale.getDefault();
/*      */ 
/*      */       
/* 3903 */       ResourceBundle rb = ResourceBundle.getBundle("org.mozilla.javascript.resources.Messages", locale);
/*      */ 
/*      */       
/*      */       try {
/* 3907 */         formatString = rb.getString(messageId);
/* 3908 */       } catch (MissingResourceException mre) {
/* 3909 */         throw new RuntimeException("no message resource found for message property " + messageId);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3918 */       MessageFormat formatter = new MessageFormat(formatString);
/* 3919 */       return formatter.format(arguments);
/*      */     } }
/*      */ 
/*      */ 
/*      */   
/*      */   public static EcmaError constructError(String error, String message) {
/* 3925 */     int[] linep = new int[1];
/* 3926 */     String filename = Context.getSourcePositionFromStack(linep);
/* 3927 */     return constructError(error, message, filename, linep[0], null, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static EcmaError constructError(String error, String message, int lineNumberDelta) {
/* 3934 */     int[] linep = new int[1];
/* 3935 */     String filename = Context.getSourcePositionFromStack(linep);
/* 3936 */     if (linep[0] != 0) {
/* 3937 */       linep[0] = linep[0] + lineNumberDelta;
/*      */     }
/* 3939 */     return constructError(error, message, filename, linep[0], null, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static EcmaError constructError(String error, String message, String sourceName, int lineNumber, String lineSource, int columnNumber) {
/* 3949 */     return new EcmaError(error, message, sourceName, lineNumber, lineSource, columnNumber);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static EcmaError typeError(String message) {
/* 3955 */     return constructError("TypeError", message);
/*      */   }
/*      */ 
/*      */   
/*      */   public static EcmaError typeError0(String messageId) {
/* 3960 */     String msg = getMessage0(messageId);
/* 3961 */     return typeError(msg);
/*      */   }
/*      */ 
/*      */   
/*      */   public static EcmaError typeError1(String messageId, String arg1) {
/* 3966 */     String msg = getMessage1(messageId, arg1);
/* 3967 */     return typeError(msg);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static EcmaError typeError2(String messageId, String arg1, String arg2) {
/* 3973 */     String msg = getMessage2(messageId, arg1, arg2);
/* 3974 */     return typeError(msg);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static EcmaError typeError3(String messageId, String arg1, String arg2, String arg3) {
/* 3980 */     String msg = getMessage3(messageId, arg1, arg2, arg3);
/* 3981 */     return typeError(msg);
/*      */   }
/*      */ 
/*      */   
/*      */   public static RuntimeException undefReadError(Object object, Object id) {
/* 3986 */     return typeError2("msg.undef.prop.read", toString(object), toString(id));
/*      */   }
/*      */ 
/*      */   
/*      */   public static RuntimeException undefCallError(Object object, Object id) {
/* 3991 */     return typeError2("msg.undef.method.call", toString(object), toString(id));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static RuntimeException undefWriteError(Object object, Object id, Object value) {
/* 3998 */     return typeError3("msg.undef.prop.write", toString(object), toString(id), toString(value));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static RuntimeException undefDeleteError(Object object, Object id) {
/* 4004 */     throw typeError2("msg.undef.prop.delete", toString(object), toString(id));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static RuntimeException notFoundError(Scriptable object, String property) {
/* 4011 */     String msg = getMessage1("msg.is.not.defined", property);
/* 4012 */     throw constructError("ReferenceError", msg);
/*      */   }
/*      */ 
/*      */   
/*      */   public static RuntimeException notFunctionError(Object value) {
/* 4017 */     return notFunctionError(value, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static RuntimeException notFunctionError(Object value, Object messageHelper) {
/* 4024 */     String msg = (messageHelper == null) ? "null" : messageHelper.toString();
/*      */     
/* 4026 */     if (value == Scriptable.NOT_FOUND) {
/* 4027 */       return typeError1("msg.function.not.found", msg);
/*      */     }
/* 4029 */     return typeError2("msg.isnt.function", msg, typeof(value));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static RuntimeException notFunctionError(Object obj, Object value, String propertyName) {
/* 4036 */     String objString = toString(obj);
/* 4037 */     if (obj instanceof NativeFunction) {
/*      */       
/* 4039 */       int paren = objString.indexOf(')');
/* 4040 */       int curly = objString.indexOf('{', paren);
/* 4041 */       if (curly > -1) {
/* 4042 */         objString = objString.substring(0, curly + 1) + "...}";
/*      */       }
/*      */     } 
/* 4045 */     if (value == Scriptable.NOT_FOUND) {
/* 4046 */       return typeError2("msg.function.not.found.in", propertyName, objString);
/*      */     }
/*      */     
/* 4049 */     return typeError3("msg.isnt.function.in", propertyName, objString, typeof(value));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static RuntimeException notXmlError(Object value) {
/* 4055 */     throw typeError1("msg.isnt.xml.object", toString(value));
/*      */   }
/*      */ 
/*      */   
/*      */   private static void warnAboutNonJSObject(Object nonJSObject) {
/* 4060 */     String message = "RHINO USAGE WARNING: Missed Context.javaToJS() conversion:\nRhino runtime detected object " + nonJSObject + " of class " + nonJSObject.getClass().getName() + " where it expected String, Number, Boolean or Scriptable instance. Please check your code for missing Context.javaToJS() call.";
/*      */ 
/*      */     
/* 4063 */     Context.reportWarning(message);
/*      */     
/* 4065 */     System.err.println(message);
/*      */   }
/*      */ 
/*      */   
/*      */   public static RegExpProxy getRegExpProxy(Context cx) {
/* 4070 */     return cx.getRegExpProxy();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setRegExpProxy(Context cx, RegExpProxy proxy) {
/* 4075 */     if (proxy == null) throw new IllegalArgumentException(); 
/* 4076 */     cx.regExpProxy = proxy;
/*      */   }
/*      */ 
/*      */   
/*      */   public static RegExpProxy checkRegExpProxy(Context cx) {
/* 4081 */     RegExpProxy result = getRegExpProxy(cx);
/* 4082 */     if (result == null) {
/* 4083 */       throw Context.reportRuntimeError0("msg.no.regexp");
/*      */     }
/* 4085 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public static Scriptable wrapRegExp(Context cx, Scriptable scope, Object compiled) {
/* 4090 */     return cx.getRegExpProxy().wrapRegExp(cx, scope, compiled);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static XMLLib currentXMLLib(Context cx) {
/* 4096 */     if (cx.topCallScope == null) {
/* 4097 */       throw new IllegalStateException();
/*      */     }
/* 4099 */     XMLLib xmlLib = cx.cachedXMLLib;
/* 4100 */     if (xmlLib == null) {
/* 4101 */       xmlLib = XMLLib.extractFromScope(cx.topCallScope);
/* 4102 */       if (xmlLib == null)
/* 4103 */         throw new IllegalStateException(); 
/* 4104 */       cx.cachedXMLLib = xmlLib;
/*      */     } 
/*      */     
/* 4107 */     return xmlLib;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String escapeAttributeValue(Object value, Context cx) {
/* 4118 */     XMLLib xmlLib = currentXMLLib(cx);
/* 4119 */     return xmlLib.escapeAttributeValue(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String escapeTextValue(Object value, Context cx) {
/* 4130 */     XMLLib xmlLib = currentXMLLib(cx);
/* 4131 */     return xmlLib.escapeTextValue(value);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Ref memberRef(Object obj, Object elem, Context cx, int memberTypeFlags) {
/* 4137 */     if (!(obj instanceof XMLObject)) {
/* 4138 */       throw notXmlError(obj);
/*      */     }
/* 4140 */     XMLObject xmlObject = (XMLObject)obj;
/* 4141 */     return xmlObject.memberRef(cx, elem, memberTypeFlags);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Ref memberRef(Object obj, Object namespace, Object elem, Context cx, int memberTypeFlags) {
/* 4147 */     if (!(obj instanceof XMLObject)) {
/* 4148 */       throw notXmlError(obj);
/*      */     }
/* 4150 */     XMLObject xmlObject = (XMLObject)obj;
/* 4151 */     return xmlObject.memberRef(cx, namespace, elem, memberTypeFlags);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Ref nameRef(Object name, Context cx, Scriptable scope, int memberTypeFlags) {
/* 4157 */     XMLLib xmlLib = currentXMLLib(cx);
/* 4158 */     return xmlLib.nameRef(cx, name, scope, memberTypeFlags);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Ref nameRef(Object namespace, Object name, Context cx, Scriptable scope, int memberTypeFlags) {
/* 4164 */     XMLLib xmlLib = currentXMLLib(cx);
/* 4165 */     return xmlLib.nameRef(cx, namespace, name, scope, memberTypeFlags);
/*      */   }
/*      */ 
/*      */   
/*      */   private static void storeIndexResult(Context cx, int index) {
/* 4170 */     cx.scratchIndex = index;
/*      */   }
/*      */ 
/*      */   
/*      */   static int lastIndexResult(Context cx) {
/* 4175 */     return cx.scratchIndex;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void storeUint32Result(Context cx, long value) {
/* 4180 */     if (value >>> 32L != 0L)
/* 4181 */       throw new IllegalArgumentException(); 
/* 4182 */     cx.scratchUint32 = value;
/*      */   }
/*      */ 
/*      */   
/*      */   public static long lastUint32Result(Context cx) {
/* 4187 */     long value = cx.scratchUint32;
/* 4188 */     if (value >>> 32L != 0L)
/* 4189 */       throw new IllegalStateException(); 
/* 4190 */     return value;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static void storeScriptable(Context cx, Scriptable value) {
/* 4196 */     if (cx.scratchScriptable != null)
/* 4197 */       throw new IllegalStateException(); 
/* 4198 */     cx.scratchScriptable = value;
/*      */   }
/*      */ 
/*      */   
/*      */   public static Scriptable lastStoredScriptable(Context cx) {
/* 4203 */     Scriptable result = cx.scratchScriptable;
/* 4204 */     cx.scratchScriptable = null;
/* 4205 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static String makeUrlForGeneratedScript(boolean isEval, String masterScriptUrl, int masterScriptLine) {
/* 4211 */     if (isEval) {
/* 4212 */       return masterScriptUrl + '#' + masterScriptLine + "(eval)";
/*      */     }
/* 4214 */     return masterScriptUrl + '#' + masterScriptLine + "(Function)";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean isGeneratedScript(String sourceUrl) {
/* 4221 */     return (sourceUrl.indexOf("(eval)") >= 0 || sourceUrl.indexOf("(Function)") >= 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static RuntimeException errorWithClassName(String msg, Object val) {
/* 4227 */     return Context.reportRuntimeError1(msg, val.getClass().getName());
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
/*      */   public static JavaScriptException throwError(Context cx, Scriptable scope, String message) {
/* 4239 */     int[] linep = { 0 };
/* 4240 */     String filename = Context.getSourcePositionFromStack(linep);
/* 4241 */     Scriptable error = newBuiltinObject(cx, scope, TopLevel.Builtins.Error, new Object[] { message, filename, Integer.valueOf(linep[0]) });
/*      */     
/* 4243 */     return new JavaScriptException(error, filename, linep[0]);
/*      */   }
/*      */   
/* 4246 */   public static final Object[] emptyArgs = new Object[0];
/* 4247 */   public static final String[] emptyStrings = new String[0];
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ScriptRuntime.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */