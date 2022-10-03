/*     */ package org.mozilla.javascript.optimizer;
/*     */ import org.mozilla.javascript.Callable;
/*     */ import org.mozilla.javascript.ConsString;
/*     */ import org.mozilla.javascript.Context;
/*     */ import org.mozilla.javascript.ContextAction;
/*     */ import org.mozilla.javascript.NativeFunction;
/*     */ import org.mozilla.javascript.Script;
/*     */ import org.mozilla.javascript.ScriptRuntime;
/*     */ import org.mozilla.javascript.Scriptable;
/*     */ import org.mozilla.javascript.ScriptableObject;
/*     */ 
/*     */ public final class OptRuntime extends ScriptRuntime {
/*  13 */   public static final Double zeroObj = new Double(0.0D);
/*  14 */   public static final Double oneObj = new Double(1.0D);
/*  15 */   public static final Double minusOneObj = new Double(-1.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object call0(Callable fun, Scriptable thisObj, Context cx, Scriptable scope) {
/*  23 */     return fun.call(cx, scope, thisObj, ScriptRuntime.emptyArgs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object call1(Callable fun, Scriptable thisObj, Object arg0, Context cx, Scriptable scope) {
/*  32 */     return fun.call(cx, scope, thisObj, new Object[] { arg0 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object call2(Callable fun, Scriptable thisObj, Object arg0, Object arg1, Context cx, Scriptable scope) {
/*  42 */     return fun.call(cx, scope, thisObj, new Object[] { arg0, arg1 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object callN(Callable fun, Scriptable thisObj, Object[] args, Context cx, Scriptable scope) {
/*  52 */     return fun.call(cx, scope, thisObj, args);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object callName(Object[] args, String name, Context cx, Scriptable scope) {
/*  61 */     Callable f = getNameFunctionAndThis(name, cx, scope);
/*  62 */     Scriptable thisObj = lastStoredScriptable(cx);
/*  63 */     return f.call(cx, scope, thisObj, args);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object callName0(String name, Context cx, Scriptable scope) {
/*  72 */     Callable f = getNameFunctionAndThis(name, cx, scope);
/*  73 */     Scriptable thisObj = lastStoredScriptable(cx);
/*  74 */     return f.call(cx, scope, thisObj, ScriptRuntime.emptyArgs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object callProp0(Object value, String property, Context cx, Scriptable scope) {
/*  83 */     Callable f = getPropFunctionAndThis(value, property, cx, scope);
/*  84 */     Scriptable thisObj = lastStoredScriptable(cx);
/*  85 */     return f.call(cx, scope, thisObj, ScriptRuntime.emptyArgs);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Object add(Object val1, double val2) {
/*  90 */     if (val1 instanceof Scriptable)
/*  91 */       val1 = ((Scriptable)val1).getDefaultValue(null); 
/*  92 */     if (!(val1 instanceof CharSequence))
/*  93 */       return wrapDouble(toNumber(val1) + val2); 
/*  94 */     return new ConsString((CharSequence)val1, toString(val2));
/*     */   }
/*     */ 
/*     */   
/*     */   public static Object add(double val1, Object val2) {
/*  99 */     if (val2 instanceof Scriptable)
/* 100 */       val2 = ((Scriptable)val2).getDefaultValue(null); 
/* 101 */     if (!(val2 instanceof CharSequence))
/* 102 */       return wrapDouble(toNumber(val2) + val1); 
/* 103 */     return new ConsString(toString(val1), (CharSequence)val2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static Object elemIncrDecr(Object obj, double index, Context cx, int incrDecrMask) {
/* 113 */     return elemIncrDecr(obj, index, cx, getTopCallScope(cx), incrDecrMask);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object elemIncrDecr(Object obj, double index, Context cx, Scriptable scope, int incrDecrMask) {
/* 120 */     return ScriptRuntime.elemIncrDecr(obj, new Double(index), cx, scope, incrDecrMask);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Object[] padStart(Object[] currentArgs, int count) {
/* 125 */     Object[] result = new Object[currentArgs.length + count];
/* 126 */     System.arraycopy(currentArgs, 0, result, count, currentArgs.length);
/* 127 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void initFunction(NativeFunction fn, int functionType, Scriptable scope, Context cx) {
/* 133 */     ScriptRuntime.initFunction(cx, scope, fn, functionType, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object callSpecial(Context cx, Callable fun, Scriptable thisObj, Object[] args, Scriptable scope, Scriptable callerThis, int callType, String fileName, int lineNumber) {
/* 142 */     return ScriptRuntime.callSpecial(cx, fun, thisObj, args, scope, callerThis, callType, fileName, lineNumber);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object newObjectSpecial(Context cx, Object fun, Object[] args, Scriptable scope, Scriptable callerThis, int callType) {
/* 151 */     return ScriptRuntime.newSpecial(cx, fun, args, scope, callType);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Double wrapDouble(double num) {
/* 156 */     if (num == 0.0D) {
/* 157 */       if (1.0D / num > 0.0D)
/*     */       {
/* 159 */         return zeroObj; } 
/*     */     } else {
/* 161 */       if (num == 1.0D)
/* 162 */         return oneObj; 
/* 163 */       if (num == -1.0D)
/* 164 */         return minusOneObj; 
/* 165 */       if (num != num)
/* 166 */         return NaNobj; 
/*     */     } 
/* 168 */     return new Double(num);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static String encodeIntArray(int[] array) {
/* 174 */     if (array == null) return null; 
/* 175 */     int n = array.length;
/* 176 */     char[] buffer = new char[1 + n * 2];
/* 177 */     buffer[0] = '\001';
/* 178 */     for (int i = 0; i != n; i++) {
/* 179 */       int value = array[i];
/* 180 */       int shift = 1 + i * 2;
/* 181 */       buffer[shift] = (char)(value >>> 16);
/* 182 */       buffer[shift + 1] = (char)value;
/*     */     } 
/* 184 */     return new String(buffer);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int[] decodeIntArray(String str, int arraySize) {
/* 190 */     if (arraySize == 0) {
/* 191 */       if (str != null) throw new IllegalArgumentException(); 
/* 192 */       return null;
/*     */     } 
/* 194 */     if (str.length() != 1 + arraySize * 2 && str.charAt(0) != '\001') {
/* 195 */       throw new IllegalArgumentException();
/*     */     }
/* 197 */     int[] array = new int[arraySize];
/* 198 */     for (int i = 0; i != arraySize; i++) {
/* 199 */       int shift = 1 + i * 2;
/* 200 */       array[i] = str.charAt(shift) << 16 | str.charAt(shift + 1);
/*     */     } 
/* 202 */     return array;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Scriptable newArrayLiteral(Object[] objects, String encodedInts, int skipCount, Context cx, Scriptable scope) {
/* 211 */     int[] skipIndexces = decodeIntArray(encodedInts, skipCount);
/* 212 */     return newArrayLiteral(objects, skipIndexces, cx, scope);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void main(final Script script, final String[] args) {
/* 217 */     ContextFactory.getGlobal().call(new ContextAction()
/*     */         {
/*     */           public Object run(Context cx) {
/* 220 */             ScriptableObject global = ScriptRuntime.getGlobal(cx);
/*     */ 
/*     */ 
/*     */             
/* 224 */             Object[] argsCopy = new Object[args.length];
/* 225 */             System.arraycopy(args, 0, argsCopy, 0, args.length);
/* 226 */             Scriptable argsObj = cx.newArray((Scriptable)global, argsCopy);
/* 227 */             global.defineProperty("arguments", argsObj, 2);
/*     */             
/* 229 */             script.exec(cx, (Scriptable)global);
/* 230 */             return null;
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public static void throwStopIteration(Object obj) {
/* 236 */     throw new JavaScriptException(NativeIterator.getStopIterationObject((Scriptable)obj), "", 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Scriptable createNativeGenerator(NativeFunction funObj, Scriptable scope, Scriptable thisObj, int maxLocals, int maxStack) {
/* 246 */     return (Scriptable)new NativeGenerator(scope, funObj, new GeneratorState(thisObj, maxLocals, maxStack));
/*     */   }
/*     */ 
/*     */   
/*     */   public static Object[] getGeneratorStackState(Object obj) {
/* 251 */     GeneratorState rgs = (GeneratorState)obj;
/* 252 */     if (rgs.stackState == null)
/* 253 */       rgs.stackState = new Object[rgs.maxStack]; 
/* 254 */     return rgs.stackState;
/*     */   }
/*     */   
/*     */   public static Object[] getGeneratorLocalsState(Object obj) {
/* 258 */     GeneratorState rgs = (GeneratorState)obj;
/* 259 */     if (rgs.localsState == null)
/* 260 */       rgs.localsState = new Object[rgs.maxLocals]; 
/* 261 */     return rgs.localsState;
/*     */   }
/*     */ 
/*     */   
/*     */   public static class GeneratorState
/*     */   {
/*     */     static final String CLASS_NAME = "org/mozilla/javascript/optimizer/OptRuntime$GeneratorState";
/*     */     
/*     */     public int resumptionPoint;
/*     */     
/*     */     static final String resumptionPoint_NAME = "resumptionPoint";
/*     */     
/*     */     static final String resumptionPoint_TYPE = "I";
/*     */     public Scriptable thisObj;
/*     */     static final String thisObj_NAME = "thisObj";
/*     */     static final String thisObj_TYPE = "Lorg/mozilla/javascript/Scriptable;";
/*     */     Object[] stackState;
/*     */     Object[] localsState;
/*     */     int maxLocals;
/*     */     int maxStack;
/*     */     
/*     */     GeneratorState(Scriptable thisObj, int maxLocals, int maxStack) {
/* 283 */       this.thisObj = thisObj;
/* 284 */       this.maxLocals = maxLocals;
/* 285 */       this.maxStack = maxStack;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\optimizer\OptRuntime.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */