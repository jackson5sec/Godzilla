/*    */ package javassist.convert;
/*    */ 
/*    */ import javassist.CtMethod;
/*    */ import javassist.NotFoundException;
/*    */ import javassist.bytecode.BadBytecode;
/*    */ import javassist.bytecode.CodeIterator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TransformAfter
/*    */   extends TransformBefore
/*    */ {
/*    */   public TransformAfter(Transformer next, CtMethod origMethod, CtMethod afterMethod) throws NotFoundException {
/* 29 */     super(next, origMethod, afterMethod);
/*    */   }
/*    */ 
/*    */   
/*    */   protected int match2(int pos, CodeIterator iterator) throws BadBytecode {
/* 34 */     iterator.move(pos);
/* 35 */     iterator.insert(this.saveCode);
/* 36 */     iterator.insert(this.loadCode);
/* 37 */     int p = iterator.insertGap(3);
/* 38 */     iterator.setMark(p);
/* 39 */     iterator.insert(this.loadCode);
/* 40 */     pos = iterator.next();
/* 41 */     p = iterator.getMark();
/* 42 */     iterator.writeByte(iterator.byteAt(pos), p);
/* 43 */     iterator.write16bit(iterator.u16bitAt(pos + 1), p + 1);
/* 44 */     iterator.writeByte(184, pos);
/* 45 */     iterator.write16bit(this.newIndex, pos + 1);
/* 46 */     iterator.move(p);
/* 47 */     return iterator.next();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\convert\TransformAfter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */