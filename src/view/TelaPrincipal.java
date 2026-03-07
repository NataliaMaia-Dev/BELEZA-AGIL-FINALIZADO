package view;

import utils.ToggleButtonUtils;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.SwingUtilities;

public class TelaPrincipal extends javax.swing.JFrame {

    private CardLayout cardLayout;

    public TelaPrincipal() {
        initComponents();
        cardLayout = (CardLayout) mainPanel.getLayout();

        ClientePanel clientePanel = new ClientePanel();
        AgendaPanel agendaPanel = new AgendaPanel();
        ProfissionalPanel profissionalPanel = new ProfissionalPanel(agendaPanel);
        ServicoPanel servicoPainel = new ServicoPanel();

        clientePanel.setAgendaPanel(agendaPanel);

        mainPanel.add(agendaPanel, "AGENDA");
        mainPanel.add(servicoPainel, "SERVICO");
        mainPanel.add(clientePanel, "CLIENTE");
        mainPanel.add(profissionalPanel, "PROFISSIONAL");
        cardLayout.show(mainPanel, "AGENDA");

        ToggleButtonUtils.aplicarEstiloGrupo(grupo);
    }

    public void mostrarTela(String nomeTela) {
        cardLayout.show(mainPanel, nomeTela);

        switch (nomeTela) {
            case "AGENDA":
                btnAgenda.setSelected(true);
                btnAgenda.setFont(new Font("Arial", Font.BOLD, 16));
                btnAgenda.setForeground(Color.DARK_GRAY);
                break;
        }

        ToggleButtonUtils.aplicarEstiloGrupo(grupo);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grupo = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        menuPanel = new javax.swing.JPanel();
        btnAgenda = new javax.swing.JToggleButton();
        btnProfissioanal = new javax.swing.JToggleButton();
        btnServico = new javax.swing.JToggleButton();
        btnCliente = new javax.swing.JToggleButton();
        jLabel1 = new javax.swing.JLabel();
        mainPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new java.awt.Dimension(1000, 600));

        menuPanel.setBackground(new java.awt.Color(230, 230, 230));

        grupo.add(btnAgenda);
        btnAgenda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/calendar-day.png"))); // NOI18N
        btnAgenda.setSelected(true);
        btnAgenda.setText("Agenda");
        btnAgenda.setBorder(null);
        btnAgenda.setContentAreaFilled(false);
        btnAgenda.setIconTextGap(10);
        btnAgenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgendaActionPerformed(evt);
            }
        });

        grupo.add(btnProfissioanal);
        btnProfissioanal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/user-plumber.png"))); // NOI18N
        btnProfissioanal.setText("Cadastrar profissional");
        btnProfissioanal.setBorder(null);
        btnProfissioanal.setContentAreaFilled(false);
        btnProfissioanal.setIconTextGap(10);
        btnProfissioanal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProfissioanl(evt);
            }
        });

        grupo.add(btnServico);
        btnServico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tools.png"))); // NOI18N
        btnServico.setText("Criar serviço");
        btnServico.setBorder(null);
        btnServico.setContentAreaFilled(false);
        btnServico.setIconTextGap(10);
        btnServico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnServicoActionPerformed(evt);
            }
        });

        grupo.add(btnCliente);
        btnCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/review.png"))); // NOI18N
        btnCliente.setText("Cadastrar cliente");
        btnCliente.setBorder(null);
        btnCliente.setContentAreaFilled(false);
        btnCliente.setIconTextGap(10);
        btnCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClienteActionPerformed(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logo-beleza-agil 1.png"))); // NOI18N

        javax.swing.GroupLayout menuPanelLayout = new javax.swing.GroupLayout(menuPanel);
        menuPanel.setLayout(menuPanelLayout);
        menuPanelLayout.setHorizontalGroup(
            menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuPanelLayout.createSequentialGroup()
                .addGroup(menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(menuPanelLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnProfissioanal)
                            .addComponent(btnServico)
                            .addComponent(btnCliente)
                            .addComponent(btnAgenda)))
                    .addGroup(menuPanelLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        menuPanelLayout.setVerticalGroup(
            menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61)
                .addComponent(btnAgenda)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnServico)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCliente)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnProfissioanal)
                .addContainerGap(550, Short.MAX_VALUE))
        );

        mainPanel.setBackground(new java.awt.Color(204, 204, 204));
        mainPanel.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(menuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 830, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(menuPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClienteActionPerformed
        ToggleButtonUtils.aplicarEstiloGrupo(grupo);
        ((CardLayout) mainPanel.getLayout()).show(mainPanel, "CLIENTE");
    }//GEN-LAST:event_btnClienteActionPerformed

    private void btnServicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnServicoActionPerformed
        ToggleButtonUtils.aplicarEstiloGrupo(grupo);
        ((CardLayout) mainPanel.getLayout()).show(mainPanel, "SERVICO");
    }//GEN-LAST:event_btnServicoActionPerformed

    private void btnProfissioanl(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProfissioanl
        ToggleButtonUtils.aplicarEstiloGrupo(grupo);
        ((CardLayout) mainPanel.getLayout()).show(mainPanel, "PROFISSIONAL");
    }//GEN-LAST:event_btnProfissioanl

    private void btnAgendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgendaActionPerformed
        ToggleButtonUtils.aplicarEstiloGrupo(grupo);
        ((CardLayout) mainPanel.getLayout()).show(mainPanel, "AGENDA");
    }//GEN-LAST:event_btnAgendaActionPerformed

    /**
     * @param args the command line arguments
     */
    public void main(String args[]) {
        SwingUtilities.invokeLater(() -> new TelaPrincipal().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton btnAgenda;
    private javax.swing.JToggleButton btnCliente;
    private javax.swing.JToggleButton btnProfissioanal;
    private javax.swing.JToggleButton btnServico;
    private javax.swing.ButtonGroup grupo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel menuPanel;
    // End of variables declaration//GEN-END:variables

}
