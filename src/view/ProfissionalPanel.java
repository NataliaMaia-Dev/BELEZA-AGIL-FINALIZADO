package view;

import dto.ProfissionalDTO;
import service.ProfissionalService;

import java.time.LocalDate;
import java.time.ZoneId;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.sql.SQLException;

public class ProfissionalPanel extends javax.swing.JPanel {

    private AgendaPanel agendaPanel;
    private final ProfissionalService profissionalService;

    public ProfissionalPanel(AgendaPanel agendaPanel) {
        this(agendaPanel, new ProfissionalService());
    }

    public ProfissionalPanel(AgendaPanel agendaPanel, ProfissionalService profissionalService) {
        initComponents();
        this.agendaPanel = agendaPanel;
        this.profissionalService = profissionalService;
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
        jLabel6 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        cbFuncao = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jDataSelecionada = new com.toedter.calendar.JDateChooser();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jLabel1.setText("Cadastrar profissional");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 19, -1, -1));

        txtCpf.setToolTipText("Digite o CPF apenas com números (ex: 12345678900)");
        add(txtCpf, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 286, 117, 34));

        txtNome.setToolTipText("Informe o nome completo da profissional");
        txtNome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeActionPerformed(evt);
            }
        });
        add(txtNome, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 155, 336, 34));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Nome do profissional");
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 129, -1, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("CPF");
        add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 261, -1, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Data nascimento");
        add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 195, -1, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Email");
        add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 332, -1, -1));

        txtEmail.setToolTipText("Informe o email");
        txtEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailActionPerformed(evt);
            }
        });
        add(txtEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 357, 336, 34));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Função");
        add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(27, 406, -1, -1));

        cbFuncao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Manicure", "Depiladora", "Massagista" }));
        cbFuncao.setToolTipText("Selecione a função");
        cbFuncao.setBorder(null);
        add(cbFuncao, new org.netbeans.lib.awtextra.AbsoluteConstraints(27, 432, 144, 34));

        jButton1.setText("Cancelar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 490, 95, 30));

        jButton2.setBackground(new java.awt.Color(153, 255, 153));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/check.png"))); // NOI18N
        jButton2.setText("Salvar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 490, 110, 30));
        add(jDataSelecionada, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 220, 190, 30));
    }// </editor-fold>//GEN-END:initComponents

    private void txtNomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeActionPerformed

    private void txtEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        TelaPrincipal telaPrincipal = (TelaPrincipal) SwingUtilities.getWindowAncestor(this);
        telaPrincipal.mostrarTela("AGENDA");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String funcaoSelecionado = (String) cbFuncao.getSelectedItem();

        if (txtNome.getText().trim().isEmpty()
                || jDataSelecionada.getDate() == null
                || txtCpf.getText().trim().isEmpty()
                || txtEmail.getText().trim().isEmpty()
                || funcaoSelecionado == null
                || funcaoSelecionado.trim().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
            return;
        }

        try {
            int total = profissionalService.contarProfissionais(); 

            if (total >= 5) {
                JOptionPane.showMessageDialog(this,
                        "Limite máximo de 5 profissionais atingido!",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String nomeProfissional = txtNome.getText().trim();
            LocalDate dataLocal = jDataSelecionada.getDate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate();
            String cpf = txtCpf.getText().trim();
            String email = txtEmail.getText().trim();

            ProfissionalDTO profissional = new ProfissionalDTO();
            profissional.setNome(nomeProfissional);
            profissional.setDataNascimento(dataLocal);
            profissional.setCpf(cpf);
            profissional.setEmail(email);
            profissional.setFuncao(funcaoSelecionado);

            profissionalService.salvar(profissional);

            agendaPanel.atualizarColunasAgenda();

            JOptionPane.showMessageDialog(this,
                    "Profissional cadastrado com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);

            txtNome.setText("");
            jDataSelecionada.setDate(null);
            txtCpf.setText("");
            txtEmail.setText("");
            cbFuncao.setSelectedIndex(0);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao salvar profissional: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Formato inválido! Digite números válidos.");
        }
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cbFuncao;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private com.toedter.calendar.JDateChooser jDataSelecionada;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JTextField txtCpf;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtNome;
    // End of variables declaration//GEN-END:variables
}
