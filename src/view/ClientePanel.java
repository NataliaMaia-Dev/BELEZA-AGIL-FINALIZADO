package view;

import dto.ClienteDTO;
import service.ClienteService;

import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.sql.SQLException;

public class ClientePanel extends javax.swing.JPanel {

    private ClienteDTO cliente;
    private AgendamentoDialog agendamento;
    private AgendaPanel agendaPanel;
    private final ClienteService clienteService;

    public ClientePanel() {
        this(new ClienteService());
    }

    public ClientePanel(ClienteService clienteService) {
        this.clienteService = clienteService;
        initComponents();
    }

    public void setAgendaPanel(AgendaPanel agendaPanel) {
        this.agendaPanel = agendaPanel;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtCpf = new javax.swing.JTextField();
        txtNome = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        btnCadastrar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txtTelefone = new javax.swing.JTextField();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jLabel1.setText("Cadastrar cliente");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 19, -1, -1));

        txtCpf.setToolTipText("Informe o CPF com números (ex: 12345678900)");
        add(txtCpf, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 286, 117, 34));

        txtNome.setToolTipText("Informe o nome completo do cliente");
        txtNome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeActionPerformed(evt);
            }
        });
        add(txtNome, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 155, 336, 34));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Nome do cliente");
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 129, -1, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("CPF");
        add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 261, -1, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Data nascimento");
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

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Telefone");
        add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 332, -1, -1));

        txtTelefone.setToolTipText("Informe o telefone com DDD (ex: (11) 98765-4321)");
        add(txtTelefone, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 357, 117, 34));

        jDateChooser1.setToolTipText("Informe a data de nascimento");
        jDateChooser1.setDateFormatString("Selecione a data de nascimento");
        jDateChooser1.setMinSelectableDate(new java.util.Date(-1577908722000L));
        add(jDateChooser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 220, 200, 40));
    }// </editor-fold>//GEN-END:initComponents

    private void btnCadastrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadastrarActionPerformed

        if (txtNome.getText().trim().isEmpty()
                || jDateChooser1.getDate() == null
                || txtCpf.getText().trim().isEmpty()
                || txtTelefone.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
            return;
        }

        try {

            String nome = txtNome.getText().trim();
            Date dataNascimento = jDateChooser1.getDate();
            String cpf = txtCpf.getText().trim();
            String telefone = txtTelefone.getText().trim();

            ClienteDTO cliente = new ClienteDTO();
            cliente.setNome(nome);
            cliente.setDataNascimento(dataNascimento);
            cliente.setCpf(cpf);
            cliente.setTelefone(telefone);

            try {
                clienteService.salvar(cliente);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Erro ao salvar no banco: " + e.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Cliente cadastrado com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE
            );

            txtNome.setText("");
            jDateChooser1.setDate(null);
            txtCpf.setText("");
            txtTelefone.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Formato inválido! Digite números válidos.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_btnCadastrarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        TelaPrincipal telaPrincipal = (TelaPrincipal) SwingUtilities.getWindowAncestor(this);
        telaPrincipal.mostrarTela("AGENDA");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtNomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCadastrar;
    private javax.swing.JButton jButton1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JTextField txtCpf;
    private javax.swing.JTextField txtNome;
    private javax.swing.JTextField txtTelefone;
    // End of variables declaration//GEN-END:variables
}
