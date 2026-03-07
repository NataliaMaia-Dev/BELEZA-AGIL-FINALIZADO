package view;

import dto.AgendamentoDTO;
import dto.AgendamentoSlot;
import dto.ClienteDTO;
import dto.ComandaDTO;
import dto.ProfissionalDTO;
import dto.ServicoDTO;
import java.awt.Frame;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import service.AgendamentoService;
import service.ClienteService;
import service.ProfissionalService;

public class AgendaPanel extends javax.swing.JPanel {
    private final AgendamentoService agendamentoService;
    private final ServicoService servicoService;
    private final ClienteService clienteService;
    private final ProfissionalService profissionalService;

    public AgendaPanel() {
        this(new AgendamentoService(), new ServicoService(), new ClienteService(), new ProfissionalService());
    }

    public AgendaPanel(AgendamentoService agendamentoService,
                       ServicoService servicoService,
                       ClienteService clienteService,
                       ProfissionalService profissionalService) {
        this.agendamentoService = agendamentoService;
        this.servicoService = servicoService;
        this.clienteService = clienteService;
        this.profissionalService = profissionalService;
        initComponents();

        jTable1.setDefaultRenderer(Object.class, new AgendaCellRenderer());

        List<ProfissionalDTO> profissionais;
        try {
            profissionais = profissionalService.listarTodos();
        } catch (SQLException ex) {
            profissionais = new ArrayList<>();
            JOptionPane.showMessageDialog(this, "Erro ao carregar profissionais: " + ex.getMessage());
        }

        AgendaTableModel model = new AgendaTableModel(profissionais);
        jTable1.setModel(model);

        jTable1.getColumnModel().getColumn(0).setPreferredWidth(60);
        jTable1.getColumnModel().getColumn(0).setMinWidth(60);
        jTable1.getColumnModel().getColumn(0).setMaxWidth(60);
    }

    public void adicionarAgendamentoNaTabela(AgendamentoDTO agendamento) {

        List<String> horarios = agendamento.getHorariosOcupados();
        if (horarios == null || horarios.isEmpty()) {
            String horario = String.format("%02d:00", agendamento.getDataAgendamento().getHour());
            horarios = new ArrayList<>();
            horarios.add(horario);
        }

        String profissional = agendamento.getProfissional().getNome();

        int col = -1;
        for (int j = 1; j < jTable1.getColumnCount(); j++) {
            if (jTable1.getColumnName(j).equalsIgnoreCase(profissional)) {
                col = j;
                break;
            }
        }
        if (col == -1) {
            return;
        }

        for (int i = 0; i < horarios.size(); i++) {
            String horario = horarios.get(i);
            int row = -1;
            for (int r = 0; r < jTable1.getRowCount(); r++) {
                Object valor = jTable1.getValueAt(r, 0);
                if (valor != null && valor.toString().equalsIgnoreCase(horario)) {
                    row = r;
                    break;
                }
            }
            if (row == -1) {
                continue;
            }

            int displayLevel;
            if (i == 0) {
                displayLevel = 0;
            } else if (i == 1) {
                displayLevel = 1;
            } else if (i == 2) {
                displayLevel = 2;
            } else {
                displayLevel = 3;
            }
            AgendamentoSlot slot = new AgendamentoSlot(agendamento, displayLevel);
            jTable1.setValueAt(slot, row, col);
        }

        jTable1.repaint();
    }


    public void atualizarColunasAgenda() {
        List<ProfissionalDTO> profissionais;
        try {
            profissionais = profissionalService.listarTodos();
        } catch (SQLException ex) {
            profissionais = new ArrayList<>();
        }

        AgendaTableModel model = new AgendaTableModel(profissionais);
        jTable1.setModel(model);

        jTable1.setDefaultRenderer(Object.class, new AgendaCellRenderer());

        jTable1.getColumnModel().getColumn(0).setPreferredWidth(60);
        jTable1.getColumnModel().getColumn(0).setMinWidth(60);
        jTable1.getColumnModel().getColumn(0).setMaxWidth(60);

        for (int i = 1; i < jTable1.getColumnCount(); i++) {
            jTable1.getColumnModel().getColumn(i).setPreferredWidth(140);
            jTable1.getColumnModel().getColumn(i).setMinWidth(140);
            jTable1.getColumnModel().getColumn(i).setMaxWidth(140);
        }

        for (int i = 0; i < jTable1.getRowCount(); i++) {
            jTable1.setRowHeight(i, 17);
        }

        jTable1.revalidate();
        jTable1.repaint();
    }

    public void atualizarAgendaPorData(Date dataSelecionada) {
        atualizarColunasAgenda();

        LocalDate data = dataSelecionada.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        try {
            List<AgendamentoDTO> agendamentosDoBanco = agendamentoService.listarPorData(data);
            for (AgendamentoDTO ag : agendamentosDoBanco) {
                adicionarAgendamentoNaTabela(ag);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar agendamentos: " + e.getMessage());
        }
    }


    public void adicionarAgendamentoNaAgenda(AgendamentoDTO agendamento) {
        try {
            agendamentoService.salvarAgendamento(agendamento);
            atualizarAgendaPorData(jCalendar1.getDate());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar agendamento: " + e.getMessage());
        }
    }

    private void abrirDetalhesComanda(AgendamentoDTO ag, int row, int col, List<AgendamentoDTO> agendamentos) {
        DetalhesComandaDialog dialog = new DetalhesComandaDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                true,
                this
        );
        dialog.preencherDados(ag, row, col, agendamentos);
        dialog.setVisible(true);
    }

    public JTable getTabelaAgenda() {
        return jTable1;
    }

    public List<String> getHorariosBase() {
        List<String> horarios = new ArrayList<>();
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            Object valor = jTable1.getValueAt(i, 0);
            if (valor != null) {
                horarios.add(valor.toString());
            }
        }
        return horarios;
    }

    public void finalizarComanda(ComandaDTO comanda) {
        try {
            agendamentoService.finalizarComanda(comanda.getIdComanda());
            atualizarAgendaPorData(jCalendar1.getDate());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao finalizar comanda: " + e.getMessage());
        }
    }

    public void excluirComanda(ComandaDTO comanda) {
        if (comanda == null) {
            return;
        }

        try {
            agendamentoService.excluirAgendamentosDaComanda(comanda.getIdComanda());
            atualizarAgendaPorData(jCalendar1.getDate());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir agendamentos: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        topBar = new javax.swing.JPanel();
        lblAgenda = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jCalendar1 = new com.toedter.calendar.JCalendar();
        btnExcluir = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setMinimumSize(new java.awt.Dimension(900, 600));
        setPreferredSize(new java.awt.Dimension(900, 600));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        topBar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblAgenda.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblAgenda.setForeground(new java.awt.Color(102, 102, 102));
        lblAgenda.setText("Agenda");
        topBar.add(lblAgenda, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        add(topBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jScrollPane1.setPreferredSize(new java.awt.Dimension(900, 600));

        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, 780, 360));

        jCalendar1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jCalendar1PropertyChange(evt);
            }
        });
        add(jCalendar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(23, 72, -1, -1));

        btnExcluir.setBackground(new java.awt.Color(255, 204, 102));
        btnExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/event.png"))); // NOI18N
        btnExcluir.setText("Agendar Serviço");
        btnExcluir.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgendarActionPerformed(evt);
            }
        });
        add(btnExcluir, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 70, 170, 30));

        jButton1.setText("Listar Serviços");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 110, 170, 30));

        jButton2.setText("Listar Profissionais");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 190, 170, 30));

        jButton3.setText("Listar Clientes");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 150, 170, 30));
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgendarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgendarActionPerformed
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        AgendamentoDialog dialog = new AgendamentoDialog(parentFrame, true, this);
        dialog.setVisible(true);
    }//GEN-LAST:event_btnAgendarActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        int row = jTable1.rowAtPoint(evt.getPoint());
        int col = jTable1.columnAtPoint(evt.getPoint());

        Object value = jTable1.getValueAt(row, col);

        if (value instanceof AgendamentoSlot) {
            try {
                AgendamentoSlot slot = (AgendamentoSlot) value;
                AgendamentoDTO ag = slot.getAgendamento();

                List<AgendamentoDTO> agendamentosDoBanco = new AgendamentoDAO().listarTodos();
                abrirDetalhesComanda(ag, row, col, agendamentosDoBanco);
            } catch (SQLException ex) {
                Logger.getLogger(AgendaPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jCalendar1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jCalendar1PropertyChange
        Date dataSelecionada = jCalendar1.getDate();
        atualizarAgendaPorData(dataSelecionada);
    }//GEN-LAST:event_jCalendar1PropertyChange

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            ListagemServicosDialog dialog = new ListagemServicosDialog(
                    (Frame) SwingUtilities.getWindowAncestor(this),
                    true,
                    null,
                    servicoService
            );
            dialog.setVisible(true);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar serviços do banco: " + ex.getMessage());
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        ListagemProfissionaisDialog dialog = new ListagemProfissionaisDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                true,
                this
        );
        dialog.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            ListagemClientesDialog dialog = new ListagemClientesDialog(
                    (Frame) SwingUtilities.getWindowAncestor(this),
                    true,
                    this,
                    clienteService
            );
            dialog.setVisible(true);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar clientes do banco: " + ex.getMessage());
        }
    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private com.toedter.calendar.JCalendar jCalendar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblAgenda;
    private javax.swing.JPanel topBar;
    // End of variables declaration//GEN-END:variables
}
