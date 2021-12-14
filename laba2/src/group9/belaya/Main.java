package group9.belaya;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.lang.Math;

public class Main  extends JFrame {
    private static final int WIDTH = 400; // размер окна
    private static final int HEIGHT = 320;

    private JTextField textFieldX; //текстовые поля для считывания значений переменных
    private JTextField textFieldY;
    private JTextField textFieldZ;

    private Double result = 0.0;
    private JLabel textFieldResult; // Текстовое поле для отображения результата

    private JLabel textFieldMem1;
    private JLabel textFieldMem2;
    private JLabel textFieldMem3;

    private ButtonGroup FormulasRadioButtons = new ButtonGroup();
    private ButtonGroup MemRadioButtons = new ButtonGroup();

    private Box hboxFormulaType = Box.createHorizontalBox();
    private Box hboxMemVariablesType = Box.createHorizontalBox();

    private int formulaId = 1;
    private int memId = 1;

    private Double mem1 = 0.0;
    private Double mem2 = 0.0;
    private Double mem3 = 0.0;

    public Double calculate1(Double x, Double y, Double z) {
        return (Math.sin(Math.log(y)+Math.sin(Math.PI*y*y))*Math.pow((x*x+Math.sin(z)+Math.pow(Math.E,Math.cos(z))),1/4));
    }
    public Double calculate2(Double x, Double y, Double z) {
        return ((1+Math.pow(x,z)+Math.log(y*y))/(Math.sqrt(x*x*x+1))*(1-Math.sin(y*z)));
    }

    private void addFormulaRadioButton(String buttonName, final int formulaId) {
        JRadioButton button = new JRadioButton(buttonName);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                Main.this.formulaId = formulaId;
            }
        });
        FormulasRadioButtons.add(button);
        hboxFormulaType.add(button);
    }
    private void addMemRadioButton(String buttonName, final int memId) {
        JRadioButton button = new JRadioButton(buttonName);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                Main.this.memId = memId;
                if(memId==1){
                    result = mem1;
                    textFieldResult.setText(result.toString());
                }
            }
        });
        MemRadioButtons.add(button);
        hboxMemVariablesType.add(button);
    }
    public Main() {
        super("Formula calculation");
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        // центрирование окна приложения на экране
        setLocation((kit.getScreenSize().width - WIDTH) / 2, (kit.getScreenSize().height - HEIGHT) / 2);

        hboxFormulaType.add(Box.createHorizontalGlue());
        addFormulaRadioButton("Formula 1", 1);
        addFormulaRadioButton("Formula 2", 2);
        FormulasRadioButtons.setSelected(FormulasRadioButtons.getElements().nextElement().getModel(), true);
        hboxFormulaType.add(Box.createHorizontalGlue());
        hboxFormulaType.setBorder(BorderFactory.createLineBorder(Color.RED));

        //текстовые поля для переменных
        JLabel labelForX = new JLabel("X:");
        textFieldX = new JTextField("0",5);
        textFieldX.setMaximumSize(textFieldX.getPreferredSize());
        JLabel labelForY = new JLabel("Y:");
        textFieldY = new JTextField("0",5);
        textFieldY.setMaximumSize(textFieldY.getPreferredSize());
        JLabel labelForZ = new JLabel("Z:");
        textFieldZ = new JTextField("0",5);
        textFieldZ.setMaximumSize(textFieldZ.getPreferredSize());

        Box hboxVariables = Box.createHorizontalBox();
        hboxVariables.add(Box.createHorizontalStrut(0));
        hboxVariables.add(labelForX);
        hboxVariables.add(Box.createHorizontalStrut(10));
        hboxVariables.add(textFieldX);
        hboxVariables.add(Box.createHorizontalGlue());
        hboxVariables.add(labelForY);
        hboxVariables.add(Box.createHorizontalStrut(10));
        hboxVariables.add(textFieldY);
        hboxVariables.add(Box.createHorizontalGlue());
        hboxVariables.add(labelForZ);
        hboxVariables.add(Box.createHorizontalStrut(10));
        hboxVariables.add(textFieldZ);
        hboxVariables.add(Box.createHorizontalStrut(0));
        hboxVariables.setBorder(BorderFactory.createLineBorder(Color.BLUE));

        hboxMemVariablesType.add(Box.createHorizontalGlue());
        addMemRadioButton("Mem 1", 1);
        hboxMemVariablesType.add(Box.createHorizontalStrut(10));
        addMemRadioButton("Mem 2", 2);
        hboxMemVariablesType.add(Box.createHorizontalStrut(10));
        addMemRadioButton("Mem 3", 3);
        MemRadioButtons.setSelected(MemRadioButtons.getElements().nextElement().getModel(),true);
        hboxMemVariablesType.add(Box.createHorizontalGlue());
        hboxMemVariablesType.setBorder(BorderFactory.createLineBorder(Color.GREEN));

        textFieldMem1 = new JLabel("0",10);
        textFieldMem2 = new JLabel("0",10);
        textFieldMem3 = new JLabel("0",10);
        textFieldMem1.setMaximumSize(textFieldMem1.getPreferredSize());
        textFieldMem2.setMaximumSize(textFieldMem2.getPreferredSize());
        textFieldMem3.setMaximumSize(textFieldMem3.getPreferredSize());

        JButton buttonM = new JButton("M+");
        buttonM.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    Double x = Double.parseDouble(textFieldResult.getText());
                    if(memId==1){
                        mem1 += x;
                        textFieldMem1.setText(mem1.toString());
                    }
                    else{
                        if(memId==2){
                            mem2 += x;
                            textFieldMem2.setText(mem2.toString());
                        } else {
                            mem3 += x;
                            textFieldMem3.setText(mem3.toString());
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(Main.this, "Float number record error", "Number format error", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        JButton buttonMC = new JButton("MC");
        buttonMC.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    if(memId==1){
                        mem1 = 0.0;
                        textFieldMem1.setText("0");
                    }
                    else {
                        if (memId == 2) {
                            mem2 = 0.0;
                            textFieldMem2.setText("0");
                        } else {
                            mem3 = 0.0;
                            textFieldMem3.setText("0");
                        }
                    }
                }   catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(Main.this, "Float number record error", "Number format error", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        Box hboxM = Box.createHorizontalBox();
        hboxM.add(Box.createHorizontalGlue());

        hboxM.add(buttonM);
        hboxM.add(Box.createHorizontalStrut(10));
        hboxM.add(buttonMC);
        hboxM.add(Box.createHorizontalStrut(10));
        hboxM.add(Box.createHorizontalGlue());
        hboxM.setBorder(BorderFactory.createLineBorder(Color.YELLOW));

        Box hboxMemValue = Box.createHorizontalBox();
        hboxMemValue.add(Box.createHorizontalGlue());
        hboxMemValue.add(textFieldMem1);
        hboxMemValue.add(Box.createHorizontalStrut(10));
        hboxMemValue.add(textFieldMem2);
        hboxMemValue.add(Box.createHorizontalStrut(10));
        hboxMemValue.add(textFieldMem3);
        hboxMemValue.add(Box.createHorizontalStrut(10));
        hboxMemValue.add(Box.createHorizontalGlue());
        hboxMemValue.setBorder(BorderFactory.createLineBorder(Color.CYAN));

        JLabel labelForResult = new JLabel("Result:");
        textFieldResult = new JLabel("0");
        textFieldResult.setMaximumSize(textFieldResult.getPreferredSize());


        Box hboxResult = Box.createHorizontalBox();
        hboxResult.add(Box.createHorizontalGlue());
        hboxResult.add(labelForResult);
        hboxResult.add(Box.createHorizontalStrut(10));
        hboxResult.add(textFieldResult);
        hboxResult.add(Box.createHorizontalGlue());
        hboxResult.setBorder(BorderFactory.createLineBorder(Color.BLUE));

        //создаем кнопку вычисления
        JButton buttonCalc = new JButton("Calculate");
        buttonCalc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    Double x = Double.parseDouble(textFieldX.getText());
                    Double y = Double.parseDouble(textFieldY.getText());
                    Double z = Double.parseDouble(textFieldZ.getText());
                    if (formulaId == 1)
                        result = calculate1(x, y, z);
                    else
                        result = calculate2(x, y, z);
                    textFieldResult.setText(result.toString());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(Main.this, "ERROR: float number record", "ERROR: number format", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        JButton buttonReset = new JButton("Clear fields");
        buttonReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                textFieldX.setText("0");
                textFieldY.setText("0");
                textFieldZ.setText("0");
                textFieldResult.setText("0");
            }
        });

        Box hboxButtons = Box.createHorizontalBox();
        hboxButtons.setBorder(BorderFactory.createLineBorder(Color.GREEN));
        hboxButtons.add(Box.createHorizontalGlue());
        hboxButtons.add(buttonCalc);
        hboxButtons.add(Box.createHorizontalStrut(10));
        hboxButtons.add(buttonReset);
        hboxButtons.add(Box.createHorizontalGlue());

        //собираем панели окна
        Box contentBox = Box.createVerticalBox();
        contentBox.add(Box.createVerticalGlue());
        contentBox.add(hboxFormulaType);
        contentBox.add(hboxVariables);
        contentBox.add(hboxM);
        contentBox.add(hboxMemVariablesType);
        contentBox.add(hboxMemValue);
        contentBox.add(hboxResult);
        contentBox.add(hboxButtons);
        contentBox.add(Box.createVerticalGlue());
        getContentPane().add(contentBox, BorderLayout.CENTER);
    }
    public static void main(String[] args) {
        Main frame = new Main();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
