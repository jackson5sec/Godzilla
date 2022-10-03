/*    */ package javassist.bytecode.stackmap;
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
/*    */ public interface TypeTag
/*    */ {
/*    */   public static final String TOP_TYPE = "*top*";
/* 23 */   public static final TypeData.BasicType TOP = new TypeData.BasicType("*top*", 0, ' ');
/* 24 */   public static final TypeData.BasicType INTEGER = new TypeData.BasicType("int", 1, 'I');
/* 25 */   public static final TypeData.BasicType FLOAT = new TypeData.BasicType("float", 2, 'F');
/* 26 */   public static final TypeData.BasicType DOUBLE = new TypeData.BasicType("double", 3, 'D');
/* 27 */   public static final TypeData.BasicType LONG = new TypeData.BasicType("long", 4, 'J');
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\stackmap\TypeTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */