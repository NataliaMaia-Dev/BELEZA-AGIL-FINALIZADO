package view;

import dto.ServicoDTO;
import service.ServicoService;

import java.math.BigDecimal;
import java.time.Duration;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.sql.SQLException;

public class ServicoPanel extends javax.swing.JPanel {

    private ServicoDTO servicoDTO;
    private final ServicoService servicoService;

    public ServicoPanel() {
        this(new ServicoService());
    }

    public ServicoPanel(ServicoService servicoService) {
        this.servicoService = servicoService;
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel9 = new javax.swing.JLabel();
        txtValor = new javax.swing.JTextField();
        txtTempo = new javax.swing.JTextField();
        txtNome = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        btnCadastrar = new javax.swing.JButton();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(102, 102, 102));
        jLabel9.setText("Cadastrar serviço");
        add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 19, -1, -1));

        txtValor.setToolTipText("Informe o valor do serviço (ex: 35,00)");
        add(txtValor, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 286, 117, 34));

        txtTempo.setToolTipText("Informe o tempo de execução do serviço em minutos (ex: 30)");
        add(txtTempo, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 221, 117, 34));

        txtNome.setToolTipText("Informe o nome do serviço");
        txtNome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeActionPerformed(evt);
            }
        });
        add(txtNome, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 155, 336, 34));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Nome do serviço");
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 129, -1, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Valor");
        add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 261, -1, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Tempo execução");
        add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 195, -1, -1));

        jButton1.setText("Cancelar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 490, 95, 30));

        btnCadastrar.setBackground(new java.awt.Color(153, 255, 153));
        btnCadastrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/check.png"))); // NOI18N
        btnCadastrar.setText("Salvar");
        btnCadastrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadastrarActionPerformed(evt);
            }
        });
        add(btnCadastrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 490, 110, 30));
    }// </editor-fold>//GEN-END:initComponents

    private void txtNomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        TelaPrincipal telaPrincipal = (TelaPrincipal) SwingUtilities.getWindowAncestor(this);
        telaPrincipal.mostrarTela("AGENDA");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnCadastrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadastrarActionPerformed
        try {
            Duration duracao = Duration.ofMinutes(Long.parseLong(txtTempo.getText().trim()));
            BigDecimal valor = new BigDecimal(txtValor.getText().trim().replace(",", "."));

            ServicoDTO servico = new ServicoDTO();
            servico.setNome(txtNome.getText().trim());
            servico.setTempoExecucao(duracao);
            servico.setValor(valor);

            try {
                servicoService.salvar(servico);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao salvar serviço no banco: " + e.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Serviço cadastrado com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE
            );

            txtNome.setText("");
            txtTempo.setText("");
            txtValor.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Formato inválido! Digite números válidos.");
        }

    }//GEN-LAST:event_btnCadastrarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCadastrar;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField txtNome;
    private javax.swing.JTextField txtTempo;
    private javax.swing.JTextField txtValor;
    // End of variables declaration//GEN-END:variables
}
