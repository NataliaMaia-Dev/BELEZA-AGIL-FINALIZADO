package view;

import dto.ClienteDTO;
import service.ClienteService;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;

public class ListagemClientesDialog extends JDialog {

    private AgendaPanel agendaPanel;
    private JTable tabela;
    private DefaultTableModel modelo;
    private JButton btnExcluir;
    private final ClienteService clienteService;

    public ListagemClientesDialog(Frame parent, boolean modal, AgendaPanel agendaPanel) {
        this(parent, modal, agendaPanel, new ClienteService());
    }

    public ListagemClientesDialog(Frame parent, boolean modal, AgendaPanel agendaPanel, ClienteService clienteService) {
        super(parent, "Listagem de Clientes", modal);
        this.agendaPanel = agendaPanel;
        this.clienteService = clienteService;
        inicializarComponentes();
        configurarEventos();
        carregarTabela();
        setSize(700, 400);
        setLocationRelativeTo(parent);
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));

        String[] colunas = {"ID", "Nome", "Data de Nascimento", "CPF", "Telefone"};
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
        btnExcluir = new JButton("Excluir cliente");
        painelBotoes.add(btnExcluir);
        add(painelBotoes, BorderLayout.SOUTH);
    }

    private void configurarEventos() {
        btnExcluir.addActionListener(e -> {
            int linhaSelecionada = tabela.getSelectedRow();

            if (linhaSelecionada == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um cliente para excluir.");
                return;
            }

            UIManager.put("OptionPane.yesButtonText", "Sim");
            UIManager.put("OptionPane.noButtonText", "Não");

            int opcao = JOptionPane.showConfirmDialog(
                    this,
                    "Deseja realmente excluir este cliente?",
                    "Confirmação",
                    JOptionPane.YES_NO_OPTION
            );

            if (opcao == JOptionPane.YES_OPTION) {
                int idSelecionado = (int) modelo.getValueAt(linhaSelecionada, 0);

                try {
                    clienteService.excluir(idSelecionado);
                    JOptionPane.showMessageDialog(this, "Cliente removido com sucesso!");
                } catch (SQLException e1) {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir cliente: " + e1.getMessage());
                    return;
                }

                carregarTabela();

                JOptionPane.showMessageDialog(this, "Cliente removido com sucesso!");
            }
        });
    }

    private void carregarTabela() {
        modelo.setRowCount(0);

        List<ClienteDTO> clientes;
        try {
            clientes = clienteService.listarTodos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar clientes: " + e.getMessage());
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (ClienteDTO c : clientes) {
            modelo.addRow(new Object[]{
                c.getIdCliente(),
                c.getNome(),
                c.getDataNascimento() != null ? sdf.format(c.getDataNascimento()) : "",
                c.getCpf(),
                c.getTelefone()
            });
        }
    }
}
