package view;

import dto.ServicoDTO;
import service.ServicoService;

import java.awt.*;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;

public class ListagemServicosDialog extends JDialog {

    private JTable tabelaServicos;
    private DefaultTableModel modelo;
    private JButton btnExcluir;
    private JButton btnFechar;
    private AgendamentoDialog agendamentoDialog;
    private final ServicoService servicoService;

    public ListagemServicosDialog(Frame parent, boolean modal, AgendamentoDialog agendamentoDialog) {
        this(parent, modal, agendamentoDialog, new ServicoService());
    }

    public ListagemServicosDialog(Frame parent, boolean modal, AgendamentoDialog agendamentoDialog, ServicoService servicoService) {
        super(parent, modal);
        this.agendamentoDialog = agendamentoDialog;
        this.servicoService = servicoService;
        initComponents();
        carregarTabela();
    }

    private void initComponents() {
        setTitle("Listagem de Serviços");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        String[] colunas = {"ID", "Nome", "Tempo Execução", "Valor"};
        modelo = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaServicos = new JTable(modelo);
        tabelaServicos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(tabelaServicos);
        add(scroll, BorderLayout.CENTER);

        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnExcluir = new JButton("Excluir serviço");

        panelBotoes.add(btnExcluir);
        add(panelBotoes, BorderLayout.SOUTH);

        btnExcluir.addActionListener(e -> excluirServicoSelecionado());
    }

    private void carregarTabela() {
        modelo.setRowCount(0);

        List<ServicoDTO> servicos;
        try {
            servicos = servicoService.listarTodos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar serviços: " + e.getMessage());
            return;
        }

        for (ServicoDTO s : servicos) {
            Object[] linha = {
                s.getIdServico(),
                s.getNome(),
                formatarTempo(s.getTempoExecucao()),
                "R$ " + s.getValor().setScale(2, BigDecimal.ROUND_HALF_UP)
            };
            modelo.addRow(linha);
        }
    }

    private String formatarTempo(Duration tempo) {
        if (tempo == null) {
            return "-";
        }
        long minutos = tempo.toMinutes();
        return minutos + " min";
    }

    private void excluirServicoSelecionado() {
        int linhaSelecionada = tabelaServicos.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um serviço para excluir!");
            return;
        }

        UIManager.put("OptionPane.yesButtonText", "Sim");
        UIManager.put("OptionPane.noButtonText", "Não");

        int resposta = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente excluir este serviço?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (resposta == JOptionPane.YES_OPTION) {
            int idServico = (int) modelo.getValueAt(linhaSelecionada, 0);

            try {
                servicoService.excluir(idServico);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir serviço: " + e.getMessage());
                return;
            }

            carregarTabela();

            if (agendamentoDialog != null) {
                agendamentoDialog.atualizarComboServicos();
            }

            JOptionPane.showMessageDialog(this, "Serviço excluído com sucesso!");
        }
    }
}
