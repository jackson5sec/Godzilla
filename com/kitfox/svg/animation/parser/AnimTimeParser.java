/*     */ package com.kitfox.svg.animation.parser;
/*     */ import com.kitfox.svg.animation.TimeBase;
/*     */ import com.kitfox.svg.animation.TimeCompound;
/*     */ import com.kitfox.svg.animation.TimeDiscrete;
/*     */ import com.kitfox.svg.animation.TimeIndefinite;
/*     */ import com.kitfox.svg.animation.TimeLookup;
/*     */ import com.kitfox.svg.animation.TimeSum;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ public class AnimTimeParser implements AnimTimeParserTreeConstants, AnimTimeParserConstants {
/*  18 */   protected JJTAnimTimeParserState jjtree = new JJTAnimTimeParserState(); public AnimTimeParserTokenManager token_source; SimpleCharStream jj_input_stream; public Token token; public Token jj_nt;
/*     */   private int jj_ntk;
/*     */   private Token jj_scanpos;
/*     */   private Token jj_lastpos;
/*     */   private int jj_la;
/*     */   private int jj_gen;
/*     */   
/*     */   public static void main(String[] args) throws ParseException {
/*  26 */     StringReader reader = new StringReader("1:30 + 5ms");
/*  27 */     AnimTimeParser parser = new AnimTimeParser(reader);
/*     */ 
/*     */     
/*  30 */     TimeBase tc = parser.Expr();
/*  31 */     System.err.println("AnimTimeParser eval to " + tc.evalTime());
/*     */     
/*  33 */     reader = new StringReader("19");
/*  34 */     parser.ReInit(reader);
/*  35 */     tc = parser.Expr();
/*  36 */     System.err.println("AnimTimeParser eval to " + tc.evalTime());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final TimeBase Expr() throws ParseException {
/*  45 */     ASTExpr jjtn000 = new ASTExpr(0);
/*  46 */     boolean jjtc000 = true;
/*  47 */     this.jjtree.openNodeScope(jjtn000);
/*  48 */     ArrayList<TimeBase> list = new ArrayList(); try {
/*     */       TimeBase term;
/*  50 */       switch ((this.jj_ntk == -1) ? jj_ntk_f() : this.jj_ntk) {
/*     */         case 8:
/*     */         case 9:
/*     */         case 10:
/*     */         case 11:
/*     */         case 12:
/*     */         case 14:
/*  57 */           term = Sum();
/*  58 */           list.add(term);
/*     */           break;
/*     */         
/*     */         default:
/*  62 */           this.jj_la1[0] = this.jj_gen;
/*     */           break;
/*     */       } 
/*     */ 
/*     */       
/*  67 */       while (jj_2_1(2)) {
/*     */ 
/*     */ 
/*     */         
/*  71 */         jj_consume_token(15);
/*  72 */         term = Sum();
/*  73 */         list.add(term);
/*     */       } 
/*  75 */       switch ((this.jj_ntk == -1) ? jj_ntk_f() : this.jj_ntk) {
/*     */         case 15:
/*  77 */           jj_consume_token(15);
/*     */           break;
/*     */         
/*     */         default:
/*  81 */           this.jj_la1[1] = this.jj_gen;
/*     */           break;
/*     */       } 
/*  84 */       this.jjtree.closeNodeScope(jjtn000, true);
/*  85 */       jjtc000 = false;
/*  86 */       switch (list.size()) {
/*     */         
/*     */         case 0:
/*  89 */           if ("" != null) return (TimeBase)new TimeIndefinite(); 
/*     */         case 1:
/*  91 */           if ("" != null) return list.get(0);  break;
/*     */       } 
/*  93 */       if ("" != null) return (TimeBase)new TimeCompound(list);
/*     */     
/*  95 */     } catch (Throwable jjte000) {
/*  96 */       if (jjtc000) {
/*  97 */         this.jjtree.clearNodeScope(jjtn000);
/*  98 */         jjtc000 = false;
/*     */       } else {
/* 100 */         this.jjtree.popNode();
/*     */       } 
/* 102 */       if (jjte000 instanceof ParseException) {
/* 103 */         throw (ParseException)jjte000;
/*     */       }
/* 105 */       if (jjte000 instanceof RuntimeException) {
/* 106 */         throw (RuntimeException)jjte000;
/*     */       }
/* 108 */       throw (Error)jjte000;
/*     */     } finally {
/* 110 */       if (jjtc000) {
/* 111 */         this.jjtree.closeNodeScope(jjtn000, true);
/*     */       }
/*     */     } 
/* 114 */     throw new IllegalStateException("Missing return statement in function");
/*     */   }
/*     */   
/*     */   public final TimeBase Sum() throws ParseException {
/* 118 */     ASTSum jjtn000 = new ASTSum(1);
/* 119 */     boolean jjtc000 = true;
/* 120 */     this.jjtree.openNodeScope(jjtn000); Token t = null;
/*     */     
/* 122 */     TimeBase t2 = null;
/*     */     try {
/* 124 */       TimeBase t1 = Term();
/* 125 */       switch ((this.jj_ntk == -1) ? jj_ntk_f() : this.jj_ntk) {
/*     */         case 16:
/*     */         case 17:
/* 128 */           switch ((this.jj_ntk == -1) ? jj_ntk_f() : this.jj_ntk) {
/*     */             case 16:
/* 130 */               t = jj_consume_token(16);
/*     */               break;
/*     */             
/*     */             case 17:
/* 134 */               t = jj_consume_token(17);
/*     */               break;
/*     */             
/*     */             default:
/* 138 */               this.jj_la1[2] = this.jj_gen;
/* 139 */               jj_consume_token(-1);
/* 140 */               throw new ParseException();
/*     */           } 
/* 142 */           t2 = Term();
/*     */           break;
/*     */         
/*     */         default:
/* 146 */           this.jj_la1[3] = this.jj_gen;
/*     */           break;
/*     */       } 
/* 149 */       this.jjtree.closeNodeScope(jjtn000, true);
/* 150 */       jjtc000 = false;
/* 151 */       if (t2 == null && "" != null) return t1;
/*     */       
/* 153 */       if (t.image.equals("-"))
/*     */       
/* 155 */       { if ("" != null) return (TimeBase)new TimeSum(t1, t2, false);
/*     */         
/*     */          }
/*     */       
/* 159 */       else if ("" != null) { return (TimeBase)new TimeSum(t1, t2, true); }
/*     */     
/* 161 */     } catch (Throwable jjte000) {
/* 162 */       if (jjtc000) {
/* 163 */         this.jjtree.clearNodeScope(jjtn000);
/* 164 */         jjtc000 = false;
/*     */       } else {
/* 166 */         this.jjtree.popNode();
/*     */       } 
/* 168 */       if (jjte000 instanceof ParseException) {
/* 169 */         throw (ParseException)jjte000;
/*     */       }
/* 171 */       if (jjte000 instanceof RuntimeException) {
/* 172 */         throw (RuntimeException)jjte000;
/*     */       }
/* 174 */       throw (Error)jjte000;
/*     */     } finally {
/* 176 */       if (jjtc000) {
/* 177 */         this.jjtree.closeNodeScope(jjtn000, true);
/*     */       }
/*     */     } 
/* 180 */     throw new IllegalStateException("Missing return statement in function");
/*     */   }
/*     */   
/*     */   public final TimeBase Term() throws ParseException {
/* 184 */     ASTTerm jjtn000 = new ASTTerm(2);
/* 185 */     boolean jjtc000 = true;
/* 186 */     this.jjtree.openNodeScope(jjtn000); try {
/*     */       TimeIndefinite timeIndefinite; TimeDiscrete timeDiscrete2; TimeLookup timeLookup; TimeDiscrete timeDiscrete1;
/* 188 */       switch ((this.jj_ntk == -1) ? jj_ntk_f() : this.jj_ntk) {
/*     */         case 10:
/* 190 */           timeIndefinite = IndefiniteTime();
/* 191 */           this.jjtree.closeNodeScope(jjtn000, true);
/* 192 */           jjtc000 = false;
/* 193 */           if ("" != null) return (TimeBase)timeIndefinite;
/*     */           
/*     */           break;
/*     */         case 8:
/*     */         case 9:
/* 198 */           timeDiscrete2 = LiteralTime();
/* 199 */           this.jjtree.closeNodeScope(jjtn000, true);
/* 200 */           jjtc000 = false;
/* 201 */           if ("" != null) return (TimeBase)timeDiscrete2;
/*     */           
/*     */           break;
/*     */         case 14:
/* 205 */           timeLookup = LookupTime();
/* 206 */           this.jjtree.closeNodeScope(jjtn000, true);
/* 207 */           jjtc000 = false;
/* 208 */           if ("" != null) return (TimeBase)timeLookup;
/*     */           
/*     */           break;
/*     */         case 11:
/*     */         case 12:
/* 213 */           timeDiscrete1 = EventTime();
/* 214 */           this.jjtree.closeNodeScope(jjtn000, true);
/* 215 */           jjtc000 = false;
/* 216 */           if ("" != null) return (TimeBase)timeDiscrete1;
/*     */           
/*     */           break;
/*     */         default:
/* 220 */           this.jj_la1[4] = this.jj_gen;
/* 221 */           jj_consume_token(-1);
/* 222 */           throw new ParseException();
/*     */       } 
/* 224 */     } catch (Throwable jjte000) {
/* 225 */       if (jjtc000) {
/* 226 */         this.jjtree.clearNodeScope(jjtn000);
/* 227 */         jjtc000 = false;
/*     */       } else {
/* 229 */         this.jjtree.popNode();
/*     */       } 
/* 231 */       if (jjte000 instanceof ParseException) {
/* 232 */         throw (ParseException)jjte000;
/*     */       }
/* 234 */       if (jjte000 instanceof RuntimeException) {
/* 235 */         throw (RuntimeException)jjte000;
/*     */       }
/* 237 */       throw (Error)jjte000;
/*     */     } finally {
/* 239 */       if (jjtc000) {
/* 240 */         this.jjtree.closeNodeScope(jjtn000, true);
/*     */       }
/*     */     } 
/* 243 */     throw new IllegalStateException("Missing return statement in function");
/*     */   }
/*     */   
/*     */   public final TimeIndefinite IndefiniteTime() throws ParseException {
/* 247 */     ASTIndefiniteTime jjtn000 = new ASTIndefiniteTime(3);
/* 248 */     boolean jjtc000 = true;
/* 249 */     this.jjtree.openNodeScope(jjtn000);
/*     */     try {
/* 251 */       jj_consume_token(10);
/* 252 */       this.jjtree.closeNodeScope(jjtn000, true);
/* 253 */       jjtc000 = false;
/* 254 */       if ("" != null) return new TimeIndefinite(); 
/*     */     } finally {
/* 256 */       if (jjtc000) {
/* 257 */         this.jjtree.closeNodeScope(jjtn000, true);
/*     */       }
/*     */     } 
/* 260 */     throw new IllegalStateException("Missing return statement in function");
/*     */   }
/*     */   
/*     */   public final TimeDiscrete EventTime() throws ParseException {
/* 264 */     ASTEventTime jjtn000 = new ASTEventTime(4);
/* 265 */     boolean jjtc000 = true;
/* 266 */     this.jjtree.openNodeScope(jjtn000);
/*     */     try {
/* 268 */       switch ((this.jj_ntk == -1) ? jj_ntk_f() : this.jj_ntk) {
/*     */         case 11:
/* 270 */           jj_consume_token(11);
/*     */           break;
/*     */         
/*     */         case 12:
/* 274 */           jj_consume_token(12);
/*     */           break;
/*     */         
/*     */         default:
/* 278 */           this.jj_la1[5] = this.jj_gen;
/* 279 */           jj_consume_token(-1);
/* 280 */           throw new ParseException();
/*     */       } 
/* 282 */       this.jjtree.closeNodeScope(jjtn000, true);
/* 283 */       jjtc000 = false;
/*     */       
/* 285 */       if ("" != null) return new TimeDiscrete(0.0D); 
/*     */     } finally {
/* 287 */       if (jjtc000) {
/* 288 */         this.jjtree.closeNodeScope(jjtn000, true);
/*     */       }
/*     */     } 
/* 291 */     throw new IllegalStateException("Missing return statement in function");
/*     */   }
/*     */   
/*     */   public final TimeDiscrete LiteralTime() throws ParseException {
/* 295 */     ASTLiteralTime jjtn000 = new ASTLiteralTime(5);
/* 296 */     boolean jjtc000 = true;
/* 297 */     this.jjtree.openNodeScope(jjtn000); double t3 = Double.NaN; try {
/*     */       double t2;
/*     */       Token t;
/* 300 */       double t1 = Number();
/* 301 */       double value = t1;
/* 302 */       switch ((this.jj_ntk == -1) ? jj_ntk_f() : this.jj_ntk) {
/*     */         case 13:
/*     */         case 18:
/* 305 */           switch ((this.jj_ntk == -1) ? jj_ntk_f() : this.jj_ntk) {
/*     */             case 18:
/* 307 */               jj_consume_token(18);
/* 308 */               t2 = Number();
/* 309 */               switch ((this.jj_ntk == -1) ? jj_ntk_f() : this.jj_ntk) {
/*     */                 case 18:
/* 311 */                   jj_consume_token(18);
/* 312 */                   t3 = Number();
/*     */                   break;
/*     */                 
/*     */                 default:
/* 316 */                   this.jj_la1[6] = this.jj_gen;
/*     */                   break;
/*     */               } 
/*     */               
/* 320 */               if (Double.isNaN(t3)) {
/*     */                 
/* 322 */                 value = t1 * 60.0D + t2;
/*     */                 
/*     */                 break;
/*     */               } 
/* 326 */               value = t1 * 3600.0D + t2 * 60.0D + t3;
/*     */               break;
/*     */ 
/*     */             
/*     */             case 13:
/* 331 */               t = jj_consume_token(13);
/*     */               
/* 333 */               if (t.image.equals("ms")) value = t1 / 1000.0D; 
/* 334 */               if (t.image.equals("min")) value = t1 * 60.0D; 
/* 335 */               if (t.image.equals("h")) value = t1 * 3600.0D;
/*     */               
/*     */               break;
/*     */           } 
/* 339 */           this.jj_la1[7] = this.jj_gen;
/* 340 */           jj_consume_token(-1);
/* 341 */           throw new ParseException();
/*     */ 
/*     */ 
/*     */         
/*     */         default:
/* 346 */           this.jj_la1[8] = this.jj_gen;
/*     */           break;
/*     */       } 
/* 349 */       this.jjtree.closeNodeScope(jjtn000, true);
/* 350 */       jjtc000 = false;
/* 351 */       if ("" != null) return new TimeDiscrete(value); 
/* 352 */     } catch (Throwable jjte000) {
/* 353 */       if (jjtc000) {
/* 354 */         this.jjtree.clearNodeScope(jjtn000);
/* 355 */         jjtc000 = false;
/*     */       } else {
/* 357 */         this.jjtree.popNode();
/*     */       } 
/* 359 */       if (jjte000 instanceof ParseException) {
/* 360 */         throw (ParseException)jjte000;
/*     */       }
/* 362 */       if (jjte000 instanceof RuntimeException) {
/* 363 */         throw (RuntimeException)jjte000;
/*     */       }
/* 365 */       throw (Error)jjte000;
/*     */     } finally {
/* 367 */       if (jjtc000) {
/* 368 */         this.jjtree.closeNodeScope(jjtn000, true);
/*     */       }
/*     */     } 
/* 371 */     throw new IllegalStateException("Missing return statement in function");
/*     */   }
/*     */   
/*     */   public final TimeLookup LookupTime() throws ParseException {
/* 375 */     ASTLookupTime jjtn000 = new ASTLookupTime(6);
/* 376 */     boolean jjtc000 = true;
/* 377 */     this.jjtree.openNodeScope(jjtn000); double paramNum = 0.0D;
/*     */     
/*     */     try {
/* 380 */       Token node = jj_consume_token(14);
/* 381 */       jj_consume_token(19);
/* 382 */       Token event = jj_consume_token(14);
/* 383 */       switch ((this.jj_ntk == -1) ? jj_ntk_f() : this.jj_ntk) {
/*     */         case 20:
/* 385 */           paramNum = ParamList();
/*     */           break;
/*     */         
/*     */         default:
/* 389 */           this.jj_la1[9] = this.jj_gen;
/*     */           break;
/*     */       } 
/* 392 */       this.jjtree.closeNodeScope(jjtn000, true);
/* 393 */       jjtc000 = false;
/* 394 */       if ("" != null) return new TimeLookup(null, node.image, event.image, "" + paramNum); 
/* 395 */     } catch (Throwable jjte000) {
/* 396 */       if (jjtc000) {
/* 397 */         this.jjtree.clearNodeScope(jjtn000);
/* 398 */         jjtc000 = false;
/*     */       } else {
/* 400 */         this.jjtree.popNode();
/*     */       } 
/* 402 */       if (jjte000 instanceof ParseException) {
/* 403 */         throw (ParseException)jjte000;
/*     */       }
/* 405 */       if (jjte000 instanceof RuntimeException) {
/* 406 */         throw (RuntimeException)jjte000;
/*     */       }
/* 408 */       throw (Error)jjte000;
/*     */     } finally {
/* 410 */       if (jjtc000) {
/* 411 */         this.jjtree.closeNodeScope(jjtn000, true);
/*     */       }
/*     */     } 
/* 414 */     throw new IllegalStateException("Missing return statement in function");
/*     */   }
/*     */   
/*     */   public final double ParamList() throws ParseException {
/* 418 */     ASTParamList jjtn000 = new ASTParamList(7);
/* 419 */     boolean jjtc000 = true;
/* 420 */     this.jjtree.openNodeScope(jjtn000);
/*     */     try {
/* 422 */       jj_consume_token(20);
/* 423 */       double num = Number();
/* 424 */       jj_consume_token(21);
/* 425 */       this.jjtree.closeNodeScope(jjtn000, true);
/* 426 */       jjtc000 = false;
/* 427 */       if ("" != null) return num; 
/* 428 */     } catch (Throwable jjte000) {
/* 429 */       if (jjtc000) {
/* 430 */         this.jjtree.clearNodeScope(jjtn000);
/* 431 */         jjtc000 = false;
/*     */       } else {
/* 433 */         this.jjtree.popNode();
/*     */       } 
/* 435 */       if (jjte000 instanceof ParseException) {
/* 436 */         throw (ParseException)jjte000;
/*     */       }
/* 438 */       if (jjte000 instanceof RuntimeException) {
/* 439 */         throw (RuntimeException)jjte000;
/*     */       }
/* 441 */       throw (Error)jjte000;
/*     */     } finally {
/* 443 */       if (jjtc000) {
/* 444 */         this.jjtree.closeNodeScope(jjtn000, true);
/*     */       }
/*     */     } 
/* 447 */     throw new IllegalStateException("Missing return statement in function");
/*     */   }
/*     */   
/*     */   public final double Number() throws ParseException {
/* 451 */     ASTNumber jjtn000 = new ASTNumber(8);
/* 452 */     boolean jjtc000 = true;
/* 453 */     this.jjtree.openNodeScope(jjtn000); 
/*     */     try { Token t;
/* 455 */       switch ((this.jj_ntk == -1) ? jj_ntk_f() : this.jj_ntk) {
/*     */         case 9:
/* 457 */           t = jj_consume_token(9);
/* 458 */           this.jjtree.closeNodeScope(jjtn000, true);
/* 459 */           jjtc000 = false;
/*     */           try {
/* 461 */             if ("" != null) return Double.parseDouble(t.image);
/*     */           
/* 463 */           } catch (Exception e) {
/* 464 */             Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, "Could not parse double '" + t.image + "'", e);
/*     */           } 
/*     */ 
/*     */           
/* 468 */           if ("" != null) return 0.0D;
/*     */           
/*     */           break;
/*     */         case 8:
/* 472 */           t = jj_consume_token(8);
/* 473 */           this.jjtree.closeNodeScope(jjtn000, true);
/* 474 */           jjtc000 = false;
/*     */           try {
/* 476 */             if ("" != null) return Double.parseDouble(t.image);
/*     */           
/* 478 */           } catch (Exception e) {
/* 479 */             Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, "Could not parse double '" + t.image + "'", e);
/*     */           } 
/*     */ 
/*     */           
/* 483 */           if ("" != null) return 0.0D;
/*     */           
/*     */           break;
/*     */         default:
/* 487 */           this.jj_la1[10] = this.jj_gen;
/* 488 */           jj_consume_token(-1);
/* 489 */           throw new ParseException();
/*     */       } 
/*     */       
/* 492 */       if (jjtc000)
/* 493 */         this.jjtree.closeNodeScope(jjtn000, true);  } finally { if (jjtc000) this.jjtree.closeNodeScope(jjtn000, true);
/*     */        }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public final int Integer() throws ParseException {
/* 500 */     ASTInteger jjtn000 = new ASTInteger(9);
/* 501 */     boolean jjtc000 = true;
/* 502 */     this.jjtree.openNodeScope(jjtn000);
/*     */     
/* 504 */     try { Token t = jj_consume_token(8);
/* 505 */       this.jjtree.closeNodeScope(jjtn000, true);
/* 506 */       jjtc000 = false;
/*     */       try {
/* 508 */         if ("" != null) return Integer.parseInt(t.image);
/*     */       
/* 510 */       } catch (Exception e) {
/* 511 */         Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, "Could not parse int '" + t.image + "'", e);
/*     */       } 
/*     */ 
/*     */       
/* 515 */       if ("" != null) return 0;
/*     */       
/* 517 */       if (jjtc000)
/* 518 */         this.jjtree.closeNodeScope(jjtn000, true);  } finally { if (jjtc000) this.jjtree.closeNodeScope(jjtn000, true);
/*     */        }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean jj_2_1(int xla) {
/* 526 */     this.jj_la = xla;
/* 527 */     this.jj_scanpos = this.token;
/* 528 */     this.jj_lastpos = this.token; 
/* 529 */     try { return !jj_3_1(); }
/* 530 */     catch (LookaheadSuccess ls) { return true; }
/* 531 */     finally { jj_save(0, xla); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean jj_3R_3() {
/* 537 */     Token xsp = this.jj_scanpos;
/* 538 */     if (jj_3R_4()) {
/* 539 */       this.jj_scanpos = xsp;
/* 540 */       if (jj_3R_5()) {
/* 541 */         this.jj_scanpos = xsp;
/* 542 */         if (jj_3R_6()) {
/* 543 */           this.jj_scanpos = xsp;
/* 544 */           if (jj_3R_7()) return true; 
/*     */         } 
/*     */       } 
/*     */     } 
/* 548 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean jj_3R_4() {
/* 553 */     if (jj_3R_8()) return true; 
/* 554 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean jj_3R_9() {
/* 559 */     if (jj_3R_12()) return true; 
/* 560 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean jj_3R_5() {
/* 565 */     if (jj_3R_9()) return true; 
/* 566 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean jj_3R_12() {
/* 572 */     Token xsp = this.jj_scanpos;
/* 573 */     if (jj_3R_13()) {
/* 574 */       this.jj_scanpos = xsp;
/* 575 */       if (jj_3R_14()) return true; 
/*     */     } 
/* 577 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean jj_3R_13() {
/* 582 */     if (jj_scan_token(9)) return true; 
/* 583 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean jj_3R_6() {
/* 588 */     if (jj_3R_10()) return true; 
/* 589 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean jj_3R_7() {
/* 594 */     if (jj_3R_11()) return true; 
/* 595 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean jj_3R_2() {
/* 600 */     if (jj_3R_3()) return true; 
/* 601 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean jj_3R_8() {
/* 606 */     if (jj_scan_token(10)) return true; 
/* 607 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean jj_3R_10() {
/* 612 */     if (jj_scan_token(14)) return true; 
/* 613 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean jj_3_1() {
/* 618 */     if (jj_scan_token(15)) return true; 
/* 619 */     if (jj_3R_2()) return true; 
/* 620 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean jj_3R_14() {
/* 625 */     if (jj_scan_token(8)) return true; 
/* 626 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean jj_3R_11() {
/* 632 */     Token xsp = this.jj_scanpos;
/* 633 */     if (jj_scan_token(11)) {
/* 634 */       this.jj_scanpos = xsp;
/* 635 */       if (jj_scan_token(12)) return true; 
/*     */     } 
/* 637 */     return false;
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
/* 651 */   private final int[] jj_la1 = new int[11]; private static int[] jj_la1_0;
/*     */   
/*     */   static {
/* 654 */     jj_la1_init_0();
/*     */   }
/*     */   private static void jj_la1_init_0() {
/* 657 */     jj_la1_0 = new int[] { 24320, 32768, 196608, 196608, 24320, 6144, 262144, 270336, 270336, 1048576, 768 };
/*     */   }
/* 659 */   private final JJCalls[] jj_2_rtns = new JJCalls[1];
/*     */   private boolean jj_rescan = false;
/* 661 */   private int jj_gc = 0; private final LookaheadSuccess jj_ls; private List<int[]> jj_expentries;
/*     */   private int[] jj_expentry;
/*     */   private int jj_kind;
/*     */   private int[] jj_lasttokens;
/*     */   private int jj_endpos;
/*     */   
/*     */   public AnimTimeParser(InputStream stream) {
/* 668 */     this(stream, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void ReInit(InputStream stream) {
/* 691 */     ReInit(stream, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void ReInit(InputStream stream, Charset encoding) {
/* 699 */     this.jj_input_stream.reInit(stream, encoding, 1, 1);
/* 700 */     this.token_source.ReInit(this.jj_input_stream);
/* 701 */     this.token = new Token();
/* 702 */     this.jj_ntk = -1;
/* 703 */     this.jj_gen = 0; int i;
/* 704 */     for (i = 0; i < 11; ) { this.jj_la1[i] = -1; i++; }
/* 705 */      for (i = 0; i < this.jj_2_rtns.length; ) { this.jj_2_rtns[i] = new JJCalls(); i++; }
/*     */   
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void ReInit(Reader stream) {
/* 729 */     if (this.jj_input_stream == null) {
/* 730 */       this.jj_input_stream = new SimpleCharStream(stream, 1, 1);
/*     */     } else {
/* 732 */       this.jj_input_stream.reInit(stream, 1, 1);
/*     */     } 
/* 734 */     if (this.token_source == null) {
/* 735 */       this.token_source = new AnimTimeParserTokenManager(this.jj_input_stream);
/*     */     }
/*     */     
/* 738 */     this.token_source.ReInit(this.jj_input_stream);
/* 739 */     this.token = new Token();
/* 740 */     this.jj_ntk = -1;
/* 741 */     this.jj_gen = 0; int i;
/* 742 */     for (i = 0; i < 11; i++)
/* 743 */       this.jj_la1[i] = -1; 
/* 744 */     for (i = 0; i < this.jj_2_rtns.length; i++) {
/* 745 */       this.jj_2_rtns[i] = new JJCalls();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void ReInit(AnimTimeParserTokenManager tm) {
/* 766 */     this.token_source = tm;
/* 767 */     this.token = new Token();
/* 768 */     this.jj_ntk = -1;
/* 769 */     this.jj_gen = 0; int i;
/* 770 */     for (i = 0; i < 11; ) { this.jj_la1[i] = -1; i++; }
/* 771 */      for (i = 0; i < this.jj_2_rtns.length; ) { this.jj_2_rtns[i] = new JJCalls(); i++; }
/*     */   
/*     */   }
/*     */   private Token jj_consume_token(int kind) throws ParseException {
/* 775 */     Token oldToken = this.token;
/* 776 */     if (this.token.next != null) {
/* 777 */       this.token = this.token.next;
/*     */     } else {
/* 779 */       this.token.next = this.token_source.getNextToken();
/* 780 */       this.token = this.token.next;
/*     */     } 
/* 782 */     this.jj_ntk = -1;
/* 783 */     if (this.token.kind == kind) {
/* 784 */       this.jj_gen++;
/* 785 */       if (++this.jj_gc > 100) {
/* 786 */         this.jj_gc = 0;
/* 787 */         for (int i = 0; i < this.jj_2_rtns.length; i++) {
/* 788 */           JJCalls c = this.jj_2_rtns[i];
/* 789 */           while (c != null) {
/* 790 */             if (c.gen < this.jj_gen)
/* 791 */               c.first = null; 
/* 792 */             c = c.next;
/*     */           } 
/*     */         } 
/*     */       } 
/* 796 */       return this.token;
/*     */     } 
/* 798 */     this.token = oldToken;
/* 799 */     this.jj_kind = kind;
/* 800 */     throw generateParseException();
/*     */   }
/*     */   private static final class LookaheadSuccess extends IllegalStateException {
/*     */     private LookaheadSuccess() {} }
/*     */   
/* 805 */   public AnimTimeParser(InputStream stream, Charset encoding) { this.jj_ls = new LookaheadSuccess();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 866 */     this.jj_expentries = (List)new ArrayList<int>();
/*     */     
/* 868 */     this.jj_kind = -1;
/* 869 */     this.jj_lasttokens = new int[100]; this.jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); this.token_source = new AnimTimeParserTokenManager(this.jj_input_stream); this.token = new Token(); this.jj_ntk = -1; this.jj_gen = 0; int i; for (i = 0; i < 11; ) { this.jj_la1[i] = -1; i++; }  for (i = 0; i < this.jj_2_rtns.length; ) { this.jj_2_rtns[i] = new JJCalls(); i++; }  } public AnimTimeParser(Reader stream) { this.jj_ls = new LookaheadSuccess(); this.jj_expentries = (List)new ArrayList<int>(); this.jj_kind = -1; this.jj_lasttokens = new int[100]; this.jj_input_stream = new SimpleCharStream(stream, 1, 1); this.token_source = new AnimTimeParserTokenManager(this.jj_input_stream); this.token = new Token(); this.jj_ntk = -1; this.jj_gen = 0; int i; for (i = 0; i < 11; i++) this.jj_la1[i] = -1;  for (i = 0; i < this.jj_2_rtns.length; i++) this.jj_2_rtns[i] = new JJCalls();  } public AnimTimeParser(AnimTimeParserTokenManager tm) { this.jj_ls = new LookaheadSuccess(); this.jj_expentries = (List)new ArrayList<int>(); this.jj_kind = -1; this.jj_lasttokens = new int[100]; this.token_source = tm; this.token = new Token(); this.jj_ntk = -1; this.jj_gen = 0; int i; for (i = 0; i < 11; ) { this.jj_la1[i] = -1; i++; }  for (i = 0; i < this.jj_2_rtns.length; ) { this.jj_2_rtns[i] = new JJCalls(); i++; }  }
/*     */   private boolean jj_scan_token(int kind) { if (this.jj_scanpos == this.jj_lastpos) { this.jj_la--; if (this.jj_scanpos.next == null) { this.jj_lastpos = this.jj_scanpos = this.jj_scanpos.next = this.token_source.getNextToken(); } else { this.jj_lastpos = this.jj_scanpos = this.jj_scanpos.next; }  } else { this.jj_scanpos = this.jj_scanpos.next; }  if (this.jj_rescan) { int i = 0; Token tok = this.token; while (tok != null && tok != this.jj_scanpos) { i++; tok = tok.next; }  if (tok != null)
/*     */         jj_add_error_token(kind, i);  }  if (this.jj_scanpos.kind != kind)
/*     */       return true;  if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos)
/* 873 */       throw this.jj_ls;  return false; } private void jj_add_error_token(int kind, int pos) { if (pos >= 100) {
/*     */       return;
/*     */     }
/*     */     
/* 877 */     if (pos == this.jj_endpos + 1) {
/* 878 */       this.jj_lasttokens[this.jj_endpos++] = kind;
/* 879 */     } else if (this.jj_endpos != 0) {
/* 880 */       this.jj_expentry = new int[this.jj_endpos];
/*     */       
/* 882 */       for (int i = 0; i < this.jj_endpos; i++) {
/* 883 */         this.jj_expentry[i] = this.jj_lasttokens[i];
/*     */       }
/*     */       
/* 886 */       for (int[] oldentry : this.jj_expentries) {
/* 887 */         if (oldentry.length == this.jj_expentry.length) {
/* 888 */           boolean isMatched = true;
/* 889 */           for (int j = 0; j < this.jj_expentry.length; j++) {
/* 890 */             if (oldentry[j] != this.jj_expentry[j]) {
/* 891 */               isMatched = false;
/*     */               break;
/*     */             } 
/*     */           } 
/* 895 */           if (isMatched) {
/* 896 */             this.jj_expentries.add(this.jj_expentry);
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/* 902 */       if (pos != 0) {
/* 903 */         this.jj_endpos = pos;
/* 904 */         this.jj_lasttokens[this.jj_endpos - 1] = kind;
/*     */       } 
/*     */     }  }
/*     */   public final Token getNextToken() { if (this.token.next != null) { this.token = this.token.next; }
/*     */     else { this.token = this.token.next = this.token_source.getNextToken(); }
/*     */      this.jj_ntk = -1; this.jj_gen++; return this.token; }
/*     */   public final Token getToken(int index) { Token t = this.token; for (int i = 0; i < index; i++) { if (t.next == null)
/*     */         t.next = this.token_source.getNextToken();  t = t.next; }
/*     */      return t; }
/*     */   private int jj_ntk_f() { this.jj_nt = this.token.next; if (this.jj_nt == null) { this.token.next = this.token_source.getNextToken(); this.jj_ntk = this.token.next.kind; return this.jj_ntk; }
/* 914 */      this.jj_ntk = this.jj_nt.kind; return this.jj_ntk; } public ParseException generateParseException() { this.jj_expentries.clear();
/* 915 */     boolean[] la1tokens = new boolean[22];
/* 916 */     if (this.jj_kind >= 0) {
/* 917 */       la1tokens[this.jj_kind] = true;
/* 918 */       this.jj_kind = -1;
/*     */     }  int i;
/* 920 */     for (i = 0; i < 11; i++) {
/* 921 */       if (this.jj_la1[i] == this.jj_gen) {
/* 922 */         for (int k = 0; k < 32; k++) {
/* 923 */           if ((jj_la1_0[i] & 1 << k) != 0) {
/* 924 */             la1tokens[k] = true;
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/* 929 */     for (i = 0; i < 22; i++) {
/* 930 */       if (la1tokens[i]) {
/* 931 */         this.jj_expentry = new int[1];
/* 932 */         this.jj_expentry[0] = i;
/* 933 */         this.jj_expentries.add(this.jj_expentry);
/*     */       } 
/*     */     } 
/* 936 */     this.jj_endpos = 0;
/* 937 */     jj_rescan_token();
/* 938 */     jj_add_error_token(0, 0);
/* 939 */     int[][] exptokseq = new int[this.jj_expentries.size()][];
/* 940 */     for (int j = 0; j < this.jj_expentries.size(); j++) {
/* 941 */       exptokseq[j] = this.jj_expentries.get(j);
/*     */     }
/* 943 */     return new ParseException(this.token, exptokseq, tokenImage); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean trace_enabled() {
/* 950 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void enable_tracing() {}
/*     */ 
/*     */   
/*     */   public final void disable_tracing() {}
/*     */   
/*     */   private void jj_rescan_token() {
/* 960 */     this.jj_rescan = true;
/* 961 */     for (int i = 0; i < 1; i++) {
/*     */       try {
/* 963 */         JJCalls p = this.jj_2_rtns[i];
/*     */         do {
/* 965 */           if (p.gen > this.jj_gen) {
/* 966 */             this.jj_la = p.arg;
/* 967 */             this.jj_scanpos = p.first;
/* 968 */             this.jj_lastpos = p.first;
/* 969 */             switch (i) { case 0:
/* 970 */                 jj_3_1(); break; }
/*     */           
/*     */           } 
/* 973 */           p = p.next;
/* 974 */         } while (p != null);
/* 975 */       } catch (LookaheadSuccess lookaheadSuccess) {}
/*     */     } 
/* 977 */     this.jj_rescan = false;
/*     */   }
/*     */   
/*     */   private void jj_save(int index, int xla) {
/* 981 */     JJCalls p = this.jj_2_rtns[index];
/* 982 */     while (p.gen > this.jj_gen) {
/* 983 */       if (p.next == null) {
/* 984 */         p.next = new JJCalls();
/* 985 */         p = p.next;
/*     */         break;
/*     */       } 
/* 988 */       p = p.next;
/*     */     } 
/* 990 */     p.gen = this.jj_gen + xla - this.jj_la;
/* 991 */     p.first = this.token;
/* 992 */     p.arg = xla;
/*     */   }
/*     */   
/*     */   static final class JJCalls {
/*     */     int gen;
/*     */     Token first;
/*     */     int arg;
/*     */     JJCalls next;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\animation\parser\AnimTimeParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */