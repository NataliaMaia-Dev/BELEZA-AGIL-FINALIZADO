const Validators = {
    isEmpty(value) {
        return !value || String(value).trim() === '';
    },

    validateRequired(value, fieldName) {
        if (this.isEmpty(value)) {
            return `O campo "${fieldName}" é obrigatório.`;
        }
        return null;
    },

    validateCPF(cpf) {
        const cleaned = String(cpf).replace(/\D/g, '');

        if (cleaned.length !== 11) {
            return 'CPF deve conter 11 dígitos.';
        }

        if (/^(\d)\1{10}$/.test(cleaned)) {
            return 'CPF inválido.';
        }

        let sum = 0;
        for (let i = 0; i < 9; i++) {
            sum += parseInt(cleaned.charAt(i), 10) * (10 - i);
        }
        let remainder = (sum * 10) % 11;
        if (remainder === 10 || remainder === 11) remainder = 0;
        if (remainder !== parseInt(cleaned.charAt(9), 10)) {
            return 'CPF inválido.';
        }

        sum = 0;
        for (let i = 0; i < 10; i++) {
            sum += parseInt(cleaned.charAt(i), 10) * (11 - i);
        }
        remainder = (sum * 10) % 11;
        if (remainder === 10 || remainder === 11) remainder = 0;
        if (remainder !== parseInt(cleaned.charAt(10), 10)) {
            return 'CPF inválido.';
        }

        return null;
    },

    validateEmail(email) {
        if (this.isEmpty(email)) {
            return 'E-mail é obrigatório.';
        }
        const pattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!pattern.test(email)) {
            return 'E-mail inválido.';
        }
        return null;
    },

    validatePhone(phone) {
        const cleaned = String(phone).replace(/\D/g, '');
        if (cleaned.length < 10 || cleaned.length > 11) {
            return 'Telefone deve conter 10 ou 11 dígitos (com DDD).';
        }
        return null;
    },

    validateDateNotFuture(dateStr) {
        if (this.isEmpty(dateStr)) {
            return 'Data de nascimento é obrigatória.';
        }
        const date = new Date(dateStr + 'T00:00:00');
        const today = new Date();
        today.setHours(0, 0, 0, 0);
        if (date > today) {
            return 'Data de nascimento não pode ser no futuro.';
        }
        return null;
    },

    validateDateNotPast(dateStr) {
        if (this.isEmpty(dateStr)) {
            return 'Data é obrigatória.';
        }
        const date = new Date(dateStr + 'T00:00:00');
        const today = new Date();
        today.setHours(0, 0, 0, 0);
        if (date < today) {
            return 'Não é possível agendar em datas passadas.';
        }
        return null;
    },

    validatePositiveNumber(value, fieldName) {
        const num = parseFloat(String(value).replace(',', '.'));
        if (isNaN(num) || num <= 0) {
            return `${fieldName} deve ser um número positivo.`;
        }
        return null;
    },

    validatePositiveInteger(value, fieldName) {
        const num = parseInt(String(value), 10);
        if (isNaN(num) || num <= 0 || !Number.isInteger(num)) {
            return `${fieldName} deve ser um número inteiro positivo.`;
        }
        return null;
    },

    validateNome(nome) {
        if (this.isEmpty(nome)) {
            return 'Nome é obrigatório.';
        }
        if (nome.trim().length < 3) {
            return 'Nome deve ter pelo menos 3 caracteres.';
        }
        return null;
    },

    showFieldError(input, message) {
        const formGroup = input.closest('.form-group');
        if (!formGroup) return;

        const errorEl = formGroup.querySelector('.form-error');
        input.classList.add('error');

        if (errorEl) {
            errorEl.textContent = message;
            errorEl.classList.add('show');
        }
    },

    clearFieldError(input) {
        const formGroup = input.closest('.form-group');
        if (!formGroup) return;

        const errorEl = formGroup.querySelector('.form-error');
        input.classList.remove('error');

        if (errorEl) {
            errorEl.textContent = '';
            errorEl.classList.remove('show');
        }
    },

    clearAllErrors(form) {
        form.querySelectorAll('.form-control').forEach(input => {
            this.clearFieldError(input);
        });
    }
};
