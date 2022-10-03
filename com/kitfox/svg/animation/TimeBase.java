/*    */ package com.kitfox.svg.animation;
/*    */ 
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
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
/*    */ public abstract class TimeBase
/*    */ {
/* 52 */   static final Matcher matchIndefinite = Pattern.compile("\\s*indefinite\\s*").matcher("");
/* 53 */   static final Matcher matchUnitTime = Pattern.compile("\\s*([-+]?((\\d*\\.\\d+)|(\\d+))([-+]?[eE]\\d+)?)\\s*(h|min|s|ms)?\\s*").matcher("");
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
/*    */   protected static TimeBase parseTimeComponent(String text) {
/* 71 */     matchIndefinite.reset(text);
/* 72 */     if (matchIndefinite.matches()) return new TimeIndefinite();
/*    */     
/* 74 */     matchUnitTime.reset(text);
/* 75 */     if (matchUnitTime.matches()) {
/*    */       
/* 77 */       String val = matchUnitTime.group(1);
/* 78 */       String units = matchUnitTime.group(6);
/*    */       
/* 80 */       double time = 0.0D; try {
/* 81 */         time = Double.parseDouble(val);
/* 82 */       } catch (Exception exception) {}
/*    */       
/* 84 */       if (units.equals("ms")) { time *= 0.001D; }
/* 85 */       else if (units.equals("min")) { time *= 60.0D; }
/* 86 */       else if (units.equals("h")) { time *= 3600.0D; }
/*    */       
/* 88 */       return new TimeDiscrete(time);
/*    */     } 
/*    */     
/* 91 */     return null;
/*    */   }
/*    */   
/*    */   public abstract double evalTime();
/*    */   
/*    */   public void setParentElement(AnimationElement ele) {}
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\animation\TimeBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */