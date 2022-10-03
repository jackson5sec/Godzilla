/*     */ package javassist.expr;
/*     */ 
/*     */ import javassist.CannotCompileException;
/*     */ import javassist.CtClass;
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.CodeAttribute;
/*     */ import javassist.bytecode.CodeIterator;
/*     */ import javassist.bytecode.ExceptionTable;
/*     */ import javassist.bytecode.MethodInfo;
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
/*     */ public class ExprEditor
/*     */ {
/*     */   public boolean doit(CtClass clazz, MethodInfo minfo) throws CannotCompileException {
/*  87 */     CodeAttribute codeAttr = minfo.getCodeAttribute();
/*  88 */     if (codeAttr == null) {
/*  89 */       return false;
/*     */     }
/*  91 */     CodeIterator iterator = codeAttr.iterator();
/*  92 */     boolean edited = false;
/*  93 */     LoopContext context = new LoopContext(codeAttr.getMaxLocals());
/*     */     
/*  95 */     while (iterator.hasNext()) {
/*  96 */       if (loopBody(iterator, clazz, minfo, context))
/*  97 */         edited = true; 
/*     */     } 
/*  99 */     ExceptionTable et = codeAttr.getExceptionTable();
/* 100 */     int n = et.size();
/* 101 */     for (int i = 0; i < n; i++) {
/* 102 */       Handler h = new Handler(et, i, iterator, clazz, minfo);
/* 103 */       edit(h);
/* 104 */       if (h.edited()) {
/* 105 */         edited = true;
/* 106 */         context.updateMax(h.locals(), h.stack());
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 112 */     if (codeAttr.getMaxLocals() < context.maxLocals) {
/* 113 */       codeAttr.setMaxLocals(context.maxLocals);
/*     */     }
/* 115 */     codeAttr.setMaxStack(codeAttr.getMaxStack() + context.maxStack);
/*     */     try {
/* 117 */       if (edited) {
/* 118 */         minfo.rebuildStackMapIf6(clazz.getClassPool(), clazz
/* 119 */             .getClassFile2());
/*     */       }
/* 121 */     } catch (BadBytecode b) {
/* 122 */       throw new CannotCompileException(b.getMessage(), b);
/*     */     } 
/*     */     
/* 125 */     return edited;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean doit(CtClass clazz, MethodInfo minfo, LoopContext context, CodeIterator iterator, int endPos) throws CannotCompileException {
/* 135 */     boolean edited = false;
/* 136 */     while (iterator.hasNext() && iterator.lookAhead() < endPos) {
/* 137 */       int size = iterator.getCodeLength();
/* 138 */       if (loopBody(iterator, clazz, minfo, context)) {
/* 139 */         edited = true;
/* 140 */         int size2 = iterator.getCodeLength();
/* 141 */         if (size != size2) {
/* 142 */           endPos += size2 - size;
/*     */         }
/*     */       } 
/*     */     } 
/* 146 */     return edited;
/*     */   }
/*     */   
/*     */   static final class NewOp {
/*     */     NewOp next;
/*     */     int pos;
/*     */     String type;
/*     */     
/*     */     NewOp(NewOp n, int p, String t) {
/* 155 */       this.next = n;
/* 156 */       this.pos = p;
/* 157 */       this.type = t;
/*     */     }
/*     */   }
/*     */   
/*     */   static final class LoopContext {
/*     */     ExprEditor.NewOp newList;
/*     */     int maxLocals;
/*     */     int maxStack;
/*     */     
/*     */     LoopContext(int locals) {
/* 167 */       this.maxLocals = locals;
/* 168 */       this.maxStack = 0;
/* 169 */       this.newList = null;
/*     */     }
/*     */     
/*     */     void updateMax(int locals, int stack) {
/* 173 */       if (this.maxLocals < locals) {
/* 174 */         this.maxLocals = locals;
/*     */       }
/* 176 */       if (this.maxStack < stack) {
/* 177 */         this.maxStack = stack;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   final boolean loopBody(CodeIterator iterator, CtClass clazz, MethodInfo minfo, LoopContext context) throws CannotCompileException {
/*     */     try {
/* 186 */       Expr expr = null;
/* 187 */       int pos = iterator.next();
/* 188 */       int c = iterator.byteAt(pos);
/*     */       
/* 190 */       if (c >= 178)
/*     */       {
/* 192 */         if (c < 188) {
/* 193 */           if (c == 184 || c == 185 || c == 182) {
/*     */ 
/*     */             
/* 196 */             expr = new MethodCall(pos, iterator, clazz, minfo);
/* 197 */             edit((MethodCall)expr);
/*     */           }
/* 199 */           else if (c == 180 || c == 178 || c == 181 || c == 179) {
/*     */ 
/*     */             
/* 202 */             expr = new FieldAccess(pos, iterator, clazz, minfo, c);
/* 203 */             edit((FieldAccess)expr);
/*     */           }
/* 205 */           else if (c == 187) {
/* 206 */             int index = iterator.u16bitAt(pos + 1);
/* 207 */             context
/* 208 */               .newList = new NewOp(context.newList, pos, minfo.getConstPool().getClassInfo(index));
/*     */           }
/* 210 */           else if (c == 183) {
/* 211 */             NewOp newList = context.newList;
/* 212 */             if (newList != null && minfo
/* 213 */               .getConstPool().isConstructor(newList.type, iterator
/* 214 */                 .u16bitAt(pos + 1)) > 0) {
/* 215 */               expr = new NewExpr(pos, iterator, clazz, minfo, newList.type, newList.pos);
/*     */               
/* 217 */               edit((NewExpr)expr);
/* 218 */               context.newList = newList.next;
/*     */             } else {
/*     */               
/* 221 */               MethodCall mcall = new MethodCall(pos, iterator, clazz, minfo);
/* 222 */               if (mcall.getMethodName().equals("<init>")) {
/* 223 */                 ConstructorCall ccall = new ConstructorCall(pos, iterator, clazz, minfo);
/* 224 */                 expr = ccall;
/* 225 */                 edit(ccall);
/*     */               } else {
/*     */                 
/* 228 */                 expr = mcall;
/* 229 */                 edit(mcall);
/*     */               }
/*     */             
/*     */             }
/*     */           
/*     */           } 
/* 235 */         } else if (c == 188 || c == 189 || c == 197) {
/*     */           
/* 237 */           expr = new NewArray(pos, iterator, clazz, minfo, c);
/* 238 */           edit((NewArray)expr);
/*     */         }
/* 240 */         else if (c == 193) {
/* 241 */           expr = new Instanceof(pos, iterator, clazz, minfo);
/* 242 */           edit((Instanceof)expr);
/*     */         }
/* 244 */         else if (c == 192) {
/* 245 */           expr = new Cast(pos, iterator, clazz, minfo);
/* 246 */           edit((Cast)expr);
/*     */         } 
/*     */       }
/*     */       
/* 250 */       if (expr != null && expr.edited()) {
/* 251 */         context.updateMax(expr.locals(), expr.stack());
/* 252 */         return true;
/*     */       } 
/* 254 */       return false;
/*     */     }
/* 256 */     catch (BadBytecode e) {
/* 257 */       throw new CannotCompileException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void edit(NewExpr e) throws CannotCompileException {}
/*     */   
/*     */   public void edit(NewArray a) throws CannotCompileException {}
/*     */   
/*     */   public void edit(MethodCall m) throws CannotCompileException {}
/*     */   
/*     */   public void edit(ConstructorCall c) throws CannotCompileException {}
/*     */   
/*     */   public void edit(FieldAccess f) throws CannotCompileException {}
/*     */   
/*     */   public void edit(Instanceof i) throws CannotCompileException {}
/*     */   
/*     */   public void edit(Cast c) throws CannotCompileException {}
/*     */   
/*     */   public void edit(Handler h) throws CannotCompileException {}
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\expr\ExprEditor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */