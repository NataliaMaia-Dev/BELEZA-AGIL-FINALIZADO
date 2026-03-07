package view;

import dto.AgendamentoDTO;
import dto.ClienteDTO;
import dto.ComandaDTO;
import dto.ProfissionalDTO;
import dto.ServicoDTO;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import utils.AgendamentoUtils;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import service.AgendamentoService;
import service.ClienteService;
import service.ProfissionalService;
import service.ServicoService;
import validator.AgendamentoValidator;

public class AgendamentoDialog extends javax.swing.JDialog {

    private boolean cadastrado = false;
    private AgendamentoDTO agendamentoCadastrado;
    private AgendaPanel agendaPanel;
    private ComandaDTO comandaGerada = new ComandaDTO();
    private final AgendamentoService agendamentoService;
    private final ClienteService clienteService;
    private final ServicoService servicoService;
    private final ProfissionalService profissionalService;

    public AgendamentoDTO getAgendamentoCadastrado() {
        return cadastrado ? agendamentoCadastrado : null;
    }

    public AgendamentoDialog(java.awt.Frame parent, boolean modal, AgendaPanel agendaPanel) {
        this(parent, modal, agendaPanel, new AgendamentoService(), new ClienteService(), new ServicoService(), new ProfissionalService());
    }

    public AgendamentoDialog(java.awt.Frame parent,
                             boolean modal,
                             AgendaPanel agendaPanel,
                             AgendamentoService agendamentoService,
                             ClienteService clienteService,
                             ServicoService servicoService,
                             ProfissionalService profissionalService) {
        super(parent, "Agendar Serviço", true);
        this.agendaPanel = agendaPanel;
        this.agendamentoService = agendamentoService;
        this.clienteService = clienteService;
        this.servicoService = servicoService;
        this.profissionalService = profissionalService;
        initComponents();
        setLocationRelativeTo(parent);

        carregarClientesNoCombo();
        carregarServicosNoCombo();
        carregarProfissionaisNoCombo();

        AutoCompleteDecorator.decorate(cbClientes);
        AutoCompleteDecorator.decorate(cbServicos);
        AutoCompleteDecorator.decorate(cbProfissionais);

        txtComanda.setEditable(false);

        panelServicos.setLayout(new BoxLayout(panelServicos, BoxLayout.Y_AXIS));

        try {
            int proximaComanda = agendamentoService.getProximoNumeroComanda();
            txtComanda.setText(String.valueOf(proximaComanda));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar número da próxima comanda: " + e.getMessage());
        }

        btnAddServico.addActionListener(e -> {

            try {
                JPanel bloco;
                bloco = criarBlocoDeServico();
                panelServicos.add(bloco);
                panelServicos.revalidate();
                panelServicos.repaint();
            } catch (SQLException ex) {
                Logger.getLogger(AgendamentoDialog.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
    }

    private JPanel criarBlocoDeServico() throws SQLException {

        JPanel bloco = new JPanel();
        bloco.setLayout(new BoxLayout(bloco, BoxLayout.X_AXIS));
        bloco.setAlignmentX(Component.LEFT_ALIGNMENT);
        bloco.setOpaque(false);

        Dimension tamanhoCombo = new Dimension(150, 30);
        Color corFundo = Color.WHITE;
        Font fontePadrao = new Font("Segoe UI", Font.PLAIN, 13);

        JComboBox<String> cbHorarioExtra = new JComboBox<>();
        for (int i = 0; i < cbHorario.getItemCount(); i++) {
            cbHorarioExtra.addItem(cbHorario.getItemAt(i));
        }
        cbHorarioExtra.setPreferredSize(new Dimension(90, 30));
        cbHorarioExtra.setMaximumSize(new Dimension(90, 30));
        cbHorarioExtra.setBackground(corFundo);
        cbHorarioExtra.setFont(fontePadrao);
        cbHorarioExtra.setFocusable(false);

        List<ServicoDTO> servicosDoBanco = servicoService.listarTodos();

        JComboBox<ServicoDTO> cbServicoExtra = new JComboBox<>();

        for (ServicoDTO s : servicosDoBanco) {
            cbServicoExtra.addItem(s);
        }

        cbServicoExtra.setPreferredSize(tamanhoCombo);
        cbServicoExtra.setMaximumSize(tamanhoCombo);
        cbServicoExtra.setBackground(corFundo);
        cbServicoExtra.setFont(fontePadrao);
        cbServicoExtra.setFocusable(false);

        List<ProfissionalDTO> profissionaisDoBanco = profissionalService.listarTodos();

        JComboBox<ProfissionalDTO> cbProfissionalExtra = new JComboBox<>();

        for (ProfissionalDTO p : profissionaisDoBanco) {
            cbProfissionalExtra.addItem(p);
        }
        cbProfissionalExtra.setPreferredSize(new Dimension(130, 30));
        cbProfissionalExtra.setMaximumSize(new Dimension(130, 30));
        cbProfissionalExtra.setBackground(corFundo);
        cbProfissionalExtra.setFont(fontePadrao);
        cbProfissionalExtra.setFocusable(false);

        JButton btnRemover = new JButton("X");
        btnRemover.setPreferredSize(new Dimension(45, 30));
        btnRemover.setFocusPainted(false);
        btnRemover.setBackground(new Color(240, 240, 240));
        btnRemover.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        btnRemover.addActionListener(e -> {
            panelServicos.remove(bloco);
            panelServicos.revalidate();
            panelServicos.repaint();
        });

        bloco.add(cbHorarioExtra);
        bloco.add(Box.createHorizontalStrut(8));
        bloco.add(cbServicoExtra);
        bloco.add(Box.createHorizontalStrut(8));
        bloco.add(cbProfissionalExtra);
        bloco.add(Box.createHorizontalStrut(8));
        bloco.add(btnRemover);

        return bloco;
    }

    public void atualizarComboServicos() {
        cbServicos.removeAllItems();

        try {
            List<ServicoDTO> servicos = servicoService.listarTodos();

            cbServicos.addItem(null);
            for (ServicoDTO s : servicos) {
                cbServicos.addItem(s);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar serviços: " + e.getMessage());
        }

        cbServicos.revalidate();
        cbServicos.repaint();
    }

    public boolean isCadastrado() {
        return cadastrado;
    }

    private void carregarClientesNoCombo() {
        cbClientes.removeAllItems();
        try {
            List<ClienteDTO> clientes = clienteService.listarTodos();
            for (ClienteDTO c : clientes) {
                cbClientes.addItem(c);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar clientes: " + ex.getMessage());
        }
    }

    private void carregarServicosNoCombo() {
        cbServicos.removeAllItems();
        try {
            List<ServicoDTO> servicos = servicoService.listarTodos();
            cbServicos.addItem(null);
            for (ServicoDTO s : servicos) {
                cbServicos.addItem(s);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar serviços: " + ex.getMessage());
        }
    }

    private void carregarProfissionaisNoCombo() {
        cbProfissionais.removeAllItems();
        try {
            List<ProfissionalDTO> profissionais = profissionalService.listarTodos();
            for (ProfissionalDTO p : profissionais) {
                cbProfissionais.addItem(p);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar profissionais: " + ex.getMessage());
        }
    }

    private AgendamentoDTO criarAgendamento(
            ClienteDTO cliente,
            ServicoDTO servico,
            ProfissionalDTO profissional,
            LocalDate data,
            String horaStr) {

        LocalTime horaLocal = LocalTime.parse(horaStr, DateTimeFormatter.ofPattern("HH:mm"));
        LocalDateTime dataAgendamento = LocalDateTime.of(data, horaLocal);

        List<String> horariosOcupados = AgendamentoUtils.gerarHorariosOcupados(
                horaStr,
                servico.getTempoExecucao(),
                10
        );

        List<String> horariosBase = new ArrayList<>();
        for (int i = 0; i < agendaPanel.getTabelaAgenda().getRowCount(); i++) {
            Object valor = agendaPanel.getTabelaAgenda().getValueAt(i, 0);
            if (valor != null) {
                horariosBase.add(valor.toString());
            }
        }

        boolean conflito = AgendamentoUtils.temConflito(
                agendaPanel.getTabelaAgenda(),
                profissional.getNome(),
                horariosOcupados,
                horariosBase
        );

        if (conflito) {
            JOptionPane.showMessageDialog(null,
                    "⛔ O profissional " + profissional.getNome() + " já possui um agendamento nesses horários.",
                    "Conflito de Horário",
                    JOptionPane.WARNING_MESSAGE);
            return null;
        }

        AgendamentoDTO agendamento = new AgendamentoDTO();
        agendamento.setCliente(cliente);
        agendamento.setServico(servico);
        agendamento.setProfissional(profissional);
        agendamento.setDataAgendamento(dataAgendamento);
        agendamento.setComanda(comandaGerada);
        agendamento.setHorariosOcupados(horariosOcupados);

        return agendamento;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtComanda = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cbHorario = new javax.swing.JComboBox<>();
        btnCadastar = new javax.swing.JButton();
        cbServicos = new javax.swing.JComboBox<>();
        cbProfissionais = new javax.swing.JComboBox<>();
        cbClientes = new javax.swing.JComboBox<>();
        jDataSelecionada = new com.toedter.calendar.JDateChooser();
        panelServicos = new javax.swing.JPanel();
        btnAddServico = new javax.swing.JButton();
        btnCancelar1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setMaximumSize(new java.awt.Dimension(673, 600));
        jPanel1.setMinimumSize(new java.awt.Dimension(673, 600));
        jPanel1.setPreferredSize(new java.awt.Dimension(673, 600));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jLabel1.setText("Agendar serviço");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 19, -1, -1));

        txtComanda.setForeground(new java.awt.Color(153, 153, 153));
        jPanel1.add(txtComanda, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, 117, 34));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Número comanda");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, -1, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Nome do cliente");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, -1, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Horário");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 280, -1, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Data agendamento");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, -1, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Serviço");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 280, -1, 20));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Profissional");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 280, -1, 20));

        cbHorario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "08:00", "08:10", "08:20", "08:30", "08:40", "08:50", "09:00", "09:10", "09:20", "09:30", "09:40", "09:50", "10:00", "10:10", "10:20", "10:30", "10:40", "10:50", "11:00", "11:10", "11:20", "11:30", "11:40", "11:50", "12:00", "12:10", "12:20", "12:30", "12:40", "12:50", "13:00", "13:10", "13:20", "13:30", "13:40", "13:50", "14:00", "14:10", "14:20", "14:30", "14:40", "14:50", "15:00", "15:10", "15:20", "15:30", "15:40", "15:50", "16:00", "16:10", "16:20", "16:30", "16:40", "16:50", "17:00", "17:10", "17:20", "17:30", "17:40", "17:50", "18:00", "18:10", "18:20", "18:30", "18:40", "18:50", "19:00" }));
        cbHorario.setToolTipText("Selecione um horário");
        cbHorario.setBorder(null);
        cbHorario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbHorarioActionPerformed(evt);
            }
        });
        jPanel1.add(cbHorario, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 300, 144, 40));

        btnCadastar.setBackground(new java.awt.Color(153, 255, 153));
        btnCadastar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/check.png"))); // NOI18N
        btnCadastar.setText("Salvar");
        btnCadastar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadastarActionPerformed(evt);
            }
        });
        jPanel1.add(btnCadastar, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 550, 110, 30));

        cbServicos.setToolTipText("Selecione um serviço");
        cbServicos.setBorder(null);
        cbServicos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbServicosActionPerformed(evt);
            }
        });
        jPanel1.add(cbServicos, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 300, 144, 40));

        cbProfissionais.setEditable(true);
        cbProfissionais.setToolTipText("Selecione um profissional");
        cbProfissionais.setBorder(null);
        cbProfissionais.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbProfissionaisActionPerformed(evt);
            }
        });
        jPanel1.add(cbProfissionais, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 300, 144, 40));

        cbClientes.setEditable(true);
        cbClientes.setToolTipText("Selecione o nome do cliente");
        cbClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbClientesActionPerformed(evt);
            }
        });
        jPanel1.add(cbClientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, 260, 40));

        jDataSelecionada.setToolTipText("Selecione uma data para agendamento");
        jDataSelecionada.setMaxSelectableDate(new java.util.Date(1893470501000L));
        jDataSelecionada.setMinSelectableDate(new java.util.Date(-1577908722000L));
        jPanel1.add(jDataSelecionada, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 230, 190, 40));

        panelServicos.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelServicos.setLayout(new javax.swing.BoxLayout(panelServicos, javax.swing.BoxLayout.Y_AXIS));
        jPanel1.add(panelServicos, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 350, 510, 130));

        btnAddServico.setText("+ Adicionar serviço");
        jPanel1.add(btnAddServico, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 490, -1, -1));

        btnCancelar1.setText("Cancelar");
        btnCancelar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelar1ActionPerformed(evt);
            }
        });
        jPanel1.add(btnCancelar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 550, 95, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 6, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 673, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 6, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 596, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCadastarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadastarActionPerformed

        try {
            AgendamentoFormContext contexto = coletarDadosDoFormulario();

            validarConflitoPrincipal(contexto);

            List<AgendamentoDTO> agendamentosParaSalvar = new ArrayList<>();
            AgendamentoDTO agendamentoPrincipal = criarAgendamento(
                    contexto.cliente,
                    contexto.servicoPrincipal,
                    contexto.profissionalPrincipal,
                    contexto.dataAgendamento,
                    contexto.horaPrincipal
            );
            agendamentosParaSalvar.add(agendamentoPrincipal);

            adicionarAgendamentosExtras(agendamentosParaSalvar, contexto);

            agendamentoService.salvarAgendamentosComComanda(contexto.comanda, agendamentosParaSalvar);
            for (AgendamentoDTO ag : agendamentosParaSalvar) {
                agendaPanel.adicionarAgendamentoNaTabela(ag);
            }

            JOptionPane.showMessageDialog(this, "✅ Agendamento(s) salvo(s) com sucesso!");

            this.cadastrado = true;
            setVisible(false);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Não foi possível salvar o agendamento no banco de dados. Verifique sua conexão.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (NullPointerException ex) {
            JOptionPane.showMessageDialog(this,
                    "Parece que algum dado necessário está faltando. Verifique, preencha os campos e tente novamente.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Validação",
                    JOptionPane.WARNING_MESSAGE
            );
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar agendamentos: " + ex.getMessage());
        }
    }//GEN-LAST:event_btnCadastarActionPerformed

    private static class AgendamentoFormContext {
        private final ClienteDTO cliente;
        private final ServicoDTO servicoPrincipal;
        private final ProfissionalDTO profissionalPrincipal;
        private final LocalDate dataAgendamento;
        private final String horaPrincipal;
        private final ComandaDTO comanda;

        private AgendamentoFormContext(ClienteDTO cliente,
                                       ServicoDTO servicoPrincipal,
                                       ProfissionalDTO profissionalPrincipal,
                                       LocalDate dataAgendamento,
                                       String horaPrincipal,
                                       ComandaDTO comanda) {
            this.cliente = cliente;
            this.servicoPrincipal = servicoPrincipal;
            this.profissionalPrincipal = profissionalPrincipal;
            this.dataAgendamento = dataAgendamento;
            this.horaPrincipal = horaPrincipal;
            this.comanda = comanda;
        }
    }

    private AgendamentoFormContext coletarDadosDoFormulario() throws SQLException {
        ClienteDTO cliente = (ClienteDTO) cbClientes.getSelectedItem();
        Date dataSelecionada = jDataSelecionada.getDate();
        ServicoDTO servicoPrincipal = (ServicoDTO) cbServicos.getSelectedItem();
        ProfissionalDTO profissionalPrincipal = (ProfissionalDTO) cbProfissionais.getSelectedItem();

        AgendamentoValidator.validarDadosObrigatorios(cliente, dataSelecionada, servicoPrincipal, profissionalPrincipal);

        LocalDate dataLocal = dataSelecionada.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        ComandaDTO comanda = agendamentoService.criarComandaVazia();
        this.comandaGerada = comanda;

        String horaPrincipal = (String) cbHorario.getSelectedItem();

        return new AgendamentoFormContext(
                cliente,
                servicoPrincipal,
                profissionalPrincipal,
                dataLocal,
                horaPrincipal,
                comanda
        );
    }

    private void validarConflitoPrincipal(AgendamentoFormContext contexto) {
        List<String> horariosPrincipal = AgendamentoUtils.gerarHorariosOcupados(
                contexto.horaPrincipal, contexto.servicoPrincipal.getTempoExecucao(), 10);

        if (AgendamentoUtils.temConflito(
                agendaPanel.getTabelaAgenda(),
                contexto.profissionalPrincipal.getNome(),
                horariosPrincipal,
                agendaPanel.getHorariosBase()
        )) {
            throw new IllegalArgumentException("⛔ Conflito detectado para o profissional " + contexto.profissionalPrincipal.getNome());
        }
    }

    private void adicionarAgendamentosExtras(List<AgendamentoDTO> agendamentosParaSalvar,
                                             AgendamentoFormContext contexto) {
        for (Component comp : panelServicos.getComponents()) {
            if (comp instanceof JPanel bloco) {
                @SuppressWarnings("unchecked")
                JComboBox<String> cbHoraExtra = (JComboBox<String>) bloco.getComponent(0);
                @SuppressWarnings("unchecked")
                JComboBox<ServicoDTO> cbServicoExtra = (JComboBox<ServicoDTO>) bloco.getComponent(2);
                @SuppressWarnings("unchecked")
                JComboBox<ProfissionalDTO> cbProfExtra = (JComboBox<ProfissionalDTO>) bloco.getComponent(4);

                String horaExtra = (String) cbHoraExtra.getSelectedItem();
                ServicoDTO servicoExtra = (ServicoDTO) cbServicoExtra.getSelectedItem();
                ProfissionalDTO profissionalExtra = (ProfissionalDTO) cbProfExtra.getSelectedItem();

                List<String> horariosExtra = AgendamentoUtils.gerarHorariosOcupados(
                        horaExtra, servicoExtra.getTempoExecucao(), 10);

                boolean conflitoTabela = AgendamentoUtils.temConflito(
                        agendaPanel.getTabelaAgenda(), profissionalExtra.getNome(), horariosExtra, agendaPanel.getHorariosBase());

                boolean conflitoInterno = agendamentosParaSalvar.stream()
                        .anyMatch(a -> a.getProfissional().getIdProfissional() == profissionalExtra.getIdProfissional()
                                && a.getHorariosOcupados().stream().anyMatch(horariosExtra::contains));

                if (conflitoTabela || conflitoInterno) {
                    throw new IllegalArgumentException("⛔ Conflito detectado para o profissional " + profissionalExtra.getNome() + " no horário " + horaExtra);
                }

                AgendamentoDTO agendamentoExtra = criarAgendamento(
                        contexto.cliente,
                        servicoExtra,
                        profissionalExtra,
                        contexto.dataAgendamento,
                        horaExtra
                );
                agendamentosParaSalvar.add(agendamentoExtra);
            }
        }
    }

    private void cbClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbClientesActionPerformed

    }//GEN-LAST:event_cbClientesActionPerformed

    private void cbServicosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbServicosActionPerformed

    }//GEN-LAST:event_cbServicosActionPerformed

    private void cbProfissionaisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbProfissionaisActionPerformed

    }//GEN-LAST:event_cbProfissionaisActionPerformed

    private void btnCancelar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelar1ActionPerformed
        dispose();
    }//GEN-LAST:event_btnCancelar1ActionPerformed

    private void cbHorarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbHorarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbHorarioActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AgendamentoDialog.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AgendamentoDialog.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AgendamentoDialog.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AgendamentoDialog.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddServico;
    private javax.swing.JButton btnCadastar;
    private javax.swing.JButton btnCancelar1;
    private javax.swing.JComboBox<ClienteDTO> cbClientes;
    private javax.swing.JComboBox<String> cbHorario;
    private javax.swing.JComboBox<ProfissionalDTO> cbProfissionais;
    private javax.swing.JComboBox<ServicoDTO> cbServicos;
    private com.toedter.calendar.JDateChooser jDataSelecionada;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel panelServicos;
    private javax.swing.JTextField txtComanda;
    // End of variables declaration//GEN-END:variables

}
