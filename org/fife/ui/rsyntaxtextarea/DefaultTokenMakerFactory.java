/*    */ package org.fife.ui.rsyntaxtextarea;
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
/*    */ class DefaultTokenMakerFactory
/*    */   extends AbstractTokenMakerFactory
/*    */   implements SyntaxConstants
/*    */ {
/*    */   protected void initTokenMakerMap() {
/* 27 */     String pkg = "org.fife.ui.rsyntaxtextarea.modes.";
/*    */     
/* 29 */     putMapping("text/plain", pkg + "PlainTextTokenMaker");
/* 30 */     putMapping("text/actionscript", pkg + "ActionScriptTokenMaker");
/* 31 */     putMapping("text/asm", pkg + "AssemblerX86TokenMaker");
/* 32 */     putMapping("text/asm6502", pkg + "Assembler6502TokenMaker");
/* 33 */     putMapping("text/bbcode", pkg + "BBCodeTokenMaker");
/* 34 */     putMapping("text/c", pkg + "CTokenMaker");
/* 35 */     putMapping("text/clojure", pkg + "ClojureTokenMaker");
/* 36 */     putMapping("text/cpp", pkg + "CPlusPlusTokenMaker");
/* 37 */     putMapping("text/cs", pkg + "CSharpTokenMaker");
/* 38 */     putMapping("text/css", pkg + "CSSTokenMaker");
/* 39 */     putMapping("text/csv", pkg + "CsvTokenMaker");
/* 40 */     putMapping("text/d", pkg + "DTokenMaker");
/* 41 */     putMapping("text/dart", pkg + "DartTokenMaker");
/* 42 */     putMapping("text/delphi", pkg + "DelphiTokenMaker");
/* 43 */     putMapping("text/dockerfile", pkg + "DockerTokenMaker");
/* 44 */     putMapping("text/dtd", pkg + "DtdTokenMaker");
/* 45 */     putMapping("text/fortran", pkg + "FortranTokenMaker");
/* 46 */     putMapping("text/golang", pkg + "GoTokenMaker");
/* 47 */     putMapping("text/groovy", pkg + "GroovyTokenMaker");
/* 48 */     putMapping("text/hosts", pkg + "HostsTokenMaker");
/* 49 */     putMapping("text/htaccess", pkg + "HtaccessTokenMaker");
/* 50 */     putMapping("text/html", pkg + "HTMLTokenMaker");
/* 51 */     putMapping("text/ini", pkg + "IniTokenMaker");
/* 52 */     putMapping("text/java", pkg + "JavaTokenMaker");
/* 53 */     putMapping("text/javascript", pkg + "JavaScriptTokenMaker");
/* 54 */     putMapping("text/jshintrc", pkg + "JshintrcTokenMaker");
/* 55 */     putMapping("text/json", pkg + "JsonTokenMaker");
/* 56 */     putMapping("text/jsp", pkg + "JSPTokenMaker");
/* 57 */     putMapping("text/kotlin", pkg + "KotlinTokenMaker");
/* 58 */     putMapping("text/latex", pkg + "LatexTokenMaker");
/* 59 */     putMapping("text/less", pkg + "LessTokenMaker");
/* 60 */     putMapping("text/lisp", pkg + "LispTokenMaker");
/* 61 */     putMapping("text/lua", pkg + "LuaTokenMaker");
/* 62 */     putMapping("text/makefile", pkg + "MakefileTokenMaker");
/* 63 */     putMapping("text/markdown", pkg + "MarkdownTokenMaker");
/* 64 */     putMapping("text/mxml", pkg + "MxmlTokenMaker");
/* 65 */     putMapping("text/nsis", pkg + "NSISTokenMaker");
/* 66 */     putMapping("text/perl", pkg + "PerlTokenMaker");
/* 67 */     putMapping("text/php", pkg + "PHPTokenMaker");
/* 68 */     putMapping("text/properties", pkg + "PropertiesFileTokenMaker");
/* 69 */     putMapping("text/python", pkg + "PythonTokenMaker");
/* 70 */     putMapping("text/ruby", pkg + "RubyTokenMaker");
/* 71 */     putMapping("text/sas", pkg + "SASTokenMaker");
/* 72 */     putMapping("text/scala", pkg + "ScalaTokenMaker");
/* 73 */     putMapping("text/sql", pkg + "SQLTokenMaker");
/* 74 */     putMapping("text/tcl", pkg + "TclTokenMaker");
/* 75 */     putMapping("text/typescript", pkg + "TypeScriptTokenMaker");
/* 76 */     putMapping("text/unix", pkg + "UnixShellTokenMaker");
/* 77 */     putMapping("text/vb", pkg + "VisualBasicTokenMaker");
/* 78 */     putMapping("text/bat", pkg + "WindowsBatchTokenMaker");
/* 79 */     putMapping("text/xml", pkg + "XMLTokenMaker");
/* 80 */     putMapping("text/yaml", pkg + "YamlTokenMaker");
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\DefaultTokenMakerFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */