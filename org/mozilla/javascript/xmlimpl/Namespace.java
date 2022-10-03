/*     */ package org.mozilla.javascript.xmlimpl;
/*     */ 
/*     */ import org.mozilla.javascript.Context;
/*     */ import org.mozilla.javascript.IdFunctionObject;
/*     */ import org.mozilla.javascript.IdScriptableObject;
/*     */ import org.mozilla.javascript.ScriptRuntime;
/*     */ import org.mozilla.javascript.Scriptable;
/*     */ import org.mozilla.javascript.Undefined;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Namespace
/*     */   extends IdScriptableObject
/*     */ {
/*     */   static final long serialVersionUID = -5765755238131301744L;
/*  19 */   private static final Object NAMESPACE_TAG = "Namespace";
/*     */   
/*     */   private Namespace prototype;
/*     */   
/*     */   private XmlNode.Namespace ns;
/*     */   private static final int Id_prefix = 1;
/*     */   private static final int Id_uri = 2;
/*     */   
/*     */   static Namespace create(Scriptable scope, Namespace prototype, XmlNode.Namespace namespace) {
/*  28 */     Namespace rv = new Namespace();
/*  29 */     rv.setParentScope(scope);
/*  30 */     rv.prototype = prototype;
/*  31 */     rv.setPrototype((Scriptable)prototype);
/*  32 */     rv.ns = namespace;
/*  33 */     return rv;
/*     */   }
/*     */   private static final int MAX_INSTANCE_ID = 2; private static final int Id_constructor = 1; private static final int Id_toString = 2; private static final int Id_toSource = 3; private static final int MAX_PROTOTYPE_ID = 3;
/*     */   final XmlNode.Namespace getDelegate() {
/*  37 */     return this.ns;
/*     */   }
/*     */   
/*     */   public void exportAsJSClass(boolean sealed) {
/*  41 */     exportAsJSClass(3, getParentScope(), sealed);
/*     */   }
/*     */   
/*     */   public String uri() {
/*  45 */     return this.ns.getUri();
/*     */   }
/*     */   
/*     */   public String prefix() {
/*  49 */     return this.ns.getPrefix();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  54 */     return uri();
/*     */   }
/*     */   
/*     */   public String toLocaleString() {
/*  58 */     return toString();
/*     */   }
/*     */   
/*     */   private boolean equals(Namespace n) {
/*  62 */     return uri().equals(n.uri());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  67 */     if (!(obj instanceof Namespace)) return false; 
/*  68 */     return equals((Namespace)obj);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  73 */     return uri().hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object equivalentValues(Object value) {
/*  78 */     if (!(value instanceof Namespace)) return Scriptable.NOT_FOUND; 
/*  79 */     boolean result = equals((Namespace)value);
/*  80 */     return result ? Boolean.TRUE : Boolean.FALSE;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  85 */     return "Namespace";
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getDefaultValue(Class<?> hint) {
/*  90 */     return uri();
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
/*     */   protected int getMaxInstanceId() {
/* 102 */     return super.getMaxInstanceId() + 2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int findInstanceIdInfo(String s) {
/* 110 */     int attr, id = 0; String X = null;
/* 111 */     int s_length = s.length();
/* 112 */     if (s_length == 3) { X = "uri"; id = 2; }
/* 113 */     else if (s_length == 6) { X = "prefix"; id = 1; }
/* 114 */      if (X != null && X != s && !X.equals(s)) id = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 119 */     if (id == 0) return super.findInstanceIdInfo(s);
/*     */ 
/*     */     
/* 122 */     switch (id) {
/*     */       case 1:
/*     */       case 2:
/* 125 */         attr = 5;
/*     */ 
/*     */ 
/*     */         
/* 129 */         return instanceIdInfo(attr, super.getMaxInstanceId() + id);
/*     */     } 
/*     */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getInstanceIdName(int id) {
/* 136 */     switch (id - super.getMaxInstanceId()) { case 1:
/* 137 */         return "prefix";
/* 138 */       case 2: return "uri"; }
/*     */     
/* 140 */     return super.getInstanceIdName(id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object getInstanceIdValue(int id) {
/* 146 */     switch (id - super.getMaxInstanceId()) {
/*     */       case 1:
/* 148 */         if (this.ns.getPrefix() == null) return Undefined.instance; 
/* 149 */         return this.ns.getPrefix();
/*     */       case 2:
/* 151 */         return this.ns.getUri();
/*     */     } 
/* 153 */     return super.getInstanceIdValue(id);
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
/*     */   protected int findPrototypeId(String s) {
/* 169 */     int id = 0; String X = null;
/* 170 */     int s_length = s.length();
/* 171 */     if (s_length == 8)
/* 172 */     { int c = s.charAt(3);
/* 173 */       if (c == 111) { X = "toSource"; id = 3; }
/* 174 */       else if (c == 116) { X = "toString"; id = 2; }
/*     */        }
/* 176 */     else if (s_length == 11) { X = "constructor"; id = 1; }
/* 177 */      if (X != null && X != s && !X.equals(s)) id = 0;
/*     */ 
/*     */ 
/*     */     
/* 181 */     return id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initPrototypeId(int id) {
/*     */     String s;
/*     */     int arity;
/* 190 */     switch (id) { case 1:
/* 191 */         arity = 2; s = "constructor"; break;
/* 192 */       case 2: arity = 0; s = "toString"; break;
/* 193 */       case 3: arity = 0; s = "toSource"; break;
/* 194 */       default: throw new IllegalArgumentException(String.valueOf(id)); }
/*     */     
/* 196 */     initPrototypeMethod(NAMESPACE_TAG, id, s, arity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/* 206 */     if (!f.hasTag(NAMESPACE_TAG)) {
/* 207 */       return super.execIdCall(f, cx, scope, thisObj, args);
/*     */     }
/* 209 */     int id = f.methodId();
/* 210 */     switch (id) {
/*     */       case 1:
/* 212 */         return jsConstructor(cx, (thisObj == null), args);
/*     */       case 2:
/* 214 */         return realThis(thisObj, f).toString();
/*     */       case 3:
/* 216 */         return realThis(thisObj, f).js_toSource();
/*     */     } 
/* 218 */     throw new IllegalArgumentException(String.valueOf(id));
/*     */   }
/*     */   
/*     */   private Namespace realThis(Scriptable thisObj, IdFunctionObject f) {
/* 222 */     if (!(thisObj instanceof Namespace))
/* 223 */       throw incompatibleCallError(f); 
/* 224 */     return (Namespace)thisObj;
/*     */   }
/*     */   
/*     */   Namespace newNamespace(String uri) {
/* 228 */     Namespace prototype = (this.prototype == null) ? this : this.prototype;
/* 229 */     return create(getParentScope(), prototype, XmlNode.Namespace.create(uri));
/*     */   }
/*     */   
/*     */   Namespace newNamespace(String prefix, String uri) {
/* 233 */     if (prefix == null) return newNamespace(uri); 
/* 234 */     Namespace prototype = (this.prototype == null) ? this : this.prototype;
/* 235 */     return create(getParentScope(), prototype, XmlNode.Namespace.create(prefix, uri));
/*     */   }
/*     */ 
/*     */   
/*     */   Namespace constructNamespace(Object uriValue) {
/*     */     String prefix;
/*     */     String uri;
/* 242 */     if (uriValue instanceof Namespace) {
/* 243 */       Namespace ns = (Namespace)uriValue;
/* 244 */       prefix = ns.prefix();
/* 245 */       uri = ns.uri();
/* 246 */     } else if (uriValue instanceof QName) {
/* 247 */       QName qname = (QName)uriValue;
/* 248 */       uri = qname.uri();
/* 249 */       if (uri != null) {
/*     */         
/* 251 */         prefix = qname.prefix();
/*     */       } else {
/* 253 */         uri = qname.toString();
/* 254 */         prefix = null;
/*     */       } 
/*     */     } else {
/* 257 */       uri = ScriptRuntime.toString(uriValue);
/* 258 */       prefix = (uri.length() == 0) ? "" : null;
/*     */     } 
/*     */     
/* 261 */     return newNamespace(prefix, uri);
/*     */   }
/*     */   
/*     */   Namespace castToNamespace(Object namespaceObj) {
/* 265 */     if (namespaceObj instanceof Namespace) {
/* 266 */       return (Namespace)namespaceObj;
/*     */     }
/* 268 */     return constructNamespace(namespaceObj);
/*     */   }
/*     */ 
/*     */   
/*     */   private Namespace constructNamespace(Object prefixValue, Object uriValue) {
/*     */     String prefix;
/*     */     String uri;
/* 275 */     if (uriValue instanceof QName) {
/* 276 */       QName qname = (QName)uriValue;
/* 277 */       uri = qname.uri();
/* 278 */       if (uri == null) {
/* 279 */         uri = qname.toString();
/*     */       }
/*     */     } else {
/* 282 */       uri = ScriptRuntime.toString(uriValue);
/*     */     } 
/*     */     
/* 285 */     if (uri.length() == 0) {
/* 286 */       if (prefixValue == Undefined.instance) {
/* 287 */         prefix = "";
/*     */       } else {
/* 289 */         prefix = ScriptRuntime.toString(prefixValue);
/* 290 */         if (prefix.length() != 0) {
/* 291 */           throw ScriptRuntime.typeError("Illegal prefix '" + prefix + "' for 'no namespace'.");
/*     */         }
/*     */       }
/*     */     
/* 295 */     } else if (prefixValue == Undefined.instance) {
/* 296 */       prefix = "";
/* 297 */     } else if (!XMLName.accept(prefixValue)) {
/* 298 */       prefix = "";
/*     */     } else {
/* 300 */       prefix = ScriptRuntime.toString(prefixValue);
/*     */     } 
/*     */     
/* 303 */     return newNamespace(prefix, uri);
/*     */   }
/*     */   
/*     */   private Namespace constructNamespace() {
/* 307 */     return newNamespace("", "");
/*     */   }
/*     */ 
/*     */   
/*     */   private Object jsConstructor(Context cx, boolean inNewExpr, Object[] args) {
/* 312 */     if (!inNewExpr && args.length == 1) {
/* 313 */       return castToNamespace(args[0]);
/*     */     }
/*     */     
/* 316 */     if (args.length == 0)
/* 317 */       return constructNamespace(); 
/* 318 */     if (args.length == 1) {
/* 319 */       return constructNamespace(args[0]);
/*     */     }
/* 321 */     return constructNamespace(args[0], args[1]);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private String js_toSource() {
/* 327 */     StringBuilder sb = new StringBuilder();
/* 328 */     sb.append('(');
/* 329 */     toSourceImpl(this.ns.getPrefix(), this.ns.getUri(), sb);
/* 330 */     sb.append(')');
/* 331 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   static void toSourceImpl(String prefix, String uri, StringBuilder sb) {
/* 336 */     sb.append("new Namespace(");
/* 337 */     if (uri.length() == 0) {
/* 338 */       if (!"".equals(prefix)) throw new IllegalArgumentException(prefix); 
/*     */     } else {
/* 340 */       sb.append('\'');
/* 341 */       if (prefix != null) {
/* 342 */         sb.append(ScriptRuntime.escapeString(prefix, '\''));
/* 343 */         sb.append("', '");
/*     */       } 
/* 345 */       sb.append(ScriptRuntime.escapeString(uri, '\''));
/* 346 */       sb.append('\'');
/*     */     } 
/* 348 */     sb.append(')');
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\xmlimpl\Namespace.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */