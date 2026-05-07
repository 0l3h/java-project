package com.example;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BookGUI extends JFrame {
    private Catalogue catalogue = new Catalogue();
    
    private JTextField titleField = new JTextField(20);
    private JTextField authorField = new JTextField(20);
    private JTextField publisherField = new JTextField(20);
    private JTextField genreField = new JTextField(20);
    private JTextField yearField = new JTextField(20);
    private JTextArea outputArea = new JTextArea(15, 40);

    public BookGUI() {
        setTitle("Каталог книг");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Панель введення
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        inputPanel.add(new JLabel("Назва:")); inputPanel.add(titleField);
        inputPanel.add(new JLabel("Автор:")); inputPanel.add(authorField);
        inputPanel.add(new JLabel("Видавництво:")); inputPanel.add(publisherField);
        inputPanel.add(new JLabel("Жанр:")); inputPanel.add(genreField);
        inputPanel.add(new JLabel("Рік:")); inputPanel.add(yearField);

        // Панель кнопок
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Додати книгу");
        JButton removeButton = new JButton("Видалити книгу");
        JButton updateButton = new JButton("Оновити книгу");
        JButton saveButton = new JButton("Зберегти у файл");
        JButton loadButton = new JButton("Завантажити з файлу");
        JButton searchButton = new JButton("Пошук");
        JButton resetButton = new JButton("Скинути");

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(resetButton);

        // Обробка подій
        addButton.addActionListener(e -> {
            try {
                catalogue.addPublication(new Book(
                    titleField.getText(),
                    Integer.parseInt(yearField.getText()),
                    authorField.getText(),
                    publisherField.getText(),
                    genreField.getText()
                ));
                refreshOutput();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Рік повинен бути числом!");
            }
        });

        removeButton.addActionListener(e -> {
            try {
                catalogue.removePublicationByTitle(titleField.getText());
                refreshOutput();
            } catch (BookNotFoundException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        updateButton.addActionListener(e -> {
            Publication p = catalogue.findPublicationByTitle(titleField.getText());
            if (p instanceof Book) {
                Book b = (Book) p;
                b.setAuthor(authorField.getText());
                b.setPublisher(publisherField.getText());
                b.setGenre(genreField.getText());
                b.setYear(Integer.parseInt(yearField.getText()));
                refreshOutput();
            } else {
                JOptionPane.showMessageDialog(this, "Книгу не знайдено для оновлення.");
            }
        });

        saveButton.addActionListener(e -> {
            try {
                catalogue.saveToFile("catalogue.dat");
                JOptionPane.showMessageDialog(this, "Збережено успішно.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Помилка збереження: " + ex.getMessage());
            }
        });

        loadButton.addActionListener(e -> {
            try {
                catalogue.loadFromFile("catalogue.dat");
                refreshOutput();
                JOptionPane.showMessageDialog(this, "Завантажено успішно.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Помилка завантаження.");
            }
        });

        searchButton.addActionListener(e -> {
            String query = titleField.getText().toLowerCase();
            outputArea.setText("Результати пошуку:\n");
            for (Publication p : catalogue.getAllPublications()) {
                if (p.getTitle().toLowerCase().contains(query)) {
                    outputArea.append(p.toString() + "\n");
                }
            }
        });

        resetButton.addActionListener(e -> {
            titleField.setText("");
            authorField.setText("");
            publisherField.setText("");
            genreField.setText("");
            yearField.setText("");
            refreshOutput();
        });

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void refreshOutput() {
        outputArea.setText("Усі публікації:\n");
        List<Publication> all = catalogue.getAllPublications();
        for (Publication p : all) {
            outputArea.append(p.toString() + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BookGUI::new);
    }
}