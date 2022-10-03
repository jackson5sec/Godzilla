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
/*     */ final class QName
/*     */   extends IdScriptableObject
/*     */ {
/*     */   static final long serialVersionUID = 416745167693026750L;
/*  19 */   private static final Object QNAME_TAG = "QName"; private XMLLibImpl lib;
/*     */   private QName prototype;
/*     */   private XmlNode.QName delegate;
/*     */   private static final int Id_localName = 1;
/*     */   private static final int Id_uri = 2;
/*     */   private static final int MAX_INSTANCE_ID = 2;
/*     */   private static final int Id_constructor = 1;
/*     */   private static final int Id_toString = 2;
/*     */   private static final int Id_toSource = 3;
/*     */   private static final int MAX_PROTOTYPE_ID = 3;
/*     */   
/*     */   static QName create(XMLLibImpl lib, Scriptable scope, QName prototype, XmlNode.QName delegate) {
/*  31 */     QName rv = new QName();
/*  32 */     rv.lib = lib;
/*  33 */     rv.setParentScope(scope);
/*  34 */     rv.prototype = prototype;
/*  35 */     rv.setPrototype((Scriptable)prototype);
/*  36 */     rv.delegate = delegate;
/*  37 */     return rv;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void exportAsJSClass(boolean sealed) {
/*  46 */     exportAsJSClass(3, getParentScope(), sealed);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  52 */     if (this.delegate.getNamespace() == null)
/*  53 */       return "*::" + localName(); 
/*  54 */     if (this.delegate.getNamespace().isGlobal())
/*     */     {
/*  56 */       return localName();
/*     */     }
/*  58 */     return uri() + "::" + localName();
/*     */   }
/*     */ 
/*     */   
/*     */   public String localName() {
/*  63 */     if (this.delegate.getLocalName() == null) return "*"; 
/*  64 */     return this.delegate.getLocalName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String prefix() {
/*  72 */     if (this.delegate.getNamespace() == null) return null; 
/*  73 */     return this.delegate.getNamespace().getPrefix();
/*     */   }
/*     */   
/*     */   String uri() {
/*  77 */     if (this.delegate.getNamespace() == null) return null; 
/*  78 */     return this.delegate.getNamespace().getUri();
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   final XmlNode.QName toNodeQname() {
/*  84 */     return this.delegate;
/*     */   }
/*     */   
/*     */   final XmlNode.QName getDelegate() {
/*  88 */     return this.delegate;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  93 */     if (!(obj instanceof QName)) return false; 
/*  94 */     return equals((QName)obj);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  99 */     return this.delegate.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object equivalentValues(Object value) {
/* 105 */     if (!(value instanceof QName)) return Scriptable.NOT_FOUND; 
/* 106 */     boolean result = equals((QName)value);
/* 107 */     return result ? Boolean.TRUE : Boolean.FALSE;
/*     */   }
/*     */   
/*     */   private boolean equals(QName q) {
/* 111 */     return this.delegate.equals(q.delegate);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getClassName() {
/* 116 */     return "QName";
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getDefaultValue(Class<?> hint) {
/* 121 */     return toString();
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
/* 133 */     return super.getMaxInstanceId() + 2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int findInstanceIdInfo(String s) {
/* 141 */     int attr, id = 0; String X = null;
/* 142 */     int s_length = s.length();
/* 143 */     if (s_length == 3) { X = "uri"; id = 2; }
/* 144 */     else if (s_length == 9) { X = "localName"; id = 1; }
/* 145 */      if (X != null && X != s && !X.equals(s)) id = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 150 */     if (id == 0) return super.findInstanceIdInfo(s);
/*     */ 
/*     */     
/* 153 */     switch (id) {
/*     */       case 1:
/*     */       case 2:
/* 156 */         attr = 5;
/*     */ 
/*     */ 
/*     */         
/* 160 */         return instanceIdInfo(attr, super.getMaxInstanceId() + id);
/*     */     } 
/*     */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getInstanceIdName(int id) {
/* 167 */     switch (id - super.getMaxInstanceId()) { case 1:
/* 168 */         return "localName";
/* 169 */       case 2: return "uri"; }
/*     */     
/* 171 */     return super.getInstanceIdName(id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object getInstanceIdValue(int id) {
/* 177 */     switch (id - super.getMaxInstanceId()) { case 1:
/* 178 */         return localName();
/* 179 */       case 2: return uri(); }
/*     */     
/* 181 */     return super.getInstanceIdValue(id);
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
/* 196 */     int id = 0; String X = null;
/* 197 */     int s_length = s.length();
/* 198 */     if (s_length == 8)
/* 199 */     { int c = s.charAt(3);
/* 200 */       if (c == 111) { X = "toSource"; id = 3; }
/* 201 */       else if (c == 116) { X = "toString"; id = 2; }
/*     */        }
/* 203 */     else if (s_length == 11) { X = "constructor"; id = 1; }
/* 204 */      if (X != null && X != s && !X.equals(s)) id = 0;
/*     */ 
/*     */ 
/*     */     
/* 208 */     return id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initPrototypeId(int id) {
/*     */     String s;
/*     */     int arity;
/* 217 */     switch (id) { case 1:
/* 218 */         arity = 2; s = "constructor"; break;
/* 219 */       case 2: arity = 0; s = "toString"; break;
/* 220 */       case 3: arity = 0; s = "toSource"; break;
/* 221 */       default: throw new IllegalArgumentException(String.valueOf(id)); }
/*     */     
/* 223 */     initPrototypeMethod(QNAME_TAG, id, s, arity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/* 233 */     if (!f.hasTag(QNAME_TAG)) {
/* 234 */       return super.execIdCall(f, cx, scope, thisObj, args);
/*     */     }
/* 236 */     int id = f.methodId();
/* 237 */     switch (id) {
/*     */       case 1:
/* 239 */         return jsConstructor(cx, (thisObj == null), args);
/*     */       case 2:
/* 241 */         return realThis(thisObj, f).toString();
/*     */       case 3:
/* 243 */         return realThis(thisObj, f).js_toSource();
/*     */     } 
/* 245 */     throw new IllegalArgumentException(String.valueOf(id));
/*     */   }
/*     */ 
/*     */   
/*     */   private QName realThis(Scriptable thisObj, IdFunctionObject f) {
/* 250 */     if (!(thisObj instanceof QName))
/* 251 */       throw incompatibleCallError(f); 
/* 252 */     return (QName)thisObj;
/*     */   }
/*     */   
/*     */   QName newQName(XMLLibImpl lib, String q_uri, String q_localName, String q_prefix) {
/* 256 */     QName prototype = this.prototype;
/* 257 */     if (prototype == null) {
/* 258 */       prototype = this;
/*     */     }
/* 260 */     XmlNode.Namespace ns = null;
/* 261 */     if (q_prefix != null) {
/* 262 */       ns = XmlNode.Namespace.create(q_prefix, q_uri);
/* 263 */     } else if (q_uri != null) {
/* 264 */       ns = XmlNode.Namespace.create(q_uri);
/*     */     } else {
/* 266 */       ns = null;
/*     */     } 
/* 268 */     if (q_localName != null && q_localName.equals("*")) q_localName = null; 
/* 269 */     return create(lib, getParentScope(), prototype, XmlNode.QName.create(ns, q_localName));
/*     */   }
/*     */ 
/*     */   
/*     */   QName constructQName(XMLLibImpl lib, Context cx, Object namespace, Object name) {
/* 274 */     String q_uri, q_prefix, nameString = null;
/* 275 */     if (name instanceof QName) {
/* 276 */       if (namespace == Undefined.instance) {
/* 277 */         return (QName)name;
/*     */       }
/* 279 */       nameString = ((QName)name).localName();
/*     */     } 
/*     */     
/* 282 */     if (name == Undefined.instance) {
/* 283 */       nameString = "";
/*     */     } else {
/* 285 */       nameString = ScriptRuntime.toString(name);
/*     */     } 
/*     */     
/* 288 */     if (namespace == Undefined.instance) {
/* 289 */       if ("*".equals(nameString)) {
/* 290 */         namespace = null;
/*     */       } else {
/* 292 */         namespace = lib.getDefaultNamespace(cx);
/*     */       } 
/*     */     }
/* 295 */     Namespace namespaceNamespace = null;
/* 296 */     if (namespace != null)
/*     */     {
/* 298 */       if (namespace instanceof Namespace) {
/* 299 */         namespaceNamespace = (Namespace)namespace;
/*     */       } else {
/* 301 */         namespaceNamespace = lib.newNamespace(ScriptRuntime.toString(namespace));
/*     */       }  } 
/* 303 */     String q_localName = nameString;
/*     */ 
/*     */     
/* 306 */     if (namespace == null) {
/* 307 */       q_uri = null;
/* 308 */       q_prefix = null;
/*     */     } else {
/* 310 */       q_uri = namespaceNamespace.uri();
/* 311 */       q_prefix = namespaceNamespace.prefix();
/*     */     } 
/* 313 */     return newQName(lib, q_uri, q_localName, q_prefix);
/*     */   }
/*     */   
/*     */   QName constructQName(XMLLibImpl lib, Context cx, Object nameValue) {
/* 317 */     return constructQName(lib, cx, Undefined.instance, nameValue);
/*     */   }
/*     */   
/*     */   QName castToQName(XMLLibImpl lib, Context cx, Object qnameValue) {
/* 321 */     if (qnameValue instanceof QName) {
/* 322 */       return (QName)qnameValue;
/*     */     }
/* 324 */     return constructQName(lib, cx, qnameValue);
/*     */   }
/*     */ 
/*     */   
/*     */   private Object jsConstructor(Context cx, boolean inNewExpr, Object[] args) {
/* 329 */     if (!inNewExpr && args.length == 1) {
/* 330 */       return castToQName(this.lib, cx, args[0]);
/*     */     }
/* 332 */     if (args.length == 0)
/* 333 */       return constructQName(this.lib, cx, Undefined.instance); 
/* 334 */     if (args.length == 1) {
/* 335 */       return constructQName(this.lib, cx, args[0]);
/*     */     }
/* 337 */     return constructQName(this.lib, cx, args[0], args[1]);
/*     */   }
/*     */ 
/*     */   
/*     */   private String js_toSource() {
/* 342 */     StringBuilder sb = new StringBuilder();
/* 343 */     sb.append('(');
/* 344 */     toSourceImpl(uri(), localName(), prefix(), sb);
/* 345 */     sb.append(')');
/* 346 */     return sb.toString();
/*     */   }
/*     */   
/*     */   private static void toSourceImpl(String uri, String localName, String prefix, StringBuilder sb) {
/* 350 */     sb.append("new QName(");
/* 351 */     if (uri == null && prefix == null) {
/* 352 */       if (!"*".equals(localName)) {
/* 353 */         sb.append("null, ");
/*     */       }
/*     */     } else {
/* 356 */       Namespace.toSourceImpl(prefix, uri, sb);
/* 357 */       sb.append(", ");
/*     */     } 
/* 359 */     sb.append('\'');
/* 360 */     sb.append(ScriptRuntime.escapeString(localName, '\''));
/* 361 */     sb.append("')");
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\xmlimpl\QName.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */