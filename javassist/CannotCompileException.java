/*     */ package javassist;
/*     */ 
/*     */ import javassist.compiler.CompileError;
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
/*     */ public class CannotCompileException
/*     */   extends Exception
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private Throwable myCause;
/*     */   private String message;
/*     */   
/*     */   public synchronized Throwable getCause() {
/*  35 */     return (this.myCause == this) ? null : this.myCause;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Throwable initCause(Throwable cause) {
/*  44 */     this.myCause = cause;
/*  45 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getReason() {
/*  54 */     if (this.message != null)
/*  55 */       return this.message; 
/*  56 */     return toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CannotCompileException(String msg) {
/*  65 */     super(msg);
/*  66 */     this.message = msg;
/*  67 */     initCause(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CannotCompileException(Throwable e) {
/*  77 */     super("by " + e.toString());
/*  78 */     this.message = null;
/*  79 */     initCause(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CannotCompileException(String msg, Throwable e) {
/*  90 */     this(msg);
/*  91 */     initCause(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CannotCompileException(NotFoundException e) {
/*  99 */     this("cannot find " + e.getMessage(), e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CannotCompileException(CompileError e) {
/* 106 */     this("[source error] " + e.getMessage(), (Throwable)e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CannotCompileException(ClassNotFoundException e, String name) {
/* 114 */     this("cannot find " + name, e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CannotCompileException(ClassFormatError e, String name) {
/* 121 */     this("invalid class format: " + name, e);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\CannotCompileException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */