/*    */ package com.jediterm.terminal;
/*    */ 
/*    */ import com.jediterm.terminal.ui.AbstractTabbedTerminalWidget;
/*    */ import com.jediterm.terminal.ui.AbstractTabs;
/*    */ import com.jediterm.terminal.ui.JediTermWidget;
/*    */ import com.jediterm.terminal.ui.TerminalTabsImpl;
/*    */ import com.jediterm.terminal.ui.settings.SettingsProvider;
/*    */ import com.jediterm.terminal.ui.settings.TabbedSettingsProvider;
/*    */ import java.util.function.Function;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ public class TabbedTerminalWidget extends AbstractTabbedTerminalWidget<JediTermWidget> {
/*    */   public TabbedTerminalWidget(@NotNull TabbedSettingsProvider settingsProvider, @NotNull Function<AbstractTabbedTerminalWidget, JediTermWidget> createNewSessionAction) {
/* 14 */     super(settingsProvider, createNewSessionAction::apply);
/*    */   }
/*    */ 
/*    */   
/*    */   public JediTermWidget createInnerTerminalWidget() {
/* 19 */     return new JediTermWidget((SettingsProvider)getSettingsProvider());
/*    */   }
/*    */ 
/*    */   
/*    */   protected AbstractTabs<JediTermWidget> createTabbedPane() {
/* 24 */     return (AbstractTabs<JediTermWidget>)new TerminalTabsImpl();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\TabbedTerminalWidget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */