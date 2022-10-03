/*     */ package org.mozilla.javascript.xmlimpl;
/*     */ 
/*     */ import org.mozilla.javascript.Context;
/*     */ import org.mozilla.javascript.IdFunctionCall;
/*     */ import org.mozilla.javascript.IdFunctionObject;
/*     */ import org.mozilla.javascript.ScriptRuntime;
/*     */ import org.mozilla.javascript.Scriptable;
/*     */ import org.mozilla.javascript.ScriptableObject;
/*     */ import org.mozilla.javascript.Undefined;
/*     */ 
/*     */ class XMLCtor
/*     */   extends IdFunctionObject
/*     */ {
/*     */   static final long serialVersionUID = -8708195078359817341L;
/*  15 */   private static final Object XMLCTOR_TAG = "XMLCtor"; private XmlProcessor options;
/*     */   private static final int Id_ignoreComments = 1;
/*     */   private static final int Id_ignoreProcessingInstructions = 2;
/*     */   private static final int Id_ignoreWhitespace = 3;
/*     */   private static final int Id_prettyIndent = 4;
/*     */   
/*     */   XMLCtor(XML xml, Object tag, int id, int arity) {
/*  22 */     super((IdFunctionCall)xml, tag, id, arity);
/*     */     
/*  24 */     this.options = xml.getProcessor();
/*  25 */     activatePrototypeMap(3);
/*     */   }
/*     */   private static final int Id_prettyPrinting = 5; private static final int MAX_INSTANCE_ID = 5; private static final int Id_defaultSettings = 1; private static final int Id_settings = 2; private static final int Id_setSettings = 3; private static final int MAX_FUNCTION_ID = 3;
/*     */   
/*     */   private void writeSetting(Scriptable target) {
/*  30 */     for (int i = 1; i <= 5; i++) {
/*  31 */       int id = super.getMaxInstanceId() + i;
/*  32 */       String name = getInstanceIdName(id);
/*  33 */       Object value = getInstanceIdValue(id);
/*  34 */       ScriptableObject.putProperty(target, name, value);
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
/*     */   private void readSettings(Scriptable source) {
/*     */     // Byte code:
/*     */     //   0: iconst_1
/*     */     //   1: istore_2
/*     */     //   2: iload_2
/*     */     //   3: iconst_5
/*     */     //   4: if_icmpgt -> 119
/*     */     //   7: aload_0
/*     */     //   8: invokespecial getMaxInstanceId : ()I
/*     */     //   11: iload_2
/*     */     //   12: iadd
/*     */     //   13: istore_3
/*     */     //   14: aload_0
/*     */     //   15: iload_3
/*     */     //   16: invokevirtual getInstanceIdName : (I)Ljava/lang/String;
/*     */     //   19: astore #4
/*     */     //   21: aload_1
/*     */     //   22: aload #4
/*     */     //   24: invokestatic getProperty : (Lorg/mozilla/javascript/Scriptable;Ljava/lang/String;)Ljava/lang/Object;
/*     */     //   27: astore #5
/*     */     //   29: aload #5
/*     */     //   31: getstatic org/mozilla/javascript/Scriptable.NOT_FOUND : Ljava/lang/Object;
/*     */     //   34: if_acmpne -> 40
/*     */     //   37: goto -> 113
/*     */     //   40: iload_2
/*     */     //   41: tableswitch default -> 98, 1 -> 76, 2 -> 76, 3 -> 76, 4 -> 87, 5 -> 76
/*     */     //   76: aload #5
/*     */     //   78: instanceof java/lang/Boolean
/*     */     //   81: ifne -> 106
/*     */     //   84: goto -> 113
/*     */     //   87: aload #5
/*     */     //   89: instanceof java/lang/Number
/*     */     //   92: ifne -> 106
/*     */     //   95: goto -> 113
/*     */     //   98: new java/lang/IllegalStateException
/*     */     //   101: dup
/*     */     //   102: invokespecial <init> : ()V
/*     */     //   105: athrow
/*     */     //   106: aload_0
/*     */     //   107: iload_3
/*     */     //   108: aload #5
/*     */     //   110: invokevirtual setInstanceIdValue : (ILjava/lang/Object;)V
/*     */     //   113: iinc #2, 1
/*     */     //   116: goto -> 2
/*     */     //   119: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #40	-> 0
/*     */     //   #41	-> 7
/*     */     //   #42	-> 14
/*     */     //   #43	-> 21
/*     */     //   #44	-> 29
/*     */     //   #45	-> 37
/*     */     //   #47	-> 40
/*     */     //   #52	-> 76
/*     */     //   #53	-> 84
/*     */     //   #57	-> 87
/*     */     //   #58	-> 95
/*     */     //   #62	-> 98
/*     */     //   #64	-> 106
/*     */     //   #40	-> 113
/*     */     //   #66	-> 119
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   14	99	3	id	I
/*     */     //   21	92	4	name	Ljava/lang/String;
/*     */     //   29	84	5	value	Ljava/lang/Object;
/*     */     //   2	117	2	i	I
/*     */     //   0	120	0	this	Lorg/mozilla/javascript/xmlimpl/XMLCtor;
/*     */     //   0	120	1	source	Lorg/mozilla/javascript/Scriptable;
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
/*     */   protected int getMaxInstanceId() {
/*  82 */     return super.getMaxInstanceId() + 5;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int findInstanceIdInfo(String s) {
/*  89 */     int attr, c, id = 0; String X = null;
/*  90 */     switch (s.length()) { case 12:
/*  91 */         X = "prettyIndent"; id = 4; break;
/*  92 */       case 14: c = s.charAt(0);
/*  93 */         if (c == 105) { X = "ignoreComments"; id = 1; break; }
/*  94 */          if (c == 112) { X = "prettyPrinting"; id = 5; }  break;
/*     */       case 16:
/*  96 */         X = "ignoreWhitespace"; id = 3; break;
/*  97 */       case 28: X = "ignoreProcessingInstructions"; id = 2; break; }
/*     */     
/*  99 */     if (X != null && X != s && !X.equals(s)) id = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 104 */     if (id == 0) return super.findInstanceIdInfo(s);
/*     */ 
/*     */     
/* 107 */     switch (id) {
/*     */       case 1:
/*     */       case 2:
/*     */       case 3:
/*     */       case 4:
/*     */       case 5:
/* 113 */         attr = 6;
/*     */ 
/*     */ 
/*     */         
/* 117 */         return instanceIdInfo(attr, super.getMaxInstanceId() + id);
/*     */     } 
/*     */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getInstanceIdName(int id) {
/* 125 */     switch (id - super.getMaxInstanceId()) { case 1:
/* 126 */         return "ignoreComments";
/* 127 */       case 2: return "ignoreProcessingInstructions";
/* 128 */       case 3: return "ignoreWhitespace";
/* 129 */       case 4: return "prettyIndent";
/* 130 */       case 5: return "prettyPrinting"; }
/*     */     
/* 132 */     return super.getInstanceIdName(id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object getInstanceIdValue(int id) {
/* 138 */     switch (id - super.getMaxInstanceId()) {
/*     */       case 1:
/* 140 */         return ScriptRuntime.wrapBoolean(this.options.isIgnoreComments());
/*     */       case 2:
/* 142 */         return ScriptRuntime.wrapBoolean(this.options.isIgnoreProcessingInstructions());
/*     */       case 3:
/* 144 */         return ScriptRuntime.wrapBoolean(this.options.isIgnoreWhitespace());
/*     */       case 4:
/* 146 */         return ScriptRuntime.wrapInt(this.options.getPrettyIndent());
/*     */       case 5:
/* 148 */         return ScriptRuntime.wrapBoolean(this.options.isPrettyPrinting());
/*     */     } 
/* 150 */     return super.getInstanceIdValue(id);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setInstanceIdValue(int id, Object value) {
/* 155 */     switch (id - super.getMaxInstanceId()) {
/*     */       case 1:
/* 157 */         this.options.setIgnoreComments(ScriptRuntime.toBoolean(value));
/*     */         return;
/*     */       case 2:
/* 160 */         this.options.setIgnoreProcessingInstructions(ScriptRuntime.toBoolean(value));
/*     */         return;
/*     */       case 3:
/* 163 */         this.options.setIgnoreWhitespace(ScriptRuntime.toBoolean(value));
/*     */         return;
/*     */       case 4:
/* 166 */         this.options.setPrettyIndent(ScriptRuntime.toInt32(value));
/*     */         return;
/*     */       case 5:
/* 169 */         this.options.setPrettyPrinting(ScriptRuntime.toBoolean(value));
/*     */         return;
/*     */     } 
/* 172 */     super.setInstanceIdValue(id, value);
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
/*     */   protected int findPrototypeId(String s) {
/* 187 */     int id = 0; String X = null;
/* 188 */     int s_length = s.length();
/* 189 */     if (s_length == 8) { X = "settings"; id = 2; }
/* 190 */     else if (s_length == 11) { X = "setSettings"; id = 3; }
/* 191 */     else if (s_length == 15) { X = "defaultSettings"; id = 1; }
/* 192 */      if (X != null && X != s && !X.equals(s)) id = 0;
/*     */ 
/*     */ 
/*     */     
/* 196 */     return id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initPrototypeId(int id) {
/*     */     String s;
/*     */     int arity;
/* 205 */     switch (id) { case 1:
/* 206 */         arity = 0; s = "defaultSettings"; break;
/* 207 */       case 2: arity = 0; s = "settings"; break;
/* 208 */       case 3: arity = 1; s = "setSettings"; break;
/* 209 */       default: throw new IllegalArgumentException(String.valueOf(id)); }
/*     */     
/* 211 */     initPrototypeMethod(XMLCTOR_TAG, id, s, arity);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/*     */     Scriptable obj;
/* 218 */     if (!f.hasTag(XMLCTOR_TAG)) {
/* 219 */       return super.execIdCall(f, cx, scope, thisObj, args);
/*     */     }
/* 221 */     int id = f.methodId();
/* 222 */     switch (id) {
/*     */       case 1:
/* 224 */         this.options.setDefault();
/* 225 */         obj = cx.newObject(scope);
/* 226 */         writeSetting(obj);
/* 227 */         return obj;
/*     */       
/*     */       case 2:
/* 230 */         obj = cx.newObject(scope);
/* 231 */         writeSetting(obj);
/* 232 */         return obj;
/*     */       
/*     */       case 3:
/* 235 */         if (args.length == 0 || args[0] == null || args[0] == Undefined.instance) {
/*     */ 
/*     */ 
/*     */           
/* 239 */           this.options.setDefault();
/* 240 */         } else if (args[0] instanceof Scriptable) {
/* 241 */           readSettings((Scriptable)args[0]);
/*     */         } 
/* 243 */         return Undefined.instance;
/*     */     } 
/*     */     
/* 246 */     throw new IllegalArgumentException(String.valueOf(id));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasInstance(Scriptable instance) {
/* 254 */     return (instance instanceof XML || instance instanceof XMLList);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\xmlimpl\XMLCtor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */