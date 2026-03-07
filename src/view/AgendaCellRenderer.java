package view;

import dto.AgendamentoDTO;
import dto.AgendamentoSlot;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;

public class AgendaCellRenderer implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        if (column == 0) {
            JLabel lbl = new JLabel(value != null ? value.toString() : "", SwingConstants.CENTER);
            lbl.setOpaque(true);
            lbl.setBackground(Color.WHITE);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            return lbl;
        }

        if (value instanceof AgendamentoSlot slot) {
            AgendamentoDTO ag = slot.getAgendamento();
            int level = slot.getDisplayLevel();

            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setOpaque(true);
            card.setBorder(null);

            if (ag.isFinalizado()) {
                card.setBackground(new Color(200, 255, 200)); 
            } else {
                card.setBackground(new Color(255, 255, 200));
            }

            if (level == 0) {
                JLabel lblComanda = new JLabel("Comanda #" + ag.getComanda().getIdComanda());
                lblComanda.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblComanda.setAlignmentX(Component.LEFT_ALIGNMENT);
                card.add(lblComanda);
            }

            if (level == 1) {
                JLabel lblCliente = new JLabel(ag.getCliente().getNome());
                lblCliente.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                lblCliente.setAlignmentX(Component.LEFT_ALIGNMENT);
                card.add(lblCliente);
            }

            if (level == 2) {
                JLabel lblServico = new JLabel(ag.getServico().getNome());
                lblServico.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                lblServico.setAlignmentX(Component.LEFT_ALIGNMENT);
                card.add(lblServico);
            }

            return card;
        }

        JPanel vazio = new JPanel();
        vazio.setOpaque(true);
        vazio.setBackground(Color.WHITE);
        return vazio;
    }
}

