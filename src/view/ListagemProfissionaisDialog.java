package view;

import dto.ProfissionalDTO;
import service.ProfissionalService;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;

public class ListagemProfissionaisDialog extends JDialog {

    private AgendaPanel agendaPanel;
    private JTable tabela;
    private DefaultTableModel modelo;
    private JButton btnExcluir;
    private final ProfissionalService profissionalService;

    public ListagemProfissionaisDialog(Frame parent, boolean modal, AgendaPanel agendaPanel) {
        this(parent, modal, agendaPanel, new ProfissionalService());
    }

    public ListagemProfissionaisDialog(Frame parent, boolean modal, AgendaPanel agendaPanel, ProfissionalService profissionalService) {
        super(parent, "Listagem de Profissionais", modal);
        this.agendaPanel = agendaPanel;
        this.profissionalService = profissionalService;
        inicializarComponentes();
        configurarEventos();
        carregarTabela();
        setSize(700, 400);
        setLocationRelativeTo(parent);
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));

        String[] colunas = {"ID", "Nome", "Data de Nascimento", "CPF", "Email", "Função"};
        modelo = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(modelo);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(tabela);
        add(scroll, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnExcluir = new JButton("Excluir profissional");
        painelBotoes.add(btnExcluir);
        add(painelBotoes, BorderLayout.SOUTH);
    }

    private void configurarEventos() {
        btnExcluir.addActionListener(e -> {
            int linhaSelecionada = tabela.getSelectedRow();

            if (linhaSelecionada == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um profissional para excluir.");
                return;
            }

            UIManager.put("OptionPane.yesButtonText", "Sim");
            UIManager.put("OptionPane.noButtonText", "Não");

            int opcao = JOptionPane.showConfirmDialog(
                    this,
                    "Deseja realmente excluir este profissional?",
                    "Confirmação",
                    JOptionPane.YES_NO_OPTION
            );

            if (opcao == JOptionPane.YES_OPTION) {
                int idSelecionado = (int) modelo.getValueAt(linhaSelecionada, 0);

                try {
                    profissionalService.excluir(idSelecionado);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Erro ao excluir profissional: " + ex.getMessage(),
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                carregarTabela();

                if (agendaPanel != null) {
                    agendaPanel.atualizarColunasAgenda();
                }

                JOptionPane.showMessageDialog(this, "Profissional removido com sucesso!");
            }
        });
    }

    private void carregarTabela() {
        modelo.setRowCount(0);

        List<ProfissionalDTO> profissionais;
        try {
            profissionais = profissionalService.listarTodos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar profissionais: " + e.getMessage());
            return;
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (ProfissionalDTO p : profissionais) {
            modelo.addRow(new Object[]{
                p.getIdProfissional(),
                p.getNome(),
                p.getDataNascimento() != null ? p.getDataNascimento().format(fmt) : "",
                p.getCpf(),
                p.getEmail(),
                p.getFuncao()
            });
        }
    }
}
