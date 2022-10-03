/*     */ package org.fife.rsta.ac.js;
/*     */ 
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.List;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.Element;
/*     */ import org.fife.io.DocumentReader;
/*     */ import org.fife.rsta.ac.js.ast.VariableResolver;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
/*     */ import org.fife.ui.rsyntaxtextarea.TextEditorPane;
/*     */ import org.fife.ui.rsyntaxtextarea.parser.AbstractParser;
/*     */ import org.fife.ui.rsyntaxtextarea.parser.DefaultParseResult;
/*     */ import org.fife.ui.rsyntaxtextarea.parser.DefaultParserNotice;
/*     */ import org.fife.ui.rsyntaxtextarea.parser.ParseResult;
/*     */ import org.fife.ui.rsyntaxtextarea.parser.Parser;
/*     */ import org.fife.ui.rsyntaxtextarea.parser.ParserNotice;
/*     */ import org.mozilla.javascript.CompilerEnvirons;
/*     */ import org.mozilla.javascript.ErrorReporter;
/*     */ import org.mozilla.javascript.EvaluatorException;
/*     */ import org.mozilla.javascript.Parser;
/*     */ import org.mozilla.javascript.RhinoException;
/*     */ import org.mozilla.javascript.ast.AstRoot;
/*     */ import org.mozilla.javascript.ast.ErrorCollector;
/*     */ import org.mozilla.javascript.ast.ParseProblem;
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
/*     */ public class JavaScriptParser
/*     */   extends AbstractParser
/*     */ {
/*     */   public static final String PROPERTY_AST = "AST";
/*     */   private RSyntaxTextArea textArea;
/*     */   private AstRoot astRoot;
/*     */   private JavaScriptLanguageSupport langSupport;
/*     */   private PropertyChangeSupport support;
/*     */   private DefaultParseResult result;
/*     */   private VariableResolver variableResolver;
/*     */   
/*     */   public JavaScriptParser(JavaScriptLanguageSupport langSupport, RSyntaxTextArea textArea) {
/*  85 */     this.textArea = textArea;
/*  86 */     this.langSupport = langSupport;
/*  87 */     this.support = new PropertyChangeSupport(this);
/*  88 */     this.result = new DefaultParseResult((Parser)this);
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
/*     */   
/*     */   public void addPropertyChangeListener(String prop, PropertyChangeListener l) {
/* 101 */     this.support.addPropertyChangeListener(prop, l);
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
/*     */   public static CompilerEnvirons createCompilerEnvironment(ErrorReporter errorHandler, JavaScriptLanguageSupport langSupport) {
/* 113 */     CompilerEnvirons env = new CompilerEnvirons();
/* 114 */     env.setErrorReporter(errorHandler);
/* 115 */     env.setIdeMode(true);
/* 116 */     env.setRecordingComments(true);
/* 117 */     env.setRecordingLocalJsDocComments(true);
/* 118 */     env.setRecoverFromErrors(true);
/* 119 */     if (langSupport != null) {
/* 120 */       env.setXmlAvailable(langSupport.isXmlAvailable());
/* 121 */       env.setStrictMode(langSupport.isStrictMode());
/* 122 */       int version = langSupport.getLanguageVersion();
/* 123 */       if (version > 0) {
/* 124 */         Logger.log("[JavaScriptParser]: JS language version set to: " + version);
/* 125 */         env.setLanguageVersion(version);
/*     */       } 
/*     */     } 
/* 128 */     return env;
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
/*     */   
/*     */   private void gatherParserErrorsJsHint(RSyntaxDocument doc) {
/*     */     try {
/* 142 */       JsHinter.parse(this, this.textArea, this.result);
/* 143 */     } catch (IOException ioe) {
/*     */       
/* 145 */       String msg = "Error launching jshint: " + ioe.getMessage();
/* 146 */       this.result.addNotice((ParserNotice)new DefaultParserNotice((Parser)this, msg, 0));
/* 147 */       ioe.printStackTrace();
/*     */     } 
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
/*     */ 
/*     */ 
/*     */   
/*     */   private void gatherParserErrorsRhino(ErrorCollector errorHandler, Element root) {
/* 163 */     List<ParseProblem> errors = errorHandler.getErrors();
/* 164 */     if (errors != null && errors.size() > 0)
/*     */     {
/* 166 */       for (ParseProblem problem : errors) {
/*     */         
/* 168 */         int offs = problem.getFileOffset();
/* 169 */         int len = problem.getLength();
/* 170 */         int line = root.getElementIndex(offs);
/* 171 */         String desc = problem.getMessage();
/* 172 */         DefaultParserNotice notice = new DefaultParserNotice((Parser)this, desc, line, offs, len);
/*     */         
/* 174 */         if (problem.getType() == ParseProblem.Type.Warning) {
/* 175 */           notice.setLevel(ParserNotice.Level.WARNING);
/*     */         }
/* 177 */         this.result.addNotice((ParserNotice)notice);
/*     */       } 
/*     */     }
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
/*     */ 
/*     */   
/*     */   public AstRoot getAstRoot() {
/* 193 */     return this.astRoot;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getJsHintIndent() {
/* 198 */     return this.langSupport.getJsHintIndent();
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getJsHintRCFile(RSyntaxTextArea textArea) {
/* 214 */     if (textArea instanceof TextEditorPane) {
/* 215 */       TextEditorPane tep = (TextEditorPane)textArea;
/* 216 */       File file = new File(tep.getFileFullPath());
/* 217 */       File parent = file.getParentFile();
/* 218 */       while (parent != null) {
/* 219 */         File possibleJsHintRc = new File(parent, ".jshintrc");
/* 220 */         if (possibleJsHintRc.isFile()) {
/* 221 */           return possibleJsHintRc;
/*     */         }
/* 223 */         parent = parent.getParentFile();
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 228 */     return this.langSupport.getDefaultJsHintRCFile();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParseResult parse(RSyntaxDocument doc, String style) {
/* 239 */     this.astRoot = null;
/* 240 */     this.result.clearNotices();
/*     */     
/* 242 */     Element root = doc.getDefaultRootElement();
/* 243 */     int lineCount = root.getElementCount();
/* 244 */     this.result.setParsedLines(0, lineCount - 1);
/*     */     
/* 246 */     DocumentReader r = new DocumentReader((Document)doc);
/* 247 */     ErrorCollector errorHandler = new ErrorCollector();
/* 248 */     CompilerEnvirons env = createCompilerEnvironment((ErrorReporter)errorHandler, this.langSupport);
/* 249 */     long start = System.currentTimeMillis();
/*     */     try {
/* 251 */       Parser parser = new Parser(env);
/* 252 */       this.astRoot = parser.parse((Reader)r, null, 0);
/* 253 */       long time = System.currentTimeMillis() - start;
/* 254 */       this.result.setParseTime(time);
/* 255 */     } catch (IOException ioe) {
/* 256 */       this.result.setError(ioe);
/* 257 */       ioe.printStackTrace();
/* 258 */     } catch (RhinoException re) {
/*     */       
/* 260 */       int line = re.lineNumber();
/*     */       
/* 262 */       Element elem = root.getElement(line);
/* 263 */       int offs = elem.getStartOffset();
/* 264 */       int len = elem.getEndOffset() - offs - 1;
/* 265 */       String msg = re.details();
/* 266 */       this.result.addNotice((ParserNotice)new DefaultParserNotice((Parser)this, msg, line, offs, len));
/*     */     }
/* 268 */     catch (Exception e) {
/* 269 */       this.result.setError(e);
/*     */     } 
/*     */     
/* 272 */     r.close();
/*     */ 
/*     */     
/* 275 */     switch (this.langSupport.getErrorParser())
/*     */     
/*     */     { default:
/* 278 */         gatherParserErrorsRhino(errorHandler, root);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 286 */         this.support.firePropertyChange("AST", (Object)null, this.astRoot);
/*     */         
/* 288 */         return (ParseResult)this.result;case JSHINT: break; }  gatherParserErrorsJsHint(doc); this.support.firePropertyChange("AST", (Object)null, this.astRoot); return (ParseResult)this.result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVariablesAndFunctions(VariableResolver variableResolver) {
/* 293 */     this.variableResolver = variableResolver;
/*     */   }
/*     */   
/*     */   public VariableResolver getVariablesAndFunctions() {
/* 297 */     return this.variableResolver;
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
/*     */   public void removePropertyChangeListener(String prop, PropertyChangeListener l) {
/* 309 */     this.support.removePropertyChangeListener(prop, l);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class JSErrorReporter
/*     */     implements ErrorReporter
/*     */   {
/*     */     public void error(String message, String sourceName, int line, String lineSource, int lineOffset) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource, int lineOffset) {
/* 327 */       return null;
/*     */     }
/*     */     
/*     */     public void warning(String message, String sourceName, int line, String lineSource, int lineOffset) {}
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\JavaScriptParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */