package com.example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Catalogue {
    private List<Publication> publications = new ArrayList<>();

    public void addPublication(Publication p) {
        publications.add(p);
    }

    public void removePublicationByTitle(String title) throws BookNotFoundException {
        Publication toRemove = findPublicationByTitle(title);
        if (toRemove == null) {
            throw new BookNotFoundException("Публікацію з назвою '" + title + "' не знайдено.");
        }
        publications.remove(toRemove);
    }

    public Publication findPublicationByTitle(String title) {
        for (Publication p : publications) {
            if (p.getTitle().equalsIgnoreCase(title)) {
                return p;
            }
        }
        return null;
    }

    public List<Publication> getAllPublications() {
        return new ArrayList<>(publications);
    }

    public void saveToFile(String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(publications);
        }
    }

    @SuppressWarnings("unchecked")
    public void loadFromFile(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            publications = (List<Publication>) ois.readObject();
        }
    }
}