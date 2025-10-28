package basicjavaedittor;

import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.BadLocationException;
import javax.swing.text.AttributeSet;
import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Juan-pinto
 */
public class JavaSyntaxDocument extends DefaultStyledDocument {
    
  
     private Style keywordStyle;
    private Style normalStyle;
    
    private static final String[] KEYWORDS = {
        "abstract", "assert", "boolean", "break", "byte", "case", "catch", 
        "char", "class", "const", "continue", "default", "do", "double", 
        "else", "enum", "extends", "final", "finally", "float", "for", 
        "goto", "if", "implements", "import", "instanceof", "int", "interface", 
        "long", "native", "new", "package", "private", "protected", "public", 
        "return", "short", "static", "strictfp", "super", "switch", 
        "synchronized", "this", "throw", "throws", "transient", "try", 
        "void", "volatile", "while"
    };
      private Pattern keywordPattern;
     
    public JavaSyntaxDocument() {
        StyleContext context = new StyleContext();
        
        // Keyword style (blue and bold)
        keywordStyle = context.addStyle("Keyword", null);
        StyleConstants.setForeground(keywordStyle, Color.BLUE);
        StyleConstants.setBold(keywordStyle, true);
        
        // Normal style (black)
        normalStyle = context.addStyle("Normal", null);
        StyleConstants.setForeground(normalStyle, Color.BLACK);
        
        // Build regex pattern for keywords
        StringBuilder sb = new StringBuilder("\\b(");
        for (int i = 0; i < KEYWORDS.length; i++) {
            sb.append(KEYWORDS[i]);
            if (i < KEYWORDS.length - 1) {
                sb.append("|");
            }
        }
        sb.append(")\\b");
        keywordPattern = Pattern.compile(sb.toString());
    }
    
      @Override
    public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
        super.insertString(offset, str, a);
        processDocument();
    }
    
    @Override
    public void remove(int offs, int len) throws BadLocationException {
        super.remove(offs, len);
        processDocument();
    }
    
     private void processDocument() {
         
        try {
            String text = getText(0, getLength());
            
            // First, reset all text to normal style
            setCharacterAttributes(0, text.length(), normalStyle, true);
            
            // Find and highlight keywords
            Matcher matcher = keywordPattern.matcher(text);
            while (matcher.find()) {
                setCharacterAttributes(matcher.start(), matcher.end() - matcher.start(), 
                                     keywordStyle, false);
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
}
}