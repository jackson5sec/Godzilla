/*     */ package org.mozilla.javascript.tools.debugger;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import javax.swing.JFrame;
/*     */ import org.mozilla.javascript.Context;
/*     */ import org.mozilla.javascript.ContextFactory;
/*     */ import org.mozilla.javascript.Kit;
/*     */ import org.mozilla.javascript.Scriptable;
/*     */ import org.mozilla.javascript.tools.shell.Global;
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
/*     */ public class Main
/*     */ {
/*     */   private Dim dim;
/*     */   private SwingGui debugGui;
/*     */   
/*     */   public Main(String title) {
/*  37 */     this.dim = new Dim();
/*  38 */     this.debugGui = new SwingGui(this.dim, title);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JFrame getDebugFrame() {
/*  45 */     return this.debugGui;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doBreak() {
/*  52 */     this.dim.setBreak();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBreakOnExceptions(boolean value) {
/*  59 */     this.dim.setBreakOnExceptions(value);
/*  60 */     this.debugGui.getMenubar().getBreakOnExceptions().setSelected(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBreakOnEnter(boolean value) {
/*  67 */     this.dim.setBreakOnEnter(value);
/*  68 */     this.debugGui.getMenubar().getBreakOnEnter().setSelected(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBreakOnReturn(boolean value) {
/*  75 */     this.dim.setBreakOnReturn(value);
/*  76 */     this.debugGui.getMenubar().getBreakOnReturn().setSelected(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearAllBreakpoints() {
/*  83 */     this.dim.clearAllBreakpoints();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void go() {
/*  90 */     this.dim.go();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScope(Scriptable scope) {
/*  97 */     setScopeProvider(IProxy.newScopeProvider(scope));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScopeProvider(ScopeProvider p) {
/* 105 */     this.dim.setScopeProvider(p);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSourceProvider(SourceProvider sourceProvider) {
/* 113 */     this.dim.setSourceProvider(sourceProvider);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExitAction(Runnable r) {
/* 121 */     this.debugGui.setExitAction(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getIn() {
/* 129 */     return this.debugGui.getConsole().getIn();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PrintStream getOut() {
/* 137 */     return this.debugGui.getConsole().getOut();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PrintStream getErr() {
/* 145 */     return this.debugGui.getConsole().getErr();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void pack() {
/* 152 */     this.debugGui.pack();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSize(int w, int h) {
/* 159 */     this.debugGui.setSize(w, h);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVisible(boolean flag) {
/* 166 */     this.debugGui.setVisible(flag);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isVisible() {
/* 173 */     return this.debugGui.isVisible();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 180 */     clearAllBreakpoints();
/* 181 */     this.dim.go();
/* 182 */     this.debugGui.dispose();
/* 183 */     this.dim = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void attachTo(ContextFactory factory) {
/* 190 */     this.dim.attachTo(factory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void detach() {
/* 197 */     this.dim.detach();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {
/* 205 */     Main main = new Main("Rhino JavaScript Debugger");
/* 206 */     main.doBreak();
/* 207 */     main.setExitAction(new IProxy(1));
/*     */     
/* 209 */     System.setIn(main.getIn());
/* 210 */     System.setOut(main.getOut());
/* 211 */     System.setErr(main.getErr());
/*     */     
/* 213 */     Global global = org.mozilla.javascript.tools.shell.Main.getGlobal();
/* 214 */     global.setIn(main.getIn());
/* 215 */     global.setOut(main.getOut());
/* 216 */     global.setErr(main.getErr());
/*     */     
/* 218 */     main.attachTo((ContextFactory)org.mozilla.javascript.tools.shell.Main.shellContextFactory);
/*     */ 
/*     */     
/* 221 */     main.setScope((Scriptable)global);
/*     */     
/* 223 */     main.pack();
/* 224 */     main.setSize(600, 460);
/* 225 */     main.setVisible(true);
/*     */     
/* 227 */     org.mozilla.javascript.tools.shell.Main.exec(args);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Main mainEmbedded(String title) {
/* 237 */     ContextFactory factory = ContextFactory.getGlobal();
/* 238 */     Global global = new Global();
/* 239 */     global.init(factory);
/* 240 */     return mainEmbedded(factory, (Scriptable)global, title);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Main mainEmbedded(ContextFactory factory, Scriptable scope, String title) {
/* 251 */     return mainEmbeddedImpl(factory, scope, title);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Main mainEmbedded(ContextFactory factory, ScopeProvider scopeProvider, String title) {
/* 262 */     return mainEmbeddedImpl(factory, scopeProvider, title);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Main mainEmbeddedImpl(ContextFactory factory, Object scopeProvider, String title) {
/* 271 */     if (title == null) {
/* 272 */       title = "Rhino JavaScript Debugger (embedded usage)";
/*     */     }
/* 274 */     Main main = new Main(title);
/* 275 */     main.doBreak();
/* 276 */     main.setExitAction(new IProxy(1));
/*     */     
/* 278 */     main.attachTo(factory);
/* 279 */     if (scopeProvider instanceof ScopeProvider) {
/* 280 */       main.setScopeProvider((ScopeProvider)scopeProvider);
/*     */     } else {
/* 282 */       Scriptable scope = (Scriptable)scopeProvider;
/* 283 */       if (scope instanceof Global) {
/* 284 */         Global global = (Global)scope;
/* 285 */         global.setIn(main.getIn());
/* 286 */         global.setOut(main.getOut());
/* 287 */         global.setErr(main.getErr());
/*     */       } 
/* 289 */       main.setScope(scope);
/*     */     } 
/*     */     
/* 292 */     main.pack();
/* 293 */     main.setSize(600, 460);
/* 294 */     main.setVisible(true);
/* 295 */     return main;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setSize(Dimension dimension) {
/* 305 */     this.debugGui.setSize(dimension.width, dimension.height);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setOptimizationLevel(int level) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void contextEntered(Context cx) {
/* 322 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void contextExited(Context cx) {
/* 331 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void contextCreated(Context cx) {
/* 340 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void contextReleased(Context cx) {
/* 350 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class IProxy
/*     */     implements Runnable, ScopeProvider
/*     */   {
/*     */     public static final int EXIT_ACTION = 1;
/*     */ 
/*     */ 
/*     */     
/*     */     public static final int SCOPE_PROVIDER = 2;
/*     */ 
/*     */ 
/*     */     
/*     */     private final int type;
/*     */ 
/*     */ 
/*     */     
/*     */     private Scriptable scope;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public IProxy(int type) {
/* 378 */       this.type = type;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static ScopeProvider newScopeProvider(Scriptable scope) {
/* 385 */       IProxy scopeProvider = new IProxy(2);
/* 386 */       scopeProvider.scope = scope;
/* 387 */       return scopeProvider;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void run() {
/* 396 */       if (this.type != 1) Kit.codeBug(); 
/* 397 */       System.exit(0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Scriptable getScope() {
/* 406 */       if (this.type != 2) Kit.codeBug(); 
/* 407 */       if (this.scope == null) Kit.codeBug(); 
/* 408 */       return this.scope;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\tools\debugger\Main.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */