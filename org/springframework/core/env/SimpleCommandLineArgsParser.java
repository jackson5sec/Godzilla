/*    */ package org.springframework.core.env;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class SimpleCommandLineArgsParser
/*    */ {
/*    */   public CommandLineArgs parse(String... args) {
/* 67 */     CommandLineArgs commandLineArgs = new CommandLineArgs();
/* 68 */     for (String arg : args) {
/* 69 */       if (arg.startsWith("--")) {
/* 70 */         String optionName, optionText = arg.substring(2);
/*    */         
/* 72 */         String optionValue = null;
/* 73 */         int indexOfEqualsSign = optionText.indexOf('=');
/* 74 */         if (indexOfEqualsSign > -1) {
/* 75 */           optionName = optionText.substring(0, indexOfEqualsSign);
/* 76 */           optionValue = optionText.substring(indexOfEqualsSign + 1);
/*    */         } else {
/*    */           
/* 79 */           optionName = optionText;
/*    */         } 
/* 81 */         if (optionName.isEmpty()) {
/* 82 */           throw new IllegalArgumentException("Invalid argument syntax: " + arg);
/*    */         }
/* 84 */         commandLineArgs.addOptionArg(optionName, optionValue);
/*    */       } else {
/*    */         
/* 87 */         commandLineArgs.addNonOptionArg(arg);
/*    */       } 
/*    */     } 
/* 90 */     return commandLineArgs;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\env\SimpleCommandLineArgsParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */