package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import models.Professor;

/**
 *
 * @author Paulo
 */
public class EventoCadastroProfessor {
        public static void adicionarProfessor(DefaultTableModel model, JTextField nome, JTextField telefone, JTextField cpf, JTextField endereco, JTextField nascimento, JTextField email, JTextField disciplina) {
    // Converte nascimento para o formato DATE (YYYY-MM-DD)
    String nascimentoStr = nascimento.getText();
    
    // Verifique se a data está no formato correto. Exemplo: 2025-03-23
    if (!nascimentoStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
        System.out.println("Data inválida");
        return;
    }

    Professor p = new Professor(nome.getText(), telefone.getText(), cpf.getText(), endereco.getText(), nascimentoStr, email.getText(), disciplina.getText());

    // Conexão com o banco de dados
    try (Connection conn = DatabaseConnection.getConnection()) {
        String sql = "INSERT INTO professores (nome, telefone, cpf, endereco, nascimento, email, disciplina) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, p.getNome());
        stmt.setString(2, p.getTelefone());
        stmt.setString(3, p.getCpf());
        stmt.setString(4, p.getEndereco());
        stmt.setString(5, p.getNascimento());
        stmt.setString(6, p.getEmail());
        stmt.setString(7, p.getDisciplina());
        stmt.executeUpdate(); // Executa a inserção no banco

        // Adiciona na tabela de UI
        model.addRow(new Object[]{p.getNome(), p.getTelefone(), p.getCpf(), p.getEndereco(), p.getNascimento(), p.getEmail(), p.getDisciplina()});
    } catch (SQLException e) {
        e.printStackTrace(); // Log de erro
    }
}

    public static void removerProfessor(DefaultTableModel model, JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            // Obtém o ID do professor para remoção
            String nomeProfessor = (String) table.getValueAt(selectedRow, 0);
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "DELETE FROM professores WHERE nome = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, nomeProfessor); // Usa o nome para remover
                stmt.executeUpdate();

                // Remove da UI
                model.removeRow(selectedRow);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
