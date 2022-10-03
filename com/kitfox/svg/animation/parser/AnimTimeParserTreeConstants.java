/*    */ package com.kitfox.svg.animation.parser;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface AnimTimeParserTreeConstants
/*    */ {
/*    */   public static final int JJTEXPR = 0;
/*    */   public static final int JJTSUM = 1;
/*    */   public static final int JJTTERM = 2;
/*    */   public static final int JJTINDEFINITETIME = 3;
/*    */   public static final int JJTEVENTTIME = 4;
/*    */   public static final int JJTLITERALTIME = 5;
/*    */   public static final int JJTLOOKUPTIME = 6;
/*    */   public static final int JJTPARAMLIST = 7;
/*    */   public static final int JJTNUMBER = 8;
/*    */   public static final int JJTINTEGER = 9;
/* 18 */   public static final String[] jjtNodeName = new String[] { "Expr", "Sum", "Term", "IndefiniteTime", "EventTime", "LiteralTime", "LookupTime", "ParamList", "Number", "Integer" };
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\animation\parser\AnimTimeParserTreeConstants.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */