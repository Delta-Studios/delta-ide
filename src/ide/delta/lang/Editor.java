/*
 * Copyright 2018 - 2022 AP-Studios
 * Copyright 2022 Delta-Studios
 * Copyright 2022 Anonymus_12321
 */

package ide.delta.lang;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.Color;

@SuppressWarnings("serial")
public final class Editor extends JFrame implements ActionListener {
	JTextPane area = new JTextPane();
    public static JFrame frmDeltaIde;
    private static int returnValue = 0;
    public Editor() { 
    	frmDeltaIde = new JFrame("Delta IDE - New");
    	
    	StyleContext styleContext = new StyleContext();
    	
        Style defaultStyle = styleContext.getStyle(StyleContext.DEFAULT_STYLE);
        Style cwStyle = styleContext.addStyle("ConstantWidth", null);
        
        StyleConstants.setForeground(cwStyle, Color.RED);
        StyleConstants.setBold(cwStyle, true);
        StyleConstants.setFontSize(cwStyle, 18);
        StyleConstants.setFontFamily(cwStyle, "Consolas");
        
        StyleConstants.setForeground(defaultStyle, Color.WHITE);
        StyleConstants.setBold(defaultStyle, true);
        StyleConstants.setFontSize(defaultStyle, 18);
        StyleConstants.setFontFamily(defaultStyle, "Consolas");
        
        JMenuBar menu_main = new JMenuBar();    
        JMenu menu_file = new JMenu("File");    
        JMenuItem menuitem_new = new JMenuItem("New");
        JMenuItem menuitem_open = new JMenuItem("Open");
        JMenuItem menuitem_save = new JMenuItem("Save");
        JMenuItem menuitem_quit = new JMenuItem("Quit");
        menuitem_new.addActionListener(this);
        menuitem_open.addActionListener(this);
        menuitem_save.addActionListener(this);
        menuitem_quit.addActionListener(this);  
        menu_main.add(menu_file);   
        menu_file.add(menuitem_new);
        menu_file.add(menuitem_open);
        menu_file.add(menuitem_save);
        menu_file.add(menuitem_quit);
        
        
        area = new JTextPane(new KeywordStyledDocument(defaultStyle, cwStyle));
        area.setBackground(Color.BLACK);
        area.setCaretColor(Color.GREEN);
        //area.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        
        JScrollPane scrollPane = new JScrollPane(area);
        frmDeltaIde.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frmDeltaIde.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmDeltaIde.setSize(640, 480);
        frmDeltaIde.setJMenuBar(menu_main);
        frmDeltaIde.setVisible(true);
    }

    @SuppressWarnings("resource")
	@Override
    public void actionPerformed(ActionEvent e) {
        String ingest = null;
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setDialogTitle("Choose destination.");
        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        String ae = e.getActionCommand();
        System.out.println(ae);
        if (ae.equals("Open")) {
            returnValue = jfc.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
            File f = new File(jfc.getSelectedFile().getAbsolutePath());
            try {
                FileReader read = new FileReader(f);
                Scanner scan = new Scanner(read);
                while(scan.hasNextLine()){
                    String line = scan.nextLine() + "\n";
                    ingest = ingest + line;
                }
                ingest=ingest.substring(4);
                area.setText(ingest);
                
                frmDeltaIde.setTitle("Delta IDE - "+f.getName());
                JScrollBar scrollBar = new JScrollBar();
                frmDeltaIde.getContentPane().add(scrollBar, BorderLayout.EAST);
            }
        catch ( FileNotFoundException ex) { ex.printStackTrace(); }
        }
        // SAVE
        } else if (ae.equals("Save")) {
            returnValue = jfc.showSaveDialog(null);
            try {
                File f = new File(jfc.getSelectedFile().getAbsolutePath());
                FileWriter out = new FileWriter(f);
                out.write(area.getText());
                out.close();
                frmDeltaIde.setTitle("Delta IDE - "+f.getName());
            } catch (FileNotFoundException ex) {
                Component f = null;
                JOptionPane.showMessageDialog(f,"File not found.");
            } catch (IOException ex) {
                Component f = null;
                JOptionPane.showMessageDialog(f,"Error.");
            }
        } else if (ae.equals("New")) {
        	frmDeltaIde.setTitle("Delta IDE - New");
            area.setText("");
        } else if (ae.equals("Quit")) { frmDeltaIde.setTitle("Delta IDE - Bye!");System.exit(0); }
    }
}