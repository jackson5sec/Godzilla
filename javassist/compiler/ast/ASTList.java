/*     */ package javassist.compiler.ast;
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
/*     */ public class ASTList
/*     */   extends ASTree
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private ASTree left;
/*     */   private ASTList right;
/*     */   
/*     */   public ASTList(ASTree _head, ASTList _tail) {
/*  32 */     this.left = _head;
/*  33 */     this.right = _tail;
/*     */   }
/*     */   
/*     */   public ASTList(ASTree _head) {
/*  37 */     this.left = _head;
/*  38 */     this.right = null;
/*     */   }
/*     */   
/*     */   public static ASTList make(ASTree e1, ASTree e2, ASTree e3) {
/*  42 */     return new ASTList(e1, new ASTList(e2, new ASTList(e3)));
/*     */   }
/*     */   
/*     */   public ASTree getLeft() {
/*  46 */     return this.left;
/*     */   }
/*     */   public ASTree getRight() {
/*  49 */     return this.right;
/*     */   }
/*     */   public void setLeft(ASTree _left) {
/*  52 */     this.left = _left;
/*     */   }
/*     */   
/*     */   public void setRight(ASTree _right) {
/*  56 */     this.right = (ASTList)_right;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ASTree head() {
/*  62 */     return this.left;
/*     */   }
/*     */   public void setHead(ASTree _head) {
/*  65 */     this.left = _head;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ASTList tail() {
/*  71 */     return this.right;
/*     */   }
/*     */   public void setTail(ASTList _tail) {
/*  74 */     this.right = _tail;
/*     */   }
/*     */   
/*     */   public void accept(Visitor v) throws CompileError {
/*  78 */     v.atASTList(this);
/*     */   }
/*     */   
/*     */   public String toString() {
/*  82 */     StringBuffer sbuf = new StringBuffer();
/*  83 */     sbuf.append("(<");
/*  84 */     sbuf.append(getTag());
/*  85 */     sbuf.append('>');
/*  86 */     ASTList list = this;
/*  87 */     while (list != null) {
/*  88 */       sbuf.append(' ');
/*  89 */       ASTree a = list.left;
/*  90 */       sbuf.append((a == null) ? "<null>" : a.toString());
/*  91 */       list = list.right;
/*     */     } 
/*     */     
/*  94 */     sbuf.append(')');
/*  95 */     return sbuf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/* 102 */     return length(this);
/*     */   }
/*     */   
/*     */   public static int length(ASTList list) {
/* 106 */     if (list == null) {
/* 107 */       return 0;
/*     */     }
/* 109 */     int n = 0;
/* 110 */     while (list != null) {
/* 111 */       list = list.right;
/* 112 */       n++;
/*     */     } 
/*     */     
/* 115 */     return n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ASTList sublist(int nth) {
/* 125 */     ASTList list = this;
/* 126 */     while (nth-- > 0) {
/* 127 */       list = list.right;
/*     */     }
/* 129 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean subst(ASTree newObj, ASTree oldObj) {
/* 137 */     for (ASTList list = this; list != null; list = list.right) {
/* 138 */       if (list.left == oldObj) {
/* 139 */         list.left = newObj;
/* 140 */         return true;
/*     */       } 
/*     */     } 
/* 143 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ASTList append(ASTList a, ASTree b) {
/* 150 */     return concat(a, new ASTList(b));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ASTList concat(ASTList a, ASTList b) {
/* 157 */     if (a == null)
/* 158 */       return b; 
/* 159 */     ASTList list = a;
/* 160 */     while (list.right != null) {
/* 161 */       list = list.right;
/*     */     }
/* 163 */     list.right = b;
/* 164 */     return a;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\compiler\ast\ASTList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */