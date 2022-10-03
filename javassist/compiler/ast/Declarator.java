/*     */ package javassist.compiler.ast;
/*     */ 
/*     */ import javassist.compiler.CompileError;
/*     */ import javassist.compiler.TokenId;
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
/*     */ public class Declarator
/*     */   extends ASTList
/*     */   implements TokenId
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected int varType;
/*     */   protected int arrayDim;
/*     */   protected int localVar;
/*     */   protected String qualifiedClass;
/*     */   
/*     */   public Declarator(int type, int dim) {
/*  34 */     super(null);
/*  35 */     this.varType = type;
/*  36 */     this.arrayDim = dim;
/*  37 */     this.localVar = -1;
/*  38 */     this.qualifiedClass = null;
/*     */   }
/*     */   
/*     */   public Declarator(ASTList className, int dim) {
/*  42 */     super(null);
/*  43 */     this.varType = 307;
/*  44 */     this.arrayDim = dim;
/*  45 */     this.localVar = -1;
/*  46 */     this.qualifiedClass = astToClassName(className, '/');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Declarator(int type, String jvmClassName, int dim, int var, Symbol sym) {
/*  53 */     super(null);
/*  54 */     this.varType = type;
/*  55 */     this.arrayDim = dim;
/*  56 */     this.localVar = var;
/*  57 */     this.qualifiedClass = jvmClassName;
/*  58 */     setLeft(sym);
/*  59 */     append(this, null);
/*     */   }
/*     */   
/*     */   public Declarator make(Symbol sym, int dim, ASTree init) {
/*  63 */     Declarator d = new Declarator(this.varType, this.arrayDim + dim);
/*  64 */     d.qualifiedClass = this.qualifiedClass;
/*  65 */     d.setLeft(sym);
/*  66 */     append(d, init);
/*  67 */     return d;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getType() {
/*  73 */     return this.varType;
/*     */   } public int getArrayDim() {
/*  75 */     return this.arrayDim;
/*     */   } public void addArrayDim(int d) {
/*  77 */     this.arrayDim += d;
/*     */   } public String getClassName() {
/*  79 */     return this.qualifiedClass;
/*     */   } public void setClassName(String s) {
/*  81 */     this.qualifiedClass = s;
/*     */   } public Symbol getVariable() {
/*  83 */     return (Symbol)getLeft();
/*     */   } public void setVariable(Symbol sym) {
/*  85 */     setLeft(sym);
/*     */   }
/*     */   public ASTree getInitializer() {
/*  88 */     ASTList t = tail();
/*  89 */     if (t != null)
/*  90 */       return t.head(); 
/*  91 */     return null;
/*     */   }
/*     */   public void setLocalVar(int n) {
/*  94 */     this.localVar = n;
/*     */   } public int getLocalVar() {
/*  96 */     return this.localVar;
/*     */   }
/*     */   public String getTag() {
/*  99 */     return "decl";
/*     */   }
/*     */   
/*     */   public void accept(Visitor v) throws CompileError {
/* 103 */     v.atDeclarator(this);
/*     */   }
/*     */   
/*     */   public static String astToClassName(ASTList name, char sep) {
/* 107 */     if (name == null) {
/* 108 */       return null;
/*     */     }
/* 110 */     StringBuffer sbuf = new StringBuffer();
/* 111 */     astToClassName(sbuf, name, sep);
/* 112 */     return sbuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private static void astToClassName(StringBuffer sbuf, ASTList name, char sep) {
/*     */     while (true) {
/* 118 */       ASTree h = name.head();
/* 119 */       if (h instanceof Symbol) {
/* 120 */         sbuf.append(((Symbol)h).get());
/* 121 */       } else if (h instanceof ASTList) {
/* 122 */         astToClassName(sbuf, (ASTList)h, sep);
/*     */       } 
/* 124 */       name = name.tail();
/* 125 */       if (name == null) {
/*     */         break;
/*     */       }
/* 128 */       sbuf.append(sep);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\compiler\ast\Declarator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */