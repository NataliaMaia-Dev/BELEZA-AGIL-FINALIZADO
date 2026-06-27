package com.belezaagil.exception;

import com.belezaagil.dto.ErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("Dados inválidos.");
        return ResponseEntity.badRequest().body(new ErrorResponse(message));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex) {
        // BUG-004 fix: mensagem orientativa indica que vínculos devem ser removidos antes
        String msg = ex.getMessage() != null ? ex.getMessage().toLowerCase() : "";
        String resposta;
        if (msg.contains("agendamento") || msg.contains("profissional_id")) {
            resposta = "Não é possível excluir: este profissional possui agendamentos vinculados. Exclua os agendamentos primeiro.";
        } else if (msg.contains("cliente") || msg.contains("cliente_id")) {
            resposta = "Não é possível excluir: este cliente possui agendamentos vinculados. Exclua os agendamentos primeiro.";
        } else if (msg.contains("servico") || msg.contains("servico_id")) {
            resposta = "Não é possível excluir: este serviço possui agendamentos vinculados. Exclua os agendamentos primeiro.";
        } else {
            resposta = "Não é possível excluir: registro possui vínculos com outros dados.";
        }
        return ResponseEntity.badRequest().body(new ErrorResponse(resposta));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro interno do servidor."));
    }
}
