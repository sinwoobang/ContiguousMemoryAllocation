package ui;

import models.memory.Memory;
import models.memory.Process;
import views.MainView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CommandBtn extends JButton implements ActionListener {
    private MainView mainView;
    private String text;

    public CommandBtn(String text) {
        this.text = text;
        setEnabled(false);
        setPreferredSize(new Dimension(10, 30));
        addActionListener(this);
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        setText(b ? text : text + " 불가");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mainView = (MainView)this.getRootPane().getParent();
        Memory memory = mainView.getMemory();
        if (text.contains("할당")) {
            String idRaw = JOptionPane.showInputDialog("새로운 프로세스 번호를 입력하세요.");
            int id, KBUnit;
            try {
                id = Integer.parseInt(idRaw);
                if (!memory.hasResource(id)) {
                    String KBRaw = JOptionPane.showInputDialog("용량을 입력하세요(KB 단위).");
                    KBUnit = Integer.parseInt(KBRaw);

                    Process p = new Process(id, KBUnit * Memory.KB);
                    int[] fits = memory.analyzeFit(p);
                    memory.allocate(fits[0], p);

                    mainView.setCommand("Request " + p.toString());
                    mainView.setSubCommand(String.format("Best fit : %d / Worst Fit : %d", fits[0], fits[1]));
                    mainView.refresh();
                } else {
                    JOptionPane.showMessageDialog(null, "이미 존재하는 프로세스 번호입니다.");
                }
            } catch (NumberFormatException ignored) {
                JOptionPane.showMessageDialog(null, "숫자만 입력 가능합니다.");
            }
        } else if (text.contains("해제")) {
            String idRaw = JOptionPane.showInputDialog("할당 해제할 프로세스 번호를 입력하세요.");
            int id;
            try {
                id = Integer.parseInt(idRaw);
                if (memory.hasResource(id)) {
                    memory.free((Process)memory.getResourceById(id));

                    mainView.setCommand("Free " + id);
                    mainView.setSubCommand("");
                    mainView.refresh();
                } else {
                    JOptionPane.showMessageDialog(null, "존재하지 않는 프로세스 번호입니다.");
                }
            } catch (NumberFormatException ignored) {
                JOptionPane.showMessageDialog(null, "숫자만 입력 가능합니다.");
            }
        } else {
            memory.compaction();
            mainView.setCommand("Compaction");
            mainView.setSubCommand("");
            mainView.refresh();
        }
    }
}
