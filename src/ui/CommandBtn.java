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
        setText(b ? text : text + " �Ұ�");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mainView = (MainView)this.getRootPane().getParent();
        Memory memory = mainView.getMemory();
        if (text.contains("�Ҵ�")) {
            String idRaw = JOptionPane.showInputDialog("���ο� ���μ��� ��ȣ�� �Է��ϼ���.");
            int id, KBUnit;
            try {
                id = Integer.parseInt(idRaw);
                if (!memory.hasResource(id)) {
                    String KBRaw = JOptionPane.showInputDialog("�뷮�� �Է��ϼ���(KB ����).");
                    KBUnit = Integer.parseInt(KBRaw);

                    Process p = new Process(id, KBUnit * Memory.KB);
                    int[] fits = memory.analyzeFit(p);
                    memory.allocate(fits[0], p);

                    mainView.setCommand("Request " + p.toString());
                    mainView.setSubCommand(String.format("Best fit : %d / Worst Fit : %d", fits[0], fits[1]));
                    mainView.refresh();
                } else {
                    JOptionPane.showMessageDialog(null, "�̹� �����ϴ� ���μ��� ��ȣ�Դϴ�.");
                }
            } catch (NumberFormatException ignored) {
                JOptionPane.showMessageDialog(null, "���ڸ� �Է� �����մϴ�.");
            }
        } else if (text.contains("����")) {
            String idRaw = JOptionPane.showInputDialog("�Ҵ� ������ ���μ��� ��ȣ�� �Է��ϼ���.");
            int id;
            try {
                id = Integer.parseInt(idRaw);
                if (memory.hasResource(id)) {
                    memory.free((Process)memory.getResourceById(id));

                    mainView.setCommand("Free " + id);
                    mainView.setSubCommand("");
                    mainView.refresh();
                } else {
                    JOptionPane.showMessageDialog(null, "�������� �ʴ� ���μ��� ��ȣ�Դϴ�.");
                }
            } catch (NumberFormatException ignored) {
                JOptionPane.showMessageDialog(null, "���ڸ� �Է� �����մϴ�.");
            }
        } else {
            memory.compaction();
            mainView.setCommand("Compaction");
            mainView.setSubCommand("");
            mainView.refresh();
        }
    }
}
