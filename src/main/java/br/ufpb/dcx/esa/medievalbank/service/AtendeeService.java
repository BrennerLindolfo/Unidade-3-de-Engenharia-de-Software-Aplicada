package br.ufpb.dcx.esa.medievalbank.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.ufpb.dcx.esa.medievalbank.MedievalBankException;

@Service
public class AtendeeService {

	public Atendee create(Atendee atendee) {
		return null;
	}

	public Atendee getOne(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	public Atendee update(Atendee atendee) {
		
		if(atendee.getName() == null) {
			throw new MedievalBankException("Name is mandatory");
		}
		
		return null;
	}

	public void delete(Atendee atendee) {
		// TODO Auto-generated method stub
		
	}

	public List<Atendee> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Atendee> findByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
