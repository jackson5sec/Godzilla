package org.fife.rsta.ac.css;

import java.util.List;
import org.fife.ui.autocomplete.Completion;
import org.fife.ui.autocomplete.CompletionProvider;

public interface CompletionGenerator {
  List<Completion> generate(CompletionProvider paramCompletionProvider, String paramString);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\css\CompletionGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */