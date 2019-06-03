package views;

import models.memory.Memory;
import ui.CommandBtn;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
    private Memory memory;
    private JPanel commandPanel = new JPanel(new BorderLayout());
    private JButton allocBtn = new CommandBtn("할당하기");
    private JButton freeBtn = new CommandBtn("해제하기");
    private JButton compactionBtn = new CommandBtn("Compaction");

    private JPanel statisticPanel = new JPanel(new BorderLayout());
    private JLabel commandLabel = new JLabel();
    private JLabel subCommandLabel = new JLabel();
    private JLabel statisticLabel = new JLabel();
    private String command;
    private String subCommand;

    public MainView(Memory memory) {
        this.memory = memory;
        loadData();
        initUI();
        setVisible(true);
    }

    public Memory getMemory() {
        return memory;
    }

    public void setCommand(String command) {
        this.command = command;
        refresh();
    }

    public void setSubCommand(String subCommand) {
        this.subCommand = subCommand;
        refresh();
    }

    public void setCLIFinished(boolean isCLIFinished) {
        allocBtn.setEnabled(isCLIFinished);
        freeBtn.setEnabled(isCLIFinished);
        compactionBtn.setEnabled(isCLIFinished);
        refresh();
    }

    void initUI() {
        setLayout(new BorderLayout());
        setSize(1000, 250);

        commandPanel.add(allocBtn, BorderLayout.NORTH);
        commandPanel.add(freeBtn, BorderLayout.CENTER);
        commandPanel.add(compactionBtn, BorderLayout.SOUTH);

        commandLabel.setPreferredSize(new Dimension(1000, 20));
        subCommandLabel.setPreferredSize(new Dimension(1000, 20));
        statisticLabel.setPreferredSize(new Dimension(1000, 60));

        statisticPanel.add(commandLabel, BorderLayout.NORTH);
        statisticPanel.add(subCommandLabel, BorderLayout.CENTER);
        statisticPanel.add(statisticLabel, BorderLayout.SOUTH);

        this.add(commandPanel, BorderLayout.NORTH);
        this.add(statisticPanel, BorderLayout.SOUTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    void loadData() {
        commandLabel.setText(command);
        subCommandLabel.setText(subCommand);
        String statistics = memory.getResourcesStatisticsString().replace("\n", "<br/>");
        statisticLabel.setText("<html>" + statistics + "</html>");
    }

    public void refresh() {
        loadData();
        invalidate();
        validate();
        repaint();
    }
}
