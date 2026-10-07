package com.example.decathlon.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.example.decathlon.core.ScoringService;

public class DesktopUI {

    private JTextField nameField;
    private JTextField resultField;
    private JComboBox<String> disciplineBox;
    private JTextArea outputArea;

    private final ScoringService scoringService = new ScoringService();
    private final Map<String, Map<String, Integer>> scores = new LinkedHashMap<>();

    private record Bounds(double min, double max) {}

    private static final Map<String, String> EVENT_IDS = new LinkedHashMap<>();
    private static final Map<String, Bounds> EVENT_BOUNDS = new LinkedHashMap<>();
    static {
        EVENT_IDS.put("100m", "100m");
        EVENT_IDS.put("400m", "400m");
        EVENT_IDS.put("1500m", "1500m");
        EVENT_IDS.put("110m Hurdles", "110mHurdles");
        EVENT_IDS.put("Long Jump", "longJump");
        EVENT_IDS.put("High Jump", "highJump");
        EVENT_IDS.put("Pole Vault", "poleVault");
        EVENT_IDS.put("Discus Throw", "discusThrow");
        EVENT_IDS.put("Javelin Throw", "javelinThrow");
        EVENT_IDS.put("Shot Put", "shotPut");

        EVENT_BOUNDS.put("100m", new Bounds(5, 17.8));
        EVENT_BOUNDS.put("400m", new Bounds(20, 100));
        EVENT_BOUNDS.put("1500m", new Bounds(2, 7));
        EVENT_BOUNDS.put("110mHurdles", new Bounds(10, 28.5));
        EVENT_BOUNDS.put("longJump", new Bounds(0, 1000));
        EVENT_BOUNDS.put("highJump", new Bounds(0, 100));
        EVENT_BOUNDS.put("poleVault", new Bounds(0, 1000));
        EVENT_BOUNDS.put("discusThrow", new Bounds(0, 85));
        EVENT_BOUNDS.put("javelinThrow", new Bounds(0, 110));
        EVENT_BOUNDS.put("shotPut", new Bounds(0, 30));
    }

    public static void main(String[] args) {
        new DesktopUI().createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Track and Field Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 450);

        JPanel panel = new JPanel(new GridLayout(7, 1));

        nameField = new JTextField(20);
        panel.add(new JLabel("Enter Competitor's Name:"));
        panel.add(nameField);

        String[] disciplines = EVENT_IDS.keySet().toArray(new String[0]);
        disciplineBox = new JComboBox<>(disciplines);
        panel.add(new JLabel("Select Discipline:"));
        panel.add(disciplineBox);

        resultField = new JTextField(10);
        panel.add(new JLabel("Enter Result:"));
        panel.add(resultField);

        JButton calculateButton = new JButton("Calculate Score");
        calculateButton.addActionListener(new CalculateButtonListener());
        panel.add(calculateButton);

        JButton exportButton = new JButton("Export CSV");
        exportButton.addActionListener(new ExportButtonListener(frame));
        panel.add(exportButton);

        outputArea = new JTextArea(5, 40);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        panel.add(scrollPane);

        frame.add(panel);
        frame.setVisible(true);
    }

    private int totalFor(String name) {
        return scores.getOrDefault(name, Map.of()).values().stream().mapToInt(Integer::intValue).sum();
    }

    private class CalculateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a competitor name", "Missing Name", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String discipline = (String) disciplineBox.getSelectedItem();
            String resultText = resultField.getText();

            try {
                double result = Double.parseDouble(resultText);
                String eventId = EVENT_IDS.get(discipline);
                Bounds bounds = EVENT_BOUNDS.get(eventId);

                if (result < bounds.min()) {
                    JOptionPane.showMessageDialog(null, "Value too low", "Invalid Result", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (result > bounds.max()) {
                    JOptionPane.showMessageDialog(null, "Value too high", "Invalid Result", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int score = scoringService.score(eventId, result);
                scores.computeIfAbsent(name, n -> new LinkedHashMap<>()).put(eventId, score);
                int total = totalFor(name);

                outputArea.append("Competitor: " + name + "\n");
                outputArea.append("Discipline: " + discipline + "\n");
                outputArea.append("Result: " + result + "\n");
                outputArea.append("Score: " + score + "\n");
                outputArea.append("Total: " + total + "\n\n");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number for the result.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class ExportButtonListener implements ActionListener {
        private final JFrame parent;

        ExportButtonListener(JFrame parent) {
            this.parent = parent;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (scores.isEmpty()) {
                JOptionPane.showMessageDialog(parent, "No results to export yet.", "Nothing to Export", JOptionPane.WARNING_MESSAGE);
                return;
            }

            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new File("results.csv"));
            int choice = chooser.showSaveDialog(parent);
            if (choice != JFileChooser.APPROVE_OPTION) {
                return;
            }

            File file = chooser.getSelectedFile();

            Set<String> eventIds = new LinkedHashSet<>();
            scores.values().forEach(m -> eventIds.addAll(m.keySet()));

            StringBuilder sb = new StringBuilder();
            sb.append("Name");
            for (String id : eventIds) sb.append(",").append(id);
            sb.append(",Total\n");

            for (Map.Entry<String, Map<String, Integer>> entry : scores.entrySet()) {
                sb.append(entry.getKey());
                for (String id : eventIds) {
                    Integer pts = entry.getValue().get(id);
                    sb.append(",").append(pts == null ? "" : pts);
                }
                sb.append(",").append(totalFor(entry.getKey())).append("\n");
            }

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(sb.toString());
                JOptionPane.showMessageDialog(parent, "Results exported to " + file.getAbsolutePath(), "Export Complete", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(parent, "Could not write file: " + ex.getMessage(), "Export Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}