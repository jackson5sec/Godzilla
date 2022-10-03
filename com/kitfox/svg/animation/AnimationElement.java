/*     */ package com.kitfox.svg.animation;
/*     */ 
/*     */ import com.kitfox.svg.SVGElement;
/*     */ import com.kitfox.svg.SVGException;
/*     */ import com.kitfox.svg.SVGLoaderHelper;
/*     */ import com.kitfox.svg.animation.parser.AnimTimeParser;
/*     */ import com.kitfox.svg.animation.parser.ParseException;
/*     */ import com.kitfox.svg.xml.StyleAttribute;
/*     */ import java.io.StringReader;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
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
/*     */ public abstract class AnimationElement
/*     */   extends SVGElement
/*     */ {
/*     */   protected String attribName;
/*  61 */   protected int attribType = 2;
/*     */   
/*     */   public static final int AT_CSS = 0;
/*     */   
/*     */   public static final int AT_XML = 1;
/*     */   public static final int AT_AUTO = 2;
/*     */   private TimeBase beginTime;
/*     */   private TimeBase durTime;
/*     */   private TimeBase endTime;
/*  70 */   private int fillType = 4;
/*     */   
/*     */   public static final int FT_REMOVE = 0;
/*     */   
/*     */   public static final int FT_FREEZE = 1;
/*     */   
/*     */   public static final int FT_HOLD = 2;
/*     */   
/*     */   public static final int FT_TRANSITION = 3;
/*     */   
/*     */   public static final int FT_AUTO = 4;
/*     */   public static final int FT_DEFAULT = 5;
/*     */   public static final int AD_REPLACE = 0;
/*     */   public static final int AD_SUM = 1;
/*  84 */   private int additiveType = 0;
/*     */   
/*     */   public static final int AC_REPLACE = 0;
/*     */   
/*     */   public static final int AC_SUM = 1;
/*     */   
/*  90 */   private int accumulateType = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String animationElementToString(int attrValue) {
/*  99 */     switch (attrValue) {
/*     */       
/*     */       case 0:
/* 102 */         return "CSS";
/*     */       case 1:
/* 104 */         return "XML";
/*     */       case 2:
/* 106 */         return "AUTO";
/*     */     } 
/* 108 */     throw new RuntimeException("Unknown element type");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loaderStartElement(SVGLoaderHelper helper, Attributes attrs, SVGElement parent) throws SAXException {
/* 116 */     super.loaderStartElement(helper, attrs, parent);
/*     */     
/* 118 */     this.attribName = attrs.getValue("attributeName");
/* 119 */     String attribType = attrs.getValue("attributeType");
/* 120 */     if (attribType != null) {
/*     */       
/* 122 */       attribType = attribType.toLowerCase();
/* 123 */       if (attribType.equals("css")) { this.attribType = 0; }
/* 124 */       else if (attribType.equals("xml")) { this.attribType = 1; }
/*     */     
/*     */     } 
/* 127 */     String beginTime = attrs.getValue("begin");
/* 128 */     String durTime = attrs.getValue("dur");
/* 129 */     String endTime = attrs.getValue("end");
/*     */ 
/*     */     
/*     */     try {
/* 133 */       if (beginTime != null) {
/*     */         
/* 135 */         helper.animTimeParser.ReInit(new StringReader(beginTime));
/* 136 */         this.beginTime = helper.animTimeParser.Expr();
/* 137 */         this.beginTime.setParentElement(this);
/*     */       } 
/*     */       
/* 140 */       if (durTime != null) {
/*     */         
/* 142 */         helper.animTimeParser.ReInit(new StringReader(durTime));
/* 143 */         this.durTime = helper.animTimeParser.Expr();
/* 144 */         this.durTime.setParentElement(this);
/*     */       } 
/*     */       
/* 147 */       if (endTime != null)
/*     */       {
/* 149 */         helper.animTimeParser.ReInit(new StringReader(endTime));
/* 150 */         this.endTime = helper.animTimeParser.Expr();
/* 151 */         this.endTime.setParentElement(this);
/*     */       }
/*     */     
/* 154 */     } catch (Exception e) {
/*     */       
/* 156 */       throw new SAXException(e);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 163 */     String fill = attrs.getValue("fill");
/*     */     
/* 165 */     if (fill != null) {
/*     */       
/* 167 */       if (fill.equals("remove")) this.fillType = 0; 
/* 168 */       if (fill.equals("freeze")) this.fillType = 1; 
/* 169 */       if (fill.equals("hold")) this.fillType = 2; 
/* 170 */       if (fill.equals("transiton")) this.fillType = 3; 
/* 171 */       if (fill.equals("auto")) this.fillType = 4; 
/* 172 */       if (fill.equals("default")) this.fillType = 5;
/*     */     
/*     */     } 
/* 175 */     String additiveStrn = attrs.getValue("additive");
/*     */     
/* 177 */     if (additiveStrn != null) {
/*     */       
/* 179 */       if (additiveStrn.equals("replace")) this.additiveType = 0; 
/* 180 */       if (additiveStrn.equals("sum")) this.additiveType = 1;
/*     */     
/*     */     } 
/* 183 */     String accumulateStrn = attrs.getValue("accumulate");
/*     */     
/* 185 */     if (accumulateStrn != null) {
/*     */       
/* 187 */       if (accumulateStrn.equals("replace")) this.accumulateType = 0; 
/* 188 */       if (accumulateStrn.equals("sum")) this.accumulateType = 1; 
/*     */     } 
/*     */   }
/*     */   
/* 192 */   public String getAttribName() { return this.attribName; }
/* 193 */   public int getAttribType() { return this.attribType; }
/* 194 */   public int getAdditiveType() { return this.additiveType; } public int getAccumulateType() {
/* 195 */     return this.accumulateType;
/*     */   }
/*     */   
/*     */   public void evalParametric(AnimationTimeEval state, double curTime) {
/* 199 */     evalParametric(state, curTime, Double.NaN, Double.NaN);
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
/*     */   protected void evalParametric(AnimationTimeEval state, double curTime, double repeatCount, double repeatDur) {
/* 219 */     double repeat, finishTime, begin = (this.beginTime == null) ? 0.0D : this.beginTime.evalTime();
/* 220 */     if (Double.isNaN(begin) || begin > curTime) {
/*     */       
/* 222 */       state.set(Double.NaN, 0);
/*     */       
/*     */       return;
/*     */     } 
/* 226 */     double dur = (this.durTime == null) ? Double.NaN : this.durTime.evalTime();
/* 227 */     if (Double.isNaN(dur)) {
/*     */       
/* 229 */       state.set(Double.NaN, 0);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 234 */     double end = (this.endTime == null) ? Double.NaN : this.endTime.evalTime();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 240 */     if (Double.isNaN(repeatCount) && Double.isNaN(repeatDur)) {
/*     */       
/* 242 */       repeat = Double.NaN;
/*     */     }
/*     */     else {
/*     */       
/* 246 */       repeat = Math.min(
/* 247 */           Double.isNaN(repeatCount) ? Double.POSITIVE_INFINITY : (dur * repeatCount), 
/* 248 */           Double.isNaN(repeatDur) ? Double.POSITIVE_INFINITY : repeatDur);
/*     */     } 
/* 250 */     if (Double.isNaN(repeat) && Double.isNaN(end))
/*     */     {
/*     */ 
/*     */       
/* 254 */       end = begin + dur;
/*     */     }
/*     */ 
/*     */     
/* 258 */     if (Double.isNaN(end)) {
/*     */       
/* 260 */       finishTime = begin + repeat;
/*     */     }
/* 262 */     else if (Double.isNaN(repeat)) {
/*     */       
/* 264 */       finishTime = end;
/*     */     }
/*     */     else {
/*     */       
/* 268 */       finishTime = Math.min(end, repeat);
/*     */     } 
/*     */     
/* 271 */     double evalTime = Math.min(curTime, finishTime);
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
/* 282 */     double ratio = (evalTime - begin) / dur;
/* 283 */     int rep = (int)ratio;
/* 284 */     double interp = ratio - rep;
/*     */ 
/*     */     
/* 287 */     if (interp < 1.0E-5D) interp = 0.0D;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 297 */     if (curTime == evalTime) {
/*     */       
/* 299 */       state.set(interp, rep);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 304 */     switch (this.fillType) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       default:
/* 310 */         state.set(Double.NaN, rep); return;
/*     */       case 1:
/*     */       case 2:
/*     */       case 3:
/*     */         break;
/* 315 */     }  state.set((interp == 0.0D) ? 1.0D : interp, rep);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   double evalStartTime() {
/* 323 */     return (this.beginTime == null) ? Double.NaN : this.beginTime.evalTime();
/*     */   }
/*     */ 
/*     */   
/*     */   double evalDurTime() {
/* 328 */     return (this.durTime == null) ? Double.NaN : this.durTime.evalTime();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   double evalEndTime() {
/* 338 */     return (this.endTime == null) ? Double.NaN : this.endTime.evalTime();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean hasEndTime() {
/* 344 */     return (this.endTime != null);
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
/*     */   public boolean updateTime(double curTime) {
/* 356 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void rebuild() throws SVGException {
/* 361 */     AnimTimeParser animTimeParser = new AnimTimeParser(new StringReader(""));
/*     */     
/* 363 */     rebuild(animTimeParser);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void rebuild(AnimTimeParser animTimeParser) throws SVGException {
/* 368 */     StyleAttribute sty = new StyleAttribute();
/*     */     
/* 370 */     if (getPres(sty.setName("begin"))) {
/*     */       
/* 372 */       String newVal = sty.getStringValue();
/* 373 */       animTimeParser.ReInit(new StringReader(newVal));
/*     */       try {
/* 375 */         this.beginTime = animTimeParser.Expr();
/* 376 */       } catch (ParseException ex) {
/* 377 */         Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, "Could not parse '" + newVal + "'", (Throwable)ex);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 382 */     if (getPres(sty.setName("dur"))) {
/*     */       
/* 384 */       String newVal = sty.getStringValue();
/* 385 */       animTimeParser.ReInit(new StringReader(newVal));
/*     */       try {
/* 387 */         this.durTime = animTimeParser.Expr();
/* 388 */       } catch (ParseException ex) {
/* 389 */         Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, "Could not parse '" + newVal + "'", (Throwable)ex);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 394 */     if (getPres(sty.setName("end"))) {
/*     */       
/* 396 */       String newVal = sty.getStringValue();
/* 397 */       animTimeParser.ReInit(new StringReader(newVal));
/*     */       try {
/* 399 */         this.endTime = animTimeParser.Expr();
/* 400 */       } catch (ParseException ex) {
/* 401 */         Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, "Could not parse '" + newVal + "'", (Throwable)ex);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 406 */     if (getPres(sty.setName("fill"))) {
/*     */       
/* 408 */       String newVal = sty.getStringValue();
/* 409 */       if (newVal.equals("remove")) this.fillType = 0; 
/* 410 */       if (newVal.equals("freeze")) this.fillType = 1; 
/* 411 */       if (newVal.equals("hold")) this.fillType = 2; 
/* 412 */       if (newVal.equals("transiton")) this.fillType = 3; 
/* 413 */       if (newVal.equals("auto")) this.fillType = 4; 
/* 414 */       if (newVal.equals("default")) this.fillType = 5;
/*     */     
/*     */     } 
/* 417 */     if (getPres(sty.setName("additive"))) {
/*     */       
/* 419 */       String newVal = sty.getStringValue();
/* 420 */       if (newVal.equals("replace")) this.additiveType = 0; 
/* 421 */       if (newVal.equals("sum")) this.additiveType = 1;
/*     */     
/*     */     } 
/* 424 */     if (getPres(sty.setName("accumulate"))) {
/*     */       
/* 426 */       String newVal = sty.getStringValue();
/* 427 */       if (newVal.equals("replace")) this.accumulateType = 0; 
/* 428 */       if (newVal.equals("sum")) this.accumulateType = 1;
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TimeBase getBeginTime() {
/* 438 */     return this.beginTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeginTime(TimeBase beginTime) {
/* 446 */     this.beginTime = beginTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TimeBase getDurTime() {
/* 454 */     return this.durTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDurTime(TimeBase durTime) {
/* 462 */     this.durTime = durTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TimeBase getEndTime() {
/* 470 */     return this.endTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEndTime(TimeBase endTime) {
/* 478 */     this.endTime = endTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFillType() {
/* 486 */     return this.fillType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFillType(int fillType) {
/* 494 */     this.fillType = fillType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAdditiveType(int additiveType) {
/* 502 */     this.additiveType = additiveType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAccumulateType(int accumulateType) {
/* 510 */     this.accumulateType = accumulateType;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\animation\AnimationElement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */