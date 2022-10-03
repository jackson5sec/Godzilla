/*    */ package javassist.bytecode.analysis;
/*    */ 
/*    */ import javassist.bytecode.CodeIterator;
/*    */ import javassist.bytecode.Opcode;
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
/*    */ 
/*    */ public class Util
/*    */   implements Opcode
/*    */ {
/*    */   public static int getJumpTarget(int pos, CodeIterator iter) {
/* 28 */     int opcode = iter.byteAt(pos);
/* 29 */     pos += (opcode == 201 || opcode == 200) ? iter.s32bitAt(pos + 1) : iter.s16bitAt(pos + 1);
/* 30 */     return pos;
/*    */   }
/*    */   
/*    */   public static boolean isJumpInstruction(int opcode) {
/* 34 */     return ((opcode >= 153 && opcode <= 168) || opcode == 198 || opcode == 199 || opcode == 201 || opcode == 200);
/*    */   }
/*    */   
/*    */   public static boolean isGoto(int opcode) {
/* 38 */     return (opcode == 167 || opcode == 200);
/*    */   }
/*    */   
/*    */   public static boolean isJsr(int opcode) {
/* 42 */     return (opcode == 168 || opcode == 201);
/*    */   }
/*    */   
/*    */   public static boolean isReturn(int opcode) {
/* 46 */     return (opcode >= 172 && opcode <= 177);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\analysis\Util.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */