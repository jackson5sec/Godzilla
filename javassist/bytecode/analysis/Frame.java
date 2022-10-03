/*     */ package javassist.bytecode.analysis;
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
/*     */ public class Frame
/*     */ {
/*     */   private Type[] locals;
/*     */   private Type[] stack;
/*     */   private int top;
/*     */   private boolean jsrMerged;
/*     */   private boolean retMerged;
/*     */   
/*     */   public Frame(int locals, int stack) {
/*  38 */     this.locals = new Type[locals];
/*  39 */     this.stack = new Type[stack];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type getLocal(int index) {
/*  49 */     return this.locals[index];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocal(int index, Type type) {
/*  59 */     this.locals[index] = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type getStack(int index) {
/*  70 */     return this.stack[index];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStack(int index, Type type) {
/*  80 */     this.stack[index] = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearStack() {
/*  87 */     this.top = 0;
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
/*     */   public int getTopIndex() {
/*  99 */     return this.top - 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int localsLength() {
/* 109 */     return this.locals.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type peek() {
/* 118 */     if (this.top < 1) {
/* 119 */       throw new IndexOutOfBoundsException("Stack is empty");
/*     */     }
/* 121 */     return this.stack[this.top - 1];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type pop() {
/* 130 */     if (this.top < 1)
/* 131 */       throw new IndexOutOfBoundsException("Stack is empty"); 
/* 132 */     return this.stack[--this.top];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void push(Type type) {
/* 141 */     this.stack[this.top++] = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Frame copy() {
/* 152 */     Frame frame = new Frame(this.locals.length, this.stack.length);
/* 153 */     System.arraycopy(this.locals, 0, frame.locals, 0, this.locals.length);
/* 154 */     System.arraycopy(this.stack, 0, frame.stack, 0, this.stack.length);
/* 155 */     frame.top = this.top;
/* 156 */     return frame;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Frame copyStack() {
/* 166 */     Frame frame = new Frame(this.locals.length, this.stack.length);
/* 167 */     System.arraycopy(this.stack, 0, frame.stack, 0, this.stack.length);
/* 168 */     frame.top = this.top;
/* 169 */     return frame;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean mergeStack(Frame frame) {
/* 180 */     boolean changed = false;
/* 181 */     if (this.top != frame.top) {
/* 182 */       throw new RuntimeException("Operand stacks could not be merged, they are different sizes!");
/*     */     }
/* 184 */     for (int i = 0; i < this.top; i++) {
/* 185 */       if (this.stack[i] != null) {
/* 186 */         Type prev = this.stack[i];
/* 187 */         Type merged = prev.merge(frame.stack[i]);
/* 188 */         if (merged == Type.BOGUS) {
/* 189 */           throw new RuntimeException("Operand stacks could not be merged due to differing primitive types: pos = " + i);
/*     */         }
/* 191 */         this.stack[i] = merged;
/*     */         
/* 193 */         if (!merged.equals(prev) || merged.popChanged()) {
/* 194 */           changed = true;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 199 */     return changed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean merge(Frame frame) {
/* 210 */     boolean changed = false;
/*     */ 
/*     */     
/* 213 */     for (int i = 0; i < this.locals.length; i++) {
/* 214 */       if (this.locals[i] != null) {
/* 215 */         Type prev = this.locals[i];
/* 216 */         Type merged = prev.merge(frame.locals[i]);
/*     */         
/* 218 */         this.locals[i] = merged;
/* 219 */         if (!merged.equals(prev) || merged.popChanged()) {
/* 220 */           changed = true;
/*     */         }
/* 222 */       } else if (frame.locals[i] != null) {
/* 223 */         this.locals[i] = frame.locals[i];
/* 224 */         changed = true;
/*     */       } 
/*     */     } 
/*     */     
/* 228 */     changed |= mergeStack(frame);
/* 229 */     return changed;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 234 */     StringBuffer buffer = new StringBuffer();
/*     */     
/* 236 */     buffer.append("locals = ["); int i;
/* 237 */     for (i = 0; i < this.locals.length; i++) {
/* 238 */       buffer.append((this.locals[i] == null) ? "empty" : this.locals[i].toString());
/* 239 */       if (i < this.locals.length - 1)
/* 240 */         buffer.append(", "); 
/*     */     } 
/* 242 */     buffer.append("] stack = [");
/* 243 */     for (i = 0; i < this.top; i++) {
/* 244 */       buffer.append(this.stack[i]);
/* 245 */       if (i < this.top - 1)
/* 246 */         buffer.append(", "); 
/*     */     } 
/* 248 */     buffer.append("]");
/*     */     
/* 250 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isJsrMerged() {
/* 259 */     return this.jsrMerged;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setJsrMerged(boolean jsrMerged) {
/* 268 */     this.jsrMerged = jsrMerged;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isRetMerged() {
/* 278 */     return this.retMerged;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setRetMerged(boolean retMerged) {
/* 288 */     this.retMerged = retMerged;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\analysis\Frame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */