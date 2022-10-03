/*     */ package org.mozilla.javascript.tools.shell;
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.CodeSource;
/*     */ import java.security.Permission;
/*     */ import java.security.PermissionCollection;
/*     */ import java.security.Policy;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.security.cert.Certificate;
/*     */ import java.util.Enumeration;
/*     */ import org.mozilla.javascript.Callable;
/*     */ import org.mozilla.javascript.Context;
/*     */ import org.mozilla.javascript.GeneratedClassLoader;
/*     */ import org.mozilla.javascript.Scriptable;
/*     */ 
/*     */ public class JavaPolicySecurity extends SecurityProxy {
/*     */   public Class<?> getStaticSecurityDomainClassInternal() {
/*  22 */     return ProtectionDomain.class;
/*     */   }
/*     */   
/*     */   private static class Loader
/*     */     extends ClassLoader
/*     */     implements GeneratedClassLoader {
/*     */     private ProtectionDomain domain;
/*     */     
/*     */     Loader(ClassLoader parent, ProtectionDomain domain) {
/*  31 */       super((parent != null) ? parent : getSystemClassLoader());
/*  32 */       this.domain = domain;
/*     */     }
/*     */     
/*     */     public Class<?> defineClass(String name, byte[] data) {
/*  36 */       return defineClass(name, data, 0, data.length, this.domain);
/*     */     }
/*     */     
/*     */     public void linkClass(Class<?> cl) {
/*  40 */       resolveClass(cl);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ContextPermissions
/*     */     extends PermissionCollection
/*     */   {
/*     */     static final long serialVersionUID = -1721494496320750721L;
/*     */ 
/*     */     
/*  52 */     AccessControlContext _context = AccessController.getContext(); ContextPermissions(ProtectionDomain staticDomain) {
/*  53 */       if (staticDomain != null) {
/*  54 */         this._statisPermissions = staticDomain.getPermissions();
/*     */       }
/*  56 */       setReadOnly();
/*     */     }
/*     */     PermissionCollection _statisPermissions;
/*     */     
/*     */     public void add(Permission permission) {
/*  61 */       throw new RuntimeException("NOT IMPLEMENTED");
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean implies(Permission permission) {
/*  66 */       if (this._statisPermissions != null && 
/*  67 */         !this._statisPermissions.implies(permission)) {
/*  68 */         return false;
/*     */       }
/*     */       
/*     */       try {
/*  72 */         this._context.checkPermission(permission);
/*  73 */         return true;
/*  74 */       } catch (AccessControlException ex) {
/*  75 */         return false;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Enumeration<Permission> elements() {
/*  82 */       return new Enumeration<Permission>() {
/*  83 */           public boolean hasMoreElements() { return false; } public Permission nextElement() {
/*  84 */             return null;
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     public String toString() {
/*  90 */       StringBuilder sb = new StringBuilder();
/*  91 */       sb.append(getClass().getName());
/*  92 */       sb.append('@');
/*  93 */       sb.append(Integer.toHexString(System.identityHashCode(this)));
/*  94 */       sb.append(" (context=");
/*  95 */       sb.append(this._context);
/*  96 */       sb.append(", static_permitions=");
/*  97 */       sb.append(this._statisPermissions);
/*  98 */       sb.append(')');
/*  99 */       return sb.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaPolicySecurity() {
/* 109 */     new CodeSource(null, (Certificate[])null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void callProcessFileSecure(final Context cx, final Scriptable scope, final String filename) {
/* 117 */     AccessController.doPrivileged(new PrivilegedAction() {
/*     */           public Object run() {
/* 119 */             URL url = JavaPolicySecurity.this.getUrlObj(filename);
/* 120 */             ProtectionDomain staticDomain = JavaPolicySecurity.this.getUrlDomain(url);
/*     */             try {
/* 122 */               Main.processFileSecure(cx, scope, url.toExternalForm(), staticDomain);
/*     */             }
/* 124 */             catch (IOException ioex) {
/* 125 */               throw new RuntimeException(ioex);
/*     */             } 
/* 127 */             return null;
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private URL getUrlObj(String url) {
/*     */     URL uRL;
/*     */     try {
/* 136 */       uRL = new URL(url);
/* 137 */     } catch (MalformedURLException ex) {
/*     */ 
/*     */       
/* 140 */       String curDir = System.getProperty("user.dir");
/* 141 */       curDir = curDir.replace('\\', '/');
/* 142 */       if (!curDir.endsWith("/")) {
/* 143 */         curDir = curDir + '/';
/*     */       }
/*     */       try {
/* 146 */         URL curDirURL = new URL("file:" + curDir);
/* 147 */         uRL = new URL(curDirURL, url);
/* 148 */       } catch (MalformedURLException ex2) {
/* 149 */         throw new RuntimeException("Can not construct file URL for '" + url + "':" + ex2.getMessage());
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 154 */     return uRL;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private ProtectionDomain getUrlDomain(URL url) {
/* 160 */     CodeSource cs = new CodeSource(url, (Certificate[])null);
/* 161 */     PermissionCollection pc = Policy.getPolicy().getPermissions(cs);
/* 162 */     return new ProtectionDomain(cs, pc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GeneratedClassLoader createClassLoader(final ClassLoader parentLoader, Object securityDomain) {
/* 169 */     final ProtectionDomain domain = (ProtectionDomain)securityDomain;
/* 170 */     return AccessController.<GeneratedClassLoader>doPrivileged((PrivilegedAction)new PrivilegedAction<Loader>() {
/*     */           public JavaPolicySecurity.Loader run() {
/* 172 */             return new JavaPolicySecurity.Loader(parentLoader, domain);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getDynamicSecurityDomain(Object securityDomain) {
/* 180 */     ProtectionDomain staticDomain = (ProtectionDomain)securityDomain;
/* 181 */     return getDynamicDomain(staticDomain);
/*     */   }
/*     */   
/*     */   private ProtectionDomain getDynamicDomain(ProtectionDomain staticDomain) {
/* 185 */     ContextPermissions p = new ContextPermissions(staticDomain);
/* 186 */     ProtectionDomain contextDomain = new ProtectionDomain(null, p);
/* 187 */     return contextDomain;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object callWithDomain(Object securityDomain, final Context cx, final Callable callable, final Scriptable scope, final Scriptable thisObj, final Object[] args) {
/* 198 */     ProtectionDomain staticDomain = (ProtectionDomain)securityDomain;
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
/* 214 */     ProtectionDomain dynamicDomain = getDynamicDomain(staticDomain);
/* 215 */     ProtectionDomain[] tmp = { dynamicDomain };
/* 216 */     AccessControlContext restricted = new AccessControlContext(tmp);
/*     */     
/* 218 */     PrivilegedAction<Object> action = new PrivilegedAction() {
/*     */         public Object run() {
/* 220 */           return callable.call(cx, scope, thisObj, args);
/*     */         }
/*     */       };
/*     */     
/* 224 */     return AccessController.doPrivileged(action, restricted);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\tools\shell\JavaPolicySecurity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */