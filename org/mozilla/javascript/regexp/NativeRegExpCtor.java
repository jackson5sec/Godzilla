/*     */ package org.mozilla.javascript.regexp;
/*     */ 
/*     */ import org.mozilla.javascript.BaseFunction;
/*     */ import org.mozilla.javascript.Context;
/*     */ import org.mozilla.javascript.ScriptRuntime;
/*     */ import org.mozilla.javascript.Scriptable;
/*     */ import org.mozilla.javascript.ScriptableObject;
/*     */ import org.mozilla.javascript.TopLevel;
/*     */ import org.mozilla.javascript.Undefined;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class NativeRegExpCtor
/*     */   extends BaseFunction
/*     */ {
/*     */   static final long serialVersionUID = -5733330028285400526L;
/*     */   private static final int Id_multiline = 1;
/*     */   private static final int Id_STAR = 2;
/*     */   private static final int Id_input = 3;
/*     */   private static final int Id_UNDERSCORE = 4;
/*     */   private static final int Id_lastMatch = 5;
/*     */   private static final int Id_AMPERSAND = 6;
/*     */   private static final int Id_lastParen = 7;
/*     */   private static final int Id_PLUS = 8;
/*     */   private static final int Id_leftContext = 9;
/*     */   private static final int Id_BACK_QUOTE = 10;
/*     */   private static final int Id_rightContext = 11;
/*     */   
/*     */   public String getFunctionName() {
/*  35 */     return "RegExp";
/*     */   }
/*     */   private static final int Id_QUOTE = 12; private static final int DOLLAR_ID_BASE = 12; private static final int Id_DOLLAR_1 = 13; private static final int Id_DOLLAR_2 = 14; private static final int Id_DOLLAR_3 = 15; private static final int Id_DOLLAR_4 = 16; private static final int Id_DOLLAR_5 = 17; private static final int Id_DOLLAR_6 = 18; private static final int Id_DOLLAR_7 = 19; private static final int Id_DOLLAR_8 = 20; private static final int Id_DOLLAR_9 = 21; private static final int MAX_INSTANCE_ID = 21;
/*     */   
/*     */   public int getLength() {
/*  40 */     return 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getArity() {
/*  45 */     return 2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/*  52 */     if (args.length > 0 && args[0] instanceof NativeRegExp && (args.length == 1 || args[1] == Undefined.instance))
/*     */     {
/*     */       
/*  55 */       return args[0];
/*     */     }
/*  57 */     return construct(cx, scope, args);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Scriptable construct(Context cx, Scriptable scope, Object[] args) {
/*  63 */     NativeRegExp re = new NativeRegExp();
/*  64 */     re.compile(cx, scope, args);
/*  65 */     ScriptRuntime.setBuiltinProtoAndParent((ScriptableObject)re, scope, TopLevel.Builtins.RegExp);
/*  66 */     return (Scriptable)re;
/*     */   }
/*     */ 
/*     */   
/*     */   private static RegExpImpl getImpl() {
/*  71 */     Context cx = Context.getCurrentContext();
/*  72 */     return (RegExpImpl)ScriptRuntime.getRegExpProxy(cx);
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
/* 114 */     return super.getMaxInstanceId() + 21;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int findInstanceIdInfo(String s) {
/* 121 */     int c, id = 0; String X = null;
/* 122 */     switch (s.length()) { case 2:
/* 123 */         switch (s.charAt(1)) { case '&':
/* 124 */             if (s.charAt(0) == '$') id = 6;  break;
/* 125 */           case '\'': if (s.charAt(0) == '$') id = 12;  break;
/* 126 */           case '*': if (s.charAt(0) == '$') id = 2;  break;
/* 127 */           case '+': if (s.charAt(0) == '$') id = 8;  break;
/* 128 */           case '1': if (s.charAt(0) == '$') id = 13;  break;
/* 129 */           case '2': if (s.charAt(0) == '$') id = 14;  break;
/* 130 */           case '3': if (s.charAt(0) == '$') id = 15;  break;
/* 131 */           case '4': if (s.charAt(0) == '$') id = 16;  break;
/* 132 */           case '5': if (s.charAt(0) == '$') id = 17;  break;
/* 133 */           case '6': if (s.charAt(0) == '$') id = 18;  break;
/* 134 */           case '7': if (s.charAt(0) == '$') id = 19;  break;
/* 135 */           case '8': if (s.charAt(0) == '$') id = 20;  break;
/* 136 */           case '9': if (s.charAt(0) == '$') id = 21;  break;
/* 137 */           case '_': if (s.charAt(0) == '$') id = 4;  break;
/* 138 */           case '`': if (s.charAt(0) == '$') id = 10;  break; } 
/*     */       case 5:
/* 140 */         X = "input"; id = 3;
/* 141 */       case 9: c = s.charAt(4);
/* 142 */         if (c == 77) { X = "lastMatch"; id = 5; }
/* 143 */         else if (c == 80) { X = "lastParen"; id = 7; }
/* 144 */         else if (c == 105) { X = "multiline"; id = 1; } 
/*     */       case 11:
/* 146 */         X = "leftContext"; id = 9;
/* 147 */       case 12: X = "rightContext"; id = 11;
/*     */       default:
/* 149 */         if (X != null && X != s && !X.equals(s)) id = 0;
/*     */         
/*     */         break; }
/*     */     
/* 153 */     if (id == 0) return super.findInstanceIdInfo(s);
/*     */ 
/*     */     
/* 156 */     switch (id)
/*     */     { case 1:
/* 158 */         attr = this.multilineAttr;
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
/* 174 */         return instanceIdInfo(attr, super.getMaxInstanceId() + id);case 2: attr = this.starAttr; return instanceIdInfo(attr, super.getMaxInstanceId() + id);case 3: attr = this.inputAttr; return instanceIdInfo(attr, super.getMaxInstanceId() + id);case 4: attr = this.underscoreAttr; return instanceIdInfo(attr, super.getMaxInstanceId() + id); }  int attr = 5; return instanceIdInfo(attr, super.getMaxInstanceId() + id);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getInstanceIdName(int id) {
/* 182 */     int shifted = id - super.getMaxInstanceId();
/* 183 */     if (1 <= shifted && shifted <= 21) {
/* 184 */       switch (shifted) { case 1:
/* 185 */           return "multiline";
/* 186 */         case 2: return "$*";
/*     */         case 3:
/* 188 */           return "input";
/* 189 */         case 4: return "$_";
/*     */         case 5:
/* 191 */           return "lastMatch";
/* 192 */         case 6: return "$&";
/*     */         case 7:
/* 194 */           return "lastParen";
/* 195 */         case 8: return "$+";
/*     */         case 9:
/* 197 */           return "leftContext";
/* 198 */         case 10: return "$`";
/*     */         case 11:
/* 200 */           return "rightContext";
/* 201 */         case 12: return "$'"; }
/*     */ 
/*     */       
/* 204 */       int substring_number = shifted - 12 - 1;
/* 205 */       char[] buf = { '$', (char)(49 + substring_number) };
/* 206 */       return new String(buf);
/*     */     } 
/* 208 */     return super.getInstanceIdName(id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object getInstanceIdValue(int id) {
/* 214 */     int shifted = id - super.getMaxInstanceId();
/* 215 */     if (1 <= shifted && shifted <= 21) {
/* 216 */       Object stringResult; int substring_number; RegExpImpl impl = getImpl();
/*     */       
/* 218 */       switch (shifted) {
/*     */         case 1:
/*     */         case 2:
/* 221 */           return ScriptRuntime.wrapBoolean(impl.multiline);
/*     */         
/*     */         case 3:
/*     */         case 4:
/* 225 */           stringResult = impl.input;
/*     */           break;
/*     */         
/*     */         case 5:
/*     */         case 6:
/* 230 */           stringResult = impl.lastMatch;
/*     */           break;
/*     */         
/*     */         case 7:
/*     */         case 8:
/* 235 */           stringResult = impl.lastParen;
/*     */           break;
/*     */         
/*     */         case 9:
/*     */         case 10:
/* 240 */           stringResult = impl.leftContext;
/*     */           break;
/*     */         
/*     */         case 11:
/*     */         case 12:
/* 245 */           stringResult = impl.rightContext;
/*     */           break;
/*     */ 
/*     */ 
/*     */         
/*     */         default:
/* 251 */           substring_number = shifted - 12 - 1;
/* 252 */           stringResult = impl.getParenSubString(substring_number);
/*     */           break;
/*     */       } 
/*     */       
/* 256 */       return (stringResult == null) ? "" : stringResult.toString();
/*     */     } 
/* 258 */     return super.getInstanceIdValue(id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setInstanceIdValue(int id, Object value) {
/* 264 */     int shifted = id - super.getMaxInstanceId();
/* 265 */     switch (shifted) {
/*     */       case 1:
/*     */       case 2:
/* 268 */         (getImpl()).multiline = ScriptRuntime.toBoolean(value);
/*     */         return;
/*     */       
/*     */       case 3:
/*     */       case 4:
/* 273 */         (getImpl()).input = ScriptRuntime.toString(value);
/*     */         return;
/*     */       
/*     */       case 5:
/*     */       case 6:
/*     */       case 7:
/*     */       case 8:
/*     */       case 9:
/*     */       case 10:
/*     */       case 11:
/*     */       case 12:
/*     */         return;
/*     */     } 
/* 286 */     int substring_number = shifted - 12 - 1;
/* 287 */     if (0 <= substring_number && substring_number <= 8) {
/*     */       return;
/*     */     }
/*     */     
/* 291 */     super.setInstanceIdValue(id, value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setInstanceIdAttributes(int id, int attr) {
/* 296 */     int shifted = id - super.getMaxInstanceId();
/* 297 */     switch (shifted) {
/*     */       case 1:
/* 299 */         this.multilineAttr = attr;
/*     */         return;
/*     */       case 2:
/* 302 */         this.starAttr = attr;
/*     */         return;
/*     */       case 3:
/* 305 */         this.inputAttr = attr;
/*     */         return;
/*     */       case 4:
/* 308 */         this.underscoreAttr = attr;
/*     */         return;
/*     */       
/*     */       case 5:
/*     */       case 6:
/*     */       case 7:
/*     */       case 8:
/*     */       case 9:
/*     */       case 10:
/*     */       case 11:
/*     */       case 12:
/*     */         return;
/*     */     } 
/*     */     
/* 322 */     int substring_number = shifted - 12 - 1;
/* 323 */     if (0 <= substring_number && substring_number <= 8) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 328 */     super.setInstanceIdAttributes(id, attr);
/*     */   }
/*     */   
/* 331 */   private int multilineAttr = 4;
/* 332 */   private int starAttr = 4;
/* 333 */   private int inputAttr = 4;
/* 334 */   private int underscoreAttr = 4;
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\regexp\NativeRegExpCtor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */