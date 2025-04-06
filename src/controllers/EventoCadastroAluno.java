package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import models.Aluno;

/**
 *
 * @author Paulo
 */
public class EventoCadastroAluno {
public static void adicionarAluno(DefaultTableModel model, JTextField nome, JTextField nascimento, JTextField turma, JTextField email) {
    // Converte nascimento para o formato DATE (YYYY-MM-DD)
    String nascimentoStr = nascimento.getText();
    
    // Verifique se a data está no formato correto. Exemplo: 2025-03-23
    if (!nascimentoStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
        System.out.println("Data inválida");
        return;
    }

    Aluno a = new Aluno(nome.getText(), nascimentoStr, turma.getText(), email.getText());

    // Conexão com o banco de dados
    try (Connection conn = DatabaseConnection.getConnection()) {
        String sql = "INSERT INTO alunos (nome, nascimento, turma, email) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, a.getNome());
        stmt.setString(2, a.getNascimento());
        stmt.setString(3, a.getTurma());
        stmt.setString(4, a.getEmail());
        stmt.executeUpdate(); // Executa a inserção no banco

        // Adiciona na tabela de UI
        model.addRow(new Object[]{a.getNome(), a.getNascimento(), a.getTurma(), a.getEmail()});
    } catch (SQLException e) {
        e.printStackTrace(); // Log de erro
    }
}

    public static void removerAluno(DefaultTableModel model, JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            // Obtém o ID do aluno para remoção
            String nomeAluno = (String) table.getValueAt(selectedRow, 0);
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "DELETE FROM alunos WHERE nome = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, nomeAluno); // Usa o nome para remover
                stmt.executeUpdate();

                // Remove da UI
                model.removeRow(selectedRow);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
