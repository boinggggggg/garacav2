import javax.swing.*;
import java.util.*;
import java.io.*;

// Classe che rappresenta un cavallo, estende la classe Thread
class Cavallo extends Thread {
    private String nome;
    private int l_Percorso;
    private int m_Percorsi = 0;
    private int v;
    private boolean infortunio = false;
    private Random random = new Random();

    public Cavallo(String nome, int l_Percorso, int v) {
        this.nome = nome;
        this.l_Percorso = l_Percorso;
        this.v = v;
    }

    @Override
    public void run() {
        while (m_Percorsi < l_Percorso && !infortunio) {
            // Possibilità di infortunio (10%)
            if (random.nextInt(100) < 10) {
                infortunio = true;
                System.out.println(nome + " si è infortunato e ha abbandonato la gara!");
                return;
            }

            // Distanza percorsa in base alla velocità
            m_Percorsi += v;

            if (m_Percorsi > l_Percorso) {
                m_Percorsi = l_Percorso;
            }

            System.out.println(nome + " ha percorso " + m_Percorsi + " metri.");
            try {
                Thread.sleep(1000); // Un secondo prima del prossimo passo
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public String getNome() {
        return nome;
    }

    public int getMPercorsi() {
        return m_Percorsi;
    }

    public boolean Infortunato() {
        return infortunio;
    }
}

public class GaraCavalli {
    public static void main(String[] args) {
        // Chiede all'utente la lunghezza del percorso
        int lPercorso = Integer.parseInt(JOptionPane.showInputDialog("Inserisci la lunghezza del percorso in metri:"));
        
        // Chiede all'utente il numero di cavalli
        int nCavalli = Integer.parseInt(JOptionPane.showInputDialog("Inserisci il numero di cavalli:"));

        // Crea una lista per memorizzare i cavalli
        List<Cavallo> cavalli = new ArrayList<>();

        for (int i = 0; i < nCavalli; i++) {
            // Chiede all'utente di inserire il nome del cavallo
            String nome = JOptionPane.showInputDialog("Inserisci il nome del cavallo " + (i + 1) + ":");
            int velocità = Integer.parseInt(JOptionPane.showInputDialog("Inserisci i metri percorsi dal cavallo " + nome + " in un secondo:"));
            // Crea un nuovo Cavallo
            Cavallo cavallo = new Cavallo(nome, lPercorso, velocità);
            cavalli.add(cavallo);
            cavallo.start(); 
        }

        // Aspetta che tutti i cavalli finiscano la gara
        for (Cavallo cavallo : cavalli) {
            try {
                // Aspetta che il thread del cavallo termini
                cavallo.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Classifica dei cavalli
        List<Cavallo> finito = new ArrayList<>();
        for (Cavallo cavallo : cavalli) {
            if (!cavallo.Infortunato()) {
                finito.add(cavallo);
            }
        }

        // Stampa classifica
        StringBuilder classifica = new StringBuilder("Classifica:\n");
        for (int i = 0; i < Math.min(3, finito.size()); i++) {
            classifica.append(i+1).append(")").append(finito.get(i).getNome()).append("\n");
        }

        // Salva i risultati in un file
        String nomeFile = JOptionPane.showInputDialog("Inserisci il nome del file per salvare i risultati:");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeFile, true))) {
            writer.write(classifica.toString());
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Errore durante il salvataggio dei risultati: " + e.getMessage());
        }

        
        JOptionPane.showMessageDialog(null, "La gara è finita!\n" + classifica);
    }
}
