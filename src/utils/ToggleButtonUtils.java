package utils;

import java.awt.Color;
import java.awt.Font;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;

public class ToggleButtonUtils {
        public static void aplicarEstilo(JToggleButton botao) {
        if (botao.isSelected()) {
            botao.setFont(new Font("Arial", Font.BOLD, 16));
            botao.setForeground(Color.DARK_GRAY);
        } else {
            botao.setFont(new Font("Arial", Font.PLAIN, 16));
            botao.setForeground(Color.BLACK);
        }
    }

    public static void aplicarEstiloGrupo(ButtonGroup grupo) {
        for (AbstractButton b : java.util.Collections.list(grupo.getElements())) {
            aplicarEstilo((JToggleButton) b);
        }
    }
}
