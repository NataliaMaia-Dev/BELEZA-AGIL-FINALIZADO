const Utils = {
    formatCPF(cpf) {
        const cleaned = String(cpf).replace(/\D/g, '');
        if (cleaned.length <= 3) return cleaned;
        if (cleaned.length <= 6) return `${cleaned.slice(0, 3)}.${cleaned.slice(3)}`;
        if (cleaned.length <= 9) return `${cleaned.slice(0, 3)}.${cleaned.slice(3, 6)}.${cleaned.slice(6)}`;
        return `${cleaned.slice(0, 3)}.${cleaned.slice(3, 6)}.${cleaned.slice(6, 9)}-${cleaned.slice(9, 11)}`;
    },

    formatPhone(phone) {
        const cleaned = String(phone).replace(/\D/g, '');
        if (cleaned.length <= 2) return cleaned;
        if (cleaned.length <= 7) return `(${cleaned.slice(0, 2)}) ${cleaned.slice(2)}`;
        if (cleaned.length <= 11) {
            return `(${cleaned.slice(0, 2)}) ${cleaned.slice(2, 7)}-${cleaned.slice(7)}`;
        }
        return cleaned;
    },

    formatCurrency(value) {
        return new Intl.NumberFormat('pt-BR', {
            style: 'currency',
            currency: 'BRL'
        }).format(value);
    },

    formatDate(dateStr) {
        if (!dateStr) return '';
        const [year, month, day] = dateStr.split('-');
        return `${day}/${month}/${year}`;
    },

    cleanCPF(cpf) {
        return String(cpf).replace(/\D/g, '');
    },

    cleanPhone(phone) {
        return String(phone).replace(/\D/g, '');
    },

    gerarHorariosOcupados(horaInicial, tempoMinutos, intervalo) {
        const [hours, minutes] = horaInicial.split(':').map(Number);
        let totalMinutes = hours * 60 + minutes;
        const slots = Math.ceil(tempoMinutos / intervalo);
        const horarios = [];

        for (let i = 0; i < slots; i++) {
            const slotMinutes = totalMinutes + (i * intervalo);
            const h = Math.floor(slotMinutes / 60);
            const m = slotMinutes % 60;
            horarios.push(`${String(h).padStart(2, '0')}:${String(m).padStart(2, '0')}`);
        }

        return horarios;
    },

    showAlert(containerId, message, type = 'success') {
        const container = document.getElementById(containerId);
        if (!container) return;

        container.textContent = message;
        container.className = `alert alert-${type} show`;

        setTimeout(() => {
            container.classList.remove('show');
        }, 4000);
    },

    getTodayISO() {
        const today = new Date();
        const year = today.getFullYear();
        const month = String(today.getMonth() + 1).padStart(2, '0');
        const day = String(today.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`; 
    },

    debounce(fn, delay) {
        let timeout;
        return function (...args) {
            clearTimeout(timeout);
            timeout = setTimeout(() => fn.apply(this, args), delay);
        };
    }
};
