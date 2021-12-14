package group9.belaya;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class MainFrame extends JFrame {
    private static final int WIDTH = 800, HEIGHT = 600;
    private JFileChooser fileChooser = null;
    private JCheckBoxMenuItem showAxisMenuItem, showMarkersMenuItem, showModuleMenuItem;
    private GraphicsDisplay display = new GraphicsDisplay();
    private boolean fileLoaded = false;

    public MainFrame() {
        super("Graphics building");
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH)/2, (kit.getScreenSize().height - HEIGHT)/2);
        setExtendedState(MAXIMIZED_BOTH);

        // Создание полосы меню
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        Action openGraphicsAction = new AbstractAction("Open file with graphic") {
            public void actionPerformed(ActionEvent ev) {
                if (fileChooser == null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }

                if (fileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION)
                    openGraphics(fileChooser.getSelectedFile());
            }
        };

        fileMenu.add(openGraphicsAction);
        JMenu graphicsMenu = new JMenu("Graphic");
        menuBar.add(graphicsMenu);

        //показать оси
        Action showAxisAction = new AbstractAction("Show axises") {
            public void actionPerformed (ActionEvent ev) {
                display.setShowAxis(showAxisMenuItem.isSelected());
            }
        };

        showAxisMenuItem = new JCheckBoxMenuItem(showAxisAction);
        graphicsMenu.add(showAxisMenuItem);
        showAxisMenuItem.setVisible(false);

        Action showMarkersAction = new AbstractAction ("Show dots markers") {
            public void actionPerformed (ActionEvent ev) {
                display.setShowMarkers(showMarkersMenuItem.isSelected());
            }
        };

        showMarkersMenuItem = new JCheckBoxMenuItem(showMarkersAction);
        graphicsMenu.add(showMarkersMenuItem);
        showMarkersMenuItem.setVisible(false);

        Action showModuleAction = new AbstractAction("Min and max of function") {
            public void actionPerformed (ActionEvent ev) {
                display.setShowMinMax(showModuleMenuItem.isSelected());
            }
        };

        showModuleMenuItem = new JCheckBoxMenuItem(showModuleAction);
        graphicsMenu.add(showModuleMenuItem);
        showModuleMenuItem.setVisible(false);

        graphicsMenu.addMenuListener(new graphicsMenuListener());

        getContentPane().add(display, BorderLayout.CENTER);
    }

    // Считывание данных графика из существующего файла
    protected void openGraphics(File selectedFile) {
        try {
            DataInputStream in = new DataInputStream(new FileInputStream(selectedFile));
            Double[][] graphicsData = new Double[in.available()/(Double.SIZE/8)/2][];
            int i = 0;
            while (in.available() > 0) {
                Double x = in.readDouble();
                Double y = in.readDouble();
                graphicsData[i++] = new Double[] {x, y};
            }

            if (graphicsData != null && graphicsData.length > 0) {
                fileLoaded = true;
                display.showGraphics(graphicsData);
            }

            in.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(MainFrame.this, "File is not found", "Downloading error", JOptionPane.WARNING_MESSAGE);
            return;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(MainFrame.this, "Coordinates error", "Downloading error", JOptionPane.WARNING_MESSAGE);
            return;
        }
    }

    public static void main(String[] args) {
        MainFrame frame = new MainFrame ();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private class graphicsMenuListener implements MenuListener {

        public void menuCanceled(MenuEvent arg0) {
        }

        public void menuDeselected(MenuEvent arg0) {
        }

        public void menuSelected(MenuEvent arg0) {
            showAxisMenuItem.setVisible(fileLoaded);
            showModuleMenuItem.setVisible(fileLoaded);
            showMarkersMenuItem.setVisible(fileLoaded);
        }
    }

}
