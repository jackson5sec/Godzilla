/*     */ package org.fife.ui.autocomplete;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.helpers.DefaultHandler;
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
/*     */ public class CompletionXMLParser
/*     */   extends DefaultHandler
/*     */ {
/*     */   private List<Completion> completions;
/*     */   private CompletionProvider provider;
/*     */   private ClassLoader completionCL;
/*     */   private String name;
/*     */   private String type;
/*     */   private String returnType;
/*     */   private StringBuilder returnValDesc;
/*     */   private StringBuilder desc;
/*     */   private String paramName;
/*     */   private String paramType;
/*     */   private boolean endParam;
/*     */   private StringBuilder paramDesc;
/*     */   private List<ParameterizedCompletion.Parameter> params;
/*     */   private String definedIn;
/*     */   private boolean doingKeywords;
/*     */   private boolean inKeyword;
/*     */   private boolean gettingReturnValDesc;
/*     */   private boolean gettingDesc;
/*     */   private boolean gettingParams;
/*     */   private boolean inParam;
/*     */   private boolean gettingParamDesc;
/*     */   private boolean inCompletionTypes;
/*     */   private char paramStartChar;
/*     */   private char paramEndChar;
/*     */   private String paramSeparator;
/*     */   private String funcCompletionType;
/*     */   private static ClassLoader defaultCompletionClassLoader;
/*     */   
/*     */   public CompletionXMLParser(CompletionProvider provider) {
/*  95 */     this(provider, null);
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
/*     */   public CompletionXMLParser(CompletionProvider provider, ClassLoader cl) {
/* 110 */     this.provider = provider;
/* 111 */     this.completionCL = cl;
/* 112 */     if (this.completionCL == null)
/*     */     {
/* 114 */       this.completionCL = defaultCompletionClassLoader;
/*     */     }
/* 116 */     this.completions = new ArrayList<>();
/* 117 */     this.params = new ArrayList<>(1);
/* 118 */     this.desc = new StringBuilder();
/* 119 */     this.paramDesc = new StringBuilder();
/* 120 */     this.returnValDesc = new StringBuilder();
/* 121 */     this.paramStartChar = this.paramEndChar = Character.MIN_VALUE;
/* 122 */     this.paramSeparator = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void characters(char[] ch, int start, int length) {
/* 131 */     if (this.gettingDesc) {
/* 132 */       this.desc.append(ch, start, length);
/*     */     }
/* 134 */     else if (this.gettingParamDesc) {
/* 135 */       this.paramDesc.append(ch, start, length);
/*     */     }
/* 137 */     else if (this.gettingReturnValDesc) {
/* 138 */       this.returnValDesc.append(ch, start, length);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private FunctionCompletion createFunctionCompletion() {
/* 145 */     FunctionCompletion fc = null;
/* 146 */     if (this.funcCompletionType != null) {
/*     */       try {
/*     */         Class<?> clazz;
/* 149 */         if (this.completionCL != null) {
/* 150 */           clazz = Class.forName(this.funcCompletionType, true, this.completionCL);
/*     */         }
/*     */         else {
/*     */           
/* 154 */           clazz = Class.forName(this.funcCompletionType);
/*     */         } 
/* 156 */         Constructor<?> c = clazz.getDeclaredConstructor(new Class[] { CompletionProvider.class, String.class, String.class });
/*     */         
/* 158 */         fc = (FunctionCompletion)c.newInstance(new Object[] { this.provider, this.name, this.returnType });
/*     */       }
/* 160 */       catch (RuntimeException re) {
/* 161 */         throw re;
/* 162 */       } catch (Exception e) {
/* 163 */         e.printStackTrace();
/*     */       } 
/*     */     }
/*     */     
/* 167 */     if (fc == null) {
/* 168 */       fc = new FunctionCompletion(this.provider, this.name, this.returnType);
/*     */     }
/*     */     
/* 171 */     if (this.desc.length() > 0) {
/* 172 */       fc.setShortDescription(this.desc.toString());
/* 173 */       this.desc.setLength(0);
/*     */     } 
/* 175 */     fc.setParams(this.params);
/* 176 */     fc.setDefinedIn(this.definedIn);
/* 177 */     if (this.returnValDesc.length() > 0) {
/* 178 */       fc.setReturnValueDescription(this.returnValDesc.toString());
/* 179 */       this.returnValDesc.setLength(0);
/*     */     } 
/*     */     
/* 182 */     return fc;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private BasicCompletion createOtherCompletion() {
/* 188 */     BasicCompletion bc = new BasicCompletion(this.provider, this.name);
/* 189 */     if (this.desc.length() > 0) {
/* 190 */       bc.setSummary(this.desc.toString());
/* 191 */       this.desc.setLength(0);
/*     */     } 
/* 193 */     return bc;
/*     */   }
/*     */ 
/*     */   
/*     */   private MarkupTagCompletion createMarkupTagCompletion() {
/* 198 */     MarkupTagCompletion mc = new MarkupTagCompletion(this.provider, this.name);
/*     */     
/* 200 */     if (this.desc.length() > 0) {
/* 201 */       mc.setDescription(this.desc.toString());
/* 202 */       this.desc.setLength(0);
/*     */     } 
/* 204 */     mc.setAttributes(this.params);
/* 205 */     mc.setDefinedIn(this.definedIn);
/* 206 */     return mc;
/*     */   }
/*     */ 
/*     */   
/*     */   private VariableCompletion createVariableCompletion() {
/* 211 */     VariableCompletion vc = new VariableCompletion(this.provider, this.name, this.returnType);
/*     */     
/* 213 */     if (this.desc.length() > 0) {
/* 214 */       vc.setShortDescription(this.desc.toString());
/* 215 */       this.desc.setLength(0);
/*     */     } 
/* 217 */     vc.setDefinedIn(this.definedIn);
/* 218 */     return vc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void endElement(String uri, String localName, String qName) {
/* 228 */     if ("keywords".equals(qName)) {
/* 229 */       this.doingKeywords = false;
/*     */     
/*     */     }
/* 232 */     else if (this.doingKeywords) {
/*     */       
/* 234 */       if ("keyword".equals(qName)) {
/*     */         Completion c;
/* 236 */         if ("function".equals(this.type)) {
/* 237 */           c = createFunctionCompletion();
/*     */         }
/* 239 */         else if ("constant".equals(this.type)) {
/* 240 */           c = createVariableCompletion();
/*     */         }
/* 242 */         else if ("tag".equals(this.type)) {
/* 243 */           c = createMarkupTagCompletion();
/*     */         }
/* 245 */         else if ("other".equals(this.type)) {
/* 246 */           c = createOtherCompletion();
/*     */         } else {
/*     */           
/* 249 */           throw new InternalError("Unexpected type: " + this.type);
/*     */         } 
/* 251 */         this.completions.add(c);
/* 252 */         this.inKeyword = false;
/*     */       }
/* 254 */       else if (this.inKeyword) {
/* 255 */         if ("returnValDesc".equals(qName)) {
/* 256 */           this.gettingReturnValDesc = false;
/*     */         }
/* 258 */         else if (this.gettingParams) {
/* 259 */           if ("params".equals(qName)) {
/* 260 */             this.gettingParams = false;
/*     */           }
/* 262 */           else if ("param".equals(qName)) {
/* 263 */             ParameterizedCompletion.Parameter param = new ParameterizedCompletion.Parameter(this.paramType, this.paramName, this.endParam);
/*     */             
/* 265 */             if (this.paramDesc.length() > 0) {
/* 266 */               param.setDescription(this.paramDesc.toString());
/* 267 */               this.paramDesc.setLength(0);
/*     */             } 
/* 269 */             this.params.add(param);
/* 270 */             this.inParam = false;
/*     */           }
/* 272 */           else if (this.inParam && 
/* 273 */             "desc".equals(qName)) {
/* 274 */             this.gettingParamDesc = false;
/*     */           }
/*     */         
/*     */         }
/* 278 */         else if ("desc".equals(qName)) {
/* 279 */           this.gettingDesc = false;
/*     */         }
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 285 */     else if (this.inCompletionTypes && 
/* 286 */       "completionTypes".equals(qName)) {
/* 287 */       this.inCompletionTypes = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(SAXParseException e) throws SAXException {
/* 296 */     throw e;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Completion> getCompletions() {
/* 305 */     return this.completions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char getParamEndChar() {
/* 315 */     return this.paramEndChar;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getParamSeparator() {
/* 325 */     return this.paramSeparator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char getParamStartChar() {
/* 335 */     return this.paramStartChar;
/*     */   }
/*     */ 
/*     */   
/*     */   private static char getSingleChar(String str) {
/* 340 */     return (str.length() == 1) ? str.charAt(0) : Character.MIN_VALUE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset(CompletionProvider provider) {
/* 350 */     this.provider = provider;
/* 351 */     this.completions.clear();
/* 352 */     this.doingKeywords = this.inKeyword = this.gettingDesc = this.gettingParams = this.inParam = this.gettingParamDesc = false;
/*     */     
/* 354 */     this.paramStartChar = this.paramEndChar = Character.MIN_VALUE;
/* 355 */     this.paramSeparator = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputSource resolveEntity(String publicID, String systemID) throws SAXException {
/* 362 */     return new InputSource(getClass()
/* 363 */         .getResourceAsStream("CompletionXml.dtd"));
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
/*     */   public static void setDefaultCompletionClassLoader(ClassLoader cl) {
/* 377 */     defaultCompletionClassLoader = cl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startElement(String uri, String localName, String qName, Attributes attrs) {
/* 387 */     if ("keywords".equals(qName)) {
/* 388 */       this.doingKeywords = true;
/*     */     }
/* 390 */     else if (this.doingKeywords) {
/* 391 */       if ("keyword".equals(qName)) {
/* 392 */         this.name = attrs.getValue("name");
/* 393 */         this.type = attrs.getValue("type");
/* 394 */         this.returnType = attrs.getValue("returnType");
/* 395 */         this.params.clear();
/* 396 */         this.definedIn = attrs.getValue("definedIn");
/* 397 */         this.inKeyword = true;
/*     */       }
/* 399 */       else if (this.inKeyword) {
/* 400 */         if ("returnValDesc".equals(qName)) {
/* 401 */           this.gettingReturnValDesc = true;
/*     */         }
/* 403 */         else if ("params".equals(qName)) {
/* 404 */           this.gettingParams = true;
/*     */         }
/* 406 */         else if (this.gettingParams) {
/* 407 */           if ("param".equals(qName)) {
/* 408 */             this.paramName = attrs.getValue("name");
/* 409 */             this.paramType = attrs.getValue("type");
/* 410 */             this.endParam = Boolean.parseBoolean(attrs.getValue("endParam"));
/* 411 */             this.inParam = true;
/*     */           } 
/* 413 */           if (this.inParam && 
/* 414 */             "desc".equals(qName)) {
/* 415 */             this.gettingParamDesc = true;
/*     */           
/*     */           }
/*     */         }
/* 419 */         else if ("desc".equals(qName)) {
/* 420 */           this.gettingDesc = true;
/*     */         }
/*     */       
/*     */       } 
/* 424 */     } else if ("environment".equals(qName)) {
/* 425 */       this.paramStartChar = getSingleChar(attrs.getValue("paramStartChar"));
/* 426 */       this.paramEndChar = getSingleChar(attrs.getValue("paramEndChar"));
/* 427 */       this.paramSeparator = attrs.getValue("paramSeparator");
/*     */     
/*     */     }
/* 430 */     else if ("completionTypes".equals(qName)) {
/* 431 */       this.inCompletionTypes = true;
/*     */     }
/* 433 */     else if (this.inCompletionTypes && 
/* 434 */       "functionCompletionType".equals(qName)) {
/* 435 */       this.funcCompletionType = attrs.getValue("type");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void warning(SAXParseException e) throws SAXException {
/* 443 */     throw e;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\autocomplete\CompletionXMLParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */