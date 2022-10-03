/*      */ package org.mozilla.javascript;
/*      */ 
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintWriter;
/*      */ import java.io.Reader;
/*      */ import java.io.StringWriter;
/*      */ import java.io.Writer;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import org.mozilla.javascript.ast.AstRoot;
/*      */ import org.mozilla.javascript.ast.ScriptNode;
/*      */ import org.mozilla.javascript.debug.DebuggableScript;
/*      */ import org.mozilla.javascript.debug.Debugger;
/*      */ import org.mozilla.javascript.xml.XMLLib;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Context
/*      */ {
/*      */   public static final int VERSION_UNKNOWN = -1;
/*      */   public static final int VERSION_DEFAULT = 0;
/*      */   public static final int VERSION_1_0 = 100;
/*      */   public static final int VERSION_1_1 = 110;
/*      */   public static final int VERSION_1_2 = 120;
/*      */   public static final int VERSION_1_3 = 130;
/*      */   public static final int VERSION_1_4 = 140;
/*      */   public static final int VERSION_1_5 = 150;
/*      */   public static final int VERSION_1_6 = 160;
/*      */   public static final int VERSION_1_7 = 170;
/*      */   public static final int VERSION_1_8 = 180;
/*      */   public static final int FEATURE_NON_ECMA_GET_YEAR = 1;
/*      */   public static final int FEATURE_MEMBER_EXPR_AS_FUNCTION_NAME = 2;
/*      */   public static final int FEATURE_RESERVED_KEYWORD_AS_IDENTIFIER = 3;
/*      */   public static final int FEATURE_TO_STRING_AS_SOURCE = 4;
/*      */   public static final int FEATURE_PARENT_PROTO_PROPERTIES = 5;
/*      */   @Deprecated
/*      */   public static final int FEATURE_PARENT_PROTO_PROPRTIES = 5;
/*      */   public static final int FEATURE_E4X = 6;
/*      */   public static final int FEATURE_DYNAMIC_SCOPE = 7;
/*      */   public static final int FEATURE_STRICT_VARS = 8;
/*      */   public static final int FEATURE_STRICT_EVAL = 9;
/*      */   public static final int FEATURE_LOCATION_INFORMATION_IN_ERROR = 10;
/*      */   public static final int FEATURE_STRICT_MODE = 11;
/*      */   public static final int FEATURE_WARNING_AS_ERROR = 12;
/*      */   public static final int FEATURE_ENHANCED_JAVA_ACCESS = 13;
/*      */   public static final int FEATURE_V8_EXTENSIONS = 14;
/*      */   public static final String languageVersionProperty = "language version";
/*      */   public static final String errorReporterProperty = "error reporter";
/*  300 */   public static final Object[] emptyArgs = ScriptRuntime.emptyArgs;
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
/*      */   public Context() {
/*  318 */     this(ContextFactory.getGlobal());
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
/*      */   protected Context(ContextFactory factory) {
/*  332 */     if (factory == null) {
/*  333 */       throw new IllegalArgumentException("factory == null");
/*      */     }
/*  335 */     this.factory = factory;
/*  336 */     this.version = 0;
/*  337 */     this.optimizationLevel = (codegenClass != null) ? 0 : -1;
/*  338 */     this.maximumInterpreterStackDepth = Integer.MAX_VALUE;
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
/*      */   public static Context getCurrentContext() {
/*  355 */     Object helper = VMBridge.instance.getThreadContextHelper();
/*  356 */     return VMBridge.instance.getContext(helper);
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
/*      */   public static Context enter() {
/*  369 */     return enter(null);
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
/*      */   @Deprecated
/*      */   public static Context enter(Context cx) {
/*  390 */     return enter(cx, ContextFactory.getGlobal());
/*      */   }
/*      */ 
/*      */   
/*      */   static final Context enter(Context cx, ContextFactory factory) {
/*  395 */     Object helper = VMBridge.instance.getThreadContextHelper();
/*  396 */     Context old = VMBridge.instance.getContext(helper);
/*  397 */     if (old != null) {
/*  398 */       cx = old;
/*      */     } else {
/*  400 */       if (cx == null) {
/*  401 */         cx = factory.makeContext();
/*  402 */         if (cx.enterCount != 0) {
/*  403 */           throw new IllegalStateException("factory.makeContext() returned Context instance already associated with some thread");
/*      */         }
/*  405 */         factory.onContextCreated(cx);
/*  406 */         if (factory.isSealed() && !cx.isSealed()) {
/*  407 */           cx.seal(null);
/*      */         }
/*      */       }
/*  410 */       else if (cx.enterCount != 0) {
/*  411 */         throw new IllegalStateException("can not use Context instance already associated with some thread");
/*      */       } 
/*      */       
/*  414 */       VMBridge.instance.setContext(helper, cx);
/*      */     } 
/*  416 */     cx.enterCount++;
/*  417 */     return cx;
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
/*      */   public static void exit() {
/*  433 */     Object helper = VMBridge.instance.getThreadContextHelper();
/*  434 */     Context cx = VMBridge.instance.getContext(helper);
/*  435 */     if (cx == null) {
/*  436 */       throw new IllegalStateException("Calling Context.exit without previous Context.enter");
/*      */     }
/*      */     
/*  439 */     if (cx.enterCount < 1) Kit.codeBug(); 
/*  440 */     if (--cx.enterCount == 0) {
/*  441 */       VMBridge.instance.setContext(helper, null);
/*  442 */       cx.factory.onContextReleased(cx);
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
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static Object call(ContextAction action) {
/*  462 */     return call(ContextFactory.getGlobal(), action);
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
/*      */   public static Object call(ContextFactory factory, final Callable callable, final Scriptable scope, final Scriptable thisObj, final Object[] args) {
/*  484 */     if (factory == null) {
/*  485 */       factory = ContextFactory.getGlobal();
/*      */     }
/*  487 */     return call(factory, new ContextAction() {
/*      */           public Object run(Context cx) {
/*  489 */             return callable.call(cx, scope, thisObj, args);
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Object call(ContextFactory factory, ContextAction action) {
/*  498 */     Context cx = enter(null, factory);
/*      */     try {
/*  500 */       return action.run(cx);
/*      */     } finally {
/*      */       
/*  503 */       exit();
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
/*      */   @Deprecated
/*      */   public static void addContextListener(ContextListener listener) {
/*  516 */     String DBG = "org.mozilla.javascript.tools.debugger.Main";
/*  517 */     if (DBG.equals(listener.getClass().getName())) {
/*  518 */       Class<?> cl = listener.getClass();
/*  519 */       Class<?> factoryClass = Kit.classOrNull("org.mozilla.javascript.ContextFactory");
/*      */       
/*  521 */       Class<?>[] sig = new Class[] { factoryClass };
/*  522 */       Object[] args = { ContextFactory.getGlobal() };
/*      */       try {
/*  524 */         Method m = cl.getMethod("attachTo", sig);
/*  525 */         m.invoke(listener, args);
/*  526 */       } catch (Exception ex) {
/*  527 */         RuntimeException rex = new RuntimeException();
/*  528 */         Kit.initCause(rex, ex);
/*  529 */         throw rex;
/*      */       } 
/*      */       
/*      */       return;
/*      */     } 
/*  534 */     ContextFactory.getGlobal().addListener(listener);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void removeContextListener(ContextListener listener) {
/*  545 */     ContextFactory.getGlobal().addListener(listener);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ContextFactory getFactory() {
/*  553 */     return this.factory;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isSealed() {
/*  564 */     return this.sealed;
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
/*      */   public final void seal(Object sealKey) {
/*  581 */     if (this.sealed) onSealedMutation(); 
/*  582 */     this.sealed = true;
/*  583 */     this.sealKey = sealKey;
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
/*      */   public final void unseal(Object sealKey) {
/*  597 */     if (sealKey == null) throw new IllegalArgumentException(); 
/*  598 */     if (this.sealKey != sealKey) throw new IllegalArgumentException(); 
/*  599 */     if (!this.sealed) throw new IllegalStateException(); 
/*  600 */     this.sealed = false;
/*  601 */     this.sealKey = null;
/*      */   }
/*      */ 
/*      */   
/*      */   static void onSealedMutation() {
/*  606 */     throw new IllegalStateException();
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
/*      */   public final int getLanguageVersion() {
/*  619 */     return this.version;
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
/*      */   public void setLanguageVersion(int version) {
/*  634 */     if (this.sealed) onSealedMutation(); 
/*  635 */     checkLanguageVersion(version);
/*  636 */     Object listeners = this.propertyListeners;
/*  637 */     if (listeners != null && version != this.version) {
/*  638 */       firePropertyChangeImpl(listeners, "language version", Integer.valueOf(this.version), Integer.valueOf(version));
/*      */     }
/*      */ 
/*      */     
/*  642 */     this.version = version;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isValidLanguageVersion(int version) {
/*  647 */     switch (version) {
/*      */       case 0:
/*      */       case 100:
/*      */       case 110:
/*      */       case 120:
/*      */       case 130:
/*      */       case 140:
/*      */       case 150:
/*      */       case 160:
/*      */       case 170:
/*      */       case 180:
/*  658 */         return true;
/*      */     } 
/*  660 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void checkLanguageVersion(int version) {
/*  665 */     if (isValidLanguageVersion(version)) {
/*      */       return;
/*      */     }
/*  668 */     throw new IllegalArgumentException("Bad language version: " + version);
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
/*      */ 
/*      */   
/*      */   public final String getImplementationVersion() {
/*  692 */     if (implementationVersion == null) {
/*  693 */       implementationVersion = ScriptRuntime.getMessage0("implementation.version");
/*      */     }
/*      */     
/*  696 */     return implementationVersion;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ErrorReporter getErrorReporter() {
/*  706 */     if (this.errorReporter == null) {
/*  707 */       return DefaultErrorReporter.instance;
/*      */     }
/*  709 */     return this.errorReporter;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ErrorReporter setErrorReporter(ErrorReporter reporter) {
/*  720 */     if (this.sealed) onSealedMutation(); 
/*  721 */     if (reporter == null) throw new IllegalArgumentException(); 
/*  722 */     ErrorReporter old = getErrorReporter();
/*  723 */     if (reporter == old) {
/*  724 */       return old;
/*      */     }
/*  726 */     Object listeners = this.propertyListeners;
/*  727 */     if (listeners != null) {
/*  728 */       firePropertyChangeImpl(listeners, "error reporter", old, reporter);
/*      */     }
/*      */     
/*  731 */     this.errorReporter = reporter;
/*  732 */     return old;
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
/*      */   public final Locale getLocale() {
/*  744 */     if (this.locale == null)
/*  745 */       this.locale = Locale.getDefault(); 
/*  746 */     return this.locale;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Locale setLocale(Locale loc) {
/*  756 */     if (this.sealed) onSealedMutation(); 
/*  757 */     Locale result = this.locale;
/*  758 */     this.locale = loc;
/*  759 */     return result;
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
/*      */   public final void addPropertyChangeListener(PropertyChangeListener l) {
/*  771 */     if (this.sealed) onSealedMutation(); 
/*  772 */     this.propertyListeners = Kit.addListener(this.propertyListeners, l);
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
/*      */   public final void removePropertyChangeListener(PropertyChangeListener l) {
/*  784 */     if (this.sealed) onSealedMutation(); 
/*  785 */     this.propertyListeners = Kit.removeListener(this.propertyListeners, l);
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
/*      */   final void firePropertyChange(String property, Object oldValue, Object newValue) {
/*  801 */     Object listeners = this.propertyListeners;
/*  802 */     if (listeners != null) {
/*  803 */       firePropertyChangeImpl(listeners, property, oldValue, newValue);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void firePropertyChangeImpl(Object listeners, String property, Object oldValue, Object newValue) {
/*  810 */     for (int i = 0;; i++) {
/*  811 */       Object l = Kit.getListener(listeners, i);
/*  812 */       if (l == null)
/*      */         break; 
/*  814 */       if (l instanceof PropertyChangeListener) {
/*  815 */         PropertyChangeListener pcl = (PropertyChangeListener)l;
/*  816 */         pcl.propertyChange(new PropertyChangeEvent(this, property, oldValue, newValue));
/*      */       } 
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
/*      */ 
/*      */ 
/*      */   
/*      */   public static void reportWarning(String message, String sourceName, int lineno, String lineSource, int lineOffset) {
/*  836 */     Context cx = getContext();
/*  837 */     if (cx.hasFeature(12)) {
/*  838 */       reportError(message, sourceName, lineno, lineSource, lineOffset);
/*      */     } else {
/*  840 */       cx.getErrorReporter().warning(message, sourceName, lineno, lineSource, lineOffset);
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
/*      */   public static void reportWarning(String message) {
/*  852 */     int[] linep = { 0 };
/*  853 */     String filename = getSourcePositionFromStack(linep);
/*  854 */     reportWarning(message, filename, linep[0], null, 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void reportWarning(String message, Throwable t) {
/*  859 */     int[] linep = { 0 };
/*  860 */     String filename = getSourcePositionFromStack(linep);
/*  861 */     Writer sw = new StringWriter();
/*  862 */     PrintWriter pw = new PrintWriter(sw);
/*  863 */     pw.println(message);
/*  864 */     t.printStackTrace(pw);
/*  865 */     pw.flush();
/*  866 */     reportWarning(sw.toString(), filename, linep[0], null, 0);
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
/*      */   public static void reportError(String message, String sourceName, int lineno, String lineSource, int lineOffset) {
/*  883 */     Context cx = getCurrentContext();
/*  884 */     if (cx != null) {
/*  885 */       cx.getErrorReporter().error(message, sourceName, lineno, lineSource, lineOffset);
/*      */     } else {
/*      */       
/*  888 */       throw new EvaluatorException(message, sourceName, lineno, lineSource, lineOffset);
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
/*      */   public static void reportError(String message) {
/*  901 */     int[] linep = { 0 };
/*  902 */     String filename = getSourcePositionFromStack(linep);
/*  903 */     reportError(message, filename, linep[0], null, 0);
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
/*      */   public static EvaluatorException reportRuntimeError(String message, String sourceName, int lineno, String lineSource, int lineOffset) {
/*  924 */     Context cx = getCurrentContext();
/*  925 */     if (cx != null) {
/*  926 */       return cx.getErrorReporter().runtimeError(message, sourceName, lineno, lineSource, lineOffset);
/*      */     }
/*      */ 
/*      */     
/*  930 */     throw new EvaluatorException(message, sourceName, lineno, lineSource, lineOffset);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static EvaluatorException reportRuntimeError0(String messageId) {
/*  937 */     String msg = ScriptRuntime.getMessage0(messageId);
/*  938 */     return reportRuntimeError(msg);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static EvaluatorException reportRuntimeError1(String messageId, Object arg1) {
/*  944 */     String msg = ScriptRuntime.getMessage1(messageId, arg1);
/*  945 */     return reportRuntimeError(msg);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static EvaluatorException reportRuntimeError2(String messageId, Object arg1, Object arg2) {
/*  951 */     String msg = ScriptRuntime.getMessage2(messageId, arg1, arg2);
/*  952 */     return reportRuntimeError(msg);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static EvaluatorException reportRuntimeError3(String messageId, Object arg1, Object arg2, Object arg3) {
/*  959 */     String msg = ScriptRuntime.getMessage3(messageId, arg1, arg2, arg3);
/*  960 */     return reportRuntimeError(msg);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static EvaluatorException reportRuntimeError4(String messageId, Object arg1, Object arg2, Object arg3, Object arg4) {
/*  967 */     String msg = ScriptRuntime.getMessage4(messageId, arg1, arg2, arg3, arg4);
/*      */     
/*  969 */     return reportRuntimeError(msg);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static EvaluatorException reportRuntimeError(String message) {
/*  980 */     int[] linep = { 0 };
/*  981 */     String filename = getSourcePositionFromStack(linep);
/*  982 */     return reportRuntimeError(message, filename, linep[0], null, 0);
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
/*      */   public final ScriptableObject initStandardObjects() {
/* 1001 */     return initStandardObjects(null, false);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ScriptableObject initSafeStandardObjects() {
/* 1028 */     return initSafeStandardObjects(null, false);
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
/*      */   
/*      */   public final Scriptable initStandardObjects(ScriptableObject scope) {
/* 1051 */     return initStandardObjects(scope, false);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Scriptable initSafeStandardObjects(ScriptableObject scope) {
/* 1082 */     return initSafeStandardObjects(scope, false);
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
/*      */   public ScriptableObject initStandardObjects(ScriptableObject scope, boolean sealed) {
/* 1115 */     return ScriptRuntime.initStandardObjects(this, scope, sealed);
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
/*      */   public ScriptableObject initSafeStandardObjects(ScriptableObject scope, boolean sealed) {
/* 1156 */     return ScriptRuntime.initSafeStandardObjects(this, scope, sealed);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object getUndefinedValue() {
/* 1164 */     return Undefined.instance;
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
/*      */ 
/*      */   
/*      */   public final Object evaluateString(Scriptable scope, String source, String sourceName, int lineno, Object securityDomain) {
/* 1188 */     Script script = compileString(source, sourceName, lineno, securityDomain);
/*      */     
/* 1190 */     if (script != null) {
/* 1191 */       return script.exec(this, scope);
/*      */     }
/* 1193 */     return null;
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Object evaluateReader(Scriptable scope, Reader in, String sourceName, int lineno, Object securityDomain) throws IOException {
/* 1219 */     Script script = compileReader(scope, in, sourceName, lineno, securityDomain);
/*      */     
/* 1221 */     if (script != null) {
/* 1222 */       return script.exec(this, scope);
/*      */     }
/* 1224 */     return null;
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
/*      */   public Object executeScriptWithContinuations(Script script, Scriptable scope) throws ContinuationPending {
/* 1244 */     if (!(script instanceof InterpretedFunction) || !((InterpretedFunction)script).isScript())
/*      */     {
/*      */ 
/*      */       
/* 1248 */       throw new IllegalArgumentException("Script argument was not a script or was not created by interpreted mode ");
/*      */     }
/*      */     
/* 1251 */     return callFunctionWithContinuations((InterpretedFunction)script, scope, ScriptRuntime.emptyArgs);
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
/*      */   public Object callFunctionWithContinuations(Callable function, Scriptable scope, Object[] args) throws ContinuationPending {
/* 1272 */     if (!(function instanceof InterpretedFunction))
/*      */     {
/* 1274 */       throw new IllegalArgumentException("Function argument was not created by interpreted mode ");
/*      */     }
/*      */     
/* 1277 */     if (ScriptRuntime.hasTopCall(this)) {
/* 1278 */       throw new IllegalStateException("Cannot have any pending top calls when executing a script with continuations");
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1283 */     this.isContinuationsTopCall = true;
/* 1284 */     return ScriptRuntime.doTopCall(function, this, scope, scope, args);
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
/*      */   public ContinuationPending captureContinuation() {
/* 1301 */     return new ContinuationPending(Interpreter.captureContinuation(this));
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
/*      */ 
/*      */   
/*      */   public Object resumeContinuation(Object continuation, Scriptable scope, Object functionResult) throws ContinuationPending {
/* 1325 */     Object[] args = { functionResult };
/* 1326 */     return Interpreter.restartContinuation((NativeContinuation)continuation, this, scope, args);
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
/*      */   
/*      */   public final boolean stringIsCompilableUnit(String source) {
/* 1349 */     boolean errorseen = false;
/* 1350 */     CompilerEnvirons compilerEnv = new CompilerEnvirons();
/* 1351 */     compilerEnv.initFromContext(this);
/*      */ 
/*      */     
/* 1354 */     compilerEnv.setGeneratingSource(false);
/* 1355 */     Parser p = new Parser(compilerEnv, DefaultErrorReporter.instance);
/*      */     try {
/* 1357 */       p.parse(source, (String)null, 1);
/* 1358 */     } catch (EvaluatorException ee) {
/* 1359 */       errorseen = true;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1364 */     if (errorseen && p.eof()) {
/* 1365 */       return false;
/*      */     }
/* 1367 */     return true;
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
/*      */   @Deprecated
/*      */   public final Script compileReader(Scriptable scope, Reader in, String sourceName, int lineno, Object securityDomain) throws IOException {
/* 1381 */     return compileReader(in, sourceName, lineno, securityDomain);
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
/*      */ 
/*      */   
/*      */   public final Script compileReader(Reader in, String sourceName, int lineno, Object securityDomain) throws IOException {
/* 1405 */     if (lineno < 0)
/*      */     {
/* 1407 */       lineno = 0;
/*      */     }
/* 1409 */     return (Script)compileImpl(null, in, null, sourceName, lineno, securityDomain, false, null, null);
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
/*      */ 
/*      */   
/*      */   public final Script compileString(String source, String sourceName, int lineno, Object securityDomain) {
/* 1433 */     if (lineno < 0)
/*      */     {
/* 1435 */       lineno = 0;
/*      */     }
/* 1437 */     return compileString(source, null, null, sourceName, lineno, securityDomain);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final Script compileString(String source, Evaluator compiler, ErrorReporter compilationErrorReporter, String sourceName, int lineno, Object securityDomain) {
/*      */     try {
/* 1448 */       return (Script)compileImpl(null, null, source, sourceName, lineno, securityDomain, false, compiler, compilationErrorReporter);
/*      */     
/*      */     }
/* 1451 */     catch (IOException ex) {
/*      */       
/* 1453 */       throw new RuntimeException();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Function compileFunction(Scriptable scope, String source, String sourceName, int lineno, Object securityDomain) {
/* 1478 */     return compileFunction(scope, source, null, null, sourceName, lineno, securityDomain);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final Function compileFunction(Scriptable scope, String source, Evaluator compiler, ErrorReporter compilationErrorReporter, String sourceName, int lineno, Object securityDomain) {
/*      */     try {
/* 1489 */       return (Function)compileImpl(scope, null, source, sourceName, lineno, securityDomain, true, compiler, compilationErrorReporter);
/*      */ 
/*      */     
/*      */     }
/* 1493 */     catch (IOException ioe) {
/*      */ 
/*      */       
/* 1496 */       throw new RuntimeException();
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
/*      */   public final String decompileScript(Script script, int indent) {
/* 1511 */     NativeFunction scriptImpl = (NativeFunction)script;
/* 1512 */     return scriptImpl.decompile(indent, 0);
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
/*      */   public final String decompileFunction(Function fun, int indent) {
/* 1530 */     if (fun instanceof BaseFunction) {
/* 1531 */       return ((BaseFunction)fun).decompile(indent, 0);
/*      */     }
/* 1533 */     return "function " + fun.getClassName() + "() {\n\t[native code]\n}\n";
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
/*      */   public final String decompileFunctionBody(Function fun, int indent) {
/* 1552 */     if (fun instanceof BaseFunction) {
/* 1553 */       BaseFunction bf = (BaseFunction)fun;
/* 1554 */       return bf.decompile(indent, 1);
/*      */     } 
/*      */     
/* 1557 */     return "[native code]\n";
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
/*      */   public Scriptable newObject(Scriptable scope) {
/* 1570 */     NativeObject result = new NativeObject();
/* 1571 */     ScriptRuntime.setBuiltinProtoAndParent(result, scope, TopLevel.Builtins.Object);
/*      */     
/* 1573 */     return result;
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
/*      */   public Scriptable newObject(Scriptable scope, String constructorName) {
/* 1588 */     return newObject(scope, constructorName, ScriptRuntime.emptyArgs);
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
/*      */ 
/*      */ 
/*      */   
/*      */   public Scriptable newObject(Scriptable scope, String constructorName, Object[] args) {
/* 1613 */     return ScriptRuntime.newObject(this, scope, constructorName, args);
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
/*      */   public Scriptable newArray(Scriptable scope, int length) {
/* 1626 */     NativeArray result = new NativeArray(length);
/* 1627 */     ScriptRuntime.setBuiltinProtoAndParent(result, scope, TopLevel.Builtins.Array);
/*      */     
/* 1629 */     return result;
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
/*      */   public Scriptable newArray(Scriptable scope, Object[] elements) {
/* 1644 */     if (elements.getClass().getComponentType() != ScriptRuntime.ObjectClass)
/* 1645 */       throw new IllegalArgumentException(); 
/* 1646 */     NativeArray result = new NativeArray(elements);
/* 1647 */     ScriptRuntime.setBuiltinProtoAndParent(result, scope, TopLevel.Builtins.Array);
/*      */     
/* 1649 */     return result;
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
/*      */   public final Object[] getElements(Scriptable object) {
/* 1671 */     return ScriptRuntime.getArrayElements(object);
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
/*      */   public static boolean toBoolean(Object value) {
/* 1685 */     return ScriptRuntime.toBoolean(value);
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
/*      */   public static double toNumber(Object value) {
/* 1701 */     return ScriptRuntime.toNumber(value);
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
/*      */   public static String toString(Object value) {
/* 1715 */     return ScriptRuntime.toString(value);
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
/*      */   public static Scriptable toObject(Object value, Scriptable scope) {
/* 1737 */     return ScriptRuntime.toObject(scope, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static Scriptable toObject(Object value, Scriptable scope, Class<?> staticType) {
/* 1748 */     return ScriptRuntime.toObject(scope, value);
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
/*      */   public static Object javaToJS(Object value, Scriptable scope) {
/* 1781 */     if (value instanceof String || value instanceof Number || value instanceof Boolean || value instanceof Scriptable)
/*      */     {
/*      */       
/* 1784 */       return value; } 
/* 1785 */     if (value instanceof Character) {
/* 1786 */       return String.valueOf(((Character)value).charValue());
/*      */     }
/* 1788 */     Context cx = getContext();
/* 1789 */     return cx.getWrapFactory().wrap(cx, scope, value, null);
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
/*      */   public static Object jsToJava(Object value, Class<?> desiredType) throws EvaluatorException {
/* 1807 */     return NativeJavaObject.coerceTypeImpl(desiredType, value);
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
/*      */   @Deprecated
/*      */   public static Object toType(Object value, Class<?> desiredType) throws IllegalArgumentException {
/*      */     try {
/* 1822 */       return jsToJava(value, desiredType);
/* 1823 */     } catch (EvaluatorException ex) {
/*      */       
/* 1825 */       IllegalArgumentException ex2 = new IllegalArgumentException(ex.getMessage());
/* 1826 */       Kit.initCause(ex2, ex);
/* 1827 */       throw ex2;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static RuntimeException throwAsScriptRuntimeEx(Throwable e) {
/* 1850 */     while (e instanceof InvocationTargetException) {
/* 1851 */       e = ((InvocationTargetException)e).getTargetException();
/*      */     }
/*      */     
/* 1854 */     if (e instanceof Error) {
/* 1855 */       Context cx = getContext();
/* 1856 */       if (cx == null || !cx.hasFeature(13))
/*      */       {
/*      */         
/* 1859 */         throw (Error)e;
/*      */       }
/*      */     } 
/* 1862 */     if (e instanceof RhinoException) {
/* 1863 */       throw (RhinoException)e;
/*      */     }
/* 1865 */     throw new WrappedException(e);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isGeneratingDebug() {
/* 1874 */     return this.generatingDebug;
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
/*      */   public final void setGeneratingDebug(boolean generatingDebug) {
/* 1886 */     if (this.sealed) onSealedMutation(); 
/* 1887 */     this.generatingDebugChanged = true;
/* 1888 */     if (generatingDebug && getOptimizationLevel() > 0)
/* 1889 */       setOptimizationLevel(0); 
/* 1890 */     this.generatingDebug = generatingDebug;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isGeneratingSource() {
/* 1899 */     return this.generatingSource;
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
/*      */   public final void setGeneratingSource(boolean generatingSource) {
/* 1914 */     if (this.sealed) onSealedMutation(); 
/* 1915 */     this.generatingSource = generatingSource;
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
/*      */   public final int getOptimizationLevel() {
/* 1928 */     return this.optimizationLevel;
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
/*      */   public final void setOptimizationLevel(int optimizationLevel) {
/* 1950 */     if (this.sealed) onSealedMutation(); 
/* 1951 */     if (optimizationLevel == -2)
/*      */     {
/* 1953 */       optimizationLevel = -1;
/*      */     }
/* 1955 */     checkOptimizationLevel(optimizationLevel);
/* 1956 */     if (codegenClass == null)
/* 1957 */       optimizationLevel = -1; 
/* 1958 */     this.optimizationLevel = optimizationLevel;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isValidOptimizationLevel(int optimizationLevel) {
/* 1963 */     return (-1 <= optimizationLevel && optimizationLevel <= 9);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void checkOptimizationLevel(int optimizationLevel) {
/* 1968 */     if (isValidOptimizationLevel(optimizationLevel)) {
/*      */       return;
/*      */     }
/* 1971 */     throw new IllegalArgumentException("Optimization level outside [-1..9]: " + optimizationLevel);
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
/*      */   public final int getMaximumInterpreterStackDepth() {
/* 1991 */     return this.maximumInterpreterStackDepth;
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
/*      */   public final void setMaximumInterpreterStackDepth(int max) {
/* 2013 */     if (this.sealed) onSealedMutation(); 
/* 2014 */     if (this.optimizationLevel != -1) {
/* 2015 */       throw new IllegalStateException("Cannot set maximumInterpreterStackDepth when optimizationLevel != -1");
/*      */     }
/* 2017 */     if (max < 1) {
/* 2018 */       throw new IllegalArgumentException("Cannot set maximumInterpreterStackDepth to less than 1");
/*      */     }
/* 2020 */     this.maximumInterpreterStackDepth = max;
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
/*      */   public final void setSecurityController(SecurityController controller) {
/* 2036 */     if (this.sealed) onSealedMutation(); 
/* 2037 */     if (controller == null) throw new IllegalArgumentException(); 
/* 2038 */     if (this.securityController != null) {
/* 2039 */       throw new SecurityException("Can not overwrite existing SecurityController object");
/*      */     }
/* 2041 */     if (SecurityController.hasGlobal()) {
/* 2042 */       throw new SecurityException("Can not overwrite existing global SecurityController object");
/*      */     }
/* 2044 */     this.securityController = controller;
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
/*      */   public final synchronized void setClassShutter(ClassShutter shutter) {
/* 2057 */     if (this.sealed) onSealedMutation(); 
/* 2058 */     if (shutter == null) throw new IllegalArgumentException(); 
/* 2059 */     if (this.hasClassShutter) {
/* 2060 */       throw new SecurityException("Cannot overwrite existing ClassShutter object");
/*      */     }
/*      */     
/* 2063 */     this.classShutter = shutter;
/* 2064 */     this.hasClassShutter = true;
/*      */   }
/*      */ 
/*      */   
/*      */   final synchronized ClassShutter getClassShutter() {
/* 2069 */     return this.classShutter;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final synchronized ClassShutterSetter getClassShutterSetter() {
/* 2078 */     if (this.hasClassShutter)
/* 2079 */       return null; 
/* 2080 */     this.hasClassShutter = true;
/* 2081 */     return new ClassShutterSetter() {
/*      */         public void setClassShutter(ClassShutter shutter) {
/* 2083 */           Context.this.classShutter = shutter;
/*      */         }
/*      */         public ClassShutter getClassShutter() {
/* 2086 */           return Context.this.classShutter;
/*      */         }
/*      */       };
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
/*      */   public final Object getThreadLocal(Object key) {
/* 2108 */     if (this.threadLocalMap == null)
/* 2109 */       return null; 
/* 2110 */     return this.threadLocalMap.get(key);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final synchronized void putThreadLocal(Object key, Object value) {
/* 2121 */     if (this.sealed) onSealedMutation(); 
/* 2122 */     if (this.threadLocalMap == null)
/* 2123 */       this.threadLocalMap = new HashMap<Object, Object>(); 
/* 2124 */     this.threadLocalMap.put(key, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void removeThreadLocal(Object key) {
/* 2134 */     if (this.sealed) onSealedMutation(); 
/* 2135 */     if (this.threadLocalMap == null)
/*      */       return; 
/* 2137 */     this.threadLocalMap.remove(key);
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
/*      */   public static void setCachingEnabled(boolean cachingEnabled) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setWrapFactory(WrapFactory wrapFactory) {
/* 2160 */     if (this.sealed) onSealedMutation(); 
/* 2161 */     if (wrapFactory == null) throw new IllegalArgumentException(); 
/* 2162 */     this.wrapFactory = wrapFactory;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final WrapFactory getWrapFactory() {
/* 2172 */     if (this.wrapFactory == null) {
/* 2173 */       this.wrapFactory = new WrapFactory();
/*      */     }
/* 2175 */     return this.wrapFactory;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Debugger getDebugger() {
/* 2184 */     return this.debugger;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Object getDebuggerContextData() {
/* 2193 */     return this.debuggerData;
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
/*      */   public final void setDebugger(Debugger debugger, Object contextData) {
/* 2205 */     if (this.sealed) onSealedMutation(); 
/* 2206 */     this.debugger = debugger;
/* 2207 */     this.debuggerData = contextData;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static DebuggableScript getDebuggableView(Script script) {
/* 2217 */     if (script instanceof NativeFunction) {
/* 2218 */       return ((NativeFunction)script).getDebuggableView();
/*      */     }
/* 2220 */     return null;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasFeature(int featureIndex) {
/* 2251 */     ContextFactory f = getFactory();
/* 2252 */     return f.hasFeature(this, featureIndex);
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
/*      */   public XMLLib.Factory getE4xImplementationFactory() {
/* 2267 */     return getFactory().getE4xImplementationFactory();
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
/*      */   public final int getInstructionObserverThreshold() {
/* 2280 */     return this.instructionThreshold;
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
/*      */   public final void setInstructionObserverThreshold(int threshold) {
/* 2300 */     if (this.sealed) onSealedMutation(); 
/* 2301 */     if (threshold < 0) throw new IllegalArgumentException(); 
/* 2302 */     this.instructionThreshold = threshold;
/* 2303 */     setGenerateObserverCount((threshold > 0));
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
/*      */   public void setGenerateObserverCount(boolean generateObserverCount) {
/* 2318 */     this.generateObserverCount = generateObserverCount;
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
/*      */ 
/*      */   
/*      */   protected void observeInstructionCount(int instructionCount) {
/* 2342 */     ContextFactory f = getFactory();
/* 2343 */     f.observeInstructionCount(this, instructionCount);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public GeneratedClassLoader createClassLoader(ClassLoader parent) {
/* 2353 */     ContextFactory f = getFactory();
/* 2354 */     return f.createClassLoader(parent);
/*      */   }
/*      */ 
/*      */   
/*      */   public final ClassLoader getApplicationClassLoader() {
/* 2359 */     if (this.applicationClassLoader == null) {
/* 2360 */       ContextFactory f = getFactory();
/* 2361 */       ClassLoader loader = f.getApplicationClassLoader();
/* 2362 */       if (loader == null) {
/* 2363 */         ClassLoader threadLoader = VMBridge.instance.getCurrentThreadClassLoader();
/*      */         
/* 2365 */         if (threadLoader != null && Kit.testIfCanLoadRhinoClasses(threadLoader))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2372 */           return threadLoader;
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 2377 */         Class<?> fClass = f.getClass();
/* 2378 */         if (fClass != ScriptRuntime.ContextFactoryClass) {
/* 2379 */           loader = fClass.getClassLoader();
/*      */         } else {
/* 2381 */           loader = getClass().getClassLoader();
/*      */         } 
/*      */       } 
/* 2384 */       this.applicationClassLoader = loader;
/*      */     } 
/* 2386 */     return this.applicationClassLoader;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void setApplicationClassLoader(ClassLoader loader) {
/* 2391 */     if (this.sealed) onSealedMutation(); 
/* 2392 */     if (loader == null) {
/*      */       
/* 2394 */       this.applicationClassLoader = null;
/*      */       return;
/*      */     } 
/* 2397 */     if (!Kit.testIfCanLoadRhinoClasses(loader)) {
/* 2398 */       throw new IllegalArgumentException("Loader can not resolve Rhino classes");
/*      */     }
/*      */     
/* 2401 */     this.applicationClassLoader = loader;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Context getContext() {
/* 2412 */     Context cx = getCurrentContext();
/* 2413 */     if (cx == null) {
/* 2414 */       throw new RuntimeException("No Context associated with current Thread");
/*      */     }
/*      */     
/* 2417 */     return cx;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Object compileImpl(Scriptable scope, Reader sourceReader, String sourceString, String sourceName, int lineno, Object securityDomain, boolean returnFunction, Evaluator compiler, ErrorReporter compilationErrorReporter) throws IOException {
/*      */     Object result;
/* 2428 */     if (sourceName == null) {
/* 2429 */       sourceName = "unnamed script";
/*      */     }
/* 2431 */     if (securityDomain != null && getSecurityController() == null) {
/* 2432 */       throw new IllegalArgumentException("securityDomain should be null if setSecurityController() was never called");
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 2437 */     if ((((sourceReader == null) ? 1 : 0) ^ ((sourceString == null) ? 1 : 0)) == 0) Kit.codeBug();
/*      */     
/* 2439 */     if ((((scope == null) ? 1 : 0) ^ returnFunction) == 0) Kit.codeBug();
/*      */     
/* 2441 */     CompilerEnvirons compilerEnv = new CompilerEnvirons();
/* 2442 */     compilerEnv.initFromContext(this);
/* 2443 */     if (compilationErrorReporter == null) {
/* 2444 */       compilationErrorReporter = compilerEnv.getErrorReporter();
/*      */     }
/*      */     
/* 2447 */     if (this.debugger != null && 
/* 2448 */       sourceReader != null) {
/* 2449 */       sourceString = Kit.readReader(sourceReader);
/* 2450 */       sourceReader = null;
/*      */     } 
/*      */ 
/*      */     
/* 2454 */     Parser p = new Parser(compilerEnv, compilationErrorReporter);
/* 2455 */     if (returnFunction) {
/* 2456 */       p.calledByCompileFunction = true;
/*      */     }
/*      */     
/* 2459 */     if (sourceString != null) {
/* 2460 */       ast = p.parse(sourceString, sourceName, lineno);
/*      */     } else {
/* 2462 */       ast = p.parse(sourceReader, sourceName, lineno);
/*      */     } 
/* 2464 */     if (returnFunction)
/*      */     {
/* 2466 */       if (ast.getFirstChild() == null || ast.getFirstChild().getType() != 109)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2472 */         throw new IllegalArgumentException("compileFunction only accepts source with single JS function: " + sourceString);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/* 2477 */     IRFactory irf = new IRFactory(compilerEnv, compilationErrorReporter);
/* 2478 */     ScriptNode tree = irf.transformTree(ast);
/*      */ 
/*      */     
/* 2481 */     p = null;
/* 2482 */     AstRoot ast = null;
/* 2483 */     irf = null;
/*      */     
/* 2485 */     if (compiler == null) {
/* 2486 */       compiler = createCompiler();
/*      */     }
/*      */     
/* 2489 */     Object bytecode = compiler.compile(compilerEnv, tree, tree.getEncodedSource(), returnFunction);
/*      */ 
/*      */     
/* 2492 */     if (this.debugger != null) {
/* 2493 */       if (sourceString == null) Kit.codeBug(); 
/* 2494 */       if (bytecode instanceof DebuggableScript) {
/* 2495 */         DebuggableScript dscript = (DebuggableScript)bytecode;
/* 2496 */         notifyDebugger_r(this, dscript, sourceString);
/*      */       } else {
/* 2498 */         throw new RuntimeException("NOT SUPPORTED");
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 2503 */     if (returnFunction) {
/* 2504 */       result = compiler.createFunctionObject(this, scope, bytecode, securityDomain);
/*      */     } else {
/* 2506 */       result = compiler.createScriptObject(bytecode, securityDomain);
/*      */     } 
/*      */     
/* 2509 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static void notifyDebugger_r(Context cx, DebuggableScript dscript, String debugSource) {
/* 2515 */     cx.debugger.handleCompilationDone(cx, dscript, debugSource);
/* 2516 */     for (int i = 0; i != dscript.getFunctionCount(); i++) {
/* 2517 */       notifyDebugger_r(cx, dscript.getFunction(i), debugSource);
/*      */     }
/*      */   }
/*      */   
/* 2521 */   private static Class<?> codegenClass = Kit.classOrNull("org.mozilla.javascript.optimizer.Codegen");
/*      */   
/* 2523 */   private static Class<?> interpreterClass = Kit.classOrNull("org.mozilla.javascript.Interpreter"); private static String implementationVersion; private final ContextFactory factory; private boolean sealed; private Object sealKey; Scriptable topCallScope; boolean isContinuationsTopCall; NativeCall currentActivationCall; XMLLib cachedXMLLib;
/*      */   BaseFunction typeErrorThrower;
/*      */   ObjToIntMap iterating;
/*      */   
/*      */   private Evaluator createCompiler() {
/* 2528 */     Evaluator result = null;
/* 2529 */     if (this.optimizationLevel >= 0 && codegenClass != null) {
/* 2530 */       result = (Evaluator)Kit.newInstanceOrNull(codegenClass);
/*      */     }
/* 2532 */     if (result == null) {
/* 2533 */       result = createInterpreter();
/*      */     }
/* 2535 */     return result;
/*      */   }
/*      */   Object interpreterSecurityDomain; int version; private SecurityController securityController; private boolean hasClassShutter; private ClassShutter classShutter; private ErrorReporter errorReporter; RegExpProxy regExpProxy; private Locale locale; private boolean generatingDebug; private boolean generatingDebugChanged;
/*      */   
/*      */   static Evaluator createInterpreter() {
/* 2540 */     return (Evaluator)Kit.newInstanceOrNull(interpreterClass);
/*      */   }
/*      */ 
/*      */   
/*      */   static String getSourcePositionFromStack(int[] linep) {
/* 2545 */     Context cx = getCurrentContext();
/* 2546 */     if (cx == null)
/* 2547 */       return null; 
/* 2548 */     if (cx.lastInterpreterFrame != null) {
/* 2549 */       Evaluator evaluator = createInterpreter();
/* 2550 */       if (evaluator != null) {
/* 2551 */         return evaluator.getSourcePositionFromStack(cx, linep);
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2557 */     StackTraceElement[] stackTrace = (new Throwable()).getStackTrace();
/* 2558 */     for (StackTraceElement st : stackTrace) {
/* 2559 */       String file = st.getFileName();
/* 2560 */       if (file != null && !file.endsWith(".java")) {
/* 2561 */         int line = st.getLineNumber();
/* 2562 */         if (line >= 0) {
/* 2563 */           linep[0] = line;
/* 2564 */           return file;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 2569 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   RegExpProxy getRegExpProxy() {
/* 2574 */     if (this.regExpProxy == null) {
/* 2575 */       Class<?> cl = Kit.classOrNull("org.mozilla.javascript.regexp.RegExpImpl");
/*      */       
/* 2577 */       if (cl != null) {
/* 2578 */         this.regExpProxy = (RegExpProxy)Kit.newInstanceOrNull(cl);
/*      */       }
/*      */     } 
/* 2581 */     return this.regExpProxy;
/*      */   }
/*      */ 
/*      */   
/*      */   final boolean isVersionECMA1() {
/* 2586 */     return (this.version == 0 || this.version >= 130);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   SecurityController getSecurityController() {
/* 2592 */     SecurityController global = SecurityController.global();
/* 2593 */     if (global != null) {
/* 2594 */       return global;
/*      */     }
/* 2596 */     return this.securityController;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isGeneratingDebugChanged() {
/* 2601 */     return this.generatingDebugChanged;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addActivationName(String name) {
/* 2612 */     if (this.sealed) onSealedMutation(); 
/* 2613 */     if (this.activationNames == null)
/* 2614 */       this.activationNames = new HashSet<String>(); 
/* 2615 */     this.activationNames.add(name);
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
/*      */   public final boolean isActivationNeeded(String name) {
/* 2628 */     return (this.activationNames != null && this.activationNames.contains(name));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeActivationName(String name) {
/* 2639 */     if (this.sealed) onSealedMutation(); 
/* 2640 */     if (this.activationNames != null)
/* 2641 */       this.activationNames.remove(name); 
/*      */   }
/*      */   
/*      */   private boolean generatingSource = true;
/*      */   boolean useDynamicScope;
/*      */   private int optimizationLevel;
/*      */   private int maximumInterpreterStackDepth;
/*      */   private WrapFactory wrapFactory;
/*      */   Debugger debugger;
/*      */   private Object debuggerData;
/*      */   private int enterCount;
/*      */   private Object propertyListeners;
/*      */   private Map<Object, Object> threadLocalMap;
/*      */   private ClassLoader applicationClassLoader;
/*      */   Set<String> activationNames;
/*      */   Object lastInterpreterFrame;
/*      */   ObjArray previousInterpreterInvocations;
/*      */   int instructionCount;
/*      */   int instructionThreshold;
/*      */   int scratchIndex;
/*      */   long scratchUint32;
/*      */   Scriptable scratchScriptable;
/*      */   public boolean generateObserverCount = false;
/*      */   
/*      */   public static interface ClassShutterSetter {
/*      */     void setClassShutter(ClassShutter param1ClassShutter);
/*      */     
/*      */     ClassShutter getClassShutter();
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\Context.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */