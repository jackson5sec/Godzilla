package com.jediterm.terminal.model.hyperlinks;

import org.jetbrains.annotations.Nullable;

public interface HyperlinkFilter {
  @Nullable
  LinkResult apply(String paramString);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\model\hyperlinks\HyperlinkFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */