/*     */ package org.mozilla.javascript;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Stack;
/*     */ import org.mozilla.javascript.json.JsonParser;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class NativeJSON
/*     */   extends IdScriptableObject
/*     */ {
/*     */   static final long serialVersionUID = -4567599697595654984L;
/*  27 */   private static final Object JSON_TAG = "JSON";
/*     */   private static final int MAX_STRINGIFY_GAP_LENGTH = 10;
/*     */   private static final int Id_toSource = 1;
/*     */   private static final int Id_parse = 2;
/*     */   
/*     */   static void init(Scriptable scope, boolean sealed) {
/*  33 */     NativeJSON obj = new NativeJSON();
/*  34 */     obj.activatePrototypeMap(3);
/*  35 */     obj.setPrototype(getObjectPrototype(scope));
/*  36 */     obj.setParentScope(scope);
/*  37 */     if (sealed) obj.sealObject(); 
/*  38 */     ScriptableObject.defineProperty(scope, "JSON", obj, 2);
/*     */   }
/*     */ 
/*     */   
/*     */   private static final int Id_stringify = 3;
/*     */   private static final int LAST_METHOD_ID = 3;
/*     */   private static final int MAX_ID = 3;
/*     */   
/*     */   public String getClassName() {
/*  47 */     return "JSON";
/*     */   }
/*     */ 
/*     */   
/*     */   protected void initPrototypeId(int id) {
/*  52 */     if (id <= 3) {
/*     */       String name;
/*     */       int arity;
/*  55 */       switch (id) { case 1:
/*  56 */           arity = 0; name = "toSource"; break;
/*  57 */         case 2: arity = 2; name = "parse"; break;
/*  58 */         case 3: arity = 3; name = "stringify"; break;
/*  59 */         default: throw new IllegalStateException(String.valueOf(id)); }
/*     */       
/*  61 */       initPrototypeMethod(JSON_TAG, id, name, arity);
/*     */     } else {
/*  63 */       throw new IllegalStateException(String.valueOf(id));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/*     */     String jtext;
/*     */     Object value, reviver, replacer, space;
/*  71 */     if (!f.hasTag(JSON_TAG)) {
/*  72 */       return super.execIdCall(f, cx, scope, thisObj, args);
/*     */     }
/*  74 */     int methodId = f.methodId();
/*  75 */     switch (methodId) {
/*     */       case 1:
/*  77 */         return "JSON";
/*     */       
/*     */       case 2:
/*  80 */         jtext = ScriptRuntime.toString(args, 0);
/*  81 */         reviver = null;
/*  82 */         if (args.length > 1) {
/*  83 */           reviver = args[1];
/*     */         }
/*  85 */         if (reviver instanceof Callable) {
/*  86 */           return parse(cx, scope, jtext, (Callable)reviver);
/*     */         }
/*  88 */         return parse(cx, scope, jtext);
/*     */ 
/*     */ 
/*     */       
/*     */       case 3:
/*  93 */         value = null; replacer = null; space = null;
/*  94 */         switch (args.length) {
/*     */           default:
/*  96 */             space = args[2];
/*  97 */           case 2: replacer = args[1];
/*  98 */           case 1: value = args[0]; break;
/*     */           case 0:
/*     */             break;
/* 101 */         }  return stringify(cx, scope, value, replacer, space);
/*     */     } 
/*     */     
/* 104 */     throw new IllegalStateException(String.valueOf(methodId));
/*     */   }
/*     */ 
/*     */   
/*     */   private static Object parse(Context cx, Scriptable scope, String jtext) {
/*     */     try {
/* 110 */       return (new JsonParser(cx, scope)).parseValue(jtext);
/* 111 */     } catch (org.mozilla.javascript.json.JsonParser.ParseException ex) {
/* 112 */       throw ScriptRuntime.constructError("SyntaxError", ex.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object parse(Context cx, Scriptable scope, String jtext, Callable reviver) {
/* 119 */     Object unfiltered = parse(cx, scope, jtext);
/* 120 */     Scriptable root = cx.newObject(scope);
/* 121 */     root.put("", root, unfiltered);
/* 122 */     return walk(cx, scope, reviver, root, "");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Object walk(Context cx, Scriptable scope, Callable reviver, Scriptable holder, Object name) {
/*     */     Object property;
/* 129 */     if (name instanceof Number) {
/* 130 */       property = holder.get(((Number)name).intValue(), holder);
/*     */     } else {
/* 132 */       property = holder.get((String)name, holder);
/*     */     } 
/*     */     
/* 135 */     if (property instanceof Scriptable) {
/* 136 */       Scriptable val = (Scriptable)property;
/* 137 */       if (val instanceof NativeArray) {
/* 138 */         long len = ((NativeArray)val).getLength(); long i;
/* 139 */         for (i = 0L; i < len; i++) {
/*     */           
/* 141 */           if (i > 2147483647L) {
/* 142 */             String id = Long.toString(i);
/* 143 */             Object newElement = walk(cx, scope, reviver, val, id);
/* 144 */             if (newElement == Undefined.instance) {
/* 145 */               val.delete(id);
/*     */             } else {
/* 147 */               val.put(id, val, newElement);
/*     */             } 
/*     */           } else {
/* 150 */             int idx = (int)i;
/* 151 */             Object newElement = walk(cx, scope, reviver, val, Integer.valueOf(idx));
/* 152 */             if (newElement == Undefined.instance) {
/* 153 */               val.delete(idx);
/*     */             } else {
/* 155 */               val.put(idx, val, newElement);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } else {
/* 160 */         Object[] keys = val.getIds();
/* 161 */         for (Object p : keys) {
/* 162 */           Object newElement = walk(cx, scope, reviver, val, p);
/* 163 */           if (newElement == Undefined.instance) {
/* 164 */             if (p instanceof Number) {
/* 165 */               val.delete(((Number)p).intValue());
/*     */             } else {
/* 167 */               val.delete((String)p);
/*     */             } 
/* 169 */           } else if (p instanceof Number) {
/* 170 */             val.put(((Number)p).intValue(), val, newElement);
/*     */           } else {
/* 172 */             val.put((String)p, val, newElement);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 178 */     return reviver.call(cx, scope, holder, new Object[] { name, property });
/*     */   }
/*     */   
/*     */   private static String repeat(char c, int count) {
/* 182 */     char[] chars = new char[count];
/* 183 */     Arrays.fill(chars, c);
/* 184 */     return new String(chars);
/*     */   }
/*     */ 
/*     */   
/*     */   private static class StringifyState
/*     */   {
/*     */     Stack<Scriptable> stack;
/*     */     
/*     */     String indent;
/*     */     
/*     */     String gap;
/*     */     Callable replacer;
/*     */     List<Object> propertyList;
/*     */     Object space;
/*     */     Context cx;
/*     */     Scriptable scope;
/*     */     
/*     */     StringifyState(Context cx, Scriptable scope, String indent, String gap, Callable replacer, List<Object> propertyList, Object space) {
/* 202 */       this.stack = new Stack<Scriptable>();
/*     */       this.cx = cx;
/*     */       this.scope = scope;
/*     */       this.indent = indent;
/*     */       this.gap = gap;
/*     */       this.replacer = replacer;
/*     */       this.propertyList = propertyList;
/*     */       this.space = space;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object stringify(Context cx, Scriptable scope, Object value, Object replacer, Object space) {
/* 216 */     String indent = "";
/* 217 */     String gap = "";
/*     */     
/* 219 */     List<Object> propertyList = null;
/* 220 */     Callable replacerFunction = null;
/*     */     
/* 222 */     if (replacer instanceof Callable) {
/* 223 */       replacerFunction = (Callable)replacer;
/* 224 */     } else if (replacer instanceof NativeArray) {
/* 225 */       propertyList = new LinkedList();
/* 226 */       NativeArray replacerArray = (NativeArray)replacer; Integer[] arr$; int len$, i$;
/* 227 */       for (arr$ = replacerArray.getIndexIds(), len$ = arr$.length, i$ = 0; i$ < len$; ) { int i = arr$[i$].intValue();
/* 228 */         Object v = replacerArray.get(i, replacerArray);
/* 229 */         if (v instanceof String || v instanceof Number) {
/* 230 */           propertyList.add(v);
/* 231 */         } else if (v instanceof NativeString || v instanceof NativeNumber) {
/* 232 */           propertyList.add(ScriptRuntime.toString(v));
/*     */         } 
/*     */         i$++; }
/*     */     
/*     */     } 
/* 237 */     if (space instanceof NativeNumber) {
/* 238 */       space = Double.valueOf(ScriptRuntime.toNumber(space));
/* 239 */     } else if (space instanceof NativeString) {
/* 240 */       space = ScriptRuntime.toString(space);
/*     */     } 
/*     */     
/* 243 */     if (space instanceof Number) {
/* 244 */       int gapLength = (int)ScriptRuntime.toInteger(space);
/* 245 */       gapLength = Math.min(10, gapLength);
/* 246 */       gap = (gapLength > 0) ? repeat(' ', gapLength) : "";
/* 247 */       space = Integer.valueOf(gapLength);
/* 248 */     } else if (space instanceof String) {
/* 249 */       gap = (String)space;
/* 250 */       if (gap.length() > 10) {
/* 251 */         gap = gap.substring(0, 10);
/*     */       }
/*     */     } 
/*     */     
/* 255 */     StringifyState state = new StringifyState(cx, scope, indent, gap, replacerFunction, propertyList, space);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 262 */     ScriptableObject wrapper = new NativeObject();
/* 263 */     wrapper.setParentScope(scope);
/* 264 */     wrapper.setPrototype(ScriptableObject.getObjectPrototype(scope));
/* 265 */     wrapper.defineProperty("", value, 0);
/* 266 */     return str("", wrapper, state);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Object str(Object key, Scriptable holder, StringifyState state) {
/* 272 */     Object value = null;
/* 273 */     if (key instanceof String) {
/* 274 */       value = getProperty(holder, (String)key);
/*     */     } else {
/* 276 */       value = getProperty(holder, ((Number)key).intValue());
/*     */     } 
/*     */     
/* 279 */     if (value instanceof Scriptable) {
/* 280 */       Object toJSON = getProperty((Scriptable)value, "toJSON");
/* 281 */       if (toJSON instanceof Callable) {
/* 282 */         value = callMethod(state.cx, (Scriptable)value, "toJSON", new Object[] { key });
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 287 */     if (state.replacer != null) {
/* 288 */       value = state.replacer.call(state.cx, state.scope, holder, new Object[] { key, value });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 293 */     if (value instanceof NativeNumber) {
/* 294 */       value = Double.valueOf(ScriptRuntime.toNumber(value));
/* 295 */     } else if (value instanceof NativeString) {
/* 296 */       value = ScriptRuntime.toString(value);
/* 297 */     } else if (value instanceof NativeBoolean) {
/* 298 */       value = ((NativeBoolean)value).getDefaultValue(ScriptRuntime.BooleanClass);
/*     */     } 
/*     */     
/* 301 */     if (value == null) return "null"; 
/* 302 */     if (value.equals(Boolean.TRUE)) return "true"; 
/* 303 */     if (value.equals(Boolean.FALSE)) return "false";
/*     */     
/* 305 */     if (value instanceof CharSequence) {
/* 306 */       return quote(value.toString());
/*     */     }
/*     */     
/* 309 */     if (value instanceof Number) {
/* 310 */       double d = ((Number)value).doubleValue();
/* 311 */       if (d == d && d != Double.POSITIVE_INFINITY && d != Double.NEGATIVE_INFINITY)
/*     */       {
/*     */         
/* 314 */         return ScriptRuntime.toString(value);
/*     */       }
/* 316 */       return "null";
/*     */     } 
/*     */ 
/*     */     
/* 320 */     if (value instanceof Scriptable && !(value instanceof Callable)) {
/* 321 */       if (value instanceof NativeArray) {
/* 322 */         return ja((NativeArray)value, state);
/*     */       }
/* 324 */       return jo((Scriptable)value, state);
/*     */     } 
/*     */     
/* 327 */     return Undefined.instance;
/*     */   }
/*     */   
/*     */   private static String join(Collection<Object> objs, String delimiter) {
/* 331 */     if (objs == null || objs.isEmpty()) {
/* 332 */       return "";
/*     */     }
/* 334 */     Iterator<Object> iter = objs.iterator();
/* 335 */     if (!iter.hasNext()) return ""; 
/* 336 */     StringBuilder builder = new StringBuilder(iter.next().toString());
/* 337 */     while (iter.hasNext()) {
/* 338 */       builder.append(delimiter).append(iter.next().toString());
/*     */     }
/* 340 */     return builder.toString();
/*     */   }
/*     */   private static String jo(Scriptable value, StringifyState state) {
/*     */     String finalValue;
/* 344 */     if (state.stack.search(value) != -1) {
/* 345 */       throw ScriptRuntime.typeError0("msg.cyclic.value");
/*     */     }
/* 347 */     state.stack.push(value);
/*     */     
/* 349 */     String stepback = state.indent;
/* 350 */     state.indent += state.gap;
/* 351 */     Object[] k = null;
/* 352 */     if (state.propertyList != null) {
/* 353 */       k = state.propertyList.toArray();
/*     */     } else {
/* 355 */       k = value.getIds();
/*     */     } 
/*     */     
/* 358 */     List<Object> partial = new LinkedList();
/*     */     
/* 360 */     for (Object p : k) {
/* 361 */       Object strP = str(p, value, state);
/* 362 */       if (strP != Undefined.instance) {
/* 363 */         String member = quote(p.toString()) + ":";
/* 364 */         if (state.gap.length() > 0) {
/* 365 */           member = member + " ";
/*     */         }
/* 367 */         member = member + strP;
/* 368 */         partial.add(member);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 374 */     if (partial.isEmpty()) {
/* 375 */       finalValue = "{}";
/*     */     }
/* 377 */     else if (state.gap.length() == 0) {
/* 378 */       finalValue = '{' + join(partial, ",") + '}';
/*     */     } else {
/* 380 */       String separator = ",\n" + state.indent;
/* 381 */       String properties = join(partial, separator);
/* 382 */       finalValue = "{\n" + state.indent + properties + '\n' + stepback + '}';
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 387 */     state.stack.pop();
/* 388 */     state.indent = stepback;
/* 389 */     return finalValue;
/*     */   }
/*     */   private static String ja(NativeArray value, StringifyState state) {
/*     */     String finalValue;
/* 393 */     if (state.stack.search(value) != -1) {
/* 394 */       throw ScriptRuntime.typeError0("msg.cyclic.value");
/*     */     }
/* 396 */     state.stack.push(value);
/*     */     
/* 398 */     String stepback = state.indent;
/* 399 */     state.indent += state.gap;
/* 400 */     List<Object> partial = new LinkedList();
/*     */     
/* 402 */     long len = value.getLength(); long index;
/* 403 */     for (index = 0L; index < len; index++) {
/*     */       Object strP;
/* 405 */       if (index > 2147483647L) {
/* 406 */         strP = str(Long.toString(index), value, state);
/*     */       } else {
/* 408 */         strP = str(Integer.valueOf((int)index), value, state);
/*     */       } 
/* 410 */       if (strP == Undefined.instance) {
/* 411 */         partial.add("null");
/*     */       } else {
/* 413 */         partial.add(strP);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 419 */     if (partial.isEmpty()) {
/* 420 */       finalValue = "[]";
/*     */     }
/* 422 */     else if (state.gap.length() == 0) {
/* 423 */       finalValue = '[' + join(partial, ",") + ']';
/*     */     } else {
/* 425 */       String separator = ",\n" + state.indent;
/* 426 */       String properties = join(partial, separator);
/* 427 */       finalValue = "[\n" + state.indent + properties + '\n' + stepback + ']';
/*     */     } 
/*     */ 
/*     */     
/* 431 */     state.stack.pop();
/* 432 */     state.indent = stepback;
/* 433 */     return finalValue;
/*     */   }
/*     */   
/*     */   private static String quote(String string) {
/* 437 */     StringBuilder product = new StringBuilder(string.length() + 2);
/* 438 */     product.append('"');
/* 439 */     int length = string.length();
/* 440 */     for (int i = 0; i < length; i++) {
/* 441 */       char c = string.charAt(i);
/* 442 */       switch (c) {
/*     */         case '"':
/* 444 */           product.append("\\\"");
/*     */           break;
/*     */         case '\\':
/* 447 */           product.append("\\\\");
/*     */           break;
/*     */         case '\b':
/* 450 */           product.append("\\b");
/*     */           break;
/*     */         case '\f':
/* 453 */           product.append("\\f");
/*     */           break;
/*     */         case '\n':
/* 456 */           product.append("\\n");
/*     */           break;
/*     */         case '\r':
/* 459 */           product.append("\\r");
/*     */           break;
/*     */         case '\t':
/* 462 */           product.append("\\t");
/*     */           break;
/*     */         default:
/* 465 */           if (c < ' ') {
/* 466 */             product.append("\\u");
/* 467 */             String hex = String.format("%04x", new Object[] { Integer.valueOf(c) });
/* 468 */             product.append(hex);
/*     */             break;
/*     */           } 
/* 471 */           product.append(c);
/*     */           break;
/*     */       } 
/*     */     
/*     */     } 
/* 476 */     product.append('"');
/* 477 */     return product.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int findPrototypeId(String s) {
/* 487 */     int id = 0; String X = null;
/* 488 */     switch (s.length()) { case 5:
/* 489 */         X = "parse"; id = 2; break;
/* 490 */       case 8: X = "toSource"; id = 1; break;
/* 491 */       case 9: X = "stringify"; id = 3; break; }
/*     */     
/* 493 */     if (X != null && X != s && !X.equals(s)) id = 0;
/*     */ 
/*     */     
/* 496 */     return id;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\NativeJSON.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */