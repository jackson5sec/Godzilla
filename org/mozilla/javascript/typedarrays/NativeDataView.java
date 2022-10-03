/*     */ package org.mozilla.javascript.typedarrays;
/*     */ 
/*     */ import org.mozilla.javascript.Context;
/*     */ import org.mozilla.javascript.IdFunctionObject;
/*     */ import org.mozilla.javascript.ScriptRuntime;
/*     */ import org.mozilla.javascript.Scriptable;
/*     */ import org.mozilla.javascript.Undefined;
/*     */ 
/*     */ public class NativeDataView
/*     */   extends NativeArrayBufferView {
/*     */   private static final long serialVersionUID = 1427967607557438968L;
/*     */   public static final String CLASS_NAME = "DataView";
/*     */   private static final int Id_constructor = 1;
/*     */   private static final int Id_getInt8 = 2;
/*     */   private static final int Id_getUint8 = 3;
/*     */   private static final int Id_getInt16 = 4;
/*     */   private static final int Id_getUint16 = 5;
/*     */   private static final int Id_getInt32 = 6;
/*     */   private static final int Id_getUint32 = 7;
/*     */   private static final int Id_getFloat32 = 8;
/*     */   private static final int Id_getFloat64 = 9;
/*     */   private static final int Id_setInt8 = 10;
/*     */   private static final int Id_setUint8 = 11;
/*     */   private static final int Id_setInt16 = 12;
/*     */   private static final int Id_setUint16 = 13;
/*     */   private static final int Id_setInt32 = 14;
/*     */   private static final int Id_setUint32 = 15;
/*     */   private static final int Id_setFloat32 = 16;
/*     */   private static final int Id_setFloat64 = 17;
/*     */   private static final int MAX_PROTOTYPE_ID = 17;
/*     */   
/*     */   public NativeDataView() {}
/*     */   
/*     */   public NativeDataView(NativeArrayBuffer ab, int offset, int length) {
/*  35 */     super(ab, offset, length);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  41 */     return "DataView";
/*     */   }
/*     */ 
/*     */   
/*     */   public static void init(Context cx, Scriptable scope, boolean sealed) {
/*  46 */     NativeDataView dv = new NativeDataView();
/*  47 */     dv.exportAsJSClass(17, scope, sealed);
/*     */   }
/*     */ 
/*     */   
/*     */   private void rangeCheck(int offset, int len) {
/*  52 */     if (offset < 0 || offset + len > this.byteLength) {
/*  53 */       throw ScriptRuntime.constructError("RangeError", "offset out of range");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkOffset(Object[] args, int pos) {
/*  59 */     if (args.length <= pos) {
/*  60 */       throw ScriptRuntime.constructError("TypeError", "missing required offset parameter");
/*     */     }
/*  62 */     if (Undefined.instance.equals(args[pos])) {
/*  63 */       throw ScriptRuntime.constructError("RangeError", "invalid offset");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkValue(Object[] args, int pos) {
/*  69 */     if (args.length <= pos) {
/*  70 */       throw ScriptRuntime.constructError("TypeError", "missing required value parameter");
/*     */     }
/*  72 */     if (Undefined.instance.equals(args[pos])) {
/*  73 */       throw ScriptRuntime.constructError("RangeError", "invalid value parameter");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static NativeDataView realThis(Scriptable thisObj, IdFunctionObject f) {
/*  79 */     if (!(thisObj instanceof NativeDataView))
/*  80 */       throw incompatibleCallError(f); 
/*  81 */     return (NativeDataView)thisObj;
/*     */   }
/*     */ 
/*     */   
/*     */   private NativeDataView js_constructor(NativeArrayBuffer ab, int offset, int length) {
/*  86 */     if (length < 0) {
/*  87 */       throw ScriptRuntime.constructError("RangeError", "length out of range");
/*     */     }
/*  89 */     if (offset < 0 || offset + length > ab.getLength()) {
/*  90 */       throw ScriptRuntime.constructError("RangeError", "offset out of range");
/*     */     }
/*  92 */     return new NativeDataView(ab, offset, length);
/*     */   }
/*     */ 
/*     */   
/*     */   private Object js_getInt(int bytes, boolean signed, Object[] args) {
/*  97 */     checkOffset(args, 0);
/*     */     
/*  99 */     int offset = ScriptRuntime.toInt32(args[0]);
/* 100 */     rangeCheck(offset, bytes);
/*     */     
/* 102 */     boolean littleEndian = (isArg(args, 1) && bytes > 1 && ScriptRuntime.toBoolean(args[1]));
/*     */ 
/*     */     
/* 105 */     switch (bytes) {
/*     */       case 1:
/* 107 */         return signed ? ByteIo.readInt8(this.arrayBuffer.buffer, offset) : ByteIo.readUint8(this.arrayBuffer.buffer, offset);
/*     */       
/*     */       case 2:
/* 110 */         return signed ? ByteIo.readInt16(this.arrayBuffer.buffer, offset, littleEndian) : ByteIo.readUint16(this.arrayBuffer.buffer, offset, littleEndian);
/*     */       
/*     */       case 4:
/* 113 */         return signed ? ByteIo.readInt32(this.arrayBuffer.buffer, offset, littleEndian) : ByteIo.readUint32(this.arrayBuffer.buffer, offset, littleEndian);
/*     */     } 
/*     */     
/* 116 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Object js_getFloat(int bytes, Object[] args) {
/* 122 */     checkOffset(args, 0);
/*     */     
/* 124 */     int offset = ScriptRuntime.toInt32(args[0]);
/* 125 */     rangeCheck(offset, bytes);
/*     */     
/* 127 */     boolean littleEndian = (isArg(args, 1) && bytes > 1 && ScriptRuntime.toBoolean(args[1]));
/*     */ 
/*     */     
/* 130 */     switch (bytes) {
/*     */       case 4:
/* 132 */         return ByteIo.readFloat32(this.arrayBuffer.buffer, offset, littleEndian);
/*     */       case 8:
/* 134 */         return ByteIo.readFloat64(this.arrayBuffer.buffer, offset, littleEndian);
/*     */     } 
/* 136 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void js_setInt(int bytes, boolean signed, Object[] args) {
/* 142 */     checkOffset(args, 0);
/* 143 */     checkValue(args, 1);
/*     */     
/* 145 */     int offset = ScriptRuntime.toInt32(args[0]);
/* 146 */     rangeCheck(offset, bytes);
/*     */     
/* 148 */     boolean littleEndian = (isArg(args, 2) && bytes > 1 && ScriptRuntime.toBoolean(args[2]));
/*     */ 
/*     */     
/* 151 */     switch (bytes) {
/*     */       case 1:
/* 153 */         if (signed) {
/* 154 */           ByteIo.writeInt8(this.arrayBuffer.buffer, offset, Conversions.toInt8(args[1]));
/*     */         } else {
/* 156 */           ByteIo.writeUint8(this.arrayBuffer.buffer, offset, Conversions.toUint8(args[1]));
/*     */         } 
/*     */         return;
/*     */       case 2:
/* 160 */         if (signed) {
/* 161 */           ByteIo.writeInt16(this.arrayBuffer.buffer, offset, Conversions.toInt16(args[1]), littleEndian);
/*     */         } else {
/* 163 */           ByteIo.writeUint16(this.arrayBuffer.buffer, offset, Conversions.toUint16(args[1]), littleEndian);
/*     */         } 
/*     */         return;
/*     */       case 4:
/* 167 */         if (signed) {
/* 168 */           ByteIo.writeInt32(this.arrayBuffer.buffer, offset, Conversions.toInt32(args[1]), littleEndian);
/*     */         } else {
/* 170 */           ByteIo.writeUint32(this.arrayBuffer.buffer, offset, Conversions.toUint32(args[1]), littleEndian);
/*     */         } 
/*     */         return;
/*     */     } 
/* 174 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void js_setFloat(int bytes, Object[] args) {
/* 180 */     checkOffset(args, 0);
/* 181 */     checkValue(args, 1);
/*     */     
/* 183 */     int offset = ScriptRuntime.toInt32(args[0]);
/* 184 */     rangeCheck(offset, bytes);
/*     */     
/* 186 */     boolean littleEndian = (isArg(args, 2) && bytes > 1 && ScriptRuntime.toBoolean(args[2]));
/*     */     
/* 188 */     double val = ScriptRuntime.toNumber(args[1]);
/*     */     
/* 190 */     switch (bytes) {
/*     */       case 4:
/* 192 */         ByteIo.writeFloat32(this.arrayBuffer.buffer, offset, val, littleEndian);
/*     */         return;
/*     */       case 8:
/* 195 */         ByteIo.writeFloat64(this.arrayBuffer.buffer, offset, val, littleEndian);
/*     */         return;
/*     */     } 
/* 198 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/* 208 */     if (!f.hasTag(getClassName())) {
/* 209 */       return super.execIdCall(f, cx, scope, thisObj, args);
/*     */     }
/* 211 */     int id = f.methodId();
/* 212 */     switch (id) {
/*     */       case 1:
/* 214 */         if (isArg(args, 0) && args[0] instanceof NativeArrayBuffer) {
/* 215 */           NativeArrayBuffer ab = (NativeArrayBuffer)args[0];
/* 216 */           int off = isArg(args, 1) ? ScriptRuntime.toInt32(args[1]) : 0;
/* 217 */           int len = isArg(args, 2) ? ScriptRuntime.toInt32(args[2]) : (ab.getLength() - off);
/* 218 */           return js_constructor(ab, off, len);
/*     */         } 
/* 220 */         throw ScriptRuntime.constructError("TypeError", "Missing parameters");
/*     */       
/*     */       case 2:
/* 223 */         return realThis(thisObj, f).js_getInt(1, true, args);
/*     */       case 3:
/* 225 */         return realThis(thisObj, f).js_getInt(1, false, args);
/*     */       case 4:
/* 227 */         return realThis(thisObj, f).js_getInt(2, true, args);
/*     */       case 5:
/* 229 */         return realThis(thisObj, f).js_getInt(2, false, args);
/*     */       case 6:
/* 231 */         return realThis(thisObj, f).js_getInt(4, true, args);
/*     */       case 7:
/* 233 */         return realThis(thisObj, f).js_getInt(4, false, args);
/*     */       case 8:
/* 235 */         return realThis(thisObj, f).js_getFloat(4, args);
/*     */       case 9:
/* 237 */         return realThis(thisObj, f).js_getFloat(8, args);
/*     */       case 10:
/* 239 */         realThis(thisObj, f).js_setInt(1, true, args);
/* 240 */         return Undefined.instance;
/*     */       case 11:
/* 242 */         realThis(thisObj, f).js_setInt(1, false, args);
/* 243 */         return Undefined.instance;
/*     */       case 12:
/* 245 */         realThis(thisObj, f).js_setInt(2, true, args);
/* 246 */         return Undefined.instance;
/*     */       case 13:
/* 248 */         realThis(thisObj, f).js_setInt(2, false, args);
/* 249 */         return Undefined.instance;
/*     */       case 14:
/* 251 */         realThis(thisObj, f).js_setInt(4, true, args);
/* 252 */         return Undefined.instance;
/*     */       case 15:
/* 254 */         realThis(thisObj, f).js_setInt(4, false, args);
/* 255 */         return Undefined.instance;
/*     */       case 16:
/* 257 */         realThis(thisObj, f).js_setFloat(4, args);
/* 258 */         return Undefined.instance;
/*     */       case 17:
/* 260 */         realThis(thisObj, f).js_setFloat(8, args);
/* 261 */         return Undefined.instance;
/*     */     } 
/* 263 */     throw new IllegalArgumentException(String.valueOf(id));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initPrototypeId(int id) {
/*     */     String s;
/*     */     int arity;
/* 271 */     switch (id) { case 1:
/* 272 */         arity = 1; s = "constructor"; break;
/* 273 */       case 2: arity = 1; s = "getInt8"; break;
/* 274 */       case 3: arity = 1; s = "getUint8"; break;
/* 275 */       case 4: arity = 1; s = "getInt16"; break;
/* 276 */       case 5: arity = 1; s = "getUint16"; break;
/* 277 */       case 6: arity = 1; s = "getInt32"; break;
/* 278 */       case 7: arity = 1; s = "getUint32"; break;
/* 279 */       case 8: arity = 1; s = "getFloat32"; break;
/* 280 */       case 9: arity = 1; s = "getFloat64"; break;
/* 281 */       case 10: arity = 2; s = "setInt8"; break;
/* 282 */       case 11: arity = 2; s = "setUint8"; break;
/* 283 */       case 12: arity = 2; s = "setInt16"; break;
/* 284 */       case 13: arity = 2; s = "setUint16"; break;
/* 285 */       case 14: arity = 2; s = "setInt32"; break;
/* 286 */       case 15: arity = 2; s = "setUint32"; break;
/* 287 */       case 16: arity = 2; s = "setFloat32"; break;
/* 288 */       case 17: arity = 2; s = "setFloat64"; break;
/* 289 */       default: throw new IllegalArgumentException(String.valueOf(id)); }
/*     */     
/* 291 */     initPrototypeMethod(getClassName(), id, s, arity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int findPrototypeId(String s) {
/* 301 */     int c, id = 0; String X = null;
/* 302 */     switch (s.length()) { case 7:
/* 303 */         c = s.charAt(0);
/* 304 */         if (c == 103) { X = "getInt8"; id = 2; break; }
/* 305 */          if (c == 115) { X = "setInt8"; id = 10; }  break;
/*     */       case 8:
/* 307 */         c = s.charAt(6);
/* 308 */         if (c == 49) {
/* 309 */           c = s.charAt(0);
/* 310 */           if (c == 103) { X = "getInt16"; id = 4; break; }
/* 311 */            if (c == 115) { X = "setInt16"; id = 12; }
/*     */            break;
/* 313 */         }  if (c == 51) {
/* 314 */           c = s.charAt(0);
/* 315 */           if (c == 103) { X = "getInt32"; id = 6; break; }
/* 316 */            if (c == 115) { X = "setInt32"; id = 14; }
/*     */            break;
/* 318 */         }  if (c == 116) {
/* 319 */           c = s.charAt(0);
/* 320 */           if (c == 103) { X = "getUint8"; id = 3; break; }
/* 321 */            if (c == 115) { X = "setUint8"; id = 11; } 
/*     */         }  break;
/*     */       case 9:
/* 324 */         c = s.charAt(0);
/* 325 */         if (c == 103) {
/* 326 */           c = s.charAt(8);
/* 327 */           if (c == 50) { X = "getUint32"; id = 7; break; }
/* 328 */            if (c == 54) { X = "getUint16"; id = 5; }
/*     */            break;
/* 330 */         }  if (c == 115) {
/* 331 */           c = s.charAt(8);
/* 332 */           if (c == 50) { X = "setUint32"; id = 15; break; }
/* 333 */            if (c == 54) { X = "setUint16"; id = 13; } 
/*     */         }  break;
/*     */       case 10:
/* 336 */         c = s.charAt(0);
/* 337 */         if (c == 103) {
/* 338 */           c = s.charAt(9);
/* 339 */           if (c == 50) { X = "getFloat32"; id = 8; break; }
/* 340 */            if (c == 52) { X = "getFloat64"; id = 9; }
/*     */            break;
/* 342 */         }  if (c == 115) {
/* 343 */           c = s.charAt(9);
/* 344 */           if (c == 50) { X = "setFloat32"; id = 16; break; }
/* 345 */            if (c == 52) { X = "setFloat64"; id = 17; } 
/*     */         }  break;
/*     */       case 11:
/* 348 */         X = "constructor"; id = 1; break; }
/*     */     
/* 350 */     if (X != null && X != s && !X.equals(s)) id = 0;
/*     */ 
/*     */ 
/*     */     
/* 354 */     return id;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\typedarrays\NativeDataView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */