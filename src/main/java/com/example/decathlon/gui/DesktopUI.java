package com.example.decathlon.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.example.decathlon.core.ScoringService;

public class DesktopUI {

    private JTextField nameField;
    private JTextField resultField;
    private JComboBox<String> competitionBox;
    private JComboBox<String> disciplineBox;
    private JTextArea outputArea;
    private JTextArea standingsArea;

    private final ScoringService scoringService = new ScoringService();
    private final Map<String, Map<String, Integer>> scores = new LinkedHashMap<>();

    private record Bounds(double min, double max) {}

    private static final Map<String, String> DECATHLON_EVENTS = new LinkedHashMap<>();
    private static final Map<String, String> HEPTATHLON_EVENTS = new LinkedHashMap<>();
    private static final Map<String, Bounds> EVENT_BOUNDS = new LinkedHashMap<>();
    private static final Map<String, String> EVENT_UNITS = new LinkedHashMap<>();

    static {
        DECATHLON_EVENTS.put("100m", "100m");
        DECATHLON_EVENTS.put("400m", "400m");
        DECATHLON_EVENTS.put("1500m", "1500m");
        DECATHLON_EVENTS.put("110m Hurdles", "110mHurdles");
        DECATHLON_EVENTS.put("Long Jump", "longJump");
        DECATHLON_EVENTS.put("High Jump", "highJump");
        DECATHLON_EVENTS.put("Pole Vault", "poleVault");
        DECATHLON_EVENTS.put("Discus Throw", "discusThrow");
        DECATHLON_EVENTS.put("Javelin Throw", "javelinThrow");
        DECATHLON_EVENTS.put("Shot Put", "shotPut");

        HEPTATHLON_EVENTS.put("100m Hurdles", "hep100mHurdles");
        HEPTATHLON_EVENTS.put("200m", "hep200m");
        HEPTATHLON_EVENTS.put("800m", "hep800m");
        HEPTATHLON_EVENTS.put("Long Jump", "hepLongJump");
        HEPTATHLON_EVENTS.put("High Jump", "hepHighJump");
        HEPTATHLON_EVENTS.put("Shot Put", "hepShotPut");
        HEPTATHLON_EVENTS.put("Javelin Throw", "hepJavelinThrow");

        EVENT_BOUNDS.put("100m", new Bounds(5, 17.8));
        EVENT_BOUNDS.put("400m", new Bounds(20, 100));
        EVENT_BOUNDS.put("1500m", new Bounds(150, 600));
        EVENT_BOUNDS.put("110mHurdles", new Bounds(10, 28.5));
        EVENT_BOUNDS.put("longJump", new Bounds(0, 1000));
        EVENT_BOUNDS.put("highJump", new Bounds(0, 250));
        EVENT_BOUNDS.put("poleVault", new Bounds(0, 650));
        EVENT_BOUNDS.put("discusThrow", new Bounds(0, 85));
        EVENT_BOUNDS.put("javelinThrow", new Bounds(0, 110));
        EVENT_BOUNDS.put("shotPut", new Bounds(0, 30));
        EVENT_BOUNDS.put("hep100mHurdles", new Bounds(10, 30));
        EVENT_BOUNDS.put("hep200m", new Bounds(15, 40));
        EVENT_BOUNDS.put("hep800m", new Bounds(90, 300));
        EVENT_BOUNDS.put("hepLongJump", new Bounds(0, 1000));
        EVENT_BOUNDS.put("hepHighJump", new Bounds(0, 250));
        EVENT_BOUNDS.put("hepShotPut", new Bounds(0, 30));
        EVENT_BOUNDS.put("hepJavelinThrow", new Bounds(0, 80));

        EVENT_UNITS.put("100m", "s");
        EVENT_UNITS.put("400m", "s");
        EVENT_UNITS.put("1500m", "s");
        EVENT_UNITS.put("110mHurdles", "s");
        EVENT_UNITS.put("longJump", "m");
        EVENT_UNITS.put("highJump", "cm");
        EVENT_UNITS.put("poleVault", "cm");
        EVENT_UNITS.put("discusThrow", "m");
        EVENT_UNITS.put("javelinThrow", "m");
        EVENT_UNITS.put("shotPut", "m");
        EVENT_UNITS.put("hep100mHurdles", "s");
        EVENT_UNITS.put("hep200m", "s");
        EVENT_UNITS.put("hep800m", "s");
        EVENT_UNITS.put("hepLongJump", "m");
        EVENT_UNITS.put("hepHighJump", "cm");
        EVENT_UNITS.put("hepShotPut", "m");
        EVENT_UNITS.put("hepJavelinThrow", "m");
    }

    public static void main(String[] args) {
        new DesktopUI().createAndShowGUI();
    }

    private Map<String, String> currentEvents() {
        return "Heptathlon".equals(competitionBox.getSelectedItem()) ? HEPTATHLON_EVENTS : DECATHLON_EVENTS;
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Track and Field Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);

        JPanel formPanel = new JPanel(new GridLayout(8, 1));

        nameField = new JTextField(20);
        formPanel.add(new JLabel("Enter Competitor's Name:"));
        formPanel.add(nameField);

        competitionBox = new JComboBox<>(new String[]{"Decathlon", "Heptathlon"});
        competitionBox.addItemListener(ev -> {
            if (ev.getStateChange() == ItemEvent.SELECTED) {
                populateDisciplines();
            }
        });
        formPanel.add(new JLabel("Select Competition:"));
        formPanel.add(competitionBox);

        disciplineBox = new JComboBox<>();
        formPanel.add(new JLabel("Select Discipline:"));
        formPanel.add(disciplineBox);
        populateDisciplines();

        resultField = new JTextField(10);
        formPanel.add(new JLabel("Enter Result:"));
        formPanel.add(resultField);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton calculateButton = new JButton("Calculate Score");
        calculateButton.addActionListener(new CalculateButtonListener());
        buttonPanel.add(calculateButton);

        JButton exportButton = new JButton("Export CSV");
        exportButton.addActionListener(new ExportButtonListener(frame));
        buttonPanel.add(exportButton);

        JButton importButton = new JButton("Import CSV");
        importButton.addActionListener(new ImportButtonListener(frame));
        buttonPanel.add(importButton);
        formPanel.add(buttonPanel);

        outputArea = new JTextArea(8, 30);
        outputArea.setEditable(false);
        JScrollPane logScroll = new JScrollPane(outputArea);
        logScroll.setBorder(BorderFactory.createTitledBorder("Log"));

        standingsArea = new JTextArea(8, 30);
        standingsArea.setEditable(false);
        standingsArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane standingsScroll = new JScrollPane(standingsArea);
        standingsScroll.setBorder(BorderFactory.createTitledBorder("Standings"));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, logScroll, standingsScroll);
        splitPane.setResizeWeight(0.5);

        frame.setLayout(new BorderLayout());
        frame.add(formPanel, BorderLayout.NORTH);
        frame.add(splitPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void populateDisciplines() {
        disciplineBox.removeAllItems();
        for (String label : currentEvents().keySet()) {
            disciplineBox.addItem(label);
        }
    }

    private int totalFor(String name) {
        return scores.getOrDefault(name, Map.of()).values().stream().mapToInt(Integer::intValue).sum();
    }

    private String formatResult(String eventId, double value) {
        String unit = EVENT_UNITS.get(eventId);
        if ("cm".equals(unit)) {
            return String.format("%.0f%s", value, unit);
        }
        return String.format("%.2f%s", value, unit);
    }

    private void updateStandings() {
        List<String> names = new ArrayList<>(scores.keySet());
        names.sort(Comparator.comparingInt(this::totalFor).reversed());

        StringBuilder sb = new StringBuilder();
        int rank = 1;
        for (String name : names) {
            sb.append(rank).append(". ").append(name).append(" - ").append(totalFor(name)).append(" pts\n");
            rank++;
        }
        standingsArea.setText(sb.toString());
    }

    private class CalculateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a competitor name", "Missing Name", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!name.matches("[\\p{L} '-]+")) {
                JOptionPane.showMessageDialog(null, "Name contains invalid characters. Only letters are allowed.", "Invalid Name", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String discipline = (String) disciplineBox.getSelectedItem();
            String eventId = currentEvents().get(discipline);
            String resultText = resultField.getText();

            try {
                double result = Double.parseDouble(resultText);
                Bounds bounds = EVENT_BOUNDS.get(eventId);

                if (result < bounds.min()) {
                    JOptionPane.showMessageDialog(null, "Value too low", "Invalid Result", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (result > bounds.max()) {
                    JOptionPane.showMessageDialog(null, "Value too high", "Invalid Result", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Map<String, Integer> existing = scores.get(name);
                if (existing != null && existing.containsKey(eventId)) {
                    JOptionPane.showMessageDialog(null, "A score for " + name + " in this event has already been recorded.", "Duplicate Entry", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int score = scoringService.score(eventId, result);
                scores.computeIfAbsent(name, n -> new LinkedHashMap<>()).put(eventId, score);
                int total = totalFor(name);

                outputArea.append("Competitor: " + name + "\n");
                outputArea.append("Discipline: " + discipline + "\n");
                outputArea.append("Result: " + formatResult(eventId, result) + "\n");
                outputArea.append("Score: " + score + "\n");
                outputArea.append("Total: " + total + "\n\n");

                updateStandings();
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

    private class ImportButtonListener implements ActionListener {
        private final JFrame parent;

        ImportButtonListener(JFrame parent) {
            this.parent = parent;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            int choice = chooser.showOpenDialog(parent);
            if (choice != JFileChooser.APPROVE_OPTION) {
                return;
            }

            File file = chooser.getSelectedFile();

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String headerLine = reader.readLine();
                if (headerLine == null) {
                    JOptionPane.showMessageDialog(parent, "The selected file is empty.", "Import Failed", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String[] header = headerLine.split(",");
                int imported = 0;
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.isBlank()) continue;
                    String[] cells = line.split(",");
                    String name = cells[0];

                    for (int i = 1; i < header.length - 1 && i < cells.length; i++) {
                        String eventId = header[i];
                        String cell = cells[i];
                        if (!cell.isBlank()) {
                            scores.computeIfAbsent(name, n -> new LinkedHashMap<>()).put(eventId, Integer.parseInt(cell.trim()));
                        }
                    }
                    imported++;
                }

                updateStandings();
                JOptionPane.showMessageDialog(parent, "Imported " + imported + " competitor(s) from " + file.getName(), "Import Complete", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(parent, "Could not read file: " + ex.getMessage(), "Import Failed", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(parent, "The file contains invalid score data.", "Import Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}