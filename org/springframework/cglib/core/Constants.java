/*    */ package org.springframework.cglib.core;
/*    */ 
/*    */ import org.springframework.asm.Opcodes;
/*    */ import org.springframework.asm.Type;
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
/*    */ public interface Constants
/*    */   extends Opcodes
/*    */ {
/* 27 */   public static final int ASM_API = AsmApi.value();
/*    */   
/* 29 */   public static final Class[] EMPTY_CLASS_ARRAY = new Class[0];
/* 30 */   public static final Type[] TYPES_EMPTY = new Type[0];
/*    */ 
/*    */   
/* 33 */   public static final Signature SIG_STATIC = TypeUtils.parseSignature("void <clinit>()");
/*    */   
/* 35 */   public static final Type TYPE_OBJECT_ARRAY = TypeUtils.parseType("Object[]");
/* 36 */   public static final Type TYPE_CLASS_ARRAY = TypeUtils.parseType("Class[]");
/* 37 */   public static final Type TYPE_STRING_ARRAY = TypeUtils.parseType("String[]");
/*    */   
/* 39 */   public static final Type TYPE_OBJECT = TypeUtils.parseType("Object");
/* 40 */   public static final Type TYPE_CLASS = TypeUtils.parseType("Class");
/* 41 */   public static final Type TYPE_CLASS_LOADER = TypeUtils.parseType("ClassLoader");
/* 42 */   public static final Type TYPE_CHARACTER = TypeUtils.parseType("Character");
/* 43 */   public static final Type TYPE_BOOLEAN = TypeUtils.parseType("Boolean");
/* 44 */   public static final Type TYPE_DOUBLE = TypeUtils.parseType("Double");
/* 45 */   public static final Type TYPE_FLOAT = TypeUtils.parseType("Float");
/* 46 */   public static final Type TYPE_LONG = TypeUtils.parseType("Long");
/* 47 */   public static final Type TYPE_INTEGER = TypeUtils.parseType("Integer");
/* 48 */   public static final Type TYPE_SHORT = TypeUtils.parseType("Short");
/* 49 */   public static final Type TYPE_BYTE = TypeUtils.parseType("Byte");
/* 50 */   public static final Type TYPE_NUMBER = TypeUtils.parseType("Number");
/* 51 */   public static final Type TYPE_STRING = TypeUtils.parseType("String");
/* 52 */   public static final Type TYPE_THROWABLE = TypeUtils.parseType("Throwable");
/* 53 */   public static final Type TYPE_BIG_INTEGER = TypeUtils.parseType("java.math.BigInteger");
/* 54 */   public static final Type TYPE_BIG_DECIMAL = TypeUtils.parseType("java.math.BigDecimal");
/* 55 */   public static final Type TYPE_STRING_BUFFER = TypeUtils.parseType("StringBuffer");
/* 56 */   public static final Type TYPE_RUNTIME_EXCEPTION = TypeUtils.parseType("RuntimeException");
/* 57 */   public static final Type TYPE_ERROR = TypeUtils.parseType("Error");
/* 58 */   public static final Type TYPE_SYSTEM = TypeUtils.parseType("System");
/* 59 */   public static final Type TYPE_SIGNATURE = TypeUtils.parseType("org.springframework.cglib.core.Signature");
/* 60 */   public static final Type TYPE_TYPE = Type.getType(Type.class);
/*    */   public static final String CONSTRUCTOR_NAME = "<init>";
/*    */   public static final String STATIC_NAME = "<clinit>";
/*    */   public static final String SOURCE_FILE = "<generated>";
/*    */   public static final String SUID_FIELD_NAME = "serialVersionUID";
/*    */   public static final int PRIVATE_FINAL_STATIC = 26;
/*    */   public static final int SWITCH_STYLE_TRIE = 0;
/*    */   public static final int SWITCH_STYLE_HASH = 1;
/*    */   public static final int SWITCH_STYLE_HASHONLY = 2;
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\core\Constants.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */