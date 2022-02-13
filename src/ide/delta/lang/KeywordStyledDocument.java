/*
 * Copyright 2018 - 2022 AP-Studios
 * Copyright 2022 Delta-Studios
 * Copyright 2022 Anonymus_12321
 */

package ide.delta.lang;

import java.util.ArrayList;
import java.util.List;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;

public class KeywordStyledDocument extends DefaultStyledDocument  {
    private static final long serialVersionUID = 1L;
    private Style _defaultStyle;
    private Style _cwStyle;

    public KeywordStyledDocument(Style defaultStyle, Style cwStyle) {
        _defaultStyle =  defaultStyle;
        _cwStyle = cwStyle;
    }
    
    public void remove (int offs, int len) throws BadLocationException {
        super.remove(offs, len);
        refreshDocument();
    }

     public void insertString (int offset, String str, AttributeSet a) throws BadLocationException {
         super.insertString(offset, str, a);
         refreshDocument();
     }

     private synchronized void refreshDocument() throws BadLocationException {
         String text = getText(0, getLength());
         final List<HWord> list = processWords(text);
         setCharacterAttributes(0, text.length(), _defaultStyle, true);   
         for(HWord word : list) {
             int p0 = word._position;
             setCharacterAttributes(p0, word._word.length(), _cwStyle, true);
         }
     }       

     private static List<HWord> processWords(String content) {
         content += " ";
         List<HWord> hiliteWords = new ArrayList<HWord>();
         int lastWhitespacePosition = 0;
         String word = "";
         char[] data = content.toCharArray();

         for(int index=0; index < data.length; index++) {
             char ch = data[index];
             if(!(Character.isLetter(ch) || Character.isDigit(ch) || ch == '_')) {
                 lastWhitespacePosition = index;
                 if(word.length() > 0) {
                     if(isReservedWord(word)) {
                         hiliteWords.add(new HWord(word,(lastWhitespacePosition - word.length())));
                     }
                     word="";
                 }
             }
             else {
                 word += ch;
             }
        }
        return hiliteWords;
     }

     private static final boolean isReservedWord(String word) {
    	 //System.out.println(word);
         return(word.toUpperCase().trim().equals("USE") || 
                        word.toUpperCase().trim().equals("DEF") ||
                        word.toUpperCase().trim().equals("END"));
    }
}