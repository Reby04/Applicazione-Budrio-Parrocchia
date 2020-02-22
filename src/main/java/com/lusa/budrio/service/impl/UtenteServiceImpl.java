package com.lusa.budrio.service.impl;

import com.lusa.budrio.error.ObjectAlreadyExistException;
import com.lusa.budrio.model.Sessione;
import com.lusa.budrio.model.Utente;
import com.lusa.budrio.repository.RuoloRepository;
import com.lusa.budrio.repository.SessioneRepository;
import com.lusa.budrio.repository.UtenteRepository;
import com.lusa.budrio.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@Transactional
public class UtenteServiceImpl implements UtenteService {

    @Autowired
    UtenteRepository utenteRepository;

    @Autowired
    RuoloRepository ruoloRepository;

    @Autowired
    SessioneRepository sessioneRepository;

    @Override
    public Utente createUtente(Utente utente) {
        if(emailExists(utente.getEmail())) {
            return null;
        }
        else {
            utente.setRuoli(Arrays.asList(ruoloRepository.findByNome("RUOLO_UTENTE")));
            return utenteRepository.save(utente);
        }
    }

    @Override
    public Utente getUtente(String token) {
        final Sessione tokenSessione = sessioneRepository.findByToken(token);

        if(tokenSessione != null) {
            return tokenSessione.getUtente();
        }
        return null;
    }

    @Override
    public void deleteUtente(Utente utente) {
        final Sessione sessione = sessioneRepository.findByUtente(utente);

        if(sessione != null) {
            sessioneRepository.delete(sessione);
        }

        utenteRepository.delete(utente);
    }

    private boolean emailExists(final String email) {
        return utenteRepository.findByEmail(email) != null;
    }
}
