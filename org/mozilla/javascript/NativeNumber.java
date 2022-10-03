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
/*     */ final class NativeNumber
/*     */   extends IdScriptableObject
/*     */ {
/*     */   static final long serialVersionUID = 3504516769741512101L;
/*  20 */   private static final Object NUMBER_TAG = "Number"; private static final int MAX_PRECISION = 100; private static final int Id_constructor = 1;
/*     */   private static final int Id_toString = 2;
/*     */   private static final int Id_toLocaleString = 3;
/*     */   private static final int Id_toSource = 4;
/*     */   
/*     */   static void init(Scriptable scope, boolean sealed) {
/*  26 */     NativeNumber obj = new NativeNumber(0.0D);
/*  27 */     obj.exportAsJSClass(8, scope, sealed);
/*     */   }
/*     */   private static final int Id_valueOf = 5; private static final int Id_toFixed = 6; private static final int Id_toExponential = 7; private static final int Id_toPrecision = 8; private static final int MAX_PROTOTYPE_ID = 8; private double doubleValue;
/*     */   
/*     */   NativeNumber(double number) {
/*  32 */     this.doubleValue = number;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  38 */     return "Number";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void fillConstructorProperties(IdFunctionObject ctor) {
/*  44 */     int attr = 7;
/*     */ 
/*     */ 
/*     */     
/*  48 */     ctor.defineProperty("NaN", ScriptRuntime.NaNobj, 7);
/*  49 */     ctor.defineProperty("POSITIVE_INFINITY", ScriptRuntime.wrapNumber(Double.POSITIVE_INFINITY), 7);
/*     */ 
/*     */     
/*  52 */     ctor.defineProperty("NEGATIVE_INFINITY", ScriptRuntime.wrapNumber(Double.NEGATIVE_INFINITY), 7);
/*     */ 
/*     */     
/*  55 */     ctor.defineProperty("MAX_VALUE", ScriptRuntime.wrapNumber(Double.MAX_VALUE), 7);
/*     */ 
/*     */     
/*  58 */     ctor.defineProperty("MIN_VALUE", ScriptRuntime.wrapNumber(Double.MIN_VALUE), 7);
/*     */ 
/*     */ 
/*     */     
/*  62 */     super.fillConstructorProperties(ctor);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initPrototypeId(int id) {
/*     */     String s;
/*     */     int arity;
/*  70 */     switch (id) { case 1:
/*  71 */         arity = 1; s = "constructor"; break;
/*  72 */       case 2: arity = 1; s = "toString"; break;
/*  73 */       case 3: arity = 1; s = "toLocaleString"; break;
/*  74 */       case 4: arity = 0; s = "toSource"; break;
/*  75 */       case 5: arity = 0; s = "valueOf"; break;
/*  76 */       case 6: arity = 1; s = "toFixed"; break;
/*  77 */       case 7: arity = 1; s = "toExponential"; break;
/*  78 */       case 8: arity = 1; s = "toPrecision"; break;
/*  79 */       default: throw new IllegalArgumentException(String.valueOf(id)); }
/*     */     
/*  81 */     initPrototypeMethod(NUMBER_TAG, id, s, arity);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/*     */     int base;
/*  88 */     if (!f.hasTag(NUMBER_TAG)) {
/*  89 */       return super.execIdCall(f, cx, scope, thisObj, args);
/*     */     }
/*  91 */     int id = f.methodId();
/*  92 */     if (id == 1) {
/*  93 */       double val = (args.length >= 1) ? ScriptRuntime.toNumber(args[0]) : 0.0D;
/*     */       
/*  95 */       if (thisObj == null)
/*     */       {
/*  97 */         return new NativeNumber(val);
/*     */       }
/*     */       
/* 100 */       return ScriptRuntime.wrapNumber(val);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 105 */     if (!(thisObj instanceof NativeNumber))
/* 106 */       throw incompatibleCallError(f); 
/* 107 */     double value = ((NativeNumber)thisObj).doubleValue;
/*     */     
/* 109 */     switch (id) {
/*     */ 
/*     */ 
/*     */       
/*     */       case 2:
/*     */       case 3:
/* 115 */         base = (args.length == 0 || args[0] == Undefined.instance) ? 10 : ScriptRuntime.toInt32(args[0]);
/*     */         
/* 117 */         return ScriptRuntime.numberToString(value, base);
/*     */ 
/*     */       
/*     */       case 4:
/* 121 */         return "(new Number(" + ScriptRuntime.toString(value) + "))";
/*     */       
/*     */       case 5:
/* 124 */         return ScriptRuntime.wrapNumber(value);
/*     */       
/*     */       case 6:
/* 127 */         return num_to(value, args, 2, 2, -20, 0);
/*     */ 
/*     */ 
/*     */       
/*     */       case 7:
/* 132 */         if (Double.isNaN(value)) {
/* 133 */           return "NaN";
/*     */         }
/* 135 */         if (Double.isInfinite(value)) {
/* 136 */           if (value >= 0.0D) {
/* 137 */             return "Infinity";
/*     */           }
/*     */           
/* 140 */           return "-Infinity";
/*     */         } 
/*     */ 
/*     */         
/* 144 */         return num_to(value, args, 1, 3, 0, 1);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 8:
/* 150 */         if (args.length == 0 || args[0] == Undefined.instance) {
/* 151 */           return ScriptRuntime.numberToString(value, 10);
/*     */         }
/*     */         
/* 154 */         if (Double.isNaN(value)) {
/* 155 */           return "NaN";
/*     */         }
/* 157 */         if (Double.isInfinite(value)) {
/* 158 */           if (value >= 0.0D) {
/* 159 */             return "Infinity";
/*     */           }
/*     */           
/* 162 */           return "-Infinity";
/*     */         } 
/*     */         
/* 165 */         return num_to(value, args, 0, 4, 1, 0);
/*     */     } 
/*     */ 
/*     */     
/* 169 */     throw new IllegalArgumentException(String.valueOf(id));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 175 */     return ScriptRuntime.numberToString(this.doubleValue, 10);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String num_to(double val, Object[] args, int zeroArgMode, int oneArgMode, int precisionMin, int precisionOffset) {
/*     */     int precision;
/* 184 */     if (args.length == 0) {
/* 185 */       precision = 0;
/* 186 */       oneArgMode = zeroArgMode;
/*     */     }
/*     */     else {
/*     */       
/* 190 */       double p = ScriptRuntime.toInteger(args[0]);
/* 191 */       if (p < precisionMin || p > 100.0D) {
/* 192 */         String msg = ScriptRuntime.getMessage1("msg.bad.precision", ScriptRuntime.toString(args[0]));
/*     */         
/* 194 */         throw ScriptRuntime.constructError("RangeError", msg);
/*     */       } 
/* 196 */       precision = ScriptRuntime.toInt32(p);
/*     */     } 
/* 198 */     StringBuilder sb = new StringBuilder();
/* 199 */     DToA.JS_dtostr(sb, oneArgMode, precision + precisionOffset, val);
/* 200 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int findPrototypeId(String s) {
/* 210 */     int c, id = 0; String X = null;
/* 211 */     switch (s.length()) { case 7:
/* 212 */         c = s.charAt(0);
/* 213 */         if (c == 116) { X = "toFixed"; id = 6; break; }
/* 214 */          if (c == 118) { X = "valueOf"; id = 5; }  break;
/*     */       case 8:
/* 216 */         c = s.charAt(3);
/* 217 */         if (c == 111) { X = "toSource"; id = 4; break; }
/* 218 */          if (c == 116) { X = "toString"; id = 2; }  break;
/*     */       case 11:
/* 220 */         c = s.charAt(0);
/* 221 */         if (c == 99) { X = "constructor"; id = 1; break; }
/* 222 */          if (c == 116) { X = "toPrecision"; id = 8; }  break;
/*     */       case 13:
/* 224 */         X = "toExponential"; id = 7; break;
/* 225 */       case 14: X = "toLocaleString"; id = 3; break; }
/*     */     
/* 227 */     if (X != null && X != s && !X.equals(s)) id = 0;
/*     */ 
/*     */ 
/*     */     
/* 231 */     return id;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\NativeNumber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */